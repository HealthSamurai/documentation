# aidbox.config/config

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[setup-aidbox-with-fhir-schema-validation-engine.md](../../../../../modules/profiling-and-validation/fhir-schema-validator/setup-aidbox-with-fhir-schema-validation-engine.md)
{% endhint %}

Config for AidboxOne product (devbox)- `:db` (zen/symbol)

zen/tag: [aidbox.config/db](aidbox-config-config.md#aidbox.config-db)

* `:aidbox-license` (zen/string)
* `:disable-legacy-seed` (zen/boolean)
*   `:project` (zen/map)

    Aidbox project configuration

    *   `:git` (zen/map)

        Git repository configuration

        * `:protocol` (zen/string)
        * `:public-key` (zen/string)
        * `:private-key` (zen/string)
        * `:url` (zen/string)
        *   `:sub-path` (zen/string)

            To specify path to zen directory related to the root
        *   `:access-token` (zen/string)

            Token to access HTTPS private repository
        *   `:target-path` (zen/string)

            Clone the repository into a directory
        *   `:checkout` (zen/string)

            Git branch or commit
    * `:entrypoint` (zen/string)
*   `:replication` (zen/symbol)

    zen/tag: [aidbox.config/replication](aidbox-config-config.md#aidbox.config-replication)
* `:apm` (zen/map)
  * `:disable` (zen/boolean)
  *   `:server` (zen/string)

      APM Server
* `:telemetry` (zen/map)
  *   `:usage-stats` (zen/boolean)

      Allow/disallow to send usage statistic
  *   `:errors` (zen/boolean)

      Allow/disallow to send errors
*   `:entrypoint` (zen/symbol)

    aidbox project entrypoint /
* `:instances-number` (zen/integer)
* `:stdout-pretty` (zen/boolean)
* `:box-id` (zen/string)
*   `:web` (zen/symbol)

    zen/tag: [aidbox.config/web](aidbox-config-config.md#aidbox.config-web)

## aidbox.config/db

## aidbox.config/replication

* `:instances` (zen/integer)
* `:port` (zen/integer)
*   `:kube` (zen/map)

    Kubernetes meta

    * `:service-name` (zen/string)
    *   `:namespace` (zen/string)

        Kubernetes namespace
    *   `:hostname` (zen/string)

        Kubernetes hostname. Deduce instance number

## aidbox.config/web

*   `:base-url` (zen/string)

    Base url of the Aidbox instance
* `:port` (zen/integer)
* `:thread` (zen/integer)
* `:max-body` (zen/integer)
* `:max-line` (zen/integer)
*   `:request-save-raw-body` (zen/boolean)

    Attach raw body to response
