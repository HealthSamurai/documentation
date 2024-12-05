# Role-Based Access Control (RBAC)

Aidbox provides role-based access control mechanism based on access policies and `User` resource.

## Create a patient

```yaml
POST /Patient

id: pt-1
resourceType: Patient

name:
  - given:
      - John
```

## Create a user

Create a user containing `practitioner` role.

```yaml
POST /User

id: user-1
resourceType: User
roles: 
  - value: practitioner
```

## Create an access policy

Create an access policy that allows practitioners to read patients' data.

```yaml
POST /AccessPolicy

id: practitioner-role
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      - value: practitioner
  uri: '#/Patient/.*'
  request-method: get
```

{% hint style="info" %}
Please note that if you like to add multiple request methods when using **matcho** engine you should use `$one-of` notation:

```clojure
request-method:
  $one-of:
  - get
  - post
  - put
```
{% endhint %}

## Try it

Log in as `user-1`.

Read patient data

```http
GET /Patient/pt-1
```

Aidbox will return you a Patient resource.

### What's going on here

When you make a query

```
GET /Patient/pt-1
```

Aidbox router stores data in the request object:

* Uri `/Patient/pt-1` in the `uri` property.
* Method `get` in the `request-method` property.
* User data in `user` property. In particular `user.roles[].value` contains user roles.

Access policy engine evaluates request object. And here it checks that `user.roles[].value` property contains `practitioner` string.

You can inspect request object [using `__debug` query parameter](../readme-1-1/debug.md#__debug-query-string-parameter).

## Guide: flexible RBAC

{% content-ref url="../readme-1-1/rbac/role-based-access-policies.md" %}
[role-based-access-policies.md](../readme-1-1/rbac/role-based-access-policies.md)
{% endcontent-ref %}
