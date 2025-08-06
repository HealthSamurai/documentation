---
description: >-
  This page introduces the Aidbox MPI module, its core capabilities, and guides
  for deployment, configuration, matching, and merge/unmerge operations.
---

# MPI — Master Patient Index

**Master Patient Index (MPI)** is a module in Aidbox that ensures **accurate patient identification** by detecting and removing duplicate records. It helps maintain consistent and reliable data across healthcare systems.

**MPI enables:**

* accurate [**matching**](find-duplicates-match.md) of patient records across different systems and facilities,
* [**merging**](merging-and-unmerging-records-usdmerge-and-usdunmerge.md#merge-operation) of duplicate records into a single record,
* [**unmerging**](merging-and-unmerging-records-usdmerge-and-usdunmerge.md#unmerge-operation) of incorrectly linked records,
* maintaining the **integrity** of clinical data and treatment history.

Using MPI **reduces the risk** of lost or duplicated medical data, treatment errors, and issues with data exchange. This is especially critical in complex ecosystems with many sources — such as clinics, labs, and telemedicine platforms.

The MPI module utilizes **probabilistic** (score-based or Fellegi-Sunter) method. It is more flexible and can provide better results than rule-based approaches, but at the cost of simplicity.

## MPI Capabilities Overview

### Technical Capabilities

* FHIR R4 support
* Seamless integration with the Aidbox platform
* API-first architecture with a user-friendly web-based UI
* Notifications for external systems via webhooks (non-FHIR format)
* Unlimited scalability — supports any number of patient records
* Can be deployed in the cloud or on-premises

### Data Safety, Transparency and Consistency

* Role-based access control
* Full traceability of all operations, user actions and API calls
* Supports complience with security and regulatory standarts

### Core Feature set

* Search for patients
* Flexible patient matching using a probabilistic algorithm
  * Fully configurable for specific data and use-cases
  * Handles typos and incomplete data
* Manual record merging with unique merge strategy combining golden record and survivor record approaches
* Unmerge capability
* Ability to mark record pairs as non-duplicates to exclude them from future match results

## Run MPI with Kubernetes

{% content-ref url="get-started/deploy-mpi-with-kubernetes.md" %}
[deploy-mpi-with-kubernetes.md](get-started/deploy-mpi-with-kubernetes.md)
{% endcontent-ref %}

## Configure MPI module

Configure Aidbox MPI module to use your matching model

{% content-ref url="get-started/configure-mpi-module.md" %}
[configure-mpi-module.md](get-started/configure-mpi-module.md)
{% endcontent-ref %}

## Find Duplicates

Use `$match` operation to find duplicates

{% content-ref url="find-duplicates-match-old.md" %}
[find-duplicates-match-old.md](find-duplicates-match-old.md)
{% endcontent-ref %}

## Merge and Unmerge Records

Use `$merge` and `$unmerge` operations to manage duplicate patient records

{% content-ref url="merging-and-unmerging-records-usdmerge-and-usdunmerge.md" %}
[merging-and-unmerging-records-usdmerge-and-usdunmerge.md](merging-and-unmerging-records-usdmerge-and-usdunmerge.md)
{% endcontent-ref %}

## How It Works

Learn more about:

1. How our matching model works

{% content-ref url="matching-model-explanation.md" %}
[matching-model-explanation.md](matching-model-explanation.md)
{% endcontent-ref %}

2. How record merge and unmerge operations work

{% content-ref url="merging-and-unmerging-records-usdmerge-and-usdunmerge.md" %}
[merging-and-unmerging-records-usdmerge-and-usdunmerge.md](merging-and-unmerging-records-usdmerge-and-usdunmerge.md)
{% endcontent-ref %}

3. Mathematics behind probabilistic matching

{% content-ref url="mathematical-details.md" %}
[mathematical-details.md](mathematical-details.md)
{% endcontent-ref %}
