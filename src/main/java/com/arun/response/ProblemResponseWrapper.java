package com.arun.response;

import lombok.Data;

import java.util.List;

@Data
public class ProblemResponseWrapper {
        
	@ApiObjectField(description = "Problems")
	private List<ProblemResponse> problems;
	
}
