package com.arun.service;

import java.util.List;

import com.arun.domain.Problem;
import com.arun.request.FileSaveRequest;
import com.arun.request.UpdateRequest;
import com.arun.response.ProblemResponse;

public interface MongoService {

	
	public List<String> fetchCode(String id, String criteria);
		
	public void saveCodeFile(FileSaveRequest fileSaveRequest);
	
	public Problem fetchFilesById(String id);
	
	public List<ProblemResponse> fetchProblemsByLanguage(String language, Integer userId, boolean isAdmin, String userName);
	
	public List<Problem> fetchAnswersforQuestion(String id);
	
	public void updateAnswer(UpdateRequest updateRequest);
}
