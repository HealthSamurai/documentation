---
description: This guide explains how security label access control can be enabled
---

# How to enable labels-based access control

## Prerequisites

### Docker and Docker Compose

You should have Docker and Docker Compose installed before going further. To get it installed, follow the [instructions](https://docs.docker.com/engine/install/).

## Start Aidbox locally

### 1. Create a directory

```
mkdir aidbox && cd aidbox
```

### 2. Run Aidbox on Docker

```
curl -JO https://aidbox.app/runme && docker compose up
```

### 3. Access Aidbox

Open in browser [http://localhost:8080/](http://localhost:8080/)

### 4. Activate your Aidbox instance

* Using AidboxID AidboxID is a unique identifier within the Aidbox ecosystem used for product activation
* Using Aidbox license Aidbox license can be issued on the [Aidbox user portal](https://aidbox.app/). More about Aidbox licenses [here](../../overview/aidbox-user-portal/licenses.md).

{% hint style="info" %}
If you’re using an Aidbox license, you’ll be prompted for a username (or email) and password. Use the **admin** username and retrieve the password from the **AIDBOX\_ADMIN\_PASSWORD** environment variable in the `docker-compose.yaml` file. For security, Aidbox generates a unique password for each instance.
{% endhint %}

{% hint style="info" %}
See more details related the [running Aidbox locally](../../getting-started/run-aidbox-locally.md)
{% endhint %}

## Enable security labels access control

To enable LBAC, use Aidbox UI -> Settings -> Enable LBAC. You don't have to restart the instance if you use Aidbox UI.\
\
Also, you can update environment variables

{% code title=".env" %}
```ini
# if true, security label feature is enabled
BOX_SECURITY_LBAC_ENABLED=true

# if true, removes security labels from the resource
BOX_SECURITY_LBAC_STRIP_LABELS=true

...
```
{% endcode %}

## Superadmin Role with Label-based Access Control

As mentioned [earlier](how-to-enable-labels-based-access-control.md#resource-level-access-control), resources without security labels cannot be accessed. This can affect the functionality of the Aidbox UI console, making resources like User, Client, Access Policy, etc., inaccessible until they are labeled.\
\
To avoid the need to label all resources displayed in the UI console, use the `superadmin` Role.\
\
Create a `Role` resource with the name `superadmin` and reference to the User used to log in to the UI console before enabling Label-based Access Control.

```yaml
POST /Role
content-type: text/yaml
accept: text/yaml

name: superadmin
user:
  id: <user-id>
  resourceType: User
```

## Ensure the security labels access control works

### Create TokenIntrospector

To make Aidbox trust `JWT` issued by external server token introspection is used, run the following request in the [REST Console](../../overview/aidbox-ui/rest-console.md).

```yaml
PUT /TokenIntrospector/security-labels-demo-client
content-type: text/yaml

resourceType: TokenIntrospector
id: security-labels-demo-client
type: jwt
jwt:
  iss: https://auth.example.com
  secret: secret
```

{% hint style="info" %}
Currently, we use a common secret to make the introspector work. In production installations ,it's better to switch to `jwks_uri` instead.
{% endhint %}

### Create AccessPolicy

This access policy allows `FhirRead` and `FhirSearch` operations for requesters having JWT with `iss` claim value `https://auth.example.com`.

To create the access policy, run the following request in the REST Console.

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

To create the Patient, run the following request in the REST Console.

```yaml
PUT /fhir/Patient/pt-1
content-type: text/yaml

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

To create the Encounter, run the following request in the REST Console.

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

To create the Observation, run the following request in the REST Console.

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

{% code title="Provider" %}
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fFIgaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFBTWSBodHRwOi8vdGVybWlub2xvZ3kuaGw3Lm9yZy9Db2RlU3lzdGVtL3YzLUFjdENvZGV8Q1RDT01QVCJ9.7QZ65gtJPjiWVYjtvtuatvhq6262Sth3z4un_8rDdQg
```
{% endcode %}

{% code title="Finance" %}
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fE0gaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFJFU0NPTVBUIn0.j7WY0I0s2rl6T2Bje1gadRKquuSf-_K9JH1T3T0vvcE
```
{% endcode %}

{% hint style="info" %}
To view the content of a JWT, copy and paste it to [jwt.io](https://jwt.io)
{% endhint %}

### Check resource-level access control works

#### Provider's access to the Patient

{% code title="status: 200 OK" %}
```http
GET /fhir/Patient/pt-1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fFIgaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFBTWSBodHRwOi8vdGVybWlub2xvZ3kuaGw3Lm9yZy9Db2RlU3lzdGVtL3YzLUFjdENvZGV8Q1RDT01QVCJ9.7QZ65gtJPjiWVYjtvtuatvhq6262Sth3z4un_8rDdQg
```
{% endcode %}

Provider has access to the Patient because there is overlap between the Patient labels and the Provider labels.

Patient is labeled with:

* [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`

Provider is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|N`
  * [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

#### Provider's access to the Encounter

{% code title="status: 200 OK" %}
```http
GET /fhir/Encounter/enc-1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fFIgaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFBTWSBodHRwOi8vdGVybWlub2xvZ3kuaGw3Lm9yZy9Db2RlU3lzdGVtL3YzLUFjdENvZGV8Q1RDT01QVCJ9.7QZ65gtJPjiWVYjtvtuatvhq6262Sth3z4un_8rDdQg
```
{% endcode %}

Provider has access to the Encounter because there is overlap between the Encounter labels and the Provider labels.

Encounter is labeled with:

* [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`

Provider is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|N`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

#### Provider's access to the Observation

{% code title="status: 200 OK" %}
```http
GET /fhir/Observation/obs-1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fFIgaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFBTWSBodHRwOi8vdGVybWlub2xvZ3kuaGw3Lm9yZy9Db2RlU3lzdGVtL3YzLUFjdENvZGV8Q1RDT01QVCJ9.7QZ65gtJPjiWVYjtvtuatvhq6262Sth3z4un_8rDdQg
```
{% endcode %}

Provider has access to the Observation because there is overlap between the Observation labels and the Provider labels.

Observation is labeled with:

* [x] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`

Provider is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|N`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [x] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

#### Finance's access to the Patient

{% code title="status: 200 OK" %}
```http
GET /fhir/Patient/pt-1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fE0gaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFJFU0NPTVBUIn0.j7WY0I0s2rl6T2Bje1gadRKquuSf-_K9JH1T3T0vvcE
```
{% endcode %}

Finance has access to the Patient because there is overlap between the Patient labels and the Finance labels.

Patient is labeled with:

* [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`

Finance is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M` expands to:
  * [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|RESCOMPT`

#### Finance's access to the Encounter

{% code title="status: 200 OK" %}
```http
GET /fhir/Encounter/enc-1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fE0gaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFJFU0NPTVBUIn0.j7WY0I0s2rl6T2Bje1gadRKquuSf-_K9JH1T3T0vvcE
```
{% endcode %}

Finance has access to the Encounter because there is overlap between the Encounter labels and the Finance labels.

Encounter is labeled with:

* [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`

Finance is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [x] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|RESCOMPT`

{% code title="status: 403 Forbidden" %}
```http
GET /fhir/Observation/obs-1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJzY29wZSI6Imh0dHA6Ly90ZXJtaW5vbG9neS5obDcub3JnL0NvZGVTeXN0ZW0vdjMtQ29uZmlkZW50aWFsaXR5fE0gaHR0cDovL3Rlcm1pbm9sb2d5LmhsNy5vcmcvQ29kZVN5c3RlbS92My1BY3RDb2RlfFJFU0NPTVBUIn0.j7WY0I0s2rl6T2Bje1gadRKquuSf-_K9JH1T3T0vvcE
```
{% endcode %}

Finance does not have access to the Observation because there is no overlap between the Observation labels and the Finance labels.

Observation is labeled with:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`

Finance is only allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|RESCOMPT`

### Check resource-element access control works

#### Provider

{% tabs %}
{% tab title="Encounter" %}
Provider has access to all the fields within the Encounter resource.

`Encounter.subject` is labeled:

* [x] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

Provider is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|N`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`
* [x] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

```yaml
class:
  code: IMP
  system: 'http://terminology.hl7.org/CodeSystem/v3-ActCode'
id: enc-1
meta:
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2024-02-27T15:06:00.455006Z'
  lastUpdated: '2024-02-27T15:06:00.455006Z'
  versionId: '107'
resourceType: Encounter
status: finished
subject:
  reference: Patient/pt-1
```
{% endtab %}

{% tab title="Patient" %}
Provider has no access the identifier of the Patient.

Patient.identifier is labeled:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|FMCOMPT`

Provider is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|N`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

```yaml
gender: male
id: pt-1
identifier:
  - extension:
      - url: 'http://terminology.hl7.org/CodeSystem/data-absent-reason'
        valueCode: masked
meta:
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2024-02-26T16:51:38.264449Z'
  lastUpdated: '2024-02-27T13:51:59.700672Z'
  versionId: '100'
name:
  - family: Chalmers
    given:
      - Peter
      - James
    use: official
resourceType: Patient
```
{% endtab %}
{% endtabs %}

#### Finance

{% tabs %}
{% tab title="Encounter" %}
Finance has access to all the fields within the Encounter resource but to the subject.

`Encounter.subject` is labeled:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

Finance is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|RESCOMPT`

```yaml
class:
  code: IMP
  system: 'http://terminology.hl7.org/CodeSystem/v3-ActCode'
id: enc-1
meta:
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2024-02-27T15:06:00.455006Z'
  lastUpdated: '2024-02-27T15:06:00.455006Z'
  versionId: '107'
resourceType: Encounter
status: finished
subject:
  extension:
    - url: 'http://terminology.hl7.org/CodeSystem/data-absent-reason'
      valueCode: masked
```
{% endtab %}

{% tab title="Patient" %}
Finance has no access the `identifier` and `name` of the Patient.

`Patient.identifier` is labeled:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|FMCOMPT`

`Patient.name` is labeled:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|CTCOMPT`

Finance is allowed:

* [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M` expands to:
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|M`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|L`
  * [ ] `http://terminology.hl7.org/CodeSystem/v3-Confidentiality|U`
* [ ] `http://terminology.hl7.org/CodeSystem/v3-ActCode|RESCOMPT`

```yaml
gender: male
id: pt-1
identifier:
  - extension:
      - url: 'http://terminology.hl7.org/CodeSystem/data-absent-reason'
        valueCode: masked
meta:
  extension:
    - url: 'ex:createdAt'
      valueInstant: '2024-02-26T16:51:38.264449Z'
  lastUpdated: '2024-02-27T13:51:59.700672Z'
  versionId: '100'
name:
  - extension:
      - url: 'http://terminology.hl7.org/CodeSystem/data-absent-reason'
        valueCode: masked
resourceType: Patient
```
{% endtab %}
{% endtabs %}
