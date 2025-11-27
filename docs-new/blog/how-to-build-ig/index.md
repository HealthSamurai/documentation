---
title: "How to build FHIR IG"
slug: "how-to-build-ig"
published: "2025-04-10"
author: "Ivan Bagrov"
reading-time: "4 min "
tags: []
category: "FHIR"
teaser: "FHIR IG (Implementation Guide) is a set of artifacts that describe the requirements for a FHIR server. For example, an IG may contain a set of rules, profiles, extensions, search parameters, and examples of FHIR resources."
image: "cover.png"
---

## FHIR IG development. How to create IG

FHIR IG (Implementation Guide) is a set of artifacts that describe the requirements for a [FHIR server](https://www.health-samurai.io/fhir-server). For example, an IG may contain a set of rules, profiles, extensions, search parameters, and examples of FHIR resources.

The IG is described as an NPM package with the following structure:

- package (subfolder)   
  - package.json (package manifest)
  - A set of canonical resource files …

**Package Manifest**

To start creating IG, we need to describe the package manifest file. A package manifest is a JSON file called package.json.

```javascript
{
	// The globally unique identifier of the package.
	// A package name consists of two or more namespaces separated by a dot.
	// Each namespace starts with a lowercase alphabet character
	"name": "myorganization.mypackage",

	// The version of the package (SemVer).
	// SHALL contain only letters, numbers, and the characters ".", "_", and "-"
	"version": "1.0.0",

	// The description of the package.
	"description": "My FHIR NPM Package",

	// The author of the package.
	"author": "Author Name",

	// The list of package dependencies.
	// Should contain at least one FHIR core package.
	"dependencies": {
    	  "hl7.fhir.r4.core": "4.0.1",
    	  "hl7.fhir.us.core": "5.0.1"
	}
}
```

## 2 Package Content

A package contains a set of FHIR resources in the JSON format. Let's describe a few resources for the package.

### 2.1 Profile

Let's describe a profile of the US Core Patient in the file *StructureDefinition-my-patient-profile.json.*

```javascript
{
    // Type of the FHIR resource.
    "resourceType": "StructureDefinition",
    
    // How the type relates to the baseDefinition.
    "derivation": "constraint",
    
    // Defines the kind of structure that this definition is describing.
    "kind": "resource",
    
    // Type constrained by this structure.
    "type": "Patient",
    
    // The status of this structure definition.
    "status": "active",
    
    // Name for this structure definition (computer friendly).
    "name": "my-patient-profile",
    
    // 	Whether the structure is abstract.
    "abstract": false,
    
    //The Canonical identifier for this structure definition is represented as a URI (globally unique).
    "url": "http://custom-url/my-patient-profile",
    
    // Definition from which this type is constrained.
    "baseDefinition": "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient",
    
    "differential": {
        "element": [
            {
                // Element ID
                "id": "Patient.address",
                
                // Path of the element in the hierarchy of elements
                "path": "Patient.address",
                
                // The minimum number of times this element SHALL appear in the instance.
                "min": 1
            }
        ]
    }
}
```

We have described a profile that inherits from the US Core Patient and adds a required field for address.

### 2.2 SearchParameter resource

Let's describe a custom search parameter on the Patient resource in the file *SearchParamete-my-patient-search.json.*

```javascript

{
	// Type of the FHIR resource.
	"resourceType": "SearchParameter",
    
	// ID of the search parameter.
	"id": "Patient-myname",
    
	// Canonical identifier for this search parameter, represented as a URI (globally unique).
	"url": "http://hl7.org/fhir/SearchParameter/Patient-myname",
    
	// Business version of the search parameter.
	"version": "4.0.1",
    
	// Name for this search parameter (computer friendly).
	"name": "myname",
    
	// Natural language description of the search parameter.
	"description": "text",
    
	// The resource type(s) this search parameter applies to.
	"base": ["Patient"],
    
	// Status of the search parameter.
	"status": "active",
    
	// Recommended name for the parameter in search url.
	"code": "myname",
    
	// 	The type of value that a search parameter may contain, and how the content is interpreted.
	"type": "string",
    
	//FHIRPath expression that extracts the values.
	"expression": "Patient.name"
}
```

**Build NPM package**

The resulting npm package has the following structure:

```javascript
package
├── StructureDefinition-my-patient-profile.json
├── SearchParameter-my-patient-search.json
└── package.json
```

We need to build the artifacts of the npm package into an archive in the TAR (.tar.gz) format.

```javascript
tar -czvf package.tar.gz package
```

Following [the guide](https://docs.aidbox.app/readme-1/validation-tutorials/upload-fhir-implementation-guide/aidbox-ui/local-ig-package), upload the archive to Aidbox.

> [Reach out to us](https://www.health-samurai.io/contacts) with your use cases and requirements or get started with Aidbox topic-based subscriptions for free with [our development licenses](https://aidbox.app/ui/portal#/signin).
