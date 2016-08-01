package com.fuchika.filter.spring;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Created by iainporter on 26/07/2014.
 */
@Component
public class SpringCrossOriginResourceSharingFilter extends GenericFilterBean
		implements Filter {

	@Value("${cors.allowed.origins}")
	String allowedOriginsString;

	public String getAllowedOriginsString() {
		return allowedOriginsString;
	}

	public void setAllowedOriginsString(String allowedOriginsString) {
		this.allowedOriginsString = allowedOriginsString;
	}

	private static final Set<String> EMPTY = new HashSet<String>();

	private Set<String> parseAllowedOrigins(String allowedOriginsString) {
		if (!StringUtils.isEmpty(allowedOriginsString)) {
			return new HashSet<String>(Arrays.asList(allowedOriginsString
					.split(",")));
		} else {
			return EMPTY;
		}

	}

	public Set<String> getAllowedOrigins(String allowedOriginsString) {
		return parseAllowedOrigins(allowedOriginsString);
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			if (request.getHeader("Origin") != null) {
				String origin = ((HttpServletRequest) req).getHeader("Origin");
				if (getAllowedOrigins(allowedOriginsString).contains(origin)) {

					response.setHeader("Access-Control-Allow-Credentials",
							"true");
					response.setHeader("Access-Control-Allow-Origin", origin);
					response.setHeader("Access-Control-Allow-Methods",
							"GET, POST, PUT, DELETE, OPTIONS");
					response.setHeader("Access-Control-Max-Age", "3600");
					response.setHeader("Access-Control-Allow-Headers",
							"x-requested-with, Authorization, Content-Type");
					if (request.getMethod().equals("OPTIONS")) {
						try {
							response.getWriter().print("OK");
							response.getWriter().flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			}
		}
		chain.doFilter(req, res);
	}

	public void destroy() {
	}

}