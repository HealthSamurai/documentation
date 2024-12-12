---
description: User, Session, Client resources and mechanics explained
---

# User, Session, Client resources

## **User**

This table outlines the attributes of the `User` resource and their descriptions.

<table><thead><tr><th width="269">path</th><th>type</th><th>description</th></tr></thead><tbody><tr><td>User.active</td><td>boolean</td><td><strong>Ignored Attribute.</strong> Indicates the user's administrative status.</td></tr><tr><td>User.addresses</td><td>array of objects</td><td>A collection of physical mailing addresses for the user. Common types: 'work', 'home', 'other'. Each entry may include the fields below.</td></tr><tr><td>User.addresses.country</td><td>string</td><td>Country name component of the address.</td></tr><tr><td>User.addresses.formatted</td><td>string</td><td>The full mailing address formatted for display or label printing. May contain newlines.</td></tr><tr><td>User.addresses.locality</td><td>string</td><td>The city or locality component.</td></tr><tr><td>User.addresses.postalCode</td><td>string</td><td>The zip or postal code component.</td></tr><tr><td>User.addresses.region</td><td>string</td><td>The state or region component.</td></tr><tr><td>User.addresses.streetAddress</td><td>string</td><td>The full street address, possibly multiline (e.g., house number, street name, etc.). May contain newlines.</td></tr><tr><td>User.addresses.type</td><td>string</td><td>A label for the address function, e.g., 'work' or 'home'.</td></tr><tr><td>User.costCenter</td><td>string</td><td>The name of a cost center associated with the user.</td></tr><tr><td>User.data</td><td>any</td><td>Arbitrary user-related data.</td></tr><tr><td>User.department</td><td>string</td><td>The name of a department associated with the user.</td></tr><tr><td>User.displayName</td><td>string</td><td>The user's display name, ideally their full name.</td></tr><tr><td>User.division</td><td>string</td><td>The name of a division associated with the user.</td></tr><tr><td>User.email</td><td>email</td><td>The user's primary email address.</td></tr><tr><td>User.emails</td><td>array of objects</td><td>A collection of the user's email addresses. The service should provide canonical forms, e.g., lowercase. Common types: 'work', 'home', 'other'.</td></tr><tr><td>User.emails.display</td><td>string</td><td>A human-readable label for the email address (read-only).</td></tr><tr><td>User.emails.primary</td><td>boolean</td><td>Indicates if this is the primary/preferred email. Only one can be true.</td></tr><tr><td>User.emails.type</td><td>string</td><td>The function label for the email (e.g., 'work', 'home').</td></tr><tr><td>User.emails.value</td><td>string</td><td>The user's email address, canonicalized.</td></tr><tr><td>User.employeeNumber</td><td>string</td><td>A numeric or alphanumeric identifier assigned by the organization.</td></tr><tr><td>User.entitlements</td><td>array of objects</td><td>A list of entitlements the user has.</td></tr><tr><td>User.entitlements.display</td><td>string</td><td>A human-readable label for the entitlement (read-only).</td></tr><tr><td>User.entitlements.primary</td><td>boolean</td><td>Indicates if this entitlement is primary. Only one can be true.</td></tr><tr><td>User.entitlements.type</td><td>string</td><td>A label indicating the entitlement's function.</td></tr><tr><td>User.entitlements.value</td><td>string</td><td>The value of the entitlement.</td></tr><tr><td>User.fhirUser</td><td>Reference</td><td>A reference to a related FHIR resource (e.g., Patient, Practitioner).</td></tr><tr><td>User.gender</td><td>string</td><td>The user's gender.</td></tr><tr><td>User.identifier</td><td>array of Identifier</td><td>A list of identifiers for the user.</td></tr><tr><td>User.ims</td><td>array of objects</td><td>A collection of instant messaging addresses for the user.</td></tr><tr><td>User.ims.display</td><td>string</td><td>A human-readable label for the IM address (read-only).</td></tr><tr><td>User.ims.primary</td><td>boolean</td><td>Indicates if this IM address is primary. Only one can be true.</td></tr><tr><td>User.ims.type</td><td>string</td><td>The function label for the IM address (e.g., 'aim', 'gtalk').</td></tr><tr><td>User.ims.value</td><td>string</td><td>The user's instant messaging address.</td></tr><tr><td>User.inactive</td><td>boolean</td><td>Indicates the user's administrative status.</td></tr><tr><td>User.link</td><td>array of objects</td><td>A collection of references or links associated with the user.</td></tr><tr><td>User.link.link</td><td>Reference</td><td>A referenced resource link.</td></tr><tr><td>User.link.type</td><td>string</td><td>A label indicating the link's function.</td></tr><tr><td>User.locale</td><td>string</td><td>The user's default locale, used for localization (e.g., formatting dates, numbers).</td></tr><tr><td>User.manager</td><td>Reference</td><td>The user's manager, referencing another user by id.</td></tr><tr><td>User.name</td><td>object</td><td>The components of the user's real name.</td></tr><tr><td>User.name.familyName</td><td>string</td><td>The user's family (last) name.</td></tr><tr><td>User.name.formatted</td><td>string</td><td>The user's full name, formatted for display.</td></tr><tr><td>User.name.givenName</td><td>string</td><td>The user's given (first) name.</td></tr><tr><td>User.name.honorificPrefix</td><td>string</td><td>The user's honorific prefix(es), e.g., 'Ms.'.</td></tr><tr><td>User.name.honorificSuffix</td><td>string</td><td>The user's honorific suffix(es), e.g., 'III'.</td></tr><tr><td>User.name.middleName</td><td>string</td><td>The user's middle name(s).</td></tr><tr><td>User.organization</td><td>Reference</td><td>A reference to the user's associated organization.</td></tr><tr><td>User.password</td><td>password</td><td>The user's cleartext password, used for initial setup or resets.</td></tr><tr><td>User.phoneNumber</td><td>string</td><td>The user's primary phone number.</td></tr><tr><td>User.phoneNumbers</td><td>array of objects</td><td>A collection of phone numbers for the user. Common types: 'work', 'home', 'mobile', etc. Should follow RFC 3966.</td></tr><tr><td>User.phoneNumbers.display</td><td>string</td><td>A human-readable label for the phone number (read-only).</td></tr><tr><td>User.phoneNumbers.primary</td><td>boolean</td><td>Indicates if this phone number is primary. Only one can be true.</td></tr><tr><td>User.phoneNumbers.type</td><td>string</td><td>The function label for the phone number (e.g., 'work', 'home').</td></tr><tr><td>User.phoneNumbers.value</td><td>string</td><td>The user's phone number.</td></tr><tr><td>User.photo</td><td>uri</td><td>The user's primary photo.</td></tr><tr><td>User.photos</td><td>array of objects</td><td>A collection of photo URLs for the user.</td></tr><tr><td>User.photos.display</td><td>string</td><td>A human-readable label for the photo (read-only).</td></tr><tr><td>User.photos.primary</td><td>boolean</td><td>Indicates if this photo is primary. Only one can be true.</td></tr><tr><td>User.photos.type</td><td>string</td><td>The function label for the photo, e.g., 'photo' or 'thumbnail'.</td></tr><tr><td>User.photos.value</td><td>uri</td><td>The URL of a user photo.</td></tr><tr><td>User.preferredLanguage</td><td>string</td><td>The user's preferred language (e.g., 'en_US').</td></tr><tr><td>User.profileUrl</td><td>uri</td><td>A URL pointing to the user's online profile.</td></tr><tr><td>User.roles</td><td>array of objects</td><td>A collection of roles for the user, e.g., 'Student', 'Faculty'.</td></tr><tr><td>User.roles.display</td><td>string</td><td>A human-readable label for the role (read-only).</td></tr><tr><td>User.roles.primary</td><td>boolean</td><td>Indicates if this role is primary. Only one can be true.</td></tr><tr><td>User.roles.type</td><td>string</td><td>A label indicating the role's function.</td></tr><tr><td>User.roles.value</td><td>string</td><td>The value of the role.</td></tr><tr><td>User.securityLabel</td><td>array of objects</td><td>A list of security labels associated with the user.</td></tr><tr><td>User.securityLabel.code</td><td>string</td><td>The code value of the security label.</td></tr><tr><td>User.securityLabel.system</td><td>string</td><td>The code system of the security label.</td></tr><tr><td>User.timezone</td><td>string</td><td>The user's time zone, in Olson format (e.g., 'America/Los_Angeles').</td></tr><tr><td>User.title</td><td>string</td><td>The user's title, e.g., "Vice President."</td></tr><tr><td>User.twoFactor</td><td>object</td><td>Two-factor authentication (2FA) settings for the user.</td></tr><tr><td>User.twoFactor.enabled</td><td>boolean</td><td>Indicates if two-factor authentication is currently enabled.</td></tr><tr><td>User.twoFactor.secretKey</td><td>string</td><td>The TOTP secret key for 2FA.</td></tr><tr><td>User.twoFactor.transport</td><td>string</td><td>The method of delivering the 2FA code. If absent, no code is sent.</td></tr><tr><td>User.userName</td><td>string</td><td>A required unique identifier used by the user for authentication.</td></tr><tr><td>User.userType</td><td>string</td><td>Defines the relationship between the organization and the user (e.g., 'Employee', 'Contractor').</td></tr><tr><td>User.x509Certificates</td><td>array of objects</td><td>A collection of X.509 certificates issued to the user.</td></tr><tr><td>User.x509Certificates.display</td><td>string</td><td>A human-readable label for the certificate (read-only).</td></tr><tr><td>User.x509Certificates.primary</td><td>boolean</td><td>Indicates if this certificate is primary. Only one can be true.</td></tr><tr><td>User.x509Certificates.type</td><td>string</td><td>A label indicating the certificate's function.</td></tr><tr><td>User.x509Certificates.value</td><td>base64Binary</td><td>The value of the X.509 certificate.</td></tr></tbody></table>

***

For further details, refer to the extended documentation for the `User` resource.

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
