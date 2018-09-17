package com.bcurry.videoserver.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrimaryConfiguration {

	@Bean
	public ScheduledExecutorService executorPool() {
		return Executors.newSingleThreadScheduledExecutor();
	}

}
