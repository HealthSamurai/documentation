# Search

### Overview

Search is a base [FHIR operation](https://www.hl7.org/fhir/search.html). Search is used to filter and receive linked entities.

Base search structure looks like this:

```javascript
GET [base]/[type]?name=value&...{&_format=[mime-type]}}
```

Simple search by patient name:

```javascript
GET /Patient?name=Max
```

For optimization of search results, we can specify fields of the output bundle:

```javascript
GET /Patient?_elements=id,birthDate
```

All sample requests can be run in Postman:[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/7dc2f250801cc6b71709#?env[Aidbox.Cloud]=W3sia2V5IjoiYmFzZTEiLCJ2YWx1ZSI6Imh0dHBzOi8vbWVyZWRpdGguYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6ZmFsc2V9LHsia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9wYXZseXNoaW5hMjAxODExMDkuYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX1d)

### Search Parameters

Some search parameters are available for all resources:

* `_id` logical id of entity
* `_lastUpdated` last modification time
* `_text` filter on resource content

A list of available search parameters for specific resource can be obtained via the following request:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /SearchParameter?resource=Patient&_elements=name
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: Bundle
type: searchset
params:
- resourceType: SearchParameter
  type: param
  name: resource
  modifier: null
  values:
  - {value: Patient}
  search-param:
    name: resource
    type: reference
    expression:
    - [resource]
    resource: {resourceType: Entity, id: SearchParameter}
    module: proto
    typedExpression:
    - path: [resource]
      type: Reference
- type: elements
  exclude: false
  value:
    name: {}
query-sql: ['SELECT "searchparameter".* FROM "searchparameter" WHERE knife_extract_text("searchparameter".resource,
    ?) && ARRAY[?] LIMIT ? OFFSET ?', '[["resource","id"]]', Patient, 100, 0]
query-time: 10
entry:
- resource: {name: address-city, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: active, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: given, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: address-state, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: general-practitioner, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: gender, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: name, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: family, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: telecom, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: animal-species, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: identifier, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: link, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: address-use, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: language, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: phonetic, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: death-date, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: animal-breed, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: deceased, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: address, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: email, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: organization, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: birthdate, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: address-country, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: phone, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
- resource: {name: address-postalcode, resourceType: SearchParameter}
  fullUrl: https://example.aidbox.app/SearchParameter/
total: 25
link:
- {relation: first, url: '/SearchParameter?resource=Patient&_elements=name&_page=1'}
```
{% endtab %}
{% endtabs %}

With this command above, we can get a list of all search parameters for resourceType `Patient` \(given, address-state, etc.\)

#### Modifiers

We also can do more complex search requests through modifiers that depend on search field type.

Each search parameter value can be one of the following types: 

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

#### Common

* `:missing`

```javascript
GET /Entity?description:missing=true
```

For `gender:missing=true`, server will return all resources that don't have a value for the gender parameter.

#### Strings

* `:exact` — no partial matches, case sensitive;
* `:contains` — case insensitive, partial match at start or end.

Default behavior is case insensitive, partial match at start.

```javascript
GET /Patient?name:exact=Alex
```

#### Token

#### Reference

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

#### Prefixes

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

### Chained Parameters

### \_has

### Dot parameters extension 

```javascript
GET /Patient?.name.0.given=Nikolai
```

### \_sort

We can define order in result bundle by selected parameters.

For a more accurate search, we can filter by nested fields of related entities. Reference parameters may be "chained" through `.` 

Obtain all **encounters** with patients \(**subject** — link to the patient\) with name Alex:

```javascript
GET /Encounter?subject:Patient.name=Alex
```

You can use several chained parameters by base resource:

```javascript
GET /Encounter?part-of:Encounter._id=enc1&subject:Patient._id=patient1
```

```javascript
GET /Organization?_sort=name
```

"Reversed chaining" is selection of resources based on the properties of resources that refer to them.

Obtain patient resources where encounter has id = enc1 and refer to the patient through subject field:

```javascript
GET /Patient?_has:Encounter:subject:_id=enc1
```

### \_include & \_revinclude

We have an ability to include linked entities into result. For example, we want to get all encounters and patients related to them. Structure of the request will be: `_include=<reference search parameter> or _include=<Resource>:<reference search parameter>`

```javascript
GET /Encounter?_include=subject
```

```javascript
GET /Encounter?_include=Encounter:subject
```

Or for specific **id:**

```javascript
GET /Encounter?_id=enc1&_include=Encounter:subject
```

Reverse include  is specified as a `_revinclude`

```javascript
GET /Patient?_id=patient1&_revinclude=Encounter:subject
```

We have additional modifier \(for \_include __and __\_revinclude\) `:logical` for search by identifier:

```javascript
GET /Patient?_id=patient1&_revinclude:logical=Encounter:subject
```

Ascending order is used by default but we can change this behavior. To sort by descending order, add`-` before a parameter or specify `:asc/:desc` after search parameter.

We have an access to attributes of a resource through `.` 

```javascript
GET /Organization?_sort=-name
```

It is better described by resulting SQL:

```sql
SELECT "patient".* 
FROM "patient" 
WHERE ("patient".resource#>>'{name,0,given}'in ('Nikolai'))
```

```javascript
GET /Organization?_sort:desc=name
```

As we know, not all attributes can be used as search parameters of a resource but in **\_sort** we have an ability to use them via `.` notation.

For example, with the following request we will receive an error:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Encounter?_sort=-id
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 500

resourceType: OperationOutcome
...
No search parameter for Encounter.id
```
{% endtab %}
{% endtabs %}

We can avoid it with such type of request:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Encounter?_sort=-.id
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status 200

resourceType: Bundle
entry:
...
```
{% endtab %}
{% endtabs %}

Also, we can use several fields for sorting, for this we need to add them through `,` . Priority will be determined from left to right.

```javascript
GET /Encounter?_sort=status,-.id
```

In the example above, we search for all encounters and sort them by the status parameter in cases of equality, the sorting will occur by the id field in the reverse order.

### \_total \( \_countMethod \) 

By default, for all search requests Aidbox returns total number in result which represents how many resources matched the criteria. But to do this, we run second query for count which takes some additional CPU time. To get a response faster, especially on big amounts of data, you can change this behavior using the **\_total** parameter. The **\_total** parameter can have following values:

* `none` - do not run count query 
* `estimated` - roughly estimate number of results
* `accurate` - run accurate count

### Paging

Search results can contain many records, for more convenient work we can use pagination. Available parameters are: **\_count** — total records on the page \(default value — 100\), **page** — specific page, output Bundle includes link section with **first**, **self**, **next**, **previous,** and **last** page constructed URLs.

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient?_count=10&page=3
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: Bundle
type: searchset
params:
- {type: count, value: 10}
- {type: page, value: 3}
query-sql: ['SELECT "patient".* FROM "patient" LIMIT ? OFFSET ?', 10, 20]
query-time: 9
entry:
- resource:
    ...
total: 206
link:
- {relation: first, url: '/Patient?_count=10&page=1'}
- {relation: self, url: '/Patient?_count=10&page=3'}
- {relation: next, url: '/Patient?_count=10&page=4'}
- {relation: previous, url: '/Patient?_count=10&page=2'}
- {relation: last, url: '/Patient?_count=10&page=21'}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Want to know more about [Aidbox](https://www.health-samurai.io/aidbox), FHIR, and search? Join our community [chat](https://community.aidbox.app/) \([\#aidbox](https://community.aidbox.app/) channel\).
{% endhint %}



