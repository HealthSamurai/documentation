# IAM Module Resources

The Identity and Access Management (IAM) module provides a set of resources for managing user authentication, authorization, and access control within the Aidbox.

 ## Overview

IAM module includes the following resource types:

- AccessPolicy
- AuthConfig
- Client
- Grant
- IdentityProvider
- Notification
- NotificationTemplate
- Registration
- Role
- Scope
- Session
- TokenIntrospector
- User

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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">and</td><td width="70">0..*</td><td width="150">Object</td><td>A list of conditions that must all be satisfied for the policy to grant access.</td></tr>
<tr><td width="290">clj</td><td width="70">0..1</td><td width="150">string</td><td>Clojure code that defines access policy rules. 
 DEPRECATED. DO NOT USE IT.</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>A textual description of the access policy.</td></tr>
<tr><td width="290">engine</td><td width="70">0..1</td><td width="150">string</td><td>Specifies the evaluation engine used for the policy. 

<strong>Allowed values</strong>: `json-schema` | `allow` | `sql` | `complex` | `matcho` | `clj` | `matcho-rpc` | `allow-rpc` | `signed-rpc` | `smart-on-fhir`</td></tr>
<tr><td width="290">link</td><td width="70">0..*</td><td width="150">Reference</td><td>References to resources associated with this policy. 

<strong>Allowed references</strong>: Client, User, Operation</td></tr>
<tr><td width="290">matcho</td><td width="70">0..1</td><td width="150">Object</td><td>Defines rules using the Matcho pattern-matching syntax.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this policy belongs to.</td></tr>
<tr><td width="290">or</td><td width="70">0..*</td><td width="150">Object</td><td>A list of conditions where at least one must be satisfied for the policy to grant access.</td></tr>
<tr><td width="290">roleName</td><td width="70">0..1</td><td width="150">string</td><td>Symbolic link to Role by name</td></tr>
<tr><td width="290">rpc</td><td width="70">0..1</td><td width="150">Object</td><td>Defines rules for Remote Procedure Calls (RPCs).</td></tr>
<tr><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td>JSON Schema used to validate requests against the policy.</td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td>Source identifier for the policy.</td></tr>
<tr><td width="290">sql</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>SQL-based policy definition.</td></tr>
<tr><td width="290">sql.<strong>query</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL query used to evaluate access conditions.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>The type or category of the access policy. 

<strong>Allowed values</strong>: `scope` | `rest` | `rpc`</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">asidCookieMaxAge</td><td width="70">0..1</td><td width="150">integer</td><td>In Aidbox version v:2402 and later, sessions created through the Aidbox UI log-in are not infinite.The default session expiration time is set to 432000 seconds (5 days).</td></tr>
<tr><td width="290">theme</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">theme.<strong>brand</strong></td><td width="70">0..1</td><td width="150">string</td><td>Brand for auth page</td></tr>
<tr><td width="290">theme.<strong>title</strong></td><td width="70">0..1</td><td width="150">string</td><td>Title for auth page</td></tr>
<tr><td width="290">theme.<strong>styleUrl</strong></td><td width="70">0..1</td><td width="150">uri</td><td>URL to external stylesheet</td></tr>
<tr><td width="290">theme.<strong>forgotPasswordUrl</strong></td><td width="70">0..1</td><td width="150">uri</td><td>URL to forgot password page</td></tr>
<tr><td width="290">twoFactor</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">twoFactor.<strong>webhook</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">twoFactor.<strong>webhook</strong>.<strong>headers</strong></td><td width="70">0..1</td><td width="150">Map</td><td>Map of HTTP header key-value pairs</td></tr>
<tr><td width="290">twoFactor.<strong>webhook</strong>.<strong>timeout</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Timeout in milliseconds</td></tr>
<tr><td width="290">twoFactor.<strong>webhook</strong>.<strong>endpoint</strong></td><td width="70">1..1</td><td width="150">string</td><td>URL to webhook that supports POST method</td></tr>
<tr><td width="290">twoFactor.<strong>issuerName</strong></td><td width="70">0..1</td><td width="150">string</td><td>Issuer name for OTP authenticator app</td></tr>
<tr><td width="290">twoFactor.<strong>validPastTokensCount</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Number of past tokens considered valid (useful with webhook since OTP lives ~30s)</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether this client is active and can be used for authentication.</td></tr>
<tr><td width="290">allowed-scopes</td><td width="70">0..*</td><td width="150">Reference</td><td>References to specific Scope resources this client is allowed to request. 

<strong>Allowed references</strong>: Scope</td></tr>
<tr><td width="290">allowedIssuers</td><td width="70">0..*</td><td width="150">string</td><td>List of authorized token issuers for this client.</td></tr>
<tr><td width="290">allowed_origins</td><td width="70">0..*</td><td width="150">uri</td><td>Allowed Origins are URLs that will be allowed to make requests.</td></tr>
<tr><td width="290">auth</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Authentication configuration for different OAuth flows.</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for the client credentials grant type.</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong>.<strong>token_format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format of the access token. 

<strong>Allowed values</strong>: `jwt`</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong>.<strong>access_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for access tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong>.<strong>refresh_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for refresh tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong>.<strong>audience</strong></td><td width="70">0..*</td><td width="150">string</td><td>Intended audience for issued tokens. Shows what resource server access is intended for. Aidbox compares the audience of the Client to the audience it receives within aJWT and decides if the access should be granted. The audience attribute can be defined in 2 ways: As a plain string, e.g. https://cmpl.aidbox.app/smart As a Regex. In that case, the audience value should start with the # symbol. For example, #https://cmpl.aidbox.app/tenant/[^\]/smart That validation of the audience happens when SMART on FHIR app launches</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong>.<strong>client_assertion_types</strong></td><td width="70">0..*</td><td width="150">string</td><td>Supported client assertion types. 

<strong>Allowed values</strong>: `urn:ietf:params:oauth:client-assertion-type:jwt-bearer`</td></tr>
<tr><td width="290">auth.<strong>client_credentials</strong>.<strong>refresh_token</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to issue refresh tokens with this grant type.</td></tr>
<tr><td width="290">auth.<strong>implicit</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for the implicit grant type.</td></tr>
<tr><td width="290">auth.<strong>implicit</strong>.<strong>redirect_uri</strong></td><td width="70">0..1</td><td width="150">url</td><td>Redirect URI for the implicit flow.</td></tr>
<tr><td width="290">auth.<strong>implicit</strong>.<strong>token_format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format of the access token. 

<strong>Allowed values</strong>: `jwt`</td></tr>
<tr><td width="290">auth.<strong>implicit</strong>.<strong>audience</strong></td><td width="70">0..*</td><td width="150">string</td><td>Intended audience for issued tokens.</td></tr>
<tr><td width="290">auth.<strong>implicit</strong>.<strong>access_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for access tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>password</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for the password grant type.</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>secret_required</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether client secret is required for password grant.</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>audience</strong></td><td width="70">0..*</td><td width="150">string</td><td>Intended audience for issued tokens.</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>refresh_token</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to issue refresh tokens with this grant type.</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>redirect_uri</strong></td><td width="70">0..1</td><td width="150">url</td><td>If present, turn on redirect protection</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>token_format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format of the access token. 

<strong>Allowed values</strong>: `jwt`</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>access_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for access tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>password</strong>.<strong>refresh_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for refresh tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for the authorization code grant type.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>token_format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format of the access token. 

<strong>Allowed values</strong>: `jwt`</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>audience</strong></td><td width="70">0..*</td><td width="150">string</td><td>Intended audience for issued tokens.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>secret_required</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether client secret is required for token exchange.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>pkce</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether PKCE (Proof Key for Code Exchange) is required.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>redirect_uri</strong></td><td width="70">0..1</td><td width="150">url</td><td>Redirect URI for the authorization code flow.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>access_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for access tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>refresh_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for refresh tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>authorization_code</strong>.<strong>refresh_token</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to issue refresh tokens with this grant type.</td></tr>
<tr><td width="290">auth.<strong>token_exchange</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for the token exchange grant type.</td></tr>
<tr><td width="290">auth.<strong>token_exchange</strong>.<strong>token_format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format of the access token. 

<strong>Allowed values</strong>: `jwt`</td></tr>
<tr><td width="290">auth.<strong>token_exchange</strong>.<strong>access_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for access tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>token_exchange</strong>.<strong>refresh_token_expiration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for refresh tokens in seconds.</td></tr>
<tr><td width="290">auth.<strong>token_exchange</strong>.<strong>audience</strong></td><td width="70">0..*</td><td width="150">string</td><td>Intended audience for issued tokens.</td></tr>
<tr><td width="290">auth.<strong>token_exchange</strong>.<strong>refresh_token</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to issue refresh tokens with this grant type.</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>A description of the client application for administrative purposes.</td></tr>
<tr><td width="290">details</td><td width="70">0..1</td><td width="150">Object</td><td>Additional client details or configuration options.</td></tr>
<tr><td width="290">fhir-base-url</td><td width="70">0..1</td><td width="150">string</td><td>Base URL of the FHIR server this client interacts with.</td></tr>
<tr><td width="290">first_party</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether this is a first-party client.</td></tr>
<tr><td width="290">grant_types</td><td width="70">0..*</td><td width="150">string</td><td>OAuth 2.0 grant types this client is authorized to use. 

<strong>Allowed values</strong>: `basic` | `authorization_code` | `code` | `password` | `client_credentials` | `implicit` | `refresh_token` | `urn:ietf:params:oauth:grant-type:token-exchange`</td></tr>
<tr><td width="290">jwks</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>JSON Web Key Set for client authentication and/or verification.</td></tr>
<tr><td width="290">jwks.<strong>kid</strong></td><td width="70">0..1</td><td width="150">string</td><td>Key ID that identifies this key.</td></tr>
<tr><td width="290">jwks.<strong>kty</strong></td><td width="70">0..1</td><td width="150">string</td><td>Key type. 

<strong>Allowed values</strong>: `RSA`</td></tr>
<tr><td width="290">jwks.<strong>alg</strong></td><td width="70">0..1</td><td width="150">string</td><td>Algorithm used with this key. 

<strong>Allowed values</strong>: `RS384`</td></tr>
<tr><td width="290">jwks.<strong>e</strong></td><td width="70">0..1</td><td width="150">string</td><td>Exponent value for RSA key.</td></tr>
<tr><td width="290">jwks.<strong>n</strong></td><td width="70">0..1</td><td width="150">string</td><td>Modulus value for RSA key.</td></tr>
<tr><td width="290">jwks.<strong>use</strong></td><td width="70">0..1</td><td width="150">string</td><td>Key usage. 

<strong>Allowed values</strong>: `sig`</td></tr>
<tr><td width="290">jwks_uri</td><td width="70">0..1</td><td width="150">url</td><td>URI where the client's JSON Web Key Set can be retrieved.</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable name of the client application.</td></tr>
<tr><td width="290">scope</td><td width="70">0..*</td><td width="150">string</td><td>List of scopes this client is authorized to request.</td></tr>
<tr><td width="290">scopes</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Detailed scope configurations with associated policies.</td></tr>
<tr><td width="290">scopes.<strong>policy</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to an AccessPolicy resource for this scope. 

<strong>Allowed references</strong>: AccessPolicy</td></tr>
<tr><td width="290">scopes.<strong>parameters</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Parameters to be applied with the scope's policy.</td></tr>
<tr><td width="290">secret</td><td width="70">0..1</td><td width="150">sha256Hash</td><td>Hashed client secret for authentication.</td></tr>
<tr><td width="290">smart</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>SMART on FHIR configuration for this client.</td></tr>
<tr><td width="290">smart.<strong>launch_uri</strong></td><td width="70">0..1</td><td width="150">string</td><td>URI to launch the SMART app.</td></tr>
<tr><td width="290">smart.<strong>name</strong></td><td width="70">0..1</td><td width="150">string</td><td>Name of the SMART app.</td></tr>
<tr><td width="290">smart.<strong>description</strong></td><td width="70">0..1</td><td width="150">string</td><td>Description of the SMART app.</td></tr>
<tr><td width="290">trusted</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether this client is trusted and given special privileges.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>The type of client application.</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">client</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the client application being granted access. 

<strong>Allowed references</strong>: Client</td></tr>
<tr><td width="290">patient</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the patient this grant is for (in SMART on FHIR scenarios). 

<strong>Allowed references</strong>: Patient</td></tr>
<tr><td width="290">provided-scope</td><td width="70">0..*</td><td width="150">string</td><td>List of scopes that were actually granted by the user.</td></tr>
<tr><td width="290">requested-scope</td><td width="70">0..*</td><td width="150">string</td><td>List of scopes that were requested by the client.</td></tr>
<tr><td width="290">scope</td><td width="70">0..1</td><td width="150">string</td><td>Space-separated list of granted scopes.</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when this grant was created.</td></tr>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the user who granted the access. 

<strong>Allowed references</strong>: User</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether this identity provider is active and can be used for authentication.</td></tr>
<tr><td width="290">authorize_endpoint</td><td width="70">0..1</td><td width="150">string</td><td>The URL of the authorization endpoint.</td></tr>
<tr><td width="290">base_url</td><td width="70">0..1</td><td width="150">uri</td><td></td></tr>
<tr><td width="290">client</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Client configuration for this identity provider.</td></tr>
<tr><td width="290">client.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Client identifier used for authentication with the identity provider.</td></tr>
<tr><td width="290">client.<strong>redirect_uri</strong></td><td width="70">0..1</td><td width="150">uri</td><td>URI where the provider will redirect after authentication.</td></tr>
<tr><td width="290">client.<strong>auth-method</strong></td><td width="70">0..1</td><td width="150">string</td><td>Client authentication method. 

<strong>Allowed values</strong>: `symmetric` | `asymmetric`</td></tr>
<tr><td width="290">client.<strong>secret</strong></td><td width="70">0..1</td><td width="150">string</td><td>Client secret for symmetric authentication.</td></tr>
<tr><td width="290">client.<strong>private-key</strong></td><td width="70">0..1</td><td width="150">string</td><td>Private key for asymmetric authentication.</td></tr>
<tr><td width="290">client.<strong>certificate</strong></td><td width="70">0..1</td><td width="150">string</td><td>Certificate</td></tr>
<tr><td width="290">client.<strong>certificate-thumbprint</strong></td><td width="70">0..1</td><td width="150">string</td><td>Certificate thumbprint.</td></tr>
<tr><td width="290">client.<strong>creds-ts</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">introspection_endpoint</td><td width="70">0..1</td><td width="150">string</td><td>The URL of the token introspection endpoint.</td></tr>
<tr><td width="290">isEmailUniqueness</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether email uniqueness should be enforced for this provider.</td></tr>
<tr><td width="290">isScim</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether this provider supports SCIM protocol.</td></tr>
<tr><td width="290">jwks_uri</td><td width="70">0..1</td><td width="150">string</td><td>URI where the provider's JSON Web Key Set can be retrieved.</td></tr>
<tr><td width="290">kid</td><td width="70">0..1</td><td width="150">string</td><td>Key identifier used for token verification.</td></tr>
<tr><td width="290">organizations</td><td width="70">0..*</td><td width="150">string</td><td>Organizations associated with this identity provider.</td></tr>
<tr><td width="290">registration_endpoint</td><td width="70">0..1</td><td width="150">string</td><td>The URL of the registration endpoint.</td></tr>
<tr><td width="290">revocation_endpoint</td><td width="70">0..1</td><td width="150">string</td><td>The URL of the token revocation endpoint.</td></tr>
<tr><td width="290">scopes</td><td width="70">0..*</td><td width="150">string</td><td>OAuth scopes that should be requested during authentication.</td></tr>
<tr><td width="290">system</td><td width="70">0..1</td><td width="150">string</td><td>System identifier for the identity provider.</td></tr>
<tr><td width="290">team_id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name for the identity provider.</td></tr>
<tr><td width="290">toScim</td><td width="70">0..1</td><td width="150">Object</td><td>Mapping rules for transforming identity provider data.</td></tr>
<tr><td width="290">token_endpoint</td><td width="70">0..1</td><td width="150">string</td><td>The URL of the token endpoint.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>The type of identity provider. 

<strong>Allowed values</strong>: `aidbox` | `github` | `google` | `OIDC` | `OAuth` | `az-dev` | `yandex` | `okta` | `apple`</td></tr>
<tr><td width="290">userinfo-source</td><td width="70">0..1</td><td width="150">string</td><td>Source of userinfo details. 

<strong>Allowed values</strong>: `id-token` | `userinfo-endpoint`</td></tr>
<tr><td width="290">userinfo_endpoint</td><td width="70">0..1</td><td width="150">string</td><td>The URL of the userinfo endpoint.</td></tr>
<tr><td width="290">userinfo_header</td><td width="70">0..1</td><td width="150">string</td><td>Header to be used when calling the userinfo endpoint.</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">provider</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">providerData</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Status of the notification delivery (delivered or error). 

<strong>Allowed values</strong>: `delivered` | `error`</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">subject</td><td width="70">0..1</td><td width="150">string</td><td>Subject line for the notification template.</td></tr>
<tr><td width="290">template</td><td width="70">0..1</td><td width="150">string</td><td>Template content used to generate the notification message.</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">Object</td><td>Registration form data</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Status of the registration process. 

<strong>Allowed values</strong>: `activated` | `active`</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">context</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Text description of the role</td></tr>
<tr><td width="290">links</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>You may list resources here, which can later be granted access for the user with this role via an AccessPolicy resource.</td></tr>
<tr><td width="290">links.<strong>patient</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Patient resource 

<strong>Allowed references</strong>: Patient</td></tr>
<tr><td width="290">links.<strong>practitionerRole</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to PractitionerRole resource 

<strong>Allowed references</strong>: PractitionerRole</td></tr>
<tr><td width="290">links.<strong>practitioner</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Practitioner resource 

<strong>Allowed references</strong>: Practitioner</td></tr>
<tr><td width="290">links.<strong>organization</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Organization resource 

<strong>Allowed references</strong>: Organization</td></tr>
<tr><td width="290">links.<strong>person</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to Person resource 

<strong>Allowed references</strong>: Person</td></tr>
<tr><td width="290">links.<strong>relatedPerson</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to RelatedPerson resource 

<strong>Allowed references</strong>: RelatedPerson</td></tr>
<tr><td width="290">name</td><td width="70">1..1</td><td width="150">string</td><td>Role name is a string that defines role. To assign the same role to multiple users, create multiple Role resources with the same "name". [Search param: name => type string]</td></tr>
<tr><td width="290">user</td><td width="70">1..1</td><td width="150">Reference</td><td>Reference to a User resource for which the role will be applied. [Search param: user => type reference] 

<strong>Allowed references</strong>: User</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>When provided, the scope definition is additionally displayed on the consent screen</td></tr>
<tr><td width="290">scope</td><td width="70">1..1</td><td width="150">string</td><td>The value of the scope</td></tr>
<tr><td width="290">title</td><td width="70">1..1</td><td width="150">string</td><td>A user-friendly name for the scope that appears on the consent screen</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">access_token</td><td width="70">0..1</td><td width="150">sha256Hash</td><td>Access token hash associated with this session.</td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether this session is currently active.</td></tr>
<tr><td width="290">audience</td><td width="70">0..1</td><td width="150">string</td><td>Intended audience for tokens issued in this session.</td></tr>
<tr><td width="290">authorization_code</td><td width="70">0..1</td><td width="150">sha256Hash</td><td>Authorization code used to obtain this session.</td></tr>
<tr><td width="290">client</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the client application associated with this session. 

<strong>Allowed references</strong>: Client</td></tr>
<tr><td width="290">ctx</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the session ended or will end.</td></tr>
<tr><td width="290">exp</td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for the access token (in seconds since epoch).</td></tr>
<tr><td width="290">jti</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">on-behalf</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to a user on whose behalf this session is operating. 

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">parent</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to a parent session if this is a child session. 

<strong>Allowed references</strong>: Session</td></tr>
<tr><td width="290">patient</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the patient associated with this session. 

<strong>Allowed references</strong>: Patient</td></tr>
<tr><td width="290">refresh_token</td><td width="70">0..1</td><td width="150">sha256Hash</td><td>Refresh token hash associated with this session.</td></tr>
<tr><td width="290">refresh_token_exp</td><td width="70">0..1</td><td width="150">integer</td><td>Expiration time for the refresh token (in seconds since epoch).</td></tr>
<tr><td width="290">scope</td><td width="70">0..*</td><td width="150">string</td><td>List of OAuth scopes authorized for this session.</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the session started.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of session (e.g., authorization_code, password, client_credentials).</td></tr>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the user associated with this session. 

<strong>Allowed references</strong>: User</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">identity_provider</td><td width="70">0..1</td><td width="150">Reference</td><td>Link to Identity provider associated with the token introspector. 

<strong>Allowed references</strong>: IdentityProvider</td></tr>
<tr><td width="290">introspection_endpoint</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">introspection_endpoint.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>The fully qualified URL of the remote introspection endpoint.</td></tr>
<tr><td width="290">introspection_endpoint.<strong>authorization</strong></td><td width="70">0..1</td><td width="150">string</td><td>The authorization header value (e.g. a Basic Auth or Bearer token) used when calling the introspection endpoint. If present it will be included in the request headers.</td></tr>
<tr><td width="290">jwks_uri</td><td width="70">0..1</td><td width="150">string</td><td>A URL pointing to a JSON Web Key Set (JWKS). When type is jwt the introspector retrieves public keys from this URI to validate token signatures.</td></tr>
<tr><td width="290">jwt</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Configuration for local JWT validation used when type is jwt.</td></tr>
<tr><td width="290">jwt.<strong>iss</strong></td><td width="70">0..1</td><td width="150">string</td><td>The expected issuer (iss) claim value for JWTs. The TokenIntrospector ensures that tokens it validates come from this issuer.</td></tr>
<tr><td width="290">jwt.<strong>secret</strong></td><td width="70">0..1</td><td width="150">string</td><td>A shared secret key or other signing key material used to verify the JWT's signature.</td></tr>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td>Specifies the type of token to introspect. 

<strong>Allowed values</strong>: `opaque` | `jwt` | `aspxauth`</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">active</td><td width="70">0..1</td><td width="150">boolean</td><td>NB: this attr is ignored. Indicates the User's administrative status.</td></tr>
<tr><td width="290">addresses</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A physical mailing address for this User (e.g. 'work', 'home').</td></tr>
<tr><td width="290">addresses.<strong>formatted</strong></td><td width="70">0..1</td><td width="150">string</td><td>Full address, formatted for display or mailing label.</td></tr>
<tr><td width="290">addresses.<strong>streetAddress</strong></td><td width="70">0..1</td><td width="150">string</td><td>Street address component (may contain newlines).</td></tr>
<tr><td width="290">addresses.<strong>locality</strong></td><td width="70">0..1</td><td width="150">string</td><td>City or locality component.</td></tr>
<tr><td width="290">addresses.<strong>region</strong></td><td width="70">0..1</td><td width="150">string</td><td>State or region component.</td></tr>
<tr><td width="290">addresses.<strong>postalCode</strong></td><td width="70">0..1</td><td width="150">string</td><td>Zip code or postal code.</td></tr>
<tr><td width="290">addresses.<strong>country</strong></td><td width="70">0..1</td><td width="150">string</td><td>Country name component.</td></tr>
<tr><td width="290">addresses.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the address type, e.g. 'work' or 'home'.</td></tr>
<tr><td width="290">costCenter</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the name of a cost center.</td></tr>
<tr><td width="290">data</td><td width="70">0..1</td><td width="150">Object</td><td>Arbitrary user-related data.</td></tr>
<tr><td width="290">department</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the name of a department.</td></tr>
<tr><td width="290">displayName</td><td width="70">0..1</td><td width="150">string</td><td>The name of the User, suitable for display to end-users.</td></tr>
<tr><td width="290">division</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the name of a division.</td></tr>
<tr><td width="290">email</td><td width="70">0..1</td><td width="150">email</td><td>Primary email for the user.</td></tr>
<tr><td width="290">emails</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Email addresses for the user. Values should be canonicalized (e.g. 'bjensen@example.com').</td></tr>
<tr><td width="290">emails.<strong>value</strong></td><td width="70">0..1</td><td width="150">string</td><td>An individual email address (canonicalized).</td></tr>
<tr><td width="290">emails.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">emails.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the attribute's function, e.g. 'work', 'home'.</td></tr>
<tr><td width="290">emails.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary email. Only one primary may be 'true'.</td></tr>
<tr><td width="290">employeeNumber</td><td width="70">0..1</td><td width="150">string</td><td>Numeric or alphanumeric identifier assigned to a person by the organization.</td></tr>
<tr><td width="290">entitlements</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A list of entitlements for the User that represent a thing the User has.</td></tr>
<tr><td width="290">entitlements.<strong>value</strong></td><td width="70">0..1</td><td width="150">string</td><td>The value of an entitlement.</td></tr>
<tr><td width="290">entitlements.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">entitlements.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the attribute's function.</td></tr>
<tr><td width="290">entitlements.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary entitlement. Only one may be 'true'.</td></tr>
<tr><td width="290">fhirUser</td><td width="70">0..1</td><td width="150">Reference</td><td>A reference to a related FHIR resource 

<strong>Allowed references</strong>: Patient, Practitioner, Person</td></tr>
<tr><td width="290">gender</td><td width="70">0..1</td><td width="150">string</td><td>The user's gender.</td></tr>
<tr><td width="290">identifier</td><td width="70">0..*</td><td width="150">Identifier</td><td>A list of identifiers for the user.</td></tr>
<tr><td width="290">ims</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Instant messaging addresses for the User.</td></tr>
<tr><td width="290">ims.<strong>value</strong></td><td width="70">0..1</td><td width="150">string</td><td>Instant messaging address.</td></tr>
<tr><td width="290">ims.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily for display (READ-ONLY).</td></tr>
<tr><td width="290">ims.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the IM type, e.g. 'aim', 'gtalk'.</td></tr>
<tr><td width="290">ims.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary IM. Only one may be 'true'.</td></tr>
<tr><td width="290">inactive</td><td width="70">0..1</td><td width="150">boolean</td><td>A Boolean value indicating the User's administrative status.</td></tr>
<tr><td width="290">link</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A collection of references or links associated with the user.</td></tr>
<tr><td width="290">link.<strong>link</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>A referenced resource link.</td></tr>
<tr><td width="290">link.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the link's function.</td></tr>
<tr><td width="290">locale</td><td width="70">0..1</td><td width="150">string</td><td>Indicates the User's default location for localization (e.g., currency, date format).</td></tr>
<tr><td width="290">manager</td><td width="70">0..1</td><td width="150">Reference</td><td>Another User resource who is this User's manager. 

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>The components of the user's real name (formatted, family, given, etc.).</td></tr>
<tr><td width="290">name.<strong>formatted</strong></td><td width="70">0..1</td><td width="150">string</td><td>Full name, including titles and suffixes, formatted for display.</td></tr>
<tr><td width="290">name.<strong>familyName</strong></td><td width="70">0..1</td><td width="150">string</td><td>Family name (last name in Western languages).</td></tr>
<tr><td width="290">name.<strong>givenName</strong></td><td width="70">0..1</td><td width="150">string</td><td>Given name (first name in Western languages).</td></tr>
<tr><td width="290">name.<strong>middleName</strong></td><td width="70">0..1</td><td width="150">string</td><td>The middle name(s) of the User.</td></tr>
<tr><td width="290">name.<strong>honorificPrefix</strong></td><td width="70">0..1</td><td width="150">string</td><td>Honorific prefix (title), e.g. 'Ms.'.</td></tr>
<tr><td width="290">name.<strong>honorificSuffix</strong></td><td width="70">0..1</td><td width="150">string</td><td>Honorific suffix, e.g. 'III'.</td></tr>
<tr><td width="290">organization</td><td width="70">0..1</td><td width="150">Reference</td><td>Identifies the name of an organization. 

<strong>Allowed references</strong>: Organization</td></tr>
<tr><td width="290">password</td><td width="70">0..1</td><td width="150">password</td><td>The User's cleartext password, used for initial or reset scenarios.</td></tr>
<tr><td width="290">phoneNumber</td><td width="70">0..1</td><td width="150">string</td><td>Primary phone number.</td></tr>
<tr><td width="290">phoneNumbers</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Phone numbers for the User, e.g. 'tel:+1-201-555-0123'.</td></tr>
<tr><td width="290">phoneNumbers.<strong>value</strong></td><td width="70">0..1</td><td width="150">string</td><td>The user's phone number.</td></tr>
<tr><td width="290">phoneNumbers.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">phoneNumbers.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label for the phone number's function, e.g. 'home', 'work'.</td></tr>
<tr><td width="290">phoneNumbers.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary phone number. Only one may be 'true'.</td></tr>
<tr><td width="290">photo</td><td width="70">0..1</td><td width="150">uri</td><td>Primary photo for the user.</td></tr>
<tr><td width="290">photos</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>URLs of photos of the user.</td></tr>
<tr><td width="290">photos.<strong>value</strong></td><td width="70">0..1</td><td width="150">uri</td><td>URL of a photo of the User.</td></tr>
<tr><td width="290">photos.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">photos.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating 'photo' or 'thumbnail'.</td></tr>
<tr><td width="290">photos.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary photo. Only one may be 'true'.</td></tr>
<tr><td width="290">preferredLanguage</td><td width="70">0..1</td><td width="150">string</td><td>The User's preferred written or spoken language, e.g. 'en_US'.</td></tr>
<tr><td width="290">profileUrl</td><td width="70">0..1</td><td width="150">uri</td><td>A fully qualified URL pointing to a page representing the User's online profile.</td></tr>
<tr><td width="290">roles</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A list of roles for the User that collectively represent who the User is (e.g. 'Student', 'Faculty').</td></tr>
<tr><td width="290">roles.<strong>value</strong></td><td width="70">0..1</td><td width="150">string</td><td>The value of a role.</td></tr>
<tr><td width="290">roles.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">roles.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the attribute's function.</td></tr>
<tr><td width="290">roles.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary role. Only one may be 'true'.</td></tr>
<tr><td width="290">securityLabel</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>List of security labes associated to the user</td></tr>
<tr><td width="290">securityLabel.<strong>system</strong></td><td width="70">0..1</td><td width="150">string</td><td>Code system</td></tr>
<tr><td width="290">securityLabel.<strong>code</strong></td><td width="70">0..1</td><td width="150">string</td><td>Code value</td></tr>
<tr><td width="290">timezone</td><td width="70">0..1</td><td width="150">string</td><td>The User's time zone in the 'Olson' format, e.g. 'America/Los_Angeles'.</td></tr>
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td>The user's title, e.g. 'Vice President'.</td></tr>
<tr><td width="290">twoFactor</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Two factor settings for user</td></tr>
<tr><td width="290">twoFactor.<strong>enabled</strong></td><td width="70">1..1</td><td width="150">boolean</td><td>Defines whether two-factor auth is currently enabled.</td></tr>
<tr><td width="290">twoFactor.<strong>transport</strong></td><td width="70">0..1</td><td width="150">string</td><td>Transport of 2FA confirmation code (if used).</td></tr>
<tr><td width="290">twoFactor.<strong>secretKey</strong></td><td width="70">1..1</td><td width="150">string</td><td>TOTP Secret key.</td></tr>
<tr><td width="290">userName</td><td width="70">0..1</td><td width="150">string</td><td>Unique identifier for the User, typically used to directly authenticate. Must be unique across the service provider's Users.</td></tr>
<tr><td width="290">userType</td><td width="70">0..1</td><td width="150">string</td><td>Identifies the relationship between the organization and the user (e.g. 'Employee', 'Contractor').</td></tr>
<tr><td width="290">x509Certificates</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A list of certificates issued to the User.</td></tr>
<tr><td width="290">x509Certificates.<strong>value</strong></td><td width="70">0..1</td><td width="150">base64Binary</td><td>The value of an X.509 certificate (base64).</td></tr>
<tr><td width="290">x509Certificates.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable name, primarily used for display purposes (READ-ONLY).</td></tr>
<tr><td width="290">x509Certificates.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>A label indicating the certificate's function.</td></tr>
<tr><td width="290">x509Certificates.<strong>primary</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates if this is the primary certificate. Only one may be 'true'.</td></tr></tbody>
</table>

