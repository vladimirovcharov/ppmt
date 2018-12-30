package com.example.ppmt.repositories;

import com.example.ppmt.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
	List<ProjectTask> findByProjectIdOrderByPriority(String id);

	ProjectTask findByProjectSequence(String sequence);
}
