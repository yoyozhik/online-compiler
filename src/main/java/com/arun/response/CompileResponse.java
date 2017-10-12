package com.arun.response;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.List;

@Data
public class CompileResponse {

	@ApiObjectField(description = "Compiled Output")
	private List<String> compiledOutput;

	@ApiObjectField(description = "Flag")
	private boolean flag;

}
