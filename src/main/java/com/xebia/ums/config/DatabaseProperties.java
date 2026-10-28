package com.xebia.ums.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Database connection-validation settings. Connection coordinates and credentials themselves are
 * bound to {@code spring.datasource.*} from environment variables in {@code application.yml}.
 */
@ConfigurationProperties(prefix = "ums.db")
public record DatabaseProperties(boolean validateOnStartup, int validationTimeoutSeconds) {

  public DatabaseProperties {
    if (validationTimeoutSeconds <= 0) {
      validationTimeoutSeconds = 2;
    }
  }
}
