package com.bcurry.videoserver.pojo;

import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TorrentRepresentation {
	private CompletableFuture task;
	private String magnetLink, name;
}
