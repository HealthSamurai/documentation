# Custom resources using FHIR Schema

{% hint style="warning" %}
Custom resources are defined by individual organizations or projects to meet specific needs not covered by the FHIR standard. While these resources can be useful within a particular ecosystem, they may not be interoperable with other systems that do not recognize or support those custom resources.
{% endhint %}

FHIR Schema is a community project that aims to simplify the implementation and validation of FHIR (Fast Healthcare Interoperability Resources) resources across different programming languages. It also provides the ability to define custom resources. You can read more about the syntax and validation algorithms in the [FHIR Schema documentation](https://fhir-schema.github.io/fhir-schema/).

## Configure Aidbox

To begin using custom FHIR resources, enable the FHIRSchema validator engine in Aidbox.

{% content-ref url="../profiling-and-validation/fhir-schema-validator/setup-aidbox-with-fhir-schema-validation-engine.md" %}
[setup-aidbox-with-fhir-schema-validation-engine.md](../profiling-and-validation/fhir-schema-validator/setup-aidbox-with-fhir-schema-validation-engine.md)
{% endcontent-ref %}

## FHIR Schema endpoint

Aidbox provides the `/fhir/FHIRSchema` POST endpoint for creating FHIRSchema instances. Examples of usage are given in this article below.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/FHIRSchema
content-type: application/json
accept: application/json

<FHIRSchema JSON Body>
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status 200" %}
```
<FHIRSchema JSON Body>
```
{% endcode %}
{% endtab %}
{% endtabs %}

## FHIR Schema top level properties

To understand the meaning of the schemas described below, it is important to know the meaning of some basic FHIR Schema properties:

<table data-full-width="true"><thead><tr><th>Property</th><th>Description</th><th>Additional Notes</th></tr></thead><tbody><tr><td><code>base</code></td><td><p>This property contains the canonical URL to the FHIR Schema/StructureDefinition whose elements and constraints will be inherited.</p><p>It shares the same meaning as FHIR <code>StructureDefinition.baseDefinition</code></p></td><td><p>Usually, you want to set this property to <code>DomainResource</code>. This provides all the resource infrastructure, such as top-level <code>extension</code> collection and <code>meta</code>, which allows you to populate meta in the FHIR manner and add profile references.<br></p><p>If you omit this field, only the elements defined in the <code>elements</code> section of FHIR Schema will be validated. Aidbox does not perform any automatic inference of the <code>base</code> if it is omitted.</p></td></tr><tr><td><code>url</code>*</td><td><p>This field contains the unique canonical URL for your FHIRSchema.<br><br>In the context of custom resources, this field is not important. However, it becomes crucial when defining profiles, as you use the profile's canonical URL to reference it in the <code>meta.profile</code> of a resource instance.<br></p><p>It shares the same meaning as FHIR <code>StructureDefinition.url</code></p></td><td></td></tr><tr><td><code>name</code>*</td><td>This field provides the computer-readable name for your custom resource or profile.<br><br>It shares the same meaning as FHIR <code>StructureDefinition.name</code></td><td>When defining a new resource type, the current implementation requires that this field must be equal to both the <code>type</code> and <code>id</code>.</td></tr><tr><td><code>type</code></td><td>This property provides reource type name for a new resource definition or in case of profiling the type of resource that is being constrained.<br><br>It shares the same meaning as FHIR <code>StructureDefinition.type</code></td><td>When defining a new resource type, the current implementation requires that this field must be equal to both the <code>name</code> and <code>id</code>.</td></tr><tr><td><code>kind</code>*</td><td>This property is used to define the kind of structure that FHIRSchema is describing.<br><br>It shares the same meaning as FHIR <code>StructureDefinition.kind</code></td><td>To define <strong>custom resources</strong>, you should use <code>kind</code>: <code>resource</code> or <code>kind: logical</code></td></tr><tr><td><code>derivation</code>*</td><td>This property represents how the type relates to the <code>base</code> property. If it is set to <code>specialization</code> - Aidbox will create a new resource type with tables in the database and other resource infrastructure. If it is set to <code>constraint</code> - Aidbox will create a new profile that can be referenced on resource instances</td><td></td></tr><tr><td><code>id</code></td><td>It shares the same meaning as FHIR <code>Resource.id</code></td><td>When defining a new resource type, the current implementation requires that this field must be equal to both the <code>type</code> and <code>name</code>.</td></tr></tbody></table>

FHIR Schema may contain additional fields at the top level, but they share the same meaning as properties defined in the `elements` instruction.

For more information about these fields, please refer to the FHIR Schema reference [specification](https://fhir-schema.github.io/fhir-schema/index.html).

### Limitations of FHIR Logical Models

Logical models (StructureDefinition/FHIRSchema with kind: logical) have two limitations as guided by the FHIR specification:

1. **Base Value:**
   * The base value is limited to two possible values: `Element` or `base`.
2. **Type Value:**
   * The type value must be a fully specified URL and may share the same value as the URL property. This requirement is enforced by FHIR to avoid clashes with core resources.

Since FHIR Schema shares the same semantic meaning and purpose as StructureDefinition, it inherits all these limitations of the original StructureDefinition resource.

{% hint style="danger" %}
Due to the second limitation, resource definitions based on logical models are not intended for instantiation and are provided to an end FHIR server only as data structure examples.
{% endhint %}

## Create customs resources using FHIR Schemas

This guide will walk you through the process of creating custom resources for a notification system, demonstrating the typical workflow of creating, managing, and sending template-based notifications from a healthcare system to patients using custom resources defined through FHIRSchema.

Additionally, there is a [JavaScript application](https://github.com/Aidbox/app-examples/tree/main/aidbox-notify-via-custom-resources#aidbox-notify-via-custom-resources) that showcases the implementation of notification handling. This includes requesting notifications, locking them for processing, and completing the sending process.

To implement a notification flow, you may need a notification resource and a template resource to store your notification messages.

Let's start with shaping a TutorNotificationTemplate resource.

This resource contains one property defined under `FHIRSchema.elements`:

1. `template`: this property is for notification template text and is of the FHIR `string` data type. Also, it is a required property.

Also `FHIRSchema.derivation`: `specialization` is a property that tells the Aidbox to create a new resource instead of constraining an existing one with a new profile.

{% tabs %}
{% tab title="Request" %}
<pre class="language-json" data-line-numbers><code class="lang-json"><strong>POST /fhir/FHIRSchema
</strong>content-type: application/json
accept: application/json

{
  "url": "http://example.com/aidbox-sms-tutor/TutorNotificationTemplate",
  "type": "TutorNotificationTemplate",
  "name": "TutorNotificationTemplate",
  "base": "DomainResource",
  "kind": "resource",
  "derivation": "specialization",
  "required": [
    "template"
  ],
  "elements": {
    "template": {
      "type": "string",
      "scalar": true
    }
  }
}
</code></pre>
{% endtab %}

{% tab title="Response" %}
```json
Status: 200
{
 "derivation": "specialization",
 "name": "TutorNotificationTemplate",
 "type": "TutorNotificationTemplate",
 "resourceType": "FHIRSchema",
 "elements": {
  "template": {
   "type": "string",
   "scalar": true
  }
 },
 "id": "TutorNotificationTemplate",
 "kind": "resource",
 "url": "http://example.com/aidbox-sms-tutor/TutorNotificationTemplate",
 "base": "DomainResource",
 "required": [
  "template"
 ]
}
```
{% endtab %}
{% endtabs %}

Now, when a resource to store notification templates is available, a more complex resource can be shaped: the `TutorNotification`. This resource will include several key properties:

1. `type`: property that contains `binding` value set URL in `valueSet` property and `strength`: `required`, that is used to force binding validation.
2. `status`: property with `binding` to `valueSet: http://hl7.org/fhir/ValueSet/task-status` with additional constraint to `requested`, `in-progress` or `completed` values.
3. `template`: reference to `TutorNotificationTemplate` resource that we created above.
4. `message`: message text and is of the FHIR `string` data type.
5. `sendAfter`: property that specifies the `dateTime` after which this notification should be sent.
6. `subject`: reference to the `Patient` resource to whom this notification will be sent.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/FHIRSchema
content-type: application/json
accept: application/json

{
  "url": "http://example.com/aidbox-sms-tutor/TutorNotification",
  "type": "TutorNotification",
  "name": "TutorNotification",
  "base": "DomainResource",
  "kind": "resource",
  "derivation": "specialization",
  "required": [
    "sendAfter",
    "status",
    "subject",
    "template",
    "type"
  ],
  "elements": {
    "type": {
      "type": "string",
      "scalar": true,
      "binding": {
        "valueSet": "http://hl7.org/fhir/ValueSet/contact-point-system",
        "strength": "required"
      }
    },
    "status": {
      "type": "string",
      "scalar": true,
      "constraints": {
        "cont-status": {
          "human": "Status should be 'requested', 'in-progress' or 'completed'",
          "severity": "error",
          "expression": "%context='requested' or %context='in-progress' or %context='completed'"
        }
      },
      "binding": {
        "valueSet": "http://hl7.org/fhir/ValueSet/task-status",
        "strength": "required"
      }
    },
    "template": {
      "type": "Reference",
      "scalar": true,
      "refers": [
        "TutorNotificationTemplate"
      ]
    },
    "message": {
      "type": "string",
      "scalar": true
    },
    "sendAfter": {
      "type": "dateTime",
      "scalar": true
    },
    "subject": {
      "type": "Reference",
      "scalar": true,
      "refers": [
        "Patient"
      ]
    }
  }
}
```
{% endtab %}

{% tab title="Response" %}
```json
Status: 200
{
 "derivation": "specialization",
 "name": "TutorNotification",
 "type": "TutorNotification",
 "resourceType": "FHIRSchema",
 "elements": {
  "type": {
   "type": "string",
   "scalar": true,
   "binding": {
    "valueSet": "http://hl7.org/fhir/ValueSet/contact-point-system",
    "strength": "required"
   }
  },
  "status": {
   "type": "string",
   "scalar": true,
   "constraints": {
    "cont-status": {
     "human": "Status should be 'requested', 'in-progress' or 'completed'",
     "severity": "error",
     "expression": "%context='requested' or %context='in-progress' or %context='completed'"
    }
   },
   "binding": {
    "valueSet": "http://hl7.org/fhir/ValueSet/task-status",
    "strength": "required"
   }
  },
  "template": {
   "type": "Reference",
   "scalar": true,
   "refers": [
    "TutorNotificationTemplate"
   ]
  },
  "message": {
   "type": "string",
   "scalar": true
  },
  "sendAfter": {
   "type": "dateTime",
   "scalar": true
  },
  "subject": {
   "type": "Reference",
   "scalar": true,
   "refers": [
    "Patient"
   ]
  }
 },
 "id": "TutorNotification",
 "kind": "resource",
 "url": "http://example.com/aidbox-sms-tutor/TutorNotification",
 "base": "DomainResource",
 "required": [
  "sendAfter",
  "status",
  "subject",
  "template",
  "type"
 ]
}
```
{% endtab %}
{% endtabs %}

## Define search parameters

With defined resources, most of the work is done, but there is one missing aspect of any FHIR resource. You definitely want to check your requested notifications or include related subjects to the search bundle. Aidbox allows you to define SearchParameter resources in addition to custom resources.

Let's create the search parameters mentioned above.

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

This one defines the `expression` to achieve resource status, which allows you to search for TutorNotification resources by status like this:

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

The other one is used to include related Patient resources to the search bundle.

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

It allows you to make following requests:

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

## Interact with a resource

Now you can interact with created resources just like with any other FHIR resources.

Let's create an instance of `TutorNotificationTemplate` resource with welcome message based on related patient's given name.

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

Then we probably want to create some patient:

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

So request that creates welcome sms notification for James Morgan at 12:00 should look like this:

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

{% tab title="Response" %}
```json
Status: 201
{
 "meta": {
  "lastUpdated": "2024-07-24T11:43:35.415536Z",
  "versionId": "283",
  "extension": [
   {
    "url": "http://example.com/createdat",
    "valueInstant": "2024-07-24T11:43:35.415536Z"
   }
  ]
 },
 "type": "sms",
 "template": {
  "reference": "TutorNotificationTemplate/welcome"
 },
 "resourceType": "TutorNotification",
 "status": "requested",
 "id": "17843645-27c2-4e6b-9681-19f2aa526ce7",
 "sendAfter": "2024-07-12T12:00:00.000Z",
 "subject": {
  "reference": "Patient/pt-1"
 }
}
```
{% endtab %}
{% endtabs %}

## How Aidbox Deals with FHIR Limitations for Custom Resources

### **FHIR Type ValueSet Bindings**

FHIR defines certain ValueSets that list all resource types and binds them with required strength to some properties. For example, the `SearchParameter.base` property points to the resource this `SearchParameter` is intended for and has this exact binding. Obviously, your custom resource type is not mentioned in this ValueSet. However, you still want to create and use search parameters for your custom resources.

During validation, Aidbox checks whether the resource type is in the given ValueSet or if it is a known custom resource for Aidbox. This allows you to use custom resources in resources like `CapabilityStatement` or `SearchParameter`, or in the `type` property of references.

### **References to Unknown FHIR Types**

FHIR allows references to point only to FHIR resources. Aidbox, however, allows you to specify custom resources in reference targets as well.

### **Bundle Entries Must Inherit from Resource FHIR Type**

FHIR explicitly states that `Bundle.entry.resource` must be a type that inherits from the Resource FHIR type. Aidbox relaxes this constraint and checks that the referenced resource inherits from at least one `StructureDefinition`/`FHIRSchema` with `kind: resource` and `derivation: specialization`.

\\
