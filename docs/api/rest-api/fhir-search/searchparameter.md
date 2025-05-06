# SearchParameter

[SearchParameter FHIR resource](https://www.hl7.org/fhir/searchparameter.html) defines how a specific resource property can be used in search operations. It specifies:

* The name of the parameter used in the URL
* The type of parameter (string, token, reference, etc., explained below)
* The path to the property in the resource that is being searched
* Any special handling rules or modifiers that apply

For example, the Patient resource [has search parameters](https://www.hl7.org/fhir/patient.html#search) like:

* `name` - Search by patient name (string type)
* `birthdate` - Search by date of birth (date type)
* `identifier` - Search by patient identifier (token type)

Search parameters can be defined in:

* The core FHIR specification (e.g. [4.0.1](https://hl7.org/fhir/R4/))
* Implementation guides (e.g. [US Core](https://build.fhir.org/ig/HL7/US-Core/))
* Aidbox itself in `app.aidbox.main` package (see [Custom Search Parameter tutorial](broken-reference))

## SearchParameter example

Patient SearchParameters can be found at the end of [FHIR Patient page](https://www.hl7.org/fhir/patient.html#search) or in AidboxUI (see [Custom Search Parameter tutorial](broken-reference)).

Patient's `name` SearchParameter example:

```json
{
  "url": "http://hl7.org/fhir/SearchParameter/Patient-name",
  "id": "Patient-name",
  "base": [
    "Patient"
  ],
  "expression": "Patient.name",
  "code": "name",
  "name": "name",
  "status": "draft",
  "type": "string",
  "version": "4.0.1",
  "resourceType": "SearchParameter",
  "description": "A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text"
}
```

## SearchParameter fields

This table contains properties required by the FHIR specification and properties that Aidbox interprets.

<table><thead><tr><th width="144">Property</th><th width="167">FHIR datatype</th><th>Description</th></tr></thead><tbody><tr><td>url</td><td>uri</td><td>Search parameter unique canonical url</td></tr><tr><td>version</td><td>string</td><td>Search parameter version</td></tr><tr><td>name</td><td>string</td><td>Search parameter name, Aidbox ignores it</td></tr><tr><td>status</td><td>code</td><td>draft | active | retired | unknown</td></tr><tr><td>description</td><td>markdown</td><td>Human readable description of the search parameter</td></tr><tr><td>code</td><td>code</td><td>The code used in the URL to invoke this search parameter</td></tr><tr><td>base</td><td>code[]</td><td>The resource type(s) this search parameter applies to</td></tr><tr><td>type</td><td>code</td><td>number | date | string | token | reference | composite | quantity | uri | special</td></tr><tr><td>expression</td><td>string</td><td><a href="https://hl7.org/fhir/fhirpath.html">FHIRPath</a> expression that extracts the values</td></tr><tr><td>component</td><td>BackboneElement</td><td>For Composite SearchParameters to define the parts</td></tr></tbody></table>

## Search Parameter Types

SearchParameter type is defined in the `SearchParameter.type` field. It is important to understand the type of SearchParameter because there are differences in how they are processed.

For example, if the search parameter with code `title` is of type 'token', the search:

```
GET /fhir/Patient?title=smith
```

will match resources with `title` exactly equal to "smith", not "Smith" or "smiths".\
But it `title` is of type `string`, the search will match "smith", "Smith", "smiths", etc.

The list of SearchParameter types:

| Type      | Description                 | Example                                    |
| --------- | --------------------------- | ------------------------------------------ |
| String    | Plain text matching         | `name=smith`                               |
| Token     | Coded or identifier values  | `status=active`, `code=123`                |
| Reference | Links to other resources    | `patient=Patient/123`                      |
| Date      | Date/time values and ranges | `birthdate=2020`, `date=ge2019-01`         |
| Number    | Numeric values and ranges   | `age=55`, `length=gt100`                   |
| Quantity  | Values with units           | `weight=100`                               |
| Uri       | URIs                        | `url=http://example.com`                   |
| Composite | Combines multiple values    | `component-code-value-quantity=code$value` |
| Special   | Implementation specific     | `_filter`, `_text`, `_content`             |

### string

String search parameters match against sequences of characters. Common search parameters of this type include names, addresses, and narrative text.

String searches are **case-insensitive** and **accent-insensitive** by default. The search matches if the field value equals or starts with the search parameter value.

For example:

* `name=John` will match "John", "Johnny", "Johnson"
* `name=eve` will match "Eve" and "Steven"
* `name=andré` will match "Andre", "André", "Andres"

String searches support these modifiers:

* `:contains` - Matches if the parameter value appears anywhere in the field (not just the start)
* `:exact` - Matches the entire string exactly, including case and accents
* `:missing` - Matches resources that do not have a value in the field
* `:text` - Matches if the textual value in a resource matches the supplied parameter value using basic string matching (begins with or is, case-insensitive)
* `:starts` - Matches if the parameter value appears at the start of the field
* `:ends` - Matches if the parameter value appears at the end of the field

For example:

* `name:contains=eve` will match "Eve", "Steven", "Genevieve"
* `name:exact=John` will only match "John", not "john" or "Johnny"

### token

Token search is used to search by **exact match** of status codes, terminology codes, identifiers, etc.

For example:

* `status=active` will match resources with `status` exactly equal to "active"
* `code=123` will match resources with `code` exactly equal to "123"
* `identifier=mysystem|123` will match resources with [identifier](https://www.hl7.org/fhir/datatypes.html#Identifier) exactly equal to "123" of system "mysystem"

Token searches support these modifiers:

| Modifier | Description                                                                                                                                       | Example                        |
| -------- | ------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------ |
| :missing | Matches resources that do not have a value in the field                                                                                           | `code:missing=true`            |
| :text    | Matches if the textual value in a resource matches the supplied parameter value using basic string matching (begins with or is, case-insensitive) | `code:text=test`               |
| :in      | Search within a ValueSet                                                                                                                          | `code:in=/ValueSet/test-codes` |
| :i       | Case-insensitive search                                                                                                                           | `code:i=TEST123`               |
| :not     | Negates the search value                                                                                                                          | `code:not=inactive`            |
| :of-type | Search for resource identifier                                                                                                                    | \`identifier:of-type=system    |

### uri

Uri search parameters match against URIs (RFC 3986). Common search parameters of this type include URLs, URNs, and other URI-based identifiers.

Uri searches are **case-sensitive** and match the entire URI string **exactly**.

For example:

* `url=http://example.com` will match exactly "http://example.com"
* `url=urn:oid:1.2.3.4` will match exactly "urn:oid:1.2.3.4"

URI searches support these modifiers:

| Modifier | Description                                                                                                                   | Example                           |
| -------- | ----------------------------------------------------------------------------------------------------------------------------- | --------------------------------- |
| :missing | Matches resources that do not have a value in the field                                                                       | `url:missing=true`                |
| :below   | Tests whether the value in a resource is or is subsumed by the supplied parameter value (is-a, or hierarchical relationships) | `url:below=http://acme.org/fhir/` |
| :not     | Negates the search value                                                                                                      | `url:not=http://example.com`      |

### reference

Reference search parameters match against references to other resources. They are used to search for resources that reference a specific resource.

For example:

* `subject=Patient/123` will match resources that reference the Patient resource with ID "123"
* `author=Practitioner/456` will match resources that reference the Practitioner resource with ID "456"
* `subject=123` will match resources that reference **any resource with ID "123"** (depending on the SearchParameter definition)

Reference searches support these modifiers:

| Modifier    | Description                                                                                                                                         | Example                     |
| ----------- | --------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------- |
| :identifier | Tests whether the Reference.identifier in a resource (rather than the Reference.reference) matches the supplied parameter value (logical reference) | `patient:identifier=system` |
| :missing    | Matches resources that do not have a value in the field                                                                                             | `patient:missing=true`      |
| :not        | Negates the search value                                                                                                                            | `patient:not=Patient/123`   |

### date

As mentioned in [FHIR Search specification](https://www.hl7.org/fhir/search.html#date), Date parameters may be used with the date, dateTime, instant, Period, or Timing data types. Every date in the search matches `yyyy-mm-ddThh:mm:ss[Z|(+|-)hh:mm]` format.

**FHIR Ranges**

All date comparisons in FHIR are range-based. The range consists of a lower bound and an upper bound.

FHIR [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime) value can be an incomplete datetime. E.g. `2020` is a FHIR dateTime, but it is an incomplete datetime, since it doesn't specify month, day, etc.

The lower bound of the FHIR dateTime is the earliest complete datetime, which matches the FHIR dateTime value. For 2020, the lower bound is `2020-01-01T00:00:00Z`.

Vice versa, the upper bound of the FHIR dateTime is the latest complete datetime.

For FHIR [Period](https://build.fhir.org/datatypes.html#Period) lower bound is the lower bound of the `Period.start`, similarly for the upper bound. Missing start or end in the period is treated as infinity.

**Search using ranges**

Searching by date is done by comparing the search range and the resource (target) range. The search range is the range of the search value.

For example, searching `GET /fhir/Patient?birthdate=2020` means that the search range is `2020-01-01T00:00:00Z—2020-12-31T23:59:59Z`.

Resource range is the range of the target resource value. For example, if the Patient resource contains birthDate `2020-01-01`, the resource range is `2020-01-01T00:00:00Z—2020-01-01T23:59:59Z`.

Let $$(s, S)$$ be the search range; $$(r, R)$$​ be the resource range.

Description of FHIR prefixes:

* `eq` - equal. Formula: $$(s, S) \supset (r, R)$$​
* `ne` - not equal. Formula: $$(s, S) \not\supset (r, R)$$​
* `lt` - less than. Formula: $$r \le s$$​
* `le` - less than or equal. Formula: $$r \le S$$
* `gt` - greater than. Formula: $$R > S$$
* `ge` - greater than or equal. Formula: $$R \ge s$$​
* `sa` - starts after. Formula: $$r \ge S$$​
* `eb` - ends before. Formula: $$R \le s$$

### number

Number search parameters match against decimal and integer values. They support the same prefixes as date parameters for range comparisons:

| Prefix | Description           | Example             |
| ------ | --------------------- | ------------------- |
| eq     | Equal (default)       | `probability=0.8`   |
| ne     | Not equal             | `probability=ne0.8` |
| gt     | Greater than          | `probability=gt0.8` |
| lt     | Less than             | `probability=lt0.8` |
| ge     | Greater than or equal | `probability=ge0.8` |
| le     | Less than or equal    | `probability=le0.8` |

Numbers can be expressed in decimal or exponential format:

* `probability=0.8`
* `probability=8e-1`

The number search parameter also supports the `:missing` modifier to test for the presence/absence of values:

* `probability:missing=true` matches resources with no probability value
* `probability:missing=false` matches resources that have a probability value

Multiple values can be combined with a comma for OR logic:

* `probability=0.8,0.9` matches resources with a probability of either 0.8 OR 0.9

Note that Aidbox does not involve an implicit range while searching on decimals, as FHIR suggests. Aidbox always searches for an exact number.

### quantity

Quantity search parameters match against values with units. They support the same prefixes as number parameters for range comparisons:

| Prefix | Description     | Example        |
| ------ | --------------- | -------------- |
| eq     | Equal (default) | `weight=100`   |
| ne     | Not equal       | `weight=ne100` |
| gt     | Greater than    | `weight=gt100` |

However, Aidbox supports only numbers, not system with code.\
For example, `weight=100` will match resources with `weight` exactly equal to "100", the unit is ignored.

### composite

Since version 2308, Aidbox supports Composite Search Parameters.

[Composite Search Parameters](https://www.hl7.org/fhir/search.html#composite) are special search parameters that match resources by two or more values, separated by a `$` sign. Such search parameters will search not by simple intersection like `search-param1=value1&search-param2=value2`, but more strictly.

For example, take a look at [Observation](https://www.hl7.org/fhir/observation.html) resource structure and suppose we have the following resource:

```yaml
id: my-observation
component:
- code: loinc|12907-2
  valueQuantity: 
    value: 1
- code: loinc|12907-1
  valueQuantity: 
    value: 2
# ...
```

If we want the search to match _my-observation_ only if some component has both `code = loinc|12907-2` and `valueQuantity=1`, we must use composite search:

```
GET /fhir/Observation?code-value-quantity=loinc|12907-2$1 // found
GET /fhir/Observation?code-value-quantity=loinc|12907-2$2 // not found
GET /fhir/Observation?code-value-quantity=loinc|12907-1$1 // not found
GET /fhir/Observation?code-value-quantity=loinc|12907-1$2 // found
```

However, if we use simple intersection, _my-observation_ may be found in all cases:

```
GET /fhir/Observation?code=loinc|12907-2&value-quantity=1 // found
GET /fhir/Observation?code=loinc|12907-2&value-quantity=2 // found
GET /fhir/Observation?code=loinc|12907-1&value-quantity=1 // found
GET /fhir/Observation?code=loinc|12907-1&value-quantity=2 // found
```

### special

Special search parameters have unique search behavior that cannot be expressed using the standard search parameter types. The behavior of special search parameters must be explicitly defined in their SearchParameter definition. Special parameters are useful for cases where the standard parameter types (string, token, reference, etc.) cannot adequately express the desired search functionality.

For example, the `_filter` parameter is a special parameter that allows complex boolean expressions for advanced filtering. The `Location.near` parameter is another special parameter that enables geographic proximity searches.

## Supported special FHIR SearchParameters

See also [Aidbox special search parameters](../aidbox-search.md#aidbox-special-search-parameters).

### \_id

Search by resource ID:

```
GET /fhir/Patient?_id=pt-1
```

### \_list

The `_list` parameter allows for the retrieval of resources that are referenced by a [List](https://www.hl7.org/fhir/list.html) resource.

```javascript
 GET /fhir/Patient?_list=42
```

This request returns all Patient resources referenced from the list found at `/List/42` in List.entry.item (which are not labeled as deleted by List.entry.item.deleted). While it is possible to retrieve the list and then iterate through he entries in the list, fetching each patient, using a list as a search criterion allows for additional search criteria to be specified. For instance:

```javascript
 GET /fhir/Patient?_list=42&gender=female
```

This request will return all female patients on the list. The server can return the list referred to in the search parameter as an included resource, but is not required to do so. In addition, a system can support searching by lists by their logical function. For example:

```java
GET /fhir/AllergyIntolerance?patient=42&_list=$current-allergies
```

This request will return all allergies in the patient 42's "Current Allergy List". The server returns all relevant AllergyIntolerance resources, and can also choose to return the list.

### \_lastUpdated

Search by the last modification time of the resource `meta.lastUpdated` (`ts` column in the database table)

```javascript
GET /fhir/Patient?_lastUpdated=2019-01-01
```

You can use operators `lt,le,gt,ge` like in other date search parameters.

### \_profile

Search by the [resource profile](https://www.hl7.org/fhir/profiling.html)

```yaml
GET /fhir/Patient?_profile=http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient
```

### \_text and \_content

[Full-text-search](https://en.wikipedia.org/wiki/Full-text_search) by resources can be achieved:&#x20;

* to search by narrative using \_**text**
* to search by retaining the resource content using \_**content**

```
GET /fhir/Patient?_text=John
```

```
GET /fhir/Patient?_content=New-York
```

Full-text search requests support grouping and logical operations:

```
GET /fhir/Patient?_content=(NOT bar OR baz) AND foo
```

If you want to search by the phrase, just quote it:

```
GET /fhir/Patient?_content="Mad Max"
```

### \_ilike

With **\_ilike** search parameter, you search for term inclusion as a substring in the text representation of FHIR resource. It can provide quick feedback to the user about matches without forcing to print the whole word (as with full text search). For example `jo` will find Johns and Jolie or `asp` will match Aspirin.

```
GET /fhir/Patient?_ilike=joh+smit,jes+park
```

With **\_ilike** parameter, your term is separated with a space, combined with `AND` and separated by comma `,` with `OR`. The example above is translated into a SQL query like this:

```sql
SELECT * FROM patient
WHERE
resource::text ilike '%joh%' AND ... ilike '%smit%'
OR
resource::text ilike '%jes%' AND ... '%park%'
```

**ILIKE** search can be efficiently indexed with the trigram PostgreSQL extension and GIN Index, responding to tens of milliseconds on millions of records.

```sql
CREATE INDEX patient_trgm_idx  on patient
USING gin (
  (id || ' ' || resource::text) gin_trgm_ops
);

VACUUM ANALYZE patient;
```

### \_elements

A client can request a specific set of elements to be returned as part of a resource in the search results using the `_elements` parameter:

```yaml
GET /fhir/Patient?_elements=birthDate,name.given,address.city
```

The `_elements` parameter consists of a comma-separated list of element paths. Only element paths that are listed should be returned. The list of elements does not apply to included resources.

If you want to exclude specific elements, you can prefix them with the `-` sign:

```yaml
GET /fhir/Patient?_elements=-text,-identifier
```

You can include or exclude nested elements using a dot-separated path to an element:

```yaml
GET /fhir/Patient?_elements=name.given,name.family
```

The \_elements parameter is not applied to included resources. If you want to filter included resource elements, prefix the element path with resourceType. For example:

```http
GET /fhir/Encounter?_include=patient&_elements=id,type,Patient.name
```

Result will contain id and type elements from Encounter and the Patient's name. The `-` prefix will exclude elements (for example `-Patient.identifier` will exclude the identifier from Patient resources).

### \_count

`_count` is used to limit the number of records on the page.

```yaml
GET /fhir/Patient?_count=10
```

Will return 10 records or fewer if no more records are available.

### \_page

Search results can contain many records, and for more convenient work, we can use pagination.

```http
GET /fhir/Patient?_count=10&_page=3
```

Will return 10 records from the third page.

You can see the next, previous, first, and last pages in the response:

```yaml
resourceType: Bundle
type: searchset
entry:
- resource:
    # ...
total: 206
link:
- {relation: first, url: '/Patient?_count=10&_page=1'}
- {relation: self, url: '/Patient?_count=10&_page=3'}
- {relation: next, url: '/Patient?_count=10&_page=4'}
- {relation: previous, url: '/Patient?_count=10&_page=2'}
- {relation: last, url: '/Patient?_count=10&_page=21'}
```

#### \_total

By default, for all search requests, Aidbox returns the total number in the result, which represents how many resources matched the criteria. But to do this, we run the second query for the count, which takes some additional time.

**Sometimes the count query is longer than your query**.

To get a response faster, you can change this behavior using the **\_total** parameter. The **\_total** parameter can have the following values:

* `none` - do not run count query (fastest)
* `estimate` - roughly estimate the number of results (fast, additional request is **EXPLAIN** request)
* `accurate`- run accurate count (could be slow, additional request is **COUNT(\*)** request)

Note: if you use `_total=none` you still get `total` when you don't use `_page` and the number of returned resources is less than `_count`.

### \_include

See:

{% content-ref url="include-and-revinclude.md" %}
[include-and-revinclude.md](include-and-revinclude.md)
{% endcontent-ref %}

### \_revinclude

See:

{% content-ref url="include-and-revinclude.md" %}
[include-and-revinclude.md](include-and-revinclude.md)
{% endcontent-ref %}

### \_filter

Aidbox offers partial support for the [FHIR \_filter API](https://www.hl7.org/fhir/search.html#filter). The most common use case to use `_filter` instead of the usual combination of search parameters is OR logic. In FHIR, it is impossible to use OR logic with 2 different search parameters without `_filter,`:

```
GET /fhir/Patient?_filter=name co 'smi' or birthdate gt '1996-06-06'
```

Supported \_filter operators:

| Operation   | String | Number | Date | Token | Reference | Quantity |
| ----------- | ------ | ------ | ---- | ----- | --------- | -------- |
| eq          | +      | +\*\*  | +    | +\*   | n/a       | +\*\*\*  |
| ne          | -      | +\*\*  | +    | -     | n/a       | +\*\*\*  |
| co          | +      | -      | -    | n/a   | n/a       | n/a      |
| sw          | +      | n/a    | n/a  | n/a   | n/a       | n/a      |
| ew          | +      | n/a    | n/a  | n/a   | n/a       | n/a      |
| gt/ge/lt/le | -      | +      | +    | n/a   | n/a       | +\*\*\*  |
| po          | n/a    | n/a    | -    | n/a   | n/a       | n/a      |
| pr\*\*\*\*  | +      | +      | +    | +     | +         | +        |
| ss          | n/a    | n/a    | n/a  | -     | n/a       | n/a      |
| sb          | n/a    | n/a    | n/a  | -     | n/a       | n/a      |
| in          | n/a    | n/a    | n/a  | -     | n/a       | n/a      |
| re          | n/a    | n/a    | n/a  | n/a   | -         | n/a      |

\* token search is case sensitive

\*\* number search doesn't support implicit precision

\*\*\* support only numbers, not system with code

\*\*\*\* available since version 2503

Additionally, Aidbox supports:

* forward-chained search parameters in the \_filter query
* dot expressions in \_filter query

Examples:

```
# returns patient with specific id
GET /fhir/Patient?_filter=id eq 'pt-2'

# returns patients with name that contain specific substring e.g. Smith
GET /fhir/Patient?_filter=name co 'smi'

# returns patients with address.city starting with provided string, e.g. London
GET /fhir/Patient?_filter=address-city sw 'Lon'

# returns all patients with birthdate >= (<=) provided date
GET /fhir/Patient?_filter=birthdate ge 1996-06-06
GET /fhir/Patient?_filter=birthdate le 1996-06-06

# logical expressions support
GET /fhir/Patient?_filter=(name co 'smi' or name co 'fed') or name co 'unex'

# forward chains
GET /fhir/Patient?_filter=(organization:Organization.name eq 'myorg')

# dot expressions
GET /fhir/Patient?_filter=.name.0.family eq 'Doe'
GET /fhir/Patient?_filter=.name isnull true
```

### \_has

See:

{% content-ref url="chaining.md" %}
[chaining.md](chaining.md)
{% endcontent-ref %}

### \_source

[\_source search parameter](https://www.hl7.org/fhir/search.html#_source) matches resources based on source information in the [Resource.meta.source](https://www.hl7.org/fhir/resource-definitions.html#Meta.source) element.

Examples:

```
GET /fhir/Patient?_source=http://example.com/Organization/123
```

```
GET /fhir/Patient?_source:below=http://example.com/
```

### \_security

The `_security` search parameter matches resources based on security labels in the [Resource.meta.security](https://www.hl7.org/fhir/resource-definitions.html#Meta.security) element.

Example:

```
GET /fhir/Observation?_security=http://terminology.hl7.org/CodeSystem/v3-Confidentiality|R
```

### \_sort

Supports all search parameter types.&#x20;

```
GET /fhir/Organization?_sort=name
```

Aidbox also supports sorting with [dot expressions](../aidbox-search.md#dot-expressions):

```
GET /fhir/Patient?_sort=.name.0.family
```

You can sort by multiple parameters:

```
GET /fhir/Organization?_sort=name,_id
```

#### Sorting direction

You can change the sorting direction by prefixing the parameter with `-` sign

```
GET /fhir/Organization?_sort=-name,-lastUpdated
```

### \_summary

The client can request the server to return only **summary** elements of the resources by using the parameter `_summary`

Request:

```yaml
GET /fhir/Patient?_summary=true
```

Response:

```
resourceType: Bundle
type: searchset
entry:
- resource: {id: Patient.active, isSummary: true, resourceType: Attribute}
  fullUrl: /Attribute/Patient.active
- resource: {id: Patient.address, isSummary: true, resourceType: Attribute}
  fullUrl: /Attribute/Patient.address
- resource: {id: Patient.animal, isSummary: true, resourceType: Attribute}
  fullUrl: /Attribute/Patient.animal
- resource: {id: Patient.animal.breed, isSummary: true, resourceType: Attribute}
  fullUrl: /Attribute/Patient.animal.breed
# .....
```

Values table:

| Value                                                       | Description                                                                                                                                                                                                                                                                                                            |
| ----------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [true](https://www.hl7.org/fhir/search.html#summary-true)   | Return a limited subset of elements from the resource. This subset SHOULD consist solely of all supported elements that are marked as "summary" in the base definition of the resource(s) (see [ElementDefinition.isSummary](https://www.hl7.org/fhir/elementdefinition-definitions.html#ElementDefinition.isSummary)) |
| [text](https://www.hl7.org/fhir/search.html#summary-text)   | Return only the "text" element, the 'id' element, the 'meta' element, and only top-level mandatory elements                                                                                                                                                                                                            |
| [data](https://www.hl7.org/fhir/search.html#summary-data)   | Remove the text element                                                                                                                                                                                                                                                                                                |
| [count](https://www.hl7.org/fhir/search.html#summary-count) | Search only: just return a count of the matching resources, without returning the actual matches                                                                                                                                                                                                                       |
| [false](https://www.hl7.org/fhir/search.html#summary-false) | Return all parts of the resource(s)                                                                                                                                                                                                                                                                                    |

The intent of the `_summary` parameter is to reduce the total processing load on the server, client, and resources between them, such as the network. It is most useful for resources that are large, particularly ones that include images or elements that may repeat many times.

The purpose of the summary form is to allow a client **to quickly retrieve a large set of resources** and let a user pick the appropriate one. The summary for an element is defined to allow a user to quickly sort and filter the resources, and typically omits important content on the basis that the entire resource will be retrieved when the user selects a resource.

#### _Limitations_

* You can't expect only a summary response as requested. There is a limited number of summary forms defined for resources to allow servers to store the summarized form(s) in advance.
* &#x20;`_include` and `_revinclude` cannot be mixed with `_summary=text`.

### Location.near

[Location search](https://www.hl7.org/fhir/location.html#search) is used to search locations by coordinates using the WGS84 datum. Geopositional search is supported in Aidbox only with Aidboxdb 15.3 and later. Search for locations near the specified geo-coded position. Supported units are _km_ and _\[mi\_us]_.

The request:

```
GET /fhir/Location?near=<latitude>|<longitude>|[distance]|[units]
```

Latitude and longitude are required.

If the units are omitted, then `kms` are assumed.

If the distance is also omitted, then Aidbox treats "near" as 3 km.

#### Examples

1. Get locations within 11.2 km of some geo-coded position:

```http
#                   latitude |longitude|distance|units
GET /fhir/Location?near=-83.674810|42.266500|11.20|km
```

Response:

```yaml
resourceType: Bundle
total: 1
entry:
  - resource:
      position:
        latitude: -83.69481
        longitude: 42.2565
      id: >- loc-1
      resourceType: Location
```

2. Search in miles:

```
GET /fhir/Location?near=-83.674810|42.266500|11.20|[mi_us]
```

3. Search without distance and units

```
# distance = 3, units = km
GET /fhir/Location?near=-83.674810|42.266500
```

### \_tag

The `_tag` search parameter allows searching for resources tagged with specific codes. Tags are metadata that can be attached to any resource and are useful for categorizing or marking resources for various purposes.

Example:

```
GET /fhir/Patient?tag=emergency
```

## Custom Search Parameter

See tutorials:
* [Custom SearchParameter](../../../tutorials/crud-search-tutorials/search-tutorials/custom-searchparameter-tutorial.md)
* [Create Custom Aidbox Search resource](../../../tutorials/crud-search-tutorials/search-tutorials/create-custom-aidbox-search-resource.md)

See also [Aidbox Search page](../aidbox-search.md):

* [Aidbox special search parameters](../aidbox-search.md#aidbox-special-search-parameters)
* [Search resource](../aidbox-search.md#search-resource)
* [dot-expressions](../aidbox-search.md#dot-expressions)
* [AidboxQuery](../aidbox-search.md#aidboxquery)
