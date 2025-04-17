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
<tr><td width="290">every</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: periodic | each-day</td></tr>
<tr><td width="290">at</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">stop</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">job</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">nextStart</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">locked</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr><td width="290">box</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">auth</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.<strong>keys</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">auth.<strong>keys</strong>.<strong>secret</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">auth.<strong>keys</strong>.<strong>public</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">dateTime</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: pending | done | error</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr><td width="290">bind</td><td width="70">1..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">schema</td><td width="70">1..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">query</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">count-query</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">enable-links</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: query | execute</td></tr>
<tr><td width="290">omit-sql</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr></tbody>
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
<tr><td width="290">resource</td><td width="70">1..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">total</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">as</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">limit</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">includes</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">query</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">query.<strong>order-by</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">query.<strong>join</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">query.<strong>where</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">designation</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">designation.<strong>definition</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">designation.<strong>display</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">ancestors</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">valueset</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">hierarchy</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">deprecated</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">property</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">system</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">code</td><td width="70">1..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">meta.<strong>pre-sql</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta.<strong>post-sql</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">version</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">fhirCode</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">transform</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">transform.<strong>request</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">transform.<strong>request</strong>.<strong>engine</strong></td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr><td width="290">transform.<strong>request</strong>.<strong>template</strong></td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">transform.<strong>request</strong>.<strong>part</strong></td><td width="70">1..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: body</td></tr>
<tr><td width="290">route-params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">fhirResource</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">no-op-logs</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">implicit-params</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">implicit-params.<strong>path</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">implicit-params.<strong>query</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">request</td><td width="70">0..*</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">fhirUrl</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">app</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: App</td></tr>
<tr><td width="290">public</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">data</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">where</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">param-parser</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: token | reference</td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">order-by</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">token-sql</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">token-sql.<strong>text</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">token-sql.<strong>both</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">token-sql.<strong>only-system</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">token-sql.<strong>only-code</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">token-sql.<strong>text-format</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">token-sql.<strong>no-system</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">multi</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: array</td></tr>
<tr><td width="290">format</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">migrations</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">migrations.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">migrations.<strong>dateTime</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">migrations.<strong>action</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: app | addon</td></tr>
<tr><td width="290">endpoint</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">endpoint.<strong>secret</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">endpoint.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">endpoint.<strong>type</strong></td><td width="70">0..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: http-rpc | ws-rpc | native</td></tr>
<tr><td width="290">apiVersion</td><td width="70">1..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">hooks</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">subscriptions</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">entities</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">operations</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr><td width="290">archiveFile</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">serviceAccount</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">serviceAccount.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">serviceAccount.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">serviceAccount.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">serviceAccount.<strong>secret-key</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">bucket</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">archivedResourcesCount</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">criteriaPaths</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">history</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">storageBackend</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: gcp | aws | local</td></tr>
<tr><td width="290">period</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">period.<strong>start</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">period.<strong>end</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">targetResourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">resource.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resource.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resource.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">run</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">run.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">run.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">run.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">profiles</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">errors</td><td width="70">0..*</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">errorsThreshold</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">schemas</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">invalid</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">async</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">limit</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">profiles</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">valid</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: in-progress | complete</td></tr>
<tr><td width="290">filter</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">duration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr></tbody>
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
<tr><td width="290">data_type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: smallint | integer | bigint</td></tr>
<tr><td width="290">cycle</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">increment</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">minvalue</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">maxvalue</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr></tbody>
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
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: sync | async</td></tr>
<tr><td width="290">resources</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">resource</td><td width="70">1..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">isUnique</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">refers</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">isCollection</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">extensionUrl</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">valueSet</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">valueSet.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">valueSet.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">valueSet.<strong>uri</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">union</td><td width="70">0..*</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">path</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">order</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">isSummary</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">isOpen</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">isModifier</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">isRequired</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">enum</td><td width="70">0..*</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">sequencePrefix</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">schema</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">nonPersistable</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">isMeta</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">history</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: none | diff</td></tr>
<tr><td width="290">type</td><td width="70">1..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: abstract | resource | type | primitive</td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">isOpen</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">idGeneration</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: sequence | uuid</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">user</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: User</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: http | sql</td></tr>
<tr><td width="290">command</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">origin</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">tags</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">notebook-superuser-secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">edit-secret</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">publication-id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells.<strong>id</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells.<strong>evaluating?</strong></td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">cells.<strong>folded</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells.<strong>folded</strong>.<strong>code</strong></td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">cells.<strong>folded</strong>.<strong>result</strong></td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">cells.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: rpc | rest | empty | markdown | sql</td></tr>
<tr><td width="290">cells.<strong>value</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells.<strong>result</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells.<strong>error</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">cells.<strong>nb-title</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">returns</td><td width="70">0..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: transaction | resource</td></tr>
<tr><td width="290">body</td><td width="70">1..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">params.<strong>omit-drop-blanks</strong></td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">scopeSchema</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">text.<strong>status</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text.<strong>div</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">unmapped</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">unmapped.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">unmapped.<strong>mode</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">sourceValueSet</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">element</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">element.<strong>target</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">element.<strong>target</strong>.<strong>comment</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">element.<strong>target</strong>.<strong>equivalence</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">target</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">conceptmapUrl</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">targetValueSet</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">conceptmapId</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: in-progress | done | fail</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">result.<strong>count</strong></td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">error.<strong>code</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error.<strong>message</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">package-name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">module</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">tag</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">tag-index</td><td width="70">0..1</td><td width="150"></td><td></td></tr></tbody>
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
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">filename</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>

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
<tr><td width="290">procstatus</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: pending | in-progress | done | error</td></tr>
<tr><td width="290">message</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resource</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">index</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr></tbody>
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
<tr><td width="290">hook</td><td width="70">1..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: audit</td></tr>
<tr><td width="290">code</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">version</td><td width="70">0..1</td><td width="150">string</td><td>

<strong>Allowed values</strong>: v2</td></tr>
<tr><td width="290">resources</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr><td width="290">resources.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resources.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resources.<strong>meta</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">filename</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">md5-hash</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">retried</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">retryOf</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: SubsSubscription</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: success | failed</td></tr>
<tr><td width="290">subscription</td><td width="70">0..1</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: SubsSubscription</td></tr>
<tr><td width="290">retries</td><td width="70">0..*</td><td width="150">Reference</td><td>

<strong>Allowed references</strong>: SubsSubscription</td></tr>
<tr><td width="290">duration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">response</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">notification</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
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
<tr><td width="290">trigger</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">channel</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">channel.<strong>timeout</strong></td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">channel.<strong>heartbeatPeriod</strong></td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">channel.<strong>headers</strong></td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">channel.<strong>type</strong></td><td width="70">1..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: rest-hook</td></tr>
<tr><td width="290">channel.<strong>endpoint</strong></td><td width="70">1..1</td><td width="150">url</td><td></td></tr>
<tr><td width="290">channel.<strong>payload</strong></td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">identifier</td><td width="70">0..*</td><td width="150">Identifier</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">code</td><td>

<strong>Allowed values</strong>: active | off</td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>

