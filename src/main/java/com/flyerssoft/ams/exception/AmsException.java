package com.flyerssoft.ams.exception;

/**
 * Exception class representing an error specific to the AMS application.
 * The {@code AmsException} class is a runtime exception that can be
 * thrown during the execution of the AMS application.
 * It encapsulates information about the error, including an error message,
 * and can be used to handle exceptional scenarios within the application.
 * This exception class is meant to be used for AMS-specific errors and
 * can be caught and handled accordingly in the application code.
 */
public class AmsException extends RuntimeException {

  private String message;

  public AmsException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setCustomMessage(String message) {
    this.message = message;
  }
}
