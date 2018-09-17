import React from 'react'
import RecentlyViewed from '../RecentlyViewedBrowser'
import ReactPlayer from 'react-player'
import LibraryAccessor from '../../accessors/LibraryAccessor'
import { Player } from 'video-react'
var accessor = new LibraryAccessor()

export default class MainPage extends React.Component {

	constructor(props) {
		super(props)
	}

	disableAllPlayers() {

	}

	render() {
		console.log("MAIN PAGE RENDER")
		return (
			<div id="main-page">
				<div id="recentlyViewed">
					<p className="content-title" id="recently-viewed-header">Recently Viewed</p>
					<div className="recently-viewed-div">
						{this.props.libs.map((recentlyViewed) => {
							return recentlyViewed.recentlyViewed.map((video) => {
								{ console.log(recentlyViewed) }
								return (
									<div className="individual-recently-viewed">
										<Player className="recently-viewed-video" ref={"player"+video.name} videoId="video-1">
											<source src={accessor.urlFromVideo(video)} />
										</Player>
										{video.name}
									</div>)
							})
						})}
					</div>
				</div>
			</div>
		)
	}



}

/**



**/