---
description: Configure Client resources for programmatic API access with OAuth 2.0 flows, Basic Auth, and granular permissions.
---

# Application/Client Management

To provide programmatic access to the Aidbox API, you have to use a [`Client`](../../reference/system-resources-reference/iam-module-resources.md#client) resource. The primary purpose of the `Client` resource is to facilitate authentication and access control within Aidbox.

In Aidbox, a `Client` resource represents an application or service that needs programmatic access to the FHIR server. Clients authenticate using Basic authentication or OAuth2 flows.

Unlike `User` resources (which represent human users), `Client` resources represent:

* Backend services
* Mobile applications
* Web applications
* Integration engines
* Other FHIR servers

Each Client has a unique identity (client ID) and authentication credentials (such as a client secret), enabling Aidbox to identify the requesting application and apply appropriate access controls.

Example of a `Client` resource:

```json
{
  "resourceType": "Client",
  "id": "my-client",
  "secret": "my-client-secret",
  "grant_types": ["basic"]
}
```

## Client Access Control

Clients are subjects in the Aidbox access control system. This means:

1. Each Client can be linked to specific AccessPolicy resources
2. Different Clients can have different permissions
3. API access is restricted based on the authenticated Client's permissions

This granular control allows you to limit what each application can do in your Aidbox instance. For example, a reporting application might only have read access to specific resources, while an EHR system might have broader read/write permissions.

## Common Client Use Cases

### API Integration Clients

Services that need to exchange data with Aidbox programmatically:

* EHR systems
* Analytics platforms
* Health information exchanges

### Single-Page Applications

JavaScript applications running in a browser:

* Patient portals
* Administrative dashboards
* Clinical viewers

### Mobile Applications

Native apps on mobile devices:

* Patient apps
* Provider apps
* Care management tools

### Backend Services

Server-side applications performing automated tasks:

* Notification services
* Data processing pipelines
* Reporting engines

## Security Considerations

When creating and managing Clients, consider these security best practices:

* Assign the minimum necessary permissions using [`AccessPolicies`](../../access-control/authorization/access-policies.md)
* Use secure client secrets (high entropy, regularly rotated)
* For public clients (like SPAs), use [authorization\_code](../../tutorials/security-access-control-tutorials/authorization-code-grant.md) with [PKCE](https://datatracker.ietf.org/doc/html/rfc7636)
* Specify allowed redirect URIs for OAuth2 flows
* Consider token lifetimes and refresh policies
