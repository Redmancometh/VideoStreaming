package com.bcurry.videoserver.config.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import com.bcurry.videoserver.pojo.Video;
import lombok.Data;

/**
 * 
 * @author Redmancometh
 *
 */
@Data
public class LibraryConfig {

	private Map<String, Library> libraries;
	private String putioKey;

	@PostConstruct
	public void cacheLibraries() {
		System.out.println("CACHING LIBS");
		this.libraries.values().forEach((lib) -> lib.cacheFiles());
	}

	/**
	 * Change this behavior to utilize the caching system maybe.
	 * 
	 * @param name
	 * @return
	 */
	public List<Video> searchLibraries(String name) {
		System.out.println("Searching for: " + name);
		List<Video> results = new ArrayList();
		libraries.values().forEach((lib) -> {
			System.out.println("Searching lib: " + lib.getName());
			results.addAll(lib.searchForVideos(name));
		});
		return results;
	}

	public List<Video> getAllRecentlyViewed() {
		List<Video> videos = new ArrayList();
		libraries.values().forEach((lib) -> videos.addAll(lib.getRecentlyViewed()));
		return videos;
	}

	public Optional<String> searchLibrariesStrict(String name) {
		String dir = null;
		for (Library lib : libraries.values()) {
			String dirName = lib.getFileDirectory(name);
			if (dirName != null)
				dir = dirName;
		}
		return Optional.ofNullable(dir);
	}

}
