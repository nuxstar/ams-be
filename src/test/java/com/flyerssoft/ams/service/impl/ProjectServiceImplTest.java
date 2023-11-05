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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Test Case For Project Service Implementation")
@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

  @MockBean
  private ProjectRepository projectRepository;

  @MockBean
  private EmployeeRepository employeeRepository;

  @MockBean
  private ProjectMapper projectMapper;

  @InjectMocks
  private ProjectServiceImpl projectService;

  private ProjectDto projectDto;

  private Project projectRequest;
  private Project project;

  private Employee employee;

  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @BeforeEach
  void init(){
    projectDto = new ProjectDto();
    projectDto.setProjectId(1);
    projectDto.setProjectName("Jio");
    project = new Project();
    project.setProjectId(1);
    project.setProjectName("Jio");
    project.setEmployeesCount((short) 1);
    employee = new Employee();
    employee.setEmployeeId(1);
    projectRequest = new Project();
    projectRequest.setProjectId(1);
    projectRequest.setProjectName("Jio");
  }



  @DisplayName("Test Case For Create Project")
  @Test
  public void testCreateProject_Success() {
    ProjectDto projectDto = new ProjectDto();
    projectDto.setProjectName("Sample Project");
    Project projectEntity = new Project();
    projectEntity.setProjectId(1);
    projectEntity.setProjectName("Sample Project");
    when(projectMapper.toEntity(projectDto)).thenReturn(projectEntity);
    when(projectMapper.toDto(projectEntity)).thenReturn(projectDto);
    when(projectRepository.save(projectEntity)).thenReturn(projectEntity);
    ProjectDto createdProject = projectService.createProject(projectDto);
    verify(projectMapper).toEntity(projectDto);
    verify(projectRepository).save(projectEntity);
    verify(projectMapper).toDto(projectEntity);
    assertNotNull(createdProject);
    assertEquals(projectDto.getProjectName(), createdProject.getProjectName());

  }

  @DisplayName("Test Case For Duplicate Mapping When Creating Project")
  @Test
  public void testCreateProject_DuplicateProjectName() {
    ProjectDto projectDto = new ProjectDto();
    projectDto.setProjectName("Sample Project");
    Project projectEntity = new Project();
    projectEntity.setProjectId(1);
    projectEntity.setProjectName("Sample Project");
    when(projectMapper.toEntity(projectDto)).thenReturn(projectEntity);
    when(projectRepository.save(projectEntity)).thenThrow(DataIntegrityViolationException.class);
    assertThrows(NotAcceptableException.class, () -> {
      projectService.createProject(projectDto);
    });
    verify(projectMapper).toEntity(projectDto);
    verify(projectRepository).save(projectEntity);
  }

  @DisplayName("Test Case For Get All Projects")
  @Test
  void testReadProject_AllProjects_Success() {
    int employeeId = 0;
    int page = 0;
    int offset = 10;
    Pageable pageable = PageRequest.of(page, offset);
    Page<Project> projectPage = mock(Page.class);
    when(projectRepository.findAll(pageable)).thenReturn(projectPage);
    CustomPageDto expectedPageDto = new CustomPageDto();
    when(projectMapper.toPageDto(projectPage)).thenReturn(expectedPageDto);
    CustomPageDto result = projectService.readProject(employeeId, page, offset);
    verify(projectRepository).findAll(pageable);
    verify(projectMapper).toPageDto(projectPage);
    assertNotNull(result);
    assertEquals(expectedPageDto, result);
  }


  @DisplayName("Test Case For Get All Projects Under Employee")
  @Test
  void testReadProject_ProjectsByEmployee_Success() {
    int employeeId = 1;
    int page = 0;
    int offset = 10;
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    Pageable pageable = PageRequest.of(page, offset);
    Page<Project> projectPage = mock(Page.class);
    when(projectRepository.findAllByEmployeeId(employeeId, pageable)).thenReturn(projectPage);
    CustomPageDto expectedPageDto = new CustomPageDto();
    when(projectMapper.toPageDto(projectPage)).thenReturn(expectedPageDto);
    CustomPageDto result = projectService.readProject(employeeId, page, offset);
    verify(employeeRepository).findById(employeeId);
    verify(projectRepository).findAllByEmployeeId(employeeId, pageable);
    verify(projectMapper).toPageDto(projectPage);
    assertNotNull(result);
    assertEquals(expectedPageDto, result);
  }

  @DisplayName("Test Case For Get All Projects With Invalid Input")
  @Test
  void testReadProject_InvalidEmployeeId_ExceptionThrown() {
    int employeeId = -1;
    int page = 0;
    int offset = 10;
    assertThrows(IllegalArgumentException.class, () -> {
      projectService.readProject(employeeId, page, offset);
    });
  }

  @DisplayName("Test Case For Get All Projects Employee Not Found")
  @Test
  void testReadProject_EmployeeNotFound_ExceptionThrown() {
    int employeeId = 1;
    int pageNo = 0;
    int offSet = 10;

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> projectService.readProject(employeeId, pageNo, offSet));

    assertEquals("Employee with ID 1 not found", exception.getMessage());
    verify(employeeRepository, times(1)).findById(employeeId);
    verify(projectRepository, never()).findAll(any(Pageable.class));
    verify(projectMapper, never()).toPageDto(any(Page.class));
  }

  @DisplayName("Test Case For Update Project")
  @Test
  void updateProject_ValidIdAndDto_ProjectUpdatedSuccessfully() {
    int projectId = 1;
    ProjectDto projectDto = new ProjectDto();
    projectDto.setProjectName("New Project Name");

    Project project = new Project();
    project.setProjectName("Old Project Name");

    Project updatedProject = new Project();
    updatedProject.setProjectName("New Project Name");

    short id =1;
    ProjectResponseDto expectedResponse = new ProjectResponseDto(1,"New Project Name",id);

    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(projectRepository.save(project)).thenReturn(updatedProject);
    when(projectMapper.toResponseDto(updatedProject)).thenReturn(expectedResponse);

    ProjectResponseDto response = projectService.updateProject(projectId, projectDto);

    assertEquals(expectedResponse, response);
    assertEquals("New Project Name", project.getProjectName());
    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, times(1)).save(project);
    verify(projectMapper, times(1)).toResponseDto(updatedProject);
  }

  @DisplayName("Test Case For Update Project IllegalArgument")
  @Test
  void updateProject_InvalidId_ThrowIllegalArgumentException() {
    int projectId = 0;
    ProjectDto projectDto = new ProjectDto();

    assertThrows(IllegalArgumentException.class,
        () -> projectService.updateProject(projectId, projectDto));

    verify(projectRepository, never()).findById(anyInt());
    verify(projectRepository, never()).save(any());
  }

  @DisplayName("Test Case For Update Project Not Found")
  @Test
  void updateProject_NonexistentProject_ThrowNotFoundException() {
    int projectId = 1;
    ProjectDto projectDto = new ProjectDto();

    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> projectService.updateProject(projectId, projectDto));

    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, never()).save(any());
  }

  @DisplayName("Test Case For Update Project Duplicate Project Name")
  @Test
  void updateProject_DuplicateProjectName_ThrowNotAllowedException() {
    int projectId = 1;
    ProjectDto projectDto = new ProjectDto();
    projectDto.setProjectName("Duplicate Project Name");

    Project project = new Project();
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(projectRepository.save(project)).thenThrow(DataIntegrityViolationException.class);

    assertThrows(NotAcceptableException.class,
        () -> projectService.updateProject(projectId, projectDto));

    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, times(1)).save(project);
  }

  @DisplayName("Test Case For Map  Employee To Project")
  @Test
  void assignEmployee_ValidIds_AssignSuccessfully() {

    int projectId = 1;
    int employeeId = 1;
    boolean isRemove = false;

    Project project = new Project();
    project.setProjectId(projectId);
    project.setEmployees(new ArrayList<>());

    Employee employee = new Employee();
    employee.setEmployeeId(employeeId);

    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(projectRepository.save(project)).thenReturn(project);

    String expectedMessage = "Employee with ID " + employeeId + " is mapped to project with ID " + projectId + " successfully";
    String result = projectService.assignOrRemoveEmployee(projectId, employeeId, isRemove);

    assertEquals(expectedMessage, result);
    assertTrue(project.getEmployees().contains(employee));
    assertEquals(1, project.getEmployeesCount());
    verify(projectRepository, times(1)).findById(projectId);
    verify(employeeRepository, times(1)).findById(employeeId);
    verify(projectRepository, times(1)).save(project);

  }

  @DisplayName("Test Case For Invalid Input For Update Project")
  @Test
  void testAssignEmployeeToProject_InvalidIds_ExceptionThrown() {
    int projectId = -1;
    int employeeId = 0;
    boolean isRemove = false;

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> projectService.assignOrRemoveEmployee(projectId, employeeId, isRemove));

    assertEquals("Project ID and employee ID must be positive integers", exception.getMessage());
    verify(projectRepository, never()).findById(anyInt());
    verify(employeeRepository, never()).findById(anyInt());
    verify(projectRepository, never()).save(any());
  }

  @DisplayName("Test Case For Update Project Project Not Found")
  @Test
  void testAssignEmployeeToProject_ProjectNotFound_ExceptionThrown() {
    int projectId = 1;
    int employeeId = 1;
    boolean isRemove = false;

    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> projectService.assignOrRemoveEmployee(projectId, employeeId, isRemove));

    assertEquals("Project with ID 1 not found", exception.getMessage());
    verify(projectRepository, times(1)).findById(projectId);
    verify(employeeRepository, never()).findById(anyInt());
    verify(projectRepository, never()).save(any());
  }

  @DisplayName("Test Case For Delete Project")
  @Test
  void deleteProject_ValidId_ProjectDeletedSuccessfully() {
    int projectId = 1;

    Project project = new Project();
    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

    projectService.deleteProject(projectId);

    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, times(1)).delete(project);
  }

  @DisplayName("Test Case For Invalid Input In Delete Project")
  @Test
  void deleteProject_InvalidId_ThrowIllegalArgumentException() {
    int projectId = 0;

    assertThrows(IllegalArgumentException.class,
        () -> projectService.deleteProject(projectId));

    verify(projectRepository, never()).findById(anyInt());
    verify(projectRepository, never()).delete(any());
  }

  @DisplayName("Test Case For Project Not Found")
  @Test
  void deleteProject_NonexistentProject_ThrowNotFoundException() {
    int projectId = 1;
    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class,
        () -> projectService.deleteProject(projectId));
    verify(projectRepository, times(1)).findById(projectId);
    verify(projectRepository, never()).delete(any());
  }

}