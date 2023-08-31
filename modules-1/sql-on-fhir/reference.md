# Reference

## ViewDefinition structure



SQL on FHIR elements

* constant
* foreach
* where
* ...

## FHIRPath expressions

#### Exists(\[criteria])

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

* join
* ...
