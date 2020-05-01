package com.tegar.exception;

public class BookServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8872196528459173923L;

	private int status;
	
	private String message;
	
	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public BookServiceException(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

}
