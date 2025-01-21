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
* `delegate-alerts` (optional): When set the web-component emits `alert` events with the alert objects as the event detail, instead of showing them in the UI.
* `config` (optional): The [configuration](configuration.md) provided as a JSON string.
* `language` (optional): The default language to use for the builder interface. It will not override the language set by a user in the builder.

{% hint style="warning" %}
Deprecated: Pass `language` as part of the `config` attribute value.
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
* `delegate-alerts` (optional): When set the web-component emits `alert` events with the alert objects as the event detail, instead of showing them in the UI.
* `show-share` (optional): Shows the share button on the builder page.
* `config` (optional): The [configuration](configuration.md) provided as a JSON string.
* `language` (optional): The default language to use for the builder interface. It will not override the language set by a user in the builder.

{% hint style="warning" %}
Deprecated: Pass `language` as part of the `config` attribute value.
{% endhint %}

* `translation-languages` (optional): Whitelist of comma-separated languages that can be used for translations in the builder. If not provided, [all languages](ui-builder-interface.md#list-of-supported-languages) are allowed.

{% hint style="warning" %}
Deprecated: Pass `translation-languages` as part of the `config` attribute value.
{% endhint %}


### Step 4: Listen to Builder Events

The builder emits the following events:

* `change`: Emitted when the questionnaire is modified, triggered on every change or when the save or publish button is clicked. `event.detail` contains the modified questionnaire as a JSON object.
* `back`: Emitted when back button (near form title) is clicked
* `ready`: Emitted when the Aidbox Form Builder is loaded and ready to be used.
* `select`: Emitted when an item is selected in the item list. `event.detail` contains the selected item as a JSON object.

By following these steps, you can seamlessly embed the Aidbox Form Builder into your application, providing a powerful tool for form creation and management directly within your user interface.

## Request Interception

In the builder, you can intercept requests made to the Aidbox server [endpoints](endpoints.md). This feature is useful for debugging or implementing custom behavior, such as redirecting requests to a different server, appending authentication headers, or modifying requests.

To enable request interception, set the `enable-fetch-proxy` attribute and pass an interception function as `fetch` property (not an attribute) to the DOM element.

The interception function must follow the same signature as the standard [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Window/fetch) function, with the following exceptions:
1.	The function can return null or undefined to bypass the interception and allow the builder to handle the request using the standard fetch.
2.	The [init object](https://developer.mozilla.org/en-US/docs/Web/API/RequestInit) (the second argument) may include an additional tag property. This tag is a string representing the name of one of the [endpoints](endpoints.md), allowing you to differentiate between requests without relying on the URL or HTTP method, which may be subject to future changes.

Below is an example of how to intercept requests in the builder to log them to the console:

```html
<aidbox-form-builder
  id="aidbox-form-builder"
  enable-fetch-proxy
/>

<script>
    const builder = document.getElementById('aidbox-form-builder');
    
    builder.fetch = async (url, init) => {
        console.log('Intercepted request', url, init);
        const response = await fetch(url, init);
        
        const cloned = response.clone();
        console.log('Intercepted response', response.status, 
          response.headers.get('content-type') === 'application/json' 
            ? await cloned.json() 
            : await cloned.text());
        
        return response;
    };
</script>
```

Below is an example of how to intercept requests in the builder to attach authorization header and re-route requests to a different endpoint:

```html

<aidbox-form-builder
  id="aidbox-form-builder"
  enable-fetch-proxy
/>

<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.fetch = async (url, init) => {
    const headers = new Headers(init.headers);
    const accessToken = 'YOUR_ACCESS_TOKEN'
    headers.set('Authorization', `Bearer ${accessToken}`);
    return fetch(`/protected${url}`, {...init, headers});
  };
</script>
```

Below is example of how to intercept read/write questionnaire request in the builder to make it work with your custom questionnaire storage:

```html
<aidbox-form-builder
  form-id="local-questionnaire"
  id="aidbox-form-builder"
  enable-fetch-proxy
/>

<script>
  const builder = document.getElementById('aidbox-form-builder');

  builder.fetch = async (url, init) => {
    if (init.tag === 'get-questionnaire') {
      const id = url.split('/').pop();
      
      if (id === 'local-questionnaire') {
        let localQuestionnaire = {
          "resourceType": "Questionnaire",
          "id": "local-questionnaire",
        };
        
        try {
            localQuestionnaire = JSON.parse(localStorage.getItem('local-questionnaire'));
        } catch (e) {
            // ignore if local questionnaire is not found or invalid
        }
        
        return new Response(JSON.stringify(localQuestionnaire), { status: 200 });
      }
    } else if (init.tag === 'save-questionnaire') {
      const questionnaire = JSON.parse(init.body);
      
      if (questionnaire.id === 'local-questionnaire') {
        localStorage.setItem('local-questionnaire', JSON.stringify(questionnaire));
        return new Response(JSON.stringify(questionnaire), { status: 200 });
      }
    }
    
    // let the builder handle the request
    return null;
  };
  
</script>
```
