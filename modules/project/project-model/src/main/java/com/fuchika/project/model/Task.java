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
@Document(collection = "task")
public class Task extends BaseEntity {

	private String projectId;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private String startTime;
	private String endTime;
	private RepeatType repeatType = RepeatType.ONCE;
	private String repeatValue = "";
	private String worker;
	private boolean completed = false;
	private boolean terminated = false;
	private boolean deleted = false;
	private List<String> dailyWorks;

	public Task() {
		super();
	}

	public Task(String id) {
		super(id);
	}

	public Task(DBObject dbObject) {
		this((String) dbObject.get("_id"));
		this.title = (String) dbObject.get("title");
		this.description = (String) dbObject.get("description");
		this.startDate = (Date) dbObject.get("startDate");
		this.endDate = (Date) dbObject.get("endDate");
		this.startTime = (String) dbObject.get("startTime");
		this.endTime = (String) dbObject.get("endTime");
		this.repeatType = RepeatType.valueOf((String)dbObject.get("repeatType")) ;
		this.repeatValue = (String) dbObject.get("repeatValue");
		this.worker = (String) dbObject.get("worker");
		this.completed = (Boolean) dbObject.get("completed");
		this.terminated = (Boolean) dbObject.get("terminated");
		this.deleted = (Boolean) dbObject.get("deleted");
		this.dailyWorks = (List<String>) dbObject.get("dailyWorks");
		
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public RepeatType getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}

	public String getRepeatValue() {
		return repeatValue;
	}

	public void setRepeatValue(String repeatValue) {
		this.repeatValue = repeatValue;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
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

	public List<String> getDailyWorks() {
		return dailyWorks;
	}

	public void setDailyWorks(List<String> dailyWorks) {
		this.dailyWorks = dailyWorks;
	}


	

	
}