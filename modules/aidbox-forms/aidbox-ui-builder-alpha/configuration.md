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

```
(def SDCConfig
  {:attrs {:name        {:desc "Machine readable config name" :type "string" :isRequired true},
           :description {:desc "Human readable config description" :type "string"},
           :default     {:desc "Set's config as default for system/tenant" :type "boolean"},
           :language    {:desc "Default language for UI" :type "string"},
           :theme       {:desc   "Default theme"
                         :type   "Reference",
                         :refers ["QuestionnaireTheme"]},
           :term-server {:attrs {:endpoint {:desc "FHIR Server that stores Terminology (ValueSet/expand$) (if not set - use Aidbox)"
                                            :type "url"}
                                 :headers {:desc "Headers (with credentials) for accessing Service (optional)"
                                           :isOpen true
                                           :type "Map"}}}
           :data-store  {:attrs {:endpoint {:desc "FHIR Server that will be used for storing/getting reponses, populate data from and extract to (if not set - use Aidbox)"
                                            :type "url"}
                                 :headers {:desc "Headers (with credentials) for accessing Service (optional)"
                                           :isOpen true
                                           :type "Map"}}}
           :form-store  {:attrs {:endpoint {:desc "FHIR Server that will be used for storing/getting Questionnaire (if not set - use Aidbox)"
                                            :type "url"}
                                 :headers {:desc "Headers with credentials (optional)"
                                           :isOpen true
                                           :type "Map"}}}
           :builder     {:attrs {:form-url-prefix  {:desc "URL prefix that used in url generation of new forms"
                                                    :type "url"}
                                 :hide-back-button {:desc "Redirect URI that will be used on form submit/amend button click"
                                                    :type "boolean"}}},
           :form        {:attrs {:redirect-on-submit {:desc "Redirect URI that used on form sign/amend"
                                                      :type "url"},
                                 :redirect-on-save   {:desc "Redirect URI that used on form save/close button"
                                                      :type "url"},
                                 :read-only          {:desc "Should form be read-only"
                                                      :type "boolean"},
                                 :app-name           {:desc "App name that will be mentioned in AuditEvent logs"
                                                      :type "string"}}}}})
```

* `resourceType`: The type of the resource. Must be `SDCConfig`.
* `id`: The unique identifier for the configuration resource.
* `name`: The machine-readable name of the configuration resource.
* `description`: The human-readable description of the configuration resource.
* `default`: A boolean value that specifies whether the configuration is the default for the system or tenant.
* `language`: The default language for the UI.
* `theme`: A reference to the default theme for the UI.
* `builder`: Configuration settings for the form builder.
  * `form-url-prefix`: The URL prefix used in URL generation for new forms.
  * `hide-back-button`: A boolean value that specifies whether the back button should be hidden.
* `form`: Configuration settings for the form.
  * `redirect-on-submit`: The redirect URI used when the form is submitted or amended.
  * `redirect-on-save`: The redirect URI used when the form is saved or closed.
  * `read-only`: A boolean value that specifies whether the form should be read-only.
  * `app-name`: The name of the app that will be mentioned in the AuditEvent logs.

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
