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
* `config` (optional): The [configuration](configuration.md) provided as a JSON string.
* `theme` (optional): The theme provided as a JSON string (see the [Theme Configuration](embedding-renderer.md#theme-configuration) section for more details).

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
* `theme` (optional): The theme provided as a JSON string (see the [Theme Configuration](embedding-renderer.md#theme-configuration) section for more details).

### Step 4: Listen to Renderer Events

The renderer emits the following events:

* `change`: Triggered when the questionnaire response is updated, either during auto-save or when the submit button is clicked. The `event.detail` contains the updated questionnaire response as a JSON object.
* `ready`: Fired when the Aidbox Forms Renderer has fully loaded and is ready for use.

By following these steps, you can effortlessly integrate the Aidbox Form Renderer into your application, offering a robust solution for form creation and management directly within your user interface.

## Theme Configuration

Theme configuration allows you to customize the appearance of the Aidbox Form Renderer. You can define the theme as a JSON object and pass it to the renderer using the `theme` attribute. The theme object can include the following properties:

```json5
{
  "base-font-size": "16px", // Base font size for the theme
  "font-family": "Arial, sans-serif", // Font family used in the theme
  "main-color": "#3498db", // Primary color of the theme
  "background": {
    "form-color": "#ffffff", // Background color of forms
    "main-color": "#f0f0f0", // Background color of the main page
    "toolbar-color": "#2c3e50" // Background color of the toolbar
  },
  "brand-image": {
    "bottom-left": "https://example.com/img-bottom-left.png", // Link to the image displayed at the bottom-left corner
    "top-right": "https://example.com/img-top-right.png" // Link to the image displayed at the top-right corner
  },
  "input": {
    "accent-color": "#2980b9", // Accent color for input elements like focus states or borders
    "background-color": "#ecf0f1", // Background color for input fields
    "font-size": "14px", // Font size for input text
    "text-color": "#2c3e50" // Text color inside input fields
  },
  "button": {
    "accent-color": "#27ae60", // General accent color for buttons
    "amend-color": "#f39c12", // Background color for the "Amend" button
    "amend-text-color": "#ffffff", // Text color for the "Amend" button
    "amend-text": "Amend", // Label text for the "Amend" button
    "print-color": "#8e44ad", // Background color for the "Print" button
    "print-text-color": "#ffffff", // Text color for the "Print" button
    "print-text": "Print", // Label text for the "Print" button
    "redirect-color": "#c0392b", // Background color for the "Close" button
    "redirect-text-color": "#ffffff", // Text color for the "Close" button
    "redirect-text": "Close", // Label text for the "Close" button
    "submit-text": "Submit", // Label text for the "Submit" button
    "text-color": "#ffffff" // General text color for all buttons
  }
}
```

All of these properties are optional, and you can customize the theme to suit your application's design. By providing a theme object, you can create a consistent and branded experience for users interacting with forms in your application.
