---
description: Differences between FHIR JSON structure and Fhirbase JSON structure.
---

# JSON Format

Fhirbase uses a slightly adjusted FHIR JSON format to make queries simpler and faster. It adopted some ideas originated in [FHIR Fuel project](https://github.com/fhir-fuel/fhir-fuel.github.io/issues%E2%80%8B).

There are 2 major differences between Fhirbase JSON and FHIR JSON: polymorphic elements and references representation.

### Polymorphic elements <a href="#polymorphic-elements" id="polymorphic-elements"></a>

Some resource’s elements can have variable types, in the FHIR specification such elements have `[x]` postfix (i.e. `Observation.value[x]`). In JSON representation such elements are encoded by substitution of postfix with specific title-cased type name:

```
{ 
  "resourceType": "Observation",
  "component": [
    "valueString": "string value"
  ]
}

{
  "resourceType": "Observation", 
  "component": [
     "valueQuantity": {
       "value": 42,
       "unit": "mg/day"
     }
  ]
}
```

1. This approach to the representation forces an unnecessary constraint: polymorphic elements cannot repeat, i.e. they must have a maximum cardinality of 1. There is no absolute reason to force this besides format representation.
2. On the other side, most of object-oriented FHIR implementations of usually provide convenient accessors for polymorphic elements like `observation.getValue()`. But when you handle JSON representation without any wrapper, you have to iterate through the object keys to find exact key name holding the value.
3. JSON schema is quite popular way to specify the shape of JSON object. But it's impossible to constraint JSON object with it to have only one `value[x]` element or force `value[x]` element to be required.
4. Implementation of FHIR search for missing elements, like `Observation?value:missing=true` is tricky (see 2)

To mitigate those difficulties and limitations in Fhirbase we represent polymorphic elements with nested object:

```
{ "valueString": "...." } => { "value": { "string": "..." }}
{ "valueNumber": 42 }     => { "value": { "number": 42 }}
```

### References <a href="#references" id="references"></a>

References as URI strings are not very useful in most cases, and usually you want them to be splitted into discrete parts to operate with. Fhirbase parses references on Create or Update operations and stores ID and Resource Type separately:

```
// Local reference
{ "subject": { "resourceType": "Patient", "id": "pt-1", "display": "John Doe" }}

// Contained resource reference
{ "subject": { "id": "#pt-1" }}

// Remote reference
​{ "subject": { "uri": "http://otherserver/fhir/Patient/pt-1" }}
```
