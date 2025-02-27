# FHIR

## General

### Root FHIR package <a href="#root-fhir-package-fdfsdfasdfadsf" id="root-fhir-package-fdfsdfasdfadsf"></a>

Identifier for the main Aidbox FHIR package that stores dependencies and canonical resources provided by the user.

<table data-header-hidden><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>root-fhir-package</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Sensitive</td><td><code>true</code></td></tr><tr><td>Available from</td><td>2501</td></tr><tr><td>Hot reload</td><td>false</td></tr><tr><td>Environment variables</td><td><code>BOX_ROOT_FHIR_PACKAGE</code> , <code>AIDBOX_ROOT_FHIR_PACKAGE</code></td></tr><tr><td>Default value</td><td><code>app.aidbox.main#0.0.1</code></td></tr></tbody></table>

## Bulk Data Export

### Bulk storage provider

Storage provider for bulk export

<table data-header-hidden><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.bulk-storage.provider</code></td></tr><tr><td>Type</td><td>Enum</td></tr><tr><td>Values</td><td><code>gcp</code> - Google Cloud Platform<br><code>aws</code> - Amazon Web Services<br><code>azure</code> - Microsoft Azure: Cloud Computing Services</td></tr><tr><td>Default value</td><td>(no default) </td></tr><tr><td>Sensitive</td><td><code>false</code></td></tr><tr><td>Hot reload</td><td><code>true</code></td></tr><tr><td>Environment variables</td><td><code>BOX_FHIR_BULK_STORAGE_PROVIDER</code> </td></tr><tr><td>Available from</td><td>2501</td></tr></tbody></table>

## Search

### Default search result count estimation method <a href="#fhir.search.default-params.total" id="fhir.search.default-params.total"></a>

FHIR search response bundle may contain a result count estimation.

<table data-header-hidden><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>fhir.search.default-params.total</code></td></tr><tr><td>Type</td><td>enum, string, boolean, integer</td></tr><tr><td>Unit</td><td>byte, second (if present)</td></tr><tr><td>Values</td><td><code>none</code>  — omit estimation (fastest)<br><code>estimate</code>  — use approximate value (fast)<br><code>accurate</code>  — use exact value (could be slow)</td></tr><tr><td>Default value</td><td><code>accurate</code><br>(no default) </td></tr><tr><td>Environment variables</td><td><code>BOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL</code> , <code>AIDBOX_FHIR_SEARCH_DEFAULT_PARAMS_TOTAL</code></td></tr><tr><td>Available from</td><td><code>2501</code></td></tr><tr><td>Sensitive </td><td><code>true</code>  — can be set only via environment variable<br><code>false</code>  — can be set via UI and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code>  — requires Aidbox restart<br><code>true</code>  — can be changed at runtime</td></tr></tbody></table>
