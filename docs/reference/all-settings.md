# Aidbox Settings Reference

This document contains the full list of settings available in Aidbox.

## General

General settings

### Aidbox name<a href="#box-id" id="box-id"></a>

```yaml
BOX_ID: "devbox"
```

Aidbox instance unique ID. Can be used to separate telemetry data (logs, metrics, traces) in observability systems for multiple deployments.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>box-id</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>devbox</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ID</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_BOX_ID</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Instance name<a href="#instance-name" id="instance-name"></a>

```yaml
BOX_INSTANCE_NAME: "Aidbox"
```

Provided instance name will be attached to metric labels. It is required for monitoring multiple Aidbox instances.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>instance-name</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>Aidbox</code></td></tr><tr><td>Environment variable</td><td><code>BOX_INSTANCE_NAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Aidbox license<a href="#license" id="license"></a>

```yaml
BOX_LICENSE: "<String>"
```

License key obtained from the Aidbox user portal.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>license</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_LICENSE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LICENSE</code></td></tr><tr><td>Available from</td><td><code>2401</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### JAVA options<a href="#java-opts" id="java-opts"></a>

```yaml
JAVA_OPTS: "<String>"
```

Configure general JAVA options. For example - request and max heap size configuration `-Xms1024m -Xmx2048m`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>java-opts</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>JAVA_OPTS</code></td></tr><tr><td>Available from</td><td><code>2401</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Root FHIR package<a href="#root-fhir-package" id="root-fhir-package"></a>

```yaml
BOX_ROOT_FHIR_PACKAGE: "app.aidbox.main#0.0.1"
```

Identifier for the main Aidbox FHIR package that stores dependencies and canonical resources provided by the user.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>root-fhir-package</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>app.aidbox.main#0.0.1</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ROOT_FHIR_PACKAGE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_AR_PACKAGE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Bootstrap FHIR package list<a href="#bootstrap-fhir-packages" id="bootstrap-fhir-packages"></a>

```yaml
BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1"
```

During the first startup Aidbox loads these packages.
This setting has no effect on any consecutive starts.

Format: 
`<name1>#<version1>:<name2>#<version2>...`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>bootstrap-fhir-packages</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>hl7.fhir.r4.core#4.0.1</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_BOOTSTRAP_FHIR_PACKAGES</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FHIR_PACKAGES</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### FHIR NPM Package registry<a href="#fhir-npm-package-registry" id="fhir-npm-package-registry"></a>

```yaml
BOX_FHIR_NPM_PACKAGE_REGISTRY: "https://fs.get-ig.org/pkgs"
```

Aidbox will use the provided NPM registry URL to download or update FHIR NPM packages.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir-npm-package-registry</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>https://fs.get-ig.org/pkgs</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_NPM_PACKAGE_REGISTRY</code></td></tr><tr><td>Available from</td><td><code>2511</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Init bundle<a href="#init-bundle" id="init-bundle"></a>

```yaml
BOX_INIT_BUNDLE: "<String>"
```

URL of the Bundle resource in JSON format executed on Aidbox startup. Not available to edit because if file is not reachable, Aidbox will not start.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>init-bundle</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_INIT_BUNDLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Share usage statistics with Aidbox developers<a href="#usage-stats" id="usage-stats"></a>

```yaml
BOX_USAGE_STATS: true
```

Allows Aidbox to collect and send high-level API usage statistics to help improve the product.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>usage-stats</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_USAGE_STATS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_TELEMETRY_USAGE_STATS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Settings mode<a href="#settings-mode" id="settings-mode"></a>

```yaml
BOX_SETTINGS_MODE: "read-write"
```

Settings mode defines the source of settings values and
    possibility to change them at runtime.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>settings-mode</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>legacy</code> — Reads configuration values from the legacy Aidbox                 configuration project (zen) in read-only mode. This mode exists                 for backward compatibility. It will be obsolete in July 2025.                 &lt;a href=&apos;https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine&apos;&gt;Read more&lt;/a&gt;<br /><code>read-only</code> — Reads settings values from environment variables and                 Aidbox settings in read-only mode. Loading configuration from                 the Aidbox configuration project (Zen) is disabled.<br /><code>read-write</code> — Enables editing Aidbox settings using the UI. Loading                 configuration from the Aidbox configuration project (Zen) is                 disabled.</td></tr><tr><td>Recommended value</td><td><code>read-write</code></td></tr><tr><td>Default value</td><td><code>legacy</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SETTINGS_MODE</code></td></tr><tr><td>Available from</td><td><code>2502</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable export settings endpoint<a href="#export-settings-endpoint" id="export-settings-endpoint"></a>

```yaml
BOX_EXPORT_SETTINGS_ENDPOINT: false
```

Allow user to request current Aidbox settings as a set of environment variables on `/api/v1/settings/export`.

**Be aware, that endpoint may expose your sensitive settings.**

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>export-settings-endpoint</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_EXPORT_SETTINGS_ENDPOINT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable cache replication<a href="#cache-replication-disable" id="cache-replication-disable"></a>

```yaml
BOX_CACHE_REPLICATION_DISABLE: false
```

Aidbox automatically syncs runtime cache (SearchParameters, FHIR Schemas, OperationDefinitions, etc) across multiple instances for high availability. If you configure everything at startup and don't modify metadata at runtime, you can disable cache replication to improve performance.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>cache-replication-disable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_CACHE_REPLICATION_DISABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Aidbox modules load list<a href="#module-load" id="module-load"></a>

```yaml
BOX_MODULE_LOAD: "<String>"
```

During startup Aidbox loads these modules.
Format: 
`<module-name2>,<module-name2>,<module-name3>...`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module-load</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_LOAD</code></td></tr><tr><td>Available from</td><td><code>2505</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Aidbox module jar<a href="#module-jar" id="module-jar"></a>

```yaml
BOX_MODULE_JAR: "<String>"
```

Path to an Aidbox module jar files separated by ":"
Format: 
`<path1>:<path2>:<path3>...`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module-jar</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_JAR</code></td></tr><tr><td>Available from</td><td><code>2505</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## FHIR

FHIR settings

### General

General FHIR settings

#### Enable FHIR compliant mode<a href="#fhir.compliant-mode" id="fhir.compliant-mode"></a>

```yaml
BOX_FHIR_COMPLIANT_MODE: true
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

Becomes required if FHIRSchema is enabled.


<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.compliant-mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_COMPLIANT_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FHIR_COMPLIANT_MODE</code> , <br /><code>BOX_COMPLIANT__MODE__ENABLED?</code> , <br /><code>AIDBOX_COMPLIANCE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Return 404 on deleting non-existent resources<a href="#fhir.return-404-on-empty-delete" id="fhir.return-404-on-empty-delete"></a>

```yaml
BOX_FHIR_RETURN_404_ON_EMPTY_DELETE: false
```

Controls server response when deleting non-existing resources.
When enabled, returns 404 (Not Found) status code instead of the default
204 (No Content). Follows FHIR REST implementation where DELETE operations
on missing resources can signal resource absence rather than successful
deletion.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.return-404-on-empty-delete</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_RETURN_404_ON_EMPTY_DELETE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_HTTP_RETURN__404__ON__EMPTY__DELETE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Transaction max isolation level<a href="#fhir.transaction-max-isolation-level" id="fhir.transaction-max-isolation-level"></a>

```yaml
BOX_FHIR_TRANSACTION_MAX_ISOLATION_LEVEL: "none"
```

Sets the maximum (inclusive) isolation level for transactions. Can be overridden by the `x-max-isolation-level` header.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.transaction-max-isolation-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>none</code><br /><code>read-committed</code><br /><code>repeatable-read</code><br /><code>serializable</code></td></tr><tr><td>Default value</td><td><code>none</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TRANSACTION_MAX_ISOLATION_LEVEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FHIR_TRANSACTION_MAX__ISOLATION__LEVEL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Scheduler executor threads<a href="#scheduler-executors" id="scheduler-executors"></a>

```yaml
BOX_SCHEDULER_EXECUTORS: 4
```

Number of executor threads for the async task scheduler. Controls how many async tasks (e.g. `$purge` in async mode) can run concurrently.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>scheduler-executors</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>4</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SCHEDULER_EXECUTORS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Validation

Validation settings

#### Enable FHIR Schema validation mode<a href="#fhir.validation.fhir-schema-validation" id="fhir.validation.fhir-schema-validation"></a>

```yaml
BOX_FHIR_SCHEMA_VALIDATION: true
```

Activates the FHIR Schema validation engine which replaces
legacy ZEN and Entity/Attribute validation systems. Provides more
comprehensive structure validation against the FHIR resource schemas,
ensuring stronger conformance to FHIR specifications and more precise error
reporting.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.fhir-schema-validation</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SCHEMA_VALIDATION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FHIR_SCHEMA_VALIDATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Enforce strict profile resolution<a href="#fhir.validation.strict-profile-resolution" id="fhir.validation.strict-profile-resolution"></a>

```yaml
BOX_FHIR_VALIDATOR_STRICT_PROFILE_RESOLUTION: false
```

Requires all referenced profiles to be pre-loaded in Aidbox before
validation. When enabled, validation fails if profiles referenced in
resources are unknown to the server. Ensures complete validation integrity
by preventing partial validation against unknown profiles.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.strict-profile-resolution</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_VALIDATOR_STRICT_PROFILE_RESOLUTION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_VALIDATOR_STRICT_PROFILE_RESOLUTION_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Enforce strict FHIR extension resolution<a href="#fhir.validation.strict-extension-resolution" id="fhir.validation.strict-extension-resolution"></a>

```yaml
BOX_FHIR_VALIDATOR_STRICT_EXTENSION_RESOLUTION: false
```

Requires all referenced extensions to be formally defined in
    profiles loaded to the server.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.strict-extension-resolution</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_VALIDATOR_STRICT_EXTENSION_RESOLUTION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_VALIDATOR_STRICT_EXTENSION_RESOLUTION_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Bundle execution validation mode<a href="#fhir.bundle-execution-validation-mode" id="fhir.bundle-execution-validation-mode"></a>

```yaml
BOX_FHIR_BUNDLE_EXECUTION_VALIDATION_MODE: "limited"
```

Define validation mode for FHIR Bundle execution (after POST on `/fhir` endpoint).
 Doesn't effect CRUD on Bundle resources.
 Doesn't effect if `fhir.validation.fhir-schema-validation` is disabled.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bundle-execution-validation-mode</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>legacy</code> — Check only essential to execute bundle structure (default)<br /><code>limited</code> — Separated validation of the bundle structure (before execution) and resources in it (during execution)<br /><code>full</code> — Full bundle validation before execution (may cause performance issues due to double validation of resources</td></tr><tr><td>Recommended value</td><td><code>limited</code></td></tr><tr><td>Default value</td><td><code>legacy</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BUNDLE_EXECUTION_VALIDATION_MODE</code></td></tr><tr><td>Available from</td><td><code>2509</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Skip FHIR reference validation<a href="#fhir.validation.skip-reference" id="fhir.validation.skip-reference"></a>

```yaml
BOX_FHIR_VALIDATION_SKIP_REFERENCE: false
```

Bypasses validation of resource references during FHIR
operations. When enabled, allows creating and updating resources containing
references to non-existent target resources. Useful for staged data loading
or systems with eventual consistency but may compromise referential
integrity.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.skip-reference</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_VALIDATION_SKIP_REFERENCE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FEATURES_VALIDATION_SKIP__REFERENCE</code> , <br /><code>BOX_FEATURES_VALIDATION_SKIP__REFERENCE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Correct Aidbox format<a href="#fhir.validation.correct-aidbox-format" id="fhir.validation.correct-aidbox-format"></a>

```yaml
BOX_FHIR_CORRECT_AIDBOX_FORMAT: true
```

Transforms polymorphic extensions from FHIR format to Aidbox's internal
format. When enabled, extensions like `extension.*.valueString` are stored
as `extension.0.value.string` instead. Improves query performance and
consistency in Aidbox-specific operations while maintaining FHIR
compatibility in API responses.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.correct-aidbox-format</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_CORRECT_AIDBOX_FORMAT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_CORRECT_AIDBOX_FORMAT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### createdAt extension URL<a href="#fhir.validation.createdat-url" id="fhir.validation.createdat-url"></a>

```yaml
BOX_FHIR_CREATEDAT_URL: "https://aidbox.app/ex/createdAt"
```

Specifies the URL for the `createdAt` extension.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.createdat-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>https://aidbox.app/ex/createdAt</code></td></tr><tr><td>Default value</td><td><code>ex:createdAt</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_CREATEDAT_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_CREATED_AT_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### JSON schema datetime<a href="#fhir.validation.json-schema-datetime-regex" id="fhir.validation.json-schema-datetime-regex"></a>

```yaml
BOX_FHIR_JSON_SCHEMA_DATETIME_REGEX: "#{:fhir-datetime}"
```

Enables strict datetime validation in JSON schema validation engine.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.json-schema-datetime-regex</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>#{:fhir-datetime}</code></td></tr><tr><td>Default value</td><td><code>#{}</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_JSON_SCHEMA_DATETIME_REGEX</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Legacy FCE package<a href="#fhir.validation.legacy-fce-package" id="fhir.validation.legacy-fce-package"></a>

```yaml
BOX_FHIR_LEGACY_FCE_PACKAGE: "<String>"
```

The name and version of the package from which Aidbox first-class extensions are generated
Format: `package-name#package-version`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.validation.legacy-fce-package</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_LEGACY_FCE_PACKAGE</code></td></tr><tr><td>Available from</td><td><code>2508</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Search

Search settings

#### Use correct range arithmetic in search<a href="#fhir.search.comparisons" id="fhir.search.comparisons"></a>

```yaml
BOX_FHIR_SEARCH_COMPARISONS: true
```

FHIR date search is range based.
That is, dates are always converted to datetime ranges and then compared.

Historically, Aidbox uses slightly different range comparison arithmetic.
Turn on this setting to use FHIR comparisons.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.comparisons</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_COMPARISONS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_FHIR__COMPARISONS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Enable FHIR-conformant (rev)include behavior<a href="#fhir.search.include.conformant" id="fhir.search.include.conformant"></a>

```yaml
BOX_FHIR_SEARCH_INCLUDE_CONFORMANT: true
```

Due to historical reasons Aidbox treats the _include and _revinclude parameters slightly differently from the behavior described in the specification (without FHIR-conformant mode on).
The _(rev)include search parameter without the :iterate or :recurse modifier should only be applied to the initial ("matched") result. However, in Aidbox mode, it is also applied to the previous _(rev)include.
The _(rev)include parameter with the :iterate(:recurse) modifier should be repeatedly applied to the result with included resources. However, in Aidbox mode, it only resolves cyclic references.
In Aidbox mode, it is possible to search without specifying source type: GET /Patient?_include=general-practitioner, but in the FHIR-conformant mode it is not possible.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.include.conformant</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_INCLUDE_CONFORMANT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_INCLUDE_CONFORMANT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Authorize inline requests<a href="#fhir.search.authorize-inline-requests" id="fhir.search.authorize-inline-requests"></a>

```yaml
BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: true
```

Authorize inline requests (`_revinclude` and `_include`) with access policies. [Learn more](https://www.health-samurai.io/docs/aidbox/api/rest-api/fhir-search/include-and-revinclude#authorize-inline-requests-mode)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.authorize-inline-requests</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_AUTHORIZE_INLINE_REQUESTS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Use subselects in chained searches<a href="#fhir.search.chain.subselect" id="fhir.search.chain.subselect"></a>

```yaml
BOX_FHIR_SEARCH_CHAIN_SUBSELECT: true
```

Uses subselects instead of INNER JOINs + DISTINCT ON for chain searches:
- Forward chain searches
- Simple reverse chain searches (_has) with one level

This optimization significantly improves performance,
especially for queries with large result sets or many-to-one relationships.
May require building additional indexes for optimal performance.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.chain.subselect</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_CHAIN_SUBSELECT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_CHAIN_SUBSELECT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Enable FHIR composite search parameters<a href="#fhir.search.composite-parameters" id="fhir.search.composite-parameters"></a>

```yaml
BOX_FHIR_SEARCH_COMPOSITE_PARAMETERS: false
```

Enable support for FHIR composite search parameters.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.composite-parameters</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_COMPOSITE_PARAMETERS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_COMPOSITE__SEARCH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Iteration limit for (rev)include:iterate<a href="#fhir.search.include.iterate-max" id="fhir.search.include.iterate-max"></a>

```yaml
BOX_FHIR_SEARCH_INCLUDE_ITERATE_MAX: 10
```

Maximum number of iterations for `_include` and `_revinclude`
with `:recur` or `:iterate` modifier.

The default value is 10.
If set to 0, queries for _(rev)include will not be performed.
If set to a negative value, no limit will be applied.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.include.iterate-max</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_INCLUDE_ITERATE_MAX</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_INCLUDE_ITERATE__MAX</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default search timeout<a href="#fhir.search.default-params.timeout" id="fhir.search.default-params.timeout"></a>

```yaml
BOX_FHIR_SEARCH_DEFAULT_PARAMS_TIMEOUT: 60
```

Default timeout value (seconds). Also uses as timeout for the `count` query.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_DEFAULT__PARAMS_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default number of results per search page<a href="#fhir.search.default-params.count" id="fhir.search.default-params.count"></a>

```yaml
BOX_FHIR_SEARCH_DEFAULT_PARAMS_COUNT: 100
```

This is the default value of the _count search parameter.

It limits number of results per page

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.count</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>100</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_COUNT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_DEFAULT__PARAMS_COUNT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default search result count estimation method<a href="#fhir.search.default-params.total" id="fhir.search.default-params.total"></a>

```yaml
BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL: "accurate"
```

FHIR search response bundle may contain a result count estimation.

    If you use `BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL=none` you still get `total`when:
- you don't use `_page`
- the number of returned resources is less than `_count` (by default is 100).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.total</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>none</code> — omit estimation (fastest)<br /><code>estimate</code> — use approximate value (fast)<br /><code>accurate</code> — use exact value (could be slow)</td></tr><tr><td>Default value</td><td><code>accurate</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_DEFAULT__PARAMS_TOTAL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SQL operator to use for token search<a href="#fhir.search.token-operator" id="fhir.search.token-operator"></a>

```yaml
BOX_FHIR_SEARCH_TOKEN_OPERATOR: "@>"
```

Token and Reference search parameters use exact match.

Aidbox uses Postgres @> operator for this type of searches. The @> operator is the containment operator. It checks that FHIR resource contains some subresource.

The main advantage of the @> operator is that the single GIN index covers all token and reference searches. However sometimes Postgres planner can not build effecient query plan.

Alternatively in some cases it is possible to extract value directly using #>> operator. This operator extracts value from the given path. There is a limitation: path must not contain any arrays.
Engines options:

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.token-operator</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>@&gt;</code> — One GIN index per resource covers all token searches. Sometimes the Postgres planner can incorrectly estimate the index lookup cost, which leads to slow queries.<br /><code>#&gt;&gt;</code> — Needs an index per #&gt;&gt; expression. If path to the target element contains arrays, @&gt; will be used instead.</td></tr><tr><td>Default value</td><td><code>@&gt;</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_TOKEN_OPERATOR</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_TOKEN__OPERATOR</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### JSONB query engine<a href="#fhir.search.engine" id="fhir.search.engine"></a>

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

#### Enable support for multiple languages in search<a href="#fhir.search.multilingual.enable" id="fhir.search.multilingual.enable"></a>

```yaml
BOX_FHIR_SEARCH_MULTILINGUAL_ENABLE: false
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

#### Use Accept-Language header for search<a href="#fhir.search.multilingual.use-accept-language-header" id="fhir.search.multilingual.use-accept-language-header"></a>

```yaml
BOX_FHIR_SEARCH_MULTILINGUAL_USE_ACCEPT_LANGUAGE_HEADER: false
```

Use the Accept-Language header to specify search language

See fhir.search.multilingual.enable for details.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.multilingual.use-accept-language-header</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_MULTILINGUAL_USE_ACCEPT_LANGUAGE_HEADER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MULTILINGUAL_USE__ACCEPT__LANGUAGE__HEADER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Use main value if translation is not found<a href="#fhir.search.multilingual.fallback" id="fhir.search.multilingual.fallback"></a>

```yaml
BOX_FHIR_SEARCH_MULTILINGUAL_FALLBACK: true
```

When the _search-language parameter is used,
Aidbox uses translation in FHIR extension for search.

If this setting is enabled, Aidbox additionally uses
the main value (i.e. not in translation extension)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.multilingual.fallback</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_SEARCH_MULTILINGUAL_FALLBACK</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MULTILINGUAL_FALLBACK</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Terminology

Terminology settings

#### FHIR terminology service base URL<a href="#fhir.terminology.service-base-url" id="fhir.terminology.service-base-url"></a>

```yaml
BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: "https://tx.health-samurai.io/fhir"
```

Specifies the base URL of the terminology server used for code
validation and ValueSet expansion operations. Required for validating coded
elements against their ValueSets and CodeSystems. When not configured, code
validation is skipped entirely.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.terminology.service-base-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>https://tx.health-samurai.io/fhir</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_TERMINOLOGY_SERVICE_BASE_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Terminology Engine<a href="#fhir.terminology.engine" id="fhir.terminology.engine"></a>

```yaml
BOX_FHIR_TERMINOLOGY_ENGINE: "hybrid"
```

Controls how Aidbox handles terminology APIs

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.terminology.engine</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>hybrid</code> — Combines local storage with external server fallback (recommended)<br /><code>legacy</code> — Routes all requests to external terminology servers<br /><code>local</code> — Uses only resources stored in Aidbox&apos;s FAR</td></tr><tr><td>Recommended value</td><td><code>hybrid</code></td></tr><tr><td>Default value</td><td><code>legacy</code></td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TERMINOLOGY_ENGINE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### External Terminology Server<a href="#fhir.terminology.engine.hybrid.external-tx-server" id="fhir.terminology.engine.hybrid.external-tx-server"></a>

```yaml
BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: "https://tx.health-samurai.io/fhir"
```

Specifies the base URL of an external terminology server to be used in 'hybrid' terminology engine mode. This setting is ignored for other modes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.terminology.engine.hybrid.external-tx-server</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>https://tx.health-samurai.io/fhir</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Bulk Data Export

Bulk Data Export settings

#### Bulk storage provider<a href="#fhir.bulk-storage.provider" id="fhir.bulk-storage.provider"></a>

```yaml
BOX_FHIR_BULK_STORAGE_PROVIDER: "<Enum>"
```

Storage provider for bulk export

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.provider</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>gcp</code> — Google Cloud Platform<br /><code>aws</code> — Amazon Web Services<br /><code>azure</code> — Microsoft Azure: Cloud Computing Services</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_PROVIDER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_BACKEND</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### GCP service account<a href="#fhir.bulk-storage.gcp.service-account" id="fhir.bulk-storage.gcp.service-account"></a>

```yaml
BOX_FHIR_BULK_STORAGE_GCP_SERVICE_ACCOUNT: "<String>"
```

`GCPServiceAccount` resource ID for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.gcp.service-account</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_GCP_SERVICE_ACCOUNT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### GCP bucket<a href="#fhir.bulk-storage.gcp.bucket" id="fhir.bulk-storage.gcp.bucket"></a>

```yaml
BOX_FHIR_BULK_STORAGE_GCP_BUCKET: "<String>"
```

GCP bucket name for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.gcp.bucket</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_GCP_BUCKET</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_GCP_BUCKET</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### AWS service account ID<a href="#fhir.bulk-storage.aws.account" id="fhir.bulk-storage.aws.account"></a>

```yaml
BOX_FHIR_BULK_STORAGE_AWS_ACCOUNT: "<String>"
```

AWS Account resource ID for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.aws.account</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_AWS_ACCOUNT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_AWS_ACCOUNT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### AWS bucket<a href="#fhir.bulk-storage.aws.bucket" id="fhir.bulk-storage.aws.bucket"></a>

```yaml
BOX_FHIR_BULK_STORAGE_AWS_BUCKET: "<String>"
```

AWS S3 bucket name for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.aws.bucket</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_AWS_BUCKET</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_AWS_BUCKET</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Azure service account ID<a href="#fhir.bulk-storage.azure.container" id="fhir.bulk-storage.azure.container"></a>

```yaml
BOX_FHIR_BULK_STORAGE_AZURE_CONTAINER: "<String>"
```

Azure Container resource ID for `$export`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.azure.container</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_FHIR_BULK_STORAGE_AZURE_CONTAINER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_BULK__STORAGE_AZURE_CONTAINER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## Security and Access Control

Security & Access Control settings

### Grant page URL<a href="#security.grant-page-url" id="security.grant-page-url"></a>

```yaml
BOX_SECURITY_GRANT_PAGE_URL: "/auth/grant"
```

URL of consent screen. A consent screen is an interface presented to a user during the authorization code grant flow.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.grant-page-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>/auth/grant</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_GRANT_PAGE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_AUTH_GRANT__PAGE__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable FHIR Audit Log<a href="#security.audit-log.enabled" id="security.audit-log.enabled"></a>

```yaml
BOX_SECURITY_AUDIT_LOG_ENABLED: true
```

Generates structured audit logs in FHIR R4 AuditEvent format (with other FHIR versions will not be generated).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUDIT_LOG_ENABLED</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_SECURITY_AUDIT__LOG_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Audit Log repository URL<a href="#security.audit-log.repository-url" id="security.audit-log.repository-url"></a>

```yaml
BOX_SECURITY_AUDIT_LOG_REPOSITORY_URL: "<String>"
```

Full URL of the external destination where Aidbox streams all audit events.
Before setting the URL, you must enable the audit log in Aidbox.
If audit log is enabled, repository URL not specified, Aidbox will store Audit Event in the PostgreSQL database.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.repository-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUDIT_LOG_REPOSITORY_URL</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Audit Log flush interval<a href="#security.audit-log.flush-interval" id="security.audit-log.flush-interval"></a>

```yaml
BOX_SECURITY_AUDIT_LOG_FLUSH_INTERVAL: 5000
```

Interval time in ms to flush audit events to Audit Log Repository

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.flush-interval</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>5000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUDIT_LOG_FLUSH_INTERVAL</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Audit Log maximum flush interval<a href="#security.audit-log.max-flush-interval" id="security.audit-log.max-flush-interval"></a>

```yaml
BOX_SECURITY_AUDIT_LOG_MAX_FLUSH_INTERVAL: 60000
```

If sending the audit event to the repository fails, the send interval gradually increases up to this value.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.max-flush-interval</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUDIT_LOG_MAX_FLUSH_INTERVAL</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Audit Log batch count<a href="#security.audit-log.batch-count" id="security.audit-log.batch-count"></a>

```yaml
BOX_SECURITY_AUDIT_LOG_BATCH_COUNT: 1000
```

Max count of Audit Log batch (FHIR bandle entry count).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.batch-count</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>1000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUDIT_LOG_BATCH_COUNT</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Audit Log request headers<a href="#security.audit-log.request-headers" id="security.audit-log.request-headers"></a>

```yaml
BOX_SECURITY_AUDIT_LOG_REQUEST_HEADERS: "<String>"
```

The headers for Audit Log external repository requests, formatted as HeaderName:HeaderValue&#92;nHeaderName:HeaderValue.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.audit-log.request-headers</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUDIT_LOG_REQUEST_HEADERS</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable access control for mapping<a href="#security.iam.mapping.enable-access-control" id="security.iam.mapping.enable-access-control"></a>

```yaml
BOX_SECURITY_IAM_MAPPING_ENABLE_ACCESS_CONTROL: false
```

Enable access control for `/Mapping/<mapping-id>/$apply` operation.
If enabled, access control will be applied to the resulting transaction.
If disabled, only access to $apply endpoints are verified.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.iam.mapping.enable-access-control</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_IAM_MAPPING_ENABLE_ACCESS_CONTROL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MAPPING_ENABLE__ACCESS__CONTROL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Encryption API secret<a href="#security.encrypt-secret" id="security.encrypt-secret"></a>

```yaml
BOX_SECURITY_ENCRYPT_SECRET: "<String>"
```

Secret key for encryption API. [Learn more](https://www.health-samurai.io/docs/aidbox/api/other/encryption-api)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.encrypt-secret</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_ENCRYPT_SECRET</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ENCRYPT_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Allow CORS requests<a href="#security.cors.enabled" id="security.cors.enabled"></a>

```yaml
BOX_SECURITY_CORS_ENABLED: true
```

Enable Cross-Origin Resource Sharing (CORS) request handling.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.cors.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_CORS_ENABLED</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_WEB_CORS_ENABLED</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Allow CORS requests from origins<a href="#security.cors.origins" id="security.cors.origins"></a>

```yaml
BOX_SECURITY_CORS_ORIGINS: "*"
```

Comma separated list of origins `[schema]://[domain]:[port]`
Default is wildcard value `"*"` 

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.cors.origins</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>*</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_CORS_ORIGINS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_WEB_CORS_ORIGINS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Content security policy header<a href="#security.content-security-policy-header" id="security.content-security-policy-header"></a>

```yaml
BOX_SECURITY_CONTENT_SECURITY_POLICY_HEADER: "<String>"
```

Defines the Content Security Policy (CSP) header to enhance
security by restricting resource loading. It specifies the policies for
loading scripts, styles, media, fonts, and other resources.

Refer to the [OWASP Content Security Policy Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Content_Security_Policy_Cheat_Sheet.html)

Recommended value:
```
default-src 'self'; script-src 'report-sample' 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'report-sample' 'self' 'unsafe-inline'; object-src 'none'; base-uri 'self'; connect-src 'self'; font-src 'self'; frame-src 'self'; frame-ancestors 'self'; img-src 'self'; manifest-src 'self'; media-src 'self'; worker-src 'self';
```

Explanation:

| **Directive**     | **Allowed Sources**                                             | **Description**                                                                          | **Security Implications**                                                                             |
| ----------------- | --------------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| `default-src`     | `'self'`                                                        | Sets the default policy for all resource types unless overridden by specific directives. | Restricts all resources to the same origin unless explicitly allowed elsewhere.                       |
| `script-src`      | `'report-sample'`, `'self'`, `'unsafe-inline'`, `'unsafe-eval'` | Controls JavaScript sources.                                                             | Allows same-origin scripts but also permits inline scripts and `eval()`, which are security risks.    |
| `style-src`       | `'report-sample'`, `'self'`, `'unsafe-inline'`                  | Defines valid sources for stylesheets.                                                   | Allows same-origin styles but permits inline styles, which can be exploited if not carefully managed. |
| `object-src`      | `'none'`                                                        | Blocks `<object>` elements entirely.                                                     | Prevents the use of potentially dangerous `<object>` elements, mitigating XSS risks.                  |
| `base-uri`        | `'self'`                                                        | Restricts the URLs allowed in `<base>` elements to the same origin.                      | Protects against base URL manipulation attacks.                                                       |
| `connect-src`     | `'self'`                                                        | Limits connections (e.g., AJAX, WebSocket) to the same origin.                           | Prevents data exfiltration to unauthorized endpoints.                                                 |
| `font-src`        | `'self'`                                                        | Restricts font loading to the same origin.                                               | Reduces risks from malicious or unauthorized fonts.                                                   |
| `frame-src`       | `'self'`                                                        | Allows embedding content in frames only from the same origin.                            | Mitigates clickjacking attacks by disallowing external framing of your content.                       |
| `frame-ancestors` | `'self'`                                                        | Ensures that only pages from the same origin can embed this page in a frame.             | Further protects against clickjacking by controlling who can frame Aidbox pages .                     |
| `img-src`         | `'self'` `data:`                                                | Limits image sources to the same origin.                                                 | Prevents data leaks via malicious or unauthorized images.                                             |
| `manifest-src`    | `'self'`                                                        | Ensures that web app manifests are loaded only from the same origin.                     | Protects against unauthorized or malicious web app manifests being loaded into Aidbox.                |
| `media-src`       | `'self'`                                                        | Restricts audio and video sources to the same origin.                                    | Prevents unauthorized media files from being loaded into Aidbox                                       |
| `worker-src`      | `'self'`                                                        | Limits web workers and shared workers to scripts from the same origin.                   | Reduces risks of malicious workers being executed within your Aidbox context.                         |

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.content-security-policy-header</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_CONTENT_SECURITY_POLICY_HEADER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_CONTENT_SECURITY_POLICY_HEADER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Skip JWT validation<a href="#security.skip-jwt-validation" id="security.skip-jwt-validation"></a>

```yaml
BOX_SECURITY_SKIP_JWT_VALIDATION: false
```

Skip JWT token validation process.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.skip-jwt-validation</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_SKIP_JWT_VALIDATION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_AUTHENTICATION_SKIP__JWT__VALIDATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### JWT public key<a href="#security.auth.keys.public" id="security.auth.keys.public"></a>

```yaml
BOX_SECURITY_AUTH_KEYS_PUBLIC: "<String>"
```

RS256 signing algorithm expects providing private key for signing JWT and public key for verifying it.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth.keys.public</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUTH_KEYS_PUBLIC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_AUTH_KEYS_PUBLIC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### JWT private key<a href="#security.auth.keys.private" id="security.auth.keys.private"></a>

```yaml
BOX_SECURITY_AUTH_KEYS_PRIVATE: "<String>"
```

RS256 signing algorithm expects providing private key for signing JWT and public key for verifying it.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth.keys.private</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUTH_KEYS_PRIVATE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_AUTH_KEYS_PRIVATE</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### JWT secret<a href="#security.auth.keys.secret" id="security.auth.keys.secret"></a>

```yaml
BOX_SECURITY_AUTH_KEYS_SECRET: "<String>"
```

HS256 signing algorithm needs only having a secret for both operations.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth.keys.secret</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUTH_KEYS_SECRET</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_AUTH_KEYS_SECRET</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Auto-create users from foreign tokens<a href="#security.introspection-create-user" id="security.introspection-create-user"></a>

```yaml
BOX_SECURITY_INTROSPECTION_CREATE_USER: false
```

Creates local user accounts automatically when valid external JWT tokens are presented but no matching user exists.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.introspection-create-user</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_INTROSPECTION_CREATE_USER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_AUTHENTICATION_INTROSPECTION_CREATE__USER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Auth with non-validated JWT<a href="#security.auth-with-not-validated-jwt" id="security.auth-with-not-validated-jwt"></a>

```yaml
BOX_SECURITY_AUTH_WITH_NOT_VALIDATED_JWT: "<String>"
```

This configuration is used when `skip-jwt-validation` setting is enabled.
It's a string that contains EDN object with `:headers` and `:user-id-paths` keys.
For example: `{:headers #{"authorization" "x-client-token"}, :user-id-paths #{[:authorization :user_id] [:my-client-token :user :id]}}`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.auth-with-not-validated-jwt</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_AUTH_WITH_NOT_VALIDATED_JWT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_AUTHENTICATION_AUTH__WITH__NOT__VALIDATED__JWT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable LBAC<a href="#security.lbac.enabled" id="security.lbac.enabled"></a>

```yaml
BOX_SECURITY_LBAC_ENABLED: false
```

Label-based Access Control engine provides a mechanism to restrict access to bundles, resources, or resource elements depending on permissions associated with a request.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.lbac.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_LBAC_ENABLED</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_SECURITY__LABELS_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Strip security labels<a href="#security.lbac.strip-labels" id="security.lbac.strip-labels"></a>

```yaml
BOX_SECURITY_LBAC_STRIP_LABELS: false
```

Removes security labels from resource responses before
returning them to clients. When enabled, prevents sensitive security
metadata from being exposed in API responses while maintaining access
control enforcement internally. Useful for hiding security implementation
details from end users. Stripping is only applied during the masking.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.lbac.strip-labels</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_LBAC_STRIP_LABELS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_SECURITY__LABELS_STRIP__LABELS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable organization-based hierarchical access control<a href="#security.orgbac.enabled" id="security.orgbac.enabled"></a>

```yaml
BOX_SECURITY_ORGBAC_ENABLED: false
```

Activates hierarchical access control based on organizational
structure. Restricts user access to resources based on their organizational
affiliation and hierarchy position. 

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.orgbac.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_ORGBAC_ENABLED</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_ORGBAC_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable SU header<a href="#security.debug-su-enable" id="security.debug-su-enable"></a>

```yaml
BOX_SECURITY_DEBUG_SU_ENABLE: false
```

This setting enables `SU` header functionality.
`SU` header allows a user to substitute User ID for the duration of the request.
Only the administrator is allowed to use the `SU` header.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.debug-su-enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_DEBUG_SU_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DEBUG_SU_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable Aidbox developer mode<a href="#security.dev-mode" id="security.dev-mode"></a>

```yaml
BOX_SECURITY_DEV_MODE: true
```

Activates debugging features for access policy development, including the `_debug=policy URL` parameter and `x-debug` header. Returns detailed policy evaluation traces showing why requests were allowed or denied. For development environments only - not recommended for production systems.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>security.dev-mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_SECURITY_DEV_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_DEV_MODE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Vault config path<a href="#vault.config" id="vault.config"></a>

```yaml
BOX_VAULT_CONFIG: "/etc/aidbox/vault-config.json"
```

Path to the vault config JSON file that maps named secrets to file paths and resource scopes. When set, Aidbox reads the config at startup and resolves secret-backed resource fields from mounted files at runtime. See [External Secrets](../configuration/secret-files.md) for full documentation.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>vault.config</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default — feature disabled)</td></tr><tr><td>Environment variable</td><td><code>BOX_VAULT_CONFIG</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Modules

Modules settings

### Subscriptions

Google Cloud Pub/Sub subscriptions settings

#### Google Cloud Pub/Sub topic name<a href="#module.subscriptions.pubsub.topic" id="module.subscriptions.pubsub.topic"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_TOPIC: "<String>"
```

Name of the Google Cloud Pub/Sub topic.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.topic</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_TOPIC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Google Cloud Pub/Sub project name<a href="#module.subscriptions.pubsub.project" id="module.subscriptions.pubsub.project"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_PROJECT: "<String>"
```

Name of the Google Cloud Project which contains Pub/Sub topics and subscriptions.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.project</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_PROJECT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_PROJECT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Google Cloud Pub/Sub resource types<a href="#module.subscriptions.pubsub.resource-types" id="module.subscriptions.pubsub.resource-types"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_RESOURCE_TYPES: "<String>"
```

Specify resource types and boxes for which to publish notifications.
If not set, messages for all resource types from all boxes are published.

```
<rt>:?<box-id>?( <rt>:?<box-id>?)*
// Examples:
//
// Notify only on Patient or Encounter resources
// change from any box.

"Patient Encounter"

// Notify on Patient changes from boxone or boxtwo
// and about Encounter from any box.

"Patient:boxone Patient:boxtwo Encounter"
```

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.resource-types</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_RESOURCE_TYPES</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_RESOURCE__TYPES</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Google Cloud Pub/Sub service account email<a href="#module.subscriptions.pubsub.service-account.email" id="module.subscriptions.pubsub.service-account.email"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_EMAIL: "<String>"
```

Email of the Google Cloud Pub/Sub service account.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.service-account.email</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_EMAIL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_EMAIL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Google Cloud Pub/Sub service account private key<a href="#module.subscriptions.pubsub.service-account.private-key" id="module.subscriptions.pubsub.service-account.private-key"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_PRIVATE_KEY: "<String>"
```

Private key of the Google Cloud Pub/Sub service account.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.service-account.private-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_PRIVATE_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_PRIVATE__KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Google Cloud Pub/Sub publish before save<a href="#module.subscriptions.pubsub.before-save" id="module.subscriptions.pubsub.before-save"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_BEFORE_SAVE: false
```

If true, the resource will be published to the Pub/Sub topic before saving it to the database.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.before-save</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_BEFORE_SAVE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_BEFORE__SAVE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Google Cloud Pub/Sub emulator URL<a href="#module.subscriptions.pubsub.emulator-url" id="module.subscriptions.pubsub.emulator-url"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_EMULATOR_URL: "<String>"
```

URL of the Google Cloud Pub/Sub emulator.
If set, the emulator will be used instead of the real Pub/Sub service.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.emulator-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_EMULATOR_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_EMULATOR__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Notebooks

Aidbox notebooks settings

#### Notebook repository URL<a href="#module.notebook.repo-url" id="module.notebook.repo-url"></a>

```yaml
BOX_MODULE_NOTEBOOK_REPO_URL: "https://aidbox.app"
```

Set repository to fetch published notebooks

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.notebook.repo-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>https://aidbox.app</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_NOTEBOOK_REPO_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_NOTEBOOKS_REPO_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Mail Provider

Mail Provider settings

#### Default provider type<a href="#provider.default.type" id="provider.default.type"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_TYPE: "<String>"
```

Specifies the email service provider used for system-generated communications.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.type</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_TYPE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__PROVIDER_TYPE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider from address<a href="#provider.default.from" id="provider.default.from"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_FROM: "<String>"
```

From address for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider username<a href="#provider.default.username" id="provider.default.username"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_USERNAME: "<String>"
```

Username for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.username</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_USERNAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider password<a href="#provider.default.password" id="provider.default.password"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: "<String>"
```

Password for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider URL<a href="#provider.default.url" id="provider.default.url"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_URL: "<String>"
```

URL for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider port<a href="#provider.default.port" id="provider.default.port"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_PORT: "<String>"
```

Port for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.port</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider SSL<a href="#provider.default.ssl" id="provider.default.ssl"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_SSL: false
```

Enable SSL for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.ssl</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_SSL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__PROVIDER_SSL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider TLS<a href="#provider.default.tls" id="provider.default.tls"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_TLS: false
```

Enable TLS for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.tls</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_TLS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__PROVIDER_TLS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default provider host<a href="#provider.default.host" id="provider.default.host"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_HOST: "<String>"
```

Host for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_HOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Mailgun provider from address<a href="#provider.mailgun.from" id="provider.mailgun.from"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_FROM: "<String>"
```

From address for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Mailgun provider username<a href="#provider.mailgun.username" id="provider.mailgun.username"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_USERNAME: "<String>"
```

Username for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.username</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_USERNAME</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_USERNAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Mailgun provider password<a href="#provider.mailgun.password" id="provider.mailgun.password"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_PASSWORD: "<String>"
```

Password for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_PASSWORD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Mailgun provider URL<a href="#provider.mailgun.url" id="provider.mailgun.url"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_URL: "<String>"
```

URL for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Postmark provider from address<a href="#provider.postmark.from" id="provider.postmark.from"></a>

```yaml
BOX_MODULE_PROVIDER_POSTMARK_FROM: "<String>"
```

From address for the Postmark provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.postmark.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_POSTMARK_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_POSTMARK__PROVIDER_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Postmark provider API key<a href="#provider.postmark.api-key" id="provider.postmark.api-key"></a>

```yaml
BOX_MODULE_PROVIDER_POSTMARK_API_KEY: "<String>"
```

API key for the Postmark provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.postmark.api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_POSTMARK_API_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_POSTMARK__PROVIDER_API__KEY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Sendgrid provider from address<a href="#provider.sendgrid.from" id="provider.sendgrid.from"></a>

```yaml
BOX_MODULE_PROVIDER_SENDGRID_FROM: "<String>"
```

From address for the Sendgrid provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.sendgrid.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SENDGRID_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Sendgrid provider API key<a href="#provider.sendgrid.api-key" id="provider.sendgrid.api-key"></a>

```yaml
BOX_MODULE_PROVIDER_SENDGRID_API_KEY: "<String>"
```

API key for the Sendgrid provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.sendgrid.api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SENDGRID_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### EU Data Residency<a href="#provider.sendgrid.dataresidency" id="provider.sendgrid.dataresidency"></a>

```yaml
BOX_MODULE_PROVIDER_SENDGRID_DATARESIDENCY: "<String>"
```

EU Data Resident sending (via an EU-pinned subuser)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.sendgrid.dataresidency</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SENDGRID_DATARESIDENCY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider from address<a href="#provider.smtp.from" id="provider.smtp.from"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_FROM: "<String>"
```

From address for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider host<a href="#provider.smtp.host" id="provider.smtp.host"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_HOST: "<String>"
```

Host for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_HOST</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_HOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider password<a href="#provider.smtp.password" id="provider.smtp.password"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_PASSWORD: "<String>"
```

Password for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_PASSWORD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider port<a href="#provider.smtp.port" id="provider.smtp.port"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_PORT: "<String>"
```

Port for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.port</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_PORT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider SSL<a href="#provider.smtp.ssl" id="provider.smtp.ssl"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_SSL: false
```

Enable SSL for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.ssl</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_SSL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_SSL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider TLS<a href="#provider.smtp.tls" id="provider.smtp.tls"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_TLS: false
```

Enable TLS for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.tls</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_TLS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_TLS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### SMTP provider username<a href="#provider.smtp.username" id="provider.smtp.username"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_USERNAME: "<String>"
```

Username for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.username</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_USERNAME</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_USERNAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMARTbox

SMARTbox settings

#### Sandbox URL<a href="#module.smartbox.sandbox-url" id="module.smartbox.sandbox-url"></a>

```yaml
BOX_MODULE_SMARTBOX_SANDBOX_URL: "<String>"
```

URL for accessing sandbox environment

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.sandbox-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SANDBOX_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SANDBOX__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Terms of use URL<a href="#module.smartbox.terms-of-use-url" id="module.smartbox.terms-of-use-url"></a>

```yaml
BOX_MODULE_SMARTBOX_TERMS_OF_USE_URL: "<String>"
```

URL for accessing terms of use

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.terms-of-use-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_TERMS_OF_USE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_TERMS__OF__USE__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Sandbox basic<a href="#module.smartbox.sandbox-basic" id="module.smartbox.sandbox-basic"></a>

```yaml
BOX_MODULE_SMARTBOX_SANDBOX_BASIC: "<String>"
```

Basic authentication credentials for sandbox

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.sandbox-basic</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SANDBOX_BASIC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SANDBOX__BASIC</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Sandbox admin<a href="#module.smartbox.sandbox-admin" id="module.smartbox.sandbox-admin"></a>

```yaml
BOX_MODULE_SMARTBOX_SANDBOX_ADMIN: "<String>"
```

Admin credentials for sandbox access

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.sandbox-admin</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SANDBOX_ADMIN</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SANDBOX__ADMIN</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Session logs link<a href="#module.smartbox.session-logs-link" id="module.smartbox.session-logs-link"></a>

```yaml
BOX_MODULE_SMARTBOX_SESSION_LOGS_LINK: "<String>"
```

Link to access session logs

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.session-logs-link</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SESSION_LOGS_LINK</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SESSION__LOGS__LINK</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### MDM

MDM settings

#### Default MDM model<a href="#module.mdm.default-patient-model" id="module.mdm.default-patient-model"></a>

```yaml
BOX_MODULE_DEFAULT_PATIENT_MODEL: "<String>"
```

The default MDM model used for `$match` operation. Will be used if no model is specified with the `model` query parameter.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.mdm.default-patient-model</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_DEFAULT_PATIENT_MODEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_MDM_DEFAULT_PATIENT_MODEL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### MCP

MCP settings

#### Enable MCP server<a href="#module.mcp.server-enabled" id="module.mcp.server-enabled"></a>

```yaml
BOX_MODULE_MCP_SERVER_ENABLED: false
```

Enable MCP server working through SSE protocol

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.mcp.server-enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_MCP_SERVER_ENABLED</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Forms

Forms settings

#### Questionnaire.url prefix<a href="#module.sdc.builder.form-url-prefix" id="module.sdc.builder.form-url-prefix"></a>

```yaml
BOX_MODULE_SDC_BUILDER_FORM_URL_PREFIX: "http://forms.aidbox.io/questionnaire/"
```

URL prefix that will be used in URL generation for new forms

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.builder.form-url-prefix</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>http://forms.aidbox.io/questionnaire/</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_BUILDER_FORM_URL_PREFIX</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_BUILDER_FORM_URL_PREFIX</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### OpenAI API key<a href="#module.sdc.openai-api-key" id="module.sdc.openai-api-key"></a>

```yaml
BOX_MODULE_SDC_OPENAI_API_KEY: "<String>"
```

API key for OpenAI service

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.openai-api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_OPENAI_API_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_OPENAI_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Gemini API key<a href="#modules.sdc.gemini-api-key" id="modules.sdc.gemini-api-key"></a>

```yaml
BOX_SDC_GEMINI_API_KEY: "<String>"
```

API key for Gemini service

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>modules.sdc.gemini-api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SDC_GEMINI_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default language for UI<a href="#module.sdc.language" id="module.sdc.language"></a>

```yaml
BOX_MODULE_SDC_LANGUAGE: "en"
```

Language used as default in Form Builder and Form Renderer

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.language</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>en</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_LANGUAGE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_LANGUAGE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Default form theme<a href="#module.sdc.theme" id="module.sdc.theme"></a>

```yaml
BOX_MODULE_SDC_THEME: "<String>"
```

Theme that will be used in all forms

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.theme</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_THEME</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_THEME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Hide builder's back button<a href="#module.sdc.builder.hide-back-button" id="module.sdc.builder.hide-back-button"></a>

```yaml
BOX_MODULE_SDC_BUILDER_HIDE_BACK_BUTTON: false
```

Hide back button in UI Form Builder

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.builder.hide-back-button</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_BUILDER_HIDE_BACK_BUTTON</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_BUILDER_HIDE_BACK_BUTTON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Form's redirect-on-submit URL<a href="#module.sdc.form.redirect-on-submit" id="module.sdc.form.redirect-on-submit"></a>

```yaml
BOX_MODULE_SDC_FORM_REDIRECT_ON_SUBMIT: "<String>"
```

Redirect URI that will be used on form submit/amend button click

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.form.redirect-on-submit</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_FORM_REDIRECT_ON_SUBMIT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_FORM_REDIRECT_ON_SUBMIT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Form's redirect-on-save URL<a href="#module.sdc.form.redirect-on-save" id="module.sdc.form.redirect-on-save"></a>

```yaml
BOX_MODULE_SDC_FORM_REDIRECT_ON_SAVE: "<String>"
```

Redirect URI that will be used on form save/close button click

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.form.redirect-on-save</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_FORM_REDIRECT_ON_SAVE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_FORM_REDIRECT_ON_SAVE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Enable strict access control for sdc operations<a href="#module.sdc.strict-access-control" id="module.sdc.strict-access-control"></a>

```yaml
BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: true
```

Enable strict access control for operations(like populate/submit/reference-lookup) that can request different FHIR resources

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.strict-access-control</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_STRICT_ACCESS_CONTROL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_STRICT_ACCESS_CONTROL</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### GraphQL

GraphQL settings

#### Warmup GraphQL cache on startup<a href="#module.graphql.warmup-on-startup" id="module.graphql.warmup-on-startup"></a>

```yaml
BOX_MODULE_GRAPHQL_WARMUP_ON_STARTUP: false
```

Warmup GraphQL API cache on startup. When false, cache will be warmed up on first request.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.warmup-on-startup</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_WARMUP_ON_STARTUP</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Allow reference to any resource<a href="#module.graphql.reference-any" id="module.graphql.reference-any"></a>

```yaml
BOX_MODULE_GRAPHQL_REFERENCE_ANY: false
```

Enable GraphQL API (rev)include for reference with target - any

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.reference-any</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_REFERENCE_ANY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_REFERENCE__ANY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### GraphQL timeout<a href="#module.graphql.timeout" id="module.graphql.timeout"></a>

```yaml
BOX_MODULE_GRAPHQL_TIMEOUT: 60
```

GraphQL API query timeout in seconds: Set to zero to disable.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### GraphQL access control mode<a href="#module.graphql.access-control" id="module.graphql.access-control"></a>

```yaml
BOX_MODULE_GRAPHQL_ACCESS_CONTROL: "disabled"
```

Access control in GraphQL API

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.access-control</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>rest-search</code> — Additionally authorization checks access to corresponding search queries<br /><code>disabled</code> — Only access to GraphQL endpoint is verified</td></tr><tr><td>Default value</td><td><code>disabled</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_ACCESS_CONTROL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_ACCESS__CONTROL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Inject resource creation timestamp at meta.createdAt<a href="#module.graphql.inject-meta-created-at" id="module.graphql.inject-meta-created-at"></a>

```yaml
BOX_MODULE_GRAPHQL_INJECT_META_CREATED_AT: false
```

Aidbox GraphQL implementation sees resources in Aidbox format.
FHIR does not have a built-in property for creation datetime;
ergo, in FHIR Schema mode there is no way to access creation
datetime using GraphQL.

This settings injects `createdAt` property to resource meta.
The name is chosen to be consistent with Aidbox format
and non FHIR Schema GraphQL implementation in Aidbox.

This setting has no effect when FHIR Schema mode is not enabled.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.inject-meta-created-at</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_INJECT_META_CREATED_AT</code></td></tr><tr><td>Available from</td><td><code>2511</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Enable Apollo Federation support<a href="#module.graphql.federation-support" id="module.graphql.federation-support"></a>

```yaml
BOX_MODULE_GRAPHQL_FEDERATION_SUPPORT: false
```

Enable Apollo Federation support for the GraphQL API.

When enabled, Aidbox exposes federation-specific fields and directives
that allow it to participate as a subgraph in a federated GraphQL architecture.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.federation-support</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_FEDERATION_SUPPORT</code></td></tr><tr><td>Available from</td><td><code>2601</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Webpush

Webpush settings

#### Public Key<a href="#module.webpush.public-key" id="module.webpush.public-key"></a>

```yaml
BOX_MODULE_WEBPUSH_PUBLIC_KEY: "<String>"
```

ECDH/BC/prime256v1 Public Key

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.webpush.public-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_WEBPUSH_PUBLIC_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_WEBPUSH_KEYPAIR_PUBLIC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Private Key<a href="#module.webpush.private-key" id="module.webpush.private-key"></a>

```yaml
BOX_MODULE_WEBPUSH_PRIVATE_KEY: "<String>"
```

ECDH/BC/prime256v1 Private Key

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.webpush.private-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_WEBPUSH_PRIVATE_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_WEBPUSH_KEYPAIR_PRIVATE</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### JWT mail<a href="#module.webpush.jwt-mail" id="module.webpush.jwt-mail"></a>

```yaml
BOX_MODULE_WEBPUSH_JWT_MAIL: "<String>"
```

`JWT.sub` field value needs to be either a URL or a mailto email address.
If a push service needs to reach out to sender, it can find contact information from the JWT.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.webpush.jwt-mail</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_WEBPUSH_JWT_MAIL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_WEBPUSH_JWT_MAIL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## Database

Database settings

### Primary

Primary database settings

#### Database host<a href="#db.host" id="db.host"></a>

```yaml
BOX_DB_HOST: "postgres"
```

Database host address.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>postgres</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_HOST</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGHOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database port<a href="#db.port" id="db.port"></a>

```yaml
BOX_DB_PORT: 5432
```

Database port.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Recommended value</td><td><code>5432</code></td></tr><tr><td>Default value</td><td><code>5432</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_PORT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGPORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database name<a href="#db.database" id="db.database"></a>

```yaml
BOX_DB_DATABASE: "aidbox"
```

The database name. `postgres` name is not allowed.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.database</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>aidbox</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_DATABASE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGDATABASE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database user<a href="#db.user" id="db.user"></a>

```yaml
BOX_DB_USER: "aidbox"
```

The database username.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.user</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>aidbox</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_USER</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGUSER</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database password<a href="#db.password" id="db.password"></a>

```yaml
BOX_DB_PASSWORD: "<pg-password>"
```

A password of database role name.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>&lt;pg-password&gt;</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_PASSWORD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>PGPASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Install PostgreSQL extensions at startup<a href="#db.install-pg-extensions" id="db.install-pg-extensions"></a>

```yaml
BOX_DB_INSTALL_PG_EXTENSIONS: true
```

Automatically installs PostgreSQL extensions (pgcrypto, unaccent, pg_trgm, fuzzystrmatch) during server startup.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.install-pg-extensions</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_INSTALL_PG_EXTENSIONS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_INSTALL_PG_EXTENSIONS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database extension schema<a href="#db.extension-schema" id="db.extension-schema"></a>

```yaml
BOX_DB_EXTENSION_SCHEMA: "<String>"
```

Schema for PostgreSQL extensions. Default is current schema.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.extension-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_EXTENSION_SCHEMA</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_EXTENSION_SCHEMA</code> , <br /><code>AIDBOX_DB_PARAM_CURRENT_SCHEMA</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### ViewDefinition materialization schema<a href="#db.view-definition-schema" id="db.view-definition-schema"></a>

```yaml
BOX_VIEW_DEFINITION_SCHEMA: "sof"
```

Schema for storing `ViewDefinition` materialization. Changing this setting does not affect already materialized views; it applies only to new ones. It is recommended to use a dedicated schema for `ViewDefinition` to avoid potential collisions.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.view-definition-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>sof</code></td></tr><tr><td>Environment variable</td><td><code>BOX_VIEW_DEFINITION_SCHEMA</code></td></tr><tr><td>Available from</td><td><code>2508</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Pool connection timeout<a href="#db.pool.connection-timeout" id="db.pool.connection-timeout"></a>

```yaml
BOX_DB_POOL_CONNECTION_TIMEOUT: 30000
```

Maximum wait time (in milliseconds) for a database connection from the pool before timing out.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.connection-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>30000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_CONNECTION_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_CONNECTION__TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool connection init SQL<a href="#db.pool.connection-init-sql" id="db.pool.connection-init-sql"></a>

```yaml
BOX_DB_POOL_CONNECTION_INIT_SQL: "select 1"
```

Specifies a SQL statement that will be executed after every new connection creation before adding it to the pool.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.connection-init-sql</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>select 1</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_CONNECTION_INIT_SQL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_CONNECTION__INIT__SQL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool idle timeout<a href="#db.pool.idle-timeout" id="db.pool.idle-timeout"></a>

```yaml
BOX_DB_POOL_IDLE_TIMEOUT: 10000
```

Maximum timeout (in milliseconds) to close idle connection.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.idle-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_IDLE_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_IDLE__TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool minimum idle<a href="#db.pool.minimum-idle" id="db.pool.minimum-idle"></a>

```yaml
BOX_DB_POOL_MINIMUM_IDLE: 0
```

Minimum number of connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.minimum-idle</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>0</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_MINIMUM_IDLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_MINIMUM__IDLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool size<a href="#db.pool.maximum-pool-size" id="db.pool.maximum-pool-size"></a>

```yaml
BOX_DB_POOL_MAXIMUM_POOL_SIZE: 8
```

Maximum number of simultaneous database connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.pool.maximum-pool-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_POOL_MAXIMUM_POOL_SIZE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DB_POOL_MAXIMUM__POOL__SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Propagate DB health status to Aidbox<a href="#db.propagate-db-health-status-to-box" id="db.propagate-db-health-status-to-box"></a>

```yaml
BOX_PROPAGATE_DB_HEALTH_STATUS_TO_BOX: false
```

If enabled, the health status of the database will be reflected in the overall health status of Aidbox.
 If the database is unhealthy, Aidbox will also be considered unhealthy.

**NOTE**: Aidbox caches DB connections.
 That may cause inconsistencies when DB status is already `fail` but Aidbox instance still has valid connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.propagate-db-health-status-to-box</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_PROPAGATE_DB_HEALTH_STATUS_TO_BOX</code></td></tr><tr><td>Available from</td><td><code>2509</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Read-only replica

Read-only database replica settings

#### JDBC Application name<a href="#db.application-name" id="db.application-name"></a>

```yaml
BOX_DB_APPLICATION_NAME: "HealthSamurai Aidbox"
```

Controls the application name of the connection.

This name is visible for example in pg_stat_activity view.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.application-name</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>HealthSamurai Aidbox</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_APPLICATION_NAME</code></td></tr><tr><td>Available from</td><td><code>2603</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### JDBC Application name<a href="#db.maintenance.application-name" id="db.maintenance.application-name"></a>

```yaml
BOX_DB_MAINTENANCE_APPLICATION_NAME: "HealthSamurai Aidbox"
```

Controls the application name of the connection.

This name is visible for example in pg_stat_activity view.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.maintenance.application-name</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>HealthSamurai Aidbox</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_MAINTENANCE_APPLICATION_NAME</code></td></tr><tr><td>Available from</td><td><code>2603</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Read-only replica enable<a href="#db.ro-replica.enabled" id="db.ro-replica.enabled"></a>

```yaml
BOX_DB_RO_REPLICA_ENABLED: false
```

Enable options to process requests to read-only db replica.
If enabled you should define: `db.ro-replica.database`, `db.ro-replica.host`,
`db.ro-replica.port`, `db.ro-replica.user`, and `db.ro-replica.password`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_ENABLED</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database name<a href="#db.ro-replica.database" id="db.ro-replica.database"></a>

```yaml
BOX_DB_RO_REPLICA_DATABASE: "<String>"
```

The database name. `postgres` name is not allowed.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.database</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_DATABASE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database host<a href="#db.ro-replica.host" id="db.ro-replica.host"></a>

```yaml
BOX_DB_RO_REPLICA_HOST: "<String>"
```

AidboxDB host address.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_HOST</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database port<a href="#db.ro-replica.port" id="db.ro-replica.port"></a>

```yaml
BOX_DB_RO_REPLICA_PORT: "<Int>"
```

The database port number.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_PORT</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database user<a href="#db.ro-replica.user" id="db.ro-replica.user"></a>

```yaml
BOX_DB_RO_REPLICA_USER: "<String>"
```

The database username.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.user</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_USER</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database password<a href="#db.ro-replica.password" id="db.ro-replica.password"></a>

```yaml
BOX_DB_RO_REPLICA_PASSWORD: "<String>"
```

The database password.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_PASSWORD</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Database extension schema<a href="#db.ro-replica.extension-schema" id="db.ro-replica.extension-schema"></a>

```yaml
AIDBOX_EXTENSION_SCHEMA: "<String>"
```

The database extension schema.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.extension-schema</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>AIDBOX_EXTENSION_SCHEMA</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_DB_PARAM_CURRENT_SCHEMA</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### JDBC Application name<a href="#db.ro-replica.application-name" id="db.ro-replica.application-name"></a>

```yaml
AIDBOX_DB_RO_REPLICA_APPLICATION_NAME: "HealthSamurai Aidbox"
```

Controls the application name of the connection.

This name is visible for example in pg_stat_activity view.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.application-name</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>HealthSamurai Aidbox</code></td></tr><tr><td>Environment variable</td><td><code>AIDBOX_DB_RO_REPLICA_APPLICATION_NAME</code></td></tr><tr><td>Available from</td><td><code>2603</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Maximum wait time<a href="#db.ro-replica.pool.connection-timeout" id="db.ro-replica.pool.connection-timeout"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_CONNECTION_TIMEOUT: 30000
```

Maximum wait time (in milliseconds) for a database connection from the pool before timing out.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.connection-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>30000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_CONNECTION_TIMEOUT</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool idle timeout<a href="#db.ro-replica.pool.idle-timeout" id="db.ro-replica.pool.idle-timeout"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_IDLE_TIMEOUT: 10000
```

Maximum timeout (in milliseconds) to close idle connection.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.idle-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_IDLE_TIMEOUT</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool minimum idle<a href="#db.ro-replica.pool.minimum-idle" id="db.ro-replica.pool.minimum-idle"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_MINIMUM_IDLE: 0
```

Minimum number of connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.minimum-idle</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>0</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_MINIMUM_IDLE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool size<a href="#db.ro-replica.pool.maximum-pool-size" id="db.ro-replica.pool.maximum-pool-size"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_MAXIMUM_POOL_SIZE: 8
```

Maximum number of simultaneous database connections.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.maximum-pool-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_MAXIMUM_POOL_SIZE</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pool connection init SQL<a href="#db.ro-replica.pool.connection-init-sql" id="db.ro-replica.pool.connection-init-sql"></a>

```yaml
BOX_DB_RO_REPLICA_POOL_CONNECTION_INIT_SQL: "select 1"
```

The pool connection initialization SQL statement.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>db.ro-replica.pool.connection-init-sql</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>select 1</code></td></tr><tr><td>Environment variable</td><td><code>BOX_DB_RO_REPLICA_POOL_CONNECTION_INIT_SQL</code></td></tr><tr><td>Available from</td><td><code>2507</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Web Server

Web Server settings

### Base URL<a href="#web.base-url" id="web.base-url"></a>

```yaml
BOX_WEB_BASE_URL: "<base-url>"
```

Base URL is the URL Aidbox is available at. It consists of scheme (HTTP, HTTPS), domain, port (optional) and URL path (optional). Trailing slash is not allowed.

Aidbox uses this value to identify its own location. The Base URL is embedded in various generated artifacts, such as: tokens ("iss" field), links in search and notification bundles, and internal references. Some components validate that tokens, links , or requests originate from the same base URL, ensuring consistency and security.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.base-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Recommended value</td><td><code>&lt;base-url&gt;</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_BASE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_BASE_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Web server port<a href="#web.port" id="web.port"></a>

```yaml
BOX_WEB_PORT: 8888
```

Web server port that Aidbox listens on.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Recommended value</td><td><code>8888</code></td></tr><tr><td>Default value</td><td><code>8080</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_PORT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Web thread count<a href="#web.thread" id="web.thread"></a>

```yaml
BOX_WEB_THREAD: 8
```

The number of web server workers in Aidbox. The number of workers determines how many concurrent web requests Aidbox can handle.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.thread</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_THREAD</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### HTTP initial line max length<a href="#web.max-line" id="web.max-line"></a>

```yaml
BOX_WEB_MAX_LINE: 8192
```

Length limit for HTTP initial line and per header length, 414 (Request-URI Too Long) will be returned if exceeding this limit.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.max-line</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8192</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_MAX_LINE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_WEB_MAX__LINE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Request max body size<a href="#web.max-body" id="web.max-body"></a>

```yaml
BOX_WEB_MAX_BODY: 20971520
```

Maximum size of the request body in bytes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.max-body</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>20971520</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_MAX_BODY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_WEB_MAX__BODY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Observability

Observability settings

### Logs

Logs settings

#### Disable health logs<a href="#observability.disable-health-logs" id="observability.disable-health-logs"></a>

```yaml
BOX_OBSERVABILITY_DISABLE_HEALTH_LOGS: false
```

Disable `/health` endpoint requests logging. Default value is `false`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.disable-health-logs</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DISABLE_HEALTH_LOGS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_LOGGING_DISABLE__HEALTH__LOGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

#### Log file path<a href="#observability.log-file.path" id="observability.log-file.path"></a>

```yaml
BOX_OBSERVABILITY_LOG_FILE_PATH: "<String>"
```

If provided, enables mode to pipe logs as JSON into the file by specified path.

If ElasticSearch URL is provided then the file is used as a fallback in case if ElasticSearch is not available.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.log-file.path</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_LOG_FILE_PATH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LOGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Logs max lines<a href="#observability.log-file.max-lines" id="observability.log-file.max-lines"></a>

```yaml
BOX_OBSERVABILITY_LOG_FILE_MAX_LINES: 10000
```

Sets the limit of log records to push into the file
When the limit is reached, the current log file is renamed with `.old` postfix and a new log file is created

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.log-file.max-lines</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_LOG_FILE_MAX_LINES</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LOGS_MAX_LINES</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Logging SQL min duration<a href="#observability.sql.min-duration" id="observability.sql.min-duration"></a>

```yaml
BOX_OBSERVABILITY_SQL_MIN_DURATION: -1
```

Threshold for logging only long queries. Analogous from PostgreSQL.
Log only requests whose execution time exceeds the specified number of milliseconds.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.sql.min-duration</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>-1</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_SQL_MIN_DURATION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_LOGGING_SQL_MIN__DURATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Logging SQL max length<a href="#observability.sql.max-length" id="observability.sql.max-length"></a>

```yaml
BOX_OBSERVABILITY_SQL_MAX_LENGTH: 500
```

Max length of a query to be logged.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.sql.max-length</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>500</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_SQL_MAX_LENGTH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_LOGGING_SQL_MAX__LENGTH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Metrics

Metrics settings

#### Metrics server port<a href="#observability.metrics.server-port" id="observability.metrics.server-port"></a>

```yaml
BOX_METRICS_PORT: "<Int>"
```

Port on which Aidbox will expose metrics.
To disable metrics server, leave this setting empty

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.metrics.server-port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_METRICS_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Enable Postgres metrics<a href="#observability.metrics.enable-postgres-metrics" id="observability.metrics.enable-postgres-metrics"></a>

```yaml
BOX_OBSERVABILITY_METRICS_ENABLE_POSTGRES_METRICS: true
```

Controls whether to provide metrics [related to PostgreSQL](https://www.health-samurai.io/docs/aidbox/modules/observability/metrics/monitoring/use-aidbox-metrics-server#postgres)

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.metrics.enable-postgres-metrics</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_METRICS_ENABLE_POSTGRES_METRICS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_METRICS_POSTGRES_ON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Stdout

Stdout settings

#### Stdout log level<a href="#observability.stdout.log-level" id="observability.stdout.log-level"></a>

```yaml
BOX_OBSERVABILITY_STDOUT_LOG_LEVEL: "<Enum>"
```

Controls stdout with specified logs level.
Possible values: off, fatal, error, warn, info, debug, trace, all, true 
By setting one of these levels you would also get all the levels to the left. 
e.g. if you set log level to `warn` you would also get log events with `fatal` 
and `error` levels (off is excluded).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.stdout.log-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>off</code><br /><code>fatal</code><br /><code>error</code><br /><code>warn</code><br /><code>info</code><br /><code>debug</code><br /><code>trace</code><br /><code>all</code><br /><code>true</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_STDOUT_LOG_LEVEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_STDOUT_JSON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Pretty print log level<a href="#observability.stdout.pretty-log-level" id="observability.stdout.pretty-log-level"></a>

```yaml
BOX_OBSERVABILITY_STDOUT_PRETTY_LOG_LEVEL: "warn"
```

Controls pretty print of logs to stdout with specified level.
Possible values: off, fatal, error, warn, info, debug, trace, all, true 
By setting one of these levels you would also get all the levels to the left. 
e.g. if you set log level to `warn` you would also get log events with `fatal` 
and `error` levels (off is excluded).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.stdout.pretty-log-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>off</code><br /><code>fatal</code><br /><code>error</code><br /><code>warn</code><br /><code>info</code><br /><code>debug</code><br /><code>trace</code><br /><code>all</code><br /><code>true</code></td></tr><tr><td>Default value</td><td><code>warn</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_STDOUT_PRETTY_LOG_LEVEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_STDOUT_PRETTY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Google log level<a href="#observability.stdout.google-log-level" id="observability.stdout.google-log-level"></a>

```yaml
BOX_OBSERVABILITY_STDOUT_GOOGLE_LOG_LEVEL: "<Enum>"
```

Produces in Google Logging format with specified log level.
Possible values: off, fatal, error, warn, info, debug, trace, all, true 
By setting one of these levels you would also get all the levels to the left. 
e.g. if you set log level to `warn` you would also get log events with `fatal` 
and `error` levels (off is excluded).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.stdout.google-log-level</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>off</code><br /><code>fatal</code><br /><code>error</code><br /><code>warn</code><br /><code>info</code><br /><code>debug</code><br /><code>trace</code><br /><code>all</code><br /><code>true</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_STDOUT_GOOGLE_LOG_LEVEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_STDOUT_GOOGLE_JSON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Loki

Grafana Loki settings

#### Loki URL<a href="#observability.loki-url" id="observability.loki-url"></a>

```yaml
BOX_OBSERVABILITY_LOKI_URL: "<String>"
```

Loki URL to enable Aidbox logs uploading into Loki

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_LOKI_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LK_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Loki batch size<a href="#observability.loki.batch-size" id="observability.loki.batch-size"></a>

```yaml
BOX_OBSERVABILITY_LOKI_BATCH_SIZE: "<String>"
```

Loki batch size for log uploading.
Aidbox uploads logs when either at least specific `observability.loki.batch-size` collected
or time passed from previous log uploading exceeds `observability.loki.batch-timeout`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki.batch-size</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_LOKI_BATCH_SIZE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LK_BATCH_SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Loki batch timeout<a href="#observability.loki.batch-timeout" id="observability.loki.batch-timeout"></a>

```yaml
BOX_OBSERVABILITY_LOKI_BATCH_TIMEOUT: "<Int>"
```

How long to wait before uploading
Aidbox uploads logs when either at least `observability.loki.batch-size` entries collected
or time passed from previous log uploading exceeds `observability.loki.batch-timeout`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki.batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_LOKI_BATCH_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LK_BATCH_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Loki stream<a href="#observability.loki.stream" id="observability.loki.stream"></a>

```yaml
BOX_OBSERVABILITY_LOKI_STREAM: "<String>"
```

Stream refers to the labels or metadata associated with a log stream
Is defined by a unique set of labels, which serve as the stream key.
For example: `{"box": "aidbox"}`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.loki.stream</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_LOKI_STREAM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_LK_STREAM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Datadog

Datadog settings

#### Datadog API Key<a href="#observability.datadog.api-key" id="observability.datadog.api-key"></a>

```yaml
BOX_OBSERVABILITY_DATADOG_API_KEY: "<String>"
```

Datadog API Key.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DATADOG_API_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DD_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Datadog regional site<a href="#observability.datadog.site" id="observability.datadog.site"></a>

```yaml
BOX_OBSERVABILITY_DATADOG_SITE: "<Enum>"
```

The regional site for a Datadog customer.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.site</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>datadoghq.com</code><br /><code>us3.datadoghq.com</code><br /><code>us5.datadoghq.com</code><br /><code>datadoghq.eu</code><br /><code>ddog-gov.com</code><br /><code>ap1.datadoghq.com</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DATADOG_SITE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DD_SITE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Datadog log tags<a href="#observability.datadog.tags" id="observability.datadog.tags"></a>

```yaml
BOX_OBSERVABILITY_DATADOG_TAGS: "<String>"
```

Tags associated with your logs.
Convenient for transferring the name of the environment.
For example `env:staging`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.tags</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DATADOG_TAGS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DD_TAGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Datadog log file path<a href="#observability.datadog.logs" id="observability.datadog.logs"></a>

```yaml
BOX_OBSERVABILITY_DATADOG_LOGS: "<String>"
```

Fallback file to write logs in if uploading to Datadog fails

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.logs</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DATADOG_LOGS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_DD_LOGS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Datadog log batch size<a href="#observability.datadog.batch-size" id="observability.datadog.batch-size"></a>

```yaml
BOX_OBSERVABILITY_DATADOG_BATCH_SIZE: "<Int>"
```

How many log entries to collect before uploading.
Aidbox uploads logs when either at least specific `observability.datadog.batch-size` collected
or time passed from previous log uploading exceeds `observability.datadog.batch-timeout`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.batch-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DATADOG_BATCH_SIZE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DD_BATCH_SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Datadog log batch timeout<a href="#observability.datadog.batch-timeout" id="observability.datadog.batch-timeout"></a>

```yaml
BOX_OBSERVABILITY_DATADOG_BATCH_TIMEOUT: "<Int>"
```

How long to wait before uploading
Aidbox uploads logs when either at least specific `observability.datadog.batch-size` collected
or time passed from previous log uploading exceeds `observability.datadog.batch-timeout`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.datadog.batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_DATADOG_BATCH_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_DD_BATCH_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Elasticsearch

Elasticsearch settings

#### Elasticsearch auth<a href="#observability.elasticsearch.auth" id="observability.elasticsearch.auth"></a>

```yaml
BOX_OBSERVABILITY_ELASTICSEARCH_AUTH: "<String>"
```

Format: `<user>:<password>`
Basic auth credentials for Elasticsearch. API key is not supported.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elasticsearch.auth</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_ELASTICSEARCH_AUTH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_SEARCH_AUTH</code> , <br /><code>AIDBOX_ES_AUTH</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Elasticsearch URL<a href="#observability.elasticsearch.url" id="observability.elasticsearch.url"></a>

```yaml
BOX_OBSERVABILITY_ELASTICSEARCH_URL: "<String>"
```

If provided, enables mode to push logs to Elasticsearch

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elasticsearch.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_ELASTICSEARCH_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_SEARCH_URL</code> , <br /><code>AIDBOX_ES_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Elasticsearch batch size<a href="#observability.elasticsearch.batch-size" id="observability.elasticsearch.batch-size"></a>

```yaml
BOX_OBSERVABILITY_ELASTICSEARCH_BATCH_SIZE: "<Int>"
```

Log batch size used to optimize log shipping performance. The default value is 200

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elasticsearch.batch-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_ELASTICSEARCH_BATCH_SIZE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_BATCH_SIZE</code> , <br /><code>AIDBOX_ES_BATCH_SIZE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Elasticsearch batch timeout<a href="#observability.elasticsearch.batch-timeout" id="observability.elasticsearch.batch-timeout"></a>

```yaml
BOX_OBSERVABILITY_ELASTICSEARCH_BATCH_TIMEOUT: 60000
```

Timeout to post a batch to Elasticsearch. If there is not enough records to reach full batch size

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elasticsearch.batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_ELASTICSEARCH_BATCH_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_BATCH_TIMEOUT</code> , <br /><code>AIDBOX_ES_BATCH_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### Elasticsearch log format<a href="#observability.elasticsearch.index-pattern" id="observability.elasticsearch.index-pattern"></a>

```yaml
BOX_OBSERVABILITY_ELASTICSEARCH_INDEX_PATTERN: "'aidbox-logs'-yyyy-MM-dd"
```

Custom index format string. The default value is 'aidbox-logs'-yyyy-MM-dd.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.elasticsearch.index-pattern</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>&apos;aidbox-logs&apos;-yyyy-MM-dd</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_ELASTICSEARCH_INDEX_PATTERN</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_OBSERVABILITY_ELASTIC_INDEX_PATTERN</code> , <br /><code>AIDBOX_ES_INDEX_PAT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### OTEL

OpenTelemetry settings

#### OTEL metrics request headers<a href="#observability.otel.metrics-request-headers" id="observability.otel.metrics-request-headers"></a>

```yaml
BOX_OBSERVABILITY_OTEL_METRICS_REQUEST_HEADERS: "<String>"
```

The headers for OTEL metrics requests, formatted as HeaderName:HeaderValue
HeaderName:HeaderValue.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.metrics-request-headers</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_METRICS_REQUEST_HEADERS</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL metrics URL<a href="#observability.otel.metrics-url" id="observability.otel.metrics-url"></a>

```yaml
BOX_OBSERVABILITY_OTEL_METRICS_URL: "<String>"
```

The metrics' consumer URL (OTEL collector, Elastic EPM etc.).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.metrics-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_METRICS_URL</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL metrics interval<a href="#observability.otel.metrics-interval" id="observability.otel.metrics-interval"></a>

```yaml
BOX_OBSERVABILITY_OTEL_METRICS_INTERVAL: 5
```

The time interval (in seconds) for sending OTEL metrics.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.metrics-interval</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>5</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_METRICS_INTERVAL</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL traces request headers<a href="#observability.otel.traces-request-headers" id="observability.otel.traces-request-headers"></a>

```yaml
BOX_OBSERVABILITY_OTEL_TRACES_REQUEST_HEADERS: "<String>"
```

The headers for OTEL traces requests, formatted as HeaderName:HeaderValue
HeaderName:HeaderValue.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.traces-request-headers</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_TRACES_REQUEST_HEADERS</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL traces URL<a href="#observability.otel.traces-url" id="observability.otel.traces-url"></a>

```yaml
BOX_OBSERVABILITY_OTEL_TRACES_URL: "<String>"
```

The traces' consumer URL (OTEL collector, Elastic EPM etc.).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.traces-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_TRACES_URL</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL traces batch max size<a href="#observability.otel.traces-batch-max-size" id="observability.otel.traces-batch-max-size"></a>

```yaml
BOX_OBSERVABILITY_OTEL_TRACES_BATCH_MAX_SIZE: 100
```

Max amount of traces in one send traces request

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.traces-batch-max-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>100</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_TRACES_BATCH_MAX_SIZE</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL traces batch timeout<a href="#observability.otel.traces-batch-timeout" id="observability.otel.traces-batch-timeout"></a>

```yaml
BOX_OBSERVABILITY_OTEL_TRACES_BATCH_TIMEOUT: 1000
```

Timeout in milliseconds between send traces requests

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.traces-batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>1000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_TRACES_BATCH_TIMEOUT</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL traces history size<a href="#observability.otel.traces-history-size" id="observability.otel.traces-history-size"></a>

```yaml
BOX_OBSERVABILITY_OTEL_TRACES_HISTORY_SIZE: 10
```

Traces history size on telemetry $status endpoint

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.traces-history-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_TRACES_HISTORY_SIZE</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL logs request headers<a href="#observability.otel.logs-request-headers" id="observability.otel.logs-request-headers"></a>

```yaml
BOX_OBSERVABILITY_OTEL_LOGS_REQUEST_HEADERS: "<String>"
```

The headers for OTEL logs requests, formatted as HeaderName:HeaderValue
HeaderName:HeaderValue.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.logs-request-headers</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_LOGS_REQUEST_HEADERS</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL logs URL<a href="#observability.otel.logs-url" id="observability.otel.logs-url"></a>

```yaml
BOX_OBSERVABILITY_OTEL_LOGS_URL: "<String>"
```

The logs' consumer URL (OTEL collector, Elastic EPM etc.).

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.logs-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_LOGS_URL</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL logs batch max size<a href="#observability.otel.logs-batch-max-size" id="observability.otel.logs-batch-max-size"></a>

```yaml
BOX_OBSERVABILITY_OTEL_LOGS_BATCH_MAX_SIZE: 100
```

Max amount of logs in one send logs request

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.logs-batch-max-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>100</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_LOGS_BATCH_MAX_SIZE</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL logs batch timeout<a href="#observability.otel.logs-batch-timeout" id="observability.otel.logs-batch-timeout"></a>

```yaml
BOX_OBSERVABILITY_OTEL_LOGS_BATCH_TIMEOUT: 1000
```

Timeout in milliseconds between send logs requests

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.logs-batch-timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>1000</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_LOGS_BATCH_TIMEOUT</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

#### OTEL logs history size<a href="#observability.otel.logs-history-size" id="observability.otel.logs-history-size"></a>

```yaml
BOX_OBSERVABILITY_OTEL_LOGS_HISTORY_SIZE: 10
```

Logs history size on telemetry $status endpoint

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>observability.otel.logs-history-size</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>10</code></td></tr><tr><td>Environment variable</td><td><code>BOX_OBSERVABILITY_OTEL_LOGS_HISTORY_SIZE</code></td></tr><tr><td>Available from</td><td><code>2503</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen Project

Settings related to Zen Project for backward compatibility. Read [d͟e͟t͟a͟i͟l͟s͟](https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine).

### Zen project entrypoint<a href="#zen-project.entrypoint" id="zen-project.entrypoint"></a>

```yaml
BOX_ZEN_PROJECT_ENTRYPOINT: "<String>"
```

Entrypoint for zen project to start from.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.entrypoint</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_ENTRYPOINT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_ENTRYPOINT</code> , <br /><code>BOX_PROJECT_ENTRYPOINT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen project entry<a href="#zen-project.entry" id="zen-project.entry"></a>

```yaml
BOX_ZEN_PROJECT_ENTRY: "<String>"
```

Environment variable is used to specify zen project entry namespace

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.entry</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_ENTRY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_ENTRY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Load zen namespace as EDN<a href="#zen-project.load" id="zen-project.load"></a>

```yaml
BOX_ZEN_PROJECT_LOAD: "<String>"
```

Used to load a single namespace represented as EDN

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.load</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_LOAD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_LOAD</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### URL or path to the zen project source<a href="#zen-project.paths" id="zen-project.paths"></a>

```yaml
BOX_ZEN_PROJECT_PATHS: "<String>"
```

Source of the zen project using the following syntax `<source>:<format>:<path>[,<source>:<format>:<path>]*`.

`<source>` is either `url`, or `path`.
* `url` is used to load project from remote location
* `path` is used to load project from local location
`<format>` is either `zip`, or `dir`, or `edn`.

Table of sources and format compatibility:

|               |       |       |       |
| ------------- | ----- | ----- | ----- |
| source/format | `zip` | `dir` | `edn` |
| `url`         | ✓     |       | ✓     |
| `path`        | ✓     | ✓     | ✓     |
    

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.paths</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_PATHS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_PATHS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen project git protocol<a href="#zen-project.git.protocol" id="zen-project.git.protocol"></a>

```yaml
BOX_ZEN_PROJECT_GIT_PROTOCOL: "<String>"
```

Either `https` or `ssh`. Assumes local directory if omitted.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.protocol</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_PROTOCOL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_PROTOCOL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen project git URL<a href="#zen-project.git.url" id="zen-project.git.url"></a>

```yaml
BOX_ZEN_PROJECT_GIT_URL: "<String>"
```

Where to clone your project from. Aidbox substitutes it to `git clone <url>` command.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen project git access token<a href="#zen-project.git.access-token" id="zen-project.git.access-token"></a>

```yaml
BOX_ZEN_PROJECT_GIT_ACCESS_TOKEN: "<String>"
```

Token to access HTTPS private repository

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.access-token</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_ACCESS_TOKEN</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_ACCESS__TOKEN</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen project git checkout<a href="#zen-project.git.checkout" id="zen-project.git.checkout"></a>

```yaml
BOX_ZEN_PROJECT_GIT_CHECKOUT: "<String>"
```

Git branch or commit

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.checkout</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_CHECKOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_CHECKOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen project target path<a href="#zen-project.git.target-path" id="zen-project.git.target-path"></a>

```yaml
BOX_ZEN_PROJECT_GIT_TARGET_PATH: "<String>"
```

Clone the repository into a directory. Default value is a directory in `/tmp`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.target-path</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_TARGET_PATH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_TARGET__PATH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Path to the zen project inside a git repository<a href="#zen-project.git.sub-path" id="zen-project.git.sub-path"></a>

```yaml
BOX_ZEN_PROJECT_GIT_SUB_PATH: "<String>"
```

The value of the setting should be set to a path starting with a repository name.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.sub-path</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_SUB_PATH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_SUB__PATH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen dev mode<a href="#zen-project.dev.mode" id="zen-project.dev.mode"></a>

```yaml
BOX_ZEN_PROJECT_DEV_MODE: false
```

Enables watcher which reloads zen namespaces when they change.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.dev.mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_DEV_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_DEV_MODE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Task executor service workers<a href="#zen-project.default-service-workers" id="zen-project.default-service-workers"></a>

```yaml
BOX_ZEN_PROJECT_DEFAULT_SERVICE_WORKERS: 3
```

The default number of task executor service workers.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.default-service-workers</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>3</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_DEFAULT_SERVICE_WORKERS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_DEFAULT_SERVICE_WORKERS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Zen config expose<a href="#zen-project.config.expose" id="zen-project.config.expose"></a>

```yaml
BOX_ZEN_PROJECT_CONFIG_EXPOSE: false
```

Show zen Aidbox config in zen UI and on $config endpoint

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.config.expose</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_CONFIG_EXPOSE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_CONFIG_EXPOSE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Sync indexes on startup<a href="#zen-project.index.sync-on-start" id="zen-project.index.sync-on-start"></a>

```yaml
BOX_ZEN_PROJECT_INDEX_SYNC_ON_START: false
```

If enabled, Aidbox synchronizes managed index on startup

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.index.sync-on-start</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_INDEX_SYNC_ON_START</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_INDEX_SYNC__ON__START</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Manifest to zen migration<a href="#zen-project.manifest-to-zen-migration" id="zen-project.manifest-to-zen-migration"></a>

```yaml
BOX_ZEN_PROJECT_MANIFEST_TO_ZEN_MIGRATION: "<String>"
```



<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.manifest-to-zen-migration</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_MANIFEST_TO_ZEN_MIGRATION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MANIFEST__TO__ZEN__MIGRATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable zen-FHIR search parameters<a href="#zen-project.search.zen-fhir" id="zen-project.search.zen-fhir"></a>

```yaml
BOX_ZEN_PROJECT_SEARCH_ZEN_FHIR: "<Enum>"
```

Aidbox zen packages may contain search parameters.

Enable this setting to load these search parameters into Aidbox.
If disabled, only the pre-bundled and user-created search 
parameters are available.

This setting has no effect if FHIR-Schema validator is enabled.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.search.zen-fhir</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>enable</code><br /><code>disable</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_SEARCH_ZEN_FHIR</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_ZEN__FHIR</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Check bindings<a href="#zen-project.validation.value-set-mode" id="zen-project.validation.value-set-mode"></a>

```yaml
BOX_ZEN_PROJECT_VALIDATION_VALUE_SET_MODE: true
```

Disable validation of FHIR terminology bindings

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.validation.value-set-mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_VALIDATION_VALUE_SET_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FEATURES_VALIDATION_VALUE__SET_MODE</code> , <br /><code>BOX_FEATURES_VALIDATION_VALUE__SET_MODE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Use SQL backward-compatible with old zen search<a href="#zen-project.search.resource-compat" id="zen-project.search.resource-compat"></a>

```yaml
BOX_ZEN_PROJECT_SEARCH_RESOURCE_COMPAT: true
```

For some time zen-search generated slightly different
SQL expressions.

Turn on this feature if you use zen-search
and do not wish to update indexes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.search.resource-compat</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_SEARCH_RESOURCE_COMPAT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_RESOURCE__COMPAT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable terminology import<a href="#zen-project.terminology.import.enable" id="zen-project.terminology.import.enable"></a>

```yaml
BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_ENABLE: true
```

Enable terminology import.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.terminology.import.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_TERMINOLOGY_IMPORT_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable terminology sync<a href="#zen-project.terminology.import.sync" id="zen-project.terminology.import.sync"></a>

```yaml
BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_SYNC: false
```

Enable terminology sync.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.terminology.import.sync</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_SYNC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Build FTR index on startup<a href="#zen-project.ftr.build-index-on-startup.enable" id="zen-project.ftr.build-index-on-startup.enable"></a>

```yaml
BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_ENABLE: true
```

Build FTR index on startup.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.build-index-on-startup.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Sync FTR index on Aidbox start<a href="#zen-project.ftr.build-index-on-startup.sync" id="zen-project.ftr.build-index-on-startup.sync"></a>

```yaml
BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_SYNC: false
```

Sync FTR index on Aidbox start.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.build-index-on-startup.sync</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_SYNC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Enable incremental updates of the FTR index<a href="#zen-project.ftr.incremental-index-updates.enable" id="zen-project.ftr.incremental-index-updates.enable"></a>

```yaml
BOX_ZEN_PROJECT_FTR_INCREMENTAL_INDEX_UPDATES_ENABLE: true
```

Enable incremental updates of the FTR index

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.incremental-index-updates.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_INCREMENTAL_INDEX_UPDATES_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_INCREMENTAL__INDEX__UPDATES_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Load FTR index into Aidbox DB<a href="#zen-project.ftr.pull.enable" id="zen-project.ftr.pull.enable"></a>

```yaml
BOX_ZEN_PROJECT_FTR_PULL_ENABLE: false
```

Load FTR index into Aidbox DB

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.pull.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_PULL_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_PULL_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Load FTR index synchronously<a href="#zen-project.ftr.pull.sync" id="zen-project.ftr.pull.sync"></a>

```yaml
BOX_ZEN_PROJECT_FTR_PULL_SYNC: false
```

Block Aidbox start until FTR index is loaded into Aidbox DB.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.pull.sync</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_PULL_SYNC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_PULL_SYNC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>