# SDC Module Resources

## Overview

SDC module includes the following resource types:

- QuestionnaireTheme
- SDCConfig
- SDCPrintTemplate
- SDCDocument
- SDCFormMetadata
- SDCFormVersion
- SDCWorkflow
- SDCWorkflowVersion
- SDCAddendum

## QuestionnaireTheme

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">font-family</td><td width="70">0..1</td><td width="150">string</td><td>Font-Family</td></tr>
<tr class="top-element"><td width="290">brand-image</td><td width="70">0..1</td><td width="150"></td><td>Brand images</td></tr>
<tr class="nested-element"><td width="290">brand-image.top-right</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">brand-image.top-right.url</td><td width="70">0..1</td><td width="150">string</td><td>Web link to top-right image</td></tr>
<tr class="nested-element"><td width="290">brand-image.bottom-left</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">brand-image.bottom-left.url</td><td width="70">0..1</td><td width="150">string</td><td>Web link to bottom-left image</td></tr>
<tr class="top-element"><td width="290">base-font-size</td><td width="70">0..1</td><td width="150">number</td><td>Minimal text size (px)</td></tr>
<tr class="top-element"><td width="290">input</td><td width="70">0..1</td><td width="150"></td><td>Input styles</td></tr>
<tr class="nested-element"><td width="290">input.accent-color</td><td width="70">0..1</td><td width="150">string</td><td>Main color for inputs (border color for text inputs background for checkbox etc in RGB hex)</td></tr>
<tr class="nested-element"><td width="290">input.text-color</td><td width="70">0..1</td><td width="150">string</td><td>text color (RGB hex)</td></tr>
<tr class="nested-element"><td width="290">input.background-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color (RGB hex)</td></tr>
<tr class="nested-element"><td width="290">input.font-size</td><td width="70">0..1</td><td width="150">number</td><td>Input font size (px)</td></tr>
<tr class="top-element"><td width="290">language</td><td width="70">0..1</td><td width="150">code</td><td>Theme Language</td></tr>
<tr class="top-element"><td width="290">main-color</td><td width="70">0..1</td><td width="150">string</td><td>Main Accent color (RBG hex)</td></tr>
<tr class="top-element"><td width="290">background</td><td width="70">0..1</td><td width="150"></td><td>Background styles</td></tr>
<tr class="nested-element"><td width="290">background.main-color</td><td width="70">0..1</td><td width="150">string</td><td>Main background color (RBG hex)</td></tr>
<tr class="nested-element"><td width="290">background.form-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color for form (RBG hex)</td></tr>
<tr class="nested-element"><td width="290">background.toolbar-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color for toolbar that displays below the form (with submit button) (RGB hex)</td></tr>
<tr class="top-element"><td width="290">theme-name</td><td width="70">0..1</td><td width="150">string</td><td>Theme Title</td></tr>
<tr class="top-element"><td width="290">button</td><td width="70">0..1</td><td width="150"></td><td>Button styles</td></tr>
<tr class="nested-element"><td width="290">button.amend-text-color</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.print-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color for "Print" button</td></tr>
<tr class="nested-element"><td width="290">button._amend-text</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">button._submit-text</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">button.redirect-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color for "Save & Close" button</td></tr>
<tr class="nested-element"><td width="290">button._redirect-text</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">button.submit-text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.print-text-color</td><td width="70">0..1</td><td width="150">string</td><td>Text color for "Print" button</td></tr>
<tr class="nested-element"><td width="290">button.print-text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.redirect-text-color</td><td width="70">0..1</td><td width="150">string</td><td>Text color for "Save & Close" button</td></tr>
<tr class="nested-element"><td width="290">button._print-text</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">button.amend-color</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.text-color</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.submit-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color for "Submit" button</td></tr>
<tr class="nested-element"><td width="290">button.redirect-text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.amend-text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">button.submit-text-color</td><td width="70">0..1</td><td width="150">string</td><td>Text color for "Submit" button</td></tr>
<tr class="nested-element"><td width="290">button.accent-color</td><td width="70">0..1</td><td width="150">string</td><td>Background color for filled buttons border color for outlined buttons (deprecated)</td></tr></tbody>
</table>


## SDCConfig

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">form</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">form.app-name</td><td width="70">0..1</td><td width="150">string</td><td>App name that will be mentioned in AuditEvent logs</td></tr>
<tr class="nested-element"><td width="290">form.hide-print-button</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide print button from the form</td></tr>
<tr class="nested-element"><td width="290">form.enable-amend-button</td><td width="70">0..1</td><td width="150">boolean</td><td>Enable amend button</td></tr>
<tr class="nested-element"><td width="290">form.read-only</td><td width="70">0..1</td><td width="150">boolean</td><td>Should form be read-only</td></tr>
<tr class="nested-element"><td width="290">form.hide-language-selector</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide language selector in the builder</td></tr>
<tr class="nested-element"><td width="290">form.hide-footer</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide the footer in the renderer altogether</td></tr>
<tr class="nested-element"><td width="290">form.default-max-width</td><td width="70">0..1</td><td width="150">string</td><td>Default max width of the form</td></tr>
<tr class="nested-element"><td width="290">form.non-sticky-footer</td><td width="70">0..1</td><td width="150">boolean</td><td>Non-sticky footer in the form</td></tr>
<tr class="nested-element"><td width="290">form.redirect-on-save</td><td width="70">0..1</td><td width="150">url</td><td>Redirect URI that used on form save/close button</td></tr>
<tr class="nested-element"><td width="290">form.redirect-on-submit</td><td width="70">0..1</td><td width="150">url</td><td>Redirect URI that used on form sign/amend</td></tr>
<tr class="nested-element"><td width="290">form.enable-save-button</td><td width="70">0..1</td><td width="150">boolean</td><td>Enable close button</td></tr>
<tr class="top-element"><td width="290">translations</td><td width="70">0..1</td><td width="150">Object</td><td>Custom translations strings for UI</td></tr>
<tr class="top-element"><td width="290">storage</td><td width="70">0..1</td><td width="150"></td><td>Link to storage to store attachments</td></tr>
<tr class="nested-element"><td width="290">storage.account</td><td width="70">0..1</td><td width="150">Reference</td><td>Storage Account References: AwsAccount, GcpServiceAccount, AzureContainer</td></tr>
<tr class="nested-element"><td width="290">storage.bucket</td><td width="70">0..1</td><td width="150">string</td><td>Storage bucket (required for GCP and S3)</td></tr>
<tr class="top-element"><td width="290">form-store</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">form-store.endpoint</td><td width="70">0..1</td><td width="150">url</td><td>FHIR Server that will be used for storing/getting Questionnaire (if not set - use Aidbox)</td></tr>
<tr class="nested-element"><td width="290">form-store.headers</td><td width="70">0..1</td><td width="150">Object</td><td>Headers with credentials (optional)</td></tr>
<tr class="top-element"><td width="290">builder</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">builder.hide-extraction</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide extraction tab in debug instruments</td></tr>
<tr class="nested-element"><td width="290">builder.highlight-download-button</td><td width="70">0..1</td><td width="150">boolean</td><td>Highlight download button in the builder's debugger panel</td></tr>
<tr class="nested-element"><td width="290">builder.translation-languages</td><td width="70">0..*</td><td width="150">string</td><td>List of languages that will be used for translation</td></tr>
<tr class="nested-element"><td width="290">builder.hide-back-button</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide back button in builder</td></tr>
<tr class="nested-element"><td width="290">builder.hide-save</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide save button</td></tr>
<tr class="nested-element"><td width="290">builder.hide-publish</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide Publish button in builder</td></tr>
<tr class="nested-element"><td width="290">builder.collapse-debugger</td><td width="70">0..1</td><td width="150">boolean</td><td>Collapse debugger by default in builder</td></tr>
<tr class="nested-element"><td width="290">builder.disable-submit-button</td><td width="70">0..1</td><td width="150">boolean</td><td>Disable submit button in the builder's form preview footer</td></tr>
<tr class="nested-element"><td width="290">builder.hide-add-theme</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide add theme button</td></tr>
<tr class="nested-element"><td width="290">builder.disable-use-blank-form</td><td width="70">0..1</td><td width="150">boolean</td><td>Disable initializing the builder with a sample form when no form is provided</td></tr>
<tr class="nested-element"><td width="290">builder.enable-share</td><td width="70">0..1</td><td width="150">boolean</td><td>Make share button visible in the builder and enable accepting a serialized questionnaire from query param</td></tr>
<tr class="nested-element"><td width="290">builder.hide-population</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide population tab in debug instruments</td></tr>
<tr class="nested-element"><td width="290">builder.hide-save-theme</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide save theme</td></tr>
<tr class="nested-element"><td width="290">builder.disable-save</td><td width="70">0..1</td><td width="150">boolean</td><td>Disable save</td></tr>
<tr class="nested-element"><td width="290">builder.disable-publish</td><td width="70">0..1</td><td width="150">boolean</td><td>Disable publish button</td></tr>
<tr class="nested-element"><td width="290">builder.form-url-prefix</td><td width="70">0..1</td><td width="150">url</td><td>URL prefix that used in url generation of new forms</td></tr>
<tr class="nested-element"><td width="290">builder.disable-load-fhir-version</td><td width="70">0..1</td><td width="150">boolean</td><td>Disable loading of FHIR version from the server. Deprecated: not used anymore and has no effect</td></tr>
<tr class="nested-element"><td width="290">builder.hide-edit-theme</td><td width="70">0..1</td><td width="150">boolean</td><td>Hide edit theme button</td></tr>
<tr class="top-element required-field"><td width="290">name</td><td width="70">1..1</td><td width="150">string</td><td>Machine readable config name</td></tr>
<tr class="top-element"><td width="290">data-store</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">data-store.endpoint</td><td width="70">0..1</td><td width="150">url</td><td>FHIR Server that will be used for storing/getting reponses populate data from and extract to (if not set - use Aidbox)</td></tr>
<tr class="nested-element"><td width="290">data-store.headers</td><td width="70">0..1</td><td width="150">Object</td><td>Headers (with credentials) for accessing Service (optional)</td></tr>
<tr class="top-element"><td width="290">theme</td><td width="70">0..1</td><td width="150">Reference</td><td>Default theme References: QuestionnaireTheme</td></tr>
<tr class="top-element"><td width="290">language</td><td width="70">0..1</td><td width="150">code</td><td>Default language for UI</td></tr>
<tr class="top-element"><td width="290">term-server</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">term-server.endpoint</td><td width="70">0..1</td><td width="150">url</td><td>FHIR Server that stores Terminology (ValueSet/expand$) (if not set - use Aidbox)</td></tr>
<tr class="nested-element"><td width="290">term-server.headers</td><td width="70">0..1</td><td width="150">Object</td><td>Headers (with credentials) for accessing Service (optional)</td></tr>
<tr class="top-element"><td width="290">default</td><td width="70">0..1</td><td width="150">boolean</td><td>Set's config as default for system/tenant</td></tr>
<tr class="top-element"><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Human readable config description</td></tr></tbody>
</table>


## SDCPrintTemplate

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">content</td><td width="70">0..1</td><td width="150">string</td><td>Template for SDC print</td></tr></tbody>
</table>


## SDCDocument

Base schema for questionnaire(document) definition. Also a resource in DB - SDCDocument

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">based-on</td><td width="70">0..1</td><td width="150"></td><td>Request led to creation of this Document</td></tr>
<tr class="nested-element"><td width="290">based-on.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">based-on.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">based-on.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">author</td><td width="70">0..1</td><td width="150"></td><td>Reference to user which create document</td></tr>
<tr class="nested-element"><td width="290">author.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">author.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">author.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">form</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">form.form</td><td width="70">0..1</td><td width="150">string</td><td>Full name of form, with which document was captured</td></tr>
<tr class="nested-element"><td width="290">form.version</td><td width="70">0..1</td><td width="150">number</td><td>Used Form version</td></tr>
<tr class="top-element"><td width="290">encounter</td><td width="70">0..1</td><td width="150"></td><td>Reference to encounter</td></tr>
<tr class="nested-element"><td width="290">encounter.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">encounter.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">encounter.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">unit-system</td><td width="70">0..1</td><td width="150">string</td><td>What unit system was used in this document at launch</td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Status of the document </td></tr>
<tr class="top-element"><td width="290">source</td><td width="70">0..1</td><td width="150"></td><td>The person who answered the questions</td></tr>
<tr class="nested-element"><td width="290">source.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">source.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">source.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Name/version of form, with which document was captured</td></tr>
<tr class="top-element"><td width="290">patient</td><td width="70">0..1</td><td width="150"></td><td>The subject of the question</td></tr>
<tr class="nested-element"><td width="290">patient.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">patient.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">patient.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## SDCFormMetadata

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">properties</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
</table>


## SDCFormVersion

Form Metadata that can be used for Dynamic updates

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">form</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">version</td><td width="70">0..1</td><td width="150">number</td><td>Incremental number of form version.</td></tr>
<tr class="top-element"><td width="290">hash</td><td width="70">0..1</td><td width="150">number</td><td>Hash of snapshot with injected document/layout/rules</td></tr>
<tr class="top-element"><td width="290">snapshot</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## SDCWorkflow

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element"><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td>Workflow params schema definition</td></tr>
<tr class="top-element"><td width="290">cancel-reason</td><td width="70">0..1</td><td width="150">string</td><td>Define why workfow is canceled</td></tr>
<tr class="top-element"><td width="290">items</td><td width="70">0..1</td><td width="150">Object</td><td>Workflow items</td></tr>
<tr class="top-element"><td width="290">workflow</td><td width="70">0..1</td><td width="150">string</td><td>Workflow symbolic name for storing in DB</td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Status of WF lifecycle. Should be changed via rpc: `cancel-wf`, `complete-wf` `cancel-task`, `complete-task`</td></tr>
<tr class="top-element"><td width="290">order</td><td width="70">0..*</td><td width="150"></td><td>Order of items (array of item keys)</td></tr>
<tr class="top-element"><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td>Title of the workflow</td></tr>
<tr class="top-element"><td width="290">version</td><td width="70">0..1</td><td width="150">number</td><td>Workflow version</td></tr>
<tr class="top-element"><td width="290">ctx</td><td width="70">0..1</td><td width="150"></td><td>Workflow context, which can be used from tasks as well</td></tr>
<tr class="nested-element"><td width="290">ctx.encounter</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.encounter.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.encounter.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.encounter.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.patient</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.patient.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.patient.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.patient.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.user</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.user.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.user.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">ctx.user.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## SDCWorkflowVersion

Snapshotted workflow template with incremental versioning.

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element required-field"><td width="290">workflow</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">version</td><td width="70">1..1</td><td width="150">number</td><td>Incremental number of workflow version.</td></tr>
<tr class="top-element required-field"><td width="290">hash</td><td width="70">1..1</td><td width="150">number</td><td>Hash of snapshot with injected document/layout/rules</td></tr>
<tr class="top-element required-field"><td width="290">snapshot</td><td width="70">1..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## SDCAddendum

Addednum Resource. Contains additional information abount SDCDocument/SDCWorkflow

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr class="top-element required-field"><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">target</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">target.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">target.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">target.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">user</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">user.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">user.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">user.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">date</td><td width="70">1..1</td><td width="150">dateTime</td><td></td></tr></tbody>
</table>

