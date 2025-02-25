---
description: Embedding the Aidbox Form Builder and Renderer into your application using web components.
---

# Embedding Aidbox Form Builder and Renderer

You can embed the **Aidbox Form Builder** and **Aidbox Form Renderer** into your application or website using web components.
- **Aidbox Form Builder** allows users to create and manage forms directly within your application.
- **Aidbox Form Renderer** enables users to fill out forms within your application without leaving your platform.

## How to Embed the Components

### Step 1: Include the Script

{% tabs %}

{% tab title="Builder" %}
For the Builder:
```html
<script src="{{ YOUR_AIDBOX_INSTANCE_BASE_URL }}/static/aidbox-forms-builder-webcomponent.js"></script>
```
{% endtab %}

{% tab title="Renderer" %}
For the Renderer:
```html
<script src="{{ YOUR_AIDBOX_INSTANCE_BASE_URL }}/static/aidbox-forms-renderer-webcomponent.js"></script>
```
{% endtab %}

{% endtabs %}

### Step 2: Add the Component Tag

{% tabs %}

{% tab title="Builder" %}
For the **Builder**:
```html
<aidbox-form-builder
  style="width: 100%; border: none; align-self: stretch; display: flex"
  form-id="{{ FORM_ID }}"
  hide-back
/>
```
{% endtab %}

{% tab title="Renderer" %}
For the **Renderer**:
```html
<aidbox-form-renderer
  style="width: 100%; border: none; align-self: stretch; display: flex"
  questionnaire-id="{{ ID }}"
/>
```
{% endtab %}

{% endtabs %}

### Step 3: Configure Attributes

#### Common Attributes
- `base-url` (optional): The base URL of your Aidbox instance.
- `style` (optional): Custom styling for the iframe.
- `config` (optional): The [configuration](configuration.md) as a JSON string.
- `delegate-alerts` (optional): Emits alert events instead of showing them in the UI.
- `enable-fetch-proxy` (optional): Enables request interception for custom fetch behavior.
- `theme` (optional): Theme settings as a JSON string.
- `token` (optional): JWT token for authentication.

#### Component-Specific Attributes

{% tabs %}

{% tab title="Builder" %}
- `form-id` (optional): The ID of the form to load.
- `value` (optional): JSON string representing the form content.
- `hide-back`, `hide-save`, `hide-publish` (optional): Hides respective buttons in the UI.
- `disable-save`, `disable-publish` (optional): Disables respective actions.
- `hide-population`, `hide-extraction` (optional): Hides respective functionalities.
- `collapse-debugger` (optional): Collapses the debugging panel.
- `hide-add-theme`, `hide-edit-theme`, `hide-save-theme` (optional): Controls theme-related UI elements.
- `show-share` (optional): Shows the share button.
- `language` (optional): Default language for the builder.
- `translation-languages` (optional): Comma-separated list of allowed languages.
- `disable-load-sdc-config` (optional): Disables automatic loading of SDC configuration.
{% endtab %}

{% tab title="Renderer" %}
- `questionnaire-id` (optional): ID of the questionnaire to load.
- `questionnaire-response-id` (optional): ID of the questionnaire response to load.
- `questionnaire` (optional): JSON string representing the questionnaire.
- `questionnaire-response` (optional): JSON string representing the questionnaire response.
- `hide-footer` (optional): Hides the form footer.
- `hide-language-selector` (optional): Hides language selector.
- `disable-load-sdc-config` (optional): Disables automatic loading of SDC configuration.
{% endtab %}

{% endtabs %}

### Step 4 (Optional): Request Interception

Both components support intercepting network requests. This allows debugging or customization, such as modifying authentication headers or rerouting requests.

Enable request interception by setting the `enable-fetch-proxy` attribute and defining a custom `fetch` function:

{% tabs %}

{% tab title="Builder" %}
```html
<aidbox-form-builder id="aidbox-form-builder" enable-fetch-proxy />

<script>
    const builder = document.getElementById('aidbox-form-builder');
    
    builder.fetch = async (url, init) => {
        console.log('Intercepted request', url, init);
        return fetch(url, init);
    };
</script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<aidbox-form-renderer id="aidbox-form-renderer" enable-fetch-proxy />

<script>
    const renderer = document.getElementById('aidbox-form-renderer');
    
    renderer.fetch = async (url, init) => {
        console.log('Intercepted request', url, init);
        return fetch(url, init);
    };
</script>
```
{% endtab %}


{% endtabs %}

For more complex use cases, such as attaching authorization headers or storing questionnaires locally, refer to the [detailed interception guide](embedding-builder.md#request-interception).

## Controlled Mode (Deprecated)

In **controlled mode**, you manage the form state within your application. This mode is now deprecated.

{% tabs %}

{% tab title="Builder" %}
```html
<aidbox-form-builder
  id="aidbox-form-builder"
  value="YOUR FORM AS JSON STRING"
  show-share
  hide-back
  hide-save
  hide-publish
/>
```

Listening to change events:
```html
<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.addEventListener('change', (event) => {
    console.log('Form changed', event.detail);
    builder.setAttribute('value', JSON.stringify(event.detail));
  });
</script>
```
{% endtab %}

{% tab title="Renderer" %}
```html
<aidbox-form-renderer
  questionnaire="YOUR QUESTIONNAIRE AS JSON STRING"
  questionnaire-response="YOUR QUESTIONNAIRE RESPONSE AS JSON STRING"
/>
```

Listening to change events:
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

## Listening to Events

Both components emit events for interaction tracking:

{% tabs %}

{% tab title="Builder" %}
- `alert`: Emitted when an alert occurs.
- `change`: Triggered when the form is modified.
- `back`: Emitted when the back button is clicked.
- `save`: Emitted when the form is saved.
- `select`: Emitted when an item is selected.
- `ready`: Emitted when the builder is loaded.
{% endtab %}

{% tab title="Renderer" %}
- `alert`: Emitted when an alert occurs.
- `change`: Triggered when the questionnaire response is updated.
- `submit`: Emitted when the Submit button is clicked.
- `extracted`: Emitted when data extraction occurs.
- `ready`: Emitted when the renderer is loaded.
{% endtab %}

{% endtabs %}

## Conclusion
By embedding the **Aidbox Form Builder** and **Aidbox Form Renderer**, you can seamlessly integrate form creation and submission into your application, providing users with a powerful and user-friendly experience.
