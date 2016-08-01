package com.fuchika.user.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fuchika.user.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByEmailAddress(final String name);

	public User findById(final String id);
	
	public List<User> findByIdIn(final List<String> id);

	public User findByFacebook(final String id);

	public User findByGoogle(final String id);

	public User findByLinkedin(final String id);

	public User findByGithub(final String id);

	public User findByFoursquare(final String id);

	public User findByTwitter(final String id);
}
