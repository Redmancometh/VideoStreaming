package com.bcurry.videoserver.config.pojo;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SpringConfig {
	private List<String> profiles;
	private Map<String, Object> properties;
}
