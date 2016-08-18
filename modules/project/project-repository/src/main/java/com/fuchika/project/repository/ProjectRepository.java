package com.fuchika.project.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fuchika.project.model.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {

	public Project findById(final String id);
	
	public List<Project> findByOwner(final String id);
	
}
