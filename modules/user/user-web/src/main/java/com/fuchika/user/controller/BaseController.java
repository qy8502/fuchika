package com.fuchika.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;

import com.fuchika.oauth2.exception.AuthorizationException;
import com.fuchika.user.model.User;
import com.fuchika.user.repository.UserRepository;


/**
 * @version 1.0
 * @author: Iain Porter
 * @since 07/05/2013
 */
public class BaseController {

	@Autowired
	private UserRepository userRepository;

	// TODO: Cache to reduce calls to userRepository
	protected User ensureUserIsAuthorized(Principal principal, String userId)
			throws AuthorizationException {
		User user = loadUserFromPrincipal(principal);
		if (user != null
				&& (user.getId().equals(userId) || user.getEmailAddress()
						.equals(userId.toLowerCase()))) {
			return user;
		}
		throw new AuthorizationException(
				"User not permitted to access this resource");

	}

	protected User loadUserFromPrincipal(Principal principal) {
		User user = null;
		if (principal instanceof User) {
			user = (User) principal;
		} else if (principal instanceof Principal) {
			user = userRepository.findById(principal.getName());
		} else {
			user = userRepository.findById(String.valueOf(principal));
		}
		return user;
	}
}
