---
description: Embedding the Aidbox Form Builder into your application using web components.
---

# Embedding UI Builder

Integrate the Aidbox Form Builder directly into your application or website using web components. This allows users to create and manage forms without leaving your application environment.

## How to Embed the Builder

Follow these steps to embed the Aidbox Form Builder:

### Step 1: Include the Script

Add the following script tag to your HTML file:

```html
<script src="{{ YOUR_AIDBOX_INSTANCE_BASE_URL }}/static/aidbox-forms-builder-webcomponent.js"></script>
```

### Step 2: Add the Builder Tag

Insert the following HTML tag where you want the builder to appear:

```html
<aidbox-form-builder
  style="width: 100%; border: none; align-self: stretch; display: flex"
  form-id="{{ FORM_ID }}"
  hide-back
/>
```

### Step 3: Configure Attributes

You can pass the following attributes to the `<aidbox-form-builder>` tag:

* `base-url` (optional): The base URL of your Aidbox instance. Defaults to the URL from which the script is loaded.
* `style` (optional): Style attributes for the underlying iframe.
* `form-id` (optional): ID of the form to load. If not provided, the builder opens with a blank form.
* `hide-back` (optional): Hides the back button on the builder page.
* `config` (optional): The [configuration](configuration.md) provided as a JSON string. 
* `language` (optional): The default language to use for the builder interface. It will not override the language set by a user in the builder.

  {% hint style="warning" %}
  Deprecated: Pass language as part of the `config` attribute value.
  {% endhint %}

* `translation-languages` (optional): Whitelist of comma-separated languages that can be used for translations in the builder. If not provided, [all languages](ui-builder-interface.md#list-of-supported-languages) are allowed.

  {% hint style="warning" %}
  Deprecated: Pass `translation-languages` as part of the `config` attribute value.
  {% endhint %}

## Embedding Builder in Controlled Mode

In controlled mode, you can manage the form state within your application. The builder does not load or save the form to Aidbox; instead, it expects you to provide the form as a JSON string and emits change events back to your application.

### Step 1: Include the Script

Add the following script tag to your HTML file:

```html
<aidbox-form-builder
  id="aidbox-form-builder"
  style="width: 100%; border: none; align-self: stretch; display: flex"
  value="YOUR FORM AS JSON STRING"
  show-share
  hide-back
  hide-save
  hide-publish
/>
```

### Step 2: Add Change Event Listener

Add a change event listener to the builder element to capture the updated form state:

```html
<script>
  const builder = document.getElementById('aidbox-form-builder');
  
  builder.addEventListener('change', (event) => {
    console.log('Form changed', event.detail);
    // Modify the form state in your application if needed
    // event.detail contains the modified questionnaire as a JSON object
    // You can also pass the modified form back to the builder using the value attribute
    builder.setAttribute('value', JSON.stringify(event.detail));
  });
</script>
```

### Step 3: Configure Attributes

You can pass the following attributes to the `<aidbox-form-builder>` tag:

#### Available Attributes

* `base-url` (optional): The base URL of your Aidbox instance. Defaults to the URL from which the script is loaded.
* `style` (optional): Style attributes for the underlying iframe.
* `value` (required): The questionnaire as a JSON string.
* `hide-back` (optional): Hides the back button on the builder page.
* `hide-publish` (optional): Hides the publish button on the builder page.
* `hide-save` (optional): Hides the save button on the builder page.
* `show-share` (optional): Shows the share button on the builder page.
* `language` (optional): The default language to use for the builder interface. It will not override the language set by a user in the builder.
* `translation-languages` (optional): Whitelist of comma-separated languages that can be used for translations in the builder. If not provided, [all languages](ui-builder-interface.md#list-of-supported-languages) are allowed.

### Step 4: Listen to Builder Events

The builder emits the following events:

* `change`: Emitted when the questionnaire is modified, triggered on every change or when the save or publish button is clicked. `event.detail` contains the modified questionnaire as a JSON object.
* `ready`: Emitted when the Aidbox Form Builder is loaded and ready to be used.
* `select`: Emitted when an item is selected in the item list. `event.detail` contains the selected item as a JSON object.

By following these steps, you can seamlessly embed the Aidbox Form Builder into your application, providing a powerful tool for form creation and management directly within your user interface.
