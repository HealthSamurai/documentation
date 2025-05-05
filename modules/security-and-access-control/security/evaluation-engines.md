---
description: Evaluation engines are used to implement checks for AccessPolicy rules
---

# Evaluation engines

Aidbox provides several ways to specify rules for `AccessPolicy` resources — so-called evaluation engines. They come with their syntax and offer varying degrees of flexibility and convenience for writing those rules.

There are five engines. They are used to specify rules for a generic request object.

* Allow
* Matcho
* JSON Schema
* SQL
* Complex

Two additional engines provide a convenient way to write rules specifically for RPC endpoints.

* Allow-RPC
* Matcho-RPC

It is recommended to use Matcho engine. In 90% of cases, it is enough. Sometimes, the complex access policy can be only written by SQL or Complex engines.

All access policies are listed in **Access Control -> Access Policy** Aidbox UI page.

To debug the created access policy, use [Access policy dev tool](access-policy-dev-tool.md).

## Allow engine

The `allow` engine always evaluates `true` regardless of a request object. Use it if you want to provide unrestricted access to everything.

### Example

```yaml
resourceType: AccessPolicy
id: this-policy-allows-everything
engine: allow
link:
- { resourceType: User, id: admin }
```

## Matcho engine

`matcho` engine leverages [Matcho](https://github.com/HealthSamurai/matcho) pattern matching — custom DSL developed by Health Samurai. It has compact and declarative syntax with limited expressivity. It is well-suited for writing all sorts of rules and thus is one of the easiest options to specify `AccessPolicy` checks.

To test matcho DSL without creating AccessPolicy and sending a request, you can use the [$matcho](../../../api/rest-api/other/usdmatcho.md) endpoint.

### Example

In this example, the request is allowed only if:

* Uri is related to Encounter. E.g. `/fhir/Encounter` or `/Encounter`,
* HTTP request is either `GET` or `POST`, not `PUT`,
* `practitioner` parameter provided as a query string in case of the `GET` request or body params in case of the `POST` request must be equal to `practitioner_id` of the user,
* User data, which is fetched from the `"user"` table must contain `inpatient` department and practitioner.

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

### Match DSL definition

* Strings, numbers, and booleans are compared by value.
* If the pattern is a dictionary, search for its inclusion into a test subject. This check is nested and recursive.
  * Pattern: `{x: 1}`&#x20;
    * matches: `{x: 1}`, `{x: 1, y: 2, ...}`
    * doesn't match: `{z: 1}`&#x20;
  * Pattern `{a: {b: 5}`&#x20;
    * matches: `{a: {b: 5, c: 6, ...}, d: 7}`
    * doesn't match: `{a: {c: 5}}`, `{b: {a: 5}}`&#x20;
* If a pattern is an array, search for its elements in the order given.
  * Pattern `[1, 2]`&#x20;
    * matches `[1, 2]`, `[1, 2, 3, …]`
    * doesn't match: `[2, 1]`

#### Regular expressions

If a string starts with `#` , it is treated as a regular expression.

* Pattern `{a: "#\\d+"}`&#x20;
  * matches: `{a: "2345"}`
  * doesn't match: `{a: "abc"}`

#### Special string literals (postfixed with `?`)

* **present?** — matches non-`null` values
  * Pattern `{a: "present?"}`&#x20;
    * matches: `{a: 5}`,`{a: {b: 6}}`
    * doesn't match: `{b: 5}`
* **nil?** — matches `null` values;
* **not-blank?** — matches a non-empty string.

#### Matching values from the request context&#x20;

If a string starts with `.` , it is interpreted as a path in [the provided request context.](access-control.md#request-object-structure)&#x20;

* Pattern: `{params: {user_id: ".user.id"}}`&#x20;
  * matches: `{user: {id: 1}, params: {user_id: 1}}` where `user.id == param.user_id`.

In this example, $matcho will evaluate true only if 'a' is equal to my-value from the context.&#x20;

```
POST /$matcho

context: { my-value: 'value'}
matcho: {'a': '.my-value'}
resource: {'a': 'value'}
```

#### Special keys

* **$enum** — value must be equal to one of the items in the enumeration. Strings, numbers, and booleans are supported.
  * Pattern: `{request-method: {$enum: ["get", "post"]}}`&#x20;
    * matches: `{request-method: "post"},` `{request-method: "get"}`
    * doesn't match: `{request-method: "put"}`
*   **$one-of —** value must match any of the patterns. Should be used in cases, when **$enum** can not be used.

    * Pattern `{a: {$one-of: [{b: "present?"}, {c: "present?"}]}}`&#x20;
      * matches `{a: {c: 5}}`
      * doesn't match: `{a: {d: 5}`, `{a: {b: null}`

    Note that the `$one-of` key cannot be utilized concurrently with other keys.

    *   **Correct usage example.** Here, the `resource/type` key is correctly nested within each option specified by the `$one-of` key.

        ```yaml
        resourceType: AccessPolicy
        engine: matcho
        matcho:
          request-method: get
          params:
            $one-of:
              - name: present?
                resource/type: Patient
              - _id: present?
                resource/type: Patient
              - id: present?
                resource/type: Patient
        ```
    * **Incorrect usage example.**&#x20;

    In this case, the `$one-of` key is improperly combined with the `resource/type` key within the same `params` block:

    ```yaml
    resourceType: AccessPolicy
    engine: matcho
    matcho:
      request-method: get
      params:
        resource/type: Patient
        $one-of:
          - name: present?
          - _id: present?
          - id: present?
    ```
*   **$reference** — parse `Reference` or string into [aidbox format](../../../api/rest-api/other/aidbox-and-fhir-formats.md#references). It is useful to map FHIR reference into resourceType and id like this:

    `{reference: "Patient/pid"}` => `{id: "pid", resourceType: "Patient"}`

Example: `{resource: {patient: {$reference: {id: '.user.data.patient_id'}}}`

* **$contains** — collection must contain at least one match.
  * Pattern: `{type: {$contains: {system: "loinc"}}`&#x20;
    * matches `{type: [{system: "snomed"}, {system: "loinc"}]}`
* **$every** — each item in a collection must satisfy a pattern.
  * Pattern: `{col: {"$every": {foo: "bar"}}`&#x20;
    * matches: `{col: [{foo: "bar"}, {foo: "bar", baz: "quux"}]}`
*   **$not** — negates a pattern.

    * Pattern: `{message: {$not: {status: private}}`&#x20;
      * matches: `{message: {status: public}}`&#x20;
      * doesn't match: `{message: {status: private}}`.&#x20;

    **Be careful** when using **$not**, as it is possible to create policies that are too permissive.

{% hint style="warning" %}
Consider the following policy which uses `$not` key.

```yaml
sourceType: AccessPolicy
engine: matcho
matcho:
  request-method: delete
  uri: '#^/Patient.*$'
  user: {$not: {data: {role: guest}}}
```

While the original intent was to prevent Guest users from deleting Patient resources, this AccessPolicy allows them to perform DELETE /Patient/ for all other users, including unauthorized ones. In this case, it is better to explicitly list allowed roles with **$enum**.
{% endhint %}

* **$present-all** — checks that every element in $present-all is present in the original array, with no sort check. It can be combined with $length.
* **$length** — checks the length of the array.

### More examples

```yaml
resourceType: AccessPolicy
id: only-users-working-in-inpatient-department
engine: matcho
matcho:
  request-method: delete
  uri: '#^/Patient.*$'
  user: {department: {$contains: inpatient}}
```

```yaml
resourceType: AccessPolicy
id: only-authorized-users-can-get-patients-or-encountres
engine: matcho
matcho:
  user: present?
  request-method: get
  params:
    resource/type: {$enum: ['Patient', 'Encounter']}
```

```yaml
# When searching for encounter require
# practitioner value to be equal to user.data.pract_id
resourceType: AccessPolicy
engine: matcho
matcho:
  user: {data: {pract_id: present?}}
  uri: '/Encounter'
  params: 
    practitioner: '.user.data.pract_id'
```

```yaml
resourceType: AccessPolicy
engine: matcho
matcho:
  resource:
    $length: 2
    $present-all:
      - resourceType: Patient
      - resourceType: Encounter
```

{% hint style="info" %}
Need help with the `matcho` engine? Contact us on the [telegram chat](https://t.me/aidbox)!
{% endhint %}

## Allow-RPC

Requires to specify `type: rpc` in the `AccessPolicy` resource.

`allow-rpc` engine allows access to every RPC endpoint listed under the `rpc` field of the `AccessPolicy` resource.

### Example

```yaml
resourceType: AccessPolicy
type: rpc
engine: allow-rpc
rpc:
  aidbox.notebooks.list-notebooks: true
```

## Matcho-RPC

Requires to specify `type: rpc` for `AccessPolicy` resource.

`matcho-rpc` allows access to all endpoints that satisfy specified [Matcho](evaluation-engines.md#matcho) rules.

### Example 1

```yaml
resourceType: AccessPolicy
id: allow-everyone-to-access-hello-notebook
type: rpc
engine: matcho-rpc
rpc:
  aidbox.notebooks/get-notebook-by-id:
    params:
      notebook:
        id: hello
```

### Example 2

To control [tenant](broken-reference) access, use `tenant/org` in macho-rpc. `tenant/org` contains the current organization id in the multitenancy API.

```yaml
resourceType: AccessPolicy
id: client-uses-rpc-method-on-org-a
type: rpc
engine: matcho-rpc
rpc:
  rpc-method-name:
    uri: "/Organization/org-a/rpc"
    # organization id should persist
    tenant/org: "present?"
    # client.details.org-id field should be equal to the current organization id
    client:
      details:
        org-id: ".tenant/org.id"
```

## SQL

The `sql` engine executes an SQL statement and uses its return value as an evaluation result. Thus the statement should return a single row with just one column:

```sql
SELECT true FROM patient WHERE id = {{jwt.patient_id}} LIMIT 1;
```

SQL statement can refer to request fields with a double curly braces interpolation. The string inside the braces will be used as a path to value in the request object.

The SQL engine is sometimes the only way to perform complex checks when the AccesPolicy needs to check data stored in the database but not in the request context.

### Example

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

### Interpolation Rules

You can parameterize your SQL queries with request object using `{{path}}` syntax. For example, to get a user role provided  `{user: {data: {role: "admin"}}}` you write `{{user.data.role}}`. Parameter expressions are escaped by default to protect from SQL injection.

For dynamic queries — to parameterize table name, for example — you have to use `{{!path}}` syntax. The expression `SELECT true from {{!params.resource/type}} limit 1` when a request contains `{params: {"resource/type": "Patient"}}` will be transformed into `SELECT true from "patient".` By default, identifier names are double-quoted and lower-cased.

### More examples

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

## JSON Schema

`json-schema` engine allows you to use [JSON Schema](https://json-schema.org) to validate the request object. It is specified under `schema` a field of `AccessPolicy` resource. The currently supported JSON Schema version is **draft-07**.

{% hint style="info" %}
Fields with empty values `— []`, `{}`, `""`, `null` — are removed from the request before access policy checks are applied. Make sure to specify all necessary fields as `required.`
{% endhint %}

{% hint style="info" %}
It is recommended to use Matcho engine instead of JSON Schema engine. Matcho engine is more expressive due to its special keys.
{% endhint %}

### Example

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

## Complex

The `complex` engine allows you to combine several rules with `and` and `or` operators. You can use any engine rule to define a rule and even `complex` engine itself but it is forbidden to have both `and` and `or` keys on the same level. Rules are defined as an array of objects that must include an engine with a set of corresponding keys.

### How AND & OR work

Aidbox applies inner policies one after the other top-bottom.

#### AND rule

Aidbox applies policies till one of two events happens:

1. One policy rejects the access. No further policies are applied. The access is rejected
2. There are no more policies to evaluate. It means the access is granted

#### OR rule

Aidbox applies policies till at least one grants access. If it has happened no further policies are applied.

### Example 1

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

### Example 2

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
