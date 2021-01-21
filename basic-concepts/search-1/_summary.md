---
description: Include only summary elements
---

# \_summary

The client can request the server to return only **summary** elements of the resources by using the parameter `_summary`

```yaml
GET /Patient?_summary=true
```

There is boolean **isSummary** attribute in [Attribute](../../advanced/entities-and-attributes.md) definition. Which elements will be returned for a specific resource as \_summary, can be inspected using the following request to the Attribute :

```yaml
GET /Attribute?entity=Patient&.isSummary=true&_elements=id,isSummary&_sort=_id

# resp

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
.....
```

