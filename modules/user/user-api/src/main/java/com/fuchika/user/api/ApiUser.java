package com.fuchika.user.api;

import static org.springframework.util.Assert.notNull;

import java.security.Principal;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.springframework.util.StringUtils;

import com.fuchika.user.model.Role;
import com.fuchika.user.model.User;

@XmlRootElement
public class ApiUser implements Principal {

	@Email
	@NotNull
	private String emailAddress;

	private String displayName;
	private String picture;

	private String firstName;
	private String lastName;
	private Integer age;
	private String id;

	public ApiUser() {
	}

	public ApiUser(User user) {
		this.emailAddress = user.getEmailAddress();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.id = user.getId();
		this.age = user.getAge();
		this.displayName = StringUtils.hasText(user.getDisplayName()) ? user.getDisplayName() : user.getEmailAddress();
		this.picture = user.getPicture();
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		notNull(emailAddress, "Mandatory argument 'emailAddress missing.'");
		this.emailAddress = emailAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		notNull(id, "Mandatory argument 'id missing.'");
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		notNull(firstName, "Mandatory argument 'firstName missing.'");
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		notNull(lastName, "Mandatory argument 'lastName missing.'");
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getName() {
		return this.getDisplayName();
	}

}