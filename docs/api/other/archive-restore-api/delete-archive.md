---
description: Delete archived FHIR resource data from AWS S3 or GCP Cloud Storage using delete-archive task.
---

# Delete-archive

To delete archived data **from cloud** you should start deleting process with `/execution/aidbox.archive/delete-archive` task.

### delete-archive properties:

<table><thead><tr><th width="169">Properties</th><th width="112.33333333333331" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>archiveId</td><td>true</td><td><code>archiveId</code> property of succeeded <a href="create-archive.md">create-archive</a> status response if it archived at least 1 row.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```http
POST /execution/aidbox.archive/delete-archive
accept: text/yaml
content-type: text/yaml

archiveId: 07738389-196d-4879-a2da-04e9cd9272
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 201

id: 646fbd75-7365-4422-938a-0ba8d0efcb0e

```
{% endtab %}
{% endtabs %}

To fetch status of the task make `/execution/aidbox.archive/delete-archive/[task_id]` request with id from delete-archive response:

{% tabs %}
{% tab title="Request" %}
```http
GET /execution/aidbox.archive/delete-archive/646fbd75-7365-4422-938a-0ba8d0efcb0e
accept: text/yaml
content-type: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

definition: aidbox.archive/delete-archive
meta:
  lastUpdated: '2023-01-10T11:18:29.355638Z'
  createdAt: '2023-01-10T11:18:28.803714Z'
  versionId: '26064'
params:
  archiveId: 07738389-196d-4879-a2da-04e9cd9272
retryCount: 1
outcome: succeeded
resourceType: AidboxTask
requester:
  id: >-
    admin
  resourceType: User
status: done
result:
  unmarkedResourcesCount: 30
execId: 1fd9ecee-d093-4949-9d73-15cba78ada7a
id: >-
  646fbd75-7365-4422-938a-0ba8d0efcb0e
```
{% endtab %}
{% endtabs %}
