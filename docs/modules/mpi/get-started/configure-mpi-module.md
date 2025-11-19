---
description: >-
  This page explains how to configure the Aidbox MPI module by adding a matching
  model. It includes an example model for testing, and notes on performance and
  model tuning.
---

# Configure MPI module

{% hint style="info" %}
The current implementation only supports the Patient resource. If you need support for additional resources, please [contact us](../../../overview/contact-us.md).
{% endhint %}

The example in the next section provides a **basic model** that allows you to **start the MPI module** and **test** its functionality. For a detailed explanation of all model elements and matching logic, see [Matching Model Explanation](../matching-model-explanation.md).

## Create OAuth Client for MDM Frontend

To enable authentication for the MDM frontend, create an OAuth client in Aidbox:

```yaml
PUT /fhir/Client/mpi-dev
content-type: text/yaml
accept: text/yaml

id: mpi-dev
auth:
  authorization_code:
    redirect_uri: http://localhost:3000/api/auth/callback/aidbox
    token_format: jwt
    refresh_token: true
    secret_required: true
    access_token_expiration: 36000
    refresh_token_expiration: 864000
secret: pass
first_party: true
grant_types:
- code
```

## Add admin privileges to your user

Navigate to: **Aidbox → IAM → Users → Your Admin**

1. Open the Aidbox dashboard.
2. Go to the IAM (Identity and Access Management) section.
3. Select Users field.
4. Find and open your Admin user profile.

Add the following section to the user configuration JSON:

```json
{
  "data": {
    "groups": [
      "SIT_EMPI_ADMIN_DEV"
    ]
  }
}
```

## Create SQL functions

Create the following SQL functions in your Aidbox database:

```sql
CREATE OR REPLACE FUNCTION public.immutable_unaccent(x text)
    RETURNS text
    LANGUAGE sql
    IMMUTABLE
    AS $function$
    SELECT
        unaccent($1);
$function$;

CREATE OR REPLACE FUNCTION public.immutable_unaccent_upper(text)
    RETURNS text
    LANGUAGE plpgsql
    IMMUTABLE
    AS $function$
BEGIN
    RETURN upper(public.unaccent($1));
END;
$function$;

CREATE OR REPLACE FUNCTION public.immutable_remove_spaces_unaccent_upper(text)
    RETURNS text
    LANGUAGE plpgsql
    IMMUTABLE
    AS $function$
BEGIN
    RETURN replace(public.upper(public.unaccent($1)), ' ', '');
END;
$function$;
```

## Create database indexes

{% hint style="info" %}
The indexes below are **recommendations** that work well with the example model from the "Add model to Aidbox" section. Since the matching model is customizable, you may need to adjust these indexes based on your specific model configuration and data requirements.
{% endhint %}

Create the following indexes to optimize matching performance and resource reference lookups:

```sql
-- Patient indexes for matching and search
CREATE INDEX IF NOT EXISTS patient_full_name_idx_mpi ON public.patient USING btree ((((immutable_unaccent_upper((resource #>> '{name,0,family}'::text[])) || ' '::text) || immutable_unaccent_upper((resource #>> '{name,0,given,0}'::text[]))))); -- match blocks
CREATE INDEX IF NOT EXISTS patient_given_gin_idx_mpi ON public.patient USING gin (((resource #>> '{name,0,given,0}'::text[])) gin_trgm_ops); -- search by partial given
CREATE INDEX IF NOT EXISTS patient_family_gin_idx_mpi ON public.patient USING gin (((resource #>> '{name,0,family}'::text[])) gin_trgm_ops); -- search by partial family
CREATE INDEX IF NOT EXISTS patient_given_btree_idx_mpi ON public.patient USING btree (immutable_unaccent_upper((resource #>> '{name,0,given,0}'::text[]))); -- search by exact given
CREATE INDEX IF NOT EXISTS patient_family_btree_idx_mpi ON public.patient USING btree (immutable_unaccent_upper((resource #>> '{name,0,family}'::text[]))); -- search by exact family
CREATE INDEX IF NOT EXISTS patient_email_idx_mpi ON public.patient USING gin (jsonb_path_query_array(resource, '$."telecom"[*]?(@."system" == "email")."value"'::jsonpath) jsonb_path_ops); -- search by email
CREATE INDEX IF NOT EXISTS patient_identifier_idx_mpi ON public.patient USING gin (jsonb_path_query_array(resource, '$."identifier"[*]."value"'::jsonpath) jsonb_path_ops); -- search by identifier
CREATE INDEX IF NOT EXISTS patient_phone_idx_mpi ON public.patient USING gin (jsonb_path_query_array(resource, '$."telecom"[*]?(@."system" == "phone")."value"'::jsonpath) jsonb_path_ops); -- search by phone
CREATE INDEX IF NOT EXISTS patient_address_line_btree_idx_mpi ON public.patient USING btree (immutable_remove_spaces_unaccent_upper((resource #>> '{address,0,line,0}'::text[]))); -- match blocks
CREATE INDEX IF NOT EXISTS patient_identifier_idx2_mpi ON public.patient USING gin (((resource #> '{identifier}'::text[]))); -- for second model, review needed
CREATE INDEX IF NOT EXISTS patient_birthdate_idx_mpi ON public.patient USING btree (((resource #>> '{birthDate}'::text[]))); -- match blocks

-- Observation indexes for merge/unmerge operations
CREATE INDEX IF NOT EXISTS observation_encounter_references_idx_mpi ON public.observation USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Encounter")."id"'::jsonpath)); -- unmerge
CREATE INDEX IF NOT EXISTS observation_patient_references_idx_mpi ON public.observation USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath)); -- merge

-- Specimen indexes for merge operations
CREATE INDEX IF NOT EXISTS specimen_patient_references_idx_mpi ON public.specimen USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath)); -- merge

-- DiagnosticReport indexes for merge/unmerge operations
CREATE INDEX IF NOT EXISTS diagnosticreport_patient_references_idx_mpi ON public.diagnosticreport USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath)); -- merge
CREATE INDEX IF NOT EXISTS diagnosticreport_encounter_references_idx_mpi ON public.diagnosticreport USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Encounter")."id"'::jsonpath)); -- unmerge

-- Encounter indexes for merge operations
CREATE INDEX IF NOT EXISTS encounter_patient_references_idx_mpi ON public.encounter USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath)); -- merge
CREATE INDEX IF NOT EXISTS encounter_identifier_idx_mpi ON public.encounter USING gin ((jsonb_path_query_array(resource, '$."identifier".**."value"')) jsonb_path_ops);

-- Condition indexes for merge/unmerge operations
CREATE INDEX IF NOT EXISTS condition_patient_references_idx_mpi ON public.condition USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath)); -- merge
CREATE INDEX IF NOT EXISTS condition_encounter_references_idx_mpi ON public.condition USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Encounter")."id"'::jsonpath)); -- unmerge

-- Media indexes for merge/unmerge operations
CREATE INDEX IF NOT EXISTS media_patient_references_idx_mpi ON public.media USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath)); -- merge
CREATE INDEX IF NOT EXISTS media_encounter_references_idx_mpi ON public.media USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Encounter")."id"'::jsonpath)); -- unmerge

-- SourceMessage indexes for merge/unmerge operations
CREATE INDEX IF NOT EXISTS sourcemessage_patient_references_idx_mpi ON public.sourcemessage USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Patient")."id"'::jsonpath));
CREATE INDEX IF NOT EXISTS sourcemessage_encounter_references_idx_mpi ON public.sourcemessage USING gin (jsonb_path_query_array(resource, '$.**?(@."resourceType" == "Encounter")."id"'::jsonpath));
```

## Add model to Aidbox

To add a matching model, you need to create a custom resource named `AidboxLinkageModel`.&#x20;

Example of creating an **AidboxLinkageModel**:

```yaml
POST /fhir/AidboxLinkageModel
content-type: text/yaml
accept: text/yaml

features:
  fn:
    - bf: 0
      expr: ' ( l.resource->''name'' IS NULL OR r.resource->''name'' IS NULL )'
    - bf: 13.336495228175629
      expr: l.#name = r.#name
    - bf: 13.104401641242227
      expr: r.#given = l.#family AND l.#given = r.#family
    - bf: 5.36329167966839
      expr: >-
        r.#family = l.#family AND
        length(l.#given) <= 5 AND
        length(r.#given) <= 5 AND
        levenshtein(l.#given, r.#given) <= 2
    - bf: 9.288385498954133
      expr: levenshtein(l.#name, r.#name) <= 2
    - bf: 10.36329167966839
      expr: >-
        r.#given = l.#given AND string_to_array(l.#family, ' ') &&
        string_to_array(r.#family, ' ')
    - bf: 10.36329167966839
      expr: >-
        r.#family = l.#family AND string_to_array(l.#given, ' ') &&
        string_to_array(r.#given, ' ')
    - bf: 2.402276401131933
      expr: r.#given = l.#given
    - else: -12.37233293924643
  dob:
    - bf: 0
      expr: ' ( l.#dob  IS NULL OR r.#dob IS NULL )'
    - bf: 10.59415069916466
      expr: l.#dob = r.#dob
    - bf: 3.9911610470417744
      expr: levenshtein(l.#dob, r.#dob) <= 1
    - bf: 0.5164298695732575
      expr: levenshtein(l.#dob, r.#dob) <= 2
    - else: -10.322063538772698
  telecom:
    - bf: 0
      expr: ' ( l.#telecomArray IS NULL OR r.#telecomArray IS NULL OR array_length(l.#telecomArray, 1) IS NULL OR array_length(r.#telecomArray, 1) IS NULL )'
    - bf: 6.465648574292063
      expr: l.#telecomArray && r.#telecomArray
    - else: -10.517360697819983
  address:
    - bf: 0
      expr: ' ( l.#address IS NULL OR r.#address IS NULL )'
    - bf: 9.236771286242664
      expr: >-
        ((l.#addressLength > r.#addressLength) and (l.#address %>> r.#address))
        or ((l.#addressLength <= r.#addressLength) and (l.#address <<% r.#address))
    - bf: 7.465648574292063
      expr: >-
        (l.#addressLength = r.#addressLength) and (l.#address = r.#address)
    - else: -10.517360697819983
  sex:
    - bf: 0
      expr: ' ( l.#gender IS NULL OR r.#gender IS NULL )'
    - bf: 1.8504082299552485
      expr: ' l.#gender = r.#gender'
    - else: -4.842034404727677
resourceType: AidboxLinkageModel
id: >-
  model
resource: Patient
thresholds:
  auto: 25
  manual: 16
blocks:
  fn:
    var: name
  dob:
    var: dob
  addr:
    sql: (l.#address = r.#address)
vars:
  dob: (#.resource#>>'{birthDate}')
  name: ((#.#family) || ' ' || (#.#given))
  given: (immutable_unaccent_upper(#.resource#>>'{name,0,given,0}'))
  family: (immutable_unaccent_upper(#.resource#>>'{name,0,family}'))
  gender: (#.resource#>>'{gender}')
  address: (immutable_remove_spaces_unaccent_upper(#.resource#>>'{address,0,line,0}'))
  telecomArray: >-
    array(select jsonb_array_elements_text(jsonb_path_query_array( #.resource,
    '$.telecom[*] ? (@.value != "").value')))
  addressLength: (length(#.resource#>>'{address,0,line,0}'))
```

### Matching Model Tuning

The example model is intended for **testing and demonstration purposes** and may not deliver optimal results out of the box.

For production use and reliable, accurate matching on your data, you should:

* **Adapt the model** to reflect your data specifics and your definition of a correct match.
* **Calibrate feature weights** using your real-world data. This step typically involves **machine learning** and **manual expert tuning**.

{% hint style="success" %}
We offer a **professional service** for model training and expert tuning.\
If you need assistance, please [contact us](../../../overview/contact-us.md).
{% endhint %}

### Performance considerations

For fast and accurate matching, consider the following:

* **Database indexes:** If you are working with large volumes of patient records, ensure proper database indexes are created to keep matching fast and scalable.
* **Data normalization:** Matching quality depends heavily on well‑normalized input data. Avoid using placeholders like `"UNKNOWN"` or `"not provided"` for names, addresses, or birthdates, as they negatively impact results.

## Configure Audit Events (Optional)

The MDM module can track and export audit events for compliance and monitoring purposes. When enabled, the system generates FHIR AuditEvent resources for operations like:

* Patient merge/unmerge operations
* Patient search and matching
* Marking/unmarking duplicates
* Patient record creation and viewing

### Enable Audit Worker

To enable audit event collection and export, configure the following environment variables in your backend service:

```bash
# Enable audit worker
MPI_AUDIT_WORKER_ENABLE=true

# URL where audit events will be sent (FHIR Bundle endpoint)
MPI_AUDIT_CONSUMER_URL=http://your-audit-repository:8080/fhir/Bundle

# Polling interval in milliseconds (how often to check for pending events)
MPI_AUDIT_INTERVAL=1000

# Number of events to process per batch
MPI_AUDIT_BATCH_SIZE=10

# PostgreSQL advisory lock ID (prevents concurrent workers)
MPI_AUDIT_LOCK_ID=54321
```

### How it works

1. **Event Collection**: The system creates FHIR AuditEvent resources for auditable operations and stores them in the `mpi.audit_event` table with `send_status = 'pending'`.

2. **Worker Processing**: The audit worker periodically:
   - Fetches pending audit events (up to `batch-size`)
   - Bundles them into a FHIR Bundle (type: "collection")
   - POSTs the bundle to the configured `audit-repository-url`
   - Marks events as `delivered` on successful response (HTTP 2xx)

3. **Event Format**: Each audit event includes:
   - Operation type and outcome (success/failure)
   - User information (from Aidbox IAM)
   - Affected resources (patients, related resources)
   - Timestamp and source system details

### Audit Repository Requirements

The audit events are sent as FHIR AuditEvent resources following the [BALP (Basic Audit Log Patterns)](https://hl7.org/fhir/uv/balp/) specification. You can use any FHIR-compliant audit repository, but we recommend **Auditbox** for optimal integration and audit log management.

{% hint style="success" %}
**Recommended**: Use [Auditbox](https://www.health-samurai.io/auditbox) for comprehensive audit event storage, querying, and compliance reporting with built-in FHIR AuditEvent support.
{% endhint %}

Your audit consumer endpoint should:

- Accept FHIR Bundle resources via HTTP POST
- Support `application/json` content type
- Return HTTP 2xx status for successful processing
- Handle Bundle resources with `type: "collection"` containing AuditEvent entries
- Support FHIR AuditEvent resources (R4 specification)

## Configure Merge/Unmerge Notifications (Optional)

The notification worker sends real-time alerts when patient merge or unmerge operations occur, allowing external systems to react to patient record changes.

### Enable Notification Worker

Configure the following environment variables in your backend service:

```bash
# Enable notification worker
MPI_NOTIFICATION_WORKER_ENABLE=true

# URL where notifications will be sent
MPI_NOTIFICATION_CONSUMER_URL=http://your-consumer-service:9876/notifications

# Polling interval in milliseconds
MPI_NOTIFICATION_INTERVAL=1000

# Number of notifications to process per batch
MPI_NOTIFICATION_BATCH_SIZE=10

# PostgreSQL advisory lock ID (prevents concurrent workers)
MPI_NOTIFICATION_LOCK_ID=12345
```

### How it works

1. **Event Tracking**: When merge/unmerge operations complete, they are marked with `notification_status = 'not_delivered'` in the database.

2. **Worker Processing**: The notification worker periodically:
   - Fetches undelivered merge and unmerge operations (up to `batch-size`)
   - POSTs them to the configured `consumer-url`
   - Marks as `delivered` on successful response (HTTP 2xx)

3. **Notification Payload**: The worker sends a JSON payload containing:

```json
{
  "merges": [
    {
      "id": "merge-id",
      "target-patient-id": "Patient/123",
      "source-patient-id": "Patient/456",
      "related-resources-refs": ["Observation/789", "Encounter/012"],
      "result-patient": { /* FHIR Patient resource */ }
    }
  ],
  "unmerges": [
    {
      "id": "unmerge-id",
      "merge-id": "original-merge-id",
      "source-patient": { /* Restored Patient resource */ },
      "user-id": "user-123",
      "related-resources": ["Observation/789", "Encounter/012"]
    }
  ]
}
```

### Consumer Endpoint Requirements

Your notification consumer endpoint should:

- Accept HTTP POST requests with `Content-Type: application/json`
- Process the payload containing `merges` and `unmerges` arrays
- Return HTTP 2xx status for successful processing
