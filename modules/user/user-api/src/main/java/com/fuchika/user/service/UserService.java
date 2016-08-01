package com.fuchika.user.service;

import java.util.List;

import org.springframework.util.Assert;

import com.fuchika.exception.ValidationException;
import com.fuchika.user.exception.AuthenticationException;
import com.fuchika.user.exception.DuplicateUserException;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.User;
import com.fuchika.user.api.ApiUser;
import com.fuchika.user.api.ApiUserSimple;
import com.fuchika.user.api.CreateUserRequest;
import com.fuchika.user.api.UpdateUserRequest;

public interface UserService {

	public ApiUser createUser(final CreateUserRequest createUserRequest)
			throws DuplicateUserException, ValidationException;

	public ApiUser authenticate(String username, String password) throws AuthenticationException;

	public ApiUser getUser(String userId) throws UserNotFoundException;

	public ApiUserSimple getUserSimple(String userId) throws UserNotFoundException;

	public List<ApiUserSimple> getUserSimpleList(List<String> userIds);

	/**
	 * Save User
	 * 
	 * @param userId
	 * @param request
	 * @throws UserNotFoundException
	 * @throws ValidationException
	 */
	public ApiUser saveUser(String userId, UpdateUserRequest request) throws UserNotFoundException, ValidationException;

}
