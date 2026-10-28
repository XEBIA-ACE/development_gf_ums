package com.xebia.ums;

import static org.assertj.core.api.Assertions.assertThat;

import com.xebia.ums.config.DatabaseConnectionValidator;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Boots the full application against a real PostgreSQL container, supplying configuration only
 * through the same environment variable names used in production (DB_HOST, DB_PORT, ...).
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserManagementServiceApplicationTests {

  @Container
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @DynamicPropertySource
  static void environment(DynamicPropertyRegistry registry) {
    registry.add("DB_HOST", POSTGRES::getHost);
    registry.add("DB_PORT", () -> POSTGRES.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
    registry.add("DB_NAME", POSTGRES::getDatabaseName);
    registry.add("DB_USERNAME", POSTGRES::getUsername);
    registry.add("DB_PASSWORD", POSTGRES::getPassword);
    registry.add("APP_ENVIRONMENT", () -> "it");
  }

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private DatabaseConnectionValidator databaseConnectionValidator;

  @Test
  void databaseConnectionIsValid() {
    assertThat(databaseConnectionValidator.isConnectionValid()).isTrue();
  }

  @Test
  @SuppressWarnings("rawtypes")
  void healthEndpointRespondsUp() {
    ResponseEntity<Map> response = restTemplate.getForEntity("/health", Map.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).containsEntry("status", "UP");
  }

  @Test
  @SuppressWarnings("rawtypes")
  void infoEndpointReturnsServiceMetadata() {
    ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/info", Map.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).containsEntry("environment", "it");
    assertThat(response.getBody()).containsKeys("version", "buildTimestamp");
  }

  @Test
  @SuppressWarnings("rawtypes")
  void unknownPathReturnsErrorResponse() {
    ResponseEntity<Map> response = restTemplate.getForEntity("/nope", Map.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).containsEntry("code", "NOT_FOUND").doesNotContainKey("trace");
  }
}
