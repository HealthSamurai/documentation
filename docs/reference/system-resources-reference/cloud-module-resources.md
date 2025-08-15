# Cloud Module Resources

Resources for configuration and management Aidbox integration with cloud providers.

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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">access-key-id</td><td width="70">0..1</td><td width="150">string</td><td>AWS access key identifier for authentication.</td></tr>
<tr><td width="290">host</td><td width="70">0..1</td><td width="150">string</td><td>AWS host endpoint for the service.</td></tr>
<tr><td width="290">path-style</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to use path-style addressing for S3 requests.</td></tr>
<tr><td width="290">region</td><td width="70">0..1</td><td width="150">string</td><td>AWS region where the resources are located.</td></tr>
<tr><td width="290">secret-access-key</td><td width="70">0..1</td><td width="150">string</td><td>AWS secret access key for authentication.</td></tr>
<tr><td width="290">use-ssl</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to use SSL for secure connections.</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">clientId</td><td width="70">0..1</td><td width="150">string</td><td>Azure AD application/client ID (required for User Delegation SAS). Available since 2508.</td></tr>
<tr><td width="290">clientSecret</td><td width="70">0..1</td><td width="150">string</td><td>Azure AD client secret (required for User Delegation SAS). Available since 2508.</td></tr>
<tr><td width="290">key</td><td width="70">0..1</td><td width="150">string</td><td>Azure storage account key for authentication (required for Account SAS).</td></tr>
<tr><td width="290">sasType</td><td width="70">0..1</td><td width="150">string</td><td>SAS type: 'account' (default) or 'user-delegation'. Available since 2508.</td></tr>
<tr><td width="290">tenantId</td><td width="70">0..1</td><td width="150">string</td><td>Azure AD tenant ID (required for User Delegation SAS).</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">account</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the Azure account credentials. 

<strong>Allowed references</strong>: AzureAccount</td></tr>
<tr><td width="290">container</td><td width="70">0..1</td><td width="150">string</td><td>Name of the Azure storage container.</td></tr>
<tr><td width="290">extension</td><td width="70">0..1</td><td width="150">string</td><td>File extension for content stored in the container.</td></tr>
<tr><td width="290">storage</td><td width="70">0..1</td><td width="150">string</td><td>Azure storage account name.</td></tr></tbody>
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
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">private-key</td><td width="70">0..1</td><td width="150">string</td><td>Private key for GCP service account authentication.</td></tr>
<tr><td width="290">service-account-email</td><td width="70">0..1</td><td width="150">string</td><td>Email address of the GCP service account.</td></tr></tbody>
</table>

