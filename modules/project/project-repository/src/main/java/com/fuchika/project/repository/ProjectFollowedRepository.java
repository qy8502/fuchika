package com.fuchika.project.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fuchika.project.model.ProjectFollowed;

public interface ProjectFollowedRepository extends MongoRepository<ProjectFollowed, String> {

	public ProjectFollowed findById(final String id);

	public List<ProjectFollowed> findByUser(final String user);

	public List<ProjectFollowed> findByProject(final String project);
	
	public long countByProject(final String project);

	public ProjectFollowed findByProjectAndUser(final String project, final String user);

}
