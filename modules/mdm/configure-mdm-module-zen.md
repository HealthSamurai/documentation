# Configure MDM module via Zen

{% hint style="info" %} Configuration MDM module via Zen is scheduled for deprecation in several upcoming releases. Please consider using new configuration flow. {% endhint %}

## Add model to Aidbox configuration project

Insert exported symbol to your Aidbox configuration project. And enable an MDM service.

Example:

```
{ns mdmbox

 import #{aidbox.mdm
          aidbox

          zenbox
          zen.fhir}

 patient-mdm-model model-exported-from-splink

 patient-mdm
 {:zen/tags #{aidbox/service}
  :engine aidbox.mdm/splink-engine
  :models #{patient-mdm-model}}

 box
 {:zen/tags #{aidbox/system}
  :services
  {:patient-mdm patient-mdm}}}
```

## References

### Zen symbols

* [aidbox.mdm/model](../../reference/zen-schema-reference/aidbox/mdm/model.md)
* [aidbox.mdm/splink-engine](../../reference/zen-schema-reference/aidbox/mdm/splink-engine.md)
