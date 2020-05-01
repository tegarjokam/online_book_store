package com.tegar.exception;

import java.util.Date;

public class ExceptionResponse {
	
	private Date timestamp;
	
	private Integer status;
	
	private String error;
	
	private String path;
	
	private String message;

	

	public Date getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getPath() {
		return path;
	}

	public String getMessage() {
		return message;
	}

	public ExceptionResponse(Date timestamp, Integer status, String error, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	
	

}
