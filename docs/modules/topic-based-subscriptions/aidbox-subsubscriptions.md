# Aidbox SubSubscriptions

{% hint style="warning" %}
While Aidbox SubSubscriptions are stable and fully functional, they will no longer receive active development or new features. For enhanced capabilities and ongoing support, please use [Aidbox topic-based subscriptions](wip-dynamic-subscriptiontopic-with-destinations/). This newer implementation offers improved performance, flexibility, and will continue to be developed to meet future needs.&#x20;
{% endhint %}

Aidbox subscriptions module is a way to subscribe and get notifications about updating resources on the server. It is a common denominator of FHIR R4/R5 subscriptions specification with some extensions.

This module introduces two new resources into Aidbox:

* SubsSubscription — meta-resource which binds events (create/update/delete resource) with a communication channel through which subscriber is notified about changes.
* SubsNotification — resource which represents notification with its status (sent or not).

{% hint style="warning" %}
Aidbox doesn't delete **SubsNotification** resources by itself. The simple way to implement a retention policy is to create a cron job. [Let us know](https://t.me/aidbox) if there is a more clear way.
{% endhint %}

{% hint style="info" %}
See tutorial ["Subscribe to new Patient resource"](../../deprecated/deprecated/other/subscribe-to-new-patient-resource.md)
{% endhint %}

Your service can register subscription by POST **SubsSubscription** resource:

```yaml
POST /SubsSubscription
Accept: text/yaml
Content-Type: text/yaml

id: myservice-subs

status: active # 'active' is default, if 'off' - subscription is disabled
# Subscribe to all changes of Patient and Person resources
trigger:
  # resource type
  Patient: 
    event: ['all'] # can be all | create | update | delete
    # collection of filters
    filter:
    # use matcho engine to filter resources
    - matcho: { active: true }
  Person:
    event: ['all']

# how to deliver notifications
channel:
  type: rest-hook 
  # url to send hook
  endpoint: https://myservice/subs/patient
  # headers to add to request (for consistency use lowercase names)
  headers:
    Authorization: Bearer <......>
  # recomended timeout for web hook in ms, server may use it's own
  timeout:  1000
  payload:
    # this is default value, you can use id-only
    content: full-resource # full-resource | id-only
    # means aidbox format, or fhir+json to get resource in FHIR format
    contentType: json # json | fhir+json
   

```

### Trigger format

Subscription.trigger is a key-value object, where key is resource type and each value can contain a collection of events (values can be 'all', 'create', 'update', 'delete') and .filter collection. For now filter support [**matcho**](../../api/rest-api/other/matcho.md) engine (FHIRPath and FHIR Search filters are coming soon):

```yaml
trigger:
 Encounter:
   filter:
     - matcho: { class: {code: 'inpatient'} }
     - matcho: { type: { coding: [{code: 'Sometype'}]}
```

Filter matches if at least one of item in the collection matches, i.e. collection has `or` semantic.

### Protocol

After you registered subscription, Aidbox sends on channel.endpoint `handshake` notification in json format. It's good if your service responds with `status: 200`

```yaml
POST https://myservice/subs/patient
Content-Type: application/json
... channel.headers ...

{
   "type": "handshake",
   "subscription": { ...SubsSubscription resource content... }
}
```

On every trigger event Aidbox will send a notification to your service endpoint. Your service has to respond with `status: 200` and optional json body.

```yaml
POST https://myservice/subs/patient
Content-Type: application/json
... channel.headers ...

{
   "id": <unique-id>,
   "type": "notification",
   "event": "create", # update | delete
   "resource":  {"resourceType": "Patient", ..... }
}
```

Results of all notifications are logged into **SubsNotification** resource:

```yaml
GET /SubsNotification
Accept: text/yaml

---
id: <unique-id>
subscription: { id: 'myservice-subs', resourceType: SubsSubscription }
duration: 23 # hook duration in ms
status: success # fail
notification: <notification content>
response: <response content if present>
```

### SubsSubscription/\<id>/$handshake

You can force a handshake notification for the specific subscription with:

```yaml
POST /SubsSubscription/<sub-id>/$handshake
Accept: text/yaml

# response

resourceType: SubsNotification
notification:
  type: handshake
  debug: true
  subscription: ...
subscription: {resourceType: SubsSubscription, id: <sub-id>}
duration: 1
status: failed
error: {message: Connection refused}
```

### SubsSubscription/\<id>/$debug

To debug subscription notifications, you can send debug messages with:

```yaml
POST /SubsSubscription/<sub-id>/$debug
Accept: text/yaml
Content-Type: text/yaml

id: notif-id
event: create
type: notification
resource: 
  resourceType: Patient
  id: pt-1

# response

resourceType: SubsNotification
notification:
  type: notification
  resource: {id: pt-1, resourceType: Patient}
  debug: true
subscription: {resourceType: SubsSubscription, id: <sub-id>}
duration: 1
status: failed
error: {message: Connection refused}
```

### SubsNotification/$notify (not implemented yet)

Or you can send a list of notifications by providing a list of search params:

```yaml
POST /SubsNotification/$notify?_id=id-1,id-2,id-3
```

### SubsNotification/\<id>/$notify

You can resend the specific notification with

```yaml
POST /SubsNotification/<notif-id>/$notify
Accept: text/yaml

# response

status: ...
duration: ...
notification: ....
response: ...
```

### /subs/webhook (not implemented yet)

You can subscribe one instance of Aidbox to notifications from another instance and replicate data between boxes by using `/subs/webhook/<source-id>` endpoint:

```yaml
POST /SubsSubscription
Accept: text/yaml
Content-Type: text/yaml

id: box-replication
status: active
trigger:
  Patient: { event: ['all'] }
channel:
  type: rest-hook
  endpoint: <other-box-url>/subs/webhook/box-1
  headers:
    Authorization: Bearer <token>

```
