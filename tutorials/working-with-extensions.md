# Working with Extensions

Read more about FHIR extensions here [http://hl7.org/fhir/STU3/extensibility.html](http://hl7.org/fhir/STU3/extensibility.html).

 Every element in a resource can have extension child elements to represent additional information that is not part of the basic definition of the resource.

 Every element in a resource or data type includes an optional "extension" child element that may be present any number of times.

All extensions used in resources require a formal published definition which can be used by application developers, or the applications themselves, to help integrate extensions into the healthcare process they support.

Every extension in a resource refers directly to its definition, which is made available as a [StructureDefinition](http://hl7.org/fhir/STU3/structuredefinition.html). A resource can be [profiled](http://hl7.org/fhir/STU3/profiling.html) to specify where particular extensions are used.

Whenever resources containing extensions are exchanged, the definitions of the extensions SHALL be available to all the parties that share the resources. Each extension contains a URI that references the source of the definitions as a [StructureDefinition](http://hl7.org/fhir/STU3/structuredefinition.html). The source SHOULD be a literal reference, such as an http: URL that refers to an end-point that responds with the contents of the definitions - preferably a [FHIR RESTful server](http://hl7.org/fhir/STU3/http.html) supporting the Resources Profile, or a logical reference \(e.g. using a urn:\) - for instance, to a national published standard. Extensions may be defined by any project or jurisdiction, up to and including international standards organizations such as HL7 itself.

Before defining a new extension, attempt to reuse existing extensions defined in one of the [shared registries described below](http://hl7.org/fhir/STU3/defining-extensions.html#registration). Also consider that some concepts may be appropriate to add as part of the core specification.

### Aidbox Extensions

{% tabs %}
{% tab title="First Tab" %}
```text
POST /Observation   

text: {div: "<div>\n Heart Rate Patient:Heart Rate (8867-4): 50 beats/minute\n\ </div>", status: generated}
hrtakencontext:
  text: not set
  coding:
    - {code: '0', system: 'https://myhealth.aidbox.app/HeartRateMotionContext', display: not set}
value:
  Quantity: {code: "/min", unit: "beats/minute", value: 50, system: "http://unitsofmeasure.org"}
status: final
subject: {display: Test}
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

{% tab title="Second Tab" %}
```text
Status: 201
id: 7d0fa8da-be89-4db1-ba40-8951427412b1
hrtakencontext:
  text: not set
  coding:
  - {code: '0', system: 'https://myhealth.aidbox.app/HeartRateMotionContext', display: not set}
value:
  Quantity: {code: /min, unit: beats/minute, value: 50, system: 'http://unitsofmeasure.org'}
status: final
text: {div: "<div>\n  Heart Rate Patient:Heart Rate (8867-4): 50 beats/minute \n</div>",
  status: generated}
subject: {display: Test}
meta: {lastUpdated: '2018-12-25T08:17:16.736Z', versionId: '18'}
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

