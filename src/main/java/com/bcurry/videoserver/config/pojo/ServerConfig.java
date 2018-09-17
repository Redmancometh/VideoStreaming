package com.bcurry.videoserver.config.pojo;

import lombok.Data;

@Data
public class ServerConfig {
	private String putioClientId, putioUser, putioPassword, putioClientSecret, putioOauth;
}
