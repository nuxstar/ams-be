package com.flyerssoft.ams.service;

import com.flyerssoft.ams.model.dto.LoginResponse;
import java.text.ParseException;

/**
 * The auth service.
 */
public interface AuthService {

  /**
   * login response.
   *
   * @param authCode authCode
   * @return login response dto
   * @throws ParseException parse exception
   */
  LoginResponse authenticate(String authCode) throws ParseException;

  LoginResponse login(String authorizationHeader) throws ParseException;
}
