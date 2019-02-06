# Working with Extensions

## General Information

Every element in a resource or data type includes an optional "extension" child element that may be present any number of times.

All extensions used in resources require a formal published definition which can be used by application developers, or the applications themselves, to help integrate extensions into the healthcare process they support.

Every extension in a resource refers directly to its definition, which is made available as a [StructureDefinition](http://hl7.org/fhir/STU3/structuredefinition.html). A resource can be [profiled](http://hl7.org/fhir/STU3/profiling.html) to specify where particular extensions are used.

Before defining a new extension, attempt to reuse existing extensions defined in one of the [shared registries described below](http://hl7.org/fhir/STU3/defining-extensions.html#registration).

Read more about FHIR extensions here: [http://hl7.org/fhir/STU3/extensibility.html](http://build.fhir.org/extensibility.html) and [http://build.fhir.org/defining-extensions.html](http://build.fhir.org/defining-extensions.html).

## Extension Registries

There are two places where you can search for existing extensions:

* official extensions that are part of the FHIR standard: [http://build.fhir.org/extensibility-registry.html](http://build.fhir.org/extensibility-registry.html),
* user defined extensions published in the FHIR registry: [https://registry.fhir.org](https://registry.fhir.org).

## How to Create an Extension

First, you will need to compose and create a StructureDefinition resource for your extension.

Here's an example of StructureDefinition resource for an extension to provide a value of heart rate motion context. The value will have the type `CodeableConcept`.

{% tabs %}
{% tab title="Request" %}
```javascript
PUT /fhir/StructureDefinition/observation-heartRateMotionContext

{
  "resourceType": "StructureDefinition",
  "id": "observation-heartRateMotionContext",
  "url": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
  "name": "heartRateMotionContext",
  "title": "heartRateMotionContext",
  "status": "active",
  "date": "2018-12-21",
  "publisher": "Health Samurai",
  
  "description": "HeartRateMotionContext when the observation was done, e.g. 0: 'notSet', 1: 'sedentary', 2: 'active'",
  "fhirVersion": "3.0.1",
  "kind": "complex-type",
  "abstract": false,
  "contextType": "resource",
  "context": [
    "Observation"
  ],
  "type": "Extension",
  ... Skipped for brevity ...
}
```
{% endtab %}

{% tab title="Full Request" %}
```javascript
PUT /fhir/StructureDefinition/observation-heartRateMotionContext

{
  "resourceType": "StructureDefinition",
  "id": "observation-heartRateMotionContext",
  "url": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
  "name": "heartRateMotionContext",
  "title": "heartRateMotionContext",
  "status": "active",
  "date": "2018-12-21",
  "publisher": "Health Samurai",
  
  "description": "HeartRateMotionContext when the observation was done, e.g. 0: 'notSet', 1: 'sedentary', 2: 'active'",
  "fhirVersion": "3.0.1",
  "kind": "complex-type",
  "abstract": false,
  "contextType": "resource",
  "context": [
    "Observation"
  ],
  "type": "Extension",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Extension",
  "derivation": "constraint",
  "snapshot": {
    "element": [{
        "id": "Extension",
        "path": "Extension",
        "short": "The body position during the observation",
        "definition": "The position of the body when the observation was done, e.g. standing, sitting. To be used only when the body position in not precoordinated in the observation code.",
        "min": 0,
        "max": "1",
        "base": {
          "path": "Extension",
          "min": 0,
          "max": "*"
        },
        "condition": [
          "ele-1"
        ],
        "constraint": [{
            "key": "ele-1",
            "severity": "error",
            "human": "All FHIR elements must have a @value or children",
            "expression": "hasValue() | (children().count() > id.count())",
            "xpath": "@value|f:*|h:div",
            "source": "Element"
          }, {
            "key": "ext-1",
            "severity": "error",
            "human": "Must have either extensions or value[x], not both",
            "expression": "extension.exists() != value.exists()",
            "xpath": "exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])",
            "source": "Extension"
          }
        ]
      }, {
        "id": "Extension.id",
        "path": "Extension.id",
        "representation": [
          "xmlAttr"
        ],
        "short": "xml:id (or equivalent in JSON)",
        "definition": "unique id for the element within a resource (for internal references). This may be any string value that does not contain spaces.",
        "min": 0,
        "max": "1",
        "base": {
          "path": "Element.id",
          "min": 0,
          "max": "1"
        },
        "type": [{
            "code": "string"
          }
        ],
        "mapping": [{
            "identity": "rim",
            "map": "n/a"
          }
        ]
      }, {
        "id": "Extension.extension",
        "path": "Extension.extension",
        "slicing": {
          "discriminator": [{
              "type": "value",
              "path": "url"
            }
          ],
          "description": "Extensions are always sliced by (at least) url",
          "rules": "open"
        },
        "short": "Additional Content defined by implementations",
        "definition": "May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.",
        "comment": "There can be no stigma associated with the use of extensions by any application, project, or standard - regardless of the institution or jurisdiction that uses or defines the extensions.  The use of extensions is what allows the FHIR specification to retain a core level of simplicity for everyone.",
        "alias": [
          "extensions",
          "user content"
        ],
        "min": 0,
        "max": "*",
        "base": {
          "path": "Element.extension",
          "min": 0,
          "max": "*"
        },
        "type": [{
            "code": "Extension"
          }
        ],
        "mapping": [{
            "identity": "rim",
            "map": "n/a"
          }
        ]
      }, {
        "id": "Extension.url",
        "path": "Extension.url",
        "representation": [
          "xmlAttr"
        ],
        "short": "identifies the meaning of the extension",
        "definition": "Source of the definition for the extension code - a logical name or a URL.",
        "comment": "The definition may point directly to a computable or human-readable definition of the extensibility codes, or it may be a logical URI as declared in some other specification. The definition SHALL be a URI for the Structure Definition defining the extension.",
        "min": 1,
        "max": "1",
        "base": {
          "path": "Extension.url",
          "min": 1,
          "max": "1"
        },
        "type": [{
            "code": "uri"
          }
        ],
        "fixedUri": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
        "mapping": [{
            "identity": "rim",
            "map": "N/A"
          }
        ]
      }, {
        "id": "Extension.valueCodeableConcept",
        "path": "Extension.valueCodeableConcept",
        "short": "Value of extension",
        "definition": "Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).",
        "min": 0,
        "max": "1",
        "base": {
          "path": "Extension.value[x]",
          "min": 0,
          "max": "1"
        },
        "type": [{
            "code": "CodeableConcept"
          }
        ],
        "mapping": [{
            "identity": "rim",
            "map": "N/A"
          }
        ]
      }
    ]
  },
  "differential": {
    "element": [{
        "id": "Extension",
        "path": "Extension",
        "short": "The body position during the observation",
        "definition": "The position of the body when the observation was done, e.g. standing, sitting. To be used only when the body position in not precoordinated in the observation code.",
        "min": 0,
        "max": "1"
      }, {
        "id": "Extension.extension",
        "path": "Extension.extension",
        "max": "0"
      }, {
        "id": "Extension.url",
        "path": "Extension.url",
        "type": [{
            "code": "uri"
          }
        ],
        "fixedUri": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext"
      }, {
        "id": "Extension.valueCodeableConcept",
        "path": "Extension.valueCodeableConcept",
        "type": [{
            "code": "CodeableConcept"
          }
        ]
      }
    ]
  }
}

```
{% endtab %}

{% tab title="Response" %}
**Status: 201**

```javascript
{
 "description": "HeartRateMotionContext when the observation was done, e.g. 0: 'notSet', 1: 'sedentary', 2: 'active'",
 "date": "2018-12-21",
 "derivation": "constraint",
 "meta": {
  "lastUpdated": "2019-02-06T14:55:58.498Z",
  "versionId": "118"
 },
 "publisher": "Health Samurai",
 "fhirVersion": "3.0.1",
 "name": "heartRateMotionContext",
 "abstract": false,
 "type": "Extension",
 "resourceType": "StructureDefinition",
 "title": "heartRateMotionContext",
 "snapshot": {
  "element": [
   {
    "constraint": [
     {
      "key": "ele-1",
      "human": "All FHIR elements must have a @value or children",
      "xpath": "@value|f:*|h:div",
      "source": "Element",
      "severity": "error",
      "expression": "hasValue() | (children().count() > id.count())"
     },
     {
      "key": "ext-1",
      "human": "Must have either extensions or value[x], not both",
      "xpath": "exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])",
      "source": "Extension",
      "severity": "error",
      "expression": "extension.exists() != value.exists()"
     }
    ],
    "path": "Extension",
    "min": 0,
    "definition": "The position of the body when the observation was done, e.g. standing, sitting. To be used only when the body position in not precoordinated in the observation code.",
    "short": "The body position during the observation",
    "max": "1",
    "id": "Extension",
    "condition": [
     "ele-1"
    ],
    "base": {
     "max": "*",
     "min": 0,
     "path": "Extension"
    }
   },
   {
    "path": "Extension.id",
    "min": 0,
    "definition": "unique id for the element within a resource (for internal references). This may be any string value that does not contain spaces.",
    "short": "xml:id (or equivalent in JSON)",
    "mapping": [
     {
      "map": "n/a",
      "identity": "rim"
     }
    ],
    "type": [
     {
      "code": "string"
     }
    ],
    "representation": [
     "xmlAttr"
    ],
    "max": "1",
    "id": "Extension.id",
    "base": {
     "max": "1",
     "min": 0,
     "path": "Element.id"
    }
   },
   {
    "path": "Extension.extension",
    "min": 0,
    "definition": "May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.",
    "short": "Additional Content defined by implementations",
    "mapping": [
     {
      "map": "n/a",
      "identity": "rim"
     }
    ],
    "slicing": {
     "rules": "open",
     "description": "Extensions are always sliced by (at least) url",
     "discriminator": [
      {
       "path": "url",
       "type": "value"
      }
     ]
    },
    "type": [
     {
      "code": "Extension"
     }
    ],
    "alias": [
     "extensions",
     "user content"
    ],
    "max": "*",
    "id": "Extension.extension",
    "comment": "There can be no stigma associated with the use of extensions by any application, project, or standard - regardless of the institution or jurisdiction that uses or defines the extensions.  The use of extensions is what allows the FHIR specification to retain a core level of simplicity for everyone.",
    "base": {
     "max": "*",
     "min": 0,
     "path": "Element.extension"
    }
   },
   {
    "path": "Extension.url",
    "min": 1,
    "definition": "Source of the definition for the extension code - a logical name or a URL.",
    "short": "identifies the meaning of the extension",
    "fixedUri": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
    "mapping": [
     {
      "map": "N/A",
      "identity": "rim"
     }
    ],
    "type": [
     {
      "code": "uri"
     }
    ],
    "representation": [
     "xmlAttr"
    ],
    "max": "1",
    "id": "Extension.url",
    "comment": "The definition may point directly to a computable or human-readable definition of the extensibility codes, or it may be a logical URI as declared in some other specification. The definition SHALL be a URI for the Structure Definition defining the extension.",
    "base": {
     "max": "1",
     "min": 1,
     "path": "Extension.url"
    }
   },
   {
    "path": "Extension.valueCodeableConcept",
    "min": 0,
    "definition": "Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).",
    "short": "Value of extension",
    "mapping": [
     {
      "map": "N/A",
      "identity": "rim"
     }
    ],
    "type": [
     {
      "code": "CodeableConcept"
     }
    ],
    "max": "1",
    "id": "Extension.valueCodeableConcept",
    "base": {
     "max": "1",
     "min": 0,
     "path": "Extension.value[x]"
    }
   }
  ]
 },
 "status": "active",
 "id": "observation-heartRateMotionContext",
 "kind": "complex-type",
 "url": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
 "context": [
  "Observation"
 ],
 "differential": {
  "element": [
   {
    "id": "Extension",
    "max": "1",
    "min": 0,
    "path": "Extension",
    "short": "The body position during the observation",
    "definition": "The position of the body when the observation was done, e.g. standing, sitting. To be used only when the body position in not precoordinated in the observation code."
   },
   {
    "id": "Extension.extension",
    "max": "0",
    "path": "Extension.extension"
   },
   {
    "id": "Extension.url",
    "path": "Extension.url",
    "type": [
     {
      "code": "uri"
     }
    ],
    "fixedUri": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext"
   },
   {
    "id": "Extension.valueCodeableConcept",
    "path": "Extension.valueCodeableConcept",
    "type": [
     {
      "code": "CodeableConcept"
     }
    ]
   }
  ]
 },
 "contextType": "resource",
 "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Extension"
}
```
{% endtab %}
{% endtabs %}

Now, you can create appropriate resources applying your extension.

{% tabs %}
{% tab title="Request" %}
```javascript
POST /fhir/Observation

{
  "resourceType": "Observation",  
  "extension": [{
      "url": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
      "valueCodeableConcept": {
        "coding": [{
            "system": "https://myhealth.aidbox.app/HeartRateMotionContext",
            "code": "2",
            "display": "active"
          }
        ],
        "text": "active"
      }
    }
  ],
  ... skipped for brevity ...
}
```
{% endtab %}

{% tab title="Full Request" %}
```javascript
POST /fhir/Observation

{
  "resourceType": "Observation",
  "meta": {
    "profile": [
      "http://hl7.org/fhir/StructureDefinition/vitalsigns"
    ]
  },
  "text": {
    "status": "generated",
    "div": "<div xmlns='http://www.w3.org/1999/xhtml'>Heart Rate Patient: Jame Smith           Heart Rate (8867-4): 101 beats/minute          </div>"
  },
  "extension": [{
      "url": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
      "valueCodeableConcept": {
        "coding": [{
            "system": "https://myhealth.aidbox.app/HeartRateMotionContext",
            "code": "2",
            "display": "active"
          }
        ],
        "text": "active"
      }
    }
  ],
  "status": "final",
  "category": [{
      "coding": [{
          "system": "http://hl7.org/fhir/observation-category",
          "code": "vital-signs",
          "display": "Vital Signs"
        }
      ],
      "text": "Vital Signs"
    }
  ],
  "code": {
    "coding": [{
        "system": "http://loinc.org",
        "code": "8867-4",
        "display": "Heart rate"
      }
    ],
    "text": "Heart rate"
  },
  "subject": {
    "display": "Patient Jane Smith"
  },
  "effectiveDateTime": "2018-12-21T10:05:52.049Z",
  "valueQuantity": {
    "value": 101,
    "unit": "beats/minute",
    "system": "http://unitsofmeasure.org",
    "code": "/min"
  }
}
```
{% endtab %}

{% tab title="Response" %}
**Status: 201**

```javascript
{
 "category": [
  {
   "text": "Vital Signs",
   "coding": [
    {
     "code": "vital-signs",
     "system": "http://hl7.org/fhir/observation-category",
     "display": "Vital Signs"
    }
   ]
  }
 ],
 "meta": {
  "profile": [
   "http://hl7.org/fhir/StructureDefinition/vitalsigns"
  ],
  "lastUpdated": "2019-02-06T15:13:01.578Z",
  "versionId": "119"
 },
 "valueQuantity": {
  "code": "/min",
  "unit": "beats/minute",
  "value": 101,
  "system": "http://unitsofmeasure.org"
 },
 "resourceType": "Observation",
 "extension": [
  {
   "url": "https://myhealth.aidbox.app/fhir/StructureDefinition/observation-heartRateMotionContext",
   "valueCodeableConcept": {
    "text": "active",
    "coding": [
     {
      "code": "2",
      "system": "https://myhealth.aidbox.app/HeartRateMotionContext",
      "display": "active"
     }
    ]
   }
  }
 ],
 "effectiveDateTime": "2018-12-21T10:05:52.049Z",
 "status": "final",
 "id": "3d1e5b25-41d3-4618-9a70-75de383e1333",
 "code": {
  "text": "Heart rate",
  "coding": [
   {
    "code": "8867-4",
    "system": "http://loinc.org",
    "display": "Heart rate"
   }
  ]
 },
 "subject": {
  "display": "Patient Jane Smith"
 },
 "text": {
  "div": "<div>\n Heart Rate Patient: Jane Smith Heart Rate (8867-4): 101 beats/minute \n</div>",
  "status": "generated"
 }
}
```
{% endtab %}
{% endtabs %}

## Aidbox Native Extensions

In addition to the FHIR extensions, Aidbox has its own implementation of extensions as native attributes.

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

