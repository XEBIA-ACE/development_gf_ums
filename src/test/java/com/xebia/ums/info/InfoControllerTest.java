package com.xebia.ums.info;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InfoController.class)
@Import(InfoControllerTest.Config.class)
@TestPropertySource(properties = "ums.info.environment=test")
class InfoControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void returnsServiceInfoFromBuildMetadataAndConfiguration() throws Exception {
    mockMvc
        .perform(get("/api/v1/info"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.version").value("1.2.3"))
        .andExpect(jsonPath("$.environment").value("test"))
        .andExpect(jsonPath("$.buildTimestamp").value("2026-01-01T00:00:00Z"));
  }

  @TestConfiguration
  @EnableConfigurationProperties(ServiceInfoProperties.class)
  static class Config {
    @Bean
    BuildProperties buildProperties() {
      Properties props = new Properties();
      props.setProperty("version", "1.2.3");
      props.setProperty("time", Instant.parse("2026-01-01T00:00:00Z").toString());
      return new BuildProperties(props);
    }
  }
}
