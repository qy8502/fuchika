package com.fuchika.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fuchika.model.BaseEntity;
import com.mongodb.DBObject;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 24/04/2013
 */
@Document(collection = "user")
public class User extends BaseEntity implements UserDetails {

	private String emailAddress;
	private String firstName;
	private String lastName;
	private Integer age;
	private String hashedPassword;
	private Boolean verified = false;
	private List<Role> roles = new ArrayList<Role>();

	private String displayName;
	private String picture;
	private String facebook;
	private String google;
	private String linkedin;
	private String github;
	private String foursquare;
	private String twitter;

	public User() {
		super();
	}

	public User(String id) {
		super(id);
	}

	/*
	 * public User(final ApiUser apiUser, final String hashedPassword, Role
	 * role) { this(); this.emailAddress =
	 * apiUser.getEmailAddress().toLowerCase(); this.hashedPassword =
	 * hashedPassword; this.displayName = apiUser.getDisplayName();
	 * this.firstName = apiUser.getFirstName(); this.lastName =
	 * apiUser.getLastName(); this.age = apiUser.getAge(); this.roles.add(role);
	 * }
	 */

	public User(final OAuthAccountProvider provider, final OAuthAccount account,
			Role role) {
		this();
		this.setProviderId(provider, account.getId());
		this.displayName = account.getDisplayName();
		this.setPicture(account.getPicture());
		this.roles.add(role);
	}

	public User(DBObject dbObject) {
		this((String) dbObject.get("_id"));
		this.emailAddress = (String) dbObject.get("emailAddress");
		this.firstName = (String) dbObject.get("firstName");
		this.lastName = (String) dbObject.get("lastName");
		this.hashedPassword = (String) dbObject.get("hashedPassword");
		this.verified = (Boolean) dbObject.get("verified");
		List<String> roles = (List<String>) dbObject.get("roles");
		deSerializeRoles(roles);
	}

	private void deSerializeRoles(List<String> roles) {
		for (String role : roles) {
			this.addRole(Role.valueOf(role));
		}
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (Role role : this.getRoles()) {
			GrantedAuthority authority = new SimpleGrantedAuthority(role.name());
			authorities.add(authority);
		}
		return authorities;
	}

	public String getPassword() {
		return hashedPassword;
	}

	public String getUsername() {
		return getId();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean isVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public List<Role> getRoles() {
		return Collections.unmodifiableList(this.roles);
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	public boolean hasRole(Role role) {
		return (this.roles.contains(role));
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String name) {
		this.displayName = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getFacebook() {
		return facebook;
	}

	public String getGoogle() {
		return google;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public String getGithub() {
		return github;
	}

	public String getFoursquare() {
		return foursquare;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setProviderId(final OAuthAccountProvider provider, final String value) {
		switch (provider) {
		case FACEBOOK:
			this.facebook = value;
			break;
		case GOOGLE:
			this.google = value;
			break;
		case LINKEDIN:
			this.linkedin = value;
			break;
		case GITHUB:
			this.github = value;
			break;
		case FOURSQUARE:
			this.foursquare = value;
			break;
		case TWITTER:
			this.twitter = value;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	@JsonIgnore
	public int getSignInMethodCount()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		int count = 0;

		if (this.getPassword() != null) {
			count++;
		}

		for (final OAuthAccountProvider p : OAuthAccountProvider.values()) {
			if (this.getClass().getDeclaredField(p.name).get(this) != null) {
				count++;
			}
		}

		return count;
	}
}