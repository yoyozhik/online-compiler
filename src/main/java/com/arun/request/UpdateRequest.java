package com.arun.request;

import lombok.Data;

@Data
public class UpdateRequest {

	private String questionId;
	private String feedback;
	private String rating;

}
