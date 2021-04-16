---
description: Define and use SearchParameter resource
---

# SearchParameter

SearchParameter is a special meta-resource, which describes a part of the resource you want to make searchable. Aidbox comes with the predefined set of search params for different resource types, but you can define new search params on the fly.

{% hint style="warning" %}
Aidbox SearchParameter has its own structure, which is not the same as FHIR SearchParameters. To import FHIR SearchParams, you have to convert it into Aidbox representation!
{% endhint %}

Here is an example of the **Patient.name** search parameter:

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /fhir/SearchParameter/Patient.name

# 200

id: Patient.name
type: string
name: name
resourceType: SearchParameter
base:
  - Patient
code: name
expression: Patient.name
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
GET /SearchParameter/Patient.name

# 200
name: name
type: string
resource: {id: Patient, resourceType: Entity}
expression:
- [name]
```
{% endtab %}
{% endtabs %}

#### Structure

All these attributes are required.

| path | type | desc |
| :--- | :--- | :--- |
| .**name** | [keyword](http://localhost:8765/static/console.html#/entities/Attribute?entity=keyword) | Name of search parameter, will be used in search query string |
| .**resource** | [Reference](http://localhost:8765/static/console.html#/entities/Attribute?entity=Reference) | Reference to resource, i.e. {id: Patient, resourceType: Reference} |
| .**type** | [keyword](http://localhost:8765/static/console.html#/entities/Attribute?entity=keyword) | Type of search parameter \(see [Types](searchparameter.md#types)\) |
| .**expression** | [expression](http://localhost:8765/static/console.html#/entities/Attribute?entity=SearchParameterExpression) | expression for elements to search \(see [Expression](searchparameter.md#expression)\) |

#### Search parameter types

| Type | Description |
| :--- | :--- |
| [number](https://www.hl7.org/fhir/search.html#number) | Search parameter SHALL be a number \(a whole number or a decimal\). |
| [date](https://www.hl7.org/fhir/search.html#date) | Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported. |
| [string](https://www.hl7.org/fhir/search.html#string) | Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string.  |
| [token](https://www.hl7.org/fhir/search.html#token) | Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem \(for codes\) and label, system and key \(for identifier\). Its value is either a string or a pair of namespace and value, separated by a "\|", depending on the modifier used. |
| [reference](https://www.hl7.org/fhir/search.html#reference) | A reference to another resource. |
| [composite](https://www.hl7.org/fhir/search.html#composite) | A composite search parameter that combines a search on two values together. |
| [quantity](https://www.hl7.org/fhir/search.html#quantity) | A search parameter that searches on quantity. |
| [uri](https://www.hl7.org/fhir/search.html#uri) | A search parameter that searches on a URI \(RFC 3986\). |

Depending on the value type, different modifiers can be applied.

#### Expression

Expression is a set of elements by which we want to search. Expression is a logically very simple subset of FHIRPath \(which can be efficiently implemented in a database\) expressed as data. Expression is an array of PathArrays; PathArray is an array of strings, integers, or objects, where each type has a special meaning:

* string - a name of the element
* integer - index in collection
* object - filter by pattern in the collection

For example, PathArray `["name", "given"]` extracts all given and family names as an array and flattens into one collection. Equivalent FHIRPath expression is `name.given | name.family`.

PathArray `[["name", 0, "given", 0]]` extracts only a first given of first name \(FHIRPath `name.0.given.0`

PathArray `[["telecom", {"system": "phone"}, "value"]]` extracts all values of the telecom collection filtered by `system=phone` \(FHIRPath: `telecom.where(system='phone').value` \)

An expression consists of an array of PathArrays, all results of the PathArray are concatenated into one collection.

### Define custom SearchParameter

You can define custom search params by creating a SearchParameter resource. Let's say you want to search a patient by city:

{% tabs %}
{% tab title="FHIR format" %}
```yaml
PUT /SearchParameter/Patient.city

name: city
type: token
resource: {id: Patient, resourceType: Entity}
expression: [[address, city]]
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
PUT /SearchParameter/Patient.city

name: city
type: token
resource: {id: Patient, resourceType: Entity}
expression: [[address, city]]
```
{% endtab %}
{% endtabs %}

Now let's test the new search parameter

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /fhir/Patient?city=New-York

resourceType: Bundle
type: searchset
entry:
  [...]
total: 10
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
GET /Patient?city=New-York

resourceType: Bundle
type: searchset
entry: [...]
total: 10
query-sql: 
- | 
  SELECT "patient".* FROM "patient" 
  WHERE ("patient".resource @> '{"address":[{"city":"NY"}]}')
   LIMIT 100 OFFSET 0
```
{% endtab %}
{% endtabs %}

