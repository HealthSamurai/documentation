# Create indexes manually

{% hint style="info" %}
The following indexes cover regular searches, e.g. Patient?name=Smith. Please [contact us ](../../overview/contact-us.md)if you need more examples.&#x20;
{% endhint %}

### Account

Search parameter: `Account.status`, `Account.type`, `Account.subject`, `Account.owner`, `Account.identifier`, `Account.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS account_resource__gin
ON "account"
USING GIN (resource);
```

Search parameter: `Account.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS account_resource__name_gin_trgm
ON "account"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Account.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS account_resource__period_max
ON "account"
USING btree (knife_extract_max_timestamptz(resource, '[["servicePeriod","start"],["servicePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS account_resource__period_min
ON "account"
USING btree (knife_extract_min_timestamptz(resource, '[["servicePeriod","start"],["servicePeriod","end"]]'));
```

### ActivityDefinition

Search parameter: `ActivityDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__publisher_gin_trgm
ON "activitydefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.version`, `ActivityDefinition.context-type`, `ActivityDefinition.status`, `ActivityDefinition.identifier`, `ActivityDefinition.jurisdiction`, `ActivityDefinition.topic`, `ActivityDefinition.composed-of`, `ActivityDefinition.url`, `ActivityDefinition.predecessor`, `ActivityDefinition.context`, `ActivityDefinition.successor`, `ActivityDefinition.derived-from`, `ActivityDefinition.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__gin
ON "activitydefinition"
USING GIN (resource);
```

Search parameter: `ActivityDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__name_gin_trgm
ON "activitydefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__title_gin_trgm
ON "activitydefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__description_gin_trgm
ON "activitydefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__context_quantity_max
ON "activitydefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__context_quantity_min
ON "activitydefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `ActivityDefinition.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__effective_max
ON "activitydefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__effective_min
ON "activitydefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `ActivityDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__date_max
ON "activitydefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__date_min
ON "activitydefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### AdverseEvent

Search parameter: `AdverseEvent.study`, `AdverseEvent.subject`, `AdverseEvent.location`, `AdverseEvent.seriousness`, `AdverseEvent.substance`, `AdverseEvent.recorder`, `AdverseEvent.category`, `AdverseEvent.event`, `AdverseEvent.actuality`, `AdverseEvent.severity`, `AdverseEvent.resultingcondition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS adverseevent_resource__gin
ON "adverseevent"
USING GIN (resource);
```

Search parameter: `AdverseEvent.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS adverseevent_resource__date_max
ON "adverseevent"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS adverseevent_resource__date_min
ON "adverseevent"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### AllergyIntolerance

Search parameter: `AllergyIntolerance.criticality`, `AllergyIntolerance.type`, `AllergyIntolerance.category`, `AllergyIntolerance.route`, `AllergyIntolerance.patient`, `AllergyIntolerance.identifier`, `AllergyIntolerance.verification-status`, `AllergyIntolerance.severity`, `AllergyIntolerance.manifestation`, `AllergyIntolerance.asserter`, `AllergyIntolerance.recorder`, `AllergyIntolerance.clinical-status`, `AllergyIntolerance.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__gin
ON "allergyintolerance"
USING GIN (resource);
```

Search parameter: `AllergyIntolerance.last-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__last_date_max
ON "allergyintolerance"
USING btree (knife_extract_max_timestamptz(resource, '[["lastOccurrence"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__last_date_min
ON "allergyintolerance"
USING btree (knife_extract_min_timestamptz(resource, '[["lastOccurrence"]]'));
```

Search parameter: `AllergyIntolerance.onset`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__onset_max
ON "allergyintolerance"
USING btree (knife_extract_max_timestamptz(resource, '[["reaction","onset"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__onset_min
ON "allergyintolerance"
USING btree (knife_extract_min_timestamptz(resource, '[["reaction","onset"]]'));
```

Search parameter: `AllergyIntolerance.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__date_max
ON "allergyintolerance"
USING btree (knife_extract_max_timestamptz(resource, '[["recordedDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__date_min
ON "allergyintolerance"
USING btree (knife_extract_min_timestamptz(resource, '[["recordedDate"]]'));
```

### Appointment

Search parameter: `Appointment.part-status`, `Appointment.slot`, `Appointment.supporting-info`, `Appointment.based-on`, `Appointment.reason-code`, `Appointment.identifier`, `Appointment.specialty`, `Appointment.status`, `Appointment.service-category`, `Appointment.appointment-type`, `Appointment.actor`, `Appointment.reason-reference`, `Appointment.practitioner`, `Appointment.service-type`, `Appointment.patient`, `Appointment.location`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS appointment_resource__gin
ON "appointment"
USING GIN (resource);
```

Search parameter: `Appointment.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS appointment_resource__date_max
ON "appointment"
USING btree (knife_extract_max_timestamptz(resource, '[["start"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS appointment_resource__date_min
ON "appointment"
USING btree (knife_extract_min_timestamptz(resource, '[["start"]]'));
```

### AppointmentResponse

Search parameter: `AppointmentResponse.practitioner`, `AppointmentResponse.actor`, `AppointmentResponse.part-status`, `AppointmentResponse.patient`, `AppointmentResponse.appointment`, `AppointmentResponse.identifier`, `AppointmentResponse.location`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS appointmentresponse_resource__gin
ON "appointmentresponse"
USING GIN (resource);
```

### AuditEvent

Search parameter: `AuditEvent.action`, `AuditEvent.source`, `AuditEvent.type`, `AuditEvent.agent-role`, `AuditEvent.policy`, `AuditEvent.entity-type`, `AuditEvent.outcome`, `AuditEvent.agent`, `AuditEvent.entity-role`, `AuditEvent.altid`, `AuditEvent.subtype`, `AuditEvent.entity`, `AuditEvent.site`, `AuditEvent.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__gin
ON "auditevent"
USING GIN (resource);
```

Search parameter: `AuditEvent.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__address_gin_trgm
ON "auditevent"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["agent","network","address"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `AuditEvent.entity-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__entity_name_gin_trgm
ON "auditevent"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["entity","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `AuditEvent.agent-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__agent_name_gin_trgm
ON "auditevent"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["agent","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `AuditEvent.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__date_max
ON "auditevent"
USING btree (knife_extract_max_timestamptz(resource, '[["recorded"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__date_min
ON "auditevent"
USING btree (knife_extract_min_timestamptz(resource, '[["recorded"]]'));
```

### Basic

Search parameter: `Basic.subject`, `Basic.author`, `Basic.identifier`, `Basic.patient`, `Basic.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS basic_resource__gin
ON "basic"
USING GIN (resource);
```

Search parameter: `Basic.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS basic_resource__created_max
ON "basic"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS basic_resource__created_min
ON "basic"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### BodyStructure

Search parameter: `BodyStructure.location`, `BodyStructure.morphology`, `BodyStructure.patient`, `BodyStructure.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS bodystructure_resource__gin
ON "bodystructure"
USING GIN (resource);
```

### Bundle

Search parameter: `Bundle.type`, `Bundle.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS bundle_resource__gin
ON "bundle"
USING GIN (resource);
```

Search parameter: `Bundle.composition`

```sql
NOT SUPPORTED
```

Search parameter: `Bundle.message`

```sql
NOT SUPPORTED
```

Search parameter: `Bundle.timestamp`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS bundle_resource__timestamp_max
ON "bundle"
USING btree (knife_extract_max_timestamptz(resource, '[["timestamp"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS bundle_resource__timestamp_min
ON "bundle"
USING btree (knife_extract_min_timestamptz(resource, '[["timestamp"]]'));
```

### CarePlan

Search parameter: `CarePlan.goal`, `CarePlan.category`, `CarePlan.status`, `CarePlan.identifier`, `CarePlan.condition`, `CarePlan.activity-code`, `CarePlan.instantiates-uri`, `CarePlan.performer`, `CarePlan.instantiates-canonical`, `CarePlan.patient`, `CarePlan.intent`, `CarePlan.encounter`, `CarePlan.subject`, `CarePlan.part-of`, `CarePlan.activity-reference`, `CarePlan.based-on`, `CarePlan.replaces`, `CarePlan.care-team`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careplan_resource__gin
ON "careplan"
USING GIN (resource);
```

Search parameter: `CarePlan.activity-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careplan_resource__activity_date_max
ON "careplan"
USING btree (knife_extract_max_timestamptz(resource, '[["activity","detail","scheduled","Period","start"],["activity","detail","scheduled","Period","end"],["activity","detail","scheduled","string"],["activity","detail","scheduled","Timing","event"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS careplan_resource__activity_date_min
ON "careplan"
USING btree (knife_extract_min_timestamptz(resource, '[["activity","detail","scheduled","Period","start"],["activity","detail","scheduled","Period","end"],["activity","detail","scheduled","string"],["activity","detail","scheduled","Timing","event"]]'));
```

Search parameter: `CarePlan.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careplan_resource__date_max
ON "careplan"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS careplan_resource__date_min
ON "careplan"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### CareTeam

Search parameter: `CareTeam.identifier`, `CareTeam.subject`, `CareTeam.category`, `CareTeam.patient`, `CareTeam.participant`, `CareTeam.encounter`, `CareTeam.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careteam_resource__gin
ON "careteam"
USING GIN (resource);
```

Search parameter: `CareTeam.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careteam_resource__date_max
ON "careteam"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS careteam_resource__date_min
ON "careteam"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### ChargeItem

Search parameter: `ChargeItem.subject`, `ChargeItem.performer-function`, `ChargeItem.performer-actor`, `ChargeItem.enterer`, `ChargeItem.context`, `ChargeItem.requesting-organization`, `ChargeItem.account`, `ChargeItem.performing-organization`, `ChargeItem.patient`, `ChargeItem.identifier`, `ChargeItem.code`, `ChargeItem.service`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__gin
ON "chargeitem"
USING GIN (resource);
```

Search parameter: `ChargeItem.entered-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__entered_date_max
ON "chargeitem"
USING btree (knife_extract_max_timestamptz(resource, '[["enteredDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__entered_date_min
ON "chargeitem"
USING btree (knife_extract_min_timestamptz(resource, '[["enteredDate"]]'));
```

Search parameter: `ChargeItem.price-override`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__price_override_max
ON "chargeitem"
USING btree (knife_extract_max_numeric(resource, '[["priceOverride","value"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__price_override_min
ON "chargeitem"
USING btree (knife_extract_min_numeric(resource, '[["priceOverride","value"]]'));
```

Search parameter: `ChargeItem.quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__quantity_max
ON "chargeitem"
USING btree (knife_extract_max_numeric(resource, '[["quantity","value"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__quantity_min
ON "chargeitem"
USING btree (knife_extract_min_numeric(resource, '[["quantity","value"]]'));
```

Search parameter: `ChargeItem.occurrence`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__occurrence_max
ON "chargeitem"
USING btree (knife_extract_max_timestamptz(resource, '[["occurrence","Period","start"],["occurrence","Period","end"],["occurrence","dateTime"],["occurrence","Timing","event"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__occurrence_min
ON "chargeitem"
USING btree (knife_extract_min_timestamptz(resource, '[["occurrence","Period","start"],["occurrence","Period","end"],["occurrence","dateTime"],["occurrence","Timing","event"]]'));
```

Search parameter: `ChargeItem.factor-override`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__factor_override_max
ON "chargeitem"
USING btree (knife_extract_max_numeric(resource, '[["factorOverride"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__factor_override_min
ON "chargeitem"
USING btree (knife_extract_min_numeric(resource, '[["factorOverride"]]'));
```

### ChargeItemDefinition

Search parameter: `ChargeItemDefinition.identifier`, `ChargeItemDefinition.context-type`, `ChargeItemDefinition.url`, `ChargeItemDefinition.context`, `ChargeItemDefinition.status`, `ChargeItemDefinition.jurisdiction`, `ChargeItemDefinition.version`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__gin
ON "chargeitemdefinition"
USING GIN (resource);
```

Search parameter: `ChargeItemDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__publisher_gin_trgm
ON "chargeitemdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ChargeItemDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__title_gin_trgm
ON "chargeitemdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ChargeItemDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__description_gin_trgm
ON "chargeitemdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ChargeItemDefinition.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__effective_max
ON "chargeitemdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__effective_min
ON "chargeitemdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `ChargeItemDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__context_quantity_max
ON "chargeitemdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__context_quantity_min
ON "chargeitemdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `ChargeItemDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__date_max
ON "chargeitemdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__date_min
ON "chargeitemdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Claim

Search parameter: `Claim.provider`, `Claim.patient`, `Claim.insurer`, `Claim.facility`, `Claim.encounter`, `Claim.care-team`, `Claim.identifier`, `Claim.payee`, `Claim.subdetail-udi`, `Claim.enterer`, `Claim.detail-udi`, `Claim.item-udi`, `Claim.procedure-udi`, `Claim.use`, `Claim.status`, `Claim.priority`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claim_resource__gin
ON "claim"
USING GIN (resource);
```

Search parameter: `Claim.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claim_resource__created_max
ON "claim"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS claim_resource__created_min
ON "claim"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### ClaimResponse

Search parameter: `ClaimResponse.insurer`, `ClaimResponse.use`, `ClaimResponse.patient`, `ClaimResponse.outcome`, `ClaimResponse.status`, `ClaimResponse.identifier`, `ClaimResponse.request`, `ClaimResponse.requestor`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__gin
ON "claimresponse"
USING GIN (resource);
```

Search parameter: `ClaimResponse.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__disposition_gin_trgm
ON "claimresponse"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ClaimResponse.payment-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__payment_date_max
ON "claimresponse"
USING btree (knife_extract_max_timestamptz(resource, '[["payment","date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__payment_date_min
ON "claimresponse"
USING btree (knife_extract_min_timestamptz(resource, '[["payment","date"]]'));
```

Search parameter: `ClaimResponse.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__created_max
ON "claimresponse"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__created_min
ON "claimresponse"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### ClinicalImpression

Search parameter: `ClinicalImpression.subject`, `ClinicalImpression.finding-code`, `ClinicalImpression.previous`, `ClinicalImpression.finding-ref`, `ClinicalImpression.encounter`, `ClinicalImpression.investigation`, `ClinicalImpression.problem`, `ClinicalImpression.status`, `ClinicalImpression.patient`, `ClinicalImpression.identifier`, `ClinicalImpression.assessor`, `ClinicalImpression.supporting-info`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS clinicalimpression_resource__gin
ON "clinicalimpression"
USING GIN (resource);
```

Search parameter: `ClinicalImpression.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS clinicalimpression_resource__date_max
ON "clinicalimpression"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS clinicalimpression_resource__date_min
ON "clinicalimpression"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### CodeSystem

Search parameter: `CodeSystem.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__name_gin_trgm
ON "codesystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.supplements`, `CodeSystem.language`, `CodeSystem.jurisdiction`, `CodeSystem.version`, `CodeSystem.context-type`, `CodeSystem.identifier`, `CodeSystem.status`, `CodeSystem.context`, `CodeSystem.code`, `CodeSystem.url`, `CodeSystem.content-mode`, `CodeSystem.system`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__gin
ON "codesystem"
USING GIN (resource);
```

Search parameter: `CodeSystem.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__description_gin_trgm
ON "codesystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__title_gin_trgm
ON "codesystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__publisher_gin_trgm
ON "codesystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__context_quantity_max
ON "codesystem"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__context_quantity_min
ON "codesystem"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `CodeSystem.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__date_max
ON "codesystem"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__date_min
ON "codesystem"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Communication

Search parameter: `Communication.sender`, `Communication.category`, `Communication.recipient`, `Communication.instantiates-uri`, `Communication.identifier`, `Communication.medium`, `Communication.part-of`, `Communication.instantiates-canonical`, `Communication.subject`, `Communication.based-on`, `Communication.patient`, `Communication.encounter`, `Communication.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communication_resource__gin
ON "communication"
USING GIN (resource);
```

Search parameter: `Communication.received`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communication_resource__received_max
ON "communication"
USING btree (knife_extract_max_timestamptz(resource, '[["received"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS communication_resource__received_min
ON "communication"
USING btree (knife_extract_min_timestamptz(resource, '[["received"]]'));
```

Search parameter: `Communication.sent`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communication_resource__sent_max
ON "communication"
USING btree (knife_extract_max_timestamptz(resource, '[["sent"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS communication_resource__sent_min
ON "communication"
USING btree (knife_extract_min_timestamptz(resource, '[["sent"]]'));
```

### CommunicationRequest

Search parameter: `CommunicationRequest.patient`, `CommunicationRequest.identifier`, `CommunicationRequest.group-identifier`, `CommunicationRequest.encounter`, `CommunicationRequest.requester`, `CommunicationRequest.replaces`, `CommunicationRequest.based-on`, `CommunicationRequest.status`, `CommunicationRequest.priority`, `CommunicationRequest.sender`, `CommunicationRequest.medium`, `CommunicationRequest.category`, `CommunicationRequest.subject`, `CommunicationRequest.recipient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communicationrequest_resource__gin
ON "communicationrequest"
USING GIN (resource);
```

Search parameter: `CommunicationRequest.authored`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communicationrequest_resource__authored_max
ON "communicationrequest"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS communicationrequest_resource__authored_min
ON "communicationrequest"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

Search parameter: `CommunicationRequest.occurrence`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communicationrequest_resource__occurrence_max
ON "communicationrequest"
USING btree (knife_extract_max_timestamptz(resource, '[["occurrence","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS communicationrequest_resource__occurrence_min
ON "communicationrequest"
USING btree (knife_extract_min_timestamptz(resource, '[["occurrence","dateTime"]]'));
```

### CompartmentDefinition

Search parameter: `CompartmentDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__publisher_gin_trgm
ON "compartmentdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CompartmentDefinition.code`, `CompartmentDefinition.context-type`, `CompartmentDefinition.status`, `CompartmentDefinition.url`, `CompartmentDefinition.context`, `CompartmentDefinition.resource`, `CompartmentDefinition.version`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__gin
ON "compartmentdefinition"
USING GIN (resource);
```

Search parameter: `CompartmentDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__name_gin_trgm
ON "compartmentdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CompartmentDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__description_gin_trgm
ON "compartmentdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CompartmentDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__context_quantity_max
ON "compartmentdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__context_quantity_min
ON "compartmentdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `CompartmentDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__date_max
ON "compartmentdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__date_min
ON "compartmentdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Composition

Search parameter: `Composition.encounter`, `Composition.context`, `Composition.patient`, `Composition.author`, `Composition.attester`, `Composition.related-ref`, `Composition.related-id`, `Composition.identifier`, `Composition.type`, `Composition.subject`, `Composition.status`, `Composition.section`, `Composition.category`, `Composition.entry`, `Composition.confidentiality`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__gin
ON "composition"
USING GIN (resource);
```

Search parameter: `Composition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__title_gin_trgm
ON "composition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Composition.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__period_max
ON "composition"
USING btree (knife_extract_max_timestamptz(resource, '[["event","period","start"],["event","period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__period_min
ON "composition"
USING btree (knife_extract_min_timestamptz(resource, '[["event","period","start"],["event","period","end"]]'));
```

Search parameter: `Composition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__date_max
ON "composition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__date_min
ON "composition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### ConceptMap

Search parameter: `ConceptMap.source-uri`, `ConceptMap.context`, `ConceptMap.target-uri`, `ConceptMap.source-system`, `ConceptMap.jurisdiction`, `ConceptMap.product`, `ConceptMap.target`, `ConceptMap.identifier`, `ConceptMap.version`, `ConceptMap.url`, `ConceptMap.source`, `ConceptMap.status`, `ConceptMap.target-system`, `ConceptMap.source-code`, `ConceptMap.dependson`, `ConceptMap.context-type`, `ConceptMap.other`, `ConceptMap.target-code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__gin
ON "conceptmap"
USING GIN (resource);
```

Search parameter: `ConceptMap.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__publisher_gin_trgm
ON "conceptmap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__description_gin_trgm
ON "conceptmap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__name_gin_trgm
ON "conceptmap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__title_gin_trgm
ON "conceptmap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__date_max
ON "conceptmap"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__date_min
ON "conceptmap"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `ConceptMap.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__context_quantity_max
ON "conceptmap"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__context_quantity_min
ON "conceptmap"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### Condition

Search parameter: `Condition.subject`, `Condition.clinical-status`, `Condition.patient`, `Condition.encounter`, `Condition.severity`, `Condition.code`, `Condition.evidence`, `Condition.body-site`, `Condition.asserter`, `Condition.evidence-detail`, `Condition.stage`, `Condition.verification-status`, `Condition.category`, `Condition.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__gin
ON "condition"
USING GIN (resource);
```

Search parameter: `Condition.onset-info`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__onset_info_gin_trgm
ON "condition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["onset","string"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Condition.abatement-string`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__abatement_string_gin_trgm
ON "condition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["abatement","string"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Condition.onset-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__onset_date_max
ON "condition"
USING btree (knife_extract_max_timestamptz(resource, '[["onset","dateTime"],["onset","Period","start"],["onset","Period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__onset_date_min
ON "condition"
USING btree (knife_extract_min_timestamptz(resource, '[["onset","dateTime"],["onset","Period","start"],["onset","Period","end"]]'));
```

Search parameter: `Condition.recorded-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__recorded_date_max
ON "condition"
USING btree (knife_extract_max_timestamptz(resource, '[["recordedDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__recorded_date_min
ON "condition"
USING btree (knife_extract_min_timestamptz(resource, '[["recordedDate"]]'));
```

Search parameter: `Condition.onset-age`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__onset_age_max
ON "condition"
USING btree (knife_extract_max_numeric(resource, '[["onset","Age"],["onset","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__onset_age_min
ON "condition"
USING btree (knife_extract_min_numeric(resource, '[["onset","Age"],["onset","Range"]]'));
```

Search parameter: `Condition.abatement-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__abatement_date_max
ON "condition"
USING btree (knife_extract_max_timestamptz(resource, '[["abatement","dateTime"],["abatement","Period","start"],["abatement","Period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__abatement_date_min
ON "condition"
USING btree (knife_extract_min_timestamptz(resource, '[["abatement","dateTime"],["abatement","Period","start"],["abatement","Period","end"]]'));
```

Search parameter: `Condition.abatement-age`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__abatement_age_max
ON "condition"
USING btree (knife_extract_max_numeric(resource, '[["abatement","Age"],["abatement","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__abatement_age_min
ON "condition"
USING btree (knife_extract_min_numeric(resource, '[["abatement","Age"],["abatement","Range"]]'));
```

### Consent

Search parameter: `Consent.organization`, `Consent.actor`, `Consent.patient`, `Consent.security-label`, `Consent.data`, `Consent.consentor`, `Consent.scope`, `Consent.category`, `Consent.identifier`, `Consent.purpose`, `Consent.status`, `Consent.action`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS consent_resource__gin
ON "consent"
USING GIN (resource);
```

Search parameter: `Consent.source-reference`

```sql
NOT SUPPORTED
```

Search parameter: `Consent.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS consent_resource__date_max
ON "consent"
USING btree (knife_extract_max_timestamptz(resource, '[["dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS consent_resource__date_min
ON "consent"
USING btree (knife_extract_min_timestamptz(resource, '[["dateTime"]]'));
```

Search parameter: `Consent.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS consent_resource__period_max
ON "consent"
USING btree (knife_extract_max_timestamptz(resource, '[["provision","period","start"],["provision","period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS consent_resource__period_min
ON "consent"
USING btree (knife_extract_min_timestamptz(resource, '[["provision","period","start"],["provision","period","end"]]'));
```

### Contract

Search parameter: `Contract.authority`, `Contract.status`, `Contract.url`, `Contract.identifier`, `Contract.subject`, `Contract.domain`, `Contract.signer`, `Contract.instantiates`, `Contract.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS contract_resource__gin
ON "contract"
USING GIN (resource);
```

Search parameter: `Contract.issued`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS contract_resource__issued_max
ON "contract"
USING btree (knife_extract_max_timestamptz(resource, '[["issued"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS contract_resource__issued_min
ON "contract"
USING btree (knife_extract_min_timestamptz(resource, '[["issued"]]'));
```

### Coverage

Search parameter: `Coverage.status`, `Coverage.identifier`, `Coverage.policy-holder`, `Coverage.patient`, `Coverage.subscriber`, `Coverage.beneficiary`, `Coverage.type`, `Coverage.payor`, `Coverage.class-type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverage_resource__gin
ON "coverage"
USING GIN (resource);
```

Search parameter: `Coverage.class-value`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverage_resource__class_value_gin_trgm
ON "coverage"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["class","value"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Coverage.dependent`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverage_resource__dependent_gin_trgm
ON "coverage"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["dependent"]]$JSON$))) gin_trgm_ops);
```

### CoverageEligibilityRequest

Search parameter: `CoverageEligibilityRequest.identifier`, `CoverageEligibilityRequest.provider`, `CoverageEligibilityRequest.facility`, `CoverageEligibilityRequest.enterer`, `CoverageEligibilityRequest.status`, `CoverageEligibilityRequest.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityrequest_resource__gin
ON "coverageeligibilityrequest"
USING GIN (resource);
```

Search parameter: `CoverageEligibilityRequest.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityrequest_resource__created_max
ON "coverageeligibilityrequest"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityrequest_resource__created_min
ON "coverageeligibilityrequest"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### CoverageEligibilityResponse

Search parameter: `CoverageEligibilityResponse.patient`, `CoverageEligibilityResponse.status`, `CoverageEligibilityResponse.identifier`, `CoverageEligibilityResponse.insurer`, `CoverageEligibilityResponse.request`, `CoverageEligibilityResponse.requestor`, `CoverageEligibilityResponse.outcome`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityresponse_resource__gin
ON "coverageeligibilityresponse"
USING GIN (resource);
```

Search parameter: `CoverageEligibilityResponse.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityresponse_resource__disposition_gin_trgm
ON "coverageeligibilityresponse"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CoverageEligibilityResponse.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityresponse_resource__created_max
ON "coverageeligibilityresponse"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityresponse_resource__created_min
ON "coverageeligibilityresponse"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### DetectedIssue

Search parameter: `DetectedIssue.implicated`, `DetectedIssue.identifier`, `DetectedIssue.code`, `DetectedIssue.author`, `DetectedIssue.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS detectedissue_resource__gin
ON "detectedissue"
USING GIN (resource);
```

Search parameter: `DetectedIssue.identified`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS detectedissue_resource__identified_max
ON "detectedissue"
USING btree (knife_extract_max_timestamptz(resource, '[["identified","Period","start"],["identified","Period","end"],["identified","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS detectedissue_resource__identified_min
ON "detectedissue"
USING btree (knife_extract_min_timestamptz(resource, '[["identified","Period","start"],["identified","Period","end"],["identified","dateTime"]]'));
```

### Device

Search parameter: `Device.udi-carrier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__udi_carrier_gin_trgm
ON "device"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["udiCarrier","carrierHRF"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.manufacturer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__manufacturer_gin_trgm
ON "device"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["manufacturer"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.url`, `Device.type`, `Device.status`, `Device.identifier`, `Device.organization`, `Device.location`, `Device.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__gin
ON "device"
USING GIN (resource);
```

Search parameter: `Device.udi-di`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__udi_di_gin_trgm
ON "device"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["udiCarrier","deviceIdentifier"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.model`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__model_gin_trgm
ON "device"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["modelNumber"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.device-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__device_name_gin_trgm
ON "device"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["deviceName","name"],["type","coding","display"],["type","text"]]$JSON$))) gin_trgm_ops);
```

### DeviceDefinition

Search parameter: `DeviceDefinition.identifier`, `DeviceDefinition.type`, `DeviceDefinition.parent`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicedefinition_resource__gin
ON "devicedefinition"
USING GIN (resource);
```

### DeviceMetric

Search parameter: `DeviceMetric.category`, `DeviceMetric.identifier`, `DeviceMetric.source`, `DeviceMetric.type`, `DeviceMetric.parent`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicemetric_resource__gin
ON "devicemetric"
USING GIN (resource);
```

### DeviceRequest

Search parameter: `DeviceRequest.performer`, `DeviceRequest.instantiates-canonical`, `DeviceRequest.patient`, `DeviceRequest.subject`, `DeviceRequest.code`, `DeviceRequest.prior-request`, `DeviceRequest.group-identifier`, `DeviceRequest.identifier`, `DeviceRequest.intent`, `DeviceRequest.encounter`, `DeviceRequest.based-on`, `DeviceRequest.status`, `DeviceRequest.requester`, `DeviceRequest.insurance`, `DeviceRequest.device`, `DeviceRequest.instantiates-uri`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicerequest_resource__gin
ON "devicerequest"
USING GIN (resource);
```

Search parameter: `DeviceRequest.event-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicerequest_resource__event_date_max
ON "devicerequest"
USING btree (knife_extract_max_timestamptz(resource, '[["occurrence","dateTime"],["occurrence","Period","start"],["occurrence","Period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicerequest_resource__event_date_min
ON "devicerequest"
USING btree (knife_extract_min_timestamptz(resource, '[["occurrence","dateTime"],["occurrence","Period","start"],["occurrence","Period","end"]]'));
```

Search parameter: `DeviceRequest.authored-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicerequest_resource__authored_on_max
ON "devicerequest"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicerequest_resource__authored_on_min
ON "devicerequest"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

### DeviceUseStatement

Search parameter: `DeviceUseStatement.patient`, `DeviceUseStatement.device`, `DeviceUseStatement.identifier`, `DeviceUseStatement.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS deviceusestatement_resource__gin
ON "deviceusestatement"
USING GIN (resource);
```

### DiagnosticReport

Search parameter: `DiagnosticReport.specimen`, `DiagnosticReport.patient`, `DiagnosticReport.status`, `DiagnosticReport.code`, `DiagnosticReport.category`, `DiagnosticReport.based-on`, `DiagnosticReport.performer`, `DiagnosticReport.encounter`, `DiagnosticReport.result`, `DiagnosticReport.media`, `DiagnosticReport.identifier`, `DiagnosticReport.results-interpreter`, `DiagnosticReport.conclusion`, `DiagnosticReport.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS diagnosticreport_resource__gin
ON "diagnosticreport"
USING GIN (resource);
```

Search parameter: `DiagnosticReport.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS diagnosticreport_resource__date_max
ON "diagnosticreport"
USING btree (knife_extract_max_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS diagnosticreport_resource__date_min
ON "diagnosticreport"
USING btree (knife_extract_min_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"]]'));
```

Search parameter: `DiagnosticReport.issued`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS diagnosticreport_resource__issued_max
ON "diagnosticreport"
USING btree (knife_extract_max_timestamptz(resource, '[["issued"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS diagnosticreport_resource__issued_min
ON "diagnosticreport"
USING btree (knife_extract_min_timestamptz(resource, '[["issued"]]'));
```

### DocumentManifest

Search parameter: `DocumentManifest.source`, `DocumentManifest.author`, `DocumentManifest.type`, `DocumentManifest.status`, `DocumentManifest.subject`, `DocumentManifest.item`, `DocumentManifest.recipient`, `DocumentManifest.related-ref`, `DocumentManifest.related-id`, `DocumentManifest.patient`, `DocumentManifest.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentmanifest_resource__gin
ON "documentmanifest"
USING GIN (resource);
```

Search parameter: `DocumentManifest.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentmanifest_resource__description_gin_trgm
ON "documentmanifest"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `DocumentManifest.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentmanifest_resource__created_max
ON "documentmanifest"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentmanifest_resource__created_min
ON "documentmanifest"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### DocumentReference

Search parameter: `DocumentReference.type`, `DocumentReference.author`, `DocumentReference.custodian`, `DocumentReference.related`, `DocumentReference.patient`, `DocumentReference.location`, `DocumentReference.event`, `DocumentReference.relation`, `DocumentReference.relatesto`, `DocumentReference.status`, `DocumentReference.encounter`, `DocumentReference.setting`, `DocumentReference.subject`, `DocumentReference.language`, `DocumentReference.category`, `DocumentReference.format`, `DocumentReference.facility`, `DocumentReference.authenticator`, `DocumentReference.security-label`, `DocumentReference.contenttype`, `DocumentReference.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__gin
ON "documentreference"
USING GIN (resource);
```

Search parameter: `DocumentReference.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__description_gin_trgm
ON "documentreference"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `DocumentReference.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__period_max
ON "documentreference"
USING btree (knife_extract_max_timestamptz(resource, '[["context","period","start"],["context","period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__period_min
ON "documentreference"
USING btree (knife_extract_min_timestamptz(resource, '[["context","period","start"],["context","period","end"]]'));
```

Search parameter: `DocumentReference.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__date_max
ON "documentreference"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__date_min
ON "documentreference"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### EffectEvidenceSynthesis

Search parameter: `EffectEvidenceSynthesis.status`, `EffectEvidenceSynthesis.jurisdiction`, `EffectEvidenceSynthesis.identifier`, `EffectEvidenceSynthesis.url`, `EffectEvidenceSynthesis.version`, `EffectEvidenceSynthesis.context-type`, `EffectEvidenceSynthesis.context`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__gin
ON "effectevidencesynthesis"
USING GIN (resource);
```

Search parameter: `EffectEvidenceSynthesis.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__publisher_gin_trgm
ON "effectevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__description_gin_trgm
ON "effectevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__title_gin_trgm
ON "effectevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__name_gin_trgm
ON "effectevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__effective_max
ON "effectevidencesynthesis"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__effective_min
ON "effectevidencesynthesis"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `EffectEvidenceSynthesis.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__date_max
ON "effectevidencesynthesis"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__date_min
ON "effectevidencesynthesis"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `EffectEvidenceSynthesis.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__context_quantity_max
ON "effectevidencesynthesis"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__context_quantity_min
ON "effectevidencesynthesis"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### Encounter

Search parameter: `Encounter.type`, `Encounter.episode-of-care`, `Encounter.appointment`, `Encounter.diagnosis`, `Encounter.special-arrangement`, `Encounter.practitioner`, `Encounter.subject`, `Encounter.account`, `Encounter.class`, `Encounter.location`, `Encounter.patient`, `Encounter.part-of`, `Encounter.identifier`, `Encounter.based-on`, `Encounter.status`, `Encounter.reason-code`, `Encounter.participant`, `Encounter.participant-type`, `Encounter.reason-reference`, `Encounter.service-provider`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__gin
ON "encounter"
USING GIN (resource);
```

Search parameter: `Encounter.length`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__length_max
ON "encounter"
USING btree (knife_extract_max_numeric(resource, '[["length"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__length_min
ON "encounter"
USING btree (knife_extract_min_numeric(resource, '[["length"]]'));
```

Search parameter: `Encounter.location-period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__location_period_max
ON "encounter"
USING btree (knife_extract_max_timestamptz(resource, '[["location","period","start"],["location","period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__location_period_min
ON "encounter"
USING btree (knife_extract_min_timestamptz(resource, '[["location","period","start"],["location","period","end"]]'));
```

Search parameter: `Encounter.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__date_max
ON "encounter"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__date_min
ON "encounter"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### Endpoint

Search parameter: `Endpoint.identifier`, `Endpoint.status`, `Endpoint.organization`, `Endpoint.connection-type`, `Endpoint.payload-type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS endpoint_resource__gin
ON "endpoint"
USING GIN (resource);
```

Search parameter: `Endpoint.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS endpoint_resource__name_gin_trgm
ON "endpoint"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### EnrollmentRequest

Search parameter: `EnrollmentRequest.patient`, `EnrollmentRequest.status`, `EnrollmentRequest.identifier`, `EnrollmentRequest.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS enrollmentrequest_resource__gin
ON "enrollmentrequest"
USING GIN (resource);
```

### EnrollmentResponse

Search parameter: `EnrollmentResponse.identifier`, `EnrollmentResponse.request`, `EnrollmentResponse.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS enrollmentresponse_resource__gin
ON "enrollmentresponse"
USING GIN (resource);
```

### EpisodeOfCare

Search parameter: `EpisodeOfCare.care-manager`, `EpisodeOfCare.type`, `EpisodeOfCare.organization`, `EpisodeOfCare.identifier`, `EpisodeOfCare.condition`, `EpisodeOfCare.status`, `EpisodeOfCare.incoming-referral`, `EpisodeOfCare.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS episodeofcare_resource__gin
ON "episodeofcare"
USING GIN (resource);
```

Search parameter: `EpisodeOfCare.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS episodeofcare_resource__date_max
ON "episodeofcare"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS episodeofcare_resource__date_min
ON "episodeofcare"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### EventDefinition

Search parameter: `EventDefinition.depends-on`, `EventDefinition.successor`, `EventDefinition.identifier`, `EventDefinition.composed-of`, `EventDefinition.predecessor`, `EventDefinition.context`, `EventDefinition.jurisdiction`, `EventDefinition.status`, `EventDefinition.context-type`, `EventDefinition.version`, `EventDefinition.topic`, `EventDefinition.url`, `EventDefinition.derived-from`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__gin
ON "eventdefinition"
USING GIN (resource);
```

Search parameter: `EventDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__description_gin_trgm
ON "eventdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__name_gin_trgm
ON "eventdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__title_gin_trgm
ON "eventdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__publisher_gin_trgm
ON "eventdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__context_quantity_max
ON "eventdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__context_quantity_min
ON "eventdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `EventDefinition.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__effective_max
ON "eventdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__effective_min
ON "eventdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `EventDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__date_max
ON "eventdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__date_min
ON "eventdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Evidence

Search parameter: `Evidence.successor`, `Evidence.jurisdiction`, `Evidence.depends-on`, `Evidence.context`, `Evidence.context-type`, `Evidence.composed-of`, `Evidence.predecessor`, `Evidence.identifier`, `Evidence.topic`, `Evidence.status`, `Evidence.version`, `Evidence.url`, `Evidence.derived-from`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__gin
ON "evidence"
USING GIN (resource);
```

Search parameter: `Evidence.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__publisher_gin_trgm
ON "evidence"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__name_gin_trgm
ON "evidence"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__title_gin_trgm
ON "evidence"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__description_gin_trgm
ON "evidence"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__context_quantity_max
ON "evidence"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__context_quantity_min
ON "evidence"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `Evidence.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__effective_max
ON "evidence"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__effective_min
ON "evidence"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `Evidence.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__date_max
ON "evidence"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__date_min
ON "evidence"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### EvidenceVariable

Search parameter: `EvidenceVariable.context-type`, `EvidenceVariable.predecessor`, `EvidenceVariable.identifier`, `EvidenceVariable.derived-from`, `EvidenceVariable.composed-of`, `EvidenceVariable.version`, `EvidenceVariable.successor`, `EvidenceVariable.context`, `EvidenceVariable.topic`, `EvidenceVariable.status`, `EvidenceVariable.url`, `EvidenceVariable.jurisdiction`, `EvidenceVariable.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__gin
ON "evidencevariable"
USING GIN (resource);
```

Search parameter: `EvidenceVariable.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__publisher_gin_trgm
ON "evidencevariable"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__name_gin_trgm
ON "evidencevariable"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__description_gin_trgm
ON "evidencevariable"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__title_gin_trgm
ON "evidencevariable"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__date_max
ON "evidencevariable"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__date_min
ON "evidencevariable"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `EvidenceVariable.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__effective_max
ON "evidencevariable"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__effective_min
ON "evidencevariable"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `EvidenceVariable.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__context_quantity_max
ON "evidencevariable"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__context_quantity_min
ON "evidencevariable"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### ExampleScenario

Search parameter: `ExampleScenario.identifier`, `ExampleScenario.context`, `ExampleScenario.url`, `ExampleScenario.status`, `ExampleScenario.context-type`, `ExampleScenario.jurisdiction`, `ExampleScenario.version`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__gin
ON "examplescenario"
USING GIN (resource);
```

Search parameter: `ExampleScenario.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__name_gin_trgm
ON "examplescenario"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ExampleScenario.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__publisher_gin_trgm
ON "examplescenario"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ExampleScenario.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__context_quantity_max
ON "examplescenario"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__context_quantity_min
ON "examplescenario"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `ExampleScenario.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__date_max
ON "examplescenario"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__date_min
ON "examplescenario"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### ExplanationOfBenefit

Search parameter: `ExplanationOfBenefit.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS explanationofbenefit_resource__disposition_gin_trgm
ON "explanationofbenefit"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ExplanationOfBenefit.patient`, `ExplanationOfBenefit.claim`, `ExplanationOfBenefit.status`, `ExplanationOfBenefit.provider`, `ExplanationOfBenefit.identifier`, `ExplanationOfBenefit.procedure-udi`, `ExplanationOfBenefit.care-team`, `ExplanationOfBenefit.facility`, `ExplanationOfBenefit.item-udi`, `ExplanationOfBenefit.subdetail-udi`, `ExplanationOfBenefit.enterer`, `ExplanationOfBenefit.coverage`, `ExplanationOfBenefit.payee`, `ExplanationOfBenefit.encounter`, `ExplanationOfBenefit.detail-udi`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS explanationofbenefit_resource__gin
ON "explanationofbenefit"
USING GIN (resource);
```

Search parameter: `ExplanationOfBenefit.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS explanationofbenefit_resource__created_max
ON "explanationofbenefit"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS explanationofbenefit_resource__created_min
ON "explanationofbenefit"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### FamilyMemberHistory

Search parameter: `FamilyMemberHistory.identifier`, `FamilyMemberHistory.status`, `FamilyMemberHistory.patient`, `FamilyMemberHistory.instantiates-uri`, `FamilyMemberHistory.relationship`, `FamilyMemberHistory.instantiates-canonical`, `FamilyMemberHistory.sex`, `FamilyMemberHistory.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS familymemberhistory_resource__gin
ON "familymemberhistory"
USING GIN (resource);
```

Search parameter: `FamilyMemberHistory.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS familymemberhistory_resource__date_max
ON "familymemberhistory"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS familymemberhistory_resource__date_min
ON "familymemberhistory"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Flag

Search parameter: `Flag.identifier`, `Flag.patient`, `Flag.author`, `Flag.subject`, `Flag.encounter`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS flag_resource__gin
ON "flag"
USING GIN (resource);
```

Search parameter: `Flag.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS flag_resource__date_max
ON "flag"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS flag_resource__date_min
ON "flag"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### Goal

Search parameter: `Goal.achievement-status`, `Goal.subject`, `Goal.category`, `Goal.lifecycle-status`, `Goal.patient`, `Goal.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS goal_resource__gin
ON "goal"
USING GIN (resource);
```

Search parameter: `Goal.start-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS goal_resource__start_date_max
ON "goal"
USING btree (knife_extract_max_timestamptz(resource, '[["start","date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS goal_resource__start_date_min
ON "goal"
USING btree (knife_extract_min_timestamptz(resource, '[["start","date"]]'));
```

Search parameter: `Goal.target-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS goal_resource__target_date_max
ON "goal"
USING btree (knife_extract_max_timestamptz(resource, '[["target","due","date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS goal_resource__target_date_min
ON "goal"
USING btree (knife_extract_min_timestamptz(resource, '[["target","due","date"]]'));
```

### GraphDefinition

Search parameter: `GraphDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__publisher_gin_trgm
ON "graphdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `GraphDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__description_gin_trgm
ON "graphdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `GraphDefinition.start`, `GraphDefinition.url`, `GraphDefinition.jurisdiction`, `GraphDefinition.context-type`, `GraphDefinition.context`, `GraphDefinition.version`, `GraphDefinition.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__gin
ON "graphdefinition"
USING GIN (resource);
```

Search parameter: `GraphDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__name_gin_trgm
ON "graphdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `GraphDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__date_max
ON "graphdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__date_min
ON "graphdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `GraphDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__context_quantity_max
ON "graphdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__context_quantity_min
ON "graphdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### Group

Search parameter: `Group.actual`, `Group.code`, `Group.managing-entity`, `Group.identifier`, `Group.member`, `Group.characteristic`, `Group.exclude`, `Group.type`, `Group.value`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS group_resource__gin
ON "group"
USING GIN (resource);
```

### GuidanceResponse

Search parameter: `GuidanceResponse.identifier`, `GuidanceResponse.subject`, `GuidanceResponse.request`, `GuidanceResponse.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS guidanceresponse_resource__gin
ON "guidanceresponse"
USING GIN (resource);
```

### HealthcareService

Search parameter: `HealthcareService.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS healthcareservice_resource__name_gin_trgm
ON "healthcareservice"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `HealthcareService.specialty`, `HealthcareService.characteristic`, `HealthcareService.identifier`, `HealthcareService.coverage-area`, `HealthcareService.program`, `HealthcareService.organization`, `HealthcareService.service-type`, `HealthcareService.endpoint`, `HealthcareService.location`, `HealthcareService.service-category`, `HealthcareService.active`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS healthcareservice_resource__gin
ON "healthcareservice"
USING GIN (resource);
```

### ImagingStudy

Search parameter: `ImagingStudy.patient`, `ImagingStudy.instance`, `ImagingStudy.identifier`, `ImagingStudy.modality`, `ImagingStudy.basedon`, `ImagingStudy.dicom-class`, `ImagingStudy.encounter`, `ImagingStudy.referrer`, `ImagingStudy.performer`, `ImagingStudy.interpreter`, `ImagingStudy.status`, `ImagingStudy.series`, `ImagingStudy.subject`, `ImagingStudy.bodysite`, `ImagingStudy.reason`, `ImagingStudy.endpoint`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS imagingstudy_resource__gin
ON "imagingstudy"
USING GIN (resource);
```

Search parameter: `ImagingStudy.started`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS imagingstudy_resource__started_max
ON "imagingstudy"
USING btree (knife_extract_max_timestamptz(resource, '[["started"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS imagingstudy_resource__started_min
ON "imagingstudy"
USING btree (knife_extract_min_timestamptz(resource, '[["started"]]'));
```

### Immunization

Search parameter: `Immunization.identifier`, `Immunization.reason-code`, `Immunization.patient`, `Immunization.status`, `Immunization.reason-reference`, `Immunization.status-reason`, `Immunization.performer`, `Immunization.target-disease`, `Immunization.vaccine-code`, `Immunization.reaction`, `Immunization.location`, `Immunization.manufacturer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__gin
ON "immunization"
USING GIN (resource);
```

Search parameter: `Immunization.series`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__series_gin_trgm
ON "immunization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["protocolApplied","series"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Immunization.lot-number`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__lot_number_gin_trgm
ON "immunization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["lotNumber"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Immunization.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__date_max
ON "immunization"
USING btree (knife_extract_max_timestamptz(resource, '[["occurrence","string"],["occurrence","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__date_min
ON "immunization"
USING btree (knife_extract_min_timestamptz(resource, '[["occurrence","string"],["occurrence","dateTime"]]'));
```

Search parameter: `Immunization.reaction-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__reaction_date_max
ON "immunization"
USING btree (knife_extract_max_timestamptz(resource, '[["reaction","date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__reaction_date_min
ON "immunization"
USING btree (knife_extract_min_timestamptz(resource, '[["reaction","date"]]'));
```

### ImmunizationEvaluation

Search parameter: `ImmunizationEvaluation.target-disease`, `ImmunizationEvaluation.immunization-event`, `ImmunizationEvaluation.status`, `ImmunizationEvaluation.patient`, `ImmunizationEvaluation.dose-status`, `ImmunizationEvaluation.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationevaluation_resource__gin
ON "immunizationevaluation"
USING GIN (resource);
```

Search parameter: `ImmunizationEvaluation.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationevaluation_resource__date_max
ON "immunizationevaluation"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationevaluation_resource__date_min
ON "immunizationevaluation"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### ImmunizationRecommendation

Search parameter: `ImmunizationRecommendation.patient`, `ImmunizationRecommendation.status`, `ImmunizationRecommendation.support`, `ImmunizationRecommendation.information`, `ImmunizationRecommendation.target-disease`, `ImmunizationRecommendation.vaccine-type`, `ImmunizationRecommendation.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationrecommendation_resource__gin
ON "immunizationrecommendation"
USING GIN (resource);
```

Search parameter: `ImmunizationRecommendation.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationrecommendation_resource__date_max
ON "immunizationrecommendation"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationrecommendation_resource__date_min
ON "immunizationrecommendation"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### ImplementationGuide

Search parameter: `ImplementationGuide.status`, `ImplementationGuide.url`, `ImplementationGuide.global`, `ImplementationGuide.version`, `ImplementationGuide.depends-on`, `ImplementationGuide.resource`, `ImplementationGuide.experimental`, `ImplementationGuide.context-type`, `ImplementationGuide.context`, `ImplementationGuide.jurisdiction`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__gin
ON "implementationguide"
USING GIN (resource);
```

Search parameter: `ImplementationGuide.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__name_gin_trgm
ON "implementationguide"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__description_gin_trgm
ON "implementationguide"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__title_gin_trgm
ON "implementationguide"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__publisher_gin_trgm
ON "implementationguide"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__date_max
ON "implementationguide"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__date_min
ON "implementationguide"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `ImplementationGuide.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__context_quantity_max
ON "implementationguide"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__context_quantity_min
ON "implementationguide"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### InsurancePlan

Search parameter: `InsurancePlan.owned-by`, `InsurancePlan.address-use`, `InsurancePlan.administered-by`, `InsurancePlan.type`, `InsurancePlan.endpoint`, `InsurancePlan.identifier`, `InsurancePlan.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__gin
ON "insuranceplan"
USING GIN (resource);
```

Search parameter: `InsurancePlan.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_country_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["contact","address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__phonetic_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_city_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["contact","address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__name_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"],["alias"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_state_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["contact","address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_postalcode_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["contact","address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_gin_trgm
ON "insuranceplan"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["contact","address","text"],["contact","address","district"],["contact","address","country"],["contact","address","city"],["contact","address","line"],["contact","address","state"],["contact","address","postalCode"]]$JSON$))) gin_trgm_ops);
```

### Invoice

Search parameter: `Invoice.issuer`, `Invoice.type`, `Invoice.status`, `Invoice.recipient`, `Invoice.participant`, `Invoice.patient`, `Invoice.account`, `Invoice.identifier`, `Invoice.participant-role`, `Invoice.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__gin
ON "invoice"
USING GIN (resource);
```

Search parameter: `Invoice.totalnet`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__totalnet_max
ON "invoice"
USING btree (knife_extract_max_numeric(resource, '[["totalNet","value"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__totalnet_min
ON "invoice"
USING btree (knife_extract_min_numeric(resource, '[["totalNet","value"]]'));
```

Search parameter: `Invoice.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__date_max
ON "invoice"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__date_min
ON "invoice"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `Invoice.totalgross`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__totalgross_max
ON "invoice"
USING btree (knife_extract_max_numeric(resource, '[["totalGross","value"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__totalgross_min
ON "invoice"
USING btree (knife_extract_min_numeric(resource, '[["totalGross","value"]]'));
```

### Library

Search parameter: `Library.url`, `Library.composed-of`, `Library.predecessor`, `Library.derived-from`, `Library.version`, `Library.context`, `Library.successor`, `Library.content-type`, `Library.context-type`, `Library.depends-on`, `Library.status`, `Library.topic`, `Library.identifier`, `Library.jurisdiction`, `Library.type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__gin
ON "library"
USING GIN (resource);
```

Search parameter: `Library.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__name_gin_trgm
ON "library"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__title_gin_trgm
ON "library"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__publisher_gin_trgm
ON "library"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__description_gin_trgm
ON "library"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__date_max
ON "library"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__date_min
ON "library"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `Library.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__context_quantity_max
ON "library"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__context_quantity_min
ON "library"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `Library.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__effective_max
ON "library"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__effective_min
ON "library"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

### Linkage

Search parameter: `Linkage.author`, `Linkage.item`, `Linkage.source`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS linkage_resource__gin
ON "linkage"
USING GIN (resource);
```

### List

Search parameter: `List.identifier`, `List.subject`, `List.code`, `List.status`, `List.empty-reason`, `List.encounter`, `List.item`, `List.source`, `List.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__gin
ON "list"
USING GIN (resource);
```

Search parameter: `List.notes`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__notes_gin_trgm
ON "list"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["note","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `List.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__title_gin_trgm
ON "list"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `List.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__date_max
ON "list"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__date_min
ON "list"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Location

Search parameter: `Location.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_state_gin_trgm
ON "location"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address-use`, `Location.type`, `Location.status`, `Location.partof`, `Location.endpoint`, `Location.operational-status`, `Location.identifier`, `Location.organization`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__gin
ON "location"
USING GIN (resource);
```

Search parameter: `Location.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_gin_trgm
ON "location"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_postalcode_gin_trgm
ON "location"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__name_gin_trgm
ON "location"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"],["alias"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_country_gin_trgm
ON "location"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_city_gin_trgm
ON "location"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

### Measure

Search parameter: `Measure.identifier`, `Measure.topic`, `Measure.successor`, `Measure.derived-from`, `Measure.url`, `Measure.version`, `Measure.predecessor`, `Measure.context-type`, `Measure.context`, `Measure.jurisdiction`, `Measure.composed-of`, `Measure.status`, `Measure.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__gin
ON "measure"
USING GIN (resource);
```

Search parameter: `Measure.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__description_gin_trgm
ON "measure"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__name_gin_trgm
ON "measure"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__title_gin_trgm
ON "measure"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__publisher_gin_trgm
ON "measure"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__context_quantity_max
ON "measure"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__context_quantity_min
ON "measure"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `Measure.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__effective_max
ON "measure"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__effective_min
ON "measure"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `Measure.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__date_max
ON "measure"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__date_min
ON "measure"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### MeasureReport

Search parameter: `MeasureReport.patient`, `MeasureReport.subject`, `MeasureReport.evaluated-resource`, `MeasureReport.measure`, `MeasureReport.reporter`, `MeasureReport.identifier`, `MeasureReport.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measurereport_resource__gin
ON "measurereport"
USING GIN (resource);
```

Search parameter: `MeasureReport.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measurereport_resource__period_max
ON "measurereport"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS measurereport_resource__period_min
ON "measurereport"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

Search parameter: `MeasureReport.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measurereport_resource__date_max
ON "measurereport"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS measurereport_resource__date_min
ON "measurereport"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Media

Search parameter: `Media.encounter`, `Media.subject`, `Media.operator`, `Media.identifier`, `Media.status`, `Media.view`, `Media.device`, `Media.patient`, `Media.site`, `Media.modality`, `Media.type`, `Media.based-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS media_resource__gin
ON "media"
USING GIN (resource);
```

Search parameter: `Media.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS media_resource__created_max
ON "media"
USING btree (knife_extract_max_timestamptz(resource, '[["created","Period","start"],["created","Period","end"],["created","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS media_resource__created_min
ON "media"
USING btree (knife_extract_min_timestamptz(resource, '[["created","Period","start"],["created","Period","end"],["created","dateTime"]]'));
```

### Medication

Search parameter: `Medication.manufacturer`, `Medication.form`, `Medication.status`, `Medication.ingredient`, `Medication.ingredient-code`, `Medication.identifier`, `Medication.lot-number`, `Medication.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medication_resource__gin
ON "medication"
USING GIN (resource);
```

Search parameter: `Medication.expiration-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medication_resource__expiration_date_max
ON "medication"
USING btree (knife_extract_max_timestamptz(resource, '[["batch","expirationDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medication_resource__expiration_date_min
ON "medication"
USING btree (knife_extract_min_timestamptz(resource, '[["batch","expirationDate"]]'));
```

### MedicationAdministration

Search parameter: `MedicationAdministration.request`, `MedicationAdministration.patient`, `MedicationAdministration.subject`, `MedicationAdministration.code`, `MedicationAdministration.medication`, `MedicationAdministration.identifier`, `MedicationAdministration.reason-given`, `MedicationAdministration.device`, `MedicationAdministration.context`, `MedicationAdministration.performer`, `MedicationAdministration.reason-not-given`, `MedicationAdministration.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationadministration_resource__gin
ON "medicationadministration"
USING GIN (resource);
```

Search parameter: `MedicationAdministration.effective-time`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationadministration_resource__effective_time_max
ON "medicationadministration"
USING btree (knife_extract_max_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationadministration_resource__effective_time_min
ON "medicationadministration"
USING btree (knife_extract_min_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"]]'));
```

### MedicationDispense

Search parameter: `MedicationDispense.context`, `MedicationDispense.responsibleparty`, `MedicationDispense.status`, `MedicationDispense.performer`, `MedicationDispense.type`, `MedicationDispense.medication`, `MedicationDispense.patient`, `MedicationDispense.code`, `MedicationDispense.destination`, `MedicationDispense.receiver`, `MedicationDispense.identifier`, `MedicationDispense.subject`, `MedicationDispense.prescription`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationdispense_resource__gin
ON "medicationdispense"
USING GIN (resource);
```

Search parameter: `MedicationDispense.whenprepared`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationdispense_resource__whenprepared_max
ON "medicationdispense"
USING btree (knife_extract_max_timestamptz(resource, '[["whenPrepared"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationdispense_resource__whenprepared_min
ON "medicationdispense"
USING btree (knife_extract_min_timestamptz(resource, '[["whenPrepared"]]'));
```

Search parameter: `MedicationDispense.whenhandedover`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationdispense_resource__whenhandedover_max
ON "medicationdispense"
USING btree (knife_extract_max_timestamptz(resource, '[["whenHandedOver"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationdispense_resource__whenhandedover_min
ON "medicationdispense"
USING btree (knife_extract_min_timestamptz(resource, '[["whenHandedOver"]]'));
```

### MedicationKnowledge

Search parameter: `MedicationKnowledge.monitoring-program-name`, `MedicationKnowledge.monograph-type`, `MedicationKnowledge.monograph`, `MedicationKnowledge.classification-type`, `MedicationKnowledge.monitoring-program-type`, `MedicationKnowledge.ingredient-code`, `MedicationKnowledge.classification`, `MedicationKnowledge.doseform`, `MedicationKnowledge.status`, `MedicationKnowledge.ingredient`, `MedicationKnowledge.code`, `MedicationKnowledge.source-cost`, `MedicationKnowledge.manufacturer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationknowledge_resource__gin
ON "medicationknowledge"
USING GIN (resource);
```

### MedicationRequest

Search parameter: `MedicationRequest.intent`, `MedicationRequest.subject`, `MedicationRequest.intended-performertype`, `MedicationRequest.intended-performer`, `MedicationRequest.status`, `MedicationRequest.medication`, `MedicationRequest.patient`, `MedicationRequest.code`, `MedicationRequest.category`, `MedicationRequest.requester`, `MedicationRequest.encounter`, `MedicationRequest.identifier`, `MedicationRequest.priority`, `MedicationRequest.intended-dispenser`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationrequest_resource__gin
ON "medicationrequest"
USING GIN (resource);
```

Search parameter: `MedicationRequest.authoredon`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationrequest_resource__authoredon_max
ON "medicationrequest"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationrequest_resource__authoredon_min
ON "medicationrequest"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

Search parameter: `MedicationRequest.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationrequest_resource__date_max
ON "medicationrequest"
USING btree (knife_extract_max_timestamptz(resource, '[["dosageInstruction","timing","event"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationrequest_resource__date_min
ON "medicationrequest"
USING btree (knife_extract_min_timestamptz(resource, '[["dosageInstruction","timing","event"]]'));
```

### MedicationStatement

Search parameter: `MedicationStatement.patient`, `MedicationStatement.status`, `MedicationStatement.context`, `MedicationStatement.subject`, `MedicationStatement.source`, `MedicationStatement.part-of`, `MedicationStatement.medication`, `MedicationStatement.category`, `MedicationStatement.identifier`, `MedicationStatement.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationstatement_resource__gin
ON "medicationstatement"
USING GIN (resource);
```

Search parameter: `MedicationStatement.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationstatement_resource__effective_max
ON "medicationstatement"
USING btree (knife_extract_max_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationstatement_resource__effective_min
ON "medicationstatement"
USING btree (knife_extract_min_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"]]'));
```

### MedicinalProduct

Search parameter: `MedicinalProduct.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproduct_resource__name_gin_trgm
ON "medicinalproduct"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","productName"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MedicinalProduct.identifier`, `MedicinalProduct.name-language`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproduct_resource__gin
ON "medicinalproduct"
USING GIN (resource);
```

### MedicinalProductAuthorization

Search parameter: `MedicinalProductAuthorization.identifier`, `MedicinalProductAuthorization.status`, `MedicinalProductAuthorization.subject`, `MedicinalProductAuthorization.country`, `MedicinalProductAuthorization.holder`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductauthorization_resource__gin
ON "medicinalproductauthorization"
USING GIN (resource);
```

### MedicinalProductContraindication

Search parameter: `MedicinalProductContraindication.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductcontraindication_resource__gin
ON "medicinalproductcontraindication"
USING GIN (resource);
```

### MedicinalProductIndication

Search parameter: `MedicinalProductIndication.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductindication_resource__gin
ON "medicinalproductindication"
USING GIN (resource);
```

### MedicinalProductInteraction

Search parameter: `MedicinalProductInteraction.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductinteraction_resource__gin
ON "medicinalproductinteraction"
USING GIN (resource);
```

### MedicinalProductPackaged

Search parameter: `MedicinalProductPackaged.identifier`, `MedicinalProductPackaged.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductpackaged_resource__gin
ON "medicinalproductpackaged"
USING GIN (resource);
```

### MedicinalProductPharmaceutical

Search parameter: `MedicinalProductPharmaceutical.identifier`, `MedicinalProductPharmaceutical.route`, `MedicinalProductPharmaceutical.target-species`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductpharmaceutical_resource__gin
ON "medicinalproductpharmaceutical"
USING GIN (resource);
```

### MedicinalProductUndesirableEffect

Search parameter: `MedicinalProductUndesirableEffect.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductundesirableeffect_resource__gin
ON "medicinalproductundesirableeffect"
USING GIN (resource);
```

### MessageDefinition

Search parameter: `MessageDefinition.url`, `MessageDefinition.identifier`, `MessageDefinition.parent`, `MessageDefinition.version`, `MessageDefinition.focus`, `MessageDefinition.category`, `MessageDefinition.context-type`, `MessageDefinition.context`, `MessageDefinition.jurisdiction`, `MessageDefinition.status`, `MessageDefinition.event`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__gin
ON "messagedefinition"
USING GIN (resource);
```

Search parameter: `MessageDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__description_gin_trgm
ON "messagedefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__publisher_gin_trgm
ON "messagedefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__name_gin_trgm
ON "messagedefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__title_gin_trgm
ON "messagedefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__date_max
ON "messagedefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__date_min
ON "messagedefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `MessageDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__context_quantity_max
ON "messagedefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__context_quantity_min
ON "messagedefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### MessageHeader

Search parameter: `MessageHeader.target`, `MessageHeader.responsible`, `MessageHeader.receiver`, `MessageHeader.sender`, `MessageHeader.code`, `MessageHeader.focus`, `MessageHeader.source-uri`, `MessageHeader.destination-uri`, `MessageHeader.author`, `MessageHeader.response-id`, `MessageHeader.enterer`, `MessageHeader.event`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messageheader_resource__gin
ON "messageheader"
USING GIN (resource);
```

Search parameter: `MessageHeader.destination`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messageheader_resource__destination_gin_trgm
ON "messageheader"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["destination","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageHeader.source`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messageheader_resource__source_gin_trgm
ON "messageheader"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["source","name"]]$JSON$))) gin_trgm_ops);
```

### MolecularSequence

Search parameter: `MolecularSequence.referenceseqid`, `MolecularSequence.identifier`, `MolecularSequence.type`, `MolecularSequence.chromosome`, `MolecularSequence.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__gin
ON "molecularsequence"
USING GIN (resource);
```

Search parameter: `MolecularSequence.window-start`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__window_start_max
ON "molecularsequence"
USING btree (knife_extract_max_numeric(resource, '[["referenceSeq","windowStart"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__window_start_min
ON "molecularsequence"
USING btree (knife_extract_min_numeric(resource, '[["referenceSeq","windowStart"]]'));
```

Search parameter: `MolecularSequence.window-end`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__window_end_max
ON "molecularsequence"
USING btree (knife_extract_max_numeric(resource, '[["referenceSeq","windowEnd"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__window_end_min
ON "molecularsequence"
USING btree (knife_extract_min_numeric(resource, '[["referenceSeq","windowEnd"]]'));
```

Search parameter: `MolecularSequence.variant-start`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__variant_start_max
ON "molecularsequence"
USING btree (knife_extract_max_numeric(resource, '[["variant","start"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__variant_start_min
ON "molecularsequence"
USING btree (knife_extract_min_numeric(resource, '[["variant","start"]]'));
```

Search parameter: `MolecularSequence.variant-end`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__variant_end_max
ON "molecularsequence"
USING btree (knife_extract_max_numeric(resource, '[["variant","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__variant_end_min
ON "molecularsequence"
USING btree (knife_extract_min_numeric(resource, '[["variant","end"]]'));
```

### NamingSystem

Search parameter: `NamingSystem.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__context_quantity_max
ON "namingsystem"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__context_quantity_min
ON "namingsystem"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `NamingSystem.contact`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__contact_gin_trgm
ON "namingsystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["contact","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.responsible`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__responsible_gin_trgm
ON "namingsystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["responsible"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__period_max
ON "namingsystem"
USING btree (knife_extract_max_timestamptz(resource, '[["uniqueId","period","start"],["uniqueId","period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__period_min
ON "namingsystem"
USING btree (knife_extract_min_timestamptz(resource, '[["uniqueId","period","start"],["uniqueId","period","end"]]'));
```

Search parameter: `NamingSystem.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__publisher_gin_trgm
ON "namingsystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.value`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__value_gin_trgm
ON "namingsystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["uniqueId","value"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__description_gin_trgm
ON "namingsystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__date_max
ON "namingsystem"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__date_min
ON "namingsystem"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `NamingSystem.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__name_gin_trgm
ON "namingsystem"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.context`, `NamingSystem.id-type`, `NamingSystem.context-type`, `NamingSystem.status`, `NamingSystem.telecom`, `NamingSystem.jurisdiction`, `NamingSystem.kind`, `NamingSystem.type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__gin
ON "namingsystem"
USING GIN (resource);
```

### NutritionOrder

Search parameter: `NutritionOrder.formula`, `NutritionOrder.encounter`, `NutritionOrder.status`, `NutritionOrder.patient`, `NutritionOrder.identifier`, `NutritionOrder.additive`, `NutritionOrder.oraldiet`, `NutritionOrder.provider`, `NutritionOrder.supplement`, `NutritionOrder.instantiates-canonical`, `NutritionOrder.instantiates-uri`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS nutritionorder_resource__gin
ON "nutritionorder"
USING GIN (resource);
```

Search parameter: `NutritionOrder.datetime`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS nutritionorder_resource__datetime_max
ON "nutritionorder"
USING btree (knife_extract_max_timestamptz(resource, '[["dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS nutritionorder_resource__datetime_min
ON "nutritionorder"
USING btree (knife_extract_min_timestamptz(resource, '[["dateTime"]]'));
```

### Observation

Search parameter: `Observation.category`, `Observation.component-value-concept`, `Observation.has-member`, `Observation.part-of`, `Observation.device`, `Observation.derived-from`, `Observation.component-data-absent-reason`, `Observation.status`, `Observation.performer`, `Observation.data-absent-reason`, `Observation.identifier`, `Observation.patient`, `Observation.value-concept`, `Observation.specimen`, `Observation.based-on`, `Observation.code`, `Observation.subject`, `Observation.component-code`, `Observation.method`, `Observation.encounter`, `Observation.focus`, `Observation.combo-data-absent-reason`, `Observation.combo-code`, `Observation.combo-value-concept`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__gin
ON "observation"
USING GIN (resource);
```

Search parameter: `Observation.value-string`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__value_string_gin_trgm
ON "observation"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["value","string"],["value","CodeableConcept","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Observation.combo-value-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__combo_value_quantity_max
ON "observation"
USING btree (knife_extract_max_numeric(resource, '[["value","Quantity","value"],["value","SampledData"],["component","value","Quantity","value"],["component","value","SampledData"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__combo_value_quantity_min
ON "observation"
USING btree (knife_extract_min_numeric(resource, '[["value","Quantity","value"],["value","SampledData"],["component","value","Quantity","value"],["component","value","SampledData"]]'));
```

Search parameter: `Observation.component-value-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__component_value_quantity_max
ON "observation"
USING btree (knife_extract_max_numeric(resource, '[["component","value","Quantity","value"],["component","value","SampledData"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__component_value_quantity_min
ON "observation"
USING btree (knife_extract_min_numeric(resource, '[["component","value","Quantity","value"],["component","value","SampledData"]]'));
```

Search parameter: `Observation.value-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__value_date_max
ON "observation"
USING btree (knife_extract_max_timestamptz(resource, '[["value","dateTime"],["value","Period","start"],["value","Period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__value_date_min
ON "observation"
USING btree (knife_extract_min_timestamptz(resource, '[["value","dateTime"],["value","Period","start"],["value","Period","end"]]'));
```

Search parameter: `Observation.value-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__value_quantity_max
ON "observation"
USING btree (knife_extract_max_numeric(resource, '[["value","Quantity","value"],["value","SampledData"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__value_quantity_min
ON "observation"
USING btree (knife_extract_min_numeric(resource, '[["value","Quantity","value"],["value","SampledData"]]'));
```

Search parameter: `Observation.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__date_max
ON "observation"
USING btree (knife_extract_max_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__date_min
ON "observation"
USING btree (knife_extract_min_timestamptz(resource, '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'));
```

### OperationDefinition

Search parameter: `OperationDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__name_gin_trgm
ON "operationdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.context`, `OperationDefinition.input-profile`, `OperationDefinition.jurisdiction`, `OperationDefinition.output-profile`, `OperationDefinition.url`, `OperationDefinition.type`, `OperationDefinition.system`, `OperationDefinition.instance`, `OperationDefinition.status`, `OperationDefinition.code`, `OperationDefinition.kind`, `OperationDefinition.version`, `OperationDefinition.context-type`, `OperationDefinition.base`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__gin
ON "operationdefinition"
USING GIN (resource);
```

Search parameter: `OperationDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__title_gin_trgm
ON "operationdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__description_gin_trgm
ON "operationdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__publisher_gin_trgm
ON "operationdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__date_max
ON "operationdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__date_min
ON "operationdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `OperationDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__context_quantity_max
ON "operationdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__context_quantity_min
ON "operationdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### Organization

Search parameter: `Organization.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.active`, `Organization.type`, `Organization.endpoint`, `Organization.identifier`, `Organization.address-use`, `Organization.partof`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__gin
ON "organization"
USING GIN (resource);
```

Search parameter: `Organization.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_postalcode_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__name_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"],["alias"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_state_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_city_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_country_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__phonetic_gin_trgm
ON "organization"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### OrganizationAffiliation

Search parameter: `OrganizationAffiliation.email`, `OrganizationAffiliation.phone`, `OrganizationAffiliation.active`, `OrganizationAffiliation.telecom`, `OrganizationAffiliation.role`, `OrganizationAffiliation.location`, `OrganizationAffiliation.primary-organization`, `OrganizationAffiliation.service`, `OrganizationAffiliation.identifier`, `OrganizationAffiliation.network`, `OrganizationAffiliation.endpoint`, `OrganizationAffiliation.participating-organization`, `OrganizationAffiliation.specialty`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organizationaffiliation_resource__gin
ON "organizationaffiliation"
USING GIN (resource);
```

Search parameter: `OrganizationAffiliation.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organizationaffiliation_resource__date_max
ON "organizationaffiliation"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS organizationaffiliation_resource__date_min
ON "organizationaffiliation"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### Patient

Search parameter: `Patient.birthdate`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__birthdate_max
ON "patient"
USING btree (knife_extract_max_timestamptz(resource, '[["birthDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__birthdate_min
ON "patient"
USING btree (knife_extract_min_timestamptz(resource, '[["birthDate"]]'));
```

Search parameter: `Patient.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__name_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.death-date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__death_date_max
ON "patient"
USING btree (knife_extract_max_timestamptz(resource, '[["deceased","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__death_date_min
ON "patient"
USING btree (knife_extract_min_timestamptz(resource, '[["deceased","dateTime"]]'));
```

Search parameter: `Patient.family`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__family_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_city_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.given`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__given_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","given"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_state_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_postalcode_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.phone`, `Patient.organization`, `Patient.email`, `Patient.deceased`, `Patient.language`, `Patient.address-use`, `Patient.link`, `Patient.identifier`, `Patient.telecom`, `Patient.gender`, `Patient.general-practitioner`, `Patient.active`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__gin
ON "patient"
USING GIN (resource);
```

Search parameter: `Patient.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__phonetic_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_country_gin_trgm
ON "patient"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

### PaymentNotice

Search parameter: `PaymentNotice.response`, `PaymentNotice.payment-status`, `PaymentNotice.identifier`, `PaymentNotice.status`, `PaymentNotice.request`, `PaymentNotice.provider`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentnotice_resource__gin
ON "paymentnotice"
USING GIN (resource);
```

Search parameter: `PaymentNotice.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentnotice_resource__created_max
ON "paymentnotice"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentnotice_resource__created_min
ON "paymentnotice"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### PaymentReconciliation

Search parameter: `PaymentReconciliation.payment-issuer`, `PaymentReconciliation.outcome`, `PaymentReconciliation.identifier`, `PaymentReconciliation.request`, `PaymentReconciliation.requestor`, `PaymentReconciliation.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentreconciliation_resource__gin
ON "paymentreconciliation"
USING GIN (resource);
```

Search parameter: `PaymentReconciliation.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentreconciliation_resource__disposition_gin_trgm
ON "paymentreconciliation"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PaymentReconciliation.created`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentreconciliation_resource__created_max
ON "paymentreconciliation"
USING btree (knife_extract_max_timestamptz(resource, '[["created"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentreconciliation_resource__created_min
ON "paymentreconciliation"
USING btree (knife_extract_min_timestamptz(resource, '[["created"]]'));
```

### Person

Search parameter: `Person.birthdate`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__birthdate_max
ON "person"
USING btree (knife_extract_max_timestamptz(resource, '[["birthDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__birthdate_min
ON "person"
USING btree (knife_extract_min_timestamptz(resource, '[["birthDate"]]'));
```

Search parameter: `Person.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_state_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_city_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__name_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_postalcode_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_country_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__phonetic_gin_trgm
ON "person"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address-use`, `Person.phone`, `Person.practitioner`, `Person.email`, `Person.gender`, `Person.identifier`, `Person.telecom`, `Person.organization`, `Person.relatedperson`, `Person.patient`, `Person.link`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__gin
ON "person"
USING GIN (resource);
```

### PlanDefinition

Search parameter: `PlanDefinition.composed-of`, `PlanDefinition.status`, `PlanDefinition.version`, `PlanDefinition.successor`, `PlanDefinition.context`, `PlanDefinition.identifier`, `PlanDefinition.predecessor`, `PlanDefinition.jurisdiction`, `PlanDefinition.derived-from`, `PlanDefinition.topic`, `PlanDefinition.context-type`, `PlanDefinition.definition`, `PlanDefinition.url`, `PlanDefinition.type`, `PlanDefinition.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__gin
ON "plandefinition"
USING GIN (resource);
```

Search parameter: `PlanDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__description_gin_trgm
ON "plandefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__publisher_gin_trgm
ON "plandefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__name_gin_trgm
ON "plandefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__title_gin_trgm
ON "plandefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__context_quantity_max
ON "plandefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__context_quantity_min
ON "plandefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `PlanDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__date_max
ON "plandefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__date_min
ON "plandefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `PlanDefinition.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__effective_max
ON "plandefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__effective_min
ON "plandefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

### Practitioner

Search parameter: `Practitioner.family`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__family_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.given`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__given_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","given"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__phonetic_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_state_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_country_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_city_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_postalcode_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__name_gin_trgm
ON "practitioner"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.gender`, `Practitioner.email`, `Practitioner.address-use`, `Practitioner.telecom`, `Practitioner.communication`, `Practitioner.phone`, `Practitioner.active`, `Practitioner.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__gin
ON "practitioner"
USING GIN (resource);
```

### PractitionerRole

Search parameter: `PractitionerRole.telecom`, `PractitionerRole.location`, `PractitionerRole.specialty`, `PractitionerRole.phone`, `PractitionerRole.identifier`, `PractitionerRole.practitioner`, `PractitionerRole.role`, `PractitionerRole.active`, `PractitionerRole.service`, `PractitionerRole.email`, `PractitionerRole.organization`, `PractitionerRole.endpoint`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitionerrole_resource__gin
ON "practitionerrole"
USING GIN (resource);
```

Search parameter: `PractitionerRole.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitionerrole_resource__date_max
ON "practitionerrole"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitionerrole_resource__date_min
ON "practitionerrole"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### Procedure

Search parameter: `Procedure.part-of`, `Procedure.identifier`, `Procedure.reason-code`, `Procedure.encounter`, `Procedure.code`, `Procedure.instantiates-canonical`, `Procedure.instantiates-uri`, `Procedure.performer`, `Procedure.status`, `Procedure.category`, `Procedure.location`, `Procedure.subject`, `Procedure.reason-reference`, `Procedure.based-on`, `Procedure.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS procedure_resource__gin
ON "procedure"
USING GIN (resource);
```

Search parameter: `Procedure.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS procedure_resource__date_max
ON "procedure"
USING btree (knife_extract_max_timestamptz(resource, '[["performed","Age"],["performed","Period","start"],["performed","Period","end"],["performed","string"],["performed","dateTime"],["performed","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS procedure_resource__date_min
ON "procedure"
USING btree (knife_extract_min_timestamptz(resource, '[["performed","Age"],["performed","Period","start"],["performed","Period","end"],["performed","string"],["performed","dateTime"],["performed","Range"]]'));
```

### Provenance

Search parameter: `Provenance.agent-type`, `Provenance.location`, `Provenance.entity`, `Provenance.signature-type`, `Provenance.agent`, `Provenance.agent-role`, `Provenance.target`, `Provenance.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS provenance_resource__gin
ON "provenance"
USING GIN (resource);
```

Search parameter: `Provenance.recorded`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS provenance_resource__recorded_max
ON "provenance"
USING btree (knife_extract_max_timestamptz(resource, '[["recorded"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS provenance_resource__recorded_min
ON "provenance"
USING btree (knife_extract_min_timestamptz(resource, '[["recorded"]]'));
```

Search parameter: `Provenance.when`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS provenance_resource__when_max
ON "provenance"
USING btree (knife_extract_max_timestamptz(resource, '[["occurred","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS provenance_resource__when_min
ON "provenance"
USING btree (knife_extract_min_timestamptz(resource, '[["occurred","dateTime"]]'));
```

### Questionnaire

Search parameter: `Questionnaire.version`, `Questionnaire.status`, `Questionnaire.jurisdiction`, `Questionnaire.url`, `Questionnaire.context`, `Questionnaire.context-type`, `Questionnaire.subject-type`, `Questionnaire.identifier`, `Questionnaire.code`, `Questionnaire.definition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__gin
ON "questionnaire"
USING GIN (resource);
```

Search parameter: `Questionnaire.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__name_gin_trgm
ON "questionnaire"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__description_gin_trgm
ON "questionnaire"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__publisher_gin_trgm
ON "questionnaire"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__title_gin_trgm
ON "questionnaire"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__effective_max
ON "questionnaire"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__effective_min
ON "questionnaire"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `Questionnaire.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__context_quantity_max
ON "questionnaire"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__context_quantity_min
ON "questionnaire"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `Questionnaire.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__date_max
ON "questionnaire"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__date_min
ON "questionnaire"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### QuestionnaireResponse

Search parameter: `QuestionnaireResponse.questionnaire`, `QuestionnaireResponse.author`, `QuestionnaireResponse.status`, `QuestionnaireResponse.based-on`, `QuestionnaireResponse.encounter`, `QuestionnaireResponse.source`, `QuestionnaireResponse.identifier`, `QuestionnaireResponse.patient`, `QuestionnaireResponse.subject`, `QuestionnaireResponse.part-of`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaireresponse_resource__gin
ON "questionnaireresponse"
USING GIN (resource);
```

Search parameter: `QuestionnaireResponse.authored`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaireresponse_resource__authored_max
ON "questionnaireresponse"
USING btree (knife_extract_max_timestamptz(resource, '[["authored"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaireresponse_resource__authored_min
ON "questionnaireresponse"
USING btree (knife_extract_min_timestamptz(resource, '[["authored"]]'));
```

### RelatedPerson

Search parameter: `RelatedPerson.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__name_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_state_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_city_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_country_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.birthdate`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__birthdate_max
ON "relatedperson"
USING btree (knife_extract_max_timestamptz(resource, '[["birthDate"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__birthdate_min
ON "relatedperson"
USING btree (knife_extract_min_timestamptz(resource, '[["birthDate"]]'));
```

Search parameter: `RelatedPerson.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__phonetic_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_postalcode_gin_trgm
ON "relatedperson"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.patient`, `RelatedPerson.telecom`, `RelatedPerson.phone`, `RelatedPerson.gender`, `RelatedPerson.relationship`, `RelatedPerson.active`, `RelatedPerson.identifier`, `RelatedPerson.email`, `RelatedPerson.address-use`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__gin
ON "relatedperson"
USING GIN (resource);
```

### RequestGroup

Search parameter: `RequestGroup.group-identifier`, `RequestGroup.instantiates-uri`, `RequestGroup.patient`, `RequestGroup.participant`, `RequestGroup.author`, `RequestGroup.priority`, `RequestGroup.intent`, `RequestGroup.subject`, `RequestGroup.status`, `RequestGroup.code`, `RequestGroup.identifier`, `RequestGroup.instantiates-canonical`, `RequestGroup.encounter`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS requestgroup_resource__gin
ON "requestgroup"
USING GIN (resource);
```

Search parameter: `RequestGroup.authored`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS requestgroup_resource__authored_max
ON "requestgroup"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS requestgroup_resource__authored_min
ON "requestgroup"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

### ResearchDefinition

Search parameter: `ResearchDefinition.url`, `ResearchDefinition.version`, `ResearchDefinition.composed-of`, `ResearchDefinition.identifier`, `ResearchDefinition.context-type`, `ResearchDefinition.predecessor`, `ResearchDefinition.jurisdiction`, `ResearchDefinition.topic`, `ResearchDefinition.successor`, `ResearchDefinition.context`, `ResearchDefinition.status`, `ResearchDefinition.derived-from`, `ResearchDefinition.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__gin
ON "researchdefinition"
USING GIN (resource);
```

Search parameter: `ResearchDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__description_gin_trgm
ON "researchdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__name_gin_trgm
ON "researchdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__publisher_gin_trgm
ON "researchdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__title_gin_trgm
ON "researchdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__effective_max
ON "researchdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__effective_min
ON "researchdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `ResearchDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__context_quantity_max
ON "researchdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__context_quantity_min
ON "researchdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `ResearchDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__date_max
ON "researchdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__date_min
ON "researchdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### ResearchElementDefinition

Search parameter: `ResearchElementDefinition.successor`, `ResearchElementDefinition.identifier`, `ResearchElementDefinition.derived-from`, `ResearchElementDefinition.context-type`, `ResearchElementDefinition.composed-of`, `ResearchElementDefinition.jurisdiction`, `ResearchElementDefinition.predecessor`, `ResearchElementDefinition.topic`, `ResearchElementDefinition.version`, `ResearchElementDefinition.url`, `ResearchElementDefinition.status`, `ResearchElementDefinition.context`, `ResearchElementDefinition.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__gin
ON "researchelementdefinition"
USING GIN (resource);
```

Search parameter: `ResearchElementDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__description_gin_trgm
ON "researchelementdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__title_gin_trgm
ON "researchelementdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__publisher_gin_trgm
ON "researchelementdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__name_gin_trgm
ON "researchelementdefinition"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__context_quantity_max
ON "researchelementdefinition"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__context_quantity_min
ON "researchelementdefinition"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `ResearchElementDefinition.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__effective_max
ON "researchelementdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__effective_min
ON "researchelementdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `ResearchElementDefinition.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__date_max
ON "researchelementdefinition"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__date_min
ON "researchelementdefinition"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### ResearchStudy

Search parameter: `ResearchStudy.site`, `ResearchStudy.status`, `ResearchStudy.principalinvestigator`, `ResearchStudy.protocol`, `ResearchStudy.keyword`, `ResearchStudy.identifier`, `ResearchStudy.sponsor`, `ResearchStudy.location`, `ResearchStudy.focus`, `ResearchStudy.category`, `ResearchStudy.partof`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchstudy_resource__gin
ON "researchstudy"
USING GIN (resource);
```

Search parameter: `ResearchStudy.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchstudy_resource__title_gin_trgm
ON "researchstudy"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchStudy.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchstudy_resource__date_max
ON "researchstudy"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchstudy_resource__date_min
ON "researchstudy"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### ResearchSubject

Search parameter: `ResearchSubject.status`, `ResearchSubject.identifier`, `ResearchSubject.patient`, `ResearchSubject.study`, `ResearchSubject.individual`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchsubject_resource__gin
ON "researchsubject"
USING GIN (resource);
```

Search parameter: `ResearchSubject.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchsubject_resource__date_max
ON "researchsubject"
USING btree (knife_extract_max_timestamptz(resource, '[["period","start"],["period","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchsubject_resource__date_min
ON "researchsubject"
USING btree (knife_extract_min_timestamptz(resource, '[["period","start"],["period","end"]]'));
```

### RiskAssessment

Search parameter: `RiskAssessment.encounter`, `RiskAssessment.method`, `RiskAssessment.performer`, `RiskAssessment.subject`, `RiskAssessment.risk`, `RiskAssessment.identifier`, `RiskAssessment.condition`, `RiskAssessment.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskassessment_resource__gin
ON "riskassessment"
USING GIN (resource);
```

Search parameter: `RiskAssessment.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskassessment_resource__date_max
ON "riskassessment"
USING btree (knife_extract_max_timestamptz(resource, '[["occurrence","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskassessment_resource__date_min
ON "riskassessment"
USING btree (knife_extract_min_timestamptz(resource, '[["occurrence","dateTime"]]'));
```

Search parameter: `RiskAssessment.probability`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskassessment_resource__probability_max
ON "riskassessment"
USING btree (knife_extract_max_numeric(resource, '[["prediction","probability","Range"],["prediction","probability","decimal"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskassessment_resource__probability_min
ON "riskassessment"
USING btree (knife_extract_min_numeric(resource, '[["prediction","probability","Range"],["prediction","probability","decimal"]]'));
```

### RiskEvidenceSynthesis

Search parameter: `RiskEvidenceSynthesis.context`, `RiskEvidenceSynthesis.jurisdiction`, `RiskEvidenceSynthesis.url`, `RiskEvidenceSynthesis.context-type`, `RiskEvidenceSynthesis.version`, `RiskEvidenceSynthesis.identifier`, `RiskEvidenceSynthesis.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__gin
ON "riskevidencesynthesis"
USING GIN (resource);
```

Search parameter: `RiskEvidenceSynthesis.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__name_gin_trgm
ON "riskevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__publisher_gin_trgm
ON "riskevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__title_gin_trgm
ON "riskevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__description_gin_trgm
ON "riskevidencesynthesis"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.effective`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__effective_max
ON "riskevidencesynthesis"
USING btree (knife_extract_max_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__effective_min
ON "riskevidencesynthesis"
USING btree (knife_extract_min_timestamptz(resource, '[["effectivePeriod","start"],["effectivePeriod","end"]]'));
```

Search parameter: `RiskEvidenceSynthesis.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__date_max
ON "riskevidencesynthesis"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__date_min
ON "riskevidencesynthesis"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

Search parameter: `RiskEvidenceSynthesis.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__context_quantity_max
ON "riskevidencesynthesis"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__context_quantity_min
ON "riskevidencesynthesis"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

### Schedule

Search parameter: `Schedule.service-type`, `Schedule.service-category`, `Schedule.specialty`, `Schedule.active`, `Schedule.identifier`, `Schedule.actor`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS schedule_resource__gin
ON "schedule"
USING GIN (resource);
```

Search parameter: `Schedule.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS schedule_resource__date_max
ON "schedule"
USING btree (knife_extract_max_timestamptz(resource, '[["planningHorizon","start"],["planningHorizon","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS schedule_resource__date_min
ON "schedule"
USING btree (knife_extract_min_timestamptz(resource, '[["planningHorizon","start"],["planningHorizon","end"]]'));
```

### ServiceRequest

Search parameter: `ServiceRequest.instantiates-canonical`, `ServiceRequest.requisition`, `ServiceRequest.intent`, `ServiceRequest.status`, `ServiceRequest.requester`, `ServiceRequest.body-site`, `ServiceRequest.code`, `ServiceRequest.replaces`, `ServiceRequest.instantiates-uri`, `ServiceRequest.priority`, `ServiceRequest.patient`, `ServiceRequest.subject`, `ServiceRequest.identifier`, `ServiceRequest.performer`, `ServiceRequest.performer-type`, `ServiceRequest.encounter`, `ServiceRequest.based-on`, `ServiceRequest.category`, `ServiceRequest.specimen`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS servicerequest_resource__gin
ON "servicerequest"
USING GIN (resource);
```

Search parameter: `ServiceRequest.occurrence`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS servicerequest_resource__occurrence_max
ON "servicerequest"
USING btree (knife_extract_max_timestamptz(resource, '[["occurrence","Period","start"],["occurrence","Period","end"],["occurrence","dateTime"],["occurrence","Timing","event"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS servicerequest_resource__occurrence_min
ON "servicerequest"
USING btree (knife_extract_min_timestamptz(resource, '[["occurrence","Period","start"],["occurrence","Period","end"],["occurrence","dateTime"],["occurrence","Timing","event"]]'));
```

Search parameter: `ServiceRequest.authored`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS servicerequest_resource__authored_max
ON "servicerequest"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS servicerequest_resource__authored_min
ON "servicerequest"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

### Slot

Search parameter: `Slot.schedule`, `Slot.specialty`, `Slot.service-type`, `Slot.appointment-type`, `Slot.status`, `Slot.identifier`, `Slot.service-category`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS slot_resource__gin
ON "slot"
USING GIN (resource);
```

Search parameter: `Slot.start`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS slot_resource__start_max
ON "slot"
USING btree (knife_extract_max_timestamptz(resource, '[["start"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS slot_resource__start_min
ON "slot"
USING btree (knife_extract_min_timestamptz(resource, '[["start"]]'));
```

### Specimen

Search parameter: `Specimen.container-id`, `Specimen.accession`, `Specimen.bodysite`, `Specimen.status`, `Specimen.identifier`, `Specimen.patient`, `Specimen.container`, `Specimen.collector`, `Specimen.subject`, `Specimen.parent`, `Specimen.type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS specimen_resource__gin
ON "specimen"
USING GIN (resource);
```

Search parameter: `Specimen.collected`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS specimen_resource__collected_max
ON "specimen"
USING btree (knife_extract_max_timestamptz(resource, '[["collection","collected","Period","start"],["collection","collected","Period","end"],["collection","collected","dateTime"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS specimen_resource__collected_min
ON "specimen"
USING btree (knife_extract_min_timestamptz(resource, '[["collection","collected","Period","start"],["collection","collected","Period","end"],["collection","collected","dateTime"]]'));
```

### SpecimenDefinition

Search parameter: `SpecimenDefinition.identifier`, `SpecimenDefinition.type`, `SpecimenDefinition.container`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS specimendefinition_resource__gin
ON "specimendefinition"
USING GIN (resource);
```

### StructureMap

Search parameter: `StructureMap.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__publisher_gin_trgm
ON "structuremap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.version`, `StructureMap.identifier`, `StructureMap.context-type`, `StructureMap.url`, `StructureMap.status`, `StructureMap.context`, `StructureMap.jurisdiction`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__gin
ON "structuremap"
USING GIN (resource);
```

Search parameter: `StructureMap.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__name_gin_trgm
ON "structuremap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__description_gin_trgm
ON "structuremap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__title_gin_trgm
ON "structuremap"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__context_quantity_max
ON "structuremap"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__context_quantity_min
ON "structuremap"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `StructureMap.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__date_max
ON "structuremap"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__date_min
ON "structuremap"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### Substance

Search parameter: `Substance.status`, `Substance.category`, `Substance.substance-reference`, `Substance.container-identifier`, `Substance.identifier`, `Substance.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS substance_resource__gin
ON "substance"
USING GIN (resource);
```

Search parameter: `Substance.expiry`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS substance_resource__expiry_max
ON "substance"
USING btree (knife_extract_max_timestamptz(resource, '[["instance","expiry"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS substance_resource__expiry_min
ON "substance"
USING btree (knife_extract_min_timestamptz(resource, '[["instance","expiry"]]'));
```

Search parameter: `Substance.quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS substance_resource__quantity_max
ON "substance"
USING btree (knife_extract_max_numeric(resource, '[["instance","quantity","value"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS substance_resource__quantity_min
ON "substance"
USING btree (knife_extract_min_numeric(resource, '[["instance","quantity","value"]]'));
```

### SubstanceSpecification

Search parameter: `SubstanceSpecification.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS substancespecification_resource__gin
ON "substancespecification"
USING GIN (resource);
```

### SupplyDelivery

Search parameter: `SupplyDelivery.patient`, `SupplyDelivery.supplier`, `SupplyDelivery.receiver`, `SupplyDelivery.identifier`, `SupplyDelivery.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS supplydelivery_resource__gin
ON "supplydelivery"
USING GIN (resource);
```

### SupplyRequest

Search parameter: `SupplyRequest.subject`, `SupplyRequest.requester`, `SupplyRequest.identifier`, `SupplyRequest.category`, `SupplyRequest.status`, `SupplyRequest.supplier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS supplyrequest_resource__gin
ON "supplyrequest"
USING GIN (resource);
```

Search parameter: `SupplyRequest.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS supplyrequest_resource__date_max
ON "supplyrequest"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS supplyrequest_resource__date_min
ON "supplyrequest"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

### Task

Search parameter: `Task.group-identifier`, `Task.business-status`, `Task.status`, `Task.subject`, `Task.requester`, `Task.encounter`, `Task.focus`, `Task.identifier`, `Task.patient`, `Task.priority`, `Task.based-on`, `Task.performer`, `Task.code`, `Task.intent`, `Task.part-of`, `Task.owner`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__gin
ON "task"
USING GIN (resource);
```

Search parameter: `Task.modified`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__modified_max
ON "task"
USING btree (knife_extract_max_timestamptz(resource, '[["lastModified"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__modified_min
ON "task"
USING btree (knife_extract_min_timestamptz(resource, '[["lastModified"]]'));
```

Search parameter: `Task.period`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__period_max
ON "task"
USING btree (knife_extract_max_timestamptz(resource, '[["executionPeriod","start"],["executionPeriod","end"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__period_min
ON "task"
USING btree (knife_extract_min_timestamptz(resource, '[["executionPeriod","start"],["executionPeriod","end"]]'));
```

Search parameter: `Task.authored-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__authored_on_max
ON "task"
USING btree (knife_extract_max_timestamptz(resource, '[["authoredOn"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__authored_on_min
ON "task"
USING btree (knife_extract_min_timestamptz(resource, '[["authoredOn"]]'));
```

### ValueSet

Search parameter: `ValueSet.expansion`, `ValueSet.version`, `ValueSet.identifier`, `ValueSet.status`, `ValueSet.context-type`, `ValueSet.context`, `ValueSet.url`, `ValueSet.jurisdiction`, `ValueSet.reference`, `ValueSet.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__gin
ON "valueset"
USING GIN (resource);
```

Search parameter: `ValueSet.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__title_gin_trgm
ON "valueset"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__publisher_gin_trgm
ON "valueset"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__description_gin_trgm
ON "valueset"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__name_gin_trgm
ON "valueset"
USING GIN ((aidbox_text_search(knife_extract_text(resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.context-quantity`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__context_quantity_max
ON "valueset"
USING btree (knife_extract_max_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__context_quantity_min
ON "valueset"
USING btree (knife_extract_min_numeric(resource, '[["useContext","value","Quantity","value"],["useContext","value","Range"]]'));
```

Search parameter: `ValueSet.date`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__date_max
ON "valueset"
USING btree (knife_extract_max_timestamptz(resource, '[["date"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__date_min
ON "valueset"
USING btree (knife_extract_min_timestamptz(resource, '[["date"]]'));
```

### VerificationResult

Search parameter: `VerificationResult.target`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS verificationresult_resource__gin
ON "verificationresult"
USING GIN (resource);
```

### VisionPrescription

Search parameter: `VisionPrescription.encounter`, `VisionPrescription.identifier`, `VisionPrescription.prescriber`, `VisionPrescription.status`, `VisionPrescription.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS visionprescription_resource__gin
ON "visionprescription"
USING GIN (resource);
```

Search parameter: `VisionPrescription.datewritten`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS visionprescription_resource__datewritten_max
ON "visionprescription"
USING btree (knife_extract_max_timestamptz(resource, '[["dateWritten"]]'));

CREATE INDEX CONCURRENTLY
IF NOT EXISTS visionprescription_resource__datewritten_min
ON "visionprescription"
USING btree (knife_extract_min_timestamptz(resource, '[["dateWritten"]]'));
```
