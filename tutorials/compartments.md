# Compartments

## Overview

Each resource may belong to one or more logical compartments. A compartment is a logical grouping of resources which share a common property. Compartments have two principal roles:

* Function as an access mechanism for finding a set of related resources quickly
* Provide a definitional basis for applying access control to resources quickly

Read more about compartments in the FHIR documentation [http://build.fhir.org/compartmentdefinition.html](http://build.fhir.org/compartmentdefinition.html).

## Creation of Compartments on a Server

In order to use compartments, you will need to create CompartmentDefinition resources on your server. Visit the [http://hl7.org/fhir/compartmentdefinition-examples.html](http://hl7.org/fhir/compartmentdefinition-examples.html) for CompartmentDefinition examples.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT   /fhir/CompartmentDefinition/Patient

resourceType: CompartmentDefinition
id: Patient
url: http://hl7.org/fhir/CompartmentDefinition/patient
name: Base FHIR compartment definition for Patient

...snipped for brevity...
```
{% endtab %}

{% tab title="Patient" %}
```yaml
PUT   /fhir/CompartmentDefinition/Patient

resourceType: CompartmentDefinition
id: Patient
url: http://hl7.org/fhir/CompartmentDefinition/patient
name: Base FHIR compartment definition for Patient
status: draft
experimental: true
date: '2018-12-04T09:28:36.308Z'
publisher: FHIR Project Team
contact:
- telecom:
  - system: url
    value: http://hl7.org/fhir
description: There is an instance of the patient compartment for each patient resource,
  and the identity of the compartment is the same as the patient. When a patient is
  linked to another patient, all the records associated with the linked patient are
  in the compartment associated with the target of the link.. The set of resources
  associated with a particular patient
code: Patient
search: true
resource:
- code: Account
  param:
  - subject
- code: ActivityDefinition
- code: AdverseEvent
  param:
  - subject
- code: AllergyIntolerance
  param:
  - patient
  - recorder
  - asserter
- code: Appointment
  param:
  - actor
- code: AppointmentResponse
  param:
  - actor
- code: AuditEvent
  param:
  - patient
  - agent.patient
  - entity.patient
- code: Basic
  param:
  - patient
  - author
- code: Binary
- code: BodySite
  param:
  - patient
- code: Bundle
- code: CapabilityStatement
- code: CarePlan
  param:
  - patient
  - performer
- code: CareTeam
  param:
  - patient
  - participant
- code: ChargeItem
  param:
  - subject
- code: Claim
  param:
  - patient
  - payee
- code: ClaimResponse
  param:
  - patient
- code: ClinicalImpression
  param:
  - subject
- code: CodeSystem
- code: Communication
  param:
  - subject
  - sender
  - recipient
- code: CommunicationRequest
  param:
  - subject
  - sender
  - recipient
  - requester
- code: CompartmentDefinition
- code: Composition
  param:
  - subject
  - author
  - attester
- code: ConceptMap
- code: Condition
  param:
  - patient
  - asserter
- code: Consent
  param:
  - patient
- code: Contract
- code: Coverage
  param:
  - policy-holder
  - subscriber
  - beneficiary
  - payor
- code: DataElement
- code: DetectedIssue
  param:
  - patient
- code: Device
- code: DeviceComponent
- code: DeviceMetric
- code: DeviceRequest
  param:
  - subject
  - requester
  - performer
- code: DeviceUseStatement
  param:
  - subject
- code: DiagnosticReport
  param:
  - subject
- code: DocumentManifest
  param:
  - subject
  - author
  - recipient
- code: DocumentReference
  param:
  - subject
  - author
- code: EligibilityRequest
  param:
  - patient
- code: EligibilityResponse
- code: Encounter
  param:
  - patient
- code: Endpoint
- code: EnrollmentRequest
  param:
  - subject
- code: EnrollmentResponse
- code: EpisodeOfCare
  param:
  - patient
- code: ExpansionProfile
- code: ExplanationOfBenefit
  param:
  - patient
  - payee
- code: FamilyMemberHistory
  param:
  - patient
- code: Flag
  param:
  - patient
- code: Goal
  param:
  - patient
- code: GraphDefinition
- code: Group
  param:
  - member
- code: GuidanceResponse
- code: HealthcareService
- code: ImagingManifest
  param:
  - patient
  - author
- code: ImagingStudy
  param:
  - patient
- code: Immunization
  param:
  - patient
- code: ImmunizationRecommendation
  param:
  - patient
- code: ImplementationGuide
- code: Library
- code: Linkage
- code: List
  param:
  - subject
  - source
- code: Location
- code: Measure
- code: MeasureReport
  param:
  - patient
- code: Media
  param:
  - subject
- code: Medication
- code: MedicationAdministration
  param:
  - patient
  - performer
  - subject
- code: MedicationDispense
  param:
  - subject
  - patient
  - receiver
- code: MedicationRequest
  param:
  - subject
- code: MedicationStatement
  param:
  - subject
- code: MessageDefinition
- code: MessageHeader
- code: NamingSystem
- code: NutritionOrder
  param:
  - patient
- code: Observation
  param:
  - subject
  - performer
- code: OperationDefinition
- code: OperationOutcome
- code: Organization
- code: Patient
  param:
  - link
- code: PaymentNotice
- code: PaymentReconciliation
- code: Person
  param:
  - patient
- code: PlanDefinition
- code: Practitioner
- code: PractitionerRole
- code: Procedure
  param:
  - patient
  - performer
- code: ProcedureRequest
  param:
  - subject
  - performer
- code: ProcessRequest
- code: ProcessResponse
- code: Provenance
  param:
  - target.subject
  - target.patient
  - patient
- code: Questionnaire
- code: QuestionnaireResponse
  param:
  - subject
  - author
- code: ReferralRequest
  param:
  - patient
  - requester
- code: RelatedPerson
  param:
  - patient
- code: RequestGroup
  param:
  - subject
  - participant
- code: ResearchStudy
- code: ResearchSubject
  param:
  - individual
- code: RiskAssessment
  param:
  - subject
- code: Schedule
  param:
  - actor
- code: SearchParameter
- code: Sequence
- code: ServiceDefinition
- code: Slot
- code: Specimen
  param:
  - subject
- code: StructureDefinition
- code: StructureMap
- code: Subscription
- code: Substance
- code: SupplyDelivery
  param:
  - patient
- code: SupplyRequest
  param:
  - requester
- code: Task
- code: TestReport
- code: TestScript
- code: ValueSet
- code: VisionPrescription
  param:
  - patient

```
{% endtab %}

{% tab title="Practitioner" %}
```yaml
PUT   /fhir/CompartmentDefinition/Practitioner

resourceType: CompartmentDefinition
id: Practitioner
url: http://hl7.org/fhir/CompartmentDefinition/practitioner
name: Base FHIR compartment definition for Practitioner
status: draft
experimental: true
date: '2017-04-19T07:44:43+10:00'
publisher: FHIR Project Team
contact:
- telecom:
  - system: url
    value: http://hl7.org/fhir
description: There is an instance of the practitioner compartment for each Practitioner
  resource, and the identity of the compartment is the same as the Practitioner. The
  set of resources associated with a particular practitioner
code: Practitioner
search: true
resource:
- code: Account
  param:
  - subject
- code: ActivityDefinition
- code: AdverseEvent
  param:
  - recorder
- code: AllergyIntolerance
  param:
  - recorder
  - asserter
- code: Appointment
  param:
  - actor
- code: AppointmentResponse
  param:
  - actor
- code: AuditEvent
  param:
  - agent
- code: Basic
  param:
  - author
- code: Binary
- code: BodySite
- code: Bundle
- code: CapabilityStatement
- code: CarePlan
  param:
  - performer
- code: CareTeam
  param:
  - participant
- code: ChargeItem
  param:
  - enterer
  - participant-actor
- code: Claim
  param:
  - enterer
  - provider
  - payee
  - care-team
- code: ClaimResponse
  param:
  - request-provider
- code: ClinicalImpression
  param:
  - assessor
- code: CodeSystem
- code: Communication
  param:
  - sender
  - recipient
- code: CommunicationRequest
  param:
  - sender
  - recipient
  - requester
- code: CompartmentDefinition
- code: Composition
  param:
  - subject
  - author
  - attester
- code: ConceptMap
- code: Condition
  param:
  - asserter
- code: Consent
- code: Contract
- code: Coverage
- code: DataElement
- code: DetectedIssue
  param:
  - author
- code: Device
- code: DeviceComponent
- code: DeviceMetric
- code: DeviceRequest
  param:
  - requester
  - performer
- code: DeviceUseStatement
- code: DiagnosticReport
  param:
  - performer
- code: DocumentManifest
  param:
  - subject
  - author
  - recipient
- code: DocumentReference
  param:
  - subject
  - author
  - authenticator
- code: EligibilityRequest
  param:
  - enterer
  - provider
- code: EligibilityResponse
  param:
  - request-provider
- code: Encounter
  param:
  - practitioner
  - participant
- code: Endpoint
- code: EnrollmentRequest
- code: EnrollmentResponse
- code: EpisodeOfCare
  param:
  - care-manager
- code: ExpansionProfile
- code: ExplanationOfBenefit
  param:
  - enterer
  - provider
  - payee
  - care-team
- code: FamilyMemberHistory
- code: Flag
  param:
  - author
- code: Goal
- code: GraphDefinition
- code: Group
  param:
  - member
- code: GuidanceResponse
- code: HealthcareService
- code: ImagingManifest
  param:
  - author
- code: ImagingStudy
- code: Immunization
  param:
  - practitioner
- code: ImmunizationRecommendation
- code: ImplementationGuide
- code: Library
- code: Linkage
  param:
  - author
- code: List
  param:
  - source
- code: Location
- code: Measure
- code: MeasureReport
- code: Media
  param:
  - subject
  - operator
- code: Medication
- code: MedicationAdministration
  param:
  - performer
- code: MedicationDispense
  param:
  - performer
  - receiver
- code: MedicationRequest
  param:
  - requester
- code: MedicationStatement
  param:
  - source
- code: MessageDefinition
- code: MessageHeader
  param:
  - receiver
  - author
  - responsible
  - enterer
- code: NamingSystem
- code: NutritionOrder
  param:
  - provider
- code: Observation
  param:
  - performer
- code: OperationDefinition
- code: OperationOutcome
- code: Organization
- code: Patient
  param:
  - general-practitioner
- code: PaymentNotice
  param:
  - provider
- code: PaymentReconciliation
  param:
  - request-provider
- code: Person
  param:
  - practitioner
- code: PlanDefinition
- code: Practitioner
  param:
  - "{def}"
- code: PractitionerRole
  param:
  - practitioner
- code: Procedure
  param:
  - performer
- code: ProcedureRequest
  param:
  - performer
  - requester
- code: ProcessRequest
  param:
  - provider
- code: ProcessResponse
  param:
  - request-provider
- code: Provenance
  param:
  - agent
- code: Questionnaire
- code: QuestionnaireResponse
  param:
  - author
  - source
- code: ReferralRequest
  param:
  - requester
  - recipient
- code: RelatedPerson
- code: RequestGroup
  param:
  - participant
  - author
- code: ResearchStudy
  param:
  - principalinvestigator
- code: ResearchSubject
- code: RiskAssessment
  param:
  - performer
- code: Schedule
  param:
  - actor
- code: SearchParameter
- code: Sequence
- code: ServiceDefinition
- code: Slot
- code: Specimen
  param:
  - collector
- code: StructureDefinition
- code: StructureMap
- code: Subscription
- code: Substance
- code: SupplyDelivery
  param:
  - supplier
  - receiver
- code: SupplyRequest
  param:
  - requester
- code: Task
- code: TestReport
- code: TestScript
- code: ValueSet
- code: VisionPrescription
  param:
  - prescriber
```
{% endtab %}

{% tab title="RelatedPerson" %}
```yaml
PUT   /fhir/CompartmentDefinition/RelatedPerson

resourceType: CompartmentDefinition
id: RelatedPerson
url: http://hl7.org/fhir/CompartmentDefinition/relatedPerson
name: Base FHIR compartment definition for RelatedPerson
status: draft
experimental: true
date: '2017-04-19T07:44:43+10:00'
publisher: FHIR Project Team
contact:
- telecom:
  - system: url
    value: http://hl7.org/fhir
description: There is an instance of the relatedPerson compartment for each relatedPerson
  resource, and the identity of the compartment is the same as the relatedPerson.
  The set of resources associated with a particular 'related person'
code: RelatedPerson
search: true
resource:
- code: Account
- code: ActivityDefinition
- code: AdverseEvent
  param:
  - recorder
- code: AllergyIntolerance
  param:
  - asserter
- code: Appointment
  param:
  - actor
- code: AppointmentResponse
  param:
  - actor
- code: AuditEvent
- code: Basic
  param:
  - author
- code: Binary
- code: BodySite
- code: Bundle
- code: CapabilityStatement
- code: CarePlan
  param:
  - performer
- code: CareTeam
  param:
  - participant
- code: ChargeItem
  param:
  - enterer
  - participant-actor
- code: Claim
  param:
  - payee
- code: ClaimResponse
- code: ClinicalImpression
- code: CodeSystem
- code: Communication
  param:
  - sender
  - recipient
- code: CommunicationRequest
  param:
  - sender
  - recipient
  - requester
- code: CompartmentDefinition
- code: Composition
  param:
  - author
- code: ConceptMap
- code: Condition
  param:
  - asserter
- code: Consent
- code: Contract
- code: Coverage
  param:
  - policy-holder
  - subscriber
  - payor
- code: DataElement
- code: DetectedIssue
- code: Device
- code: DeviceComponent
- code: DeviceMetric
- code: DeviceRequest
- code: DeviceUseStatement
- code: DiagnosticReport
- code: DocumentManifest
  param:
  - author
- code: DocumentReference
  param:
  - author
- code: EligibilityRequest
- code: EligibilityResponse
- code: Encounter
  param:
  - participant
- code: Endpoint
- code: EnrollmentRequest
- code: EnrollmentResponse
- code: EpisodeOfCare
- code: ExpansionProfile
- code: ExplanationOfBenefit
  param:
  - payee
- code: FamilyMemberHistory
- code: Flag
- code: Goal
- code: GraphDefinition
- code: Group
- code: GuidanceResponse
- code: HealthcareService
- code: ImagingManifest
  param:
  - author
- code: ImagingStudy
- code: Immunization
- code: ImmunizationRecommendation
- code: ImplementationGuide
- code: Library
- code: Linkage
- code: List
- code: Location
- code: Measure
- code: MeasureReport
- code: Media
- code: Medication
- code: MedicationAdministration
  param:
  - performer
- code: MedicationDispense
- code: MedicationRequest
- code: MedicationStatement
  param:
  - source
- code: MessageDefinition
- code: MessageHeader
- code: NamingSystem
- code: NutritionOrder
- code: Observation
  param:
  - performer
- code: OperationDefinition
- code: OperationOutcome
- code: Organization
- code: Patient
  param:
  - link
- code: PaymentNotice
- code: PaymentReconciliation
- code: Person
  param:
  - link
- code: PlanDefinition
- code: Practitioner
- code: PractitionerRole
- code: Procedure
  param:
  - performer
- code: ProcedureRequest
  param:
  - performer
- code: ProcessRequest
- code: ProcessResponse
- code: Provenance
  param:
  - agent
- code: Questionnaire
- code: QuestionnaireResponse
  param:
  - author
  - source
- code: ReferralRequest
- code: RelatedPerson
  param:
  - "{def}"
- code: RequestGroup
  param:
  - participant
- code: ResearchStudy
- code: ResearchSubject
- code: RiskAssessment
- code: Schedule
  param:
  - actor
- code: SearchParameter
- code: Sequence
- code: ServiceDefinition
- code: Slot
- code: Specimen
- code: StructureDefinition
- code: StructureMap
- code: Subscription
- code: Substance
- code: SupplyDelivery
- code: SupplyRequest
  param:
  - requester
- code: Task
- code: TestReport
- code: TestScript
- code: ValueSet
- code: VisionPrescription

```
{% endtab %}

{% tab title="Device" %}
```yaml
PUT   /fhir/CompartmentDefinition/Device

resourceType: CompartmentDefinition
id: Device
url: http://hl7.org/fhir/CompartmentDefinition/device
name: Base FHIR compartment definition for Device
status: draft
experimental: true
date: '2017-04-19T07:44:43+10:00'
publisher: FHIR Project Team
contact:
- telecom:
  - system: url
    value: http://hl7.org/fhir
description: There is an instance of the practitioner compartment for each Device
  resource, and the identity of the compartment is the same as the Device. The set
  of resources associated with a particular device
code: Device
search: true
resource:
- code: Account
  param:
  - subject
- code: ActivityDefinition
- code: AdverseEvent
- code: AllergyIntolerance
- code: Appointment
  param:
  - actor
- code: AppointmentResponse
  param:
  - actor
- code: AuditEvent
  param:
  - agent
- code: Basic
- code: Binary
- code: BodySite
- code: Bundle
- code: CapabilityStatement
- code: CarePlan
- code: CareTeam
- code: ChargeItem
  param:
  - enterer
  - participant-actor
- code: Claim
- code: ClaimResponse
- code: ClinicalImpression
- code: CodeSystem
- code: Communication
  param:
  - sender
  - recipient
- code: CommunicationRequest
  param:
  - sender
  - recipient
- code: CompartmentDefinition
- code: Composition
  param:
  - author
- code: ConceptMap
- code: Condition
- code: Consent
- code: Contract
- code: Coverage
- code: DataElement
- code: DetectedIssue
  param:
  - author
- code: Device
  param:
  - "{def}"
- code: DeviceComponent
  param:
  - source
- code: DeviceMetric
  param:
  - source
- code: DeviceRequest
  param:
  - device
  - subject
  - requester
  - performer
- code: DeviceUseStatement
  param:
  - device
- code: DiagnosticReport
  param:
  - subject
- code: DocumentManifest
  param:
  - subject
  - author
- code: DocumentReference
  param:
  - subject
  - author
- code: EligibilityRequest
- code: EligibilityResponse
- code: Encounter
- code: Endpoint
- code: EnrollmentRequest
- code: EnrollmentResponse
- code: EpisodeOfCare
- code: ExpansionProfile
- code: ExplanationOfBenefit
- code: FamilyMemberHistory
- code: Flag
  param:
  - author
- code: Goal
- code: GraphDefinition
- code: Group
  param:
  - member
- code: GuidanceResponse
- code: HealthcareService
- code: ImagingManifest
  param:
  - author
- code: ImagingStudy
- code: Immunization
- code: ImmunizationRecommendation
- code: ImplementationGuide
- code: Library
- code: Linkage
- code: List
  param:
  - subject
  - source
- code: Location
- code: Measure
- code: MeasureReport
- code: Media
  param:
  - subject
- code: Medication
- code: MedicationAdministration
  param:
  - device
- code: MedicationDispense
- code: MedicationRequest
- code: MedicationStatement
- code: MessageDefinition
- code: MessageHeader
  param:
  - target
- code: NamingSystem
- code: NutritionOrder
- code: Observation
  param:
  - subject
  - device
- code: OperationDefinition
- code: OperationOutcome
- code: Organization
- code: Patient
- code: PaymentNotice
- code: PaymentReconciliation
- code: Person
- code: PlanDefinition
- code: Practitioner
- code: PractitionerRole
- code: Procedure
- code: ProcedureRequest
  param:
  - performer
  - requester
- code: ProcessRequest
- code: ProcessResponse
- code: Provenance
  param:
  - agent
- code: Questionnaire
- code: QuestionnaireResponse
  param:
  - author
- code: ReferralRequest
- code: RelatedPerson
- code: RequestGroup
  param:
  - author
- code: ResearchStudy
- code: ResearchSubject
- code: RiskAssessment
  param:
  - performer
- code: Schedule
  param:
  - actor
- code: SearchParameter
- code: Sequence
- code: ServiceDefinition
- code: Slot
- code: Specimen
  param:
  - subject
- code: StructureDefinition
- code: StructureMap
- code: Subscription
- code: Substance
- code: SupplyDelivery
- code: SupplyRequest
  param:
  - requester
- code: Task
- code: TestReport
- code: TestScript
- code: ValueSet
- code: VisionPrescription

```
{% endtab %}

{% tab title="Encounter" %}
```yaml
PUT /fhir/CompartmentDefinition/Encounter

resourceType: CompartmentDefinition
id: Encounter
url: http://hl7.org/fhir/CompartmentDefinition/encounter
name: Base FHIR compartment definition for Encounter
status: draft
experimental: true
date: '2017-04-19T07:44:43+10:00'
publisher: FHIR Project Team
contact:
- telecom:
  - system: url
    value: http://hl7.org/fhir
description: There is an instance of the encounter compartment for each encounter
  resource, and the identity of the compartment is the same as the encounter. The
  set of resources associated with a particular encounter
code: Encounter
search: true
resource:
- code: Account
- code: ActivityDefinition
- code: AdverseEvent
- code: AllergyIntolerance
- code: Appointment
- code: AppointmentResponse
- code: AuditEvent
- code: Basic
- code: Binary
- code: BodySite
- code: Bundle
- code: CapabilityStatement
- code: CarePlan
- code: CareTeam
- code: ChargeItem
  param:
  - context
- code: Claim
  param:
  - encounter
- code: ClaimResponse
- code: ClinicalImpression
  param:
  - context
- code: CodeSystem
- code: Communication
  param:
  - context
- code: CommunicationRequest
  param:
  - context
- code: CompartmentDefinition
- code: Composition
  param:
  - encounter
- code: ConceptMap
- code: Condition
  param:
  - context
- code: Consent
- code: Contract
- code: Coverage
- code: DataElement
- code: DetectedIssue
- code: Device
- code: DeviceComponent
- code: DeviceMetric
- code: DeviceRequest
  param:
  - encounter
- code: DeviceUseStatement
- code: DiagnosticReport
  param:
  - encounter
- code: DocumentManifest
- code: DocumentReference
  param:
  - encounter
- code: EligibilityRequest
- code: EligibilityResponse
- code: Encounter
  param:
  - "{def}"
- code: Endpoint
- code: EnrollmentRequest
- code: EnrollmentResponse
- code: EpisodeOfCare
- code: ExpansionProfile
- code: ExplanationOfBenefit
  param:
  - encounter
- code: FamilyMemberHistory
- code: Flag
- code: Goal
- code: GraphDefinition
- code: Group
- code: GuidanceResponse
- code: HealthcareService
- code: ImagingManifest
- code: ImagingStudy
- code: Immunization
- code: ImmunizationRecommendation
- code: ImplementationGuide
- code: Library
- code: Linkage
- code: List
- code: Location
- code: Measure
- code: MeasureReport
- code: Media
- code: Medication
- code: MedicationAdministration
  param:
  - context
- code: MedicationDispense
- code: MedicationRequest
  param:
  - context
- code: MedicationStatement
- code: MessageDefinition
- code: MessageHeader
- code: NamingSystem
- code: NutritionOrder
  param:
  - encounter
- code: Observation
  param:
  - encounter
- code: OperationDefinition
- code: OperationOutcome
- code: Organization
- code: Patient
- code: PaymentNotice
- code: PaymentReconciliation
- code: Person
- code: PlanDefinition
- code: Practitioner
- code: PractitionerRole
- code: Procedure
  param:
  - encounter
- code: ProcedureRequest
  param:
  - context
- code: ProcessRequest
- code: ProcessResponse
- code: Provenance
- code: Questionnaire
- code: QuestionnaireResponse
  param:
  - context
- code: ReferralRequest
- code: RelatedPerson
- code: RequestGroup
  param:
  - encounter
- code: ResearchStudy
- code: ResearchSubject
- code: RiskAssessment
- code: Schedule
- code: SearchParameter
- code: Sequence
- code: ServiceDefinition
- code: Slot
- code: Specimen
- code: StructureDefinition
- code: StructureMap
- code: Subscription
- code: Substance
- code: SupplyDelivery
- code: SupplyRequest
- code: Task
- code: TestReport
- code: TestScript
- code: ValueSet
- code: VisionPrescription
  param:
  - encounter

```
{% endtab %}
{% endtabs %}

Now you can search compartments on the server.

## Compartment Search

To search a [compartment](http://hl7.org/fhir/compartmentdefinition.html), for either all possible resources or for a particular resource type, respectively:

```text
  GET [base]/[Compartment]/[id]/{*?[parameters]{&_format=[mime-type]}}
  GET [base]/[Compartment]/[id]/[type]{?[parameters]{&_format=[mime-type]}}
```

For example, to retrieve all the observation resources for a particular LOINC code associated with a particular encounter:

```text
  GET [base]/Encounter/23423445/Observation?code=2951-2  {&_format=[mime-type]}
```

## Example Requests

As an example of compartment usage, to retrieve a list of a patient's conditions, use the URL:

```text
  GET [base]/Patient/[id]/Condition
```

Additional search parameters can be defined, such as this hypothetical search for acute conditions:

```text
  GET [base]/Patient/[id]/Condition?code:in=http://hspc.org/ValueSet/acute-concerns
```

Note that as searches, these are syntactic variations on these two search URLs respectively:

```text
  GET [base]/Condition?patient=[id]
  GET [base]/Condition?patient=[id]&code:in=http://hspc.org/ValueSet/acute-concerns
```

The outcome of a compartment search is the same as the equivalent normal search. For example, both these searches return the same outcome if there is no patient 333:

```text
  GET [base]/Patient/333/Condition
  GET [base]/Condition?patient=333
```

Whether the patient doesn't exist, or the user has no access to the patient, both these searches return an empty bundle with no matches. Some systems will include an operation outcome warning that there is no matching patient.

However, there is a key difference in functionality between compartment based searches and direct searches with parameters. Consider this search:

```text
  GET [base]/Patient/[id]/Communication
```

Because the definition of the [patient compartment](http://build.fhir.org/compartmentdefinition-patient.html) for [Communication ](http://build.fhir.org/communication.html)says that a Communication resource is in the patient compartment if the subject, sender, or recipient is the patient, the compartment search is actually the same as the union of these 3 searches:

```text
  GET [base]/Communication?subject=[id]
  GET [base]/Communication?sender=[id]
  GET [base]/Communication?recipient=[id]
```

There is no way to do this as a single search, except by using the [\_filter](http://build.fhir.org/search_filter.html):

```text
  GET [base]/Communication?_filter=subject re [id] or sender re [id] or recipient re [id]
```

## List of Available Compartments

| **Title** | **Description** | **Identity** | **Membership** |
| :--- | :--- | :--- | :--- |
| [Patient](http://build.fhir.org/compartmentdefinition-patient.html) | The set of resources associated with a particular patient | There is an instance of the patient compartment for each patient resource, and the identity of the compartment is the same as the patient. When a patient is linked to another patient, all the records associated with the linked patient are in the compartment associated with the target of the link. | The patient compartment includes any resources where the subject of the resource is the patient, and some other resources that are directly linked to resources in the patient compartment |
| [Encounter](http://build.fhir.org/compartmentdefinition-encounter.html) | The set of resources associated with a particular encounter | There is an instance of the encounter compartment for each encounter resource, and the identity of the compartment is the same as the encounter | The encounter compartment includes any resources where the resource has an explicitly nominated encounter, and some other resources that themselves link to resources in the encounter compartment. Note that for many resources, the exact nature of the link to encounter can be ambiguous \(e.g. for a DiagnosticReport, is it the encounter when it was initiated, or when it was reported?\) |
| [RelatedPerson](http://build.fhir.org/compartmentdefinition-relatedperson.html) | The set of resources associated with a particular 'related person' | There is an instance of the relatedPerson compartment for each relatedPerson resource, and the identity of the compartment is the same as the relatedPerson | The relatedPerson compartment includes any resources where the resource is explicitly linked to relatedPerson \(usually as author\) |
| [Practitioner](http://build.fhir.org/compartmentdefinition-practitioner.html) | The set of resources associated with a particular practitioner | There is an instance of the practitioner compartment for each Practitioner resource, and the identity of the compartment is the same as the Practitioner | The practitioner compartment includes any resources where the resource is explicitly linked to a Practitioner \(usually as author, but other kinds of linkage exist\) |
| [Device](http://build.fhir.org/compartmentdefinition-device.html) | The set of resources associated with a particular device | There is an instance of the device compartment for each Device resource, and the identity of the compartment is the same as the Device | The device compartment includes any resources where the resource is explicitly linked to a Device \(mostly subject or performer\) |

{% hint style="warning" %}
At present, compartment definitions can only be defined by HL7 International. This is because their existence creates significant impact on the behavior of servers.
{% endhint %}

