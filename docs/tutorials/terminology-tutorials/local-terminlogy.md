# Local Terminlogy

## Objectives

* Create a local CodeSystem and ValueSet for use in the resource validation.&#x20;
* Explore how canonical resources with different versions exist in the same FHIR Server

## Before you begin

* Set up the local Aidbox instance using the Getting Started [guide](../../getting-started/run-aidbox-locally.md). It will make sure that the Aidbox version is greater than or equal to `2507`
*   Add the following environment variables to the `docker-compose.yaml` file for the aidbox.environment section:\


    ```yaml
    aidbox:
        ...
        environment:
          ....
          FHIR_TERMINOLOGY_ENGINE: hybrid
          FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
    ```



You can read about those settings [here](../../reference/settings/fhir.md#terminology).

## Creating and Uploading a FHIR package

Let's create the FHIR Package that contains:

* **CodeSystem** with three codes
* **ValueSet** that includes all the content from the **CodeSystem**
* Profile for the **ServideRequest** resource that binds the ServiceRequest.code element to the **ValueSet** and makes this binding required.\


1. Create a folder structure for the FHIR package with the following command\
   `mkdir -p mypackage/input/resources`
2.  Create  the JSON file with the CodeSystem FHIR resource: `mypackage/input/resources/CodeSystem-tutorial-service-codes.json`\
    with the following content:\


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
3.  Create the JSON file with the ValueSet FHIR resource: \
    `mypackage/input/resources/ValueSet-tutorial-service-codes.json`\
    with the following content:\


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
4.  Create the JSON file with the  FHIR profile:\
    `mypackage/input/resources/StructureDefinition-tutorial-service-request.json`\
    with the following content:\


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
      "mapping": [
        {
          "identity": "workflow",
          "uri": "http://hl7.org/fhir/workflow",
          "name": "Workflow Pattern"
        }
      ],
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

5.  Create the mypackage/package.json file:\


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
6.  The folder structure at this point should look like this:\


    ```
    ├── input
    │   └── resources
    │       ├── CodeSystem-tutorial-service-codes.json
    │       ├── StructureDefinition-tutorial-service-request.json
    │       └── ValueSet-tutorial-service-codes.json
    └── package.json
    ```
7.  Create tar.gz file for FHIR package with the following command:\


    ```bash
    cd mypackage && tar -czf ../tutorial-fhir-package-1.0.0.tgz .
    ```
8. Open [Aidbox UI](http://localhost:8080/) and navigate to the "FAR" tab. Click "**Import package**" button. Select the `tutorial-fhir-package-1.0.0.tgz` file and click **Import**\


<figure><img src="../../.gitbook/assets/image (4).png" alt=""><figcaption></figcaption></figure>

At this point, you should be able to see the loaded package in the packages list. \


<figure><img src="../../.gitbook/assets/image (5).png" alt=""><figcaption></figcaption></figure>

## Testing the local terminology

1.  Navigate to **Aidbox UI -> REST Console** and  create the patient with id `pt-1`\


    ```json
    POST /fhir/Patient
    content-type: application/json
    accept: application/json

    {
      "resourceType": "Patient",
      "id": "pt-1"
    }
    ```
2.  Create a ServiceRequest that is valid against the profile we imported with the package.\


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
    The `meta.profile` property contains the profile we imported and the `code.coding` contains a valid code from the code system we imported.
3.  Create the invalid Service Request\
    Execute the following request:\


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

You will see the error:

```
The provided code 'http://example.com/fhir/CodeSystem/tutorial-service-codes#follow-up' was not found in the value set 'http://example.com/fhir/ValueSet/tutorial-service-codes|1.0.0
```

It's expected because the CodeSystem `http://example.com/fhir/CodeSystem/tutorial-service-codes` doesn't contain the code `follow-up`

## Adding new code to the CodeSystem

Let's say we want to add a new code to the CodeSystem we imported in a package. We can do it without creating a new version of the FHIR package and reloading it to the FHIR Server. \
ValueSet references the CodeSystem without mentioning the exact code system version:

```json
"compose": {
    "include": [
      {
        "system": "http://example.com/fhir/CodeSystem/tutorial-service-codes"
      }
    ]
  }
```

Which means the system will look to the latest version of the CodeSystem available in the FHIR server when validating the actual code. You can read more about versioning for canonical resources [here](../../artifact-registry/artifact-registry-overview.md#versioning-strategy).\


1.  Create the new version of the **CodeSystem** by executing the request below\
    Not that we increased the version to `1.0.1` and added a `follow-up` code.\


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
    Note that it's not possible to update the content of the package loaded in Aidbox - the new version of the CodeSystem was created in the default - `app.aidbox.main` package.
2.  Verify that the request for creating a follow-up ServiceRequest can now be executed successfully:\


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
