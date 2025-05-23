---
description: >-
  This article shows how to create a user for a practitioner and allow
  practitioners to read their own data
---

# Flexible RBAC built-in to Aidbox

Aidbox provides role-based access control mechanism based on access policies and custom `Role` resource.

Each `Role` resource assigns a role to a `User.` `AccessPolicy` resource has an optional `roleName` property. Aidbox applies access policy with `roleName` specified only to users which have the corresponding role assigned.

## Create a practitioner

```yaml
POST /fhir/Practitioner

id: pr-1
resourceType: Practitioner
name:
  - given:
      - John
```

## Create a user

Aidbox does not store any role information in a User resource. So create a user as usual.

```yaml
POST /fhir/User

id: user-1
password: password
resourceType: User
```

## Create a Role resource

Role name is a string that defines role. You don't need to declare it explicitly. Role resource links a role and a user.

Role resource has an optional links property which specifies related resources. Aidbox does not assign any special meaning to this property. Additionally, Role is an open resource so you can add any information to it.

You can put any additional data into `context` property.

Create a `Role` resource which assigns a role to the user we created.

```yaml
POST /fhir/Role

id: practioner-role-user-1
resourceType: Role
user:
  reference: User/user-1
name: practitioner
links:
  practitioner:
    reference: Practitioner/pr-1
```

If you need to assign the same role to multiple users, then just create multiple `Role` resources with the same `name` property.

## Create an access policy

`AccessPolicy` resource has a `roleName` property. This property specifies the role name for the policy. Access policy with a role name is applied only users with the corresponding role.

Create an `AccessPolicy` which allows practitioners to read their own data

```yaml
POST /AccessPolicy

id: practitioner-role
resourceType: AccessPolicy

roleName: practitioner
engine: matcho
link:
  - id: FhirRead
    resourceType: Operation
matcho:
  uri: '#/fhir/Practitioner/.*'
  request-method: get
  params:
    resource/id: .session.role.0.links.practitioner.id
```

## Try it

Log in as `user-1`.

Read your data

```http
GET /fhir/Practitioner/pr-1
```

Aidbox returns you a Practitioner resource.

### What's going on here

When you make a query

```
GET /fhir/Practitioner/pr-1
```

Aidbox router stores data in the request object:

* Uri `/fhir/Practitioner/pr-1` in the `uri` property.
* Method `get` in the `request-method` property.
* path parameter `pr-1` in the `params.resource/id` property.

Aidbox applies access policy with `roleName` property only if a role with corresponding `name` is assigned to user. In this case Aidbox adds the corresponding Role resource to the `role` property of the request object.

Access policy engine evaluates request object. And here it checks that `params.resource/id` property is equal to `role.links.practitioner.id` property.

You can inspect request object [using `__debug` query parameter](../debug-access-control.md#__debug-query-string-parameter).

### Role resource schema

```yaml
desc: User role
attrs:
  name:
    type: string
    isRequired: true
    search: { name: name, type: string }
  description:
    type: string
  user:
    type: Reference
    isRequired: true
    refers: [ User ]
    search: { name: user, type: reference }
  links:
    attrs:
      patient:
        type: Reference
        refers: [ Patient ]
      practitionerRole:
        type: Reference
        refers: [ PractitionerRole ]
      practitioner:
        type: Reference
        refers: [ Practitioner ]
      organization:
        type: Reference
        refers: [ Organization ]
      person:
        type: Reference
        refers: [ Person ]
      relatedPerson:
        type: Reference
        refers: [ RelatedPerson ]
  context: { isOpen: true }
```
