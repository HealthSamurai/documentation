---
description: Structured Data Capturing with Aidbox Custom Resources
---

# SDC with Custom Resources

{% hint style="warning" %}
This is an ALPHA (preview) feature of Aidbox, which was published to collect users feedback. It's available now on 0.4.2-SNAPSHOT Devbox and on edge cluster in the cloud.
{% endhint %}

Sometimes FHIR granularity is too small to collect patient data from user interfaces. For example, when a physician or a nurse records vital signs in terms of FHIR, they produce a bundle of observations. How to save this data to a FHIR server? How to keep track that this data was collected on the Vitals Form?

One option is to build a transaction bundle with a provenance on the client side and send it to the transaction endpoint. But with this approach it is rather problematic to track data back; update or delete it as a whole.

There is a [Structured Data Capture Implementation Guide](http://hl7.org/fhir/us/sdc/index.html) (SDC IG) which describes how to collect data using Questionnaire and QuestionnaireResponse resources. It is a good idea and a work in progress. One problem we see with it is that the QuestionnaireResponse table is stuffed with all sorts of things and managing this table, doing searches and updates is problematic. Another problem is that Questionnaire/QuestionnaireResponse is too generic and verbose which makes working with them inconvenient.

Aidbox supports Custom Resources, which cover all Questionnaire functionality and flexibility while providing many more features - validation, separate storage, search, references to other FHIR resources, etc. We applied key ideas from the [SDC IG](http://hl7.org/fhir/us/sdc/index.html) to Custom Resources to give you the best of both worlds.

## Demonstration Scenario

The first step is to define your custom resource. We recommend using **sugar** App API. As an example we are defining resource Vitals with temperature, heart\_rate, and patient reference:

{% code title="custom-resource.yaml" %}
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
{% endcode %}

Now we can check that Vitals work properly.

Create a new vitals record:

{% code title="patient" %}
```yaml
PUT /Patient/pt-1

name: 
- family: Jackson
  given: ['John']
```
{% endcode %}

{% code title="create-vitals" %}
```yaml
POST /Vitals

resourceType: Vitals
temperature: 36.6
heart_rate: 102
patient: {id: 'pt-1', resourceType: 'Patient'}
ts: '2019-04-09T12:00:00Z'
```
{% endcode %}

Get all vitals records for the patient `pt-1`:

{% code title="get-vitals" %}
```yaml
GET /Vitals?.patient.id=pt-1
```
{% endcode %}

### Debugging Extraction

Now we want to transform Vitals to a set of FHIR Observations. We are using template extraction. You can play and debug extraction using `/AlphaSDC/$debug`

{% code title="debug.yaml" %}
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
{% endcode %}

{% code title="response.yaml" %}
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
{% endcode %}

**template** it structured as `{ [resourceType] : { [id-prefix] : <resource-template> }}`, for example to generate Observation with prefix tmp you write `{Observation: {tmp: <resource-template>}}`

**\<resource-template>** is a template/prototype of a resulting FHIR resource, where some attributes contain substitution rules like `{ $path!: ["attr", "attr"]}`. These rules will be replaced with values from Custom resource, indicated by this path.

### Advanced substitution rules examples

{% code title="resource.yaml" %}
```yaml
resourceType: QuestionnaireResponse
id: qr1
questionnaire: Questionnaire/q1|1
item:
- text: What is your height in feet.inches?
  answer:
  - valueDecimal: 5.2
  linkId: ht
- text: What is your weight in pounds?
  answer:
  - valueDecimal: 122
  linkId: wt
```
{% endcode %}

Let's suppose you want to extract the height of a respondent, for this purpose you can use this `$path` expression `["item, {"linkId": ht"}, "answer", 0, "valueDecimal"]`

`{"linkId": ht"}` - this object is used for filtering, you can declare the objects with as many keys as you need to match item in the specified collection.

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

**.id** of AlphaSDC resource should be exactly the same as the name of Custom Resource - i.e., `Vitals`

.**template** is a template described in the section above.

Now you can test how data is extracted using  `POST /AlphaSDC/<id>/$extract` endpoint:

{% code title="request" %}
```yaml
POST /AlphaSDC/Vitals/$extract

resourceType: Vitals
temperature: 36.6
heart_rate: 102
patient: {id: 'pt-1', resourceType: 'Patient'}
ts: '2019-04-09T12:00:00Z'
```
{% endcode %}

You should see the following response:

```yaml
source: <original resource>
template: <template>
extracted: <result-of-extraction>
```

### Make it work on resource save

To make this extraction work on Vitals save, we have to override its `Create Operation`

```yaml
PUT /Operation/vitals-create

request: [post, Vitals]
action: sdc/create
```

**sdc/create** action will do extraction and save original custom resource and extracted FHIR resources:

{% code title="create-vitals" %}
```yaml
POST /Vitals

resourceType: Vitals
id: v-2
temperature: 36.6
heart_rate: 102
patient: {id: 'pt-1', resourceType: 'Patient'}
ts: '2019-04-09T12:00:00Z'
```
{% endcode %}

Now we can see the Observations that have been created:

{% code title="get-observations" %}
```yaml
GET /Observation?.subject.id=pt-1
# or
GET /Vitals?.patient.id=pt-1
```
{% endcode %}

{% hint style="info" %}
Please provide your feedback and use cases for SDC using [github](https://github.com/Aidbox/Issues/issues)
{% endhint %}

## TBD

* alternative template languages - JUTE; FHIRPath etc
* support for Update & Delete
* template validation
* $pre-populate Operation with SQL engine
