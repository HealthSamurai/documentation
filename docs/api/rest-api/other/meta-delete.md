---
description: Remove profile, tag, and security labels from FHIR resource metadata using $meta-delete operation.
---

# $meta-delete

## Introduction

The `$meta-delete` operation removes `profile`, `tag`, and `security` labels from a resource's metadata.
See [http://hl7.org/fhir/resource-operation-meta-delete.html](http://hl7.org/fhir/resource-operation-meta-delete.html) for the official documentation.

```
POST /fhir/<resourceType>/<id>/$meta-delete
```

The operation accepts a `Parameters` resource containing the metadata to remove:

{% code title="By FHIR spec it receives a Parameters resource as a body:" %}
```yaml
POST /fhir/<resourceType>/<id>/$meta-delete

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

The identity of a `tag` or `security` label is based on the combination of `system` and `code`, other fields are ignored.
For `profile` entries, matching is based on the full URL.
It is not an error if these `tag`, `profile`, and `security` labels do not exist.

Returned value is the current resource metadata.

## Examples

### Removing metadata

{% code title="Removing tag metadata from a Patient." %}
```yaml
# Request:
POST /fhir/Patient/pt-1/$meta-delete
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
```
{% endcode %}

{% code title="Response" %}
```
HTTP 200 OK

resourceType: Parameters
parameter:
  - name: return
    valueMeta:
      tag:
        - code: record-lost
          system: http://example.org/codes/tags
          display: Patient File2 Lost
      profile:
        - http://hl7.org/fhir/StructureDefinition/daf-patient
        - http://hl7.org/fhir/StructureDefinition/uslab-patient
      security:
        - code: EMP
          system: http://hl7.org/fhir/v3/ActCode
          display: employee information sensitivity
      lastUpdated: '2025-10-27T10:28:15.304548Z'
      versionId: '342'
```
{% endcode %}

{% code title="Removing remaining tags, profiles, and security labels from a Patient." %}
```yaml
# Request:
POST /fhir/Patient/pt-1/$meta-delete
content-type: text/yaml
accept: text/yaml

resourceType: Parameters
parameter:
  - name: meta
    valueMeta:
      tag:
        - code: record-lost
          system: http://example.org/codes/tags
          display: Patient File2 Lost
      profile:
        - http://hl7.org/fhir/StructureDefinition/daf-patient
        - http://hl7.org/fhir/StructureDefinition/uslab-patient
      security:
        - code: EMP
          system: http://hl7.org/fhir/v3/ActCode
          display: employee information sensitivity
```
{% endcode %}

{% code title="Response" %}
```yaml
HTTP 200 OK

resourceType: Parameters
parameter:
  - name: return
    valueMeta:
      lastUpdated: '2025-10-27T10:31:10.043131Z'
      versionId: '344'
```
{% endcode %}

See also:
- [$meta operation](meta.md)
- [$meta-add operation](meta-add.md)
- [FHIR $meta-delete operation specification](http://hl7.org/fhir/resource-operation-meta-delete.html)
