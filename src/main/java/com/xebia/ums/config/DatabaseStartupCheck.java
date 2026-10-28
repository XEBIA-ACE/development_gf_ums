package com.xebia.ums.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Fails application startup fast when the configured database is unreachable. */
@Component
@ConditionalOnProperty(
    prefix = "ums.db",
    name = "validate-on-startup",
    havingValue = "true",
    matchIfMissing = true)
public class DatabaseStartupCheck implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(DatabaseStartupCheck.class);

  private final DatabaseConnectionValidator validator;

  public DatabaseStartupCheck(DatabaseConnectionValidator validator) {
    this.validator = validator;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (!validator.isConnectionValid()) {
      throw new IllegalStateException(
          "Database connection could not be validated on startup; check DB_* environment variables");
    }
    log.info("Database connection validated on startup");
  }
}
