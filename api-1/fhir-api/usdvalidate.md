# $validate

## Introduction

The tool introduced by FHIR to provide a separate validation mechanism for 2-steps commit workflow or for development needs. It works for create, update and delete operations, is called using `?mode=` query parameter with values `create`, `update`, `delete` but changes won't be committed. Instead a requester will get an `OperationOutcome` with information about validation results. See [http://hl7.org/fhir/resource-operation-validate.html](http://hl7.org/fhir/resource-operation-validate.html) for the official documentation. 

```text
#FHIR format endpoint:
POST /fhir/<resourceType>/$validate
POST /fhir/<resourceType>/<id>/$validate

#Aidbox format endpoint:
POST /<resourceType>/$validate
POST /<resourceType>/<id>/$validate
```

Such requests check the resource structure, internal business rules and return a list of problems if some exist.

**`200`** **OK** — those requests always return status 200  
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

| Parameter | Description |
| :--- | :--- |
| `resourceType` | Required. Type of the resource which needs validation |
| `id` | Optional for `mode`=`create`. Can either be passed in the resource body or be specified in the route params |
| `resource` | Optional for `mode`=`delete`, required otherwise. Resource to be validated |
| `mode` | Optional. Default is `create`. Possible values are `create, update, delete, patch` |
| `profile` | Optional. Can be passed multiple times. Used to validate with specific profiles. Value should be `StructureDefinition.url` of the profile defined as [zen schema](../../app-development-guides/tutorials/profiling.md#validation-with-zen) |

<table>
  <thead>
    <tr>
      <th style="text-align:left"><code>mode</code>
      </th>
      <th style="text-align:left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left"><code>create</code>
      </td>
      <td style="text-align:left">Ignores errors about attributes<code>id</code> &amp; <code>lastUpdated</code> being
        required</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>update</code>
      </td>
      <td style="text-align:left">Validates without ignoring errors about attributes<code>id</code> &amp; <code>lastUpdated</code>
      </td>
    </tr>
    <tr>
      <td style="text-align:left"><code>delete</code>
      </td>
      <td style="text-align:left">Checks if resource with such <code>id</code> exists in Aidbox</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>patch</code>
      </td>
      <td style="text-align:left">
        <p>Merges the existing resource to the received resource and then validates
          as <code>update</code>.</p>
        <p>Patching strategy will be determined as <a href="../api/crud-1/patch.md#patch-method">described here</a>
        </p>
      </td>
    </tr>
    <tr>
      <td style="text-align:left"><code>merge-patch</code>
      </td>
      <td style="text-align:left">simple deep merge semantics (<a href="https://tools.ietf.org/html/rfc7386">read more in RFC</a>)</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>json-patch</code>
      </td>
      <td style="text-align:left">advanced JSON transformation (<a href="https://tools.ietf.org/html/rfc6902">read more in RFC</a>)</td>
    </tr>
  </tbody>
</table>

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

