---
description: Define fine-grained access control with AccessPolicy using Allow, Matcho, SQL, JSON Schema, or Complex engines.
---

# Access Policies

AccessPolicy is an Aidbox custom resource representing a set of checks for the request. AccessPolicy resources are used to check request objects against a set of rules. If there are no AccessPolicy resources defined in Aidbox, all requests will be denied.

If AccessPolicies exist, Aidbox iterates through them, evaluating each AccessPolicy against the request object. As soon as one of them passes the validation (the evaluation result is true), the request is considered authorized, and Aidbox stops further policy evaluation. If all policies fail to validate it (all of them evaluated to false), then Aidbox denies the request and responds with 403 Forbidden.

The structure of the AccessPolicy resource and the request object are described below.

## AccessPolicy resource structure

AccessPolicy resource has the following structure:

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

It supports various evaluation engines: Allow, JSON Schema, SQL, Matcho, and Complex, as well as Allow-RPC and Matcho-RPC. They specify how checks are implemented — with an SQL statement, with a JSON Schema, or as a list of allowed endpoints.

### Request object structure

Aidbox evaluates AccessPolicy against a request object that represents an incoming HTTP request. It has the following structure:

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

# Incoming FHIR resource (for POST, PUT or PATCH)
resource: null

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

## AccessPolicy links

AccessPolicy instance can be linked to User, Client or Operation resources with the AccessPolicy.link field. If AccessPolicy has no links, it’s considered a global policy. To authorize a request, Aidbox uses global policies in addition to all AccessPolicy rules linked to User, Client, and Operation associated with the current request.

{% hint style="info" %}
**Performance consideration.** Link your policy to User, Client or Operation. Otherwise, it will be evaluated for every request, increasing the number of checks.
{% endhint %}

See tutorials:

{% content-ref url="../../tutorials/security-access-control-tutorials/accesspolicy-best-practices.md" %}
[accesspolicy-best-practices.md](../../tutorials/security-access-control-tutorials/accesspolicy-best-practices.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/debug-access-control.md" %}
[debug-access-control.md](../../tutorials/security-access-control-tutorials/debug-access-control.md)
{% endcontent-ref %}

## Evaluation engines

Aidbox provides several ways to specify rules for AccessPolicy resources — so-called evaluation engines. They come with their syntax and offer varying degrees of flexibility and convenience for writing those rules.

There are five evaluation engines:

| Engine      | Description                                                                                     | Use Cases                                                                                                                               |
| ----------- | ----------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| Allow       | Simplest engine that allows everything for certain `User`, `Client` or makes `Operation` public | <p>- Testing and development environments<br>- Superuser/admin access<br>- Public endpoints</p>                                         |
| Matcho      | Mathes request context with a set of rules                                                      | - Most common access control scenarios (90% of cases)                                                                                   |
| JSON Schema | Uses JSON Schema validation for request objects                                                 | - For those familiar with JSON Schema                                                                                                   |
| SQL         | Uses SQL queries for access control logic                                                       | <p>- Complex data-dependent rules<br>- When you need to join multiple tables<br>- Performance-critical scenarios</p>                    |
| Complex     | Combines multiple engines and conditions                                                        | <p>- Multi-step validation workflows<br>- When you need to combine different types of checks<br>- Advanced access control scenarios</p> |

It is recommended to pick the Matcho engine. In 90% of cases, it is enough. Sometimes, the complex access policy can only be written by SQL or Complex engines.

### Allow engine

The Allow engine is the simplest. It allows all requests.

The allow engine always evaluates true regardless of the request object. Use it if you want to provide unrestricted access to everything.

Example:

```yaml
resourceType: AccessPolicy
id: this-policy-allows-everything
engine: allow
link:
- { resourceType: User, id: admin }
```

### Matcho engine

Matcho engine leverages [Matcho](https://github.com/HealthSamurai/matcho) pattern matching, it has compact and declarative syntax with limited expressivity. It is well-suited for writing all sorts of rules and thus is one of the easiest options to specify `AccessPolicy` checks.

To test the Matcho engine without creating AccessPolicy and sending a request, you can use the [$matcho endpoint](../../api/rest-api/other/matcho.md).

#### Example

In this example, the request is allowed only if:

* Uri is related to Encounter. E.g. `/fhir/Encounter` or `/Encounter`,
* HTTP request is either `GET` or `POST`, not `PUT`,
* `practitioner` parameter provided as a query string in case of the `GET` request or body params in case of the `POST` request must be equal to `practitioner_id` of the user,

User data, which is fetched from the "user" table, must contain the `inpatient` department and practitioner.

```yaml
resourceType: AccessPolicy
id: as-practitioner-who-works-in-inpatient-department-allowed-to-see-his-patients
engine: matcho
matcho:
  user: 
    department: inpatient
    # user.data.practitioner_id should be present
    data: 
      practitioner_id: present?
  # uri must match regexp /Encounter.*
  uri: '#/Encounter.*'
  request-method: {$enum: ['get', 'post']}
  params:
    practitioner: .user.data.practitioner_id 
```

See also:

{% content-ref url="../../reference/matcho-dsl-reference.md" %}
[matcho-dsl-reference.md](../../reference/matcho-dsl-reference.md)
{% endcontent-ref %}

### SQL engine

The `sql` engine executes an SQL statement and uses its return value as an evaluation result. Thus, the statement should return a single row with just one column:

```sql
SELECT true FROM patient WHERE id = {{jwt.patient_id}} LIMIT 1;
```

SQL statement can refer to request fields with a double curly braces interpolation. The string inside the braces will be used as a path to value in the request object.

The SQL engine is sometimes the only way to perform complex checks when the AccesPolicy needs to check data stored in the database but not in the request context.

#### Example

Suppose that we are checking a request that comes with a `User` resource referencing a `Practitioner` through `User.data.practitioner_id` element. The following policy allows requests only to `/fhir/Patient/<patient_id>` URLs and only to those patients who have a `Patient.generalPractitioner` element referencing the same practitioner as a `User` of our current request. In other words, `User` as a `Practitioner` is only allowed to see his own patients — those who reference him as their `generalPractitioner`.

```yaml
resourceType: AccessPolicy
id: practitioner-only-allowed-to-see-his-patients
engine: sql
sql:
  query: |
    SELECT
      {{user}} IS NOT NULL
      AND {{user.data.practitioner_id}} IS NOT NULL
      AND {{uri}} LIKE '/fhir/Patient/%'
      AND resource->'generalPractitioner' @>
    jsonb_build_array(jsonb_build_object('resourceType',
        'Practitioner', 'id', {{user.data.practitioner_id}}::text))
      FROM patient WHERE id = {{params.resource/id}};
```

#### Interpolation Rules

You can parameterize your SQL queries with request object using `{{path}}` syntax. For example, to get a user role provided `{user: {data: {role: "admin"}}}` you write `{{user.data.role}}`. Parameter expressions are escaped by default to protect from SQL injection.

For dynamic queries — to parameterize table name, for example — you have to use `{{!path}}` syntax. The expression `SELECT true from {{!params.resource/type}} limit 1` when a request contains `{params: {"resource/type": "Patient"}}` will be transformed into `SELECT true from "patient".` By default, identifier names are double-quoted and lower-cased.

#### More examples

In this example, `User` as a `Practitioner` is only allowed to see the conditions of his patients — those who reference him as their `generalPractitioner`.

```
PUT /AccessPolicy/practitioner-only-allowed-to-see-his-patients-conditions

resourceType: AccessPolicy
id: practitioner-only-allowed-to-see-his-patients-conditions
engine: sql
sql:
  query: |
    SELECT 1
    FROM condition c
    JOIN patient p ON (
      c.resource @> (
        $JSON${
          "subject":{
            "id":"$JSON$
            ||
            p.id
            ||
            $JSON$",
            "resourceType":"Patient"
          }
        }$JSON$
      )::jsonb
    )
    WHERE
      {{user}} IS NOT NULL
      AND ({{user.data.practitioner_id}})::text IS NOT NULL
      AND ({{uri}})::text LIKE '/fhir/Condition/%'
      AND p.resource @> (
        $JSON${
          "generalPractitioner":[{
            "id":"$JSON$
            ||
            ({{user.data.practitioner_id}})::text
            ||
            $JSON$",
            "resourceType":"Practitioner"
          }]
        }$JSON$
      )::jsonb
      AND c.id = {{params.resource/id}}
```

### JSON Schema

`json-schema` engine allows you to use [JSON Schema](https://json-schema.org) to validate the request object. It is specified under `schema` a field of `AccessPolicy` resource. The currently supported JSON Schema version is **draft-07**.

{% hint style="info" %}
Fields with empty values `— []`, `{}`, `""`, `null` — are removed from the request before access policy checks are applied. Make sure to specify all necessary fields as `required.`
{% endhint %}

{% hint style="info" %}
It is recommended to use Matcho engine instead of JSON Schema engine. Matcho engine is more expressive due to its special keys.
{% endhint %}

#### Example

The following policy requires `request.params.resource/type` to be present and have a value of `"Organization"`:

```yaml
resourceType: AccessPolicy
engine: json-schema
schema:
  properties:
    params:
      required: [resource/type]
    properties:
      resource/type:
        const:
          Organization
```

### Complex

The `complex` engine allows you to combine several rules with `and` and `or` operators. You can use any engine rule to define a rule and even `complex` engine itself but it is forbidden to have both `and` and `or` keys on the same level. Rules are defined as an array of objects that must include an engine with a set of corresponding keys.

#### How AND & OR work

Aidbox applies inner policies one after the other top-bottom.

#### AND rule

Aidbox applies policies till one of two events happens:

1. One policy rejects the access. No further policies are applied. The access is rejected
2. There are no more policies to evaluate. It means the access is granted

#### OR rule

Aidbox applies policies till at least one grants access. If it has happened no further policies are applied.

#### Example 1

```yaml
resourceType: AccessPolicy
engine: complex
and:
  - { engine: "sql", sql: { query: "select true" } }       # check-1
  - engine: complex
    or:
      - { engine: "sql", sql: { query: "select false" } }  # check-2
      - { engine: "sql", sql: { query: "select false" } }  # check-3
```

Policy in the example above represents the following logical expression:\
`check-1 AND (check-2 OR check-3)`

#### Example 2

Let's split the SQL policy example from above into two separate rules and combine them under `and` rule:

```yaml
resourceType: AccessPolicy
engine: complex
and:
  # Rule: request.user && request.user.data.practitioner_id are required
  - engine: json-schema
    schema:
      type: object
      required: ["user"]
      properties:
        user:
          type: object
          required: ["data"]
          properties:
            data:
              type: object
              required: ["practitioner_id"]
  # Rule: current practitioner is patient’s generalPractitioner
  - engine: sql
    sql:
      query: |
        SELECT
          {{uri}} LIKE '/fhir/Patient/%'
          AND resource->'generalPractitioner' @>
        jsonb_build_array(jsonb_build_object('resourceType',
            'Practitioner', 'id', {{user.data.practitioner_id}}::text))
          FROM patient WHERE id = {{params.resource/id}};
```

## See also

* [AccessPolicy resource schema](../../reference/system-resources-reference/core-module-resources.md#accesspolicy)
* [AccessPolicy best practices](../../tutorials/security-access-control-tutorials/accesspolicy-best-practices.md)
* [AccessPolicy examples](../../tutorials/security-access-control-tutorials/accesspolicy-examples.md)
* [Debug access control](../../tutorials/security-access-control-tutorials/debug-access-control.md)
* [Create and test access control](../../tutorials/security-access-control-tutorials/create-and-test-access-control.md)
* [Restrict operations on resource type](../../tutorials/security-access-control-tutorials/restrict-operations-on-resource-type.md)
