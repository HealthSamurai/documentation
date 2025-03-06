## General

General settings

### Aidbox name<a href="#box-id" id="box-id"></a>

Aidbox instance unique ID. Can be used to separate telemetry data (logs, metrics, traces) in observability systems for multiple deployments.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>box-id</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>devbox</code></td></tr><tr><td>Environment variables</td><td><code>BOX_ID</code> , <br /><code>AIDBOX_BOX_ID</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Instance name<a href="#instance-name" id="instance-name"></a>

Provided instance name will be attached to metric labels. It is required for monitoring multiple Aidbox instances.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>instance-name</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_INSTANCE_NAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Root FHIR package<a href="#root-fhir-package" id="root-fhir-package"></a>

Identifier for the main Aidbox FHIR package that stores dependencies and canonical resources provided by the user.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>root-fhir-package</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>app.aidbox.main#0.0.1</code></td></tr><tr><td>Environment variables</td><td><code>BOX_ROOT_FHIR_PACKAGE</code> , <br /><code>AIDBOX_AR_PACKAGE</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Bootstrap FHIR package list<a href="#bootstrap-fhir-packages" id="bootstrap-fhir-packages"></a>

During the first startup Aidbox loads these packages.
This setting has no effect on any consecutive starts.

Format: 
`<name1>#<version1>:<name2>#<version2>...`

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>bootstrap-fhir-packages</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_BOOTSTRAP_FHIR_PACKAGES</code> , <br /><code>AIDBOX_FHIR_PACKAGES</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Init bundle<a href="#init-bundle" id="init-bundle"></a>

URL of the Bundle resource in JSON format executed on Aidbox startup. Not available to edit because if file is not reachable, Aidbox will not start.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>init-bundle</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_INIT_BUNDLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Share usage statistics with Aidbox developers<a href="#usage-stats" id="usage-stats"></a>

Allows Aidbox to collect and send high-level API usage statistics to help improve the product.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>usage-stats</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variables</td><td><code>BOX_USAGE_STATS</code> , <br /><code>BOX_TELEMETRY_USAGE_STATS</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — can be changed at runtime</td></tr></tbody></table>

### Settings mode<a href="#settings-mode" id="settings-mode"></a>

Settings mode defines the source of settings values and
    possibility to change them at runtime.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>settings-mode</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>legacy</code> — Reads configuration values from the legacy Aidbox                 configuration project (zen) in read-only mode. This mode exists                 for backward compatibility. It will be obsolete in July 2025.                 &lt;a href=&apos;https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine&apos;&gt;Read more&lt;/a&gt;<br /><code>read-only</code> — Reads settings values from environment variables and                 Aidbox settings in read-only mode. Loading configuration from                 the Aidbox configuration project (Zen) is disabled.<br /><code>read-write</code> — Enables editing Aidbox settings using the UI. Loading                 configuration from the Aidbox configuration project (Zen) is                 disabled.</td></tr><tr><td>Default value</td><td><code>legacy</code></td></tr><tr><td>Environment variables</td><td><code>BOX_SETTINGS_MODE</code></td></tr><tr><td>Available from</td><td><code>2502</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Enable export settings endpoint<a href="#export-settings-endpoint" id="export-settings-endpoint"></a>

Allow user to request current Aidbox settings as a set of environment variables on `/api/v1/settings/export`.

**Be aware, that endpoint may expose your sensitive settings.**

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>export-settings-endpoint</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_EXPORT_SETTINGS_ENDPOINT</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Enable cache replication<a href="#cache-replication-disable" id="cache-replication-disable"></a>

Aidbox automatically syncs runtime cache (SearchParameters, FHIR Schemas, OperationDefinitions, etc) across multiple instances for high availability. If you configure everything at startup and don't modify metadata at runtime, you can disable cache replication to improve performance.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>cache-replication-disable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_CACHE_REPLICATION_DISABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>
