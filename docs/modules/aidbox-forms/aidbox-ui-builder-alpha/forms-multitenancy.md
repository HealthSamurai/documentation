---
description: >-
  Multi-tenancy support for differentiating access among organizations within
  Forms
---

# Forms multitenancy

## Overview

Forms now supports multi-tenancy, allowing for the segregation of access between different organizations (tenants) within the product. This feature ensures that organizations can manage their forms independently, enhancing security and privacy. This document outlines how to utilize multi-tenancy in Forms, including navigating to the builder for specific organizations and performing back-end operations in a multi-tenant mode.

## Implementation Details

The implementation of multi-tenancy in Forms is based on Aidbox's standard for [Organization-based hierarchical access control](../../../access-control/authorization/scoped-api/organization-based-hierarchical-access-control.md). This approach allows for a structured and secure method to manage access across different levels of the organization hierarchy.

## Enabling Multi-Tenancy

To enable multi-tenancy for Forms, it is necessary to enable Organization-based hierarchical access control for the entire Aidbox environment. Detailed instructions on how to enable this feature can be found here: [How to Enable Hierarchical Access Control](../../../tutorials/security-access-control-tutorials/how-to-enable-hierarchical-access-control.md).

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

## **Multitenancy in Form UI**

Multitenancy in Aidbox Forms allows organizations to manage forms and responses independently within the same instance. When multitenancy is enabled, each user operates within the context of a selected organization, ensuring proper data isolation and controlled access to shared content.

To use multitenancy, you need to:

1. Enable the multitenancy feature in your instance settings.
2. Create or import organizations into the instance.

Once these steps are completed, the **organization selector** becomes available in the Form UI.

### **Organization Selector**

After multitenancy is enabled, a dropdown appears on the **Form** page.\
Users can switch between organizations to work within the desired context.

When an organization is selected:

* All pages show only the data related to that organization.
* Both **Questionnaires** (forms) and **QuestionnaireResponses** (responses) are filtered accordingly.

### **Form Templates**

The **Form Template** page provides several actions, depending on the user's current organization and the form’s ownership.

**1. Create a Form**

Users can create a new form under the currently selected organization.\
If no organization is selected, the form is created under the **root API**.

**2. Delete a Form**

Users can delete forms **only** if the form belongs to the currently selected organization.

**3. Share a Form with Child Organizations**

A form can be shared **only** with child organizations of the current one.\
Once shared, child organizations can view and use the form.

**4. Duplicate a Form**

Users can duplicate:

* Their own forms
* Forms shared with them by a parent organization

Duplicating a shared form creates a local copy under the selected organization, allowing independent editing.

**Restrictions**

Multitenancy introduces several rules to ensure proper isolation and hierarchy behavior.

**1. Sharing Limitations**

* A form can be shared **only** with child organizations.
* Sharing with sibling or parent organizations is not allowed.

**2. Editing Permissions**

* Users **cannot edit or delete** forms owned by another organization.
* To modify a shared form, the user must **duplicate it** under the selected organization.

**3. Root-Level Forms**

* If a form is created when no organization is selected, it becomes a **root-level form**.
* Root-level forms are visible outside any organizational context.

### **Responses**

On the **Response** page, users can see only the responses related to the currently selected organization.<br>

### **Form Gallery**

When importing a form from the **Form Gallery**, the imported form is automatically added under the currently selected organization.\
This ensures consistent ownership and makes the form manageable according to the multitenancy rules.
