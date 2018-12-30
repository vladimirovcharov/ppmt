package com.example.ppmt.services;

import com.example.ppmt.domain.Project;

public interface ProjectService {
	Project saveOrUpdate(Project project, String userName);
	Project findProjectByIdentifier(String projectId, String userName);
	Iterable<Project> findAll(String userName);
	void deleteProjectByIdentifier(String identifier, String userName);
}
