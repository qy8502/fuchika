package com.fuchika.project.repository;
 
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fuchika.project.model.ProjectLiked;

public interface ProjectLikedRepository extends MongoRepository<ProjectLiked, String> {

	public ProjectLiked findById(final String id);

	public List<ProjectLiked> findByUser(final String user);

	public List<ProjectLiked> findByProject(final String project);
	
	public long countByProject(final String project);

	public ProjectLiked findByProjectAndUser(final String project, final String user);

}
