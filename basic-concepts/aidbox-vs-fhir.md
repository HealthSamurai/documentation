# Aidbox vs FHIR

There are some decisions in [FHIR RESTful API specification](https://www.hl7.org/fhir/http.html) that HealthSamurai team found not intuitive and logical. That's why [Aidbox](https://www.health-samurai.io/aidbox) provides two ways to access resources:

via `https://<your-box>.aidbox.app` and`https://<your-box>.aidbox.app/fhir` endpoints

They are mostly the same, but first one is opinionated by HealthSamurai engineers Aidbox version of FHIR API, the second one is FHIR compatible version.  
We assume that `[base]` is `https://<your-box>.aidbox.app`, but if you need full FHIR compatibility use `https://<your-box>.aidbox.app/fhir`.  


The two types of differences: related to data format and operation behavior are described below.

## Operations

### create

```http
POST [base]/[type]
```

FHIR API [ignores](https://www.hl7.org/fhir/http.html#create) `id` in `POST` requests, but Aidbox API respect `id` inside request body and creates resource with specific `id`. This decision was made because we didn't find any reasons to ignore it and to make Aidbox API be closer to sql `INSERT` query. As a result new response code `409 Conflict` appeared:\`

* **`201` Created** - resource successfully created
* **`400` Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** - resource with such id already exists
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

### update

```http
PUT [base]/[type]/[id]
```

Aidbox doesn't have atomic update yet. It also allows to omit `id` in resource body, ~~_but there is no important reason behind it_~~.

* **`200` OK** - resource successfully updated
* **`201` Created** - resource successfully created
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

### delete

```
DELETE [base]/[type]/[id]
```

Respond with `200 OK` on successful delete, but on deletion of already deleted resource respond with `204 No Content` \(conforming FHIR specification\). This feature added to make delete work in the same way as SQL `DELETE RETURNING *`.

To get `204 No Content` instead of `200 OK` use `_no-content=true` query parameter. 

* **`200` OK** - resource successfully delete
* **`204` No Content** - resource already deleted
* **`404` Not Found** - resource not found

### conditional create

```
POST [base]/[type]?[search parameters]
```

Instead of using `If-None-Exist` header, Aidbox uses query parameters as in ordinary `read` operation. This done to make all conditional operations to look the same \(use search query parameters\). 

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server returns the found resource and `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### conditional update

```
PUT [base]/[type]?[search parameters]
```

In contrast to FHIR, Aidbox conditional update allows to create a resource with specific `id`. `Conditional update` in case of one match ignores id coming in request body, that mean that resource `id` can't be changed by any `update` operation.

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### conditional delete

```text
DELETE [base]/[type]?[search parameters]
```

It's not clear how to perform an ordinary `delete` on no matches, that's why `404 Not Found` will be returned in this case.

* **No matches:** The respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough

## Query parameters

### \_no-content

`?_no-content=true` make any mutating operation omit response body and return `204 No Content`.

## Data format

Differences between FHIR and Aidbox resource structure.

Aidbox uses a slightly adjusted FHIR resource format to make queries simpler and faster. It adopted some ideas originated in [FHIR Fuel project](https://github.com/fhir-fuel/fhir-fuel.github.io/issues%E2%80%8B).

There are 2 major differences of resource structure between Aidbox and FHIR: polymorphic elements and references representation.

### Polymorphic elements <a id="polymorphic-elements"></a>

Some resource’s elements can have variable types, in the FHIR specification such elements have `[x]` postfix \(i.e. `Observation.value[x]`\). In YAML representation such elements of FHIR resources are encoded by substitution of postfix with specific title-cased type name:

```yaml
- resourceType: Observation
  component:
  - valueString: "string value"
  
- resourceType: Observation
  component:
  - valueQuantity:
      value: 42
      unit: mg/day
```

1. This approach to the representation forces an unnecessary constraint: polymorphic elements cannot repeat, i.e. they must have a maximum cardinality of 1. There is no absolute reason to force this besides format representation.
2. On the other side, most of object-oriented FHIR implementations of usually provide convenient accessors for polymorphic elements like `observation.getValue()`. But when you handle JSON representation without any wrapper, you have to iterate through the object keys to find exact key name holding the value.
3. JSON schema is quite popular way to specify the shape of JSON object. But it's impossible to constraint JSON object with it to have only one `value[x]` element or force `value[x]` element to be required.
4. Implementation of FHIR search for missing elements, like `Observation?value:missing=true` is tricky \(see 2\)

To mitigate those difficulties and limitations in Aidbox we represent polymorphic elements with nested object:

```yaml
# FHIR
valueString: "...."
# Aidbox
value:
  string: "...."

# FHIR
valueNumber: 42
# Aidbox
value:
  number: 42

```

### References <a id="references"></a>

References as URI strings are not very useful in most cases, and usually you want them to be splitted into discrete parts to operate with. Aidbox parses references on Create or Update operations and stores ID and Resource Type separately:

```yaml
# FHIR
subject:
  reference: "Patient/pt-1"
  
# Aidbox
subject:
  resourceType: "Patient"
  id: "pt-1"
  
## Contained resource reference
subject:
  id: "#pt-1"

## Remote reference
subject:
  uri: "http://otherserver/fhir/Patient/pt-1"

```

