# Index management

{% hint style="info" %}
The following indexes cover regular searches, e.g. Patient?name=Smith. Please [contact us ](../contact-us.md)if you need more examples.&#x20;
{% endhint %}

### ImmunizationEvaluation

Search parameter: `ImmunizationEvaluation.date`

```sql
WIP: work in progress
```

Search parameter: `ImmunizationEvaluation.identifier`, `ImmunizationEvaluation.dose-status`, `ImmunizationEvaluation.patient`, `ImmunizationEvaluation.status`, `ImmunizationEvaluation.immunization-event`, `ImmunizationEvaluation.target-disease`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationevaluation_resource__gin
ON immunizationevaluation
USING GIN (resource);
```

### Appointment

Search parameter: `Appointment.location`, `Appointment.patient`, `Appointment.service-type`, `Appointment.practitioner`, `Appointment.reason-reference`, `Appointment.actor`, `Appointment.appointment-type`, `Appointment.service-category`, `Appointment.status`, `Appointment.specialty`, `Appointment.identifier`, `Appointment.reason-code`, `Appointment.based-on`, `Appointment.supporting-info`, `Appointment.slot`, `Appointment.part-status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS appointment_resource__gin
ON appointment
USING GIN (resource);
```

Search parameter: `Appointment.date`

```sql
WIP: work in progress
```

### StructureMap

Search parameter: `StructureMap.jurisdiction`, `StructureMap.context`, `StructureMap.status`, `StructureMap.url`, `StructureMap.context-type`, `StructureMap.identifier`, `StructureMap.version`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__gin
ON structuremap
USING GIN (resource);
```

Search parameter: `StructureMap.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__name_gin_trgm
ON structuremap
USING GIN ((aidbox_text_search(knife_extract_text("structuremap".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.date`

```sql
WIP: work in progress
```

Search parameter: `StructureMap.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__description_gin_trgm
ON structuremap
USING GIN ((aidbox_text_search(knife_extract_text("structuremap".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `StructureMap.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__publisher_gin_trgm
ON structuremap
USING GIN ((aidbox_text_search(knife_extract_text("structuremap".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `StructureMap.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS structuremap_resource__title_gin_trgm
ON structuremap
USING GIN ((aidbox_text_search(knife_extract_text("structuremap".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

### CareTeam

Search parameter: `CareTeam.date`

```sql
WIP: work in progress
```

Search parameter: `CareTeam.status`, `CareTeam.encounter`, `CareTeam.participant`, `CareTeam.patient`, `CareTeam.category`, `CareTeam.subject`, `CareTeam.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careteam_resource__gin
ON careteam
USING GIN (resource);
```

### Linkage

Search parameter: `Linkage.source`, `Linkage.item`, `Linkage.author`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS linkage_resource__gin
ON linkage
USING GIN (resource);
```

### Communication

Search parameter: `Communication.received`, `Communication.sent`

```sql
WIP: work in progress
```

Search parameter: `Communication.status`, `Communication.encounter`, `Communication.patient`, `Communication.based-on`, `Communication.subject`, `Communication.instantiates-canonical`, `Communication.part-of`, `Communication.medium`, `Communication.identifier`, `Communication.instantiates-uri`, `Communication.recipient`, `Communication.category`, `Communication.sender`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communication_resource__gin
ON communication
USING GIN (resource);
```

### MedicationDispense

Search parameter: `MedicationDispense.whenprepared`, `MedicationDispense.whenhandedover`

```sql
WIP: work in progress
```

Search parameter: `MedicationDispense.prescription`, `MedicationDispense.subject`, `MedicationDispense.identifier`, `MedicationDispense.receiver`, `MedicationDispense.destination`, `MedicationDispense.code`, `MedicationDispense.patient`, `MedicationDispense.medication`, `MedicationDispense.type`, `MedicationDispense.performer`, `MedicationDispense.status`, `MedicationDispense.responsibleparty`, `MedicationDispense.context`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationdispense_resource__gin
ON medicationdispense
USING GIN (resource);
```

### ImagingStudy

Search parameter: `ImagingStudy.endpoint`

```sql
WIP: work in progress
```

Search parameter: `ImagingStudy.started`

```sql
WIP: work in progress
```

Search parameter: `ImagingStudy.reason`, `ImagingStudy.bodysite`, `ImagingStudy.subject`, `ImagingStudy.series`, `ImagingStudy.status`, `ImagingStudy.interpreter`, `ImagingStudy.performer`, `ImagingStudy.referrer`, `ImagingStudy.encounter`, `ImagingStudy.dicom-class`, `ImagingStudy.basedon`, `ImagingStudy.modality`, `ImagingStudy.identifier`, `ImagingStudy.instance`, `ImagingStudy.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS imagingstudy_resource__gin
ON imagingstudy
USING GIN (resource);
```

### ChargeItem

Search parameter: `ChargeItem.service`, `ChargeItem.code`, `ChargeItem.identifier`, `ChargeItem.patient`, `ChargeItem.performing-organization`, `ChargeItem.account`, `ChargeItem.requesting-organization`, `ChargeItem.context`, `ChargeItem.enterer`, `ChargeItem.performer-actor`, `ChargeItem.performer-function`, `ChargeItem.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitem_resource__gin
ON chargeitem
USING GIN (resource);
```

Search parameter: `ChargeItem.factor-override`

```sql
WIP: work in progress
```

Search parameter: `ChargeItem.price-override`

```sql
WIP: work in progress
```

Search parameter: `ChargeItem.quantity`

```sql
WIP: work in progress
```

Search parameter: `ChargeItem.entered-date`, `ChargeItem.occurrence`

```sql
WIP: work in progress
```

### AdverseEvent

Search parameter: `AdverseEvent.date`

```sql
WIP: work in progress
```

Search parameter: `AdverseEvent.resultingcondition`, `AdverseEvent.severity`, `AdverseEvent.actuality`, `AdverseEvent.event`, `AdverseEvent.category`, `AdverseEvent.recorder`, `AdverseEvent.substance`, `AdverseEvent.seriousness`, `AdverseEvent.location`, `AdverseEvent.subject`, `AdverseEvent.study`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS adverseevent_resource__gin
ON adverseevent
USING GIN (resource);
```

### Media

Search parameter: `Media.based-on`, `Media.type`, `Media.modality`, `Media.site`, `Media.patient`, `Media.device`, `Media.view`, `Media.status`, `Media.identifier`, `Media.operator`, `Media.subject`, `Media.encounter`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS media_resource__gin
ON media
USING GIN (resource);
```

Search parameter: `Media.created`

```sql
WIP: work in progress
```

### QuestionnaireResponse

Search parameter: `QuestionnaireResponse.authored`

```sql
WIP: work in progress
```

Search parameter: `QuestionnaireResponse.part-of`, `QuestionnaireResponse.subject`, `QuestionnaireResponse.patient`, `QuestionnaireResponse.identifier`, `QuestionnaireResponse.source`, `QuestionnaireResponse.encounter`, `QuestionnaireResponse.based-on`, `QuestionnaireResponse.status`, `QuestionnaireResponse.author`, `QuestionnaireResponse.questionnaire`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaireresponse_resource__gin
ON questionnaireresponse
USING GIN (resource);
```

### Coverage

Search parameter: `Coverage.class-type`, `Coverage.payor`, `Coverage.type`, `Coverage.beneficiary`, `Coverage.subscriber`, `Coverage.patient`, `Coverage.policy-holder`, `Coverage.identifier`, `Coverage.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverage_resource__gin
ON coverage
USING GIN (resource);
```

Search parameter: `Coverage.dependent`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverage_resource__dependent_gin_trgm
ON coverage
USING GIN ((aidbox_text_search(knife_extract_text("coverage".resource, $JSON$[["dependent"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Coverage.class-value`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverage_resource__class_value_gin_trgm
ON coverage
USING GIN ((aidbox_text_search(knife_extract_text("coverage".resource, $JSON$[["class","value"]]$JSON$))) gin_trgm_ops);
```

### Procedure

Search parameter: `Procedure.patient`, `Procedure.based-on`, `Procedure.reason-reference`, `Procedure.subject`, `Procedure.location`, `Procedure.category`, `Procedure.status`, `Procedure.performer`, `Procedure.instantiates-uri`, `Procedure.instantiates-canonical`, `Procedure.code`, `Procedure.encounter`, `Procedure.reason-code`, `Procedure.identifier`, `Procedure.part-of`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS procedure_resource__gin
ON procedure
USING GIN (resource);
```

Search parameter: `Procedure.date`

```sql
WIP: work in progress
```

### AuditEvent

Search parameter: `AuditEvent.patient`

```sql
WIP: work in progress
```

Search parameter: `AuditEvent.agent-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__agent_name_gin_trgm
ON auditevent
USING GIN ((aidbox_text_search(knife_extract_text("auditevent".resource, $JSON$[["agent","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `AuditEvent.entity-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__entity_name_gin_trgm
ON auditevent
USING GIN ((aidbox_text_search(knife_extract_text("auditevent".resource, $JSON$[["entity","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `AuditEvent.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__address_gin_trgm
ON auditevent
USING GIN ((aidbox_text_search(knife_extract_text("auditevent".resource, $JSON$[["agent","network","address"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `AuditEvent.date`

```sql
WIP: work in progress
```

Search parameter: `AuditEvent.site`, `AuditEvent.entity`, `AuditEvent.subtype`, `AuditEvent.altid`, `AuditEvent.entity-role`, `AuditEvent.agent`, `AuditEvent.outcome`, `AuditEvent.entity-type`, `AuditEvent.policy`, `AuditEvent.agent-role`, `AuditEvent.type`, `AuditEvent.source`, `AuditEvent.action`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS auditevent_resource__gin
ON auditevent
USING GIN (resource);
```

### PaymentReconciliation

Search parameter: `PaymentReconciliation.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentreconciliation_resource__disposition_gin_trgm
ON paymentreconciliation
USING GIN ((aidbox_text_search(knife_extract_text("paymentreconciliation".resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PaymentReconciliation.status`, `PaymentReconciliation.requestor`, `PaymentReconciliation.request`, `PaymentReconciliation.identifier`, `PaymentReconciliation.outcome`, `PaymentReconciliation.payment-issuer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentreconciliation_resource__gin
ON paymentreconciliation
USING GIN (resource);
```

Search parameter: `PaymentReconciliation.created`

```sql
WIP: work in progress
```

### CompartmentDefinition

Search parameter: `CompartmentDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__publisher_gin_trgm
ON compartmentdefinition
USING GIN ((aidbox_text_search(knife_extract_text("compartmentdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CompartmentDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `CompartmentDefinition.version`, `CompartmentDefinition.resource`, `CompartmentDefinition.context`, `CompartmentDefinition.url`, `CompartmentDefinition.status`, `CompartmentDefinition.context-type`, `CompartmentDefinition.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__gin
ON compartmentdefinition
USING GIN (resource);
```

Search parameter: `CompartmentDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__description_gin_trgm
ON compartmentdefinition
USING GIN ((aidbox_text_search(knife_extract_text("compartmentdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CompartmentDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS compartmentdefinition_resource__name_gin_trgm
ON compartmentdefinition
USING GIN ((aidbox_text_search(knife_extract_text("compartmentdefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CompartmentDefinition.context-quantity`

```sql
WIP: work in progress
```

### Organization

Search parameter: `Organization.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__name_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["name"],["alias"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_country_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_postalcode_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_city_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.partof`, `Organization.address-use`, `Organization.identifier`, `Organization.endpoint`, `Organization.type`, `Organization.active`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__gin
ON organization
USING GIN (resource);
```

Search parameter: `Organization.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__address_state_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Organization.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organization_resource__phonetic_gin_trgm
ON organization
USING GIN ((aidbox_text_search(knife_extract_text("organization".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### ExplanationOfBenefit

Search parameter: `ExplanationOfBenefit.created`

```sql
WIP: work in progress
```

Search parameter: `ExplanationOfBenefit.detail-udi`, `ExplanationOfBenefit.encounter`, `ExplanationOfBenefit.payee`, `ExplanationOfBenefit.coverage`, `ExplanationOfBenefit.enterer`, `ExplanationOfBenefit.subdetail-udi`, `ExplanationOfBenefit.item-udi`, `ExplanationOfBenefit.facility`, `ExplanationOfBenefit.care-team`, `ExplanationOfBenefit.procedure-udi`, `ExplanationOfBenefit.identifier`, `ExplanationOfBenefit.provider`, `ExplanationOfBenefit.status`, `ExplanationOfBenefit.claim`, `ExplanationOfBenefit.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS explanationofbenefit_resource__gin
ON explanationofbenefit
USING GIN (resource);
```

Search parameter: `ExplanationOfBenefit.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS explanationofbenefit_resource__disposition_gin_trgm
ON explanationofbenefit
USING GIN ((aidbox_text_search(knife_extract_text("explanationofbenefit".resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

### Composition

Search parameter: `Composition.period`, `Composition.date`

```sql
WIP: work in progress
```

Search parameter: `Composition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__title_gin_trgm
ON composition
USING GIN ((aidbox_text_search(knife_extract_text("composition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Composition.confidentiality`, `Composition.entry`, `Composition.category`, `Composition.section`, `Composition.status`, `Composition.subject`, `Composition.type`, `Composition.identifier`, `Composition.related-id`, `Composition.related-ref`, `Composition.attester`, `Composition.author`, `Composition.patient`, `Composition.context`, `Composition.encounter`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS composition_resource__gin
ON composition
USING GIN (resource);
```

### CoverageEligibilityResponse

Search parameter: `CoverageEligibilityResponse.created`

```sql
WIP: work in progress
```

Search parameter: `CoverageEligibilityResponse.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityresponse_resource__disposition_gin_trgm
ON coverageeligibilityresponse
USING GIN ((aidbox_text_search(knife_extract_text("coverageeligibilityresponse".resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CoverageEligibilityResponse.outcome`, `CoverageEligibilityResponse.requestor`, `CoverageEligibilityResponse.request`, `CoverageEligibilityResponse.insurer`, `CoverageEligibilityResponse.identifier`, `CoverageEligibilityResponse.status`, `CoverageEligibilityResponse.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityresponse_resource__gin
ON coverageeligibilityresponse
USING GIN (resource);
```

### DocumentReference

Search parameter: `DocumentReference.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__description_gin_trgm
ON documentreference
USING GIN ((aidbox_text_search(knife_extract_text("documentreference".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `DocumentReference.identifier`

```sql
WIP: work in progress
```

Search parameter: `DocumentReference.contenttype`, `DocumentReference.security-label`, `DocumentReference.authenticator`, `DocumentReference.facility`, `DocumentReference.format`, `DocumentReference.category`, `DocumentReference.language`, `DocumentReference.subject`, `DocumentReference.setting`, `DocumentReference.encounter`, `DocumentReference.status`, `DocumentReference.relatesto`, `DocumentReference.relation`, `DocumentReference.event`, `DocumentReference.location`, `DocumentReference.patient`, `DocumentReference.related`, `DocumentReference.custodian`, `DocumentReference.author`, `DocumentReference.type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentreference_resource__gin
ON documentreference
USING GIN (resource);
```

Search parameter: `DocumentReference.period`, `DocumentReference.date`

```sql
WIP: work in progress
```

### EventDefinition

Search parameter: `EventDefinition.derived-from`, `EventDefinition.url`, `EventDefinition.topic`, `EventDefinition.version`, `EventDefinition.context-type`, `EventDefinition.status`, `EventDefinition.jurisdiction`, `EventDefinition.context`, `EventDefinition.predecessor`, `EventDefinition.composed-of`, `EventDefinition.identifier`, `EventDefinition.successor`, `EventDefinition.depends-on`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__gin
ON eventdefinition
USING GIN (resource);
```

Search parameter: `EventDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__name_gin_trgm
ON eventdefinition
USING GIN ((aidbox_text_search(knife_extract_text("eventdefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__description_gin_trgm
ON eventdefinition
USING GIN ((aidbox_text_search(knife_extract_text("eventdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__publisher_gin_trgm
ON eventdefinition
USING GIN ((aidbox_text_search(knife_extract_text("eventdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS eventdefinition_resource__title_gin_trgm
ON eventdefinition
USING GIN ((aidbox_text_search(knife_extract_text("eventdefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EventDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `EventDefinition.effective`, `EventDefinition.date`

```sql
WIP: work in progress
```

### Encounter

Search parameter: `Encounter.length`

```sql
WIP: work in progress
```

Search parameter: `Encounter.service-provider`, `Encounter.reason-reference`, `Encounter.participant-type`, `Encounter.participant`, `Encounter.reason-code`, `Encounter.status`, `Encounter.based-on`, `Encounter.identifier`, `Encounter.part-of`, `Encounter.patient`, `Encounter.location`, `Encounter.class`, `Encounter.account`, `Encounter.subject`, `Encounter.practitioner`, `Encounter.special-arrangement`, `Encounter.diagnosis`, `Encounter.appointment`, `Encounter.episode-of-care`, `Encounter.type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS encounter_resource__gin
ON encounter
USING GIN (resource);
```

Search parameter: `Encounter.location-period`, `Encounter.date`

```sql
WIP: work in progress
```

### ImplementationGuide

Search parameter: `ImplementationGuide.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__description_gin_trgm
ON implementationguide
USING GIN ((aidbox_text_search(knife_extract_text("implementationguide".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ImplementationGuide.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__title_gin_trgm
ON implementationguide
USING GIN ((aidbox_text_search(knife_extract_text("implementationguide".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__publisher_gin_trgm
ON implementationguide
USING GIN ((aidbox_text_search(knife_extract_text("implementationguide".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.date`

```sql
WIP: work in progress
```

Search parameter: `ImplementationGuide.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__name_gin_trgm
ON implementationguide
USING GIN ((aidbox_text_search(knife_extract_text("implementationguide".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ImplementationGuide.jurisdiction`, `ImplementationGuide.context`, `ImplementationGuide.context-type`, `ImplementationGuide.experimental`, `ImplementationGuide.resource`, `ImplementationGuide.depends-on`, `ImplementationGuide.version`, `ImplementationGuide.global`, `ImplementationGuide.url`, `ImplementationGuide.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS implementationguide_resource__gin
ON implementationguide
USING GIN (resource);
```

### EvidenceVariable

Search parameter: `EvidenceVariable.depends-on`, `EvidenceVariable.jurisdiction`, `EvidenceVariable.url`, `EvidenceVariable.status`, `EvidenceVariable.topic`, `EvidenceVariable.context`, `EvidenceVariable.successor`, `EvidenceVariable.version`, `EvidenceVariable.composed-of`, `EvidenceVariable.derived-from`, `EvidenceVariable.identifier`, `EvidenceVariable.predecessor`, `EvidenceVariable.context-type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__gin
ON evidencevariable
USING GIN (resource);
```

Search parameter: `EvidenceVariable.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__publisher_gin_trgm
ON evidencevariable
USING GIN ((aidbox_text_search(knife_extract_text("evidencevariable".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.date`, `EvidenceVariable.effective`

```sql
WIP: work in progress
```

Search parameter: `EvidenceVariable.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__name_gin_trgm
ON evidencevariable
USING GIN ((aidbox_text_search(knife_extract_text("evidencevariable".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__description_gin_trgm
ON evidencevariable
USING GIN ((aidbox_text_search(knife_extract_text("evidencevariable".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EvidenceVariable.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `EvidenceVariable.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidencevariable_resource__title_gin_trgm
ON evidencevariable
USING GIN ((aidbox_text_search(knife_extract_text("evidencevariable".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

### DiagnosticReport

Search parameter: `DiagnosticReport.subject`, `DiagnosticReport.conclusion`, `DiagnosticReport.results-interpreter`, `DiagnosticReport.identifier`, `DiagnosticReport.media`, `DiagnosticReport.result`, `DiagnosticReport.encounter`, `DiagnosticReport.performer`, `DiagnosticReport.based-on`, `DiagnosticReport.category`, `DiagnosticReport.code`, `DiagnosticReport.status`, `DiagnosticReport.patient`, `DiagnosticReport.specimen`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS diagnosticreport_resource__gin
ON diagnosticreport
USING GIN (resource);
```

Search parameter: `DiagnosticReport.date`, `DiagnosticReport.issued`

```sql
WIP: work in progress
```

### ExampleScenario

Search parameter: `ExampleScenario.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__publisher_gin_trgm
ON examplescenario
USING GIN ((aidbox_text_search(knife_extract_text("examplescenario".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ExampleScenario.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__name_gin_trgm
ON examplescenario
USING GIN ((aidbox_text_search(knife_extract_text("examplescenario".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ExampleScenario.date`

```sql
WIP: work in progress
```

Search parameter: `ExampleScenario.version`, `ExampleScenario.jurisdiction`, `ExampleScenario.context-type`, `ExampleScenario.status`, `ExampleScenario.url`, `ExampleScenario.context`, `ExampleScenario.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS examplescenario_resource__gin
ON examplescenario
USING GIN (resource);
```

Search parameter: `ExampleScenario.context-quantity`

```sql
WIP: work in progress
```

### ResearchDefinition

Search parameter: `ResearchDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__name_gin_trgm
ON researchdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchdefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.derived-from`, `ResearchDefinition.status`, `ResearchDefinition.context`, `ResearchDefinition.successor`, `ResearchDefinition.topic`, `ResearchDefinition.jurisdiction`, `ResearchDefinition.predecessor`, `ResearchDefinition.context-type`, `ResearchDefinition.identifier`, `ResearchDefinition.composed-of`, `ResearchDefinition.version`, `ResearchDefinition.url`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__gin
ON researchdefinition
USING GIN (resource);
```

Search parameter: `ResearchDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__description_gin_trgm
ON researchdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ResearchDefinition.depends-on`

```sql
WIP: work in progress
```

Search parameter: `ResearchDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__title_gin_trgm
ON researchdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchdefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchDefinition.effective`, `ResearchDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `ResearchDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchdefinition_resource__publisher_gin_trgm
ON researchdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

### MedicinalProductInteraction

Search parameter: `MedicinalProductInteraction.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductinteraction_resource__gin
ON medicinalproductinteraction
USING GIN (resource);
```

### CodeSystem

Search parameter: `CodeSystem.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__publisher_gin_trgm
ON codesystem
USING GIN ((aidbox_text_search(knife_extract_text("codesystem".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `CodeSystem.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__name_gin_trgm
ON codesystem
USING GIN ((aidbox_text_search(knife_extract_text("codesystem".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.system`, `CodeSystem.content-mode`, `CodeSystem.url`, `CodeSystem.code`, `CodeSystem.context`, `CodeSystem.status`, `CodeSystem.identifier`, `CodeSystem.context-type`, `CodeSystem.version`, `CodeSystem.jurisdiction`, `CodeSystem.language`, `CodeSystem.supplements`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__gin
ON codesystem
USING GIN (resource);
```

Search parameter: `CodeSystem.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__description_gin_trgm
ON codesystem
USING GIN ((aidbox_text_search(knife_extract_text("codesystem".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS codesystem_resource__title_gin_trgm
ON codesystem
USING GIN ((aidbox_text_search(knife_extract_text("codesystem".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `CodeSystem.date`

```sql
WIP: work in progress
```

### MessageDefinition

Search parameter: `MessageDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `MessageDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__publisher_gin_trgm
ON messagedefinition
USING GIN ((aidbox_text_search(knife_extract_text("messagedefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `MessageDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__name_gin_trgm
ON messagedefinition
USING GIN ((aidbox_text_search(knife_extract_text("messagedefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.status`, `MessageDefinition.jurisdiction`, `MessageDefinition.context`, `MessageDefinition.context-type`, `MessageDefinition.category`, `MessageDefinition.focus`, `MessageDefinition.version`, `MessageDefinition.parent`, `MessageDefinition.identifier`, `MessageDefinition.url`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__gin
ON messagedefinition
USING GIN (resource);
```

Search parameter: `MessageDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__title_gin_trgm
ON messagedefinition
USING GIN ((aidbox_text_search(knife_extract_text("messagedefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messagedefinition_resource__description_gin_trgm
ON messagedefinition
USING GIN ((aidbox_text_search(knife_extract_text("messagedefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageDefinition.event`

```sql
WIP: work in progress
```

### NutritionOrder

Search parameter: `NutritionOrder.instantiates-uri`, `NutritionOrder.instantiates-canonical`, `NutritionOrder.supplement`, `NutritionOrder.provider`, `NutritionOrder.oraldiet`, `NutritionOrder.additive`, `NutritionOrder.identifier`, `NutritionOrder.patient`, `NutritionOrder.status`, `NutritionOrder.encounter`, `NutritionOrder.formula`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS nutritionorder_resource__gin
ON nutritionorder
USING GIN (resource);
```

Search parameter: `NutritionOrder.datetime`

```sql
WIP: work in progress
```

### VerificationResult

Search parameter: `VerificationResult.target`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS verificationresult_resource__gin
ON verificationresult
USING GIN (resource);
```

### MedicationAdministration

Search parameter: `MedicationAdministration.status`, `MedicationAdministration.reason-not-given`, `MedicationAdministration.performer`, `MedicationAdministration.context`, `MedicationAdministration.device`, `MedicationAdministration.reason-given`, `MedicationAdministration.identifier`, `MedicationAdministration.medication`, `MedicationAdministration.code`, `MedicationAdministration.subject`, `MedicationAdministration.patient`, `MedicationAdministration.request`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationadministration_resource__gin
ON medicationadministration
USING GIN (resource);
```

Search parameter: `MedicationAdministration.effective-time`

```sql
WIP: work in progress
```

### Flag

Search parameter: `Flag.encounter`, `Flag.subject`, `Flag.author`, `Flag.patient`, `Flag.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS flag_resource__gin
ON flag
USING GIN (resource);
```

Search parameter: `Flag.date`

```sql
WIP: work in progress
```

### DeviceUseStatement

Search parameter: `DeviceUseStatement.subject`, `DeviceUseStatement.identifier`, `DeviceUseStatement.device`, `DeviceUseStatement.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS deviceusestatement_resource__gin
ON deviceusestatement
USING GIN (resource);
```

### Contract

Search parameter: `Contract.issued`

```sql
WIP: work in progress
```

Search parameter: `Contract.patient`, `Contract.instantiates`, `Contract.signer`, `Contract.domain`, `Contract.subject`, `Contract.identifier`, `Contract.url`, `Contract.status`, `Contract.authority`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS contract_resource__gin
ON contract
USING GIN (resource);
```

### Invoice

Search parameter: `Invoice.subject`, `Invoice.participant-role`, `Invoice.identifier`, `Invoice.account`, `Invoice.patient`, `Invoice.participant`, `Invoice.recipient`, `Invoice.status`, `Invoice.type`, `Invoice.issuer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS invoice_resource__gin
ON invoice
USING GIN (resource);
```

Search parameter: `Invoice.totalnet`

```sql
WIP: work in progress
```

Search parameter: `Invoice.date`

```sql
WIP: work in progress
```

Search parameter: `Invoice.totalgross`

```sql
WIP: work in progress
```

### PaymentNotice

Search parameter: `PaymentNotice.provider`, `PaymentNotice.request`, `PaymentNotice.status`, `PaymentNotice.identifier`, `PaymentNotice.payment-status`, `PaymentNotice.response`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS paymentnotice_resource__gin
ON paymentnotice
USING GIN (resource);
```

Search parameter: `PaymentNotice.created`

```sql
WIP: work in progress
```

### Location

Search parameter: `Location.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_postalcode_gin_trgm
ON location
USING GIN ((aidbox_text_search(knife_extract_text("location".resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__name_gin_trgm
ON location
USING GIN ((aidbox_text_search(knife_extract_text("location".resource, $JSON$[["name"],["alias"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_gin_trgm
ON location
USING GIN ((aidbox_text_search(knife_extract_text("location".resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_state_gin_trgm
ON location
USING GIN ((aidbox_text_search(knife_extract_text("location".resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_city_gin_trgm
ON location
USING GIN ((aidbox_text_search(knife_extract_text("location".resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Location.organization`, `Location.identifier`, `Location.operational-status`, `Location.endpoint`, `Location.partof`, `Location.status`, `Location.type`, `Location.address-use`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__gin
ON location
USING GIN (resource);
```

Search parameter: `Location.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS location_resource__address_country_gin_trgm
ON location
USING GIN ((aidbox_text_search(knife_extract_text("location".resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

### Claim

Search parameter: `Claim.priority`, `Claim.status`, `Claim.use`, `Claim.procedure-udi`, `Claim.item-udi`, `Claim.detail-udi`, `Claim.enterer`, `Claim.subdetail-udi`, `Claim.payee`, `Claim.identifier`, `Claim.care-team`, `Claim.encounter`, `Claim.facility`, `Claim.insurer`, `Claim.patient`, `Claim.provider`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claim_resource__gin
ON claim
USING GIN (resource);
```

Search parameter: `Claim.created`

```sql
WIP: work in progress
```

### Specimen

Search parameter: `Specimen.collected`

```sql
WIP: work in progress
```

Search parameter: `Specimen.type`, `Specimen.parent`, `Specimen.subject`, `Specimen.collector`, `Specimen.container`, `Specimen.patient`, `Specimen.identifier`, `Specimen.status`, `Specimen.bodysite`, `Specimen.accession`, `Specimen.container-id`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS specimen_resource__gin
ON specimen
USING GIN (resource);
```

### MedicationStatement

Search parameter: `MedicationStatement.effective`

```sql
WIP: work in progress
```

Search parameter: `MedicationStatement.code`, `MedicationStatement.identifier`, `MedicationStatement.category`, `MedicationStatement.medication`, `MedicationStatement.part-of`, `MedicationStatement.source`, `MedicationStatement.subject`, `MedicationStatement.context`, `MedicationStatement.status`, `MedicationStatement.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationstatement_resource__gin
ON medicationstatement
USING GIN (resource);
```

### EnrollmentResponse

Search parameter: `EnrollmentResponse.status`, `EnrollmentResponse.request`, `EnrollmentResponse.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS enrollmentresponse_resource__gin
ON enrollmentresponse
USING GIN (resource);
```

### Evidence

Search parameter: `Evidence.effective`, `Evidence.date`

```sql
WIP: work in progress
```

Search parameter: `Evidence.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__publisher_gin_trgm
ON evidence
USING GIN ((aidbox_text_search(knife_extract_text("evidence".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `Evidence.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__title_gin_trgm
ON evidence
USING GIN ((aidbox_text_search(knife_extract_text("evidence".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__description_gin_trgm
ON evidence
USING GIN ((aidbox_text_search(knife_extract_text("evidence".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Evidence.derived-from`, `Evidence.url`, `Evidence.version`, `Evidence.status`, `Evidence.topic`, `Evidence.identifier`, `Evidence.predecessor`, `Evidence.composed-of`, `Evidence.context-type`, `Evidence.context`, `Evidence.depends-on`, `Evidence.jurisdiction`, `Evidence.successor`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__gin
ON evidence
USING GIN (resource);
```

Search parameter: `Evidence.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS evidence_resource__name_gin_trgm
ON evidence
USING GIN ((aidbox_text_search(knife_extract_text("evidence".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### Bundle

Search parameter: `Bundle.timestamp`

```sql
WIP: work in progress
```

Search parameter: `Bundle.identifier`, `Bundle.type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS bundle_resource__gin
ON bundle
USING GIN (resource);
```

Search parameter: `Bundle.message`, `Bundle.composition`

```sql
WIP: work in progress
```

### ResearchElementDefinition

Search parameter: `ResearchElementDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__title_gin_trgm
ON researchelementdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchelementdefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ResearchElementDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__name_gin_trgm
ON researchelementdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchelementdefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.effective`, `ResearchElementDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `ResearchElementDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__publisher_gin_trgm
ON researchelementdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchelementdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__description_gin_trgm
ON researchelementdefinition
USING GIN ((aidbox_text_search(knife_extract_text("researchelementdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ResearchElementDefinition.context`, `ResearchElementDefinition.status`, `ResearchElementDefinition.url`, `ResearchElementDefinition.version`, `ResearchElementDefinition.topic`, `ResearchElementDefinition.predecessor`, `ResearchElementDefinition.jurisdiction`, `ResearchElementDefinition.composed-of`, `ResearchElementDefinition.context-type`, `ResearchElementDefinition.derived-from`, `ResearchElementDefinition.identifier`, `ResearchElementDefinition.successor`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchelementdefinition_resource__gin
ON researchelementdefinition
USING GIN (resource);
```

Search parameter: `ResearchElementDefinition.depends-on`

```sql
WIP: work in progress
```

### BodyStructure

Search parameter: `BodyStructure.identifier`, `BodyStructure.patient`, `BodyStructure.morphology`, `BodyStructure.location`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS bodystructure_resource__gin
ON bodystructure
USING GIN (resource);
```

### MedicinalProduct

Search parameter: `MedicinalProduct.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproduct_resource__name_gin_trgm
ON medicinalproduct
USING GIN ((aidbox_text_search(knife_extract_text("medicinalproduct".resource, $JSON$[["name","productName"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MedicinalProduct.name-language`, `MedicinalProduct.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproduct_resource__gin
ON medicinalproduct
USING GIN (resource);
```

### ResearchStudy

Search parameter: `ResearchStudy.partof`, `ResearchStudy.category`, `ResearchStudy.focus`, `ResearchStudy.location`, `ResearchStudy.sponsor`, `ResearchStudy.identifier`, `ResearchStudy.keyword`, `ResearchStudy.protocol`, `ResearchStudy.principalinvestigator`, `ResearchStudy.status`, `ResearchStudy.site`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchstudy_resource__gin
ON researchstudy
USING GIN (resource);
```

Search parameter: `ResearchStudy.date`

```sql
WIP: work in progress
```

Search parameter: `ResearchStudy.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchstudy_resource__title_gin_trgm
ON researchstudy
USING GIN ((aidbox_text_search(knife_extract_text("researchstudy".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

### AppointmentResponse

Search parameter: `AppointmentResponse.location`, `AppointmentResponse.identifier`, `AppointmentResponse.appointment`, `AppointmentResponse.patient`, `AppointmentResponse.part-status`, `AppointmentResponse.actor`, `AppointmentResponse.practitioner`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS appointmentresponse_resource__gin
ON appointmentresponse
USING GIN (resource);
```

### MedicinalProductIndication

Search parameter: `MedicinalProductIndication.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductindication_resource__gin
ON medicinalproductindication
USING GIN (resource);
```

### Measure

Search parameter: `Measure.effective`, `Measure.date`

```sql
WIP: work in progress
```

Search parameter: `Measure.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__publisher_gin_trgm
ON measure
USING GIN ((aidbox_text_search(knife_extract_text("measure".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__description_gin_trgm
ON measure
USING GIN ((aidbox_text_search(knife_extract_text("measure".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.depends-on`

```sql
WIP: work in progress
```

Search parameter: `Measure.status`, `Measure.composed-of`, `Measure.jurisdiction`, `Measure.context`, `Measure.context-type`, `Measure.predecessor`, `Measure.version`, `Measure.url`, `Measure.derived-from`, `Measure.successor`, `Measure.topic`, `Measure.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__gin
ON measure
USING GIN (resource);
```

Search parameter: `Measure.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__name_gin_trgm
ON measure
USING GIN ((aidbox_text_search(knife_extract_text("measure".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Measure.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `Measure.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measure_resource__title_gin_trgm
ON measure
USING GIN ((aidbox_text_search(knife_extract_text("measure".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

### Person

Search parameter: `Person.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_gin_trgm
ON person
USING GIN ((aidbox_text_search(knife_extract_text("person".resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.birthdate`

```sql
WIP: work in progress
```

Search parameter: `Person.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_state_gin_trgm
ON person
USING GIN ((aidbox_text_search(knife_extract_text("person".resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_country_gin_trgm
ON person
USING GIN ((aidbox_text_search(knife_extract_text("person".resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.link`, `Person.patient`, `Person.relatedperson`, `Person.organization`, `Person.telecom`, `Person.identifier`, `Person.gender`, `Person.email`, `Person.practitioner`, `Person.phone`, `Person.address-use`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__gin
ON person
USING GIN (resource);
```

Search parameter: `Person.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_postalcode_gin_trgm
ON person
USING GIN ((aidbox_text_search(knife_extract_text("person".resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.name`, `Person.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__name_gin_trgm
ON person
USING GIN ((aidbox_text_search(knife_extract_text("person".resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Person.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS person_resource__address_city_gin_trgm
ON person
USING GIN ((aidbox_text_search(knife_extract_text("person".resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

### InsurancePlan

Search parameter: `InsurancePlan.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_city_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["contact","address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__phonetic_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_country_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["contact","address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_postalcode_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["contact","address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.status`, `InsurancePlan.identifier`, `InsurancePlan.endpoint`, `InsurancePlan.type`, `InsurancePlan.administered-by`, `InsurancePlan.address-use`, `InsurancePlan.owned-by`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__gin
ON insuranceplan
USING GIN (resource);
```

Search parameter: `InsurancePlan.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["contact","address","text"],["contact","address","district"],["contact","address","country"],["contact","address","city"],["contact","address","line"],["contact","address","state"],["contact","address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__name_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["name"],["alias"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `InsurancePlan.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS insuranceplan_resource__address_state_gin_trgm
ON insuranceplan
USING GIN ((aidbox_text_search(knife_extract_text("insuranceplan".resource, $JSON$[["contact","address","state"]]$JSON$))) gin_trgm_ops);
```

### Patient

Search parameter: `Patient.death-date`, `Patient.birthdate`

```sql
WIP: work in progress
```

Search parameter: `Patient.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_state_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.family`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__family_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["name","family"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_city_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.active`, `Patient.general-practitioner`, `Patient.gender`, `Patient.telecom`, `Patient.identifier`, `Patient.link`, `Patient.address-use`, `Patient.language`, `Patient.deceased`, `Patient.email`, `Patient.organization`, `Patient.phone`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__gin
ON patient
USING GIN (resource);
```

Search parameter: `Patient.given`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__given_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["name","given"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.name`, `Patient.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__name_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_postalcode_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Patient.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS patient_resource__address_country_gin_trgm
ON patient
USING GIN ((aidbox_text_search(knife_extract_text("patient".resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

### EffectEvidenceSynthesis

Search parameter: `EffectEvidenceSynthesis.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__publisher_gin_trgm
ON effectevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("effectevidencesynthesis".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.effective`, `EffectEvidenceSynthesis.date`

```sql
WIP: work in progress
```

Search parameter: `EffectEvidenceSynthesis.context`, `EffectEvidenceSynthesis.context-type`, `EffectEvidenceSynthesis.version`, `EffectEvidenceSynthesis.url`, `EffectEvidenceSynthesis.identifier`, `EffectEvidenceSynthesis.jurisdiction`, `EffectEvidenceSynthesis.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__gin
ON effectevidencesynthesis
USING GIN (resource);
```

Search parameter: `EffectEvidenceSynthesis.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__title_gin_trgm
ON effectevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("effectevidencesynthesis".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__description_gin_trgm
ON effectevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("effectevidencesynthesis".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `EffectEvidenceSynthesis.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `EffectEvidenceSynthesis.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS effectevidencesynthesis_resource__name_gin_trgm
ON effectevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("effectevidencesynthesis".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### ResearchSubject

Search parameter: `ResearchSubject.date`

```sql
WIP: work in progress
```

Search parameter: `ResearchSubject.individual`, `ResearchSubject.study`, `ResearchSubject.patient`, `ResearchSubject.identifier`, `ResearchSubject.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS researchsubject_resource__gin
ON researchsubject
USING GIN (resource);
```

### Medication

Search parameter: `Medication.expiration-date`

```sql
WIP: work in progress
```

Search parameter: `Medication.code`, `Medication.lot-number`, `Medication.identifier`, `Medication.ingredient-code`, `Medication.ingredient`, `Medication.status`, `Medication.form`, `Medication.manufacturer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medication_resource__gin
ON medication
USING GIN (resource);
```

### ConceptMap

Search parameter: `ConceptMap.target-code`, `ConceptMap.other`, `ConceptMap.context-type`, `ConceptMap.dependson`, `ConceptMap.source-code`, `ConceptMap.target-system`, `ConceptMap.status`, `ConceptMap.source`, `ConceptMap.url`, `ConceptMap.version`, `ConceptMap.identifier`, `ConceptMap.target`, `ConceptMap.product`, `ConceptMap.jurisdiction`, `ConceptMap.source-system`, `ConceptMap.target-uri`, `ConceptMap.context`, `ConceptMap.source-uri`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__gin
ON conceptmap
USING GIN (resource);
```

Search parameter: `ConceptMap.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__description_gin_trgm
ON conceptmap
USING GIN ((aidbox_text_search(knife_extract_text("conceptmap".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ConceptMap.date`

```sql
WIP: work in progress
```

Search parameter: `ConceptMap.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__publisher_gin_trgm
ON conceptmap
USING GIN ((aidbox_text_search(knife_extract_text("conceptmap".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__title_gin_trgm
ON conceptmap
USING GIN ((aidbox_text_search(knife_extract_text("conceptmap".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ConceptMap.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS conceptmap_resource__name_gin_trgm
ON conceptmap
USING GIN ((aidbox_text_search(knife_extract_text("conceptmap".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### CoverageEligibilityRequest

Search parameter: `CoverageEligibilityRequest.created`

```sql
WIP: work in progress
```

Search parameter: `CoverageEligibilityRequest.patient`, `CoverageEligibilityRequest.status`, `CoverageEligibilityRequest.enterer`, `CoverageEligibilityRequest.facility`, `CoverageEligibilityRequest.provider`, `CoverageEligibilityRequest.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS coverageeligibilityrequest_resource__gin
ON coverageeligibilityrequest
USING GIN (resource);
```

### VisionPrescription

Search parameter: `VisionPrescription.datewritten`

```sql
WIP: work in progress
```

Search parameter: `VisionPrescription.patient`, `VisionPrescription.status`, `VisionPrescription.prescriber`, `VisionPrescription.identifier`, `VisionPrescription.encounter`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS visionprescription_resource__gin
ON visionprescription
USING GIN (resource);
```

### MolecularSequence

Search parameter: `MolecularSequence.variant-end`

```sql
WIP: work in progress
```

Search parameter: `MolecularSequence.patient`, `MolecularSequence.chromosome`, `MolecularSequence.type`, `MolecularSequence.identifier`, `MolecularSequence.referenceseqid`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS molecularsequence_resource__gin
ON molecularsequence
USING GIN (resource);
```

Search parameter: `MolecularSequence.window-start`

```sql
WIP: work in progress
```

Search parameter: `MolecularSequence.window-end`

```sql
WIP: work in progress
```

Search parameter: `MolecularSequence.variant-start`

```sql
WIP: work in progress
```

### MedicinalProductUndesirableEffect

Search parameter: `MedicinalProductUndesirableEffect.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductundesirableeffect_resource__gin
ON medicinalproductundesirableeffect
USING GIN (resource);
```

### MessageHeader

Search parameter: `MessageHeader.destination`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messageheader_resource__destination_gin_trgm
ON messageheader
USING GIN ((aidbox_text_search(knife_extract_text("messageheader".resource, $JSON$[["destination","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageHeader.source`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messageheader_resource__source_gin_trgm
ON messageheader
USING GIN ((aidbox_text_search(knife_extract_text("messageheader".resource, $JSON$[["source","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `MessageHeader.event`

```sql
WIP: work in progress
```

Search parameter: `MessageHeader.enterer`, `MessageHeader.response-id`, `MessageHeader.author`, `MessageHeader.destination-uri`, `MessageHeader.source-uri`, `MessageHeader.focus`, `MessageHeader.code`, `MessageHeader.sender`, `MessageHeader.receiver`, `MessageHeader.responsible`, `MessageHeader.target`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS messageheader_resource__gin
ON messageheader
USING GIN (resource);
```

### AllergyIntolerance

Search parameter: `AllergyIntolerance.clinical-status`, `AllergyIntolerance.recorder`, `AllergyIntolerance.asserter`, `AllergyIntolerance.manifestation`, `AllergyIntolerance.severity`, `AllergyIntolerance.verification-status`, `AllergyIntolerance.identifier`, `AllergyIntolerance.patient`, `AllergyIntolerance.route`, `AllergyIntolerance.category`, `AllergyIntolerance.type`, `AllergyIntolerance.criticality`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS allergyintolerance_resource__gin
ON allergyintolerance
USING GIN (resource);
```

Search parameter: `AllergyIntolerance.last-date`, `AllergyIntolerance.onset`, `AllergyIntolerance.date`

```sql
WIP: work in progress
```

Search parameter: `AllergyIntolerance.code`

```sql
WIP: work in progress
```

### SupplyDelivery

Search parameter: `SupplyDelivery.status`, `SupplyDelivery.identifier`, `SupplyDelivery.receiver`, `SupplyDelivery.supplier`, `SupplyDelivery.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS supplydelivery_resource__gin
ON supplydelivery
USING GIN (resource);
```

### EpisodeOfCare

Search parameter: `EpisodeOfCare.date`

```sql
WIP: work in progress
```

Search parameter: `EpisodeOfCare.patient`, `EpisodeOfCare.incoming-referral`, `EpisodeOfCare.status`, `EpisodeOfCare.condition`, `EpisodeOfCare.identifier`, `EpisodeOfCare.organization`, `EpisodeOfCare.type`, `EpisodeOfCare.care-manager`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS episodeofcare_resource__gin
ON episodeofcare
USING GIN (resource);
```

### PractitionerRole

Search parameter: `PractitionerRole.endpoint`, `PractitionerRole.organization`, `PractitionerRole.email`, `PractitionerRole.service`, `PractitionerRole.active`, `PractitionerRole.role`, `PractitionerRole.practitioner`, `PractitionerRole.identifier`, `PractitionerRole.phone`, `PractitionerRole.specialty`, `PractitionerRole.location`, `PractitionerRole.telecom`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitionerrole_resource__gin
ON practitionerrole
USING GIN (resource);
```

Search parameter: `PractitionerRole.date`

```sql
WIP: work in progress
```

### Library

Search parameter: `Library.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__title_gin_trgm
ON library
USING GIN ((aidbox_text_search(knife_extract_text("library".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.type`, `Library.jurisdiction`, `Library.identifier`, `Library.topic`, `Library.status`, `Library.depends-on`, `Library.context-type`, `Library.content-type`, `Library.successor`, `Library.context`, `Library.version`, `Library.derived-from`, `Library.predecessor`, `Library.composed-of`, `Library.url`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__gin
ON library
USING GIN (resource);
```

Search parameter: `Library.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__description_gin_trgm
ON library
USING GIN ((aidbox_text_search(knife_extract_text("library".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.date`, `Library.effective`

```sql
WIP: work in progress
```

Search parameter: `Library.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `Library.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__publisher_gin_trgm
ON library
USING GIN ((aidbox_text_search(knife_extract_text("library".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Library.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS library_resource__name_gin_trgm
ON library
USING GIN ((aidbox_text_search(knife_extract_text("library".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### Practitioner

Search parameter: `Practitioner.name`, `Practitioner.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__name_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.given`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__given_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["name","given"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.identifier`, `Practitioner.active`, `Practitioner.phone`, `Practitioner.communication`, `Practitioner.telecom`, `Practitioner.address-use`, `Practitioner.email`, `Practitioner.gender`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__gin
ON practitioner
USING GIN (resource);
```

Search parameter: `Practitioner.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_state_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_postalcode_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_city_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.family`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__family_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["name","family"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Practitioner.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS practitioner_resource__address_country_gin_trgm
ON practitioner
USING GIN ((aidbox_text_search(knife_extract_text("practitioner".resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

### MedicationRequest

Search parameter: `MedicationRequest.intended-dispenser`, `MedicationRequest.priority`, `MedicationRequest.identifier`, `MedicationRequest.encounter`, `MedicationRequest.requester`, `MedicationRequest.category`, `MedicationRequest.code`, `MedicationRequest.patient`, `MedicationRequest.medication`, `MedicationRequest.status`, `MedicationRequest.intended-performer`, `MedicationRequest.intended-performertype`, `MedicationRequest.subject`, `MedicationRequest.intent`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationrequest_resource__gin
ON medicationrequest
USING GIN (resource);
```

Search parameter: `MedicationRequest.authoredon`, `MedicationRequest.date`

```sql
WIP: work in progress
```

### ImmunizationRecommendation

Search parameter: `ImmunizationRecommendation.identifier`, `ImmunizationRecommendation.vaccine-type`, `ImmunizationRecommendation.target-disease`, `ImmunizationRecommendation.information`, `ImmunizationRecommendation.support`, `ImmunizationRecommendation.status`, `ImmunizationRecommendation.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunizationrecommendation_resource__gin
ON immunizationrecommendation
USING GIN (resource);
```

Search parameter: `ImmunizationRecommendation.date`

```sql
WIP: work in progress
```

### Immunization

Search parameter: `Immunization.date`, `Immunization.reaction-date`

```sql
WIP: work in progress
```

Search parameter: `Immunization.series`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__series_gin_trgm
ON immunization
USING GIN ((aidbox_text_search(knife_extract_text("immunization".resource, $JSON$[["protocolApplied","series"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Immunization.manufacturer`, `Immunization.location`, `Immunization.reaction`, `Immunization.vaccine-code`, `Immunization.target-disease`, `Immunization.performer`, `Immunization.status-reason`, `Immunization.reason-reference`, `Immunization.status`, `Immunization.patient`, `Immunization.reason-code`, `Immunization.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__gin
ON immunization
USING GIN (resource);
```

Search parameter: `Immunization.lot-number`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS immunization_resource__lot_number_gin_trgm
ON immunization
USING GIN ((aidbox_text_search(knife_extract_text("immunization".resource, $JSON$[["lotNumber"]]$JSON$))) gin_trgm_ops);
```

### GraphDefinition

Search parameter: `GraphDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `GraphDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__description_gin_trgm
ON graphdefinition
USING GIN ((aidbox_text_search(knife_extract_text("graphdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `GraphDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `GraphDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__publisher_gin_trgm
ON graphdefinition
USING GIN ((aidbox_text_search(knife_extract_text("graphdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `GraphDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__name_gin_trgm
ON graphdefinition
USING GIN ((aidbox_text_search(knife_extract_text("graphdefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `GraphDefinition.status`, `GraphDefinition.version`, `GraphDefinition.context`, `GraphDefinition.context-type`, `GraphDefinition.jurisdiction`, `GraphDefinition.url`, `GraphDefinition.start`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS graphdefinition_resource__gin
ON graphdefinition
USING GIN (resource);
```

### Account

Search parameter: `Account.period`

```sql
WIP: work in progress
```

Search parameter: `Account.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS account_resource__name_gin_trgm
ON account
USING GIN ((aidbox_text_search(knife_extract_text("account".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Account.patient`, `Account.identifier`, `Account.owner`, `Account.subject`, `Account.type`, `Account.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS account_resource__gin
ON account
USING GIN (resource);
```

### MeasureReport

Search parameter: `MeasureReport.period`, `MeasureReport.date`

```sql
WIP: work in progress
```

Search parameter: `MeasureReport.status`, `MeasureReport.identifier`, `MeasureReport.reporter`, `MeasureReport.measure`, `MeasureReport.evaluated-resource`, `MeasureReport.subject`, `MeasureReport.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS measurereport_resource__gin
ON measurereport
USING GIN (resource);
```

### DeviceMetric

Search parameter: `DeviceMetric.parent`, `DeviceMetric.type`, `DeviceMetric.source`, `DeviceMetric.identifier`, `DeviceMetric.category`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicemetric_resource__gin
ON devicemetric
USING GIN (resource);
```

### Goal

Search parameter: `Goal.start-date`, `Goal.target-date`

```sql
WIP: work in progress
```

Search parameter: `Goal.identifier`, `Goal.patient`, `Goal.lifecycle-status`, `Goal.category`, `Goal.subject`, `Goal.achievement-status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS goal_resource__gin
ON goal
USING GIN (resource);
```

### MedicationKnowledge

Search parameter: `MedicationKnowledge.manufacturer`, `MedicationKnowledge.source-cost`, `MedicationKnowledge.code`, `MedicationKnowledge.ingredient`, `MedicationKnowledge.status`, `MedicationKnowledge.doseform`, `MedicationKnowledge.classification`, `MedicationKnowledge.ingredient-code`, `MedicationKnowledge.monitoring-program-type`, `MedicationKnowledge.classification-type`, `MedicationKnowledge.monograph`, `MedicationKnowledge.monograph-type`, `MedicationKnowledge.monitoring-program-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicationknowledge_resource__gin
ON medicationknowledge
USING GIN (resource);
```

### ClaimResponse

Search parameter: `ClaimResponse.requestor`, `ClaimResponse.request`, `ClaimResponse.identifier`, `ClaimResponse.status`, `ClaimResponse.outcome`, `ClaimResponse.patient`, `ClaimResponse.use`, `ClaimResponse.insurer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__gin
ON claimresponse
USING GIN (resource);
```

Search parameter: `ClaimResponse.disposition`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS claimresponse_resource__disposition_gin_trgm
ON claimresponse
USING GIN ((aidbox_text_search(knife_extract_text("claimresponse".resource, $JSON$[["disposition"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ClaimResponse.payment-date`, `ClaimResponse.created`

```sql
WIP: work in progress
```

### DeviceDefinition

Search parameter: `DeviceDefinition.parent`, `DeviceDefinition.type`, `DeviceDefinition.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicedefinition_resource__gin
ON devicedefinition
USING GIN (resource);
```

### Slot

Search parameter: `Slot.start`

```sql
WIP: work in progress
```

Search parameter: `Slot.service-category`, `Slot.identifier`, `Slot.status`, `Slot.appointment-type`, `Slot.service-type`, `Slot.specialty`, `Slot.schedule`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS slot_resource__gin
ON slot
USING GIN (resource);
```

### ValueSet

Search parameter: `ValueSet.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ValueSet.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__publisher_gin_trgm
ON valueset
USING GIN ((aidbox_text_search(knife_extract_text("valueset".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.reference`, `ValueSet.jurisdiction`, `ValueSet.url`, `ValueSet.context`, `ValueSet.context-type`, `ValueSet.status`, `ValueSet.identifier`, `ValueSet.version`, `ValueSet.expansion`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__gin
ON valueset
USING GIN (resource);
```

Search parameter: `ValueSet.code`

```sql
WIP: work in progress
```

Search parameter: `ValueSet.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__name_gin_trgm
ON valueset
USING GIN ((aidbox_text_search(knife_extract_text("valueset".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.date`

```sql
WIP: work in progress
```

Search parameter: `ValueSet.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__description_gin_trgm
ON valueset
USING GIN ((aidbox_text_search(knife_extract_text("valueset".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ValueSet.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS valueset_resource__title_gin_trgm
ON valueset
USING GIN ((aidbox_text_search(knife_extract_text("valueset".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

### MedicinalProductAuthorization

Search parameter: `MedicinalProductAuthorization.holder`, `MedicinalProductAuthorization.country`, `MedicinalProductAuthorization.subject`, `MedicinalProductAuthorization.status`, `MedicinalProductAuthorization.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductauthorization_resource__gin
ON medicinalproductauthorization
USING GIN (resource);
```

### MedicinalProductContraindication

Search parameter: `MedicinalProductContraindication.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductcontraindication_resource__gin
ON medicinalproductcontraindication
USING GIN (resource);
```

### DeviceRequest

Search parameter: `DeviceRequest.instantiates-uri`, `DeviceRequest.device`, `DeviceRequest.insurance`, `DeviceRequest.requester`, `DeviceRequest.status`, `DeviceRequest.based-on`, `DeviceRequest.encounter`, `DeviceRequest.intent`, `DeviceRequest.identifier`, `DeviceRequest.group-identifier`, `DeviceRequest.prior-request`, `DeviceRequest.code`, `DeviceRequest.subject`, `DeviceRequest.patient`, `DeviceRequest.instantiates-canonical`, `DeviceRequest.performer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS devicerequest_resource__gin
ON devicerequest
USING GIN (resource);
```

Search parameter: `DeviceRequest.event-date`, `DeviceRequest.authored-on`

```sql
WIP: work in progress
```

### List

Search parameter: `List.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__title_gin_trgm
ON list
USING GIN ((aidbox_text_search(knife_extract_text("list".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `List.notes`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__notes_gin_trgm
ON list
USING GIN ((aidbox_text_search(knife_extract_text("list".resource, $JSON$[["note","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `List.date`

```sql
WIP: work in progress
```

Search parameter: `List.patient`, `List.source`, `List.item`, `List.encounter`, `List.empty-reason`, `List.status`, `List.code`, `List.subject`, `List.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS list_resource__gin
ON list
USING GIN (resource);
```

### Questionnaire

Search parameter: `Questionnaire.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `Questionnaire.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__title_gin_trgm
ON questionnaire
USING GIN ((aidbox_text_search(knife_extract_text("questionnaire".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__name_gin_trgm
ON questionnaire
USING GIN ((aidbox_text_search(knife_extract_text("questionnaire".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.effective`, `Questionnaire.date`

```sql
WIP: work in progress
```

Search parameter: `Questionnaire.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__publisher_gin_trgm
ON questionnaire
USING GIN ((aidbox_text_search(knife_extract_text("questionnaire".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__description_gin_trgm
ON questionnaire
USING GIN ((aidbox_text_search(knife_extract_text("questionnaire".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Questionnaire.definition`, `Questionnaire.code`, `Questionnaire.identifier`, `Questionnaire.subject-type`, `Questionnaire.context-type`, `Questionnaire.context`, `Questionnaire.url`, `Questionnaire.jurisdiction`, `Questionnaire.status`, `Questionnaire.version`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS questionnaire_resource__gin
ON questionnaire
USING GIN (resource);
```

### Endpoint

Search parameter: `Endpoint.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS endpoint_resource__name_gin_trgm
ON endpoint
USING GIN ((aidbox_text_search(knife_extract_text("endpoint".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Endpoint.payload-type`, `Endpoint.connection-type`, `Endpoint.organization`, `Endpoint.status`, `Endpoint.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS endpoint_resource__gin
ON endpoint
USING GIN (resource);
```

### NamingSystem

Search parameter: `NamingSystem.value`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__value_gin_trgm
ON namingsystem
USING GIN ((aidbox_text_search(knife_extract_text("namingsystem".resource, $JSON$[["uniqueId","value"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__publisher_gin_trgm
ON namingsystem
USING GIN ((aidbox_text_search(knife_extract_text("namingsystem".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `NamingSystem.date`, `NamingSystem.period`

```sql
WIP: work in progress
```

Search parameter: `NamingSystem.contact`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__contact_gin_trgm
ON namingsystem
USING GIN ((aidbox_text_search(knife_extract_text("namingsystem".resource, $JSON$[["contact","name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__name_gin_trgm
ON namingsystem
USING GIN ((aidbox_text_search(knife_extract_text("namingsystem".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__description_gin_trgm
ON namingsystem
USING GIN ((aidbox_text_search(knife_extract_text("namingsystem".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.responsible`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__responsible_gin_trgm
ON namingsystem
USING GIN ((aidbox_text_search(knife_extract_text("namingsystem".resource, $JSON$[["responsible"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `NamingSystem.type`, `NamingSystem.kind`, `NamingSystem.jurisdiction`, `NamingSystem.telecom`, `NamingSystem.status`, `NamingSystem.context-type`, `NamingSystem.id-type`, `NamingSystem.context`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS namingsystem_resource__gin
ON namingsystem
USING GIN (resource);
```

### MedicinalProductPackaged

Search parameter: `MedicinalProductPackaged.subject`, `MedicinalProductPackaged.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductpackaged_resource__gin
ON medicinalproductpackaged
USING GIN (resource);
```

### Basic

Search parameter: `Basic.created`

```sql
WIP: work in progress
```

Search parameter: `Basic.code`, `Basic.patient`, `Basic.identifier`, `Basic.author`, `Basic.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS basic_resource__gin
ON basic
USING GIN (resource);
```

### PlanDefinition

Search parameter: `PlanDefinition.depends-on`

```sql
WIP: work in progress
```

Search parameter: `PlanDefinition.date`, `PlanDefinition.effective`

```sql
WIP: work in progress
```

Search parameter: `PlanDefinition.type`, `PlanDefinition.url`, `PlanDefinition.definition`, `PlanDefinition.context-type`, `PlanDefinition.topic`, `PlanDefinition.derived-from`, `PlanDefinition.jurisdiction`, `PlanDefinition.predecessor`, `PlanDefinition.identifier`, `PlanDefinition.context`, `PlanDefinition.successor`, `PlanDefinition.version`, `PlanDefinition.status`, `PlanDefinition.composed-of`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__gin
ON plandefinition
USING GIN (resource);
```

Search parameter: `PlanDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__name_gin_trgm
ON plandefinition
USING GIN ((aidbox_text_search(knife_extract_text("plandefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__publisher_gin_trgm
ON plandefinition
USING GIN ((aidbox_text_search(knife_extract_text("plandefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `PlanDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__description_gin_trgm
ON plandefinition
USING GIN ((aidbox_text_search(knife_extract_text("plandefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `PlanDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS plandefinition_resource__title_gin_trgm
ON plandefinition
USING GIN ((aidbox_text_search(knife_extract_text("plandefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

### RelatedPerson

Search parameter: `RelatedPerson.address-use`, `RelatedPerson.email`, `RelatedPerson.identifier`, `RelatedPerson.active`, `RelatedPerson.relationship`, `RelatedPerson.gender`, `RelatedPerson.phone`, `RelatedPerson.telecom`, `RelatedPerson.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__gin
ON relatedperson
USING GIN (resource);
```

Search parameter: `RelatedPerson.address-state`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_state_gin_trgm
ON relatedperson
USING GIN ((aidbox_text_search(knife_extract_text("relatedperson".resource, $JSON$[["address","state"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_gin_trgm
ON relatedperson
USING GIN ((aidbox_text_search(knife_extract_text("relatedperson".resource, $JSON$[["address","text"],["address","district"],["address","country"],["address","city"],["address","line"],["address","state"],["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.birthdate`

```sql
WIP: work in progress
```

Search parameter: `RelatedPerson.address-postalcode`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_postalcode_gin_trgm
ON relatedperson
USING GIN ((aidbox_text_search(knife_extract_text("relatedperson".resource, $JSON$[["address","postalCode"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address-city`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_city_gin_trgm
ON relatedperson
USING GIN ((aidbox_text_search(knife_extract_text("relatedperson".resource, $JSON$[["address","city"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.address-country`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__address_country_gin_trgm
ON relatedperson
USING GIN ((aidbox_text_search(knife_extract_text("relatedperson".resource, $JSON$[["address","country"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RelatedPerson.name`, `RelatedPerson.phonetic`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS relatedperson_resource__name_gin_trgm
ON relatedperson
USING GIN ((aidbox_text_search(knife_extract_text("relatedperson".resource, $JSON$[["name","family"],["name","given"],["name","middle"],["name","text"]]$JSON$))) gin_trgm_ops);
```

### SubstanceSpecification

Search parameter: `SubstanceSpecification.code`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS substancespecification_resource__gin
ON substancespecification
USING GIN (resource);
```

### GuidanceResponse

Search parameter: `GuidanceResponse.patient`, `GuidanceResponse.request`, `GuidanceResponse.subject`, `GuidanceResponse.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS guidanceresponse_resource__gin
ON guidanceresponse
USING GIN (resource);
```

### ClinicalImpression

Search parameter: `ClinicalImpression.date`

```sql
WIP: work in progress
```

Search parameter: `ClinicalImpression.supporting-info`, `ClinicalImpression.assessor`, `ClinicalImpression.identifier`, `ClinicalImpression.patient`, `ClinicalImpression.status`, `ClinicalImpression.problem`, `ClinicalImpression.investigation`, `ClinicalImpression.encounter`, `ClinicalImpression.finding-ref`, `ClinicalImpression.previous`, `ClinicalImpression.finding-code`, `ClinicalImpression.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS clinicalimpression_resource__gin
ON clinicalimpression
USING GIN (resource);
```

### OrganizationAffiliation

Search parameter: `OrganizationAffiliation.specialty`, `OrganizationAffiliation.participating-organization`, `OrganizationAffiliation.endpoint`, `OrganizationAffiliation.network`, `OrganizationAffiliation.identifier`, `OrganizationAffiliation.service`, `OrganizationAffiliation.primary-organization`, `OrganizationAffiliation.location`, `OrganizationAffiliation.role`, `OrganizationAffiliation.telecom`, `OrganizationAffiliation.active`, `OrganizationAffiliation.phone`, `OrganizationAffiliation.email`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS organizationaffiliation_resource__gin
ON organizationaffiliation
USING GIN (resource);
```

Search parameter: `OrganizationAffiliation.date`

```sql
WIP: work in progress
```

### Condition

Search parameter: `Condition.onset-age`

```sql
WIP: work in progress
```

Search parameter: `Condition.abatement-string`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__abatement_string_gin_trgm
ON condition
USING GIN ((aidbox_text_search(knife_extract_text("condition".resource, $JSON$[["abatement","string"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Condition.abatement-age`

```sql
WIP: work in progress
```

Search parameter: `Condition.onset-date`, `Condition.recorded-date`, `Condition.abatement-date`

```sql
WIP: work in progress
```

Search parameter: `Condition.onset-info`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__onset_info_gin_trgm
ON condition
USING GIN ((aidbox_text_search(knife_extract_text("condition".resource, $JSON$[["onset","string"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Condition.identifier`, `Condition.category`, `Condition.verification-status`, `Condition.stage`, `Condition.evidence-detail`, `Condition.asserter`, `Condition.body-site`, `Condition.evidence`, `Condition.code`, `Condition.severity`, `Condition.encounter`, `Condition.patient`, `Condition.clinical-status`, `Condition.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS condition_resource__gin
ON condition
USING GIN (resource);
```

### HealthcareService

Search parameter: `HealthcareService.active`, `HealthcareService.service-category`, `HealthcareService.location`, `HealthcareService.endpoint`, `HealthcareService.service-type`, `HealthcareService.organization`, `HealthcareService.program`, `HealthcareService.coverage-area`, `HealthcareService.identifier`, `HealthcareService.characteristic`, `HealthcareService.specialty`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS healthcareservice_resource__gin
ON healthcareservice
USING GIN (resource);
```

Search parameter: `HealthcareService.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS healthcareservice_resource__name_gin_trgm
ON healthcareservice
USING GIN ((aidbox_text_search(knife_extract_text("healthcareservice".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

### SpecimenDefinition

Search parameter: `SpecimenDefinition.container`, `SpecimenDefinition.type`, `SpecimenDefinition.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS specimendefinition_resource__gin
ON specimendefinition
USING GIN (resource);
```

### RiskAssessment

Search parameter: `RiskAssessment.date`

```sql
WIP: work in progress
```

Search parameter: `RiskAssessment.patient`, `RiskAssessment.condition`, `RiskAssessment.identifier`, `RiskAssessment.risk`, `RiskAssessment.subject`, `RiskAssessment.performer`, `RiskAssessment.method`, `RiskAssessment.encounter`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskassessment_resource__gin
ON riskassessment
USING GIN (resource);
```

Search parameter: `RiskAssessment.probability`

```sql
WIP: work in progress
```

### OperationDefinition

Search parameter: `OperationDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__description_gin_trgm
ON operationdefinition
USING GIN ((aidbox_text_search(knife_extract_text("operationdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__title_gin_trgm
ON operationdefinition
USING GIN ((aidbox_text_search(knife_extract_text("operationdefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.base`, `OperationDefinition.context-type`, `OperationDefinition.version`, `OperationDefinition.kind`, `OperationDefinition.code`, `OperationDefinition.status`, `OperationDefinition.instance`, `OperationDefinition.system`, `OperationDefinition.type`, `OperationDefinition.url`, `OperationDefinition.output-profile`, `OperationDefinition.jurisdiction`, `OperationDefinition.input-profile`, `OperationDefinition.context`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__gin
ON operationdefinition
USING GIN (resource);
```

Search parameter: `OperationDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `OperationDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `OperationDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__name_gin_trgm
ON operationdefinition
USING GIN ((aidbox_text_search(knife_extract_text("operationdefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `OperationDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS operationdefinition_resource__publisher_gin_trgm
ON operationdefinition
USING GIN ((aidbox_text_search(knife_extract_text("operationdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

### ActivityDefinition

Search parameter: `ActivityDefinition.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__name_gin_trgm
ON activitydefinition
USING GIN ((aidbox_text_search(knife_extract_text("activitydefinition".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.depends-on`

```sql
WIP: work in progress
```

Search parameter: `ActivityDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__publisher_gin_trgm
ON activitydefinition
USING GIN ((aidbox_text_search(knife_extract_text("activitydefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.derived-from`, `ActivityDefinition.successor`, `ActivityDefinition.context`, `ActivityDefinition.predecessor`, `ActivityDefinition.url`, `ActivityDefinition.composed-of`, `ActivityDefinition.topic`, `ActivityDefinition.jurisdiction`, `ActivityDefinition.identifier`, `ActivityDefinition.status`, `ActivityDefinition.context-type`, `ActivityDefinition.version`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__gin
ON activitydefinition
USING GIN (resource);
```

Search parameter: `ActivityDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__description_gin_trgm
ON activitydefinition
USING GIN ((aidbox_text_search(knife_extract_text("activitydefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS activitydefinition_resource__title_gin_trgm
ON activitydefinition
USING GIN ((aidbox_text_search(knife_extract_text("activitydefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ActivityDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ActivityDefinition.effective`, `ActivityDefinition.date`

```sql
WIP: work in progress
```

### Schedule

Search parameter: `Schedule.date`

```sql
WIP: work in progress
```

Search parameter: `Schedule.actor`, `Schedule.identifier`, `Schedule.active`, `Schedule.specialty`, `Schedule.service-category`, `Schedule.service-type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS schedule_resource__gin
ON schedule
USING GIN (resource);
```

### Group

Search parameter: `Group.type`, `Group.exclude`, `Group.characteristic`, `Group.member`, `Group.identifier`, `Group.managing-entity`, `Group.code`, `Group.actual`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS group_resource__gin
ON group
USING GIN (resource);
```

Search parameter: `Group.value`

```sql
WIP: work in progress
```

### MedicinalProductPharmaceutical

Search parameter: `MedicinalProductPharmaceutical.target-species`, `MedicinalProductPharmaceutical.route`, `MedicinalProductPharmaceutical.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS medicinalproductpharmaceutical_resource__gin
ON medicinalproductpharmaceutical
USING GIN (resource);
```

### FamilyMemberHistory

Search parameter: `FamilyMemberHistory.code`, `FamilyMemberHistory.sex`, `FamilyMemberHistory.instantiates-canonical`, `FamilyMemberHistory.relationship`, `FamilyMemberHistory.instantiates-uri`, `FamilyMemberHistory.patient`, `FamilyMemberHistory.status`, `FamilyMemberHistory.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS familymemberhistory_resource__gin
ON familymemberhistory
USING GIN (resource);
```

Search parameter: `FamilyMemberHistory.date`

```sql
WIP: work in progress
```

### ServiceRequest

Search parameter: `ServiceRequest.specimen`, `ServiceRequest.category`, `ServiceRequest.based-on`, `ServiceRequest.encounter`, `ServiceRequest.performer-type`, `ServiceRequest.performer`, `ServiceRequest.identifier`, `ServiceRequest.subject`, `ServiceRequest.patient`, `ServiceRequest.priority`, `ServiceRequest.instantiates-uri`, `ServiceRequest.replaces`, `ServiceRequest.code`, `ServiceRequest.body-site`, `ServiceRequest.requester`, `ServiceRequest.status`, `ServiceRequest.intent`, `ServiceRequest.requisition`, `ServiceRequest.instantiates-canonical`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS servicerequest_resource__gin
ON servicerequest
USING GIN (resource);
```

Search parameter: `ServiceRequest.occurrence`, `ServiceRequest.authored`

```sql
WIP: work in progress
```

### DetectedIssue

Search parameter: `DetectedIssue.patient`, `DetectedIssue.author`, `DetectedIssue.code`, `DetectedIssue.identifier`, `DetectedIssue.implicated`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS detectedissue_resource__gin
ON detectedissue
USING GIN (resource);
```

Search parameter: `DetectedIssue.identified`

```sql
WIP: work in progress
```

### Device

Search parameter: `Device.model`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__model_gin_trgm
ON device
USING GIN ((aidbox_text_search(knife_extract_text("device".resource, $JSON$[["modelNumber"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.udi-di`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__udi_di_gin_trgm
ON device
USING GIN ((aidbox_text_search(knife_extract_text("device".resource, $JSON$[["udiCarrier","deviceIdentifier"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.device-name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__device_name_gin_trgm
ON device
USING GIN ((aidbox_text_search(knife_extract_text("device".resource, $JSON$[["deviceName","name"],["type","coding","display"],["type","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.patient`, `Device.location`, `Device.organization`, `Device.identifier`, `Device.status`, `Device.type`, `Device.url`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__gin
ON device
USING GIN (resource);
```

Search parameter: `Device.udi-carrier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__udi_carrier_gin_trgm
ON device
USING GIN ((aidbox_text_search(knife_extract_text("device".resource, $JSON$[["udiCarrier","carrierHRF"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Device.manufacturer`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS device_resource__manufacturer_gin_trgm
ON device
USING GIN ((aidbox_text_search(knife_extract_text("device".resource, $JSON$[["manufacturer"]]$JSON$))) gin_trgm_ops);
```

### RequestGroup

Search parameter: `RequestGroup.authored`

```sql
WIP: work in progress
```

Search parameter: `RequestGroup.encounter`, `RequestGroup.instantiates-canonical`, `RequestGroup.identifier`, `RequestGroup.code`, `RequestGroup.status`, `RequestGroup.subject`, `RequestGroup.intent`, `RequestGroup.priority`, `RequestGroup.author`, `RequestGroup.participant`, `RequestGroup.patient`, `RequestGroup.instantiates-uri`, `RequestGroup.group-identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS requestgroup_resource__gin
ON requestgroup
USING GIN (resource);
```

### RiskEvidenceSynthesis

Search parameter: `RiskEvidenceSynthesis.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__description_gin_trgm
ON riskevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("riskevidencesynthesis".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.status`, `RiskEvidenceSynthesis.identifier`, `RiskEvidenceSynthesis.version`, `RiskEvidenceSynthesis.context-type`, `RiskEvidenceSynthesis.url`, `RiskEvidenceSynthesis.jurisdiction`, `RiskEvidenceSynthesis.context`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__gin
ON riskevidencesynthesis
USING GIN (resource);
```

Search parameter: `RiskEvidenceSynthesis.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `RiskEvidenceSynthesis.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__title_gin_trgm
ON riskevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("riskevidencesynthesis".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__publisher_gin_trgm
ON riskevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("riskevidencesynthesis".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.name`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS riskevidencesynthesis_resource__name_gin_trgm
ON riskevidencesynthesis
USING GIN ((aidbox_text_search(knife_extract_text("riskevidencesynthesis".resource, $JSON$[["name"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `RiskEvidenceSynthesis.effective`, `RiskEvidenceSynthesis.date`

```sql
WIP: work in progress
```

### SupplyRequest

Search parameter: `SupplyRequest.date`

```sql
WIP: work in progress
```

Search parameter: `SupplyRequest.supplier`, `SupplyRequest.status`, `SupplyRequest.category`, `SupplyRequest.identifier`, `SupplyRequest.requester`, `SupplyRequest.subject`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS supplyrequest_resource__gin
ON supplyrequest
USING GIN (resource);
```

### Task

Search parameter: `Task.owner`, `Task.part-of`, `Task.intent`, `Task.code`, `Task.performer`, `Task.based-on`, `Task.priority`, `Task.patient`, `Task.identifier`, `Task.focus`, `Task.encounter`, `Task.requester`, `Task.subject`, `Task.status`, `Task.business-status`, `Task.group-identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS task_resource__gin
ON task
USING GIN (resource);
```

Search parameter: `Task.modified`, `Task.period`, `Task.authored-on`

```sql
WIP: work in progress
```

### CommunicationRequest

Search parameter: `CommunicationRequest.recipient`, `CommunicationRequest.subject`, `CommunicationRequest.category`, `CommunicationRequest.medium`, `CommunicationRequest.sender`, `CommunicationRequest.priority`, `CommunicationRequest.status`, `CommunicationRequest.based-on`, `CommunicationRequest.replaces`, `CommunicationRequest.requester`, `CommunicationRequest.encounter`, `CommunicationRequest.group-identifier`, `CommunicationRequest.identifier`, `CommunicationRequest.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS communicationrequest_resource__gin
ON communicationrequest
USING GIN (resource);
```

Search parameter: `CommunicationRequest.authored`, `CommunicationRequest.occurrence`

```sql
WIP: work in progress
```

### EnrollmentRequest

Search parameter: `EnrollmentRequest.subject`, `EnrollmentRequest.identifier`, `EnrollmentRequest.status`, `EnrollmentRequest.patient`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS enrollmentrequest_resource__gin
ON enrollmentrequest
USING GIN (resource);
```

### ChargeItemDefinition

Search parameter: `ChargeItemDefinition.publisher`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__publisher_gin_trgm
ON chargeitemdefinition
USING GIN ((aidbox_text_search(knife_extract_text("chargeitemdefinition".resource, $JSON$[["publisher"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ChargeItemDefinition.title`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__title_gin_trgm
ON chargeitemdefinition
USING GIN ((aidbox_text_search(knife_extract_text("chargeitemdefinition".resource, $JSON$[["title"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ChargeItemDefinition.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__description_gin_trgm
ON chargeitemdefinition
USING GIN ((aidbox_text_search(knife_extract_text("chargeitemdefinition".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `ChargeItemDefinition.effective`, `ChargeItemDefinition.date`

```sql
WIP: work in progress
```

Search parameter: `ChargeItemDefinition.context-quantity`

```sql
WIP: work in progress
```

Search parameter: `ChargeItemDefinition.version`, `ChargeItemDefinition.jurisdiction`, `ChargeItemDefinition.status`, `ChargeItemDefinition.context`, `ChargeItemDefinition.url`, `ChargeItemDefinition.context-type`, `ChargeItemDefinition.identifier`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS chargeitemdefinition_resource__gin
ON chargeitemdefinition
USING GIN (resource);
```

### Substance

Search parameter: `Substance.code`

```sql
WIP: work in progress
```

Search parameter: `Substance.expiry`

```sql
WIP: work in progress
```

Search parameter: `Substance.quantity`

```sql
WIP: work in progress
```

Search parameter: `Substance.identifier`, `Substance.container-identifier`, `Substance.substance-reference`, `Substance.category`, `Substance.status`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS substance_resource__gin
ON substance
USING GIN (resource);
```

### Provenance

Search parameter: `Provenance.patient`, `Provenance.target`, `Provenance.agent-role`, `Provenance.agent`, `Provenance.signature-type`, `Provenance.entity`, `Provenance.location`, `Provenance.agent-type`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS provenance_resource__gin
ON provenance
USING GIN (resource);
```

Search parameter: `Provenance.recorded`, `Provenance.when`

```sql
WIP: work in progress
```

### Consent

Search parameter: `Consent.date`, `Consent.period`

```sql
WIP: work in progress
```

Search parameter: `Consent.source-reference`

```sql
WIP: work in progress
```

Search parameter: `Consent.action`, `Consent.status`, `Consent.purpose`, `Consent.identifier`, `Consent.category`, `Consent.scope`, `Consent.consentor`, `Consent.data`, `Consent.security-label`, `Consent.patient`, `Consent.actor`, `Consent.organization`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS consent_resource__gin
ON consent
USING GIN (resource);
```

### CarePlan

Search parameter: `CarePlan.activity-date`, `CarePlan.date`

```sql
WIP: work in progress
```

Search parameter: `CarePlan.care-team`, `CarePlan.replaces`, `CarePlan.based-on`, `CarePlan.activity-reference`, `CarePlan.part-of`, `CarePlan.subject`, `CarePlan.encounter`, `CarePlan.intent`, `CarePlan.patient`, `CarePlan.instantiates-canonical`, `CarePlan.performer`, `CarePlan.instantiates-uri`, `CarePlan.activity-code`, `CarePlan.condition`, `CarePlan.identifier`, `CarePlan.status`, `CarePlan.category`, `CarePlan.goal`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS careplan_resource__gin
ON careplan
USING GIN (resource);
```

### Observation

Search parameter: `Observation.combo-value-quantity`

```sql
WIP: work in progress
```

Search parameter: `Observation.focus`, `Observation.encounter`, `Observation.method`, `Observation.component-code`, `Observation.subject`, `Observation.code`, `Observation.based-on`, `Observation.specimen`, `Observation.value-concept`, `Observation.patient`, `Observation.identifier`, `Observation.data-absent-reason`, `Observation.performer`, `Observation.status`, `Observation.component-data-absent-reason`, `Observation.derived-from`, `Observation.device`, `Observation.part-of`, `Observation.has-member`, `Observation.component-value-concept`, `Observation.category`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__gin
ON observation
USING GIN (resource);
```

Search parameter: `Observation.value-string`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS observation_resource__value_string_gin_trgm
ON observation
USING GIN ((aidbox_text_search(knife_extract_text("observation".resource, $JSON$[["value","string"],["value","CodeableConcept","text"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `Observation.component-value-quantity`

```sql
WIP: work in progress
```

Search parameter: `Observation.combo-value-concept`

```sql
WIP: work in progress
```

Search parameter: `Observation.combo-code`

```sql
WIP: work in progress
```

Search parameter: `Observation.value-quantity`

```sql
WIP: work in progress
```

Search parameter: `Observation.value-date`, `Observation.date`

```sql
WIP: work in progress
```

Search parameter: `Observation.combo-data-absent-reason`

```sql
WIP: work in progress
```

### DocumentManifest

Search parameter: `DocumentManifest.patient`, `DocumentManifest.related-id`, `DocumentManifest.related-ref`, `DocumentManifest.recipient`, `DocumentManifest.item`, `DocumentManifest.subject`, `DocumentManifest.status`, `DocumentManifest.type`, `DocumentManifest.author`, `DocumentManifest.source`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentmanifest_resource__gin
ON documentmanifest
USING GIN (resource);
```

Search parameter: `DocumentManifest.created`

```sql
WIP: work in progress
```

Search parameter: `DocumentManifest.description`

```sql
CREATE INDEX CONCURRENTLY
IF NOT EXISTS documentmanifest_resource__description_gin_trgm
ON documentmanifest
USING GIN ((aidbox_text_search(knife_extract_text("documentmanifest".resource, $JSON$[["description"]]$JSON$))) gin_trgm_ops);
```

Search parameter: `DocumentManifest.identifier`

```sql
WIP: work in progress
```
