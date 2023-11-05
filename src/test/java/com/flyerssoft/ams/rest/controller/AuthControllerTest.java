package com.flyerssoft.ams.rest.controller;

import com.flyerssoft.ams.model.dto.EmployeeDto;
import com.flyerssoft.ams.model.dto.LoginResponse;
import com.flyerssoft.ams.security.JwtService;
import com.flyerssoft.ams.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @MockBean
  JwtService jwtService;

  @InjectMocks
  private AuthController authController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void superAdminLogin_success() throws Exception {

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
    long expirationTime = System.currentTimeMillis() + 3600000; // 1hour
    String mockToken = "mockToken";


    var loginResponse = new LoginResponse(superAdminDto, expirationTime, mockToken, null, null);
    when(authService.login(any())).thenReturn(loginResponse);

    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders
            .post("/v1/login")
            .header("Authorization", "mockAuthorization")
    ).andExpect(status().isOk()).andReturn();
  }

  @Test
  void superAdminLogin_invalidParams() throws Exception {
    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders
            .post("/v1/login")
    ).andExpect(status().isBadRequest()).andReturn();
  }
}