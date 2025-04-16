# Base Module Resources

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
- aidbox-core-aidboxjobstatus-job
- aidbox-core-concept-system
- aidbox-core-concept-valueset
- aidbox-core-concept-deprecated
- aidbox-core-concept-code
- aidbox-core-concept-display
- aidbox-core-concept-hierarchy
- aidbox-core-concept-codetext

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
<tr class="top-element"><td width="290">every</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">at</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">stop</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="top-element"><td width="290">job</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element"><td width="290">error</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="top-element"><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">nextStart</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="top-element"><td width="290">locked</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">result</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">box</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="top-element"><td width="290">auth</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">auth.keys</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">auth.keys.secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">auth.keys.public</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">dateTime</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">result</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr class="top-element required-field"><td width="290">bind</td><td width="70">1..1</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element required-field"><td width="290">schema</td><td width="70">1..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element required-field"><td width="290">query</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">count-query</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">enable-links</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">omit-sql</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr></tbody>
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
<tr class="top-element required-field"><td width="290">resource</td><td width="70">1..1</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element"><td width="290">total</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">as</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">limit</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">includes</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">query</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">query.order-by</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">query.join</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">query.where</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">designation</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">designation.definition</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">designation.display</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">ancestors</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">valueset</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">hierarchy</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">deprecated</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">property</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">system</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">code</td><td width="70">1..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="nested-element"><td width="290">meta.pre-sql</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">meta.post-sql</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">version</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">fhirCode</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">transform</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">transform.request</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">transform.request.engine</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr class="nested-element"><td width="290">transform.request.template</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">transform.request.part</td><td width="70">1..1</td><td width="150">string</td><td>Allowed values: body</td></tr>
<tr class="top-element"><td width="290">route-params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">fhirResource</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">no-op-logs</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">implicit-params</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">implicit-params.path</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">implicit-params.query</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">request</td><td width="70">0..*</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">fhirUrl</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">app</td><td width="70">0..1</td><td width="150">Reference</td><td>References: App</td></tr>
<tr class="top-element"><td width="290">public</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">data</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">resource</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element"><td width="290">where</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">param-parser</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">order-by</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">token-sql</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">token-sql.text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">token-sql.both</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">token-sql.only-system</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">token-sql.only-code</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">token-sql.text-format</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">token-sql.no-system</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">multi</td><td width="70">0..1</td><td width="150">string</td><td>Allowed values: array</td></tr>
<tr class="top-element"><td width="290">format</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">migrations</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">migrations.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">migrations.dateTime</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">migrations.action</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">type</td><td width="70">1..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element"><td width="290">endpoint</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">endpoint.secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">endpoint.url</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">endpoint.type</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element required-field"><td width="290">apiVersion</td><td width="70">1..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">hooks</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">subscriptions</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">entities</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">operations</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">archiveFile</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">serviceAccount</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">serviceAccount.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">serviceAccount.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">serviceAccount.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">serviceAccount.secret-key</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">bucket</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">archivedResourcesCount</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr class="top-element required-field"><td width="290">criteriaPaths</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">history</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element required-field"><td width="290">storageBackend</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">period</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">period.start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="nested-element"><td width="290">period.end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="top-element required-field"><td width="290">targetResourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element required-field"><td width="290">resource</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">resource.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">resource.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">resource.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">run</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">run.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">run.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">run.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">profiles</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">errors</td><td width="70">0..*</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr class="top-element required-field"><td width="290">resource</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">errorsThreshold</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">schemas</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">invalid</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">async</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">limit</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">profiles</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">valid</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">filter</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">duration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">data_type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">cycle</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">increment</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">start</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">minvalue</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">maxvalue</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resources</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element required-field"><td width="290">resource</td><td width="70">1..1</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element"><td width="290">isUnique</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">refers</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">isCollection</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">extensionUrl</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">valueSet</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">valueSet.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">valueSet.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">valueSet.uri</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">union</td><td width="70">0..*</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element required-field"><td width="290">path</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">order</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr class="top-element"><td width="290">isSummary</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">isOpen</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">isModifier</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">isRequired</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">enum</td><td width="70">0..*</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">sequencePrefix</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">nonPersistable</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">isMeta</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">history</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">isOpen</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">idGeneration</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>References: User</td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">command</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">command</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>References: User</td></tr>
<tr class="top-element"><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">origin</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">tags</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">notebook-superuser-secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">edit-secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">publication-id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">source</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="top-element"><td width="290">cells</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr class="nested-element required-field"><td width="290">cells.id</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">cells.evaluating?</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="nested-element"><td width="290">cells.folded</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">cells.folded.code</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="nested-element"><td width="290">cells.folded.result</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">cells.type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">cells.value</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">cells.result</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">cells.error</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">cells.nb-title</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">returns</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element required-field"><td width="290">body</td><td width="70">1..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">params.omit-drop-blanks</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">scopeSchema</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">text</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">text.status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">text.div</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">unmapped</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">unmapped.url</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">unmapped.mode</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">sourceValueSet</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">element</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element"><td width="290">element.target</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">element.target.comment</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">element.target.equivalence</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">target</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">conceptmapUrl</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">targetValueSet</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">conceptmapId</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">result.count</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">error.code</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">error.message</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">package-name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">tag</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">tag-index</td><td width="70">0..1</td><td width="150"></td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">filename</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">procstatus</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">message</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resource</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">index</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">params</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr></tbody>
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
<tr class="top-element required-field"><td width="290">hook</td><td width="70">1..1</td><td width="150">code</td><td>Allowed values: audit</td></tr>
<tr class="top-element required-field"><td width="290">code</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">version</td><td width="70">0..1</td><td width="150">string</td><td>Allowed values: v2</td></tr>
<tr class="top-element"><td width="290">resources</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr class="nested-element required-field"><td width="290">resources.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">resources.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">resources.meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">filename</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">md5-hash</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">retried</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">retryOf</td><td width="70">0..1</td><td width="150">Reference</td><td>References: SubsSubscription</td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element"><td width="290">subscription</td><td width="70">0..1</td><td width="150">Reference</td><td>References: SubsSubscription</td></tr>
<tr class="top-element"><td width="290">retries</td><td width="70">0..*</td><td width="150">Reference</td><td>References: SubsSubscription</td></tr>
<tr class="top-element"><td width="290">duration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">response</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">notification</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr class="top-element"><td width="290">trigger</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="top-element required-field"><td width="290">channel</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">channel.timeout</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="nested-element"><td width="290">channel.heartbeatPeriod</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="nested-element"><td width="290">channel.headers</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">channel.type</td><td width="70">1..1</td><td width="150">code</td><td>Allowed values: rest-hook</td></tr>
<tr class="nested-element required-field"><td width="290">channel.endpoint</td><td width="70">1..1</td><td width="150">url</td><td></td></tr>
<tr class="nested-element"><td width="290">channel.payload</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr class="top-element"><td width="290">identifier</td><td width="70">0..*</td><td width="150">Identifier</td><td></td></tr>
<tr class="top-element"><td width="290">status</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## aidbox-core-aidboxjobstatus-job

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
</tbody>
</table>


## aidbox-core-concept-system

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
</tbody>
</table>


## aidbox-core-concept-valueset

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
</tbody>
</table>


## aidbox-core-concept-deprecated

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
</tbody>
</table>


## aidbox-core-concept-code

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
</tbody>
</table>


## aidbox-core-concept-display

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
</tbody>
</table>


## aidbox-core-concept-hierarchy

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
</tbody>
</table>


## aidbox-core-concept-codetext

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
</tbody>
</table>

