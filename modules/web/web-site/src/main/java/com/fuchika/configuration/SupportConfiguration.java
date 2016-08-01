package com.fuchika.configuration;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fuchika.controller.ExceptionHandler;

@Configuration
public class SupportConfiguration {

	@Bean
	public Validator validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Bean
	public ExceptionHandler exceptionHandler() {
		return new ExceptionHandler();
	}
	
	// @Bean
	// @Scope(value = "singleton")
	// public GenericExceptionMapper genericExceptionMapper() {
	// return new GenericExceptionMapper();
	// }

	// @Bean
	// public HealthCheckController healthCheckController() {
	// return new HealthCheckController();
	// }

}
