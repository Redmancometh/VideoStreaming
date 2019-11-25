package com.bcurry.videoserver.config.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bcurry.videoserver.config.AdapterlessConfigManager;
import com.bcurry.videoserver.config.pojo.TorrentConfig;
import com.bcurry.videoserver.pojo.TorrentRepresentation;

import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.Config;

@Configuration
public class TorrentContext {
	@Bean("active-torrents")
	public Map<String, TorrentRepresentation> activeTorrents() {
		return new ConcurrentHashMap();
	}

	@Bean(name = "torrent-cfg")
	public AdapterlessConfigManager<TorrentConfig> cfgPojo() {
		AdapterlessConfigManager<TorrentConfig> cfgPojo = new AdapterlessConfigManager("torrents.json",
				TorrentConfig.class);
		cfgPojo.init();
		return cfgPojo;
	}

	@Bean
	public Config torrentCfg() {
		Config config = new Config() {
			@Override
			public int getNumOfHashingThreads() {
				return Runtime.getRuntime().availableProcessors() * 2;
			}
		};
		return config;
	}

	public DHTModule dhtModule() {
		DHTModule dhtModule = new DHTModule(new DHTConfig() {
			@Override
			public boolean shouldUseRouterBootstrap() {
				return true;
			}
		});
		return dhtModule;
	}
}
