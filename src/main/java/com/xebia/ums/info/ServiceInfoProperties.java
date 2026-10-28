package com.xebia.ums.info;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Externalized service metadata; {@code environment} is sourced from {@code APP_ENVIRONMENT}. */
@ConfigurationProperties(prefix = "ums.info")
public record ServiceInfoProperties(String environment) {}
