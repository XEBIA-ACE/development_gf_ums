package com.xebia.ums.health;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xebia.ums.config.DatabaseConnectionValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DatabaseConnectionValidator databaseConnectionValidator;

  @Test
  void returnsUpWhenDatabaseReachable() throws Exception {
    when(databaseConnectionValidator.isConnectionValid()).thenReturn(true);

    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void returnsErrorResponseWithoutInternalsWhenDatabaseUnreachable() throws Exception {
    when(databaseConnectionValidator.isConnectionValid()).thenReturn(false);

    mockMvc
        .perform(get("/health"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
        .andExpect(jsonPath("$.message").value("Service is unhealthy. Please try again later."))
        .andExpect(jsonPath("$.trace").doesNotExist())
        .andExpect(jsonPath("$.exception").doesNotExist());
  }

  @Test
  void unexpectedFailureIsMappedToGenericErrorResponse() throws Exception {
    when(databaseConnectionValidator.isConnectionValid())
        .thenThrow(new IllegalStateException("secret internal detail"));

    mockMvc
        .perform(get("/health"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
        .andExpect(
            jsonPath("$.message").value("An unexpected error occurred. Please try again later."));
  }
}
