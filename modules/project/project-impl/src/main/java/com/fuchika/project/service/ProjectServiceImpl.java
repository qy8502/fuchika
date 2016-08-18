package com.fuchika.project.service;

import java.util.ArrayList;
import java.util.List;

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
import com.fuchika.project.model.ProjectFollowed;
import com.fuchika.project.model.ProjectLiked;
import com.fuchika.project.repository.ProjectFollowedRepository;
import com.fuchika.project.repository.ProjectLikedRepository;
import com.fuchika.project.repository.ProjectRepository;
import com.fuchika.service.BaseService;
import com.fuchika.user.api.ApiUserSimple;
import com.fuchika.user.exception.UserNotFoundException;
import com.fuchika.user.service.UserService;

@Service
public class ProjectServiceImpl extends BaseService implements ProjectService {

	private Logger LOG = LoggerFactory.getLogger(ProjectService.class);

	private ProjectRepository projectRepository;
	private ProjectLikedRepository projectLikedRepository;
	private ProjectFollowedRepository projectFollowedRepository;
	private UserService userService;

	@Autowired
	public ProjectServiceImpl(final UserService userService, final ProjectRepository projectRepository,
			final ProjectLikedRepository projectLikedRepository,
			final ProjectFollowedRepository projectFollowedRepository, Validator validator) {
		super(validator);
		this.userService = userService;
		this.projectRepository = projectRepository;
		this.projectLikedRepository = projectLikedRepository;
		this.projectFollowedRepository = projectFollowedRepository;
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

	public List<ApiProejct> getProjectListOwn(String userId) throws UserNotFoundException {
		List<ApiProejct> result = new ArrayList<ApiProejct>();
		List<Project> projects = projectRepository.findByOwner(userId);
		for (Project project : projects) {
			result.add(getProjectListItem(project, userId));
		}
		return result;
	}

	public ApiProejct getProjectListItem(Project project, String userId) throws UserNotFoundException {
		ApiUserSimple owner = userService.getUserSimple(project.getOwner());
		List<ApiUserSimple> partners = userService.getUserSimpleList(project.getPartners());
		long likedCount = projectLikedRepository.countByProject(project.getId());
		long followedCount = projectFollowedRepository.countByProject(project.getId());
		boolean liked = projectLikedRepository.findById(new ProjectLiked(project.getId(), userId).getId()) != null;
		boolean followed = projectFollowedRepository
				.findById(new ProjectFollowed(project.getId(), userId).getId()) != null;
		return new ApiProejct(project, owner, partners, likedCount, followedCount, liked, followed);
	}

}