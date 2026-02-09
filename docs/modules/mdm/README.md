---
description: >-
  This page introduces the Aidbox MDM module, its core capabilities, and guides
  for deployment, configuration, matching, and merge/unmerge operations.
---

# MDM — Master Data Management

**Master Data Management (MDM)** is a module in Aidbox that ensures **accurate entity identification** by detecting and removing duplicate records. It helps maintain consistent and reliable data across healthcare systems.

**MDM enables:**

* accurate [**matching**](find-duplicates-match.md) of records across different systems and facilities,
* [**merging**](merging-and-unmerging-records-usdmerge-and-usdunmerge.md#merge-operation) of duplicate records into a single record,
* [**unmerging**](merging-and-unmerging-records-usdmerge-and-usdunmerge.md#unmerge-operation) of incorrectly linked records,
* maintaining the **integrity** of clinical data and treatment history.

Using MDM **reduces the risk** of lost or duplicated data, errors, and issues with data exchange. This is especially critical in complex ecosystems with many sources — such as clinics, labs, and telemedicine platforms.

The MDM module utilizes a **probabilistic** (score-based or Fellegi-Sunter) method. It is more flexible and can provide better results than rule-based approaches, but at the cost of simplicity.

## MDM Capabilities Overview

### Technical Capabilities

* FHIR R4 support
* Seamless integration with the Aidbox platform
* API-first architecture with a user-friendly web-based UI
* Notifications for external systems via webhooks (non-FHIR format)
* Unlimited scalability — supports any number of records
* Can be deployed in the cloud or on-premises

### Data Safety, Transparency and Consistency

* Role-based access control
* Full traceability of all operations, user actions and API calls
* Supports compliance with security and regulatory standards

### Core Feature set

* Search for records
* Flexible matching using a probabilistic algorithm
  * Fully configurable for specific data and use cases
  * Handles typos and incomplete data
* Manual record merging with unique merge strategy combining golden record and survivor record approaches
* Unmerge capability
* Ability to mark record pairs as non-duplicates to exclude them from future match results

## Run MDM locally

{% content-ref url="run-mdm-locally.md" %}
[run-mdm-locally.md](run-mdm-locally.md)
{% endcontent-ref %}

## Configure MDM module

Configure the MDM module to use a matching model stored in the MDM server (backend)

{% content-ref url="configure-mdm-module.md" %}
[configure-mdm-module.md](configure-mdm-module.md)
{% endcontent-ref %}

## Find Duplicates

Use `$match` operation to find duplicates


## Merge and Unmerge Records

Use `$merge` and `$unmerge` operations to manage duplicate records

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
