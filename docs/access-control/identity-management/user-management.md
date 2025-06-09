# User Management

There are three ways to manage users in Aidbox:

1. Manage Users in Aidbox
2. Manage Users in external systems
3. Automatically create users from external systems

## Manage Users in Aidbox

Aidbox user management is based on the `User` resource.\
A `User` represents an application‑level identity and is completely stored and controlled inside the Aidbox database. Each user record contains the login name, a hashed password, an active flag, and a link to FHIR user (Practitioner or Patient), and some additional fields that determine the user's effective permissions through `AccessPolicy` rules.

Because everything is a resource, you can manage users the same way you manage clinical data: create [profiles](../../modules/profiling-and-validation/#what-is-profiling) to apply validation, versioned history, transactions, \_history, etc.

See also:

* [User resource reference](../../reference/system-resources-reference/iam-module-resources.md#user)
* [Creating user tutorial](../../tutorials/security-access-control-tutorials/creating-user-and-set-up-full-user-access.md)
* [How to prohibit user to login](../../tutorials/security-access-control-tutorials/prohibit-user-to-login.md)

## Manage Users in external systems

If you already have an identity provider, you can delegate authentication to it and keep all identities outside Aidbox. Aidbox becomes a Service Provider that trusts the external IdP and focuses on authorization.

Two building blocks are involved:

* `IdentityProvider` – resource to configure an external identity provider. Log in to Aidbox UI with SSO. When users hit the Aidbox UI, they are redirected to the IdP; after successful login, the IdP posts an ID‑token back to Aidbox, establishing a browser session.
* `TokenIntrospector` – For API access, point your application to the IdP directly, then present the resulting JWT/Opaque token to Aidbox. The TokenIntrospector resource tells Aidbox how to verify the token signature.

See also:

{% content-ref url="../authentication/sso-with-external-identity-provider.md" %}
[sso-with-external-identity-provider.md](../authentication/sso-with-external-identity-provider.md)
{% endcontent-ref %}

{% content-ref url="../authentication/token-introspector.md" %}
[token-introspector.md](../authentication/token-introspector.md)
{% endcontent-ref %}

## Automatically create users from external systems

In some cases, you want to authenticate with an external IdP and still have a corresponding User resource inside Aidbox for auditing, patient‑to‑user mapping, or granular AccessPolicy rules. Aidbox supports just‑in‑time (JIT) user provisioning – it transparently creates a local user record the first time a foreign identity appears.
