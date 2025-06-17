# Skip validation of references in resource using request header

### Skip validation of references in resource using request header

`aidbox-validation-skip` header allows to skip resource reference validations. It allows creating and updating resources containing references to non-existent target resources. Useful for staged data loading or systems with eventual consistency but may compromise referential integrity and generally is not recommended to use in production installations.

The header functionality can be enabled with `BOX_FHIR_VALIDATION_SKIP_REFERENCE` env. Check the [settings reference](../reference/settings/fhir#fhir.validation.skip-reference) for more details.

{% code title="Example" %}
```yaml
BOX_FHIR_VALIDATION_SKIP_REFERENCE=true
```
{% endcode %}

#### Usage

Request without `aidbox-validation-skip` request header causes an error.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /fhir/Observation/f001
content-type: text/yaml

resourceType: Observation
id: f001
subject:
  reference: Patient/id-does-not-exist
status: final
code:
  coding:
  - system: http://loinc.org
    code: 15074-8
```
{% endtab %}

{% tab title="Response" %}
```yaml
status: 422
body:
  issue:
  - expression:
    - Observation.subject
    diagnostics: Referenced resource Patient/id-does-not-exist does not exist
```
{% endtab %}
{% endtabs %}

The request is successful when `aidbox-validation-skip` request header is provided.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /fhir/Observation/f001
content-type: text/yaml
aidbox-validation-skip: reference

resourceType: Observation
id: f001
subject:
  reference: Patient/id-does-not-exist
status: final
code:
  coding:
  - system: http://loinc.org
    code: 15074-8
```
{% endtab %}

{% tab title="Response" %}
```yaml
status: 201
* body is skipped for simplicity *
```
{% endtab %}
{% endtabs %}
