# Custom resources using FHIRSchema

{% hint style="warning" %}
Custom resources are not interoperable with FHIR.
{% endhint %}

FHIRSchema is a project that aims to simplify the implementation and validation of FHIR (Fast Healthcare Interoperability Resources) resources across different programming languages. It also provides the ability to define custom resources. You can read more about the syntax and validation algorithms in the [FHIRSchema documentation](https://fhir-schema.github.io/fhir-schema/).

## Configure Aidbox

To begin using custom FHIR resources, enable the FHIRSchema validator engine in Aidbox.

{% content-ref url="../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md" %}
[setup.md](../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md)
{% endcontent-ref %}

## FHIRSchema top level properties

To understand the meaning of the schemas described below, you should know the meaning of some basic FHIRSchema properties:

1. `base`: This property defines the base profile from which schema will inherit all _elements_ and _constraints._
2. `url`: This property represents the FHIRSchema canonical URL. You can use it to link to the current profile as a base for other profiles. It is **required** in every FHIRSchema instance and is used in the same sense as a FHIR `StructureDefinition.url` property.&#x20;
3. `name`: This property is a machine-readable name for the profile. It can be used as an alias.
4. `type`: This property represents the resource type that the current profile restricts. In the case of custom resources, the value of this property should match the `name` property. It is **required** in every FHIRSchema instance and is used in the same sense as a FHIR `StructureDefinition.type` property.
5. `kind`: This property is used to define the kind of structure that FHIRSchema is describing. It is used in the same sense as a FHIR `StructureDefinition.kind` property. \
   To define **custom resources**, you should use `kind`: `resource`.
6. `derivation`: This property represents how the type relates to the `base` property. If it is set to `specialization` - Aidbox will create a new resource type with tables in the database and other resource logic. If it is set to `constraint` - Aidbox will create a new profile that constrains the resource type described on the base field. Otherwise, it has the same meaning as a FHIR `StructureDefinition.derivation` property.
7. `required|excluded`: This properties controls the presence or absence of required and excluded subelements.
8. `elements`: This property is where constraints on schema elements are defined. There are nested properties to constrain shape, cardinality, slicing, etc. For a more complex description, see the [FHIRSchema wiki](https://fhir-schema.github.io/fhir-schema/reference/element.html).

## Create FHIRSchema for custom resource

Let's define several resources to describe the typical notification flow. This guide is also available as a Javascript application in the [examples repository](https://github.com/Aidbox/app-examples/tree/main/aidbox-notify-via-custom-resources).

To implement a notification flow, you may need a notification resource and a template resource to store your notification messages.

Let's start with shaping a TutorNotificationTemplate resource.

{% code lineNumbers="true" %}
```yaml
POST /FHIRSchema
content-type: text/yaml
accept: text/yaml

id: TutorNotificationTemplate
resourceType: FHIRSchema
url: "http://example.com/aidbox-sms-tutor/TutorNotificationTemplate"
type: TutorNotificationTemplate
name: TutorNotificationTemplate
base: DomainResource
kind: resource
derivation: specialization
required:
  - template
elements:
  template:
    type: string
    scalar: true
```
{% endcode %}

Most important part of this schema is template constraint, which says that element `template` is required to be included in resource and its value should be _string_ type. Also `kind`: `resource` is a property that tells the Aidbox to create a new resource instead of constraining an existing one with a new profile.

Now, when we got resource to store our templates, let's shape more complex one.

```yaml
POST /fhir/FHIRSchema
content-type: text/yaml
accept: text/yaml

id: TutorNotification
resourceType: FHIRSchema
url: "http://example.com/aidbox-sms-tutor/TutorNotification"
type: TutorNotification
name: TutorNotification
base: DomainResource
kind: resource
derivation: specialization
required:
  - sendAfter
  - status
  - subject
  - template
  - type
elements:
  type:
    type: string
    scalar: true
    binding:
      valueSet: "http://hl7.org/fhir/ValueSet/contact-point-system"
      strength: required
  status:
    type: string
    scalar: true
    constraints:
      cont-status:
        human: "Status should be 'requested', 'in-progress' or 'completed'"
        severity: "error"
        expression: "%context='requested' or %context='in-progress' or %context='completed'"
    binding:
      valueSet: "http://hl7.org/fhir/ValueSet/task-status"
      strength: required
  template:
    type: Reference
    scalar: true
    refers: ["TutorNotificationTemplate"]
  message:
    type: string
    scalar: true
  sendAfter:
    type: dateTime
    scalar: true
  subject:
    type: Reference
    scalar: true
    refers: ["Patient"]
```

In addition to properties from the FHIRSchema of notification template, in this one we use   _value sets_ and _references_. To link our template with notification, we need to define the `template` element with type `Reference` and allow `TutorNotificationTemplate` type in `refers` property. More interesting one here is `type` and `status` elements. There both have terminology `binding` property that contains value set URL in `valueSet` property and `strength`: `required`, that is used to force binding validation. But in case of status we don't want to use all _task-status codes_ to specify notification status. This is the reason why the `constraints` property appears. It is used  to express that status of notification **shall be** `requested`, `in-progress` or `completed`.&#x20;



## Create Search Parameters on custom resources

With defined resources, most of the work is done, but there is one missing aspect of any FHIR resource. You definitely want to check your requested notifications or include related subjects to the search bundle. Aidbox allows you to define SearchParameter resources in addition to custom resources.

Let's create the search parameters mentioned above.

```yaml
POST /fhir/SearchParameter
content-type: text/yaml
accept: text/yaml

resourceType: SearchParameter
id: TutorNotification-status
url: http://example.com/aidbox-sms-tutor/TutorNotification-status
version: 0.0.1
status: draft

name: status
code: status
base:
  - TutorNotification
type: token
description: Search TutorNotification by status
expression: TutorNotification.status
```

This one defines the `expression` to achieve resource status, which allows you to search for TutorNotification resources by status like this:

```http
GET /fhir/TutorNotification?status=requested
```

The other one is used to include related Patient resources to the search bundle.

```yaml
POST /fhir/SearchParameter
content-type: text/yaml
accept: text/yaml

resourceType: SearchParameter
id: TutorNotification-subject
url: http://example.com/aidbox-sms-tutor/TutorNotification-subject
version: 0.0.1
status: draft

name: subject
code: subject
base:
  - TutorNotification
type: reference
description: Search TutorNotification by subject
expression: TutorNotification.subject
```

It allows you to make following requests:

```
GET /fhir/TutorNotification?_include=TutorNotification:subject:Patient
```

## Interact with a resource

Now you can interact with created resources just like with any other FHIR resources.

Let's create an instance of `TutorNotificationTemplate` resource with welcome message based on related patient's given name.

```yaml
POST /fhir/TutorNotificationTemplate
content-type: text/yaml
accept: text/yaml

id: welcome
resourceType: TutorNotificationTemplate
template: |
  Hello user name: {{patient.name.given}}
```

Then we probably want to create some patient:

```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

id: pt-1
name:
- given:
  - James
  family: Morgan
resourceType: Patient
```

So request that creates welcome sms notification for James Morgan at 12:00 should look like this:

```yaml
POST /fhir/TutorNotification
content-type: text/yaml
accept: text/yaml

resourceType: TutorNotification
type: sms
status: requested
template: 
    reference: TutorNotificationTemplate/welcome
sendAfter: 2024-07-12T12:00:00Z
subject:
    reference: Patient/pt-1
```
