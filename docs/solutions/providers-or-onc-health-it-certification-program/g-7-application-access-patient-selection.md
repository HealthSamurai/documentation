# (g)(7): Application Access - Patient Selection

#### Patient Search API

Aidbox provides FHIR R4 Patient search with US Core STU 6.1 required search parameters:

**US Core Required Search Parameters:**

* `_id` - Logical id of the resource
* `identifier` - Patient identifier (MRN, SSN, etc.)
* `name` - Any part of patient name
* `family` - Family name (last name)
* `given` - Given name (first name)
* `birthdate` - Date of birth
* `death-date` - Date of death
* `gender` - Administrative gender

**FHIR Search Capabilities:**

Aidbox supports comprehensive [FHIR Search API capabilities](https://www.health-samurai.io/docs/aidbox/api/rest-api/fhir-search):

* **Multiple criteria:** Combine parameters (`?family=Smith&birthdate=1980-05-15`)
* **OR logic:** Use comma-separated values (`?name=John,Jane`)
* **Modifiers:** Refine searches (`:exact`, `:contains`, `:missing`)
* **Sorting & paging:** Control result ordering and pagination (`?_sort=family&_page=1`)
* **Field selection:** Return specific fields only (`?_elements=id,name,birthDate`)

**Example Request:**

```http
GET /fhir/Patient?family=Smith&given=John&birthdate=1980-05-15
Authorization: Bearer {access_token}
```

**Response:** Returns FHIR Bundle with matching Patient resources. The `Patient.id` field is the identifier for subsequent data requests.

```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 1,
  "entry": [{
    "resource": {
      "resourceType": "Patient",
      "id": "patient-123",
      "name": [{"family": "Smith", "given": ["John"]}],
      "birthDate": "1980-05-15"
    }
  }]
}
```
