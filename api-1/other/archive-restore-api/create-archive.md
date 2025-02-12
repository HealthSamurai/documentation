# create-archive

`/execution/aidbox.archive/create-archive` is designed to create and start archiving task. It returns reference to the created archive task.

### create-archive properties:

<table><thead><tr><th width="216">Properties</th><th width="113.33333333333331" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>targetResourceType</td><td>true</td><td>Name of the resource to be archived.</td></tr><tr><td>history</td><td>false</td><td><p>Archives only history records if <strong>true</strong>.</p><p>Archives only last records if <strong>false</strong>.<br>False by default.</p></td></tr><tr><td>criteriaPaths</td><td>true</td><td><p>Array of paths to define <strong>period/retentionPeriod</strong> properties. Use dots to separate nested properties.</p><p>For example: <code>["period.end", "recorded"]</code></p></td></tr><tr><td>period/retentionPeriod</td><td>true</td><td><p>Use <strong>period</strong> to specify period for which resources should be archived.</p><p>For example:</p><p><code>{"start": "2022-01-01",</code></p><p><code>"end": "2022-02-01"}</code></p><p><br>You may also use <strong>retentionPeriod</strong> instead to specify period of time for which the resources should be retained.</p><p>Available <em>units</em>: "day", "week", "month", "year".</p><p>For example:</p><p><code>{"value": 2, "unit": "day"}</code></p></td></tr><tr><td>storageBackend</td><td>true</td><td>Use this property to specify type of your storage.<br>Available types: "gcp", "aws".</td></tr><tr><td>serviceAccount</td><td>true</td><td><p>Reference to <a href="../../../storage-1/other/s3-compatible-storages/gcp-cloud-storage.md#create-gcpserviceaccount">GcpServiceAccount</a> or <a href="../../../storage-1/other/s3-compatible-storages/aws-s3.md#setup-awsaccount">AwsAccount</a> resource in your Aidbox database. Make sure that you already create one.</p><p>For example: <code>{"id": "my-account", "resourceType": "GcpServiceAccount"}</code></p></td></tr><tr><td>bucket</td><td>true</td><td>Name of the bucket where the data will be uploaded.</td></tr><tr><td>pruneArchivedData</td><td>false</td><td>Delete archived rows if <strong>true</strong>.<br><strong>False</strong> by default.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```http
POST /execution/aidbox.archive/create-archive
accept: text/yaml
content-type: text/yaml

targetResourceType: AuditEvent
period:
  end: '2022-10-15'
criteriaPaths:
  - recorded
storageBackend: gcp
bucket: aidbox-archive-test
pruneArchivedData: true
serviceAccount:
  id: my-account
  resourceType: GcpServiceAccount
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 201

id: 9ed444a3-8344-40f5-bba2-d9a5728e7471
```
{% endtab %}
{% endtabs %}

To fetch status of the task make `/execution/aidbox.archive/create-archive/[task_id]` request with id from create-archive response:

{% hint style="info" %}
If task has been executed successfully you will receive `archiveId`. You should use this property as parameter for [restore-archive](restore-archive.md), [delete-archive](delete-archive.md) and [prune-archived-data](prune-archived-data.md) tasks and to [get archive summary](create-archive.md#get-archive-summary).
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```http
GET /execution/aidbox.archive/create-archive/9ed444a3-8344-40f5-bba2-d9a5728e7471
accept: text/yaml
content-type: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

definition: aidbox.archive/create-archive
meta:
  lastUpdated: '2023-01-09T16:16:41.327914Z'
  createdAt: '2023-01-09T16:16:40.614378Z'
  versionId: '25739'
params:
  bucket: aidbox-archive-test
  period:
    end: '2022-10-15'
  criteriaPaths:
    - recorded
  serviceAccount:
    id: my-account
    resourceType: GcpServiceAccount
  storageBackend: gcp
  targetResourceType: AuditEvent
retryCount: 1
outcome: succeeded
resourceType: AidboxTask
requester:
  id: admin
  resourceType: User
status: done
result:
  outcome: archived
  archiveId: 07738389-196d-4879-a2da-04e9cd927203
  archiveName: AuditEvent/inf_2022-10-15_39c409de.ndjson.gz
  deletedRowsCount: 0
  archivedResourcesCount: 463
execId: 9e34b00d-4855-4c95-bfd9-bee69034ea24
id: 9ed444a3-8344-40f5-bba2-d9a5728e7471
```
{% endtab %}
{% endtabs %}

### Get archive summary

{% tabs %}
{% tab title="Request" %}
```http
GET AidboxArchive/08b0004b-e383-4c43-bacd-d05c5e2d1560
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

serviceAccount:
  id: >-
    my-account
  resourceType: GcpServiceAccount
meta:
  lastUpdated: '2023-01-11T10:28:16.545888Z'
  createdAt: '2023-01-11T10:28:16.545888Z'
  versionId: '141'
storageBackend: gcp
resourceType: AidboxArchive
id: >-
  08b0004b-e383-4c43-bacd-d05c5e2d1560
archiveFile: AuditEvent/inf_2022-10-15_0dfa7007.ndjson.gz
period:
  end: '2022-10-15'
targetResourceType: AuditEvent
criteriaPaths:
  - recorded
archivedResourcesCount: 24
bucket: aidbox-archive-test

```
{% endtab %}
{% endtabs %}
