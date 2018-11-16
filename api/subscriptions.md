---
description: >-
  Page describes FHIR Subscriptions API and additional long-polling endpoint
  provided by Aidbox.
---

# Subscriptions

Aidbox implements [FHIR Subscriptions API](https://www.hl7.org/fhir/subscription.html) to notify interested 3rd parties about newly created/updated resources which meets certain criteria. Additionally to Subscription's standard push-based delivery mechanism to the endpoint specified in `Subscription.channel`, Aidbox provides long-polling endpoint as a `$poll` operation on Subscription resource.

### Long-Polling Subscription Notifications

Long-Polling is an robust and easy-to-implement mechanism to get instant notification about CRUD event through HTTP protocol. The client makes a requests to get the data \(notifications\) he haven't already received. If the server has newer notifications for the client, he responds with them. But when no new notifications are available for the client, instead of responding with empty body, server keeps HTTP connection open for some time and waits for event to happen, then responds with notification about the event happened. If the request was interrupted \(because of timeout, for example\), client issues same request again.

As a response, `$poll` endpoint returns a FHIR Bundle containing matched resources. Every resource has a `meta` element with `versionId` and `lastUpdated` keys. `versionId` element is quite important in context of polling because it contains a transaction ID of operation which caused the event. It's an integer and it's always increasing, which means if event B happened after event A, then B's `versionId` will be greater than A's `versionId`. Client can specify last `versionId` it received with `from` parameter in the request's query string. In this case `$poll` endpoint will return only notifications which has `versionId` greater than number provided in `from` parameter. This approach guarantees that client will never miss a notification because of time spans between requests to the `$poll` endpoint.

To be able to call `$poll` operation, Subscription resource should be already created, it should has `Subscription.status` equal to `active` and `Subscription.criteria` should specify a resource type \(i.e. `Observation` or `Observation?code=xxxx`\). If Subscription resource does not meet those requirements, `$poll` operation will return`403 Invalid Request` response with OperationOutcome resource containing error message.

Example request to the `$poll` operation to get all notifications ever happened:

{% tabs %}
{% tab title="Request" %}
```text
GET /fhir/Subscription/test/$poll?from=0
Content-Type: application/json
```
{% endtab %}

{% tab title="Response" %}
```text
{
 "resourceType": "Bundle",
 "type": "collection",
 "entry": [
  {
   "resource": {
    "name": [
     {
      "given": [
       "mike"
      ],
      "family": "lapshin"
     }
    ],
    "id": "a90d5dc3-f4fa-49d7-a045-2f4caf8349a5",
    "resourceType": "Patient"
   },
   "txid": 90,
   "event": "created"
  },
  {
   "resource": {
    "name": [
     {
      "given": [
       "mike"
      ],
      "family": "lapshin"
     }
    ],
    "id": "b7e7dbf2-f30b-4fca-9051-b3ea37fe49b6",
    "resourceType": "Patient"
   },
   "txid": 91,
   "event": "created"
  },
  ...
 ]
}
```
{% endtab %}
{% endtabs %}

Example request to the `$poll` operation to get most recent notification \(no `from` parameter\):

```text
GET /fhir/Subscription/test/$poll
Content-Type: application/json
```

