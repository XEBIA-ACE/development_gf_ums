# Implementation Plan: SCAFFOLD-SVC-001

## Technical Context
- Technology stack: Java, Spring Boot, PostgreSQL.
- Platform: backend.
- Set up the project skeleton for this stack: dependency manifest, entrypoint, and linting/formatting config.
- Add environment-based configuration loading (no hardcoded secrets or endpoints).
- Add a health-check endpoint so the service can be verified as running locally.
- Add a database connection helper for the stack's datastore, configured via environment variables.
- Existing class (C4): User (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): OtpToken (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): AuthSession (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): Email (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): PasswordHash (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): UserRepository (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): OtpTokenRepository (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): AuthSessionRepository (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): UserApplicationService (see [references/SVC-001.md](./references/SVC-001.md))
- Existing class (C4): AuthApplicationService (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: POST /api/v1/users/registration -- Creates a new user account in UNVERIFIED status. Ensures email uniqueness, securely hashes the password, generates a time-limited OTP (5 minutes), persists it, and sends it via email. (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: POST /api/v1/users/registration/otp/resend -- Generates and sends a fresh OTP for an existing unverified user whose previous OTP is expired or lost. Respects OTP expiry rules and rate-limits resends (implementation-specific). (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: POST /api/v1/users/registration/otp/verify -- Validates the submitted OTP for the given email. If correct and not expired (within 5 minutes of generation), marks the user as VERIFIED and clears or invalidates the OTP. Otherwise returns appropriate errors (incorrect, expired, or user not found). (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: POST /api/v1/auth/login -- Authenticates a user with email and password. Only VERIFIED and not SUSPENDED users can log in. Passwords are verified using a secure hashing algorithm. Logs all login attempts (success and failure) with timestamp, user id (where resolvable), and reason for failure. Suspends the account after 3 consecutive failed attempts. Returns an access token and refresh token. Denies access for unverified, suspended, and deleted users with appropriate error messages. (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: POST /api/v1/auth/refresh -- Issues a new short-lived access token when provided with a valid refresh token. Ensures the user account is still active (not deleted, not suspended). (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: GET /api/v1/users/me -- Returns basic account information of the authenticated user, including verification and suspension status. Requires a valid access token. Session expiration is enforced via access token expiry (e.g., 15 minutes of inactivity). (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: DELETE /api/v1/users/me -- Allows a logged-in user to permanently delete their account. Removes user data from the system (subject to implementation and regulatory constraints), revokes access tokens, and sends a confirmation email. Subsequent login attempts must be rejected with a clear notification that the account does not exist. (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: GET /health -- Returns basic health status for orchestration and monitoring. Does not require authentication. (see [references/SVC-001.md](./references/SVC-001.md))
- Existing API endpoint: GET /api/v1/info -- Provides basic metadata about the User Management Service such as version, environment, and build timestamp. Useful for diagnostics and client compatibility checks. (see [references/SVC-001.md](./references/SVC-001.md))

## Contract Changes
- No material API, event, or data contract change is evidenced.

## Security and Quality
- Validate credential and personal-data handling against the story's acceptance criteria
- The scaffolded service must start successfully and its health-check endpoint must respond, using only environment-based configuration.

## Verification Plan
- **AC-1**: Scaffold the Account Management Service (business) per its approved architecture. Technology stack: Node.js, OAuth 2.0, JWT, PostgreSQL, Express.js. Responsibilities: User authentication and authorization; Account registration and management; OAuth 2.0 flow management; Token generation and validation. Ground the generated spec in this service's own C3/C4 diagrams and API spec already on file in Architecture, not just this summary.