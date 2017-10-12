package com.arun.response;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.List;

@Data
public class CodeDetailsWrapper {

	@ApiObjectField(description = "Problem")
	private ProblemCode problem;

	@ApiObjectField(description = "List of Answers")
	private List<ProblemCode> answers;

}
