package com.fuchika.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuchika.user.api.BadClientCredentialsErrorResponse;

/**
 * Created by iainporter on 16/10/2014.
 */
@Component
public class OAuthRestEntryPoint implements AuthenticationEntryPoint {

	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ObjectMapper mapper = new ObjectMapper();
		String errorEntity = mapper
				.writeValueAsString(new BadClientCredentialsErrorResponse(
						"Bad client credentials"));
		response.getOutputStream().print(errorEntity);
	}
}
