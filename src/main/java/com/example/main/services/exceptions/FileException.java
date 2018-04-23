package com.example.main.services.exceptions;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileException(String msg) {
		super(msg);
	}

	public FileException(String msg, Throwable e) {
		super(msg, e);
	}
}