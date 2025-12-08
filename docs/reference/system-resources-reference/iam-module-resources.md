---
description: Aidbox IAM module resources for managing user authentication, authorization, and access control.
---

# IAM Module Resources

The Identity and Access Management (IAM) module provides a set of resources for managing user authentication, authorization, and access control within the Aidbox.

 ## AccessPolicy

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "and",
  "name" : "and",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Object",
  "desc" : "A list of conditions that must all be satisfied for the policy to grant access."
}, {
  "path" : "clj",
  "name" : "clj",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Clojure code that defines access policy rules. \n DEPRECATED. DO NOT USE IT."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A textual description of the access policy."
}, {
  "path" : "engine",
  "name" : "engine",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Specifies the evaluation engine used for the policy. \n\n**Allowed values**: `json-schema` | `allow` | `sql` | `complex` | `matcho` | `clj` | `matcho-rpc` | `allow-rpc` | `signed-rpc` | `smart-on-fhir`"
}, {
  "path" : "link",
  "name" : "link",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References to resources associated with this policy. \n\n**Allowed references**: Client, User, Operation"
}, {
  "path" : "matcho",
  "name" : "matcho",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Defines rules using the Matcho pattern-matching syntax."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this policy belongs to."
}, {
  "path" : "or",
  "name" : "or",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Object",
  "desc" : "A list of conditions where at least one must be satisfied for the policy to grant access."
}, {
  "path" : "roleName",
  "name" : "roleName",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Symbolic link to Role by name"
}, {
  "path" : "rpc",
  "name" : "rpc",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Defines rules for Remote Procedure Calls (RPCs)."
}, {
  "path" : "schema",
  "name" : "schema",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "JSON Schema used to validate requests against the policy."
}, {
  "path" : "sql",
  "name" : "sql",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "SQL-based policy definition."
}, {
  "path" : "sql.query",
  "name" : "query",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL query used to evaluate access conditions."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The type or category of the access policy. \n\n**Allowed values**: `scope` | `rest` | `rpc`"
} ]
```


## AuthConfig

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "asidCookieMaxAge",
  "name" : "asidCookieMaxAge",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "In Aidbox version v:2402 and later, sessions created through the Aidbox UI log-in are not infinite.The default session expiration time is set to 432000 seconds (5 days)."
}, {
  "path" : "theme",
  "name" : "theme",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "theme.brand",
  "name" : "brand",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Brand for auth page"
}, {
  "path" : "theme.title",
  "name" : "title",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title for auth page"
}, {
  "path" : "theme.styleUrl",
  "name" : "styleUrl",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL to external stylesheet"
}, {
  "path" : "theme.forgotPasswordUrl",
  "name" : "forgotPasswordUrl",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL to forgot password page"
}, {
  "path" : "twoFactor",
  "name" : "twoFactor",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "twoFactor.webhook",
  "name" : "webhook",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "twoFactor.webhook.headers",
  "name" : "headers",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Map",
  "desc" : "Map of HTTP header key-value pairs"
}, {
  "path" : "twoFactor.webhook.timeout",
  "name" : "timeout",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Timeout in milliseconds"
}, {
  "path" : "twoFactor.webhook.endpoint",
  "name" : "endpoint",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "URL to webhook that supports POST method"
}, {
  "path" : "twoFactor.issuerName",
  "name" : "issuerName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Issuer name for OTP authenticator app"
}, {
  "path" : "twoFactor.validPastTokensCount",
  "name" : "validPastTokensCount",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of past tokens considered valid (useful with webhook since OTP lives ~30s)"
} ]
```


## Client

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this client is active and can be used for authentication."
}, {
  "path" : "allowed-scopes",
  "name" : "allowed-scopes",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References to specific Scope resources this client is allowed to request. \n\n**Allowed references**: Scope"
}, {
  "path" : "allowedIssuers",
  "name" : "allowedIssuers",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of authorized token issuers for this client."
}, {
  "path" : "allowed_origins",
  "name" : "allowed_origins",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "uri",
  "desc" : "Allowed Origins are URLs that will be allowed to make requests."
}, {
  "path" : "auth",
  "name" : "auth",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Authentication configuration for different OAuth flows."
}, {
  "path" : "auth.client_credentials",
  "name" : "client_credentials",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the client credentials grant type."
}, {
  "path" : "auth.client_credentials.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.client_credentials.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.client_credentials.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.client_credentials.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens. Shows what resource server access is intended for. Aidbox compares the audience of the Client to the audience it receives within aJWT and decides if the access should be granted. The audience attribute can be defined in 2 ways: As a plain string, e.g. https://cmpl.aidbox.app/smart As a Regex. In that case, the audience value should start with the # symbol. For example, #https://cmpl.aidbox.app/tenant/[^\\]/smart That validation of the audience happens when SMART on FHIR app launches"
}, {
  "path" : "auth.client_credentials.client_assertion_types",
  "name" : "client_assertion_types",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Supported client assertion types. \n\n**Allowed values**: `urn:ietf:params:oauth:client-assertion-type:jwt-bearer`"
}, {
  "path" : "auth.client_credentials.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.implicit",
  "name" : "implicit",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the implicit grant type."
}, {
  "path" : "auth.implicit.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI for the implicit flow."
}, {
  "path" : "auth.implicit.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.implicit.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.implicit.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.password",
  "name" : "password",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the password grant type."
}, {
  "path" : "auth.password.secret_required",
  "name" : "secret_required",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether client secret is required for password grant."
}, {
  "path" : "auth.password.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.password.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.password.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "If present, turn on redirect protection"
}, {
  "path" : "auth.password.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.password.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.password.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.authorization_code",
  "name" : "authorization_code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the authorization code grant type."
}, {
  "path" : "auth.authorization_code.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.authorization_code.client_assertion_types",
  "name" : "client_assertion_types",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Supported client assertion types. \n\n**Allowed values**: `urn:ietf:params:oauth:client-assertion-type:jwt-bearer`"
}, {
  "path" : "auth.authorization_code.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.authorization_code.pkce",
  "name" : "pkce",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether PKCE (Proof Key for Code Exchange) is required."
}, {
  "path" : "auth.authorization_code.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.authorization_code.secret_required",
  "name" : "secret_required",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether client secret is required for token exchange."
}, {
  "path" : "auth.authorization_code.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.authorization_code.default_identity_provider",
  "name" : "default_identity_provider",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Default IdentityProvider that will be used instead of Aidbox login. \n\n**Allowed references**: IdentityProvider"
}, {
  "path" : "auth.authorization_code.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.authorization_code.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI for the authorization code flow."
}, {
  "path" : "auth.token_exchange",
  "name" : "token_exchange",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the token exchange grant type."
}, {
  "path" : "auth.token_exchange.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.token_exchange.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.token_exchange.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.token_exchange.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.token_exchange.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A description of the client application for administrative purposes."
}, {
  "path" : "details",
  "name" : "details",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Additional client details or configuration options."
}, {
  "path" : "fhir-base-url",
  "name" : "fhir-base-url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Base URL of the FHIR server this client interacts with."
}, {
  "path" : "first_party",
  "name" : "first_party",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this is a first-party client."
}, {
  "path" : "grant_types",
  "name" : "grant_types",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "OAuth 2.0 grant types this client is authorized to use. \n\n**Allowed values**: `basic` | `authorization_code` | `code` | `password` | `client_credentials` | `implicit` | `refresh_token` | `urn:ietf:params:oauth:grant-type:token-exchange`"
}, {
  "path" : "jwks",
  "name" : "jwks",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "JSON Web Key Set for client authentication and/or verification."
}, {
  "path" : "jwks.kid",
  "name" : "kid",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key ID that identifies this key."
}, {
  "path" : "jwks.kty",
  "name" : "kty",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key type. \n\n**Allowed values**: `RSA`"
}, {
  "path" : "jwks.alg",
  "name" : "alg",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Algorithm used with this key. \n\n**Allowed values**: `RS384`"
}, {
  "path" : "jwks.e",
  "name" : "e",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Exponent value for RSA key."
}, {
  "path" : "jwks.n",
  "name" : "n",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Modulus value for RSA key."
}, {
  "path" : "jwks.use",
  "name" : "use",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key usage. \n\n**Allowed values**: `sig`"
}, {
  "path" : "jwks_uri",
  "name" : "jwks_uri",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "URI where the client's JSON Web Key Set can be retrieved."
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable name of the client application."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of scopes this client is authorized to request."
}, {
  "path" : "scopes",
  "name" : "scopes",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Detailed scope configurations with associated policies."
}, {
  "path" : "scopes.policy",
  "name" : "policy",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to an AccessPolicy resource for this scope. \n\n**Allowed references**: AccessPolicy"
}, {
  "path" : "scopes.parameters",
  "name" : "parameters",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters to be applied with the scope's policy."
}, {
  "path" : "secret",
  "name" : "secret",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "sha256Hash",
  "desc" : "Hashed client secret for authentication."
}, {
  "path" : "smart",
  "name" : "smart",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "SMART on FHIR configuration for this client."
}, {
  "path" : "smart.launch_uri",
  "name" : "launch_uri",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URI to launch the SMART app."
}, {
  "path" : "smart.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the SMART app."
}, {
  "path" : "smart.description",
  "name" : "description",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Description of the SMART app."
}, {
  "path" : "trusted",
  "name" : "trusted",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this client is trusted and given special privileges."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The type of client application."
} ]
```


## Grant

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "client",
  "name" : "client",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the client application being granted access. \n\n**Allowed references**: Client"
}, {
  "path" : "patient",
  "name" : "patient",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the patient this grant is for (in SMART on FHIR scenarios). \n\n**Allowed references**: Patient"
}, {
  "path" : "provided-scope",
  "name" : "provided-scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of scopes that were actually granted by the user."
}, {
  "path" : "requested-scope",
  "name" : "requested-scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of scopes that were requested by the client."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Space-separated list of granted scopes."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when this grant was created."
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user who granted the access. \n\n**Allowed references**: User"
} ]
```


## IdentityProvider

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this identity provider is active and can be used for authentication."
}, {
  "path" : "authorize_endpoint",
  "name" : "authorize_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the authorization endpoint."
}, {
  "path" : "base_url",
  "name" : "base_url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : ""
}, {
  "path" : "client",
  "name" : "client",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Client configuration for this identity provider."
}, {
  "path" : "client.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client identifier used for authentication with the identity provider."
}, {
  "path" : "client.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URI where the provider will redirect after authentication."
}, {
  "path" : "client.auth-method",
  "name" : "auth-method",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client authentication method. \n\n**Allowed values**: `symmetric` | `asymmetric`"
}, {
  "path" : "client.secret",
  "name" : "secret",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client secret for symmetric authentication."
}, {
  "path" : "client.private-key",
  "name" : "private-key",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Private key for asymmetric authentication."
}, {
  "path" : "client.certificate",
  "name" : "certificate",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Certificate"
}, {
  "path" : "client.certificate-thumbprint",
  "name" : "certificate-thumbprint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Certificate thumbprint."
}, {
  "path" : "client.creds-ts",
  "name" : "creds-ts",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "introspection_endpoint",
  "name" : "introspection_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the token introspection endpoint."
}, {
  "path" : "isEmailUniqueness",
  "name" : "isEmailUniqueness",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether email uniqueness should be enforced for this provider."
}, {
  "path" : "isScim",
  "name" : "isScim",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this provider supports SCIM protocol."
}, {
  "path" : "jwks_uri",
  "name" : "jwks_uri",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URI where the provider's JSON Web Key Set can be retrieved."
}, {
  "path" : "kid",
  "name" : "kid",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key identifier used for token verification."
}, {
  "path" : "organizations",
  "name" : "organizations",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Organizations associated with this identity provider."
}, {
  "path" : "registration_endpoint",
  "name" : "registration_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the registration endpoint."
}, {
  "path" : "revocation_endpoint",
  "name" : "revocation_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the token revocation endpoint."
}, {
  "path" : "scopes",
  "name" : "scopes",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "OAuth scopes that should be requested during authentication."
}, {
  "path" : "system",
  "name" : "system",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System identifier for the identity provider."
}, {
  "path" : "team_id",
  "name" : "team_id",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name for the identity provider."
}, {
  "path" : "toScim",
  "name" : "toScim",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Mapping rules for transforming identity provider data."
}, {
  "path" : "token_endpoint",
  "name" : "token_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the token endpoint."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The type of identity provider. \n\n**Allowed values**: `aidbox` | `github` | `google` | `OIDC` | `OAuth` | `az-dev` | `yandex` | `okta` | `apple`"
}, {
  "path" : "userinfo-source",
  "name" : "userinfo-source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source of userinfo details. \n\n**Allowed values**: `id-token` | `userinfo-endpoint`"
}, {
  "path" : "userinfo_endpoint",
  "name" : "userinfo_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the userinfo endpoint."
}, {
  "path" : "userinfo_header",
  "name" : "userinfo_header",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Header to be used when calling the userinfo endpoint."
} ]
```


## Notification

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "provider",
  "name" : "provider",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "providerData",
  "name" : "providerData",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the notification delivery (delivered or error). \n\n**Allowed values**: `delivered` | `error`"
} ]
```


## NotificationTemplate

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "subject",
  "name" : "subject",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Subject line for the notification template."
}, {
  "path" : "template",
  "name" : "template",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Template content used to generate the notification message."
} ]
```


## Registration

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Registration form data"
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the registration process. \n\n**Allowed values**: `activated` | `active`"
} ]
```


## Role

User role

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "context",
  "name" : "context",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text description of the role"
}, {
  "path" : "links",
  "name" : "links",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "You may list resources here, which can later be granted access for the user with this role via an AccessPolicy resource."
}, {
  "path" : "links.patient",
  "name" : "patient",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Patient resource \n\n**Allowed references**: Patient"
}, {
  "path" : "links.practitionerRole",
  "name" : "practitionerRole",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to PractitionerRole resource \n\n**Allowed references**: PractitionerRole"
}, {
  "path" : "links.practitioner",
  "name" : "practitioner",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Practitioner resource \n\n**Allowed references**: Practitioner"
}, {
  "path" : "links.organization",
  "name" : "organization",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Organization resource \n\n**Allowed references**: Organization"
}, {
  "path" : "links.person",
  "name" : "person",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Person resource \n\n**Allowed references**: Person"
}, {
  "path" : "links.relatedPerson",
  "name" : "relatedPerson",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to RelatedPerson resource \n\n**Allowed references**: RelatedPerson"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Role name is a string that defines role. To assign the same role to multiple users, create multiple Role resources with the same \"name\". [Search param: name => type string]"
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a User resource for which the role will be applied. [Search param: user => type reference] \n\n**Allowed references**: User"
} ]
```


## Scope

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "When provided, the scope definition is additionally displayed on the consent screen"
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The value of the scope"
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "A user-friendly name for the scope that appears on the consent screen"
} ]
```


## Session

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "access_token",
  "name" : "access_token",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "sha256Hash",
  "desc" : "Access token hash associated with this session."
}, {
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this session is currently active."
}, {
  "path" : "audience",
  "name" : "audience",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Intended audience for tokens issued in this session."
}, {
  "path" : "authorization_code",
  "name" : "authorization_code",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "sha256Hash",
  "desc" : "Authorization code used to obtain this session."
}, {
  "path" : "client",
  "name" : "client",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the client application associated with this session. \n\n**Allowed references**: Client"
}, {
  "path" : "ctx",
  "name" : "ctx",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "end",
  "name" : "end",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the session ended or will end."
}, {
  "path" : "exp",
  "name" : "exp",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for the access token (in seconds since epoch)."
}, {
  "path" : "iss",
  "name" : "iss",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Issuer of token for the current session"
}, {
  "path" : "jti",
  "name" : "jti",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "on-behalf",
  "name" : "on-behalf",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a user on whose behalf this session is operating. \n\n**Allowed references**: User"
}, {
  "path" : "parent",
  "name" : "parent",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a parent session if this is a child session. \n\n**Allowed references**: Session"
}, {
  "path" : "patient",
  "name" : "patient",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the patient associated with this session. \n\n**Allowed references**: Patient"
}, {
  "path" : "refresh_token",
  "name" : "refresh_token",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "sha256Hash",
  "desc" : "Refresh token hash associated with this session."
}, {
  "path" : "refresh_token_exp",
  "name" : "refresh_token_exp",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for the refresh token (in seconds since epoch)."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of OAuth scopes authorized for this session."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the session started."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of session (e.g., authorization_code, password, client_credentials)."
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user associated with this session. \n\n**Allowed references**: User"
} ]
```


## TokenIntrospector

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "identity_provider",
  "name" : "identity_provider",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Link to Identity provider associated with the token introspector. \n\n**Allowed references**: IdentityProvider"
}, {
  "path" : "introspection_endpoint",
  "name" : "introspection_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "introspection_endpoint.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The fully qualified URL of the remote introspection endpoint."
}, {
  "path" : "introspection_endpoint.authorization",
  "name" : "authorization",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The authorization header value (e.g. a Basic Auth or Bearer token) used when calling the introspection endpoint. If present it will be included in the request headers."
}, {
  "path" : "jwks_uri",
  "name" : "jwks_uri",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A URL pointing to a JSON Web Key Set (JWKS). When type is jwt the introspector retrieves public keys from this URI to validate token signatures."
}, {
  "path" : "jwt",
  "name" : "jwt",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for local JWT validation used when type is jwt."
}, {
  "path" : "jwt.iss",
  "name" : "iss",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The expected issuer (iss) claim value for JWTs. The TokenIntrospector ensures that tokens it validates come from this issuer."
}, {
  "path" : "jwt.secret",
  "name" : "secret",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A shared secret key or other signing key material used to verify the JWT's signature."
}, {
  "path" : "jwt.keys",
  "name" : "keys",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "The set of keys to use for validation."
}, {
  "path" : "jwt.keys.k",
  "name" : "k",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The symmetric key to use for validation."
}, {
  "path" : "jwt.keys.pub",
  "name" : "pub",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The asymmetric key to use for validation."
}, {
  "path" : "jwt.keys.kty",
  "name" : "kty",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The key type to use for validation. \n\n**Allowed values**: `RSA` | `EC` | `OCT`"
}, {
  "path" : "jwt.keys.alg",
  "name" : "alg",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The algorithm to use for validation. \n\n**Allowed values**: `RS256` | `RS384` | `ES256` | `HS256`"
}, {
  "path" : "jwt.keys.format",
  "name" : "format",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The format of the key to use for validation. 'plain' for symmetric algs (HS256) and 'PEM' for all asymmetric algs \n\n**Allowed values**: `PEM` | `plain`"
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Specifies the type of token to introspect. \n\n**Allowed values**: `opaque` | `jwt` | `aspxauth`"
} ]
```


## User

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "NB: this attr is ignored. Indicates the User's administrative status."
}, {
  "path" : "addresses",
  "name" : "addresses",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A physical mailing address for this User (e.g. 'work', 'home')."
}, {
  "path" : "addresses.formatted",
  "name" : "formatted",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Full address, formatted for display or mailing label."
}, {
  "path" : "addresses.streetAddress",
  "name" : "streetAddress",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Street address component (may contain newlines)."
}, {
  "path" : "addresses.locality",
  "name" : "locality",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "City or locality component."
}, {
  "path" : "addresses.region",
  "name" : "region",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "State or region component."
}, {
  "path" : "addresses.postalCode",
  "name" : "postalCode",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Zip code or postal code."
}, {
  "path" : "addresses.country",
  "name" : "country",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Country name component."
}, {
  "path" : "addresses.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the address type, e.g. 'work' or 'home'."
}, {
  "path" : "costCenter",
  "name" : "costCenter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the name of a cost center."
}, {
  "path" : "data",
  "name" : "data",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Arbitrary user-related data."
}, {
  "path" : "department",
  "name" : "department",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the name of a department."
}, {
  "path" : "displayName",
  "name" : "displayName",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The name of the User, suitable for display to end-users."
}, {
  "path" : "division",
  "name" : "division",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the name of a division."
}, {
  "path" : "email",
  "name" : "email",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Primary email for the user."
}, {
  "path" : "emails",
  "name" : "emails",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Email addresses for the user. Values should be canonicalized (e.g. 'bjensen@example.com')."
}, {
  "path" : "emails.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "An individual email address (canonicalized)."
}, {
  "path" : "emails.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name for display purposes (READ-ONLY)."
}, {
  "path" : "emails.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the attribute's function, e.g. 'work', 'home'."
}, {
  "path" : "emails.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary email. Only one primary may be 'true'."
}, {
  "path" : "employeeNumber",
  "name" : "employeeNumber",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Numeric or alphanumeric identifier assigned to a person by the organization."
}, {
  "path" : "entitlements",
  "name" : "entitlements",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A list of entitlements for the User that represent a thing the User has."
}, {
  "path" : "entitlements.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The value of an entitlement."
}, {
  "path" : "entitlements.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "entitlements.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the attribute's function."
}, {
  "path" : "entitlements.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary entitlement. Only one may be 'true'."
}, {
  "path" : "fhirUser",
  "name" : "fhirUser",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "A reference to a related FHIR resource \n\n**Allowed references**: Patient, Practitioner, PractitionerRole, Person, RelatedPerson"
}, {
  "path" : "gender",
  "name" : "gender",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The user's gender."
}, {
  "path" : "identifier",
  "name" : "identifier",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Identifier",
  "desc" : "A list of identifiers for the user."
}, {
  "path" : "ims",
  "name" : "ims",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Instant messaging addresses for the User."
}, {
  "path" : "ims.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Instant messaging address."
}, {
  "path" : "ims.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily for display (READ-ONLY)."
}, {
  "path" : "ims.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the IM type, e.g. 'aim', 'gtalk'."
}, {
  "path" : "ims.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary IM. Only one may be 'true'."
}, {
  "path" : "inactive",
  "name" : "inactive",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "A Boolean value indicating the User's administrative status."
}, {
  "path" : "link",
  "name" : "link",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A collection of references or links associated with the user."
}, {
  "path" : "link.link",
  "name" : "link",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "A referenced resource link."
}, {
  "path" : "link.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the link's function."
}, {
  "path" : "locale",
  "name" : "locale",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Indicates the User's default location for localization (e.g., currency, date format)."
}, {
  "path" : "manager",
  "name" : "manager",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Another User resource who is this User's manager. \n\n**Allowed references**: User"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "The components of the user's real name (formatted, family, given, etc.)."
}, {
  "path" : "name.formatted",
  "name" : "formatted",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Full name, including titles and suffixes, formatted for display."
}, {
  "path" : "name.familyName",
  "name" : "familyName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Family name (last name in Western languages)."
}, {
  "path" : "name.givenName",
  "name" : "givenName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Given name (first name in Western languages)."
}, {
  "path" : "name.middleName",
  "name" : "middleName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The middle name(s) of the User."
}, {
  "path" : "name.honorificPrefix",
  "name" : "honorificPrefix",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Honorific prefix (title), e.g. 'Ms.'."
}, {
  "path" : "name.honorificSuffix",
  "name" : "honorificSuffix",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Honorific suffix, e.g. 'III'."
}, {
  "path" : "organization",
  "name" : "organization",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Identifies the name of an organization. \n\n**Allowed references**: Organization"
}, {
  "path" : "password",
  "name" : "password",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "password",
  "desc" : "The User's cleartext password, used for initial or reset scenarios."
}, {
  "path" : "phoneNumber",
  "name" : "phoneNumber",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Primary phone number."
}, {
  "path" : "phoneNumbers",
  "name" : "phoneNumbers",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Phone numbers for the User, e.g. 'tel:+1-201-555-0123'."
}, {
  "path" : "phoneNumbers.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The user's phone number."
}, {
  "path" : "phoneNumbers.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name for display purposes (READ-ONLY)."
}, {
  "path" : "phoneNumbers.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label for the phone number's function, e.g. 'home', 'work'."
}, {
  "path" : "phoneNumbers.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary phone number. Only one may be 'true'."
}, {
  "path" : "photo",
  "name" : "photo",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "Primary photo for the user."
}, {
  "path" : "photos",
  "name" : "photos",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "URLs of photos of the user."
}, {
  "path" : "photos.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL of a photo of the User."
}, {
  "path" : "photos.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "photos.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating 'photo' or 'thumbnail'."
}, {
  "path" : "photos.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary photo. Only one may be 'true'."
}, {
  "path" : "preferredLanguage",
  "name" : "preferredLanguage",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The User's preferred written or spoken language, e.g. 'en_US'."
}, {
  "path" : "profileUrl",
  "name" : "profileUrl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "A fully qualified URL pointing to a page representing the User's online profile."
}, {
  "path" : "roles",
  "name" : "roles",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A list of roles for the User that collectively represent who the User is (e.g. 'Student', 'Faculty')."
}, {
  "path" : "roles.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The value of a role."
}, {
  "path" : "roles.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "roles.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the attribute's function."
}, {
  "path" : "roles.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary role. Only one may be 'true'."
}, {
  "path" : "securityLabel",
  "name" : "securityLabel",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "List of security labes associated to the user"
}, {
  "path" : "securityLabel.system",
  "name" : "system",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Code system"
}, {
  "path" : "securityLabel.code",
  "name" : "code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Code value"
}, {
  "path" : "timezone",
  "name" : "timezone",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The User's time zone in the 'Olson' format, e.g. 'America/Los_Angeles'."
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The user's title, e.g. 'Vice President'."
}, {
  "path" : "twoFactor",
  "name" : "twoFactor",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Two factor settings for user"
}, {
  "path" : "twoFactor.enabled",
  "name" : "enabled",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Defines whether two-factor auth is currently enabled."
}, {
  "path" : "twoFactor.transport",
  "name" : "transport",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Transport of 2FA confirmation code (if used)."
}, {
  "path" : "twoFactor.secretKey",
  "name" : "secretKey",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "TOTP Secret key."
}, {
  "path" : "userName",
  "name" : "userName",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the User, typically used to directly authenticate. Must be unique across the service provider's Users."
}, {
  "path" : "userType",
  "name" : "userType",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the relationship between the organization and the user (e.g. 'Employee', 'Contractor')."
}, {
  "path" : "x509Certificates",
  "name" : "x509Certificates",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A list of certificates issued to the User."
}, {
  "path" : "x509Certificates.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "base64Binary",
  "desc" : "The value of an X.509 certificate (base64)."
}, {
  "path" : "x509Certificates.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "x509Certificates.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the certificate's function."
}, {
  "path" : "x509Certificates.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary certificate. Only one may be 'true'."
} ]
```

