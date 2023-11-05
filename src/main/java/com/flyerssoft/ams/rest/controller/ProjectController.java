package com.flyerssoft.ams.rest.controller;

import com.flyerssoft.ams.model.dto.AmsResponse;
import com.flyerssoft.ams.model.dto.CustomPageDto;
import com.flyerssoft.ams.model.dto.project.ProjectDto;
import com.flyerssoft.ams.model.dto.project.ProjectResponseDto;
import com.flyerssoft.ams.service.ProjectService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The project controller class.
 */
@RestController
@RequestMapping("/v1")
public class ProjectController {

  private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

  @Autowired
  ProjectService projectService;

  /**
   * Create project.
   *
   * @param projectDto projectDto
   * @return created project details
   */
  @PreAuthorize("hasAuthority('AMS_PROJECT_CREATE')")
  @PostMapping("/projects")
  public ResponseEntity<AmsResponse<ProjectDto>> createProject(
      @RequestBody @Valid ProjectDto projectDto) {
    log.debug("Creating project with name: {}", projectDto.getProjectName());
    return new ResponseEntity<>(new AmsResponse<>(
        HttpStatus.CREATED.value(),
        true,
        projectService.createProject(projectDto)),
        HttpStatus.CREATED);
  }

  /**
   * Get projects based on employee id using pagination.
   *
   * @param employeeId employeeId
   * @param page       pageNo
   * @param offset     offSet
   * @return all projects
   */
  @PreAuthorize("hasAuthority('AMS_PROJECT_READ')")
  @GetMapping("/projects")
  public ResponseEntity<AmsResponse<CustomPageDto>> readProject(
      @RequestParam(name = "employeeId", required = false) Integer employeeId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "offset", defaultValue = "10") int offset) {
    log.debug("Reading projects with employee ID: {}", employeeId);
    int id = Optional.ofNullable(employeeId).orElse(0);
    CustomPageDto response = projectService.readProject(id, page, offset);
    return new ResponseEntity<>(new AmsResponse<>(
        HttpStatus.OK.value(), true, response), HttpStatus.OK);
  }

  /**
   * Update project details.
   *
   * @param projectId  projectId
   * @param projectDto projectDto
   * @return updated project details
   */
  @PreAuthorize("hasAuthority('AMS_PROJECT_UPDATE')")
  @PutMapping("/projects/{projectId}")
  public ResponseEntity<AmsResponse<ProjectResponseDto>> updateProjectById(
      @PathVariable int projectId,
      @RequestBody @Valid ProjectDto projectDto) {
    log.debug("Updating project with name: {}", projectDto.getProjectName());
    return new ResponseEntity<>(new AmsResponse<>(
        HttpStatus.ACCEPTED.value(), true,
        projectService.updateProject(projectId, projectDto)), HttpStatus.ACCEPTED);
  }

  /**
   * Map / un map employee with project.
   *
   * @param projectId  projectId
   * @param employeeId employeeId
   * @return updated project details
   */
  @PreAuthorize("hasAuthority('AMS_PROJECT_UPDATE')")
  @PutMapping("/projects/{projectId}/employees/{employeeId}")
  public ResponseEntity<AmsResponse<String>> updateProject(
      @PathVariable int projectId,
      @PathVariable int employeeId,
      @RequestParam(name = "isRemove", defaultValue = "false") boolean isRemove) {
    log.debug("Updating project with ID {} and employee ID {}", projectId, employeeId);
    String response = projectService.assignOrRemoveEmployee(projectId, employeeId, isRemove);
    return new ResponseEntity<>(new AmsResponse<>(
        HttpStatus.ACCEPTED.value(), true, response), HttpStatus.ACCEPTED);
  }

  /**
   * Delete project.
   *
   * @param projectId projectId
   * @return successfully message
   */
  @PreAuthorize("hasAuthority('AMS_PROJECT_DELETE')")
  @DeleteMapping("/projects/{projectId}")
  public ResponseEntity<AmsResponse<String>> deleteProject(@PathVariable int projectId) {
    log.debug("Deleting project with ID: {}", projectId);
    projectService.deleteProject(projectId);
    return new ResponseEntity<>(new AmsResponse<>(
        HttpStatus.NO_CONTENT.value(),
        true,
        "project " + projectId + " is deleted successfully"), HttpStatus.NO_CONTENT);
  }
}
