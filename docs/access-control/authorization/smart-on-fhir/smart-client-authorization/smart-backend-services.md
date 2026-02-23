---
description: Enable backend services to access FHIR APIs using client credentials grant with JWT bearer authentication.
---

# SMART backend services

This specification is designed to work with FHIR Bulk Data Access, but is not restricted to use for retrieving bulk data; it may be used to connect to any FHIR API endpoint.

## Register a Client

Before a SMART client can run against a FHIR server, the client SHALL generate or obtain an asymmetric key pair and register its public key set as `jsks_uri` in Client resource. And you should expose an  `.well-known/jwks.json` endpoint available to Aidbox.

**Request:**

```json
PUT /Client/inferno-my-clinic-bulk-client
content-type: application/json
accept: application/json

{
  "type": "bulk-api-client",
  "active": true,
  "auth": {
    "client_credentials": {
      "client_assertion_types": [
        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
      ],
      "access_token_expiration": 300,
      "token_format": "jwt"
    }
  },
  "scope": [
    "system/*.read"
  ],
  "jwks_uri": "<Your-JWKS-server>/.well-known/jwks.json",
  "grant_types": [
    "client_credentials"
  ]
}
```

**Response:**

```json
// 201 OK

{
 "type": "bulk-api-client",
 "grant_types": [
  "client_credentials"
 ],
 "resourceType": "Client",
 "scope": [
  "system/*.read"
 ],
 "auth": {
  "client_credentials": {
   "client_assertion_types": [
    "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
   ],
   "access_token_expiration": 300,
   "token_format": "jwt"
  }
 },
 "active": true,
 "id": "",
 "jwks_uri": "http://localhost:8082/.well-known/jwks.json"
}
```

## Create AccessPolicy for the Client

**Request:**

```json
PUT /AccessPolicy/inferno-my-clinic-bulk-client
accept: application/json
content-type: application/json

{
  "engine": "allow",
  "link": [
    {
      "id": "inferno-my-clinic-bulk-client",
      "resourceType": "Client"
    }
  ]
}
```

**Response:**

```json
{
 "id": "inferno-my-clinic-bulk-client",
 "link": [
  {
   "id": "inferno-my-clinic-bulk-client",
   "resourceType": "Client"
  }
 ],
 "engine": "allow",
 "resourceType": "AccessPolicy"
}
```

## Obtain access token

To obtain an access token use `/auth/token`endpoint with following parameters:

<table><thead><tr><th width="293">Parametr</th><th>Description</th></tr></thead><tbody><tr><td><code>scope</code> *</td><td>String with scopes separated by space.</td></tr><tr><td><code>grant_type</code> *</td><td>Fixed value - <code>client_credentials</code></td></tr><tr><td><code>client_assertion_type</code> *</td><td>Fixed value - <code>urn:ietf:params:oauth:client-assertion-type:jwt-bearer</code></td></tr><tr><td><code>client_assertion</code> *</td><td>Signed authentication JWT value.</td></tr></tbody></table>

\*- required parameter

**Request:**

```http
POST /auth/token
Content-Type: application/x-www-form-urlencoded

client_assertion=eyJhbGciOiJSUzI1NiIsImtpZCI6ImNsaWVudC1rZXktMSIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJleGFtcGxlLXNtYXJ0LWNsaWVudCIsInN1YiI6ImV4YW1wbGUtc21hcnQtY2xpZW50IiwiYXVkIjoiaHR0cHM6Ly9hdXRoLmV4YW1wbGUuY29tL29hdXRoL3Rva2VuIiwianRpIjoiMTE2YWIxZmQtZjRhMS00NmQxLTg2NTktOWUxOWE1MDc5MjkxIiwiaWF0IjoxNzcxODY0NDYxLCJleHAiOjE3NzE4NjQ3NjF9.j6iQivVrvlvu2kb4ffEpQ_GY5xBIqcizkiv08dwoc9tXeFeEJkzg7KVAfUbkwU_cRWSqgF4999Uiw3G3XvlrHTe7R4nIaH46qgnKFI3s7izS99j09ebAv66NOcuay3901MyEaagdONSqRu63kpWuDxYWUEcCgPSTf5d_UEd6JEDEghitWMnPS0HS5VovmyZfNAcPXQ3bZ0S2pXSOxYxbim0jTa1arWK4-9DTIHtK2UE0-sDk07hS4mtmIp8AmHePkk7DwuxPukNvRF34Jvy9_IC-o2smxxoquLpaOdyra9RylKI5RWbJtafp0p619TmS5C-bIZNmh5qV1Ho5kuVlCQ&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&grant_type=client_credentials&scope=system/*.read
```

**Response:**

```json
{
  "token_type": "Bearer",
  "scope": "system/AllergyIntolerance.read system/CarePlan.read system/CareTeam.read system/Condition.read system/Device.read system/DiagnosticReport.read system/DocumentReference.read system/Encounter.read system/Goal.read system/Group.read system/Immunization.read system/Location.read system/MedicationRequest.read system/Observation.read system/Organization.read system/Patient.read system/Practitioner.read system/Procedure.read system/Provenance.read",
  "need_patient_banner": true,
  "expires_in": 300,
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1teS1jbGluaWMtYnVsay1jbGllbnQiLCJpYXQiOjE3MzQwMTAyNDMsImp0aSI6IjEzMzZhNmIyLTZiNGMtNDE0Yy04Mjk0LWJkYjA2OWE5OTE5MSIsImV4cCI6MTczNDAxMDU0M30.glqegvLKAoF5y2cJ7rUODTz6Ro0Lhu7vUr86vvvyrhKU0ADHVDkHbue-SMyy2HhHl0ZF4LMC_Vlu4Q_yv2WWUn4htQ3INYIeBuJ_pyFOonJ2mQNa82j6ZmqLrjZyGr_PlqAOdZGPfmDyudD_jbBVABf3wnAcvLxP5fIPZrAGL_AlHKA843LgKTqIbmRbugl_QvdBwRfQj2fIN4HZNIkfcOeQclw6yCrNSIZ5qSG0O_GDmfIjU942qhiJPppk1kI8G700BLJtLvTVDuC0fjqyobRlLetuAwbGuztBSD8EROsumU-I1tPdUP-LlAHhlY8oe9rFa0VZNi5V1mth_Yw0-A",
  "refresh_token": null
}
```

## Access FHIR API

**Request:**

```http
GET /fhir/Observation?code=4548-4&_count=2
content-type: application/json
accept: application/json
authorization: "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1teS1jbGluaWMtYnVsay1jbGllbnQiLCJpYXQiOjE3MzQwMTAyNDMsImp0aSI6IjEzMzZhNmIyLTZiNGMtNDE0Yy04Mjk0LWJkYjA2OWE5OTE5MSIsImV4cCI6MTczNDAxMDU0M30.glqegvLKAoF5y2cJ7rUODTz6Ro0Lhu7vUr86vvvyrhKU0ADHVDkHbue-SMyy2HhHl0ZF4LMC_Vlu4Q_yv2WWUn4htQ3INYIeBuJ_pyFOonJ2mQNa82j6ZmqLrjZyGr_PlqAOdZGPfmDyudD_jbBVABf3wnAcvLxP5fIPZrAGL_AlHKA843LgKTqIbmRbugl_QvdBwRfQj2fIN4HZNIkfcOeQclw6yCrNSIZ5qSG0O_GDmfIjU942qhiJPppk1kI8G700BLJtLvTVDuC0fjqyobRlLetuAwbGuztBSD8EROsumU-I1tPdUP-LlAHhlY8oe9rFa0VZNi5V1mth_Yw0-A"
```

**Response:**

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

