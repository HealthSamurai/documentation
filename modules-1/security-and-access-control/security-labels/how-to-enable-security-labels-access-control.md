---
description: This guide explains how security label access control can be enabled
---

# How to enable security labels access control

## Prerequisites

### Docker and Docker Compose

You should have Docker and Docker Compose installed before go further. To get it installed follow the [instructions](https://docs.docker.com/engine/install/).&#x20;

### Aidbox license

To get the Aidbox License:

1. Go the Aidbox user portal [https://aidbox.app](https://aidbox.app/)
2. Login to the portal
3. Create new **self-hosted** Aidbox License or use the license that you already have

## Create Aidbox project

To create sample project run command below.

```shell
git clone \
  --branch=without-zen \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

{% hint style="info" %}
See more details related the [running Aidbox locally](broken-reference)
{% endhint %}

### Apply the license

Populate the `.env` file with the Aidbox License.&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY
...
```
{% endcode %}

## Enable security labels access control

Populate the `.env` file with the security labels ENVs.&#x20;

{% code title=".env" %}
```ini
# if true, security label feature is enabled
BOX_FEATURES_SECURITY__LABELS_ENABLE=true

# if true, removes security labels from the resource
BOX_FEATURES_SECURITY__LABELS_STRIP__LABELS=true
...
```
{% endcode %}

## Start Aidbox with Docker Compose

To start Aidbox run the command in the `aidbox-project` directory.

```bash
docker compose up --force-recreate
```

When Aidbox starts, navigate to the [http://localhost:8888](http://localhost:8888/) and sign in to the Aidbox UI using the credentials `admin` / `password`.

## Ensure the security labels access control works

### Create TokenIntrospector

To make Aidbox trust `JWT` issued by external server token introspection is used.

```yaml
PUT /TokenIntrospector/security-labels-demo
content-type: text/yaml

resourceType: TokenIntrospector
id: security-labels-demo-client
type: jwt
jwt:
  iss: https://auth.example.com
  secret: secret
```

{% hint style="info" %}
Currently we use a common secret to make the introspector works. In production installations it's better to switch to `jwks_uri` instead.
{% endhint %}

### Create AccessPolicy

This access policy allows `FhirRead` and `FhirSearch` operations for requesters having JWT with `iss` claim value `https://auth.example.com`.

```yaml
PUT /AccessPolicy/as-security-labels-demo-client-do-read-search
content-type: text/yaml

resourceType: AccessPolicy
id: as-security-labels-demo-client-do-read-search
link:
- resourceType: Operation
  id: FhirRead
- resourceType: Operation
  id: FhirSearch
engine: matcho
matcho:
  jwt:
    iss: https://auth.example.com
```

### Populate data samples

#### Create Patient resource

```yaml
meta:
  security:
    - code: PROCESSINLINELABEL
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    - code: M
      system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
name:
  - use: official
    given:
      - Peter
      - James
    family: Chalmers
    extension:
      - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
        valueCoding:
          code: CTCOMPT
          system: http://terminology.hl7.org/CodeSystem/v3-ActCode
          display: care team compartment
gender: male
_gender:
  extension:
    - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
      valueCoding:
        code: RESCOMPT
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: research project compartment
    - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
      valueCoding:
        code: CTCOMPT
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: care team compartment
identifier:
  - use: usual
    type:
      coding:
        - code: MR
          system: http://terminology.hl7.org/CodeSystem/v2-0203
    value: Z12345
    system: urn:oid:1.2.36.146.595.217.0.1
    extension:
      - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
        valueCoding:
          code: FMCOMPT
          system: http://terminology.hl7.org/CodeSystem/v3-ActCode
          display: financial management compartment
id: pt-1
resourceType: Patient
```

#### Create Encounter resource

```yaml
PUT /fhir/Encounter/enc-1
content-type: text/yaml

resourceType: Encounter
id: enc-1
meta:
  security:
    - code: PROCESSINLINELABEL
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    - code: L
      system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
status: finished
class:
  system: http://terminology.hl7.org/CodeSystem/v3-ActCode
  code: IMP
subject:
  reference: "Patient/pt-1"
  extension:
    - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
      valueCoding:
        code: CTCOMPT
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: care team compartment
```

#### Create Observation resource

```yaml
PUT /fhir/Observation/obs-1
content-type: text/yaml

resourceType: Observation
id: obs-1
meta:
  security:
    - code: PROCESSINLINELABEL
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    - code: PSY
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
status: final
code:
  coding:
  - system: http://loinc.org
    code: 15074-8
subject:
  reference: "Patient/pt-1"
```

### JWT for requests

There are two users and two JWTs that we will use:

{% code title="Provider's JWT" %}
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fFIgaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFBTWSBodHRwOi8vdGVybWlub2xvZ3kuaGw3Lm9yZy9Db2RlU3lzdGVtL3YzLUFjdENvZGV8Q1RDT01QVCJ9.7QZ65gtJPjiWVYjtvtuatvhq6262Sth3z4un_8rDdQg
```
{% endcode %}

{% code title="Finance's JWT" %}
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fE0gaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFJFU0NPTVBUIn0.j7WY0I0s2rl6T2Bje1gadRKquuSf-_K9JH1T3T0vvcE
```
{% endcode %}

{% hint style="info" %}
To see the content of the JWTs copy a JWT and paste to the [jwt.io](https://jwt.io/)
{% endhint %}
