# SDC Module Resources

Resources for configuration and management Aidbox SDC module.

 ## OpenEHRTemplate

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

```fhir-structure
[ {
  "path" : "background",
  "name" : "background",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Background styles"
}, {
  "path" : "background.main-color",
  "name" : "main-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Main background color (RBG hex)"
}, {
  "path" : "background.form-color",
  "name" : "form-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for form (RBG hex)"
}, {
  "path" : "background.toolbar-color",
  "name" : "toolbar-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for toolbar that displays below the form (with submit button) (RGB hex)"
}, {
  "path" : "base-font-size",
  "name" : "base-font-size",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Minimal text size (px)"
}, {
  "path" : "brand-image",
  "name" : "brand-image",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Brand images"
}, {
  "path" : "brand-image.top-right",
  "name" : "top-right",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
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
  "type" : "",
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
  "path" : "button",
  "name" : "button",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Button styles"
}, {
  "path" : "button.amend-text-color",
  "name" : "amend-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.print-color",
  "name" : "print-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for \"Print\" button"
}, {
  "path" : "button._amend-text",
  "name" : "_amend-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
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
  "path" : "button.redirect-color",
  "name" : "redirect-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for \"Save & Close\" button"
}, {
  "path" : "button._redirect-text",
  "name" : "_redirect-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "button.submit-text",
  "name" : "submit-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.print-text-color",
  "name" : "print-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text color for \"Print\" button"
}, {
  "path" : "button.print-text",
  "name" : "print-text",
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
  "desc" : "Text color for \"Save & Close\" button"
}, {
  "path" : "button._print-text",
  "name" : "_print-text",
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
  "path" : "button.text-color",
  "name" : "text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.submit-color",
  "name" : "submit-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for \"Submit\" button"
}, {
  "path" : "button.redirect-text",
  "name" : "redirect-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.amend-text",
  "name" : "amend-text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "button.submit-text-color",
  "name" : "submit-text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text color for \"Submit\" button"
}, {
  "path" : "button.accent-color",
  "name" : "accent-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Background color for filled buttons border color for outlined buttons (deprecated)"
}, {
  "path" : "font-family",
  "name" : "font-family",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Font-Family \n\n**Allowed values**: `Product Sans` | `Metropolis` | `Inter`"
}, {
  "path" : "input",
  "name" : "input",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Input styles"
}, {
  "path" : "input.accent-color",
  "name" : "accent-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Main color for inputs (border color for text inputs background for checkbox etc in RGB hex)"
}, {
  "path" : "input.text-color",
  "name" : "text-color",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "text color (RGB hex)"
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
  "type" : "number",
  "desc" : "Input font size (px)"
}, {
  "path" : "language",
  "name" : "language",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Theme Language"
}, {
  "path" : "main-color",
  "name" : "main-color",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Main Accent color (RBG hex)"
}, {
  "path" : "theme-name",
  "name" : "theme-name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Theme Title"
} ]
```


## SDCAddendum

Addednum Resource. Contains additional information abount SDCDocument/SDCWorkflow

```fhir-structure
[ {
  "path" : "date",
  "name" : "date",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "dateTime",
  "desc" : ""
}, {
  "path" : "target",
  "name" : "target",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
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
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
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
} ]
```


## SDCConfig

```fhir-structure
[ {
  "path" : "builder",
  "name" : "builder",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "builder.hide-extraction",
  "name" : "hide-extraction",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide extraction tab in debug instruments"
}, {
  "path" : "builder.highlight-download-button",
  "name" : "highlight-download-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Highlight download button in the builder's debugger panel"
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
  "path" : "builder.hide-save",
  "name" : "hide-save",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide save button"
}, {
  "path" : "builder.hide-publish",
  "name" : "hide-publish",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide Publish button in builder"
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
  "path" : "builder.hide-population",
  "name" : "hide-population",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide population tab in debug instruments"
}, {
  "path" : "builder.custom-renderers",
  "name" : "custom-renderers",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "Map",
  "desc" : "Custom renderers that can be used to preview form in Form Builder"
}, {
  "path" : "builder.custom-renderers.name",
  "name" : "name",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Webcomponent tag name"
}, {
  "path" : "builder.custom-renderers.source",
  "name" : "source",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Webcomponent source url"
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
  "desc" : "If this default renderer for form preview"
}, {
  "path" : "builder.hide-save-theme",
  "name" : "hide-save-theme",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide save theme"
}, {
  "path" : "builder.disable-save",
  "name" : "disable-save",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable save"
}, {
  "path" : "builder.disable-publish",
  "name" : "disable-publish",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable publish button"
}, {
  "path" : "builder.form-url-prefix",
  "name" : "form-url-prefix",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "URL prefix that used in url generation of new forms"
}, {
  "path" : "builder.disable-load-fhir-version",
  "name" : "disable-load-fhir-version",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Disable loading of FHIR version from the server. Deprecated: not used anymore and has no effect"
}, {
  "path" : "builder.hide-edit-theme",
  "name" : "hide-edit-theme",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide edit theme button"
}, {
  "path" : "data-store",
  "name" : "data-store",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "data-store.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "FHIR Server that will be used for storing/getting reponses populate data from and extract to (if not set - use Aidbox)"
}, {
  "path" : "data-store.headers",
  "name" : "headers",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Headers (with credentials) for accessing Service (optional)"
}, {
  "path" : "default",
  "name" : "default",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Set's config as default for system/tenant"
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human readable config description"
}, {
  "path" : "form",
  "name" : "form",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "form.app-name",
  "name" : "app-name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "App name that will be mentioned in AuditEvent logs"
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
  "path" : "form.read-only",
  "name" : "read-only",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Should form be read-only"
}, {
  "path" : "form.hide-language-selector",
  "name" : "hide-language-selector",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide language selector in the builder"
}, {
  "path" : "form.allow-repopulate",
  "name" : "allow-repopulate",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Allow repopulate for all forms"
}, {
  "path" : "form.hide-footer",
  "name" : "hide-footer",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide the footer in the renderer altogether"
}, {
  "path" : "form.default-max-width",
  "name" : "default-max-width",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Default max width of the form"
}, {
  "path" : "form.hide-download-attachment-button",
  "name" : "hide-download-attachment-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide download button in attachment widget"
}, {
  "path" : "form.non-sticky-footer",
  "name" : "non-sticky-footer",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Non-sticky footer in the form"
}, {
  "path" : "form.redirect-on-save",
  "name" : "redirect-on-save",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI that used on form save/close button"
}, {
  "path" : "form.redirect-on-submit",
  "name" : "redirect-on-submit",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI that used on form sign/amend"
}, {
  "path" : "form.enable-save-button",
  "name" : "enable-save-button",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Enable close button"
}, {
  "path" : "form.hide-title",
  "name" : "hide-title",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Hide title in the form"
}, {
  "path" : "form-store",
  "name" : "form-store",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
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
  "path" : "language",
  "name" : "language",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Default language for UI"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Machine readable config name"
}, {
  "path" : "storage",
  "name" : "storage",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Link to storage to store attachments"
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
  "path" : "term-server",
  "name" : "term-server",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
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
  "desc" : "Custom translations strings for UI"
} ]
```


## SDCDocument

Base schema for questionnaire(document) definition. Also a resource in DB - SDCDocument

```fhir-structure
[ {
  "path" : "author",
  "name" : "author",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Reference to user which create document"
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
  "path" : "based-on",
  "name" : "based-on",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Request led to creation of this Document"
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
  "path" : "encounter",
  "name" : "encounter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Reference to encounter"
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
  "path" : "form",
  "name" : "form",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "form.form",
  "name" : "form",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Full name of form, with which document was captured"
}, {
  "path" : "form.version",
  "name" : "version",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Used Form version"
}, {
  "path" : "patient",
  "name" : "patient",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "The subject of the question"
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
}, {
  "path" : "source",
  "name" : "source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
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
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the document  \n\n**Allowed values**: `draft` | `in-progress` | `canceled` | `completed` | `in-amendment` | `amended`"
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name/version of form, with which document was captured"
}, {
  "path" : "unit-system",
  "name" : "unit-system",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "What unit system was used in this document at launch"
} ]
```


## SDCFormMetadata

```fhir-structure
[ {
  "path" : "properties",
  "name" : "properties",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
} ]
```


## SDCFormVersion

Form Metadata that can be used for Dynamic updates

```fhir-structure
[ {
  "path" : "form",
  "name" : "form",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "hash",
  "name" : "hash",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Hash of snapshot with injected document/layout/rules"
}, {
  "path" : "snapshot",
  "name" : "snapshot",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Incremental number of form version."
} ]
```


## SDCPrintTemplate

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

```fhir-structure
[ {
  "path" : "cancel-reason",
  "name" : "cancel-reason",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Define why workfow is canceled"
}, {
  "path" : "ctx",
  "name" : "ctx",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Workflow context, which can be used from tasks as well"
}, {
  "path" : "ctx.encounter",
  "name" : "encounter",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
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
  "type" : "",
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
  "type" : "",
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
}, {
  "path" : "items",
  "name" : "items",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Workflow items"
}, {
  "path" : "order",
  "name" : "order",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Order of items (array of item keys)"
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Workflow params schema definition"
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of WF lifecycle. Should be changed via rpc: `cancel-wf`, `complete-wf` `cancel-task`, `complete-task` \n\n**Allowed values**: `new` | `in-progress` | `canceled` | `completed` | `in-amendment` | `amended`"
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
  "type" : "number",
  "desc" : "Workflow version"
}, {
  "path" : "workflow",
  "name" : "workflow",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Workflow symbolic name for storing in DB"
} ]
```


## SDCWorkflowVersion

Snapshotted workflow template with incremental versioning.

```fhir-structure
[ {
  "path" : "hash",
  "name" : "hash",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "number",
  "desc" : "Hash of snapshot with injected document/layout/rules"
}, {
  "path" : "snapshot",
  "name" : "snapshot",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "number",
  "desc" : "Incremental number of workflow version."
}, {
  "path" : "workflow",
  "name" : "workflow",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```

