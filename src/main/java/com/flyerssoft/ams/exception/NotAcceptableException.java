package com.flyerssoft.ams.exception;

/**
 * The Duplicate mapping exception class.
 *
 */
public class NotAcceptableException extends AmsException {

  public static final String DUPLICATE_PROJECT_NOT_ACCEPTABLE = "Duplicate project name: %s";

  public static final String EMPLOYEE_ALREADY_MAPPED =

      "Employee with ID %d is already mapped to this project";
  public static final String EMPLOYEE_NOT_PRESENT =

      "Employee with ID %d not present in the project";

  public NotAcceptableException(String message) {
    super(message);
  }
}
