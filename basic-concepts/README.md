# Overview

Aidbox is a backend for Health Care applications,  which provides you with 80% of typical needs:

* FHIR Informational Model
* Transactional PostgreSQL Storage \(based on best practices of our open source [fhirbase](https://www.health-samurai.io/fhirbase)  project\)
* Flexible schema and dynamic validation
* REST API for CRUD and Search
* OAuth 2.0 Server and Access Control
* Terminology Service

[Aidbox](https://www.health-samurai.io/aidbox) is a metadata driven platform.  It means that almost everything is represented as data \(resources\). For example in Aidbox, REST endpoints \(Operations\), Resources Definitions, Profiles, Access Policies, etc are represented as Resources - we call it Meta-Resources. Meta-Resources play by same rules as other resources  - you can request and manipulate Meta-Resources through unified REST API. 

### Get your Box

Register in  [Aidbox.Cloud](https://www.health-samurai.io/aidbox) and create your personal boxes. Each box is an instance of a FHIR server with a separate database and domain. You can create multiple boxes for development, staging and  production. For local development you can run [Aidbox.Dev](../installation/setup-aidbox.dev.md) in docker. For production you can buy [Aidbox.One](../installation/deploy-aidbox.one.md) or [Aidbox.Enterprise](../installation/aidbox.enterprise.md) editions.

### FHIR & Aidbox

Aidbox implements most of [FHIR specification](https://www.hl7.org/fhir/) and supports all official versions of this standard. In addition Aidbox has a lot of useful in "real-life" extensions. Aidbox is designed to be **FHIR** compatible, but uses its own framework.  The key differences are listed below:

* Resources are stored in [Aidbox Format](./#aidbox-and-fhir-formats), which is isomorphic to FHIR, but not the same.
* Aidbox serves two API    from `/` - **Aidbox API** and `/fhir` - **FHIR API**. Aidbox API work with Aidbox Format and FHIR work with FHIR format. When you interact with FHIR endpoints Aidbox does on-fly conversion between this two formats.
* Aidbox supports **First-Class Extensions** and **Custom Resources**, which are prohibited in FHIR, but very handy in "real" systems.
* Aidbox use its own Entity/Attribute, SearchParameter and AidboxProfile framework instead of FHIR Structure Definitions. FHIR Profiles should be converted into Aidbox meta-resources.

### Resources

In Aidbox everything is a **Resource**! Each resource type is described with special **Entity** and **Attribute** meta-resources. **Entity** describes resources and types. **Attributes** describe structure of resources and complex types. For each **Entity** Aidbox generates database schema in PostgreSQL,  REST endpoints for CRUD, History, Search and other operations and JSON-schema for validation. 

### Aidbox & FHIR Format

Aidbox stores FHIR resources almost as is with 3 types of isomorphic transformations:

* References - more structured references
* Union \(Choice Types\) - 
* First-Class Extensions

[Read More](aidbox-and-fhir-formats.md)

### CRUD Operations Differences

#### create

```http
POST [base]/[type]
```

FHIR API [ignores](https://www.hl7.org/fhir/http.html#create) `id` in `POST` requests, but Aidbox API respect `id` inside request body and creates resource with specific `id`. This decision was made because we didn't find any reasons to ignore it and to make Aidbox API be closer to sql `INSERT` query. As a result new response code `409 Conflict` appeared:\`

* **`201` Created** - resource successfully created
* **`400` Bad Request** - resource could not be parsed or failed basic FHIR validation rules
* **`409`** **Conflict** - resource with such id already exists
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

#### update

```http
PUT [base]/[type]/[id]
```

Aidbox doesn't have atomic update yet. It also allows to omit `id` in resource body

* **`200` OK** - resource successfully updated
* **`201` Created** - resource successfully created
* **`422` Unprocessable Entity** - the proposed resource violated applicable FHIR profiles or server business rules

#### delete

```
DELETE [base]/[type]/[id]
```

Respond with `200 OK` on successful delete, but on deletion of already deleted resource respond with `204 No Content` \(conforming FHIR specification\). This feature added to make delete work in the same way as SQL `DELETE RETURNING *`.

To get `204 No Content` instead of `200 OK` use `_no-content=true` query parameter. 

* **`200` OK** - resource successfully delete
* **`204` No Content** - resource already deleted
* **`404` Not Found** - resource not found

#### conditional create

```
POST [base]/[type]?[search parameters]
```

Instead of using `If-None-Exist` header, Aidbox uses query parameters as in ordinary `read` operation. This done to make all conditional operations to look the same \(use search query parameters\). 

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server returns the found resource and `200 OK`
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

#### conditional update

```
PUT [base]/[type]?[search parameters]
```

In contrast to FHIR, Aidbox conditional update allows to create a resource with specific `id`. `Conditional update` in case of one match ignores id coming in request body, that mean that resource `id` can't be changed by any `update` operation.

* **No matches**: The server performs a `create` interaction \(Aidbox version of create\)
* **One Match**: The server performs the update against the matching resource
* **Multiple matches**: The server returns a `412 Precondition Failed` error indicating the client's criteria were not selective enough

#### conditional delete

```text
DELETE [base]/[type]?[search parameters]
```

It's not clear how to perform an ordinary `delete` on no matches, that's why `404 Not Found` will be returned in this case.

* **No matches:** The respond with `404 Not Found`
* **One Match**: The server performs an ordinary `delete` on the matching resource
* **Multiple matches**: Servers respond with `412 Precondition Failed` error indicating the client's criteria were not selective enough

