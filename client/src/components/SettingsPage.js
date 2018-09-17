import React from 'react'
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import LibraryAccessor from '../accessors/LibraryAccessor'
import ReactPlayer from 'react-player'
var accessor = new LibraryAccessor()
export default class ResultBrowser extends React.Component {

	state = {
		showModal: false,
	}

	results = []

	//the props are the results from a search
	constructor(props) {
		super(props)
		this.state = {
			onOpen: props.onOpen
		}
		this.backToResults = this.backToResults.bind(this)
	}

	setResults(results) {
		this.results = results
	}



	getToolTip(video) {
	}

	backToResults() {
		this.setState({
			...this.state,
			currentVideo: undefined
		})
	}


	setVideo(video) {
		var url = accessor.urlFromVideo(video)
		console.log("CURRENT URL: " + url)
		this.setState({
			...this.state,
			currentVideo: video,
			videoUrl: url
		})
	}

	render() {
		console.log("RENDER")
		var hasVideo = false
		if (this.state.currentVideo != undefined) hasVideo = true
		var header = "Search Results"
		if (hasVideo) header = this.state.currentVideo.name
		if (hasVideo) console.log("vid: \n" + this.state.currentVideo)
		return (
			<div>
				<Modal id="search-results-box" show={this.state.showModal} onHide={this.close}>
					<Modal.Header id="results-title">{header}</Modal.Header>
					{hasVideo ? (<ReactPlayer id="modal-video" controls="true" volume=".5" width="100%" height="100%" url={this.state.videoUrl} playing />) : (
						<Modal.Body id="results-content" className="results-body">
							{this.results.map((item) => {
								return (
									<p className="search-result-text" onClick={() => this.setVideo(item)}>{item.name}</p>
								)
							})}
						</Modal.Body>
					)}
					<Modal.Footer id="results-footer">
						<Button bsStyle="primary" id="close-results-button" className="modal-footer-button" onClick={this.close}>Close</Button>
						{hasVideo && <Button id="backbutton" className="modal-footer-button" onClick={this.backToResults} bsStyle="primary">Back To Results</Button>}
					</Modal.Footer>
				</Modal>
			</div >
		)
	}

	close = () => {
		this.setState({ showModal: false, currentVideo: undefined })
	}

	open = () => {
		this.setState({ showModal: true })
	}
}