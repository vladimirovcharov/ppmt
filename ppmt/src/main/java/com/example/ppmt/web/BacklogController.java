package com.example.ppmt.web;

import com.example.ppmt.domain.ProjectTask;
import com.example.ppmt.services.ProjectTaskService;
import com.example.ppmt.services.validation.MapValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

	private ProjectTaskService projectTaskService;
	private MapValidationErrorService mapValidationErrorService;

	public BacklogController(ProjectTaskService projectTaskService, MapValidationErrorService mapValidationErrorService) {
		this.projectTaskService = projectTaskService;
		this.mapValidationErrorService = mapValidationErrorService;
	}

	@PostMapping("/{id}")
	public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
	                                                 BindingResult result, @PathVariable String id,
	                                                 Principal principal) {
		ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);
		if (errorMap != null) return errorMap;

		ProjectTask addedProjectTask = projectTaskService.addProjectTask(id, projectTask, principal.getName());

		return new ResponseEntity<>(addedProjectTask, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public Iterable<ProjectTask> getProjectBacklog(@PathVariable String id, Principal principal) {
		return projectTaskService.findBacklogById(id, principal.getName());
	}

	@GetMapping("/{id}/{projectTaskId}")
	public ResponseEntity<?> getProjectTask(@PathVariable String id, @PathVariable String projectTaskId,
	                                        Principal principal) {
		ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(id, projectTaskId,
				principal.getName());

		return new ResponseEntity<>(projectTask, HttpStatus.OK);
	}

	@PatchMapping("/{id}/{projectTaskId}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
	                                        @PathVariable String id, @PathVariable String projectTaskId,
	                                           Principal principal) {
		ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);
		if (errorMap != null) return errorMap;

		ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, id, projectTaskId,
				principal.getName());

		return new ResponseEntity<>(updatedProjectTask, HttpStatus.OK);
	}

	@DeleteMapping("/{id}/{projectTaskId}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String id, @PathVariable String projectTaskId,
	                                           Principal principal) {
		projectTaskService.deleteProjectTaskByProjectSequence(id, projectTaskId, principal.getName());

		return new ResponseEntity<>("Project Task with ID: '" + projectTaskId + "' was deleted", HttpStatus.OK);
	}
}
