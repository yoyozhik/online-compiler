package com.arun.request;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
public class UpdateRequest {

	@ApiObjectField(description = "Question Id")
	private String questionId;

	@ApiObjectField(description = "Feedback")
	private String feedback;

	@ApiObjectField(description = "Rating")
	private String rating;

}
