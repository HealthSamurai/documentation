---
description: Electronic prescriptions with Aidbox
---

# ePrescription

{% hint style="warning" %}
ePrescription module is currently in **preview**
{% endhint %}

Aidbox ePrescription is a powerful **pluggable module** designed to enable healthcare vendors to seamlessly **send electronic prescriptions**. Our solution provides a robust, ready-to-use backend that integrates directly with Surescripts, streamlining the prescription process.

## Features

* Synchronize pharmacies directly to Aidbox
* Maintain Surescripts providers locations
* Send new prescription
* Send cancellation request
* Handle change request (under development)
* Handle renewal request (under development)
* FDB (First DataBank) integration\
  **You will need API key to use these features.**
  * Medication and allergies search
  * Drug-drug and drug-allergy interactions checks

All features are built with FHIR compatibility in mind, eliminating the need for custom resource mapping. Simply leverage standard FHIR resources to get started.\


{% hint style="warning" %}
Controlled substances currently not supported, but we are working on it.
{% endhint %}

## Requirements

* Aidbox with FHIR r4b
* Surescripts direct contract
* User Interface for prescribing\
  **We provide an API-based solution. A user interface is required to obtain Surescripts certification**

Before using the ePrescription module in production, your implementation must be certified by Surescripts. We will gladly support you through this certification process.
