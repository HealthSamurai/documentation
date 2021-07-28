# Search Parameters

A `SearchParameter` resource specifies a search parameter that may be used on the RESTful API to search or filter on a resource. The SearchParameter resource declares:

* how to refer to the search parameter from a client
* how the search parameter is to be understood by the server
* where in the source resource the parameter matches

**`Note:`** `Search Parameter name is not equal to element name.`

You can see in [FHIR](https://www.hl7.org/fhir/searchparameter.html) specification Search Parameters and their according[ attributes](https://github.com/Aidbox/documentation/tree/560cedaf13f66f43be9f122cb8c4e2af0dcc066c/api-1/api/www.hl7.org/fhir/encounter.html#search). You can look up attached to see where search parameters can be found in the Aidbox interface Aidbox also can return search parameters for specific resouce by REST API. Here's an example for Encounter: GET /SearchParameter?resource=Encounter

![](../../.gitbook/assets/image%20%2853%29.png)

To search by Encounter.serviceProvider as it's stated in FHIR Encounter specification you should use service-provider search parameter.

![Search Parameters in Aidbox UI](../../.gitbook/assets/image%20%2846%29%20%281%29.png)



## Using

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
| .**type** | [keyword](http://localhost:8765/static/console.html#/entities/Attribute?entity=keyword) | Type of search parameter \(see [Types]()\) |
| .**expression** | [expression](http://localhost:8765/static/console.html#/entities/Attribute?entity=SearchParameterExpression) | expression for elements to search \(see [Expression]()\) |

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
{% endtabs %}

## Optimization of Search Parameters

### Getting search SQL query

If you perform a search query at the root route, you will get a SQL query in the response in the attribute `query-sql`:

![query-sql in the response](../../.gitbook/assets/image%20%2846%29.png)

{% hint style="info" %}
FHIR API does not have `query-sql`attribute, if you search with `/fhir/`prefix you won't see SQL query in the response
{% endhint %}

### Ways to optimize

* You can try to speed up your search query by creating an index. [Here's postgres documentation on indexes](https://www.postgresql.org/docs/13/indexes.html). 
* You can use [Search](../fhir-api/search-1/) resource to define your own SQL for some search parameter
* You can use entirely custom SQL query with [AidboxQuery](../fhir-api/search-1/custom-search.md) resource

