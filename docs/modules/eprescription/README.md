---
description: Send electronic prescriptions via Surescripts using Aidbox ePrescription module with FHIR-native NewRx and CancelRx.
---

# ePrescription

{% hint style="warning" %}
ePrescription module is currently in **preview**
{% endhint %}

Aidbox ePrescription is a powerful **pluggable module** designed to enable healthcare vendors to seamlessly **send electronic prescriptions**. Our solution provides a robust, ready-to-use backend that integrates directly with Surescripts, streamlining the prescription process.

## Features

All features are built with FHIR compatibility in mind, eliminating the need for custom resource mapping. Simply leverage standard FHIR resources to get started.

#### Directories management

* Synchronize pharmacies directly to Aidbox
* Get all providers locations
* Maintain Surescripts providers locations (add / update / disable)

#### Prescriptions management

* Send new prescription ([NewRx](prescribing/newrx-message.md))
* Send cancellation request ([CancelRx](prescribing/cancelrx-message.md))
* Handle change request ([RxChange](prescribing/pharmacy-initiated-messages/rx-change.md))
* Handle renewal request ([RxRenewal](prescribing/pharmacy-initiated-messages/rx-renewal.md))
* About DetectedIssue ([DetectedIssue](prescribing/detected-issue.md))

{% hint style="warning" %}
Controlled substances currently not supported, but we are working on it.
{% endhint %}

#### Medications

* FDB (First DataBank) integration\
  **You will need API key to use these features.**
  * Medication and allergies search
  * Get common SIGs for medications
  * Drug-drug and drug-allergy interactions checks
* RxNorm integration
  * Medication search

## Requirements

* Aidbox with FHIR r4b and FHIRSchema
* Surescripts direct contract
* User Interface for prescribing\
  **We provide an API-based solution. A user interface is required to obtain Surescripts certification**

Before using the ePrescription module in production, your implementation must be certified by Surescripts. We will gladly support you through this certification process.

## References

* [Frequently Asked Questions](frequently-asked-questions.md)
* [FDB Medications](medications/fdb.md)
* [List of metrics](reference/list-of-metrics.md)

### Directory management

* [DirectoryDownload message](directory/directorydownload-message.md)
* [DisableProviderLocation message](directory/disableproviderlocation-message.md)
* [GetProviderLocation message](directory/getproviderlocation-message.md)
