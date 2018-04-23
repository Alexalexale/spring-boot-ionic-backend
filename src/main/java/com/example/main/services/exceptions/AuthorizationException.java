package com.example.main.services.exceptions;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthorizationException(String msg) {
		super(msg);
	}

	public AuthorizationException(String msg, Throwable e) {
		super(msg, e);
	}
}