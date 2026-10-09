# Personal Data Vault and Consent Management System - Frontend

A cybersecurity- and privacy-focused dashboard interface built with **React**, **TypeScript**, **Vite**, **Tailwind CSS**, and **React Router**, communicating with a **Java 21 / Spring Boot 3** REST API backend.

---

## Technology Stack

* **React 19** & **TypeScript**
* **Vite 8** (Build tool and fast dev server)
* **React Router v7** (Declarative client routing & protected/role-gated route guards)
* **Axios** (Centralized HTTP client with automated Bearer authorization and token rotation interceptors)
* **Tailwind CSS v4** (Responsive privacy & cybersecurity visual theme)
* **React Hook Form** & **Zod** (Accessible forms with schema validation)
* **Recharts** (Access audit volume over time, per application, and per data type)
* **Lucide React** (Consistent iconography)
* **Vitest** & **Testing Library** (Unit and integration tests)

---

## Project Structure

```
frontend/
├── public/
│   ├── favicon.svg
│   └── icons.svg
├── src/
│   ├── assets/              # Static assets and icons
│   ├── components/
│   │   ├── applications/    # Application registration & secret dialogs
│   │   ├── consents/        # Consent request decision dialogs & status badges
│   │   ├── dashboard/       # Recharts audit charts & metrics
│   │   ├── navigation/      # Responsive collapsable sidebar navigation
│   │   ├── ui/              # Button, Dialog, DataTable, Field, PageHeader, StateView, StatusBadge
│   │   └── vault/           # Masked value reveal & vault record form
│   ├── context/
│   │   ├── AuthContext.tsx  # In-memory access token & rotating refresh token bootstrap
│   │   └── ToastContext.tsx # Centralized security & action notifications
│   ├── hooks/
│   │   ├── useAuth.ts       # Hook for current auth session and sign-in/out methods
│   │   ├── useResource.ts   # Declarative async data-fetching lifecycle hook
│   │   └── useToast.ts      # Toast notification dispatch hook
│   ├── layouts/
│   │   ├── AppLayout.tsx    # Dashboard layout with top navbar and responsive sidebar
│   │   └── AuthLayout.tsx   # Centered layout for login and registration forms
│   ├── pages/
│   │   ├── admin/           # AdminDashboard, AdminUsers, AdminApplications, AdminAudit, AdminSecurityAlerts
│   │   ├── alerts/          # User SecurityAlertsPage
│   │   ├── applications/    # User ApplicationsPage with dynamic consent counts
│   │   ├── audit/           # User AuditPage with multidimensional filtering
│   │   ├── auth/            # LoginPage and RegisterPage
│   │   ├── consents/        # ConsentsPage (Active/Expired) and ConsentRequestsPage (Pending)
│   │   ├── dashboard/       # DashboardPage with real API statistics & audit charts
│   │   ├── profile/         # ProfilePage with account details & vault shortcuts
│   │   └── vault/           # VaultPage (AES-GCM personal records with client masking)
│   ├── routes/
│   │   └── guards.tsx       # ProtectedRoute, AdminRoute, PublicOnlyRoute
│   ├── services/
│   │   ├── api.ts           # Axios instance, interceptors, error handling, unwrap helper
│   │   ├── authService.ts   # Register, login, refresh, and logout
│   │   ├── vaultService.ts  # CRUD for personal data vault records
│   │   ├── consentService.ts# Request, list, grant, deny, and revoke consents
│   │   ├── applicationService.ts # Client application registration and management
│   │   ├── auditService.ts  # Paginated user and admin audit access logs
│   │   ├── securityService.ts # User and admin security alert monitoring and resolution
│   │   └── sessionStore.ts  # In-memory access token and sessionStorage refresh token
│   ├── types/
│   │   ├── admin.ts         # Admin aggregated state models
│   │   ├── api.ts           # DTOs aligned with Spring Boot backend entities
│   │   └── session.ts       # Authenticated session user interfaces
│   ├── utils/
│   │   ├── format.ts        # Masking algorithms, date formatting, and labels
│   │   └── options.ts       # Supported DataType enums and operations
│   ├── App.tsx              # Router configuration and global provider nesting
│   ├── main.tsx             # React DOM root entrypoint
│   └── index.css            # Dark cybersecurity theme styles & typography
├── .env.example             # Base configuration template
├── eslint.config.js         # ESLint configuration
├── package.json             # NPM dependencies and run scripts
├── tsconfig.json            # Strict TypeScript configuration
└── vite.config.js           # Vite and Vitest configuration
```

---

## Environment Configuration

For local development, create a `.env` file in `frontend/` (or copy from `.env.example`):

```bash
VITE_API_BASE_URL=http://localhost:8080/api
```

For production, set `VITE_API_BASE_URL` in the frontend host's build environment to the deployed API URL, for example `https://<your-api-service>.onrender.com/api`. Production builds fail if this value is missing rather than silently targeting localhost or an unrelated API. Keep `.env` out of source control; this base URL is public configuration, not a secret.

---

## Backend APIs Consumed

All endpoints match the Spring Boot backend contracts and envelopes:

### Authentication (`/api/auth`)
* `POST /api/auth/register` — Register a new account (`fullName`, `email`, `password`, optional `phoneNumber`)
* `POST /api/auth/login` — Authenticate with `email` and `password`, receives JWT access token and refresh token
* `POST /api/auth/refresh` — Rotate refresh token and obtain a fresh access token
* `POST /api/auth/logout` — Revoke active refresh token on the server

### Personal Data Vault (`/api/vault/data`)
* `GET /api/vault/data` — List authenticated user's encrypted vault records
* `GET /api/vault/data/{id}` — Fetch a specific vault record
* `POST /api/vault/data` — Store a new personal data item (`dataType`, `value`, `description`)
* `PUT /api/vault/data/{id}` — Update an existing vault record
* `DELETE /api/vault/data/{id}` — Delete a record from the vault

### Consent Management (`/api/consents`)
* `GET /api/consents` — List user's active, expired, denied, and pending consents
* `GET /api/consents/{id}` — View single consent details
* `POST /api/consents/request` — Create a new pending consent request
* `PUT /api/consents/{id}/grant` — User approves a pending consent request
* `PUT /api/consents/{id}/deny` — User denies a pending consent request
* `PUT /api/consents/{id}/revoke` — User revokes an active granted consent

### Third-Party Applications (`/api/apps`)
* `GET /api/apps` — List client applications registered by the user
* `POST /api/apps` — Register a new application (returns one-time `clientSecret`)
* `PUT /api/apps/{id}` — Update application details (`applicationName`, `description`, `redirectUri`)
* `DELETE /api/apps/{id}` — Disable an owned application

### Access Audit History (`/api/audit/logs`)
* `GET /api/audit/logs?page=0&size=100` — Paginated access ledger for user's data
* `GET /api/audit/logs/{userId}` — Admin access to a specific user's audit records

### Security Alerts (`/api/security-alerts`)
* `GET /api/security-alerts` — List alerts for suspicious or repeated unauthorized access
* `PUT /api/security-alerts/{id}/resolve` — Mark an alert as resolved

### Administration (`/api/admin`)
* `GET /api/admin/users` — List all registered users
* `GET /api/admin/apps` — List all system client applications
* `GET /api/admin/consents` — List all system consent records
* `GET /api/admin/audit?page=0&size=100` — System-wide paginated audit trail
* `GET /api/admin/alerts` — System-wide security alerts
* `PUT /api/admin/alerts/{id}/resolve` — Admin resolution of an alert
* `PUT /api/admin/apps/{id}/disable` — Admin disabling of an application

---

## Security UX & Privacy Implementation

1. **Client-side Masking by Default**: Sensitive values (`EMAIL`, `PHONE`, `NATIONAL_ID`, `PASSPORT`, `FINANCIAL_INFORMATION`) are masked by default upon load (e.g. `j•••@example.com`, `••••••1234`). Values are only revealed in the UI upon explicit user interaction via the reveal toggle.
2. **Ephemeral Access Tokens**: Access tokens are kept strictly in memory (`activeSession`). They are never persisted to `localStorage` to mitigate token theft via XSS.
3. **Automatic Token Rotation**: Axios response interceptors transparently handle `401 Unauthorized` responses by queuing a refresh request to `/api/auth/refresh` using the single-use refresh token, updating the active session, and retrying the failed request seamlessly.
4. **No Password Persistence**: Passwords are never retained in React state or localStorage, nor exposed in URLs or logged to console.
5. **Role-Gated Routes**: Administrative views (`/admin/*`) are protected using `<AdminRoute />`, checking the verified user role before rendering.

---

## How to Run Frontend

### Prerequisites
* Node.js 18+ (Node.js 20+ recommended)
* npm 9+

### Commands

```powershell
# 1. Navigate to frontend directory
cd frontend

# 2. Install dependencies
npm install

# 3. Verify TypeScript types
npm run typecheck

# 4. Run automated tests
npm test

# 5. Check code quality and style
npm run lint

# 6. Build production bundle
npm run build

# 7. Start local development server (http://localhost:5173)
npm run dev
```
