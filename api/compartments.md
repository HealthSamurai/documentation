# Compartments

## Overview

Each resource may belong to one or more logical compartments. A compartment is a logical grouping of resources which share a common property. Compartments have two principal roles:

* Function as an access mechanism for finding a set of related resources quickly
* Provide a definitional basis for applying access control to resources quickly

Read more about compartments in FHIR documentation [http://build.fhir.org/compartmentdefinition.html](http://build.fhir.org/compartmentdefinition.html).

{% hint style="warning" %}
At present, compartment definitions can only be defined by HL7 International. This is because their existence creates significant impact on the behavior of servers.
{% endhint %}

## List of Compartments

| **Title** | **Description** | **Identity** | **Membership** |
| :--- | :--- | :--- | :--- |
| [Patient](http://build.fhir.org/compartmentdefinition-patient.html) | The set of resources associated with a particular patient | There is an instance of the patient compartment for each patient resource, and the identity of the compartment is the same as the patient. When a patient is linked to another patient, all the records associated with the linked patient are in the compartment associated with the target of the link. | The patient compartment includes any resources where the subject of the resource is the patient, and some other resources that are directly linked to resources in the patient compartment |
| [Encounter](http://build.fhir.org/compartmentdefinition-encounter.html) | The set of resources associated with a particular encounter | There is an instance of the encounter compartment for each encounter resource, and the identity of the compartment is the same as the encounter | The encounter compartment includes any resources where the resource has an explicitly nominated encounter, and some other resources that themselves link to resources in the encounter compartment. Note that for many resources, the exact nature of the link to encounter can be ambiguous \(e.g. for a DiagnosticReport, is it the encounter when it was initiated, or when it was reported?\) |
| [RelatedPerson](http://build.fhir.org/compartmentdefinition-relatedperson.html) | The set of resources associated with a particular 'related person' | There is an instance of the relatedPerson compartment for each relatedPerson resource, and the identity of the compartment is the same as the relatedPerson | The relatedPerson compartment includes any resources where the resource is explicitly linked to relatedPerson \(usually as author\) |
| [Practitioner](http://build.fhir.org/compartmentdefinition-practitioner.html) | The set of resources associated with a particular practitioner | There is an instance of the practitioner compartment for each Practitioner resource, and the identity of the compartment is the same as the Practitioner | The practitioner compartment includes any resources where the resource is explicitly linked to a Practitioner \(usually as author, but other kinds of linkage exist\) |
| [Device](http://build.fhir.org/compartmentdefinition-device.html) | The set of resources associated with a particular device | There is an instance of the device compartment for each Device resource, and the identity of the compartment is the same as the Device | The device compartment includes any resources where the resource is explicitly linked to a Device \(mostly subject or performer\) |

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

Further details of searching by compartment are [described under the search operation](http://build.fhir.org/http.html#vsearch). As a search related operation, the assignment of resources to compartments is only based on the current version of any of the resources involved. Note that [contained](http://build.fhir.org/references.html#contained) patient resources cannot create a patient compartment of their own.

Note that while this specification describes how to use the compartment syntax to find resources that are logically associated with the compartment, the compartment is not part of the identity of the resource. E.g. the response to the following is not defined by this specification:

```text
  GET [base]/Patient/[patient-id]/Condition/[resource-id]
```

The response for write operations \(PUT/DELETE/PATCH\) are also not defined by this specification. Nor is the response to a POST defined:

```text
  POST [base]/Patient/[patient-id]/Condition
```

There is no expectation for servers to support either read or write to such URLs.

