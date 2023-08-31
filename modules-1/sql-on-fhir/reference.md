# Reference

## ViewDefinition structure

### name

Name of view definition (computer and database friendly) sql-name: Name is limited to letters, numbers, or underscores and cannot start with an underscore — i.e. with a regular expression of: `^[^][A-Za-z][A-Za-z0-9]+$`

### сonstants (optional)

Contact details for the publisher. Defined as an array of elemtents that contain the following elements:

* **name** — Name of constant (referred to in FHIRPath as `%[name]`). Name is limited to letters, numbers, or underscores and cannot start with an underscore — i.e. with a regular expression of: `^[^][A-Za-z][A-Za-z0-9]+$`
* **value** — Value of constant.

### select

Defines the content of a column within the view. Defined as an array of elemtents that contain one of the following elements:

* **expr** — Creates a scope for selection relative to a parent FHIRPath expression.&#x20;
* **foreach** — Same as `expr`, but unnests a new row for each item in the collection.
* **forEachNotNull** — Same as `forEach`, but produces a single row with a `null` value if the collection is empty.

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

### Detailed explaination

#### exists(\[criteria])

Returns `true` if the collection has any elements, and `false` otherwise. Also this function takes one optional criteria which will be applied to the collection prior to the determination of the exists. If _any_ element meets the criteria then `true` will be returned.

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

* extension

#### getId(\[resourceType])

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

#### **join**

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

#### where

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
