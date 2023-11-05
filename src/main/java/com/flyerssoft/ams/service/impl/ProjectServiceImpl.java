package com.flyerssoft.ams.service.impl;

import com.flyerssoft.ams.exception.IllegalArgumentException;
import com.flyerssoft.ams.exception.NotAcceptableException;
import com.flyerssoft.ams.exception.NotFoundException;
import com.flyerssoft.ams.mapper.ProjectMapper;
import com.flyerssoft.ams.model.dto.CustomPageDto;
import com.flyerssoft.ams.model.dto.project.ProjectDto;
import com.flyerssoft.ams.model.dto.project.ProjectResponseDto;
import com.flyerssoft.ams.model.entity.Employee;
import com.flyerssoft.ams.model.entity.Project;
import com.flyerssoft.ams.model.repository.EmployeeRepository;
import com.flyerssoft.ams.model.repository.ProjectRepository;
import com.flyerssoft.ams.service.ProjectService;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The project service implementation class.
 */
@Service
public class ProjectServiceImpl implements ProjectService {
  private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private ProjectMapper projectMapper;
  @Autowired
  private EmployeeRepository employeeRepository;

  /**
   * Create project details.
   *
   * @param projectDto projectDto projectDto
   * @return created project details
   */
  @Override
  public ProjectDto createProject(ProjectDto projectDto) {
    Project project = projectMapper.toEntity(projectDto);
    log.debug("Creating project: {}", projectDto.getProjectName());
    try {
      project = projectRepository.save(project);
      log.debug("Saving project: {}", projectDto.getProjectName());
      return projectMapper.toDto(project);
    } catch (DataIntegrityViolationException ex) {
      log.error("Duplicate project name: {}", projectDto.getProjectName());
      throw new NotAcceptableException(
          String.format(
              NotAcceptableException.DUPLICATE_PROJECT_NOT_ACCEPTABLE,
              projectDto.getProjectName()
          )
      )
          ;
    }
  }

  /**
   * To get projects under employee.
   *
   * @param employeeId employeeId
   * @param page       page
   * @param offset     offSet
   * @return project details
   */
  @Override
  public CustomPageDto readProject(int employeeId, int page, int offset) {
    if (employeeId < 0) {
      log.error("Invalid employee ID: {}", employeeId);
      throw new IllegalArgumentException(
          IllegalArgumentException.ILLEGAL_ARGUMENT_EXCEPTION_EMPLOYEE
      )
          ;
    } else if (page < 0) {
      log.error("Invalid page number: {}", page);
      throw new IllegalArgumentException(
          IllegalArgumentException.ILLEGAL_ARGUMENT_EXCEPTION_PAGE
      )
          ;
    } else if (offset < 0) {
      log.error("Invalid offset number: {}", offset);
      throw new IllegalArgumentException(
          IllegalArgumentException.ILLEGAL_ARGUMENT_EXCEPTION_OFFSET
      )
          ;
    }
    Pageable pageable = PageRequest.of(page, offset);
    if (employeeId == 0) {
      CustomPageDto allProjects = projectMapper.toPageDto(projectRepository.findAll(pageable));
      log.debug("Fetching all projects");
      return allProjects;
    } else {
      employeeRepository.findById(employeeId)
          .orElseThrow(() -> new NotFoundException(String.format(
              NotFoundException.EMPLOYEE_NOT_FOUND, employeeId)));
      Page<Project> projectList = projectRepository.findAllByEmployeeId(
          employeeId, pageable);
      log.debug("Fetching projects for employee ID: {}", employeeId);
      return projectMapper.toPageDto(projectList);
    }
  }

  /**
   * Update project details.
   *
   * @param projectId  projectId
   * @param projectDto projectDto
   * @return updated project details
   */
  @Override
  public ProjectResponseDto updateProject(int projectId, ProjectDto projectDto) {
    if (projectId <= 0) {
      log.error("Invalid project ID: {}", projectId);
      throw new IllegalArgumentException(
          IllegalArgumentException.ILLEGAL_ARGUMENT_EXCEPTION_PROJECT
      )
          ;
    }
    log.debug("Fetching project with ID: {}", projectId);
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new NotFoundException(
            String.format(
                NotFoundException.PROJECT_NOT_FOUND,
                projectId
            )
        )
        )
        ;
    if (Objects.nonNull(projectDto.getProjectName())) {
      project.setProjectName(projectDto.getProjectName());
    }
    try {
      Project projectResponse = projectRepository.save(project);
      log.debug("Updating project with ID: {}", projectId);
      return projectMapper.toResponseDto(projectResponse);
    } catch (DataIntegrityViolationException ex) {
      log.error("Duplicate project name: {}", projectDto.getProjectName());
      throw new NotAcceptableException(
          String.format(
              NotAcceptableException.DUPLICATE_PROJECT_NOT_ACCEPTABLE,
              projectDto.getProjectName()
          )
      )
          ;
    }
  }

  /**
   * To assign or remove the employee from a project.
   *
   * @param projectId  projectId
   * @param employeeId employeeId
   * @param isRemove   value
   * @return project with employee details
   */
  @Override
  public String assignOrRemoveEmployee(int projectId, int employeeId, boolean isRemove) {
    if (projectId <= 0 || employeeId <= 0) {
      log.error("Invalid project or employee ID: projectId={}, employeeId={}",
          projectId, employeeId);
      throw new IllegalArgumentException(
          IllegalArgumentException.ILLEGAL_ARGUMENT_EXCEPTION_PROJECT_AND_EMPLOYEE
      )
          ;
    }
    log.debug("Fetching project with ID: {}", projectId);
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new NotFoundException(
            String.format(NotFoundException.PROJECT_NOT_FOUND, projectId)));

    var isEmployeeExist = project.getEmployees().stream().anyMatch(
        e -> e.getEmployeeId() == employeeId);

    if (isEmployeeExist && !isRemove) {
      log.error("Employee with ID {} is already mapped to this project", employeeId);
      throw new NotAcceptableException(
          String.format(
              NotAcceptableException.EMPLOYEE_ALREADY_MAPPED,
              employeeId
          )
      )
          ;
    } else if (!isEmployeeExist && isRemove) {
      log.error("Employee with ID {} is not present in the project", employeeId);
      throw new NotAcceptableException(
          String.format(
              NotAcceptableException.EMPLOYEE_NOT_PRESENT,
              employeeId
          )
      )
          ;
    }
    List<Employee> employees = project.getEmployees();
    if (!isRemove) {
      Employee employee = employeeRepository.findById(employeeId)
          .orElseThrow(() -> new NotFoundException(
              String.format(NotFoundException.EMPLOYEE_NOT_FOUND, employeeId)));
      if (Objects.nonNull(employees)) {
        employees.add(employee);
      } else {
        employees = List.of(employee);
      }
      project.setEmployees(employees);
      project.setEmployeesCount((short) employees.size());
      projectRepository.save(project);
      log.debug("Mapping employee with ID {} to project with ID {}", employeeId, projectId);
      return "Employee with ID " + employeeId + " is mapped to "
          + "project with ID " + projectId + " successfully";
    } else {
      employees.removeIf(e -> e.getEmployeeId() == employeeId);
      project.setEmployees(employees);
      project.setEmployeesCount((short) employees.size());
      projectRepository.save(project);
      log.debug("Removing employee with ID {} from project with ID {}", employeeId, projectId);
      return "Employee with ID " + employeeId + " is"
          + " unmapped to project with ID " + projectId + " successfully";
    }
  }

  /**
   * Delete project details.
   *
   * @param projectId projectId
   */
  @Override
  public void deleteProject(int projectId) {
    if (projectId <= 0) {
      log.error("Invalid project ID: {}", projectId);
      throw new IllegalArgumentException(
          IllegalArgumentException.ILLEGAL_ARGUMENT_EXCEPTION_PROJECT
      )
          ;
    }
    log.debug("Fetching project with ID: {}", projectId);
    Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException(
        String.format(NotFoundException.PROJECT_NOT_FOUND, projectId)));
    projectRepository.delete(project);
    log.debug("Deleting project with ID: {}", projectId);
  }
}
