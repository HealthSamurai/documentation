---
description: >-
  Multi-tenancy support for differentiating access among organizations within
  Forms
---

# Forms multitenancy

## Overview

Forms now supports multi-tenancy, allowing for the segregation of access between different organizations (tenants) within the product. This feature ensures that organizations can manage their forms independently, enhancing security and privacy. This document outlines how to utilize multi-tenancy in Forms, including navigating to the builder for specific organizations and performing back-end operations in a multi-tenant mode.

## Implementation Details

The implementation of multi-tenancy in Forms is based on Aidbox's standard for [Organization-based hierarchical access control](../../security-and-access-control/multitenancy/organization-based-hierarchical-access-control.md). This approach allows for a structured and secure method to manage access across different levels of the organization hierarchy.

## Enabling Multi-Tenancy

To enable multi-tenancy for Forms, it is necessary to enable Organization-based hierarchical access control for the entire Aidbox environment. Detailed instructions on how to enable this feature can be found here: [How to Enable Hierarchical Access Control](../../security-and-access-control/multitenancy/how-to-enable-hierarchical-access-control.md).

## Getting Started

### Accessing the Builder for an Organization

1. **Standard Builder Access**:
   * Typically, to access the form builder, the URL fragment path used is `#/forms/builder`.
2. **Organization-Specific Builder Access**:
   * To navigate to the builder for a specific organization, modify the URL as follows: `#/org/[organization-id]/forms/builder`. Replace `[organization-id]` with the actual ID of the Organization resource.

### Performing Back-End Operations in Multi-Tenant Mode

#### 1. Standard Back-End Call

A standard back-end operation to populate a link within a Questionnaire might look like this:

{% tabs %}
{% tab title="FHIR API" %}
```
[base]/Questionnaire/[questionnaire-id]/$populatelink
```
{% endtab %}

{% tab title="Aidbox API" %}
```
[base]/fhir/Questionnaire/[questionnaire-id]/$populatelink
```
{% endtab %}
{% endtabs %}

#### 2. Multi-Tenant Back-End Call

In multi-tenant mode, the call is modified to include the organization context:

{% tabs %}
{% tab title="FHIR API" %}
```
[base]/Organization/[organization-id]/aidbox/Questionnaire/[questionnaire-id]/$populatelink
```
{% endtab %}

{% tab title="Aidbox API" %}
```
[base]/Organization/[organization-id]/fhir/Questionnaire/[questionnaire-id]/$populatelink
```
{% endtab %}
{% endtabs %}

Replace `[organization-id]` and `[questionnaire-id]` with the respective Organization and Questionnaire resource IDs.

## Important Considerations

### Data Isolation

* **Organization-Specific Data**:
  * Each organization's data is isolated. This means that one organization cannot access the data of another organization, ensuring privacy and security.

### URL Modification

* **Accessing Organization-Specific Features**:
  * Remember to modify URLs appropriately to access organization-specific features or perform back-end operations in a multi-tenant context.

## Troubleshooting

### Common Issues

* **Incorrect Organization ID**:
  * Ensure that the organization ID in the URL is correct. An incorrect ID can lead to access issues or errors.
* **Access Denied**:
  * If you encounter an "Access Denied" error, verify that you have the necessary permissions to access the organization's resources.

## FAQs

**Q: How does multi-tenancy affect data privacy?** A: Multi-tenancy enhances data privacy by isolating each organization's data, ensuring that only authorized users within an organization can access its information.

**Q: Can I prepopulate a questionnaire with resources from other organizations?** A: Yes, but only if those organizations are subsidiaries of the current one and/or if the resource has a shared mode.
