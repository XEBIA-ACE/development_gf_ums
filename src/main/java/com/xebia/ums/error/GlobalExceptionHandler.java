package com.xebia.ums.error;

import com.xebia.ums.health.HealthCheckFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Maps exceptions to ErrorResponse payloads without exposing stack traces or internals. */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(HealthCheckFailedException.class)
  public ResponseEntity<ErrorResponse> handleHealthCheckFailed(HealthCheckFailedException ex) {
    log.warn("Health check failed: {}", ex.getMessage());
    return build(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorCodes.INTERNAL_SERVER_ERROR,
        "Service is unhealthy. Please try again later.");
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException ex) {
    return build(
        HttpStatus.NOT_FOUND, ErrorCodes.NOT_FOUND, "The requested resource was not found.");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex) {
    return build(
        HttpStatus.METHOD_NOT_ALLOWED,
        ErrorCodes.METHOD_NOT_ALLOWED,
        "The requested HTTP method is not supported for this resource.");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
    log.error("Unhandled exception", ex);
    return build(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorCodes.INTERNAL_SERVER_ERROR,
        ErrorCodes.GENERIC_ERROR_MESSAGE);
  }

  private static ResponseEntity<ErrorResponse> build(
      HttpStatus status, String code, String message) {
    return ResponseEntity.status(status).body(ErrorResponse.of(status, code, message));
  }
}
