---
description: >-
  This guide explains changes in the ViewDefinition resource and explains how to
  migrate to the updated structure
---

# Migrate to the spec-compliant ViewDefinition format

Starting from January 2024 release Aidbox supports SQL on FHIR specification-compliant `ViewDefinition` resource structure. This upgrade comes with a few minor breaking changes.

{% hint style="info" %}
These changes won't change anything in flat views you've created previously: you should be able to keep using them from your SQL requests as you did before. However, should you try to alter them via ViewDefinition, you'll have to upgrade it to the new format.
{% endhint %}

### Renames

* `alias` field of the column changed to `name`.
* `union` changed to `unionAll`.
* `constants` changed to `constant`.
* `from` is replaced with `forEach`.

### New field: column

`column` is a new field in `select`, made to extract column declarations from it for better readability and less nesting. You need to move your column-related information to it.

For example, this ViewDefinition:

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

should be modified into this:

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

If you have a `select` with `forEach` (or `forEachOrNull`), keep `forEach` outside of the `column`. Nested `select` with no `forEach` inside can be safely renamed to `column`.

For example, this ViewDefinition:

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

should be modified into this:

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
