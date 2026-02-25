---
description: Aidbox SDC module resources for Structured Data Capture and FHIR Questionnaire management.
---

# SDC Module Resources

Resources for configuration and management Aidbox SDC module.

 ## OpenEHRTemplate

Template for SDC print functionality using OpenEHR format.

```fhir-structure
[ {
  "path" : "content",
  "name" : "content",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Template for SDC print"
} ]
```


## QuestionnaireTheme

Theme styling for SDC questionnaire forms including colors, fonts, and component styles.

```fhir-structure
[ {
  "path" : "theme-name",
  "name" : "theme-name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Theme Title"
}, {
  "path" : "language",
  "name" : "language",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Theme Language"
}, {
  "path" : "design-system",
  "name" : "design-system",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Design System used in form components \n\n**Allowed values**: `aidbox-desktop` | `aidbox-mobile` | `NHS`"
}, {
  "path" : "main-color",
  "name" : "main-color",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Main accent color (RGB hex)"
}, {
  "path" : "font-family",
  "name" : "font-family",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Font-Family \n\n**Allowed values**: `Product Sans` | `Metropolis` | `Inter`"
}, {
  "path" : "base-font-size",
  "name" : "base-font-size",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Minimal text size (px)"
}, {
  "path" : "brand-image",
  "name" : "brand-image",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Brand images"
}, {
  "path" : "brand-image.top-right",
  "name" : "top-right",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "brand-image.top-right.url",
  "name" : "url",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Web link to top-right image"
}, {
  "path" : "brand-image.bottom-left",
  "name" : "bottom-left",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "brand-image.bottom-left.url",
  "name" : "url",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Web link to bottom-left image"
}, {
  "path" : "background",
  "name" : "background",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Background styles"
}, {
  "path" : "background.main-color",
  "name" : "main-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Main background color (RGB hex)"
}, {
  "path" : "background.form-color",
  "name" : "form-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for form (RGB hex)"
}, {
  "path" : "background.toolbar-color",
  "name" : "toolbar-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for toolbar that displays below the form (with submit button) (RGB hex)"
}, {
  "path" : "input",
  "name" : "input",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Input styles"
}, {
  "path" : "input.accent-color",
  "name" : "accent-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Main color for inputs (border color for text inputs, background for checkbox, etc.) (RGB hex)"
}, {
  "path" : "input.text-color",
  "name" : "text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text color (RGB hex)"
}, {
  "path" : "input.background-color",
  "name" : "background-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color (RGB hex)"
}, {
  "path" : "input.font-size",
  "name" : "font-size",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Input font size (px)"
}, {
  "path" : "input.hierarchy-padding",
  "name" : "hierarchy-padding",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Left padding showing hierarchy of nested items (rem)"
}, {
  "path" : "button",
  "name" : "button",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Button styles"
}, {
  "path" : "button.print-text",
  "name" : "print-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button._print-text",
  "name" : "_print-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "button.print-color",
  "name" : "print-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for Print button"
}, {
  "path" : "button.print-text-color",
  "name" : "print-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text color for Print button"
}, {
  "path" : "button.amend-text",
  "name" : "amend-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button._amend-text",
  "name" : "_amend-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "button.amend-color",
  "name" : "amend-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.amend-text-color",
  "name" : "amend-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button._redirect-text",
  "name" : "_redirect-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "button.redirect-color",
  "name" : "redirect-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for Save & Close button"
}, {
  "path" : "button.redirect-text",
  "name" : "redirect-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.redirect-text-color",
  "name" : "redirect-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text color for Save & Close button"
}, {
  "path" : "button.submit-color",
  "name" : "submit-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for Submit button"
}, {
  "path" : "button.submit-text-color",
  "name" : "submit-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text color for Submit button"
}, {
  "path" : "button.submit-text",
  "name" : "submit-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button._submit-text",
  "name" : "_submit-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "button.accent-color",
  "name" : "accent-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for filled buttons, border color for outlined buttons. Deprecated."
}, {
  "path" : "button.text-color",
  "name" : "text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## SDCAddendum

Addendum resource. Contains additional information about an SDCDocument or SDCWorkflow.

```fhir-structure
[ {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of addendum"
}, {
  "path" : "target",
  "name" : "target",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to target resource"
}, {
  "path" : "target.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "target.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "target.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to user who created the addendum"
}, {
  "path" : "user.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "user.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "user.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "date",
  "name" : "date",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Date of addendum creation"
} ]
```


## SDCConfig

Configuration resource for SDC system settings including language, theme, storage, form settings, and builder options.

```fhir-structure
[ {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Machine readable config name"
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human readable config description"
}, {
  "path" : "default",
  "name" : "default",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Sets config as default for system/tenant"
}, {
  "path" : "language",
  "name" : "language",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Default language for UI"
}, {
  "path" : "theme",
  "name" : "theme",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Default theme \n\n**Allowed references**: QuestionnaireTheme"
}, {
  "path" : "translations",
  "name" : "translations",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Custom translation strings for UI"
}, {
  "path" : "storage",
  "name" : "storage",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Storage configuration for attachments"
}, {
  "path" : "storage.account",
  "name" : "account",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Storage Account \n\n**Allowed references**: AwsAccount, GcpServiceAccount, AzureContainer"
}, {
  "path" : "storage.bucket",
  "name" : "bucket",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Storage bucket (required for GCP and S3)"
}, {
  "path" : "storage.storageAccount",
  "name" : "storageAccount",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure storage account name (for Azure Workload Identity)"
}, {
  "path" : "storage.container",
  "name" : "container",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure container name (for Azure Workload Identity)"
}, {
  "path" : "storage.store-absolute-url",
  "name" : "store-absolute-url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Store absolute url to QuestionnaireResponse attachments"
}, {
  "path" : "form-store",
  "name" : "form-store",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "FHIR Server settings for storing/getting Questionnaire"
}, {
  "path" : "form-store.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "FHIR Server that will be used for storing/getting Questionnaire (if not set - use Aidbox)"
}, {
  "path" : "form-store.headers",
  "name" : "headers",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Headers with credentials (optional)"
}, {
  "path" : "term-server",
  "name" : "term-server",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "term-server.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "FHIR Server that stores Terminology (ValueSet/expand$) (if not set - use Aidbox)"
}, {
  "path" : "term-server.headers",
  "name" : "headers",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Headers (with credentials) for accessing Service (optional)"
}, {
  "path" : "data-store",
  "name" : "data-store",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "data-store.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "FHIR Server for storing responses, populating data, and extraction (defaults to Aidbox)"
}, {
  "path" : "data-store.headers",
  "name" : "headers",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Headers (with credentials) for accessing Service (optional)"
}, {
  "path" : "builder",
  "name" : "builder",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "builder.custom-renderers",
  "name" : "custom-renderers",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Custom renderers that can be used to preview form in Form Builder"
}, {
  "path" : "builder.custom-renderers.name",
  "name" : "name",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Renderer name"
}, {
  "path" : "builder.custom-renderers.source",
  "name" : "source",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Web component source URL"
}, {
  "path" : "builder.custom-renderers.url",
  "name" : "url",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Web component URL"
}, {
  "path" : "builder.custom-renderers.title",
  "name" : "title",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title in Builder"
}, {
  "path" : "builder.custom-renderers.default",
  "name" : "default",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this is the default renderer for form preview"
}, {
  "path" : "builder.form-url-prefix",
  "name" : "form-url-prefix",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "URL prefix used in URL generation of new forms"
}, {
  "path" : "builder.translation-languages",
  "name" : "translation-languages",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of languages that will be used for translation"
}, {
  "path" : "builder.hide-back-button",
  "name" : "hide-back-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide back button in builder"
}, {
  "path" : "builder.hide-publish",
  "name" : "hide-publish",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide Publish button in builder"
}, {
  "path" : "builder.disable-publish",
  "name" : "disable-publish",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable publish button"
}, {
  "path" : "builder.hide-population",
  "name" : "hide-population",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide population tab in debug instruments"
}, {
  "path" : "builder.hide-extraction",
  "name" : "hide-extraction",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide extraction tab in debug instruments"
}, {
  "path" : "builder.hide-save",
  "name" : "hide-save",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide save button"
}, {
  "path" : "builder.collapse-debugger",
  "name" : "collapse-debugger",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Collapse debugger by default in builder"
}, {
  "path" : "builder.disable-submit-button",
  "name" : "disable-submit-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable submit button in the builder's form preview footer"
}, {
  "path" : "builder.hide-add-theme",
  "name" : "hide-add-theme",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide add theme button"
}, {
  "path" : "builder.disable-use-blank-form",
  "name" : "disable-use-blank-form",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable initializing the builder with a sample form when no form is provided"
}, {
  "path" : "builder.enable-share",
  "name" : "enable-share",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Make share button visible in the builder and enable accepting a serialized questionnaire from query param"
}, {
  "path" : "builder.hide-save-theme",
  "name" : "hide-save-theme",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide save theme button"
}, {
  "path" : "builder.disable-save",
  "name" : "disable-save",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable save button"
}, {
  "path" : "builder.disable-load-fhir-version",
  "name" : "disable-load-fhir-version",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Deprecated. Has no effect."
}, {
  "path" : "builder.hide-edit-theme",
  "name" : "hide-edit-theme",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide edit theme button"
}, {
  "path" : "builder.highlight-download-button",
  "name" : "highlight-download-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Highlight download button in the builder's debugger panel"
}, {
  "path" : "builder.layout",
  "name" : "layout",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Builder layout variant (default or v2)"
}, {
  "path" : "form",
  "name" : "form",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "form.redirect-on-submit",
  "name" : "redirect-on-submit",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI used on form sign/amend"
}, {
  "path" : "form.redirect-on-save",
  "name" : "redirect-on-save",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI used on form save/close button"
}, {
  "path" : "form.auto-save-interval",
  "name" : "auto-save-interval",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Form auto-save interval in milliseconds"
}, {
  "path" : "form.read-only",
  "name" : "read-only",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether the form should be read-only"
}, {
  "path" : "form.default-max-width",
  "name" : "default-max-width",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Default max width of the form"
}, {
  "path" : "form.app-name",
  "name" : "app-name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "App name that will be mentioned in AuditEvent logs"
}, {
  "path" : "form.hide-footer",
  "name" : "hide-footer",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide the footer in the renderer altogether"
}, {
  "path" : "form.hide-title",
  "name" : "hide-title",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide title in the form"
}, {
  "path" : "form.non-sticky-footer",
  "name" : "non-sticky-footer",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Non-sticky footer in the form"
}, {
  "path" : "form.hide-language-selector",
  "name" : "hide-language-selector",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide language selector in the form"
}, {
  "path" : "form.hide-print-button",
  "name" : "hide-print-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide print button from the form"
}, {
  "path" : "form.enable-amend-button",
  "name" : "enable-amend-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Enable amend button"
}, {
  "path" : "form.enable-save-button",
  "name" : "enable-save-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Enable save button"
}, {
  "path" : "form.allow-repopulate",
  "name" : "allow-repopulate",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Allow repopulate for all forms"
}, {
  "path" : "form.hide-download-attachment-button",
  "name" : "hide-download-attachment-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide download button in attachment widget"
} ]
```


## SDCDocument

Questionnaire document resource. Stores captured form responses with status tracking, patient and encounter references.

```fhir-structure
[ {
  "path" : "based-on",
  "name" : "based-on",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Request that led to creation of this document"
}, {
  "path" : "based-on.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "based-on.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "based-on.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "author",
  "name" : "author",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the user who created the document"
}, {
  "path" : "author.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "author.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "author.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "form",
  "name" : "form",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "form.form",
  "name" : "form",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Full name of the form used to capture the document"
}, {
  "path" : "form.version",
  "name" : "version",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Form version used to capture the document"
}, {
  "path" : "encounter",
  "name" : "encounter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the associated encounter"
}, {
  "path" : "encounter.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "encounter.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "encounter.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "unit-system",
  "name" : "unit-system",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unit system used in this document at launch"
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the document \n\n**Allowed values**: `draft` | `in-progress` | `canceled` | `completed` | `in-amendment` | `amended`"
}, {
  "path" : "source",
  "name" : "source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "The person who answered the questions"
}, {
  "path" : "source.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "source.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "source.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name and version of the form used to capture the document"
}, {
  "path" : "patient",
  "name" : "patient",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "The patient who is the subject of the document"
}, {
  "path" : "patient.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "patient.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "patient.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## SDCFormMetadata

Form metadata with custom properties.

```fhir-structure
[ {
  "path" : "properties",
  "name" : "properties",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Custom properties for the form."
} ]
```


## SDCFormVersion

Form metadata for dynamic updates with incremental versioning and hash.

```fhir-structure
[ {
  "path" : "form",
  "name" : "form",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Form identifier"
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Incremental number of form version."
}, {
  "path" : "hash",
  "name" : "hash",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Hash of snapshot with injected document/layout/rules"
}, {
  "path" : "snapshot",
  "name" : "snapshot",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Serialized form snapshot"
} ]
```


## SDCPrintTemplate

Template content for SDC print functionality.

```fhir-structure
[ {
  "path" : "content",
  "name" : "content",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Template for SDC print"
} ]
```


## SDCWorkflow

Workflow definition with status, params, and context for SDC forms.

```fhir-structure
[ {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Workflow parameters schema definition"
}, {
  "path" : "cancel-reason",
  "name" : "cancel-reason",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Reason why the workflow was canceled"
}, {
  "path" : "items",
  "name" : "items",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Workflow items"
}, {
  "path" : "workflow",
  "name" : "workflow",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Workflow symbolic name"
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Workflow lifecycle status. Change via RPC: cancel-wf, complete-wf, cancel-task, complete-task. \n\n**Allowed values**: `new` | `in-progress` | `canceled` | `completed` | `in-amendment` | `amended`"
}, {
  "path" : "order",
  "name" : "order",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Order of items (array of item keys)"
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title of the workflow"
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Workflow version"
}, {
  "path" : "ctx",
  "name" : "ctx",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Workflow context, also accessible from tasks"
}, {
  "path" : "ctx.encounter",
  "name" : "encounter",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "ctx.encounter.id",
  "name" : "id",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.encounter.resourceType",
  "name" : "resourceType",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.encounter.display",
  "name" : "display",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.patient",
  "name" : "patient",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "ctx.patient.id",
  "name" : "id",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.patient.resourceType",
  "name" : "resourceType",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.patient.display",
  "name" : "display",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.user",
  "name" : "user",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "ctx.user.id",
  "name" : "id",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.user.resourceType",
  "name" : "resourceType",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "ctx.user.display",
  "name" : "display",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## SDCWorkflowVersion

Snapshotted workflow template with incremental versioning.

```fhir-structure
[ {
  "path" : "workflow",
  "name" : "workflow",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Workflow identifier."
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Incremental number of workflow version."
}, {
  "path" : "hash",
  "name" : "hash",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Hash of snapshot with injected document/layout/rules"
}, {
  "path" : "snapshot",
  "name" : "snapshot",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Serialized workflow snapshot."
} ]
```

