package com.xebia.ums.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Validates connectivity to the PostgreSQL datastore. Used on startup (see {@link
 * DatabaseStartupCheck}) and by the {@code GET /health} endpoint.
 */
@Component
public class DatabaseConnectionValidator {

  private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionValidator.class);

  private final DataSource dataSource;
  private final DatabaseProperties properties;

  public DatabaseConnectionValidator(DataSource dataSource, DatabaseProperties properties) {
    this.dataSource = dataSource;
    this.properties = properties;
  }

  public boolean isConnectionValid() {
    try (Connection connection = dataSource.getConnection()) {
      return connection.isValid(properties.validationTimeoutSeconds());
    } catch (SQLException ex) {
      log.warn("Database connection validation failed: {}", ex.getMessage());
      return false;
    }
  }
}
