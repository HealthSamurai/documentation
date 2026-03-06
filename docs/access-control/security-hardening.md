---
description: CORS, security headers, cookies, session management, and infrastructure security for Aidbox.
---

# Security Hardening

This page describes infrastructure-level security features in Aidbox: CORS, security headers, cookie flags, session management, and what is not implemented. Use it together with [Access Policies](authorization/access-policies.md), [Audit and Logging](audit-and-logging.md), and [User management](identity-management/user-management.md) for a complete security picture.

## CORS

Cross-Origin Resource Sharing (CORS) is **enabled by default**. Aidbox supports:

* **Wildcard mode** — `BOX_SECURITY_CORS_ORIGINS=*` (default). Any `Origin` header is echoed back in `Access-Control-Allow-Origin`. Use only in development or when all origins are trusted.
* **Whitelist mode** — Set `BOX_SECURITY_CORS_ORIGINS` to a comma-separated list of origins, e.g. `https://app.example.com,https://trusted.example.com`. Only those origins receive CORS headers; requests from other origins get no `Access-Control-*` headers (but the API still responds).
* **Disabled** — Set `BOX_SECURITY_CORS_ENABLED=false` to disable CORS entirely.

Preflight `OPTIONS` requests are handled: allowed methods and requested headers are reflected in `Access-Control-Allow-Methods` and `Access-Control-Allow-Headers`. Credentials are supported (`Access-Control-Allow-Credentials: true`). Settings can be changed at runtime via the [Settings API](../reference/all-settings.md) (or environment variables).

See [security.cors.enabled](../reference/all-settings.md#security.cors.enabled) and [security.cors.origins](../reference/all-settings.md#security.cors.origins).

## Cookie security

Session cookies set by Aidbox (e.g. after login) use:

* **HttpOnly** — set, to reduce XSS-based cookie theft
* **SameSite=Lax** — set, to reduce CSRF from cross-site requests

The **Secure** flag is not set by Aidbox. When Aidbox is behind HTTPS (e.g. reverse proxy), consider configuring the proxy or application to set `Secure` on cookies if your deployment supports it.

## Session and token management

* **Session expiration** — Default session (cookie) lifetime is configurable (e.g. via AuthConfig). Expired sessions are cleaned up automatically.
* **Access token expiration** — Configurable per [Client](identity-management/application-client-management.md) (e.g. `auth.client_credentials.access_token_expiration`). Tokens are rejected after expiry (401).
* **Sessions list** — Active sessions can be inspected via the `Session` resource (if available in your setup).

See [OAuth 2.0](authentication/oauth-2-0.md) and [Client management](identity-management/application-client-management.md).

## What is not implemented

The following are **not** implemented in Aidbox. Rely on operational measures or reverse proxy where needed:

| Feature | Description |
|--------|-------------|
| **Rate limiting** | No request throttling on API or auth endpoints. Use a reverse proxy or WAF for rate limiting. |
| **Brute-force protection** | No automatic account lockout after failed login attempts. Use the [User.inactive](../tutorials/security-access-control-tutorials/prohibit-user-to-login.md) flag to lock accounts manually, or implement throttling at the proxy. |
| **HSTS** | Header not set. Add at reverse proxy when using HTTPS. |
| **X-Content-Type-Options / Referrer-Policy** | Not set. Add at reverse proxy for defense in depth. |

## Infrastructure recommendations

* **TLS** — Terminate TLS at a reverse proxy or load balancer. Do not expose Aidbox directly to the internet without encryption.
* **Reverse proxy** — Use nginx, Traefik, or similar to add security headers (HSTS, X-Content-Type-Options, Referrer-Policy), rate limiting, and DDoS mitigation.
* **Network** — Restrict access to Aidbox and the database to trusted networks where possible.

## See also

* [All settings (CORS, CSP)](../reference/all-settings.md) — `security.cors.*`, `security.content-security-policy-header`
* [Audit and Logging](audit-and-logging.md)
* [User management](identity-management/user-management.md) — password and lockout
* [Prohibit user to login](../tutorials/security-access-control-tutorials/prohibit-user-to-login.md) — manual lockout via `User.inactive`
