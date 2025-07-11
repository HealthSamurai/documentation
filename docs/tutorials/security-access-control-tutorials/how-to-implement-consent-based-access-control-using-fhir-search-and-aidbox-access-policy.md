# How to implement Consent-based Access Control using FHIR Search and Aidbox Access Policy

## Objectives

* Allow the Practitioner to view the Encounters for the Patients who have given Consent.

## Before you begin

* Set up the local Aidbox instance using the getting-started [guide](../../getting-started/run-aidbox-locally.md)

## Consent-based Access Control using FHIR Search and Aidbox Access Policy

### Set up the data

Navigate to the AIdbox REST Console.

<figure><img src="../../.gitbook/assets/rest-console-get.jpg" alt=""><figcaption></figcaption></figure>

Create two Practitioners by executing the following requests.

```
POST /fhir/Practitioner
content-type: application/json
accept: application/json

{
  "id": "pr-1",
  "name": [
    {
      "given": [
        "TestPractitioner"
      ]
    }
  ],
  "resourceType": "Practitioner"
}
```

```
POST /fhir/Practitioner
content-type: application/json
accept: application/json

{
  "id": "pr-2",
  "name": [
    {
      "given": [
        "TestPractitioner1"
      ]
    }
  ],
  "resourceType": "Practitioner"
}
```

Create the Patient resource.

```
POST /fhir/Patient
content-type: application/json
accept: application/json

{
  "id": "pt-1",
  "name": [
    {
      "given": [
        "John"
      ],
      "family": "Smith"
    }
  ],
  "resourceType": "Patient"
}
```

Create the Observation and Encounter for the Patient.

```
POST /fhir/Observation
content-type: application/json
accept: application/json

{
  "resourceType": "Observation",
  "status": "final",
  "subject": {
    "reference": "Patient/pt-1"
  },
  "code": {
    "coding": [
      {
        "code": "test-code"
      }
    ]
  }
}
```

```
POST /fhir/Encounter
content-type: application/json
accept: application/json

{
  "resourceType": "Encounter",
  "status": "finished",
  "subject": {
    "reference": "Patient/pt-1"
  },
  "class": {
    "code": "test-code"
  }
}
```

To model the Patient's consent, we will use the  FHIR [Consent](https://hl7.org/fhir/R4/consent.html) resource.

To model the grantee of the consent, we will use the  `provision.actor`  element:

```json
{
        "role": {
          "coding": [
            {
              "code": "GRANTEE"
            }
          ]
        },
        "reference": {
          "reference": "Practitioner/pr-1"
        }
      }
```

To model the scope of the consent, we will use `scope` element.

For example, the consent for accessing the Observations is modeled as follows:

```json
"scope": {
    "coding": [
      {
        "code": "Observation"
      }
    ]
  }
```

Create the Consent resource that models the permission for the Practitioner `pr-1` to access Observations.

```
POST /fhir/Consent
content-type: application/json
accept: application/json

{
  "category": [
    {
      "coding": [
        {
          "code": "test category"
        }
      ]
    }
  ],
  "patient": {
    "reference": "Patient/pt-1"
  },
  "policyRule": {
    "coding": [
      {
        "code": "cric"
      }
    ]
  },
  "provision": {
    "actor": [
      {
        "role": {
          "coding": [
            {
              "code": "GRANTEE"
            }
          ]
        },
        "reference": {
          "reference": "Practitioner/pr-1"
        }
      }
    ]
  },
  "resourceType": "Consent",
  "scope": {
    "coding": [
      {
        "code": "Observation"
      }
    ]
  },
  "status": "active"
}
```

Create the Consent resource that models the permission for the Practitioner `pr-2` to access Encounters.

```
POST /fhir/Consent
content-type: application/json
accept: application/json

{
  "category": [
    {
      "coding": [
        {
          "code": "test category"
        }
      ]
    }
  ],
  "patient": {
    "reference": "Patient/pt-1"
  },
  "policyRule": {
    "coding": [
      {
        "code": "cric"
      }
    ]
  },
  "provision": {
    "actor": [
      {
        "role": {
          "coding": [
            {
              "code": "GRANTEE"
            }
          ]
        },
        "reference": {
          "reference": "Practitioner/pr-2"
        }
      }
    ]
  },
  "resourceType": "Consent",
  "scope": {
    "coding": [
      {
        "code": "Encounter"
      }
    ]
  },
  "status": "active"
}
```

### Construct the FHIR Search

The FHIR Search that, for the given practitioner, will get all the Observations that have consent from the patients is:

```
GET /fhir/Consent?actor=pr-1&scope=Observation&_include=Consent:patient&_revinclude:iterate=Observation:subject
content-type: application/json
accept: application/json
```

You can also try to search the Observations and Encounters for the practitioner `pr-2`

You can learn more about FHIR Search here: [FHIR Search](../../api/rest-api/fhir-search/).

### Create the AccessPolicy

Assuming that the authentication is configured to have a real end-user session, and we have linked the Aidbox User resource to the Practitioner resource with `User.fhirUser` element, the following will be the access policy that allows the FHIR Search above:

```
PUT /fhir/AccessPolicy/practitioner-consent-based-observation
content-type: application/json
accept: application/json

{
  "engine": "matcho",
  "id": "practitioner-consent-based-observation",
  "link": [
    {
      "reference": "Operation/FhirSearch"
    }
  ],
  "matcho": {
    "user": "present?",
    "params": {
      "actor": ".user.fhirUser.id",
      "scope": "Observation",
      "_include": "Consent:patient",
      "_revinclude:iterate": "Observation:subject"
    }
  },
  "resourceType": "AccessPolicy"
}
```

You can learn more about different authentication methods here: [Authentication](../../access-control/authentication/).
