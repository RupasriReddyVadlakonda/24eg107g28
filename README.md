# Personal Data Vault and Consent Management System

A Java 21 / Spring Boot REST backend that stores user personal information encrypted, authenticates users and registered client applications, and gates application data access against explicit, time-bounded, purpose- and operation-specific consent. The active Maven application is in `backend/`; the React frontend is in `frontend/`.

## Architecture

`controller → service → repository → JPA entities`

- `controller/`: REST API, request validation, consistent response envelopes.
- `service/`: authentication, vault, application, consent, consent authorization, access and alert logic.
- `entity/` and `repository/`: JPA persistence for users, encrypted personal data, apps, consents, refresh tokens, audit records, and alerts.
- `security/`: stateless JWT filter, user and application principals, JWT generation/validation.
- `encryption/`: AES-256-GCM authenticated encryption for stored personal values.
- `audit/`: access event persistence, including failed authorization attempts.
- `redis/`: fixed-window request limiter and short-lived unauthorized-attempt counters.
- `exception/`: API-shaped error responses.

## Stack

Java 21, Spring Boot 3.2.5, Spring Web, Spring Security, Spring Data JPA, Hibernate, MySQL 8, Redis 7, Maven, JJWT, Argon2id, AES-256-GCM, Jakarta Bean Validation, springdoc OpenAPI, JUnit 5, Mockito, and H2 for tests.

## Persistence model

- `users`: unique normalized email, Argon2 password hash, role and enabled state.
- `personal_data`: owner relation, typed category, AES-GCM ciphertext (Base64 of the random 12-byte IV followed by GCM ciphertext and tag), description and timestamps. No plaintext personal values are persisted.
- `third_party_applications`: owner relation, unique client ID, Argon2 client-secret hash, redirect URI and active state. A raw client secret is returned only once at registration.
- `consents`: user/app relations plus data type, exact purpose, allowed operation, state, requested duration, grant start/expiry, and revoke time.
- `refresh_tokens`: SHA-256 token hash, owner, expiration, revoked state; raw refresh tokens are returned to the client and never stored.
- `access_logs`: user/app, data type, operation, purpose, time, IP, result and reason code. Values, passwords, credentials, JWTs and keys are not logged.
- `security_alerts`: user/app relation, alert category/severity, description, timestamp and resolution state.

## Requirements

- JDK 21 (the Maven project targets Java 21).
- Maven 3.9+.
- Docker Desktop (for the provided MySQL and Redis services), or existing MySQL 8 and Redis 7 instances.
- Generate secrets; never use development placeholders as live keys.

Generate cryptographic base64 keys in Windows PowerShell (run from the repository root):

```powershell
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$jwtBytes = New-Object byte[] 48
$aesBytes = New-Object byte[] 32
$rng.GetBytes($jwtBytes)
$rng.GetBytes($aesBytes)
[Convert]::ToBase64String($jwtBytes)
[Convert]::ToBase64String($aesBytes)
$rng.Dispose()
```

For cryptographic production key generation, use a vetted secret manager or cryptographically secure key generation tool. The first key above is for `JWT_SECRET` (at least 32 decoded bytes); the second is exactly 32 bytes for `ENCRYPTION_KEY`.

## Local setup

1. Copy `.env.example` to `.env` and replace every placeholder, including `DB_PASSWORD`, `JWT_SECRET`, and `ENCRYPTION_KEY`. Ensure MySQL is running and reachable.
2. From the repository root, start the backend. The launcher loads `.env` and maps the database settings to Spring environment variables:

   ```powershell
   .\run-local.ps1
   ```

   If your PowerShell is already in `backend/`, run `.\run-local.ps1`. Do not run `.\mvnw.cmd spring-boot:run` by itself for local startup: the preserved `application.properties` datasource points at the remote database. For an IDE/manual Maven launch, first set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, and `ENCRYPTION_KEY`. The backend listens on port `8080` by default; set `PORT` or `SERVER_PORT` to override it.
4. API base URL: `http://localhost:8080/api`.
5. Swagger UI: `http://localhost:8080/swagger-ui.html`; OpenAPI JSON: `http://localhost:8080/api-docs`.

The active backend currently defaults `JPA_DDL_AUTO` to `update` for compatibility with the existing deployment; this has no reviewed versioned migrations and should be treated as a deployment risk. Before moving to `validate`, compare the live MySQL schema against the entities and apply a reviewed migration. Do not rotate `ENCRYPTION_KEY` without a planned re-encryption/key-version migration; existing ciphertext requires its original key.

## Deploy on Render

Deploy the API as a Render **Web Service** using the repository root as its root directory and Docker as the runtime. The root Dockerfile builds the Maven project from `backend/`; alternatively, configure Render's root directory as `backend` and use `backend/Dockerfile`. Both Dockerfiles pass Render's injected `PORT` to Spring Boot; the default is `8080`.

Set these environment variables in the Render service dashboard; do not upload `.env` or put credentials in the Dockerfile. The backend copies its properties file into the image, but it contains no deployed credentials and resolves database values from environment variables. Set the Spring datasource names explicitly:

- `SPRING_DATASOURCE_URL`: the externally reachable MySQL JDBC URL, including the provider's required TLS options.
- `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD`: database credentials.
- `JWT_SECRET`: base64-encoded random key with at least 32 decoded bytes.
- `ENCRYPTION_KEY`: base64-encoded random key with exactly 32 decoded bytes. Keep it stable; rotating it without re-encrypting existing data makes stored values unreadable.
- `CORS_ALLOWED_ORIGINS`: the deployed frontend's exact `https://...` origin, without a trailing slash.
- `JPA_DDL_AUTO`: `update` for initial deployment; use reviewed migrations and `validate` after schema management is established.

The current rate limiter is in-memory, so this app version does not require a Redis service. Use an externally hosted MySQL database reachable from Render; localhost database URLs will not work from the deployed container.

Deploy the frontend separately as a Render **Static Site** with root directory `frontend`, build command `npm ci && npm run build`, and publish directory `dist`. Set the build-time variable `VITE_API_BASE_URL` to `https://<your-api-service>.onrender.com/api`. Add a rewrite from `/*` to `/index.html` with status `200` for client-side routes. Set the API's `CORS_ALLOWED_ORIGINS` to the resulting frontend origin, then redeploy the API.

For Netlify, the root `netlify.toml` configures the frontend base directory, build command, publish directory, and SPA rewrite. Set `VITE_API_BASE_URL` in the Netlify site's build environment to the same deployed API URL; production builds fail if it is omitted. Update the API's `CORS_ALLOWED_ORIGINS` to the exact deployed frontend origin.

## Configuration

See `.env.example` for local variable names. For deployments and IDE run configurations, use `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`; environment variables take precedence over the backend properties defaults.

CORS accepts an explicit comma-separated origin allowlist; wildcard origins are rejected because credentials are enabled. The default frontend origin is `http://localhost:5173`.

## API

All responses use `{ "success": true|false, "message": "...", "data": ... }`; errors contain `errorCode`. User routes require `Authorization: Bearer <user-access-token>`. Data-access routes require an app access token.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create a user and issue user access/refresh tokens |
| POST | `/api/auth/login` | Authenticate a user |
| POST | `/api/auth/refresh` | Rotate a refresh token |
| POST | `/api/auth/logout` | Revoke a refresh token (requires user access token) |
| GET | `/api/auth/me` | Retrieve the authenticated user's profile |
| POST / GET | `/api/vault/data` | Create/list own encrypted personal records |
| GET / PUT / DELETE | `/api/vault/data/{id}` | Read/update/delete an owned record |
| PATCH | `/api/vault/data/{id}` | Partially update provided record fields |
| POST | `/api/apps` | Register an app; returns its one-time client secret |
| GET | `/api/apps`, `/api/apps/{id}` | List/read own apps |
| PUT / DELETE | `/api/apps/{id}` | Update/disable an owned app |
| POST | `/api/apps/token` | Exchange client ID/secret for an app JWT |
| POST | `/api/consents/request` | Create a pending consent request (user or authenticated app) |
| GET | `/api/consents`, `/api/consents/{id}` | View own consents |
| PUT | `/api/consents/{id}/grant`, `/deny`, `/revoke` | Grant/deny pending consent or revoke granted consent |
| GET | `/api/data-access/{dataType}?userId={id}&purpose={text}` | App requests one data type with exact purpose and READ consent |
| PUT | `/api/data-access/{dataType}` | App writes one data type with exact purpose and WRITE consent |
| GET | `/api/audit/logs`, `/api/audit/logs/{userId}` | View own logs; admins may view any user |
| GET | `/api/security-alerts` | View own alerts |
| PUT | `/api/security-alerts/{id}/resolve` | Resolve own alert |
| GET | `/api/admin/users`, `/apps`, `/consents`, `/audit`, `/alerts` | Admin-only system lists |
| PUT | `/api/admin/alerts/{id}/resolve`, `/api/admin/apps/{id}/disable` | Admin alert/app controls |

Admin endpoints require a user with `ADMIN` role. Self-registration always assigns `USER`; bootstrap/admin provisioning should be handled by an explicit secured operational process, not an open public registration field.

## Example flow

### Register / login

```http
POST /api/auth/register
Content-Type: application/json

{"fullName":"Avery Example","email":"avery@example.com","password":"StrongPass!123"}
```

Login uses `POST /api/auth/login` with the same `email` and `password`. Save `data.accessToken` and `data.refreshToken` securely; refresh token rotation invalidates the previous token.

### Register a client application

```http
POST /api/apps
Authorization: Bearer <user-access-token>
Content-Type: application/json

{"applicationName":"ShoppingApp","description":"Order status notifications","redirectUri":"https://shop.example.com/oauth/callback"}
```

Store the returned `clientSecret` immediately. It cannot be retrieved later. Exchange credentials:

```http
POST /api/apps/token
Content-Type: application/json

{"clientId":"<client-id>","clientSecret":"<one-time-secret>"}
```

### Create and grant consent

An authenticated application requests a user decision using its app JWT:

```http
POST /api/consents/request
Authorization: Bearer <application-access-token>
Content-Type: application/json

{"userId":42,"applicationId":7,"dataType":"EMAIL","purpose":"Order Confirmation","operation":"READ","requestedDurationDays":7}
```

The user views `GET /api/consents` and approves the pending request:

```http
PUT /api/consents/123/grant
Authorization: Bearer <user-access-token>
```

### Authorized read

```http
GET /api/data-access/EMAIL?userId=42&purpose=Order%20Confirmation
Authorization: Bearer <application-access-token>
```

The service requires the matching active app, same user, `EMAIL`, exact purpose, `READ`, `GRANTED` status, and an unexpired consent before decrypting and returning the value. The attempt is audited.

### Denied access

Using the same application token but requesting another type or purpose, or using `WRITE` without that operation granted, returns HTTP `403` and an error envelope. For example:

```http
GET /api/data-access/PHONE?userId=42&purpose=Order%20Confirmation
Authorization: Bearer <application-access-token>
```

A denied attempt is logged; repeated consent failures increment a Redis counter and generate a high-severity alert at the configured threshold. Redis also enforces the configured per-application fixed-window request limit.

## Testing

From `backend/`, run `.\mvnw.cmd test`. Tests run against in-memory H2 and do not require production credentials.

## Container image

Build the Spring Boot image from the repository root with `docker build -t personal-data-vault .` (or from `backend/` with `docker build -t personal-data-vault -f Dockerfile .`). Pass the database and secret environment variables at run time; never bake `.env` into the image.
