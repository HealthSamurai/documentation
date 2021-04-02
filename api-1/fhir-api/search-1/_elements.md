---
description: Include or exclude specific resource attributes in the search result
---

# \_elements

A client can request a specific set of elements to be returned as part of a resource in the search results using the `_elements` parameter:

```yaml
GET /Patient?_elements=birthDate,name.given,address.city

# resp

resourceType: Bundle
type: searchset
entry:
- resource:
    birthDate: '1991-11-08'
    name: [{ given: [Marat] }]
    address: [{city: 'Tokio'}]
    resourceType: Patient
- resource:
    name: [{given: [Abram]}]
    resourceType: Patient
- resource:
    birthDate: '1965-03-29'
    name: [{ given: [John] }]
    address: [{city: 'Los Angeles'}]
    resourceType: Patient
```

The `_elements` parameter consists of a comma-separated list of element paths. Only element paths that are listed should be returned. The list of elements does not apply to included resources.

### Exclude

If you want to exclude specific elements, you can prefix them with the `-` sign: 

```javascript
GET /Patient?_elements=-text,-identifier
```

### Nested Elements

You can include or exclude nested elements using a dot separated path to an element:

```javascript
GET /Patient?_elements=name.given,name.family
```

### Elements and \(rev\)includes

 The \_elements parameter is not applied to included resources. If you want to filter included resources elements, prefix the element path with resourceType. For example:

```yaml
GET /Encounter?_include=patient&_elements=id,type,Patient.name
```

Result will contain id and type elements from Encounter and name from Patient. The `-` prefix will exclude elements \(for example `-Patient.identifier` will exclude the identifier from Patient resources\).

