---
description: >-
  Enable the US Core Implementation Guide, extend and validate data against this
  profile.
---

# 🎓 Extend an IG with a custom zen profile

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../../contact-us.md) us if you have questions, feedback or suggestions.
{% endhint %}

## Before you start

Install the Aidbox following [this guide](../../getting-started/run-aidbox-locally-with-docker/).

In the `.env` file find the line starting with `AIDBOX_IMAGE` and edit it to be like this if it is not:

{% code title=".env" %}
```bash
AIDBOX_IMAGE=healthsamurai/aidboxone:edge
```
{% endcode %}

## Create a zen project

Inside of the cloned Devbox directory create a zen project directory and open it.\
In this example zen project directory is `my-zen-project/`

```bash
mkdir my-zen-project
cd my-zen-project
```

Inside of your zen project directory initialize npm package for your project and then install zen dependencies you need. FHIR R4 and US core v3 are used as dependencies in this example

```bash
npm init -y
npm install @zen-lang/hl7-fhir-r4-core @zen-lang/hl7-fhir-us-core
```

Create zen entry file in my-zen-project directory.

{% code title="my-zen-aidbox.edn" %}
```yaml
{ns my-zen-aidbox}
```
{% endcode %}

Create namespace with your profiles.

{% code title="my-zen-profiles.edn" %}
```
{ns my-zen-profiles
 import #{hl7-fhir-us-core.us-core-patient}

 MyPatientProfile
 {:zen/tags #{zen/schema zen.fhir/profile-schema}
  :confirms #{hl7-fhir-us-core.us-core-patient/schema}
  :zen.fhir/type "Patient"
  :zen.fhir/profileUri "urn:profile:MyPatientProfile"
  :type zen/map
  :require #{:birthDate}}}
```
{% endcode %}

Import the profiles namespace in the entry namespace.

{% code title="my-zen-devbox.edn" %}
```
{ns my-zen-aidbox
 import #{my-zen-profiles}}
```
{% endcode %}

## Setup Aidbox to use Zen project

Go back to Aidbox directory and add configuration variables to the end of `.env` file:

{% code title=".env" %}
```bash
AIDBOX_ZEN_PROJECT=/my-zen-project # mind the / at the beginning of the dir name
AIDBOX_ZEN_ENTRY=my-zen-aidbox
AIDBOX_ZEN_DEV_MODE=enable # set this variable if you want to have 
                           # zen reload namespaces on the fly as they change
```
{% endcode %}

To enable your Aidbox reading the zen project data add zen project directory volume mount. In the `docker-compose.yaml` file find the section `aidbox:` and add the following line under the `volumes:` section:

```yaml
- "./my-zen-project:/my-zen-project"
```

The `volumes` section should be like this

```yaml
services:
  devbox:
    volumes:
    - "./my-zen-project:/my-zen-project"
```

So the file should look like this:

{% code title="docker-compose.yaml" %}
```yaml
version: '3.7'
services:
  aidbox-db:
    <...omitted...>

  aidbox:
    container_name: "aidbox"
    image: "${AIDBOX_IMAGE}"
    <...omitted...>
    volumes:
    - "./my-zen-project:/my-zen-project"
  <...omitted...>
```
{% endcode %}

## Check if your profile is loaded

Start Aidbox.

```
docker compose up
```

Open Aidbox in your browser and click `Profiles` tab in the left menu:

![](<../../.gitbook/assets/image (88).png>)

You should see the list of zen namespaces loaded.

![](<../../.gitbook/assets/image (90).png>)

{% hint style="info" %}
On this page you see the namespaces that are explicitly included in the zen project or used by Aidbox
{% endhint %}

Open your profile by clicking its name

![](<../../.gitbook/assets/image (91).png>)

## Start validation

Test the data against this profile using **validate** tab

![](<../../.gitbook/assets/image (93).png>)

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

## Example project

[The example project](https://github.com/Aidbox/devbox/commit/431b14170f867f77f90779d4ff870d74c051c844) from this page
