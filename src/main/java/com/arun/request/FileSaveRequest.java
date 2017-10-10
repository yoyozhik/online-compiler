package com.arun.request;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import java.io.File;
import java.util.List;

@Data
public class FileSaveRequest {

	@ApiObjectField(description = "Question Id")
	private String questionId;

	@ApiObjectField(description = "Code")
	private File code;

	@ApiObjectField(description = "Description")
	private String problemDescription;

	@ApiObjectField(description = "Title")
	private String problemTitle;

	@ApiObjectField(description = "Language")
	private String language;

	@ApiObjectField(description = "Admin Id")
	private Integer adminId;

	@ApiObjectField(description = "User Name")
	private String adminUserName;

	@ApiObjectField(description = "Participants")
	private List<String> participants;

	@ApiObjectField(description = "Answered By")
	private String answeredBy;

}
