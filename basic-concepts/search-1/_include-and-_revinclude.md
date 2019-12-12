---
description: Include associated resources
---

# \_include & \_revinclude

Client can add related resources to a search result using the **\(rev\)include** and **with** parameters.  In ORM frameworks, such feature is sometimes called an "associations eager loading". This technique can save extra roundtrips from client to server and potential N+1 problem.

For example, you may want to get encounters with patients \(each encounter refers to\):

```yaml
GET /Encounter?_include=Encounter:subject:Patient
GET /Encounter?_with=subject{Patient}
```

Or patients with conditions \(i.e. by a reverse reference\):

```yaml
GET /Patient?_revinclude=Encounter:subject:Patient
GET /Patient?_with=Encounter.subject
```

### \_include

Syntax for include:

```text
 _include(:reverse|:iterate|:logical)=(source-type)?:search-param:(target-type)?
```

**search-param** is a name of search parameter with the type `reference` defined for **source-type**.

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
_revinclude(:reverse|:iterate|:logical)=(source-type)?:search-param:(target-type)?
```

Interpretation**:**  include all **source-type** resources, which refer **target-type** resources by **search-param** in the result set.

### :logical modifier

If you provide `:logical` modifier, Aidbox will include logically referenced resources  as well. Logical reference means reference with attribute `type` set to resource-type and `identifier` attribute set to one of identifier of referenced resource. Example:

```yaml
---
resourceType: Patient
identifier:
- {system: 'ssn', value: '78787878'}

---
resourceType: Encounter
subject: 
  type: Patient
  identifier: {system: 'ssn', value: '78787878'}

---

GET Encounter?_include:logical=patient
GET Enocounter?_with=patient:logical

GET Patient?_revinclude:logical=Encounter:patient:Patient
GET Patient?_with=Encounter.patient:logical
```

### **\_include=\***

You can include all resources referenced from the search result using **\*.** This is considered _bad practice_ because it's too implicit. This feature is only implemented for conformance with the FHIR specification. **Please avoid using it!** 

{% hint style="danger" %}
\_include=\* could not be used as part of chained \(rev\)includes!
{% endhint %}

```javascript
GET /Encounter?_include=*
```

### Chained \(rev\)includes

Client can chain \(rev\)includes to load next level of references.  \(Rev\)includes should go in a proper loading order. By the FHIR specification, for chained includes a client must specify the `:iterate` modifier. However, in Aidbox this modifier is **optional** \(it's better to skip it\).

```yaml
GET /RequestGroup?
  _include=target&
  _include=patient:Patient&
    _include=Patient:organization&
    _revinclude=AllergyIntolerance:patient:Patient&
    _revinclude=Condition:subject:Patient&
  _include=author:PractitionerRole&
    _include=PractitionerRole:practitioner:Pracitioner&
    _include=PractitionerRole:location&
    _revinclude=Contract:subject:PractitionerRole&
  _include=RequestGroup:target:MedicationRequest&
      _include=MedicationRequest:medication&
      _include=MedicationRequest:requester:PractitionerRole&
      _include=MedicationRequest:intended-performer:Organization&
      _include=MedicationRequest:intended-performer:Organization
```

{% hint style="warning" %}
Client must always specify **target-type** and **source-type** for intermediate \(rev\)includes because this is explicit and allows Aidbox to prepare dependency graph before query!
{% endhint %}

To save some keystrokes, you can group \_\(rev\)include params of the same level as a comma separated list:

```yaml
GET /RequestGroup?_include=target,patient:Patient,author:PractitionerRole
```

{% hint style="info" %}
Here is the [discussion](https://chat.fhir.org/#narrow/stream/179166-implementers/topic/About.20_include.3Aiterate) in the FHIR chat about the `:iterate` ambiguity. We appreciate your opinion!
{% endhint %}

### Recursive \(rev\)includes

For self-referencing resources you can specify the `:recurse` or `:iterate` modifier with **source-type=target-type** to recursively get all children or parents:

```yaml
GET /Observation?_include:recurse=has-component
GET /Observation?_include:iterate=Observation:has-component:Observation
# get all children
GET /Organization?_revinclude:recurse=partof
GET /Organization?_revinclude:iterate=Organization:partof:Organization
# get all parents
GET /Organization?_include:recurse=partof
GET /Organization?_include:iterate=Organization:partof:Organization
```

### Logical \(rev\)includes

TBD

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
Encounter?_with=patient{Patient{organization{Organization{partof:recur}}
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
GET /Encounter?_include=patient&_element=id,status,Patient.name,Patient.birthDate
```

