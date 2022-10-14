---
description: Learn how to enable US Core
---

# 🎓 US Core IG

In this tutorial, we'll explain how to configure Aidbox to use [US Core Implementation Guide](https://www.hl7.org/fhir/us/core/) (US Core IG).

## Support

US Core IG contains the following FHIR artifacts: Profiles, Extensions, Terminology, Search Parameters, Operations and Capability Statements. Supported parts of US Core IG are listed on the following page:

{% content-ref url="us-core-ig-support-reference.md" %}
[us-core-ig-support-reference.md](us-core-ig-support-reference.md)
{% endcontent-ref %}

## Steps

{% hint style="warning" %}
Currently it is only possible to use FHIR IGs for on-premises installations. We are working to add support for other distributions as well.
{% endhint %}

1. First of all, set up Aidbox instance. You can follow this [guide](../../getting-started/run-aidbox-locally-with-docker/).
2. After Aidbox is ready to use, you need to specify US Core dependency.\
   Put the following in `zen-package.edn`:\
   `{:deps {us-core "https://github.com/zen-fhir/hl7-fhir-us-core.git"}}`
3. Go to the file with entrypoint and add `hl7-fhir-us-core` namespace to the `:import`. E.g. if using the example from the guide, the `:import` section should look like this:\
   `:import #{aidbox hl7-fhir-us-core}`
4. Commit changes:\
   `git add zen-package.edn zrc && git commit -m "Add us-core dependency"`

Done. Now you can start Aidbox.

{% hint style="warning" %}
If you put a dependency while Aidbox is running you need to restart it so that it pulls the dependency and consumes it.
{% endhint %}

{% hint style="info" %}
You can read more about providing IGs [here](../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md).&#x20;
{% endhint %}
