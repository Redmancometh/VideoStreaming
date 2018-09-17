package com.bcurry.videoserver.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bcurry.videoserver.config.pojo.DBConfig;
import com.bcurry.videoserver.config.pojo.LibraryConfig;
import com.bcurry.videoserver.config.pojo.ServerConfig;

/**
 * 
 * @author Redmancometh
 *
 */
@Configuration
public class SpringApplicationContext {

	@Bean(name = "main-exec-pool")
	public ScheduledExecutorService pool() {
		return Executors.newScheduledThreadPool(32);
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		properties.setLocation(new FileSystemResource("config/application.properties"));
		properties.setIgnoreResourceNotFound(false);
		return properties;
	}

	@Bean
	public ConfigManager<ServerConfig> serverCfg() {
		ConfigManager<ServerConfig> cfg = new ConfigManager("server.json", ServerConfig.class);
		cfg.init();
		return cfg;

	}

	@Bean(name = "library-config")
	public ConfigManager<LibraryConfig> libraryCfg() {
		ConfigManager<LibraryConfig> cfg = new ConfigManager("libraries.json", LibraryConfig.class,
				() -> this.libraryCfg().getConfig().cacheLibraries());
		cfg.init();
		cfg.getConfig().cacheLibraries();
		return cfg;
	}

	@Bean(name = "db-config")
	public AdapterlessConfigManager<DBConfig> cfg() {
		AdapterlessConfigManager<DBConfig> cfg = new AdapterlessConfigManager("db.json", DBConfig.class);
		cfg.init();
		return cfg;
	}

}
