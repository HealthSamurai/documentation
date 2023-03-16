---
description: Access policies creating and maintaning recommendations
---

# AccessPolicy best practices

## Naming

Access policy naming is an important aspect as good name makes it is easier to understand and manage policies.

### Add `as-` prefix to describe the audience of the policy

Name should describe the intended user, user group or application who was granted permissions, such as "practitioner" in the example provided. This way, anyone looking at the name can quickly identify the intended audience for the policy.

For example,  `as-practitioner-use-graphql`

### Explain what resources is granted access to

Additionally, it's helpful to include information about the resource being accessed in the policy name. For example, "use-graphql" in the example above gives context to the type of resource being accessed.

### Several good names examples

* `as-patient-upload-profile-photo`
* `as-practitioner-get-user-notifications`
* `as-anoymous-verify-one-time-password`
* `as-smart-app-read-patient-details`

## `.` path should be tested for `present?`

Path parameter is useful, but there is a corner case with it. If both values are absent, then the check evaluates to true statement.

```yaml
id: as-patient-create-owned-observation
resourceType: AccessPolicy
engine: matcho
matcho:
  uri: /Observation
  body:
    subject: .user.data.patient
  request-method: post
```

This policy allows the following request.

```yaml
POST /Observation

status: final
```

Fixed version requires `.user.data.patient` exists

```yaml
id: as-patient-create-owned-observation
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    data:
      patient:
        id: present?
  uri: /Observation
  body:
    subject: .user.data.patient
  request-method: post
```

## `complex` engine with `or` operator

When using __only__ the `or` operator in the `complex` policy, it is recommended to create several access policies rather than combining all conditions into a single policy.

It gives profits:

1. Tiny policies give well grained access control
2. Small policies are easy to maintain
3. Aidbox logs access policy which granted access. If you have "fat" policy, it is not transparant what exact rule let a request in. When there are tiny policies, it is clear who passed the request. 

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

That policy should be splitted to two ones.

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
"#^/Obseravtion$" → "/Obseravtion"
```

### `$one-of` instead of `|` operator

```
"#^/some-path/(operation-a|operation-b)$"
→
$one-of:
- /some-path/operation-a
- /some-path/operation-b
```
