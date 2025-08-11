---
description: Form's access control
---

# Access Control in Forms

Form's module access control can be set via aidbox [ACL engine](../../deprecated/deprecated/zen-related/access-control-lists-docs-acl.md)

## SDC Roles Access

SDC module suggests several roles which can be used independently or in a mix.

For **DEVELOPMENT** and configuration simplicity - it's better to use role with full access.

* **sdc admin** - full access

For **PRODUCTION** it's better to have separate roles, with more precise access patterns.

For example we can split users into 3 groups.

* **form designer** - creates and manages forms
* **form filler** - end user which filling the form
* **response manager** - reviews responses + populates new forms

### SDC Admin

Policies:

* Forms Grid
* CRUD on all SDC resources (Questionnaire/QuestionnaireResponse/QuestionnaireTheme/SDCConfig/SDCPrintTemplate)
* CRUD on production resources (Patient/Encounter/Observation resources)
* all SDC operations
* terminology related endpoints

| policy                                   | policy description                                                                      |
| ---------------------------------------- | --------------------------------------------------------------------------------------- |
| as-sdc-admin-manage-sdc-resources        | Create/Update/Delete resources used in SDC Module                                       |
| as-sdc-admin-manage-production-resources | Create/Update/Delete SDC related resources (Patient/Encounter/Observation/Practitioner) |
| as-sdc-admin-use-sdc-operations          | Use all SDC operations                                                                  |
| as-sdc-admin-use-terminology-operations  | Use terminology operations (Search concepts, Valuesets) )                               |


**as-sdc-admin-manage-sdc-resources policy**

CRUD access to next resources:

* Questionnaire
* QuestionnaireResponse
* QuestionnaireTheme
* SDCPrintTemplate
* SDCConfig

```yaml
PUT /AccessPolicy/as-sdc-admin-manage-sdc-resources
content-type: text/yaml
accept: text/yaml

id: as-sdc-admin-manage-sdc-resources
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-admin
  uri:
   $one-of:
      - '#/Questionnaire/.*$'
      - '#/Questionnaire$'
      - '#/QuestionnaireResponse/.*$'
      - '#/QuestionnaireResponse$'
      - '#/QuestionnaireTheme/.*$'
      - '#/QuestionnaireTheme$'
      - '#/SDCPrintTemplate/.*$'
      - '#/SDCPrintTemplate$'
      - '#/SDCConfig/.*$'
      - '#/SDCConfig$'
  request-method:
     $one-of:
       - get
       - post
       - put
       - delete
       - patch
```

**as-sdc-admin-manage-production-fhir-resources policy**

CRUD access to next FHIR resources:

* Patient
* Encouner
* Observation
* Organization
* Practitioner

> This is typical resources that often used in SDC Flow. But you are free to add your own.

```yaml
PUT /AccessPolicy/as-sdc-admin-manage-production-fhir-resources
content-type: text/yaml
accept: text/yaml

id: as-sdc-admin-manage-production-fhir-resources
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-admin
  uri:
   $one-of:
      - '#/Patient/.*$'
      - '#/Patient$'
      - '#/Encounter/.*$'
      - '#/Encounter$'
      - '#/Observation/.*$'
      - '#/Observation$'
      - '#/Organization/.*$'
      - '#/Organization$'
      - '#/Practitioner/.*$'
      - '#/Practitioner$'
  request-method:
     $one-of:
       - get
       - post
       - put
       - delete
       - patch
```

**as-sdc-admin-use-sdc-operations policy**

```yaml
PUT /AccessPolicy/as-sdc-admin-use-sdc-operations
content-type: text/yaml
accept: text/yaml

id: as-sdc-admin-use-sdc-operations
resourceType: AccessPolicy
engine: matcho
matcho:
  uri:
    $one-of:
    - '#\$save'
    - '#\$generate-link'
    - '#\$duplicate'
    - '#\$sdc-config'
    - '#\$process-response'
    - '#\$validate'
    - '#\$extract'
    - '#\$populate'
    - '#\$render'
    - '#\$submit'
    - '#\$usage'
    - '#\$assemble-all'
    - '#\$expand'
    - '#\$validate-response'
    - '#\$populatelink'
    - '#\$sdc-file'
    - '#\$generate-token'
    - '#\$assemble'
    - '#\$sdc-resource-types'
    - '#\$ai-generate-questionnaire'
    - '#\$openai-chat-completions'
    - '#\$sdc-resource-types'
    - '#\$sdc-resource-schema'
    - '#\$reference-lookup'
  request-method:
     $one-of:
       - post
       - get
       - put
       - delete
       - patch
```

**as-sdc-admin-use-terminology-operations policy**

Searching for ValueSets and concepts

```yaml
PUT /AccessPolicy/as-sdc-admin-use-terminology-operations
content-type: text/yaml
accept: text/yaml

id: as-sdc-admin-use-terminology-operations
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-admin
  uri:
    $one-of:
      - '#/ValueSet$'
      - '#/ValueSet/\$expand$'
  request-method:
    $one-of:
      - get
      - post
```

### Form Designer

This role give access for

* Forms Grid
* Form Builder
* Patient's and Encouner resources for populate purposes

Policies:

| policy                                                   | policy description                                    |
| -------------------------------------------------------- | ----------------------------------------------------- |
| as-sdc-form-designer-forms-grid-rpc                      | use forms grid                                        |
| as-sdc-form-designer-read-config                         | read SDCConfig                                        |
| as-sdc-form-designer-manage-themes                       | manage themes                                         |
| as-sdc-form-designer-manage-questionnaire                | Create/Read/Update/Delete Questionnaire               |
| as-sdc-form-designer-populate-questionnaire              | validate Questionnaire + QuestionnaireResponse        |
| as-sdc-form-designer-extract-questionnaire               | populate Questionnaire (+ search patient & encounter) |
| as-sdc-form-designer-validate-questionnaire-and-response | extract QuestionnaireResponse                         |
| as-sdc-form-designer-search-valueset                     | search for valuesets                                  |
| as-sdc-form-designer-search-concepts                     | search for concepts                                   |
| as-sdc-form-designer-search-references                   | search for references                                 |
| as-sdc-form-designer-use-ai-tools                        | use AI tools                                          |
| as-sdc-form-designer-get-fhir-metadata                   | Get FHIR metadata to support Template resource editor |

**as-sdc-form-designer-forms-grid-rpc policy**

grid with Questionnaires

```yaml
PUT /AccessPolicy/as-sdc-form-designer-forms-grid-rpc
content-type: text/yaml
accept: text/yaml

resourceType: AccessPolicy
id: as-sdc-form-designer-forms-grid-rpc
type: rpc
engine: matcho-rpc
rpc:
 aidbox.sdc.grid/get-definition:
   user:
     roles:
       $contains:
         value: sdc-form-designer

 aidbox.sdc.patient/forms-grid:
   user:
     roles:
       $contains:
         value: sdc-form-designer
```

**as-sdc-form-designer-read-config policy**

Access to configuration

```yaml
PUT /AccessPolicy/as-sdc-form-designer-read-config
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-read-config
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '/$sdc-config'
  request-method: post
```

**as-sdc-form-designer-manage-questionnaire policy**

All operations for manaings and retrieving Questionnaire

```yaml
PUT /AccessPolicy/as-sdc-form-designer-manage-questionnaire
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-manage-questionnaire
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri:
    $one-of:
    - '#/Questionnaire$'
    - '#/Questionnaire/.*$'
    - '#/Questionnaire/\$save'
    - '#/Questionnaire/.*/\$usage'
    - '#/Questionnaire/.*/\$duplicate'
  request-method:
    $one-of:
      - get
      - post
      - put
      - delete
```

**as-sdc-form-designer-search-response policy**

Searching for QuestionnaireResponses

> Used for checking Questionnaire usage

```yaml
PUT /AccessPolicy/as-sdc-form-designer-search-response
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-search-response
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#/QuestionnaireResponse$'
  request-method: get
```

**as-sdc-form-designer-validate-questionnaire-and-response policy**

Validate Questionnaire and QuestionnaireResponse

```yaml
PUT /AccessPolicy/as-sdc-form-designer-validate-questionnaire-and-response
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-validate-questionnaire-and-response
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri:
    $one-of:
      - '#/Questionnaire/\$validate'
      - '#/QuestionnaireResponse/\$validate'
  request-method: post
```

**as-sdc-form-designer-manage-themes policy**

Retrive and manage Questionnaire themes

```yaml
PUT /AccessPolicy/as-sdc-form-designer-manage-themes
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-manage-themes
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri:
    $one-of:
      - '#/QuestionnaireTheme$'
      - '#/QuestionnaireTheme/.*'
  request-method:
    $one-of:
      - get
      - post
      - put
      - delete
```

**as-sdc-form-designer-search-patient-and-encounter-for-populate policy**

Search for Patient and Encounter

> Used for populate debug console

```yaml
PUT /AccessPolicy/as-sdc-form-designer-search-patient-and-encounter-for-populate
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-search-patient-and-encounter-for-populate
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri:
    $one-of:
      - '#/Encounter$'
      - '#/Patient$'
  request-method: get
```

**as-sdc-form-designer-populate-questionnaire policy**

Test populate in debug console

```yaml
PUT /AccessPolicy/as-sdc-form-designer-populate-questionnaire
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-populate-questionnaire
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#/Questionnaire/\$populate$'
  request-method: post
```

**as-sdc-form-designer-extract-questionnaire policy**

Test extraction in Debug console

```yaml
PUT /AccessPolicy/as-sdc-form-designer-extract-questionnaire
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-extract-questionnaire
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#/QuestionnaireResponse/\$extract$'
  request-method: post
```

**as-sdc-form-designer-search-valueset policy**

Search for valuesets

```yaml
PUT /AccessPolicy/as-sdc-form-designer-search-valueset
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-search-valueset
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#/ValueSet$'
  request-method: get
```

**as-sdc-form-designer-search-concepts policy**

Search for concepts

> Used for importing concepts

```yaml
PUT /AccessPolicy/as-sdc-form-designer-search-concepts
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-search-concepts
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#/ValueSet/\$expand$'
  request-method:
    $one-of:
      - get
      - post
```

**as-sdc-form-designer-search-references**


```yaml
PUT /AccessPolicy/as-sdc-form-designer-search-references
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-search-references
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#\$reference-lookup$'
  request-method:
    $one-of:
      - get
      - post
```

**as-sdc-form-designer-use-ai-tools policy**

Generate Questionnaire from PDF

```yaml
PUT /AccessPolicy/as-sdc-form-designer-use-ai-tools
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-use-ai-tools
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri:
    $one-of:
    - '/$ai-generate-questionnaire'
    - '/$openai-chat-completions'
  request-method: post
```

**as-sdc-form-designer-get-fhir-metadata**

Get FHIR metadata about Resources and their schemas

```yaml
PUT /AccessPolicy/as-sdc-form-designer-get-fhir-metadata
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-designer-get-fhir-metadata
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri:
    $one-of:
    - '#\$sdc-resource-types'
    - '#\$sdc-resource-schema'
  request-method: get
```

### Form Filler

Form filler role can load Questionnaire and QuestionnaireResponse, fill and submit it

| policy                                | policy description                          |
| ------------------------------------- | ------------------------------------------- |
| as-sdc-form-filler-read-config        | Read SDCConfig                              |
| as-sdc-form-filler-read-questionnaire | Read Questionnaire and use it for rendering |
| as-sdc-form-filler-read-response      | Read saved resposne and render it           |
| as-sdc-form-filler-save-response      | Save changed response                       |
| as-sdc-form-filler-submit-response    | Submit response                             |
| as-sdc-form-filler-search-concepts    | Search terminology concepts                 |
| as-sdc-form-filler-search-references  | Search for references                       |

**as-sdc-form-filler-read-config policy**

Read configuration

```yaml
PUT /AccessPolicy/as-sdc-form-filler-read-config
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-read-config
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-filler
  uri: '/$sdc-config'
  request-method: post
```

**as-sdc-form-filler-read-response policy**

Read QuestionnaireResponse

```yaml
PUT /AccessPolicy/as-sdc-form-filler-read-response
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-read-response
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-filler
  uri: '#/QuestionnaireResponse/.*'
  request-method: get
```

**as-sdc-form-filler-read-questionnaire policy**

Read Questionnaire

```yaml
PUT /AccessPolicy/as-sdc-form-filler-read-questionnaire
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-read-questionnaire
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-filler
  uri: '#/Questionnaire/$'
  request-method: get
```

**as-sdc-form-filler-save-response policy**

Save QuestionnaireResponse

```yaml
PUT /AccessPolicy/as-sdc-form-filler-save-response
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-save-response
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-filler
  uri: '#/QuestionnaireResponse/\$save'
  request-method:  post
```

**as-sdc-form-filler-submit-response policy**

Submit QuestionnaireResponse

```yaml
PUT /AccessPolicy/as-sdc-form-filler-submit-response
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-submit-response
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-filler
  uri: '#/QuestionnaireResponse/\$submit'
  request-method:  post
```

**as-sdc-form-filler-search-concepts policy**

Search for concepts

> Used in choice items with attached valueset

```yaml
PUT /AccessPolicy/as-sdc-form-filler-search-concepts
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-search-concepts
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-filler
  uri: '#/ValueSet/\$expand'
  request-method:
    $one-of:
       - post
       - get
```

**as-sdc-form-filler-search-references policy**

Search for references

> Used in reference items with attached resourceType

```yaml
PUT /AccessPolicy/as-sdc-form-filler-search-references
content-type: text/yaml
accept: text/yaml

id: as-sdc-form-filler-search-references
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-form-designer
  uri: '#\$reference-lookup$'
  request-method:
    $one-of:
      - get
```

### Response Manager

Response manager role has access to

* Forms grid
* Responses grid
* Read responses
* Questionnaire population
* shared link generation

| policy                                               | policy description                                    |
| ---------------------------------------------------- | ----------------------------------------------------- |
| as-sdc-response-manager-forms-grid-rpc               | Forms grid with forms and responses                   |
| as-sdc-response-manager-search-config                | Search SDCConfigs before populate                     |
| as-sdc-response-manager-read-config                  | Read SDCConfig                                        |
| as-sdc-response-manager-search-and-read-theme        | Search and Read QuestionnaireTheme                    |
| as-sdc-response-manager-read-questionnaire           | Read Quetionnaire for opening forms                   |
| as-sdc-response-manager-read-response                | Read QuestionnaireResponse for looking into responses |
| as-sdc-response-manager-search-patient-and-encounter | Search for Encounters and Patients                    |
| as-sdc-response-manager-populate-questionnaire       | Create new empty/prefilled responses                  |
| as-sdc-response-manager-generate-link                | Create access link for response                       |

**as-sdc-response-manager-forms-grid-rpc policy**

Forms grid with

* Questionnaires
* QuestionnaireResponses

```yaml
PUT /AccessPolicy/as-sdc-response-manager-forms-grid-rpc
content-type: text/yaml
accept: text/yaml

resourceType: AccessPolicy
id: as-sdc-response-manager-forms-grid-rpc
type: rpc
engine: matcho-rpc
rpc:
 aidbox.sdc.grid/get-definition:
   user:
     roles:
       $contains:
         value: sdc-response-manager

 aidbox.sdc.patient/forms-grid:
   user:
     roles:
       $contains:
         value: sdc-response-manager

 aidbox.sdc.patient/documents-workflows-grid:
   user:
     roles:
       $contains:
         value: sdc-response-manager
```

**as-sdc-response-manager-search-config policy**

Searh for SDCConfigs

> Used for choosing config in 'share' (populatelink) UI

```yaml
PUT /AccessPolicy/as-sdc-response-manager-search-config
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-search-config
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri: '#/SDCConfig$'
  request-method: get
```

**as-sdc-response-manager-read-config policy**

Read configuration

```yaml
PUT /AccessPolicy/as-sdc-response-manager-read-config
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-read-config
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri: '/$sdc-config'
  request-method: post
```

**as-sdc-response-manager-search-and-read-theme policy**

Search and read theme

> Used for choosing theme in 'share' (populatelink) UI

```yaml
PUT /AccessPolicy/as-sdc-response-manager-search-and-read-theme
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-search-and-read-theme
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri:
    $one-of:
      - '#/QuestionnaireTheme$'
      - '#/QuestionnaireTheme/.*'
  request-method: get
```

**as-sdc-response-manager-search-and-read-questionnaire policy**

Search and read Questionnaires

```yaml
PUT /AccessPolicy/as-sdc-response-manager-search-and-read-questionnaire
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-search-and-read-questionnaire
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri:
    $one-of:
    - '#/Questionnaire$'
    - '#/Questionnaire/.*$'
  request-method: get
```

**as-sdc-response-manager-search-and-read-response policy**

Search and read responses

```yaml
PUT /AccessPolicy/as-sdc-response-manager-search-and-read-response
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-search-and-read-response
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri:
    $one-of:
    - '#/QuestionnaireResponse'
    - '#/QuestionnaireResponse/.*$'
  request-method: get
```

**as-sdc-response-manager-search-patient-and-encounter policy**

Search and read

* Patient
* Encounter

> Used for choosing patient and encounter in 'share' (populatelink) UI

```yaml
PUT /AccessPolicy/as-sdc-response-manager-search-patient-and-encounter
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-search-patient-and-encounter
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri:
    $one-of:
    - '#/Encounter$'
    - '#/Patient$'
  request-method: get
```

**as-sdc-response-manager-populate-questionnaire policy**

Populate questionnaire (from 'share' UI)

```yaml
PUT /AccessPolicy/as-sdc-response-manager-populate-questionnaire
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-populate-questionnaire
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri: '#/Questionnaire/.*/\$populatelink$'
  request-method: post
```

**as-sdc-response-manager-generate-link policy**

Generate access links for responses

```yaml
PUT /AccessPolicy/as-sdc-response-manager-generate-link
content-type: text/yaml
accept: text/yaml

id: as-sdc-response-manager-generate-link
resourceType: AccessPolicy
engine: matcho
matcho:
  user:
    roles:
      $contains:
        value: sdc-response-manager
  uri: '#/QuestionnaireResponse/.*/\$generate-link$'
  request-method: post
```

## Test access policies

Examples of users with roles:

* sdc admin
* form designer
* form filler
* response manager
* response manager + form filler

> it's possible to mix roles together

### SDC Admin

```yaml
POST /User
content-type: text/yaml
accept: text/yaml

resourceType: User
id: sdc-admin-user
password: password
roles:
  - value: sdc-admin
```

### Form Designer

```yaml
POST /User
content-type: text/yaml
accept: text/yaml

resourceType: User
id: form-designer-user
password: password
roles:
  - value: sdc-form-designer
```

### Form Filler

```yaml
POST /User
content-type: text/yaml
accept: text/yaml

resourceType: User
id: form-filler-user
password: password
roles:
  - value: sdc-form-filler
```

### Response Manager

```yaml
POST /User
content-type: text/yaml
accept: text/yaml

resourceType: User
id: response-manager-user
password: password
roles:
  - value: sdc-response-manager
```

### Mix roles (Form Filler + Response Manager)

```yaml
POST /User
content-type: text/yaml
accept: text/yaml

resourceType: User
id: form-user
password: password
roles:
  - value: sdc-response-manager
  - value: sdc-form-filler
```


## Strict access control

In the Structured Data Capture (SDC) module, certain operations act as proxy calls to the FHIR REST API. These operations often fetch or persist data on behalf of the user. For example, the $populate operation may internally perform a GET /Patient/[id] request to prefill a form with patient data.

By default, SDC assumes a permissive access model: if a user has permission to invoke an SDC operation such as $populate, they are implicitly allowed to access any referenced resources defined in the associated Questionnaire. However, this behavior can lead to potential data leakage in production environments. If a user has permission to both edit a Questionnaire and invoke $populate, they could manipulate the form structure to fetch unauthorized data.
Enabling Strict Mode

To mitigate this risk, the SDC module supports a strict access control mode. This mode enforces explicit authorization for all backend resource accesses initiated by SDC proxy operations.

Strict mode can be enabled via the following environment variable:

```
BOX_MODULE_SDC_STRICT_ACCESS_CONTROL=true
```

Once enabled, the system will require dedicated AccessPolicy definitions that govern which resource types and operations can be accessed by each SDC proxy operation.


Affected Proxy Operations

The following SDC operations are affected by strict access control:

- `POST /Questionnaire/$populate` and `POST /Questionnaire/$populatelink`: Prefill a form using data retrieved from FHIR resources.

- `POST /QuestionnaireResponse/$submit` : Extract data from a completed response and persist it as FHIR resources.

-  `POST /Questionnaire/$reference-lookup` : Perform lookups for resource references (e.g., used in reference widgets).

Configuring Access Policies

To enable FHIR resource access for these operations under strict mode, you must define corresponding AccessPolicy rules.
These rules must include the correct context identifier in the extra-data.sdc.context field, indicating the SDC operation the access is intended for.

Below are example AccessPolicy matcho blocks for each operation:

1. For $populate and $populatelink

```yaml
engine: matcho
matcho:
  extra-data:
    sdc:
      context: populate
```

2. For $reference-lookup

```yaml
engine: matcho
matcho:
  extra-data:
    sdc:
      context: reference-lookup
```

3. For $submit

```yaml
engine: matcho
matcho:
  extra-data:
    sdc:
      context: extract
```

Each access policy should also specify the allowed resource types, operations (e.g., read, search, create), and any additional filters or constraints as appropriate for your security model.
