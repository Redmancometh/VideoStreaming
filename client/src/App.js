import React, { Component } from 'react';
import './App.css';
import './components/css/views.css'
//My Accessors
import LibraryAccessor from './accessors/LibraryAccessor'
import Topbar from './components/Topbar'
import Sidebar from './components/Sidebar'
import ContentViewer from './components/ContentViewer'
import "../node_modules/video-react/dist/video-react.css"; // import css
var libraryAccessor = new LibraryAccessor()

class App extends Component {

	state = {
		isLoading: true,
		loadContent: true,
		contentPage: 'main-page'
	}

	constructor() {
		super()
		this.disableContent = this.disableContent.bind(this)
		this.enableContent = this.enableContent.bind(this)
		this.switchContent = this.switchContent.bind(this)
	}

	switchContent(page) {
		this.setState({
			...this.state,
			contentPage: page
		})
	}

	enableContent() {
		this.setState({
			...this.state,
			loadContent: true
		})
	}

	disableContent() {
		console.log("DISABLE")
		this.setState({
			...this.state,
			loadContent: false
		})
	}

	render() {
		const { isLoading } = this.state;
		if (isLoading) return <pre>Loading...</pre>
		return (
			<div className="App">
				<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.3.3/semantic.min.css"></link>
				<Topbar switchContent={this.switchContent} closeModal={this.enableContent} openModal={this.disableContent} closeModal={this.enableContent} />
				<div id="content">
					<div class="ui bottom attached segment pushable">
						<div id="outer-content">
							<div id="inner-content">
								{this.state.loadContent && <ContentViewer switchContent={this.switchContent} libs={this.state.libraries} page={this.state.contentPage}></ContentViewer>}
							</div>
							<Sidebar switchContent={this.switchContent} closeModal={this.enableContent} openModal={this.disableContent} libs={this.state.libraries} />
						</div>
					</div>
				</div>
			</div>
		);
	}

	componentDidMount() {
		libraryAccessor.listLibraries().then(libs => {
			this.setState({ isLoading: false, show: false, libraries: libs })
		})
	}

}

export default App;
