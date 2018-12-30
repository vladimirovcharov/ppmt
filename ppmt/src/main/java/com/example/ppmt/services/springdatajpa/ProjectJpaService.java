package com.example.ppmt.services.springdatajpa;

import com.example.ppmt.domain.Backlog;
import com.example.ppmt.domain.Project;
import com.example.ppmt.domain.User;
import com.example.ppmt.exceptions.ProjectIdException;
import com.example.ppmt.exceptions.ProjectNotFoundException;
import com.example.ppmt.repositories.BacklogRepository;
import com.example.ppmt.repositories.ProjectRepository;
import com.example.ppmt.repositories.UserRepository;
import com.example.ppmt.services.ProjectService;
import org.springframework.stereotype.Service;

@Service
public class ProjectJpaService implements ProjectService {

	private ProjectRepository projectRepository;
	private BacklogRepository backlogRepository;
	private UserRepository userRepository;

	public ProjectJpaService(ProjectRepository projectRepository, BacklogRepository backlogRepository,
	                         UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.backlogRepository = backlogRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Project saveOrUpdate(Project project, String userName) {

		if (project.getId() != null) {
			Project existingProject = projectRepository.findProjectByIdentifier(project.getIdentifier());

			if (existingProject != null && !existingProject.getProjectLeader().equals(userName)) {
				throw new ProjectNotFoundException("Project not found in your account");
			} else if (existingProject == null) {
				throw new ProjectNotFoundException("Project with ID: '" + project.getIdentifier()
						+ "' cannot be updated because it does not exist");
			}
		}

		try {
			User user = userRepository.findByUsername(userName);
			if (user != null) {
				project.setUser(user);
				project.setProjectLeader(user.getUsername());
			}

			String projectIdentifier = project.getIdentifier().toUpperCase();
			project.setIdentifier(projectIdentifier);

			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectId(projectIdentifier);
			} else {
				project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
			}

			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project ID '" + project.getIdentifier() + "' already exists");
		}

	}

	@Override
	public Project findProjectByIdentifier(String projectId, String userName) {
		Project project = projectRepository.findProjectByIdentifier(projectId.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exist");
		}
		if (!project.getProjectLeader().equals(userName)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}
		return project;
	}

	@Override
	public Iterable<Project> findAll(String userName) {
		return projectRepository.findAllByProjectLeader(userName);
	}

	@Override
	public void deleteProjectByIdentifier(String projectId, String userName) {
		projectRepository.delete(findProjectByIdentifier(projectId, userName));
	}

}
