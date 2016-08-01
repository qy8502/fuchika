package com.fuchika.user.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fuchika.user.api.ApiUser;
import com.fuchika.user.api.OAuthAccountRequest;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.OAuthAccountConflictException;
import com.fuchika.user.exception.OAuthAccountOnlyException;
import com.fuchika.user.exception.OAuthGetUserInfoException;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.OAuthAccount;
import com.fuchika.user.model.OAuthAccountProvider;

public interface OAuthAccountService {

	public ApiUser getOrCreateUserByProvider(OAuthAccountProvider provider, OAuthAccount account);

	public ApiUser linkProvider(OAuthAccountProvider provider, String userId, OAuthAccount account)
			throws UserNotFoundException, OAuthAccountConflictException;

	public ApiUser getUserByProvider(OAuthAccountProvider provider, String providerId) throws UserNotFoundException;

	public OAuthAccount getAccount(OAuthAccountProvider provider, final OAuthAccountRequest request)
			throws JsonParseException, JsonMappingException, ClientProtocolException, AuthenticationException,
			IOException, OAuthGetUserInfoException;

	public ApiUser unlinkProvider(OAuthAccountProvider accountProvider, String id) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException, OAuthAccountOnlyException;
}
