package com.fuchika.project.service;

import java.util.List;

import com.fuchika.exception.ValidationException;
import com.fuchika.project.api.ApiProejct;
import com.fuchika.project.api.CreateProjectRequest;
import com.fuchika.user.exception.UserNotFoundException;

public interface ProjectService {

	public ApiProejct createProject(final String owner, final CreateProjectRequest createProjectRequest)
			throws ValidationException, UserNotFoundException;

	public List<ApiProejct> getProjectListOwn(String userId) throws UserNotFoundException;

}
