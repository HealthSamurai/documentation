# \_filter

Aidbox offers the partial support of FHIR [\_filter](https://www.hl7.org/fhir/search_filter.html) API. However, we tend to use other search capabilities like AidboxQuery, SearchQuery, or Search resource for complex queries. They offer better expressiveness with SQL and better performance.

#### Supported operators

```text
# returns patient with specific id
GET /fhir/Patient?_filter=id eq pt-2

# returns patients with name that contain specific substring e.g. Smith
GET /fhir/Patient?_filter=name co 'smi'

# returns patients with address.city starting with provided string, e.g. London
GET /fhir/Patient?_filter=address-city sw 'Lon'

# returns all patients with birthdate >= (<=) provided date
GET /fhir/Patient?_filter=birthdate ge 1996-06-06
GET /fhir/Patient?_filter=birthdate le 1996-06-06
```

#### Logical expressions support

```text
# you can do composition of logical expressions with parentheses
GET /fhir/Patient?_filter=(name co 'smi' or name co 'fed') or name co 'unex'
```

If your application requires not supported \_filter functionality, please reach out to us via email, community chat, or private support chat.



