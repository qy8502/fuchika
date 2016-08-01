package com.fuchika.user.service;

import com.fuchika.exception.ValidationException;
import com.fuchika.user.exception.AlreadyVerifiedException;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.TokenHasExpiredException;
import com.fuchika.user.exception.TokenNotFoundException;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.VerificationToken;
import com.fuchika.user.api.LostPasswordRequest;
import com.fuchika.user.api.PasswordRequest;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
public interface VerificationTokenService {

	public VerificationToken sendEmailVerificationToken(String userId) throws UserNotFoundException;

	public VerificationToken sendEmailRegistrationToken(String userId) throws UserNotFoundException;

	public VerificationToken sendLostPasswordToken(LostPasswordRequest lostPasswordRequest) throws ValidationException;

	public VerificationToken verify(String base64EncodedToken)
			throws AlreadyVerifiedException, TokenNotFoundException, TokenHasExpiredException;

	public VerificationToken generateEmailVerificationToken(String emailAddress)
			throws UserNotFoundException, AlreadyVerifiedException;

	public VerificationToken resetPassword(String base64EncodedToken, PasswordRequest passwordRequest)
			throws AlreadyVerifiedException, AuthenticationException, TokenNotFoundException, TokenHasExpiredException,
			ValidationException;
}
