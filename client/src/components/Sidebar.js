import React from 'react';
import LibraryLister from './LibraryLister'

class Sidebar extends React.Component {


	constructor(props) {
		super(props)
		this.openLib = this.openLib.bind(this)
		this.switchContent = this.switchContent.bind(this)
	}

	closeLib() {

	}

	openLib() {		
		//this.refs.liblister.open()
	}

	switchContent(page) {
		this.props.switchContent(page)
	}

	libraryView() {

	}

	render() {
		return (
			<div className="ui visible inverted left vertical sidebar menu">
				<a onClick={()=>this.switchContent('library-manager')} className="item"><i className="list icon"></i>Manage Libraries</a>
				{
					this.props.libs.map((lib) => {
						var iconClass = lib.icon + " icon"
						return <a className="item"><i className={iconClass}></i>{lib.name}</a>
					})
				}
				<LibraryLister onOpen={this.props.onOpen} libs={this.props.libs} ref="liblister"></LibraryLister>
			</div>
		);
	}

}

export default Sidebar;
