# Include and Revinclude

{% hint style="warning" %}
Make sure [Conformant Include](../../../reference/settings/fhir.md#fhir.search.include.conformant) is set!&#x20;
{% endhint %}

The `_include` and `_revinclude` search parameters allow a search to return additional resources related to the matching resources.

## Include

The `_include` parameter returns additional resources that are referenced by the matching resources. For example, when searching for MedicationRequest resources, you can include the referenced Practitioner resources:

```
GET /fhir/MedicationRequest?patient=123&_include=MedicationRequest:practitioner
```

### Syntax

```
 _include(:reverse|:iterate|:logical)=source-type:search-param:(:target-type)
```

Here `search-param` is a name of the search parameter with the type reference defined for `source-type`.

This query can be interpreted in the following manner. For the `source-type` resources in the result include all `target-type` resources, which are referenced by the `search-param`.

`target-type` is optional for not chained includes and means all referenced resource types:

```
GET /fhir/Encounter?_include=Encounter:subject 
=> GET /fhir/Encounter?_include=Encounter:subject:*
```

For more explicit interpretation and performance reasons, the client **must** provide `target-type` for chained includes!

## Revinclude

The `_revinclude` parameter returns additional resources that reference the matching resources. It performs the reverse of `_include`. Instead of following references forward, it follows them backward to find resources referencing the matches.

For example, to find all Provenance resources that reference a specific MedicationRequest:

```
GET /fhir/MedicationRequest?_id=123&_revinclude=Provenance
```

### Syntax

```
_revinclude(:reverse|:iterate|:logical)=source-type:search-param(:target-type)
```

Interpretation: include all `source-type` resources, which refer `target-type` resources by `search-param` in the result set.

## Chained (rev)includes

The `:iterate` modifier can be used with both `_include` and `_revinclude` to recursively follow references.\
This allows retrieving resources that are multiple hops away in the reference chain. Without `:iterate` modifier, `_include` and `_revinclude` are only used for the main resource type (you can not choose different `source-type` without `:iterate`).

Example:

```
 GET /fhir/Observation?code=http://snomed.info/sct|3738000&_include=Observation:patient&_include:iterate=Patient:link
```

would match any observations with the SNOMED code `3738000` (Viral hepatitis (disorder)). The results would include resources from following the search reference `Observation.patient`, which are `Patient` resources linked via `Observation.subject`. Additionally, the server would iterate through the included patient records and follow the `Patient.link` references, including linked `Patient` or `RelatedPerson` resources.

Also, you can use `_include:iterate` to recursively follow references. `Observation.has-member` is a reference to a group of possible observations. Suppose, `org-1` is a part of `org-2`, `org-2` is a part of `org-3`. This request will return `org-1`, `org-2` and `org-3`:

```
GET /fhir/Organization?_id=org-1&_revinclude:iterate=Organization:partof
```

## (rev)include and \_elements

Aidbox enhances FHIR `_elements` parameter to support fields from included resources.

Example:

```
GET /Encounter?_include=patient&_elements=id,status,Patient.name,Patient.birthDate
```

## :logical modifier

If you provide `:logical` modifier, Aidbox will include logically referenced resources as well. Logical reference means reference with an  `type` field set to resource-type and `identifier` field set to one of the identifiers of the referenced resource.

### Example

If Encounter references Patient logically using `Reference.identifier` like this:

```
PUT /fhir/Encounter

resourceType: Encounter
id: enc-123
subject: 
  type: Patient
  identifier: {system: 'ssn', value: '78787878'}
class: {code: 'IMP', 
  system: 'http://terminology.hl7.org/CodeSystem/v3-ActCode', 
  display: 'inpatient encounter'}
status: finished
```

You can include Patient resources by providing `:logical` modifier:

```
GET /fhir/Encounter?_include:logical=Encounter:patient
```

Or include Encounter resources to Patient resources by providing `:logical` modifier:

```
GET /fhir/Patient?_revinclude:logical=Encounter:patient:Patient
```

## Authorize Inline Requests Mode

Aidbox provides access control for inline requests (\_include & \_revinclude) to ensure users can only retrieve resources they are authorized to view. When a search request contains an inline query, Aidbox verifies access by performing an authorization check against a search query for the included resource. If the requesting user lacks the necessary access rights to the included resource, the entire request is denied with a `403` status.

To enable access control for inline requests, set the following environment variable:

```
BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS=true
```

### AccessPolicy Examples with Authorize Inline Requests Mode

Below are examples of AccessPolicy configurations that allow requests such as:

```
GET /fhir/Patient?_include=Patient:organization
```

With this AccessPolicy, the user can use any `_include` parameters that result in Organization resources being included. However, the query will be rejected if it attempts to include any other resource types.

```
  operation:
    id: "FhirSearch"
  params:
    resource/type: "Patient"
  user:
    id: "my-user-1"
    resourceType: "User"

  operation:
    id: "FhirSearch"
  params:
    resource/type: "Organization"
  user:
    id: "my-user-1"
    resourceType: "User"
```

### AccessPolicy Examples without Authorize Inline Requests Mode

If this mode is not enabled, you must define the specific \_include or \_revinclude parameters allowed in an AccessPolicy, as shown in the example below. However, this method can be inflexible, and we recommend using **Authorize Inline Requests Mode** in most cases.

```
  operation:
    id: "FhirSearch"
  params:
    resource/type: "Patient"
    _include: "Patient:organization"
  user:
    id: "my-user-1"
    resourceType: "User"
```

## See also:

* [Search Settings Reference](../../../reference/settings/fhir.md#search)
