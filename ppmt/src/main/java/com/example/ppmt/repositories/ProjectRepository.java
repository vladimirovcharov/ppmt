package com.example.ppmt.repositories;

import com.example.ppmt.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {

	Project findProjectByIdentifier(String projectId);

	Iterable<Project> findAllByProjectLeader(String userName);
}
