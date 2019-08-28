# Architecture Overview

[Aidbox](https://www.health-samurai.io/aidbox) is a metadata driven platform. What does it mean? It means that we are making almost everything to be represented as data \(resources\). For example in Aidbox, REST endpoints \(Operations\), Resources Structure Definitions, Profiles, Access Policies, Periodic Jobs, etc are represented as Resources - we call it Meta-Resources. Meta-Resources play by same rules as other resources  - you can request and manipulate Meta-Resources through unified REST API. 

### Box

If you already have an [Aidbox](https://www.health-samurai.io/aidbox) account, then you can create your own boxes. Each box is an instance of a FHIR server with a separate database and URL.

For example, you can create several boxes for development, one box for staging, and another for production.

If you already have an [Aidbox](https://www.health-samurai.io/aidbox) account, then you can create your own boxes. Each box is an instance of FHIR server with a separate database and base URL.

For example, you can create several boxes for development, one box for staging, and another for production.

Boxes can be created from the Dashboard using REST API, or aidbox-cli utility.

We take care of all the maintaining, scaling, and updating of your boxes.

Box management is done on the 'Dashboard' page. 

The 'Dashboard' is a place where you can see all your existing boxes and create, manage, share and destroy boxes. Each box in the Dashboard has a name, an URL where it will be deployed, a list of users the box is shared with, a destroy option, and a payment plan indicator.

### Resources

In [Aidbox](https://www.health-samurai.io/aidbox), you define a resource by describing its metadata then Aidbox will generate storage schema on the fly in PostgreSQL to save instances of your resource, generate REST routing for CRUD, History, and Search Operations, generate JSON-schema to validate resources.

### Operations

### Aidbox vs FHIR



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

### Entities & Attributes

In [Aidbox](https://www.health-samurai.io/aidbox), structure of all resources is defined by two meta-resources Entity and Attribute. Entity can be of 3 types - primitive, type, or resource. Primitive is a built-in primitive type. You are not allowed to create your primitives so if you have missed one - please contact us. Repeating combinations of primitive types are composed into types \(complex types like Address, HumanName, etc\). Entity with the type "resource" composes a set of primitive and complex type Attributes. When resource is described - tables to storage data and REST API for this resource are generated on the fly based on the definition. 

Let's take a look at the definition of Entity and Attribute Resources. If you have access to your box you can get this metadata by the REST endpoint `GET /Entity?_id=Entity,Attribute`.

```yaml
- resourceType: Entity
  id: Entity
  text: Entity metadata
  type: resource
  module: proto
- resourceType: Entity
  id: Attribute
  text: Entity attribute metadata
  type: resource
  module: proto
```

As seen, they are both defined as **Entity** with type **resource** with id matching **resourceType** and coming from module **proto** \(see more about Modules in Aidbox\).

Let's see what attributes are defined for Entity \( `GET /Attribute?entity=Entity`\):

```yaml
- resourceType: Attribute
    module: proto
    id: Entity.id
    path: [id]
    type: { id: keyword, resourceType: Entity }
    resource: { id: Entity, resourceType: Entity }
  - resourceType: Attribute
    module: proto
    id: Entity.module
    path: [module]
    type: { id: keyword, resourceType: Entity }
    resource: { id: Entity, resourceType: Entity }
  # skip repeating attributes
  - path: [type]
    isRequired: true
    enum:
      - abstract
      - resource
      - type
      - primitive
    type: { id: keyword}
  - path: [description]
    isRequired: true
    type: { id: string}
  - path: [isOpen]
    type: { id: boolean}
  - path: [schema]
    isOpen: true
    type: { id: boolean}
```

* **id**  - attribute is common for all resources, 
* **module** - all meta-resources has module attribute \(in which module this resource is defined - see more about modules\)
* **type** -  is enum of resource, type and primitive
* **description** - is text about entity
* **isOpen** - if this flag is set resource can be arbitrary json document without fixed schema
* **schema** - if resource can not be described as list of attributes, you can describe it directly by attaching JSON schema

Now, let's inspect attributes of the Attribute meta-resource  \(`GET /Attribute?entity=Attribute`\):

```yaml
# repeating attributes are skiped
- path: [ isCollection ]
  text: Define element as collection
  type: { id: boolean }
- path: [ isUnique ]
  text: Unique constraint on element
  type: { id: boolean }
- path: [ path ]
  type: { id: keyword }
  isRequired: true
  isCollection: true
- path: [ description ]
  type: { id: string }
- path: [ type ]
  type: { id: Reference }
- path: - resource
  type: { id: Reference }
  isRequired: true
- path: - isOpen
  text: Do not validate extra keys
  type: id: boolean
- path: - isRequired
  type: id: boolean
- path: [ schema ]
  isOpen: true
- path: [ module ]
  type: id: keyword
- path: - enum
  text: For simple cases enumerate values
  type: id: string
  isCollection: true
- path: [ isModifier ]
  type: id: boolean
- path: [ union ]
  text: List of polymorphic types
  type: { id: Reference }
  isCollection: true
- path: [ text ]
  type: { id: string }
- path: [ valueSet ]
  module: fhir-3.0.1
- id: Attrubute.valueSet.id
  path: [ valueSet,  id]
  type: id: keyword
- path: [ valueSet,  uri ]
  type: { id: string }
- path: [ refers ]
  type: { id: string }
  isCollection: true
- path: [ order ]
  type: { id: integer }
  description: Order of elements for xml or humans
- path: [ valueSet,  resourceType ]
  type: { id: keyword }
- path: [ isSummary ]
  type: { id: boolean }
```

You are allowed to define new Entities, add new attributes to existing entities. We do not recommend to override or delete existing Entities and Attributes.

### Modules

[Aidbox](https://www.health-samurai.io/aidbox) is split into modules. We have core module proto, modules for each FHIR version, and additional modules like OpenID Connect, Chat, etc.

Each module can consist of definitions  of:

* Entities and Attributes  - i.e. defines a set of new Resources and/or extensions for existing ones
* Operations - REST endpoints
* SearchParameters

For example, core module **proto** introduces core meta-resources like Entity, Attributes, and basic CRUD & Search Operations. FHIR modules are definitions of FHIR resources by Entity, Attributes which are imported from FHIR metadata.

