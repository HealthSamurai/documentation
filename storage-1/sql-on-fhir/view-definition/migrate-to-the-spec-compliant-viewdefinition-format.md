---
description: >-
  This guide explains changes in the ViewDefinition resource and explains how to
  migrate to the updated structure
---

# Migrate to the spec-compliant ViewDefinition format

Aidbox's January 2024 release introduces an updated, SQL on FHIR spec-compliant `ViewDefinition` resource structure, including some minor breaking changes. These changes don't affect existing flat views, but alterations via `ViewDefinition` require adapting to the new format.

### Existing elements renames

* `alias` is now `name`
* `union` is now`unionAll`
* `constants` is now `constant`
* `from` is replaced with `forEach`

### New element: column

A `column` element is introduced in `select` for clearer, less nested column declarations. Move column-related details here.

#### Before:

```json
{
  "name": "patient_view",
  "resource": "Patient",
  "select": [
    {
      "alias": "id",
      "path": "id"
    },
    {
      "alias": "bod",
      "path": "birthDate"
    },
    {
      "alias": "gender",
      "path": "gender"
    }
  ]
}
```

#### After:

```json
{
  "name": "patient_view",
  "resource": "Patient",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        },
        {
           "name": "bod",
           "path": "birthDate"
        },
        {
           "name": "gender",
           "path": "gender"
        }
      ]
    }
  ]
}
```

For `select` containing `forEach`(or `forEachOrNull`), keep `forEach` outside the `column`. Nested `select` without `forEach` can directly be converted to `column`.

#### Before

```json
{
  "name": "unnesting",
  "description": "Element-related unnesting",
  "status": "draft",
  "resource": "Patient",
  "select": [
    {
      "alias": "id",
      "path": "id"
    },
    {
      "forEach": "name",
      "select": [
        {
          "alias": "family_name",
          "path": "family"
        },
        {
          "forEach": "given",
          "select": [
            {
              "alias": "given_name",
              "path": "$this"
            }
          ]
        }
      ]
    }
  ]
}
```

#### After

```json
{
  "name": "unnesting",
  "description": "Element-related unnesting",
  "status": "draft",
  "resource": "Patient",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        }
      ]
    },
    {
      "forEach": "name",
      "column": [
        {
          "name": "family_name",
          "path": "family"
        }
      ],
      "select": [
        {
          "forEach": "given",
          "column": [
            {
              "name": "given_name",
              "path": "$this"
            }
          ]
        }
      ]
    }
  ]
}
```
