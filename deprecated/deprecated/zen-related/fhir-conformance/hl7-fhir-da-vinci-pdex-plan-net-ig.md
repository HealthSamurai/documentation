---
description: Learn how to enable PDex Plan Net IG
---

# HL7 FHIR Da Vinci PDex Plan Net IG

{% hint style="warning" %}
This tutorial is deprecated. Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[setup.md](../../../../modules/profiling-and-validation/fhir-schema-validator/setup.md "mention")
{% endhint %}

In this tutorial, we'll explain how to configure Aidbox for the [Da Vinci PDex Plan Net Implementation Guide](http://hl7.org/fhir/us/davinci-pdex/toc.html) (PDex IG) and pass Touchstone tests.

{% hint style="info" %}
If you decide to run touchstone test, make sure you run only JSON tests.
{% endhint %}

To comply with the implementation guide you need to load PlanNet profiles and create SearchParameters. Aidbox [registers extensions](https://docs.aidbox.app/modules-1/first-class-extensions) for the loaded profiles on the fly.

To get TouchStone compliant Aidbox you can use configuration from [the Aidbox samples repository](https://github.com/Aidbox/aidbox-project-samples).

## Using the Aidbox samples repository

Clone the repository:

```
git clone https://github.com/Aidbox/aidbox-project-samples.git
```

Copy the `.env.tpl` file as `.env` file and add your base url, Aidbox license key and id to it.

Set up:

```
make plannet-setup
```

Now the Aidbox with the implementation guide loaded is started. Internally it loads PlanNet profiles and search parameters, then loads sample data.

Alternatively you can [load profiles, search parameters and the sample data in your existing Aidbox](https://github.com/Aidbox/aidbox-project-samples#load-plannet-data-sets-to-separate-aidbox-instance).

To load profiles you need to add these variables to your Aidbox environment

```
AIDBOX_ZEN_ENTRYPOINT="hl7-fhir-us-davinci-pdex-plan-net"
AIDBOX_ZEN_PATHS="url:zip:https://github.com/zen-lang/fhir/releases/download/0.2.9/hl7-fhir-us-davinci-pdex-plan-net.zip"
```

To load search parameters and sample data you can use script from the Aidbox samples repo. Change these lines in your .env file

```
AIDBOX_CLIENT_ID=$your_client_id
AIDBOX_CLIENT_SECRET=$your_client_secret
AIDBOX_BASE_URL=$your_aidbox_url
```

Then run

```
make plannet-data-load
```
