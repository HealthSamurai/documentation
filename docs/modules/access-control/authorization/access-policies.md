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

**TODO Tutorials**

* **Best practice - use Access Policies for complex access control logic**
* **Debugging Access Policies**

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

To test the Matcho engine without creating AccessPolicy and sending a request, you can use the [$matcho endpoint](../../../api/rest-api/other/matcho.md).

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

## See also

* [AccessPolicy resource schema](../../../reference/system-resources-reference/iam-module-resources.md#accesspolicy)
* TODO: How to write & debug accesspolicies tutorial
