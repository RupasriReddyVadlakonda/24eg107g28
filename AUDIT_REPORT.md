# Personal Data Vault — Project Audit

**Review date:** 2026-10-10  
**Scope:** checked-in workspace, active Spring Boot module, frontend API client, Dockerfiles and deployment docs. No connection to the live Render service or production database was available; production configuration findings are explicitly marked unverified.

## Executive summary

The deployed service is built from [`backend/`](./backend/pom.xml), not the original root Maven project. The workspace previously had a root `pom.xml` compiling a separate, incomplete `src/` tree while both Dockerfiles built `backend/`; this made a root Maven command capable of succeeding without validating the deployed application. The root build is now an aggregator for the backend module.

The intended security model is materially present: user and application JWTs have separate token types and authorities; application access rechecks the application row and active flag; vault operations are owner-scoped; data access requires a matching active consent with exact type, purpose and operation; refresh tokens are random, hashed at rest and atomically rotated; vault values use AES-GCM. The observed public-root response was caused by having no root handler even though `/` was listed as permitted. A public root status route is now implemented. A second confirmed route-level issue allowed an application principal to reach `/api/auth/me`; it is now restricted to user/admin roles.

**Overall health:** reasonable baseline security, but not production-verified. Schema evolution, in-memory throttling/alerts, blocking audit writes, and unbounded administrative lists remain important operational/privacy risks. Passing tests does not establish legal or regulatory compliance.

## Confirmed findings and fixes

| Severity | Finding / impact | Evidence and resolution |
|---|---|---|
| High — fixed | `GET /` was permitted but had no handler. A request fell through to error handling and returned the configured authentication error rather than a useful public response. | Added [`RootController.java`](./backend/src/main/java/com/datavault/personal_data_vault/controller/RootController.java) returning a minimal non-sensitive status envelope; added an integration assertion. This is an application liveness response, not a database readiness check. |
| High — fixed | `/api/auth/me` matched the broad authenticated fallback, so an `ROLE_APPLICATION` token could authenticate there; the controller expected `UserPrincipal`, creating an invalid identity boundary. | Added `/api/auth/me` to the user/admin matcher in [`SecurityConfig.java`](./backend/src/main/java/com/datavault/personal_data_vault/config/SecurityConfig.java). An app-token 403 regression assertion was added. |
| High — fixed | `JwtAuthenticationFilter` previously swallowed every `Exception`, which could hide unexpected infrastructure failures and erase useful diagnostics. It also dispatched any non-application token as a user token. | [`JwtAuthenticationFilter.java`](./backend/src/main/java/com/datavault/personal_data_vault/security/JwtAuthenticationFilter.java) now accepts only recognized `USER`/`APPLICATION` token types and treats expected invalid-token/unknown-user cases as unauthenticated; protected routes then return 401. Unexpected failures are no longer swallowed. |
| Medium — fixed | Rate windows did not reliably replace expired values when `putIfAbsent` found an expired entry; stale keys were also retained indefinitely, and concurrent first-reject alert detection could miss or repeat an alert. | [`RateLimiterService.java`](./backend/src/main/java/com/datavault/personal_data_vault/redis/RateLimiterService.java) now atomically replaces expired windows, bounds stale-map retention with periodic expiry cleanup, and uses an atomic one-shot rejection-alert latch. Regression tests cover rollover and first-rejection behavior. |
| Medium — fixed | Public registration, user login and application-token exchange had no configured request throttle. | Added per-client-address throttling to `POST /api/auth/register`, `POST /api/auth/login`, and `POST /api/apps/token`; excess requests return 429 with `RATE_LIMIT_EXCEEDED`. These limits remain per-process (see outstanding risks). |
| Medium — fixed | Request/response/entity/principal builder `toString()` output could include passwords, stored password hashes, refresh/access tokens, client secrets/hashes, contact details or vault values if later logged. | Redacted sensitive values in `toString()` representations for auth/app/vault DTOs, `UserPrincipal`, the refresh-token builder, and the `User`, `ThirdPartyApplication`, and `PersonalData` builders. This does not change JSON serialization. |
| Medium — fixed | SQL uniqueness races could escape as an internal 500 even though the database correctly rejected a duplicate. | [`GlobalExceptionHandler.java`](./backend/src/main/java/com/datavault/personal_data_vault/exception/GlobalExceptionHandler.java) maps `DataIntegrityViolationException` to a generic 409 without exposing database details. |
| Medium — fixed | OpenAPI declared a bearer scheme but did not apply a default global security requirement, so generated protected operations could appear public or omit the lock. | [`OpenApiConfig.java`](./backend/src/main/java/com/datavault/personal_data_vault/config/OpenApiConfig.java) now declares the global bearer requirement; public auth, app-token, and root operations explicitly opt out. |
| Medium — fixed | Backend datasource configuration embedded a provider-specific database host and username in source. The password was environment-substituted, but the static endpoint still exposed deployment metadata and overrode the documented URL variable. | [`backend/src/main/resources/application.properties`](./backend/src/main/resources/application.properties) now resolves `SPRING_DATASOURCE_URL`, username and password from environment variables, with local development fallbacks. No production password or key was reproduced in this report. |
| Low — fixed | Root Maven project and active backend were independent projects, so root commands did not test deployment code. | Root [`pom.xml`](./pom.xml) is now a reactor aggregator of `backend/`; root and backend README instructions were aligned. |
| Low — fixed | Email normalization used JVM-default-locale lowercase behavior, which can produce unexpected results in locales such as Turkish. | Registration and login normalization in [`AuthService.java`](./backend/src/main/java/com/datavault/personal_data_vault/service/AuthService.java) now uses `Locale.ROOT`. |
| Low — fixed | The root properties did not cap email length at the commonly supported 254-character address size, potentially leading to database-dependent failures. | Registration/login validation and the user email mapping now specify 254 characters. New/updated column metadata is non-destructive; no production data was reset. |

## Outstanding risks / unverified items

| Severity | Status | Detail and recommended next action |
|---|---|---|
| High | Confirmed architecture limitation | Rate limits and unauthorized-attempt windows are held in process-local `ConcurrentHashMap`s, not Redis/shared storage. Limits reset on restart, are not shared across Render instances, and do not create a cluster-wide cap. `RedisConfig` is empty; `backend/pom.xml` has no Redis starter. I did not add a mandatory Redis dependency because no production Redis endpoint/credentials were supplied. Add managed shared storage (Redis or equivalent), configure it through environment variables, and verify behavior with multiple instances before relying on these controls in production. |
| High | Production unverified | No live Render environment, effective environment-variable values, Render blueprint, deployed frontend origin, or production DB/schema was accessible. Verify the currently deployed service uses the root Dockerfile or `backend/Dockerfile`, and set `PORT`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `ENCRYPTION_KEY`, and the exact `CORS_ALLOWED_ORIGINS`. Never use local defaults in production. |
| High | Operational/privacy risk | There are no versioned database migrations, and `spring.jpa.hibernate.ddl-auto` defaults to `update`. This can silently mutate an existing schema and does not provide a reviewed rollout/rollback path. Do not reset production data. Compare the current schema to entity mappings, write reviewed forward migrations, back up, then switch production to `validate`. |
| Medium | Confirmed | Audit persistence is synchronous, despite the earlier architectural description of asynchronous audit logging. `AuditService.logAccess` uses `REQUIRES_NEW`, which keeps denial logs independent of outer rollback but still blocks the caller. Consider a durable queue/outbox if request latency or audit availability demands asynchronous delivery; do not use fire-and-forget that can drop events. |
| Medium | Confirmed | Admin user/app/consent lists and user alert/consent lists materialize full results in memory; security-alert methods cap at fixed 100/500 records without exposed pagination, while audit alone is pageable. Add stable ordering and pagination before datasets grow. Admin user results contain personal contact fields, so review whether each is needed. |
| Medium | Confirmed limitation | The root status handler checks only that the web application responds; it does not prove MySQL is reachable. No Actuator health endpoint is configured. Configure a host health check or a deliberately scoped readiness endpoint without exposing secrets/database details. |
| Medium | Confirmed limitation | Consent mutations do not use optimistic/pessimistic locking; simultaneous grant/deny/revoke requests can race and last-write behavior is database-dependent. Add `@Version` or a conditional state-transition update plus conflict handling and a concurrency test. This needs a schema migration if implemented with a version column. |
| Medium | Confirmed | `GET /api/admin/users`, `/apps`, `/consents`, and `/alerts` are not paginated. The service returns complete user/app/consent lists; alerts are limited to a fixed page. Fix as an API-compatible paginated evolution. |
| Low | Confirmed | Expired refresh-token rows are not periodically purged. They contain only hashes but will accumulate. Add a scheduled retention/cleanup policy that never deletes active tokens and is coordinated with production retention requirements. |
| Low | Production-only unverified | `HttpServletRequest.getRemoteAddr()` is used as the public throttling key. Behind Render's proxy, verify whether this is the client address or proxy address. Do not blindly trust arbitrary `X-Forwarded-For`; configure Spring's trusted forwarded-header strategy only for the known proxy chain. |
| Low | Confirmed | Backend integration tests use H2/MySQL mode and the local run's available JDK was Java 26. They do not prove Aiven/Render MySQL compatibility, TLS, port injection, real JDK 21 runtime, or multi-instance behavior. Run a staging startup against a disposable compatible MySQL instance and use JDK 21/Docker before deployment. |

No literal database password, live JWT signing secret, or AES key was found in the currently reviewed configuration files. The checked-in test properties contain deterministic test-only key material; do not reuse it outside tests. This scan does not inspect Git history, CI logs, Render environment settings, or secret-manager history. Rotate any production secret that was ever committed or logged.

## Endpoint and authorization matrix

`USER` means an authenticated user JWT; `ADMIN` means a user JWT with `ADMIN`; `APPLICATION` means an app JWT validated against an active application record. Every response uses `ApiResponse` except framework static docs.

| Method | Path | Auth / role | Request / result | Notes |
|---|---|---|---|---|
| GET | `/` | Public | status envelope | Added liveness response; does not test DB readiness. |
| POST | `/api/auth/register` | Public; throttled | registration DTO → 201 auth response | Role fixed to USER server-side. |
| POST | `/api/auth/login` | Public; throttled | credentials → 200 auth response | Invalid credentials 401. |
| POST | `/api/auth/refresh` | Public | refresh token → rotated tokens | Single-use hash-backed rotation; 401 for invalid/revoked/expired. |
| POST | `/api/auth/logout` | USER/ADMIN | refresh token → 200 | Revokes supplied token only when owned by principal. Existing access JWT remains usable until its short expiry; this is expected for stateless access tokens. |
| GET | `/api/auth/me` | USER/ADMIN | none → profile | App tokens explicitly denied (403). |
| POST / GET | `/api/vault/data` | USER/ADMIN | create DTO / none → records | Operates on authenticated principal's records only. |
| GET / PUT / PATCH / DELETE | `/api/vault/data/{id}` | USER/ADMIN | id, validated DTO as applicable | Repository predicates include owner ID; foreign-owner access returns 404. |
| POST | `/api/apps` | USER/ADMIN | registration DTO → 201 one-time secret | App is assigned to current owner. |
| GET | `/api/apps`, `/api/apps/{id}` | USER/ADMIN | none/id → owner-scoped apps | Client-secret hash is not returned. |
| PUT / DELETE | `/api/apps/{id}` | USER/ADMIN | update DTO / id → app or disabled message | Owner-scoped; delete is soft-disable. |
| POST | `/api/apps/token` | Public; throttled | client ID/secret → app token | Invalid/inactive credentials 401; bounded by address, process-local limiter. |
| POST | `/api/consents/request` | USER/ADMIN/APPLICATION | consent request → 201 pending consent | User token's target is principal ID; app token must match request's app ID and active status. |
| GET | `/api/consents`, `/api/consents/{id}` | USER/ADMIN | none/id → own consents | Owner-filtered; admin still sees own list here. |
| PUT | `/api/consents/{id}/grant`, `/deny`, `/revoke` | USER/ADMIN | id → updated consent | Owner-filtered; transitions are server-checked; concurrent transitions not locked. |
| GET / PUT | `/api/data-access/{dataType}` | APPLICATION | user/purpose (read) or user/purpose/value (write) | Exact application, user, data type, purpose, operation and active/nonexpired consent required; every attempt is audited. |
| GET | `/api/audit/logs` | USER/ADMIN | page/size → own logs | Page size is clamped to 1–100 and sorted newest first. |
| GET | `/api/audit/logs/{userId}` | USER/ADMIN | userId/page/size → logs | User can only query self; admins can query any user. |
| GET / PUT | `/api/security-alerts`, `/api/security-alerts/{id}/resolve` | USER/ADMIN | none/id → own alerts/resolved alert | Owner-filtered; first 100 alerts only. |
| GET | `/api/admin/users`, `/apps`, `/consents`, `/alerts` | ADMIN | none → system lists | No paging on these lists; see outstanding risk. |
| GET | `/api/admin/audit` | ADMIN | page/size → logs | Page size clamped to 1–100, newest first. |
| PUT | `/api/admin/alerts/{id}/resolve` | ADMIN | id → alert | System-wide resolution. |
| PUT | `/api/admin/apps/{id}/disable` | ADMIN | id → success envelope | System-wide soft-disable. |
| GET | `/swagger-ui/**`, `/swagger-ui.html`, `/api-docs/**` | Public | Swagger/OpenAPI | Configured paths match `springdoc` properties. |
| OPTIONS | `/**` | Public preflight | CORS headers | CORS origin allowlist remains explicit. |

The route groups are defined in [`SecurityConfig.java`](./backend/src/main/java/com/datavault/personal_data_vault/config/SecurityConfig.java); controller DTOs and service ownership/consent checks were inspected. The frontend services call matching paths for auth, vault, apps, consent, audits, alerts, and admin views. Frontend admin audit uses `/admin/audit` and the backend provides that exact path.

## Persistence, dependencies and deployment review

- Seven JPA repositories and seven core entities were reviewed. IDs are identity-generated; owner/parent references are lazy many-to-one; no cascading deletes are configured. Explicit uniqueness exists for user email, app name/client ID, refresh token hash, and one vault row per `(user_id, data_type)`. Queries use Spring Data/parameterized JPQL, not dynamic SQL.
- Vault values are AES-256-GCM with random 12-byte IVs and 128-bit tags. Ciphertext format has no key-version identifier; keep the current key stable and plan rotation/re-encryption.
- Password/client secrets use Spring Argon2 password encoding; refresh tokens are 48 random bytes, stored only as SHA-256 hashes. Token and sensitive DTO `toString()` output is now redacted.
- Audit rows include user/app/type/operation/purpose/IP/status/reason, not the decrypted vault value or credential. Purpose and IP are still personal/security metadata and need retention/access policy.
- `backend/pom.xml` uses Boot 3.2.5, Java release 21, MySQL connector, JJWT, Argon2, Springdoc, H2 and Spring test dependencies. No dependency changes were necessary for the fixes. The `RedisConfig` class is empty and Redis is not on the active dependency graph.
- Render's assigned `PORT` is wired by both Docker entrypoints and `server.port`. The root image builds `backend/pom.xml`/`backend/src`; the backend Dockerfile is valid when Render's root directory is `backend`. The backend Dockerfile copies only the POM and source directory, not `.env`; root `.dockerignore` excludes `.env`. `netlify.toml` configures frontend build/publish and SPA fallback. No Render Blueprint file is checked in.
- `application.properties` sets Swagger paths `/api-docs` and `/swagger-ui.html`; CORS defaults are localhost only and production must set the exact frontend origin. No Spring Actuator dependency/health endpoint is present.
- No production DB access was performed, no database was reset, no migration was applied, and no Render environment value was assumed.

## Files changed

The complete corrected source is in these workspace files (open each link for the full file; no partial snippets are needed to reconstruct the change):

- Build/config/deployment docs: [`pom.xml`](./pom.xml), [`README.md`](./README.md), [`backend/README.md`](./backend/README.md), [`backend/src/main/resources/application.properties`](./backend/src/main/resources/application.properties), [`backend/src/test/resources/application.properties`](./backend/src/test/resources/application.properties).
- Security/API: [`SecurityConfig.java`](./backend/src/main/java/com/datavault/personal_data_vault/config/SecurityConfig.java), [`JwtAuthenticationFilter.java`](./backend/src/main/java/com/datavault/personal_data_vault/security/JwtAuthenticationFilter.java), [`UserPrincipal.java`](./backend/src/main/java/com/datavault/personal_data_vault/security/UserPrincipal.java), [`OpenApiConfig.java`](./backend/src/main/java/com/datavault/personal_data_vault/config/OpenApiConfig.java), [`AuthController.java`](./backend/src/main/java/com/datavault/personal_data_vault/controller/AuthController.java), [`ApplicationController.java`](./backend/src/main/java/com/datavault/personal_data_vault/controller/ApplicationController.java), [`RootController.java`](./backend/src/main/java/com/datavault/personal_data_vault/controller/RootController.java), [`RateLimitExceededException.java`](./backend/src/main/java/com/datavault/personal_data_vault/exception/RateLimitExceededException.java), [`GlobalExceptionHandler.java`](./backend/src/main/java/com/datavault/personal_data_vault/exception/GlobalExceptionHandler.java), [`RateLimiterService.java`](./backend/src/main/java/com/datavault/personal_data_vault/redis/RateLimiterService.java), [`AuthService.java`](./backend/src/main/java/com/datavault/personal_data_vault/service/AuthService.java).
- Schema/size/log redaction: [`User.java`](./backend/src/main/java/com/datavault/personal_data_vault/entity/User.java), [`PersonalData.java`](./backend/src/main/java/com/datavault/personal_data_vault/entity/PersonalData.java), [`ThirdPartyApplication.java`](./backend/src/main/java/com/datavault/personal_data_vault/entity/ThirdPartyApplication.java), [`RefreshToken.java`](./backend/src/main/java/com/datavault/personal_data_vault/entity/RefreshToken.java), [`LoginRequest.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/request/LoginRequest.java), [`RegisterRequest.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/request/RegisterRequest.java), [`RefreshTokenRequest.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/request/RefreshTokenRequest.java), [`AppTokenRequest.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/request/AppTokenRequest.java), [`DataAccessRequest.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/request/DataAccessRequest.java), [`PersonalDataRequest.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/request/PersonalDataRequest.java), [`AuthResponse.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/response/AuthResponse.java), [`AppTokenResponse.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/response/AppTokenResponse.java), [`AppRegistrationResponse.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/response/AppRegistrationResponse.java), [`PersonalDataResponse.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/response/PersonalDataResponse.java), [`UserResponse.java`](./backend/src/main/java/com/datavault/personal_data_vault/dto/response/UserResponse.java).
- Regression coverage: [`PersonalDataVaultIntegrationTest.java`](./backend/src/test/java/com/datavault/personal_data_vault/PersonalDataVaultIntegrationTest.java), [`RateLimiterServiceTest.java`](./backend/src/test/java/com/datavault/personal_data_vault/redis/RateLimiterServiceTest.java), [`JwtServiceTest.java`](./backend/src/test/java/com/datavault/personal_data_vault/security/JwtServiceTest.java).

Files reviewed and retained unchanged include `JwtService`, `ApplicationPrincipal`, `CustomUserDetailsService`, both REST security handlers, Argon2 encoder configuration, AES-GCM service/tests, audit service, consent and vault/application services, all repositories and remaining JPA mappings, API response DTOs, Dockerfiles, Netlify config and frontend API services/types. The empty `backend/src/main/resources` `RedisConfig` remains inert; Redis support is an explicit outstanding infrastructure decision.

## Verification performed

- `.\backend\mvnw.cmd package` from the repository root: **BUILD SUCCESS**, including 26 backend tests (0 failures/errors), backend JAR packaging, and the root Maven reactor. The compiler targets Java 21; the runtime JDK available locally was Java 26, so JDK 21 runtime behavior remains unverified.
- `npm --prefix frontend test`: **10 tests passed**.
- `npm --prefix frontend run lint`: passed.
- `npm --prefix frontend run build`: passed.
- Tests cover root response, signup/login success and failure, duplicate registration, refresh rotation/replay, invalid/missing bearer, USER-to-ADMIN denial, app-token/profile role separation, app consent access/revoke enforcement, foreign-owner vault denial, encryption at rest, CORS preflight and auth-rate-limit response. They do not test a live Render service, MySQL migrations, expired/tampered JWT variants comprehensively, positive ADMIN operations, or multi-instance rate limiting.

## Render rollout checklist

- [ ] Confirm API service uses the repository-root Dockerfile or `backend/Dockerfile`; both expect the active `backend` module.
- [ ] Set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` to the managed MySQL provider's externally reachable TLS configuration.
- [ ] Keep the existing `ENCRYPTION_KEY` unchanged; verify `JWT_SECRET` is a private random Base64 secret meeting the decoded-length requirement. Do not paste either in tickets/logs.
- [ ] Set `CORS_ALLOWED_ORIGINS` to the deployed frontend's exact HTTPS origin, without a slash or wildcard.
- [ ] Verify the Render `PORT` is supplied to the web service and test `/` for the public liveness envelope, `/api/auth/register`/`login`, then an unauthenticated protected route for HTTP 401.
- [ ] Test an app JWT against `/api/data-access/...` with and without matching consent, and verify user access to `/api/auth/me` and denied app-token access (403).
- [ ] Compare production MySQL schema to entities before switching `JPA_DDL_AUTO` to `validate`; use a backup and reviewed migrations. Do not drop/reset tables.
- [ ] Check Render logs and build output for configuration problems, without printing secret values; verify the frontend's `VITE_API_BASE_URL` uses the deployed API `/api` base.
- [ ] Decide whether to provision shared Redis and implement a distributed limiter before scaling beyond one API instance; validate proxy-derived client IP handling.
- [ ] Re-run smoke tests after deployment. A green Render “live” state alone is not proof of DB readiness, API authorization correctness, or data integrity.

**No regulatory/compliance claim is made by this code review.**
