package com.fuchika.user.api;

import java.security.Principal;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.StringUtils;

import com.fuchika.user.model.User;

@XmlRootElement
public class ApiUserSimple implements Principal {

	private String displayName;
	private String picture;
	private String id;

	public ApiUserSimple() {
	}

	public ApiUserSimple(User user) {
		this.id = user.getId();
		this.displayName = StringUtils.hasText(user.getDisplayName()) ? user.getDisplayName() : user.getEmailAddress();
		this.picture = user.getPicture();
	}
	

	public String getId() {
		return id;
	}

	public void setId(final String id) {		
		this.id = id;
	}


	public String getName() {
		return displayName;
	}

	public void setName(String displayName) {
		this.displayName = displayName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}


}