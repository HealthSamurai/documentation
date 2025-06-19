---
description: Embedding the Aidbox Form Builder and Renderer into your application using web components.
---

# Embedding Aidbox Form Builder and Renderer

You can embed the **Builder** and **Renderer** into your application or website using web components.
- **Builder** allows users to create and manage forms directly within your application.
- **Renderer** enables users to fill out forms within your application without leaving your platform.


## Step 1: Include the Script

{% tabs %}

{% tab title="Builder" %}
```html
<script src="{{ YOUR_AIDBOX_INSTANCE_BASE_URL }}/static/aidbox-forms-builder-webcomponent.js"></script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<script src="{{ YOUR_AIDBOX_INSTANCE_BASE_URL }}/static/aidbox-forms-renderer-webcomponent.js"></script>
```
{% endtab %}

{% endtabs %}

## Step 2: Add the Component Tag

{% tabs %}

{% tab title="Builder" %}
```html
<aidbox-form-builder
  style="width: 100%; border: none; align-self: stretch; display: flex"
  form-id="{{ FORM_ID }}"
  hide-back
/>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<aidbox-form-renderer
  style="width: 100%; border: none; align-self: stretch; display: flex"
  questionnaire-id="{{ ID }}"
/>
```
{% endtab %}

{% endtabs %}

## Step 3: Configure Attributes and Properties

The following attributes are available for the Builder and Renderer components. 
These attributes control various aspects of the form’s behavior and appearance, including the form’s layout, customization options, and integration with external systems.

{% tabs %}

{% tab title="Builder" %}
- `form-id` (optional): The ID of the form to load. If not provided, the builder opens with a blank form.
- `hide-back`, `hide-save`, `hide-publish` (optional): Hides respective buttons in the UI.
- `disable-save`, `disable-publish` (optional): Disables respective actions.
- `hide-population`, `hide-extraction` (optional): Hides respective functionalities.
- `collapse-debugger` (optional): Collapses the debugging panel.
- `hide-add-theme`, `hide-edit-theme`, `hide-save-theme` (optional): Controls theme-related UI elements.
- `show-share` (optional): Shows the share button.
- `language` (optional): Default language for the builder.
- `translation-languages` (optional): Comma-separated list of allowed languages.
- `base-url` (optional): The base URL of your Aidbox instance.
- `style` (optional): Custom styling for the iframe.
- `config` (optional): The [configuration](configuration.md) as a JSON string.
- `theme` (optional): Theme settings as a JSON string.
- `token` (optional): JWT token for authentication.
- `disable-load-sdc-config` (optional): Disables automatic loading of SDC configuration.
{% endtab %}

{% tab title="Renderer" %}
- `questionnaire-id`: ID of the questionnaire to load.
- `questionnaire-response-id` (optional): ID of the questionnaire response to load.
- `hide-footer` (optional): Hides the form footer.
- `hide-language-selector` (optional): Hides language selector.
- `base-url` (optional): The base URL of your Aidbox instance.
- `style` (optional): Custom styling for the iframe.
- `config` (optional): The [configuration](configuration.md) as a JSON string.
- `theme` (optional): Theme settings as a JSON string.
- `token` (optional): JWT token for authentication.
- `disable-load-sdc-config` (optional): Disables automatic loading of SDC configuration.
{% endtab %}

{% endtabs %}

Along with the above attributes, you can also set the following properties. 

{% hint style="info" %}
Unlike **attributes**, which can only store string values and are defined in the markup, 
**properties** can hold any JavaScript value (such as objects, arrays, functions, etc.) and must be set programmatically using JavaScript.
{% endhint %}

{% tabs %}

{% tab title="Builder" %}
- `onFetch` (optional): A custom fetch handler that allows you to intercept and modify network requests. The function receives the URL and request options as arguments.
- `onAlert` (optional): A custom alert handler that allows you to intercept and handle alerts, overriding the visual alert display. The function receives the alert object as an argument.
- `onChange` (optional): A custom callback function invoked when the questionnaire is modified, without affecting the default behavior. The function receives the updated questionnaire as an argument.
- `onBack` (optional): A custom callback function that is invoked when the back button is clicked, allowing you to override the default back button behavior.
{% endtab %}

{% tab title="Renderer" %}
- `onFetch` (optional): A custom fetch handler that allows you to intercept and modify network requests. The function receives the URL and request options as arguments.
- `onAlert` (optional): A custom alert handler that allows you to intercept and handle alerts, overriding the visual alert display. The function receives the alert object as an argument.
- `onChange` (optional): A custom callback function that is invoked when the questionnaire response is modified, without affecting the default behavior. The function receives the updated questionnaire response as an argument.
- `onPreviewAttachment` (optional): A custom callback function that allows you to handle attachment previews, enabling external editors or viewers. The function receives the attachment object as an argument.
{% endtab %}

{% endtabs %}

Below are examples of how to set properties programmatically:

#### onFetch: Intercept Network Requests

{% tabs %}

{% tab title="Builder" %}
```html
<aidbox-form-builder id="aidbox-form-builder" />

<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.onFetch = async (url, init) => {
    console.log('Intercepted request', url, init);
    init.headers = {
      ...init.headers,
      Authorization: 'Bearer YOUR_TOKEN'
    };
    return fetch(url, init);
  };
</script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<aidbox-form-renderer id="aidbox-form-renderer" />

<script>
  const renderer = document.getElementById('aidbox-form-renderer');

  renderer.onFetch = async (url, init) => {
    console.log('Intercepted request', url, init);
    return fetch(url, init);
  };
</script>
```
{% endtab %}

{% endtabs %}

For more complex use cases, such as attaching authorization headers or storing questionnaires locally, refer to the [detailed interception guide](request-interception.md).

#### onAlert: Handle Alerts

{% tabs %}

{% tab title="Builder" %}
```html
<aidbox-form-builder id="aidbox-form-builder" />

<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.onAlert = (alert) => {
    console.log('Alert received:', alert);
    // e.g., display toast or log error
  };
</script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<aidbox-form-renderer id="aidbox-form-renderer" />

<script>
  const renderer = document.getElementById('aidbox-form-renderer');

  renderer.onAlert = (alert) => {
    console.log('Alert received:', alert);
    // e.g., display toast or log error
  };
</script>
```
{% endtab %}

{% endtabs %}

### onPreviewAttachment: show attachments with external editor

Allows to preview attachments in an external editor or viewer.

Return `false` to use default Aidbox Forms attachment preview, or `true` to prevent default behavior and show your previewer.

```html
<aidbox-form-renderer id="aidbox-form-renderer" />

<script>
  const renderer = document.getElementById('aidbox-form-renderer');
  
  renderer.onPreviewAttachment = (attachment) => {
    // attachment is an FHIR attachment object
    // e.g., open attachment in external editor
    if(attachment.contentType === "application/dicom") {
      window.open(attachment.data, '_blank'); // if it's a data URL
      window.open(attachment.url, '_blank'); // if it's stored in S3
      return true;
    }
  
    return false;
    
  };
</script>
```


#### onChange: React to Form Updates

{% tabs %}

{% tab title="Builder" %}
```html
<aidbox-form-builder id="aidbox-form-builder" />

<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.onChange = (questionnaire) => {
    console.log('Updated questionnaire:', questionnaire);
    // e.g., log or analyze the questionnaire
  };
</script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<aidbox-form-renderer id="aidbox-form-renderer" />

<script>
  const renderer = document.getElementById('aidbox-form-renderer');

  renderer.onChange = (response) => {
    console.log('Updated questionnaire response:', response);
    // e.g., log or analyze the response
  };
</script>
```
{% endtab %}

{% endtabs %}

#### onBack: Customize Navigation

{% tabs %}

{% tab title="Builder" %}

```html
<aidbox-form-builder id="aidbox-form-builder" />

<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.onBack = () => {
    console.log('Back button clicked');
    // Perform custom navigation
  };
</script>
```

{% endtab %}

{% endtabs %}

#### contentHeight: Adjust Component Height
To ensure the component fits well within your layout, you can get the current content height of the component and adjust its height accordingly. This is particularly useful when embedding the component in a responsive design.

```html
<aidbox-form-renderer id="aidbox-form-renderer" />

<script>
  const renderer = document.getElementById('aidbox-form-renderer');

  console.log("Current content height:", renderer.contentHeight);
</script>
```

Additionally, you can set the height of the component dynamically based on the content height by settings the `style` attribute to auto:

```html
<aidbox-form-renderer id="aidbox-form-renderer" style="height: auto" /> 
```



# Controlled Mode (Deprecated)

Controlled mode gives full manual control over loading, updating, and persisting a Questionnaire and QuestionnaireResponse at the application level. The system does not automatically save changes, so the developer must handle data flow and storage. Depending on the use case, making requests to the intended endpoints, as normal mode does, may still be necessary.

This approach is useful when custom validation is required, such as enforcing business rules that go beyond standard validation mechanisms. It is also beneficial when integrating with external systems, where the Questionnaire and QuestionnaireResponse are stored outside Aidbox or when working with multiple data sources. Additionally, it allows fine-grained control over how and when data is fetched, updated, or persisted, making it suitable for applications that need to manage state independently.

{% hint style="warning" %}
Controlled mode is deprecated in favor of request interception, as the latter provides full control over the component’s interaction with Aidbox, extending beyond just Questionnaire and QuestionnaireResponse. 
{% endhint %}

## Step 4: Enable Controlled Mode
{% tabs %}

{% tab title="Builder" %}
To enable controlled mode, remove the `form-id` attribute and specify the Questionnaire resource as a JSON string in the `value` attribute.

```html
<aidbox-form-builder
  id="aidbox-form-builder"
  value="YOUR QUESTIONNAIRE AS JSON STRING"
/>
```
{% endtab %}

{% tab title="Renderer" %}
Enable controlled mode by removing the `questionnaire-id` attribute and specifying the Questionnaire resource as a JSON string in the `questionnaire` attribute. Optionally, you can also specify the `questionnaire-response` if needed.
```html
<aidbox-form-renderer
  questionnaire="YOUR QUESTIONNAIRE AS JSON STRING"
  questionnaire-response="YOUR QUESTIONNAIRE RESPONSE AS JSON STRING"
/>
```
{% endtab %}

{% endtabs %}

The following attributes are available for controlled mode:

{% tabs %}

{% tab title="Builder" %}
- `value`: The Questionnaire resource as a JSON string.
{% endtab %}

{% tab title="Renderer" %}
- `questionnaire`: The Questionnaire resource as a JSON string.
- `questionnaire-response` (optional): The QuestionnaireResponse resource as a JSON string.
{% endtab %}

{% endtabs %}

In controlled mode, handling events is essential, as the system no longer manages updates automatically. Developers are responsible for listening to events such as change, save, and submit to track user interactions and manually persist form data.

Below is a list of events you can listen for:

{% tabs %}

{% tab title="Builder" %}
- `change`: Triggered when the form is modified.
- `back`: Emitted when the back button is clicked.
- `save`: Emitted when the form is saved.
- `select`: Emitted when an item is selected.
- `ready`: Emitted when the builder is loaded.
{% endtab %}

{% tab title="Renderer" %}
- `change`: Triggered when the questionnaire response is updated.
- `submit`: Emitted when the Submit button is clicked.
- `extracted`: Emitted when data extraction occurs.
- `ready`: Emitted when the renderer is loaded.
{% endtab %}

{% endtabs %}

Below is an example of how to listen for `change` event:

{% tabs %}

{% tab title="Builder" %}
```html
<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.addEventListener('change', (event) => {
    // event.detail contains update questionnaire as json string 
    console.log('Form changed', event.detail);
    // you might want to update update it and set it back as value
    builder.setAttribute('value', JSON.stringify(event.detail));
  });
</script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<script>
  const renderer = document.getElementById('aidbox-form-renderer');

  renderer.addEventListener('change', (event) => {
    console.log('Questionnaire Response changed', event.detail);
    renderer.setAttribute('questionnaire-response', JSON.stringify(event.detail));
  });
</script>
```
{% endtab %}

{% endtabs %}
