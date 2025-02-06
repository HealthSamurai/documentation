# splink-engine

{% hint style="info" %} Configuration MDM module via Zen is scheduled for deprecation in several upcoming releases. Please consider using new configuration flow. {% endhint %}

Aidbox service engine for MDM module. This engine is compatible with splink models.

## Schema

```
 splink-engine
 {:zen/tags #{aidbox/service-engine zen/schema}
  :type zen/map
  :keys {:models {:type zen/set
                  :every {:type zen/symbol
                          :tags #{aidbox.mdm/model}}}}}
```

## Examples

```
 patient-mdm
 {:zen/tags #{aidbox/service}
  :engine aidbox.mdm/splink-engine
  :models #{patient-mdm-model}}
```

