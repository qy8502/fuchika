package com.fuchika.user.controller;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fuchika.exception.ValidationException;
import com.fuchika.user.service.VerificationTokenService;
import com.fuchika.user.api.LostPasswordRequest;
import com.fuchika.user.api.PasswordRequest;
import com.fuchika.user.exception.AlreadyVerifiedException;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.TokenHasExpiredException;
import com.fuchika.user.exception.TokenNotFoundException;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
@RestController
@RequestMapping(value = "/password")
public class PasswordController {

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@PermitAll
	@RequestMapping(value = "/tokens", method = RequestMethod.POST)
	public void sendEmailToken(@RequestBody LostPasswordRequest request) throws ValidationException {
		verificationTokenService.sendLostPasswordToken(request);
		return;
	}

	@PermitAll
	@RequestMapping(value = "/tokens/{token}", method = RequestMethod.POST)
	public void resetPassword(@PathVariable("token") String base64EncodedToken,
			@RequestBody PasswordRequest request)
			throws AlreadyVerifiedException, AuthenticationException,
			TokenNotFoundException, TokenHasExpiredException,
			ValidationException {
		verificationTokenService.resetPassword(base64EncodedToken, request);
		return;
	}
}
