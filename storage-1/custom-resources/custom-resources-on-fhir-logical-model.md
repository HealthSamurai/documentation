# Custom resources using StructureDefinition

{% hint style="warning" %}
Custom resources are defined by individual organizations or projects to meet specific needs not covered by the FHIR standard. While these resources can be useful within a particular ecosystem, they may not be interoperable with other systems that do not recognize or support those custom resources.
{% endhint %}

FHIR uses StructureDefinition resources to model data structures. Aidbox allows you to model data in the same way by expressing your custom resource using a FHIR StructureDefinition resource.

## Configure Aidbox

To begin using custom FHIR resources, enable the FHIR Schema validation engine in Aidbox.

{% content-ref url="../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md" %}
[setup.md](../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md)
{% endcontent-ref %}

## Limitations of FHIR Logical Models

Logical models (StructureDefinition/FHIRSchema with `kind: logical`) have two limitations as guided by the FHIR specification:

1. **Base Value:**
   * The base value is limited to two possible values: `Element` or `base`.
2. **Type Value:**
   * The type value must be a fully specified URL and may share the same value as the URL property. This requirement is enforced by FHIR to avoid clashes with core resources.

{% hint style="danger" %}
Due to the second limitation, resource definitions based on logical models are not intended for instantiation and are provided to an end FHIR server only as data structure examples.&#x20;
{% endhint %}

## Create StructureDefinition for custom resource

To create a custom resource in Aidbox using StructureDefinition you have to create StructureDefinition resource via REST API.&#x20;

It is a usual FHIR [StructureDefinition resource](https://www.hl7.org/fhir/structuredefinition.html), but with several limitations:

1. `name`, `type`, and `id` must be equal.
2. &#x20;`kind`: must be equal to `resource` or `logical`
3. `derivation`: If it is set to `specialization` - Aidbox will create a new resource type with tables in the database and other resource infrastructure. If it is set to `constraint` - Aidbox will create a new profile that can be referenced on resource instances.

The further guide details the process of creating custom resources for a notification system, demonstrating the typical workflow of creating, managing, and sending template-based notifications from a healthcare system to patients using custom resources defined through StructureDefinition.

To implement a notification flow, you may need a notification resource and a template resource to store your notification messages.

Let's start with shaping a TutorNotificationTemplate resource.

This resource contains one property defined under `StructureDefinition.differential`:

1. `template`: This property is for notification template text and is of the FHIR `string` data type. Also, it is a required property.

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/StructureDefinition
content-type: application/json
accept: application/json

{
    "derivation": "specialization",
    "name": "TutorNotificationTemplate",
    "abstract": false,
    "type": "TutorNotificationTemplate",
    "status": "active",
    "kind": "resource",
    "url": "http://example.org/StructureDefinition/TutorNotificationTemplate",
    "baseDefinition": "http://hl7.org/fhir/StructureDefinition/DomainResource",
    "differential": {
        "element": [
            {
                "id": "TutorNotificationTemplate",
                "path": "TutorNotificationTemplate",
                "min": 0,
                "max": "*"
            },
            {
                "id": "TutorNotificationTemplate.template",
                "path": "TutorNotificationTemplate.template",
                "min": 1,
                "max": "1",
                "type": [
                    {
                        "code": "string"
                    }
                ]
            }
        ]
    }
}
```
{% endtab %}

{% tab title="Response" %}
{% code title="status: 200" %}
```json
{
    "derivation": "specialization",
    "name": "TutorNotificationTemplate",
    "abstract": false,
    "type": "TutorNotificationTemplate",
    "resourceType": "StructureDefinition",
    "status": "active",
    "id": "TutorNotificationTemplate",
    "kind": "resource",
    "url": "http://example.org/StructureDefinition/TutorNotificationTemplate",
    "baseDefinition": "http://hl7.org/fhir/StructureDefinition/DomainResource",
    "differential": {
        "element": [
            {
                "id": "TutorNotificationTemplate",
                "path": "TutorNotificationTemplate",
                "min": 0,
                "max": "*"
            },
            {
                "id": "TutorNotificationTemplate.template",
                "path": "TutorNotificationTemplate.template",
                "min": 1,
                "max": "1",
                "type": [
                    {
                        "code": "string"
                    }
                ]
            }
        ]
    }
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Now, when we got resource to store our templates, let's shape more complex one - resource `TutorNotification` that has following properties:

1. `type`: property that contains `binding` value set URL in `valueSet` property and `strength`: `required`, that is used to force binding validation.
2. `status`:  property with `binding` to `valueSet: http://hl7.org/fhir/ValueSet/task-status` with additional constraint to `requested`, `in-progress` or `completed` values.
3. `template`:  reference to `TutorNotificationTemplate` that we created above.
4. `message`: message text and is of the FHIR `string` data type.
5. `sendAfter`: property that specifies the `dateTime` after which this notification should be sent.
6. `subject`: reference to the `Patient` resource to whom this notification will be sent.

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/StructureDefinition
content-type: application/json
accept: application/json

{
  "url": "http://example.com/aidbox-sms-tutor/TutorNotification",
  "name": "TutorNotification",
  "status": "active",
  "kind": "resource",
  "abstract": false,
  "type": "TutorNotification",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/DomainResource",
  "derivation": "specialization",
  "differential": {
    "element": [
      {
        "id": "TutorNotification",
        "path": "TutorNotification",
        "min": 0,
        "max": "*"
      },
      {
        "id": "TutorNotification.type",
        "path": "TutorNotification.type",
        "min": 1,
        "max": "1",
        "type": [
          {
            "code": "string"
          }
        ],
        "binding": {
          "strength": "required",
          "valueSet": "http://hl7.org/fhir/ValueSet/contact-point-system"
        }
      },
      {
        "id": "TutorNotification.status",
        "path": "TutorNotification.status",
        "min": 1,
        "max": "1",
        "type": [
          {
            "code": "string"
          }
        ],
        "constraint": [
          {
            "key": "cont-status",
            "severity": "error",
            "human": "Status should be 'requested', 'in-progress' or 'completed'",
            "expression": "%context='requested' or %context='in-progress' or %context='completed'"
          }
        ],
        "binding": {
          "strength": "required",
          "valueSet": "http://hl7.org/fhir/ValueSet/task-status"
        }
      },
      {
        "id": "TutorNotification.template",
        "path": "TutorNotification.template",
        "min": 1,
        "max": "1",
        "type": [
          {
            "code": "Reference",
            "targetProfile": [
              "http://example.com/aidbox-sms-tutor/TutorNotificationTemplate"
            ]
          }
        ]
      },
      {
        "id": "TutorNotification.message",
        "path": "TutorNotification.message",
        "min": 0,
        "max": "1",
        "type": [
          {
            "code": "string"
          }
        ]
      },
      {
        "id": "TutorNotification.sendAfter",
        "path": "TutorNotification.sendAfter",
        "min": 1,
        "max": "1",
        "type": [
          {
            "code": "dateTime"
          }
        ]
      },
      {
        "id": "TutorNotification.subject",
        "path": "TutorNotification.subject",
        "min": 1,
        "max": "1",
        "type": [
          {
            "code": "Reference",
            "targetProfile": [
              "http://hl7.org/fhir/StructureDefinition/Patient"
            ]
          }
        ]
      }
    ]
  }
}
```
{% endtab %}

{% tab title="Response" %}
{% code title="status: 200" %}
```json
{
 "derivation": "specialization",
 "name": "TutorNotification",
 "abstract": false,
 "type": "TutorNotification",
 "resourceType": "StructureDefinition",
 "status": "active",
 "id": "TutorNotification",
 "kind": "resource",
 "url": "http://example.com/aidbox-sms-tutor/TutorNotification",
 "differential": {
  "element": [
   {
    "id": "TutorNotification",
    "path": "TutorNotification",
    "min": 0,
    "max": "*"
   },
   {
    "id": "TutorNotification.type",
    "path": "TutorNotification.type",
    "min": 1,
    "max": "1",
    "type": [
     {
      "code": "string"
     }
    ],
    "binding": {
     "strength": "required",
     "valueSet": "http://hl7.org/fhir/ValueSet/contact-point-system"
    }
   },
   {
    "id": "TutorNotification.status",
    "path": "TutorNotification.status",
    "min": 1,
    "max": "1",
    "type": [
     {
      "code": "string"
     }
    ],
    "constraint": [
     {
      "key": "cont-status",
      "severity": "error",
      "human": "Status should be 'requested', 'in-progress' or 'completed'",
      "expression": "%context='requested' or %context='in-progress' or %context='completed'"
     }
    ],
    "binding": {
     "strength": "required",
     "valueSet": "http://hl7.org/fhir/ValueSet/task-status"
    }
   },
   {
    "id": "TutorNotification.template",
    "path": "TutorNotification.template",
    "min": 1,
    "max": "1",
    "type": [
     {
      "code": "Reference",
      "targetProfile": [
       "http://example.com/aidbox-sms-tutor/TutorNotificationTemplate"
      ]
     }
    ]
   },
   {
    "id": "TutorNotification.message",
    "path": "TutorNotification.message",
    "min": 0,
    "max": "1",
    "type": [
     {
      "code": "string"
     }
    ]
   },
   {
    "id": "TutorNotification.sendAfter",
    "path": "TutorNotification.sendAfter",
    "min": 1,
    "max": "1",
    "type": [
     {
      "code": "dateTime"
     }
    ]
   },
   {
    "id": "TutorNotification.subject",
    "path": "TutorNotification.subject",
    "min": 1,
    "max": "1",
    "type": [
     {
      "code": "Reference",
      "targetProfile": [
       "http://hl7.org/fhir/StructureDefinition/Patient"
      ]
     }
    ]
   }
  ]
 },
 "baseDefinition": "http://hl7.org/fhir/StructureDefinition/DomainResource"
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

## Define search parameters&#x20;

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/TutorNotification
content-type: application/json
accept: application/json

{
  "resourceType": "TutorNotification",
  "type": "sms",
  "status": "requested",
  "template": {
    "reference": "TutorNotificationTemplate/welcome"
  },
  "sendAfter": "2024-07-12T12:00:00.000Z",
  "subject": {
    "reference": "Patient/pt-1"
  }
}
```
{% endtab %}
{% endtabs %}

So request that creates welcome sms notification for James Morgan at 12:00 should look like this:

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/Patient
content-type: application/json
accept: application/json

{
  "id": "pt-1",
  "name": [
    {
      "given": [
        "James"
      ],
      "family": "Morgan"
    }
  ],
  "resourceType": "Patient"
}
```
{% endtab %}

{% tab title="Response" %}
```json
Status: 201
{
 "name": [
  {
   "given": [
    "James"
   ],
   "family": "Morgan"
  }
 ],
 "id": "pt-1",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2024-07-24T11:41:44.014328Z",
  "versionId": "281",
  "extension": [
   {
    "url": "http://example.com/createdat",
    "valueInstant": "2024-07-24T11:41:44.014328Z"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

Then we probably want to create some patient:

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/TutorNotificationTemplate
content-type: application/json
accept: application/json

{
  "id": "welcome",
  "resourceType": "TutorNotificationTemplate",
  "template": "Hello user name: {{patient.name.given}}\n"
}
```
{% endtab %}

{% tab title="Response" %}
```json
Status: 201
{
 "template": "Hello user name: {{patient.name.given}}\n",
 "id": "welcome",
 "resourceType": "TutorNotificationTemplate",
 "meta": {
  "lastUpdated": "2024-07-24T11:40:16.461445Z",
  "versionId": "275",
  "extension": [
   {
    "url": "http://example.com/createdat",
    "valueInstant": "2024-07-24T11:40:16.461445Z"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

Let's create an instance of `TutorNotificationTemplate` resource with welcome message based on related patient's given name.

Now you can interact with created resources just like with any other FHIR resources.

## Interact with a resource

{% tabs %}
{% tab title="Request" %}
```json
GET /fhir/TutorNotification?_include=TutorNotification:subject:Patient
```
{% endtab %}

{% tab title="Response" %}
```json
Status: 200
{
 "resourceType": "Bundle",
 "type": "searchset",
 "meta": {
  "versionId": "0"
 },
 "total": 1,
 "link": [
  {
   "relation": "first",
   "url": "http://localhost:8765/fhir/TutorNotification?_include=TutorNotification:subject:Patient&page=1"
  },
  {
   "relation": "self",
   "url": "http://localhost:8765/fhir/TutorNotification?_include=TutorNotification:subject:Patient&page=1"
  }
 ],
 "entry": [
  {
   "resource": {
    "type": "sms",
    "status": "requested",
    "subject": {
     "reference": "Patient/pt-1"
    },
    "template": {
     "reference": "TutorNotificationTemplate/welcome"
    },
    "sendAfter": "2024-07-12T12:00:00Z",
    "id": "c4d1afd5-0a38-4dd5-843d-85834e23243c",
    "resourceType": "TutorNotification",
    "meta": {
     "lastUpdated": "2024-07-24T11:31:26.091674Z",
     "versionId": "261",
     "extension": [
      {
       "url": "http://example.com/createdat",
       "valueInstant": "2024-07-24T11:31:26.091674Z"
      }
     ]
    }
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "http://localhost:8765/TutorNotification/c4d1afd5-0a38-4dd5-843d-85834e23243c",
   "link": [
    {
     "relation": "self",
     "url": "http://localhost:8765/TutorNotification/c4d1afd5-0a38-4dd5-843d-85834e23243c"
    }
   ]
  },
  {
   "resource": {
    "name": [
     {
      "given": [
       "James"
      ],
      "family": "Morgan"
     }
    ],
    "id": "pt-1",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2024-07-23T11:07:35.330824Z",
     "versionId": "225",
     "extension": [
      {
       "url": "http://example.com/createdat",
       "valueInstant": "2024-07-23T11:07:35.330824Z"
      }
     ]
    }
   },
   "search": {
    "mode": "include"
   },
   "fullUrl": "http://localhost:8765/Patient/pt-1",
   "link": [
    {
     "relation": "self",
     "url": "http://localhost:8765/Patient/pt-1"
    }
   ]
  }
 ]
}
```
{% endtab %}
{% endtabs %}

It allows you to make following requests:

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/SearchParameter
content-type: application/json
accept: application/json

{
  "resourceType": "SearchParameter",
  "id": "TutorNotification-subject",
  "url": "http://example.com/aidbox-sms-tutor/TutorNotification-subject",
  "version": "0.0.1",
  "status": "draft",
  "name": "subject",
  "code": "subject",
  "base": [
    "TutorNotification"
  ],
  "type": "reference",
  "description": "Search TutorNotification by subject",
  "expression": "TutorNotification.subject"
}
```
{% endtab %}

{% tab title="Response" %}
```json
{
 "description": "Search TutorNotification by subject",
 "expression": "TutorNotification.subject",
 "name": "subject",
 "type": "reference",
 "resourceType": "SearchParameter",
 "status": "draft",
 "id": "TutorNotification-subject",
 "url": "http://example.com/aidbox-sms-tutor/TutorNotification-subject",
 "code": "subject",
 "base": [
  "TutorNotification"
 ],
 "version": "0.0.1"
}
```
{% endtab %}
{% endtabs %}

The other one is used to include related Patient resources to the search bundle.

{% tabs %}
{% tab title="Request" %}
```json
GET /fhir/TutorNotification?status=requested
```
{% endtab %}

{% tab title="Response" %}
```json
{
 "resourceType": "Bundle",
 "type": "searchset",
 "meta": {
  "versionId": "0"
 },
 "total": 1,
 "link": [
  {
   "relation": "first",
   "url": "http://localhost:8765/fhir/TutorNotification?status=requested&page=1"
  },
  {
   "relation": "self",
   "url": "http://localhost:8765/fhir/TutorNotification?status=requested&page=1"
  }
 ],
 "entry": [
  {
   "resource": {
    "type": "sms",
    "status": "requested",
    "subject": {
     "reference": "Patient/pt-1"
    },
    "template": {
     "reference": "TutorNotificationTemplate/welcome"
    },
    "sendAfter": "2024-07-12T12:00:00Z",
    "id": "c4d1afd5-0a38-4dd5-843d-85834e23243c",
    "resourceType": "TutorNotification",
    "meta": {
     "lastUpdated": "2024-07-24T11:31:26.091674Z",
     "versionId": "261",
     "extension": [
      {
       "url": "http://example.com/createdat",
       "valueInstant": "2024-07-24T11:31:26.091674Z"
      }
     ]
    }
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "http://localhost:8765/TutorNotification/c4d1afd5-0a38-4dd5-843d-85834e23243c",
   "link": [
    {
     "relation": "self",
     "url": "http://localhost:8765/TutorNotification/c4d1afd5-0a38-4dd5-843d-85834e23243c"
    }
   ]
  }
 ]
}
```
{% endtab %}
{% endtabs %}

This one defines the `expression` to achieve resource status, which allows you to search for TutorNotification resources by status like this:

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/SearchParameter
content-type: application/json
accept: application/json

{
  "resourceType": "SearchParameter",
  "id": "TutorNotification-status",
  "url": "http://example.com/aidbox-sms-tutor/TutorNotification-status",
  "version": "0.0.1",
  "status": "draft",
  "name": "status",
  "code": "status",
  "base": [
    "TutorNotification"
  ],
  "type": "token",
  "description": "Search TutorNotification by status",
  "expression": "TutorNotification.status"
}
```
{% endtab %}

{% tab title="Response" %}
```json
Status: 200
{
 "description": "Search TutorNotification by status",
 "expression": "TutorNotification.status",
 "name": "status",
 "type": "token",
 "resourceType": "SearchParameter",
 "status": "draft",
 "id": "TutorNotification-status",
 "url": "http://example.com/aidbox-sms-tutor/TutorNotification-status",
 "code": "status",
 "base": [
  "TutorNotification"
 ],
 "version": "0.0.1"
}
```
{% endtab %}
{% endtabs %}

Let's create the search parameters mentioned above.

With defined resources, most of the work is done, but there is one missing aspect of any FHIR resource. You definitely want to check your requested notifications or include related subjects to the search bundle. Aidbox allows you to define SearchParameter resources in addition to custom resources.

## Convenience

Manually writing StructureDefinitions can be overwhelming. Fortunately, there is an alternative: FSH/SUSHI allows you to generate a FHIR NPM package with your custom resources, which you can load into Aidbox and use.

{% content-ref url="../../modules-1/profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../../modules-1/profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/)
{% endcontent-ref %}

## How Aidbox Deals with FHIR Limitations for Custom Resources

### **FHIR Type ValueSet Bindings**

FHIR defines certain ValueSets that list all resource types and binds them with required strength to some properties. For example, the `SearchParameter.base` property points to the resource this `SearchParameter` is intended for and has this exact binding. Obviously, your custom resource type is not mentioned in this ValueSet. However, you still want to create and use search parameters for your custom resources.

During validation, Aidbox checks whether the resource type is in the given ValueSet or if it is a known custom resource for Aidbox. This allows you to use custom resources in resources like `CapabilityStatement` or `SearchParameter`, or in the `type` property of references.

### **References to Unknown FHIR Types**

FHIR allows references to point only to FHIR resources. Aidbox, however, allows you to specify custom resources in reference targets as well.

### **Bundle Entries Must Inherit from Resource FHIR Type**

FHIR explicitly states that `Bundle.entry.resource` must be a type that inherits from the Resource FHIR type. Aidbox relaxes this constraint and checks that the referenced resource inherits from at least one `StructureDefinition`/`FHIRSchema` with `kind: resource` and `derivation: specialization`.
