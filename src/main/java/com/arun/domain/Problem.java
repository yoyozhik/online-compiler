package com.arun.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Problem {

	private String id;	  
	private MetaData metadata;
	private String filename;
	private String contentType;
	private Long chunkSize;
	private Long length;
	private Date uploadDate;
	private String md5;
	private String aliases;

}