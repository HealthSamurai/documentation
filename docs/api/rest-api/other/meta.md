# $meta

## Introduction

The `$meta` operation retrieves a summary of the `profile`, `tag`, and `security` labels for a given resource.
See [http://hl7.org/fhir/resource-operation-meta.html](http://hl7.org/fhir/resource-operation-meta.html) for the official documentation.

```
GET /fhir/<resourceType>/<id>/$meta
```

The operation returns the resource's `meta` field.

The `$meta` operation is only supported at the individual resource level.

## Examples

{% code title="Request Patient metadata." %}
```yaml
# Request:
GET /fhir/Patient/pt-1/$meta
Accept: text/yaml
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

See also:
- [$meta-add operation](meta-add.md)
- [$meta-delete operation](meta-delete.md)
- [FHIR $meta operation specification](http://hl7.org/fhir/resource-operation-meta.html)
