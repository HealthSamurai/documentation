---
description: >-
  Page describes FHIR Subscriptions API and additional long-polling endpoint
  provided by Aidbox.
---

# Subscriptions

Aidbox implements [FHIR Subscriptions API](https://www.hl7.org/fhir/subscription.html) to notify interested 3rd parties about newly created/updated resources which meets certain criteria. Additionally to Subscription's standard push-based delivery mechanism to the endpoint specified in `Subscription.channel`, Aidbox provides long-polling endpoint as a `$poll` operation on Subscription resource.

### Long-Polling Subscription Notifications

Long-Polling is an robust and easy-to-implement mechanism to get instant notification about CRUD event through HTTP protocol. The client makes a requests to get the data \(notifications\) he haven't already received. If the server has newer notifications for the client, he responds with them. But when no new notifications are available for the client, instead of responding with empty body, server keeps HTTP connection open for some time and waits for event to happen, then responds with notification about the event happened. If the request was interrupted \(because of timeout, for example\), client issues same request again.

As a response, `$poll` endpoint returns a FHIR Bundle containing matched resources. Every resource has a `meta` element with `versionId` and `lastUpdated` keys. `versionId` element is quite important in context of polling because it contains a transaction ID of operation which caused the event. It's an integer and it's always increasing, which means if event B happened after event A, then B's `versionId` will be greater than A's `versionId`. Client can specify last `versionId` it received with `from` parameter in the request's query string. In this case `$poll` endpoint will return only notifications which has `versionId` greater than number provided in `from` parameter. This approach guarantees that client will never miss a notification because of time spans between requests to the `$poll` endpoint. If client polls with `from=0`, Aidbox will return all the notifications ever happened.

{% hint style="warning" %}
To be able to call `$poll` operation, Subscription resource should be already created, it should has `Subscription.status` equal to `active` and `Subscription.criteria` should specify a resource type \(i.e. `Observation` or `Observation?code=xxxx`\). If Subscription resource does not meet those requirements, `$poll` operation will return`403 Invalid Request` response with OperationOutcome resource containing error message.
{% endhint %}

To summarize the polling logic, here is a JavaScript-like client-side pseudo-code:

```text
let lastVersionId = null;

while (true) {
  // During the first request(s) `from` parameter will be blank
  // and Aidbox will return most recent notification.
  // All subsequent requests will provide `from` parameter.
  let response = httpGet('/fhir/Subscription/mysub/$poll', { from: lastVersionId });
  
  if (response.status == 200) {
    console.log("Got new notifications: ", response.body.entry.map(e => e.resource));
    
    // NB we need to get *maximum* versionId from the response
    // and update lastVersionId with it
    lastVersionId = Math.max(response.body.entry.map(e => e.resource.meta.versionId));
  } else {
    // something went wrong, break polling loop
    console.log("Error during poll request", response);
    break;  
  }
}
```

Example request to the `$poll` operation to get all notifications since transaction 137:

{% tabs %}
{% tab title="Request" %}
```text
GET /fhir/Subscription/test/$poll?from=137
Content-Type: application/json
```
{% endtab %}

{% tab title="Response" %}
```text
{
 "resourceType": "Bundle",
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
    "id": "75790d70-4889-4485-a67e-f40bbcb94ae2",
    "meta": {
     "lastUpdated": "2018-11-09T16:14:15.640Z",
     "versionId": "141",
     "tag": [
      {
       "system": "https://aidbox.app",
       "code": "created"
      }
     ]
    },
    "resourceType": "Patient"
   }
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
    "id": "22bc8414-4b10-4d04-a237-4e7e6ea7d55e",
    "meta": {
     "lastUpdated": "2018-11-12T15:58:34.401Z",
     "versionId": "142",
     "tag": [
      {
       "system": "https://aidbox.app",
       "code": "created"
      }
     ]
    },
    "deceasedBoolean": false,
    "resourceType": "Patient"
   }
  }
 ],
 "type": "collection"
}
```
{% endtab %}
{% endtabs %}

