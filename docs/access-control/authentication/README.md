---
description: Verify identity of clients with Basic Auth, OAuth 2.0, JWT tokens, SSO, and token introspection in Aidbox.
---

# Authentication

Authentication is verifying the identity of clients accessing Aidbox APIs and services. In healthcare systems, authentication is crucial as it ensures that only authorized users, applications, and services can access sensitive medical data. This includes healthcare providers accessing patient records, third-party applications integrating with your FHIR API, administrative staff using Aidbox Console, and automated system operations services.

## Basic HTTP Authentication

Basic Authentication is a simple username/password authentication suitable for development and testing environments.

{% content-ref url="basic-http-authentication.md" %}
[basic-http-authentication.md](basic-http-authentication.md)
{% endcontent-ref %}

## OAuth 2.0

OAuth 2.0 is the industry-standard protocol for authorization. Aidbox provides comprehensive support for OAuth 2.0 flows:

* Authorization Code Flow
* Client Credentials Flow
* Resource Owner Password Flow
* Implicit Flow (legacy)

Learn more about OAuth 2.0 support:

{% content-ref url="oauth-2-0.md" %}
[oauth-2-0.md](oauth-2-0.md)
{% endcontent-ref %}

## Token Introspection

Validate and inspect OAuth 2.0 tokens issued by external Identity Providers.

{% content-ref url="token-introspector.md" %}
[token-introspector.md](token-introspector.md)
{% endcontent-ref %}

## Authentication of Users Logging into the Aidbox Console UI

### Using Aidbox internal Identity Provider

Aidbox supports username/password authentication for login to the Aidbox Console UI.

See [Identity Management](../identity-management/#user-management) for managing Users and Passwords.

Two-factor authentication is also supported. See [Two Factor Authentication](two-factor-authentication.md) for more details.

### Single Sign-On (SSO) with external Identity Provider

Integrate with external Identity Providers (IdPs) for SSO to Aidbox Console UI.

{% content-ref url="sso-with-external-identity-provider.md" %}
[sso-with-external-identity-provider.md](sso-with-external-identity-provider.md)
{% endcontent-ref %}
