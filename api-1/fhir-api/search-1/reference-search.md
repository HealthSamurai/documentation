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
