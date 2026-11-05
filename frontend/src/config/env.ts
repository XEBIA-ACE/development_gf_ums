/**
 * T002: single place where runtime configuration is read (Vite env: VITE_* variables).
 * No URLs, endpoints or secrets are hardcoded.
 */
export interface AppConfig {
  /** GAP: backend base URL not defined in spec; empty until VITE_API_BASE_URL is provided. */
  apiBaseUrl: string;
  environment: string;
}

export function loadConfig(env: Record<string, string | undefined> = import.meta.env): AppConfig {
  return {
    apiBaseUrl: env.VITE_API_BASE_URL ?? '',
    environment: env.VITE_APP_ENVIRONMENT ?? 'unknown',
  };
}

export const config: AppConfig = loadConfig();
