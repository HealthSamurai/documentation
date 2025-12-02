# Debug Access Control

There are 5 different ways to debug access control in Aidbox:

* Access policy dev tool
* **\_\_debug** search parameter
* **x-debug** header
* **su** header
* **/auth/test-policy** endpoint

We recommend using Access Policy Dev Tool.

## Access policy dev tool

Access policy dev tool simplifies development and debugging AccessPolicies. It was introduced in March v2303 release of Aidbox.

<figure><img src="../../.gitbook/assets/282099be-926f-4dc0-a525-0fec435c5e3b (1).png" alt="Access policy dev tool interface showing editor and results"><figcaption><p>Access policy dev tool UI</p></figcaption></figure>

The dev tool is a part of Aidbox UI Console, which aims

* to edit AccessPolicy resource, and
* to give a nice view for AccessPolicy debug output for a specific request in the same place.

The dev tool is split into two sides, the editor side and the result side. On the left side, you define

* [AccessPolicy resource](../../access-control/authorization/access-policies.md) and
* HTTP request you are going to debug.

When you press the **Save & Run** button, the dev tool saves AccessPolicy and performs policy debug operation for the specified request, and displays the result on the right side. You can see there

* _Evaluate policy result._ List of all access policies and the result of evaluation.
* _Parsed HTTP request._ It's an internal representation of the request, which Aidbox passes to the eval-policy function.

To use the Access policy dev tool, [Aidbox Development mode](../../reference/all-settings.md#security.dev-mode) setting must be enabled.

### Sending the request as a user

By default, Aidbox sends requests with your current session (your identity and permissions). To authenticate with another session, add an `Authorization` header to the request, e.g.

```
GET /fhir/Patient
Authorization: Bearer eyJ...w5c
```

## `__debug` query-string parameter

There is a special query-string parameter `__debug=policy` you can pass to every Aidbox request. It will toggle debug mode for Request Authorization Layer, and in this mode, instead of the actual response client will get an object containing:of the

* full request object;
* an array of existing AccessPolicies;
* evaluation result for every AccessPolicy (under `AccessPolicy.evalResult`).

To use the header, [Aidbox Development mode](../../reference/all-settings.md#security.dev-mode) setting must be enabled.

## `x-debug: policy` request header

For requests with the `x-debug: policy` header, details of access policy evaluation will be logged.

```yaml
GET /fhir/Patient
x-debug: policy

# in aidbox logs
# :auth/trace-policy {:access-policy-id :policy-1, :policy-type "sql", ...
# :auth/trace-policy {:access-policy-id :policy-2, :policy-type "json-schema",...
```

To use the header, [Aidbox Development mode](../../reference/all-settings.md#security.dev-mode) setting must be enabled.

## `su` request header

`su` header allows to switch user on behalf of whom request is executed. Use `su=<user/client id>` to check how access control works for that user.

To use the header, [SU enable setting](../../reference/all-settings.md#security.debug-su-enable) must be enabled.

{% hint style="info" %}
`su` header is only available to admin users, who have at least one `AccessPolicy` with `engine` = `allow` linked to them.
{% endhint %}

Usual request without the header as an admin:

```http
GET /fhir/Patient

# Response:
Status: 200
```

Request on behalf of User `myid` who has no access to the Patient search:

```http
GET /fhir/Patient
su: myid

# Response
Status: 403
```

## `/auth/test-policy` Operation

You can use a special operation `POST /auth/test-policy` to design AccessPolicy without creating it.

Post on the `/auth/test-policy` with:

* a simulated **request** (you can provide existing `user-id` and `client-id`),
* policy code in the **policy** attribute.

If you want to test JWT auth, put your token in the `headers.authorization` with the `Bearer` prefix. In this case, the token will be parsed, and its claims appear in the `request.jwt`.

JWT in a header is parsed but not validated.

This allows you to test JWT policy without **TokenIntrospector** registration.

```yaml
POST /auth/test-policy

request:
  uri: '/fhir/Patient'
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
```

The response contains a result of evaluated policy.

```yaml
request:
  uri: /fhir/Patient
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
policy:
  engine: sql
  sql: {query: 'SELECT {{user.role}} FROM {{!params.resource/type}}'}
result:
  eval-result: false
  query: ['SELECT ? FROM "patient"', admin]
```

## See also

{% content-ref url="accesspolicy-best-practices.md" %}
[accesspolicy-best-practices.md](accesspolicy-best-practices.md)
{% endcontent-ref %}
