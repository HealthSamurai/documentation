# 🎓 Migrate to git Aidbox Configuration Projects

November 2022 release of Aidbox comes with a couple of features to reduce the startup time when using FHIR IG packages. First, we prepared a new version for our distribution of those packages which allows for their more efficient load into the Aidbox. Second, for those using configuration projects, it is now possible to remount downloaded IGs.

## How to use new FHIR IG packages?

### If you are using configuration projects

Just update their version so that it is at least 0.6.0. You can see the [latest version on the Releases page](https://github.com/zen-lang/fhir/releases).

### If you are using `AIDBOX_ZEN_PATHS`

1. Remove all `zen-lang/fhir` urls from `AIDBOX_ZEN_PATHS`
2. Add a new environment variable `AIDBOX_ZEN_PACKAGE_LOAD="{:deps {hl7-fhir-us-core \"https://github.com/zen-fhir/hl7-fhir-us-core\"}}"`

`:deps` should list all the IGs that were in `AIDBOX_ZEN_PATHS` but with their URLs changed to a corresponding repository from our [list of packages repositories](https://github.com/orgs/zen-fhir/repositories).

If your `AIDBOX_ZEN_PATHS` looks like

{% code overflow="wrap" %}
```
url:zip:https://github.com/zen-lang/fhir/releases/download/0.5.13/hl7-fhir-us-core.zip,url:zip:https://github.com/zen-lang/fhir/releases/download/0.5.13/hl7-fhir-us-carin-bb.zip
```
{% endcode %}

then`AIDBOX_ZEN_PACKAGE_LOAD` should be equal to

{% code overflow="wrap" %}
```
"{:deps {hl7-fhir-us-core \"https://github.com/zen-fhir/hl7-fhir-us-core\" hl7-fhir-us-carin-bb \"https://github.com/zen-fhir/hl7-fhir-us-carin-bb\"}}"
```
{% endcode %}
