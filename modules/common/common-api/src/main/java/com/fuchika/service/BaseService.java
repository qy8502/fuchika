package com.fuchika.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.fuchika.exception.ValidationException;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
public abstract class BaseService {

	protected Validator validator;

	public BaseService(Validator validator) {
		this.validator = validator;
	}

	protected void validate(Object request) throws ValidationException {
		Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
		if (constraintViolations.size() > 0) {
			throw new ValidationException(constraintViolations);
		}
	}

}
