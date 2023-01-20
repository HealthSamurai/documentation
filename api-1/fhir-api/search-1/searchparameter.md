---
description: Define and use SearchParameter resource
---

# SearchParameter

SearchParameter is a special meta-resource, which describes which part of resource and how you want to make searchable. Aidbox is coming with predefined set of search params for different resource types, but you can define new search params on-fly.

{% hint style="warning" %}
Aidbox SearchParameter has its own structure, which is not the same as FHIR SearchParameters. To import FHIR SearchParams you have to convert it into Aidbox representation!
{% endhint %}

Here is example of Patient.name search parameter:

```yaml
GET /SearchParameter/Patient.name
​
# 200
name: name
type: string
resource: {id: Patient, resourceType: Entity}
expression:
- [name]
```

### Structure

All this attributes are required.

| path            | type         | desc                                                               |
| --------------- | ------------ | ------------------------------------------------------------------ |
| **.name**       | `keyword`    | Name of search parameter, will be used in search query string      |
| **.resource**   | `Reference`  | Reference to resource, i.e. {id: Patient, resourceType: Reference} |
| **.type**       | `keyword`    | Type of search parameter (see Types)                               |
| **.expression** | `expression` | expression for elements to search (see Expression)                 |

### Types

|                                                                                                        |                                                                                                                                                                                                                                                                                                              |
| ------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| [number](https://web.archive.org/web/20201024081226/https://www.hl7.org/fhir/search.html#number)       | Search parameter SHALL be a number (a whole number, or a decimal).                                                                                                                                                                                                                                           |
| [date](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#date)           | Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.                                                                                                                                                                                       |
| [string](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#string)       | Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string.                                                                                                                                                              |
| [token](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#token)         | Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "\|", depending on the modifier used. |
| [reference](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#reference) | A reference to another resource.                                                                                                                                                                                                                                                                             |
| [composite](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#composite) | A composite search parameter that combines a search on two values together.                                                                                                                                                                                                                                  |
| [quantity](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#quantity)   | A search parameter that searches on a quantity.                                                                                                                                                                                                                                                              |
| [uri](https://web.archive.org/web/20200926234523/https://www.hl7.org/fhir/search.html#uri)             | A search parameter that searches on a URI (RFC 3986).                                                                                                                                                                                                                                                        |

Depending on the value type, different modifiers can be applied.

### Expression

Expression is a set of elements, by which we want to search. Expression is logically very simple subset of FHIRPath (which can be efficiently implemented in database) expressed as data. Expression is array of PathArrays; PathArray is array of strings, integers or objects, where each type has special meaning:

* string - name of element
* integer - index in collection
* object - filter by pattern in collection

For example PathArray `["name", "given"]` extracts as array all given and family names and flatten into one collection. Equivalent FHIRPath expression is `name.given | name.family`.

PathArray `[["name", 0, "given", 0]]` extract only first given of first name (FHIRPath `name.0.given.0`

PathArray `[["telecom", {"system": "phone"}, "value"]]` extract all values of telecom collection filtered by `system=phone` (FHIRPath: `telecom.where(system='phone').value` )

Expression consists of array of PathArrays, all results of PathArray are concatinated into one collection.

### Define custom SearchParameter

You can define custom search params by just creating SearchParameter resource. Let's say you want to search patient by city:

```yaml
PUT /SearchParameter/Patient.city
​
name: city
type: token
resource: {id: Patient, resourceType: Entity}
expression: [[address, city]]
```

Now let's test new search parameter

```yaml
GET /Patient?city=New-York
​
# resourceType: Bundle
type: searchset
entry: [...]
total: 10
query-sql: 
- | 
  SELECT "patient".* FROM "patient" 
  WHERE ("patient".resource @> '{"address":[{"city":"NY"}]}')
   LIMIT 100 OFFSET 0
```

#### Define custom SearchParameter with extension

If you have defined [first-class extension](../../../modules-1/first-class-extensions.md), you have to use Aidbox format for the SearchParameter expression. If you use FHIR format, you don't need to create Attribute and the `expression` path should be in FHIR format.

{% tabs %}
{% tab title="First-class extension" %}
```yaml
PUT /Attribute/ServiceRequest.precondition

resourceType: Attribute
description: "The condition or state of the patient, prior or during the diagnostic procedure or test, for example, fasting, at-rest, or post-operative. This captures circumstances that may influence the measured value and have bearing on the interpretation of the result."
resource: {id: ServiceRequest, resourceType: Entity}
path: [precondition]
id: ServiceRequest.precondition
type: {id: CodeableConcept, resourceType: Entity}
isCollection: true
extensionUrl: "http://hl7.org/fhir/StructureDefinition/servicerequest-precondition"
```
{% endtab %}

{% tab title="SearchParameter (First-class extension)" %}
```yaml
PUT /SearchParameter/ServiceRequest.precondition

name: precondition
type: token
resource: {id: ServiceRequest, resourceType: Entity}
expression: [[precondition, coding]]
```
{% endtab %}

{% tab title="SearchParameter (FHIR extension)" %}
```yaml
PUT /SearchParameter/ServiceRequest.precondition

name: precondition
type: token
resource: {id: ServiceRequest, resourceType: Entity}
expression:
- [extension, {url: 'http://hl7.org/fhir/StructureDefinition/servicerequest-precondition'}, 
value, CodeableConcept, coding, code]

```
{% endtab %}
{% endtabs %}

If you use [Zen IG](../../../aidbox-configuration/zen-configuration.md) then first-class extensions are generated from zen-schemas. You have to use Aidbox format for the custom SearchParameter `expression` (check tab #3 in the example above)
