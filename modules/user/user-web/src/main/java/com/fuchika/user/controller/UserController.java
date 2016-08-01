package com.fuchika.user.controller;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fuchika.exception.ValidationException;
import com.fuchika.oauth2.exception.AuthorizationException;
import com.fuchika.user.api.ApiUser;
import com.fuchika.user.api.CreateUserRequest;
import com.fuchika.user.api.CreateUserResponse;
import com.fuchika.user.api.MeResponse;
import com.fuchika.user.api.OAuthAccountRequest;
import com.fuchika.user.api.UpdateUserRequest;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.DuplicateUserException;
import com.fuchika.user.exception.OAuthAccountConflictException;
import com.fuchika.user.exception.OAuthAccountOnlyException;
import com.fuchika.user.exception.OAuthGetUserInfoException;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.OAuthAccount;
import com.fuchika.user.model.OAuthAccountProvider;
import com.fuchika.user.model.Role;
import com.fuchika.user.model.User;
import com.fuchika.user.service.OAuthAccountService;
import com.fuchika.user.service.UserService;
import com.fuchika.user.service.VerificationTokenService;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	private UserService userService;
	private VerificationTokenService verificationTokenService;
	private DefaultTokenServices tokenServices;
	private PasswordEncoder passwordEncoder;
	private ClientDetailsService clientDetailsService;
	private OAuthAccountService accountService;

	public UserController() {
	}

	@Autowired
	public UserController(final UserService userService, final VerificationTokenService verificationTokenService,
			final DefaultTokenServices defaultTokenServices, final PasswordEncoder passwordEncoder,
			ClientDetailsService clientDetailsService, OAuthAccountService accountService) {
		this.userService = userService;
		this.verificationTokenService = verificationTokenService;
		this.tokenServices = defaultTokenServices;
		this.passwordEncoder = passwordEncoder;
		this.clientDetailsService = clientDetailsService;
		this.accountService = accountService;
	}

	@PermitAll
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public CreateUserResponse signupUser(final @RequestBody CreateUserRequest request, Principal principal)
			throws UserNotFoundException, DuplicateUserException, ValidationException {
		ApiUser user = userService.createUser(request);
		CreateUserResponse createUserResponse = new CreateUserResponse(user, createTokenForUser(user.getId(),
				passwordEncoder.encode(request.getPassword().getPassword()), principal.getName()));
		verificationTokenService.sendEmailRegistrationToken(createUserResponse.getApiUser().getId());
		// URI location = uriInfo.getAbsolutePathBuilder()
		// .path(createUserResponse.getApiUser().getId()).build();
		return createUserResponse;
	}

	@RolesAllowed({ "ROLE_USER" })
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public MeResponse getMe(final Principal principal) throws UserNotFoundException {
		User requestingUser = loadUserFromPrincipal(principal);
		if (requestingUser == null) {
			throw new UserNotFoundException();
		}
		return new MeResponse(requestingUser);
	}

	@RolesAllowed({ "ROLE_USER" })
	@RequestMapping(value = "/me", method = RequestMethod.PUT)
	public void updateMe(Principal principal, @RequestBody UpdateUserRequest request)
			throws AuthorizationException, UserNotFoundException, ValidationException {
		User requestingUser = loadUserFromPrincipal(principal);

		boolean sendVerificationToken = StringUtils.hasLength(request.getEmailAddress())
				&& !request.getEmailAddress().equals(requestingUser.getEmailAddress());
		ApiUser savedUser = userService.saveUser(requestingUser.getId(), request);
		if (sendVerificationToken) {
			verificationTokenService.sendEmailVerificationToken(savedUser.getId());
		}
		return;
	}

	@RolesAllowed({ "ROLE_USER" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ApiUser getUser(final @PathVariable("id") String userId, final Principal principal)
			throws AuthorizationException, UserNotFoundException {
		User requestingUser = ensureUserIsAuthorized(principal, userId);
		return userService.getUser(requestingUser.getId());
	}

	private OAuth2AccessToken createTokenForUser(String userId, String hashedPassword, String clientId) {
		// String hashedPassword = passwordEncoder.encode(password);
		UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(userId,
				hashedPassword, Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_USER.toString())));
		ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
		OAuth2Request oAuth2Request = createOAuth2Request(null, clientId,
				Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_USER.toString())), true,
				authenticatedClient.getScope(), null, null, null, null);
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
		return tokenServices.createAccessToken(oAuth2Authentication);
	}

	@RolesAllowed({ "ROLE_USER" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public void updateUser(Principal principal, @PathVariable("userId") String userId,
			@RequestBody UpdateUserRequest request)
					throws AuthorizationException, UserNotFoundException, ValidationException {
		User requestingUser = ensureUserIsAuthorized(principal, userId);

		boolean sendVerificationToken = StringUtils.hasLength(request.getEmailAddress())
				&& !request.getEmailAddress().equals(requestingUser.getEmailAddress());
		ApiUser savedUser = userService.saveUser(userId, request);
		if (sendVerificationToken) {
			verificationTokenService.sendEmailVerificationToken(savedUser.getId());
		}
		return;
	}

	@PermitAll
	@RequestMapping(value = "/auth/{provider}", method = RequestMethod.POST)
	@ResponseBody
	public OAuth2AccessToken loginOAuth(@PathVariable("provider") String provider,
			@RequestBody final OAuthAccountRequest payload, Principal principal) throws OAuthAccountConflictException,
					UserNotFoundException, JsonParseException, JsonMappingException, UnsupportedOperationException,
					IOException, AuthenticationException, OAuthGetUserInfoException {

		OAuthAccountProvider accountProvider = OAuthAccountProvider.getProvider(provider);
		OAuthAccount userInfo = accountService.getAccount(accountProvider, payload);
		// Step 3. Process the authenticated the user.
		ApiUser userToLink = accountService.getOrCreateUserByProvider(accountProvider, userInfo);
		final OAuth2AccessToken token = createTokenForUser(userToLink.getId(), "", principal.getName());
		return token;
	}

	@RequestMapping(value = "/link/{provider}", method = RequestMethod.POST)
	public void linkOAuth(@PathVariable("provider") String provider, @RequestBody final OAuthAccountRequest payload,
			Principal principal) throws OAuthAccountConflictException, UserNotFoundException, JsonParseException,
					JsonMappingException, UnsupportedOperationException, IOException, AuthenticationException,
					OAuthGetUserInfoException {

		OAuthAccountProvider accountProvider = OAuthAccountProvider.getProvider(provider);
		OAuthAccount userInfo = accountService.getAccount(accountProvider, payload);
		// Step 3. Process the authenticated the user.
		final User requestingUser = loadUserFromPrincipal(principal);
		ApiUser userToLink;
		// Step 3a. If user is already signed in then link accounts.
		userToLink = accountService.linkProvider(accountProvider, requestingUser.getId(), userInfo);
	}

	@RequestMapping(value = "/unlink/{provider}", method = RequestMethod.POST)
	public void unlinkOAuth(@PathVariable("provider") String provider, Principal principal)
			throws OAuthAccountConflictException, UserNotFoundException, JsonParseException, JsonMappingException,
			UnsupportedOperationException, IOException, AuthenticationException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException, OAuthAccountOnlyException {

		OAuthAccountProvider accountProvider = OAuthAccountProvider.getProvider(provider);
		final User requestingUser = loadUserFromPrincipal(principal);
		accountService.unlinkProvider(accountProvider, requestingUser.getId());
	}

	private OAuth2Request createOAuth2Request(Map<String, String> requestParameters, String clientId,
			Collection<? extends GrantedAuthority> authorities, boolean approved, Collection<String> scope,
			Set<String> resourceIds, String redirectUri, Set<String> responseTypes,
			Map<String, Serializable> extensionProperties) {
		return new OAuth2Request(requestParameters, clientId, authorities, approved,
				scope == null ? null : new LinkedHashSet<String>(scope), resourceIds, redirectUri, responseTypes,
				extensionProperties);
	}

}