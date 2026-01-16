---
description: >-
  ONC (g)(10) certification implementation with SMART App Launch, US Core, Bulk
  FHIR APIs, and Inferno test suite compliance.
---

# (g)(10): Standardized API for Patient and Population Services

### Introduction

This document describes how to access protected health information through Aidbox's ONC g(10) certified API. The API provides health information using **FHIR® R4**, a set of clinical interoperability resources under the HL7 standards organization. FHIR is based on common web standards and can be accessed through a RESTful protocol where each FHIR resource has a known URL.

Aidbox implements the **§170.315(g)(10) Standardized API for Patient and Population Services** certification criterion, enabling:

* Single-patient data access via US Core profiles
* Population-level data access via Bulk Data Export
* Secure authorization using SMART App Launch Framework (OAuth 2.0)

### FHIR Endpoints

**Note**: Production and test endpoints are deployment-specific. Replace `[your-aidbox]` with your Aidbox instance subdomain.

#### Production Endpoint

**FHIR Base URL**: `https://[your-aidbox]/fhir`

**Authorization Server**: `https://[your-aidbox]/auth`

### Standards Compliance

Aidbox complies with the following ONC-specified standards:

<table><thead><tr><th width="270.4609375">Standard</th><th>Version</th><th>Regulation Reference</th></tr></thead><tbody><tr><td><strong>FHIR</strong></td><td>R4 (4.0.1)</td><td>§170.215(a)(1)</td></tr><tr><td><strong>US Core Implementation Guide</strong></td><td>6.1.0 (USCDI v3)</td><td>§170.215(b)(1)</td></tr><tr><td><strong>SMART App Launch Framework</strong></td><td>2.0.0</td><td>§170.215(c)</td></tr><tr><td><strong>Bulk Data Access IG</strong></td><td>2.0.0</td><td>§170.215(d)</td></tr></tbody></table>

**Compliance Deadline**: US Core 6.1.0 / USCDI v3 required by **December 31, 2025**.

**Additional Versions Supported**:

* US Core 3.1.1 (USCDI v1)
* US Core 7.0.0 (USCDI v4, SVAP)
* US Core 8.0.0+ (USCDI v5, SVAP)

### Types of SMART-on-FHIR Applications

Aidbox supports three application types:

1. **Standalone App**: Patient-facing application launched outside the EHR with patient context selection at launch time.
2. **EHR-Embedded App**: Application launched from within the EHR with pre-established patient and encounter context.
3. **Backend Services App**: Server-to-server application for bulk data export and multi-patient operations without user interface (system-level scopes).

### Types of Authentication Supported

Aidbox supports the following OAuth 2.0 client authentication methods:

1. **Public Client** (no client secret): Patient-facing apps using PKCE for authorization code flow
   * Reference: [SMART App Launch - Public Client](http://hl7.org/fhir/smart-app-launch/)
2. **Confidential Client - Symmetric** (client secret): Server-side apps using `client_secret_basic`
   * Reference: [SMART App Launch - Symmetric Auth](http://hl7.org/fhir/smart-app-launch/)
3. **Confidential Client - Asymmetric** (private key JWT): Backend services using signed JWT assertions
   * Reference: [SMART App Launch - Asymmetric Auth](http://hl7.org/fhir/smart-app-launch/)

### Steps for SMART App Launch

#### 1. Client App Registration

Aidbox provides a **Developer Sandbox Portal** for self-service app registration, testing, and submission.

**Developer Registration Process**:

1. **Sign Up**: Open the Developer Portal and click **Sign Up**
2. **Complete Registration**: Fill out the developer registration form
3. **Confirm Email**: Check email and click **Confirm Email Address**
4. **Set Password**: Create password on confirmation page
5. **Sign In**: Access your developer dashboard

**Register a SMART App**:

1. From dashboard, click **Register New**
2. Fill in app details:
   * **App Name**: Your application name
   * **Confidentiality**: Public or Confidential
   * **Redirect URL**: Where users return after authorization (e.g., `https://myapp.example.com/callback`)
   * **Launch URL**: Your app's SMART launch endpoint (e.g., `https://myapp.example.com/launch.html`)
3. Click **Create App**
4. Copy the **Client ID** from your app's draft page
5. **Test Launch**: Click **Test Launch** to test with sample patient data (`Patient/test-pt-1`)
6. **Submit for Review**: Click **Submit for Review** when ready

\
Developer portal documentation

{% content-ref url="../../aidbox-+-fhir-app-portal/tutorials/developer-sandbox.md" %}
[developer-sandbox.md](../../aidbox-+-fhir-app-portal/tutorials/developer-sandbox.md)
{% endcontent-ref %}

#### 2. Retrieve .well-known/smart-configuration

To discover Aidbox's SMART capabilities and OAuth endpoints, query the well-known configuration endpoint.

**Request**:

```http
GET /.well-known/smart-configuration
```

**Response** (200 OK):

```json
{
  "introspection_endpoint": "[your-aidbox]/auth/introspect",
  "capabilities": [
    "client-confidential-symmetric",
    "client-confidential-asymmetric",
    "launch-ehr",
    "context-banner",
    "context-style",
    "context-ehr-patient",
    "context-ehr-encounter",
    "authorize-post",
    "client-public",
    "permission-patient",
    "permission-user",
    "launch-standalone",
    "sso-openid-connect",
    "context-standalone-patient",
    "permission-offline",
    "permission-v1",
    "permission-v2"
  ],
  "grant_types_supported": [
    "authorization_code",
    "urn:ietf:params:oauth:grant-type:token-exchange",
    "implicit",
    "password",
    "client_credentials"
  ],
  "userinfo_endpoint": "[your-aidbox]/auth/userinfo",
  "token_endpoint_auth_methods_supported": [
    "client_secret_post",
    "client_secret_basic",
    "private_key_jwt"
  ],
  "claims_supported": [
    "sub",
    "aud",
    "email",
    "exp",
    "iat",
    "iss",
    "locale",
    "family_name",
    "given_name",
    "name",
    "picture"
  ],
  "subject_types_supported": [
    "public"
  ],
  "authorization_endpoint": "[your-aidbox]/auth/authorize",
  "scopes_supported": [
    "openid",
    "profile",
    "email",
    "groups",
    "launch",
    "launch/patient",
    "patient/*.cruds",
    "system/*.cruds",
    "user/*.cruds",
    "fhirUser",
    "offline_access",
    "online_access"
  ],
  "issuer": "[your-aidbox]",
  "code_challenge_methods_supported": [
    "S256"
  ],
  "response_types_supported": [
    "code",
    "token",
    "token id_token",
    "code id_token"
  ],
  "token_endpoint_auth_signing_alg_values_supported": [
    "RS384"
  ],
  "token_endpoint": "[your-aidbox]/auth/token",
  "id_token_signing_alg_values_supported": [
    "RS256"
  ],
  "jwks_uri": "[your-aidbox]/.well-known/jwks.json"
}
```

#### 3. Obtain Authorization Code

The app initiates the OAuth 2.0 authorization flow by redirecting the user to Aidbox's authorization endpoint.

**Authorization Parameters**

<table><thead><tr><th width="203.203125">Parameter</th><th width="166.6875">Required</th><th>Description</th></tr></thead><tbody><tr><td><code>response_type</code></td><td><strong>Required</strong></td><td>Fixed value: <code>code</code></td></tr><tr><td><code>client_id</code></td><td><strong>Required</strong></td><td>The client's identifier from registration</td></tr><tr><td><code>redirect_uri</code></td><td><strong>Required</strong></td><td>Must match a pre-registered redirect URI</td></tr><tr><td><code>scope</code></td><td><strong>Required</strong></td><td>Space-separated list of scopes (see table below)</td></tr><tr><td><code>state</code></td><td><strong>Required</strong></td><td>Opaque value for CSRF protection (minimum 122 bits entropy)</td></tr><tr><td><code>aud</code></td><td><strong>Required</strong></td><td>FHIR base URL: <code>https://[your-aidbox]/fhir</code></td></tr><tr><td><code>launch</code></td><td>Conditional</td><td>Launch token (EHR launch only)</td></tr><tr><td><code>code_challenge</code></td><td>Conditional</td><td>PKCE code challenge (public clients)</td></tr><tr><td><code>code_challenge_method</code></td><td>Conditional</td><td>Fixed value: <code>S256</code> (public clients)</td></tr></tbody></table>

**Scopes Supported**

<table><thead><tr><th width="217.51171875">Scope</th><th>Grants</th></tr></thead><tbody><tr><td><code>patient/*.read</code></td><td>Permission to read any resource for the current patient</td></tr><tr><td><code>patient/*.rs</code></td><td>Permission to read and search any resource for the current patient (SMART v2)</td></tr><tr><td><code>user/*.read</code></td><td>Permission to read any resource the current user can access</td></tr><tr><td><code>user/*.rs</code></td><td>Permission to read and search any resource the current user can access (SMART v2)</td></tr><tr><td><code>system/*.read</code></td><td>Permission to read any resource (backend services)</td></tr><tr><td><code>system/*.rs</code></td><td>Permission to read and search any resource (backend services, SMART v2)</td></tr><tr><td><code>openid</code></td><td>Permission to retrieve information about the logged-in user</td></tr><tr><td><code>fhirUser</code></td><td>Request user's FHIR identifier in token</td></tr><tr><td><code>launch</code></td><td>Permission to obtain EHR launch context</td></tr><tr><td><code>launch/patient</code></td><td>Request patient selection at launch time (standalone apps)</td></tr><tr><td><code>offline_access</code></td><td>Request refresh token for offline access</td></tr></tbody></table>

**Specific Resource Scopes**: You may request granular scopes like `patient/Patient.read`, `patient/Observation.rs`, `user/MedicationRequest.rs`, etc.

**Example Authorization Request (Standalone Patient App)**

**Request**:

```http
GET /auth/authorize?
  response_type=code&
  client_id=my-smart-app&
  redirect_uri=https%3A%2F%2Fmyapp.example.com%2Fcallback&
  scope=launch%2Fpatient+openid+fhirUser+offline_access+patient%2FPatient.rs+patient%2FObservation.rs+patient%2FCondition.rs&
  state=random-state-12345&
  code_challenge=E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM&
  code_challenge_method=S256&
  aud=https%3A%2F%2F[your-aidbox]%2Ffhir
Host: [your-aidbox]
```

**Authorization Response**

After user authentication and consent, Aidbox redirects to the app's `redirect_uri` with:

**Response** (302 Redirect):

```
https://myapp.example.com/callback?
  code=AUTH_CODE_12345&
  state=random-state-12345
```

**Parameters**:

* `code`: Authorization code to exchange for an access token
* `state`: Exact value from the request (must match)

#### 4. Obtain Access Token

After receiving the authorization code, the app exchanges it for an access token.

**Token Request Parameters**

<table><thead><tr><th width="206.22265625">Parameter</th><th width="148.0390625">Required</th><th>Description</th></tr></thead><tbody><tr><td><code>grant_type</code></td><td><strong>Required</strong></td><td><code>authorization_code</code> (for initial token) or <code>client_credentials</code> (backend services)</td></tr><tr><td><code>code</code></td><td><strong>Required</strong></td><td>Authorization code from previous step</td></tr><tr><td><code>redirect_uri</code></td><td><strong>Required</strong></td><td>Same redirect URI used in authorization request</td></tr><tr><td><code>client_id</code></td><td>Conditional</td><td>Required for public clients; omit for confidential clients using HTTP Basic auth</td></tr><tr><td><code>client_secret</code></td><td>Conditional</td><td>Required for confidential symmetric clients (if not using HTTP Basic)</td></tr><tr><td><code>code_verifier</code></td><td>Conditional</td><td>Required for public clients (PKCE verifier)</td></tr><tr><td><code>client_assertion_type</code></td><td>Conditional</td><td><code>urn:ietf:params:oauth:client-assertion-type:jwt-bearer</code> (asymmetric auth)</td></tr><tr><td><code>client_assertion</code></td><td>Conditional</td><td>Signed JWT (asymmetric auth)</td></tr></tbody></table>

**Example Token Request (Public Client with PKCE)**

**Request**:

```http
POST /auth/token
Host: [your-aidbox]
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&
code=AUTH_CODE_12345&
redirect_uri=https%3A%2F%2Fmyapp.example.com%2Fcallback&
client_id=my-smart-app&
code_verifier=dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk
```

**Example Token Request (Confidential Client with Secret)**

**Request**:

```http
POST /auth/token
Host: [your-aidbox]
Content-Type: application/x-www-form-urlencoded
Authorization: Basic base64(client_id:client_secret)

grant_type=authorization_code&
code=AUTH_CODE_12345&
redirect_uri=https%3A%2F%2Fbackend.example.com%2Fcallback
```

**Token Response**

**Response** (200 OK):

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "scope": "launch/patient openid fhirUser offline_access patient/Patient.rs patient/Observation.rs patient/Condition.rs",
  "refresh_token": "refresh-token-value",
  "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "patient": "patient-id-123"
}
```

**Response Parameters**:

| Parameter       | Required     | Description                                |
| --------------- | ------------ | ------------------------------------------ |
| `access_token`  | **Required** | Bearer token for API access                |
| `token_type`    | **Required** | Fixed value: `Bearer`                      |
| `expires_in`    | Recommended  | Token lifetime in seconds                  |
| `scope`         | **Required** | Granted scopes (may differ from requested) |
| `refresh_token` | Optional     | Token for obtaining new access token       |
| `id_token`      | Optional     | OpenID Connect identity token              |
| `patient`       | Optional     | Patient ID for patient-scoped access       |

#### 5. Access FHIR API

With a valid access token, the app can access FHIR resources by including the token in the `Authorization` header.

**Request Format**:

```http
GET /fhir/{ResourceType}?{searchParams}
Host: [your-aidbox]
Authorization: Bearer {access_token}
Accept: application/fhir+json
```

**Supported HTTP Methods**: `GET` (read and search operations)

**Supported Resources**: All US Core profiles and USCDI v3 data elements.

***

#### 6. Refresh Access Token

When an access token expires, use the refresh token to obtain a new access token without user interaction.

**Request**:

```http
POST /auth/token
Host: [your-aidbox]
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token&
refresh_token=refresh-token-value&
client_id=my-smart-app&
scope=patient/Patient.rs+patient/Observation.rs
```

**Response** (200 OK):

```json
{
  "access_token": "new-access-token",
  "token_type": "Bearer",
  "expires_in": 3600,
  "scope": "patient/Patient.rs patient/Observation.rs",
  "refresh_token": "new-refresh-token"
}
```

***

### Access FHIR Resources

Aidbox supports the following US Core profiles for single-patient data access:

<table><thead><tr><th width="224.45703125">FHIR Resource</th><th>US Core Profile</th></tr></thead><tbody><tr><td>Patient</td><td>US Core Patient Profile</td></tr><tr><td>AllergyIntolerance</td><td>US Core AllergyIntolerance Profile</td></tr><tr><td>CarePlan</td><td>US Core CarePlan Profile</td></tr><tr><td>CareTeam</td><td>US Core CareTeam Profile</td></tr><tr><td>Condition</td><td>US Core Condition Profile (Problems, Health Concerns)</td></tr><tr><td>Device</td><td>US Core Implantable Device Profile</td></tr><tr><td>DiagnosticReport</td><td>US Core DiagnosticReport Profile (Lab, Notes)</td></tr><tr><td>DocumentReference</td><td>US Core DocumentReference Profile</td></tr><tr><td>Encounter</td><td>US Core Encounter Profile</td></tr><tr><td>Goal</td><td>US Core Goal Profile</td></tr><tr><td>Immunization</td><td>US Core Immunization Profile</td></tr><tr><td>Location</td><td>US Core Location Profile</td></tr><tr><td>Medication</td><td>US Core Medication Profile</td></tr><tr><td>MedicationDispense</td><td>US Core MedicationDispense Profile</td></tr><tr><td>MedicationRequest</td><td>US Core MedicationRequest Profile</td></tr><tr><td>Observation</td><td>US Core Observation Profiles (Lab, Vital Signs, Smoking Status, etc.)</td></tr><tr><td>Organization</td><td>US Core Organization Profile</td></tr><tr><td>Practitioner</td><td>US Core Practitioner Profile</td></tr><tr><td>PractitionerRole</td><td>US Core PractitionerRole Profile</td></tr><tr><td>Procedure</td><td>US Core Procedure Profile</td></tr><tr><td>Provenance</td><td>US Core Provenance Profile</td></tr><tr><td>QuestionnaireResponse</td><td>US Core QuestionnaireResponse Profile</td></tr><tr><td>RelatedPerson</td><td>US Core RelatedPerson Profile</td></tr><tr><td>Coverage</td><td>US Core Coverage Profile (USCDI v3)</td></tr><tr><td>ServiceRequest</td><td>US Core ServiceRequest Profile (USCDI v3)</td></tr><tr><td>Specimen</td><td>US Core Specimen Profile</td></tr></tbody></table>

#### Example: Search Patient by ID

**Request**:

```http
GET /fhir/Patient?_id=patient-123
Host: [your-aidbox]
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/fhir+json
```

**Response** (200 OK):

```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 1,
  "link": [
    {
      "relation": "self",
      "url": "https://[your-aidbox]/fhir/Patient?_id=patient-123"
    }
  ],
  "entry": [
    {
      "fullUrl": "https://[your-aidbox]/fhir/Patient/patient-123",
      "resource": {
        "resourceType": "Patient",
        "id": "patient-123",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"
          ]
        },
        "identifier": [
          {
            "system": "http://hospital.example.org/patients",
            "value": "12345"
          }
        ],
        "name": [
          {
            "use": "official",
            "family": "Smith",
            "given": ["John"]
          }
        ],
        "gender": "male",
        "birthDate": "1980-01-01"
      }
    }
  ]
}
```

#### Example: Search Observations for Patient

**Request**:

```http
GET /fhir/Observation?patient=patient-123&category=laboratory
Host: [your-aidbox]
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/fhir+json
```

**Response** (200 OK):

```json
{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 50,
  "link": [
    {
      "relation": "self",
      "url": "https://[your-aidbox]/fhir/Observation?patient=patient-123&category=laboratory"
    }
  ],
  "entry": [
    {
      "fullUrl": "https://[your-aidbox]/fhir/Observation/obs-1",
      "resource": {
        "resourceType": "Observation",
        "id": "obs-1",
        "meta": {
          "profile": [
            "http://hl7.org/fhir/us/core/StructureDefinition/us-core-observation-lab"
          ]
        },
        "status": "final",
        "category": [
          {
            "coding": [
              {
                "system": "http://terminology.hl7.org/CodeSystem/observation-category",
                "code": "laboratory"
              }
            ]
          }
        ],
        "code": {
          "coding": [
            {
              "system": "http://loinc.org",
              "code": "2093-3",
              "display": "Cholesterol [Mass/volume] in Serum or Plasma"
            }
          ]
        },
        "subject": {
          "reference": "Patient/patient-123"
        },
        "effectiveDateTime": "2025-01-10T10:00:00Z",
        "valueQuantity": {
          "value": 180,
          "unit": "mg/dL",
          "system": "http://unitsofmeasure.org",
          "code": "mg/dL"
        }
      }
    }
  ]
}
```

### Bulk Data Export

Aidbox implements the FHIR Bulk Data Access IG for population-level data export.

#### Bulk Export Endpoints

| Endpoint                       | Description                         |
| ------------------------------ | ----------------------------------- |
| `GET /fhir/Patient/$export`    | Export all patients                 |
| `GET /fhir/Group/{id}/$export` | Export patients in a specific group |
| `GET /fhir/$export`            | Export all resources (system-level) |

#### Bulk Export Parameters

<table><thead><tr><th width="193.8125">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>_type</code></td><td>Comma-separated list of resource types (e.g., <code>Patient,Observation,Condition</code>)</td></tr><tr><td><code>_since</code></td><td>Incremental export (resources modified after this timestamp)</td></tr><tr><td><code>_outputFormat</code></td><td>Output format (default: <code>application/fhir+ndjson</code>)</td></tr><tr><td><code>patient</code></td><td>Comma-separated list of patient IDs. Exports data only for the listed patients. Available only for patient-level export.</td></tr></tbody></table>

#### Bulk Export Workflow

**Step 1: Initiate Export (Kickoff)**

**Request**:

```http
GET /fhir/Patient/$export?_type=Patient,Observation,Condition
Host: [your-aidbox]
Authorization: Bearer {access_token}
Accept: application/fhir+json
Prefer: respond-async
```

**Response** (202 Accepted):

```http
HTTP/1.1 202 Accepted
Content-Location: https://[your-aidbox]/fhir/$export-status/<id>
```

**Step 2: Poll Export Status**

**Request**:

```http
GET /fhir/$export-status/{job-id}
Host: [your-aidbox]
Authorization: Bearer {access_token}
```

**Response (In Progress)** (202 Accepted):

```json
{
  "transactionTime": "2025-01-16T10:00:00Z",
  "request": "/fhir/Patient/$export?_type=Patient,Observation,Condition",
  "requiresAccessToken": false
}
```

**Step 3: Download Export Files**

**Response (Complete)** (200 OK):

```json
{
  "transactionTime": "2025-01-16T10:00:00Z",
  "request": "/fhir/Patient/$export?_type=Patient,Observation,Condition",
  "requiresAccessToken": false,
  "output": [
    {
      "type": "Patient",
      "url": "https://storage.example.com/export-1/Patient.ndjson",
      "count": 1000
    },
    {
      "type": "Observation",
      "url": "https://storage.example.com/export-1/Observation.ndjson",
      "count": 50000
    },
    {
      "type": "Condition",
      "url": "https://storage.example.com/export-1/Condition.ndjson",
      "count": 5000
    }
  ],
  "error": []
}
```

**Step 4: Download NDJSON Files**

Each file contains one FHIR resource per line in JSON format:

```json
{"resourceType":"Patient","id":"pt-1","name":[{"family":"Smith","given":["Alice"]}]}
{"resourceType":"Patient","id":"pt-2","name":[{"family":"Jones","given":["Bob"]}]}
```
