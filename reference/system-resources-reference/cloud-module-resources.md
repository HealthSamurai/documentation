# Cloud Module Resources

## Overview

Cloud module includes the following resource types:

- AwsAccount
- AzureAccount
- AzureContainer
- GcpServiceAccount

## AwsAccount

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
<tr class="top-element"><td width="290">access-key-id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">host</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">secret-access-key</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">path-style</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">region</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">use-ssl</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr></tbody>
</table>


## AzureAccount

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
<tr class="top-element"><td width="290">key</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## AzureContainer

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
<tr class="top-element"><td width="290">storage</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">extension</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">container</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">account</td><td width="70">0..1</td><td width="150">Reference</td><td>References: AzureAccount</td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## GcpServiceAccount

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
<tr class="top-element"><td width="290">private-key</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">service-account-email</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>

