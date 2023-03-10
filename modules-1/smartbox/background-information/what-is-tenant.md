---
description: The article explains, what tenant is and how to set up tenant (clinic)
---

# What is Tenant

{% hint style="info" %}
In terms of Aidbox, a tenant is logically isolated data belonging to one medical practice (clinic)
{% endhint %}

### Structure of a Tenant

```json
id: my-clinic
resourceType: Tenant
name: My Clinic Name
logoUrl: https://example.com/my-clinic-logo.png
identityProvider:
  client:
    id: ziW2...lv0
    secret: 7Ho...gvb
  scopes:
    - user
    - read:org
    - openid
    - profile
  system: aidbox:tenant:my-clinic
  token_endpoint: https://auth.example.com/token
  userinfo_endpoint: https://auth.example.com/userinfo
  authorize_endpoint: https://auth.example.com/authorize
```

* `id` should consist of Unreserved Characters ([section 2.3 of RFC 3986](https://www.ietf.org/rfc/rfc3986.txt)) as it is used as the part of URL
* `name` is a tenant name
* `logoUrl` defines where the tenant logo image is. The link should be publicly accessible
* `identityProvider` defines the external identity provider Aidbox uses to authenticate users. If it's omitted, Aidbox uses the built-in `Login` form
  * `client`
    * `id` is the `Client ID` in the external identity provider
    * `secret`is the `Client Secret` in the external identity provider
  * `scopes` is an array of scopes identity providers supports
  * `system` should be a string representing the current tenant. Users of the tenant should have the same system value in their `identifier` property
  * `token_endpoint` is the token endpoint of the external identity provider
  * `userinfo_endpoint` is the userinfo endpoint of the external identity provider
  * `authorize_endpoint` is the authorize endpoint of the external identity provider

### How to create a Tenant

{% tabs %}
{% tab title="With external identity provider" %}
```http
POST /Tenant
content-type: text/yaml

id: my-clinic
resourceType: Tenant
name: My Clinic Name
logoUrl: https://example.com/my-clinic-logo.png
identityProvider:
  client:
    id: ziW2...lv0
    secret: 7Ho...gvb
  scopes:
    - user
    - read:org
    - openid
    - profile
  system: aidbox:tenant:my-clinic
  token_endpoint: https://auth.example.com/token
  userinfo_endpoint: https://auth.example.com/userinfo
  authorize_endpoint: https://auth.example.com/authorize
```
{% endtab %}

{% tab title="Without identity provider" %}
```http
POST /Tenant
content-type: text/yaml

id: my-clinic
resourceType: Tenant
name: My Clinic Name
logoUrl: https://example.com/my-clinic-logo.png
```
{% endtab %}
{% endtabs %}

### How to create a User

{% tabs %}
{% tab title="With link to identity provider" %}
```http
POST /User
content-type: text/yaml

resourceType: User
active: true
email: mail@example.com
identifier:
  - system: aidbox:tenant:my-clinic
    value: user-id-in-external-identity-provider
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```
{% endtab %}

{% tab title="Without link to identity provider" %}
```http
POST /User
content-type: text/yaml

resourceType: User
active: true
email: mail@example.com
password: secret # should be provided
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
`meta.tenant` links the user to the Tenant
{% endhint %}

{% hint style="info" %}
`There should be at one element in` identifier `where`

* `system` links to the `identityProvider.system`
* `value` is the `user ID` in the external identity provider
{% endhint %}
