package com.fuchika.project.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fuchika.project.model.Project;


@XmlRootElement
public class CreateProjectRequest {


	@NotNull
	@Valid
	private String title;	

	public CreateProjectRequest() {
	}

	public CreateProjectRequest(String title) {
		this.title = title;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Project createProject(String owner) {
		Project projectNew = new Project();
		projectNew.setTitle(title);
		projectNew.setOwner(owner);
		return projectNew;
	}
}
