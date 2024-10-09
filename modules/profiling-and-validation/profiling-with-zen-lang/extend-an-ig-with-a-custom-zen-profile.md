---
description: >-
  Enable the US Core Implementation Guide, extend it with profile and validate
  data against it
---

# 🎓 Load zen profiles into Aidbox

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](https://docs.aidbox.app/modules-1/profiling-and-validation/fhir-schema-validator)
{% endhint %}

## Prerequisites

Install the Aidbox following [this guide](../../../getting-started/run-aidbox-locally-with-docker/).

In the `.env` file find the line starting with `AIDBOX_IMAGE` and edit it to be like this if it is not:

{% code title=".env" %}
```bash
AIDBOX_IMAGE=healthsamurai/aidboxone:edge
```
{% endcode %}

## Start with a zen project

The Aidbox starter repository you cloned by following the guide from [Prerequisites](extend-an-ig-with-a-custom-zen-profile.md#prerequisites) is already a zen project. It has `zen-package.edn`, the project package file, and `zrc/system.edn`, the project entry file.

## Add the required IG packages as dependencies

Update `zen-package.edn` so that it reflects information about the new project’ dependencies.

```clojure
;; zen-package.edn
{:deps {hl7-fhir-r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"
        hl7-fhir-us-core "https://github.com/zen-fhir/hl7-fhir-us-core.git"}}
```

A dependency is any other zen project. It is common to include FHIR IG packages this way. We provide a number of [prepackaged FHIR IGs](https://github.com/orgs/zen-fhir/repositories) which you can use similarly to the example above.

You also need to update an entry file, `zrc/system.edn`. It is used, among other things, for importing and loading all the project’s files, including its dependencies.

```clojure
;; zrc/system.edn
{ns system
 import #{aidbox
          aidbox.config
          hl7-fhir-r4-core
          hl7-fhir-us-core}
 …}
```

## Create a namespace with your profiles

```clojure
;; zrc/my-profiles.edn
{ns my-profiles
 import #{hl7-fhir-us-core.us-core-patient}

 MyPatientProfile
 {:zen/tags #{zen/schema zen.fhir/profile-schema}
  ;; :confirms is needed only if you want to extend an existing profile
  :confirms #{hl7-fhir-us-core.us-core-patient/schema}
  :zen.fhir/type "Patient"
  :zen.fhir/profileUri "urn:profile:MyPatientProfile"
  :type zen/map
  :require #{:birthDate}}}
```

Add `my-profiles` namespace to the entry file imports.

```clojure
;; zrc/system.edn
{ns system
 import #{aidbox
          aidbox.config
          hl7-fhir-r4-core
          hl7-fhir-us-core
          my-profiles}
 …}
```

{% hint style="info" %}
Refer to [Aidbox Configuration project page](../../../aidbox-configuration/aidbox-zen-lang-project/) if you want to learn more about zen projects.
{% endhint %}

## Setup Aidbox for development with zen projects

Add `AIDBOX_ZEN_DEV_MODE=enable` to your`.env` file:

{% code title=".env" %}
```bash
AIDBOX_ZEN_DEV_MODE=enable
```
{% endcode %}

Now Aidbox will automatically reload when changes are made in the project. Note that this feature is a work in progress and some things may not reload properly.

## Check if your profile is loaded

Start Aidbox.

```
docker compose up
```

Open Aidbox in your browser and click `Profiles` tab in the left menu:

![](<../../../.gitbook/assets/image (88).png>)

You should see the list of zen namespaces loaded.

![](<../../../.gitbook/assets/image (90).png>)

{% hint style="info" %}
On this page you see the namespaces that are explicitly included in the zen project or used by Aidbox
{% endhint %}

Open your profile by clicking its name

![](<../../../.gitbook/assets/image (91) (1).png>)

## Start validation

Test the data against this profile using **validate** tab

![](<../../../.gitbook/assets/image (93).png>)

If your profile is tagged `zen.fhir/profile-schema` it can be used to validate your data\
On FHIR CRUD API requests a profile will be applied if data includes `:zen.fhir/profileUri` in the `meta.profile` attribute:

{% tabs %}
{% tab title="Request" %}
{% code title="Data is missing `birthDate` attribute, required by the profile" %}
```http
POST /Patient
Content-Type: text/yaml

name: [{given: [Harry], family: Potter}]
meta: {profile: ["urn:profile:MyPatientProfile"]}
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
{% code title="An error has been returned" %}
```yaml
# status 422
resourceType: OperationOutcome
text:
  status: generated
  div: Invalid resource
issue:
  - severity: fatal
    code: invalid
    expression:
      - Patient.birthDate
    diagnostics: ':birthDate is required'
```
{% endcode %}
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}
{% code title="Data contains `birthDate` required by profile" %}
```http
POST /Patient
Content-Type: text/yaml

name: [{given: [Harry], family: Potter}]
birthDate: "1980-07-31"
meta: {profile: ["urn:profile:MyPatientProfile"]}
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
```yaml
# status 201

id: 20fdde9d-ede6-4edb-a16a-278ddfeef952
resourceType: Patient
name:
  - given:
      - Harry
    family: Potter
birthDate: '1980-07-31'
meta:
  profile:
    - urn:profile:MyPatientProfile
  lastUpdated: '2021-09-03T14:40:11.300957Z'
  createdAt: '2021-09-03T14:40:11.300957Z'
  versionId: '57'
```
{% endtab %}
{% endtabs %}

## Development and production tips

If you want more tips about development and production usage, visit the links below:

* [Development tips](../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-local-development)
* [Production tips](../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md#tips-for-production)

## Example project

See [the example project](https://github.com/Aidbox/devbox/commit/431b14170f867f77f90779d4ff870d74c051c844) used on this page.
