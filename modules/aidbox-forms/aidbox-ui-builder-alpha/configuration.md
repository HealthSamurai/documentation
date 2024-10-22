---
description: >-
  FHIR-compliant settings for form building/rendering. Define language, themes,
  URL prefixes, redirects, and audit logs. Supports system-wide and override
  configurations.
---

# Configuration

## Overview

The Aidbox Forms implements a FHIR-compliant Configuration resource that enables fine-grained control over form builder and renderer behavior. This resource adheres to CRUD operations via the standard FHIR API endpoints. The Configuration resource schema encompasses parameters such as:

* User Interface Settings: Default language and theme preferences.
* Form Builder Configuration: Settings that control the behavior and appearance of the form creation interface.
* Form Renderer Configuration: Parameters that define how forms are displayed and interacted with by end-users.
* Navigation and Workflow Controls: Settings for redirects, button visibility, and form state (e.g., read-only mode).
* Logging and Auditing: Configurations related to event tracking and application identification.

A global Configuration resource can be instantiated to serve as the system-wide default, applying to all Questionnaire resources unless explicitly overridden. The system supports configuration inheritance and overriding. When generating a shared form link, developers can specify a custom Configuration resource reference. This allows for context-specific rendering behavior, ensuring that the form presentation adheres to the designated configuration parameters. This architecture facilitates flexible, hierarchical configuration management, enabling both system-wide defaults and use-case specific customizations within the Aidbox Forms ecosystem.

## Configuration Resource Structure

* `resourceType`: The type of the resource. Must be `SDCConfig`.
* `id`: The unique identifier for the configuration resource.
* `name`: The machine-readable name of the configuration resource.
* `description`: The human-readable description of the configuration resource.
* `default`: A boolean value that specifies whether the configuration is the default for the system or tenant.
* `language`: The default language for the UI.
* `theme`: An inlined copy of or reference to [QuestionnaireTheme](#theme) object.
* `builder`: Configuration settings for the form builder.
  * `form-url-prefix`: The URL prefix used in URL generation for new forms.
  * `hide-back-button`: A boolean value that specifies whether the back button should be hidden.
  * `translation-languages`: Array of languages that can be used for translations in the builder. If not provided, [all languages](ui-builder-interface.md#list-of-supported-languages) are allowed.
* `form`: Configuration settings for the form.
  * `redirect-on-submit`: The redirect URI used when the form is submitted or amended.
  * `redirect-on-save`: The redirect URI used when the form is saved or closed.
  * `read-only`: A boolean value that specifies whether the form should be read-only.
  * `app-name`: The name of the app that will be mentioned in the AuditEvent logs.
  * `default-max-width`: The default maximum width of the form. It can accept values in all CSS units (e.g., `px`, `rem`, `%`). Default is `960px`. This value will take effect only if the questionnaire does have [Max Form Width](form-creation/form-settings.md#appearance-settings) set.

## Configuration Resource Example

```json
{
  "resourceType": "SDCConfig",
  "id": "example-config",
  "name": "Example Configuration",
  "description": "An example configuration resource",
  "default": true,
  "language": "en",
  "theme": {
    "id": "example-theme",
    "resourceType": "QuestionnaireTheme"
  },
  "builder": {
    "form-url-prefix": "https://example.com/forms/",
    "hide-back-button": false
  },
  "form": {
    "redirect-on-submit": "https://example.com/submit",
    "redirect-on-save": "https://example.com/save",
    "read-only": false,
    "app-name": "Example App"
  }
}
```

## Configuration Resource Operations

### Create Configuration Resource

To create a configuration resource, send a `POST` request to the `/SDCConfig` endpoint with the configuration resource in the request body.

```http
POST /SDCConfig
Content-Type: application/json

{
  "resourceType": "SDCConfig",
  "id": "example-config",
  "name": "Example Configuration",
  "description": "An example configuration resource",
  "default": true,
  "language": "en",
  "theme": {
    "id": "027cf3dd-dca7-4a38-b805-3e76c1886ac7",
    "resourceType": "QuestionnaireTheme"
  },
  "builder": {
    "form-url-prefix": "https://example.com/forms/",
    "hide-back-button": false
  },
  "form": {
    "redirect-on-submit": "https://example.com/submit",
    "redirect-on-save": "https://example.com/save",
    "read-only": false,
    "app-name": "Example App"
  }
}
```

### Read Configuration Resource

To read a configuration resource, send a `GET` request to the `/SDCConfig/:id` endpoint, where `:id` is the unique identifier of the configuration resource.

```http
GET /SDCConfig/example-config
```

### Update Configuration Resource

To update a configuration resource, send a `PUT` request to the `/SDCConfig/:id` endpoint with the updated configuration resource in the request body.

```http
PUT /SDCConfig/example-config
Content-Type: application/json

{
  "resourceType": "SDCConfig",
  "id": "example-config",
  "name": "Updated Configuration",
  "description": "An updated configuration resource",
  "default": true,
  "language": "en",
  "theme": {
    "id": "027cf3dd-dca7-4a38-b805-3e76c1886ac7",
    "resourceType": "QuestionnaireTheme"
  },
  "builder": {
    "form-url-prefix": "https://example.com/forms/",
    "hide-back-button": false
  },
  "form": {
    "redirect-on-submit": "https://example.com/submit",
    "redirect-on-save": "https://example.com/save",
    "read-only": false,
    "app-name": "Example App"
  }
}
```

### Delete Configuration Resource

To delete a configuration resource, send a `DELETE` request to the `/SDCConfig/:id` endpoint, where `:id` is the unique identifier of the configuration resource.

```http
DELETE /SDCConfig/example-config
```

## List Configuration Resources

To list all configuration resources, send a `GET` request to the `/SDCConfig` endpoint.

```http
GET /SDCConfig
```

## Get Default Configuration Resource

To get the default configuration resource, send a `GET` request to the `/$sdc-config` endpoint.

```http
GET /$sdc-config
```

# Theme

`QuestionnaireTheme` resource allows you to customize the appearance of the Aidbox Form Renderer. The theme resource can include the following properties:

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
