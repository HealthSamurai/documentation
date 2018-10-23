---
description: What is a user? What is a client?
---

# Users and Clients

## Client

The `Сlient` is the application that must access the user account or user data. Before performing access, it must be authorized by the user, and authorization must be approved by the Authorization Server.

{% hint style="info" %}
Besides OAut2.0 authorization, Aidbox support simply basic auth which use the same client credentials as OAuth. For more information read documentation [Authentication and Authorization](https://docs.aidbox.app/tutorials/authentication-and-authorization).
{% endhint %}

### Structure

| Attribute | Type | Description |
| :--- | :--- | :--- |
| id | [string](../basic-concepts/meta-model/aidbox-data-types.md#string) | Client id |
| secret | [password](../basic-concepts/meta-model/aidbox-data-types.md#password) | Client secret |
| redirect\_uri | [string](../basic-concepts/meta-model/aidbox-data-types.md#string) | Set of available redirect uri separated by comma |

### Samples

Client for web SPA application

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/Client

{"resourceType": "Client",
 "id": "web-app",
 "secret": "client-secret",
 "redirect_uri": "http://localhost:4200,https://example.com"}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201
{
    "secret": "$s0$f0801$4z6uTgJ3vF2dDPMhnT5Weg==$15LXbNYov+ByAsQgzHwXWIBWck7rdQGtMIEgMxC2Bko=",
    "redirect_uri": "http://localhost:4200,https://example.com",
    "id": "web-app",
    "resourceType": "Client"
}
```
{% endtab %}
{% endtabs %}

## User

The owner of the resource or `User` is the user who authorizes the application to access his account or data. An application's access to a user account is limited to the “scope” of the authorization rights granted \(for example, read or write access\).

### Structure

<table>
  <thead>
    <tr>
      <th style="text-align:left">Attribute</th>
      <th style="text-align:left">Type</th>
      <th style="text-align:left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">id</td>
      <td style="text-align:left"><a href="../basic-concepts/meta-model/aidbox-data-types.md#string">string</a>
      </td>
      <td style="text-align:left">Unique user Id</td>
    </tr>
    <tr>
      <td style="text-align:left">email</td>
      <td style="text-align:left"><a href="../basic-concepts/meta-model/aidbox-data-types.md#email">email</a>
      </td>
      <td style="text-align:left">User email</td>
    </tr>
    <tr>
      <td style="text-align:left">password</td>
      <td style="text-align:left"><a href="../basic-concepts/meta-model/aidbox-data-types.md#password">password</a>
      </td>
      <td style="text-align:left">User password</td>
    </tr>
    <tr>
      <td style="text-align:left">data</td>
      <td style="text-align:left"><a href="../basic-concepts/meta-model/aidbox-data-types.md#object">Object</a>
      </td>
      <td style="text-align:left">
        <p>Free object with any user data.</p>
        <p>Aidbox recommend use standard <a href="https://openid.net/specs/openid-connect-core-1_0.html#Claims">OpenId claims</a>
        </p>
      </td>
    </tr>
  </tbody>
</table>### Samples

Admin user

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/User

{"resourceType": "User",
 "id": "admin",
 "password": "long-user-password",
 "data": {"given_name" : "Main", "family_name": "Administrator"}}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201
{
    "data": {
        "given_name": "Main",
        "family_name": "Administrator"
    },
    "password": "$s0$f0801$zRTDzXCSCs1Jd0S+iQi+zA==$s5xzMsN7t609Vwsfk4WvusxnVqENcXyUKnnWxedV7hY=",
    "id": "admin",
    "resourceType": "User"
}
```
{% endtab %}
{% endtabs %}



