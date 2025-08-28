# FHIR search

Aidbox supports [FHIR Search API](https://www.hl7.org/fhir/search.html).\
The FHIR Search API is the primary mechanism for finding FHIR resources by conditions.\
A base search request is composed of a list of pairs `<parameter>=<value>`:

```
GET /fhir/[resourceType]?param1=value2&param2=value2&...
```

For example, to search for a Patient resource with the name "John" and a birthdate of "1900-01-01", the request would look like this:

```
GET /fhir/Patient?name=John&birthdate=1900-01-01
```

## Search capabilities

FHIR Search API supports various search features to help retrieve exactly the data you need. Here are some of the most commonly used capabilities.

| Search capability         | Example                                                                 | Example Description                                                                                                                                                        |
| ------------------------- | ----------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Field Filtering           | `GET /fhir/Patient?name=John`                                           | Search for patients with name starting with "John"                                                                                                                         |
| Multiple Criteria         | `GET /fhir/Patient?name=John&gender=male`                               | Search for male patients with name starting with "John" and "male" gender                                                                                                 |
| OR Logic                  | `GET /fhir/Patient?name=John,Jane`                                      | Search for patients with name starting with either "John" OR "Jane"                                                                                                        |
| Sorting & Paging          | `GET /fhir/Patient?_sort=name&_page=2`                                  | Sort patients by name and get the second page of results                                                                                                                   |
| Include related resources | `GET /fhir/Patient?_include=Patient:organization`                       | Get patients and include their referenced organization resources                                                                                                           |
| Reverse include           | `GET /fhir/Organization?_revinclude=Patient:organization`               | Get organizations and include all patients that reference them                                                                                                             |
| Field selection           | `GET /fhir/Patient?_elements=name,birthDate`                            | Return only name and birthDate fields for matching patients                                                                                                                |
| Chaining                  | `GET /fhir/Patient?organization.name=Mayo`                              | Search for patients in organizations with name containing "Mayo"                                                                                                           |
| Reverse Chaining          | `GET /fhir/Organization?_has:Patient:organization:name=John`            | Search for organizations that have patients with name containing "John"                                                                                                    |
| Modifiers                 | `GET /fhir/Patient?name:exact=John`                                     | Search for patients with name exactly matching "John"                                                                                                                      |
| Advanced filtering        | `GET /fhir/Patient?_filter=name eq 'John' or birthdate eq '1990-01-01'` | Search for patients with name equal to "John" or birthdate equal to "1990-01-01". There's no other way to express this multiple search parameters OR logic in FHIR Search. |

## Search results

Search results are returned as a [FHIR Bundle resource](https://www.hl7.org/fhir/bundle.html) of type "searchset". The Bundle contains:

* A total count of matching resources
* The matched resources as entries
* Links for pagination (first, previous, next, last pages)
* Additional included/revincluded resources (if requested)

Example response:

```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 100,
  "entry": [
    {
      "resource": {
        "resourceType": "Patient",
        "id": "123",
        "gender": "male"
      }
    }
  ],
  "link": [
    {
      "relation": "self",
      "url": "https://localhost:8080/fhir/Patient?_page=1"
    },
    {
      "relation": "first",
      "url": "https://localhost:8080/fhir/Patient?_page=1" 
    },
    {
      "relation": "next",
      "url": "https://localhost:8080/fhir/Patient?_page=2"
    }
  ]
}
```

Note: using the `X-Original-Uri` header allows for complete overwrite of the content of the URL parameter. Aidbox will automatically add a page param to your link, or replace it if it exists. `x-original-uri: https://example.com/fhir/Patient?page=4` will produce:

```yaml
...
link:
  - relation: first
    url: https://example.com/fhir/Patient?page=1
  - relation: self
    url: https://example.com/fhir/Patient?page=4
...
```

See also:

* [\_count](searchparameter.md#count)
* [ \_page](searchparameter.md#page)
* [\_total](searchparameter.md#total)
* [\_sort](searchparameter.md#sort)

## SearchParameter

A SearchParameter is a FHIR resource that defines how to search for data within other FHIR resources. \
SearchParameter can be:

* Resource-specific parameters (for example, `Patient.name`)
* Common underscored parameters across all resources (`_id`, `_sort`, `_count`, `_page`, `_elements`, etc.)

See also:

{% content-ref url="searchparameter.md" %}
[searchparameter.md](searchparameter.md)
{% endcontent-ref %}

## SearchParameter types

Each SearchParameter has a defined type, which determines how it behaves and what kind of values it accepts.

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

It is important to understand the type of SearchParameter because there are differences in how they are processed. For example, different modifiers are supported, some types support prefixes, some don't, etc.

See also [SearchParameter Types](searchparameter.md#search-parameter-types).

## Modifiers

Modifiers change the behavior of a search parameter to support more specific queries.\
For example, searching for patients with the name exactly "Smith", rather than the default partial matching:

```
GET /fhir/Patient?name:exact=Smith
```

List of supported modifiers:

| Modifier           | Parameter Types       | Description                                                                                                                                          | Example                                               |
| ------------------ | --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------- |
| :missing           | all                   | Tests whether the value in a resource is present (when the supplied parameter value is true) or absent (when the supplied parameter value is false)  | `gender:missing=true`                                 |
| :text              | all                   | Tests whether the textual value in a resource matches the supplied parameter value using basic string matching (begins with or is, case-insensitive) | `email:text=gmail.com`                                |
| :exact             | string                | Exact string match                                                                                                                                   | `name:exact=Alex`                                     |
| :contains          | string                | Search for string containing value                                                                                                                   | `name:contains=lex`                                   |
| :starts            | string                | Search for string starting with value                                                                                                                | `name:starts=Ale`                                     |
| :ends              | string                | Search for string ending with value                                                                                                                  | `name:ends=lex`                                       |
| :in                | token                 | Search within a ValueSet                                                                                                                             | `code:in=/ValueSet/cardiac-conditions`                |
| :not               | token                 | Negates the search value                                                                                                                             | `gender:not=male`                                     |
| :of-type           | token                 | Search for resource identifier                                                                                                                       | `identifier:of-type=system`                           |
| :i                 | token                 | Case-insensitive search                                                                                                                              | `email:i=foo@bar.baz`                                 |
| :below             | uri                   | Tests whether the value in a resource is or is subsumed by the supplied parameter value (is-a, or hierarchical relationships)                        | `url:below=http://acme.org/fhir/`                     |
| :not               | uri, reference, token | Negates the search value                                                                                                                             | `url:not=http://acme.org/fhir/`                       |
| :identifier        | reference             | Search by identifier of referenced resource                                                                                                          | `subject:identifier=urn:oid:1.2.3.4`                  |
| :btw               | date                  | Search for dates between two values. Defined by Aidbox, not FHIR.                                                                                    | `birthdate:btw=1980,1981`                             |
| :iterate, :recurse | -                     | See [including referenced resources](#including-referenced-resources)                                                                              | `_include:iterate=Observation:has-member:Observation` |

## Including referenced resources

When searching for resources, you can include referenced resources in the search results using the `_include` parameter. This reduces the number of API calls needed to fetch related data.

For example, to search for patients and include their referenced practitioners:

```
GET /fhir/Patient?_include=Patient:practitioner
```

### Reverse include

The `_revinclude` parameter does the opposite of `_include`. It returns resources that reference the ones you're querying.

For example, to search for practitioners and include all patients who reference them:

```
GET /fhir/Practitioner?_revinclude=Patient:practitioner
```

See also:

{% content-ref url="include-and-revinclude.md" %}
[include-and-revinclude.md](include-and-revinclude.md)
{% endcontent-ref %}

## Chaining

Chaining allows you to search across references between resources. It’s useful when you need to filter by attributes of related resources.

There are two types of chaining:

* Forward chaining: `GET /fhir/Patient?general-practitioner:Practitioner.name=Oz` (get patients who have a general practitioner with the name "Oz")
* Reverse chaining: `GET /fhir/Practitioner?_has:Patient:general-practitioner.gender=male` (get practitioners that are referenced by patients with gender "male")

See also:

{% content-ref url="chaining.md" %}
[chaining.md](chaining.md)
{% endcontent-ref %}

## Other ways to search

Sometimes FHIR Search API is not enough. For example, there's no way to search using a case-insensitive match without starts-with **string** type logic (combining token and string types), which is useful to search, say, for domains of emails.&#x20;

Aidbox has custom APIs to search for resources, which can be used instead of FHIR Search API in corner cases:

* [Search resource](../aidbox-search.md#search-resource) - use SQL to define `where` and `order-by` parts of SQL statements. Combines with other SearchParameters.
* [AidboxQuery](../aidbox-search.md#aidboxquery) - use DSL to define search criteria. Does not combine with other SearchParameters.
* [Dot expressions ](../aidbox-search.md#dot-expressions)- an easy way to search without using SearchParameters at all.

It is also possible to use GraphQL to search for resources.

See also:

{% content-ref url="../aidbox-search.md" %}
[aidbox-search.md](../aidbox-search.md)
{% endcontent-ref %}

{% content-ref url="../../graphql-api.md" %}
[graphql-api.md](../../graphql-api.md)
{% endcontent-ref %}
