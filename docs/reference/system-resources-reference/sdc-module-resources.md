# SDC Module Resources

Resources for configuration and management Aidbox SDC module.

 ## Overview

SDC module includes the following resource types:

- QuestionnaireTheme
- SDCAddendum
- SDCConfig
- SDCDocument
- SDCFormMetadata
- SDCFormVersion
- SDCPrintTemplate
- SDCWorkflow
- SDCWorkflowVersion

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
<tr><td width="290">background</td><td width="70">0..1</td><td width="150"></td><td>Background styles</td></tr>
<tr><td width="290">background.<strong>main-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Main background color (RBG hex)</td></tr>
<tr><td width="290">background.<strong>form-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color for form (RBG hex)</td></tr>
<tr><td width="290">background.<strong>toolbar-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color for toolbar that displays below the form (with submit button) (RGB hex)</td></tr>
<tr><td width="290">base-font-size</td><td width="70">0..1</td><td width="150">number</td><td>Minimal text size (px)</td></tr>
<tr><td width="290">brand-image</td><td width="70">0..1</td><td width="150"></td><td>Brand images</td></tr>
<tr><td width="290">brand-image.<strong>top-right</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">brand-image.<strong>top-right</strong>.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>Web link to top-right image</td></tr>
<tr><td width="290">brand-image.<strong>bottom-left</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">brand-image.<strong>bottom-left</strong>.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>Web link to bottom-left image</td></tr>
<tr><td width="290">button</td><td width="70">0..1</td><td width="150"></td><td>Button styles</td></tr>
<tr><td width="290">button.<strong>amend-text-color</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>print-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color for "Print" button</td></tr>
<tr><td width="290">button.<strong>_amend-text</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">button.<strong>_submit-text</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">button.<strong>redirect-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color for "Save & Close" button</td></tr>
<tr><td width="290">button.<strong>_redirect-text</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">button.<strong>submit-text</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>print-text-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Text color for "Print" button</td></tr>
<tr><td width="290">button.<strong>print-text</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>redirect-text-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Text color for "Save & Close" button</td></tr>
<tr><td width="290">button.<strong>_print-text</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">button.<strong>amend-color</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>text-color</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>submit-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color for "Submit" button</td></tr>
<tr><td width="290">button.<strong>redirect-text</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>amend-text</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">button.<strong>submit-text-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Text color for "Submit" button</td></tr>
<tr><td width="290">button.<strong>accent-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color for filled buttons border color for outlined buttons (deprecated)</td></tr>
<tr><td width="290">font-family</td><td width="70">0..1</td><td width="150">string</td><td>Font-Family 

<strong>Allowed values</strong>: `Product Sans` | `Metropolis` | `Inter`</td></tr>
<tr><td width="290">input</td><td width="70">0..1</td><td width="150"></td><td>Input styles</td></tr>
<tr><td width="290">input.<strong>accent-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Main color for inputs (border color for text inputs background for checkbox etc in RGB hex)</td></tr>
<tr><td width="290">input.<strong>text-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>text color (RGB hex)</td></tr>
<tr><td width="290">input.<strong>background-color</strong></td><td width="70">0..1</td><td width="150">string</td><td>Background color (RGB hex)</td></tr>
<tr><td width="290">input.<strong>font-size</strong></td><td width="70">0..1</td><td width="150">number</td><td>Input font size (px)</td></tr>
<tr><td width="290">language</td><td width="70">0..1</td><td width="150">code</td><td>Theme Language</td></tr>
<tr><td width="290">main-color</td><td width="70">0..1</td><td width="150">string</td><td>Main Accent color (RBG hex)</td></tr>
<tr><td width="290">theme-name</td><td width="70">0..1</td><td width="150">string</td><td>Theme Title</td></tr></tbody>
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
<tr><td width="290">date</td><td width="70">1..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">target</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">target.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">target.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">target.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">user</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">user.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">user.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">user.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">builder</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">builder.<strong>hide-extraction</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide extraction tab in debug instruments</td></tr>
<tr><td width="290">builder.<strong>highlight-download-button</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Highlight download button in the builder's debugger panel</td></tr>
<tr><td width="290">builder.<strong>translation-languages</strong></td><td width="70">0..*</td><td width="150">string</td><td>List of languages that will be used for translation</td></tr>
<tr><td width="290">builder.<strong>hide-back-button</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide back button in builder</td></tr>
<tr><td width="290">builder.<strong>hide-save</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide save button</td></tr>
<tr><td width="290">builder.<strong>hide-publish</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide Publish button in builder</td></tr>
<tr><td width="290">builder.<strong>collapse-debugger</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Collapse debugger by default in builder</td></tr>
<tr><td width="290">builder.<strong>disable-submit-button</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Disable submit button in the builder's form preview footer</td></tr>
<tr><td width="290">builder.<strong>hide-add-theme</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide add theme button</td></tr>
<tr><td width="290">builder.<strong>disable-use-blank-form</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Disable initializing the builder with a sample form when no form is provided</td></tr>
<tr><td width="290">builder.<strong>enable-share</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Make share button visible in the builder and enable accepting a serialized questionnaire from query param</td></tr>
<tr><td width="290">builder.<strong>hide-population</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide population tab in debug instruments</td></tr>
<tr><td width="290">builder.<strong>hide-save-theme</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide save theme</td></tr>
<tr><td width="290">builder.<strong>disable-save</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Disable save</td></tr>
<tr><td width="290">builder.<strong>disable-publish</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Disable publish button</td></tr>
<tr><td width="290">builder.<strong>form-url-prefix</strong></td><td width="70">0..1</td><td width="150">url</td><td>URL prefix that used in url generation of new forms</td></tr>
<tr><td width="290">builder.<strong>disable-load-fhir-version</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Disable loading of FHIR version from the server. Deprecated: not used anymore and has no effect</td></tr>
<tr><td width="290">builder.<strong>hide-edit-theme</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide edit theme button</td></tr>
<tr><td width="290">data-store</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">data-store.<strong>endpoint</strong></td><td width="70">0..1</td><td width="150">url</td><td>FHIR Server that will be used for storing/getting reponses populate data from and extract to (if not set - use Aidbox)</td></tr>
<tr><td width="290">data-store.<strong>headers</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Headers (with credentials) for accessing Service (optional)</td></tr>
<tr><td width="290">default</td><td width="70">0..1</td><td width="150">boolean</td><td>Set's config as default for system/tenant</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Human readable config description</td></tr>
<tr><td width="290">form</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">form.<strong>app-name</strong></td><td width="70">0..1</td><td width="150">string</td><td>App name that will be mentioned in AuditEvent logs</td></tr>
<tr><td width="290">form.<strong>hide-print-button</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide print button from the form</td></tr>
<tr><td width="290">form.<strong>enable-amend-button</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Enable amend button</td></tr>
<tr><td width="290">form.<strong>read-only</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Should form be read-only</td></tr>
<tr><td width="290">form.<strong>hide-language-selector</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide language selector in the builder</td></tr>
<tr><td width="290">form.<strong>hide-footer</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide the footer in the renderer altogether</td></tr>
<tr><td width="290">form.<strong>default-max-width</strong></td><td width="70">0..1</td><td width="150">string</td><td>Default max width of the form</td></tr>
<tr><td width="290">form.<strong>non-sticky-footer</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Non-sticky footer in the form</td></tr>
<tr><td width="290">form.<strong>redirect-on-save</strong></td><td width="70">0..1</td><td width="150">url</td><td>Redirect URI that used on form save/close button</td></tr>
<tr><td width="290">form.<strong>redirect-on-submit</strong></td><td width="70">0..1</td><td width="150">url</td><td>Redirect URI that used on form sign/amend</td></tr>
<tr><td width="290">form.<strong>enable-save-button</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Enable close button</td></tr>
<tr><td width="290">form.<strong>hide-title</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Hide title in the form</td></tr>
<tr><td width="290">form-store</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">form-store.<strong>endpoint</strong></td><td width="70">0..1</td><td width="150">url</td><td>FHIR Server that will be used for storing/getting Questionnaire (if not set - use Aidbox)</td></tr>
<tr><td width="290">form-store.<strong>headers</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Headers with credentials (optional)</td></tr>
<tr><td width="290">language</td><td width="70">0..1</td><td width="150">code</td><td>Default language for UI</td></tr>
<tr><td width="290">name</td><td width="70">1..1</td><td width="150">string</td><td>Machine readable config name</td></tr>
<tr><td width="290">storage</td><td width="70">0..1</td><td width="150"></td><td>Link to storage to store attachments</td></tr>
<tr><td width="290">storage.<strong>account</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Storage Account 

<strong>Allowed references</strong>: AwsAccount, GcpServiceAccount, AzureContainer</td></tr>
<tr><td width="290">storage.<strong>bucket</strong></td><td width="70">0..1</td><td width="150">string</td><td>Storage bucket (required for GCP and S3)</td></tr>
<tr><td width="290">term-server</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">term-server.<strong>endpoint</strong></td><td width="70">0..1</td><td width="150">url</td><td>FHIR Server that stores Terminology (ValueSet/expand$) (if not set - use Aidbox)</td></tr>
<tr><td width="290">term-server.<strong>headers</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Headers (with credentials) for accessing Service (optional)</td></tr>
<tr><td width="290">theme</td><td width="70">0..1</td><td width="150">Reference</td><td>Default theme 

<strong>Allowed references</strong>: QuestionnaireTheme</td></tr>
<tr><td width="290">translations</td><td width="70">0..1</td><td width="150">Object</td><td>Custom translations strings for UI</td></tr></tbody>
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
<tr><td width="290">author</td><td width="70">0..1</td><td width="150"></td><td>Reference to user which create document</td></tr>
<tr><td width="290">author.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">author.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">author.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">based-on</td><td width="70">0..1</td><td width="150"></td><td>Request led to creation of this Document</td></tr>
<tr><td width="290">based-on.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">based-on.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">based-on.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">encounter</td><td width="70">0..1</td><td width="150"></td><td>Reference to encounter</td></tr>
<tr><td width="290">encounter.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">encounter.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">encounter.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">form</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">form.<strong>form</strong></td><td width="70">0..1</td><td width="150">string</td><td>Full name of form, with which document was captured</td></tr>
<tr><td width="290">form.<strong>version</strong></td><td width="70">0..1</td><td width="150">number</td><td>Used Form version</td></tr>
<tr><td width="290">patient</td><td width="70">0..1</td><td width="150"></td><td>The subject of the question</td></tr>
<tr><td width="290">patient.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">patient.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">patient.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150"></td><td>The person who answered the questions</td></tr>
<tr><td width="290">source.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">source.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">source.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Status of the document  

<strong>Allowed values</strong>: `draft` | `in-progress` | `canceled` | `completed` | `in-amendment` | `amended`</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Name/version of form, with which document was captured</td></tr>
<tr><td width="290">unit-system</td><td width="70">0..1</td><td width="150">string</td><td>What unit system was used in this document at launch</td></tr></tbody>
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
<tr><td width="290">properties</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr><td width="290">form</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">hash</td><td width="70">0..1</td><td width="150">number</td><td>Hash of snapshot with injected document/layout/rules</td></tr>
<tr><td width="290">snapshot</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">version</td><td width="70">0..1</td><td width="150">number</td><td>Incremental number of form version.</td></tr></tbody>
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
<tr><td width="290">content</td><td width="70">0..1</td><td width="150">string</td><td>Template for SDC print</td></tr></tbody>
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
<tr><td width="290">cancel-reason</td><td width="70">0..1</td><td width="150">string</td><td>Define why workfow is canceled</td></tr>
<tr><td width="290">ctx</td><td width="70">0..1</td><td width="150"></td><td>Workflow context, which can be used from tasks as well</td></tr>
<tr><td width="290">ctx.<strong>encounter</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">ctx.<strong>encounter</strong>.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>encounter</strong>.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>encounter</strong>.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>patient</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">ctx.<strong>patient</strong>.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>patient</strong>.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>patient</strong>.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>user</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">ctx.<strong>user</strong>.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>user</strong>.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">ctx.<strong>user</strong>.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">items</td><td width="70">0..1</td><td width="150">Object</td><td>Workflow items</td></tr>
<tr><td width="290">order</td><td width="70">0..*</td><td width="150">string</td><td>Order of items (array of item keys)</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td>Workflow params schema definition</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Status of WF lifecycle. Should be changed via rpc: `cancel-wf`, `complete-wf` `cancel-task`, `complete-task` 

<strong>Allowed values</strong>: `new` | `in-progress` | `canceled` | `completed` | `in-amendment` | `amended`</td></tr>
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td>Title of the workflow</td></tr>
<tr><td width="290">version</td><td width="70">0..1</td><td width="150">number</td><td>Workflow version</td></tr>
<tr><td width="290">workflow</td><td width="70">0..1</td><td width="150">string</td><td>Workflow symbolic name for storing in DB</td></tr></tbody>
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
<tr><td width="290">hash</td><td width="70">1..1</td><td width="150">number</td><td>Hash of snapshot with injected document/layout/rules</td></tr>
<tr><td width="290">snapshot</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">version</td><td width="70">1..1</td><td width="150">number</td><td>Incremental number of workflow version.</td></tr>
<tr><td width="290">workflow</td><td width="70">1..1</td><td width="150">string</td><td></td></tr></tbody>
</table>

