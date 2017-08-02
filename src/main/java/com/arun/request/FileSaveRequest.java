package com.arun.request;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class FileSaveRequest {

	private String questionId;
	private File code;
	private String problemDescription;
	private String problemTitle;
	private String language;
	private Integer adminId;
	private String adminUserName;
	private List<String> participants;
	private String answeredBy;

}
