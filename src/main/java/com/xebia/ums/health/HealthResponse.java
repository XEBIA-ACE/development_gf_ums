package com.xebia.ums.health;

/**
 * HealthResponse model for {@code GET /health}.
 *
 * <p>Open gap: the exported OpenAPI (references/SVC-001.md) is truncated before the HealthResponse
 * property definitions. Only a single {@code status} field is exposed as the minimal safe default
 * until the schema is confirmed.
 */
public record HealthResponse(String status) {

  public static final String STATUS_UP = "UP";

  public static HealthResponse up() {
    return new HealthResponse(STATUS_UP);
  }
}
