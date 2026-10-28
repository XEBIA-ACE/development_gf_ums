# development_gf_ums

User Management Service (SVC-001) — Java 21 / Spring Boot 3 / PostgreSQL.

This repository currently contains the **scaffold** for the service (story `SCAFFOLD-SVC-001`):
build manifest, entrypoint, formatting config, environment-based configuration, a PostgreSQL
connection helper, and the two system endpoints `GET /health` and `GET /api/v1/info`.
Business endpoints from the approved spec (`specs/SCAFFOLD-SVC-001/references/SVC-001.md`) are
**not** implemented yet.

## Layout

| Path | Purpose |
| --- | --- |
| `pom.xml`, `mvnw` | Maven build (Spring Boot Web, Data JPA, Validation, PostgreSQL driver) |
| `src/main/java/com/xebia/ums/UserManagementServiceApplication.java` | Entrypoint |
| `config/DatabaseConnectionValidator` | Reusable DB connectivity check (startup + health) |
| `config/DatabaseStartupCheck` | Fails startup fast if the DB is unreachable |
| `health/HealthController` | `GET /health` → `HealthResponse` |
| `info/InfoController` | `GET /api/v1/info` → `ServiceInfoResponse` |
| `error/GlobalExceptionHandler`, `error/ErrorResponseController` | All errors rendered as `ErrorResponse`, never with stack traces |

## Configuration (environment variables)

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `DB_HOST` | yes* | – | PostgreSQL host |
| `DB_PORT` | no | `5432` | PostgreSQL port |
| `DB_NAME` | yes* | – | Database name |
| `DB_URL` | no | – | Full JDBC URL; overrides `DB_HOST`/`DB_PORT`/`DB_NAME` (*) |
| `DB_USERNAME` | yes | – | Database user |
| `DB_PASSWORD` | yes | – | Database password |
| `DB_POOL_MAX_SIZE` | no | `10` | Hikari max pool size |
| `DB_CONNECTION_TIMEOUT_MS` | no | `5000` | Max wait for a pooled connection (bounds `/health` latency when the DB is down) |
| `DB_VALIDATE_ON_STARTUP` | no | `true` | Validate DB connectivity on startup |
| `DB_VALIDATION_TIMEOUT_SECONDS` | no | `2` | Timeout for connection validation |
| `SERVER_PORT` | no | `8080` | HTTP port |
| `APP_ENVIRONMENT` | no | `unknown` | Environment name reported by `/api/v1/info` |

No credentials or environment-specific endpoints are stored in the repository. See `.env.example`.

## Run locally

```bash
cp .env.example .env            # fill in DB_PASSWORD etc.
set -a && . ./.env && set +a
docker compose up -d postgres   # optional local PostgreSQL
./mvnw spring-boot:run
curl localhost:${SERVER_PORT:-8080}/health        # {"status":"UP"}
curl localhost:${SERVER_PORT:-8080}/api/v1/info
```

## Build, test, format

```bash
./mvnw verify            # compile, tests (Testcontainers PostgreSQL if Docker is available), spotless:check
./mvnw spotless:apply    # format sources (google-java-format)
```

## Open gaps (not resolved by the exported spec)

These were implemented as minimal safe defaults and should be confirmed against the full contract:

1. **HealthResponse / ServiceInfoResponse fields** — the OpenAPI in `references/SVC-001.md` is
   truncated before `components.schemas`. `HealthResponse` exposes only `status` (`"UP"`).
   `ServiceInfoResponse` exposes `version`, `environment`, `buildTimestamp` (the fields named in the
   endpoint description).
2. **Health failure semantics** — the contract lists `500` + `ErrorResponse` for an unhealthy
   service; `/health` returns that when the DB connectivity check fails.
3. **Generic error codes** — the export defines domain error codes only. Generic codes
   `INTERNAL_SERVER_ERROR`, `NOT_FOUND`, `METHOD_NOT_ALLOWED` are used for system/framework errors.
   `details` and `traceId` are omitted (no correlation-id mechanism is specified).
4. **Environment variable names** — not specified by the export; the names above were chosen.
5. **Build metadata source** — version/build time come from Spring Boot `build-info`
   (`META-INF/build-info.properties`); when absent, `version` is `unknown` and `buildTimestamp` is omitted.
6. **Schema management** — no migration tool is specified; `spring.jpa.hibernate.ddl-auto=none`.
7. **Domain/C4 classes** — `User`, `OtpToken`, `AuthSession`, `Email`, `PasswordHash`, the
   repositories and application services are not needed for the scaffold to compile or start, so
   no placeholders were added to avoid pre-empting their contracts.
8. **Tech stack conflict** — AC-1 text mentions Node.js/Express.js, while the implementation
   constraints and C4 design specify Java/Spring Boot/PostgreSQL. The scaffold follows the latter.
