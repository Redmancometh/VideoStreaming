package com.bcurry.videoserver.config.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.bcurry.videoserver.pojo.Video;

import lombok.Data;

@Data
/**
 * 
 * @author Redmancometh
 *
 */
public class Library {
	private String name, icon;
	private List<String> directories;
	protected Map<String, Video> videoFiles = new HashMap();
	protected final String[] acceptedFormat = { "mp4" };
	private Set<Video> recentlyViewed;

	public void cacheFiles() {
		System.out.println("NULL: " + (directories == null));
		directories.forEach((dir) -> {
			FileUtils.listFiles(new File(dir), acceptedFormat, true).forEach((file) -> videoFiles.put(file.getName(),
					new Video(file.getName(), file.getAbsolutePath(), this.getName())));
		});
	}

	public void addRecentVideo(Video video) {
		if (this.recentlyViewed.size() > 5)
			this.recentlyViewed.iterator().remove();
		this.recentlyViewed.add(video);
	}

	public String getFileDirectory(String fileName) {
		System.out.println("TEST GETFILEDIR");
		for (String directory : directories) {
			System.out.println("DIR: " + directory);
			System.out.println("WALKING PATHS");
			List<File> files = new ArrayList(FileUtils.listFiles(new File(directory), acceptedFormat, true));
			for (File file : files) {
				System.out.println(file);
				if (file.getName().equalsIgnoreCase(fileName))
					return file.getAbsolutePath();
			}
		}
		return fileName;
	}

	public List<Video> searchForVideos(String searchString) {
		System.out.println("Search String: " + searchString);
		List<Video> results = new ArrayList();
		videoFiles.forEach((name, video) -> {
			if (name.toLowerCase().contains(searchString)) {
				System.out.println("Found: " + name);
				results.add(video);

			}
		});
		return results;
	}

}
