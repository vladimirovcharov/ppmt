package com.example.ppmt.web;

import com.example.ppmt.domain.Project;
import com.example.ppmt.services.ProjectService;
import com.example.ppmt.services.validation.MapValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

	private final ProjectService projectService;
	private final MapValidationErrorService mapValidationErrorService;

	public ProjectController(ProjectService projectService, MapValidationErrorService mapValidationErrorService) {
		this.projectService = projectService;
		this.mapValidationErrorService = mapValidationErrorService;
	}

	@PostMapping
	public ResponseEntity<?> createProject(@Valid @RequestBody Project project, BindingResult result,
	                                       Principal principal) {
		ResponseEntity<?> errors = mapValidationErrorService.validateMapError(result);
		if (errors != null) {
			return errors;
		}
		Project createdProject = projectService.saveOrUpdate(project, principal.getName());
		return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
	}

	@GetMapping("/{projectIdentifier}")
	public ResponseEntity<?> getProjectByIdentifier(@PathVariable String projectIdentifier, Principal principal) {
		Project project = projectService.findProjectByIdentifier(projectIdentifier, principal.getName());
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<Project> getAllProjects(Principal principal) {
		return projectService.findAll(principal.getName());
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
		projectService.deleteProjectByIdentifier(projectId, principal.getName());
		return new ResponseEntity<>("Project with ID: '" + projectId + "' was deleted", HttpStatus.OK);
	}

}
