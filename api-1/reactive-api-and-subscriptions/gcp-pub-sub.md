---
description: Aidbox has support of GCP Pub/Sub integration
---

# GCP Pub/Sub

Any create, update, delete operations on all resources in Aidbox may be published to GCP Pub/Sub topic.

In order to enable GCP Pub/Sub integration you need to provide few env variables:

```
BOX_SUBSCRIPTIONS_PUBSUB_PROJECT=<GCP_PROJECT_NAME>
BOX_SUBSCRIPTIONS_PUBSUB_TOPIC=<GCP_TOPIC_NAME>
BOX_SUBSCRIPTIONS_PUBSUB_SERVICE_ACCOUNT=<GCP_SERVICE_ACCOUNT_ID_IN_AIDBOX>
```

Aidbox doesn't create the topic, specified in `BOX_SUBSCRIPTIONS_PUBSUB_TOPIC`. In order to create GcpServiceAccount in Aidbox follow [Create GcpServiceAccount paragraph in the doc](../../storage-1/gcp-cloud-storage.md#create-gcpserviceaccount).

On every resource update, Aidbox will publish to GCP Pub/Sub message with empty body and few attributes:

* resourceType — resource type of updated resource
* id — id of updated resource
* versionId — version id of updated resource
* action — action (create, update, delete)
* box-id — Aidbox id.
