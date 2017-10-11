package com.arun.request;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.List;

@Data
public class ProblemCreateRequest {

	@ApiObjectField(description = "Code")
	private List<String> code;

	@ApiObjectField(description = "Description")
	private String problemDescription;

	@ApiObjectField(description = "Problem Title")
	private String problemTitle;

	@ApiObjectField(description = "Language")
	private String language;

	@ApiObjectField(description = "Question Id")
	private String questionId;

	@ApiObjectField(description = "Class Name")
	private String className;

	@ApiObjectField(description = "Admin Id")
	private Integer adminId;

	@ApiObjectField(description = "Admin User Name")
	private String adminUserName;

	@ApiObjectField(description = "Participants")
	private List<String> participants;

	@ApiObjectField(description = "Answered By")
	private String answeredBy;

}
