import React from 'react';
import LibraryAccessor from '../accessors/LibraryAccessor'
//Import my components
import PropTypes from 'prop-types';
import SearchResultBrowser from './SearchResultBrowser'
//Import my CSS


var libAccessor = new LibraryAccessor()
export default class Topbar extends React.Component {

	state = { className: "", results: [] }

	constructor(props) {
		super(props)
		this.state = {
			...this.state,
			libs: props.className,
		}
		this.submitSearch = this.submitSearch.bind(this)
		this.openModal = this.openModal.bind(this)
		this.closeModal = this.closeModal.bind(this)
	}

	submitSearch() {
		var currentInput = this.validateInput(this.refs.searchInput.value)
		libAccessor.searchLibraries(currentInput).then(result => {
			this.refs.resultBrowser.setResults(result)
			this.refs.resultBrowser.open()
			this.props.openModal()
		})
	}

	enterPressed(event) {
		var code = event.keyCode || event.which;
		if (code === 13) {
			this.submitSearch()
		}
	}

	validateInput(toValidate) {
		return toValidate
	}

	openModal() {
		this.props.openModal()
	}

	closeModal() {
		this.props.closeModal()
	}

	render() {
		return (
			<div class="ui inverted menu">
				<div onClick={() => this.props.switchContent('main-page')}><a class="active item"><i class="home icon" />Home</a></div>
				<input onKeyPress={this.enterPressed.bind(this)} onSubmit={this.submitSearch} ref="searchInput" id="search-bar" placeholder="Search libraries for video..."></input>
				<div class="ui icon buttons">
					<button onClick={this.submitSearch} id="search-button" class="ui button"><i class="search icon"></i></button>
				</div>
				<div id="topbar-right-button-group">
					<div onClick={() => this.props.switchContent('settings-page')}><a id="topbar-settings-button" class="active item"><i class="server icon" />Settings</a></div>
				</div>
				<SearchResultBrowser openModal={this.openModal} closeModal={this.closeModal} results={this.state.results} ref="resultBrowser" />
			</div>
		)
	}
}


