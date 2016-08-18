package com.fuchika.project.repository;
 
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fuchika.project.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

	public Task findById(final String id);
	
	public List<Task> findByIdIn(final List<String> id);
}
