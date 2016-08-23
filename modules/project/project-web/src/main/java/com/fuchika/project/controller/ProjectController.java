package com.fuchika.project.controller;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fuchika.exception.ValidationException;
import com.fuchika.project.api.ApiProejct;
import com.fuchika.project.api.CreateProjectRequest;
import com.fuchika.project.service.ProjectService;
import com.fuchika.user.controller.BaseController;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.model.User;

@RestController
@RequestMapping(value = "/project")
public class ProjectController extends BaseController {

	private ProjectService projectService;

	public ProjectController() {
	}

	@Autowired
	public ProjectController(final ProjectService projectService) {
		this.projectService = projectService;
	}

	@RolesAllowed({ "ROLE_USER" })
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ApiProejct createProject(final @RequestBody CreateProjectRequest request, Principal principal)
			throws ValidationException, UserNotFoundException {
		User requestingUser = loadUserFromPrincipal(principal);
		return projectService.createProject(requestingUser.getId(), request);
	}
	

	@RolesAllowed({ "ROLE_USER" })
	@RequestMapping(value = "/list/own", method = RequestMethod.GET)
	@ResponseBody
	public List<ApiProejct> createProject(Principal principal)
			throws ValidationException, UserNotFoundException {
		User requestingUser = loadUserFromPrincipal(principal);
		return projectService.getProjectListOwn(requestingUser.getId());
	}
}
