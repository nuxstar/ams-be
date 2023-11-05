package com.flyerssoft.ams.exception;

/**
 * The illegal argument exception class.
 */
public class IllegalArgumentException extends AmsException {

  public static final String ILLEGAL_ARGUMENT_EXCEPTION_EMPLOYEE =

      "Employee id must be integer value";

  public static  final String ILLEGAL_ARGUMENT_EXCEPTION_PAGE =

      "page number must be integer value";

  public static  final String ILLEGAL_ARGUMENT_EXCEPTION_OFFSET =

      "offset number must be integer value";

  public static final String ILLEGAL_ARGUMENT_EXCEPTION_PROJECT =

      "Project id must be integer value";

  public static final String ILLEGAL_ARGUMENT_EXCEPTION_PROJECT_AND_EMPLOYEE =

      "Project ID and employee ID must be positive integers";

  public IllegalArgumentException(String message) {
    super(message);
  }
}
