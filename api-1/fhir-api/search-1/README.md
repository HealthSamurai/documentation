---
description: Search API for FHIR resources
---

# Search

Aidbox provides a Search API for all stored resources. Aidbox Search API is a superset of the [FHIR Search API](https://www.hl7.org/fhir/search.html).

{% hint style="info" %}
There are two versions of API, which differ by the [resources format](../../../modules-1/fhir-resources/aidbox-and-fhir-formats.md):

* search by **`/[resourceType]`** returns results in [Aidbox Format](../../../modules-1/fhir-resources/aidbox-and-fhir-formats.md)
* search by **`/fhir/[resourceType]`** returns data in FHIR Format

All data is stored and searched in Aidbox Format and converted on the fly to FHIR on FHIR endpoints!
{% endhint %}

A base search request is composed of the list of pairs **param** = **value**:

{% tabs %}
{% tab title="FHIR format" %}
```
GET [base]/fhir/[resourceType]?param=value&param=value&...
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET [base]/[resourceType]?param=value&param=value&...
```
{% endtab %}
{% endtabs %}

Where **param** can be one of:

* [Underscored parameter](./#special-parameters) started with underscore, like **`_sort`**
* Name of [search parameter](searchparameter.md)
* [Chained parameter ](../usdmatch/chained-parameters.md)expression
* [Dotted expression ](../usdmatch/.-expressions.md)- started with **`.`**

Simple search by patient name:

{% tabs %}
{% tab title="FHIR format" %}
```
GET /fhir/Patient?name=Max&_elements=id, birthDAte
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Patient?name=Max&_elements=id, birthDAte
```
{% endtab %}
{% endtabs %}

## Special Parameters

| Parameter                                                                        | Type   | Description                                                                        |
| -------------------------------------------------------------------------------- | ------ | ---------------------------------------------------------------------------------- |
| [\_id](../usdmatch/search-parameters-list/\_id.md)                               | FHIR   | Search and sort by resource id                                                     |
| [\_lastUpdated](../usdmatch/search-parameters-list/\_lastupdated.md)             | FHIR   | Search and sort by resource last modification date                                 |
| [\_text](../usdmatch/search-parameters-list/\_text-and-\_content.md)             | FHIR   | Full text search by resource narrative                                             |
| [\_content](../usdmatch/search-parameters-list/\_text-and-\_content.md)          | FHIR   | Full text search by resource content                                               |
| [\_ilike](../usdmatch/search-parameters-list/\_ilike.md)                         |        | `ILIKE` search by resource content                                                 |
| [\_elements](../usdmatch/search-parameters-list/\_elements.md)                   | FHIR+  | Include or exclude specific resource elements                                      |
| [\_summary](../usdmatch/search-parameters-list/\_summary.md)                     | FHIR   | Include only summary elements                                                      |
| [\_list](../usdmatch/search-parameters-list/\_list.md)                           | FHIR   | Search resources included into specific List                                       |
| [\_sort](../usdmatch/search-parameters-list/\_sort.md)                           | FHIR   | Sort search results                                                                |
| [\_total](../usdmatch/search-parameters-list/\_total-or-\_countmethod.md)        | FHIR   | Turn on/off total count                                                            |
| [\_include](../usdmatch/search-parameters-list/\_include-and-\_revinclude.md)    | FHIR   | Include referenced resources into result                                           |
| [\_with](../usdmatch/search-parameters-list/\_include-and-\_revinclude.md)       | Aidbox | Include into result resources (compact way compared to \_include and \_revinclude) |
| [\_revinclude](../usdmatch/search-parameters-list/\_include-and-\_revinclude.md) | FHIR   | Include into result resources, which reference searched resources                  |
| [\_explain](../usdmatch/search-parameters-list/\_explain.md)                     |        | Get query execution plan                                                           |
| [\_result](../usdmatch/search-parameters-list/\_result.md)                       |        | Change result format                                                               |
| \_security                                                                       | FHIR   |                                                                                    |
| [\_profile](../usdmatch/search-parameters-list/\_profile.md)                     | FHIR   | Search by resource profile                                                         |
| \_has                                                                            | FHIR   |                                                                                    |
| \_tag                                                                            | FHIR   |                                                                                    |

## Search Parameters

Search is defined in terms of "[search parameters](searchparameter.md)". SearchParameter is a meta-resource, which describes which part of the resource it is and how you can make it searchable.

Search parameter can be one of the following types:

| Type                                                        | Description                                                                                                                                                                                                                                                                                                  |
| ----------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| [number](https://www.hl7.org/fhir/search.html#number)       | Search parameter SHALL be a number (a whole number, or a decimal).                                                                                                                                                                                                                                           |
| [date](https://www.hl7.org/fhir/search.html#date)           | Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.                                                                                                                                                                                       |
| [string](https://www.hl7.org/fhir/search.html#string)       | Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.                                                                                                                        |
| [token](https://www.hl7.org/fhir/search.html#token)         | Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "\|", depending on the modifier used. |
| [reference](https://www.hl7.org/fhir/search.html#reference) | A reference to another resource.                                                                                                                                                                                                                                                                             |
| [composite](https://www.hl7.org/fhir/search.html#composite) | A composite search parameter that combines a search on two values together.                                                                                                                                                                                                                                  |
| [quantity](https://www.hl7.org/fhir/search.html#quantity)   | A search parameter that searches on a quantity.                                                                                                                                                                                                                                                              |
| [uri](https://www.hl7.org/fhir/search.html#uri)             | A search parameter that searches on a URI (RFC 3986).                                                                                                                                                                                                                                                        |

Depending on the value type, different modifiers can be applied.

### Common

* `:missing`
* `:text` — case insensitive, partial match of text & data associated with search parameter.

{% tabs %}
{% tab title="FHIR format" %}
```javascript
GET /fhir/Entity?description:missing=true
// For gender:missing=true,
// server will return all resources that
// don't have a value for the gender parameter. 
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Entity?description:missing=true
// For gender:missing=true,
// server will return all resources that
// don't have a value for the gender parameter.
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="FHIR format" %}
```javascript
// Search for any patient with johndoe@mail.com email
GET /fhir/Patient?email:text=JoHnDoE@mail.com

// Search for any patient with gmail or icloud email
GET /fhir/Patient?email:text=GMail.com,ICloud.com

// Search for any patient which have "fhir" in any of their contact info
GET /fhir/Patient?telecom:text=fhir
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
// Search for any patient with johndoe@mail.com email
GET /Patient?email:text=JoHnDoE@mail.com

// Search for any patient with gmail or icloud email
GET /Patient?email:text=GMail.com,ICloud.com

// Search for any patient which have "fhir" in any of their contact info
GET /Patient?telecom:text=fhir
```
{% endtab %}
{% endtabs %}

### Strings

* `:exact` — no partial matches, case sensitive;
* `:contains` — case insensitive, partial match at start or end.

Default behavior is case insensitive, partial match at start.

{% tabs %}
{% tab title="FHIR format" %}
```javascript
GET /fhir/Patient?name:exact=Alex
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Patient?name:exact=Alex
```
{% endtab %}
{% endtabs %}

### Token

* `:not` — reverses the code matching: returns all resources that do not have a matching item.
* `:i` — case insensitive, exact match of text associated with token or token itself.
* `:in` — the search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is in the specified value set.

{% tabs %}
{% tab title="FHIR format" %}
```javascript
//Search for any patient with a gender that does not have the code "male"
//Note that for :not, the search does not return any resources that have a gen
GET /fhir/Patient?gender:not=male
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
//Search for any patient with a gender that does not have the code "male"
//Note that for :not, the search does not return any resources that have a gen
GET /Patient?gender:not=male
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="FHIR format" %}
```javascript
// Search for patient with email which is Foo@Bar.BAZ
GET /fhir/Patient?email:i=foo@bar.baz
// Note: this search won't find patient with emails like:
// ffoo@bar.baz
// foo@bar.bazz
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
// Search for patient with email which is Foo@Bar.BAZ
GET /Patient?email:i=foo@bar.baz
// Note: this search won't find patient with emails like:
// ffoo@bar.baz
// foo@bar.bazz
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="FHIR format" %}
```javascript
//Search for any condition that is in the institutions list of cardiac conditions
//Note: you must have Concept with valueset defined

GET /fhir/Condition?code:in=/ValueSet/cardiac-conditions
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
//Search for any condition that is in the institutions list of cardiac conditions
//Note: you must have Concept with valueset defined

GET /Condition?code:in=/ValueSet/cardiac-conditions
```
{% endtab %}
{% endtabs %}

### Reference

Reference describes the relationship between resources. Following options are available for filtering by reference:

```javascript
[parameter]=[id]
[parameter]=[type]/[id]
```

For example, let's find all encounters related to a specified patient:

{% tabs %}
{% tab title="FHIR format" %}
```javascript
GET /fhir/Encounter?subject=patientid
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Encounter?subject=patientid
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="FHIR format" %}
```javascript
GET /fhir/Encounter?subject=Patient/patientid
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Encounter?subject=Patient/patientid
```
{% endtab %}
{% endtabs %}

### Prefixes

For Numbers, Dates, and Quantities (will be supported), we can use the following conditionals in a search:

* `eq` - equal (default)
* `ne` - non-equal
* `lt` - less than
* `le` - less or equal
* `gt` - greater than
* `ge` - greater or equal

{% tabs %}
{% tab title="FHIR format" %}
```javascript
GET /fhir/Patient?birthdate=gt1986-04-28
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Patient?birthdate=gt1986-04-28
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Want to know more about [Aidbox](https://www.health-samurai.io/aidbox), FHIR, and search? Join our community [chat](https://t.me/aidbox) .
{% endhint %}
