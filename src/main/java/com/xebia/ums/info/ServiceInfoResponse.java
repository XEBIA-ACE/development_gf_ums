package com.xebia.ums.info;

import java.time.Instant;

/**
 * ServiceInfoResponse model for {@code GET /api/v1/info}.
 *
 * <p>Open gap: the exported OpenAPI is truncated before the ServiceInfoResponse property
 * definitions. Fields are limited to those named in the endpoint description: version, environment,
 * and build timestamp.
 */
public record ServiceInfoResponse(String version, String environment, Instant buildTimestamp) {}
