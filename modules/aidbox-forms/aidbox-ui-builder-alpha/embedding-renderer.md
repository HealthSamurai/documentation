---
description: >-
  Incorporate the Aidbox Form Renderer into your application or website using
  web components, allowing users to fill out forms without leaving your
  platform.
---

# Embedding Form Renderer

Embed the Aidbox Form Renderer directly into your application or website using web components. This enables users to complete forms within your app, without navigating away.

## How to Embed the Renderer

Follow these steps to embed the Aidbox Form Renderer:

### Step 1: Include the Script

Add the following script tag to your HTML file:

```html
<script src="{{ YOUR_AIDBOX_INSTANCE_BASE_URL }}/static/aidbox-forms-renderer-webcomponent.js"></script>
```

### Step 2: Add the Renderer Tag

Insert the following HTML tag where you want the renderer to appear:

```html
<aidbox-form-renderer
  style="width: 100%; border: none; align-self: stretch; display: flex"
  questionnaire-id="{{ id }}"
>
</aidbox-form-renderer>
```

### Step 3: Configure Attributes

Here is a comprehensive list of attributes that can be passed to the `<aidbox-form-renderer>` tag:

* `base-url` (optional): The base URL of your Aidbox instance. By default, it uses the URL of the Aidbox instance from which the script is loaded.
* `style` (optional): Defines the style of the embedded iframe.
* `token` (optional): JWT token for authenticating the user.
* `questionnaire-id` (optional): The ID of the questionnaire to load. Either `questionnaire` or `questionnaire-id` must be provided.
* `questionnaire-response-id` (optional): The ID of the questionnaire response to load. Either `questionnaire-response` or `questionnaire-response-id` must be provided.
* `hide-footer` (optional): Hides the footer of the form.
* `hide-language-selector` (optional): Hide language selector if multilingual questionnaire is rendered.
* `delegate-alerts` (optional): When set the web-component emits `alert` events with the alert objects as the event detail, instead of showing them in the UI.
* `config` (optional): The [configuration](configuration.md) provided as a JSON string.
* `theme` (optional): The theme provided as a JSON string.

{% hint style="warning" %}
Deprecated: Pass `theme` as part of the `config` attribute value.
{% endhint %}


## Embedding Renderer in Controlled Mode

In controlled mode, you can manage the renderer's state directly within your application. The renderer does not handle loading or saving the questionnaire or questionnaire response to Aidbox. Instead, it requires you to supply them as a JSON string through attributes and sends change events back to your application.

### Step 1: Include the Script

Add the following script tag to your HTML file:

```html
<aidbox-form-renderer
  style="width: 100%; border: none; align-self: stretch; display: flex"
  questionnaire="YOUR QUESTIONNAIRE AS JSON STRING"
  questionnaire-response="YOUR QUESTIONNAIRE RESPONSE AS JSON STRING"
>
</aidbox-form-renderer>
```

### Step 2: Add Change Event Listener

Add a change event listener to the renderer element to capture the updated form state:

```html
<script>
  const renderer = document.getElementById('aidbox-form-renderer');

  renderer.addEventListener('change', (event) => {
    console.log('Questionnaire Response changed', event.detail);
    // You can update the form state in your application if necessary
    // event.detail contains the updated questionnaire response as a JSON object
    // Additionally, you can pass the updated response back to the renderer using the questionnaire-response attribute
    renderer.setAttribute('questionnaire-response', JSON.stringify(event.detail));
  });
</script>
```

### Step 3: Configure Attributes

You can pass the following attributes to the `<aidbox-form-renderer>` tag:

#### Available Attributes

* `base-url` (optional): The base URL of your Aidbox instance. By default, it uses the URL of the Aidbox instance from which the script is loaded.
* `style` (optional): Specifies the style of the embedded iframe.
* `token` (optional): JWT token used for user authentication.
* `questionnaire` (required): The questionnaire provided as a JSON string.
* `questionnaire-response` (optional): The questionnaire response provided as a JSON string.
* `hide-footer` (optional): Hides the footer of the form.
* `hide-language-selector` (optional): Hide language selector if multilingual questionnaire is rendered.
* `delegate-alerts` (optional): When set the web-component emits `alert` events with the alert objects as the event detail, instead of showing them in the UI.
* `config` (optional): The [configuration](configuration.md) provided as a JSON string.
* `theme` (optional): The theme provided as a JSON string.

{% hint style="warning" %}
Deprecated: Pass `theme` as part of the `config` attribute value.
{% endhint %}


### Step 4: Listen to Renderer Events

The renderer emits the following events:

* `change`: Triggered when the questionnaire response is updated, either during auto-save. The `event.detail` contains the updated questionnaire response as a JSON object.
* `submit`: Emitted when 'Submit' button is clicked. The `event.detail` contains the questionnaire response as a JSON object.
* `ready`: Fired when the Aidbox Forms Renderer has fully loaded and is ready for use.

By following these steps, you can effortlessly integrate the Aidbox Form Renderer into your application, offering a robust solution for form creation and management directly within your user interface.
