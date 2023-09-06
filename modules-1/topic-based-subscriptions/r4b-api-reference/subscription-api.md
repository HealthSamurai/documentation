# Subscription API

### `POST /fhir/Subscription`

Creates a new R4B subscription.

{% hint style="info" %}
The R4B Subscriptions resource uses FHIR extension fields that should be written according to the [specification](http://hl7.org/fhir/R4B/extensibility.html#extension).\
You should also add the subscription backport profile URL to your resource's meta.profile field.
{% endhint %}

#### Params:

<table data-full-width="true"><thead><tr><th width="369">Parameter</th><th width="92">Type</th><th width="101" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td><strong>meta</strong></td><td>object</td><td>false</td><td></td></tr><tr><td>meta.profile</td><td>string[]</td><td>true</td><td>Extension URL:<br><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription">http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription</a></td></tr><tr><td>id</td><td>string</td><td>false</td><td>Identifier of the resource.<br><em>Default value: generated</em><br></td></tr><tr><td>criteria</td><td>string</td><td>true</td><td>Url reference to subscription topic.<br><em>Example: http://aidbox.app/SubscriptionTopic/patient-test-1</em></td></tr><tr><td>reason</td><td>string</td><td>false</td><td>Human readable reason for subscription.</td></tr><tr><td><strong>criteria.extension:</strong><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria">filterCriteria</a></td><td>string</td><td>false</td><td><p><a href="http://hl7.org/fhir/uv/subscriptions-backport/STU1.1/components.html#subscription-filters">Search-style filters</a> that can be applied to narrow the stream of subscription topics. Parameters used in filter criteria should be specified in the subscription topic. When multiple filters are applied, evaluates to true if all the conditions are met.<br><br>Allowed filters, comparators and modifiers used in filter criteria should also be defined in the subscription topic.</p><p><br><em>Example: Patient?birth=lt1989-01-01T01:00:00Z</em></p></td></tr><tr><td><strong>channel.extension:</strong><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period">heartbeatPeriod</a></td><td>integer</td><td>false</td><td><p>Specifies the interval for heartbeat notifications on this channel in seconds.</p><p><br><em>Default value: 120</em></p></td></tr><tr><td><strong>channel.extension:</strong><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout">timeout</a></td><td>integer</td><td>false</td><td><p>Maximum amount of time a server will allow before a notification attempt fails is seconds.</p><p><br><em>Default value: 30</em></p></td></tr><tr><td><strong>channel.extension</strong>.<a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count">maxCount</a></td><td>integer</td><td>false</td><td>Maximum number of triggering resources that will be included in a notification bundle.<br><em>Default value: 10</em></td></tr><tr><td><strong>channel</strong>.type</td><td>string</td><td>true</td><td>The type of channel to which notifications will be sent. <br><br><em>Supported values: rest-hook</em></td></tr><tr><td><strong>channel</strong>.header</td><td>string[]</td><td>false</td><td><p>Additional information added to the notification depends on the channel type. In the case of the rest-hook, it will add headers to the http request.</p><p><br><em>Example: ["random-header: secret123"]</em></p></td></tr><tr><td><strong>channel</strong>.endpoint</td><td>string</td><td>false</td><td><p>The URL that describes the actual endpoint to which messages should be sent.</p><p><br><em>Example: https://my.app/endpoint</em></p></td></tr><tr><td><strong>channel</strong>.payload</td><td>string</td><td>false</td><td>Mime type of the notification.<br><br><em>Supported values: application/fhir+json</em></td></tr><tr><td><strong>channel.payload.extesion</strong>.<a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content">content</a></td><td>string</td><td>false</td><td><p>How much of the resource content to deliver in the notification payload.</p><p><br><em>Supported types:</em> <em>empty, id-only, full-resource.</em></p><p></p><p><em>Default value: empty</em></p></td></tr><tr><td>status</td><td>string</td><td>true</td><td>Subscription status <br><br><em>Supported values: requested</em></td></tr></tbody></table>

#### Result:

<table data-full-width="false"><thead><tr><th width="198">Parameter</th><th width="169">Type</th><th>Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Created Subscription resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /fhir/Subscription
content-type: text/yaml
accept: text/yaml

meta:
  profile:
  - http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription
criteria: http://aidbox.app/SubscriptionTopic/patient-test-1
channel:
  extension:
  - url: http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period
    valueUnsignedInt: 60
  - url: http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout
    valueUnsignedInt: 1
  - url: http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count
    valuePositiveInt: 2
  type: rest-hook
  endpoint: http://localhost:27193/patient-test-1
  payload: application/fhir+json
  _payload:
    extension:
    - url: http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content
      valueCode: id-only
resourceType: Subscription
reason: R4/B Test Topic-Based Subscription for Patient resources
status: active
id: subscription-patient-test-1
end: '2020-12-31T12:00:00Z'
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 201

meta:
  profile:
    - >-
      http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription
  lastUpdated: '2023-09-06T07:03:26.763869Z'
  versionId: '735'
  extension:
    - url: http://example.com/createdat
      valueInstant: '2023-09-06T07:03:26.763869Z'
criteria: http://aidbox.app/SubscriptionTopic/patient-test-1
channel:
  type: rest-hook
  payload: application/fhir+json
  extension:
    - url: >-
        http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout
      valueUnsignedInt: 1
    - url: >-
        http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count
      valuePositiveInt: 2
    - url: >-
        http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period
      valueUnsignedInt: 60
  _payload:
    extension:
      - url: >-
          http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content
        valueCode: id-only
  endpoint: http://localhost:27193/patient-test-1
resourceType: Subscription
reason: R4/B Test Topic-Based Subscription for Patient resources
status: requested
id: subscription-patient-test-1
end: '2020-12-31T12:00:00Z'
```
{% endtab %}
{% endtabs %}

###

### `GET /fhir/Subscription/[id]/$status`

Get SubscriptionStatus bundle.

#### Result:

<table data-full-width="false"><thead><tr><th width="179">Parameter</th><th width="169">Type</th><th>Description</th></tr></thead><tbody><tr><td>bundle</td><td>object[]</td><td>FHIR bundle with the SubscriptionStatus resource as the first entry element.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
GET /fhir/Subscription/subscription-patient-test-1/$status
content-type: text/yaml
accept: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

resourceType: Bundle
type: history
timestamp: '2023-09-06T07:25:19.631905713Z'
entry:
  - resource:
      resourceType: SubscriptionStatus
      status: active
      type: query-status
      eventsSinceSubscriptionStart: 0
      subscription:
        reference: http://localhost:8765/fhir/Subscription/subscription-patient-test-1
      topic: http://aidbox.app/SubscriptionTopic/patient-test-1
```
{% endtab %}
{% endtabs %}



### `GET /fhir/Subscription/[id]/$events`

Get bundle with SubscriptionStatus and last received events.

#### Result:

<table data-full-width="false"><thead><tr><th width="179">Parameter</th><th width="169">Type</th><th>Description</th></tr></thead><tbody><tr><td>bundle</td><td>object[]</td><td>FHIR bundle with the SubscriptionStatus resource as the first entry element, and received events as rest entry elements.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
<pre class="language-yaml"><code class="lang-yaml"><strong>GET /fhir/Subscription/subscription-patient-test-1/$events
</strong>content-type: text/yaml
accept: text/yaml
</code></pre>
{% endtab %}

{% tab title="Response" %}
<pre class="language-yaml"><code class="lang-yaml">Status: 200

resourceType: Bundle
type: history
timestamp: '2023-09-06T07:40:59.527416959Z'
entry:
  - resource:
      resourceType: SubscriptionStatus
      status: active
      type: query-event
      eventsSinceSubscriptionStart: 19
      subscription:
        reference: http://localhost:8765/fhir/Subscription/subscription-patient-test-1
      topic: http://aidbox.app/SubscriptionTopic/patient-test-1
      notificationEvent:
        - eventNumber: 1
          timestamp: '2023-09-04 10:38:43.020984+03'
          focus:
            reference: >-
              http://localhost:8765/fhir/Patient/c868fc6f-0631-43cc-940a-c43177fe6567
        - eventNumber: 2
          timestamp: '2023-09-04 10:38:44.382835+03'
          focus:
            reference: >-
              http://localhost:8765/fhir/Patient/fe174a4e-e3ba-4d75-98b6-17fea275dda1
        - eventNumber: 3
          timestamp: '2023-09-04 10:38:44.888717+03'
          focus:
            reference: >-
              http://localhost:8765/fhir/Patient/98485698-083d-4659-8869-b4f0d87734ac
        - eventNumber: 4
          timestamp: '2023-09-04 10:38:45.031019+03'
          focus:
            reference: >-
              http://localhost:8765/fhir/Patient/cc95822b-ed0d-4e6a-ad48-358d029d8c61
<strong>        - eventNumber: 5
</strong>          timestamp: '2023-09-04 10:38:45.171641+03'
          focus:
            reference: >-
              http://localhost:8765/fhir/Patient/36df7d8f-1a03-4073-8435-6741cdba90e9
  - fullUrl: http://localhost:8765/fhir/Patient/c868fc6f-0631-43cc-940a-c43177fe6567
    request:
      method: POST
      url: Patient/c868fc6f-0631-43cc-940a-c43177fe6567
    response:
      status: '201'
  - fullUrl: http://localhost:8765/fhir/Patient/fe174a4e-e3ba-4d75-98b6-17fea275dda1
    request:
      method: POST
      url: Patient/fe174a4e-e3ba-4d75-98b6-17fea275dda1
    response:
      status: '201'
  - fullUrl: http://localhost:8765/fhir/Patient/98485698-083d-4659-8869-b4f0d87734ac
    request:
      method: POST
      url: Patient/98485698-083d-4659-8869-b4f0d87734ac
    response:
      status: '201'
  - fullUrl: http://localhost:8765/fhir/Patient/cc95822b-ed0d-4e6a-ad48-358d029d8c61
    request:
      method: POST
      url: Patient/cc95822b-ed0d-4e6a-ad48-358d029d8c61
    response:
      status: '201'
  - fullUrl: http://localhost:8765/fhir/Patient/36df7d8f-1a03-4073-8435-6741cdba90e9
    request:
      method: POST
      url: Patient/36df7d8f-1a03-4073-8435-6741cdba90e9
    response:
      status: '201'



</code></pre>
{% endtab %}
{% endtabs %}



### `DELETE /fhir/Subscription/[id]`

Delete Subscription resource.

#### Result:

<table data-full-width="false"><thead><tr><th width="156">Parameter</th><th width="148">Type</th><th>Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Deleted Subscription resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
DELETE /fhir/Subscription/subscription-patient-test-1
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

{
 "meta": {
  "profile": [
   "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
  ],
  "lastUpdated": "2023-09-06T07:15:30.481730Z",
  "versionId": "746",
  "extension": [
   {
    "url": "http://example.com/createdat",
    "valueInstant": "2023-09-06T07:15:30.481730Z"
   }
  ]
 },
 "criteria": "http://aidbox.app/SubscriptionTopic/patient-test-1",
 "channel": {
  "type": "rest-hook",
  "payload": "application/fhir+json",
  "extension": [
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout",
    "valueUnsignedInt": 1
   },
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count",
    "valuePositiveInt": 2
   },
   {
    "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period",
    "valueUnsignedInt": 60
   }
  ],
  "_payload": {
   "extension": [
    {
     "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
     "valueCode": "id-only"
    }
   ]
  },
  "endpoint": "http://localhost:27193/patient-test-1"
 },
 "resourceType": "Subscription",
 "reason": "R4/B Test Topic-Based Subscription for Patient resources",
 "status": "active",
 "id": "subscription-patient-test-1",
 "end": "2020-12-31T12:00:00Z"
}
```
{% endtab %}
{% endtabs %}
