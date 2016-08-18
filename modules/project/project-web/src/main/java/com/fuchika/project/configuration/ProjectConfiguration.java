package com.fuchika.project.configuration;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fuchika.project.controller.ProjectController;
import com.fuchika.project.repository.ProjectFollowedRepository;
import com.fuchika.project.repository.ProjectLikedRepository;
import com.fuchika.project.repository.ProjectRepository;
import com.fuchika.project.service.ProjectService;
import com.fuchika.project.service.ProjectServiceImpl;
import com.fuchika.user.service.UserService;

@Configuration
public class ProjectConfiguration {

	@Autowired
	private Validator validator;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectLikedRepository projectLikedRepository;

	@Autowired
	private ProjectFollowedRepository projectFollowedRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	/*
	 * @Autowired ObjectMapper objectMapper;
	 */

	@Bean
	public ProjectService projectService() {
		return new ProjectServiceImpl(userService, projectRepository, projectLikedRepository, projectFollowedRepository,
				validator);
	}

	@Bean
	public ProjectController projectController() {
		return new ProjectController(projectService);
	}

}
