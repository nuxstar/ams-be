package com.flyerssoft.ams.service.impl;

import com.flyerssoft.ams.exception.BadRequestException;
import com.flyerssoft.ams.exception.NotFoundException;
import com.flyerssoft.ams.mapper.EmployeeMapper;
import com.flyerssoft.ams.mapper.EntitlementMapper;
import com.flyerssoft.ams.model.dto.EmployeeDto;
import com.flyerssoft.ams.model.dto.EntitlementDto;
import com.flyerssoft.ams.model.dto.LoginResponse;
import com.flyerssoft.ams.model.entity.Employee;
import com.flyerssoft.ams.model.entity.Entitlement;
import com.flyerssoft.ams.model.entity.UserGroupPermission;
import com.flyerssoft.ams.model.repository.EmployeeRepository;
import com.flyerssoft.ams.model.repository.EntitlementRepository;
import com.flyerssoft.ams.model.repository.UserGroupPermissionRepository;
import com.flyerssoft.ams.security.JwtService;
import com.flyerssoft.ams.security.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private EntitlementRepository entitlementRepository;

  @Mock
  private UserGroupPermissionRepository userGroupPermissionRepository;

  @Mock
  private EntitlementMapper entitlementMapper;

  @Mock
  private EmployeeMapper employeeMapper;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthServiceImpl authService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(authService, "superAdminPassword", "testPassword");
  }

  @Test
  public void login_Success() throws ParseException {
    // Mock input data
    String authorizationHeader = "Basic dXNlcjpwYXNzd29yZA=="; // "user:password"

    // Mock employee data
    Employee superAdmin = new Employee();
    superAdmin.setEmployeeId(1);
    superAdmin.setEmployeeEmail("user");
    superAdmin.setEmployeeName("Super Admin");

    // Mock user group permission data
    UserGroupPermission groupPermission1 = new UserGroupPermission();
    groupPermission1.setId(1);
    groupPermission1.setName("Group 1");

    // Mock entitlement data
    Entitlement entitlement1 = new Entitlement();
    entitlement1.setId(1);
    entitlement1.setName("Entitlement 1");

    // Mock token and expiration time
    String token = "mockToken";
    long expirationTime = System.currentTimeMillis() + 3600000; // 1 hour

    // Mock DTOs
    List<EntitlementDto> entitlementDtos = Collections.singletonList(
        new EntitlementDto(
            "test",
            "Get",
            "/"
        )
    );
    EmployeeDto superAdminDto = new EmployeeDto(
        1,
        "Super Admin",
        "user",
        "",
        "",
        "chennai",
        "superAdmin",
        "super-admin"
        );

    when(employeeRepository.findById(1)).thenReturn(Optional.of(superAdmin));
    when(employeeMapper.toUser(superAdmin)).thenReturn(new User());
    when(jwtService.generateToken(any(), any())).thenReturn(token);
    when(jwtService.extractExpiration(any())).thenReturn(new Date(expirationTime));
    when(entitlementMapper.toDto(Collections.singletonList(entitlement1))).thenReturn(entitlementDtos);
    when(employeeMapper.toDto(superAdmin)).thenReturn(superAdminDto);
    when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

    passwordEncoder.matches("asdf", "asdf");

    // Call the login method
    LoginResponse loginResponse = authService.login(authorizationHeader);

    // Verify the response
    assertNotNull(loginResponse);
    assertEquals(superAdminDto, loginResponse.profile());
    assertEquals(expirationTime, loginResponse.expiresIn());
    assertEquals(token, loginResponse.accessToken());

    // Verify the method invocations
    verify(employeeRepository).findById(1);
    verify(employeeMapper).toUser(superAdmin);
    verify(jwtService).extractExpiration(token);
    verify(employeeMapper).toDto(superAdmin);
  }

  @Test
  public void login_InvalidCredentials() {
    // Mock input data
    String authorizationHeader = "Basic dXNlcjpwYXNzd29yZA=="; // "user:password"

    // Mock employee data
    Employee superAdmin = new Employee();
    superAdmin.setEmployeeId(1);
    superAdmin.setEmployeeEmail("admin");
    superAdmin.setEmployeeName("Super Admin");

    when(employeeRepository.findById(1)).thenReturn(Optional.of(superAdmin));

    // Call the login method and assert that it throws BadRequestException
    assertThrows(BadRequestException.class, () -> authService.login(authorizationHeader));

    // Verify the method invocations
    verify(employeeRepository).findById(1);
  }

  @Test
  public void login_EmployeeNotFound() {
    // Mock input data
    String authorizationHeader = "Basic dXNlcjpwYXNzd29yZA=="; // "user:password"

    when(employeeRepository.findById(1)).thenReturn(Optional.empty());

    // Call the login method and assert that it throws NotFoundException
    assertThrows(NotFoundException.class, () -> authService.login(authorizationHeader));

    // Verify the method invocations
    verify(employeeRepository).findById(1);
    verifyNoInteractions(employeeMapper, jwtService, entitlementRepository, userGroupPermissionRepository);
  }
}
