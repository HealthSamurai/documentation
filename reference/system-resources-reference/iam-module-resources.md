# IAM Module Resources

The Identity and Access Management (IAM) module provides a set of resources for managing user authentication, authorization, and access control within the Aidbox. ## Overview

IAM module includes the following resource types:

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
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">matcho</td><td width="70">0..1</td><td width="150">Object</td><td>Defines rules using the Matcho pattern-matching syntax.</td></tr>
<tr><td width="290">clj</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td>JSON Schema used to validate requests against the policy.</td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">or</td><td width="70">0..*</td><td width="150">Object</td><td>A list of conditions where at least one must be satisfied for the policy to grant access.</td></tr>
<tr><td width="290">roleName</td><td width="70">0..1</td><td width="150">string</td><td>Symbolic link to Role by name</td></tr>
<tr><td width="290">and</td><td width="70">0..*</td><td width="150">Object</td><td>A list of conditions that must all be satisfied for the policy to grant access.</td></tr>
<tr><td width="290">link</td><td width="70">0..*</td><td width="150">Reference</td><td>References to resources associated with this policy. 

<strong>Allowed references</strong>: Client, User, Operation</td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>The type or category of the access policy. 

<strong>Allowed values</strong>: scope | rest | rpc</td></tr>
<tr><td width="290">engine</td><td width="70">0..1</td><td width="150">string</td><td>Specifies the evaluation engine used for the policy. 

<strong>Allowed values</strong>: json-schema | allow | sql | complex | matcho | clj | matcho-rpc | allow-rpc | signed-rpc | smart-on-fhir</td></tr>
<tr><td width="290">rpc</td><td width="70">0..1</td><td width="150">Object</td><td>Defines rules for Remote Procedure Calls (RPCs).</td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">sql</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>A textual description of the access policy.</td></tr></tbody>
</table>


## TokenIntrospector

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td>Specifies the type of token to introspect. 

<strong>Allowed values</strong>: opaque | jwt | aspxauth</td></tr>
<tr><td width="290">jwks_uri</td><td width="70">0..1</td><td width="150">string</td><td>A URL pointing to a JSON Web Key Set (JWKS). When type is jwt the introspector retrieves public keys from this URI to validate token signatures.</td></tr>
<tr><td width="290">jwt</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for local JWT validation used when type is jwt.</td></tr>
<tr><td width="290">jwt.iss</td><td width="70">0..1</td><td width="150">string</td><td>The expected issuer (iss) claim value for JWTs. The TokenIntrospector ensures that tokens it validates come from this issuer.</td></tr>
<tr><td width="290">jwt.secret</td><td width="70">0..1</td><td width="150">string</td><td>A shared secret key or other signing key material used to verify the JWT’s signature.</td></tr>
<tr><td width="290">introspection_endpoint</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">introspection_endpoint.url</td><td width="70">0..1</td><td width="150">string</td><td>The fully qualified URL of the remote introspection endpoint.</td></tr>
<tr><td width="290">introspection_endpoint.authorization</td><td width="70">0..1</td><td width="150">string</td><td>The authorization header value (e.g. a Basic Auth or Bearer token) used when calling the introspection endpoint. If present it will be included in the request headers.</td></tr>
<tr><td width="290">identity_provider</td><td width="70">0..1</td><td width="150">Reference</td><td>Link to Identity provider associated with the token introspector. 

<strong>Allowed references</strong>: IdentityProvider</td></tr></tbody>
</table>


## Role

User role

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">name</td><td width="70">1..1</td><td width="150">string</td><td>Role name is a string that defines role. To assign the same role to multiple users, create multiple Role resources with the same "name". [Search param: name => type string]</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Text description of the role</td></tr>
<tr><td width="290">user</td><td width="70">1..1</td><td width="150">Reference</td><td>Reference to a User resource for which the role will be applied. [Search param: user => type reference] 

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">links</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>You may list resources here, which can later be granted access for the user with this role via an AccessPolicy resource.</td></tr>
<tr><td width="290">links.patient</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Patient resource 

<strong>Allowed references</strong>: Patient</td></tr>
<tr><td width="290">links.practitionerRole</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to PractitionerRole resource 

<strong>Allowed references</strong>: PractitionerRole</td></tr>
<tr><td width="290">links.practitioner</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Practitioner resource 

<strong>Allowed references</strong>: Practitioner</td></tr>
<tr><td width="290">links.organization</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Organization resource 

<strong>Allowed references</strong>: Organization</td></tr>
<tr><td width="290">links.person</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Person resource 

<strong>Allowed references</strong>: Person</td></tr>
<tr><td width="290">links.relatedPerson</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to RelatedPerson resource 

<strong>Allowed references</strong>: RelatedPerson</td></tr>
<tr><td width="290">context</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
</table>


## User

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">entitlements</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A list of entitlements for the User that represent a thing the User has.</td></tr>
<tr><td width="290">entitlements.value</td><td width="70">0..1</td><td width="150">string</td><td>The value of an entitlement.</td></tr>
<tr><td width="290">entitlements.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">entitlements.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the attribute's function.</td></tr>
<tr><td width="290">entitlements.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary entitlement. Only one may be 'true'.</td></tr>
<tr><td width="290">profileUrl</td><td width="70">0..1</td><td width="150">uri</td><td>A fully qualified URL pointing to a page representing the User's online profile.</td></tr>
<tr><td width="290">department</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the name of a department.</td></tr>
<tr><td width="290">preferredLanguage</td><td width="70">0..1</td><td width="150">string</td><td>The User's preferred written or spoken language, e.g. 'en_US'.</td></tr>
<tr><td width="290">securityLabel</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>List of security labes associated to the user</td></tr>
<tr><td width="290">securityLabel.system</td><td width="70">0..1</td><td width="150">string</td><td>Code system</td></tr>
<tr><td width="290">securityLabel.code</td><td width="70">0..1</td><td width="150">string</td><td>Code value</td></tr>
<tr><td width="290">ims</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Instant messaging addresses for the User.</td></tr>
<tr><td width="290">ims.value</td><td width="70">0..1</td><td width="150">string</td><td>Instant messaging address.</td></tr>
<tr><td width="290">ims.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily for display (READ-ONLY).</td></tr>
<tr><td width="290">ims.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the IM type, e.g. 'aim', 'gtalk'.</td></tr>
<tr><td width="290">ims.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary IM. Only one may be 'true'.</td></tr>
<tr><td width="290">timezone</td><td width="70">0..1</td><td width="150">string</td><td>The User's time zone in the 'Olson' format, e.g. 'America/Los_Angeles'.</td></tr>
<tr><td width="290">displayName</td><td width="70">0..1</td><td width="150">string</td><td>The name of the User, suitable for display to end-users.</td></tr>
<tr><td width="290">twoFactor</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Two factor settings for user</td></tr>
<tr><td width="290">twoFactor.enabled</td><td width="70">1..1</td><td width="150">boolean</td><td>Defines whether two-factor auth is currently enabled.</td></tr>
<tr><td width="290">twoFactor.transport</td><td width="70">0..1</td><td width="150">string</td><td>Transport of 2FA confirmation code (if used).</td></tr>
<tr><td width="290">twoFactor.secretKey</td><td width="70">1..1</td><td width="150">string</td><td>TOTP Secret key.</td></tr>
<tr><td width="290">gender</td><td width="70">0..1</td><td width="150">string</td><td>The user's gender.</td></tr>
<tr><td width="290">email</td><td width="70">0..1</td><td width="150">email</td><td>Primary email for the user.</td></tr>
<tr><td width="290">userType</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the relationship between the organization and the user (e.g. 'Employee', 'Contractor').</td></tr>
<tr><td width="290">division</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the name of a division.</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>The components of the user's real name (formatted, family, given, etc.).</td></tr>
<tr><td width="290">name.formatted</td><td width="70">0..1</td><td width="150">string</td><td>Full name, including titles and suffixes, formatted for display.</td></tr>
<tr><td width="290">name.familyName</td><td width="70">0..1</td><td width="150">string</td><td>Family name (last name in Western languages).</td></tr>
<tr><td width="290">name.givenName</td><td width="70">0..1</td><td width="150">string</td><td>Given name (first name in Western languages).</td></tr>
<tr><td width="290">name.middleName</td><td width="70">0..1</td><td width="150">string</td><td>The middle name(s) of the User.</td></tr>
<tr><td width="290">name.honorificPrefix</td><td width="70">0..1</td><td width="150">string</td><td>Honorific prefix (title), e.g. 'Ms.'.</td></tr>
<tr><td width="290">name.honorificSuffix</td><td width="70">0..1</td><td width="150">string</td><td>Honorific suffix, e.g. 'III'.</td></tr>
<tr><td width="290">locale</td><td width="70">0..1</td><td width="150">string</td><td>Indicates the User's default location for localization (e.g., currency, date format).</td></tr>
<tr><td width="290">fhirUser</td><td width="70">0..1</td><td width="150">Reference</td><td>A reference to a related FHIR resource 

<strong>Allowed references</strong>: Patient, Practitioner, Person</td></tr>
<tr><td width="290">identifier</td><td width="70">0..*</td><td width="150">Identifier</td><td>A list of identifiers for the user.</td></tr>
<tr><td width="290">photo</td><td width="70">0..1</td><td width="150">uri</td><td>Primary photo for the user.</td></tr>
<tr><td width="290">phoneNumber</td><td width="70">0..1</td><td width="150">string</td><td>Primary phone number.</td></tr>
<tr><td width="290">userName</td><td width="70">0..1</td><td width="150">string</td><td>Unique identifier for the User, typically used to directly authenticate. Must be unique across the service provider's Users.</td></tr>
<tr><td width="290">addresses</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A physical mailing address for this User (e.g. 'work', 'home').</td></tr>
<tr><td width="290">addresses.formatted</td><td width="70">0..1</td><td width="150">string</td><td>Full address, formatted for display or mailing label.</td></tr>
<tr><td width="290">addresses.streetAddress</td><td width="70">0..1</td><td width="150">string</td><td>Street address component (may contain newlines).</td></tr>
<tr><td width="290">addresses.locality</td><td width="70">0..1</td><td width="150">string</td><td>City or locality component.</td></tr>
<tr><td width="290">addresses.region</td><td width="70">0..1</td><td width="150">string</td><td>State or region component.</td></tr>
<tr><td width="290">addresses.postalCode</td><td width="70">0..1</td><td width="150">string</td><td>Zip code or postal code.</td></tr>
<tr><td width="290">addresses.country</td><td width="70">0..1</td><td width="150">string</td><td>Country name component.</td></tr>
<tr><td width="290">addresses.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the address type, e.g. 'work' or 'home'.</td></tr>
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td>The user's title, e.g. 'Vice President'.</td></tr>
<tr><td width="290">link</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A collection of references or links associated with the user.</td></tr>
<tr><td width="290">link.link</td><td width="70">0..1</td><td width="150">Reference</td><td>A referenced resource link.</td></tr>
<tr><td width="290">link.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the link's function.</td></tr>
<tr><td width="290">employeeNumber</td><td width="70">0..1</td><td width="150">string</td><td>Numeric or alphanumeric identifier assigned to a person by the organization.</td></tr>
<tr><td width="290">password</td><td width="70">0..1</td><td width="150">password</td><td>The User's cleartext password, used for initial or reset scenarios.</td></tr>
<tr><td width="290">photos</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>URLs of photos of the user.</td></tr>
<tr><td width="290">photos.value</td><td width="70">0..1</td><td width="150">uri</td><td>URL of a photo of the User.</td></tr>
<tr><td width="290">photos.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">photos.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating 'photo' or 'thumbnail'.</td></tr>
<tr><td width="290">photos.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary photo. Only one may be 'true'.</td></tr>
<tr><td width="290">manager</td><td width="70">0..1</td><td width="150">Reference</td><td>Another User resource who is this User's manager. 

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">x509Certificates</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A list of certificates issued to the User.</td></tr>
<tr><td width="290">x509Certificates.value</td><td width="70">0..1</td><td width="150">base64Binary</td><td>The value of an X.509 certificate (base64).</td></tr>
<tr><td width="290">x509Certificates.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">x509Certificates.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the certificate's function.</td></tr>
<tr><td width="290">x509Certificates.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary certificate. Only one may be 'true'.</td></tr>
<tr><td width="290">emails</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Email addresses for the user. Values should be canonicalized (e.g. 'bjensen@example.com').</td></tr>
<tr><td width="290">emails.value</td><td width="70">0..1</td><td width="150">string</td><td>An individual email address (canonicalized).</td></tr>
<tr><td width="290">emails.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">emails.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the attribute's function, e.g. 'work', 'home'.</td></tr>
<tr><td width="290">emails.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary email. Only one primary may be 'true'.</td></tr>
<tr><td width="290">inactive</td><td width="70">0..1</td><td width="150">boolean</td><td>A Boolean value indicating the User's administrative status.</td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td>NB: this attr is ignored. Indicates the User's administrative status.</td></tr>
<tr><td width="290">phoneNumbers</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Phone numbers for the User, e.g. 'tel:+1-201-555-0123'.</td></tr>
<tr><td width="290">phoneNumbers.value</td><td width="70">0..1</td><td width="150">string</td><td>The user's phone number.</td></tr>
<tr><td width="290">phoneNumbers.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">phoneNumbers.type</td><td width="70">0..1</td><td width="150">string</td><td>A label for the phone number's function, e.g. 'home', 'work'.</td></tr>
<tr><td width="290">phoneNumbers.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary phone number. Only one may be 'true'.</td></tr>
<tr><td width="290">data</td><td width="70">0..1</td><td width="150">Object</td><td>Arbitrary user-related data.</td></tr>
<tr><td width="290">organization</td><td width="70">0..1</td><td width="150">Reference</td><td>Identifies the name of an organization. 

<strong>Allowed references</strong>: Organization</td></tr>
<tr><td width="290">costCenter</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the name of a cost center.</td></tr>
<tr><td width="290">roles</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A list of roles for the User that collectively represent who the User is (e.g. 'Student', 'Faculty').</td></tr>
<tr><td width="290">roles.value</td><td width="70">0..1</td><td width="150">string</td><td>The value of a role.</td></tr>
<tr><td width="290">roles.display</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">roles.type</td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the attribute's function.</td></tr>
<tr><td width="290">roles.primary</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary role. Only one may be 'true'.</td></tr></tbody>
</table>


## Scope

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">scope</td><td width="70">1..1</td><td width="150">string</td><td>The value of the scope</td></tr>
<tr><td width="290">title</td><td width="70">1..1</td><td width="150">string</td><td>A user-friendly name for the scope that appears on the consent screen</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>When provided, the scope definition is additionally displayed on the consent screen</td></tr></tbody>
</table>


## Client

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">first_party</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.client_credentials</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.client_credentials.token_format</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: jwt</td></tr>
<tr><td width="290">auth.client_credentials.access_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.client_credentials.refresh_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.client_credentials.audience</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">auth.client_credentials.client_assertion_types</td><td width="70">0..*</td><td width="150">string</td><td>

<strong>Allowed values</strong>: urn:ietf:params:oauth:client-assertion-type:jwt-bearer</td></tr>
<tr><td width="290">auth.client_credentials.refresh_token</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth.implicit</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.implicit.redirect_uri</td><td width="70">0..1</td><td width="150">url</td><td></td></tr>
<tr><td width="290">auth.implicit.token_format</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: jwt</td></tr>
<tr><td width="290">auth.implicit.audience</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">auth.implicit.access_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.password</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.password.secret_required</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth.password.audience</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">auth.password.refresh_token</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth.password.redirect_uri</td><td width="70">0..1</td><td width="150">url</td><td>If present, turn on redirect protection</td></tr>
<tr><td width="290">auth.password.token_format</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: jwt</td></tr>
<tr><td width="290">auth.password.access_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.password.refresh_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.authorization_code</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.authorization_code.token_format</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: jwt</td></tr>
<tr><td width="290">auth.authorization_code.audience</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">auth.authorization_code.secret_required</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth.authorization_code.pkce</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth.authorization_code.redirect_uri</td><td width="70">0..1</td><td width="150">url</td><td></td></tr>
<tr><td width="290">auth.authorization_code.access_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.authorization_code.refresh_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.authorization_code.refresh_token</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">auth.token_exchange</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.token_exchange.token_format</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: jwt</td></tr>
<tr><td width="290">auth.token_exchange.access_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.token_exchange.refresh_token_expiration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">auth.token_exchange.audience</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">auth.token_exchange.refresh_token</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">trusted</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">allowed_origins</td><td width="70">0..*</td><td width="150">uri</td><td>Allowed Origins are URLs that will be allowed to make requests from JavaScript to Server (CORS). By default, callback URLs are allowed. You can use wildcards at the subdomain level (e.g., https://*.contoso.com). Query strings and hash info are not considered.</td></tr>
<tr><td width="290">grant_types</td><td width="70">0..*</td><td width="150">string</td><td>

<strong>Allowed values</strong>: basic | authorization_code | code | password | client_credentials | implicit | refresh_token | urn:ietf:params:oauth:grant-type:token-exchange</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">jwks</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">jwks.kid</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">jwks.kty</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: RSA</td></tr>
<tr><td width="290">jwks.alg</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: RS384</td></tr>
<tr><td width="290">jwks.e</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">jwks.n</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">jwks.use</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: sig</td></tr>
<tr><td width="290">scopes</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">scopes.policy</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: AccessPolicy</td></tr>
<tr><td width="290">scopes.parameters</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">fhir-base-url</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">allowed-scopes</td><td width="70">0..*</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: Scope</td></tr>
<tr><td width="290">scope</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">allowedIssuers</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">secret</td><td width="70">0..1</td><td width="150">sha256Hash</td><td></td></tr>
<tr><td width="290">details</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">jwks_uri</td><td width="70">0..1</td><td width="150">url</td><td></td></tr>
<tr><td width="290">smart</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">smart.launch_uri</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">smart.name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">smart.description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## Grant

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">client</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: Client</td></tr>
<tr><td width="290">requested-scope</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">provided-scope</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">patient</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: Patient</td></tr>
<tr><td width="290">scope</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr></tbody>
</table>


## Session

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">on-behalf</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">parent</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: Session</td></tr>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">access_token</td><td width="70">0..1</td><td width="150">sha256Hash</td><td></td></tr>
<tr><td width="290">refresh_token_exp</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">jti</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">authorization_code</td><td width="70">0..1</td><td width="150">sha256Hash</td><td></td></tr>
<tr><td width="290">exp</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">scope</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">refresh_token</td><td width="70">0..1</td><td width="150">sha256Hash</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">patient</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: Patient</td></tr>
<tr><td width="290">audience</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx</td><td width="70">0..1</td><td width="150">Object</td><td>Smart on FHIR context</td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">client</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: Client</td></tr>
<tr><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr></tbody>
</table>


## Notification

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: delivered | error</td></tr>
<tr><td width="290">provider</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">providerData</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
</table>


## NotificationTemplate

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">subject</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">template</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## Registration

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">Object</td><td>Registration form data</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: activated | active</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td>Authorization params for continue authorization process after registration</td></tr></tbody>
</table>


## IdentityProvider

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">introspection_endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">registration_endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">team_id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">revocation_endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">authorize_endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">userinfo-source</td><td width="70">0..1</td><td width="150">string</td><td>Source of userinfo details. If id-token, no request to userinfo_endpoint performed 

<strong>Allowed values</strong>: id-token | userinfo-endpoint</td></tr>
<tr><td width="290">userinfo_header</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">base_url</td><td width="70">0..1</td><td width="150">uri</td><td></td></tr>
<tr><td width="290">isEmailUniqueness</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">scopes</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">isScim</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">kid</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: aidbox | github | google | OIDC | OAuth | az-dev | yandex | okta | apple</td></tr>
<tr><td width="290">organizations</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">userinfo_endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">system</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">toScim</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">token_endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">client</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">client.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">client.redirect_uri</td><td width="70">0..1</td><td width="150">uri</td><td></td></tr>
<tr><td width="290">client.auth-method</td><td width="70">0..1</td><td width="150">string</td><td>Client authentication method. symmetric (default) or asymmetric 

<strong>Allowed values</strong>: symmetric | asymmetric</td></tr>
<tr><td width="290">client.secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">client.private-key</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">client.certificate</td><td width="70">0..1</td><td width="150">string</td><td>Certificate</td></tr>
<tr><td width="290">client.certificate-thumbprint</td><td width="70">0..1</td><td width="150">string</td><td>Certificate thumbprint. no colons expected.</td></tr>
<tr><td width="290">client.creds-ts</td><td width="70">0..1</td><td width="150">string</td><td>Last time secret/private-key was updated</td></tr>
<tr><td width="290">jwks_uri</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## AuthConfig

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">theme</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">theme.brand</td><td width="70">0..1</td><td width="150">string</td><td>Brand for auth page</td></tr>
<tr><td width="290">theme.title</td><td width="70">0..1</td><td width="150">string</td><td>Title for auth page</td></tr>
<tr><td width="290">theme.styleUrl</td><td width="70">0..1</td><td width="150">uri</td><td>URL to external stylesheet</td></tr>
<tr><td width="290">theme.forgotPasswordUrl</td><td width="70">0..1</td><td width="150">uri</td><td>URL to forgot password page</td></tr>
<tr><td width="290">twoFactor</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">twoFactor.webhook</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">twoFactor.webhook.headers</td><td width="70">0..1</td><td width="150">Map</td><td>Map of HTTP header key-value pairs</td></tr>
<tr><td width="290">twoFactor.webhook.timeout</td><td width="70">0..1</td><td width="150">integer</td><td>Timeout in milliseconds</td></tr>
<tr><td width="290">twoFactor.webhook.endpoint</td><td width="70">1..1</td><td width="150">string</td><td>URL to webhook that supports POST method</td></tr>
<tr><td width="290">twoFactor.issuerName</td><td width="70">0..1</td><td width="150">string</td><td>Issuer name for OTP authenticator app</td></tr>
<tr><td width="290">twoFactor.validPastTokensCount</td><td width="70">0..1</td><td width="150">integer</td><td>Number of past tokens considered valid (useful with webhook since OTP lives ~30s)</td></tr>
<tr><td width="290">asidCookieMaxAge</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr></tbody>
</table>

