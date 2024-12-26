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
* `translations`: Object containing [custom translations](#translations) strings for the UI.
* `theme`: An inlined copy of or reference to [QuestionnaireTheme](#theme) object.
* `storage`: Storage configuration for attachments types
  * `account`: Reference to [storage resource](https://docs.aidbox.app/storage-1/s3-compatible-storages)
    * `id`: id
    * `resourceType`: One of (GcpServiceAccount, AwsAccount, AzureContainer)
  * `bucket`: Bucket to store attachment files (required for GcpServiceAccount and AwsAccount)
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
  "storage": {
    "account": {
      "id": "aws-test-account",
      "resourceType": "AwsAccount"
    },
    "bucket": "attachments-bucket"
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

# Translations

The translations object in the Configuration resource allows defining custom text for the Aidbox Form Builder and Renderer.
It uses a structured, hierarchical format where each key corresponds to a feature or component, and the leaf nodes specify locale-based translations.
This structure supports multiple languages and regional variations by enabling you to specify locale codes such as en for English, fr for French, or more granular options like en-US for American English and fr-CA for Canadian French.

The example provided below represents a comprehensive translations object for English, covering all possible keys. You can extend this object by adding translations for additional languages.
If a specific language (e.g., en for English or any other) is omitted, the Aidbox Form will use its standard default translations for that language.

```json
{
  "widget": {
    "drawing-pad": {
      "close": { "en": "Close annotation editor" },
      "alt": { "en": "Annotation pad background" },
      "clear": { "en": "Clear canvas" },
      "open": { "en": "Edit annotation" }
    },
    "speech-to-text": {
      "tooltip": {
        "recording": { "en": "Recording your speech" },
        "unsupported": { "en": "Speech to text is disabled because neither GPU is available nor cross-origin isolation is enabled" },
        "initializing": { "en": "Initializing speech to text engine" },
        "record": { "en": "Click to record your speech" },
        "converting": { "en": "Converting your speech to text" }
      },
      "record": { "en": "Record" }
    },
    "choice": {
      "no-options": { "en": "No options" },
      "select": { "en": "Select" },
      "search": { "en": "Search" },
      "loading-more": { "en": "Loading options..." },
      "load-more": { "en": "Load more options" }
    },
    "file": {
      "browse": { "en": "Browse" },
      "preview": {
        "title": { "en": "Preview" },
        "alt": { "en": "Unable to show image" }
      },
      "placeholder": { "en": "Choose file" }
    },
    "signature": {
      "alt": { "en": "Sign here" },
      "clear": { "en": "Clear signature" },
      "open": { "en": "Expand sign pad" },
      "close": { "en": "Close sign pad" }
    },
    "page": {
      "current-page": { "en": "Page %d of %d" },
      "next-page": { "en": "Next Page" },
      "previous-page": { "en": "Previous Page" }
    },
    "repeats": {
      "add": { "en": "Add item" }
    }
  },
  "form": {
    "status": {
      "ready": { "en": "Ready to go" },
      "saved-time": { "en": "Saved " },
      "offline-ready": { "en": "Changes will be saved locally" },
      "submitting": { "en": "Submitting changes..." },
      "offline-saved": { "en": "Changes saved locally" },
      "saved": { "en": "Changes saved" },
      "offline": { "en": "Offline" },
      "error": {
        "validation": { "en": "Validation error" },
        "other": { "en": "Not submitted yet" }
      },
      "amended": { "en": "Form amended" },
      "saving": { "en": "Saving changes..." },
      "submitted": { "en": "Form submitted" },
      "amending": { "en": "Amending changes..." }
    },
    "button": {
      "amend": { "en": "Amend" },
      "offline-amend": { "en": "You are offline and cannot amend" },
      "repopulate": { "en": "Repopulate" },
      "offline-submit": { "en": "You are offline and cannot submit" },
      "print": { "en": "Print" },
      "saved": { "en": "Saved" },
      "submit": { "en": "Submit" },
      "save-and-close": { "en": "Save & Close" }
    },
    "outline": {
      "title": { "en": "Contents" }
    }
  },
  "error": {
    "validation": {
      "invalid-answer": { "en": "Invalid answer" },
      "value-less-than-min": { "en": "Value can't be less than %d" },
      "file-too-large": { "en": "File is too large (allowed max size: %s)" },
      "type-mismatch": { "en": "Expected answer of type: '%s', got: '%s'" },
      "signature-author-required": { "en": "Signature author is required" },
      "signature-required": { "en": "Signature is required" },
      "answer-required": { "en": "Answer is required" },
      "value-greater-than-max": { "en": "Value can't be greater than %d" }
    },
    "invalid-jwt": { "en": "Invalid JWT" },
    "fhir": {
      "version-conversion": { "en": "Unable to convert the provided resource to the required format." },
      "resource-not-questionnaire": { "en": "The provided resource does not match the format of a questionnaire." },
      "empty-resource": { "en": "The provided resource contains no data." },
      "version-incompatible": { "en": "The questionnaire contains properties from multiple incompatible FHIR versions." }
    }
  },
  "alert": {
    "questionnaire-response-not-found": {
      "message": { "en": "Cannot load the questionnaire response" },
      "details": { "en": "Response with id %s does not exists." }
    },
    "provide-url": {
      "message": { "en": "Please provide a URL for your link." }
    },
    "questionnaire-response-cannot-repopulate": {
      "message": { "en": "Repopulate process encountered errors" }
    },
    "questionnaire-response-cannot-save": {
      "message": { "en": "Cannot save QuestionnaireResponse" }
    },
    "file-too-large": {
      "message": { "en": "Max allowed file size is: %d" }
    },
    "network": {
      "message": { "en": "Network error" },
      "close": { "en": "Close" },
      "details": { "en": "Network issues detected, please try again later." },
      "refresh": { "en": "Refresh" }
    },
    "questionnaire-not-found": {
      "message": { "en": "Cannot load the questionnaire" },
      "details": { "en": "Questionnaire with url '%' does not exists" }
    },
    "questionnaire-response-save-issues": {
      "message": { "en": "Warnings" }
    },
    "unauthorized": {
      "message": { "en": "Authorization required" },
      "details": { "en": "Your token is invalid or expired" }
    }
  },
  "offline": {
    "new-version-available": { "en": "New version of this page is available. Please refresh the page to get the latest version." },
    "reloading": { "en": "Reloading..." },
    "reload": { "en": "Reload" }
  },
  "warning": {
    "fhir": {
      "version-indeterminate": { "en": "Cannot determine version; the questionnaire contains fields common to multiple known versions." }
    }
  }
}
```
