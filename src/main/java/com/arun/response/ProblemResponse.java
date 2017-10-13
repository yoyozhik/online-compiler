package com.arun.response;

import com.arun.domain.MetaData;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
public class ProblemResponse {

	@ApiObjectField(description = "Problem Id")
	private String problemId;

	@ApiObjectField(description = "Metadata")
	private MetaData metaData;

}
