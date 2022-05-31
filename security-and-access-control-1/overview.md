---
description: User, Session, Client resources and mechanics explained
---

# Overview

## **User**

Aidbox has a SCIM User Resource.

Attributes:

| element    | type          | description      |
| ---------- | ------------- | ---------------- |
| id         | string        |                  |
| email      | string        |                  |
| password   | string        | Hash of password |
| identifier | Identifier\[] |                  |
| userName   |               |                  |

### Create Users

To create user you can use CRUD API, e.g. `POST /User` and `PUT /User/`

### User Login

Human User can use /auth/login to log in with credentials defined in a User resource

System can authenticate a User as it is specified in OAuth 2.0 spec. For example, you can get access token via

```
POST /auth/token
client_id: password-client
grant_type: password
username: user@mail.com
password: password
```

Note for such authentification a Client resource should be created

{% hint style="info" %}
You can find different authorization flow examples in the Auth Sandbox in the Aidbox ui
{% endhint %}

`GET /auth/userinfo` returns info about current User session

### Activate/Deactivate Users

To control User active status you can change `User.inactive` attribute by setting true or false value. Deactivating user doesn't affect Session's activation.



## Sessions

For each user login Aidbox creates Session resource

{% code title="Get last 10 sessions" %}
```sql
select cts, resource#>>'{user,id}'
from session
order by cts desc
limit 10
```
{% endcode %}

### Session expiration

Basically, all sessions stored in Aidbox are perpetual, and you have to manage session expiration by yourself manually. 

However since [Aidbox v:2205](https://docs.aidbox.app/getting-started/versioning-and-release-notes/release-notes#may-2022-v-2205-edge) `Session.exp` field was added. It represents NumericDate from [RFC7519](https://www.rfc-editor.org/rfc/rfc7519#section-2) and it identifies the expiration time after which the Session will not be accepted for processing.

You can specify `auth.*.access_token_expiration` (in seconds) on Client resource, so `Session.exp` field will be propagated once corresponding grant_type is used to launch a Session.

## Client

To provide programmatic access to Aidbox you have to register a client - Client resource.

Client resource must have `grant_types` attribute defining authentification scheme for this Client.

> [Application grant types](https://auth0.com/docs/configure/applications/application-grant-types#available-grant-types) (or flows) are methods through which applications can gain [Access Tokens](https://auth0.com/docs/security/tokens/access-tokens) and by which you grant limited access to your resources to another entity without exposing credentials. 

Grant types are choosed appropriately based on the `grant_types` property of your Auth0-registered Application. The [OAuth 2.0 protocol](https://auth0.com/docs/authorization/flows/which-oauth-2-0-flow-should-i-use) supports several types of grants, which allow different types of access. **To see available grant types and grant type** mapping refer to the [doc](https://auth0.com/docs/configure/applications/application-grant-types#available-grant-types). 

Other required attributes are determined based on the values of this attribute `grant_types` is an array of strings, possible values are:

* basic
* client_credentials
* password
* implicit
* authorization_code
* code

{% hint style="info" %}
You can find different authorization flow examples in the Auth Sandbox in the Aidbox ui
{% endhint %}
