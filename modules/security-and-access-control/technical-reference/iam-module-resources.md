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

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>matcho</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>Defines rules using the Matcho pattern-matching syntax.</td></tr>
<tr class="top-element"><td width="280"><code>clj</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>schema</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>JSON Schema used to validate requests against the policy.</td></tr>
<tr class="top-element"><td width="280"><code>id</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>_source</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>module</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>or</code></td><td width="90">0..*</td><td width="150"><code>Object</code></td><td>A list of conditions where at least one must be satisfied for the policy to grant access.</td></tr>
<tr class="top-element"><td width="280"><code>roleName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Symbolic link to Role by name</td></tr>
<tr class="top-element"><td width="280"><code>and</code></td><td width="90">0..*</td><td width="150"><code>Object</code></td><td>A list of conditions that must all be satisfied for the policy to grant access.</td></tr>
<tr class="top-element"><td width="280"><code>link</code></td><td width="90">0..*</td><td width="150"><code>Reference</code></td><td>References to resources associated with this policy. References: <code>Client</code>, <code>User</code>, <code>Operation</code></td></tr>
<tr class="top-element"><td width="280"><code>source</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The type or category of the access policy.</td></tr>
<tr class="top-element"><td width="280"><code>engine</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Specifies the evaluation engine used for the policy.</td></tr>
<tr class="top-element"><td width="280"><code>rpc</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>Defines rules for Remote Procedure Calls (RPCs).</td></tr>
<tr class="top-element"><td width="280"><code>meta</code></td><td width="90">0..1</td><td width="150"><code>Meta</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>resourceType</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>sql</code></td><td width="90">0..1</td><td width="150"></td><td></td></tr>
<tr class="top-element"><td width="280"><code>description</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A textual description of the access policy.</td></tr></tbody>
</table>


## TokenIntrospector

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element required-field"><td width="280"><code class="required">type</code></td><td width="90">1..1</td><td width="150"><code>string</code></td><td>Specifies the type of token to introspect.</td></tr>
<tr class="top-element"><td width="280"><code>jwks_uri</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A URL pointing to a JSON Web Key Set (JWKS). When type is jwt the introspector retrieves public keys from this URI to validate token signatures.</td></tr>
<tr class="top-element"><td width="280"><code>jwt</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td>Configuration for local JWT validation used when type is jwt.</td></tr>
<tr class="nested-element"><td width="280"><code>jwt.iss</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The expected issuer (iss) claim value for JWTs. The TokenIntrospector ensures that tokens it validates come from this issuer.</td></tr>
<tr class="nested-element"><td width="280"><code>jwt.secret</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A shared secret key or other signing key material used to verify the JWT’s signature.</td></tr>
<tr class="top-element"><td width="280"><code>introspection_endpoint</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>introspection_endpoint.url</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The fully qualified URL of the remote introspection endpoint.</td></tr>
<tr class="nested-element"><td width="280"><code>introspection_endpoint.authorization</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The authorization header value (e.g. a Basic Auth or Bearer token) used when calling the introspection endpoint. If present it will be included in the request headers.</td></tr>
<tr class="top-element"><td width="280"><code>identity_provider</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Link to Identity provider associated with the token introspector. References: <code>IdentityProvider</code></td></tr></tbody>
</table>


## Role

User role

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element required-field"><td width="280"><code class="required">name</code></td><td width="90">1..1</td><td width="150"><code>string</code></td><td>Role name is a string that defines role. To assign the same role to multiple users, create multiple Role resources with the same "name". [Search param: name => type string]</td></tr>
<tr class="top-element"><td width="280"><code>description</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Text description of the role</td></tr>
<tr class="top-element required-field"><td width="280"><code class="required">user</code></td><td width="90">1..1</td><td width="150"><code>Reference</code></td><td>Reference to a User resource for which the role will be applied. [Search param: user => type reference] References: <code>User</code></td></tr>
<tr class="top-element"><td width="280"><code>links</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td>You may list resources here, which can later be granted access for the user with this role via an AccessPolicy resource.</td></tr>
<tr class="nested-element"><td width="280"><code>links.patient</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Reference to Patient resource References: <code>Patient</code></td></tr>
<tr class="nested-element"><td width="280"><code>links.practitionerRole</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Reference to PractitionerRole resource References: <code>PractitionerRole</code></td></tr>
<tr class="nested-element"><td width="280"><code>links.practitioner</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Reference to Practitioner resource References: <code>Practitioner</code></td></tr>
<tr class="nested-element"><td width="280"><code>links.organization</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Reference to Organization resource References: <code>Organization</code></td></tr>
<tr class="nested-element"><td width="280"><code>links.person</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Reference to Person resource References: <code>Person</code></td></tr>
<tr class="nested-element"><td width="280"><code>links.relatedPerson</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Reference to RelatedPerson resource References: <code>RelatedPerson</code></td></tr>
<tr class="top-element"><td width="280"><code>context</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td></td></tr></tbody>
</table>


## User

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>entitlements</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>A list of entitlements for the User that represent a thing the User has.</td></tr>
<tr class="nested-element"><td width="280"><code>entitlements.value</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The value of an entitlement.</td></tr>
<tr class="nested-element"><td width="280"><code>entitlements.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>entitlements.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the attribute's function.</td></tr>
<tr class="nested-element"><td width="280"><code>entitlements.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary entitlement. Only one may be 'true'.</td></tr>
<tr class="top-element"><td width="280"><code>profileUrl</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td>A fully qualified URL pointing to a page representing the User's online profile.</td></tr>
<tr class="top-element"><td width="280"><code>department</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Identifies the name of a department.</td></tr>
<tr class="top-element"><td width="280"><code>preferredLanguage</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The User's preferred written or spoken language, e.g. 'en_US'.</td></tr>
<tr class="top-element"><td width="280"><code>securityLabel</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>List of security labes associated to the user</td></tr>
<tr class="nested-element"><td width="280"><code>securityLabel.system</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Code system</td></tr>
<tr class="nested-element"><td width="280"><code>securityLabel.code</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Code value</td></tr>
<tr class="top-element"><td width="280"><code>ims</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>Instant messaging addresses for the User.</td></tr>
<tr class="nested-element"><td width="280"><code>ims.value</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Instant messaging address.</td></tr>
<tr class="nested-element"><td width="280"><code>ims.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name, primarily for display (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>ims.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the IM type, e.g. 'aim', 'gtalk'.</td></tr>
<tr class="nested-element"><td width="280"><code>ims.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary IM. Only one may be 'true'.</td></tr>
<tr class="top-element"><td width="280"><code>timezone</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The User's time zone in the 'Olson' format, e.g. 'America/Los_Angeles'.</td></tr>
<tr class="top-element"><td width="280"><code>displayName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The name of the User, suitable for display to end-users.</td></tr>
<tr class="top-element"><td width="280"><code>twoFactor</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td>Two factor settings for user</td></tr>
<tr class="nested-element required-field"><td width="280"><code class="required">twoFactor.enabled</code></td><td width="90">1..1</td><td width="150"><code>boolean</code></td><td>Defines whether two-factor auth is currently enabled.</td></tr>
<tr class="nested-element"><td width="280"><code>twoFactor.transport</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Transport of 2FA confirmation code (if used).</td></tr>
<tr class="nested-element required-field"><td width="280"><code class="required">twoFactor.secretKey</code></td><td width="90">1..1</td><td width="150"><code>string</code></td><td>TOTP Secret key.</td></tr>
<tr class="top-element"><td width="280"><code>gender</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The user's gender.</td></tr>
<tr class="top-element"><td width="280"><code>email</code></td><td width="90">0..1</td><td width="150"><code>email</code></td><td>Primary email for the user.</td></tr>
<tr class="top-element"><td width="280"><code>userType</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Identifies the relationship between the organization and the user (e.g. 'Employee', 'Contractor').</td></tr>
<tr class="top-element"><td width="280"><code>division</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Identifies the name of a division.</td></tr>
<tr class="top-element"><td width="280"><code>name</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td>The components of the user's real name (formatted, family, given, etc.).</td></tr>
<tr class="nested-element"><td width="280"><code>name.formatted</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Full name, including titles and suffixes, formatted for display.</td></tr>
<tr class="nested-element"><td width="280"><code>name.familyName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Family name (last name in Western languages).</td></tr>
<tr class="nested-element"><td width="280"><code>name.givenName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Given name (first name in Western languages).</td></tr>
<tr class="nested-element"><td width="280"><code>name.middleName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The middle name(s) of the User.</td></tr>
<tr class="nested-element"><td width="280"><code>name.honorificPrefix</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Honorific prefix (title), e.g. 'Ms.'.</td></tr>
<tr class="nested-element"><td width="280"><code>name.honorificSuffix</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Honorific suffix, e.g. 'III'.</td></tr>
<tr class="top-element"><td width="280"><code>locale</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Indicates the User's default location for localization (e.g., currency, date format).</td></tr>
<tr class="top-element"><td width="280"><code>fhirUser</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>A reference to a related FHIR resource References: <code>Patient</code>, <code>Practitioner</code>, <code>Person</code></td></tr>
<tr class="top-element"><td width="280"><code>identifier</code></td><td width="90">0..*</td><td width="150"><code>Identifier</code></td><td>A list of identifiers for the user.</td></tr>
<tr class="top-element"><td width="280"><code>photo</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td>Primary photo for the user.</td></tr>
<tr class="top-element"><td width="280"><code>phoneNumber</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Primary phone number.</td></tr>
<tr class="top-element"><td width="280"><code>userName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Unique identifier for the User, typically used to directly authenticate. Must be unique across the service provider's Users.</td></tr>
<tr class="top-element"><td width="280"><code>addresses</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>A physical mailing address for this User (e.g. 'work', 'home').</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.formatted</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Full address, formatted for display or mailing label.</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.streetAddress</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Street address component (may contain newlines).</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.locality</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>City or locality component.</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.region</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>State or region component.</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.postalCode</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Zip code or postal code.</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.country</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Country name component.</td></tr>
<tr class="nested-element"><td width="280"><code>addresses.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the address type, e.g. 'work' or 'home'.</td></tr>
<tr class="top-element"><td width="280"><code>title</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The user's title, e.g. 'Vice President'.</td></tr>
<tr class="top-element"><td width="280"><code>link</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>A collection of references or links associated with the user.</td></tr>
<tr class="nested-element"><td width="280"><code>link.link</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>A referenced resource link.</td></tr>
<tr class="nested-element"><td width="280"><code>link.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the link's function.</td></tr>
<tr class="top-element"><td width="280"><code>employeeNumber</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Numeric or alphanumeric identifier assigned to a person by the organization.</td></tr>
<tr class="top-element"><td width="280"><code>password</code></td><td width="90">0..1</td><td width="150"><code>password</code></td><td>The User's cleartext password, used for initial or reset scenarios.</td></tr>
<tr class="top-element"><td width="280"><code>photos</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>URLs of photos of the user.</td></tr>
<tr class="nested-element"><td width="280"><code>photos.value</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td>URL of a photo of the User.</td></tr>
<tr class="nested-element"><td width="280"><code>photos.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>photos.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating 'photo' or 'thumbnail'.</td></tr>
<tr class="nested-element"><td width="280"><code>photos.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary photo. Only one may be 'true'.</td></tr>
<tr class="top-element"><td width="280"><code>manager</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Another User resource who is this User's manager. References: <code>User</code></td></tr>
<tr class="top-element"><td width="280"><code>x509Certificates</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>A list of certificates issued to the User.</td></tr>
<tr class="nested-element"><td width="280"><code>x509Certificates.value</code></td><td width="90">0..1</td><td width="150"><code>base64Binary</code></td><td>The value of an X.509 certificate (base64).</td></tr>
<tr class="nested-element"><td width="280"><code>x509Certificates.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>x509Certificates.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the certificate's function.</td></tr>
<tr class="nested-element"><td width="280"><code>x509Certificates.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary certificate. Only one may be 'true'.</td></tr>
<tr class="top-element"><td width="280"><code>emails</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>Email addresses for the user. Values should be canonicalized (e.g. 'bjensen@example.com').</td></tr>
<tr class="nested-element"><td width="280"><code>emails.value</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>An individual email address (canonicalized).</td></tr>
<tr class="nested-element"><td width="280"><code>emails.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name for display purposes (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>emails.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the attribute's function, e.g. 'work', 'home'.</td></tr>
<tr class="nested-element"><td width="280"><code>emails.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary email. Only one primary may be 'true'.</td></tr>
<tr class="top-element"><td width="280"><code>inactive</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>A Boolean value indicating the User's administrative status.</td></tr>
<tr class="top-element"><td width="280"><code>active</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>NB: this attr is ignored. Indicates the User's administrative status.</td></tr>
<tr class="top-element"><td width="280"><code>phoneNumbers</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>Phone numbers for the User, e.g. 'tel:+1-201-555-0123'.</td></tr>
<tr class="nested-element"><td width="280"><code>phoneNumbers.value</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The user's phone number.</td></tr>
<tr class="nested-element"><td width="280"><code>phoneNumbers.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name for display purposes (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>phoneNumbers.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label for the phone number's function, e.g. 'home', 'work'.</td></tr>
<tr class="nested-element"><td width="280"><code>phoneNumbers.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary phone number. Only one may be 'true'.</td></tr>
<tr class="top-element"><td width="280"><code>data</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>Arbitrary user-related data.</td></tr>
<tr class="top-element"><td width="280"><code>organization</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>Identifies the name of an organization. References: <code>Organization</code></td></tr>
<tr class="top-element"><td width="280"><code>costCenter</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Identifies the name of a cost center.</td></tr>
<tr class="top-element"><td width="280"><code>roles</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td>A list of roles for the User that collectively represent who the User is (e.g. 'Student', 'Faculty').</td></tr>
<tr class="nested-element"><td width="280"><code>roles.value</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>The value of a role.</td></tr>
<tr class="nested-element"><td width="280"><code>roles.display</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr class="nested-element"><td width="280"><code>roles.type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>A label indicating the attribute's function.</td></tr>
<tr class="nested-element"><td width="280"><code>roles.primary</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td>Indicates if this is the primary role. Only one may be 'true'.</td></tr></tbody>
</table>


## Scope

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element required-field"><td width="280"><code class="required">scope</code></td><td width="90">1..1</td><td width="150"><code>string</code></td><td>The value of the scope</td></tr>
<tr class="top-element required-field"><td width="280"><code class="required">title</code></td><td width="90">1..1</td><td width="150"><code>string</code></td><td>A user-friendly name for the scope that appears on the consent screen</td></tr>
<tr class="top-element"><td width="280"><code>description</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>When provided, the scope definition is additionally displayed on the consent screen</td></tr></tbody>
</table>


## Client

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>first_party</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>auth</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials.token_format</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: jwt</td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials.access_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials.refresh_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials.audience</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials.client_assertion_types</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td>Allowed values: urn:ietf:params:oauth:client-assertion-type:jwt-bearer</td></tr>
<tr class="nested-element"><td width="280"><code>auth.client_credentials.refresh_token</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.implicit</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.implicit.redirect_uri</code></td><td width="90">0..1</td><td width="150"><code>url</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.implicit.token_format</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: jwt</td></tr>
<tr class="nested-element"><td width="280"><code>auth.implicit.audience</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.implicit.access_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.password</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.secret_required</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.audience</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.refresh_token</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.redirect_uri</code></td><td width="90">0..1</td><td width="150"><code>url</code></td><td>If present, turn on redirect protection</td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.token_format</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: jwt</td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.access_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.password.refresh_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.token_format</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: jwt</td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.audience</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.secret_required</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.pkce</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.redirect_uri</code></td><td width="90">0..1</td><td width="150"><code>url</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.access_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.refresh_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.authorization_code.refresh_token</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.token_exchange</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.token_exchange.token_format</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: jwt</td></tr>
<tr class="nested-element"><td width="280"><code>auth.token_exchange.access_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.token_exchange.refresh_token_expiration</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.token_exchange.audience</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>auth.token_exchange.refresh_token</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>trusted</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>allowed_origins</code></td><td width="90">0..*</td><td width="150"><code>uri</code></td><td>Allowed Origins are URLs that will be allowed to make requests from JavaScript to Server (CORS). By default, callback URLs are allowed. You can use wildcards at the subdomain level (e.g., https://*.contoso.com). Query strings and hash info are not considered.</td></tr>
<tr class="top-element"><td width="280"><code>grant_types</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>name</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>jwks</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>jwks.kid</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>jwks.kty</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: RSA</td></tr>
<tr class="nested-element"><td width="280"><code>jwks.alg</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: RS384</td></tr>
<tr class="nested-element"><td width="280"><code>jwks.e</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>jwks.n</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>jwks.use</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Allowed values: sig</td></tr>
<tr class="top-element"><td width="280"><code>scopes</code></td><td width="90">0..*</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>scopes.policy</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>AccessPolicy</code></td></tr>
<tr class="nested-element"><td width="280"><code>scopes.parameters</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>fhir-base-url</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>allowed-scopes</code></td><td width="90">0..*</td><td width="150"><code>Reference</code></td><td>References: <code>Scope</code></td></tr>
<tr class="top-element"><td width="280"><code>scope</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>allowedIssuers</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>secret</code></td><td width="90">0..1</td><td width="150"><code>sha256Hash</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>details</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>active</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>jwks_uri</code></td><td width="90">0..1</td><td width="150"><code>url</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>smart</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>smart.launch_uri</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>smart.name</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>smart.description</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>description</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr></tbody>
</table>


## Grant

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>user</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>User</code></td></tr>
<tr class="top-element"><td width="280"><code>client</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>Client</code></td></tr>
<tr class="top-element"><td width="280"><code>requested-scope</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>provided-scope</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>patient</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>Patient</code></td></tr>
<tr class="top-element"><td width="280"><code>scope</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>start</code></td><td width="90">0..1</td><td width="150"><code>dateTime</code></td><td></td></tr></tbody>
</table>


## Session

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>on-behalf</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>User</code></td></tr>
<tr class="top-element"><td width="280"><code>parent</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>Session</code></td></tr>
<tr class="top-element"><td width="280"><code>user</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>User</code></td></tr>
<tr class="top-element"><td width="280"><code>access_token</code></td><td width="90">0..1</td><td width="150"><code>sha256Hash</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>refresh_token_exp</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>jti</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>authorization_code</code></td><td width="90">0..1</td><td width="150"><code>sha256Hash</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>exp</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>start</code></td><td width="90">0..1</td><td width="150"><code>dateTime</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>scope</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>refresh_token</code></td><td width="90">0..1</td><td width="150"><code>sha256Hash</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>patient</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>Patient</code></td></tr>
<tr class="top-element"><td width="280"><code>audience</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>ctx</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>Smart on FHIR context</td></tr>
<tr class="top-element"><td width="280"><code>active</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>client</code></td><td width="90">0..1</td><td width="150"><code>Reference</code></td><td>References: <code>Client</code></td></tr>
<tr class="top-element"><td width="280"><code>end</code></td><td width="90">0..1</td><td width="150"><code>dateTime</code></td><td></td></tr></tbody>
</table>


## Notification

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>status</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>provider</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>providerData</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td></td></tr></tbody>
</table>


## NotificationTemplate

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>subject</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>template</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr></tbody>
</table>


## Registration

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>resource</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>Registration form data</td></tr>
<tr class="top-element"><td width="280"><code>status</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>params</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td>Authorization params for continue authorization process after registration</td></tr></tbody>
</table>


## IdentityProvider

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>introspection_endpoint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>registration_endpoint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>team_id</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>revocation_endpoint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>authorize_endpoint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>userinfo-source</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Source of userinfo details. If id-token, no request to userinfo_endpoint performed</td></tr>
<tr class="top-element"><td width="280"><code>userinfo_header</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>base_url</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>isEmailUniqueness</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>scopes</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>isScim</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>title</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>kid</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>type</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>organizations</code></td><td width="90">0..*</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>userinfo_endpoint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>system</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>toScim</code></td><td width="90">0..1</td><td width="150"><code>Object</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>token_endpoint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>active</code></td><td width="90">0..1</td><td width="150"><code>boolean</code></td><td></td></tr>
<tr class="top-element"><td width="280"><code>client</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>client.id</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>client.redirect_uri</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>client.auth-method</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Client authentication method. symmetric (default) or asymmetric</td></tr>
<tr class="nested-element"><td width="280"><code>client.secret</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>client.private-key</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>client.certificate</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Certificate</td></tr>
<tr class="nested-element"><td width="280"><code>client.certificate-thumbprint</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Certificate thumbprint. no colons expected.</td></tr>
<tr class="nested-element"><td width="280"><code>client.creds-ts</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Last time secret/private-key was updated</td></tr>
<tr class="top-element"><td width="280"><code>jwks_uri</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td></td></tr></tbody>
</table>


## AuthConfig

<table>
<thead>
<tr>
<th width="280">Path</th>
<th width="90">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="280"><code>theme</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>theme.brand</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Brand for auth page</td></tr>
<tr class="nested-element"><td width="280"><code>theme.title</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Title for auth page</td></tr>
<tr class="nested-element"><td width="280"><code>theme.styleUrl</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td>URL to external stylesheet</td></tr>
<tr class="nested-element"><td width="280"><code>theme.forgotPasswordUrl</code></td><td width="90">0..1</td><td width="150"><code>uri</code></td><td>URL to forgot password page</td></tr>
<tr class="top-element"><td width="280"><code>twoFactor</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>twoFactor.webhook</code></td><td width="90">0..1</td><td width="150"><code>BackboneElement</code></td><td></td></tr>
<tr class="nested-element"><td width="280"><code>twoFactor.webhook.headers</code></td><td width="90">0..1</td><td width="150"><code>Map</code></td><td>Map of HTTP header key-value pairs</td></tr>
<tr class="nested-element"><td width="280"><code>twoFactor.webhook.timeout</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td>Timeout in milliseconds</td></tr>
<tr class="nested-element required-field"><td width="280"><code class="required">twoFactor.webhook.endpoint</code></td><td width="90">1..1</td><td width="150"><code>string</code></td><td>URL to webhook that supports POST method</td></tr>
<tr class="nested-element"><td width="280"><code>twoFactor.issuerName</code></td><td width="90">0..1</td><td width="150"><code>string</code></td><td>Issuer name for OTP authenticator app</td></tr>
<tr class="nested-element"><td width="280"><code>twoFactor.validPastTokensCount</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td>Number of past tokens considered valid (useful with webhook since OTP lives ~30s)</td></tr>
<tr class="top-element"><td width="280"><code>asidCookieMaxAge</code></td><td width="90">0..1</td><td width="150"><code>integer</code></td><td></td></tr></tbody>
</table>

