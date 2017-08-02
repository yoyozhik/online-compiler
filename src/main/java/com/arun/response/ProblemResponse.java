package com.arun.response;

import com.arun.domain.MetaData;
import lombok.Data;

@Data
public class ProblemResponse {

	private String problemId;
	private MetaData metaData;

}
