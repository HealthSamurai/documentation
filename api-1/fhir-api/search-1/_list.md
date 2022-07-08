---
description: Search resources included into the specific List
---

# \_list

The `_list` parameter allows for the retrieval of resources that are referenced by a [List](https://www.hl7.org/fhir/list.html) resource.

```javascript
 GET /Patient?_list=42
```

This request returns all Patient resources that are referenced from the list found at `/List/42` in List.entry.item \(which are not labeled as deleted by List.entry.item.deleted\). While it is possible to retrieve the list, and then iterate the entries in the list fetching each patient, using a list as a search criterion allows for additional search criteria to be specified. For instance:

```javascript
 GET /Patient?_list=42&gender=female
```

This request will return all female patients on the list. The server can return the list referred to in the search parameter as an included resource but is not required to do so. In addition, a system can support searching by lists by their logical function. For example:

```java
GET /AllergyIntolerance?patient=42&_list=$current-allergies
```

This request will return all allergies in the patient 42's "Current Allergy List". The server returns all relevant AllergyIntolerance resources, and can also choose to return the list. 

