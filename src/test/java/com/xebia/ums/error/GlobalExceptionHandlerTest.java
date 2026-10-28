package com.xebia.ums.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xebia.ums.config.DatabaseConnectionValidator;
import com.xebia.ums.health.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthController.class)
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DatabaseConnectionValidator databaseConnectionValidator;

  @Test
  void unknownPathReturnsErrorResponse() throws Exception {
    mockMvc
        .perform(get("/does-not-exist"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.code").value("NOT_FOUND"))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void unsupportedMethodReturnsErrorResponse() throws Exception {
    mockMvc
        .perform(post("/health"))
        .andExpect(status().isMethodNotAllowed())
        .andExpect(jsonPath("$.status").value(405))
        .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"));
  }
}
