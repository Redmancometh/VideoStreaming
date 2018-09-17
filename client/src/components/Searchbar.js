import React from 'react';
import LibraryAccessor from '../accessors/LibraryAccessor'
//Import my components
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import SearchIcon from '@material-ui/icons/Search';
import PropTypes from 'prop-types';
import Input from '@material-ui/core/Input';
import './css/topbar.css'
//Import my CSS



export default class Topbar extends React.Component {

	state = { className: "" }

	constructor(props) {
		super(props)
		console.log(props)
		this.state = {
			...this.state,
			libs: props.className
		}
	}

	render() {
		console.log(PropTypes.object.isRequired)
		return (
			<div>
				<input id="search-bar"></input>
			</div >
		)
	}
}
