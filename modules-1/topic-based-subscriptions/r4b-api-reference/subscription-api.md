# Subscription API

### Create Subscription - `POST /fhir/Subscription`

Creates a new R4B subscription.

{% hint style="info" %}
The R4B Subscriptions resource uses FHIR extension fields that should be written according to the [specification](http://hl7.org/fhir/R4B/extensibility.html#extension).\
You should also add the subscription backport profile URL to your resource's `meta.profile` field.
{% endhint %}

#### Params:

<table data-full-width="false"><thead><tr><th width="322.30314960629926">Parameter</th><th width="92">Type</th><th width="101" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td><strong>meta</strong></td><td>object</td><td>false</td><td></td></tr><tr><td>meta.profile</td><td>string[]</td><td>true</td><td>Extension URL:<br><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription">http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription</a></td></tr><tr><td>id</td><td>string</td><td>false</td><td>Identifier of the resource. Will be auto-generated if not provided.</td></tr><tr><td>reason</td><td>string</td><td>false</td><td>Human readable reason for subscription.</td></tr><tr><td>criteria</td><td>string</td><td>true</td><td>Canonical URL for the topic used to generate events.<br><em>Example: http://aidbox.app/SubscriptionTopic/patient-test-1</em></td></tr><tr><td><strong>criteria.extension:</strong><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria">filterCriteria</a></td><td>string[]</td><td>false</td><td><p>The <a href="http://hl7.org/fhir/uv/subscriptions-backport/STU1.1/components.html#subscription-filters">filters</a> applied to events in the following format:</p><p><br><code>[resourceType.]filterParameter=[modifier]value</code></p><p></p><p><code>filterParameter</code> above should be one of the filter names defined by the relevant <code>SubscriptionTopic</code></p><p>(<code>canFilterBy.filterParameter</code> field).</p><p><code>resourceType</code> and <code>modifiers</code> should be the ones allowed in the corresponding <code>canFilterBy.filterParameter</code> of the relevant <code>SubscriptionTopic</code> as well. </p><p><code>resourceType</code> is only necessary for disambiguation.</p><p>When multiple filters are applied, it evaluates to true if all the conditions applicable to that resource are met; otherwise, it returns false (i.e., logical AND).</p><p><br><em>Example: patient-birth=gt2005-01-01T01:00:00Z</em></p></td></tr><tr><td><strong>channel.extension:</strong><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period">heartbeatPeriod</a></td><td>integer</td><td>false</td><td><p>Interval in seconds to send 'heartbeat' notification.</p><p><br><em>Default value: 120</em></p></td></tr><tr><td><strong>channel.extension:</strong><a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout">timeout</a></td><td>integer</td><td>false</td><td><p>Timeout in seconds to attempt notification delivery.</p><p><br><em>Default value: 30</em></p></td></tr><tr><td><strong>channel.extension</strong>.<a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count">maxCount</a></td><td>integer</td><td>false</td><td><p>Maximum number of triggering resources included in notification bundles.</p><p><br><em>Default value: 10</em></p></td></tr><tr><td><strong>channel</strong>.type</td><td>string</td><td>true</td><td>The type of channel to send notifications on.<br><br><em>Supported values: rest-hook</em></td></tr><tr><td><strong>channel</strong>.header</td><td>string[]</td><td>false</td><td><p>Additional headers/information to send as part of the notification.  For the rest-hook channel type, this will be HTTP request headers.</p><p><br><em>Example: ["random-header: secret123"]</em></p></td></tr><tr><td><strong>channel</strong>.endpoint</td><td>string</td><td>false</td><td><p>The URL that describes the actual end-point to send messages to.</p><p><br><em>Example: https://my.app/endpoint</em></p></td></tr><tr><td><strong>channel</strong>.payload</td><td>string</td><td>false</td><td>MIME-type to send, or omit for no payload.<br><br><em>Supported values: application/fhir+json</em></td></tr><tr><td><strong>channel.payload.extesion</strong>.<a href="http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content">content</a></td><td>string</td><td>false</td><td><p><a href="http://hl7.org/fhir/uv/subscriptions-backport/STU1.1/ValueSet-backport-content-value-set.html">Notification content level</a>.</p><p><br><em>Supported values:</em> <em>empty, id-only, full-resource.</em></p><p><em>Default value: empty</em></p></td></tr><tr><td>status</td><td>string</td><td>false</td><td><p>Status of the subscription. </p><p>On the creation of this resource, the status is always <code>requested</code> and other values will be ignored.</p></td></tr><tr><td>end</td><td>string</td><td>false</td><td>When to automatically delete the subscription</td></tr></tbody></table>

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

### Subscription Status - `GET /fhir/Subscription/[id]/$status`

This operation is used to get the current status information about a topic-based Subscription.

Returns FHIR bundle where the [SubscriptionStatus resource](http://hl7.org/fhir/R4B/subscriptionstatus.html) is the only entry element.

#### Params:

No params are required.



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
        reference: http://aidbox.app/fhir/Subscription/subscription-patient-test-1
      topic: http://aidbox.app/SubscriptionTopic/patient-test-1
```
{% endtab %}
{% endtabs %}



### Subscription Events - `GET /fhir/Subscription/[id]/$events`

This operation is used to search for and get notifications that have been previously triggered by a topic-based Subscription.

Returns FHIR bundle where the [SubscriptionStatus resource](http://hl7.org/fhir/R4B/subscriptionstatus.html) is the first entry element, and triggered events are the rest.

#### Params:

<table data-full-width="false"><thead><tr><th width="208">Parameter</th><th width="96">Type</th><th width="99" data-type="checkbox">required</th><th>Description</th></tr></thead><tbody><tr><td>eventsSinceNumber</td><td>integer</td><td>false</td><td>The starting event number, inclusive of this event (lower bound).</td></tr><tr><td>eventsUntilNumber</td><td>integer</td><td>false</td><td>The ending event number, inclusive of this event (upper bound).</td></tr></tbody></table>

If the params above are not provided, returns the latest 20 events.



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
        reference: http://aidbox.app/fhir/Subscription/subscription-patient-test-1
      topic: http://aidbox.app/SubscriptionTopic/patient-test-1
      notificationEvent:
        - eventNumber: 1
          timestamp: '2023-09-04 10:38:43.020984+03'
          focus:
            reference: >-
              http://aidbox.app/fhir/Patient/c868fc6f-0631-43cc-940a-c43177fe6567
        - eventNumber: 2
          timestamp: '2023-09-04 10:38:44.382835+03'
          focus:
            reference: >-
              http://aidbox.app/fhir/Patient/fe174a4e-e3ba-4d75-98b6-17fea275dda1
        - eventNumber: 3
          timestamp: '2023-09-04 10:38:44.888717+03'
          focus:
            reference: >-
              http://aidbox.app/fhir/Patient/98485698-083d-4659-8869-b4f0d87734ac
        - eventNumber: 4
          timestamp: '2023-09-04 10:38:45.031019+03'
          focus:
            reference: >-
              http://aidbox.app/fhir/Patient/cc95822b-ed0d-4e6a-ad48-358d029d8c61
<strong>        - eventNumber: 5
</strong>          timestamp: '2023-09-04 10:38:45.171641+03'
          focus:
            reference: >-
              http://aidbox.app/fhir/Patient/36df7d8f-1a03-4073-8435-6741cdba90e9
  - fullUrl: http://aidbox.app/fhir/Patient/c868fc6f-0631-43cc-940a-c43177fe6567
    request:
      method: POST
      url: Patient/c868fc6f-0631-43cc-940a-c43177fe6567
    response:
      status: '201'
  - fullUrl: http://aidbox.app/fhir/Patient/fe174a4e-e3ba-4d75-98b6-17fea275dda1
    request:
      method: POST
      url: Patient/fe174a4e-e3ba-4d75-98b6-17fea275dda1
    response:
      status: '201'
  - fullUrl: http://aidbox.app/fhir/Patient/98485698-083d-4659-8869-b4f0d87734ac
    request:
      method: POST
      url: Patient/98485698-083d-4659-8869-b4f0d87734ac
    response:
      status: '201'
  - fullUrl: http://aidbox.app/fhir/Patient/cc95822b-ed0d-4e6a-ad48-358d029d8c61
    request:
      method: POST
      url: Patient/cc95822b-ed0d-4e6a-ad48-358d029d8c61
    response:
      status: '201'
  - fullUrl: http://aidbox.app/fhir/Patient/36df7d8f-1a03-4073-8435-6741cdba90e9
    request:
      method: POST
      url: Patient/36df7d8f-1a03-4073-8435-6741cdba90e9
    response:
      status: '201'



</code></pre>
{% endtab %}
{% endtabs %}

### Delete Subscription - `DELETE /fhir/Subscription/[id]`

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
