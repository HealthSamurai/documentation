---
description: Learn how to share or embed the form with users.
---

# Form sharing

Aidbox Forms supports two main scenarios for sharing forms with users:

1. [Sharing a Form Link with the User](form-sharing.md#sharing-a-form-link-with-the-user)
2. [Embed the Form via iframe into an application or website](form-sharing.md#embed-the-form-via-iframe-into-an-application-or-website)

## Sharing a Form Link with the User

After creating a form, you can generate a link to the form and send it to the user via email or any other messenger for him to fill out. Here's how you can do it:

1. **Select the Form:**
   * Choose the form you want to share from your list of forms.
2. **Generate the Link:**
   * Use the "Share" option within the Aidbox Forms interface.
   * Generate the link via[ $populatelink ](../../../reference/aidbox-forms/fhir-sdc-api.md#populate-questionnaire-and-generate-a-link-usdpopulatelink)FHIR SDC operation.
3. **Copy the Link:**
   * Once the link is generated, copy it to your clipboard.
4. **Send the Link:**
   * Paste the link into an email or messenger of your choice.
   * Send the message containing the link to the user or users who need to fill out the form.
5. **User Fills Out the Form:**
   * When the user clicks on the link, they will be directed to the form.
   * The user can then fill out the form online (or [offline](./offline-forms.md)) and submit it.
6. **Review Responses:**
   * After the user submits the form, you can access and review their responses within Aidbox Forms.

{% hint style="info" %}
You can set up the user redirect after the user submits the form. Look [here](../../../reference/aidbox-forms/fhir-sdc-api.md#redirect-on-submit).

Default behavior when the user remains on the page with a completed form
{% endhint %}



## Embed the Form via iframe into an application or website

After creating a form, you can generate an iframe code for this form and embed it into your application's HTML or website where you want the form to appear. Here's how you can do it:

1. **Select the Form:**
   * Choose the form you want to embed from your list of forms.
2. **Generate the iframe Code:**
   * Use the "Share" option within the Aidbox Forms interface.
   * Generate the link via[ $populatelink ](../../../reference/aidbox-forms/fhir-sdc-api.md#populate-questionnaire-and-generate-a-link-usdpopulatelink)FHIR SDC operation and use it with iframe. &#x20;

```
<iframe src="your-generated-link"></iframe>
```

3. **Copy the iframe Code:**&#x20;
   * Once the iframe code is generated, copy it to your clipboard.
4. **Embed into Your Application's HTML:**
   * Open your application's HTML file where you want the form to appear.
   * Paste the copied iframe code into the appropriate location within the HTML file.
5. **Save and Publish Your Application:**
   * Save the changes to your HTML file and publish your application to make the embedded form accessible to users.
6. **User Fills Out the Form:**
   * When users access your application, they will see the embedded form and can fill it out directly within your application.
7. **Collect and Review Responses:**
   * After users submit the form, you can access and review their responses within Aidbox Forms.
