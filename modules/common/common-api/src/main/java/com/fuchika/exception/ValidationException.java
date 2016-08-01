package com.fuchika.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.fuchika.api.ErrorResponse;
import com.fuchika.api.ValidationError;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 25/04/2013
 */
public class ValidationException extends BaseException {

	private final static int status = 400;
	private final static String defaultErrorMessage = "Validation Error";
	private final static String defaultDeveloperMessage = "The data passed in the request was invalid. Please check and resubmit";
	private String errorMessage;
	private String developerMessage;
	private List<ValidationError> errors = new ArrayList<ValidationError>();

	public ValidationException() {
		super(status, defaultErrorMessage, defaultDeveloperMessage);
	}

	public ValidationException(String message) {
		super(status, message, defaultDeveloperMessage);
		errorMessage = message;
	}

	public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
		this();
		for (ConstraintViolation<?> constraintViolation : violations) {
			ValidationError error = new ValidationError();
			error.setMessage(constraintViolation.getMessage());
			error.setPropertyName(constraintViolation.getPropertyPath().toString());
			error.setPropertyValue(constraintViolation.getInvalidValue() != null
					? constraintViolation.getInvalidValue().toString() : null);
			errors.add(error);
		}
	}

	public ErrorResponse getErrorResponse() {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode(this.getClass().getSimpleName());
		response.setApplicationMessage(developerMessage);
		response.setConsumerMessage(errorMessage);
		response.setValidationErrors(errors);
		return response;
	}

}
