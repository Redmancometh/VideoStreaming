package com.bcurry.videoserver.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.bcurry.videoserver.config.ConfigManager;
import com.bcurry.videoserver.config.pojo.Library;
import com.bcurry.videoserver.config.pojo.LibraryConfig;
import com.bcurry.videoserver.pojo.Video;

/**
 * 
 * @author Redmancometh
 *
 */
@RestController
@CrossOrigin
public class VideoDirectoryController {

	@Autowired
	private ConfigManager<LibraryConfig> librariesConf;

	/**
	 * 
	 * @param libraryName
	 * @param fileName
	 * @return
	 */
	@GetMapping(value = "/getVideo", produces = "video/mp4")
	public StreamingResponseBody getVideo(@RequestParam(value = "library") String libraryName,
			@RequestParam(value = "fileName") String fileName) {
		Library lib = librariesConf.getConfig().getLibraries().get(libraryName);
		Video video = lib.getVideoFiles().get(fileName);
		if (video != null) {
			lib.addRecentVideo(video);
			librariesConf.writeConfig();
		}
		librariesConf.getConfig().getLibraries().keySet().forEach((libr) -> System.out.println("LIBR: :" + libr));
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream out) throws IOException {
				try (FileInputStream in = new FileInputStream(video.getFileLocation())) {
					IOUtils.copy(in, out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("searchLibsStrict")
	public String searchLibsStrict(@RequestParam(value = "name") String name) {
		Optional<String> val = librariesConf.getConfig().searchLibrariesStrict(name);
		if (val.isPresent())
			return val.get();
		else
			return "No results found.";
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("recentlyViewed")
	public List<Video> recentlyViewed() {
		return librariesConf.getConfig().getAllRecentlyViewed();

	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("recentlyViewedLib")
	public Set<Video> recentlyViewedInLibrary(@RequestParam(value = "library") String libraryName) {
		return librariesConf.getConfig().getLibraries().get(libraryName.toLowerCase()).getRecentlyViewed();
	}

	@GetMapping("searchLibs")
	public List<Video> searchLibs(@RequestParam(value = "name") String name,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(required = false, value = "stop") Integer stop) {
		List<Video> videos = librariesConf.getConfig().searchLibraries(name);
		if (start != null && stop != null)
			return videos.subList(start, stop);
		return videos;
	}

	@GetMapping("libraryNames")
	public List<String> listLibraries() {
		librariesConf.getConfig().getLibraries().values().forEach((lib) -> {
			System.out.println("VID CACHE SIZE: " + lib.getVideoFiles());
		});
		LibraryConfig cfg = librariesConf.getConfig();
		List<String> libs = cfg.getLibraries().values().stream().map(lib -> lib.getName()).collect(Collectors.toList());
		System.out.println("LIBS: \n" + libs);
		libs.add("THING\n");
		return libs;
	}

	@GetMapping("libraryInfo")
	public List<Library> libraries() {
		LibraryConfig cfg = librariesConf.getConfig();
		return new ArrayList(cfg.getLibraries().values());
	}

	@GetMapping("addLibrary")
	public void addLibrary(@RequestParam(value = "fileName") String fileName) {

	}
}
