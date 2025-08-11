# SMART: Symmetric (/"client Secret") Authentication

This page describes SMART’s `client-confidential-symmetric` authentication mechanism. It is intended for SMART App Launch clients that can maintain a secret but cannot manage asymmetric keypairs. For clients that can manage asymmetric keypairs, [Asymmetric Authentication](./smart-asymmetric-docs-private-key-jwt-authentication.md) is preferred. This profile is not intended for SMART Backend Services clients.

## Register Client

```json
PUT /Client/client-confidential-symmetric
content-type: application/json
accept: application/json

{
  "id": "client-confidential-symmetric",
  "secret": "secret",
  "grant_types": [
    "basic"
  ]
}


```

## Create AccessPolicy

```json
PUT /AccessPolicy/client-confidential-symmetric-allow
content-type: application/json
accept: application/json

{
  "id": "client-confidential-symmetric-allow",
  "engine": "allow",
  "link": [
    {
      "resourceType": "Client",
      "id": "client-confidential-symmetric"
    }
  ]
}
```

## Access FHIR API

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/Observation?code=4548-4&_count=2
content-type: application/json
accept: application/json
authorization: "Basic Y2xpZW50LWNvbmZpZGVudGlhbC1zeW1tZXRyaWM6c2VjcmV0"
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK

{
 "resourceType": "Bundle",
 "type": "searchset",
 "entry": [
  {
   "resource": {
    "category": [
     {
      "coding": [
       {
        "code": "laboratory",
        "system": "http://terminology.hl7.org/CodeSystem/observation-category",
        "display": "laboratory"
       }
      ]
     }
    ],
    "meta": {
     "lastUpdated": "2024-08-29T15:51:05.117806Z",
     "versionId": "74",
     "extension": [
      {
       "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
       "valueInstant": "2024-08-29T15:51:05.117806Z"
      }
     ]
    },
    "encounter": {
     "reference": "Encounter/67b8fa04-6e1b-4074-8b8c-3ec44bfec48f"
    },
    "valueQuantity": {
     "code": "%",
     "unit": "%",
     "value": 2.856519918445372,
     "system": "http://unitsofmeasure.org"
    },
    "resourceType": "Observation",
    "effectiveDateTime": "2014-05-11T12:39:55+04:00",
    "status": "final",
    "id": "00592410-ec4a-4d64-a674-f0bfb244a978",
    "code": {
     "text": "Hemoglobin A1c/Hemoglobin.total in Blood",
     "coding": [
      {
       "code": "4548-4",
       "system": "http://loinc.org",
       "display": "Hemoglobin A1c/Hemoglobin.total in Blood"
      }
     ]
    },
    "issued": "2014-05-11T12:39:55.513+04:00",
    "subject": {
     "reference": "Patient/a6a91d7e-7ded-4325-9dbe-42a088e7e039"
    }
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "https://releasetest.edge.aidbox.app/Observation/00592410-ec4a-4d64-a674-f0bfb244a978",
   "link": [
    {
     "relation": "self",
     "url": "https://releasetest.edge.aidbox.app/Observation/00592410-ec4a-4d64-a674-f0bfb244a978"
    }
   ]
  },
  {
   "resource": {
    "category": [
     {
      "coding": [
       {
        "code": "laboratory",
        "system": "http://terminology.hl7.org/CodeSystem/observation-category",
        "display": "laboratory"
       }
      ]
     }
    ],
    "meta": {
     "lastUpdated": "2024-08-29T15:51:05.117806Z",
     "versionId": "74",
     "extension": [
      {
       "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
       "valueInstant": "2024-08-29T15:51:05.117806Z"
      }
     ]
    },
    "encounter": {
     "reference": "Encounter/f1c8a70d-0dfa-47a6-b940-d441fdfd1323"
    },
    "valueQuantity": {
     "code": "%",
     "unit": "%",
     "value": 3.1257055258079536,
     "system": "http://unitsofmeasure.org"
    },
    "resourceType": "Observation",
    "effectiveDateTime": "2018-01-14T11:39:55+03:00",
    "status": "final",
    "id": "01e57d19-35b7-47d0-9c3b-29d14d16d3f5",
    "code": {
     "text": "Hemoglobin A1c/Hemoglobin.total in Blood",
     "coding": [
      {
       "code": "4548-4",
       "system": "http://loinc.org",
       "display": "Hemoglobin A1c/Hemoglobin.total in Blood"
      }
     ]
    },
    "issued": "2018-01-14T11:39:55.513+03:00",
    "subject": {
     "reference": "Patient/a6a91d7e-7ded-4325-9dbe-42a088e7e039"
    }
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "https://releasetest.edge.aidbox.app/Observation/01e57d19-35b7-47d0-9c3b-29d14d16d3f5",
   "link": [
    {
     "relation": "self",
     "url": "https://releasetest.edge.aidbox.app/Observation/01e57d19-35b7-47d0-9c3b-29d14d16d3f5"
    }
   ]
  }
 ]
}
```
{% endtab %}
{% endtabs %}

