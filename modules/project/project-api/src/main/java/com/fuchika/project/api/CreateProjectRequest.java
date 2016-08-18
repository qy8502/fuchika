package com.fuchika.project.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		projectNew.setStartDate(new Date());
		projectNew.setEndDate(new Date());
		List<String> partners = new ArrayList<String>();
		partners.add(owner);
		projectNew.setPartners(partners);
		return projectNew;
	}
}
