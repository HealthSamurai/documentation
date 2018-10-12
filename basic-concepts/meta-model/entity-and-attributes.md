# Entity & Attribute

In Aidbox, structure of all resources is defined by two meta-resources Entity and Attribute. Entity can be of 3 types - primitive, type, or resource. Primitive is a built-in primitive type. You are not allowed to create your primitives so if you have missed one - please contact us. Repeating combinations of primitive types are composed into types \(complex types like Address, HumanName, etc\). Entity with the type "resource" composes a set of primitive and complex type Attributes. When resource is described - tables to storage data and REST API for this resource are generated on the fly based on the definition. 

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

