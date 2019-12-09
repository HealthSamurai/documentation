---
description: Include associated resources
---

# \_include & \_revinclude

Client can add related resources to search result using **include,** **revinclude**  and **with** parameters.  In ORM frameworks this feature sometimes is called "associations eager loading". This technique can save extra roundtrips from client to server and potential N+1 problem.

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
 _include(:reverse|:iterate)=(source-resource-type)?:search-param:(target-resource-type)?
```

**search-param** is a name of search parameter  with type `reference` defined for **source-resource-type**.

This query can be interpreted as: for **source-resource-type** resources in result include all **target-resource-type resources,** which are referenced by **search-param**. If you skip **source-resource-type** - it will be set to resource-type you are searching for:

```yaml
GET /Encounter?_include=subject:Patient 
=> GET /Encounter?_include=Encounter:subject:Patient
```

**target-resource-type** is optional for not chained includes and means all referenced resource-types"

```yaml
GET /Encounter?_include=subject 
=> GET /Encounter?_include=subject:*
```

{% hint style="warning" %}
For more explicit interpretation and for performance reason, client has to provide target-resource-type for chained includes!
{% endhint %}

### **\_revinclude**

Syntax for **revinclude**

```text
_revinclude(:reverse|:iterate)=(source-resource-type)?:search-param:(target-resource-type)?
```

Interpretation**:**  include all **source-type** resources, which refers **target-type** resources by **search-param** in result set.

### **\_include=\***

You can include \(but not revinclude\) all referenced resources using **\*.** This considered _bad practice,_ because it's too implicit. This feature is only implemented because FHIR specification. Please avoid to use it! 

```javascript
GET /Encounter?_include=*
```

### Chained includes and revincludes

Client can chain \(rev\)includes to load next level of references.  \(Rev\)includes should go in a proper loading order. In FHIR spec for chained includes client have to specify `:iterate` modifier - in Aidbox this modifier is completely optional \(it's better just skip it\).

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
Client have to always specify **target-resource-type** and **source-resource-type** for intermediate \(rev\)includes, because this explicit and allow Aidbox to prepare dependency graph before query!
{% endhint %}

### Recursive includes and revincludes

### Using \_with parameter



