---
description: Include associated resources
---

# \_include & \_revinclude

Client can add related resources to search result using **\(rev\)include**  and **with** parameters.  In ORM frameworks this feature sometimes is called "associations eager loading". This technique can save extra roundtrips from client to server and potential N+1 problem.

For example you may want to get encounters with patients \(each encounter refers to\):

```yaml
GET /Encounter?_include=Encounter:subject:Patient
GET /Encounter?_with=subject{Patient}
```

Or patients with conditions \(i.e. by reverse reference\):

```yaml
GET /Patient?_revinclude=Encounter:subject:Patient
GET /Patient?_with=Encounter.subject
```

### \_include

Syntax for include:

```text
 _include(:reverse|:iterate)=(source-type)?:search-param:(target-type)?
```

**search-param** is a name of search parameter  with type `reference` defined for **source-type**.

This query can be interpreted as: for **source-type** resources in result include all **target-type resources,** which are referenced by **search-param**. If you skip **source-type** - it will be set to resource-type you are searching for:

```yaml
GET /Encounter?_include=subject:Patient 
=> GET /Encounter?_include=Encounter:subject:Patient
```

**target-type** is optional for not chained includes and means all referenced resource-types"

```yaml
GET /Encounter?_include=subject 
=> GET /Encounter?_include=subject:*
```

{% hint style="warning" %}
For more explicit interpretation and for performance reason, client has to provide target-type for chained includes!
{% endhint %}

### **\_revinclude**

Syntax for **revinclude**

```text
_revinclude(:reverse|:iterate)=(source-type)?:search-param:(target-type)?
```

Interpretation**:**  include all **source-type** resources, which refers **target-type** resources by **search-param** in result set.

### **\_include=\***

You can include \(but not revinclude\) all referenced resources using **\*.** This considered _bad practice,_ because it's too implicit. This feature is only implemented because FHIR specification. Please avoid to use it! 

```javascript
GET /Encounter?_include=*
```

### Chained \(rev\)includes

Client can chain \(rev\)includes to load next level of references.  \(Rev\)includes should go in a proper loading order. In FHIR spec for chained includes client has to specify `:iterate` modifier - in Aidbox this modifier is  **optional** \(it's better skip it\).

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
Client have to always specify **target-type** and **source-type** for intermediate \(rev\)includes, because this explicit and allows Aidbox to prepare dependency graph before query!
{% endhint %}

To save some keystrokes you can group \_\(rev\)include params on same level as a comma separated list:

```yaml
GET /RequestGroup?_include=target,patient:Patient,author:PractitionerRole
```

{% hint style="warning" %}
Here is [discussion](https://chat.fhir.org/#narrow/stream/179166-implementers/topic/About.20_include.3Aiterate) in FHIR chat about `:iterate` ambiguity. We appreciate your opinion!
{% endhint %}

### Recursive \(rev\)includes

For self-referencing resources you can specify `:recurse` modifier or `:iterate` modifier with **source-type=target-type** to recursively get all children or parents:

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

### Using \_with parameter

FHIR \(rev\)include syntax is non-DRY and sometimes confusing. We introduced `_with` parameter - simple \(aka GraphQL\) DSL to describe includes in a more compact way.

```javascript
expr = param-expr (space param-expr)*
param-expr = param  ( '{' typed-ref-expr (space typed-ref-expr)* '}')?
typed-ref-expr = resource-type | resource-type '{' expr '}'
param = resource-type '.' param-name  ':recur' ? | param-name ':recur' ?
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



