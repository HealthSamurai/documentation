# Token search

Token search is used to search by exact match of codes, Codings, CodeableConcepts, Identifiers...

### Modifiers

#### :of-type

Aidbox (> 2203) support search with `of-type` modifier for identifiers.&#x20;

The search parameter has the format system|code|value, where the system and code refer to a `Identifier.type.coding.system` and `.code`, and match if any of the type codes match. All 3 parts must be present.

```yaml
PUT /Patient
content-type: text/yaml

resourceType: Patient
id: pt1
identifier:
- value: 'value'
  type: {coding: [{code: 'code', system: 'system'}]}
```

```yaml
GET /Patient?identifier:of-type=system|code|value
```

