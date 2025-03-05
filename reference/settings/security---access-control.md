## Security & Access Control

Security & Access Control settings

### Grant page URL<a href="#security.grant-page-url" id="security.grant-page-url"></a>

URL of consent screen. A consent screen is an interface presented to a user during the authorization code grant flow.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.grant-page-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>/auth/grant</code></td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_GRANT_PAGE_URL</code> , <br /><code>BOX_AUTH_GRANT__PAGE__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Enable FHIR audit log<a href="#security.audit-log.enabled" id="security.audit-log.enabled"></a>

Generates structured audit logs in FHIR AuditEvent format.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_AUDIT_LOG_ENABLED</code> , <br /><code>AIDBOX_SECURITY_AUDIT__LOG_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Enable access control for mapping<a href="#security.iam.mapping.enable-access-control" id="security.iam.mapping.enable-access-control"></a>

Enable access control for `/Mapping/<mapping-id>/$apply` operation.
If enabled, access control will be applied to the resulting transaction.
If disabled, only access to $apply endpoints are verified.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.iam.mapping.enable-access-control</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_IAM_MAPPING_ENABLE_ACCESS_CONTROL</code> , <br /><code>BOX_FEATURES_MAPPING_ENABLE__ACCESS__CONTROL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Encryption API secret<a href="#security.encrypt-secret" id="security.encrypt-secret"></a>

Secret key for encryption API. [Learn more](https://docs.aidbox.app/api-1/other/encryption-api)

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.encrypt-secret</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_ENCRYPT_SECRET</code> , <br /><code>AIDBOX_ENCRYPT_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Allow CORS requests<a href="#security.cors.enabled" id="security.cors.enabled"></a>

Enable Cross-Origin Resource Sharing (CORS) request handling.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.cors.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_CORS_ENABLED</code> , <br /><code>BOX_WEB_CORS_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Allow CORS requests from origins<a href="#security.cors.origins" id="security.cors.origins"></a>

Comma separated list of origins `[schema]://[domain]:[port]`
Default is wildcard value `"*"` 

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.cors.origins</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>*</code></td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_CORS_ORIGINS</code> , <br /><code>BOX_WEB_CORS_ORIGINS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Content security policy header<a href="#security.content-security-policy-header" id="security.content-security-policy-header"></a>

Defines the Content Security Policy (CSP) header to enhance
    security by restricting resource loading. It specifies the policies for
    loading scripts, styles, media, fonts, and other resources.

Refer to the [OWASP Content Security Policy Cheat
                    Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Content_Security_Policy_Cheat_Sheet.html)

Recommended value:
```
default-src 'self'; script-src 'report-sample' 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'report-sample' 'self' 'unsafe-inline'; object-src 'none'; base-uri 'self'; connect-src 'self'; font-src 'self'; frame-src 'self'; frame-ancestors 'self'; img-src 'self'; manifest-src 'self'; media-src 'self'; worker-src 'self';
```

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.content-security-policy-header</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_CONTENT_SECURITY_POLICY_HEADER</code> , <br /><code>AIDBOX_CONTENT_SECURITY_POLICY_HEADER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Skip JWT validation<a href="#security.skip-jwt-validation" id="security.skip-jwt-validation"></a>

Skip JWT token validation process.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.skip-jwt-validation</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_SKIP_JWT_VALIDATION</code> , <br /><code>BOX_FEATURES_AUTHENTICATION_SKIP__JWT__VALIDATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### JWT public key<a href="#security.auth.keys.public" id="security.auth.keys.public"></a>

RS256 signing algorithm expects providing private key for signing JWT and public key for verifying it.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth.keys.public</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_AUTH_KEYS_PUBLIC</code> , <br /><code>BOX_AUTH_KEYS_PUBLIC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### JWT private key<a href="#security.auth.keys.private" id="security.auth.keys.private"></a>

RS256 signing algorithm expects providing private key for signing JWT and public key for verifying it.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth.keys.private</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_AUTH_KEYS_PRIVATE</code> , <br /><code>BOX_AUTH_KEYS_PRIVATE</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### JWT secret<a href="#security.auth.keys.secret" id="security.auth.keys.secret"></a>

HS256 signing algorithm needs only having a secret for both operations.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth.keys.secret</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_AUTH_KEYS_SECRET</code> , <br /><code>BOX_AUTH_KEYS_SECRET</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — can be set only via environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Auto-create users from foreign tokens<a href="#security.introspection-create-user" id="security.introspection-create-user"></a>

Creates local user accounts automatically when valid external
    JWT tokens are presented but no matching user exists.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.introspection-create-user</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_INTROSPECTION_CREATE_USER</code> , <br /><code>BOX_FEATURES_AUTHENTICATION_INTROSPECTION_CREATE__USER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Auth with non-validated JWT<a href="#security.auth-with-not-validated-jwt" id="security.auth-with-not-validated-jwt"></a>

This configuration is used when `skip-jwt-validation` setting is enabled.
It's a string that contains EDN object with `:headers` and `:user-id-paths` keys.
For example: `{:headers #{"authorization" "x-client-token"}, :user-id-paths #{[:authorization :user_id] [:my-client-token :user :id]}}`

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth-with-not-validated-jwt</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_AUTH_WITH_NOT_VALIDATED_JWT</code> , <br /><code>BOX_FEATURES_AUTHENTICATION_AUTH__WITH__NOT__VALIDATED__JWT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Enable LBAC<a href="#security.lbac.enabled" id="security.lbac.enabled"></a>

Label-based Access Control engine provides a mechanism to restrict access to bundles, resources, or resource elements depending on permissions associated with a request.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.lbac.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_LBAC_ENABLED</code> , <br /><code>BOX_FEATURES_SECURITY__LABELS_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Strip security labels<a href="#security.lbac.strip-labels" id="security.lbac.strip-labels"></a>

Removes security labels from resource responses before
    returning them to clients. When enabled, prevents sensitive security
    metadata from being exposed in API responses while maintaining access
    control enforcement internally. Useful for hiding security implementation
    details from end users.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.lbac.strip-labels</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_LBAC_STRIP_LABELS</code> , <br /><code>BOX_FEATURES_SECURITY__LABELS_STRIP__LABELS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Enable organization-based hierarchical access control<a href="#security.orgbac.enabled" id="security.orgbac.enabled"></a>

Activates hierarchical access control based on organizational
    structure. Restricts user access to resources based on their organizational
    affiliation and hierarchy position. 

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.orgbac.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_ORGBAC_ENABLED</code> , <br /><code>BOX_FEATURES_ORGBAC_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Enable SU header<a href="#security.debug-su-enable" id="security.debug-su-enable"></a>

This setting enables `SU` header functionality.
`SU` header allows a user to substitute User ID for the duration of the request.
Only the administrator is allowed to use the `SU` header.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.debug-su-enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_DEBUG_SU_ENABLE</code> , <br /><code>BOX_DEBUG_SU_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Enable Aidbox developer mode<a href="#security.dev-mode" id="security.dev-mode"></a>

Activates debugging features for access policy development,
    including the `_debug=policy URL` parameter. Returns detailed policy
    evaluation traces showing why requests were allowed or denied. For
    development environments only - not recommended for production systems.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.dev-mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_SECURITY_DEV_MODE</code> , <br /><code>AIDBOX_DEV_MODE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>
