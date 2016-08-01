package com.fuchika.project.api;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fuchika.project.model.RepeatType;
import com.fuchika.project.model.Task;
import com.fuchika.user.api.ApiUserSimple;

@XmlRootElement
public class ApiTask {

	private String id;
	private String projectId;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private String startTime;
	private String endTime;
	private RepeatType repeatType = RepeatType.ONCE;
	private String repeatValue = "";
	private ApiUserSimple worker;
	private boolean completed = false;
	private boolean terminated = false;
	private boolean deleted = false;
	private List<String> dailyWorks;


	public ApiTask() {
	}

	public ApiTask(Task task, ApiUserSimple worker) {
		this.id = task.getId();
		this.title = task.getTitle();
		this.description = task.getDescription();
		this.startDate = task.getStartDate();
		this.endDate = task.getEndDate();
		this.startTime = task.getStartTime();
		this.endTime = task.getEndTime();
		this.repeatType = task.getRepeatType();
		this.repeatValue = task.getRepeatValue();
		this.worker = worker;
		this.completed = task.isCompleted();
		this.terminated = task.isTerminated();
		this.deleted = task.isDeleted();
		this.dailyWorks = task.getDailyWorks();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ApiUserSimple getWorker() {
		return worker;
	}

	public void setWorker(ApiUserSimple worker) {
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