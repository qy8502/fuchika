package com.fuchika.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuchika.user.api.ApiUser;
import com.fuchika.user.api.OAuthAccountRequest;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.OAuthAccountConflictException;
import com.fuchika.user.exception.OAuthAccountOnlyException;
import com.fuchika.user.exception.OAuthGetUserInfoException;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.OAuthAccount;
import com.fuchika.user.model.OAuthAccountProvider;
import com.fuchika.user.model.Role;
import com.fuchika.user.model.User;
import com.fuchika.user.repository.UserRepository;

public class OAuthAccountServiceImpl implements OAuthAccountService {

	public static final String CLIENT_ID_KEY = "client_id", REDIRECT_URI_KEY = "redirect_uri",
			CLIENT_SECRET = "client_secret", CODE_KEY = "code", GRANT_TYPE_KEY = "grant_type",
			AUTH_CODE = "authorization_code", AUTH_HEADER_KEY = "Authorization";

	private Logger LOG = LoggerFactory.getLogger(OAuthAccountServiceImpl.class);
	private UserRepository userRepository;
	private Map<OAuthAccountProvider, String> secrets;

	public OAuthAccountServiceImpl(UserRepository userRepository, Map<OAuthAccountProvider, String> secrets) {
		this.userRepository = userRepository;
		this.secrets = secrets;
	}

	public String getFacebookSecret() {
		return secrets.get(OAuthAccountProvider.FACEBOOK);
	}

	public String getGoogleSecret() {
		return secrets.get(OAuthAccountProvider.GOOGLE);
	}

	public String getLinkedinSecret() {
		return secrets.get(OAuthAccountProvider.LINKEDIN);
	}

	public String getGithubSecret() {
		return secrets.get(OAuthAccountProvider.GITHUB);
	}

	public String getFoursquareSecret() {
		return secrets.get(OAuthAccountProvider.FOURSQUARE);
	}

	public String getTwitterSecret() {
		return secrets.get(OAuthAccountProvider.TWITTER);
	}

	public String getSecret(final OAuthAccountProvider provider) {
		switch (provider) {
		case FACEBOOK:
		case GOOGLE:
		case LINKEDIN:
		case GITHUB:
		case FOURSQUARE:
		case TWITTER:
			return secrets.get(provider);
		default:
			throw new IllegalArgumentException();
		}
	}

	public OAuthAccount getGoogleAccount(final OAuthAccountRequest request) throws IOException, ClientProtocolException,
			JsonParseException, JsonMappingException, AuthenticationException, OAuthGetUserInfoException {
		final String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
		final String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";

		// Step 1. Exchange authorization code for access token.
		CloseableHttpClient client;

		if (Boolean.valueOf(System.getProperty("USE_PROXY", "false"))) {
			HttpHost proxy = new HttpHost("127.0.0.1", 1080);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			client = HttpClientBuilder.create().setRoutePlanner(routePlanner).build();
		} else {
			client = HttpClientBuilder.create().build();
		}

		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(CLIENT_ID_KEY, request.getClientId()));
		postParams.add(new BasicNameValuePair(REDIRECT_URI_KEY, request.getRedirectUri()));
		postParams.add(new BasicNameValuePair(CLIENT_SECRET, this.getGoogleSecret()));
		postParams.add(new BasicNameValuePair(CODE_KEY, request.getCode()));
		postParams.add(new BasicNameValuePair(GRANT_TYPE_KEY, AUTH_CODE));

		CloseableHttpResponse response;
		Map<String, Object> responseMap = null;
		HttpPost post = new HttpPost(accessTokenUrl);
		post.setEntity(new UrlEncodedFormEntity(postParams, Consts.UTF_8));

		ObjectMapper objectMapper = new ObjectMapper();
		response = client.execute(post);
		// response = client.execute(post, context);
		HttpEntity entity = response.getEntity();
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			responseMap = objectMapper.readValue(entity.getContent(), new TypeReference<Map<String, Object>>() {
			});

		} else {
			throw new AuthenticationException();
		}

		// Step 2. Retrieve profile information about the current user.
		final String accessToken = (String) responseMap.get("access_token");
		HttpGet getPeople = new HttpGet(peopleApiUrl);
		CloseableHttpResponse responsePeople;
		Map<String, Object> userInfo = null;
		getPeople.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");
		getPeople.setHeader(AUTH_HEADER_KEY, String.format("Bearer %s", accessToken));
		responsePeople = client.execute(getPeople);
		// responsePeople = client.execute(postPeople, context);
		HttpEntity entityPeople = responsePeople.getEntity();

		if (responsePeople.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			userInfo = objectMapper.readValue(entityPeople.getContent(), new TypeReference<Map<String, Object>>() {
			});

		} else {
			String message = EntityUtils.toString(entityPeople);
			throw new OAuthGetUserInfoException(message);
		}
		return new OAuthAccount(userInfo.get("sub").toString(), userInfo.get("name").toString(),
				userInfo.get("picture").toString());
	}

	public OAuthAccount getAccount(OAuthAccountProvider provider, final OAuthAccountRequest request)
			throws JsonParseException, JsonMappingException, ClientProtocolException, AuthenticationException,
			IOException, OAuthGetUserInfoException {
		OAuthAccount account;
		switch (provider) {
		case FACEBOOK:
			account = this.getGoogleAccount(request);
			break;
		case GOOGLE:
			account = this.getGoogleAccount(request);
			break;
		case LINKEDIN:
			account = this.getGoogleAccount(request);
			break;
		case GITHUB:
			account = this.getGoogleAccount(request);
			break;
		case FOURSQUARE:
			account = this.getGoogleAccount(request);
			break;
		case TWITTER:
			account = this.getGoogleAccount(request);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return account;
	}

	@Transactional
	public ApiUser getOrCreateUserByProvider(OAuthAccountProvider provider, OAuthAccount account) {

		User user = locateUserByProvider(provider, account.getId());
		if (user == null) {
			LOG.info("User does not already exist in the data store - creating a new user [{}] from [{}].",
					account.getId(), provider.capitalize());
			User newUser = new User(provider, account, Role.ROLE_USER);
			user = userRepository.save(newUser);
			LOG.debug("Created new user [{}] from [{}].", account.getId(), provider.capitalize());
		}

		return new ApiUser(user);
	}

	@Transactional
	public ApiUser linkProvider(OAuthAccountProvider provider, String userId, OAuthAccount account)
			throws UserNotFoundException, OAuthAccountConflictException {
		Assert.notNull(userId);
		Assert.notNull(account);
		User userLinked = locateUserByProvider(provider, account.getId());
		if (userLinked != null) {
			if (userLinked.getId().equals(userId)) {
				return new ApiUser(userLinked);
			} else {
				throw new OAuthAccountConflictException(provider);
			}
		}
		User user = userRepository.findById(userId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		if (!StringUtils.hasText(user.getDisplayName())) {
			user.setDisplayName(account.getDisplayName());
		}
		user.setProviderId(provider, account.getId());
		userRepository.save(user);
		return new ApiUser(user);
	}

	private User locateUserByProvider(OAuthAccountProvider provider, String providerId) {
		Assert.notNull(providerId);
		User user;
		switch (provider) {
		case FACEBOOK:
			user = userRepository.findByFacebook(providerId);
			break;
		case GOOGLE:
			user = userRepository.findByGoogle(providerId);
			break;
		case LINKEDIN:
			user = userRepository.findByLinkedin(providerId);
			break;
		case GITHUB:
			user = userRepository.findByGithub(providerId);
			break;
		case FOURSQUARE:
			user = userRepository.findByFoursquare(providerId);
			break;
		case TWITTER:
			user = userRepository.findByTwitter(providerId);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return user;
	}

	public ApiUser getUserByProvider(OAuthAccountProvider provider, String providerId) throws UserNotFoundException {
		User user = locateUserByProvider(provider, providerId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return new ApiUser(user);
	}

	public ApiUser unlinkProvider(OAuthAccountProvider accountProvider, String userId) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException, OAuthAccountOnlyException {
		final User user = userRepository.findById(userId);

		// check that the user is not trying to unlink the only sign-in method
		if (user.getSignInMethodCount() == 1) {
			throw new OAuthAccountOnlyException(accountProvider);
		}
		user.setProviderId(accountProvider, null);
		userRepository.save(user);
		return new ApiUser(user);
	}
}
