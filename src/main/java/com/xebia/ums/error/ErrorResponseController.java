package com.xebia.ums.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Replaces Spring Boot's default {@code /error} handler so container-level failures (e.g. errors
 * raised outside Spring MVC) are also rendered as ErrorResponse with no internal details.
 */
@RestController
public class ErrorResponseController implements ErrorController {

  @RequestMapping("${server.error.path:/error}")
  public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
    HttpStatus status = resolveStatus(request);
    String code = status.is5xxServerError() ? ErrorCodes.INTERNAL_SERVER_ERROR : status.name();
    String message =
        status == HttpStatus.NOT_FOUND
            ? "The requested resource was not found."
            : ErrorCodes.GENERIC_ERROR_MESSAGE;
    return ResponseEntity.status(status).body(ErrorResponse.of(status, code, message));
  }

  private static HttpStatus resolveStatus(HttpServletRequest request) {
    Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (code instanceof Integer value) {
      HttpStatus resolved = HttpStatus.resolve(value);
      if (resolved != null) {
        return resolved;
      }
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
