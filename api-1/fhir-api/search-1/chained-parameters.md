---
description: Search by associated resources
---

# Chained Parameters

For a more accurate search, we can filter by nested fields of related entities. Reference parameters may be "chained" through `.`&#x20;

Obtain all **encounters** with patients (**subject** — link to the patient) with the name Alex:

```javascript
GET /Encounter?subject:Patient.name=Alex
```

You can use several chained parameters by the base resource:

```javascript
GET /Encounter?part-of:Encounter._id=enc1&subject:Patient._id=patient1
```

"Reversed chaining" is a selection of resources based on the properties of resources that refer to them.

Obtain patient resources where the encounter has the id = enc1 and refer to the patient through the subject field:

```javascript
GET /Patient?_has:Encounter:subject:_id=enc1
```

###
