package com.fuchika.exception;

public class UnauthorizedException extends BaseException {

	public UnauthorizedException() {
		super(401, "Unauthorized Error", "Unauthorized Error. Please login.");
	}

	public UnauthorizedException(String message) {
		super(401, "Unauthorized Error", message);
	}
}