package com.example.main.services.exceptions;

public class MergeObjectException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MergeObjectException(String msg) {
		super(msg);
	}

	public MergeObjectException(String msg, Throwable e) {
		super(msg, e);
	}
}