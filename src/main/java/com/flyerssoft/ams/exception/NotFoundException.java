package com.flyerssoft.ams.exception;

/**
 * Exception thrown when user try to access resource which is
 * not available.
 */
public class NotFoundException extends AmsException {

  public static final String USER_NOT_FOUND = "User with ID %d not found";

  public static final String PROJECT_NOT_FOUND = "Project with ID %d not found";

  public static final String EMPLOYEE_NOT_FOUND = "Employee with ID %d not found";

  public NotFoundException(String message) {
    super(message);
  }

}
