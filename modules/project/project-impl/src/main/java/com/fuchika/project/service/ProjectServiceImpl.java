package com.fuchika.project.service;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuchika.exception.ValidationException;
import com.fuchika.project.api.ApiProejct;
import com.fuchika.project.api.CreateProjectRequest;
import com.fuchika.project.model.Project;
import com.fuchika.project.repository.ProjectRepository;
import com.fuchika.service.BaseService;
import com.fuchika.user.api.ApiUserSimple;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.service.UserService;

@Service
public class ProjectServiceImpl extends BaseService implements ProjectService {
	
	private Logger LOG = LoggerFactory.getLogger(ProjectService.class);

	private ProjectRepository projectRepository;
	private UserService userService;


	@Autowired
	public ProjectServiceImpl(final UserService userService, final ProjectRepository projectRepository,
			Validator validator) {
		super(validator);
		this.userService = userService;
		this.projectRepository = projectRepository;
	}

	@Transactional
	public ApiProejct createProject(final String owner, final CreateProjectRequest createProjectRequest)
			throws ValidationException, UserNotFoundException {

		LOG.info("Validating project request.");
		validate(createProjectRequest);
		ApiUserSimple userOwner = userService.getUserSimple(owner);
		Project newProject = createProjectRequest.createProject(userOwner.getId());
		return new ApiProejct(projectRepository.save(newProject), userOwner);
	}

}