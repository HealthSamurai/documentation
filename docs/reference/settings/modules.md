# Modules

Modules settings

## Subscriptions

Google Cloud Pub/Sub subscriptions settings

### Google Cloud Pub/Sub topic name<a href="#module.subscriptions.pubsub.topic" id="module.subscriptions.pubsub.topic"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_TOPIC: "<String>"
```

Name of the Google Cloud Pub/Sub topic.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.topic</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_TOPIC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Google Cloud Pub/Sub project name<a href="#module.subscriptions.pubsub.project" id="module.subscriptions.pubsub.project"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_PROJECT: "<String>"
```

Name of the Google Cloud Project which contains Pub/Sub topics and subscriptions.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.project</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_PROJECT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_PROJECT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Google Cloud Pub/Sub resource types<a href="#module.subscriptions.pubsub.resource-types" id="module.subscriptions.pubsub.resource-types"></a>

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

### Google Cloud Pub/Sub service account email<a href="#module.subscriptions.pubsub.service-account.email" id="module.subscriptions.pubsub.service-account.email"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_EMAIL: "<String>"
```

Email of the Google Cloud Pub/Sub service account.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.service-account.email</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_EMAIL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_EMAIL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Google Cloud Pub/Sub service account private key<a href="#module.subscriptions.pubsub.service-account.private-key" id="module.subscriptions.pubsub.service-account.private-key"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_PRIVATE_KEY: "<String>"
```

Private key of the Google Cloud Pub/Sub service account.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.service-account.private-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT_PRIVATE_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_PRIVATE__KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Google Cloud Pub/Sub publish before save<a href="#module.subscriptions.pubsub.before-save" id="module.subscriptions.pubsub.before-save"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_BEFORE_SAVE: false
```

If true, the resource will be published to the Pub/Sub topic before saving it to the database.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.before-save</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_BEFORE_SAVE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_BEFORE__SAVE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Google Cloud Pub/Sub emulator URL<a href="#module.subscriptions.pubsub.emulator-url" id="module.subscriptions.pubsub.emulator-url"></a>

```yaml
BOX_MODULE_SUBSCRIPTIONS_PUBSUB_EMULATOR_URL: "<String>"
```

URL of the Google Cloud Pub/Sub emulator.
If set, the emulator will be used instead of the real Pub/Sub service.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.subscriptions.pubsub.emulator-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SUBSCRIPTIONS_PUBSUB_EMULATOR_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SUBSCRIPTIONS_PUBSUB_EMULATOR__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Notebooks

Aidbox notebooks settings

### Notebook repository URL<a href="#module.notebook.repo-url" id="module.notebook.repo-url"></a>

```yaml
BOX_MODULE_NOTEBOOK_REPO_URL: "https://aidbox.app"
```

Set repository to fetch published notebooks

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.notebook.repo-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>https://aidbox.app</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_NOTEBOOK_REPO_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_NOTEBOOKS_REPO_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## Mail Provider

Mail Provider settings

### Default provider type<a href="#provider.default.type" id="provider.default.type"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_TYPE: "<String>"
```

Specifies the email service provider used for system-generated communications.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.type</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_TYPE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__PROVIDER_TYPE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider from address<a href="#provider.default.from" id="provider.default.from"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_FROM: "<String>"
```

From address for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider username<a href="#provider.default.username" id="provider.default.username"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_USERNAME: "<String>"
```

Username for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.username</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_USERNAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider password<a href="#provider.default.password" id="provider.default.password"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: "<String>"
```

Password for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider URL<a href="#provider.default.url" id="provider.default.url"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_URL: "<String>"
```

URL for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider port<a href="#provider.default.port" id="provider.default.port"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_PORT: "<String>"
```

Port for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.port</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider SSL<a href="#provider.default.ssl" id="provider.default.ssl"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_SSL: false
```

Enable SSL for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.ssl</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_SSL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__PROVIDER_SSL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider TLS<a href="#provider.default.tls" id="provider.default.tls"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_TLS: false
```

Enable TLS for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.tls</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_TLS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_DEFAULT__PROVIDER_TLS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default provider host<a href="#provider.default.host" id="provider.default.host"></a>

```yaml
BOX_MODULE_PROVIDER_DEFAULT_HOST: "<String>"
```

Host for the default provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.default.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_DEFAULT_HOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Mailgun provider from address<a href="#provider.mailgun.from" id="provider.mailgun.from"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_FROM: "<String>"
```

From address for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Mailgun provider username<a href="#provider.mailgun.username" id="provider.mailgun.username"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_USERNAME: "<String>"
```

Username for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.username</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_USERNAME</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_USERNAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Mailgun provider password<a href="#provider.mailgun.password" id="provider.mailgun.password"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_PASSWORD: "<String>"
```

Password for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_PASSWORD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Mailgun provider URL<a href="#provider.mailgun.url" id="provider.mailgun.url"></a>

```yaml
BOX_MODULE_PROVIDER_MAILGUN_URL: "<String>"
```

URL for the Mailgun provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.mailgun.url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_MAILGUN_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_MAILGUN__PROVIDER_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Postmark provider from address<a href="#provider.postmark.from" id="provider.postmark.from"></a>

```yaml
BOX_MODULE_PROVIDER_POSTMARK_FROM: "<String>"
```

From address for the Postmark provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.postmark.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_POSTMARK_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_POSTMARK__PROVIDER_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Postmark provider API key<a href="#provider.postmark.api-key" id="provider.postmark.api-key"></a>

```yaml
BOX_MODULE_PROVIDER_POSTMARK_API_KEY: "<String>"
```

API key for the Postmark provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.postmark.api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_POSTMARK_API_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_POSTMARK__PROVIDER_API__KEY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider from address<a href="#provider.smtp.from" id="provider.smtp.from"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_FROM: "<String>"
```

From address for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.from</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_FROM</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_FROM</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider host<a href="#provider.smtp.host" id="provider.smtp.host"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_HOST: "<String>"
```

Host for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.host</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_HOST</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_HOST</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider password<a href="#provider.smtp.password" id="provider.smtp.password"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_PASSWORD: "<String>"
```

Password for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.password</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_PASSWORD</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_PASSWORD</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider port<a href="#provider.smtp.port" id="provider.smtp.port"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_PORT: "<String>"
```

Port for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.port</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_PORT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider SSL<a href="#provider.smtp.ssl" id="provider.smtp.ssl"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_SSL: false
```

Enable SSL for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.ssl</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_SSL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_SSL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider TLS<a href="#provider.smtp.tls" id="provider.smtp.tls"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_TLS: false
```

Enable TLS for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.tls</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_TLS</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_TLS</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### SMTP provider username<a href="#provider.smtp.username" id="provider.smtp.username"></a>

```yaml
BOX_MODULE_PROVIDER_SMTP_USERNAME: "<String>"
```

Username for the SMTP provider

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>provider.smtp.username</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_PROVIDER_SMTP_USERNAME</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_PROVIDER_SMTP__PROVIDER_USERNAME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## SMARTbox

SMARTbox settings

### Sandbox URL<a href="#module.smartbox.sandbox-url" id="module.smartbox.sandbox-url"></a>

```yaml
BOX_MODULE_SMARTBOX_SANDBOX_URL: "<String>"
```

URL for accessing sandbox environment

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.sandbox-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SANDBOX_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SANDBOX__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Terms of use URL<a href="#module.smartbox.terms-of-use-url" id="module.smartbox.terms-of-use-url"></a>

```yaml
BOX_MODULE_SMARTBOX_TERMS_OF_USE_URL: "<String>"
```

URL for accessing terms of use

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.terms-of-use-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_TERMS_OF_USE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_TERMS__OF__USE__URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Sandbox basic<a href="#module.smartbox.sandbox-basic" id="module.smartbox.sandbox-basic"></a>

```yaml
BOX_MODULE_SMARTBOX_SANDBOX_BASIC: "<String>"
```

Basic authentication credentials for sandbox

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.sandbox-basic</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SANDBOX_BASIC</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SANDBOX__BASIC</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Sandbox admin<a href="#module.smartbox.sandbox-admin" id="module.smartbox.sandbox-admin"></a>

```yaml
BOX_MODULE_SMARTBOX_SANDBOX_ADMIN: "<String>"
```

Admin credentials for sandbox access

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.sandbox-admin</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SANDBOX_ADMIN</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SANDBOX__ADMIN</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Session logs link<a href="#module.smartbox.session-logs-link" id="module.smartbox.session-logs-link"></a>

```yaml
BOX_MODULE_SMARTBOX_SESSION_LOGS_LINK: "<String>"
```

Link to access session logs

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.smartbox.session-logs-link</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SMARTBOX_SESSION_LOGS_LINK</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SMARTBOX_SESSION__LOGS__LINK</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## MDM

MDM settings

### Default MDM model<a href="#module.mdm.default-patient-model" id="module.mdm.default-patient-model"></a>

```yaml
BOX_MODULE_DEFAULT_PATIENT_MODEL: "<String>"
```

The default MDM model used for `$match` operation. Will be used if no model is specified with the `model` query parameter.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.mdm.default-patient-model</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_DEFAULT_PATIENT_MODEL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_MDM_DEFAULT_PATIENT_MODEL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## MCP

MCP settings

### Enable MCP server<a href="#module.mcp.server-enabled" id="module.mcp.server-enabled"></a>

```yaml
BOX_MODULE_MCP_SERVER_ENABLED: false
```

Enable MCP server working through SSE protocol

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.mcp.server-enabled</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_MCP_SERVER_ENABLED</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Forms

Forms settings

### Questionnaire.url prefix<a href="#module.sdc.builder.form-url-prefix" id="module.sdc.builder.form-url-prefix"></a>

```yaml
BOX_MODULE_SDC_BUILDER_FORM_URL_PREFIX: "http://forms.aidbox.io/questionnaire/"
```

URL prefix that will be used in URL generation for new forms

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.builder.form-url-prefix</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>http://forms.aidbox.io/questionnaire/</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_BUILDER_FORM_URL_PREFIX</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_BUILDER_FORM_URL_PREFIX</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### OpenAI API key<a href="#module.sdc.openai-api-key" id="module.sdc.openai-api-key"></a>

```yaml
BOX_MODULE_SDC_OPENAI_API_KEY: "<String>"
```

API key for OpenAI service

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.openai-api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_OPENAI_API_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_OPENAI_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Gemini API key<a href="#aidbox.modules.sdc.gemini-api-key" id="aidbox.modules.sdc.gemini-api-key"></a>

```yaml
BOX_SDC_GEMINI_API_KEY: "<String>"
```

API key for Gemini service

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>aidbox.modules.sdc.gemini-api-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_SDC_GEMINI_API_KEY</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default language for UI<a href="#module.sdc.language" id="module.sdc.language"></a>

```yaml
BOX_MODULE_SDC_LANGUAGE: "en"
```

Language used as default in Form Builder and Form Renderer

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.language</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td><code>en</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_LANGUAGE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_LANGUAGE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Default form theme<a href="#module.sdc.theme" id="module.sdc.theme"></a>

```yaml
BOX_MODULE_SDC_THEME: "<String>"
```

Theme that will be used in all forms

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.theme</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_THEME</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_THEME</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Hide builder's back button<a href="#module.sdc.builder.hide-back-button" id="module.sdc.builder.hide-back-button"></a>

```yaml
BOX_MODULE_SDC_BUILDER_HIDE_BACK_BUTTON: false
```

Hide back button in UI Form Builder

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.builder.hide-back-button</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_BUILDER_HIDE_BACK_BUTTON</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_BUILDER_HIDE_BACK_BUTTON</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Form's redirect-on-submit URL<a href="#module.sdc.form.redirect-on-submit" id="module.sdc.form.redirect-on-submit"></a>

```yaml
BOX_MODULE_SDC_FORM_REDIRECT_ON_SUBMIT: "<String>"
```

Redirect URI that will be used on form submit/amend button click

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.form.redirect-on-submit</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_FORM_REDIRECT_ON_SUBMIT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_FORM_REDIRECT_ON_SUBMIT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Form's redirect-on-save URL<a href="#module.sdc.form.redirect-on-save" id="module.sdc.form.redirect-on-save"></a>

```yaml
BOX_MODULE_SDC_FORM_REDIRECT_ON_SAVE: "<String>"
```

Redirect URI that will be used on form save/close button click

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.form.redirect-on-save</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_FORM_REDIRECT_ON_SAVE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_FORM_REDIRECT_ON_SAVE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Enable strict access control for sdc operations<a href="#module.sdc.strict-access-control" id="module.sdc.strict-access-control"></a>

```yaml
BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: true
```

Enable strict access control for operations(like populate/submit/reference-lookup) that can request different FHIR resources

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.sdc.strict-access-control</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Recommended value</td><td><code>true</code></td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_SDC_STRICT_ACCESS_CONTROL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_SDC_STRICT_ACCESS_CONTROL</code></td></tr><tr><td>Available from</td><td><code>2506</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## GraphQL

GraphQL settings

### Warmup GraphQL cache on startup<a href="#module.graphql.warmup-on-startup" id="module.graphql.warmup-on-startup"></a>

```yaml
BOX_MODULE_GRAPHQL_WARMUP_ON_STARTUP: false
```

Warmup GraphQL API cache on startup. When false, cache will be warmed up on first request.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.warmup-on-startup</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_WARMUP_ON_STARTUP</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

### Allow reference to any resource<a href="#module.graphql.reference-any" id="module.graphql.reference-any"></a>

```yaml
BOX_MODULE_GRAPHQL_REFERENCE_ANY: false
```

Enable GraphQL API (rev)include for reference with target - any

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.reference-any</code></td></tr><tr><td>Type</td><td>Bool</td></tr><tr><td>Default value</td><td><code>false</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_REFERENCE_ANY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_REFERENCE__ANY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### GraphQL timeout<a href="#module.graphql.timeout" id="module.graphql.timeout"></a>

```yaml
BOX_MODULE_GRAPHQL_TIMEOUT: 60
```

GraphQL API query timeout in seconds: Set to zero to disable.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.timeout</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>60</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_TIMEOUT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_TIMEOUT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### GraphQL access control mode<a href="#module.graphql.access-control" id="module.graphql.access-control"></a>

```yaml
BOX_MODULE_GRAPHQL_ACCESS_CONTROL: "disabled"
```

Access control in GraphQL API

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.graphql.access-control</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>rest-search</code> — Additionally authorization checks access to corresponding search queries<br /><code>disabled</code> — Only access to GraphQL endpoint is verified</td></tr><tr><td>Default value</td><td><code>disabled</code></td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_GRAPHQL_ACCESS_CONTROL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_GRAPHQL_ACCESS__CONTROL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

## Webpush

Webpush settings

### Public Key<a href="#module.webpush.public-key" id="module.webpush.public-key"></a>

```yaml
BOX_MODULE_WEBPUSH_PUBLIC_KEY: "<String>"
```

ECDH/BC/prime256v1 Public Key

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.webpush.public-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_WEBPUSH_PUBLIC_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_WEBPUSH_KEYPAIR_PUBLIC</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### Private Key<a href="#module.webpush.private-key" id="module.webpush.private-key"></a>

```yaml
BOX_MODULE_WEBPUSH_PRIVATE_KEY: "<String>"
```

ECDH/BC/prime256v1 Private Key

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.webpush.private-key</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_WEBPUSH_PRIVATE_KEY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_WEBPUSH_KEYPAIR_PRIVATE</code></td></tr><tr><td>Sensitive</td><td><code>true</code> — value will be masked in Admin UI</td></tr><tr><td>Set via</td><td>Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>

### JWT mail<a href="#module.webpush.jwt-mail" id="module.webpush.jwt-mail"></a>

```yaml
BOX_MODULE_WEBPUSH_JWT_MAIL: "<String>"
```

`JWT.sub` field value needs to be either a URL or a mailto email address.
If a push service needs to reach out to sender, it can find contact information from the JWT.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>module.webpush.jwt-mail</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_MODULE_WEBPUSH_JWT_MAIL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_FEATURES_WEBPUSH_JWT_MAIL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>true</code> — setting can be changed at runtime</td></tr></tbody></table></details>
