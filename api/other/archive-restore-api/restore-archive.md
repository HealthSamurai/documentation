# restore-archive

To restore archived data you should start restoration process with `/execution/aidbox.archive/restore-archive` task.

{% hint style="info" %}
Make sure that archived data is deleted from your database before restoring it. You can make this automatically with `pruneArchivedData` parameter of [create-archive](create-archive.md) request.

Restoring data that already stored in your database may cause duplication exceptions.
{% endhint %}

### restore-archive properties:

<table><thead><tr><th width="169">Properties</th><th width="130.33333333333331" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>archiveId</td><td>true</td><td><code>archiveId</code> property of succeeded <a href="create-archive.md">create-archive</a> status response if it archived at least 1 row.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```http
POST /execution/aidbox.archive/restore-archive
accept: text/yaml
content-type: text/yaml

archiveId: cd4d62de-10fb-452f-84c1-52ce9d6300f7
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 201

id: ff0fa264-bad5-48f9-b4b3-f9588a64265f
```
{% endtab %}
{% endtabs %}

To fetch status of the task make `/execution/aidbox.archive/restore-archive/[task_id]` request with id from restore-archive response:

{% tabs %}
{% tab title="Request" %}
```http
GET /execution/aidbox.archive/restore-archive/ff0fa264-bad5-48f9-b4b3-f9588a64265f
accept: text/yaml
content-type: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
definition: aidbox.archive/restore-archive
meta:
  lastUpdated: '2023-01-10T11:27:40.727967Z'
  createdAt: '2023-01-10T11:27:40.440402Z'
  versionId: '26143'
params:
  archiveId: cd4d62de-10fb-452f-84c1-52ce9d6300f7
retryCount: 1
outcome: succeeded
resourceType: AidboxTask
requester:
  id: >-
    admin
  resourceType: User
status: done
result:
  archiveId: cd4d62de-10fb-452f-84c1-52ce9d6300f7
  restoredResourcesCount: 30
execId: 03957f3a-0141-4148-9c44-c8646849b361
id: >-
  5f134af7-ba67-4537-b1c4-8cbb214b99ae
```
{% endtab %}
{% endtabs %}
