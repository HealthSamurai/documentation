---
description: Access control debug tools available in Aidbox
---

# Debug

Aidbox offers multiple ways to debug access policies:

### `__debug` query-string parameter

There is a special query-string parameter `__debug=policy` you can pass to every Aidbox request. It will toggle debug mode for Request Authorization Layer, and in this mode instead of actual response client will get an object containing:

* full request object;
* an array of existing AccessPolicies;
* evaluation result for every AccessPolicy (under `AccessPolicy.evalResult`).

Define `AIDBOX_DEV_MODE` to enable `__debug` parameter

### `x-debug: policy` request header

For requests with the `x-debug: policy` header, details of access policy evaluation will be logged.

```yaml
GET /Patient
x-debug: policy

# in aidbox logs
# :auth/trace-policy {:access-policy-id :policy-1, :policy-type "sql", ...
# :auth/trace-policy {:access-policy-id :policy-2, :policy-type "json-schema",...
```

### `su` request header

`su` header allows to switch user on behalf of whom request is executed. Use `su=<user/client id>` to check how access control works for that user.

`su` header functionality can be enabled with `box_debug_su_enable` env

{% code title="Example" %}
```
box_debug_su_enable=true
```
{% endcode %}

{% hint style="info" %}
`su` header is only available to admin users, who have at least one `AccessPolicy`  with `engine` = `allow` linked to them.&#x20;
{% endhint %}

#### Example

```http
# Request as an admin:
GET /fhir/Patient

# Response:
Status: 200
```

```http
# Request on behalf of `myid` User who has no access to Patient search:
GET /fhir/Patient
su: myid

# Response
Status: 403
```

### `/auth/test-policy` Operation

You can use a special operation `POST /auth/test-policy` to design policy without creating an AccessPolicy resource and for different users and clients. Post on the `/auth/test-policy` with a simulated **request** attribute (you can provide existing `user-id` and `client-id` — Aidbox will find and populate request) and temporal policy in the **policy** attribute. If you want to test JWT auth, put your token in the `headers.authorization` with the `Bearer` prefix — the token will be parsed and its claims appear in the `request.jwt`. JWT in a header is parsed but not validated. This allows you to test JWT policy without **TokenIntrospector** registration.

The response contains a result of evaluated policy.

```yaml
POST /auth/test-policy

-- simulate request document
request:
  uri: '/Patient'
  request-method: get
  headers:
    authorization: Bearer <your-jwt>
  user:
    data: {role: 'admin'}
  -- or 
  -- user-id: 'user-1' -- aidbox will find user in database
  client:
    id: basic
    grant_types: ['basic']
  -- or 
  -- client-id: 'client-1' -- aidbox will find client in database
    
policy:
  engine: sql
  sql:
    query: 'SELECT {{user.role}} FROM {{!params.resource/type}}'

-- response

request:
  uri: /Patient
  request-method: get
  user: {role: admin}
  params: {resource/type: Patient}
  #jwt: ...jwt-claims
operation:
  request:
  - get
  - {name: resource/type}
  action: proto.operations/search
  module: proto
  id: Search
-- original policy
policy:
  engine: sql
  sql: {query: 'SELECT {{user.role}} FROM {{!params.resource/type}}'}
-- result of policy evaluation
result:
  eval-result: false
  query: ['SELECT ? FROM "patient"', admin]
```
