package com.example.ppmt.services;

import com.example.ppmt.domain.ProjectTask;

import java.util.List;

public interface ProjectTaskService {
	ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String userName);

	List<ProjectTask> findBacklogById(String id, String userName);

	ProjectTask findProjectTaskByProjectSequence(String backlogId, String sequence, String userName);

	ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlogId, String sequence, String userName);

	void deleteProjectTaskByProjectSequence(String backlogId, String sequence, String userName);
}
