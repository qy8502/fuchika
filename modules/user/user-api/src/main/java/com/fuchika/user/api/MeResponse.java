package com.fuchika.user.api;

import org.springframework.util.StringUtils;

import com.fuchika.user.model.User;

public class MeResponse extends ApiUser {

	private boolean facebook;
	private boolean google;
	private boolean linkedin;
	private boolean github;
	private boolean foursquare;
	private boolean twitter;

	public MeResponse(User user) {
		super(user);

		this.facebook = StringUtils.hasText(user.getFacebook());
		this.google = StringUtils.hasText(user.getGoogle());
		this.linkedin = StringUtils.hasText(user.getLinkedin());
		this.github = StringUtils.hasText(user.getGithub());
		this.foursquare = StringUtils.hasText(user.getFoursquare());
		this.twitter = StringUtils.hasText(user.getTwitter());
	}

	public boolean isFacebook() {
		return facebook;
	}

	public boolean isGoogle() {
		return google;
	}

	public boolean isLinkedin() {
		return linkedin;
	}

	public boolean isGithub() {
		return github;
	}

	public boolean isFoursquare() {
		return foursquare;
	}

	public boolean isTwitter() {
		return twitter;
	}

}
