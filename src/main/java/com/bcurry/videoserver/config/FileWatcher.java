package com.bcurry.videoserver.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 
 * @author Redmancometh
 *
 */
public class FileWatcher {
	private File monitored;
	private Consumer<File> onChangedCallback;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private String lastHash;
	private ScheduledFuture future;

	/**
	 * Create a file watcher to watch for changes on the given file, and execute the
	 * given callback. Until start is called the underlying scheduler will not run
	 * and this will not work, so make sure to call start!
	 * 
	 * @param onChanged
	 * @param monitored
	 */
	public FileWatcher(Consumer<File> onChanged, File monitored) {
		this.onChangedCallback = onChanged;
		this.monitored = monitored;
		this.lastHash = getHash();
	}

	/**
	 * Turn it on.
	 */
	public void start() {
		this.future = scheduler.scheduleAtFixedRate(() -> {
			if (hasChanged())
				onChangedCallback.accept(monitored);
		}, 1, 1, TimeUnit.SECONDS);

	}

	/**
	 * Turn it off. May throw an execption.
	 */
	public void stop() {
		future.cancel(true);
	}

	public String getHash() {
		try (FileInputStream fis = new FileInputStream(monitored)) {
			String sha1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex(fis);
			return sha1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasChanged() {
		String newHash = getHash();
		if (newHash.equals(lastHash)) {
			this.lastHash = newHash;
			return false;
		}
		this.lastHash = newHash;
		return true;
	}

}
