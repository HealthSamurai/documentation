---
description: How to enable new validator engine and specify IGs
---

# Setup

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects. \
\
There's an [existing guide](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md) for this process. Adhere to this guide, <mark style="background-color:green;">but note a variation</mark> when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template-fhir-schema.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

This project is tailored with specific configurations for FHIR Schema Validator.

### Configuration Overview: Key Features and Distinctions

If you already have a configuration project, you can replicate these steps to enable FHIR Schema Validator in your Aidbox instance.

#### Enable the FHIR Schema Validator Engine

To enable the FHIR Schema Validator engine, provide the following variable in your Zen configuration project:

{% code title="zrc/config.edn" %}
```
 features
 {:zen/tags #{aidbox.config/features}
  ...
  :validation {:mode "fhir-schema"}
  ...
  }

```
{% endcode %}

{% hint style="info" %}
Please note that this validation mode suppresses other validation engines and does not cooperate with them
{% endhint %}

#### Enable a Specific Implementation Guide (IG)

To enable a specific IG, list it in the following variable. Separate different packages using a colon (`:`). You can visit the following page to get a complete [list of IGs](supported-implementation-guides.md) supported by the FHIR Schema validator. Package entry template: `<package_name>#<package_version>`.

{% code title="zrc/config.edn" %}
```
 base-config
 {:zen/tags #{aidbox.config/config}
  ...
  :fhir-packages "hl7.fhir.us.core#5.0.1"
  ...
  }

```
{% endcode %}



### External Terminology Server

To validate coded values with an external Terminology server, use the `validate-binding-url` variable by specifying the `ValueSet/$validate-code` endpoint.

{% code title="zrc/config.edn" %}
```
 base-config
 {:zen/tags #{aidbox.config/config}
  ...
  :validate-binding-url "https://tx.fhir.org/r4/ValueSet/$validate-code"
  ...
  }

```
{% endcode %}

