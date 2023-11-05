package com.flyerssoft.ams.exception;

/**
 * Exception thrown when user try to use api with invalid request details.
 */
public class BadRequestException extends AmsException {
  public BadRequestException(String message) {
    super(message);
  }
}
