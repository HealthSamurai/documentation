---
description: >-
  Current guide shows you how to register users in Smartbox, if you use Aidbox
  authorization server as a primary one.
---

# Register users

Smartbox deals with three user roles: admin, patient and provider. Once you deploy Smartbox

{% hint style="info" %}
Smartbox also have developer role. Developers live only in sandbox, they do not have direct access to production smartbox. This guide is focused on production smartbox.
{% endhint %}

## How to register admin?

Admin is a global resource. It is not attached to any tenant.

In order to register admin, go to Rest console in Aidbox UI and create User resource as following

```
POST /User

email: <USER_EMAIL>
password: <USER_PASSWORD> # Aidbox will compute hash of the password before storing it
active: true
name:
  givenName: <USER_FIRST_NAME>
  familyName: <USER_LAST_NAME>
roles:
- type: operator

```

## How to register provider?

Provider will have access to all patient records within related tenant.

{% hint style="info" %}
Let us know, if you need to have more granular access control for providers.
{% endhint %}

If you have Practitioner resource stored in Aidbox belonging to `my-clinic` tenant with id `pract-1`, you may register user you need to create a User resource and link it with the Patient resource.

```yaml
POST /User

email: <USER_EMAIL>
password: <USER_PASSWORD> # Aidbox will compute hash of the password before storing it
fhirUser:
  id: pract-1
  resourceType: Practitioner
name:
  givenName: <USER_FIRST_NAME>
  familyName: <USER_LAST_NAME>
roles:
- type: provider
active: true
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

## How to register patient?

Patient will have access only to their own data in Aidbox via SMART app.

If you have patient record stored in Aidbox belonging to `my-clinic` tenant with patient id `pt-1`, you may register user you need to create a User resource and link it with the Patient resource.

```yaml
POST /User

email: <USER_EMAIL>
password: <USER_PASSWORD> # Aidbox will compute hash of the password before storing it
fhirUser:
  id: pt-1
  resourceType: Patient
name:
  givenName: <USER_FIRST_NAME>
  familyName: <USER_LAST_NAME>
roles:
- type: patient
active: true
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

