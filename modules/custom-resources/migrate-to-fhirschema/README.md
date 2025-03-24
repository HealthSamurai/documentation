# Migrate to FHIR Schema

{% hint style="danger" %}
Entity & Attributes and Zen Schema are planned to be retired and will be replaced by FHIR Schema. Here’s a migration guide to help you transition your custom resources defined via Entity & Attributes / Zen Schema.
{% endhint %}

### Migration Overview

The migration process involves six key steps:

1. Identifying deprecated capabilities
2. Setting up a parallel environment with FHIR Schema
3. Migrating profiles
4. Migrating custom resources
5. Migrating search parameters
6. Validating resources and resolving issues

### Step 1: Identify Deprecated Capabilities

First, obtain a comprehensive list of all your EA and Zen components that will need migration:

```http
GET /deprecated/capabilities
```

Example response:

```json
{
  "custom-entities": {
    "total": 0,
    "entities": []
  },
  "custom-attributes": {
    "total": 0,
    "entities": []
  },
  "custom-search-param-resource": {
    "total": 1,
    "params": [
      "MyCustomResource.name"
    ]
  },
  "custom-zen-resources": {
    "total": 0,
    "resources": []
  },
  "zen-profiles": {
    "total": 111,
    "profiles": [
      "http://hl7.org/fhir/us/core/StructureDefinition/us-core-bmi",
      // ...additional profiles...
    ]
  },
  "custom-zen-search-params": {
    "total": 1,
    "params": [
      "main/my-parameter"
    ]
  },
  "zen-fhir-search-params": {
    "total": 100,
    "params": [
      "hl7-fhir-us-core.search.us-core-observation-code/search",
      // ...additional search params...
    ]
  }
}
```

### Step 2: Set Up Parallel Environment with FHIR Schema

Launch a parallel Aidbox instance configured to use FHIR Schema, connected to the same database and the same zen configuration project (if you have one):

```
AIDBOX_FHIR_SCHEMA_VALIDATION=true
```

For detailed configuration instructions, refer to the [FHIR Schema setup documentation](../../profiling-and-validation/fhir-schema-validator/setup.md).

### Step 3: Migrate FHIR Packages to FHIR Schema

Set FHIR core packages using an environment variable:

```
AIDBOX_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#3.1.0
```

### Step 4: Migrate Custom Resources

If you have custom resource defined by Entity & Attributes, follow this guide

{% content-ref url="migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md" %}
[migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md](migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md)
{% endcontent-ref %}

If you have custom resource defined by Zen, follow this guide -

{% content-ref url="migrate-custom-resources-defined-with-zen-to-fhir-schema.md" %}
[migrate-custom-resources-defined-with-zen-to-fhir-schema.md](migrate-custom-resources-defined-with-zen-to-fhir-schema.md)
{% endcontent-ref %}

### Step 6: Validate Resources and Resolve Issues

Since FHIR Schema validation engine is more reliable and validates all the FHIR container cases, we need to validate existing data with FHIR Schema. To do it use [Aidbox Asynchronous Validation API](../../profiling-and-validation/validation-api.md).

### Check deprecated capabilities

```
GET /deprecated/capabilities
```

If you have total 0 for each section, your migration is done. You can delete old Entity & Attributes and Zen configuration.

### Verification Steps

After completing the migration:

1. Verify API functionality:

```http
GET /fhir/Patient?_summary=count
```

2. Test search parameters:

```http
GET /fhir/Patient?custom-param=value
```

3. Validate that resource creation and updates work properly:

```http
POST /fhir/Patient
Content-Type: application/json
{
  "resourceType": "Patient",
  "name": [
    {
      "given": ["Test"],
      "family": "Patient"
    }
  ]
}
```
