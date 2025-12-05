---
description: Create local CodeSystems and ValueSets for FHIR resource validation with multiple canonical resource versions.
---

# Local Terminology

## Objectives

* Create a local CodeSystem and ValueSet for use in the resource validation.&#x20;
* Explore how multiple versions of canonical resources can coexist on the same FHIR server.

## Before you begin

* Set up your local Aidbox instance by following the Getting Started [guide](../../getting-started/run-aidbox-locally.md). It will make sure that the Aidbox version is  `2507`  or higher.
*   The following settings in the  `docker-compose.yaml` enable the hybrid terminology engine.\


    ```yaml
    aidbox:
        ...
        environment:
          ....
          BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
          BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
    ```



You can learn more about these configuration options [here](../../reference/all-settings.md#terminology).

## Creating and Uploading a FHIR package

We will create a FHIR Package that includes the following components:

* A **CodeSystem** containing three custom codes
* A **ValueSet** that includes all conceps from the **CodeSystem**
* A **profile** for the **ServiceRequest** resource that binds the _ServiceRequest.code_ element to the **ValueSet** and enforces this binding as **required**.\


1. Create the folder structure for your FHIR package using the following command:\
   `mkdir -p mypackage/input/resources`
2.  Create  the JSON file with the **CodeSystem** FHIR resource at the path: `mypackage/input/resources/CodeSystem-tutorial-service-codes.json`\
    Add the following content to the file:\


    ```json
    {
      "resourceType": "CodeSystem",
      "id": "tutorial-service-codes",
      "url": "http://example.com/fhir/CodeSystem/tutorial-service-codes",
      "version": "1.0.0",
      "name": "TutorialServiceCodes",
      "title": "Tutorial Service Codes",
      "status": "active",
      "experimental": false,
      "date": "2024-01-01",
      "publisher": "Tutorial Example",
      "description": "A sample code system for tutorial purposes containing service request codes",
      "caseSensitive": true,
      "content": "complete",
      "count": 3,
      "concept": [
        {
          "code": "consultation",
          "display": "Medical Consultation",
          "definition": "A general medical consultation service"
        },
        {
          "code": "lab-test",
          "display": "Laboratory Test",
          "definition": "Laboratory testing service"
        },
        {
          "code": "imaging",
          "display": "Medical Imaging",
          "definition": "Medical imaging service including X-ray, MRI, CT scan"
        }
      ]
    }
    ```
3.  Create the JSON file with the **ValueSet** FHIR resource at the path: \
    `mypackage/input/resources/ValueSet-tutorial-service-codes.json`\
    Add the following content to the file:\


    ```json
    {
      "resourceType": "ValueSet",
      "id": "tutorial-service-codes",
      "url": "http://example.com/fhir/ValueSet/tutorial-service-codes",
      "version": "1.0.0",
      "name": "TutorialServiceCodes",
      "title": "Tutorial Service Codes ValueSet",
      "status": "active",
      "experimental": false,
      "date": "2024-01-01",
      "publisher": "Tutorial Example",
      "description": "ValueSet containing codes for service request types in tutorial examples",
      "compose": {
        "include": [
          {
            "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
          }
        ]
      }
    }
    ```
4.  Create the JSON file with the FHIR profile at the path:\
    `mypackage/input/resources/StructureDefinition-tutorial-service-request.json`\
    Add the following content to the file:\


    ```json
    {
      "resourceType": "StructureDefinition",
      "id": "tutorial-service-request",
      "url": "http://example.com/fhir/StructureDefinition/tutorial-service-request",
      "version": "1.0.0",
      "name": "TutorialServiceRequest",
      "title": "Tutorial Service Request Profile",
      "status": "active",
      "experimental": false,
      "date": "2024-01-01",
      "publisher": "Tutorial Example",
      "description": "A ServiceRequest profile for tutorial purposes with required binding to tutorial service codes",
      "fhirVersion": "4.0.1",
      "kind": "resource",
      "abstract": false,
      "type": "ServiceRequest",
      "baseDefinition": "http://hl7.org/fhir/StructureDefinition/ServiceRequest",
      "derivation": "constraint",
      "differential": {
        "element": [
          {
            "id": "ServiceRequest",
            "path": "ServiceRequest",
            "short": "Tutorial Service Request",
            "definition": "A service request with required binding to tutorial service codes"
          },
          {
            "id": "ServiceRequest.code",
            "path": "ServiceRequest.code",
            "short": "Service code from tutorial ValueSet",
            "definition": "The code for the service being requested, must be from the tutorial service codes ValueSet",
            "min": 1,
            "binding": {
              "strength": "required",
              "valueSet": "http://example.com/fhir/ValueSet/tutorial-service-codes"
            }
          }
        ]
      }
    }
    ```

    \

5.  Create the `mypackage/package.json` file:\


    ```json
    {
      "name": "tutorial-fhir-package",
      "version": "1.0.0",
      "description": "A tutorial FHIR package with ServiceRequest profile and required bindings",
      "fhirVersions": ["4.0.1"],
      "dependencies": {
        "hl7.fhir.r4.core": "4.0.1"
      },
      "author": "Tutorial Example",
      "license": "MIT"
    }
    ```
6.  At this point, the `mypackage` directory structure should look like this:\


    ```
    mypackage
    ├── input
    │   └── resources
    │       ├── CodeSystem-tutorial-service-codes.json
    │       ├── StructureDefinition-tutorial-service-request.json
    │       └── ValueSet-tutorial-service-codes.json
    └── package.json
    ```
7.  Create `tar.gz` file for FHIR package using the following command:\


    ```bash
    cd mypackage && tar -czf ../tutorial-fhir-package-1.0.0.tgz .
    ```
8. Open [Aidbox UI](http://localhost:8080/) and go to the _**FAR**_ tab. Click the "**Import package**" button, select the `tutorial-fhir-package-1.0.0.tgz` file, and then click **Import.**\


<figure><img src="../../.gitbook/assets/image (4).png" alt="Import FHIR package dialog in Aidbox UI"><figcaption></figcaption></figure>

At this point, you should be able to see the loaded package in the packages list. \


<figure><img src="../../.gitbook/assets/image (5).png" alt="Imported packages list in Aidbox UI"><figcaption></figcaption></figure>

## Testing the local terminology

1.  Navigate to **Aidbox UI -> REST Console** and create a patient with id `pt-1`\


    ```json
    POST /fhir/Patient
    content-type: application/json
    accept: application/json

    {
      "resourceType": "Patient",
      "id": "pt-1"
    }
    ```
2.  Create a **ServiceRequest** that conforms to the profile included in the imported package.\


    ```json
    POST /fhir/ServiceRequest
    content-type: application/json
    accept: application/json

    {
      "meta": {
        "profile": [
          "http://example.com/fhir/StructureDefinition/tutorial-service-request"
        ]
      },
      "status": "active",
      "intent": "order",
      "code": {
        "coding": [
          {
            "code": "consultation",
            "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
          }
        ]
      },
      "requester": {
        "reference": "Patient/pt-1"
      },
      "subject": {
        "reference": "Patient/pt-1"
      },
      "identifier": [
        {
          "value": "123"
        }
      ],
      "authoredOn": "2024-01-01"
    }
    ```

    \
    **Note:**   `meta.profile` property references the imported profile:\


    ```json
    "meta": {
        "profile": [
          "http://example.com/fhir/StructureDefinition/tutorial-service-request"
        ]
      }
    ```

    This informs the FHIR server to validate the resource against the specified profile when it is created.\
    \
    **Note:** `code.coding` contains a valid code from the imported code system:\


    ```json
    "code": {
        "coding": [
          {
            "code": "consultation",
            "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
          }
        ]
      }
    ```
3.  Create an invalid Service Request by executing the following request:\


    ```json
    POST /fhir/ServiceRequest
    content-type: application/json
    accept: application/json

    {
      "meta": {
        "profile": [
          "http://example.com/fhir/StructureDefinition/tutorial-service-request"
        ]
      },
      "status": "active",
      "intent": "order",
      "code": {
        "coding": [
          {
            "code": "follow-up",
            "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
          }
        ]
      },
      "requester": {
        "reference": "Patient/pt-1"
      },
      "subject": {
        "reference": "Patient/pt-1"
      },
      "identifier": [
        {
          "value": "123"
        }
      ],
      "authoredOn": "2024-01-01"
    }
    ```

You will see the following error:

```
The provided code 'http://example.com/fhir/CodeSystem/tutorial-service-codes#follow-up' was not found in the value set 'http://example.com/fhir/ValueSet/tutorial-service-codes|1.0.0
```

This is expected, as the CodeSystem `http://example.com/fhir/CodeSystem/tutorial-service-codes` does not include the code `follow-up`.

## Adding new code to the CodeSystem

Suppose we want to add a new code to the CodeSystem that was previously imported as part of a FHIR package. We can do this without creating a new version of the package or reloading it into the FHIR server.\
The ValueSet references the CodeSystem without specifying an exact version:

```json
"compose": {
    "include": [
      {
        "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
      }
    ]
  }
```

This means Aidbox will use the latest available version of the CodeSystem in the system when validating codes. You can read more about versioning of canonical resources [here](../../artifact-registry/artifact-registry-overview.md#versioning-strategy).\


1. Create a new version of the **CodeSystem** by executing the request below.\
   **Note:** The version has been incremented to `1.0.1` and a new code `follow-up` has been added.\


```json
PUT /fhir/CodeSystem/tutorial-service-codes
content-type: application/json
accept: application/json

{
  "resourceType": "CodeSystem",
  "id": "tutorial-service-codes",
  "url": "http://example.com/fhir/CodeSystem/tutorial-service-codes",
  "version": "1.0.1",
  "name": "TutorialServiceCodes",
  "title": "Tutorial Service Codes",
  "status": "active",
  "experimental": false,
  "date": "2024-01-01",
  "publisher": "Tutorial Example",
  "description": "A sample code system for tutorial purposes containing service request codes",
  "caseSensitive": true,
  "content": "complete",
  "count": 4,
  "concept": [
    {
      "code": "consultation",
      "display": "Medical Consultation",
      "definition": "A general medical consultation service"
    },
    {
      "code": "lab-test",
      "display": "Laboratory Test",
      "definition": "Laboratory testing service"
    },
    {
      "code": "imaging",
      "display": "Medical Imaging",
      "definition": "Medical imaging service including X-ray, MRI, CT scan"
    },
    {
      "code": "follow-up",
      "display": "Follow-up Appointment",
      "definition": "A follow-up appointment service for ongoing care"
    }
  ]
}
```

\
**Note:**  it's not possible to update the content of an already loaded package in Aidbox. The new version of the CodeSystem was created in the default `app.aidbox.main` package.

2. Verify that the request to create  a follow-up ServiceRequest can now be executed successfully:\


```json
POST /fhir/ServiceRequest
content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://example.com/fhir/StructureDefinition/tutorial-service-request"
    ]
  },
  "status": "active",
  "intent": "order",
  "code": {
    "coding": [
      {
        "code": "follow-up",
        "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
      }
    ]
  },
  "requester": {
    "reference": "Patient/pt-1"
  },
  "subject": {
    "reference": "Patient/pt-1"
  },
  "identifier": [
    {
      "value": "123"
    }
  ],
  "authoredOn": "2024-01-01"
}
```

\


&#x20;
