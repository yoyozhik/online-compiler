package com.arun.response;

import com.arun.domain.MetaData;

public class ProblemResponse {

	private String problemId;
	private MetaData metaData;
	
	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}
	
	
}
