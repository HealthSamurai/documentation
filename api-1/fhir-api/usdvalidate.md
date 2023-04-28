# $validate

## Introduction

The tool introduced by FHIR to provide a separate validation mechanism for 2-steps commit workflow or for development needs. It works for create, update and delete operations, is called using `?mode=` query parameter with values `create`, `update`, `delete` but changes won't be committed. Instead a requester will get an `OperationOutcome` with information about validation results. See [http://hl7.org/fhir/resource-operation-validate.html](http://hl7.org/fhir/resource-operation-validate.html) for the official documentation.&#x20;

```
#FHIR format endpoint:
POST /fhir/<resourceType>/$validate
POST /fhir/<resourceType>/<id>/$validate

#Aidbox format endpoint:
POST /<resourceType>/$validate
POST /<resourceType>/<id>/$validate
```

Such requests check the resource structure, internal business rules and return a list of problems if some exist.

**`200`** **OK** — those requests always return status 200\
Success and failure of a validation request is determined by `id` of `OperationOutcome` resource. `allok` and `validationfail` are self-descriptive.

$validate supports two ways to pass arguments:

{% code title="By FHIR spec it receives a Parameters resource as a body:" %}
```yaml
POST .../$validate

resourceType: Parameters
parameter:
- name: mode
  value: {string: <mode>}
- name: profile
  value: {uri: <StructureDefinition.url>}
- name: esource
  resource: <resource>

```
{% endcode %}

{% code title="Parameters are specified in query string; body is the resource to validate:" %}
```http
POST .../$validate?mode=<mode>&profile=<StructureDefinition.url>

<resource>
```
{% endcode %}

| Parameter      | Description                                                                                                                                                                                                                                                                  |
| -------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `resourceType` | Required. Type of the resource which needs validation                                                                                                                                                                                                                        |
| `id`           | Optional for `mode`=`create`. Can either be passed in the resource body or be specified in the route params                                                                                                                                                                  |
| `resource`     | Optional for `mode`=`delete`, required otherwise. Resource to be validated                                                                                                                                                                                                   |
| `mode`         | Optional. Default is `create`. Possible values are `create, update, delete, patch`                                                                                                                                                                                           |
| `profile`      | <p>Optional. Can be passed multiple times. Used to validate with specific profiles.<br>Value should be <code>StructureDefinition.url</code> of the profile defined as <a href="../../modules-1/profiling-and-validation/profiling.md#validation-with-zen">zen schema</a></p> |

| `mode`        | Description                                                                                                                                                                                                            |
| ------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `create`      | Ignores errors about attributes`id` & `lastUpdated` being required                                                                                                                                                     |
| `update`      | Validates without ignoring errors about attributes`id` & `lastUpdated`                                                                                                                                                 |
| `delete`      | Checks if resource with such `id` exists in Aidbox                                                                                                                                                                     |
| `patch`       | <p>Merges the existing resource to the received resource and then validates as <code>update</code>.</p><p>Patching strategy will be determined as <a href="../api/crud-1/patch.md#patch-method">described here</a></p> |
| `merge-patch` | simple deep merge semantics ([read more in RFC](https://tools.ietf.org/html/rfc7386))                                                                                                                                  |
| `json-patch`  |  advanced JSON transformation ([read more in RFC](https://tools.ietf.org/html/rfc6902))                                                                                                                                |

## Examples

### Validation Success

{% code title="Request contains valid Patient resource inside the body." %}
```yaml
# Request:
POST /fhir/Patient/$validate?mode=create

name: [{given: [John]}]

# Response
HTTP 200 OK

id: allok
resourceType: OperationOutcome
issue:
- {severity: informational, code: informational, diagnostics: all ok}
```
{% endcode %}

{% code title="Request contains same valid Patient resource and passed as Parameters." %}
```yaml
# Request:
POST /fhir/Patient/$validate

resourceType: Parameters
parameter:
- name: mode
  value: {string: create}
- name: resource
  resource: {name: [{given: [John]}]}


# Response
HTTP 200 OK

id: allok
resourceType: OperationOutcome
issue:
- {severity: informational, code: informational, diagnostics: all ok}
```
{% endcode %}

### Validation Failure

{% code title="Patient name must be an array, test is a non-existing field." %}
```yaml
# Request:
POST /fhir/Patient/$validate?mode=create

name: "Bob"
test: "foo"

# Response
HTTP 200 OK

id: validationfail
resourceType: OperationOutcome
text: {status: generated, div: Invalid resource}
issue:
- severity: fatal
  code: invalid
  expression: [Patient.name]
  diagnostics: expected array
- severity: fatal
  code: invalid
  expression: [Patient.test]
  diagnostics: extra property

```
{% endcode %}

{% code title="Request contains same invalid Patient resource and passed as Parameters." %}
```yaml
# Request:
POST /fhir/Patient/$validate?mode=create

name: "Bob"
test: "foo"

# Response
HTTP 200 OK

resourceType: Parameters
parameter:
- name: mode
  value: {string: create}
- name: resource
  resource: {name: Bob, test: foo}
```
{% endcode %}
