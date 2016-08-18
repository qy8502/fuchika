package com.fuchika.project.model; 

import org.springframework.data.mongodb.core.mapping.Document;

import com.fuchika.model.BaseEntity;
import com.mongodb.DBObject;

@Document(collection = "projectLiked")
public class ProjectLiked extends BaseEntity {
	private String project;
	private String user;

	public ProjectLiked() {
		// super();
		this.project = "project";
		this.user = "user";
	}

	public ProjectLiked(String project, String user) {
		// super();
		this.project = project;
		this.user = user;
	}

	public ProjectLiked(DBObject dbObject) {
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
