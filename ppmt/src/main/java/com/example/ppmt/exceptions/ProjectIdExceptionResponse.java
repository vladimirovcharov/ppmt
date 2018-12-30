package com.example.ppmt.exceptions;

public class ProjectIdExceptionResponse {

	private String projectId;

	ProjectIdExceptionResponse(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
