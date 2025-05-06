# Working with Extensions

{% hint style="warning" %}
This tutorial is deprecated. Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Broken link](broken-reference "mention")
{% endhint %}

## Aidbox Native Extensions

In addition to the FHIR extensions, Aidbox has its own implementation of [extensions as native attributes](broken-reference).

First, you will need to create an Attribute resource.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Attribute/Observation.hrtakencontext

resourceType: Attribute
resource: {id: Observation, resourceType: Entity}
path: [hrtakencontext]
id: Observation.hrtakencontext
module: fhir-3.0.1
order: 100
type: {id: CodeableConcept, resourceType: Entity}
description: How it was done hrtakencontext
```
{% endtab %}

{% tab title="Response" %}
**Status: 201**

```yaml
resource: {id: Observation, resourceType: Entity}
path: [hrtakencontext]
id: Observation.hrtakencontext
module: fhir-3.0.1
order: 100
type: {id: CodeableConcept, resourceType: Entity}
meta: {lastUpdated: '2019-02-06T15:39:20.242Z', versionId: '120'}
resourceType: Attribute
description: How it was done hrtakencontext
```
{% endtab %}
{% endtabs %}

Now, you can create a resource using the native Aidbox extension.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /Observation/sample-for-hrtakencontext 

resourceType: Observation
id: sample-for-hrtakencontext
text: {div: "<div>\n Heart Rate Patient: Jane Smith (8867-4): 50 beats/minute\n\ </div>", status: generated}
hrtakencontext:
  text: not set
  coding:
    - {code: '0', system: 'https://myhealth.aidbox.app/HeartRateMotionContext', display: not set}
value:
  Quantity: {code: "/min", unit: "beats/minute", value: 50, system: "http://unitsofmeasure.org"}
status: final
subject: {display: Jane Smith}
category:
  - text: Vital Signs
    coding:
      - {code: vital-signs, system: 'http://hl7.org/fhir/observation-category', display: Vital Signs}
code:
  text: Heart rate
  coding:
    - {code: 8867-4, system: 'http://loinc.org', display: Heart rate}
effective: {dateTime: '2018-11-20T15:12:00.000+0300'}
```
{% endtab %}

{% tab title="Response" %}
**Status: 201**

```yaml
id: sample-for-hrtakencontext
hrtakencontext:
  text: not set
  coding:
  - {code: '0', system: 'https://myhealth.aidbox.app/HeartRateMotionContext', display: not set}
value:
  Quantity: {code: /min, unit: beats/minute, value: 50, system: 'http://unitsofmeasure.org'}
status: final
text: {div: "<div>\n  Heart Rate Patient: Jane Smith (8867-4): 50 beats/minute \n\
    </div>", status: generated}
subject: {display: Jane Smith}
meta: {lastUpdated: '2019-02-06T15:42:13.053Z', versionId: '122'}
category:
- text: Vital Signs
  coding:
  - {code: vital-signs, system: 'http://hl7.org/fhir/observation-category', display: Vital Signs}
resourceType: Observation
code:
  text: Heart rate
  coding:
  - {code: 8867-4, system: 'http://loinc.org', display: Heart rate}
effective: {dateTime: '2018-11-20T15:12:00.000+0300'}
```
{% endtab %}
{% endtabs %}
