package com.fuchika.project.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fuchika.project.model.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {

	public Project findById(final String id);
}
