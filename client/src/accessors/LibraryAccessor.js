import React from 'react';

var serverIP = "http://192.168.0.100:8080"
export default class LibraryAccessor {
	listLibraries() {
		return fetch(serverIP + "/libraryInfo").then(response => response.json())
	}

	searchLibraries(name) {
		return fetch(serverIP + "/searchLibs?name=" + name).then(response => response.json())
	}

	getRecentVideos() {
		return fetch(serverIP + "/recentlyViewed").then(response => response.json())
	}

	getVideo(video) {
		window.location.assign(this.urlFromVideo(video))
	}

	urlFromVideo(video) {
		return serverIP + "/getVideo?fileName=" + video.name + "&library=" + video.libraryName
	}

}
