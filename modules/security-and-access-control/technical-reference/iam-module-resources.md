# IAM Module Resources

The Identity and Access Management (IAM) module provides a comprehensive set of resources for managing user authentication, authorization, and access control within the Aidbox platform. This document describes the structure and attributes of all IAM resources.

## Overview

The IAM module includes the following resource types:

- AccessPolicy
- TokenIntrospector
- Role
- User
- Scope
- Client
- Grant
- Session
- Notification
- NotificationTemplate
- Registration
- IdentityProvider
- AuthConfig

## AccessPolicy

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| matcho | 0..1 | Object | Defines rules using the Matcho pattern-matching syntax. |
| clj | 0..1 | string |  |
| schema | 0..1 | Object | JSON Schema used to validate requests against the policy. |
| id | 0..1 | string |  |
| _source | 0..1 | string |  |
| module | 0..1 | string |  |
| or | 0..* | Object | A list of conditions where at least one must be satisfied for the policy to grant access. |
| roleName | 0..1 | string | Symbolic link to Role by name |
| and | 0..* | Object | A list of conditions that must all be satisfied for the policy to grant access. |
| link | 0..* | Reference | References to resources associated with this policy. References: Client, User, Operation |
| source | 0..1 | string |  |
| type | 0..1 | string | The type or category of the access policy. |
| engine | 0..1 | string | Specifies the evaluation engine used for the policy. |
| rpc | 0..1 | Object | Defines rules for Remote Procedure Calls (RPCs). |
| meta | 0..1 | Meta |  |
| resourceType | 0..1 | string |  |
| sql | 0..1 |  |  |
| description | 0..1 | string | A textual description of the access policy. |


## TokenIntrospector

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| type | 1..1 | string | Specifies the type of token to introspect. |
| jwks_uri | 0..1 | string | A URL pointing to a JSON Web Key Set (JWKS). When type is jwt the introspector retrieves public keys from this URI to validate token signatures. |
| jwt | 0..1 | BackboneElement | Configuration for local JWT validation used when type is jwt. |
| jwt.iss | 0..1 | string | The expected issuer (iss) claim value for JWTs. The TokenIntrospector ensures that tokens it validates come from this issuer. |
| jwt.secret | 0..1 | string | A shared secret key or other signing key material used to verify the JWT’s signature. |
| introspection_endpoint | 0..1 | BackboneElement |  |
| introspection_endpoint.url | 0..1 | string | The fully qualified URL of the remote introspection endpoint. |
| introspection_endpoint.authorization | 0..1 | string | The authorization header value (e.g. a Basic Auth or Bearer token) used when calling the introspection endpoint. If present it will be included in the request headers. |
| identity_provider | 0..1 | Reference | Link to Identity provider associated with the token introspector. References: IdentityProvider |


## Role

User role

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| name | 1..1 | string | Role name is a string that defines role. To assign the same role to multiple users, create multiple Role resources with the same "name". [Search param: name => type string] |
| description | 0..1 | string | Text description of the role |
| user | 1..1 | Reference | Reference to a User resource for which the role will be applied. [Search param: user => type reference] References: User |
| links | 0..1 | BackboneElement | You may list resources here, which can later be granted access for the user with this role via an AccessPolicy resource. |
| links.patient | 0..1 | Reference | Reference to Patient resource References: Patient |
| links.practitionerRole | 0..1 | Reference | Reference to PractitionerRole resource References: PractitionerRole |
| links.practitioner | 0..1 | Reference | Reference to Practitioner resource References: Practitioner |
| links.organization | 0..1 | Reference | Reference to Organization resource References: Organization |
| links.person | 0..1 | Reference | Reference to Person resource References: Person |
| links.relatedPerson | 0..1 | Reference | Reference to RelatedPerson resource References: RelatedPerson |
| context | 0..1 | Object |  |


## User

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| entitlements | 0..* | BackboneElement | A list of entitlements for the User that represent a thing the User has. |
| entitlements.value | 0..1 | string | The value of an entitlement. |
| entitlements.display | 0..1 | string | A human-readable name, primarily used for display purposes (READ-ONLY). |
| entitlements.type | 0..1 | string | A label indicating the attribute's function. |
| entitlements.primary | 0..1 | boolean | Indicates if this is the primary entitlement. Only one may be 'true'. |
| profileUrl | 0..1 | uri | A fully qualified URL pointing to a page representing the User's online profile. |
| department | 0..1 | string | Identifies the name of a department. |
| preferredLanguage | 0..1 | string | The User's preferred written or spoken language, e.g. 'en_US'. |
| securityLabel | 0..* | BackboneElement | List of security labes associated to the user |
| securityLabel.system | 0..1 | string | Code system |
| securityLabel.code | 0..1 | string | Code value |
| ims | 0..* | BackboneElement | Instant messaging addresses for the User. |
| ims.value | 0..1 | string | Instant messaging address. |
| ims.display | 0..1 | string | A human-readable name, primarily for display (READ-ONLY). |
| ims.type | 0..1 | string | A label indicating the IM type, e.g. 'aim', 'gtalk'. |
| ims.primary | 0..1 | boolean | Indicates if this is the primary IM. Only one may be 'true'. |
| timezone | 0..1 | string | The User's time zone in the 'Olson' format, e.g. 'America/Los_Angeles'. |
| displayName | 0..1 | string | The name of the User, suitable for display to end-users. |
| twoFactor | 0..1 | BackboneElement | Two factor settings for user |
| twoFactor.enabled | 1..1 | boolean | Defines whether two-factor auth is currently enabled. |
| twoFactor.transport | 0..1 | string | Transport of 2FA confirmation code (if used). |
| twoFactor.secretKey | 1..1 | string | TOTP Secret key. |
| gender | 0..1 | string | The user's gender. |
| email | 0..1 | email | Primary email for the user. |
| userType | 0..1 | string | Identifies the relationship between the organization and the user (e.g. 'Employee', 'Contractor'). |
| division | 0..1 | string | Identifies the name of a division. |
| name | 0..1 | BackboneElement | The components of the user's real name (formatted, family, given, etc.). |
| name.formatted | 0..1 | string | Full name, including titles and suffixes, formatted for display. |
| name.familyName | 0..1 | string | Family name (last name in Western languages). |
| name.givenName | 0..1 | string | Given name (first name in Western languages). |
| name.middleName | 0..1 | string | The middle name(s) of the User. |
| name.honorificPrefix | 0..1 | string | Honorific prefix (title), e.g. 'Ms.'. |
| name.honorificSuffix | 0..1 | string | Honorific suffix, e.g. 'III'. |
| locale | 0..1 | string | Indicates the User's default location for localization (e.g., currency, date format). |
| fhirUser | 0..1 | Reference | A reference to a related FHIR resource References: Patient, Practitioner, Person |
| identifier | 0..* | Identifier | A list of identifiers for the user. |
| photo | 0..1 | uri | Primary photo for the user. |
| phoneNumber | 0..1 | string | Primary phone number. |
| userName | 0..1 | string | Unique identifier for the User, typically used to directly authenticate. Must be unique across the service provider's Users. |
| addresses | 0..* | BackboneElement | A physical mailing address for this User (e.g. 'work', 'home'). |
| addresses.formatted | 0..1 | string | Full address, formatted for display or mailing label. |
| addresses.streetAddress | 0..1 | string | Street address component (may contain newlines). |
| addresses.locality | 0..1 | string | City or locality component. |
| addresses.region | 0..1 | string | State or region component. |
| addresses.postalCode | 0..1 | string | Zip code or postal code. |
| addresses.country | 0..1 | string | Country name component. |
| addresses.type | 0..1 | string | A label indicating the address type, e.g. 'work' or 'home'. |
| title | 0..1 | string | The user's title, e.g. 'Vice President'. |
| link | 0..* | BackboneElement | A collection of references or links associated with the user. |
| link.link | 0..1 | Reference | A referenced resource link. |
| link.type | 0..1 | string | A label indicating the link's function. |
| employeeNumber | 0..1 | string | Numeric or alphanumeric identifier assigned to a person by the organization. |
| password | 0..1 | password | The User's cleartext password, used for initial or reset scenarios. |
| photos | 0..* | BackboneElement | URLs of photos of the user. |
| photos.value | 0..1 | uri | URL of a photo of the User. |
| photos.display | 0..1 | string | A human-readable name, primarily used for display purposes (READ-ONLY). |
| photos.type | 0..1 | string | A label indicating 'photo' or 'thumbnail'. |
| photos.primary | 0..1 | boolean | Indicates if this is the primary photo. Only one may be 'true'. |
| manager | 0..1 | Reference | Another User resource who is this User's manager. References: User |
| x509Certificates | 0..* | BackboneElement | A list of certificates issued to the User. |
| x509Certificates.value | 0..1 | base64Binary | The value of an X.509 certificate (base64). |
| x509Certificates.display | 0..1 | string | A human-readable name, primarily used for display purposes (READ-ONLY). |
| x509Certificates.type | 0..1 | string | A label indicating the certificate's function. |
| x509Certificates.primary | 0..1 | boolean | Indicates if this is the primary certificate. Only one may be 'true'. |
| emails | 0..* | BackboneElement | Email addresses for the user. Values should be canonicalized (e.g. 'bjensen@example.com'). |
| emails.value | 0..1 | string | An individual email address (canonicalized). |
| emails.display | 0..1 | string | A human-readable name for display purposes (READ-ONLY). |
| emails.type | 0..1 | string | A label indicating the attribute's function, e.g. 'work', 'home'. |
| emails.primary | 0..1 | boolean | Indicates if this is the primary email. Only one primary may be 'true'. |
| inactive | 0..1 | boolean | A Boolean value indicating the User's administrative status. |
| active | 0..1 | boolean | NB: this attr is ignored. Indicates the User's administrative status. |
| phoneNumbers | 0..* | BackboneElement | Phone numbers for the User, e.g. 'tel:+1-201-555-0123'. |
| phoneNumbers.value | 0..1 | string | The user's phone number. |
| phoneNumbers.display | 0..1 | string | A human-readable name for display purposes (READ-ONLY). |
| phoneNumbers.type | 0..1 | string | A label for the phone number's function, e.g. 'home', 'work'. |
| phoneNumbers.primary | 0..1 | boolean | Indicates if this is the primary phone number. Only one may be 'true'. |
| data | 0..1 | Object | Arbitrary user-related data. |
| organization | 0..1 | Reference | Identifies the name of an organization. References: Organization |
| costCenter | 0..1 | string | Identifies the name of a cost center. |
| roles | 0..* | BackboneElement | A list of roles for the User that collectively represent who the User is (e.g. 'Student', 'Faculty'). |
| roles.value | 0..1 | string | The value of a role. |
| roles.display | 0..1 | string | A human-readable name, primarily used for display purposes (READ-ONLY). |
| roles.type | 0..1 | string | A label indicating the attribute's function. |
| roles.primary | 0..1 | boolean | Indicates if this is the primary role. Only one may be 'true'. |


## Scope

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| scope | 1..1 | string | The value of the scope |
| title | 1..1 | string | A user-friendly name for the scope that appears on the consent screen |
| description | 0..1 | string | When provided, the scope definition is additionally displayed on the consent screen |


## Client

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| first_party | 0..1 | boolean |  |
| auth | 0..1 | BackboneElement |  |
| auth.client_credentials | 0..1 | BackboneElement |  |
| auth.client_credentials.token_format | 0..1 | string | Allowed values: jwt |
| auth.client_credentials.access_token_expiration | 0..1 | integer |  |
| auth.client_credentials.refresh_token_expiration | 0..1 | integer |  |
| auth.client_credentials.audience | 0..* | string |  |
| auth.client_credentials.client_assertion_types | 0..* | string | Allowed values: urn:ietf:params:oauth:client-assertion-type:jwt-bearer |
| auth.client_credentials.refresh_token | 0..1 | boolean |  |
| auth.implicit | 0..1 | BackboneElement |  |
| auth.implicit.redirect_uri | 0..1 | url |  |
| auth.implicit.token_format | 0..1 | string | Allowed values: jwt |
| auth.implicit.audience | 0..* | string |  |
| auth.implicit.access_token_expiration | 0..1 | integer |  |
| auth.password | 0..1 | BackboneElement |  |
| auth.password.secret_required | 0..1 | boolean |  |
| auth.password.audience | 0..* | string |  |
| auth.password.refresh_token | 0..1 | boolean |  |
| auth.password.redirect_uri | 0..1 | url | If present, turn on redirect protection |
| auth.password.token_format | 0..1 | string | Allowed values: jwt |
| auth.password.access_token_expiration | 0..1 | integer |  |
| auth.password.refresh_token_expiration | 0..1 | integer |  |
| auth.authorization_code | 0..1 | BackboneElement |  |
| auth.authorization_code.token_format | 0..1 | string | Allowed values: jwt |
| auth.authorization_code.audience | 0..* | string |  |
| auth.authorization_code.secret_required | 0..1 | boolean |  |
| auth.authorization_code.pkce | 0..1 | boolean |  |
| auth.authorization_code.redirect_uri | 0..1 | url |  |
| auth.authorization_code.access_token_expiration | 0..1 | integer |  |
| auth.authorization_code.refresh_token_expiration | 0..1 | integer |  |
| auth.authorization_code.refresh_token | 0..1 | boolean |  |
| auth.token_exchange | 0..1 | BackboneElement |  |
| auth.token_exchange.token_format | 0..1 | string | Allowed values: jwt |
| auth.token_exchange.access_token_expiration | 0..1 | integer |  |
| auth.token_exchange.refresh_token_expiration | 0..1 | integer |  |
| auth.token_exchange.audience | 0..* | string |  |
| auth.token_exchange.refresh_token | 0..1 | boolean |  |
| trusted | 0..1 | boolean |  |
| allowed_origins | 0..* | uri | Allowed Origins are URLs that will be allowed to make requests from JavaScript to Server (CORS). By default, callback URLs are allowed. You can use wildcards at the subdomain level (e.g., https://*.contoso.com). Query strings and hash info are not considered. |
| grant_types | 0..* | string |  |
| name | 0..1 | string |  |
| jwks | 0..* | BackboneElement |  |
| jwks.kid | 0..1 | string |  |
| jwks.kty | 0..1 | string | Allowed values: RSA |
| jwks.alg | 0..1 | string | Allowed values: RS384 |
| jwks.e | 0..1 | string |  |
| jwks.n | 0..1 | string |  |
| jwks.use | 0..1 | string | Allowed values: sig |
| scopes | 0..* | BackboneElement |  |
| scopes.policy | 0..1 | Reference | References: AccessPolicy |
| scopes.parameters | 0..1 | Object |  |
| fhir-base-url | 0..1 | string |  |
| allowed-scopes | 0..* | Reference | References: Scope |
| scope | 0..* | string |  |
| allowedIssuers | 0..* | string |  |
| type | 0..1 | string |  |
| secret | 0..1 | sha256Hash |  |
| details | 0..1 | Object |  |
| active | 0..1 | boolean |  |
| jwks_uri | 0..1 | url |  |
| smart | 0..1 | BackboneElement |  |
| smart.launch_uri | 0..1 | string |  |
| smart.name | 0..1 | string |  |
| smart.description | 0..1 | string |  |
| description | 0..1 | string |  |


## Grant

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| user | 0..1 | Reference | References: User |
| client | 0..1 | Reference | References: Client |
| requested-scope | 0..* | string |  |
| provided-scope | 0..* | string |  |
| patient | 0..1 | Reference | References: Patient |
| scope | 0..1 | string |  |
| start | 0..1 | dateTime |  |


## Session

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| on-behalf | 0..1 | Reference | References: User |
| parent | 0..1 | Reference | References: Session |
| user | 0..1 | Reference | References: User |
| access_token | 0..1 | sha256Hash |  |
| refresh_token_exp | 0..1 | integer |  |
| jti | 0..1 | string |  |
| authorization_code | 0..1 | sha256Hash |  |
| exp | 0..1 | integer |  |
| start | 0..1 | dateTime |  |
| scope | 0..* | string |  |
| refresh_token | 0..1 | sha256Hash |  |
| type | 0..1 | string |  |
| patient | 0..1 | Reference | References: Patient |
| audience | 0..1 | string |  |
| ctx | 0..1 | Object | Smart on FHIR context |
| active | 0..1 | boolean |  |
| client | 0..1 | Reference | References: Client |
| end | 0..1 | dateTime |  |


## Notification

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| status | 0..1 | string |  |
| provider | 0..1 | string |  |
| providerData | 0..1 | Object |  |


## NotificationTemplate

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| subject | 0..1 | string |  |
| template | 0..1 | string |  |


## Registration

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| resource | 0..1 | Object | Registration form data |
| status | 0..1 | string |  |
| params | 0..1 | Object | Authorization params for continue authorization process after registration |


## IdentityProvider

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| introspection_endpoint | 0..1 | string |  |
| registration_endpoint | 0..1 | string |  |
| team_id | 0..1 | string |  |
| revocation_endpoint | 0..1 | string |  |
| authorize_endpoint | 0..1 | string |  |
| userinfo-source | 0..1 | string | Source of userinfo details. If id-token, no request to userinfo_endpoint performed |
| userinfo_header | 0..1 | string |  |
| base_url | 0..1 | uri |  |
| isEmailUniqueness | 0..1 | boolean |  |
| scopes | 0..* | string |  |
| isScim | 0..1 | boolean |  |
| title | 0..1 | string |  |
| kid | 0..1 | string |  |
| type | 0..1 | string |  |
| organizations | 0..* | string |  |
| userinfo_endpoint | 0..1 | string |  |
| system | 0..1 | string |  |
| toScim | 0..1 | Object |  |
| token_endpoint | 0..1 | string |  |
| active | 0..1 | boolean |  |
| client | 0..1 | BackboneElement |  |
| client.id | 0..1 | string |  |
| client.redirect_uri | 0..1 | uri |  |
| client.auth-method | 0..1 | string | Client authentication method. symmetric (default) or asymmetric |
| client.secret | 0..1 | string |  |
| client.private-key | 0..1 | string |  |
| client.certificate | 0..1 | string | Certificate |
| client.certificate-thumbprint | 0..1 | string | Certificate thumbprint. no colons expected. |
| client.creds-ts | 0..1 | string | Last time secret/private-key was updated |
| jwks_uri | 0..1 | string |  |


## AuthConfig

| Path | Cardinality | Type | Description |
| ---- | ----------- | ---- | ----------- |
| theme | 0..1 | BackboneElement |  |
| theme.brand | 0..1 | string | Brand for auth page |
| theme.title | 0..1 | string | Title for auth page |
| theme.styleUrl | 0..1 | uri | URL to external stylesheet |
| theme.forgotPasswordUrl | 0..1 | uri | URL to forgot password page |
| twoFactor | 0..1 | BackboneElement |  |
| twoFactor.webhook | 0..1 | BackboneElement |  |
| twoFactor.webhook.headers | 0..1 | Map | Map of HTTP header key-value pairs |
| twoFactor.webhook.timeout | 0..1 | integer | Timeout in milliseconds |
| twoFactor.webhook.endpoint | 1..1 | string | URL to webhook that supports POST method |
| twoFactor.issuerName | 0..1 | string | Issuer name for OTP authenticator app |
| twoFactor.validPastTokensCount | 0..1 | integer | Number of past tokens considered valid (useful with webhook since OTP lives ~30s) |
| asidCookieMaxAge | 0..1 | integer |  |

