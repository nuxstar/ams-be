package com.flyerssoft.ams.security;

import com.flyerssoft.ams.model.dto.AmsErrorResponse;
import com.flyerssoft.ams.utility.AmsUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * custom Access Denied Error Class for spring security.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * custom Access Denied Error Class.
   *
   * @param request               request.
   * @param response              response.
   * @param accessDeniedException accessDeniedException.
   * @throws IOException      IOException.
   * @throws ServletException ServletException.
   */
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) throws IOException, ServletException {
    var errorResponse = new AmsErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        "Access Denied"
    );
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(AmsUtility.convertObjectToJson(errorResponse));
  }
}
