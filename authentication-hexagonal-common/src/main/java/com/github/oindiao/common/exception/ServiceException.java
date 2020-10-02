package com.github.oindiao.common.exception;

public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5981871069455321186L;
	
	public ServiceException(String message, Throwable error) {
		super(message, error);
	}

}
