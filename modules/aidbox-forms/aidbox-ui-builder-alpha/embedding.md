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

## Step 3: Configure Attributes

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
- `delegate-alerts` (optional): Emits alert events instead of showing them in the UI.
- `enable-fetch-proxy` (optional): Enables [request interception](#step-4-optional-configure-requests-interception) for custom fetch behavior.
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
- `delegate-alerts` (optional): Emits alert events instead of showing them in the UI.
- `enable-fetch-proxy` (optional): Enables [request interception](#step-4-optional-configure-requests-interception) for custom fetch behavior.
- `theme` (optional): Theme settings as a JSON string.
- `token` (optional): JWT token for authentication.
- `disable-load-sdc-config` (optional): Disables automatic loading of SDC configuration.
{% endtab %}

{% endtabs %}

## Step 4 (Optional): Configure Requests Interception

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

# Controlled Mode (Deprecated)

Controlled mode gives full manual control over loading, updating, and persisting a Questionnaire and QuestionnaireResponse at the application level. The system does not automatically save changes, so the developer must handle data flow and storage. Depending on the use case, making requests to the intended endpoints, as normal mode does, may still be necessary.

This approach is useful when custom validation is required, such as enforcing business rules that go beyond standard validation mechanisms. It is also beneficial when integrating with external systems, where the Questionnaire and QuestionnaireResponse are stored outside Aidbox or when working with multiple data sources. Additionally, it allows fine-grained control over how and when data is fetched, updated, or persisted, making it suitable for applications that need to manage state independently.

Controlled mode is deprecated in favor of request interception, as the latter provides full control over the component’s interaction with Aidbox, extending beyond just Questionnaire and QuestionnaireResponse. 

## Step 5: Enable Controlled Mode
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

## Step 6: Listen Events

In controlled mode, event handling becomes more critical since the system does not automatically manage updates. Developers must listen for events like change, save, and submit to track modifications and persist data manually. 

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

The following events are available for listening:

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
