# First-Class Extensions using Attribute

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](../../../modules/profiling-and-validation/fhir-schema-validator/README.md)
{% endhint %}

In Aidbox there are two ways to define first-class extensions:

* Using [Zen profiles](first-class-extensions-using-attribute.md#first-class-extension-as-zen-profile)
* Using [Attribute](first-class-extensions-using-attribute.md#define-new-extension)

If you're using Aidbox with zen, all resources will be validated by zen, not Attributes. In this case, you should use zen profiles.

If you don't use zen, you can use Attributes.

## Define new extension with Attribute

In Aidbox, you can define first-class extensions using the custom resource [Attribute](entities-and-attributes.md).

Let's create an extension definition of type `Reference` in the [REST Console](broken-reference) of Aidbox:

{% code title="Create new extension in the Aidbox format:" %}
```yaml
PUT /Attribute/ServiceRequest.requestedOrganizationDepartment

description: Department in the organization that made the service request
resource: {id: ServiceRequest, resourceType: Entity}
path: [requestedOrganizationDepartment]
type: {id: Reference, resourceType: Entity}
refers: 
  - Organization
extensionUrl: urn:extension:requestedOrganizationDepartment
```
{% endcode %}

*   `description` - _string_.

    Attribute text description
*   `resource` - **required**, _Reference(Entity)._

    Reference to a target resource type
*   `path` - **required**, an _array of strings._

    Path to a new attribute location in the target resource type
*   `type` - _Reference(Entity)_.

    Type of data stored in this attribute. It can be any primitive or complex type.

    If omitted, treated as`BackboneElement`, i.e. a complex-type object with structure defined via other Attributes relying on this attribute in their `path`
*   `isCollection` - _boolean_.

    Whether the attribute is a collection, i.e. if `true` sets attribute cardinality to `..*`
*   `isRequired` - _boolean_.

    Whether the attribute is required, i.e. if `true` sets attribute cardinality to `1..`
* `isUnique` - _boolean_.\
  Sets unique restriction on the attribute.
*   `refers` - _Reference(Entity)_.

    Only for type=Reference. Specifies to which resourceTypes this reference can refer to
*   `extensionUrl` - _string_.

    URL which will be used to create `extension` element in FHIR format. **If omitted, the Attribute won't be transformed into the FHIR format**

{% hint style="info" %}
Note: you can not use Attributes and zen profiles on the same resource at the same time
{% endhint %}

## Use your extension

Now, you can create the `ServiceRequest` resource using the new attribute `requestedOrganizationDepartment` in the root of the resource:

{% code title="Create resource in the Aidbox format:" %}
```yaml
PUT /ServiceRequest/servicerequest-with-aidbox-native-extensions

requestedOrganizationDepartment:
  resourceType: Organization
  display: City Hospital Neurology Department
  identifier: {system: 'urn:oid:1.2.3.4.5.6.7.8', value: '456'}
category:
- coding:
  - {code: '183829003', 
    system: 'http://snomed.info/sct', 
    display: "Refer for imaging"}
authoredOn: '2020-01-18T15:08:00'
resourceType: ServiceRequest
requester:
  resourceType: PractitionerRole
  display: Din Morgan
  identifier: {value: "1760", system: 'urn:oid:1.2.3.4.5.6.7.6'}
priority: routine
status: active
intent: original-order
performer:
- resourceType: Organization
  display: Aga Khan University Hospital Laboratory
  identifier: {value: "1514", system: 'urn:oid:1.2.3.4.5.6.7.8'}
subject:
  resourceType: Patient
  display: Jack Black, 10.12.1941
  identifier: {value: '2155800871000065', system: 'urn:oid:1.2.3.4.5.6.7.9'}
```
{% endcode %}

## FHIR Format

You can get the resource in the FHIR format where our new attribute is rendered as the `extension` element:

{% hint style="warning" %}
Note that the URL is different:`GET /fhir/ServiceRequest/`
{% endhint %}

{% tabs %}
{% tab title="Request" %}
{% code title="Get the resource in the FHIR format:" %}
```http
GET /fhir/ServiceRequest/servicerequest-with-aidbox-native-extensions
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
```yaml
extension:
- url: urn:extension:requestedOrganizationDepartment
  valueReference:
    type: Organization
    identifier: {value: '456', system: 'urn:oid:1.2.3.4.5.6.7.8'}
    display: City Hospital Neurology Department
category:
- coding:
  - {code: '183829003', 
    system: 'http://snomed.info/sct', 
    display: "Refer for imaging"}
meta:
  lastUpdated: '2020-02-04T09:15:18.664293Z'
  versionId: '32'
  extension:
  - {url: 'ex:createdAt', valueInstant: '2020-02-04T09:12:20.103301Z'}
authoredOn: '2020-01-18T15:08:00'
resourceType: ServiceRequest
requester:
  type: PractitionerRole
  identifier: {value: '1760', system: 'urn:oid:1.2.3.4.5.6.7.6'}
  display: Din Morgan
priority: routine
status: active
id: servicerequest-with-aidbox-native-extensions
intent: original-order
subject:
  type: Patient
  identifier: {value: '2155800871000065', 
              system: 'urn:oid:1.2.3.4.5.6.7.9'}
  display: Jack Black, 10.12.1941
performer:
- type: Organization
  identifier: {value: '1514', system: 'urn:oid:1.2.3.4.5.6.7.8'}
  display: Aga Khan University Hospital Laboratory
```
{% endtab %}
{% endtabs %}

## Other extension examples

Let's create an extension of type `Reference` and list allowed resource types (Organization in that case) in the `refers` property.

{% code title="managingOrganization" %}
```yaml
PUT /Attribute/ServiceRequest.managingOrganization

resourceType: Attribute
description: Organization that made the service request
resource: {id: ServiceRequest, resourceType: Entity}
path: [managingOrganization]
id: ServiceRequest.managingOrganization
type: {id: Reference, resourceType: Entity}
refers: 
  - Organization
extensionUrl: urn:extension:requestedOrganization
```
{% endcode %}

Extension of type Coding:

{% code title="paymentType" %}
```yaml
PUT /Attribute/ServiceRequest.paymentType

resourceType: Attribute
description: Payment type for the service request, e.g. government health insurance
id: ServiceRequest.paymentType
resource:
  id: ServiceRequest
  resourceType: Entity
path:
- paymentType
type:
  id: Coding
  resourceType: Entity
extensionUrl: urn:extension:paymentType
```
{% endcode %}

{% code title="Create resource using these extensions:" %}
```yaml
PUT /ServiceRequest/servicerequest-with-aidbox-native-extensions

managingOrganization:
  resourceType: Organization
  display: City Hospital
  identifier: {value: "123", system: 'urn:oid:1.2.3.4.5.6.7.8'}
requestedOrganizationDepartment:
  resourceType: Organization
  display: City Hospital Neurology Department
  identifier: {system: 'urn:oid:1.2.3.4.5.6.7.8', value: '456'}
paymentType: {system: 'urn:CodeSystem:paymentType', 
              code: "1", 
              display: "Government Health Insurance"}
resourceType: ServiceRequest
status: active
intent: original-order
subject:
  resourceType: Patient
  display: Jack Black, 10.12.1941
  identifier: {value: '2155800871000065', system: 'urn:oid:1.2.3.4.5.6.7.9'}
```
{% endcode %}

{% tabs %}
{% tab title="Request" %}
{% code title="Get resource in the FHIR format:" %}
```yaml
GET /fhir/ServiceRequest/servicerequest-with-aidbox-native-extensions
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ServiceRequest
extension:
- url: urn:extension:requestedOrganization
  valueReference:
    type: Organization
    identifier: {value: '123', system: 'urn:oid:1.2.3.4.5.6.7.8'}
    display: City Hospital
- url: urn:extension:requestedOrganizationDepartment
  valueReference:
    type: Organization
    identifier: {value: '456', system: 'urn:oid:1.2.3.4.5.6.7.8'}
    display: City Hospital Neurology Department
- url: urn:extension:paymentType
  valueCoding: {code: '1', 
                system: 'urn:CodeSystem:paymentType', 
                display: Government Health Insurance}
status: active
id: servicerequest-with-aidbox-native-extensions
intent: original-order
subject:
  type: Patient
  identifier: {value: '2155800871000065', system: 'urn:oid:1.2.3.4.5.6.7.9'}
  display: Jack Black, 10.12.1941
```
{% endtab %}
{% endtabs %}

## Nested extensions

Let's create a structure of nested attributes:

{% code title="performerInfo" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo

resourceType: Attribute
description: Information filled in by performer
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo]
id: ServiceRequest.performerInfo
extensionUrl: urn:extension:performerInfo
```
{% endcode %}

{% code title="performerInfo.performedBy" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.performedBy

resourceType: Attribute
description: 'Information filled in by performer: performed by ["Practitioner" "PractitionerRole" "Organization" "CareTeam" "HealthcareService" "Patient" "Device" "RelatedPerson"]'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, performedBy]
id: ServiceRequest.performerInfo.performedBy
type: {id: Reference, resourceType: Entity}
isCollection: true
refers: 
  - Practitioner
  - PractitionerRole
  - Organization
  - CareTeam
  - HealthcareService
  - Patient
  - Device
  - RelatedPerson
extensionUrl: urn:extension:performerInfo.performedBy
```
{% endcode %}

{% code title="performerInfo.actualLocationReference" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.actualLocationReference

resourceType: Attribute
description: 'Information filled in by performer: actual location reference'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, actualLocationReference]
id: ServiceRequest.performerInfo.actualLocationReference
type: {id: Reference, resourceType: Entity}
isCollection: true
refers: 
  - Location
extensionUrl: urn:extension:performerInfo.actualLocationReference
```
{% endcode %}

{% code title="performerInfo.requestStatus" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestStatus

resourceType: Attribute
description: 'Information filled in by performer: request status'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestStatus]
id: ServiceRequest.performerInfo.requestStatus
type: {id: code, resourceType: Entity}
enum:
  - draft
  - new
  - in-progress
  - suspended
  - expanded
  - completed
  - archive
  - cancelled
extensionUrl: urn:extension:performerInfo.requestStatus
```
{% endcode %}

{% code title="performerInfo.requestActionHistory" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestActionHistory

resourceType: Attribute
description: 'Information filled in by performer: request action history'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestActionHistory]
id: ServiceRequest.performerInfo.requestActionHistory
isCollection: true
extensionUrl: urn:extension:performerInfo.requestActionHistory
```
{% endcode %}

{% code title="performerInfo.requestActionHistory.action" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestActionHistory.action

resourceType: Attribute
description: 'Information filled in by performer: request action history - action'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestActionHistory, action]
id: ServiceRequest.performerInfo.requestActionHistory.action
type: {id: string, resourceType: Entity}
extensionUrl: urn:extension:performerInfo.requestActionHistory.action
```
{% endcode %}

{% code title="performerInfo.requestActionHistory.date" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestActionHistory.date

resourceType: Attribute
description: 'Information filled in by performer: request action history - date'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestActionHistory, date]
id: ServiceRequest.performerInfo.requestActionHistory.date
type: {id: dateTime, resourceType: Entity}
extensionUrl: urn:extension:performerInfo.requestActionHistory.date
```
{% endcode %}

{% code title="performerInfo.requestActionHistory.subject" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestActionHistory.subject

resourceType: Attribute
description: 'Information filled in by performer: request action history - subject'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestActionHistory, subject]
id: ServiceRequest.performerInfo.requestActionHistory.subject
type: {id: Reference, resourceType: Entity}
extensionUrl: urn:extension:performerInfo.requestActionHistory.subject
```
{% endcode %}

{% code title="performerInfo.requestActionHistory.note" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestActionHistory.note

resourceType: Attribute
description: 'Information filled in by performer: request action history - note'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestActionHistory, note]
id: ServiceRequest.performerInfo.requestActionHistory.note
type: {id: string, resourceType: Entity}
extensionUrl: urn:extension:performerInfo.requestActionHistory.note
```
{% endcode %}

{% code title="performerInfo.requestActionHistory.author" %}
```yaml
PUT /Attribute/ServiceRequest.performerInfo.requestActionHistory.author

resourceType: Attribute
description: 'Information filled in by performer: request action history - author'
resource: {id: ServiceRequest, resourceType: Entity}
path: [performerInfo, requestActionHistory, author]
id: ServiceRequest.performerInfo.requestActionHistory.author
type: {id: Reference, resourceType: Entity}
extensionUrl: urn:extension:performerInfo.requestActionHistory.author
```
{% endcode %}

Let's add a resource using created extensions:

{% code title="Create resource in the Aidbox format:" %}
```yaml
PUT /ServiceRequest/servicerequest-with-aidbox-native-extensions

resourceType: ServiceRequest
performerInfo:
  performedBy:
  - resourceType: Organization
    display: City Hospital
  - resourceType: PractitionerRole
    display: Mark Sloan
  requestStatus: completed
  requestActionHistory:
  - date: '2020-01-14T12:15:22'
    action: new
    author: {display: Meredith Grey (Psychiatrist-narcologist), resourceType: PractitionerRole}
  - date: '2020-01-14T12:15:36'
    action: in-progress
    author: {display: Derek Shepherd (General practitioner), resourceType: PractitionerRole}
  - date: '2020-01-14T12:19:00'
    action: in-progress
    author: {display: Derek Shepherd (General practitioner), resourceType: PractitionerRole}
    subject: {display: Mark Sloan (Dental surgeon), resourceType: PractitionerRole}
    note: Referred to Dr. Mark Sloan (Dental surgeon)
  - date: '2020-01-14T15:05:20'
    action: meeting
    author: {display: Mark Sloan (Dental surgeon), resourceType: PractitionerRole}
  - date: '2020-01-14T15:22:46'
    action: completed
    author: {display: Mark Sloan (Dental surgeon), resourceType: PractitionerRole}    
status: completed
intent: original-order
category:
- text: Telemedicine consultation referral
  coding:
  - {code: "TMC", system: 'urn:CodeSystem:servicerequest-category', display: "Telemedicine consultation"}
subject:
  resourceType: Patient
  display: Jack Black, 10.12.1941
  identifier: {value: '2155800871000065', system: 'urn:oid:1.2.3.4.5.6.7.9'}
```
{% endcode %}

{% tabs %}
{% tab title="Request" %}
{% code title="Get resource in the FHIR format:" %}
```yaml
GET /fhir/ServiceRequest/servicerequest-with-aidbox-native-extensions
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ServiceRequest
extension:
- url: urn:extension:performerInfo
  extension:
  - url: urn:extension:performerInfo.performedBy
    valueReference: {display: City Hospital}
  - url: urn:extension:performerInfo.performedBy
    valueReference: {display: Mark Sloan}
  - {url: 'urn:extension:performerInfo.requestStatus', valueCode: completed}
  - url: urn:extension:performerInfo.requestActionHistory
    extension:
    - {url: 'urn:extension:performerInfo.requestActionHistory.date', valueDateTime: '2020-01-14T12:15:22'}
    - {url: 'urn:extension:performerInfo.requestActionHistory.action', valueString: new}
    - url: urn:extension:performerInfo.requestActionHistory.author
      valueReference: {display: Meredith Grey (Psychiatrist-narcologist)}
  - url: urn:extension:performerInfo.requestActionHistory
    extension:
    - {url: 'urn:extension:performerInfo.requestActionHistory.date', valueDateTime: '2020-01-14T12:15:36'}
    - {url: 'urn:extension:performerInfo.requestActionHistory.action', valueString: in-progress}
    - url: urn:extension:performerInfo.requestActionHistory.author
      valueReference: {display: Derek Shepherd (General practitioner)}
  - url: urn:extension:performerInfo.requestActionHistory
    extension:
    - {url: 'urn:extension:performerInfo.requestActionHistory.date', valueDateTime: '2020-01-14T12:19:00'}
    - {url: 'urn:extension:performerInfo.requestActionHistory.note', valueString: Referred to Dr. Mark Sloan (Dental surgeon)}
    - {url: 'urn:extension:performerInfo.requestActionHistory.action', valueString: in-progress}
    - url: urn:extension:performerInfo.requestActionHistory.author
      valueReference: {display: Derek Shepherd (General practitioner)}
    - url: urn:extension:performerInfo.requestActionHistory.subject
      valueReference: {display: Mark Sloan (Dental surgeon)}
  - url: urn:extension:performerInfo.requestActionHistory
    extension:
    - {url: 'urn:extension:performerInfo.requestActionHistory.date', valueDateTime: '2020-01-14T15:05:20'}
    - {url: 'urn:extension:performerInfo.requestActionHistory.action', valueString: meeting}
    - url: urn:extension:performerInfo.requestActionHistory.author
      valueReference: {display: Mark Sloan (Dental surgeon)}
    - url: urn:extension:performerInfo.requestActionHistory.subject
      valueReference: {display: '2020-01-08'}
  - url: urn:extension:performerInfo.requestActionHistory
    extension:
    - {url: 'urn:extension:performerInfo.requestActionHistory.date', valueDateTime: '2020-01-14T15:22:46'}
    - {url: 'urn:extension:performerInfo.requestActionHistory.action', valueString: completed}
    - url: urn:extension:performerInfo.requestActionHistory.author
      valueReference: {display: Mark Sloan (Dental surgeon)}
status: completed
id: servicerequest-with-aidbox-native-extensions
intent: original-order
category:
- text: Telemedicine consultation referral
  coding:
  - {code: TMC, system: 'urn:CodeSystem:servicerequest-category', display: Telemedicine consultation}
subject:
  identifier: {value: '2155800871000065', system: 'urn:oid:1.2.3.4.5.6.7.9'}
  display: Jack Black, 10.12.1941
```
{% endtab %}
{% endtabs %}

## Using FHIR extensions

Let's create an attribute for the [servicerequest-precondition](http://hl7.org/fhir/extension-servicerequest-precondition.html) extension:

```yaml
PUT /Attribute/ServiceRequest.precondition

resourceType: Attribute
description: "The condition or state of the patient, prior or during 
the diagnostic procedure or test, for example, fasting, at-rest, 
or post-operative. This captures circumstances that may influence 
the measured value and have bearing on the interpretation of the result."
resource: {id: ServiceRequest, resourceType: Entity}
path: [precondition]
id: ServiceRequest.precondition
type: {id: CodeableConcept, resourceType: Entity}
isCollection: true
extensionUrl: "http://hl7.org/fhir/StructureDefinition/servicerequest-precondition"
```

Let's create a resource in the FHIR format:

{% code title="Create resource in the FHIR format:" %}
```yaml
PUT /fhir/ServiceRequest/servicerequest-with-aidbox-native-extensions

resourceType: ServiceRequest
status: active
intent: original-order
subject:
  type: Patient
  display: Jack Black, 10.12.1941
extension:
  - url: http://hl7.org/fhir/StructureDefinition/servicerequest-precondition
    valueCodeableConcept:
      text: After calorie fasting
      coding:
        - code: "726055006"
          system: http://snomed.info/sct
          display: After calorie fasting
  - url: http://hl7.org/fhir/StructureDefinition/servicerequest-precondition
    valueCodeableConcept:
      text: At rest
      coding:
        - code: "263678003"
          system: http://snomed.info/sct
          display: At rest          
```
{% endcode %}

Let's get the resource in the Aidbox format:

{% tabs %}
{% tab title="Request" %}
{% code title="Get resource in the Aidbox format:" %}
```yaml
GET /ServiceRequest/servicerequest-with-aidbox-native-extensions
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ServiceRequest
id: servicerequest-with-aidbox-native-extensions
intent: original-order
status: active
subject: {type: Patient, display: 'Jack Black, 10.12.1941'}
precondition:
- text: After calorie fasting
  coding:
  - {code: '726055006', 
    system: 'http://snomed.info/sct', 
    display: After calorie fasting}
- text: At rest
  coding:
  - {code: '263678003', 
    system: 'http://snomed.info/sct', 
    display: At rest}
```
{% endtab %}
{% endtabs %}

### Create FHIR primitive type extension

FHIR primitive named _fieldName_ can be extended with _\_fieldName_ field. Aidbox treats _fieldName_ as independent from _fieldName_. Aidbox do not validate fields with names started with underscore.

Suppose we need to extend Patient.gender attribute with primitive type. It means, that we need to create Patient.\_gender attribute.

```yaml
PUT /Attribute/Patient._gender

path:
  - _gender
resource:
  id: Patient
  resourceType: Entity
id: Patient._gender
resourceType: Attribute
```

Now we can add an extension attribute into \_gender attribute.

```yaml
PUT /Attribute/Patient._gender.extension

path:
  - _gender
  - extension
resource:
  id: Patient
  resourceType: Entity
id: Patient._gender.extension
isCollection: true
type:
  id: Extension
  resourceType: Entity
resourceType: Attribute
```

Finally, add Patient with extended gender.

```yaml
PUT /Patient/p-gender

gender: male
_gender: 
  extension:
    - valueCodeableConcept: 
        coding:
          - code: code-male
            system: system-gender
      url: https://example.com
```
