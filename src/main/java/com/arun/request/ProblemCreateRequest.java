package com.arun.request;

import lombok.Data;

import java.util.List;

@Data
public class ProblemCreateRequest {

	private List<String> code;
	private String problemDescription;
	private String problemTitle;
	private String language;
	private String questionId;
	private String className;
	private Integer adminId;
	private String adminUserName;
	private List<String> participants;
	private String answeredBy;

}
