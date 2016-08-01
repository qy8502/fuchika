package com.fuchika.exception;

import com.fuchika.api.ErrorResponse;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 24/04/2013
 */
public abstract class BaseException extends Exception {

	private final int status;
	private final String errorMessage;
	private final String developerMessage;

	public BaseException(int httpStatus, String errorMessage,
			String developerMessage) {
		this.status = httpStatus;
		this.errorMessage = errorMessage;
		this.developerMessage = developerMessage;
	}

	@Override
	public String getMessage() {
		return errorMessage;
	}

	public int getStatus() {
		return this.status;
	}

	public ErrorResponse getErrorResponse() {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode(this.getClass().getSimpleName());
		response.setApplicationMessage(developerMessage);
		response.setConsumerMessage(errorMessage);
		return response;
	}
}
