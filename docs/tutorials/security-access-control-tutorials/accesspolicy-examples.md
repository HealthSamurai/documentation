---
description: Practical AccessPolicy examples for Aidbox including allow-all, resource-specific, role-based, and request filtering.
---

# Access policies examples

This guide provides examples of access policies for different use cases. Each example includes:

* A description of the use case
* The policy itself
* A sample request for testing in Dev Tool

<figure><img src="../../.gitbook/assets/c6418f85-3843-4091-8510-f5797ef9f2b6.png" alt="Access Policy Dev Tool interface"><figcaption></figcaption></figure>

See also:

* [Access Policies](../../access-control/authorization/access-policies.md)
* [Access Policy Dev Tool](access-policy-dev-tool.md)
* [Access Policies Best Practices](accesspolicy-best-practices.md)
* [Matcho DSL Reference](../../reference/matcho-dsl-reference.md)

## 1. Policy that **temporarily** allows all requests

**Description:** We need to allow all requests for testing purposes.

{% hint style="warning" %}
Never use this policy in production.
{% endhint %}

**Policy:**

```yaml
id: allow-all
resourceType: AccessPolicy
engine: allow
```

**Request to test the policy:**

```http
GET /fhir/Patient
```

## 2. Policy that allows a user to view their own patient

**Description:** User is registered in Aidbox as a `User` resource with patient id stored in `User.fhirUser` element. We want to allow the user to view their own patient data

**Policy:**

```yaml
id: as-patient-get-patient-data
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Operation/FhirRead

description: Policy that allows a patient to view their own patient data
matcho:
  params:
    resource/id: .user.fhirUser.id
    resource/type: Patient

```

**Request to test the policy:**

```http
GET /fhir/Patient/123
```

## 3. Policy that allows a practitioner to view all observations

**Description:** User is registered in Aidbox as a `User` resource. A `Role` resource is assigned to the user, that links to the `Practitioner` instance:

```yaml
name: practitioner-1
resourceType: Role
user:
  reference: User/user-1
links:
  practitioner:
    reference: Practitioner/pr-1

```

We want to allow the practitioner to view all observations.

**Policy:**

```yaml
id: as-practitioner-get-all-observations
resourceType: AccessPolicy
link:
  - reference: Operation/FhirSearch
engine: matcho
matcho:
  session:
    role:
      $contains:
        links:
          practitioner: present?
```

**Request to test the policy:**

```http
GET /fhir/Observation
```

## 4. Policy that allows a system to update their patients

**Description:** We have a couple of systems. Each system maintains their own patients with identifiers. We want to allow these systems to update their patients - patients that have identifiers from their own system. There's a Client resource in Aidbox created for each system.

**Policy:**

```yaml
id: as-system1-app-update-patients
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Operation/FhirConditionalUpdate
  - reference: Client/system1-client
description: Policy that allows a system1 to update their patients
matcho:
  params:
    resource/type: Patient
    identifier: "#http://system1"
  resource:
    identifier:
      $contains:
        system: http://system1

```

**Request to test the policy:**

```http
PUT /fhir/Patient/123
Authorization: Basic <base64(system1-client:client-secret)> # Base64 encoded client-id and client-secret
content-type: text/yaml
accept: text/yaml

resourceType: Patient
id: 123
identifier:
  - system: http://system1
    value: 12345
name:
  - use: official
    family: Doe
    given:
      - John
gender: male
birthDate: 1980-01-01
```

## 5. Policy that allows a system to search for their patients

**Description:** We have a couple of systems. Each system maintains their own patients with identifiers. We want to allow these systems to get their patients - patients that have identifiers from their own system.

**Policy:**

```yaml
id: as-system1-app-read-patients
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Operation/FhirSearch
  - reference: Client/system1-client
description: Policy that allows a system1 to search for their patients
matcho:
  params:
    resource/type: Patient
    identifier: "#http://system1"
```

**Request to test the policy:**

```http
GET /fhir/Patient?id=123&identifier=http://system1|123
Authorization: Basic <base64(system1-client:client-secret)> # Base64 encoded client-id and client-secret
```

## 6. Policy that allows an application to do CRUD on Patient and Practitioner resources.

**Description:** We have an application that is registered as a Client resource with id `client-id` in Aidbox. We want to allow this application to do CRUD on Patient and Practitioner resources.

**Policy:**

```yaml
id: as-client-id-crud-patients-and-practitioners
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Client/client-id
description: Policy that allows a client-id to do CRUD on Patient and Practitioner resources
matcho:
  params:
    resource/type:
      $enum:
        - Patient
        - Practitioner
  request-method:
    $enum:
      - get
      - post
      - patch
      - put
```

**Request to test the policy:**

```http
GET /fhir/Patient
Authorization: Basic <base64(client-id:client-secret)> # Base64 encoded client-id and client-secret
```

## 7. Policy that allows an admin access to Aidbox UI to the admin users

**Description:** We have integration with external identity provider configured in Aidbox. We want to users with the role `Aidbox-Admins` to access Aidbox UI.

**Policy:**

```yaml
id: as-admin-allowed-access-to-aidbox-ui
resourceType: AccessPolicy
engine: matcho
description: Policy that allows admin access to Aidbox UI for admin users
matcho:
  user:
    data:
      groups:
        $contains: Aidbox-Admins
  request-method:
    $enum:
      - get
      - post
      - put
      - delete
```

**See also:**

* [Managing Admin Access to the Aidbox UI Using Okta Groups](managing-admin-access-to-the-aidbox-ui-using-okta-groups.md)

## 8. Policy that allows access to Workflow Engine screen only for a specific engineer

**Description:** We want to grant a specific engineer (identified by email) access to only the Workflow Engine screen in Aidbox UI. This policy restricts access to the `/rpc` endpoint with specific limitations on the `_m` parameter values, allowing only workflow-related operations.

**Policy:**

```yaml
id: as-engineer-allowed-to-access-workflow-engine
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Operation/rpc
description: Policy that allows access to Workflow Engine screen only for a John Doe engineer
matcho:
  user:
    data:
      email: john.doe@example.com
  params:
    _m:
      $enum:
        - awf.workflow/list
        - awf.task/list
        - awf.task/status
        - awf.workflow/status
```

## 9. Policy that allows graphql search requests to the Patient resource

**Description:** We want to allow an application, registered as a Client resource in Aidbox, to search the Patient resource using GraphQL. [GraphQL Access Control mode](../../reference/all-settings.md#module.graphql.access-control) is set to `rest-search`.

```yaml
BOX_FEATURES_GRAPHQL_ACCESS__CONTROL=rest-search
```

**Policy:**

```yaml
id: as-client-allowed-graphql-search-patients
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Client/my-client
description: Policy that allows graphql search requests to the Patient resource for my-client client
matcho:
  request-method: get
  uri: /Patient
```

**Request to test the policy:**

```graphql
query { PatientList(_count: 1) { id } }
```

## 10. Organization-based hierarchical access control policy for a end-user

**Description:** This example allows an org-based user (created by `PUT /Organization/<org-id>/fhir/User`) to see patients that are also created in the same organization.

**Policy:**

```yaml
id: as-user-allowed-to-see-patients-in-organization
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Operation/FhirSearch
description: A user should be able to get every patient in their organization.
matcho:
  params:
    resource/type: Patient
  request-method: get
  user:
    meta:
      extension:
        $contains:
          url: https://aidbox.app/tenant-organization-id
          value:
            Reference:
              id: .params.organization/id
```

**Request to test the policy:**

```http
GET /Organization/org-a/fhir/Patient/pt-1
```

**See also:**

* [Organization-based hierarchical access control](../../access-control/authorization/scoped-api/organization-based-hierarchical-access-control.md)

## 11. Policy that allows all the requests with JWT issued by certain issuer

**Description:** [Token introspector](set-up-token-introspection.md) is configured in Aidbox to trust JWT issued by certain issuer - `https://auth.example.com`. We want to allow all the requests with JWT issued by this issuer.

**Policy:**

```yaml
id: jwt-issued-by-auth-example-com-allowed
resourceType: AccessPolicy
engine: matcho
description: Policy that allows all the requests with JWT issued by certain issuer
matcho:
  jwt:
    iss: https://auth.example.com
```

**Request to test the policy:**

```http
GET /fhir/Patient
Authorization: Bearer <your-jwt>
```

## 12. Policy that allows the practitioner to read their patients

**Description:** Practitioner is registered in Aidbox as a `User` resource with practitioner id stored in `User.data.practitioner_id` element. We want to allow the practitioner to read their patients.

**Policy:**

```yaml
id: as-practitioner-allowed-to-see-his-patients
resourceType: AccessPolicy
engine: matcho
link:
- reference: Operation/FhirSearch
description: Policy that allows the practitioner to read their patients
matcho:
  params:
    # Only for Patient resources
    resource/type: Patient
    # query parameter general-practitioner should be equal to user.data.practitioner_id
    general-practitioner: .user.data.practitioner_id
  user: 
    # user.data.practitioner_id should be present
    data: 
      practitioner_id: present?

```

**Request to test the policy:**

```http
GET /fhir/Patient?general-practitioner=pr-1
```

## 13. Policy that allows the practitioner to read patients based on given consent.

**Description:** Consent is stored in Aidbox as a `Consent` resource, with practitioner id stored in `Consent.actor` element. Practitioner is registered in Aidbox as a `User` resource with practitioner id stored in `User.data.practitioner_id` element. We want to allow the practitioner to read patients based on given consent.

**Policy:**

```yaml
id: as-practitioner-allowed-to-read-patients-with-consent
resourceType: AccessPolicy
engine: matcho
link:
  - reference: Operation/FhirSearch
description: Policy that allows the practitioner to read patients based on given consent
matcho:
  user: present?
  params:
    _has:Consent:patient:actor: .user.fhirUser.id
    _has:Consent:patient:scope: Encounter
    _revinclude: Encounter:subject
  request-method: get

```

**Request to test the policy:**

```http
GET /fhir/Patient?_has:Consent:patient:actor=<practitioner-id>
```

## 14. Policy that allows the practitioner to create Observations for their patients

**Description:** Practitioner is registered in Aidbox as a `User` resource with practitioner id stored in `User.data.practitioner_id` element. We want to allow the practitioner to create observations for their patients. The relation between the practitioner and the patient is stored in `Patient.generalPractitioner` element.

**Policy:**

```yaml
id: as-practitioner-allowed-to-create-observations-for-their-patients
resourceType: AccessPolicy
engine: complex
link:
  - reference: Operation/FhirCreate
description: Allow practitioner to create observations for their patients     
and: 
  - engine: matcho
    matcho:
      params:
        resource/type: Observation
      user: present?
      user: 
        data:
          practitioner_id: present?
  - engine: sql   
    sql:
      query: |-
        SELECT
          EXISTS (
            SELECT 1 FROM patient p
            WHERE p.id = split_part(({{resource.subject.reference}})::text, '/',2)
            AND p.resource->'generalPractitioner' @>
              jsonb_build_array(
                jsonb_build_object(
                  'resourceType', 'Practitioner',
                  'id', {{user.data.practitioner_id}}::text
                )
              )
          );
```

**Request to test the policy:**

```http
POST /fhir/Observation
Content-Type: text/yaml
Accept: text/yaml

resourceType: Observation
status: final
code:
  coding:
    - system: http://loinc.org
      code: "29463-7"
      display: Body Weight
subject:
  reference: Patient/pt-2
effectiveDateTime: "2025-09-08T10:00:00Z"
valueQuantity:
  value: 72
  unit: kg
  system: http://unitsofmeasure.org
  code: kg
```

## 15. Policy that denies a system to update "protected" resources

**Description:** We have a system that is registered as a `Client` resource in Aidbox. We want to allow this system to update all the Practitioner resources except the ones with `meta.source` equal to `MDM`.

**Policy:**

```yaml
id: as-system-allowed-to-update-practitioners-except-mdm
resourceType: AccessPolicy
engine: complex
link:
  - reference: Operation/FhirUpdate
description: Allow practitioner to create observations for their patients     
and: 
  - engine: matcho
    matcho:
      params:
        resource/type: Practitioner
  - engine: sql   
    sql:
      query: |-
        SELECT resource -> 'meta' ->> 'source' IS NULL OR  resource -> 'meta' ->> 'source' != 'MDM'  FROM practitioner WHERE id = {{params.resource/id}};
```

**Request to test the policy:**

```http
PUT /fhir/Practitioner/pr-1
Authorization: Basic <base64(system1-client:client-secret)> # Base64 encoded client-id and client-secret

{
  "name": [
    {
      "given": [
        "John"
      ]
    }
  ]
}
```

## 16. Policy that allows only read operations to $sql endpoint

**Description:** This policy restricts access to the $sql endpoint by allowing only read-only SQL queries (such as `SELECT`). Any write or schema-modifying operations (`INSERT`, `UPDATE`, `DELETE`, `CREATE`, etc.) are explicitly denied, ensuring that the endpoint can be used safely for querying data without risk of altering the database.

**Policy:**

```yaml
link:
  - reference: User/ca57d5ad-22de-4cce-aaba-b5d6c50a88e8
engine: matcho
matcho:
  uri: /$sql
  body:
    sql: "#^(?i)(?!.*(INSERT|UPDATE|DELETE|MERGE|COPY|CREATE|ALTER|DROP|GRANT|REVOKE|TRUNCATE|CALL|DO|COMMENT|VACUUM|ANALYZE)).*"
  request-method: post
```

## 17. Policy that allows using their own compartment for patients

**Description:** Given that each patient has an external identifier corresponding to a user ID from an external Identity Provider (`jwt.IDP-UserId`), we want to enforce a policy that allows each user to access only the resources within their own compartment.

**Policy:**

```yaml
engine: complex
description: Allow member to use correct patient compartment
id: as-member-allowed-to-use-thier-own-compartment
link:
  - reference: Operation/FhirCompartmentSearch
and:
  - engine: matcho
    matcho:
      jwt:
        IDP-UserId: present?
      params:
        resource/type: Patient
  - engine: sql
    sql:
      query: |-
        SELECT p.id = ({{params.resource/id}})::text FROM patient p
        WHERE p.resource->'identifier' @>
          jsonb_build_array(
            jsonb_build_object(
              'system', 'https://IDP',
              'value', {{jwt.IDP-UserId}}::text
            )
          );
```

**Request to test the policy:**

```http
GET /fhir/Patient/<patient-id>/Observation
```

## 18. Policy that allows using FHIR transactions for the application

**Description:** We have an application that is registered as a Client resource with id `client-id` in Aidbox. We want to allow this application to use [FHIR transactions](../../api/batch-transaction.md).

{% hint style="warning" %}
Note that this policy only allows access to the `/fhir` endpoint itself. You still need to configure access for individual requests within the transaction bundle — for example, the `/fhir/Patient` endpoint.
{% endhint %}

**Policy:**

```yaml
engine: matcho
description: Allow client-id to use FHIR transactions
id: as-client-id-allowed-to-use-fhir-transactions
link:
  - reference: Client/client-id
matcho:
  operation: 
    id: FhirTransaction
```

**Request to test the policy:**

```http
POST /fhir
Authorization: Basic <base64(client-id:client-secret)> # Base64 encoded client-id and client-secret
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "/Patient"
      },
      "resource": {
        "resourceType": "Patient",
        "name": [
          {
            "given": ["John"]
          }
        ]
      }
    }
  ]
}
```

