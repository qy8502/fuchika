package com.fuchika.user.exception;

import com.fuchika.exception.BaseException;

public class AuthenticationException extends BaseException {

	public AuthenticationException() {
		super(401, "Authentication Error", "Authentication Error. The username or password were incorrect");
	}

	public AuthenticationException(String message) {
		super(401, "Authentication Error", message);
	}
}