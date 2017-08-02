package com.arun.response;

import lombok.Data;

import java.util.List;

@Data
public class CodeDetailsWrapper {

	private ProblemCode problem;
	private List<ProblemCode> answers;

}
