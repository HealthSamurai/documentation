---
description: Manage user identities with Aidbox User resource or delegate to external identity providers like Okta, Google, Auth0.
---

# User management

There are two ways to manage users in Aidbox:

1. Manage Users in Aidbox
2. Manage Users in external systems

## Manage Users in Aidbox

Aidbox user management is based on the [`User`](../../reference/system-resources-reference/core-module-resources.md#user) resource.\
A `User` represents an application‑level identity and is completely stored and controlled inside the Aidbox database. Each user record contains the login name, a hashed password, an active flag, and a link to FHIR user (Practitioner or Patient), and some additional fields that determine the user's effective permissions through [`AccessPolicy`](../../reference/system-resources-reference/core-module-resources.md#accesspolicy) rules.

Because everything is a resource, you can manage users the same way you manage clinical data: create [profiles](../../modules/profiling-and-validation/#what-is-profiling) to apply validation, versioned history, transactions, \_history, etc.

### Password management

Passwords are managed through the standard CRUD API on the `User` resource. Aidbox automatically hashes passwords using scrypt before storing them.

#### Creating a user with a password

```http
PUT /User/my-user
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "my-secret-password"
}
```

The response contains the scrypt hash (format `$s0$...`) instead of the plaintext password.

#### Changing a password

Update the `password` field via `PUT` or `PATCH`:

```http
PATCH /User/my-user
Content-Type: application/json

{
  "password": "new-secret-password"
}
```

{% hint style="warning" %}
Password changes do **not** require the current password. Any client with write access to the `User` resource can change any user's password. Protect the `User` resource with an [AccessPolicy](../authorization/access-policies.md) that restricts write access.
{% endhint %}

When audit logging is enabled, password changes generate an AuditEvent with DICOM subtype `110139` ("User password changed"). See [Audit and Logging](../audit-and-logging.md) for details.

### Security considerations

{% hint style="warning" %}
**Password hash exposure**: `GET /User/:id` and `GET /User?` return the scrypt password hash in the response body. Only the `/auth/userinfo` endpoint strips the password field. Use an AccessPolicy to restrict read access to the `User` resource for non-admin clients.
{% endhint %}

{% hint style="info" %}
**No automatic account lockout**: Aidbox does not lock accounts after repeated failed login attempts. The only lockout mechanism is manually setting `User.inactive` to `true`. See [Prohibit user to login](../../tutorials/security-access-control-tutorials/prohibit-user-to-login.md).
{% endhint %}

{% hint style="info" %}
**No built-in password policies**: Aidbox does not enforce password complexity, expiry, or reuse rules. Implement these checks in your application layer before calling the Aidbox API.
{% endhint %}

See also:

* [User resource reference](../../reference/system-resources-reference/core-module-resources.md#user)
* [Creating user tutorial](../../tutorials/security-access-control-tutorials/creating-user-and-set-up-full-user-access.md)
* [How to prohibit user to login](../../tutorials/security-access-control-tutorials/prohibit-user-to-login.md)

## Manage Users in external systems

If you already have an identity provider, you can delegate authentication to it and keep all identities outside Aidbox. Aidbox becomes a Service Provider that trusts the external IdP and focuses on authorization.

Two building blocks are involved:

* `IdentityProvider` – resource to configure an external identity provider. Log in to Aidbox UI with SSO. When users hit the Aidbox UI, they are redirected to the IdP; after successful login, the IdP posts an ID‑token back to Aidbox, establishing a browser session. Aidbox retrieves user data from external identity provider and stores it in the User resource.
* `TokenIntrospector` – For API access, point your application to the IdP directly, then present the resulting JWT/Opaque token to Aidbox. The TokenIntrospector resource tells Aidbox how to verify the token signature.

See also:

{% content-ref url="../authentication/sso-with-external-identity-provider.md" %}
[sso-with-external-identity-provider.md](../authentication/sso-with-external-identity-provider.md)
{% endcontent-ref %}

{% content-ref url="../authentication/token-introspector.md" %}
[token-introspector.md](../authentication/token-introspector.md)
{% endcontent-ref %}

### Automatically create users from external systems
In some cases, you want to authenticate with an external IdP and still have a corresponding User resource inside Aidbox for auditing, patient‑to‑user mapping, or granular AccessPolicy rules. Aidbox supports just‑in‑time user provisioning:
- In case of SSO, Users are created automatically.
– In case of API access, it is possible to create User at first request using [setting](../../reference/all-settings.md#security.introspection-create-user).

See also:

{% content-ref url="../../tutorials/security-access-control-tutorials/keycloak-auto-create-user.md" %}
[keycloak-auto-create-user.md](../../tutorials/security-access-control-tutorials/keycloak-auto-create-user.md)
{% endcontent-ref %}
