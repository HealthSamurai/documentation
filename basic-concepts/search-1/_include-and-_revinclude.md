---
description: Include associated resources
---

# \_include & \_revinclude

A client can add related resources to a search result using **\(rev\)include** and **with** parameters. In ORM frameworks, such feature is sometimes called an "associations eager loading". This technique can save extra roundtrips from the client to the server and potential N+1 problem.

For example, you may want to get encounters with patients \(each encounter refers to\):

{% tabs %}
{% tab title="GET" %}
```yaml
GET /Encounter?_include=Encounter:subject:Patient
```

```yaml
GET /Encounter?_with=subject{Patient}
```
{% endtab %}

{% tab title="PUT Patient" %}
```
PUT /Patient

resourceType: Patient
id: pat-234
name:
  - family: Smith
```
{% endtab %}

{% tab title="PUT Encounter" %}
```
PUT /Encounter

resourceType: Encounter
id: enc-234
subject: 
  resourceType: Patient
  id: pat-234
class: {code: 'IMP', 
  system: 'http://terminology.hl7.org/CodeSystem/v3-ActCode', 
  display: 'inpatient encounter'}
status: finished
```
{% endtab %}
{% endtabs %}

Or patients with conditions \(i.e. by a reverse reference\):

```yaml
GET /Patient?_revinclude=Encounter:subject:Patient
```

```yaml
GET /Patient?_with=Encounter.subject
```

### \_include

Syntax for include:

```text
 _include(:reverse|:iterate|:logical)=(source-type:)search-param:(:target-type)
```

**search-param** is a name of the search parameter with the type `reference` defined for **source-type**.

This query can be interpreted as: for the **source-type** resources in the result include all **target-type resources,** which are referenced by the **search-param**. If you skip the **source-type,** it will be set to the resource-type you are searching for:

```yaml
GET /Encounter?_include=subject:Patient 
=> GET /Encounter?_include=Encounter:subject:Patient
```

**target-type** is optional for not chained includes and means all referenced resource-types:

```yaml
GET /Encounter?_include=subject 
=> GET /Encounter?_include=subject:*
```

{% hint style="warning" %}
For more explicit interpretation and for performance reason, client must provide target-type for chained includes!
{% endhint %}

### **\_revinclude**

Syntax for **revinclude:**

```text
_revinclude(:reverse|:iterate|:logical)=(source-type:)search-param(:target-type)
```

Interpretation**:**  include all **source-type** resources, which refer **target-type** resources by **search-param** in the result set.

### :logical modifier

If you provide `:logical` modifier, Aidbox will include logically referenced resources  as well. Logical reference means reference with attribute `type` set to resource-type and `identifier` attribute set to one of identifier of referenced resource. Example:

{% tabs %}
{% tab title="GET" %}
```markup
GET /Encounter?_include:logical=patient
```

```yaml
GET /Encounter?_with=patient:logical
```

```yaml
GET /Patient?_revinclude:logical=Encounter:patient:Patient
```

```yaml
GET /Patient?_with=Encounter.patient:logical
```
{% endtab %}

{% tab title="PUT Patient" %}
```yaml
PUT /Patient

resourceType: Patient
id: pat-123
identifier:
- {system: 'ssn', value: '78787878'}
```
{% endtab %}

{% tab title="PUT Encounter" %}
```yaml
PUT /Encounter

resourceType: Encounter
id: enc-123
subject: 
  type: Patient
  identifier: {system: 'ssn', value: '78787878'}
class: {code: 'IMP', 
  system: 'http://terminology.hl7.org/CodeSystem/v3-ActCode', 
  display: 'inpatient encounter'}
status: finished
```
{% endtab %}
{% endtabs %}

### **\_include=\***

You can include all resources referenced from the search result using **\*.** This is considered _bad practice_ because it's too implicit. This feature is only implemented for conformance with the FHIR specification. **Please avoid using it!** 

{% hint style="danger" %}
\_include=\* could not be used as part of chained \(rev\)includes!
{% endhint %}

```javascript
GET /Encounter?_include=*
```

### Chained \(rev\)includes

Client can chain \(rev\)includes to load next level of references.  \(Rev\)includes should go in a proper loading order. According to the FHIR specification, for chained includes a client must specify the `:iterate` modifier. However, in Aidbox this modifier is **optional** \(it's better to skip it\).

{% tabs %}
{% tab title="GET" %}
```yaml
GET /RequestGroup?_include=encounter\
  &_include=patient:Patient\
  &_include=Patient:organization\
  &_revinclude=AllergyIntolerance:patient:Patient\
  &_revinclude=Condition:subject:Patient\
  &_include=author:PractitionerRole\
  &_include=PractitionerRole:practitioner:Pracitioner\
  &_include=PractitionerRole:location\
  &_revinclude=Contract:subject:PractitionerRole\
  &_include=MedicationRequest:medication\
  &_include=MedicationRequest:requester:PractitionerRole\
  &_include=MedicationRequest:intended-performer:Organization\
  &_include=MedicationRequest:intended-performer:Organization
```
{% endtab %}

{% tab title="PUT RequestGroup" %}
```
PUT /RequestGroup/example

encounter: {id: enc-234, resourceType: Encounter}
reasonCode:
- {text: Treatment}
authoredOn: '2019-03-06T17:31:00Z'
resourceType: RequestGroup
note:
- {text: Additional notes about the request group}
author: {display: Practitioner/1}
contained:
- id: medicationrequest-1
  intent: proposal
  status: unknown
  subject: {id: pat-234, resourceType: Patient}
  medication:
    CodeableConcept: {text: Medication 1}
  resourceType: MedicationRequest
- id: medicationrequest-2
  intent: proposal
  status: unknown
  subject: {id: pat-234, resourceType: Patient}
  medication:
    CodeableConcept: {text: Medication 2}
  resourceType: MedicationRequest
priority: routine
status: draft
id: example
groupIdentifier: {value: '00001', system: 'http://example.org/treatment-group'}
identifier:
- {value: requestgroup-1}
intent: plan
action:
- description: Administer medications at the appropriate time
  textEquivalent: Administer medication 1, followed an hour later by medication 2
  participant:
  - {display: Practitioner/1}
  title: Administer Medications
  prefix: '1'
  selectionBehavior: all
  requiredBehavior: must
  timing: {dateTime: '2019-03-06T19:00:00Z'}
  groupingBehavior: logical-group
  action:
  - id: medication-action-1
    type:
      coding:
      - {code: create}
    resource: {localRef: medicationrequest-1}
    description: Administer medication 1
  - id: medication-action-2
    type:
      coding:
      - {code: create}
    resource: {localRef: medicationrequest-2}
    description: Administer medication 2
    relatedAction:
    - offset:
        Duration: {unit: h, value: 1}
      actionId: medication-action-1
      relationship: after-end
  precheckBehavior: 'yes'
  cardinalityBehavior: single
subject: {id: pat-234, resourceType: Patient}
text: {div: '<div xmlns="http://www.w3.org/1999/xhtml">Example RequestGroup illustrating related actions to administer medications in sequence with time delay.</div>', status: generated}
```
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
Client must always specify **target-type** and **source-type** for intermediate \(rev\)includes because this is explicit and allows Aidbox to prepare dependency graph before query!
{% endhint %}

To save some keystrokes, you can group \_\(rev\)include params of the same level as a comma separated list:

```yaml
GET /RequestGroup?_include=encounter,patient:Patient,author:PractitionerRole
```

{% hint style="info" %}
Here is the [discussion](https://chat.fhir.org/#narrow/stream/179166-implementers/topic/About.20_include.3Aiterate) in the FHIR chat about the `:iterate` ambiguity. We appreciate your opinion!
{% endhint %}

### Recursive \(rev\)includes

For self-referencing resources, you can specify the `:recurse` or `:iterate` modifier with **source-type=target-type** to recursively get all children or parents:

{% tabs %}
{% tab title="GET" %}
```yaml
GET /Observation?_include:recurse=has-member
```
{% endtab %}

{% tab title="PUT Obs1" %}
```
put /Observation/bloodgroup

category:
- text: Laboratory
  coding:
  - {code: laboratory, 
  system: 'http://terminology.hl7.org/CodeSystem/observation-category', 
  display: Laboratory}
value:
  CodeableConcept:
    text: A
    coding:
    - {code: '112144000', system: 'http://snomed.info/sct', 
    display: Blood group A (finding)}
resourceType: Observation
status: final
effective: {dateTime: '2018-03-11T16:07:54+00:00'}
id: bloodgroup
code:
  text: Blood Group
  coding:
  - {code: 883-9, system: 'http://loinc.org', 
  display: 'ABO group [Type] in Blood'}
subject: {id: pat-234, resourceType: Patient}
```
{% endtab %}

{% tab title="PUT Obs2" %}
```
put /Observation/rhstatus

category:
- text: Laboratory
  coding:
  - {code: laboratory, system: 'http://terminology.hl7.org/CodeSystem/observation-category', display: Laboratory}
meta: {lastUpdated: '2019-12-17T13:44:27.218564Z', createdAt: '2019-12-17T13:44:27.218564Z', versionId: '99'}
value:
  CodeableConcept:
    text: A
    coding:
    - {code: '112144000', system: 'http://snomed.info/sct', display: Blood group A (finding)}
resourceType: Observation
status: final
effective: {dateTime: '2018-03-11T16:07:54+00:00'}
id: rhstatus
code:
  text: Blood Group
  coding:
  - {code: 883-9, system: 'http://loinc.org', display: 'ABO group [Type] in Blood'}
subject: {id: pat-234, resourceType: Patient}
```
{% endtab %}

{% tab title="PUT Obs3" %}
```
put /Observation/bgpanel

category:
- text: Laboratory
  coding:
  - {code: laboratory, system: 'http://terminology.hl7.org/CodeSystem/observation-category', display: Laboratory}
hasMember:
- {id: bloodgroup, resourceType: Observation}
- {id: rhstatus, resourceType: Observation}
meta: {lastUpdated: '2019-12-17T13:45:37.780512Z', createdAt: '2019-12-17T13:45:37.780512Z', versionId: '106'}
resourceType: Observation
status: final
effective: {dateTime: '2018-03-11T16:07:54+00:00'}
id: bgpanel
code:
  text: Blood Group Panel
  coding:
  - {code: 34532-2, system: 'http://loinc.org', display: ' Blood type and Indirect antibody screen panel - Blood'}
subject: {id: pat-234, resourceType: Patient}
```
{% endtab %}
{% endtabs %}

```yaml
GET /Observation?_include:iterate=Observation:has-member:Observation
```

{% tabs %}
{% tab title="GET" %}
```yaml
# get all children
GET /Organization?_revinclude:recurse=Organization:partof
```
{% endtab %}

{% tab title="PUT Org1" %}
```
put /Organization/org-123

name: Blackwood Hospital
```
{% endtab %}

{% tab title="PUT Org2" %}
```
put /Organization/org-234

name: Blackwood Hospital Department
partOf:
  resourceType: Organization
  id: org-123
```
{% endtab %}

{% tab title="PUT Org3" %}
```
put /Organization/org-345

name: Blackwood Hospital Department Facility
partOf:
  resourceType: Organization
  id: org-234
```
{% endtab %}

{% tab title="PUT Org4" %}
```
put /Organization/org-456

name: Blackwood Hospital Department Facility Room 1
partOf:
  resourceType: Organization
  id: org-345
```
{% endtab %}
{% endtabs %}

```yaml
# get all children
GET /Organization?_revinclude:iterate=Organization:partof:Organization
```

```yaml
# get all parents
GET /Organization?_include:recurse=partof
```

```yaml
# get all parents
GET /Organization?_include:iterate=Organization:partof:Organization
```

### Using the \_with parameter

FHIR \(rev\)include syntax is non-DRY and sometimes confusing. We introduced the `_with` parameter that is a simple \(aka GraphQL\) DSL to describe includes in a more compact way.

```javascript
expr = param-expr (space param-expr)*
param-expr = param  ( '{' typed-ref-expr (space typed-ref-expr)* '}')?
typed-ref-expr = resource-type | resource-type '{' expr '}'
param = resource-type '.' param-name  (':recur' | ':logical') ? | param-name (':recur' | ':logical') ?
space = ',' | ' ' | '\n'
param-name = ALPAHNUM
```

Examples:

```yaml
Encounter?_with=patient
=> Encounter?_include=Encounter:patient
---
Encounter?_with=patient,participant
=> Encounter?_include=Encounter:patient,Encounter:participant
---
Encounter?_with=patient{Patient}
=> Encounter?_include=Encounter:patient:Patient
---
Encounter?_with=patient{Patient{organization}}
=> Encounter?_include=Encounter:patient:Patient&
             _include(:iterate)=Patient:organization
---            
Encounter?_with=patient{Patient{organization{Organization{partof:recur}}}}
=> Encounter?_include=Encounter:patient:Patient&
             _include(:iterate)=Patient:organization
             _include(:recurse)=Organization:parto-of           
---             
Patient?_with=organization,Condition.patient,MedicationStatement.patient{medication}
=> Patient?_include=Patient:organization&
           _revinclude=Condition:patient:Patient
           _revinclude=MedicationStatement:patient:Patient
           _include=MedicationStatement:medication
---             
RequestGroup?_with=
 author
 patient{Patient{organization,AllergyIntolerance.patient}}
 target{
  MedicationRequest{
    medication
    intended-performer{Organization}
    requester{PractitionerRole{practitioner,location}}}}
=>
RequestGroup?_include=patient,author
    &_include:iterate=RequestGroup:target:MedicationRequest
    &_include:iterate=MedicationRequest:medication
    &_include:iterate=MedicationRequest:requester:PractitionerRole
    &_include:iterate=MedicationRequest:intended-performer:Organization
    &_include:iterate=PractitionerRole:practitioner
    &_include:iterate=Patient:organization
    &_include:iterate=PractitionerRole:location
    &_revinclude:iterate=AllergyIntolerance:patient
 ---
 Organization?partof:recur{Organization}
 => Organization?_include:recurse=partof:Organization
```

### \(rev\)include and \_elements

You can use the extended [elements](_elements.md#elements-and-rev-includes) parameter to control elements of \(rev\)included resources by prefixing desired elements with the resource type:

```yaml
GET /Encounter?_include=patient&_elements=id,status,Patient.name,Patient.birthDate
```



