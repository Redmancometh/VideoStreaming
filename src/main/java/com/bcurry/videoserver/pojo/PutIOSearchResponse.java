package com.bcurry.videoserver.pojo;

import java.util.List;

import lombok.Data;

@Data
public class PutIOSearchResponse {
	private List<PutIOVideo> files;
}
