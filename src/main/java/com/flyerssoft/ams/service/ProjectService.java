package com.flyerssoft.ams.service;

import com.flyerssoft.ams.exception.NotAcceptableException;
import com.flyerssoft.ams.exception.NotFoundException;
import com.flyerssoft.ams.model.dto.CustomPageDto;
import com.flyerssoft.ams.model.dto.project.ProjectDto;
import com.flyerssoft.ams.model.dto.project.ProjectResponseDto;

/**
 * The project service.
 */
public interface ProjectService {

  /**
   * Create project.
   *
   * @param projectDto projectDto
   * @return project details
   */
  ProjectDto createProject(ProjectDto projectDto) throws NotAcceptableException;

  /**
   * Get all project.
   *
   * @return all project details
   */
  CustomPageDto readProject(
      int employeeId,
      int page,
      int offSet) throws NotFoundException;

  /**
   * Update project.
   *
   * @param projectId projectId
   */
  ProjectResponseDto updateProject(int projectId, ProjectDto projectDto) throws NotFoundException;

  /**
   * Update(map or unmap) employee with project.
   *
   * @param projectId projectId
   * @param employeeId employeeId
   */
  String assignOrRemoveEmployee(
      int projectId,
      int employeeId,
      boolean isAssign
  ) throws NotFoundException, IllegalStateException;

  /**
   * Delete project.
   *
   * @param projectId projectId
   */
  void deleteProject(int projectId) throws NotFoundException;
}
