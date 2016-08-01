package com.fuchika.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuchika.api.ErrorResponse;
import com.fuchika.exception.ApplicationRuntimeException;
import com.fuchika.exception.BaseException;
import com.fuchika.exception.UnauthorizedException;

public class ExceptionHandler implements HandlerExceptionResolver {

	private Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		if (exception instanceof AccessDeniedException) {
			exception = new UnauthorizedException();
		}
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		ErrorResponse errorResponse;
		if (exception instanceof BaseException) {
			LOG.info("Web Application Exception: " + exception);
			errorResponse = ((BaseException) exception).getErrorResponse();
			response.setStatus(((BaseException) exception).getStatus());
		} else {
			LOG.error("Internal Server Error: " + exception);
			LOG.error("Internal Server Error: " + exception.getCause());
			StringWriter errors = new StringWriter();
			exception.printStackTrace(new PrintWriter(errors));
			BaseException error = new ApplicationRuntimeException(errors.toString());
			errorResponse = error.getErrorResponse();
			response.setStatus(error.getStatus());
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> errorMap = objectMapper.convertValue(errorResponse, Map.class);
		view.setAttributesMap(errorMap);
		return new ModelAndView(view);
		// return mav.addObject(errorResponse);
	}
}
