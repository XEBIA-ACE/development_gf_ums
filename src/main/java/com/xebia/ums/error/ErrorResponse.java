package com.xebia.ums.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import org.springframework.http.HttpStatus;

/** Standard ErrorResponse schema shared by all endpoints. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String code,
    String message,
    Object details,
    String traceId) {

  public static ErrorResponse of(HttpStatus status, String code, String message) {
    return new ErrorResponse(
        Instant.now(), status.value(), status.getReasonPhrase(), code, message, null, null);
  }
}
