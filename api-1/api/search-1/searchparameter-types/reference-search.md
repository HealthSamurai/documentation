# Reference search

Reference describes the relationship between resources. Following options are available for filtering by reference:

```javascript
[parameter]=[id]
[parameter]=[type]/[id]
```

For example, let's find all encounters related to a specified patient:

```
GET /fhir/Encounter?subject=patientid
```

or

```
GET /fhir/Encounter?subject=Patient/patientid
```

## Supported modifiers

* `identifier` - Tests whether the `Reference.identifier` in a resource (rather than the `Reference.reference`) matches the supplied parameter value

Example:

```
// search for observations with a subject containing 
// the identifier 'http://example.org/fhir/mrn|12345'
GET /fhir/Observation?subject:identifier=http://example.org/fhir/mrn|12345
```

* `not` - Reverses the code matching: returns all resources that do not have a matching item.

Example:&#x20;

```
//Search for any patient with a gender that does not have the code "male"
//Note that for :not, the search does not return any resources that have a gen
GET /fhir/Patient?gender:not=male
```
