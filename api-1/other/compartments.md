# Compartments API

## Overview

Each resource may belong to one or more compartments. A compartment is a logical grouping of resources which share a common property. Compartments have two principal roles:

* Function as an access mechanism for finding a set of related resources quickly \(described here\)
* Provide a definitional basis for applying access control to resources quickly

Read more about compartments in the FHIR [documentation](https://www.hl7.org/fhir/compartmentdefinition.html). All examples in this tutorial are executable in Aidbox REST console.

Aidbox supports 5 default FHIR compartments in FHIR versions 1.4.0, 1.8.0, 3.0.1, 3.2.0, 3.3.0, 4.0.0, 4.0.1

## Defining Compartments \(FHIR R4\)

In order to use compartments, you will need to create CompartmentDefinition resources on your server. Visit FHIR [documentation](https://www.hl7.org/fhir/compartmentdefinition-examples.html) for official CompartmentDefinition examples or use the following REST console snippets to create the resources.

{% hint style="info" %}
Which `CompartmentDefinition`is used is determined by its code or id:

1. The compartment with`id` = `target resourceType`is used
2. Else if not found compartment with`code` = `target resourceType`is used

If multiple compartments with the `code = traget resourceType` are found then it is not determined which one will be used since FHIR spec doesn't specify this case
{% endhint %}

{% tabs %}
{% tab title="General Structure" %}
```yaml
PUT /fhir/CompartmentDefinition/Patient

resourceType: CompartmentDefinition
id: Patient
url: http://hl7.org/fhir/CompartmentDefinition/patient
experimental: true
name: Base FHIR compartment definition for Patient
status: draft
publisher: FHIR Project Team
version: 4.0.1
date: '2019-11-01T09:29:23+11:00'
search: true
code: Patient
contact:
- telecom:
  - {system: url, value: 'http://hl7.org/fhir'}
description: ...
# array that enumerates resources included in the compartment
resource: ... 
```
{% endtab %}

{% tab title="Patient" %}
```yaml
PUT /fhir/CompartmentDefinition/Patient

resourceType: CompartmentDefinition
id: Patient
url: http://hl7.org/fhir/CompartmentDefinition/patient
experimental: true
name: Base FHIR compartment definition for Patient
status: draft
publisher: FHIR Project Team
version: 4.0.1
date: '2019-11-01T09:29:23+11:00'
search: true
code: Patient
contact:
- telecom:
  - {system: url, value: 'http://hl7.org/fhir'}
description: There is an instance of the patient compartment for each patient resource, and the identity of the compartment is the same as the patient. When a patient is linked to another patient, all the records associated with the linked patient are in the compartment associated with the target of the link.. The set of resources associated with a particular patient
resource:
- code: Account
  param: [subject]
- {code: ActivityDefinition}
- code: AdverseEvent
  param: [subject]
- code: AllergyIntolerance
  param: [patient, recorder, asserter]
- code: Appointment
  param: [actor]
- code: AppointmentResponse
  param: [actor]
- code: AuditEvent
  param: [patient]
- code: Basic
  param: [patient, author]
- {code: Binary}
- {code: BiologicallyDerivedProduct}
- code: BodyStructure
  param: [patient]
- {code: Bundle}
- {code: CapabilityStatement}
- code: CarePlan
  param: [patient, performer]
- code: CareTeam
  param: [patient, participant]
- {code: CatalogEntry}
- code: ChargeItem
  param: [subject]
- {code: ChargeItemDefinition}
- code: Claim
  param: [patient, payee]
- code: ClaimResponse
  param: [patient]
- code: ClinicalImpression
  param: [subject]
- {code: CodeSystem}
- code: Communication
  param: [subject, sender, recipient]
- code: CommunicationRequest
  param: [subject, sender, recipient, requester]
- {code: CompartmentDefinition}
- code: Composition
  param: [subject, author, attester]
- {code: ConceptMap}
- code: Condition
  param: [patient, asserter]
- code: Consent
  param: [patient]
- {code: Contract}
- code: Coverage
  param: [policy-holder, subscriber, beneficiary, payor]
- code: CoverageEligibilityRequest
  param: [patient]
- code: CoverageEligibilityResponse
  param: [patient]
- code: DetectedIssue
  param: [patient]
- {code: Device}
- {code: DeviceDefinition}
- {code: DeviceMetric}
- code: DeviceRequest
  param: [subject, performer]
- code: DeviceUseStatement
  param: [subject]
- code: DiagnosticReport
  param: [subject]
- code: DocumentManifest
  param: [subject, author, recipient]
- code: DocumentReference
  param: [subject, author]
- {code: EffectEvidenceSynthesis}
- code: Encounter
  param: [patient]
- {code: Endpoint}
- code: EnrollmentRequest
  param: [subject]
- {code: EnrollmentResponse}
- code: EpisodeOfCare
  param: [patient]
- {code: EventDefinition}
- {code: Evidence}
- {code: EvidenceVariable}
- {code: ExampleScenario}
- code: ExplanationOfBenefit
  param: [patient, payee]
- code: FamilyMemberHistory
  param: [patient]
- code: Flag
  param: [patient]
- code: Goal
  param: [patient]
- {code: GraphDefinition}
- code: Group
  param: [member]
- {code: GuidanceResponse}
- {code: HealthcareService}
- code: ImagingStudy
  param: [patient]
- code: Immunization
  param: [patient]
- code: ImmunizationEvaluation
  param: [patient]
- code: ImmunizationRecommendation
  param: [patient]
- {code: ImplementationGuide}
- {code: InsurancePlan}
- code: Invoice
  param: [subject, patient, recipient]
- {code: Library}
- {code: Linkage}
- code: List
  param: [subject, source]
- {code: Location}
- {code: Measure}
- code: MeasureReport
  param: [patient]
- code: Media
  param: [subject]
- {code: Medication}
- code: MedicationAdministration
  param: [patient, performer, subject]
- code: MedicationDispense
  param: [subject, patient, receiver]
- {code: MedicationKnowledge}
- code: MedicationRequest
  param: [subject]
- code: MedicationStatement
  param: [subject]
- {code: MedicinalProduct}
- {code: MedicinalProductAuthorization}
- {code: MedicinalProductContraindication}
- {code: MedicinalProductIndication}
- {code: MedicinalProductIngredient}
- {code: MedicinalProductInteraction}
- {code: MedicinalProductManufactured}
- {code: MedicinalProductPackaged}
- {code: MedicinalProductPharmaceutical}
- {code: MedicinalProductUndesirableEffect}
- {code: MessageDefinition}
- {code: MessageHeader}
- code: MolecularSequence
  param: [patient]
- {code: NamingSystem}
- code: NutritionOrder
  param: [patient]
- code: Observation
  param: [subject, performer]
- {code: ObservationDefinition}
- {code: OperationDefinition}
- {code: OperationOutcome}
- {code: Organization}
- {code: OrganizationAffiliation}
- code: Patient
  param: [link]
- {code: PaymentNotice}
- {code: PaymentReconciliation}
- code: Person
  param: [patient]
- {code: PlanDefinition}
- {code: Practitioner}
- {code: PractitionerRole}
- code: Procedure
  param: [patient, performer]
- code: Provenance
  param: [patient]
- {code: Questionnaire}
- code: QuestionnaireResponse
  param: [subject, author]
- code: RelatedPerson
  param: [patient]
- code: RequestGroup
  param: [subject, participant]
- {code: ResearchDefinition}
- {code: ResearchElementDefinition}
- {code: ResearchStudy}
- code: ResearchSubject
  param: [individual]
- code: RiskAssessment
  param: [subject]
- {code: RiskEvidenceSynthesis}
- code: Schedule
  param: [actor]
- {code: SearchParameter}
- code: ServiceRequest
  param: [subject, performer]
- {code: Slot}
- code: Specimen
  param: [subject]
- {code: SpecimenDefinition}
- {code: StructureDefinition}
- {code: StructureMap}
- {code: Subscription}
- {code: Substance}
- {code: SubstanceNucleicAcid}
- {code: SubstancePolymer}
- {code: SubstanceProtein}
- {code: SubstanceReferenceInformation}
- {code: SubstanceSourceMaterial}
- {code: SubstanceSpecification}
- code: SupplyDelivery
  param: [patient]
- code: SupplyRequest
  param: [subject]
- {code: Task}
- {code: TerminologyCapabilities}
- {code: TestReport}
- {code: TestScript}
- {code: ValueSet}
- {code: VerificationResult}
- code: VisionPrescription
  param: [patient]

```
{% endtab %}

{% tab title="Encounter" %}
```yaml
PUT /fhir/CompartmentDefinition/Encounter

resourceType: CompartmentDefinition
id: Encounter
url: http://hl7.org/fhir/CompartmentDefinition/encounter
experimental: true
name: Base FHIR compartment definition for Encounter
status: draft
publisher: FHIR Project Team
version: 4.0.1
date: '2019-11-01T09:29:23+11:00'
search: true
code: Encounter
contact:
- telecom:
  - {system: url, value: 'http://hl7.org/fhir'}
description: There is an instance of the encounter compartment for each encounter resource, and the identity of the compartment is the same as the encounter. The set of resources associated with a particular encounter
resource:
- {code: Account}
- {code: ActivityDefinition}
- {code: AdverseEvent}
- {code: AllergyIntolerance}
- {code: Appointment}
- {code: AppointmentResponse}
- {code: AuditEvent}
- {code: Basic}
- {code: Binary}
- {code: BiologicallyDerivedProduct}
- {code: BodyStructure}
- {code: Bundle}
- {code: CapabilityStatement}
- code: CarePlan
  param: [encounter]
- code: CareTeam
  param: [encounter]
- {code: CatalogEntry}
- code: ChargeItem
  param: [context]
- {code: ChargeItemDefinition}
- code: Claim
  param: [encounter]
- {code: ClaimResponse}
- code: ClinicalImpression
  param: [encounter]
- {code: CodeSystem}
- code: Communication
  param: [encounter]
- code: CommunicationRequest
  param: [encounter]
- {code: CompartmentDefinition}
- code: Composition
  param: [encounter]
- {code: ConceptMap}
- code: Condition
  param: [encounter]
- {code: Consent}
- {code: Contract}
- {code: Coverage}
- {code: CoverageEligibilityRequest}
- {code: CoverageEligibilityResponse}
- {code: DetectedIssue}
- {code: Device}
- {code: DeviceDefinition}
- {code: DeviceMetric}
- code: DeviceRequest
  param: [encounter]
- {code: DeviceUseStatement}
- code: DiagnosticReport
  param: [encounter]
- code: DocumentManifest
  param: [related-ref]
- code: DocumentReference
  param: [encounter]
- {code: EffectEvidenceSynthesis}
- code: Encounter
  param: ['{def}']
- {code: Endpoint}
- {code: EnrollmentRequest}
- {code: EnrollmentResponse}
- {code: EpisodeOfCare}
- {code: EventDefinition}
- {code: Evidence}
- {code: EvidenceVariable}
- {code: ExampleScenario}
- code: ExplanationOfBenefit
  param: [encounter]
- {code: FamilyMemberHistory}
- {code: Flag}
- {code: Goal}
- {code: GraphDefinition}
- {code: Group}
- {code: GuidanceResponse}
- {code: HealthcareService}
- {code: ImagingStudy}
- {code: Immunization}
- {code: ImmunizationEvaluation}
- {code: ImmunizationRecommendation}
- {code: ImplementationGuide}
- {code: InsurancePlan}
- {code: Invoice}
- {code: Library}
- {code: Linkage}
- {code: List}
- {code: Location}
- {code: Measure}
- {code: MeasureReport}
- code: Media
  param: [encounter]
- {code: Medication}
- code: MedicationAdministration
  param: [context]
- {code: MedicationDispense}
- {code: MedicationKnowledge}
- code: MedicationRequest
  param: [encounter]
- {code: MedicationStatement}
- {code: MedicinalProduct}
- {code: MedicinalProductAuthorization}
- {code: MedicinalProductContraindication}
- {code: MedicinalProductIndication}
- {code: MedicinalProductIngredient}
- {code: MedicinalProductInteraction}
- {code: MedicinalProductManufactured}
- {code: MedicinalProductPackaged}
- {code: MedicinalProductPharmaceutical}
- {code: MedicinalProductUndesirableEffect}
- {code: MessageDefinition}
- {code: MessageHeader}
- {code: MolecularSequence}
- {code: NamingSystem}
- code: NutritionOrder
  param: [encounter]
- code: Observation
  param: [encounter]
- {code: ObservationDefinition}
- {code: OperationDefinition}
- {code: OperationOutcome}
- {code: Organization}
- {code: OrganizationAffiliation}
- {code: Patient}
- {code: PaymentNotice}
- {code: PaymentReconciliation}
- {code: Person}
- {code: PlanDefinition}
- {code: Practitioner}
- {code: PractitionerRole}
- code: Procedure
  param: [encounter]
- {code: Provenance}
- {code: Questionnaire}
- code: QuestionnaireResponse
  param: [encounter]
- {code: RelatedPerson}
- code: RequestGroup
  param: [encounter]
- {code: ResearchDefinition}
- {code: ResearchElementDefinition}
- {code: ResearchStudy}
- {code: ResearchSubject}
- {code: RiskAssessment}
- {code: RiskEvidenceSynthesis}
- {code: Schedule}
- {code: SearchParameter}
- code: ServiceRequest
  param: [encounter]
- {code: Slot}
- {code: Specimen}
- {code: SpecimenDefinition}
- {code: StructureDefinition}
- {code: StructureMap}
- {code: Subscription}
- {code: Substance}
- {code: SubstanceNucleicAcid}
- {code: SubstancePolymer}
- {code: SubstanceProtein}
- {code: SubstanceReferenceInformation}
- {code: SubstanceSourceMaterial}
- {code: SubstanceSpecification}
- {code: SupplyDelivery}
- {code: SupplyRequest}
- {code: Task}
- {code: TerminologyCapabilities}
- {code: TestReport}
- {code: TestScript}
- {code: ValueSet}
- {code: VerificationResult}
- code: VisionPrescription
  param: [encounter]

```
{% endtab %}
{% endtabs %}

Although FHIR specification states that compartment definitions can only be defined by HL7 International, this restriction does not apply to Aidbox. You can define any compartments in your box so long as they are valid.

## Compartment Search

To search a [compartment](http://hl7.org/fhir/compartmentdefinition.html) for either all possible resources or for a particular resource, type respectively:

```text
GET [base]/[Compartment]/[id]/{*?[parameters]{&_format=[mime-type]}}
GET [base]/[Compartment]/[id]/[type]{?[parameters]{&_format=[mime-type]}}
```

For example, to retrieve all the observation resources for a particular LOINC code associated with a specific encounter:

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

Our example compartment search is basically equivalent to these standard FHIR search requests:

```text
GET [base]/Condition?patient=[id]
GET [base]/Condition?patient=[id]&code:in=http://hspc.org/ValueSet/acute-concerns
```

The outcome of a compartment search is the same as the equivalent FHIR search. For example, both these searches return the same outcome if there is no patient 333:

```text
GET [base]/Patient/333/Condition
GET [base]/Condition?patient=333
```

If the patient doesn't exist or the user has no access to the patient, both these searches return an empty bundle with no matches. 

However, there is a key difference in functionality between compartment-based searches and direct searches with parameters. Consider this search:

```text
GET [base]/Patient/[id]/Communication
```

Because the definition of the [patient compartment](http://build.fhir.org/compartmentdefinition-patient.html) for [Communication ](http://build.fhir.org/communication.html)says that a Communication resource is in the patient compartment if the subject, sender, or recipient is the patient, the compartment search is actually the same as the union of these 3 searches:

```text
GET [base]/Communication?subject=[id]
GET [base]/Communication?sender=[id]
GET [base]/Communication?recipient=[id]
```

