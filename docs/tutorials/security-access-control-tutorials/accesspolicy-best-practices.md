---
description: Access policies creating and maintaning recommendations
---

# AccessPolicy best practices

## Link AccessPolicy to `User` , `Client` or `Operation`

An `AccessPolicy` instance can be linked to `User`, `Client` or `Operation` resources using the`AccessPolicy.link` field.

When authorizing a request, Aidbox evaluates all relevant `AccessPolicy` rules — those linked to the associated `User`, `Client`, and `Operation`, as well as any global policies.

If no links are specified, the policy is treated as a **global policy**. It means that **if you do not link your AccessPolicy to `User`, `Client` or `Operation` resource, it evaluates on every request, which affects performance.**

### Using Client and Operation in AccessPolicy

We can find `id` of Operation in **APIs -> Operations** page.

<figure><img src="../../.gitbook/assets/image (1) (1) (1) (1) (1).png" alt="Operations page showing operation IDs"><figcaption></figcaption></figure>

The following policy will be evaluated on every request from `myclient` Client. It allows `myclient` Client reading every resource by ID.

```json
{
  "resourceType": "AccessPolicy",
  "link": [{"reference": "Client/myclient"}],
  "engine": "matcho",
  "matcho": {"operation": {"id": "FhirRead"}}
}
```

Note that in the case of multiple links in the `link` field, it is interpreted as `OR` operation. This policy will allow everything for Client `myclient` and `FhirRead` operation to anybody.

```json
{
  "id": "wrong-access-policy",
  "resourceType": "AccessPolicy",
  "link": [{"reference": "Client/myclient"}, {"reference": "Operation/FhirRead"}],
  "engine": "allow"
}
```

## `.` path should be tested for `present?`

The path parameter is useful, but there’s a corner case to be aware of: if **both values are missing**, the check may still evaluate to true.

Here’s an example of a policy that illustrates this behavior:

```json
{
  "engine": "matcho",
  "id": "as-patient-create-owned-observation",
  "link": [
    {
      "reference": "Operation/FhirCreate"
    }
  ],
  "matcho": {
    "uri": "/fhir/Observation",
    "body": {
      "subject": ".user.data.patient"
    },
    "request-method": "post"
  },
  "resourceType": "AccessPolicy"
}
```

This policy would allow the following request, even if `subject` and `user.data.patient` are both missing:

```json
POST /fhir/Observation

{"status": "final"}
```

To fix this, we need to explicitly require that `.user.data.patient` exists:

```json
{
  "engine": "matcho",
  "id": "as-patient-create-owned-observation",
  "link": [
    {
      "reference": "Operation/FhirCreate"
    }
  ],
  "matcho": {
    "uri": "/fhir/Observation",
    "body": {
      "subject": ".user.data.patient"
    },
    "user": {
      "data": {
        "patient": "present?"
      }
    },
    "request-method": "post"
  },
  "resourceType": "AccessPolicy"
}
```

This version ensures the `subject` value is only checked if `.user.data.patient` is actually present.

## `complex` engine with `or` operator

When using **only** the `or` operator in the `complex` policy, it is recommended to create several access policies rather than combining all conditions into a single policy.

It gives profits:

1. Tiny policies give well-grained access control
2. Small policies are easy to maintain
3. Aidbox logs the access policy that granted access. If you have a "fat" policy, it is not transparent what exact rule lets a request in. When there are tiny policies, it is clear who passed the request.

For example, we have such an access policy.

```yaml
id: practitioner-policies
resourceType: AccessPolicy
roleName: practitioner
engine: complex
or:
  - engine: matcho
    matcho:
      uri:
        $one-of:
          - /Patient
          - '#/Patient/[^/]+$'
        request-method: get
  - engine: matcho
    matcho:
      uri: /$graphql
      request-method: post
```

That policy should be split into two policies.

```yaml
# see patients list & read certain patient resource
id: as-practitioner-see-patients-list-and-read-patient
resourceType: AccessPolicy
roleName: practitioner
engine: matcho
matcho:
  uri:
    $one-of:
      - /Patient
      - '#/Patient/[^/]+$'
  request-method: get

# grant access to graphql
id: as-practitioner-use-graphql
resourceType: AccessPolicy
roleName: practitioner
engine: matcho
matcho:
  uri: /$graphql
  request-method: post
```

## Needless RegEx usage

Replacing RegEx patterns with plain string comparison can improve policy readability.

```
"#^/Observation$" → "/Observation"
```

### `$one-of` instead of `|` operator

```
"#^/some-path/(operation-a|operation-b)$"
→
$one-of:
  - /some-path/operation-a
  - /some-path/operation-b
```

## Disable unsafe search parameters

By default access policy in Aidbox allows all the search parameters. Access policies check only fields specified in the policy and ignore others. It does nothing with the semantics of the operation.

Let's say you want to make GET /Practitioner publicly available, and you make the following AccessPolicy.

```yaml
engine: matcho
matcho:
  uri: /Practitioner
  request-method: GET
```

This policy accepts `GET /fhir/Practitioner` request with any search parameter, including unsafe ones (e.g. `_include`, `_revinclude`, `_with`, `_assoc`).

You may explicitly restrict _unsafe_ search parameters.

```yaml
engine: matcho
matcho:
  uri: /fhir/Practitioner
  request-method: GET
  params:
    _include: nil?
    _revinclude: nil?
    _with: nil?
    _assoc: nil?
```

## Naming

Access policy naming is an important aspect, as a good name makes it easier to understand and manage policies.

### Add `as-` prefix to describe the audience of the policy

The name should describe the intended user, user group, or application that was granted permissions, such as "practitioner" in the example provided. This way, anyone looking at the name can quickly identify the intended audience for the policy.

For example, `as-practitioner-use-graphql`

### Explain what resources are granted access to

Additionally, it's helpful to include information about the resource being accessed in the policy name. For example, "use-graphql" in the example above gives context to the type of resource being accessed.

### Several good names examples

* `as-patient-upload-profile-photo`
* `as-practitioner-get-user-notifications`
* `as-anonymous-verify-one-time-password`
* `as-smart-app-read-patient-details`
