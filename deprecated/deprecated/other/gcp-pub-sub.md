---
description: Aidbox has support of GCP Pub/Sub integration
---

# GCP Pub/Sub

## Note

This is old, not stable version.

Since version 2309 Aidbox has GCP Pub/Sub integration via Topic-Based Subscriptions, which is ready for production use.

{% content-ref url="../zen-related/topic-based-subscriptions/" %}
[topic-based-subscriptions](../zen-related/topic-based-subscriptions/)
{% endcontent-ref %}

## GCP Pub/Sub

{% hint style="info" %}
GCP Pub/Sub integration currently is in _Alpha_
{% endhint %}



Any create, update, or delete operations on all resources in Aidbox may be published to a GCP Pub/Sub topic.

In order to enable GCP Pub/Sub integration you need to provide a few env variables:

```
BOX_SUBSCRIPTIONS_PUBSUB_PROJECT=<GCP_PROJECT_NAME>
BOX_SUBSCRIPTIONS_PUBSUB_TOPIC=<GCP_TOPIC_NAME>
BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_EMAIL=<GCP_SERVICE_ACCOUNT_EMAIL>
BOX_SUBSCRIPTIONS_PUBSUB_SERVICE__ACCOUNT_PRIVATE__KEY=<GCP_SERVICE_ACCOUNT_PRIVATE_KEY (without line breaks)>
```

Aidbox doesn't create the topic, specified in `BOX_SUBSCRIPTIONS_PUBSUB_TOPIC`.

On every resource update, Aidbox will publish to GCP Pub/Sub message with empty body and few attributes:

* resourceType — resource type of updated resource
* id — id of the updated resource
* versionId — version id of the updated resource
* action — action (create, update, delete)
* box-id — Aidbox id.

#### Running Pub/Sub Integration with a local emulator

You can enable GCP Pub/Sub integration to work with a local Pub/Sub emulator. In that case, you need to provide these env variables:

```
BOX_SUBSCRIPTIONS_PUBSUB_PROJECT=test_project
BOX_SUBSCRIPTIONS_PUBSUB_TOPIC=test_topic
BOX_SUBSCRIPTIONS_PUBSUB_EMULATOR__URL=http://localhost:8264
```

## Publish a message before a resource is saved to a Database

By default message about change is published after the change is written to a Database. This approach makes the situation possible when a change was performed, but the message was not published.

Set

```
BOX_SUBSCRIPTIONS_PUBSUB_BEFORE__SAVE=true
```

to publish a message before a change is saved to Database.

`versionId` attribute of a message will store the id of the version **before the update** of `nil` for `create` operation.

{% hint style="info" %}
With this option enabled, the situation is possible, where a message is published but no corresponding change was actually saved to Database.
{% endhint %}

## Specify resource types and boxes for which to publish notification

By default, messages for all resource types from all boxes are published. To specify, what resource types and boxes should trigger a notification set

```
BOX_SUBSCRIPTIONS_PUBSUB_RESOURCE__TYPES="<rt>:?<box-id>?( <rt>:?<box-id>?)*"
// Examples:
// notify only on Patient or Encounter resources change from any box. 
BOX_SUBSCRIPTIONS_PUBSUB_RESOURCE__TYPES="Patient Encounter"

// notify on Patient changes from boxone or boxtwo
// and about Encounter from any box. 
BOX_SUBSCRIPTIONS_PUBSUB_RESOURCE__TYPES="Patient:boxone Patient:boxtwo Encounter"
```

## Multibox: specify topic per box

It's possible to specify different topics per box. Topic without box specification treated as default.

```
// For the box with an id `box-one` `box-one-topic` will be used. For all other boxes
`default-topic` will be used. 
BOX_SUBSCRIPTIONS_PUBSUB_TOPIC="default-topic box-one-topic:box-one"

```
