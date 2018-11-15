---
description: >-
  Page describes FHIR Subscriptions API and additional long-polling endpoint
  provided by Aidbox.
---

# Subscriptions

Aidbox implements [FHIR Subscriptions API](https://www.hl7.org/fhir/subscription.html) to notify interested 3rd parties about newly created/updated resources which meets certain criteria. Additionally to Subscription's standard push-based delivery mechanism to the endpoint specified in `Subscription.channel`, Aidbox provides long-polling endpoint as a `$poll` operation on Subscription resource.

### Long-Polling Subscription Notifications

Long-Polling is an robust and easy-to-implement mechanism to get instant notification about event through HTTP protocol. The client constantly makes a requests to get the data \(notifications\) he haven't already received. If the server has new notifications for the client, he responds with them. But when no new notifications are available for the client, instead of responding with empty body, server keeps HTTP connection open for some time and waits for event to happen, then responds with notification about the event happened. If the request was interrupted \(because of timeout, for example\), client issues same request again.

There is a guarantee that client won't miss a notification because of time spans between subsequent polling requests. Every notification received from `$poll` endpoint has a number called `txid` \(transaction ID\), and client should keep track of the last `txid` he received. With every request to the `$poll` operation he provides this last `txid`, and Aidbox guarantees to return all the notifications since `txid` provided. If client does not provide last received `txid`, Aidbox will wait for next notification and will respond with it. If the client provides `0` as the last `txid` he got, he will get all notifications ever happened.

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

