# Base Module Resources


           System resources that do not belong to any specific module.

 ## Overview

Base module includes the following resource types:

- AidboxJob
- AidboxJobStatus
- AidboxConfig
- AidboxMigration
- AidboxProfile
- AidboxQuery
- SearchQuery
- Concept
- Module
- Operation
- Search
- App
- AidboxArchive
- BatchValidationError
- BatchValidationRun
- PGSequence
- AidboxSubscription
- Attribute
- Entity
- ui_history
- ui_snippet
- Notebook
- Mapping
- ConceptMapRule
- FlatImportStatus
- FtrConfig
- TerminologyBundleFile
- IndexCreationJob
- Lambda
- SeedImport
- SubsNotification
- SubsSubscription

## AidboxJob

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
<tr><td width="290">every</td><td width="70">0..1</td><td width="150">integer</td><td>Frequency in seconds at which the job should run.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">Object</td><td>Current status information for the job.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this job belongs to.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable description of the job.</td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td>Action to be performed when the job runs.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of job scheduling pattern. 

<strong>Allowed values</strong>: periodic | each-day</td></tr>
<tr><td width="290">at</td><td width="70">0..1</td><td width="150">string</td><td>Time of day when the job should run (for each-day type).</td></tr></tbody>
</table>


## AidboxJobStatus

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
<tr><td width="290">stop</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the job execution stopped.</td></tr>
<tr><td width="290">job</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the job definition.</td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150">Object</td><td>Error information if the job failed.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the job execution.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this job status belongs to.</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the job execution started.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable status information.</td></tr>
<tr><td width="290">nextStart</td><td width="70">0..1</td><td width="150">dateTime</td><td>Scheduled time for the next execution of the job.</td></tr>
<tr><td width="290">locked</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether the job is currently locked to prevent concurrent execution.</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150">Object</td><td>Result data from the job execution.</td></tr></tbody>
</table>


## AidboxConfig

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
<tr><td width="290">box</td><td width="70">0..1</td><td width="150"></td><td>Box configuration parameters.</td></tr>
<tr><td width="290">auth</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Authentication configuration settings.</td></tr>
<tr><td width="290">auth.<strong>keys</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Cryptographic keys for authentication.</td></tr>
<tr><td width="290">auth.<strong>keys</strong>.<strong>secret</strong></td><td width="70">0..1</td><td width="150">string</td><td>Secret key used for signing.</td></tr>
<tr><td width="290">auth.<strong>keys</strong>.<strong>public</strong></td><td width="70">0..1</td><td width="150">string</td><td>Public key used for verification.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## AidboxMigration

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
<tr><td width="290">dateTime</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the migration was created or executed.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the migration. 

<strong>Allowed values</strong>: pending | done | error</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this migration belongs to.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable description of the migration.</td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td>Action to be performed for the migration.</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150">Object</td><td>Result data from the migration execution.</td></tr></tbody>
</table>


## AidboxProfile

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
<tr><td width="290">bind</td><td width="70">1..1</td><td width="150">Reference</td><td>Reference to the resource to which this profile is bound.</td></tr>
<tr><td width="290">schema</td><td width="70">1..1</td><td width="150">Object</td><td>Schema definition for the profile.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## AidboxQuery

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
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td>Parameters for the query.</td></tr>
<tr><td width="290">query</td><td width="70">1..1</td><td width="150">string</td><td>SQL query string.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">count-query</td><td width="70">0..1</td><td width="150">string</td><td>SQL query to count total results.</td></tr>
<tr><td width="290">enable-links</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to generate FHIR links for pagination.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of query operation. 

<strong>Allowed values</strong>: query | execute</td></tr>
<tr><td width="290">omit-sql</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to omit SQL in response metadata.</td></tr></tbody>
</table>


## SearchQuery

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
<tr><td width="290">resource</td><td width="70">1..1</td><td width="150">Reference</td><td>Reference to the resource type to search.</td></tr>
<tr><td width="290">total</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to include total count in results.</td></tr>
<tr><td width="290">as</td><td width="70">0..1</td><td width="150">string</td><td>Alias for the resource in the query.</td></tr>
<tr><td width="290">limit</td><td width="70">0..1</td><td width="150">integer</td><td>Maximum number of results to return.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td>Search parameters for the query.</td></tr>
<tr><td width="290">includes</td><td width="70">0..1</td><td width="150">Object</td><td>Resources to include with the results.</td></tr>
<tr><td width="290">query</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Detailed query configuration.</td></tr>
<tr><td width="290">query.<strong>order-by</strong></td><td width="70">0..1</td><td width="150">string</td><td>Column or expression to order results by.</td></tr>
<tr><td width="290">query.<strong>join</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Join conditions for the query.</td></tr>
<tr><td width="290">query.<strong>where</strong></td><td width="70">0..1</td><td width="150">string</td><td>Where clause for the query.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## Concept

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
<tr><td width="290">designation</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Additional representations for the concept.</td></tr>
<tr><td width="290">designation.<strong>definition</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Additional definitions for the concept.</td></tr>
<tr><td width="290">designation.<strong>display</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Display names in different languages or contexts.</td></tr>
<tr><td width="290">ancestors</td><td width="70">0..1</td><td width="150">Object</td><td>List of ancestor concepts in the hierarchy.</td></tr>
<tr><td width="290">valueset</td><td width="70">0..*</td><td width="150">string</td><td>Value sets that include this concept.</td></tr>
<tr><td width="290">hierarchy</td><td width="70">0..*</td><td width="150">string</td><td>Hierarchies this concept belongs to.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">deprecated</td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether the concept is deprecated.</td></tr>
<tr><td width="290">property</td><td width="70">0..1</td><td width="150">Object</td><td>Additional properties associated with the concept.</td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td>Formal definition of the concept.</td></tr>
<tr><td width="290">display</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable representation of the concept.</td></tr>
<tr><td width="290">system</td><td width="70">1..1</td><td width="150">string</td><td>Code system that defines the concept.</td></tr>
<tr><td width="290">code</td><td width="70">1..1</td><td width="150">string</td><td>Symbol or identifier for the concept within the system.</td></tr></tbody>
</table>


## Module

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
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td>Metadata for the module.</td></tr>
<tr><td width="290">meta.<strong>pre-sql</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL to execute before module installation.</td></tr>
<tr><td width="290">meta.<strong>post-sql</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL to execute after module installation.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Identifier for the module.</td></tr>
<tr><td width="290">version</td><td width="70">0..1</td><td width="150">integer</td><td>Version number of the module.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## Operation

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
<tr><td width="290">fhirCode</td><td width="70">0..1</td><td width="150">string</td><td>FHIR operation code.</td></tr>
<tr><td width="290">transform</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Transformation configuration for the operation.</td></tr>
<tr><td width="290">transform.<strong>request</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Request transformation settings.</td></tr>
<tr><td width="290">transform.<strong>request</strong>.<strong>engine</strong></td><td width="70">0..1</td><td width="150">code</td><td>Transformation engine to use.</td></tr>
<tr><td width="290">transform.<strong>request</strong>.<strong>template</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to a template for transformation.</td></tr>
<tr><td width="290">transform.<strong>request</strong>.<strong>part</strong></td><td width="70">1..1</td><td width="150">string</td><td>Part of the request to transform. 

<strong>Allowed values</strong>: body</td></tr>
<tr><td width="290">route-params</td><td width="70">0..1</td><td width="150">Object</td><td>Parameters for route matching.</td></tr>
<tr><td width="290">fhirResource</td><td width="70">0..*</td><td width="150">string</td><td>FHIR resources this operation applies to.</td></tr>
<tr><td width="290">no-op-logs</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to disable operation logging.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">implicit-params</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Parameters that are implicitly included in the operation.</td></tr>
<tr><td width="290">implicit-params.<strong>path</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Path parameters.</td></tr>
<tr><td width="290">implicit-params.<strong>query</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Query parameters.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this operation belongs to.</td></tr>
<tr><td width="290">request</td><td width="70">0..*</td><td width="150">Object</td><td>Request configurations.</td></tr>
<tr><td width="290">fhirUrl</td><td width="70">0..1</td><td width="150">string</td><td>FHIR URL pattern for the operation.</td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td>Action to be performed by the operation.</td></tr>
<tr><td width="290">app</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to associated App. 

<strong>Allowed references</strong>: App</td></tr>
<tr><td width="290">public</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether the operation is publicly accessible.</td></tr>
<tr><td width="290">data</td><td width="70">0..1</td><td width="150">Object</td><td>Additional operation data.</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable description of the operation.</td></tr></tbody>
</table>


## Search

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
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the resource type this search applies to.</td></tr>
<tr><td width="290">where</td><td width="70">0..1</td><td width="150">string</td><td>SQL WHERE clause for the search.</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td>Name of the search parameter.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">param-parser</td><td width="70">0..1</td><td width="150">string</td><td>Parser to use for the search parameter. 

<strong>Allowed values</strong>: token | reference</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this search belongs to.</td></tr>
<tr><td width="290">order-by</td><td width="70">0..1</td><td width="150">string</td><td>Default sort order for search results.</td></tr>
<tr><td width="290">token-sql</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>SQL templates for token parameter handling.</td></tr>
<tr><td width="290">token-sql.<strong>text</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL template for text search.</td></tr>
<tr><td width="290">token-sql.<strong>both</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL template when both system and code are provided.</td></tr>
<tr><td width="290">token-sql.<strong>only-system</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL template when only system is provided.</td></tr>
<tr><td width="290">token-sql.<strong>only-code</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL template when only code is provided.</td></tr>
<tr><td width="290">token-sql.<strong>text-format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format for text search.</td></tr>
<tr><td width="290">token-sql.<strong>no-system</strong></td><td width="70">0..1</td><td width="150">string</td><td>SQL template when no system is provided.</td></tr>
<tr><td width="290">multi</td><td width="70">0..1</td><td width="150">string</td><td>How multiple values for this parameter should be handled. 

<strong>Allowed values</strong>: array</td></tr>
<tr><td width="290">format</td><td width="70">0..1</td><td width="150">string</td><td>Format of the search parameter.</td></tr></tbody>
</table>


## App

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
<tr><td width="290">migrations</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>List of migrations for this app.</td></tr>
<tr><td width="290">migrations.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Unique identifier for the migration.</td></tr>
<tr><td width="290">migrations.<strong>dateTime</strong></td><td width="70">0..1</td><td width="150">string</td><td>Timestamp for the migration.</td></tr>
<tr><td width="290">migrations.<strong>action</strong></td><td width="70">0..1</td><td width="150">string</td><td>Action to be performed for the migration.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">code</td><td>Type of application. 

<strong>Allowed values</strong>: app | addon</td></tr>
<tr><td width="290">endpoint</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Endpoint configuration for the app.</td></tr>
<tr><td width="290">endpoint.<strong>secret</strong></td><td width="70">0..1</td><td width="150">string</td><td>Secret key for endpoint authentication.</td></tr>
<tr><td width="290">endpoint.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>URL of the endpoint.</td></tr>
<tr><td width="290">endpoint.<strong>type</strong></td><td width="70">0..1</td><td width="150">code</td><td>Type of endpoint protocol. 

<strong>Allowed values</strong>: http-rpc | ws-rpc | native</td></tr>
<tr><td width="290">apiVersion</td><td width="70">1..1</td><td width="150">integer</td><td>Version of the API this app uses.</td></tr>
<tr><td width="290">hooks</td><td width="70">0..1</td><td width="150">Object</td><td>Hooks configuration for the app.</td></tr>
<tr><td width="290">subscriptions</td><td width="70">0..1</td><td width="150">Object</td><td>Subscriptions configuration for the app.</td></tr>
<tr><td width="290">entities</td><td width="70">0..1</td><td width="150">Object</td><td>Entities defined by the app.</td></tr>
<tr><td width="290">operations</td><td width="70">0..1</td><td width="150">Object</td><td>Operations defined by the app.</td></tr></tbody>
</table>


## AidboxArchive

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
<tr><td width="290">archiveFile</td><td width="70">0..1</td><td width="150">string</td><td>Name of the archive file.</td></tr>
<tr><td width="290">serviceAccount</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Service account credentials for accessing storage.</td></tr>
<tr><td width="290">serviceAccount.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier for the service account.</td></tr>
<tr><td width="290">serviceAccount.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource that contains service account credentials.</td></tr>
<tr><td width="290">serviceAccount.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable name for the service account.</td></tr>
<tr><td width="290">serviceAccount.<strong>secret-key</strong></td><td width="70">0..1</td><td width="150">string</td><td>Secret key for service account authentication.</td></tr>
<tr><td width="290">bucket</td><td width="70">0..1</td><td width="150">string</td><td>Storage bucket where archives are stored.</td></tr>
<tr><td width="290">archivedResourcesCount</td><td width="70">0..1</td><td width="150">number</td><td>Count of resources that have been archived.</td></tr>
<tr><td width="290">criteriaPaths</td><td width="70">0..*</td><td width="150">string</td><td>Paths to use for filtering resources to archive.</td></tr>
<tr><td width="290">history</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to include resource history in the archive.</td></tr>
<tr><td width="290">storageBackend</td><td width="70">0..1</td><td width="150">string</td><td>Type of storage backend to use. 

<strong>Allowed values</strong>: gcp | aws | local</td></tr>
<tr><td width="290">period</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Time period for the resources to archive.</td></tr>
<tr><td width="290">period.<strong>start</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Start date/time for the archive period.</td></tr>
<tr><td width="290">period.<strong>end</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>End date/time for the archive period.</td></tr>
<tr><td width="290">targetResourceType</td><td width="70">0..1</td><td width="150">string</td><td>Resource type to be archived.</td></tr></tbody>
</table>


## BatchValidationError

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
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Reference to the resource that failed validation.</td></tr>
<tr><td width="290">resource.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Resource identifier.</td></tr>
<tr><td width="290">resource.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource.</td></tr>
<tr><td width="290">resource.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable display name for the resource.</td></tr>
<tr><td width="290">run</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Reference to the validation run that produced this error.</td></tr>
<tr><td width="290">run.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Validation run identifier.</td></tr>
<tr><td width="290">run.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of the validation run resource.</td></tr>
<tr><td width="290">run.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable display name for the validation run.</td></tr>
<tr><td width="290">profiles</td><td width="70">0..*</td><td width="150">string</td><td>Profiles against which the resource was validated.</td></tr>
<tr><td width="290">errors</td><td width="70">0..*</td><td width="150">Object</td><td>List of validation errors found.</td></tr></tbody>
</table>


## BatchValidationRun

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
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">string</td><td>Resource type to validate.</td></tr>
<tr><td width="290">errorsThreshold</td><td width="70">0..1</td><td width="150">integer</td><td>Maximum number of errors before stopping validation.</td></tr>
<tr><td width="290">schemas</td><td width="70">0..*</td><td width="150">string</td><td>List of schemas to validate against.</td></tr>
<tr><td width="290">invalid</td><td width="70">0..1</td><td width="150">integer</td><td>Count of invalid resources found.</td></tr>
<tr><td width="290">async</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether validation is performed asynchronously.</td></tr>
<tr><td width="290">limit</td><td width="70">0..1</td><td width="150">integer</td><td>Maximum number of resources to validate.</td></tr>
<tr><td width="290">profiles</td><td width="70">0..*</td><td width="150">string</td><td>FHIR profiles to validate against.</td></tr>
<tr><td width="290">valid</td><td width="70">0..1</td><td width="150">integer</td><td>Count of valid resources found.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the validation run. 

<strong>Allowed values</strong>: in-progress | complete</td></tr>
<tr><td width="290">filter</td><td width="70">0..1</td><td width="150">string</td><td>Expression to filter resources for validation.</td></tr>
<tr><td width="290">duration</td><td width="70">0..1</td><td width="150">integer</td><td>Duration of the validation run in milliseconds.</td></tr></tbody>
</table>


## PGSequence

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
<tr><td width="290">data_type</td><td width="70">0..1</td><td width="150">string</td><td>PostgreSQL data type for the sequence. 

<strong>Allowed values</strong>: smallint | integer | bigint</td></tr>
<tr><td width="290">cycle</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether the sequence should cycle when reaching max/min value.</td></tr>
<tr><td width="290">increment</td><td width="70">0..1</td><td width="150">integer</td><td>Value to increment by for each sequence call.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">integer</td><td>Starting value for the sequence.</td></tr>
<tr><td width="290">minvalue</td><td width="70">0..1</td><td width="150">integer</td><td>Minimum value for the sequence.</td></tr>
<tr><td width="290">maxvalue</td><td width="70">0..1</td><td width="150">integer</td><td>Maximum value for the sequence.</td></tr></tbody>
</table>


## AidboxSubscription

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
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this subscription belongs to.</td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td>Action to be performed when the subscription is triggered.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of subscription execution model. 

<strong>Allowed values</strong>: sync | async</td></tr>
<tr><td width="290">resources</td><td width="70">0..*</td><td width="150">string</td><td>List of resource types this subscription applies to.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## Attribute

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
<tr><td width="290">resource</td><td width="70">1..1</td><td width="150">Reference</td><td>Reference to the resource type this attribute belongs to.</td></tr>
<tr><td width="290">isUnique</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this attribute has unique values.</td></tr>
<tr><td width="290">refers</td><td width="70">0..*</td><td width="150">string</td><td>Resource types this attribute can reference.</td></tr>
<tr><td width="290">isCollection</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this attribute is a collection.</td></tr>
<tr><td width="290">extensionUrl</td><td width="70">0..1</td><td width="150">string</td><td>URL for the extension this attribute represents.</td></tr>
<tr><td width="290">valueSet</td><td width="70">0..1</td><td width="150"></td><td>Value set constraint for this attribute.</td></tr>
<tr><td width="290">valueSet.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of the value set resource.</td></tr>
<tr><td width="290">valueSet.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier of the value set.</td></tr>
<tr><td width="290">valueSet.<strong>uri</strong></td><td width="70">0..1</td><td width="150">string</td><td>URI of the value set.</td></tr>
<tr><td width="290">union</td><td width="70">0..*</td><td width="150">Reference</td><td>References to other attributes in a union type.</td></tr>
<tr><td width="290">path</td><td width="70">0..*</td><td width="150">string</td><td>Path to the attribute within the resource.</td></tr>
<tr><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td>Schema for the attribute.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this attribute belongs to.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable text about the attribute.</td></tr>
<tr><td width="290">order</td><td width="70">0..1</td><td width="150">integer</td><td>Order for display or processing.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the attribute's data type.</td></tr>
<tr><td width="290">isSummary</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this attribute is included in summary views.</td></tr>
<tr><td width="290">isOpen</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this attribute allows additional properties.</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable description of the attribute.</td></tr>
<tr><td width="290">isModifier</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this attribute changes the meaning of the resource.</td></tr>
<tr><td width="290">isRequired</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this attribute is required.</td></tr>
<tr><td width="290">enum</td><td width="70">0..*</td><td width="150">string</td><td>Enumeration of allowed values for this attribute.</td></tr></tbody>
</table>


## Entity

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
<tr><td width="290">sequencePrefix</td><td width="70">0..1</td><td width="150">string</td><td>Prefix for sequence-generated IDs.</td></tr>
<tr><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td>Schema definition for the entity.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">nonPersistable</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this entity should not be persisted.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this entity belongs to.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable text about the entity.</td></tr>
<tr><td width="290">isMeta</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this entity is a metadata entity.</td></tr>
<tr><td width="290">history</td><td width="70">0..1</td><td width="150">string</td><td>History tracking mode for this entity. 

<strong>Allowed values</strong>: none | diff</td></tr>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td>Type of entity definition. 

<strong>Allowed values</strong>: abstract | resource | type | primitive</td></tr>
<tr><td width="290">isOpen</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this entity allows additional properties.</td></tr>
<tr><td width="290">idGeneration</td><td width="70">0..1</td><td width="150">string</td><td>Strategy for generating IDs for this entity. 

<strong>Allowed values</strong>: sequence | uuid</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable description of the entity.</td></tr></tbody>
</table>


## ui_history

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
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the user who performed the action. 

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of history entry. 

<strong>Allowed values</strong>: http | sql</td></tr>
<tr><td width="290">command</td><td width="70">0..1</td><td width="150">string</td><td>Command that was executed.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## ui_snippet

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
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">command</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: http | sql</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## Notebook

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
<tr><td width="290">origin</td><td width="70">0..1</td><td width="150">string</td><td>Origin information for the notebook.</td></tr>
<tr><td width="290">tags</td><td width="70">0..*</td><td width="150">string</td><td>Tags associated with the notebook.</td></tr>
<tr><td width="290">notebook-superuser-secret</td><td width="70">0..1</td><td width="150">string</td><td>Secret for superuser access to the notebook.</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td>Name of the notebook.</td></tr>
<tr><td width="290">edit-secret</td><td width="70">0..1</td><td width="150">string</td><td>Secret for edit access to the notebook.</td></tr>
<tr><td width="290">publication-id</td><td width="70">0..1</td><td width="150">string</td><td>Identifier for the published version of the notebook.</td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150"></td><td>Source content for the notebook.</td></tr>
<tr><td width="290">cells</td><td width="70">0..*</td><td width="150"></td><td>Cells contained in the notebook.</td></tr>
<tr><td width="290">cells.<strong>id</strong></td><td width="70">0..1</td><td width="150"></td><td>Unique identifier for the cell.</td></tr>
<tr><td width="290">cells.<strong>evaluating?</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether the cell is currently being evaluated.</td></tr>
<tr><td width="290">cells.<strong>folded</strong></td><td width="70">0..1</td><td width="150"></td><td>Folding state of the cell.</td></tr>
<tr><td width="290">cells.<strong>folded</strong>.<strong>code</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether the code section is folded.</td></tr>
<tr><td width="290">cells.<strong>folded</strong>.<strong>result</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether the result section is folded.</td></tr>
<tr><td width="290">cells.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of cell content. 

<strong>Allowed values</strong>: rpc | rest | empty | markdown | sql</td></tr>
<tr><td width="290">cells.<strong>value</strong></td><td width="70">0..1</td><td width="150"></td><td>Content value of the cell.</td></tr>
<tr><td width="290">cells.<strong>result</strong></td><td width="70">0..1</td><td width="150"></td><td>Result of cell evaluation.</td></tr>
<tr><td width="290">cells.<strong>error</strong></td><td width="70">0..1</td><td width="150"></td><td>Error information if evaluation failed.</td></tr>
<tr><td width="290">cells.<strong>nb-title</strong></td><td width="70">0..1</td><td width="150">string</td><td>Title for the cell.</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td>Description of the notebook.</td></tr></tbody>
</table>


## Mapping

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
<tr><td width="290">returns</td><td width="70">0..1</td><td width="150">code</td><td>Type of result returned by the mapping. 

<strong>Allowed values</strong>: transaction | resource</td></tr>
<tr><td width="290">body</td><td width="70">1..1</td><td width="150">Object</td><td>Mapping transformation definition.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td>Parameters for the mapping execution.</td></tr>
<tr><td width="290">params.<strong>omit-drop-blanks</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to omit blank values from the result.</td></tr>
<tr><td width="290">scopeSchema</td><td width="70">0..1</td><td width="150">Object</td><td>Schema defining the scope for mapping.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150"></td><td>Human-readable text about the mapping.</td></tr>
<tr><td width="290">text.<strong>status</strong></td><td width="70">0..1</td><td width="150">string</td><td>Status of the text.</td></tr>
<tr><td width="290">text.<strong>div</strong></td><td width="70">0..1</td><td width="150">string</td><td>HTML representation of the text.</td></tr></tbody>
</table>


## ConceptMapRule

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
<tr><td width="290">unmapped</td><td width="70">0..1</td><td width="150">Object</td><td>Rules for handling unmapped concepts.</td></tr>
<tr><td width="290">unmapped.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>URL for unmapped value set.</td></tr>
<tr><td width="290">unmapped.<strong>mode</strong></td><td width="70">0..1</td><td width="150">string</td><td>Mode for handling unmapped concepts.</td></tr>
<tr><td width="290">sourceValueSet</td><td width="70">0..1</td><td width="150">string</td><td>Source value set for the mapping.</td></tr>
<tr><td width="290">element</td><td width="70">0..1</td><td width="150">Object</td><td>Element mapping definition.</td></tr>
<tr><td width="290">element.<strong>target</strong></td><td width="70">0..1</td><td width="150"></td><td>Target mapping information.</td></tr>
<tr><td width="290">element.<strong>target</strong>.<strong>comment</strong></td><td width="70">0..1</td><td width="150">string</td><td>Comment about the mapping.</td></tr>
<tr><td width="290">element.<strong>target</strong>.<strong>equivalence</strong></td><td width="70">0..1</td><td width="150">string</td><td>Equivalence relationship between source and target.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td>Source system for the mapping.</td></tr>
<tr><td width="290">target</td><td width="70">0..1</td><td width="150">string</td><td>Target system for the mapping.</td></tr>
<tr><td width="290">conceptmapUrl</td><td width="70">0..1</td><td width="150">string</td><td>URL of the parent concept map.</td></tr>
<tr><td width="290">targetValueSet</td><td width="70">0..1</td><td width="150">string</td><td>Target value set for the mapping.</td></tr>
<tr><td width="290">conceptmapId</td><td width="70">0..1</td><td width="150">string</td><td>ID of the parent concept map.</td></tr></tbody>
</table>


## FlatImportStatus

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
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the import process. 

<strong>Allowed values</strong>: in-progress | done | fail</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td>Parameters used for the import.</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td>Result information for successful import.</td></tr>
<tr><td width="290">result.<strong>count</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Count of successfully imported resources.</td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td>Error information when import fails.</td></tr>
<tr><td width="290">error.<strong>code</strong></td><td width="70">0..1</td><td width="150">string</td><td>Error code.</td></tr>
<tr><td width="290">error.<strong>message</strong></td><td width="70">0..1</td><td width="150">string</td><td>Error message description.</td></tr></tbody>
</table>


## FtrConfig

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
<tr><td width="290">package-name</td><td width="70">0..1</td><td width="150">string</td><td>Name of the FHIR terminology package.</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td>Module that this configuration belongs to.</td></tr>
<tr><td width="290">tag</td><td width="70">0..1</td><td width="150">string</td><td>Tag for terminology resources.</td></tr>
<tr><td width="290">tag-index</td><td width="70">0..1</td><td width="150"></td><td>Index of tags and their associations.</td></tr></tbody>
</table>


## TerminologyBundleFile

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
<tr><td width="290">filename</td><td width="70">0..1</td><td width="150">string</td><td>Name of the terminology bundle file.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the bundle file processing. 

<strong>Allowed values</strong>: pending | in-progress | fail | success</td></tr></tbody>
</table>


## IndexCreationJob

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
<tr><td width="290">procstatus</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the index creation process. 

<strong>Allowed values</strong>: pending | in-progress | done | error</td></tr>
<tr><td width="290">message</td><td width="70">0..1</td><td width="150">string</td><td>Status or error message.</td></tr>
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">string</td><td>Resource type for which the index is being created.</td></tr>
<tr><td width="290">index</td><td width="70">0..1</td><td width="150">string</td><td>Name of the database index being created.</td></tr>
<tr><td width="290">params</td><td width="70">0..*</td><td width="150">string</td><td>Parameters for the index creation.</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the index creation started.</td></tr>
<tr><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the index creation finished.</td></tr></tbody>
</table>


## Lambda

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
<tr><td width="290">hook</td><td width="70">1..1</td><td width="150">code</td><td>Type of hook this lambda responds to. 

<strong>Allowed values</strong>: audit</td></tr>
<tr><td width="290">code</td><td width="70">1..1</td><td width="150">string</td><td>Code to be executed by the lambda.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>


## SeedImport

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
<tr><td width="290">version</td><td width="70">0..1</td><td width="150">string</td><td>Version of the seed import format. 

<strong>Allowed values</strong>: v2</td></tr>
<tr><td width="290">resources</td><td width="70">0..*</td><td width="150"></td><td>Resources to be imported.</td></tr>
<tr><td width="290">resources.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource.</td></tr>
<tr><td width="290">resources.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier for the resource.</td></tr>
<tr><td width="290">resources.<strong>meta</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Metadata for the resource.</td></tr>
<tr><td width="290">filename</td><td width="70">0..1</td><td width="150">string</td><td>Name of the seed import file.</td></tr>
<tr><td width="290">md5-hash</td><td width="70">0..1</td><td width="150">string</td><td>MD5 hash of the import file for integrity verification.</td></tr></tbody>
</table>


## SubsNotification

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
<tr><td width="290">retried</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether this notification is a retry.</td></tr>
<tr><td width="290">retryOf</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the original notification this is retrying. 

<strong>Allowed references</strong>: SubsSubscription</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">code</td><td>Status of the notification delivery. 

<strong>Allowed values</strong>: success | failed</td></tr>
<tr><td width="290">subscription</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the subscription that generated this notification. 

<strong>Allowed references</strong>: SubsSubscription</td></tr>
<tr><td width="290">retries</td><td width="70">0..*</td><td width="150">Reference</td><td>References to retry notifications. 

<strong>Allowed references</strong>: SubsSubscription</td></tr>
<tr><td width="290">duration</td><td width="70">0..1</td><td width="150">integer</td><td>Duration of the notification processing in milliseconds.</td></tr>
<tr><td width="290">response</td><td width="70">0..1</td><td width="150">Object</td><td>Response received from the notification endpoint.</td></tr>
<tr><td width="290">notification</td><td width="70">0..1</td><td width="150">Object</td><td>Content of the notification that was sent.</td></tr></tbody>
</table>


## SubsSubscription

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
<tr><td width="290">trigger</td><td width="70">0..1</td><td width="150">Object</td><td>Events that trigger this subscription.</td></tr>
<tr><td width="290">channel</td><td width="70">1..1</td><td width="150"></td><td>Channel configuration for delivering notifications.</td></tr>
<tr><td width="290">channel.<strong>timeout</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Timeout in milliseconds for notification delivery.</td></tr>
<tr><td width="290">channel.<strong>heartbeatPeriod</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Period in milliseconds for sending heartbeat notifications.</td></tr>
<tr><td width="290">channel.<strong>headers</strong></td><td width="70">0..1</td><td width="150">Object</td><td>HTTP headers to include with notifications.</td></tr>
<tr><td width="290">channel.<strong>type</strong></td><td width="70">1..1</td><td width="150">code</td><td>Type of channel for notifications. 

<strong>Allowed values</strong>: rest-hook</td></tr>
<tr><td width="290">channel.<strong>endpoint</strong></td><td width="70">1..1</td><td width="150">url</td><td>URL endpoint where notifications are sent.</td></tr>
<tr><td width="290">channel.<strong>payload</strong></td><td width="70">0..1</td><td width="150"></td><td>Content to be sent in the notification.</td></tr>
<tr><td width="290">identifier</td><td width="70">0..*</td><td width="150">Identifier</td><td>Business identifiers for the subscription.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">code</td><td>Current status of the subscription. 

<strong>Allowed values</strong>: active | off</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr></tbody>
</table>

