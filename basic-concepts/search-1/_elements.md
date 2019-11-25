---
description: include or exclude specific resource attributes in search result
---

# \_elements

A client can request a specific set of elements to be returned as part of a resource in the search results using the `_elements` parameter:

```yaml
GET [base]/Patient?_elements=birthDate,name.given,address.city

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

The `_elements` parameter consists of a comma-separated list of  element paths such as. Only element paths that are listed are to be returned. The list of elements does not apply to included resources.

### Exclude

If you want to exclude specific elements you can prefix it with `-` sign: 

```javascript
GET /Patient?_elements=-text,-identifier
```

### Nested elements

You can select or exclude nested elements using dot separated path to element:

```javascript
GET /Patient?_elements=name.given,name.family
```

### Elements and \(ref\)includes

 \_elements parameter is not applied to included resources. If you want to filter included resources elements - prefix element path with resourceType. For example:

```yaml
GET /Encounter?_include=patient&_elements=id,type,Patient.name
```

Result will contain id and type elements from Encounter and  name from Patient. Prefix with `-` will  exclude elements \(for example `-Patient.identifier` - will exclude identifier from patients\)

