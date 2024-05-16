# Skip validation of references in resource using request header

### Skip validation of references in resource using request header

`aidbox-validation-skip` header allows to skip resource reference validations.

The header functionality can be enabled with `box_features_validation_skip_reference` env.

{% code title="Example" %}
```yaml
box_features_validation_skip_reference=true
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
