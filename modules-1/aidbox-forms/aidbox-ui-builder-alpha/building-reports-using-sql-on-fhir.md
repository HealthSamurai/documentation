---
description: >-
  This article provides a step-by-step guide to building a report using Aidbox
  Forms and SQL on FHIR.
---

# Building reports using SQL on FHIR

## Overview

The SQL on FHIR specification allows users to create flat views of their resources in a simple, straightforward way and use it for performing analysis or building reports.

&#x20;In our case, we will focus on the QuestionnaireResponse that references the Vitals Signs Record Form to show how it works.

Example of the Vitals Signs Record Form (Questionnaire):

```json
// {
  "title": "Vitals Sign Test Form",
  "id": "ed0ac227-19d1-41f7-960c-bab88f9a84da",
  "status": "draft",
  "url": "http://forms.aidbox.io/questionnaire/vitals-sign-test-form",
  "meta": {
    "lastUpdated": "2024-08-07T14:09:56.567202Z",
    "versionId": "310",
    "extension": [
      {
        "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
        "valueInstant": "2024-08-07T13:53:26.150467Z"
      }
    ]
  },
  "resourceType": "Questionnaire",
  "item": [
    {
      "linkId": "group_1",
      "item": [
        {
          "text": "Visit Date",
          "type": "date",
          "linkId": "vd"
        },
        {
          "text": "Blood Pressure",
          "type": "string",
          "linkId": "blood_pr"
        },
        {
          "text": "Temperature ",
          "type": "quantity",
          "linkId": "tempr",
          "extension": [
            {
              "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-unitOption",
              "valueCoding": {
                "display": "C"
              }
            }
          ]
        }
      ],
      "repeats": true,
      "extension": [
        {
          "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl",
          "valueCodeableConcept": {
            "coding": [
              {
                "code": "gtable"
              }
            ]
          }
        }
      ],
      "type": "group"
    }
  ]
} 
```



Example of the completed Vitals Signs Record Form (QuestionnaireResponse):

```json
// {
 "item": [
  {
   "item": [
    {
     "text": "Visit Date",
     "answer": [
      {
       "valueDate": "2024-08-05"
      }
     ],
     "linkId": "vd"
    },
    {
     "text": "Blood Pressure",
     "answer": [
      {
       "valueString": "120/80"
      }
     ],
     "linkId": "blood_pr"
    },
    {
     "text": "Temperature ",
     "answer": [
      {
       "valueQuantity": {
        "unit": "C",
        "value": 37
       }
      }
     ],
     "linkId": "tempr"
    }
   ],
   "linkId": "group_1"
  },
  {
   "item": [
    {
     "text": "Visit Date",
     "answer": [
      {
       "valueDate": "2024-08-06"
      }
     ],
     "linkId": "vd"
    },
    {
     "text": "Blood Pressure",
     "answer": [
      {
       "valueString": "110/70"
      }
     ],
     "linkId": "blood_pr"
    },
    {
     "text": "Temperature ",
     "answer": [
      {
       "valueQuantity": {
        "unit": "C",
        "value": 37
       }
      }
     ],
     "linkId": "tempr"
    }
   ],
   "linkId": "group_1"
  },
  {
   "item": [
    {
     "text": "Visit Date",
     "answer": [
      {
       "valueDate": "2024-08-07"
      }
     ],
     "linkId": "vd"
    },
    {
     "text": "Blood Pressure",
     "answer": [
      {
       "valueString": "100/60"
      }
     ],
     "linkId": "blood_pr"
    },
    {
     "text": "Temperature ",
     "answer": [
      {
       "valueQuantity": {
        "unit": "C",
        "value": 37
       }
      }
     ],
     "linkId": "tempr"
    }
   ],
   "linkId": "group_1"
  }
 ],
 "status": "in-progress",
 "subject": {
  "reference": "Patient/morgan"
 },
 "questionnaire": "http://forms.aidbox.io/questionnaire/vitals-sign-test-form",
 "id": "[75177295-2edf-45f7-b57e-0b1a0fd71fb7](https://forms220724.edge.aidbox.app/ui/console#/rest?req=GET%20/QuestionnaireResponse/75177295-2edf-45f7-b57e-0b1a0fd71fb7)",
 "resourceType": "QuestionnaireResponse",
 "meta": {
  "lastUpdated": "2024-08-07T14:12:51.846100Z",
  "versionId": "320",
  "extension": [
   {
    "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
    "valueInstant": "2024-08-07T14:11:58.571600Z"
   }
  ]
 }
}
```

### Incoming Data

* Vitals Sign Test Form -  url http://forms.aidbox.io/questionnaire/vitals-sign-test-form
* Data Fields in QuestionnaireResponse:
  * Visit Date - linkID: `vd`
  * Blood Pressure - linkID: `blood_pr`
  * Group containing these fields - linkID: `group_1`
* Patient id - `morgan`

### Purpose

To build a report of how blood pressure changes from visit to visit for a specific patient.

## Flow

1. Create ViewDefinition for QuestionnaireResponse of Vitals Signs Test Form using ViewDefinition Designer
2. POST ViewDefinition resource in Aidbox
3. Create AidboxQuery for this ViewDefinition
4. Use `$query` endpoint to get the required data for reporting

#### **1. Create ViewDefinition for QuestionnaireResponse of Vitals Signs Test Form using ViewDefinition Designer**

SQL on FHIR utilizes ViewDefinition resources to describe the structure of flat views. To create a ViewDefinition resource for a specific QuestionnaireResponse follow these steps:

1. Sign in to [the ViewDefinition Designer](https://sqlonfhir.aidbox.app)&#x20;
2. Connect to your current instance on the Settings page (if your instance is in Aidbox cloud)
3. Go to the View Definition page and click on the **+ViewDefinition** button to create a new resource or import an existing resource.

Example of ViewDefinition for the QuestionnaireResponses of Vitals Signs Test Form in which we filter data of two fields:

```json
// {
  "name": "vs_test",
  "where": [
    {
      "path": "questionnaire = 'http://forms.aidbox.io/questionnaire/vitals-sign-test-form'"
    }
  ],
  "select": [
    {
      "select": [
        {
          "column": [
            {
              "name": "date",
              "path": "item.where(linkId='vd').answer.value.ofType(date)"
            },
            {
              "name": "blood_pressure",
              "path": "item.where(linkId='blood_pr').answer.value.ofType(string)"
            }
          ]
        }
      ],
      "forEach": "item.where(linkId='group_1')"
    },
    {
      "column": [
        {
          "name": "pid",
          "path": "subject.id"
        }
      ]
    }
  ],
  "status": "unknown",
  "resource": "QuestionnaireResponse",
  "id": "c8153197-79de-4555-a2f3-3d08df3cec24",
  "resourceType": "ViewDefinition",
  "meta": {
    "lastUpdated": "2024-08-07T15:22:56.659712Z",
    "createdAt": "2024-08-07T14:37:54.879920Z",
    "versionId": "341"
  }
}
```

Once you have created and tested the new resource (ViewDefinition), click the save button and the resource will be saved to your Aidbox instance.

#### 2. POST ViewDefinition resource in Aidbox&#x20;

There are two options for saving the ViewDefinition resource in the Aidbox instance:

* Click the save button on View Definition Designer to save the resource to your Aidbox instance connected to View Definition Designer.
* Copy the JSON file from View Definition Designer and POST it in Aidbox.

Once saved, the corresponding flat view will be created in the database in the `sof` schema (e.g., `sof.vs_test`).

#### **3. Create AidboxQuery for this ViewDefinition**

Create an AidboxQuery resource for the ViewDefinition parameterized by a patient to turn your SQL query into a REST Endpoint.

Example:

```yaml
// PUT /AidboxQuery/vs_test
content-type: text/yaml
accept: text/yaml

params:
  pid:
    isRequired: true
    type: string
query: 'select * from sof.vs_test where pid = {{params.pid}}'
```

#### **4. Use `$query` endpoint to get the required data**

After creating the AidboxQuery, you can use it to fetch the data.

Example:

```
GET /$query/vs_test?pid=morgan
```

Where&#x20;

pid - patient id&#x20;

vs\_test - AidboxQuery id



This approach allows you to flatten nested FHIR resources (QuestionnaireResponse) and use them to build reports and analytics more easily.
