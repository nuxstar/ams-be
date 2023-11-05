package com.flyerssoft.ams.rest.controller;

import com.flyerssoft.ams.model.dto.AmsResponse;
import com.flyerssoft.ams.model.dto.LoginResponse;
import com.flyerssoft.ams.service.AuthService;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller class that handles authentication-related HTTP requests
 * and manages the REST API endpoints for authentication.
 */
@RestController
@RequestMapping("/v1")
public class AuthController {

  /**
   * The service class responsible for handling
   * authentication business logic.
   */
  @Autowired
  private AuthService authService;

  /**
   * Handles the HTTP GET request for user authentication.
   *
   * @param authCode the authentication code to be used for user login
   * @return the response containing the authentication result
   * @throws ParseException if there is an error parsing the authentication code
   */
  @GetMapping("/authenticate")
  public AmsResponse<LoginResponse> userAuthentication(
      @RequestParam String authCode
  ) throws ParseException {
    return new AmsResponse<>(
        HttpStatus.OK.value(),
        true,
        authService.authenticate(authCode)
    );
  }

  /**
   * Handles the HTTP GET request for user authentication.
   *
   * @param authorizationHeader  authorization header contains basic auth.
   * @return the response containing the authentication result
   * @throws ParseException if there is an error parsing the authentication code
   */
  @PostMapping("/login")
  public AmsResponse<LoginResponse> superAdminLogin(
      @RequestHeader("Authorization") String authorizationHeader
  ) throws ParseException {
    return new AmsResponse<>(
        HttpStatus.OK.value(),
        true,
        authService.login(authorizationHeader)
    );
  }
}
