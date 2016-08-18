package com.fuchika.project.api;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fuchika.project.model.Project;
import com.fuchika.user.api.ApiUserSimple;

@XmlRootElement
public class ApiProejct {

	private String id;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private ApiUserSimple owner;
	private boolean completed = false;
	private boolean terminated = false;
	private long likedCount = 0;
	private boolean liked = false;
	private long followedCount = 0;
	private boolean followed = false;
	private boolean deleted = false;
	private List<String> tags;
	private List<ApiUserSimple> partners;
	private List<ApiTask> tasks;

	public ApiProejct() {
	}

	public ApiProejct(final Project project, ApiUserSimple owner, List<ApiUserSimple> partners, long likedCount,
			long followedCount, boolean liked, boolean followed, List<ApiTask> tasks) {
		this(project, owner, partners, likedCount, followedCount, liked, followed);
		this.tasks = tasks;
	}

	public ApiProejct(final Project project, ApiUserSimple owner, List<ApiUserSimple> partners, long likedCount,
			long followedCount, boolean liked, boolean followed) {
		this(project, owner);
		this.partners = partners;
	}

	public ApiProejct(final Project project, ApiUserSimple owner) {
		this.id = project.getId();
		this.title = project.getTitle();
		this.description = project.getDescription();
		this.startDate = project.getStartDate();
		this.endDate = project.getEndDate();
		this.owner = owner;
		this.completed = project.isCompleted();
		this.terminated = project.isTerminated();
		this.deleted = project.isDeleted();
		this.tags = project.getTags();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ApiUserSimple getOwner() {
		return owner;
	}

	public void setOwner(ApiUserSimple owner) {
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

	public long getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(long likedCount) {
		this.likedCount = likedCount;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public long getFollowedCount() {
		return followedCount;
	}

	public void setFollowedCount(long followedCount) {
		this.followedCount = followedCount;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
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

	public List<ApiTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ApiTask> tasks) {
		this.tasks = tasks;
	}

	public List<ApiUserSimple> getPartners() {
		return partners;
	}

	public void setPartners(List<ApiUserSimple> partners) {
		this.partners = partners;
	}

}
