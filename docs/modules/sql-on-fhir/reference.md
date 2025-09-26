# Reference

## ViewDefinition structure

### name

Name of view definition (computer and database friendly) sql-name: Name is limited to letters, numbers, or underscores and cannot start with an underscore — i.e. with a regular expression of: `^[^][A-Za-z][A-Za-z0-9]+$`

### сonstant (optional)

Contact details for the publisher. Defined as an array of elemtents that contain the following elements:

* **name** — Name of constant (referred to in FHIRPath as `%[name]`). Name is limited to letters, numbers, or underscores and cannot start with an underscore — i.e. with a regular expression of: `^[^][A-Za-z][A-Za-z0-9]+$`
* **value** — Value of constant.

### select

A collection of columns and nested selects to include in the view. Defined as an array of elements with a following structure:

* **column** — A column to be produced in the resulting table. Contains the following elements:
  * **path** — FHIRPath expression that creates a column and defines its content. Supports a subset of FHIRPath described in FHIRPath expressions.
  * **name** — Column name produced in the output.
* **select** (optional) — Nested select relative to a parent expression.
* **forEach** (optional) — A FHIRPath expression to retrieve the parent element(s) used in the containing select. The default is effectively `$this`. Can't be set when `forEachOrNull` is set.
* **forEachOrNull** (optional) — Same as forEach, but will produce a row with null values if the collection is empty. Can't be set when `forEach` is set.

## FHIRPath expressions

SQL on FHIR engine supports a subset of FHIRPath funcitons:

* **where** — returns a collection containing only those elements in the input collection for which the stated `criteria` expression evaluates to `true`. Elements for which the expression evaluates to `false` or empty (`{ }`) are not included in the result.
* **exists** — returns `true` if the collection has any elements, and `false` otherwise. This is the opposite of `empty()`, and as such is a shorthand for `empty().not()`. If the input collection is empty (`{ }`), the result is `false`.
* **empty** — returns `true` if the input collection is empty (`{ }`) and `false` otherwise.
* **extension** — will filter the input collection for items named "extension" with the given url. This is a syntactical shortcut for `.extension.where(url = string)`, but is simpler to write. Will return an empty collection if the input collection is empty or the url is empty.
* **join** — the join function takes a collection of strings and _joins_ them into a single string, optionally using the given separator.
* **ofType** — returns a collection that contains all items in the input collection that are of the given type or a subclass thereof. If the input collection is empty (`{ }`), the result is empty.
* **first** — returns a collection containing only the first item in the input collection. This function is equivalent to `item[0]`, so it will return an empty collection if the input collection has no items.
* Boolean operators: **and**, **or**, **not**.
* Math operators: **addition (+)**, **subtraction (-)**, **multiplication (\*)**, **division (/)**.
* Comparison operators: **equals (=)**, **not equals (!=)**, **greater than (>)**, **less or equal (<=)**.
* **getResourceKey:** resource id from the id column
* **getReferenceKey**: get id of the reference. The desired type can be specified if the reference may contain different types (for example, Observation.subject).

## Detailed explanation

### exists(\[criteria: expression]) : Boolean

Returns `true` if the collection has any elements, and `false` otherwise. Also, this function takes one optional criteria which will be applied to the collection prior to the determination of the exists. If _any_ element meets the criteria then `true` will be returned.

For example we have two patients:

First patient:

```
{
  name: [{
    given: [ 'Lael' ]
  }]
}
```

Second patient:

```
{
  name: [{
    given: [ 'Anastasia', 'Nastya' ]
  }]
}
```

The following FHIRPath expression will show patient names that contain given name 'Anastasia':

```
Patient.name.given.exists($this = 'Anastasia')
```

The result of expression will be:

| value |
| ----- |
| false |
| true  |

### empty() : Boolean

Returns `true` if the input colleciton is empty and false otherwise.

### extension(url: string) : collection

Will filter the input collection for items named "extension" with the given url. This is a syntactical shortcut for `.extension.where(url = string)`, but is simpler to write. Will return an empty collection if the input collection is empty or the url is empty.

### getId(\[resourceType])

Returns the field `id` of input element. One optional string argument may be provided to get IDs of resources whose type is equal to the argument&#x20;

For example we have two observations:

First observation:

```
{
  ...
  subject: {
    id: pt1,
    resourceType: Patient
  }
}
```

Second observation:

```
{
  ...
  subject: {
    id: gr1,
    resourceType: Group
  }
}
```

The following FHIRPath expression will show ID of subject object if resourceType equals to 'Patient':

```
Observation.subject.getId('Patient')
```

The result of expression will be:

| ID     |
| ------ |
| pt1    |
| `NULL` |

### **join(\[separator: String]) : String**

The join function takes a collection of strings and _joins_ them into a single string, optionally using the given separator.

If the input is empty, the result is empty.

If no separator is specified, the strings are directly concatenated.

First patient:

```
{
  name: [{
    given: [ 'Lael' ]
  }]
}
```

Second patient:

```
{
  name: [{
    given: [ 'Anastasia', 'Nastya' ]
  }]
}
```

The following expression will concatenate elements in one string separated with provided string argument:

```
Patient.name.given.join(';')
```

The result will be:

| Value            |
| ---------------- |
| Lael             |
| Anastasia;Nastya |

### ofType(type: type specifier) : collection

Returns a collection that contains all items in the input collection that are of the given type or a subclass thereof. If the input collection is empty, the result is empty. The `type` argument is an identifier that must resolve to the name of a type in a model.

### first() : collection

Returns a collection containing only the first item in the input collection. This function is equivalent to `item[0]`, so it will return an empty collection if the input collection has no items.

### where(criteria: expression) : collection

Returns a collection containing only those elements in the input collection for which the stated `criteria` expression evaluates to `true`. Elements for which the expression evaluates to `false` or empty are not included in the result.

If the input collection is empty, the result is empty.

First patient:

```
{
  name: [{
    given: [ 'Lael' ],
    family: 'Gitya'
  }]
}
```

Second patient:

```
{
  name: [{
    given: [ 'Anastasia', 'Nastya' ],
    family: 'Smith'
  }]
}
```

The following expression will filter patients with names that contain 'Nastya', and their family name will be returned:

```
Patient.name.where(given.exists($this = 'Nastya')).family
```

The result will be:

| Value  |
| ------ |
| `NULL` |
| Smith  |

### getResourceKey() : KeyType

Query `id` column of the resource.&#x20;

### getReferenceKey(\[resource: type specifier]) : KeyType

This is invoked on Reference elements and returns an opaque value that represents the database key of the row being referenced. The value returned must be equal to the getResourceKey value returned on the resource itself.

Users may pass an optional resource type (e.g., `Patient` or `Observation` ) to indicate the expected type that the reference should point to. The getReferenceKey function will return an empty collection (effectively null since FHIRPath always returns collections) if the reference is not of the expected type. For example, `Observation.subject.getReferenceKey(Patient)` would return a row key if the subject is a Patient, or the empty collection ( i.e., `{}`) if it is not.

## Aidbox-specific functions

Aidbox stores some data in separate columns and gives access to it by Aidbox-specific functions.

For example, it is impossible to get [created date time](../../reference/all-settings.md#fhir.validation.createdat-url) value using a `meta` field in fhirpath, because it is stored in `cts` column and appended to the response at runtime.&#x20;

See also [Aidbox database schema](../../database/database-schema.md).

### getAidboxTs(): dateTime

Query `ts` column of the resource.&#x20;

### getAidboxCts(): dateTime

Query `cts` column of the resource.&#x20;

### getAidboxStatus(): string

Query `status` column of the resource.&#x20;

### getAidboxTxid(): integer

Query `txid` column of the resource.&#x20;

