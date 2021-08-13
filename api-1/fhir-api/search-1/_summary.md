---
description: This tutorial designed for summary parameter demonstration purposes
---

# Summary parameter

### How to

The client can request the server to return only **summary** elements of the resources by using the parameter `_summary`

```yaml
GET /Patient?_summary=true
```

There is boolean **isSummary** attribute in [Attribute](../../../core-modules/entities-and-attributes.md) definition. Which exact elements will be returned for a specific resource as \_summary can be inspected using the following request to the Attribute :

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

### Values table

![Summary parameter values table](../../../.gitbook/assets/image%20%2855%29.png)

### Summary purpose

The intent of the `_summary` parameter is to reduce the total processing load on server, client, and resources between them such as the network. It is most useful for resources that are large, particularly ones that include images or elements that may repeat many times.

The purpose of the summary form is to allow a client **to quickly retrieve a large set of resources**, and let a user pick the appropriate one. The summary for an element is defined to allow a user to quickly sort and filter the resources, and typically omit important content on the basis that the entire resource will be retrieved when the user selects a resource.

### Limitations

You can't expect only a summary response as requested. There is limited number of summary forms defined for resources in order to allow servers to store the summarized form\(s\) in advance. Servers **SHOULD** mark the resources with the tag [`SUBSETTED`](https://www.hl7.org/fhir/v3/SecurityIntegrityObservationValue/cs.html#SUBSETTED) to ensure that the incomplete resource is not accidentally used to overwrite a complete resource.

{% hint style="info" %}
`_include` and `_revinclude`cannot be mixed with `_summary=text`.
{% endhint %}

> **Implementation Note:** There is some question about the inclusion of extensions in the summary. Additional rules may be made around this in the future.

