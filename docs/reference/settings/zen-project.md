# Zen Project

Settings related to Zen Project for backward compatibility. Read [d͟e͟t͟a͟i͟l͟s͟](https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine).

## Zen project entrypoint<a href="#zen-project.entrypoint" id="zen-project.entrypoint"></a>

```yaml
BOX_ZEN_PROJECT_ENTRYPOINT: "<String>"
```

Entrypoint for zen project to start from.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.entrypoint</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_ENTRYPOINT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_ENTRYPOINT</code> , <br /><code>BOX_PROJECT_ENTRYPOINT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen project entry<a href="#zen-project.entry" id="zen-project.entry"></a>

```yaml
BOX_ZEN_PROJECT_ENTRY: "<String>"
```

Environment variable is used to specify zen project entry namespace

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.entry</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_ENTRY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_ENTRY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Load zen namespace as EDN<a href="#zen-project.load" id="zen-project.load"></a>

```yaml
BOX_ZEN_PROJECT_LOAD: "<String>"
```

Used to load a single namespace represented as EDN

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.load</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_LOAD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_LOAD</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## URL or path to the zen project source<a href="#zen-project.paths" id="zen-project.paths"></a>

```yaml
BOX_ZEN_PROJECT_PATHS: "<String>"
```

Source of the zen project using the following syntax `<source>:<format>:<path>[,<source>:<format>:<path>]*`.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.paths</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_PATHS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_PATHS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen project git protocol<a href="#zen-project.git.protocol" id="zen-project.git.protocol"></a>

```yaml
BOX_ZEN_PROJECT_GIT_PROTOCOL: "<String>"
```

Either `https` or `ssh`. Assumes local directory if omitted.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.protocol</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_PROTOCOL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_PROTOCOL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen project git URL<a href="#zen-project.git.url" id="zen-project.git.url"></a>

```yaml
BOX_ZEN_PROJECT_GIT_URL: "<String>"
```

Where to clone your project from. Aidbox substitutes it to `git clone <url>` command.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen project git access token<a href="#zen-project.git.access-token" id="zen-project.git.access-token"></a>

```yaml
BOX_ZEN_PROJECT_GIT_ACCESS_TOKEN: "<String>"
```

Token to access HTTPS private repository

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.access-token</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_ACCESS_TOKEN</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_ACCESS__TOKEN</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen project git checkout<a href="#zen-project.git.checkout" id="zen-project.git.checkout"></a>

```yaml
BOX_ZEN_PROJECT_GIT_CHECKOUT: "<String>"
```

Git branch or commit

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.checkout</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_CHECKOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_CHECKOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen project target path<a href="#zen-project.git.target-path" id="zen-project.git.target-path"></a>

```yaml
BOX_ZEN_PROJECT_GIT_TARGET_PATH: "<String>"
```

Clone the repository into a directory. Default value is a directory in `/tmp`

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.target-path</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_TARGET_PATH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_TARGET__PATH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Path to the zen project inside a git repository<a href="#zen-project.git.sub-path" id="zen-project.git.sub-path"></a>

```yaml
BOX_ZEN_PROJECT_GIT_SUB_PATH: "<String>"
```

The value of the setting should be set to a path starting with a repository name.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.git.sub-path</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_GIT_SUB_PATH</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROJECT_GIT_SUB__PATH</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen dev mode<a href="#zen-project.dev.mode" id="zen-project.dev.mode"></a>

```yaml
BOX_ZEN_PROJECT_DEV_MODE: false
```

Enables watcher which reloads zen namespaces when they change.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.dev.mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_DEV_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_DEV_MODE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Task executor service workers<a href="#zen-project.default-service-workers" id="zen-project.default-service-workers"></a>

```yaml
BOX_ZEN_PROJECT_DEFAULT_SERVICE_WORKERS: 3
```

The default number of task executor service workers.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.default-service-workers</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>3</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_DEFAULT_SERVICE_WORKERS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_DEFAULT_SERVICE_WORKERS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Zen config expose<a href="#zen-project.config.expose" id="zen-project.config.expose"></a>

```yaml
BOX_ZEN_PROJECT_CONFIG_EXPOSE: false
```

Show zen Aidbox config in zen UI and on $config endpoint

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.config.expose</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_CONFIG_EXPOSE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_ZEN_CONFIG_EXPOSE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Sync indexes on startup<a href="#zen-project.index.sync-on-start" id="zen-project.index.sync-on-start"></a>

```yaml
BOX_ZEN_PROJECT_INDEX_SYNC_ON_START: false
```

If enabled, Aidbox synchronizes managed index on startup

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.index.sync-on-start</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_INDEX_SYNC_ON_START</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_INDEX_SYNC__ON__START</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Manifest to zen migration<a href="#zen-project.manifest-to-zen-migration" id="zen-project.manifest-to-zen-migration"></a>

```yaml
BOX_ZEN_PROJECT_MANIFEST_TO_ZEN_MIGRATION: "<String>"
```



<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.manifest-to-zen-migration</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_MANIFEST_TO_ZEN_MIGRATION</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_MANIFEST__TO__ZEN__MIGRATION</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Enable zen-FHIR search parameters<a href="#zen-project.search.zen-fhir" id="zen-project.search.zen-fhir"></a>

```yaml
BOX_ZEN_PROJECT_SEARCH_ZEN_FHIR: "<Enum>"
```

Aidbox zen packages may contain search parameters.

Enable this setting to load these search parameters into Aidbox.
If disabled, only the pre-bundled and user-created search 
parameters are available.

This setting has no effect if FHIR-Schema validator is enabled.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.search.zen-fhir</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>enable</code><br /><code>disable</code></td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_SEARCH_ZEN_FHIR</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_ZEN__FHIR</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Check bindings<a href="#zen-project.validation.value-set-mode" id="zen-project.validation.value-set-mode"></a>

```yaml
BOX_ZEN_PROJECT_VALIDATION_VALUE_SET_MODE: true
```

Disable validation of FHIR terminology bindings

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.validation.value-set-mode</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_VALIDATION_VALUE_SET_MODE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_FEATURES_VALIDATION_VALUE__SET_MODE</code> , <br /><code>BOX_FEATURES_VALIDATION_VALUE__SET_MODE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Use SQL backward-compatible with old zen search<a href="#zen-project.search.resource-compat" id="zen-project.search.resource-compat"></a>

```yaml
BOX_ZEN_PROJECT_SEARCH_RESOURCE_COMPAT: true
```

For some time zen-search generated slightly different
SQL expressions.

Turn on this feature if you use zen-search
and do not wish to update indexes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.search.resource-compat</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_SEARCH_RESOURCE_COMPAT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SEARCH_RESOURCE__COMPAT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Enable terminology import<a href="#zen-project.terminology.import.enable" id="zen-project.terminology.import.enable"></a>

```yaml
BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_ENABLE: true
```

Enable terminology import.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.terminology.import.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_TERMINOLOGY_IMPORT_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Enable terminology sync<a href="#zen-project.terminology.import.sync" id="zen-project.terminology.import.sync"></a>

```yaml
BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_SYNC: false
```

Enable terminology sync.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.terminology.import.sync</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_TERMINOLOGY_IMPORT_SYNC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Build FTR index on startup<a href="#zen-project.ftr.build-index-on-startup.enable" id="zen-project.ftr.build-index-on-startup.enable"></a>

```yaml
BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_ENABLE: true
```

Build FTR index on startup.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.build-index-on-startup.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Sync FTR index on Aidbox start<a href="#zen-project.ftr.build-index-on-startup.sync" id="zen-project.ftr.build-index-on-startup.sync"></a>

```yaml
BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_SYNC: false
```

Sync FTR index on Aidbox start.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.build-index-on-startup.sync</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_BUILD_INDEX_ON_STARTUP_SYNC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_BUILD__INDEX__ON__STARTUP_SYNC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Enable incremental updates of the FTR index<a href="#zen-project.ftr.incremental-index-updates.enable" id="zen-project.ftr.incremental-index-updates.enable"></a>

```yaml
BOX_ZEN_PROJECT_FTR_INCREMENTAL_INDEX_UPDATES_ENABLE: true
```

Enable incremental updates of the FTR index

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.incremental-index-updates.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>true</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_INCREMENTAL_INDEX_UPDATES_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_INCREMENTAL__INDEX__UPDATES_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Load FTR index into Aidbox DB<a href="#zen-project.ftr.pull.enable" id="zen-project.ftr.pull.enable"></a>

```yaml
BOX_ZEN_PROJECT_FTR_PULL_ENABLE: false
```

Load FTR index into Aidbox DB

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.pull.enable</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_PULL_ENABLE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_PULL_ENABLE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Load FTR index synchronously<a href="#zen-project.ftr.pull.sync" id="zen-project.ftr.pull.sync"></a>

```yaml
BOX_ZEN_PROJECT_FTR_PULL_SYNC: false
```

Block Aidbox start until FTR index is loaded into Aidbox DB.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>zen-project.ftr.pull.sync</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_ZEN_PROJECT_FTR_PULL_SYNC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_FTR_PULL_SYNC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>
