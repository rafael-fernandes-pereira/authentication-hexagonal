package com.github.oindiao.common.exception;

public class UserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161967927265643966L;

	public UserException(String message, Throwable error) {
		super(message, error);
	}
	
	public UserException(String message) {
		super(message);
	}
	
}
