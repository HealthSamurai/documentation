# FHIR

FHIR settings

## General

General FHIR settings

### Enable FHIR compliant mode<a href="#fhir.compliant-mode" id="fhir.compliant-mode"></a>

```yaml
BOX_FHIR_COMPLIANT_MODE: "<Bool>"
```

Enforces FHIR compatibility when enabled:

- Adds various attributes and endpoints info to CapabilityStatement
- Sanitises CapabilityStatement (i.e. removes attributes containing null values and empty arrays)
- Adds `/fhir` to base URL for FHIR search parameters definitions in CapabilityStatement
- Adds `BOX_WEB_BASE_URL` in `Bundle.link.url`
- Adds FHIR date search parameter validation on lastUpdated search parameter
- Adds `alg: RS256` entry for JWKS
- Changes validation error status to 422 (instead of 400)
- Changes cache-control header to no-store on authorization code auth flow (instead of `no-cache`, `no-store`, `max-age=0`, `must-revalidate`)
- Removes `Bundle.entry` if empty


<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.compliant-mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_COMPLIANT_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FHIR_COMPLIANT_MODE</code> , <br /><code>BOX_COMPLIANT__MODE__ENABLED?</code> , <br /><code>AIDBOX_COMPLIANCE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Return 404 on deleting non-existent resources<a href="#fhir.return-404-on-empty-delete" id="fhir.return-404-on-empty-delete"></a>

```yaml
BOX_FHIR_RETURN_404_ON_EMPTY_DELETE: "<Bool>"
```

Controls server response when deleting non-existing resources.
When enabled, returns 404 (Not Found) status code instead of the default
204 (No Content). Follows FHIR REST implementation where DELETE operations
on missing resources can signal resource absence rather than successful
deletion.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.return-404-on-empty-delete</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_RETURN_404_ON_EMPTY_DELETE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_HTTP_RETURN__404__ON__EMPTY__DELETE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Transaction max isolation level<a href="#fhir.transaction-max-isolation-level" id="fhir.transaction-max-isolation-level"></a>

```yaml
BOX_FHIR_TRANSACTION_MAX_ISOLATION_LEVEL: "none"
```

Sets the maximum (inclusive) isolation level for transactions. Can be overridden by the `x-max-isolation-level` header.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.transaction-max-isolation-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>none</code><br /><code>read-committed</code><br /><code>repeatable-read</code><br /><code>serializable</code></td></tr><tr><td>Default value</td><td><code>none</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TRANSACTION_MAX_ISOLATION_LEVEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FHIR_TRANSACTION_MAX__ISOLATION__LEVEL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Validation

Validation settings

### Enable FHIR Schema validation mode<a href="#fhir.validation.fhir-schema-validation" id="fhir.validation.fhir-schema-validation"></a>

```yaml
BOX_FHIR_SCHEMA_VALIDATION: "<Bool>"
```

Activates the FHIR Schema validation engine which replaces
legacy ZEN and Entity/Attribute validation systems. Provides more
comprehensive structure validation against the FHIR resource schemas,
ensuring stronger conformance to FHIR specifications and more precise error
reporting.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.fhir-schema-validation</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SCHEMA_VALIDATION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FHIR_SCHEMA_VALIDATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enforce strict profile resolution<a href="#fhir.validation.strict-profile-resolution" id="fhir.validation.strict-profile-resolution"></a>

```yaml
BOX_FHIR_VALIDATOR_STRICT_PROFILE_RESOLUTION: "<Bool>"
```

Requires all referenced profiles to be pre-loaded in Aidbox before
validation. When enabled, validation fails if profiles referenced in
resources are unknown to the server. Ensures complete validation integrity
by preventing partial validation against unknown profiles.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.strict-profile-resolution</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_VALIDATOR_STRICT_PROFILE_RESOLUTION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_VALIDATOR_STRICT_PROFILE_RESOLUTION_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enforce strict FHIR extension resolution<a href="#fhir.validation.strict-extension-resolution" id="fhir.validation.strict-extension-resolution"></a>

```yaml
BOX_FHIR_VALIDATOR_STRICT_EXTENSION_RESOLUTION: "<Bool>"
```

Requires all referenced extensions to be formally defined in
    profiles loaded to the server.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.strict-extension-resolution</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_VALIDATOR_STRICT_EXTENSION_RESOLUTION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_VALIDATOR_STRICT_EXTENSION_RESOLUTION_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Bundle execution validation mode<a href="#fhir.bundle-execution-validation-mode" id="fhir.bundle-execution-validation-mode"></a>

```yaml
BOX_FHIR_BUNDLE_EXECUTION_VALIDATION_MODE: "legacy"
```

Define validation mode for FHIR Bundle execution (after POST on `/fhir` endpoint).
 Doesn't effect CRUD on Bundle resources.
 Doesn't effect if `fhir.validation.fhir-schema-validation` is disabled.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bundle-execution-validation-mode</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>legacy</code> — Check only essential to execute bundle structure (default)<br /><code>limited</code> — Separated validation of the bundle structure (before execution) and resources in it (during execution)<br /><code>full</code> — Full bundle validation before execution (may cause performance issues due to double validation of resources</td></tr><tr><td>Default value</td><td><code>legacy</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BUNDLE_EXECUTION_VALIDATION_MODE</code></td></tr><tr><td>Available from</td><td><code>2509</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Skip FHIR reference validation<a href="#fhir.validation.skip-reference" id="fhir.validation.skip-reference"></a>

```yaml
BOX_FHIR_VALIDATION_SKIP_REFERENCE: "<Bool>"
```

Bypasses validation of resource references during FHIR
operations. When enabled, allows creating and updating resources containing
references to non-existent target resources. Useful for staged data loading
or systems with eventual consistency but may compromise referential
integrity.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.skip-reference</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_VALIDATION_SKIP_REFERENCE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FEATURES_VALIDATION_SKIP__REFERENCE</code> , <br /><code>BOX_FEATURES_VALIDATION_SKIP__REFERENCE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Correct Aidbox format<a href="#fhir.validation.correct-aidbox-format" id="fhir.validation.correct-aidbox-format"></a>

```yaml
BOX_FHIR_CORRECT_AIDBOX_FORMAT: "<Bool>"
```

Transforms polymorphic extensions from FHIR format to Aidbox's internal
format. When enabled, extensions like `extension.*.valueString` are stored
as `extension.0.value.string` instead. Improves query performance and
consistency in Aidbox-specific operations while maintaining FHIR
compatibility in API responses.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.correct-aidbox-format</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_CORRECT_AIDBOX_FORMAT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_CORRECT_AIDBOX_FORMAT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### createdAt extension URL<a href="#fhir.validation.createdat-url" id="fhir.validation.createdat-url"></a>

```yaml
BOX_FHIR_CREATEDAT_URL: "ex:createdAt"
```

Specifies the URL for the `createdAt` extension.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.createdat-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>ex:createdAt</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_CREATEDAT_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_CREATED_AT_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### JSON schema datetime<a href="#fhir.validation.json-schema-datetime-regex" id="fhir.validation.json-schema-datetime-regex"></a>

```yaml
BOX_FHIR_JSON_SCHEMA_DATETIME_REGEX: "#{}"
```

Enables strict datetime validation in JSON schema validation engine.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.json-schema-datetime-regex</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>#{}</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_JSON_SCHEMA_DATETIME_REGEX</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Legacy FCE package<a href="#fhir.validation.legacy-fce-package" id="fhir.validation.legacy-fce-package"></a>

```yaml
BOX_FHIR_LEGACY_FCE_PACKAGE: "<String>"
```

The name and version of the package from which Aidbox first-class extensions are generated
Format: `package-name#package-version`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.legacy-fce-package</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_LEGACY_FCE_PACKAGE</code></td></tr><tr><td>Available from</td><td><code>2508</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Search

Search settings

### Use correct range arithmetic in search<a href="#fhir.search.comparisons" id="fhir.search.comparisons"></a>

```yaml
BOX_FHIR_SEARCH_COMPARISONS: "<Bool>"
```

FHIR date search is range based.
That is, dates are always converted to datetime ranges and then compared.

Historically, Aidbox uses slightly different range comparison arithmetic.
Turn on this setting to use FHIR comparisons.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.comparisons</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_COMPARISONS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_FHIR__COMPARISONS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable FHIR-conformant (rev)include behavior<a href="#fhir.search.include.conformant" id="fhir.search.include.conformant"></a>

```yaml
BOX_FHIR_SEARCH_INCLUDE_CONFORMANT: "<Bool>"
```

Due to historical reasons Aidbox treats the _include and _revinclude parameters slightly differently from the behavior described in the specification (without FHIR-conformant mode on).
The _(rev)include search parameter without the :iterate or :recurse modifier should only be applied to the initial ("matched") result. However, in Aidbox mode, it is also applied to the previous _(rev)include.
The _(rev)include parameter with the :iterate(:recurse) modifier should be repeatedly applied to the result with included resources. However, in Aidbox mode, it only resolves cyclic references.
In Aidbox mode, it is possible to search without specifying source type: GET /Patient?_include=general-practitioner, but in the FHIR-conformant mode it is not possible.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.include.conformant</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_INCLUDE_CONFORMANT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_INCLUDE_CONFORMANT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Authorize inline requests<a href="#fhir.search.authorize-inline-requests" id="fhir.search.authorize-inline-requests"></a>

```yaml
BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "<Bool>"
```

Authorize inline requests (`_revinclude` and `_include`) with access policies. [Learn more](https://docs.aidbox.app/api/rest-api/fhir-search/include-and-revinclude#authorize-inline-requests-mode)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.authorize-inline-requests</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_AUTHORIZE_INLINE_REQUESTS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Use semi join in chained searches<a href="#fhir.search.chain.subselect" id="fhir.search.chain.subselect"></a>

```yaml
BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "<Bool>"
```

When the search query does not use _has search parameters, use subselect instead of INNER JOIN for forward chain searches.
This is a performance optimization which could require building additional indexes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.chain.subselect</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_CHAIN_SUBSELECT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_CHAIN_SUBSELECT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable FHIR composite search parameters<a href="#fhir.search.composite-parameters" id="fhir.search.composite-parameters"></a>

```yaml
BOX_FHIR_SEARCH_COMPOSITE_PARAMETERS: "<Bool>"
```

Enable support for FHIR composite search parameters.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.composite-parameters</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_COMPOSITE_PARAMETERS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_COMPOSITE__SEARCH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Iteration limit for (rev)include:iterate<a href="#fhir.search.include.iterate-max" id="fhir.search.include.iterate-max"></a>

```yaml
BOX_FHIR_SEARCH_INCLUDE_ITERATE_MAX: "10"
```

Maximum number of iterations for `_include` and `_revinclude`
with `:recur` or `:iterate` modifier.

The default value is 10.
If set to 0, queries for _(rev)include will not be performed.
If set to a negative value, no limit will be applied.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.include.iterate-max</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_INCLUDE_ITERATE_MAX</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_INCLUDE_ITERATE__MAX</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default search timeout<a href="#fhir.search.default-params.timeout" id="fhir.search.default-params.timeout"></a>

```yaml
BOX_FHIR_SEARCH_DEFAULT_PARAMS_TIMEOUT: "60"
```

Default timeout value (seconds). Also uses as timeout for the `count` query.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_DEFAULT__PARAMS_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default number of results per search page<a href="#fhir.search.default-params.count" id="fhir.search.default-params.count"></a>

```yaml
BOX_FHIR_SEARCH_DEFAULT_PARAMS_COUNT: "100"
```

This is the default value of the _count search parameter.

It limits number of results per page

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.count</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>100</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_COUNT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_DEFAULT__PARAMS_COUNT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default search result count estimation method<a href="#fhir.search.default-params.total" id="fhir.search.default-params.total"></a>

```yaml
BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL: "accurate"
```

FHIR search response bundle may contain a result count estimation.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.total</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>none</code> — omit estimation (fastest)<br /><code>estimate</code> — use approximate value (fast)<br /><code>accurate</code> — use exact value (could be slow)</td></tr><tr><td>Default value</td><td><code>accurate</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_DEFAULT__PARAMS_TOTAL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SQL operator to use for token search<a href="#fhir.search.token-operator" id="fhir.search.token-operator"></a>

```yaml
BOX_FHIR_SEARCH_TOKEN_OPERATOR: "@>"
```

Token and Reference search parameters use exact match.

Aidbox uses Postgres @> operator for this type of searches. The @> operator is the containment operator. It checks that FHIR resource contains some subresource.

The main advantage of the @> operator is that the single GIN index covers all token and reference searches. However sometimes Postgres planner can not build effecient query plan.

Alternatively in some cases it is possible to extract value directly using #>> operator. This operator extracts value from the given path. There is a limitation: path must not contain any arrays.
Engines options:

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.token-operator</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>@&gt;</code> — One GIN index per resource covers all token searches. Sometimes the Postgres planner can incorrectly estimate the index lookup cost, which leads to slow queries.<br /><code>#&gt;&gt;</code> — Needs an index per #&gt;&gt; expression. If path to the target element contains arrays, @&gt; will be used instead.</td></tr><tr><td>Default value</td><td><code>@&gt;</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_TOKEN_OPERATOR</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_TOKEN__OPERATOR</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### JSONB query engine<a href="#fhir.search.engine" id="fhir.search.engine"></a>

```yaml
BOX_FHIR_SEARCH_ENGINE: "knife"
```

Aidbox has two engines to search: jsonpath and jsonknife.

The engine is responsible for SQL generation for search operations.
SQL by jsonpath and jsonknife is different for search parameter types: date, number, quantity, reference, string, token, uri. 
_lastUpdated, _createdAt senarch parameters and :missing modifier searches also differ by engine.
jsonpath-engine:

jsonknife:
*using indexes makes performance approximately the same

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.engine</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>knife</code> — Legacy engine. Uses custom Postgres module in Aidboxdb and SQL functions fallback in other Postgres instances. Being phased out. Has better performance for dates, number and quantity search parameters. Using indexes makes performance approximately the same<br /><code>jsonpath</code> — &lt;ul&gt;&lt;li&gt;JSONpath language is available starting from PostgreSQL 12.&lt;/li&gt;&lt;li&gt;supported by PostgreSQL without external extensions, can be used with managed PostgreSQL, e.g. Azure PostgreSQL&lt;/li&gt;&lt;li&gt;better performance for string search parameters and all string-related search (e.g. :text modifier)*&lt;/li&gt;&lt;li&gt;will be supported as main engine&lt;/li&gt;&lt;/ul&gt;</td></tr><tr><td>Default value</td><td><code>knife</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_ENGINE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_ENGINE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable support for multiple languages in search<a href="#fhir.search.multilingual.enable" id="fhir.search.multilingual.enable"></a>

```yaml
BOX_FHIR_SEARCH_MULTILINGUAL_ENABLE: "<Bool>"
```

FHIR uses special extension to provide translations in resources.
Enable this setting to turn on the _search-language parameter.
This parameter (_search-language) specifies which language
to use for search.
i.e. which translation in a resource to use.

This feature requires Aidbox to build more
complex (so possibly slower) queries.
Leave this setting disabled if you don't need to search
across translations.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.multilingual.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_MULTILINGUAL_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MULTILINGUAL_ENABLE__SEARCH__LANGUAGE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Use Accept-Language header for search<a href="#fhir.search.multilingual.use-accept-language-header" id="fhir.search.multilingual.use-accept-language-header"></a>

```yaml
BOX_FHIR_SEARCH_MULTILINGUAL_USE_ACCEPT_LANGUAGE_HEADER: "<Bool>"
```

Use the Accept-Language header to specify search language

See fhir.search.multilingual.enable for details.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.multilingual.use-accept-language-header</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_MULTILINGUAL_USE_ACCEPT_LANGUAGE_HEADER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MULTILINGUAL_USE__ACCEPT__LANGUAGE__HEADER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Use main value if translation is not found<a href="#fhir.search.multilingual.fallback" id="fhir.search.multilingual.fallback"></a>

```yaml
BOX_FHIR_SEARCH_MULTILINGUAL_FALLBACK: "true"
```

When the _search-language parameter is used,
Aidbox uses translation in FHIR extension for search.

If this setting is enabled, Aidbox additionally uses
the main value (i.e. not in translation extension)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.multilingual.fallback</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_MULTILINGUAL_FALLBACK</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MULTILINGUAL_FALLBACK</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## Terminology

Terminology settings

### FHIR terminology service base URL<a href="#fhir.terminology.service-base-url" id="fhir.terminology.service-base-url"></a>

```yaml
BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: "<String>"
```

Specifies the base URL of the terminology server used for code
validation and ValueSet expansion operations. Required for validating coded
elements against their ValueSets and CodeSystems. When not configured, code
validation is skipped entirely.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.terminology.service-base-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_TERMINOLOGY_SERVICE_BASE_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Terminology Engine<a href="#fhir.terminology.engine" id="fhir.terminology.engine"></a>

```yaml
BOX_FHIR_TERMINOLOGY_ENGINE: "legacy"
```

Controls how Aidbox handles terminology APIs

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.terminology.engine</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>hybrid</code> — Combines local storage with external server fallback (recommended)<br /><code>legacy</code> — Routes all requests to external terminology servers<br /><code>local</code> — Uses only resources stored in Aidbox&apos;s FAR</td></tr><tr><td>Default value</td><td><code>legacy</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TERMINOLOGY_ENGINE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### External Terminology Server<a href="#fhir.terminology.engine.hybrid.external-tx-server" id="fhir.terminology.engine.hybrid.external-tx-server"></a>

```yaml
BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: "<String>"
```

Specifies the base URL of an external terminology server to be used in 'hybrid' terminology engine mode. This setting is ignored for other modes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.terminology.engine.hybrid.external-tx-server</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Bulk Data Export

Bulk Data Export settings

### Bulk storage provider<a href="#fhir.bulk-storage.provider" id="fhir.bulk-storage.provider"></a>

```yaml
BOX_FHIR_BULK_STORAGE_PROVIDER: "<Enum>"
```

Storage provider for bulk export

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.provider</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>gcp</code> — Google Cloud Platform<br /><code>aws</code> — Amazon Web Services<br /><code>azure</code> — Microsoft Azure: Cloud Computing Services</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_PROVIDER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_BACKEND</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### GCP service account<a href="#fhir.bulk-storage.gcp.service-account" id="fhir.bulk-storage.gcp.service-account"></a>

```yaml
BOX_FHIR_BULK_STORAGE_GCP_SERVICE_ACCOUNT: "<String>"
```

`GCPServiceAccount` resource ID for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.gcp.service-account</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_GCP_SERVICE_ACCOUNT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### GCP bucket<a href="#fhir.bulk-storage.gcp.bucket" id="fhir.bulk-storage.gcp.bucket"></a>

```yaml
BOX_FHIR_BULK_STORAGE_GCP_BUCKET: "<String>"
```

GCP bucket name for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.gcp.bucket</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_GCP_BUCKET</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_GCP_BUCKET</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### AWS service account ID<a href="#fhir.bulk-storage.aws.account" id="fhir.bulk-storage.aws.account"></a>

```yaml
BOX_FHIR_BULK_STORAGE_AWS_ACCOUNT: "<String>"
```

AWS Account resource ID for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.aws.account</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_AWS_ACCOUNT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_AWS_ACCOUNT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### AWS bucket<a href="#fhir.bulk-storage.aws.bucket" id="fhir.bulk-storage.aws.bucket"></a>

```yaml
BOX_FHIR_BULK_STORAGE_AWS_BUCKET: "<String>"
```

AWS S3 bucket name for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.aws.bucket</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_AWS_BUCKET</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_AWS_BUCKET</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Azure service account ID<a href="#fhir.bulk-storage.azure.container" id="fhir.bulk-storage.azure.container"></a>

```yaml
BOX_FHIR_BULK_STORAGE_AZURE_CONTAINER: "<String>"
```

Azure Container resource ID for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.azure.container</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_AZURE_CONTAINER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_AZURE_CONTAINER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>
