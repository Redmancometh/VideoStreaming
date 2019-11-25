package com.bcurry.videoserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import com.bcurry.videoserver.config.ConfigManager;
import com.bcurry.videoserver.config.pojo.SpringConfig;
import com.bcurry.videoserver.controller.MasterController;

import lombok.Getter;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class })
@ComponentScan(basePackages = { "com.bcurry.videoserver.*" })
@EntityScan(basePackages = "com.bcurry.videoserver.model")
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
public class VideoServer {
	@Autowired
	@Getter
	private MasterController master;
	private static ConfigManager<SpringConfig> cfgMon = new ConfigManager("spring.json", SpringConfig.class);
	public static ScheduledExecutorService main = Executors.newSingleThreadScheduledExecutor();

	public static void main(String[] args) {
		main.execute(() -> {
			System.out.println("MAIN");
			// changed some code
			cfgMon.init();
			SpringConfig cfg = cfgMon.getConfig();
			SpringApplication application = new SpringApplication(VideoServer.class);
			application.setDefaultProperties(cfg.getProperties());
			ConfigurableEnvironment environment = new StandardEnvironment();
			environment.setActiveProfiles(cfg.getProfiles().toArray(new String[cfg.getProfiles().size()]));
			application.setEnvironment(environment);
			application.run();
		});
	}
}
