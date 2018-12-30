package com.example.ppmt.services.springdatajpa;

import com.example.ppmt.domain.Backlog;
import com.example.ppmt.domain.Project;
import com.example.ppmt.domain.ProjectTask;
import com.example.ppmt.exceptions.ProjectNotFoundException;
import com.example.ppmt.repositories.BacklogRepository;
import com.example.ppmt.repositories.ProjectRepository;
import com.example.ppmt.repositories.ProjectTaskRepository;
import com.example.ppmt.services.ProjectService;
import com.example.ppmt.services.ProjectTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskJpaService implements ProjectTaskService {

	private BacklogRepository backlogRepository;
	private ProjectTaskRepository projectTaskRepository;
	private ProjectRepository projectRepository;
	private ProjectService projectService;

	public ProjectTaskJpaService(BacklogRepository backlogRepository,
	                             ProjectTaskRepository projectTaskRepository,
	                             ProjectRepository projectRepository, ProjectService projectService) {
		this.backlogRepository = backlogRepository;
		this.projectTaskRepository = projectTaskRepository;
		this.projectRepository = projectRepository;
		this.projectService = projectService;
	}

	@Override
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String userName) {
		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, userName).getBacklog();
		projectTask.setBacklog(backlog);
		Integer backlogSequence = backlog.getPTSequence();
		backlogSequence++;
		backlog.setPTSequence(backlogSequence);

		projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
		projectTask.setProjectId(projectIdentifier);

		if (projectTask.getStatus() == null || projectTask.getStatus().isEmpty()) {
			projectTask.setStatus("TO_DO");
		}
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		return projectTaskRepository.save(projectTask);
	}

	@Override
	public List<ProjectTask> findBacklogById(String id, String userName) {
		projectService.findProjectByIdentifier(id, userName);

		return projectTaskRepository.findByProjectIdOrderByPriority(id);
	}

	@Override
	public ProjectTask findProjectTaskByProjectSequence(String backlogId, String sequence, String userName) {

		projectService.findProjectByIdentifier(backlogId, userName);

		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task with id: '" + sequence + "' does not exist");
		}

		if (!projectTask.getProjectId().equals(backlogId)) {
			throw new ProjectNotFoundException("Project Task with id: '" + sequence + "' does not exist in project '"
					+ backlogId + "'");
		}

		return projectTask;
	}

	@Override
	public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlogId, String sequence,
	                                           String userName) {
		findProjectTaskByProjectSequence(backlogId, sequence, userName);
		return projectTaskRepository.save(updatedProjectTask);
	}

	@Override
	public void deleteProjectTaskByProjectSequence(String backlogId, String sequence, String userName) {
		ProjectTask projectTask = findProjectTaskByProjectSequence(backlogId, sequence, userName);
		projectTaskRepository.delete(projectTask);
	}
}
