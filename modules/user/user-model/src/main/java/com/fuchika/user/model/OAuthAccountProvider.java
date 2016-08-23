package com.fuchika.user.model;

import org.springframework.util.StringUtils;

public enum OAuthAccountProvider {
	FACEBOOK("facebook"), GOOGLE("google"), LINKEDIN("linkedin"), GITHUB(
			"github"), FOURSQUARE("foursquare"), TWITTER("twitter");

	String name;

	OAuthAccountProvider(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return this.name;
	}

	public String capitalize() {
		return StringUtils.capitalize(this.name);
	}

	public static OAuthAccountProvider getProvider(String provider) {
		return OAuthAccountProvider.valueOf(provider.toUpperCase());
	}
}
