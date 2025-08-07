# Prune-archived-data

To delete data that has been archived from your Aidbox database you can make `/execution/aidbox.archive/prune-archived-data` request. You also can make this automatically with `pruneArchivedData` parameter of [create-archive](create-archive.md) request.&#x20;

### prune-archived-data properties:

<table><thead><tr><th width="161">Properties</th><th width="104.33333333333331" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>archiveId</td><td>true</td><td><code>archiveId</code> property of succeeded <a href="create-archive.md">create-archive</a> response if it archived at least 1 row.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```http
POST /execution/aidbox.archive/prune-archived-data
accept: text/yaml
content-type: text/yaml

archiveId: ace351aa-ad5a-44a7-8314-532628e7d9ae
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 201

id: 51991757-8c7f-4d1c-9a49-16dbd6b68fae

```
{% endtab %}
{% endtabs %}

To fetch status of the task make `/execution/aidbox.archive/prune-archived-data/[task_id]` request with id from prune-archived-data response:

{% tabs %}
{% tab title="Request" %}
```http
GET /execution/aidbox.archive/prune-archived-data/51991757-8c7f-4d1c-9a49-16dbd6b68fae
accept: text/yaml
content-type: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

definition: aidbox.archive/prune-archived-data
meta:
  lastUpdated: '2023-01-10T11:32:40.407637Z'
  createdAt: '2023-01-10T11:32:40.367675Z'
  versionId: '26174'
params:
  archiveId: ace351aa-ad5a-44a7-8314-532628e7d9ae
retryCount: 1
outcome: succeeded
resourceType: AidboxTask
requester:
  id: >-
    admin
  resourceType: User
status: done
result:
  deleteRowsCount: 30
execId: 39fd4f76-c9a3-4e6e-ac35-74eb4241d57a
id: >-
  51991757-8c7f-4d1c-9a49-16dbd6b68fae
```
{% endtab %}
{% endtabs %}
