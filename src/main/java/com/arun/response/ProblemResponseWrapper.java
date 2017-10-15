package com.arun.response;

import lombok.Data;

import java.util.List;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
public class ProblemResponseWrapper {
        
	@ApiObjectField(description = "Problems")
	private List<ProblemResponse> problems;
	
}
