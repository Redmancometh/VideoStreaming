import React, { Component } from 'react';
import MainPage from './views/MainPage'
import LibraryContent from './views/LibraryContent'
import SettingsPage from './views/SettingsPage'
export default class ContentViewer extends React.Component {


	constructor(props) {
		super(props)
	}

	getContent() {
		switch (this.props.page) {
			case 'library-manager':
				return <LibraryContent/>
			case 'settings-page':
				return <SettingsPage/>
			default:
				return <MainPage libs={this.props.libs}/>;
		}
	}

	render() {
		return (this.getContent())
	}
}