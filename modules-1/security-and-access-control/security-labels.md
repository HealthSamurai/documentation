---
description: This pages explains how security labels access control control works in Aidbox
---

# Security Labels

## What are Security Labels?

Security Labels is a set of permissions associated with the request. When security labels are present in the request context, the requester is allowed to gather information according to the security labels.

Two security label code systems are currently supported:

1. http://terminology.hl7.org/CodeSystem/v3-Confidentiality
2. http://terminology.hl7.org/CodeSystem/v3-ActCode

## Security Labels in the request context

There are two ways of the security labels appear to the request context:

1. JWT’s `scope` claim
2. Aidbox User’s property `securityLabel`

### From the scope claim in JWT <a href="#docs-internal-guid-71b74bb2-7fff-d9f8-f70b-bfac58a2d392" id="docs-internal-guid-71b74bb2-7fff-d9f8-f70b-bfac58a2d392"></a>

Aidbox parses the `scope` claim and fetches security labels. There can be multiple security labels on the scope.

A security label must be defined using the pattern `system|code`. For example, `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY.`

### From the User.securityLabel

If the request context is associated with a Aidbox user, Aidbox tries to get security labels from the `User.securityLabel`.&#x20;

For example, the user resource containing two security labels.

```yaml
resourceType: User
id: some-user-id
securityLabel:
  - system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    code: PSY
  - system: https://terminology.hl7.org/CodeSystem/v3-Confidentiality
    code: M
```
