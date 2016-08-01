package com.fuchika.user.controller;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fuchika.user.service.VerificationTokenService;
import com.fuchika.user.api.EmailVerificationRequest;
import com.fuchika.user.exception.AlreadyVerifiedException;
import com.fuchika.user.exception.TokenHasExpiredException;
import com.fuchika.user.exception.TokenNotFoundException;
import com.fuchika.user.exception.UserNotFoundException;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
@RestController
@RequestMapping(value = "/verify")
public class VerificationController {

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@PermitAll
	@RequestMapping(value = "/tokens/{token}", method = RequestMethod.POST)
	public void verifyToken(@PathVariable("token") String token)
			throws AlreadyVerifiedException, TokenNotFoundException, TokenHasExpiredException {
		verificationTokenService.verify(token);
		return;
	}

	@PermitAll
	@RequestMapping(value = "/tokens", method = RequestMethod.POST)
	public void sendEmailToken(EmailVerificationRequest request)
			throws UserNotFoundException, AlreadyVerifiedException {
		verificationTokenService.generateEmailVerificationToken(request.getEmailAddress());
		return;
	}
}
