# SMART Backend services

Intro

## Register a client

Before a SMART client can run against a FHIR server, the client SHALL generate or obtain an asymmetric key pair and SHALL register its public key set with that FHIR server’s authorization service

{% tabs %}
{% tab title="Request example" %}
<pre class="language-json"><code class="lang-json"><strong>PUT fhir/Client/inferno-my-clinic-bulk-client
</strong>content-type: application/json
accept: application/json

  {
    "type": "bulk-api-client",
    "active": true,
    "auth": {
      "client_credentials": {
        "client_assertion_types": [
          "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
        ],
        "access_token_expiration": 300
      }
    },
    "scope": [
      "system/*.read"
    ],
    "jwks_uri": "&#x3C;AIDBOX BASE URL>/.well-known/jwks.json",
    "grant_types": [
      "client_credentials"
    ]
  }
</code></pre>
{% endtab %}

{% tab title="Response" %}
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
   "access_token_expiration": 300
  }
 },
 "active": true,
 "id": "",
 "jwks_uri": "https://releasetest.edge.aidbox.app/.well-known/jwks.json"
}
```
{% endtab %}
{% endtabs %}

## Create AccessPolicy for bulk client

{% tabs %}
{% tab title="Request" %}
PUT /AccessPolicy/inferno-my-clinic-bulk-client accept: application/json content-type: application/json

{ "engine": "allow", "link": \[ { "id": "inferno-my-clinic-bulk-client", "resourceType": "Client" } ] }
{% endtab %}

{% tab title="Response" %}
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
 "resourceType": "AccessPolicy",
 "meta": {
  "lastUpdated": "2024-12-12T13:29:07.490769Z",
  "createdAt": "2024-12-12T13:29:07.490769Z",
  "versionId": "1456"
 }
}
```


{% endtab %}
{% endtabs %}

## Obtain access token

{% tabs %}
{% tab title="Request example" %}
<pre class="language-json"><code class="lang-json"><strong>POST /auth/token
</strong><strong>
</strong><strong>{
</strong>  "client_assertion": "eyJhbGciOiJSUzM4NCIsImtpZCI6ImI0MTUyOGI2ZjM3YTk1MDBlZGI4YTkwNWE1OTViZGQ3IiwidHlwIjoiSldUIn0.eyJpc3MiOiJpbmZlcm5vLW15LWNsaW5pYy1idWxrLWNsaWVudCIsInN1YiI6ImluZmVybm8tbXktY2xpbmljLWJ1bGstY2xpZW50IiwiYXVkIjoiaHR0cHM6Ly9nMTB0ZXN0LmVkZ2UuYWlkYm94LmFwcC9hdXRoL3Rva2VuIiwiZXhwIjoxNzM0MDA5NjI2LCJqdGkiOiJkZGI4NzQ5OTk1YjFkNWRiNDVkNTQ2NDVmZmU0ZmExZTkxODRhODI3YjlmOWM5MDY5ZDQxYzRmYjJhNjBjYTY3In0.hxKAec655NTH7Gs6qy2Cz2CXvETWnxF0jydjEdXNKYyrQvecBWct_ITc92eFiDnZ5jubhExqojeE2HUDn3lmS89Q9qFfGEsByLWXy4nJqSHa2y5mWxD5aI3LF3c4oSOZXSj-jFxAlSmxhV7MxumnJ2XP-6e81QQT-QQ9mDomWhgrIjqaHhv5yPQzI6CqDad9XBInMcE7S_TZ9QTpq3WtzC520-8SH3KdVF9dILO6pBGOOrlZ8468Vwfl5WL6XuhhwjbIIp8B5F0qAOGIGiA8V_-eE6PM1CNZtKQfrZNvVh0VwSu4T2k3gL4ZfI_8nhpUt8EEusOsu_6EvK3sP1yv7w",
  "client_assertion_type": "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
  "grant_type": "client_credentials",
  "scope": "system/*.read"
}
</code></pre>


{% endtab %}

{% tab title="Response" %}
```
{
  "token_type": "Bearer",
  "scope": "system/AllergyIntolerance.read system/CarePlan.read system/CareTeam.read system/Condition.read system/Device.read system/DiagnosticReport.read system/DocumentReference.read system/Encounter.read system/Goal.read system/Group.read system/Immunization.read system/Location.read system/MedicationRequest.read system/Observation.read system/Organization.read system/Patient.read system/Practitioner.read system/Procedure.read system/Provenance.read",
  "need_patient_banner": true,
  "expires_in": 300,
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1teS1jbGluaWMtYnVsay1jbGllbnQiLCJpYXQiOjE3MzQwMTAyNDMsImp0aSI6IjEzMzZhNmIyLTZiNGMtNDE0Yy04Mjk0LWJkYjA2OWE5OTE5MSIsImV4cCI6MTczNDAxMDU0M30.glqegvLKAoF5y2cJ7rUODTz6Ro0Lhu7vUr86vvvyrhKU0ADHVDkHbue-SMyy2HhHl0ZF4LMC_Vlu4Q_yv2WWUn4htQ3INYIeBuJ_pyFOonJ2mQNa82j6ZmqLrjZyGr_PlqAOdZGPfmDyudD_jbBVABf3wnAcvLxP5fIPZrAGL_AlHKA843LgKTqIbmRbugl_QvdBwRfQj2fIN4HZNIkfcOeQclw6yCrNSIZ5qSG0O_GDmfIjU942qhiJPppk1kI8G700BLJtLvTVDuC0fjqyobRlLetuAwbGuztBSD8EROsumU-I1tPdUP-LlAHhlY8oe9rFa0VZNi5V1mth_Yw0-A",
  "refresh_token": null
}
```
{% endtab %}
{% endtabs %}

## Access FHIR API

{% tabs %}
{% tab title="Request example" %}
```
GET /fhir/Observation?code=4548-4&_count=2
content-type: application/json
accept: application/json
authorization: "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1teS1jbGluaWMtYnVsay1jbGllbnQiLCJpYXQiOjE3MzQwMTAyNDMsImp0aSI6IjEzMzZhNmIyLTZiNGMtNDE0Yy04Mjk0LWJkYjA2OWE5OTE5MSIsImV4cCI6MTczNDAxMDU0M30.glqegvLKAoF5y2cJ7rUODTz6Ro0Lhu7vUr86vvvyrhKU0ADHVDkHbue-SMyy2HhHl0ZF4LMC_Vlu4Q_yv2WWUn4htQ3INYIeBuJ_pyFOonJ2mQNa82j6ZmqLrjZyGr_PlqAOdZGPfmDyudD_jbBVABf3wnAcvLxP5fIPZrAGL_AlHKA843LgKTqIbmRbugl_QvdBwRfQj2fIN4HZNIkfcOeQclw6yCrNSIZ5qSG0O_GDmfIjU942qhiJPppk1kI8G700BLJtLvTVDuC0fjqyobRlLetuAwbGuztBSD8EROsumU-I1tPdUP-LlAHhlY8oe9rFa0VZNi5V1mth_Yw0-A"
```
{% endtab %}

{% tab title="Response" %}
```json
//status 200 OK

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

