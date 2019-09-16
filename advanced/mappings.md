---
description: >-
  Mapper module provides a way to convert data with arbitrary schema to FHIR
  resource(s) and store results in Aidbox.
---

# Mappings

Most of real-world healthcare applications are integrated with 3rd-party systems to ingest data from them. Using Mapping resource you can describe how 3rd-party data is being converted to the FHIR format. Mappings are written using [JUTE language](https://github.com/HealthSamurai/jute.clj#introduction).

Mapping is an Aidbox resource, so all [CRUD operations](../basic-concepts/crud-1/) are applicable to it. Mapping's schema is pretty straightforward:

```yaml
resourceType: Mapping
id: mapping-id-here
body:
  # Executable part of the mapping written in JUTE language (required)
scopeSchema:
  # JSON schema for the incoming data (optional)
```

If `scopeSchema` attribute is provided, incoming mapping data \(also called a scope\) will be validated against it first. Then a JUTE template from `body` will be executed. Mapping should return a valid [Transaction Bundle](../basic-concepts/transaction.md), so when it will be applied, it will be able to operate with multiple resources, not just single one.

### Example

Let's do a simple mapping which will create a Patient resource from following data structure:

```yaml
firstName: John
lastName: Smith
birthDate: 2000-01-02
```

Following request will create a mapping resource:

{% hint style="info" %}
If you're not familiar with JUTE, please check out [JUTE Tutorial](https://github.com/HealthSamurai/jute.clj#quickstart-tutorial) to understand basic  concepts.
{% endhint %}

```http
PUT /Mapping/example
Content-Type: text/yaml

resourceType: Mapping
id: example
scopeSchema:
  # JSON schema describing incoming data structure
  type: object
  required: ["firstName", "lastName", "birthDate"]
  properties:
    firstName:
      type: string 
    lastName:
      type: string
    birthDate:
      type: string
      pattern: "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"

body:
  # JUTE template which transforms incoming data to Transaction Bundle
  resourceType: Bundle
  type: transaction
  entry:
    - request:
        url: /fhir/Patient
        method: POST
      resource:
        resourceType: Patient
        birthDate: $ birthDate
        name:
          - given: ["$ firstName"]
            family: $ lastName

```

When template was created, you can invoke $apply operation on it to generate Patient resource and save it into the database:

```yaml
POST /Mapping/example/$apply
Content-Type: text/yaml

firstName: John
lastName: Smith
birthDate: 2010-12-12
```

### $apply Endpoint

To execute Mapping and to store it's result in Aidbox database do a POST request to the $apply endpoint. Request's body will be passed to a JUTE as an incoming data \(scope\):

```http
POST /Mapping/<mapping-id>/$apply
Content-Type: application/json

{ "foo": 42, "bar": "hello" }
```

{% hint style="warning" %}
Make sure that your Mapping returns a Transaction Bundle, otherwise it's result won't be persisted in the database.
{% endhint %}

### $debug Endpoint

To check Mapping's result without actually persisting it, you can do a POST request to the $debug endpoint:

```http
POST /Mapping/<mapping-id>/$debug
Content-Type: text/yaml

foo: 42
bar: hello
```

Response will contain a mapping result or an error if evaluation failed for some reason.

### Global $debug Endpoint

There is a way to check mapping result without persisting it as a resource:

```http
POST /Mapping/$debug
Content-Type: text/yaml

mapping:
  body:
    mul: $ 2 * foo
    str: $ "hello, " + name
    
  scopeSchema:
    type: object
    required: ["foo", "name"]
    properties:
      foo:
        type: integer
      name:
        type: string
    
scope:
  foo: 4
  name: "Bob"
```

You pass both Mapping and incoming data \(scope\) in a request body. Request response will contain mapping result or an error information.

### Including Mapping inside other Mapping

In Aidbox there is an `$include` directive which allows you to include a Mapping within another one:

```text
PUT /Mapping/index
Content-Type: text/yaml

resourceType: Mapping
id: index
body:
  $switch: $ type
  patient:
    $include: "patient"
  practitioner:
    $include: "practitioner"
  $default: null
```

This template will pass execution to either `/Mapping/patient` or `/Mapping/practitioner` depending on value of `type` key. Current evaluation scope will be passed to the included Mapping.

{% hint style="warning" %}
Because potentially there is a way to create infinite recursion using a `$include` directive, there is a inclusion depth limit which equals **5** for now.
{% endhint %}

### Mapping Editor in the Aidbox UI

![Mapping Editor UI](../.gitbook/assets/screenshot-2019-09-16-at-17.26.13.png)

There is a Mapping Editor in the Aidbox UI with built-in syntax checker and Debug capabilities. Search for a "Mappings" item in the left menu.

