---
title: "How to upload StructureDefinition into Aidbox via FHIR API"
slug: "upload-structuredefinition"
published: "2025-04-18"
author: "Ivan Bagrov"
reading-time: "3 min "
tags: []
category: "FHIR"
teaser: "StructureDefinition is a resource that describes the structure of other resources, including profiles, extensions, and data types."
image: "cover.png"
---

StructureDefinition is a resource that describes the structure of other resources, including profiles, extensions, and data types.

Aidbox allows you to create StructureDefinition resources at runtime using the FHIR API. This enables you to define profiles for standard resources, create extensions, and design custom user-defined resources.

## Request body example for creating an profile via StructureDefinition

In this example, a profile is created for the Patient resource, where the name field is marked as required. This can be done via a REST request to the FHIR endpoint for StructureDefinition, with the profile definition included in the body of the request.

```javascript
POST /fhir/StructureDefinition
content-type: application/json
accept: application/json

{
	"derivation": "constraint",
	"name": "my-patient-profile",
	"abstract": false,
	"type": "Patient",
	"status": "active",
	"kind": "resource",
	"url": "http://example.org/StructureDefinition/my-patient-profile",
	"baseDefinition": "http://hl7.org/fhir/StructureDefinition/Patient",
	"differential": {
    	"element": [
        	{
            	"id": "Patient.name",
            	"path": "Patient.name",
            	"min": 1
        	}
    	]
	}
}
```

## Request body example for creating an extension via StructureDefinition

An extension named insurance-plan is created to allow referencing a related insurance plan (the InsurancePlan resource).

```javascript
POST /fhir/StructureDefinition

{
  "abstract": false,
  "url": "http://my.company/StructureDefinition/insurance-plan",
  "id": "insurance-plan",
  "name": "InsurancePlanReference",
  "status": "active",
  "kind": "complex-type",
  "type": "Extension",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Extension",
  "version": "0.0.1",
  "differential": {
	"element": [
  	{
    	"id": "Extension",
    	"max": "1",
    	"min": 0,
    	"path": "Extension",
    	"short": "Reference to the insurance plan"
  	},
  	{
    	"id": "Extension.url",
    	"max": "1",
    	"min": 1,
    	"path": "Extension.url",
    	"fixedUri": "http://my.company/StructureDefinition/insurance-plan"
  	},
  	{
    	"id": "Extension.value[x]",
    	"max": "1",
    	"min": 1,
    	"path": "Extension.value[x]",
    	"type": [
      	{
        	"code": "Reference",
        	"targetProfile": [
          	"http://hl7.org/fhir/StructureDefinition/InsurancePlan"
        	]
      	}
    	]
  	}
	]
  },
  "resourceType": "StructureDefinition",
  "derivation": "constraint"
}
```

## Request body example for creating a custom resource via StructureDefinition

In this example, a custom resource named TutorNotificationTemplate is created, which contains a single required field template of type string.

```javascript
POST /fhir/StructureDefinition
content-type: application/json
accept: application/json

{
	"derivation": "specialization",
	"name": "TutorNotificationTemplate",
	"abstract": false,
	"type": "TutorNotificationTemplate",
	"status": "active",
	"kind": "resource",
	"url": "http://example.org/StructureDefinition/TutorNotificationTemplate",
	"baseDefinition": "http://hl7.org/fhir/StructureDefinition/DomainResource",
	"differential": {
    	"element": [
        	{
            	"id": "TutorNotificationTemplate",
            	"path": "TutorNotificationTemplate",
            	"min": 0,
            	"max": "*"
        	},
        	{
            	"id": "TutorNotificationTemplate.template",
            	"path": "TutorNotificationTemplate.template",
            	"min": 1,
            	"max": "1",
            	"type": [
                	{
                    	"code": "string"
                	}
            	]
        	}
    	]
	}
}
```

You can use these examples as a starting point for creating your own profiles, extensions, and custom resources in [Aidbox FHIR server](https://www.health-samurai.io/fhir-server). For more advanced scenarios, refer to the official Aidbox and FHIR [documentation](https://docs.aidbox.app/).
