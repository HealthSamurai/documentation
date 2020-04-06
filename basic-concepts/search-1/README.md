---
description: Search API for FHIR resources
---

# Search

Aidbox provides a Search API for all stored resources. Aidbox Search API is superset of [FHIR Search API](https://www.hl7.org/fhir/search.html).  

{% hint style="info" %}
There are two versions of API, which differ by [resources format](../aidbox-and-fhir-formats.md):

* search by **`/[resourceType]`** returns results in [Aidbox Format](../aidbox-and-fhir-formats.md)
* search by **`/fhir/[resourceType]`** returns data in FHIR Format

All data stored and searched in Aidbox Format, and converted on fly to FHIR on FHIR endpoints!
{% endhint %}

Base search request is composed of list of pairs **param** = **value**:

```javascript
GET [base]/[resourceType]?param=value&param=value&...
```

Where **param** can be one of:

* [Underscored parameter](./#special-parameters) started with underscore, like **`_sort`**
* Name of [search parameter](searchparameter.md)
* [Chained parameter ](chained-parameters.md)expression
* [Dotted expression ](.-expressions.md)- started with **`.`**

Simple search by patient name:

```javascript
GET /Patient?name=Max&_elements=id, birthDAte
```

## Special Parameters

| Parameter |  |  |
| :--- | :--- | :--- |
| [\_id](_id.md) | FHIR | Search and sort by resource id |
| [\_lastUpdated](_lastupdated.md) | FHIR | Search and sort by resource last modification date |
| [\_text](_text-and-_content.md) | FHIR | Full text search by resource narrative |
| [\_content](_text-and-_content.md) | FHIR | Full text search by resource content |
| [\_ilike](_ilike.md) |  | `ILIKE` search by resource content |
| [\_elements](_elements.md) | FHIR+ | Include or exclude specific resource elements |
| [\_summary](_summary.md) | FHIR | Include only summary elements  |
| [\_list](_list.md) | FHIR | Search resources included into specific List |
| [\_sort](_sort.md) | FHIR | Sort search results |
| [\_total](_total-or-_countmethod.md) | FHIR | Turn on/off total count |
| [\_include](_include-and-_revinclude.md) | FHIR | Include referenced resources into result |
| [\_revinclude](_include-and-_revinclude.md) | FHIR | Include into result resources, which reference searched resources |
| [\_explain](_explain.md) |  | See query execution plan |
| [\_result](_result.md) |  | Change result format |

## Search Parameters

Search defined in terms of "[search parameters](searchparameter.md)". SearchParameter is a meta-resource, which describes which part of resource and how you can make searchable.

Search parameter can be one of the following types: 

| Type | Description |
| :--- | :--- |
| [number](https://www.hl7.org/fhir/search.html#number) | Search parameter SHALL be a number \(a whole number, or a decimal\). |
| [date](https://www.hl7.org/fhir/search.html#date) | Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported. |
| [string](https://www.hl7.org/fhir/search.html#string) | Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces. |
| [token](https://www.hl7.org/fhir/search.html#token) | Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem \(for codes\) and label, system and key \(for identifier\). Its value is either a string or a pair of namespace and value, separated by a "\|", depending on the modifier used. |
| [reference](https://www.hl7.org/fhir/search.html#reference) | A reference to another resource. |
| [composite](https://www.hl7.org/fhir/search.html#composite) | A composite search parameter that combines a search on two values together. |
| [quantity](https://www.hl7.org/fhir/search.html#quantity) | A search parameter that searches on a quantity. |
| [uri](https://www.hl7.org/fhir/search.html#uri) | A search parameter that searches on a URI \(RFC 3986\). |

Depending on the value type, different modifiers can be applied.

### Common

* `:missing`

```javascript
GET /Entity?description:missing=true
```

For `gender:missing=true`, server will return all resources that don't have a value for the gender parameter.

### Strings

* `:exact` — no partial matches, case sensitive;
* `:contains` — case insensitive, partial match at start or end.

Default behavior is case insensitive, partial match at start.

```javascript
GET /Patient?name:exact=Alex
```

### Token

* `:not` — reverse the code matching: return all resources that do not have a matching item.
* `:text` — case insensitive, partial match of text associated with token or token itself.
* `:i` — case insensitive, exact match of text associated with token or token itself.
* `:in` — the search parameter is a URI \(relative or absolute\) that identifies a value set, and the search parameter tests whether the coding is in the specified value set.

```
//Search for any patient with a gender that does not have the code "male"
//Note that for :not, the search does not return any resources that have a gen
GET /Patient?gender:not=male
```

```
//Search for any patient with johndoe@mail.com email
GET /Patient?email:text=JoHnDoE@mail.com
```

```
//Search for any patient with gmail or icloud email
GET /Patient?email:text=GMail.com,ICloud.com
```

```
//Search for any patient which have "fhir" in any of their contact info
GET /Patient?telecom:text=fhir
```

```
//Search for any condition that is in the institutions list of cardiac conditions
//Note: you must have Concept with valueset defined
GET /Condition?code:in=http://acme.org/fhir/ValueSet/cardiac-conditions
```

### Reference

Reference describes the relationship between resources. Following options are available for filtering by reference:

Сoded element or identifier

```javascript
GET /Patient?gender=female
```

```text
[parameter]=[id]
```

```text
[parameter]=[type]/[id]
```

For example, let's find all encounters related to a specified patient:

```javascript
GET /Encounter?subject=patientid
```

```javascript
GET /Encounter?subject=Patient/patientid
```

### Prefixes

For Numbers, Dates and Quantities \(will be supported\), we can use the following conditionals in search:

* `eq` - equal \(default\)
* `ne` - non-equal
* `lt` - less than
* `le` - less or equal
* `gt` - greater than
* `ge` - greater or equal

```javascript
GET /Patient?birthdate=gt1986-04-28
```

{% hint style="info" %}
Want to know more about [Aidbox](https://www.health-samurai.io/aidbox), FHIR, and search? Join our community [chat](https://community.aidbox.app/) \([\#aidbox](https://community.aidbox.app/) channel\).
{% endhint %}



