package com.fuchika.user.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.fuchika.mail.service.MailSenderService;
import com.fuchika.user.controller.UserController;
import com.fuchika.user.model.OAuthAccountProvider;
import com.fuchika.user.repository.UserRepository;
import com.fuchika.user.repository.VerificationTokenRepository;
import com.fuchika.user.service.OAuthAccountService;
import com.fuchika.user.service.OAuthAccountServiceImpl;
import com.fuchika.user.service.UserService;
import com.fuchika.user.service.UserServiceImpl;
import com.fuchika.user.service.VerificationTokenService;
import com.fuchika.user.service.VerificationTokenServiceImpl;

@Configuration
public class UserConfiguration {

	private static final String CLIENT_SECRETS_FACEBOOK = "client.secrets.facebook";
	private static final String CLIENT_SECRETS_GOOGLE = "client.secrets.google";
	private static final String CLIENT_SECRETS_LINKEDIN = "client.secrets.linkedin";
	private static final String CLIENT_SECRETS_GITHUB = "client.secrets.github";
	private static final String CLIENT_SECRETS_FOURSQUARE = "client.secrets.foursquare";
	private static final String CLIENT_SECRETS_TWITTER = "client.secrets.twitter";

	@Value("${" + CLIENT_SECRETS_FACEBOOK + "}")
	String facebook;

	@Value("${" + CLIENT_SECRETS_GOOGLE + "}")
	String google;

	@Value("${" + CLIENT_SECRETS_LINKEDIN + "}")
	String linkedin;

	@Value("${" + CLIENT_SECRETS_GITHUB + "}")
	String github;

	@Value("${" + CLIENT_SECRETS_FOURSQUARE + "}")
	String foursquare;

	@Value("${" + CLIENT_SECRETS_TWITTER + "}")
	String twitter;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private MailSenderService mailSenderService;

	@Autowired
	private Validator validator;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DefaultTokenServices tokenServices;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private OAuthAccountService oAuthAccountService;

	/*
	 * @Autowired ObjectMapper objectMapper;
	 */

	@Bean
	public OAuthAccountService oAuthAccountService() {
		Map<OAuthAccountProvider, String> secrets = new HashMap<OAuthAccountProvider, String>();
		secrets.put(OAuthAccountProvider.FACEBOOK, facebook);
		secrets.put(OAuthAccountProvider.GOOGLE, google);
		secrets.put(OAuthAccountProvider.LINKEDIN, linkedin);
		secrets.put(OAuthAccountProvider.GITHUB, github);
		secrets.put(OAuthAccountProvider.FOURSQUARE, foursquare);
		secrets.put(OAuthAccountProvider.TWITTER, twitter);
		return new OAuthAccountServiceImpl(userRepository, secrets);
	}

	@Bean
	public VerificationTokenService verificationTokenService() {
		return new VerificationTokenServiceImpl(userRepository,
				verificationTokenRepository, mailSenderService, validator,
				passwordEncoder);
	}

	@Bean
	public UserService userService() {
		return new UserServiceImpl(userRepository, validator, passwordEncoder);
	}

	@Bean
	public UserController userController() {
		return new UserController(userService(), verificationTokenService(),
				tokenServices, passwordEncoder, clientDetailsService,
				oAuthAccountService);
	}
	//
	// @Bean
	// public PasswordResource passwordResource() {
	// return new PasswordResource();
	// }
	//
	// @Bean
	// public VerificationResource verificationResource() {
	// return new VerificationResource();
	// }
	//
	// @Bean
	// public MeResource meResource() {
	// return new MeResource();
	// }

}
