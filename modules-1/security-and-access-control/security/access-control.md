---
description: >-
  AccessPolicy resources are used to check request objects against a set of
  rules
---

# AccessPolicy

### Resource structure

`AccessPolicy` resource has the following structure:

```yaml
resourceType: AccessPolicy
description: policy description text

# type of evaluation engine
engine: allow | json-schema | sql | matcho | complex | allow-rpc | matcho-rpc

# engine specific fields
# e.g. `schema` for `json-schema` engine
schema: {}

# References to either Client, User or Operation resources
link:
- { resourceType: Operation, id: op-id }
- { resourceType: User, id: user-1 }
- { resourceType: Client, id: client-1 }
```

It supports various [evaluation engines](evaluation-engines.md): _Allow_, _JSON Schema_, _SQL_, _Matcho_ and _Complex_ as well as _Allow-RPC_ and _Matcho-RPC_. They specify how checks are implemented — with an SQL statement, with a JSON Schema or as a list of allowed endpoints.

### Request object structure

Aidbox evaluates `AccessPolicy` against a **request object** which represents incoming HTTP request. It has the following structure:

```yaml
# Request method (get/post/put/delete)
request-method: get

# Request scheme (http or https)
scheme: http

# Request URI (no query string)
uri: /fhir/Patient

# Query string
query-string: __debug=policy

# Parsed query-string params merged with URL params extracted by routing engine
params: {__debug: policy, type: Patient}

# Request body (for POST, PUT or PATCH)
body: null

# Parsed JWT claims (if any)
jwt: {sub: xxxxxxx, jti: xxxxxxx, iss: aidbox,
  iat: 15409xxxxx, exp: 15409xxxxxa

# Object containing current User resource (if any)
user:
  data:
    patient_id: 42
  email: foo@foo.com
  id: b66bc7c5-1a56-422f-8cf8-e64469135ce2
  meta:
    lastUpdated: '2018-10-31T13:43:18.566Z'
    tag:
    - code: updated
      system: https://aidbox.io
    versionId: '6'
  phone: 123-123-123
  resourceType: User

# Client’s IP address
remote-addr: 10.128.0.6

# Client resource (if any)
client:
  id: b4930671-410c-462b-8b12-23cdef91af0c
  meta:
    lastUpdated: '2018-10-31T13:38:08.982Z'
    tag:
    - code: created
      system: https://aidbox.io
    versionId: '4'
  resourceType: Client

# Request headers
headers: {x-original-uri: '/fhir/Patient?__debug=policy', origin: 'https://ui.aidbox.app',
  x-forwarded-host: xxxx.aidbox.app, host: xxxx.aidbox.app, user-agent: 'Mozilla/5.0
    (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100
    Safari/537.36', content-type: 'text/yaml, application/json', x-forwarded-port: '443',
  referer: 'https://ui.aidbox.app/', connection: close, accept: text/yaml, accept-language: 'en-US,en;q=0.9,ru;q=0.8',
  authorization: Bearer xxxxxx,
  x-forwarded-for: 10.128.0.6, accept-encoding: 'gzip, deflate, br', x-forwarded-proto: https,
  x-scheme: https, x-real-ip: 10.128.0.6}
```

### Request authorization logic

`AccessPolicy` instance can be linked to `User`, `Client` or `Operation` resources with `AccessPolicy.link` attribute. If `AccessPolicy` has no links, it’s considered a global policy. To authorize a request, Aidbox uses global policies in additiion to all `AccessPolicy` rules linked to `User`, `Client` and `Operation` associated with the current request.

It iterates through them, evaluating each `AccessPolicy` against the request object. As soon as one of them passes the validation (evaluation result is `true`), the request is considered authorized and Aidbox stops further policies evaluation. If all policies fail to validate it (all of them evaluate to `false`), then Aidbox denies the request and responds with `403 Unauthorized`.

{% hint style="info" %}
**Performance consideration** Link your policy to `User`, `Client` or `Operation`. Otherwise it will be evaluated for every request increasing the number of checks.
{% endhint %}

If there are no `AccessPolicy` resources defined for a box, all requests will be denied.

### Signed RPC policy token

You can create policy-token to access rpc without creating AccessPolicy resource

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

This RPC method will return you a JWT token, which can be used only to call two methods with params you described:

* `aidbox.sdc/read-document`
* `aidbox.sdc/save-document`

To make a call RPC with this token just pass it in body:

```javascript
POST {{base}}/rpc

method: aidbox.sdc/read-document
params:
  id: doc-1
policy: <token from previous step>
```
