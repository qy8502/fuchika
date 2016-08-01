package com.fuchika.user.service;

import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fuchika.exception.ValidationException;
import com.fuchika.service.BaseService;
import com.fuchika.user.api.ApiUser;
import com.fuchika.user.api.ApiUserSimple;
import com.fuchika.user.api.CreateUserRequest;
import com.fuchika.user.api.UpdateUserRequest;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.DuplicateUserException;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.Role;
import com.fuchika.user.model.User;
import com.fuchika.user.repository.UserRepository;

@Service
public class UserServiceImpl extends BaseService implements UserService, UserDetailsService {

	private Logger LOG = LoggerFactory.getLogger(UserService.class);
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(final UserRepository userRepository, Validator validator, PasswordEncoder passwordEncoder) {
		super(validator);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return locateUser(username);
	}

	@Transactional
	public ApiUser createUser(final CreateUserRequest createUserRequest)
			throws DuplicateUserException, ValidationException {

		LOG.info("Validating user request.");
		validate(createUserRequest);
		final String emailAddress = createUserRequest.getUser().getEmailAddress().toLowerCase();
		if (userRepository.findByEmailAddress(emailAddress) == null) {
			LOG.info("User does not already exist in the data store - creating a new user [{}].", emailAddress);
			User newUser = insertNewUser(createUserRequest);
			LOG.debug("Created new user [{}].", newUser.getEmailAddress());
			return new ApiUser(newUser);
		} else {
			LOG.info("Duplicate user located, exception raised with appropriate HTTP response code.");
			throw new DuplicateUserException();
		}
	}

	public ApiUser authenticate(String username, String password) throws AuthenticationException {
		Assert.notNull(username);
		Assert.notNull(password);
		User user = locateUser(username);
		if (!passwordEncoder.encode(password).equals(user.getHashedPassword())) {
			throw new AuthenticationException();
		}
		return new ApiUser(user);
	}

	@Transactional
	public ApiUser saveUser(String userId, UpdateUserRequest request)
			throws UserNotFoundException, ValidationException {
		validate(request);
		User user = userRepository.findById(userId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getEmailAddress() != null) {
			if (!request.getEmailAddress().equals(user.getEmailAddress())) {
				user.setEmailAddress(request.getEmailAddress());
				user.setVerified(false);
			}
		}
		userRepository.save(user);
		return new ApiUser(user);
	}

	public ApiUser getUser(String userId) throws UserNotFoundException {
		Assert.notNull(userId);
		User user = userRepository.findById(userId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return new ApiUser(user);
	}

	public ApiUserSimple getUserSimple(String userId) throws UserNotFoundException {
		Assert.notNull(userId);
		User user = userRepository.findById(userId);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return new ApiUserSimple(user);
	}

	public List<ApiUserSimple> getUserSimpleList(List<String> userIds) {
		List<User> users = userRepository.findByIdIn(userIds);
		List<ApiUserSimple> result = new ArrayList<ApiUserSimple>();
		for (User user : users) {
			result.add(new ApiUserSimple(user));
		}
		return result;
	}

	/**
	 * Locate the user and throw an exception if not found.
	 * 
	 * @param username
	 * @return a User object is guaranteed.
	 * @throws AuthenticationException
	 *             if user not located.
	 */
	private User locateUser(final String username) {
		notNull(username, "Mandatory argument 'username' missing.");
		User user = userRepository.findByEmailAddress(username.toLowerCase());
		if (user == null) {
			LOG.debug("Credentials [{}] failed to locate a user.", username.toLowerCase());
			throw new UsernameNotFoundException("failed to locate a user");
		}
		return user;
	}

	private User insertNewUser(final CreateUserRequest createUserRequest) {
		String hashedPassword = passwordEncoder.encode(createUserRequest.getPassword().getPassword());
		User newUser = createUserRequest.createUser(hashedPassword, Role.ROLE_USER);
		return userRepository.save(newUser);
	}

}