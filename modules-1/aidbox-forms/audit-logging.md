---
description: This article outlines how to audit Aidbox Forms usage
---

# Overview 

Audit Logging is central part for system observability - providing the ability to track and monitor system activity for security, compliance, and operational insights. Through audit logging, administrators can review actions performed within the system, identify potential issues, and ensure that operations comply with relevant regulations.

# Audit Logging in Aidbox

Aidbox supports Audit Logging and acts as Audit Record repository.
Audit Logging disabled by default and should be enabled via configuration.

- [setup audit logging](../../modules-1/audit/setup-audit-logging.md)

# Audit Logging in Aidbox Forms Module

The Aidbox Forms module is a critical component for capturing information.
This module also supports Audit Logging to capture and review user interactions and system events related to form construction and usage. 
Audit Logging in Forms module will be enabled at the same time as in Aidbox.

## Logged Events

In addition to Aidbox's default events for CRUD operations on 

- `Questionnaire`
- `QuestionnaireResponse`

Aidbox Forms module records additional ones

- `populate` - Logs what form was populated with data, including the patient related to response
- `populate-link` - Records a form name that was populated and the response saved as a  result
- `generate-form-token` - Tracks token creation for anonimous access to response
- `generate-form-link` -  Records link creation for anonimous access to response
- `update-response` - Tracks modifications made to existing responses, 
- `amend-response`  - Records response amendemnt, including the user amended the form
- `submit-response` - Captures details of form submissions, including the user submitting the form
- `assemble-form`  - Logs whenever a complex form is assembled, including user and timestamp

## Events Customization

There is one customization option accessible for the user 

- setting Application name in which forms are embedded - this is usefull when someone wants to embed forms in several applications and want to identify application from which user filling the form 

This is can be done via adding `app-name` parameter to `populate-link` / `generate-link` operations

Example
```yaml
POST /fhir/Questionnaire/[id]/$populatelink
content-type: text/yaml
accept: text/yaml

resourceType: Parameters
parameter:
- name: subject
  valueReference:
    reference: Patient/pt-1
- name: app-name
  valueString: forms-app
```

