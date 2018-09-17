package com.bcurry.videoserver.util;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {
	/**
	 * Takes the given file name and replaces it with this given extension and
	 * returns the file name with the new extension
	 * 
	 * @param fileName
	 * @param extensions
	 * @return
	 */
	public static String changeExtension(String fileName, String extension) {
		System.out.println("CHANGE EXT: " + FilenameUtils.removeExtension(fileName) + extension);
		return FilenameUtils.removeExtension(fileName) + extension;
	}
}
