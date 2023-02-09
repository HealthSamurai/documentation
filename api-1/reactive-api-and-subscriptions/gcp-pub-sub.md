---
description: Aidbox has support of GCP Pub/Sub integration
---

# GCP Pub/Sub

{% hint style="info" %}
GCP Pub/Sub integration currently is in _Alpha_&#x20;
{% endhint %}

Any create, update, delete operations on all resources in Aidbox may be published to GCP Pub/Sub topic.

In order to enable GCP Pub/Sub integration you need to provide few env variables:

```
BOX_SUBSCRIPTIONS_PUBSUB_PROJECT=<GCP_PROJECT_NAME>
BOX_SUBSCRIPTIONS_PUBSUB_TOPIC=<GCP_TOPIC_NAME>
BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_EMAIL=<GCP_SERVICE_ACCOUNT_EMAIL>
BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_PRIVATE__KEY=<GCP_SERVICE_ACCOUNT_PRIVATE_KEY (without line breaks)>
```

Aidbox doesn't create the topic, specified in `BOX_SUBSCRIPTIONS_PUBSUB_TOPIC`.

On every resource update, Aidbox will publish to GCP Pub/Sub message with empty body and few attributes:

* resourceType — resource type of updated resource
* id — id of updated resource
* versionId — version id of updated resource
* action — action (create, update, delete)
* box-id — Aidbox id.

#### Running Pub/Sub Integration with local emulator

You can enable GCP Pub/Sub integration to work with local Pub/Sub emulator. In that case you need to provide these env variables:

```
BOX_SUBSCRIPTIONS_PUBSUB_PROJECT=test_project
BOX_SUBSCRIPTIONS_PUBSUB_TOPIC=test_topic
BOX_SUBSCRIPTIONS_PUBSUB_EMULATOR__URL=http://localhost:8264
```
