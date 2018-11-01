# Transaction/Batch

### Introduction

Transaction interaction allows to perform few interaction using one http request. There are two types of transaction interaction \(type is specified by field `type`\): batch and transaction, the first one just executes requests one by one, the second one doing the same, but rollback all changes if any request failed. 

```
POST [base]
```

Body of such request contains one resource of type Bundle, which contains field entry with an array of interactions, for example \(taken from [Getting Started with SPA ](../tutorials/getting-started-with-spa.md)tutorial\):

```yaml
POST /
​
type: transaction
entry:
- resource:
    id: admin
    email: "admin@mail.com" # Change this value
    password: "password" # Change this value
  request:
    method: POST
    url: "/User"
​
- resource:
    id: SPA
    redirect_uri: http://localhost:4200
  request:
    method: POST
    url: "/Client"
    
- resource:
    engine: json-schema
    schema:
      type: object
      required:
      - user
  request:
    method: POST
    url: "/AccessPolicy"
```

Each element of entry array contains resource field \(body of request\) and request field \(request line in terms of HTTP request\).

```yaml
resource: # not needed for DELETE and GET
  # resource here

fullUrl: "something-here" # needed if you want to refer
                          # the resource inside bundle
request:
  method: POST # POST/GET/PUT/DELETE
  url: "/ResourceType" # request url
```

### Processing rules and Conditional refs

Transaction interaction processed in the order provided in a bundle, each interaction executed one by one. It differs from FHIR transaction [processing rules](https://www.hl7.org/fhir/http.html#2.21.0.17.2).

For `type: batch` references to resources inside bundle won't be resolved.

For `type: transaction` before processing interactions, all references in resource will be attempted to resolve. In this example ProcedureRequest will refer to newly created patient:

```yaml
- resource:
    resourceType: Patient
  fullUrl: "urn:uuid"
  request: 
    method: POST
    url: "/Patient?_identifier=mrn:123"
    
- resource:
    resourceType: ProcedureRequest
    subject: {uri: "urn:uuid"} 
    # will be changed before processing to 
    # {id: <id-of-patient>, resourceType: Patient}
  request:
    method: POST
    url: "/ProcedureRequest"
    
```

You can provide a fullUrl with value like `"urn:<uuid-here>"` and reference to the resource created by such interaction using ref: `{uri: "urn:<uuid-here>"}`. Those references are temporary and will be translated to valid Aidbox references, when interaction entry will be processed.

{% hint style="danger" %}
You SHALL NOT refer resource, which will be created later using conditional operations!
{% endhint %}

Happy transacting!

