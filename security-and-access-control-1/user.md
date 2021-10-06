---
description: TBD
---

# User

Aidbox has a SCIM User Resource.

Attributes:

| element | type | description |
| :--- | :--- | :--- |
| id | string |  |
| email | string |  |
| password | string | Hash of password |
| identifier | Identifier\[\] |  |
| userName |  |  |

### Create Users

To create user you can use CRUD API, e.g. `POST /User` and `PUT /User/`

### User Login

Human User can use /auth/login to log in with credentials defined in a User resource

System can authenticate a User as it is specified in OAuth 2.0 spec. For example, you can get access token via `POST /auth/token`

client\_id: password-client grant\_type: password username: user@mail.com password: password

Note for such authentification a Client resource should be created

{% hint style="info" %}
You can find different authorization flow examples in the Auth Sandbox in the Aidbox ui
{% endhint %}

`GET /auth/userinfo` returns info about current User session

### Activate/Deactivate Users

To control User active status you can change User.inactive attribute by setting true or false value

