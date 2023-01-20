---
description: Pass Touchstone FHIR4-0-1-USCore-ClinData the test suite
---

# Touchstone FHIR USCore ClinData

You can pass Touchstone FHIR 4.0.1 basic server test suite on self-hosted Aidbox or in Aidbox User Portal Sandbox. Both self-hosted and the Sandbox Aidboxes use the same Configuration Projects.

## Pass with Aidbox User Portal Sandbox

### Register a box in Aidbox User Portal

1. Go to [https://aidbox.app](https://aidbox.app)
2. Request a new license by clicking **+ License**
3. Fill in required fields
4. Choose `Sandbox` **hosting**
5. Pick `HL7 FHIR R4 Core 4.0.1` and `Touchstone fixtures for FHIR R4 Core 4.0.1` **Configuration Project**
6. Choose `4.0.1` **FHIR version**

### Set up a Test System in Touchstone

1. Go to [https://touchstone.aegis.net](https://touchstone.aegis.net)
2. Register a new test system by clicking `Test Systems` > `New Test System`
3. Choose `FHIR 4.0.1` **Specification**
4. Tick `JSON` and untick `XML` in **Supported Formats**
5. Put url of the box in **Base URL** with `/fhir` postfix (e.g. `https://your-box.edge.aidbox.app/fhir`)
6. Tick **Requires** OAuth2
7. Choose `Dynamic Token` **OAuth2 Token Type**
8. Put `<box base url>/auth/authorize` **Authorization Endpoint** without `/fhir` postfix (e.g. `https://your-box.edge.aidbox.app/auth/authorize`)
9. Put `<box base url>/auth/token` **Token Endpoint** without `/fhir` postfix (e.g. `https://your-box.edge.aidbox.app/auth/token`)
10. Select `JWT Assertion` **OAuth2 Grant Type**
11. Put `system/*.*` **OAuth2 Scopes Supported**
12. Fill in other required fields
13. Save configuration with **Save Changes** button
14. Download a CapabilitiesStatement of the box by pressing download icon in the **Capabilities** column.

### Create Test Setup

1. Go to [https://touchstone.aegis.net/touchstone/testdefinitions?selectedTestGrp=/FHIR4-0-1-Advanced/USCore-ClinData\&activeOnly=false\&contentEntry=TEST\_SCRIPTS](https://touchstone.aegis.net/touchstone/testdefinitions?selectedTestGrp=/FHIR4-0-1-Advanced/USCore-ClinData\&activeOnly=false\&contentEntry=TEST\_SCRIPTS)
2. Select all tests
3. Press `Create Test Setup`
4. Select your `Test System` **Destination**
5. Put `org-1` **organizationId**
6. Put `pt-1` **patientId**
7. Put `loc-1` **locationId**
8. Put `pr-1` **practitionerId**
9. Put `pt-1-provenance` **provenanceId**
10. Press `Save`

### Run Test Setup

1. Go to your **Test Setup**
2. Press `Execute`
