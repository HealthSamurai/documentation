---
description: User, Session, Client resources and mechanics explained
---

# User, Session, Client resources

## **User**

This document outlines the attributes of the `User` resource and their descriptions.

<table><thead><tr><th width="270">Property</th><th width="105">Type</th><th>Description</th></tr></thead><tbody><tr><td>User.<strong>active</strong></td><td>boolean</td><td>Indicates whether the User is active. <strong>Note:</strong> This attribute is ignored.</td></tr><tr><td>User.<strong>addresses</strong></td><td>Object</td><td>A collection of physical mailing addresses for the User. Includes sub-attributes:</td></tr><tr><td>User.addresses.<strong>country</strong></td><td>string</td><td>Specifies the country name.</td></tr><tr><td>User.addresses.<strong>formatted</strong></td><td>string</td><td>The complete mailing address formatted for display or mailing label usage. Can include newlines.</td></tr><tr><td>User.addresses.<strong>locality</strong></td><td>string</td><td>Specifies the city or locality.</td></tr><tr><td>User.addresses.<strong>postalCode</strong></td><td>string</td><td>Specifies the postal or zip code.</td></tr><tr><td>User.addresses.<strong>region</strong></td><td>string</td><td>Specifies the state or region.</td></tr><tr><td>User.addresses.<strong>streetAddress</strong></td><td>string</td><td>The detailed street address, including house number, street name, P.O. box, and extended street information. Can include newlines.</td></tr><tr><td>User.addresses.<strong>type</strong></td><td>string</td><td>A label indicating the type of address, such as 'work', 'home', or 'other'.</td></tr><tr><td>User.<strong>costCenter</strong></td><td>string</td><td>Identifies the cost center associated with the User.</td></tr><tr><td>User.<strong>data</strong></td><td>Anything</td><td>Additional custom data associated with the User.</td></tr><tr><td>User.<strong>department</strong></td><td>string</td><td>Specifies the department the User belongs to.</td></tr><tr><td>User.<strong>displayName</strong></td><td>string</td><td>A name suitable for display, typically the User's full name if available.</td></tr><tr><td>User.<strong>division</strong></td><td>string</td><td>Identifies the division the User is part of.</td></tr><tr><td>User.<strong>email</strong></td><td>email</td><td>The primary email address of the User.</td></tr><tr><td>User.<strong>emails</strong></td><td>Object</td><td>A list of email addresses for the User. Values should be canonicalized (e.g., 'bjensen@example.com'). Includes sub-attributes:</td></tr><tr><td>User.emails.<strong>display</strong></td><td>string</td><td>A human-readable display name for the email. <strong>READ-ONLY.</strong></td></tr><tr><td>User.emails.<strong>primary</strong></td><td>boolean</td><td>Indicates whether this is the preferred email address. Only one email can be marked as primary.</td></tr><tr><td>User.emails.<strong>type</strong></td><td>string</td><td>Specifies the type of email, such as 'work', 'home', or 'other'.</td></tr><tr><td>User.emails.<strong>value</strong></td><td>string</td><td>The email address value.</td></tr><tr><td>User.<strong>employeeNumber</strong></td><td>string</td><td>A unique identifier assigned to the User, often used for organizational purposes.</td></tr><tr><td>User.<strong>entitlements</strong></td><td>Object</td><td>A list of entitlements representing the User's access rights or privileges. Includes sub-attributes:</td></tr><tr><td>User.entitlements.<strong>display</strong></td><td>string</td><td>A human-readable name for the entitlement. <strong>READ-ONLY.</strong></td></tr><tr><td>User.entitlements.<strong>primary</strong></td><td>boolean</td><td>Indicates the primary entitlement. Only one entitlement can be marked as primary.</td></tr><tr><td>User.entitlements.<strong>type</strong></td><td>string</td><td>Specifies the type of entitlement.</td></tr><tr><td>User.entitlements.<strong>value</strong></td><td>string</td><td>The value of the entitlement.</td></tr><tr><td>User.<strong>fhirUser</strong></td><td>Reference</td><td>A reference to the corresponding FHIR resource for the User.</td></tr><tr><td>User.<strong>gender</strong></td><td>string</td><td>The User's gender.</td></tr><tr><td>User.<strong>identifier</strong></td><td>Identifier</td><td>A list of identifiers associated with the User.</td></tr><tr><td>User.<strong>ims</strong></td><td>Object</td><td>A collection of instant messaging addresses for the User. Includes sub-attributes:</td></tr><tr><td>User.ims.<strong>display</strong></td><td>string</td><td>A display-friendly name for the messaging address. <strong>READ-ONLY.</strong></td></tr><tr><td>User.ims.<strong>primary</strong></td><td>boolean</td><td>Indicates whether this is the primary messaging address. Only one can be primary.</td></tr><tr><td>User.ims.<strong>type</strong></td><td>string</td><td>Specifies the type of messaging address, such as 'aim', 'gtalk', or 'xmpp'.</td></tr><tr><td>User.ims.<strong>value</strong></td><td>string</td><td>The value of the messaging address.</td></tr></tbody></table>

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

Basically, all sessions stored in Aidbox are infinite, and you have to manage session expiration by yourself manually.

However since [Aidbox v:2205](https://docs.aidbox.app/getting-started/versioning-and-release-notes/release-notes#may-2022-v-2205-edge) `Session.exp` field was added. It represents NumericDate from [RFC7519](https://www.rfc-editor.org/rfc/rfc7519#section-2) and it identifies the expiration time after which the Session will not be accepted for processing.

You can specify `auth.*.access_token_expiration` (in seconds) on Client resource, so `Session.exp` field will be propagated once corresponding grant\_type is used to launch a Session.

### Session expiration for Aidbox UI

In Aidbox version [v:2402](https://docs.aidbox.app/overview/release-notes#february-2024-stable-2402) and later, sessions created through the Aidbox UI log-in are not infinite. The default session expiration time is set to 432000 seconds (5 days). To change the default time, create an `AuthConfig` resource and set the `asidCookieMaxAge` to the desired value:

```yaml
PUT /AuthConfig/my-auth-config
content-type: text/yaml
accept: text/yaml

asidCookieMaxAge: 86400 # seconds
```

## Client

To provide programmatic access to Aidbox you have to register a `Client` resource.

### `Client.audience`

`A Client` can have the `audience` attribute. The `audience` shows what resource server access is intended for. Aidbox compares the `audience` of the `Client` to the `audience` it receives within a`JWT` and decides if the access should be granted.

The `audience` attribute can be defined in 2 ways:

* As a plain string. For example, `https://cmpl.aidbox.app/smart`
* As a `Regex`. In that case, the `audience` value should start with the `#` symbol. For example, `#https://cmpl.aidbox.app/tenant/[^\]/smart`

{% hint style="info" %}
That validation of the `audience` happens when SMART on FHIR app launches
{% endhint %}

### `Client.grant_types`

`Client` resource must have `grant_types` attribute defining authentification scheme for this Client.

> [Application grant types](https://auth0.com/docs/configure/applications/application-grant-types#available-grant-types) (or flows) are methods through which applications can gain [Access Tokens](https://auth0.com/docs/security/tokens/access-tokens) and by which you grant limited access to your resources to another entity without exposing credentials.

Grant types are choosed appropriately based on the `grant_types` property of your Auth0-registered Application. The [OAuth 2.0 protocol](https://auth0.com/docs/authorization/flows/which-oauth-2-0-flow-should-i-use) supports several types of grants, which allow different types of access. **To see available grant types and grant type** mapping refer to the [doc](https://auth0.com/docs/configure/applications/application-grant-types#available-grant-types).

Other required attributes are determined based on the values of this attribute `grant_types` is an array of strings, possible values are:

* basic
* client\_credentials
* password
* implicit
* authorization\_code
* code

{% hint style="info" %}
You can find different authorization flow examples in the Auth Sandbox in the Aidbox ui
{% endhint %}
