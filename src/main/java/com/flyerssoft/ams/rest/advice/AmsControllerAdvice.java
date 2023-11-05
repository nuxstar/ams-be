package com.flyerssoft.ams.rest.advice;

import com.flyerssoft.ams.exception.AmsException;
import com.flyerssoft.ams.exception.BadRequestException;
import com.flyerssoft.ams.exception.IllegalArgumentException;
import com.flyerssoft.ams.exception.NotAcceptableException;
import com.flyerssoft.ams.exception.NotFoundException;
import com.flyerssoft.ams.exception.UnauthorizedException;
import com.flyerssoft.ams.exception.UpstreamException;
import com.flyerssoft.ams.model.dto.AmsErrorResponse;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This method will catch the given exception and send the custom error response
 * to the client.
 */
@RestControllerAdvice
public class AmsControllerAdvice {

  /**
   * This method will catch the Bad Request exception and
   * send the custom error response to the client.
   */
  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public AmsErrorResponse unauthorizedException(
      UnauthorizedException ex
  ) {
    return new AmsErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        ex.getMessage()
    );
  }

  /**
   * This method will catch the Bad Request exception and
   * send the custom error response to the client.
   */
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public AmsErrorResponse notFoundException(
      NotFoundException ex
  ) {
    return new AmsErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage()
    );
  }

  /**
   * This method will catch the Bad Request exception and
   * send the custom error response to the client.
   */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public AmsErrorResponse badRequestException(
      BadRequestException ex
  ) {
    return new AmsErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage()
    );
  }

  /**
   * This method will catch the MethodArgumentNotValidException exception.
   *
   * @param e e
   *
   * @return send the custom error response
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AmsErrorResponse customValidation(MethodArgumentNotValidException e) {
    String errorMessage = "Invalid input.";
    Optional<FieldError> error = e.getBindingResult().getFieldErrors().stream().findFirst();
    if (error.isPresent()) {
      errorMessage = error.get().getDefaultMessage();
    }
    return new AmsErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        errorMessage
    );

  }

  /**
   * This method will catch the UpstreamException exception.
   *
   * @return send the custom error response
   */
  @ExceptionHandler(UpstreamException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  public AmsErrorResponse upstreamException(UpstreamException ex) {
    return new AmsErrorResponse(
        HttpStatus.GATEWAY_TIMEOUT.value(),
        ex.getMessage()
    );
  }

  /**
   * This method will catch the not allowed  exception.
   *

   * @param ex ex
   * @return proper error response
   */
  @ExceptionHandler(NotAcceptableException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public AmsErrorResponse notAllowed(AmsException ex) {
    return  new AmsErrorResponse(
        HttpStatus.NOT_ACCEPTABLE.value(),
        ex.getMessage()
    );
  }

  /**
   * This method will catch the illegal argument  exception.
   *

   * @param ex ex
   * @return proper error response
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AmsErrorResponse illegalArgument(AmsException ex) {
    return new AmsErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      ex.getMessage()
    );
  }
}
