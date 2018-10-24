---
description: Difference between Aidbox and FHIR
---

# Aidbox vs FHIR

There are some decisions in [FHIR RESTful API specification](https://www.hl7.org/fhir/http.html) that HealthSamurai team found not intuitive and logical. That's why Aidbox provides to ways to access resources:

via `https://<your-box>.aidbox.app` and`https://<your-box>.aidbox.app/fhir` endpoints

They are mostly the same, but first one is opinionated by HealthSamurai engineers Aidbox version of FHIR API, the second one is FHIR compatible version of API.  
We assume that `[base]` is `https://<your-box>.aidbox.app`, but if you need full FHIR compatibility use `/fhir`.  


The two types of differences: related to data format and operation behavior are described below.

## Operations

### create

```http
POST [base]/[type] {?_format=[mime-type]}
```

FHIR API [ignores](https://www.hl7.org/fhir/http.html#create) `id` in `POST` requests, but Aidbox API respect `id` inside request body and creates resource with specific `id`. This decision was made because we didn't find any reasons to ignore it and to make Aidbox API be closer to sql `INSERT` query. As a result new response code appeared:

* **`201` Created** - resource successfully created
* **`400` Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** - resource with such id already exists
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

### update

```http
PUT [base]/[type]/[id]{?_format=[mime-type]}
```

Aidbox doesn't have atomic update yet. It also allows to omit `id` in resource body, but there is no important reason behind it.

* **`200` OK** - resource successfully updated
* **`201` Created** - resource successfully created
* **`400` Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`404` Not Found** - resource not found
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

### delete

```
DELETE [base]/[type]/[id]
```

Respond with `200 OK` on successful delete, but on deletion of already deleted resource respond with `404 Not found` instead of `200 OK` \(proposed by FHIR specification\). This feature added to make delete work in the same way as conditional delete.

To get `204 No Content` instead of `200 OK` use `_no-content=true` query parameter. 

* **`200` OK** - resource successfully delete
* **`404` Not Found** - resource not found
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

### conditional create

```
POST [base]/[type]?[search parameters]
```

Instead of using `If-None-Exist` header, Aidbox uses query parameters as in ordinary `read` operation. This done to make all conditional operations to look the same \(use search query parameters\). 

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server ignore the post and returns `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### conditional update

```
PUT [base]/[type]?[search parameters]
```

In contrast to FHIR, Aidbox conditional update allows to create a resource with specific `id`. `Conditional update` also ignores id coming in request body, that mean that resource `id` can't be changed by any `update` operation.

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

### conditional delete

```text
DELETE [base]/[type]/?[search parameters]
```

It's not clear how to perform an ordinary `delete` on no matches, that's why `404 Not Found` will be returned in this case.

* **No matches:** The respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough

## Query parameters

### \_no-content

`?_no-content=true` make any mutating operation omit response body and return `204 No Content`.

## Data format

[https://fhirbase.aidbox.app/json-format](https://fhirbase.aidbox.app/json-format)

