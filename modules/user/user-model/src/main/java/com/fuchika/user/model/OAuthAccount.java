package com.fuchika.user.model;

public class OAuthAccount {
	private String id;
	private String displayName;
	private String picture;

	public OAuthAccount(String id, String displayName, String picture) {
		this.id = id;
		this.displayName = displayName;
		this.picture = picture;
	}

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getPicture() {
		return picture;
	}
}
