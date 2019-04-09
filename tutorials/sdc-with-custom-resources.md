---
description: Structured Data Capturing with Aidbox Custom Resources
---

# SDC with Custom Resources

{% hint style="warning" %}
This is ALPHA \(pre-view\) feature of Aidbox, which is published to collect users feedback. It's available now on 0.4.2-SNAPSHOT Aidbox.Dev and on edge cluster in cloud.
{% endhint %}

Sometimes FHIR granularity is too small to collect patient data from user interfaces! For example when Physician or Nurse record vital signs in terms of FHIR this will produce bundle of observations. How to save this data into FHIR server? How to keep track that this data was collected from Vitals Form?

One option is to build transaction bundle on client side with provenance and send it to transaction endpoint. But with this approach is rather problematic to track data back; update or delete it as a whole.

There is [SDC IG](http://hl7.org/fhir/us/sdc/index.html) - how to collect data using Questionnaire and  QuestionnaireResponse. It's a good idea and work in progress. The one problem we see that your QuestionnaireResponse table will be stuffed with all sorts of things and managing this table, doing searches and updates will be problematic. Another problem that Questionnaire/QuestionnaireResponse is too generic and verbose to work with in a convenient way.

Aidbox supports Custom Resources, which are cover all Questionnaire functionality and flexibility and provide you much more features - validation, separate storage, searches, references with other FHIR resources etc. We applied key ideas  from [SDC IG](http://hl7.org/fhir/us/sdc/index.html) to Custom Resources to give you the best from both worlds.

## Demonstration Scenario

First step is to define your custom resource. We recommend do it using **sugar** App API. As example we are defining resource Vitals with temperature, heart\_rate and patient reference:

{% code-tabs %}
{% code-tabs-item title="custom-resource.yaml" %}
```yaml
POST /App

resourceType: App
id: Vitals
apiVersion: 1
type: app
entities:
  Vitals:
    attrs:
      temperature: {type: number}
      heart_rate: {type: number}
      ts: {type: dateTime}
      patient:
        type: Reference
        refers: [Patient]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Now we can check that  Vitals works properly.

Create new vitals record:

{% code-tabs %}
{% code-tabs-item title="patient" %}
```yaml
PUT /Patient/pt-1

name: 
- family: Jackson
  given: ['John']
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="create-vitals" %}
```yaml
POST /Vitals

resourceType: Vitals
temperature: 36.6
heart_rate: 102
patient: {id: 'pt-1', resourceType: 'Patient'}
ts: '2019-04-09T12:00:00Z'
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Get all vitals records for `pt-1`:

{% code-tabs %}
{% code-tabs-item title="get-vitals" %}
```yaml
GET /Vitals?.patient.id=pt-1
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Debugging Extraction

Now we want to transform Vitals into set of FHIR Observations. We will use template extracting. You can play and debug extraction  using `/AlphaSDC/$debug`

{% code-tabs %}
{% code-tabs-item title="debug.yaml" %}
```yaml
POST /AlphaSDC/$debug

source:
 id: src-1
 temp: 36.6
template:
  Observation:
     temp: 
       value: { $path!: [temp] }
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="response.yaml" %}
```yaml
source: {id: src-1, temp: 36.6}
template:
  Observation:
    temp:
      value:
        $path!: [temp]
extracted:
  Observation:
    temp:
      value: 36.6
      id: temp-src-1
      meta: {sourceId: src-1}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

**template** it structured as `{ [resourceType] : { [id-prefix] : <resource-template> }}`, for example to generate Observation with prefix tmp you do `{Observation: {tmp: <resource-template>}}`

**&lt;resource-template&gt;** is a template/prototype of resulting FHIR resource, where some attributes are contains substitution rules like `{ $path!: ["attr", "attr"]}`, this rules will be replaced with value from Custom resource, indicated by this path.

### Create SDC resource

Now let's define how FHIR Observations will be extracted from Vitals. We have to create AlphaSDC resource for that:

```yaml
PUT /AlphaSDC/Vitals

template:
  Observation:
    temp:
      subject:
        $path!: [patient]
      category:
      - coding:
        - {system: 'http://terminology.hl7.org/CodeSystem/observation-category', code: vital-signs, display: Vital Signs}
      status: final
      code:
        coding:
        - {system: 'http://loinc.org', code: 8310-5, display: Body temperature}
      effective:
        dateTime:
          $path!: [ts]
      value:
        Quantity:
          value:
            $path!: [temperature]
          unit: degrees C
          system: http://unitsofmeasure.org
          code: Cel
    hr:
      subject:
        $path!: [patient]
      category:
      - coding:
        - {system: 'http://terminology.hl7.org/CodeSystem/observation-category',
          code: vital-signs, display: Vital Signs}
      status: final
      code:
        coding:
        - {system: 'http://loinc.org', code: 8867-4, display: Heart rate}
      effective:
        dateTime:
          $path!: [ts]
      value:
        Quantity:
          value:
            $path!: [heart_rate]
          unit: beats/minute
          system: http://unitsofmeasure.org
          code: /min
```

**.id** of AlphaSDC resource should be exactly the same as name of Custom Resource - i.e. `Vitals`

.**template** is a template described in  section above.

Now you can test how data are extracted using  `POST /AlphaSDC/<id>/$extract` endpoint:

{% code-tabs %}
{% code-tabs-item title="request" %}
```yaml
POST /AlphaSDC/Vitals/$extract

resourceType: Vitals
temperature: 36.6
heart_rate: 102
patient: {id: 'pt-1', resourceType: 'Patient'}
ts: '2019-04-09T12:00:00Z'
```
{% endcode-tabs-item %}
{% endcode-tabs %}

You have to see the following response:

```yaml
source: <original resource>
template: <template>
extracted: <result-of-extraction>
```

### Make it work on resource save

To make this extraction work on Vitals save, we have to override it's `Create Operation`

```yaml
PUT /Operation/vitals-create

request: [post, Vitals]
action: sdc/create
```

**sdc/create** action will do extraction and save original custom resource and extracted FHIR resources:

{% code-tabs %}
{% code-tabs-item title="create-vitals" %}
```yaml
POST /Vitals

resourceType: Vitals
id: v-2
temperature: 36.6
heart_rate: 102
patient: {id: 'pt-1', resourceType: 'Patient'}
ts: '2019-04-09T12:00:00Z'
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Now let's see Observations should be created:

{% code-tabs %}
{% code-tabs-item title="get-observations" %}
```yaml
GET /Observation?.subject.id=pt-1
# or
GET /Vitals?.patient.id=pt-1
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% hint style="info" %}
Please provide your feedback and use cases for SDC using [github](https://github.com/Aidbox/Issues/issues)
{% endhint %}

## TBD

* alternative template languages - JUTE; FHIRPath etc
* support for Update & Delete
* template validation
* $pre-populate Operation with SQL engine

