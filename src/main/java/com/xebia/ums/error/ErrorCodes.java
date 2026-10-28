package com.xebia.ums.error;

/**
 * Generic application error codes used by the scaffold.
 *
 * <p>Open gap: the export defines domain codes (EMAIL_ALREADY_IN_USE, INVALID_OTP, ...) but no
 * codes for generic/system failures. These generic codes mirror the HTTP status names until the
 * catalogue is confirmed.
 */
public final class ErrorCodes {

  public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
  public static final String NOT_FOUND = "NOT_FOUND";
  public static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";

  public static final String GENERIC_ERROR_MESSAGE =
      "An unexpected error occurred. Please try again later.";

  private ErrorCodes() {}
}
