package com.xebia.ums.health;

import com.xebia.ums.config.DatabaseConnectionValidator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** {@code GET /health} (operationId: getHealth). Unauthenticated. */
@RestController
public class HealthController {

  private final DatabaseConnectionValidator databaseConnectionValidator;

  public HealthController(DatabaseConnectionValidator databaseConnectionValidator) {
    this.databaseConnectionValidator = databaseConnectionValidator;
  }

  @GetMapping(path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
  public HealthResponse getHealth() {
    if (!databaseConnectionValidator.isConnectionValid()) {
      throw new HealthCheckFailedException("Database connectivity check failed");
    }
    return HealthResponse.up();
  }
}
