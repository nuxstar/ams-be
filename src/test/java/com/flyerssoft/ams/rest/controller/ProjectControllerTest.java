package com.flyerssoft.ams.rest.controller;

import com.flyerssoft.ams.model.dto.AmsResponse;
import com.flyerssoft.ams.model.dto.CustomPageDto;
import com.flyerssoft.ams.model.dto.project.ProjectDto;
import com.flyerssoft.ams.model.dto.project.ProjectResponseDto;
import com.flyerssoft.ams.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringJUnitConfig
class ProjectControllerTest {
  @InjectMocks
  private ProjectController projectController;

  @Mock
  private ProjectService projectService;

  public ProjectControllerTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createProject() {
    ProjectDto projectDto = new ProjectDto();
    projectDto.setProjectName("Test Project");
    ProjectDto createdProjectDto = new ProjectDto();
    createdProjectDto.setProjectId(1);
    createdProjectDto.setProjectName("Test Project");
    when(projectService.createProject(projectDto)).thenReturn(createdProjectDto);
    ResponseEntity<AmsResponse<ProjectDto>> response = projectController.createProject(projectDto);
    verify(projectService).createProject(projectDto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    AmsResponse<ProjectDto> responseBody = response.getBody();
    assertEquals(HttpStatus.CREATED.value(), responseBody.getStatusCode());
    assertTrue(responseBody.getResponse());
    assertEquals(createdProjectDto, responseBody.getData());
  }

  @Test
  void readProject() {
    Integer employeeId = 1;
    int page = 1;
    int offset = 10;
    CustomPageDto customPageDto = new CustomPageDto();
    when(projectService.readProject(employeeId, page, offset)).thenReturn(customPageDto);
    ResponseEntity<AmsResponse<CustomPageDto>> response = projectController.readProject(employeeId, page, offset);
    verify(projectService).readProject(employeeId, page, offset);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    AmsResponse<CustomPageDto> responseBody = response.getBody();
    assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
    assertTrue(responseBody.getResponse());
    assertEquals(customPageDto, responseBody.getData());
  }

  @Test
  void updateProjectById() {
    int projectId = 1;
    ProjectDto projectDto = new ProjectDto();
    projectDto.setProjectName("Updated Project");
    ProjectResponseDto updatedProjectResponseDto = new ProjectResponseDto(projectId, "Updated Project", (short) 1);
    when(projectService.updateProject(projectId, projectDto)).thenReturn(updatedProjectResponseDto);
    ResponseEntity<AmsResponse<ProjectResponseDto>> response = projectController.updateProjectById(projectId, projectDto);
    verify(projectService).updateProject(projectId, projectDto);
    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    AmsResponse<ProjectResponseDto> responseBody = response.getBody();
    assertEquals(HttpStatus.ACCEPTED.value(), responseBody.getStatusCode());
    assertTrue(responseBody.getResponse());
    assertEquals(updatedProjectResponseDto, responseBody.getData());
  }
  @Test
  void updateProject() {
    int projectId = 1;
    int employeeId = 2;
    boolean isRemove = true;
    String response = "Employee removed successfully";
    when(projectService.assignOrRemoveEmployee(projectId, employeeId, isRemove)).thenReturn(response);
    ResponseEntity<AmsResponse<String>> responseEntity = projectController.updateProject(projectId, employeeId, isRemove);
    verify(projectService).assignOrRemoveEmployee(projectId, employeeId, isRemove);
    assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    AmsResponse<String> responseBody = responseEntity.getBody();
    assertEquals(HttpStatus.ACCEPTED.value(), responseBody.getStatusCode());
    assertTrue(responseBody.getResponse());
    assertEquals(response, responseBody.getData());
  }

  @Test
  void deleteProject() {
    int projectId = 1;
    ResponseEntity<AmsResponse<String>> responseEntity = projectController.deleteProject(projectId);
    verify(projectService).deleteProject(projectId);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    AmsResponse<String> responseBody = responseEntity.getBody();
    assertEquals(HttpStatus.NO_CONTENT.value(), responseBody.getStatusCode());
    assertTrue(responseBody.getResponse());
    assertEquals("project " + projectId + " is deleted successfully", responseBody.getData());
  }
}