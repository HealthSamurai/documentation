---
description: Difference between Aidbox and FHIR formats
---

# Aidbox and FHIR Formats

Aidbox stores FHIR resources almost as is with 3 types of isomorphic transformations:

* References
* Union (Choice Types)
* First-Class Extensions

### References:

In FHIR, references are represented as URI string. In most cases, you are interested in discrete parts of references like resource id and type. For performance and accuracy reasons Aidbox parses reference and stores its parts in discrete fields. There are three types of references - absolute, relative, and local. Aidbox parses them into different attributes.

**Relative** (interpreted as a reference to a resource on the same server; trigger referential consistency check) :

```yaml
# FHIR
subject:
  reference: "Patient/pt-1" 

# Aidbox
subject:
  resourceType: "Patient"
  id: "pt-1"
```

**reference** is parsed into a pair of **`{id,resourceType}`** attributes

**Absolute** (interpreted as a reference to an external resource;  no ref validation)

```yaml
# FHIR
subject:
  reference: "http://external/fhir/Patient/pt-1" 

# Aidbox
subject:
  uri: "http://external/fhir/Patient/pt-1"
```

reference is parsed into the **uri** attribute

**Local** (interpreted as a local ref to contained resources )

```yaml
# FHIR
subject:
  reference: "#pt" 

# Aidbox
subject:
  localRef: "pt"
```

reference is parsed into a **ref** attribute

{% hint style="info" %}
To enable referential integrity checks in extensions (`extension.valueReference`) create a [first-class extension](aidbox-and-fhir-formats.md#first-class-extensions).
{% endhint %}

### Union (Choice) Types:

Some elements can have multiple types. Such elements in FHIR spec prefixed with `[x]` like `Observation.value[x]` and represented in JSON in a _wrong_ (postfixed) way like`Observation.valueString` . The simple logical check "why it's wrong" is "you could not have a collection of union elements in FHIR JSON!". Aidbox fixes this moving type as a key inside a nested object - `valueString:... => value: {string: ...}`

```yaml
#FHIR
resourceType: Observation
valueQuantity:
  unit: ...
  value: ...

# becomes Aidbox
resourceType: Observation
value:
  Quantity:
    unit: ...
    value: ...
```

### First-Class Extensions

While FHIR uses two different ways to define **core elements** and **extensions**, Aidbox provides unified framework to describe both. Aidbox supports user-defined attributes or "first-class extensions". In Aidbox, you can define new attributes (elements) for existing (FHIR) resources.  Let's illustrate this on race complex attribute for Patient from US-Core FHIR Profile.

This is how a patient with race looks in FHIR format:

```yaml
resourceType: Patient
id: sample-pt
extension:
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-race
  extension:
  - url: text
    valueString: Asian Indian
  - url: ombCategory
    valueCoding:
       system: urn:oid:2.16.840.1.113883.6.238
       code: 2028-9
       display: Asian
  - url: detailed
    valueCoding:
       system:
       code: 2029-7	
       display: Asian Indian
```

If you try to save this resource in "default", Aidbox will keep these extensions as is. But if you define attributes for these extensions, Aidbox will store them in a more friendly format.

```yaml
PUT /

- resourceType: Attribute
  id: Patient.race
  path: ['race']
  resource: {id: 'Patient', resourceType: 'Entity'}
  extensionUrl: 'http://hl7.org/fhir/us/core/StructureDefinition/us-core-race'
- resourceType: Attribute
  id: Patient.race.text
  path: ['race', 'text']
  resource: {id: 'Patient', resourceType: 'Entity'}
  type: {id: 'string', resourceType: 'Entity'}
  extensionUrl: text
- resourceType: Attribute
  id: Patient.race.category
  path: ['race', 'category']
  resource: {id: 'Patient', resourceType: 'Entity'}
  type: {id: 'Coding', resourceType: 'Entity'}
  extensionUrl: ombCategory
- resourceType: Attribute
  id: Patient.race.detailed
  path: ['race', 'detailed']
  resource: {id: 'Patient', resourceType: 'Entity'}
  type: {id: 'Coding', resourceType: 'Entity'}
  extensionUrl: detailed
```

Now you can test how a resource will be stored in Aidbox with:

```yaml
POST /to-format/aidbox

resourceType: Patient
id: sample-pt
extension:
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-race
  extension:
  - url: text
    valueString: Asian Indian
  - url: ombCategory
    valueCoding:
       system: urn:oid:2.16.840.1.113883.6.238
       code: 2028-9
       display: Asian
  - url: detailed
    valueCoding:
       system:
       code: 2029-7	
       display: Asian Indian
```

Read more about [/$to-format Operation](../other/to-format.md).

The response should be:

```yaml
resourceType: Patient
  id: sample-pt
  race:
    text: Asian Indian
    category: {system: 'urn:oid:2.16.840.1.113883.6.238', code: 2028-9, display: Asian}
    detailed: {system: 'urn:oid:2.16.840.1.113883.6.238', code: 2029-7, display: Asian Indian}
```
