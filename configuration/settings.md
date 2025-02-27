# Settings

This page explains how to configure Aidbox using settings. Settings control how your Aidbox instance behaves and can be managed using Aidbox UI and environment variables.

### How settings work

Each setting in Aidbox:

* Has a unique name that identifies it (like `fhir.search.default-params.count`)
* Can be configured through the UI or environment variables
* Includes a description explaining its purpose
* Maps to a specific environment variable name
* May have a default value (for example, `fhir.search.default-params.count` defaults to `100`)

### Setting categories

You can find all settings organized into these categories in the Aidbox UI under the `Settings` tab:

* **General**: Core server configuration options
* **FHIR**: Settings specific to FHIR functionality
* **Security and Access Control**: Authentication, authorization, audit logging settings
* **Modules**: Settings for individual Aidbox modules
* **Web**: HTTP server configuration
* **Database**: Database connection and connection pool settings
* **Observability**: Logging, monitoring, and debugging options
* **Zen**: Legacy settings (Note: Will be removed in July 2025)

### Setting groups

Some settings have special properties:

* **Sensitive Settings**: These contain secure data like passwords and API keys
  * Can only be set using environment variables
  * Example: `security.encrypt-secret`
  * Not editable in the UI
* **Restart Required Settings**: Changes to these require an Aidbox server restart
  * Example: `db.pool.maximum-pool-size`
  * The UI will notify you when a restart is needed

Full details are available in the reference documentation.

### Settings precedence

When Aidbox starts up, it determines setting values in this order:

1. Environment variables (highest priority)
   * Take precedence over all other sources
   * Cannot be changed using the UI
   * Best practice for production environments
2. User-defined settings in UI
   * Applied when no environment variable exists
   * Can be changed using the UI
3. System defaults (lowest priority)
   * Built-in values provided by Aidbox
   * Used when no other value is specified

This hierarchy provides:

* Flexible and quick configuration during development via UI
* Secure, consistent configuration in deployment scripts and CI/CD pipelines via environment variables

### Enable runtime settings editing

A user can enable runtime editing of Aidbox settings using the `settings.mode` in `read-write` value.

Possible values:

* `read-write`: enables editing Aidbox settings using the UI. This mode disables loading configuration from the Aidbox configuration project (zen).
* `read-only`: reads settings values from environment variables and Aidbox settings in read-only mode. This mode disables loading configuration from the Aidbox configuration project (zen).
* `legacy`: reads configuration values from the legacy Aidbox configuration project (zen) in read-only mode. This mode exists for backward compatibility. It will be obsolete in July 2025. Read [more](https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine).

For example, a user can set `BOX_SETTINGS_MODE: read-write` in the environment variables to enable runtime editing of Aidbox settings.

Starting from March 2025 existing instances will use `legacy` mode for backward compatibility. All new instances by default will use `read-write`. Starting from July 2025 `legacy` option will not be available.

### Product-specific settings

In some cases there're specific settings defined in products: Smartbox, Multibox, etc. These settings will have `<product>.<setting>` name format.

### Settings reference documentation

You can find a complete list of settings in the reference documentation
