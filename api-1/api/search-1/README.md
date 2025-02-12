---
description: Search API for FHIR resources
---

# Search

Aidbox provides a Search API for all stored resources. Aidbox Search API is a superset of the [FHIR Search API](https://www.hl7.org/fhir/search.html).

{% hint style="info" %}
There are two versions of API, which differ by the [resources format](../../../storage-1/other/aidbox-and-fhir-formats.md):

* search by **`/[resourceType]`** returns results in [Aidbox Format](../../../storage-1/other/aidbox-and-fhir-formats.md)
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
* [Chained parameter ](chained-parameters.md)expression
* [Dotted expression ](.-expressions.md)- started with **`.`**

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

<table><thead><tr><th width="180">Parameter</th><th width="145.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td><a href="search-parameters-list/_text-and-_content.md">_content</a></td><td>FHIR</td><td>Full text search by resource content</td></tr><tr><td><a href="search-parameters-list/_elements.md">_elements</a></td><td>FHIR+</td><td>Include or exclude specific resource elements</td></tr><tr><td><a href="search-parameters-list/_explain.md">_explain</a></td><td></td><td>Get query execution plan</td></tr><tr><td>_filter</td><td>FHIR</td><td>Perform an advanced search</td></tr><tr><td><a href="chained-parameters.md">_has</a></td><td>FHIR</td><td>Reverse chaining</td></tr><tr><td><a href="search-parameters-list/_id.md">_id</a></td><td>FHIR</td><td>Search and sort by resource id</td></tr><tr><td><a href="search-parameters-list/_ilike.md">_ilike</a></td><td></td><td><code>ILIKE</code> search by resource content</td></tr><tr><td><a href="search-parameters-list/_include-and-_revinclude.md">_include</a></td><td>FHIR</td><td>Include referenced resources into result</td></tr><tr><td><a href="search-parameters-list/_lastupdated.md">_lastUpdated</a></td><td>FHIR</td><td>Search and sort by resource last modification date</td></tr><tr><td><a href="search-parameters-list/_list.md">_list</a></td><td>FHIR</td><td>Search resources included into specific List</td></tr><tr><td><a href="search-parameters-list/_profile.md">_profile</a></td><td>FHIR</td><td>Search by resource profile</td></tr><tr><td><a href="search-parameters-list/_result.md">_result</a></td><td></td><td>Change result format</td></tr><tr><td><a href="search-parameters-list/_include-and-_revinclude.md">_revinclude</a></td><td>FHIR</td><td>Include into result resources, which reference searched resources</td></tr><tr><td>_security</td><td>FHIR</td><td></td></tr><tr><td><a href="search-parameters-list/_sort.md">_sort</a></td><td>FHIR</td><td>Sort search results</td></tr><tr><td><a href="search-parameters-list/_summary.md">_summary</a></td><td>FHIR</td><td>Include only summary elements</td></tr><tr><td>_tag</td><td>FHIR</td><td></td></tr><tr><td><a href="search-parameters-list/_text-and-_content.md">_text</a></td><td>FHIR</td><td>Full text search by resource narrative</td></tr><tr><td><a href="search-parameters-list/_total-or-_countmethod.md">_total</a></td><td>FHIR</td><td>Turn on/off total count</td></tr><tr><td><a href="search-parameters-list/_include-and-_revinclude.md">_with</a></td><td>Aidbox</td><td>Include into result resources (compact way compared to _include and _revinclude)</td></tr></tbody></table>

## Search Parameters

Search is defined in terms of "[search parameters](searchparameter.md)". SearchParameter is a meta-resource, which describes which part of the resource it is and how you can make it searchable.

Search parameter can be one of the following types:

<table><thead><tr><th width="138.33333333333331">Type</th><th width="141">Support<select></select></th><th>Description</th></tr></thead><tbody><tr><td><a href="https://www.hl7.org/fhir/search.html#number">number</a></td><td></td><td>Search parameter SHALL be a number (a whole number, or a decimal).</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#date">date</a></td><td></td><td>Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#string">string</a></td><td></td><td>Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#token">token</a></td><td></td><td>Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "|", depending on the modifier used.</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#reference">reference</a></td><td></td><td>A reference to another resource.</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#composite">composite</a></td><td></td><td>A composite search parameter that combines a search on two values together.</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#quantity">quantity</a></td><td></td><td>A search parameter that searches on a quantity.</td></tr><tr><td><a href="https://www.hl7.org/fhir/search.html#uri">uri</a></td><td></td><td>A search parameter that searches on a URI (RFC 3986).</td></tr></tbody></table>

Depending on the value type, different modifiers can be applied.

## Supported modifiers

<table><thead><tr><th width="130.33333333333331">Modifier</th><th width="221">Types</th><th>Description</th></tr></thead><tbody><tr><td>missing</td><td>all</td><td>Tests whether the value in a resource is present (when the supplied parameter value is <code>true</code>) or absent (when the supplied parameter value is <code>false</code>)</td></tr><tr><td>text</td><td>string, token, reference, uri</td><td>Tests whether the textual value in a resource matches the supplied parameter value using basic string matching (begins with or is, case-insensitive)</td></tr><tr><td>below</td><td>uri</td><td>Tests whether the value in a resource is or is subsumed by the supplied parameter value (is-a, or hierarchical relationships)</td></tr><tr><td>contains</td><td>string</td><td>Case insensitive, partial match at start or end</td></tr><tr><td>ends, ew</td><td>string</td><td>Case insensitive, partial match at end</td></tr><tr><td>exact</td><td>string</td><td>No partial matches, case sensitive</td></tr><tr><td>starts, sw</td><td>string</td><td>Case insensitive, partial match at start</td></tr><tr><td>btw</td><td>date</td><td>Search between two dates</td></tr><tr><td>identifier</td><td>reference</td><td>Tests whether the <code>Reference.identifier</code> in a resource (rather than the <code>Reference.reference</code>) matches the supplied parameter value</td></tr><tr><td>not</td><td>reference, token, uri</td><td>Reverses the code matching: returns all resources that do not have a matching item.</td></tr><tr><td>i</td><td>token</td><td>Case insensitive, exact match of text associated with token or token itself</td></tr><tr><td>in</td><td>token</td><td>Tests whether the value in a resource is a member of the supplied parameter ValueSet</td></tr><tr><td>of-type</td><td>token (only Identifier)</td><td>Tests whether the <code>Identifier</code> value in a resource matches the supplied parameter value</td></tr><tr><td>iterate, reverse, logical</td><td>n/a</td><td>See <a href="search-parameters-list/_include-and-_revinclude.md">_include</a></td></tr></tbody></table>

### Search with modifiers examples

#### `:missing`

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

#### `:text`

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

#### `:exact`

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

#### `:not`

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

#### `i`

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

#### `in`

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

## Prefixes

For Numbers, Dates, and Quantities, we can use the following conditionals in a search:

* `eq` - equal (default)
* `ne` - non-equal
* `lt` - less than
* `le` - less or equal
* `gt` - greater than
* `ge` - greater or equal

For example, to search for patients, who were born before 1986-04-28:

{% tabs %}
{% tab title="FHIR format" %}
```javascript
GET /fhir/Patient?birthdate=lt1986-04-28
```
{% endtab %}

{% tab title="Aidbox format" %}
```javascript
GET /Patient?birthdate=lt1986-04-28
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Want to know more about [Aidbox](https://www.health-samurai.io/aidbox), FHIR, and search? Join our community [chat](https://t.me/aidbox) .
{% endhint %}

## Page links

Aidbox returns links in response in search requests:

```yaml
GET /fhir/Patient

resourceType: Bundle
type: searchset
meta:
  versionId: '0'
total: 0
link:
  - relation: first
    url: /fhir/Patient?page=1
  - relation: self
    url: /fhir/Patient?page=1
entry: []
```

There are two options that can modify url param content:

* _AIDBOX\_COMPLIANCE_ environment variable
* _X-Original-Uri_ header

#### AIDBOX\_COMPLIANCE env

IF `AIDBOX_COMPLIANCE` env is enabled then `AIDBOX_BASE_URL` environment variable will be used like this: `<AIDBOX_BASE_URL>/fhir/Patient?page=1`. For example we set `AIDBOX_BASE_URL` to `"https://example.com"`:

```yaml
GET /fhir/Patient

resourceType: Bundle
type: searchset
meta:
  versionId: '0'
total: 0
link:
  - relation: first
    url: https://example.com/fhir/Patient?page=1
  - relation: self
    url: https://example.com/fhir/Patient?page=1
entry: []
```

#### X-Original-Uri header

This header allows you completely overwrite content of `url` param. Aidbox will automatically add `page` param to your link, or replace if it exists. For example:

```yaml
GET /fhir/Patient?name=example
x-original-uri: https://example.com/fhir/Patient?page=4

resourceType: Bundle
type: searchset
meta:
  versionId: '0'
total: 0
link:
  - relation: first
    url: https://example.com/fhir/Patient?page=1
  - relation: self
    url: https://example.com/fhir/Patient?page=4
entry: []
```
