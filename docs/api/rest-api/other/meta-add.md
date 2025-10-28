# $meta-add

## Introduction

The `$meta-add` operation adds new `profile`, `tag`, and `security` labels to a resource's metadata.
See [http://hl7.org/fhir/resource-operation-meta-add.html](http://hl7.org/fhir/resource-operation-meta-add.html) for the official documentation.

```
POST /fhir/<resourceType>/<id>/$meta-add
```

The operation accepts a `Parameters` resource containing the metadata to add:

{% code title="By FHIR spec it receives a Parameters resource as a body:" %}
```yaml
POST /fhir/<resourceType>/<id>/$meta-add

resourceType: Parameters
parameter:
  - name: meta
    valueMeta:
      profile:
        - <profile.url>
      security:
        - system: <system.url>
          code: <code>
          display: <displayText>
      tag:
        - system: <system.url>
          code: <code>
          display: <displayText>
```
{% endcode %}

The `profile`, `tag`, and `security` fields are treated as sets, so duplicates are not created.
The identity of a `tag` or `security` label is based on the combination of `system` and `code`, other fields are ignored.
For `profile` entries, matching is based on the full URL.

Returned value is the current resource metadata.

## Examples

### Adding metadata

{% code title="Adding profile, security, and tag metadata to Patient." %}
```yaml
# Request:
POST /fhir/Patient/pt-1/$meta-add
content-type: text/yaml
accept: text/yaml

resourceType: Parameters
parameter:
  - name: meta
    valueMeta:
      profile:
        - http://hl7.org/fhir/StructureDefinition/daf-patient
        - http://hl7.org/fhir/StructureDefinition/uslab-patient
      security:
        - system: http://hl7.org/fhir/v3/ActCode
          code: EMP
          display: employee information sensitivity
      tag:
        - system: http://example.org/codes/tags
          code: current
          display: Current Inpatient

```
{% endcode %}

{% code title="Response" %}
```yaml
HTTP 200 OK

resourceType: Parameters
parameter:
  - name: return
    valueMeta:
      tag:
        - code: current
          system: http://example.org/codes/tags
          display: Current Inpatient
      profile:
        - http://hl7.org/fhir/StructureDefinition/daf-patient
        - http://hl7.org/fhir/StructureDefinition/uslab-patient
      security:
        - code: EMP
          system: http://hl7.org/fhir/v3/ActCode
          display: employee information sensitivity
      lastUpdated: '2025-10-27T09:41:07.455313Z'
      versionId: '331'
```
{% endcode %}

### Adding metadata with partially existing fields

{% code title="Adding two tags, one already present to Patient." %}
```yaml
# Request:
POST /fhir/Patient/pt-1/$meta-add
content-type: text/yaml
accept: text/yaml

resourceType: Parameters
parameter:
  - name: meta
    valueMeta:
      tag:
        - system: http://example.org/codes/tags
          code: current
          display: Current Inpatient
        - system: http://example.org/codes/tags
          code: record-lost
          display: Patient File Lost

```
{% endcode %}

{% code title="Response" %}
```yaml
HTTP 200 OK

resourceType: Parameters
parameter:
  - name: return
    valueMeta:
      tag:
        - code: current
          system: http://example.org/codes/tags
          display: Current Inpatient
        - code: record-lost
          system: http://example.org/codes/tags
          display: Patient File Lost
      profile:
        - http://hl7.org/fhir/StructureDefinition/daf-patient
        - http://hl7.org/fhir/StructureDefinition/uslab-patient
      security:
        - code: EMP
          system: http://hl7.org/fhir/v3/ActCode
          display: employee information sensitivity
      lastUpdated: '2025-10-27T10:21:22.813834Z'
      versionId: '338'
```
{% endcode %}

See also:
- [$meta operation](meta.md)
- [$meta-delete operation](meta-delete.md)
- [FHIR $meta-add operation specification](http://hl7.org/fhir/resource-operation-meta-add.html)
