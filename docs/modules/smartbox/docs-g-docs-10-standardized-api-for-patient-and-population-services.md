# (/G)(/10) Standardized API for Patient and Population Services

Smartbox| FHIR API for EHRs comprehensively supports the §170.315(g)(10) Standardized API for patient and population services certification criterion. Here's an overview of how Aidbox covers the key requirements:

### Data Response

Aidbox fully supports responding to requests for both single and multiple patients' data according to the required standards:

* Implements FHIR R4 (HL7 FHIR Release 4.0.1) as the base standard
* Supports the US Core Implementation Guide for single-patient data access
* Enables bulk data export using the FHIR Bulk Data Access specification for multiple patients

### Supported Search Operations

Aidbox offers robust search capabilities that align with certification requirements:

* Supports all mandatory search parameters defined in US Core for single patient queries
* Implements the search criteria specified in the Bulk Data Access IG for multi-patient queries

### Application Registration

Aidbox provides a flexible authorization server that allows applications to register and obtain credentials for API access

### Secure Connection

Secure connections are established using:

* OAuth 2.0 and OpenID Connect for patient and user-scoped access
* SMART Backend Services authorization for system-scoped access

### Authentication and Authorization

Aidbox implements the full SMART App Launch framework, supporting:

* First-time connections with refresh token issuance
* Subsequent connections using refresh tokens
* Token introspection capabilities1

### Patient Authorization Revocation

Aidbox allows revoking application access at a patient's request, meeting the 1-hour revocation requirement.

### Service Base URL Publication

Aidbox supports the ONC requirement for publishing service base URLs:

* URLs are published in FHIR Endpoint resource format

### Documentation

{% embed url="https://cmpl.aidbox.app/documentation" %}

