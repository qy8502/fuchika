package com.fuchika.project.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fuchika.model.BaseEntity;
import com.mongodb.DBObject;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 24/04/2013
 */
@Document(collection = "project")
public class Project extends BaseEntity {

	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private String owner;
	private boolean completed = false;
	private boolean terminated = false;
	private boolean deleted = false;
	private List<String> tags;
	private List<String> tasks;
	private List<String> partners;

	public Project() {
		super();
	}

	public Project(String id) {
		super(id);
	}

	public Project(DBObject dbObject) {
		this((String) dbObject.get("_id"));
		this.title = (String) dbObject.get("title");
		this.description = (String) dbObject.get("description");
		this.startDate = (Date) dbObject.get("startDate");
		this.endDate = (Date) dbObject.get("endDate");
		this.owner = (String) dbObject.get("owner");
		this.completed = (boolean) dbObject.get("completed");
		this.terminated = (boolean) dbObject.get("terminated");
		this.deleted = (boolean) dbObject.get("deleted");
		this.tags = (List<String>) dbObject.get("tags");
		this.tasks = (List<String>) dbObject.get("tasks");
		this.partners = (List<String>) dbObject.get("partners");
		
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getTasks() {
		return tasks;
	}

	public void setTasks(List<String> tasks) {
		this.tasks = tasks;
	}

	public List<String> getPartners() {
		return partners;
	}

	public void setPartners(List<String> partners) {
		this.partners = partners;
	}

	
}