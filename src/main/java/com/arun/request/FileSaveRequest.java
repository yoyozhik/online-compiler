package com.arun.request;

import java.io.File;
import java.util.List;

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
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public File getCode() {
		return code;
	}
	public void setCode(File code) {
		this.code = code;
	}
	public String getProblemDescription() {
		return problemDescription;
	}
	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getProblemTitle() {
		return problemTitle;
	}
	public void setProblemTitle(String problemTitle) {
		this.problemTitle = problemTitle;
	}
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public String getAdminUserName() {
		return adminUserName;
	}
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	public List<String> getParticipants() {
		return participants;
	}
	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}
	public String getAnsweredBy() {
		return answeredBy;
	}
	public void setAnsweredBy(String answeredBy) {
		this.answeredBy = answeredBy;
	}
	
	
}
