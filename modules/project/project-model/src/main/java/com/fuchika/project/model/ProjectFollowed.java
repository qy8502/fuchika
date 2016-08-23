package com.fuchika.project.model; 

import org.springframework.data.mongodb.core.mapping.Document;

import com.fuchika.model.BaseEntity;
import com.mongodb.DBObject;

@Document(collection = "projectFollowed")
public class ProjectFollowed extends BaseEntity {
	private String project;
	private String user;

	public ProjectFollowed() {
		// super();
		this.project = "project";
		this.user = "user";
	}

	public ProjectFollowed(String project, String user) {
		// super();
		this.project = project;
		this.user = user;
	}

	public ProjectFollowed(DBObject dbObject) {
		this((String) dbObject.get("project"), (String) dbObject.get("user"));
	}

	public String getId() {
		return project + "_" + user;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
