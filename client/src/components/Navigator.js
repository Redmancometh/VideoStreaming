import React from 'react';
import Button from 'react-bootstrap/lib/Button';
import Modal from 'react-overlays/lib/Modal';
import App from '../App'
let rand = () => (Math.floor(Math.random() * 20) - 10);

const modalStyle = {
	position: 'fixed',
	zIndex: 1040,
	top: 0, bottom: 0, left: 0, right: 0
};

const backdropStyle = {
	...modalStyle,
	zIndex: 'auto',
	backgroundColor: '#000',
	opacity: 0.5
};

const dialogStyle = function() {
	// we use some psuedo random coords so nested modals
	// don't sit right on top of each other.
	let top = 50;
	let left = 50;

	return {
		position: 'absolute',
		width: "80%",
		height: "50%",
		top: top + '%', left: left + '%',
		transform: `translate(-${top}%, -${left}%)`,
		border: '1px solid #e5e5e5',
		backgroundColor: 'white',
		boxShadow: '0 5px 15px rgba(0,0,0,.5)',
		padding: 20
	};
};


class Navigator extends React.Component {

	state = {
		showModal: false
	}
	render() {
		return (
			<div className='modal-example'>
				<Modal aria-labelledby='modal-label' style={modalStyle} backdropStyle={backdropStyle} show={this.state.showModal} onHide={this.close}>
					<div style={dialogStyle()} >
						<div className="folder-list">
							<h1>TEST</h1>
						</div>
						<div className="file-list">
							<h1>TEST2</h1>
						</div>
						<Navigator />
					</div>
				</Modal>
			</div>
		);
	}

	componentDidMount() {

	}

	close = () => {
		this.setState({ showModal: false });
	}

	open = () => {
		this.setState({ showModal: true });
	}
}

export default Navigator;
