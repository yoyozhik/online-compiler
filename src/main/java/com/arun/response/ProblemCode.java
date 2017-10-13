package com.arun.response;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.List;

@Data
public class ProblemCode {

	@ApiObjectField(description = "Problem Id")
	private String problemId;

	@ApiObjectField(description = "Problem Description")
	private String problemDesc;

	@ApiObjectField(description = "Problem Title")
	private String problemTitle;

	@ApiObjectField(description = "Code Lines")
	private List<String> codeLines;

	@ApiObjectField(description = "Answered By")
	private String answeredBy;

	@ApiObjectField(description = "Class Name")
	private String className;

	@ApiObjectField(description = "Feedback")
	private String feedback;

	@ApiObjectField(description = "Rating")
	private String rating;

}
