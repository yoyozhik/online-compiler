package com.bootmongo.service;

import java.util.List;

import com.bootmongo.domain.Problem;
import com.bootmongo.request.FileSaveRequest;
import com.bootmongo.request.UpdateRequest;
import com.bootmongo.response.ProblemResponse;

public interface MongoService {

	
	public List<String> fetchCode(String id, String criteria);
		
	public void saveCodeFile(FileSaveRequest fileSaveRequest);
	
	public Problem fetchFilesById(String id);
	
	public List<ProblemResponse> fetchProblemsByLanguage(String language, Integer userId, boolean isAdmin, String userName);
	
	public List<Problem> fetchAnswersforQuestion(String id);
	
	public void updateAnswer(UpdateRequest updateRequest);
}
