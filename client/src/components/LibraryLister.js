import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import LibraryAccessor from '../accessors/LibraryAccessor'


var libraryAccessor = new LibraryAccessor()
var librarylist = []

export default class LibraryManager extends React.Component {
	state = { loadModal: false, libs: [] }

	constructor(props) {
		super(props)
		this.onOpen = this.onOpen.bind(this)
		console.log("LIB LISTER FUNC")
		console.log(this.props.onOpen)

	}

	onOpen() {
		console.log("ON OPEN")
		this.props.onOpen()
	}

	render() {
		return (
			<div className='lib-container'>
				<Modal id="lib-modal" show={this.state.show} onShow={this.onOpen} onHide={this.close}>
					<Modal.Header id="lib-header">
						<Modal.Title id="lib-title">Library Manager</Modal.Title>
					</Modal.Header>
					<Modal.Body id="lib-body">
						{this.props.libs.map((lib) => {
							return <p className="search-result-text">{lib.name}</p>
						})}
						<div id="yoicons" class="ui icon buttons">
						  <button class="ui button"><i class="plus square icon"></i></button>
						</div>
					</Modal.Body>
				</Modal>
			</div>
		)
	}

	close = () => {
		this.setState({ ...this.state, show: false });
	}

	open = () => {
		this.setState({ ...this.state, show: true });
	}

}