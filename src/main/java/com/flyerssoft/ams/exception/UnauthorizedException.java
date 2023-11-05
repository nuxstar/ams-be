package com.flyerssoft.ams.exception;

/**
 * Exception thrown when user try to access unauthorized resource.
 */
public class UnauthorizedException extends AmsException {
  public static final String INVALID_TOKEN = "Invalid Access Token";

  public UnauthorizedException(String message) {
    super(message);
  }
}
