# FHIR R4/Subscriptions

[Aidbox](https://www.health-samurai.io/aidbox) implements [FHIR Subscriptions API](https://www.hl7.org/fhir/subscription.html) to notify interested 3rd parties about newly created/updated resources which meet a certain criteria. To the subscription's standard push-based delivery mechanism to the endpoint specified in `Subscription.channel`, Aidbox additionally provides the long-polling endpoint as a `$poll` operation on the Subscription resource.

### Long-Polling Subscription Notifications

Long-Polling is a robust and easy-to-implement mechanism to get instant notifications about CRUD events through the HTTP protocol. The client makes a request to get the data \(notifications\) he hasn't yet received. If the server has newer notifications for the client, he responds with them. But when no new notifications are available for the client, the server keeps HTTP connection open for some time and waits for event to happen \(instead of responding with the empty body\) and then responds with the notification about the event happened. If the request was interrupted \(because of the timeout, for example\), client issues the same request again.

As a response, `$poll` endpoint returns a FHIR Bundle containing matched resources. Every resource has a `meta` element with `versionId` and `lastUpdated` keys. The `versionId` element is quite important in context of polling because it contains a transaction ID of operation which caused the event. It's an integer and it's always increasing which means if event B happened after event A, then B's `versionId` will be greater than A's `versionId`. A client can specify the last `versionId` he received with the `from` parameter in the request's query string. In this case, the `$poll` endpoint will return only notifications which have `versionId` greater than the number provided in the `from` parameter. This approach guarantees that client will never miss a notification because of time spans between requests to the `$poll` endpoint. If a client polls with `from=0`, Aidbox will return all the notifications that ever happened.

{% hint style="warning" %}
To be able to call the `$poll` operation, the Subscription resource should be already created, it should have `Subscription.status` equal to `active,` and `Subscription.criteria` should specify a resource type \(i.e. `Observation` or `Observation?code=xxxx`\). If the Subscription resource doesn't meet those requirements, the`$poll` operation will return`403 Invalid Request` response with the OperationOutcome resource containing the error message.
{% endhint %}

To summarize the polling logic, here is the JavaScript-like client-side pseudo-code:

```javascript
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

### Long-polling example

Create subscription for all patients:

```yaml
PUT /fhir/Subscription/all-pt
Accept: text/yaml
Content-Type: text/yaml

criteria: Patient?
status: active
reason: test
channel: 
  type: poll
```

Create test patient:

```yaml
PUT /Patient/pt-1
Accept: text/yaml
Content-Type: text/yaml

name:
- family: Doe
  given: ['John']
```

Get current patients:

```yaml
GET /Subscription/all-pt/$poll?from=0&_format=yaml
Accept: text/yaml

resourceType: Bundle
entry:
- resource:
    name:
    - given: [John]
      family: Doe
    id: pt-1
    resourceType: Patient
    meta: {lastUpdated: '2019-05-08T20:00:37.262Z', versionId: '28'}
```

To subscribe to changes, set `from` to max `versionId`  and enter url in the browser:

```yaml
<your-box-url>/Subscription/all-pt/$poll?from=28&_format=yaml
```

Browser will be awaiting a response from server. In another tab, in the REST console, create another patient:

```yaml
PUT /Patient/pt-2
Accept: text/yaml
Content-Type: text/yaml

name:
- family: Smith
  given: ['Mike']
```

Take a look into tab with the poll request!

### REST Hook Example

Configuration of Subscription with REST hook is described in the [FHIR documentation](https://www.hl7.org/fhir/subscription.html#2.46.6.1).

Subscription with the channel type `rest-hook` should be created. All `headers` provided with channel will be attached to the request to `endpoint`. If the `payload` field is omitted, the request will not contain body.

Let's create the following Subscription resource:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /fhir/Subscription
Accept: application/json
Content-Type: application/json

{
  "criteria": "Patient?name=subscription",
  "reason": "test",
  "channel": {
    "payload": "application/fhir+json",
    "type": "rest-hook",
    "header": [
      "DEMO: DEMO",
      "Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l"
    ],
    "endpoint": "https://aidbox.requestcatcher.com/subscription"
  },
  "resourceType": "Subscription",
  "status": "active"
}

```
{% endtab %}

{% tab title="Response" %}
```javascript
{
  "reason": "test",
  "status": "active",
  "channel": {
    "type": "rest-hook",
    "header": [
      "DEMO: DEMO",
      "Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l"
    ],
    "payload": "application/fhir+json",
    "endpoint": "https://aidbox.requestcatcher.com/subscription"
  },
  "criteria": "Patient?name=subscription",
  "id": "69c7c227-c3fd-491a-a42a-59a2d7a50509",
  "resourceType": "Subscription",
  "meta": {
    "lastUpdated": "2018-11-16T17:25:15.804Z",
    "versionId": "1",
    "tag": [
      {
        "system": "https://aidbox.app",
        "code": "created"
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

Our Subscription uses aidbox.requestcatcher.com as the `endpoint`. Let's open this URL:

{% tabs %}
{% tab title="Request" %}
Open [https://aidbox.requestcatcher.com](https://aidbox.requestcatcher.com)
{% endtab %}
{% endtabs %}

Now,  let's create a new Patient resource:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /fhir/Patient
Accept: application/json
Content-Type: application/json

{
  "resourceType": "Patient",
  "name": [
    {
      "family": "subscription"
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
  "name": [
    {
      "family": "subscription"
    }
  ],
  "id": "d2831faf-c15d-428a-8256-beeb8e679af6",
  "resourceType": "Patient",
  "meta": {
    "lastUpdated": "2018-11-16T18:50:34.255Z",
    "versionId": "1",
    "tag": [
      {
        "system": "https://aidbox.app",
        "code": "created"
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

If a subscription is saved and is `active`, each resource that satisfies the `criteria` will trigger the hook.  In our case, you will see the following result of the hook execution:

![](../../.gitbook/assets/scr-2019-03-01_17-52-49.png)

