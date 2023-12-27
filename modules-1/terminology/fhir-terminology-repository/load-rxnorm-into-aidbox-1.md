---
description: >-
  Load all official versions of vocabulary value sets provided by the Value Set
  Authority Center (VSAC) at the National Library of Medicine (NLM)
---

# Load US VSAC Package into Aidbox

{% hint style="warning" %}
The current package is a pre-built version of the VSAC contents 0.7.0 (2022-06-11). Please [reach out to us](../../../contact-us.md) if you need a newer version.
{% endhint %}

## How to set up Aidbox with VSAC value sets

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects.&#x20;

{% hint style="info" %}
Please check the [existing guide](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md) that explains how to run Aidbox locally      &#x20;
{% endhint %}

```sh
git clone \
  https://github.com/Aidbox/aidbox-project-template \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

### Add VSAC dependency to the configuration project

{% code title="zen-package.edn" %}
```
{:deps { vsac "https://github.com/zen-fhir/us-nlm-vsac.git"}}
```
{% endcode %}

### Import VSAC namespace to configuration project entrypoint

```
{ns main
 import #{aidbox
          config
          us-nlm-vsac}
}
```

### Start Aidbox with Docker Compose

```shell
docker compose up --force-recreate
```

Navigate to [http://localhost:8888/](http://localhost:8888/) and Sign In to the Aidbox UI using the login `admin` and password `password`.

