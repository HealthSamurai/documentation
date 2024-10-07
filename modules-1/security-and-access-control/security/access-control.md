---
description: >-
  AccessPolicy resources are used to check request objects against a set of
  rules
---

# AccessPolicy

AccessPolicy is an Aidbox custom resource representing a set of checks for the request.

If there are no `AccessPolicy` resources defined in Aidbox, all requests will be denied.

If AccessPolicies exist, Aidbox iterates through them, evaluating each `AccessPolicy` against the request object. As soon as one of them passes the validation (the evaluation result is `true`), the request is considered authorized and Aidbox stops further policies evaluation. If all policies fail to validate it (all of them evaluated to `false`), then Aidbox denies the request and responds with `403 Forbidden`.

The structure of the AccessPolicy resource and the request object are described below.

### AccessPolicy resource structure

`AccessPolicy` resource has the following structure:

```yaml
resourceType: AccessPolicy
description: policy description text

# type of evaluation engine
engine: allow | matcho| sql | json-schema | complex | allow-rpc | matcho-rpc

# engine-specific fields
# e.g. `schema` for `json-schema` engine
schema: {}

# References to either Client, User, or Operation resources
link:
- { resourceType: Operation, id: op-id }
- { resourceType: User, id: user-1 }
- { resourceType: Client, id: client-1 }
```

It supports various [evaluation engines](evaluation-engines.md): _Allow_, _JSON Schema_, _SQL_, _Matcho,_ and _Complex_ as well as _Allow-RPC_ and _Matcho-RPC_. They specify how checks are implemented — with an SQL statement, with a JSON Schema, or as a list of allowed endpoints.

### Request object structure

Aidbox evaluates `AccessPolicy` against a **request object** that represents an incoming HTTP request. It has the following structure:

```yaml
# Request method (get/post/put/delete)
request-method: get

# Request scheme (http or https)
scheme: http

# Request URI (no query string)
uri: /fhir/Patient

# Query string
query-string: name=John

# Parsed query-string params merged with URL params extracted by the routing engine
params: {name: John, type: Patient}

# Request body (for POST, PUT or PATCH)
body: null

# Parsed JWT claims (if any)
jwt: {sub: xxxxxxx, jti: xxxxxxx, iss: aidbox, iat: 15409xxxxx, exp: 15409xxxxxa}

# Object containing current User resource (if any)
user:
  data:
    patient_id: 42
  email: foo@foo.com
  id: b66bc7c5-1a56-422f-8cf8-e64469135ce2
  phone: 123-123-123
  resourceType: User
  # ...

# Client’s IP address
remote-addr: 10.128.0.6

# Client resource (if any)
client:
  id: b4930671-410c-462b-8b12-23cdef91af0c
  resourceType: Client
  # ...

# Request headers
headers: {x-original-uri: '/fhir/Patient?name=John' ...}
```

### AccessPolicy links

`AccessPolicy` instance can be linked to `User`, `Client` or `Operation` resources with the`AccessPolicy.link` field. If `AccessPolicy` has no links, it’s considered a global policy. To authorize a request, Aidbox uses global policies in addition to all `AccessPolicy` rules linked to `User`, `Client` and `Operation` associated with the current request.

{% hint style="info" %}
**Performance consideration**&#x20;

Link your policy to `User`, `Client` or `Operation`. Otherwise, it will be evaluated for every request increasing the number of checks.
{% endhint %}

### Signed RPC policy token

You can create policy-token to access RPC without creating an AccessPolicy resource

To do that call `aidbox.policy/create-policy-token` RPC method:

```javascript
POST {{base}}/rpc

method: aidbox.policy/create-policy-token
params:
  expiration: 3
  methods:
    aidbox.sdc/read-document:
      params:
        id: doc-1
    aidbox.sdc/save-document:
      params:
        id: doc-1
```

This RPC method will return you a JWT token, which can be used only to call two methods with the params you described:

* `aidbox.sdc/read-document`
* `aidbox.sdc/save-document`

To make a call RPC with this token, just pass it in the body:

```javascript
POST {{base}}/rpc

method: aidbox.sdc/read-document
params:
  id: doc-1
policy: <token from previous step>
```
