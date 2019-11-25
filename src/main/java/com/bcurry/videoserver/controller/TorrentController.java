package com.bcurry.videoserver.controller;

import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcurry.videoserver.config.AdapterlessConfigManager;
import com.bcurry.videoserver.config.pojo.TorrentConfig;
import com.bcurry.videoserver.pojo.TorrentRepresentation;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;

@RestController
@CrossOrigin(origins = "localhost")
public class TorrentController {
	@Autowired
	@Qualifier(value = "active-torrents")
	private Map<String, TorrentRepresentation> activeTorrents;
	@Autowired
	private Config config;
	@Autowired
	private DHTModule dhtModule;
	@Autowired
	@Qualifier
	private AdapterlessConfigManager<TorrentConfig> cfg;

	@GetMapping("/api/torrents/addMagnetTorrent")
	public void addTorrent(String name, String magnetLink) {
		@SuppressWarnings("deprecation")
		Storage storage = new FileSystemStorage(new File(cfg.getConfig().getDonwloadDirectory()));
		BtClient client = Bt.client().config(config).storage(storage).magnet(magnetLink).autoLoadModules()
				.module(dhtModule).stopWhenDownloaded().build();
		CompletableFuture task = client.startAsync();
		activeTorrents.put(name, new TorrentRepresentation(task, magnetLink, name));
		task.thenAccept((whateverGotReturned) -> {
			System.out.println(whateverGotReturned);
			activeTorrents.remove(name);
		});
	}

	@GetMapping("/api/torrents/addTorrentFile")
	public void addTorrentUrl(String url) {

	}

}
