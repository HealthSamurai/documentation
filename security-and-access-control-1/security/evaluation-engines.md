---
description: Evaluation engines are used to implement checks for AccessPolicy rules
---

# Evaluation engines

Aidbox provides several ways to specify rules for `AccessPolicy` resources — so called evaluation engines. They come with their own syntax and offer varying degrees of flexibility and convenience for writing those rules.

There are five main engines. They are used to specify rules for a generic request object.

* Allow
* JSON Schmea
* SQL
* Matcho
* Complex

There are also two additional engines which serve as a convenient way to write rules specifically for RPC endpoints.

* Allow-RPC
* Matcho-RPC

All of them are described in a greater detail below.

## Allow

`allow` engine always evaluates to `true` regardless of a request object. Use it if you want to provide an unrestricted access to everything.

### Example

```yaml
resourceType: AccessPolicy
id: this-policy-allows-everything
engine: allow
link:
- { resourceType: User, id: admin }
```

## JSON Schema

`json-schema` engine allows you to use [JSON Schema](https://json-schema.org) to validate the request object. It is specified under `schema` field of `AccessPolicy` resource. Currently supported JSON Schema version is **draft-07**.

{% hint style="info" %}
Fields with empty values `— []`, `{}`, `""`, `null` — are removed from request before before access policy checks are applied to it. Make sure to specify all necessary fields as `required.`
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

## SQL

`sql` engine executes an SQL statement and uses its return value as an evaluation result. Thus the statement should return a single row with just one column:

```sql
SELECT true FROM patient WHERE id = {{jwt.patient_id}} LIMIT 1;
```

SQL statement can refer to request fields with a double curly braces interpolation. String inside the braces will be used as a path to value in the request object.

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

You can parameterize your SQL queries with request object using `{{path}}` syntax. For example, to get a user role provided at `{user: {data: {role: "admin"}}}` you write `{{user.data.role}}`. Parameter expressions are escaped by default to protect from SQL injection.

For dynamic queries — to parameterize table name, for example — you have to use `{{!path}}` syntax. The expression `SELECT true from {{!params.resource/type}} limit 1` when a request contains `{params: {"resource/type": "Patient"}}` will be transformed into `SELECT true from "patient".` By default identifier names are double quoted and lower-cased.

## Matcho

`matcho` engine leverages [Matcho](https://github.com/HealthSamurai/matcho) pattern matching — custom DSL developed by Health Samurai. It has very compact and declarative syntax with a limited expressivity. It is well-suited for writing all sorts of rules and thus is one of the easiest options to specify `AccessPolicy` checks.

```yaml
resourceType: AccessPolicy
id: practitioner-only-who-works-in-inpatient-department-allowed-to-see-his-patients
engine: matcho
matcho:
  user: 
    # user.department should be equal to inpatient
    department: inpatient
    # user.data.practitioner_id should be present
    data: 
      practitioner_id: present?
  # uri should match regexp
  uri: '#/Encounter.*'
  # request method should be GET or POST
  request-method: {$enum: ['get', 'post']}
  params:
    # practitioner param should be equal to user.data.practitioner_id
    practitioner: .user.data.practitioner_id
```

Match DSL definition:

* If pattern is a dictionary, search for its inclusion into a test subject. This algorithm is nested and recursive.\
  Pattern `{x: 1}` matches `{x: 1, y: 2, …}`\
  \`\`Pattern `{a: {b: 5}` matches `{a: {b: 5, c: 6, …}, d: 7, …}`
* If a pattern is an array, search for its elements in the order given.\
  Pattern `[1, 2]` matches `[1, 2, 3, …]`
* Primitive values — strings, numbers and booleans — are compared by value.
* If a string starts with `#` , it is treated as a regular expression.\
  Pattern `{a: "#\\d+"}` matches `{a: "2345"}`
* If a string starts with `.` , it is interpreted as a path in the current test subject.\
  Pattern `{params: {user_id: ".user.id"}}` matches `{user: {id: 1}, params: {user_id: 1}}` where `user.id == param.user_id`.
* Special string literals (postfixed with `?)`
  * **present?** — matches non-`null` values;\
    Pattern `{a: "present?"}` matches `{a: 5}` or `{a: {b: 6}}`
  * **nil?** — matches `null` values;
  * **not-blank?** — matches non-empty string.
* Special keys:
  * **$enum** — value must be equal to one of the items in the enumeration.\
    Pattern `{request-method: {$enum: ["get", "post"]}}` matches `{request-method: "post"}`
  * **$one-of —** value must match any of the patterns.\
    Pattern `{a: {$one-of: [{b: "present?"}, {c: "present?"}]}` matches `{a: {c: 5}}`
  * **$reference** — parse `Reference` or string into [aidbox format](../../modules-1/fhir-resources/aidbox-and-fhir-formats.md#references). Examples:
    * Parse `Reference` elements
      * `parser: {reference: "Patient/pid"} => {id: "pid", resourceType: "Patient"}`
      * `{resource: {patient: {$reference: {id: '.user.data.patient_id'}}}`
    * Parse reference string
      * `"Patient/pid" => {id: "pid", resourceType: "Patient"}`
      * `{params: {subject: {$reference: {id: '.user.data.patient_id'}}}`
  * **$contains** — collection must contain at least one match.\
    Pattern `{type: {$contains: {system: "loinc"}}` matches `{type: [{system: "snomed"}, {system: "loinc"}]}`
  * **$every** — each item in a collection must satisfy a pattern.\
    Pattern `{col: {"$every": {foo: "bar"}}` matches `{col: [{foo: "bar"}, {foo: "bar", baz: "quux"}]}`
  * **$not** — negates a pattern.\
    Pattern `{message: {$not: {status: private}}` matches {message: `{status: public}}` and does not match `{message: {status: private}}`. **Be careful** using `$not` as it is possible to create **too permissive** policies.

{% hint style="warning" %}
Consider the following policy which uses `$not` key.

```yaml
resourceType: AccessPolicy
engine: matcho
matcho:
  request-method: delete
  uri: '#^/Patient.*$'
  user: {$not: {data: {role: guest}}}
```

While original intent was to forbid `guest` users to delete `Patient` resources this `AccessPolicy` allows to do `DELETE /Patient/<id>` for all the other users including unauthorized ones. In this case it is better to explicitly list allowed roles with `$enum.`
{% endhint %}

### Examples

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
    resource/type: {$enum: ['Patient', 'Encounter'}}
```

```yaml
# when searching for encounter require
# practitioner value to be equal to user.data.pract_id
resourceType: AccessPolicy
engine: matcho
matcho:
  user: {data: {pract_id: present?}}
  uri: '/Encounter'
  params: 
    practitioner: '.user.data.pract_id'
```

{% hint style="info" %}
Need help with `matcho` engine? Contact us on the [telegram chat](https://t.me/aidbox)!
{% endhint %}

## Complex

`complex` engine allows you to combine several rules with `and` and `or` operators. You can use any engine rule to define a rule and even `complex` engine itself but it is forbidden to have both `and` and `or` keys on the same level. Rules are defined as an array of objects which must include an engine with a set of corresponding keys.

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

Let's split SQL policy example from above into two separate rules and combine them under `and` rule:

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

## Allow-RPC

Requires to specify `type: rpc` for `AccessPolicy` resource.

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

### Example

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
