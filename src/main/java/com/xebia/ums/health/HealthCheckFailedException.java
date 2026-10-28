package com.xebia.ums.health;

/** Raised when a health dependency (e.g. the database) is unavailable. */
public class HealthCheckFailedException extends RuntimeException {

  public HealthCheckFailedException(String message) {
    super(message);
  }
}
