# Subscriptions

Aidbox subscriptions module is a way to subscribe and get notifications about changes on server. It is common denominator of FHIR R4/R5 subscriptions specification with some extensions.

Your service can register subscription by POST **SubsSubscription** resource:

```yaml
POST /SubsSubscription

id: myservice-subs

status: active # 'active' is default, if 'off' - subscription is disabled
# Subscribe to all changes of Patient and Person resources
trigger:
  # resource type
  Patient: 
    event: ['all'] # can be all | create | update | delete
  Person:
    event: ['all']

# how to deliver notifications
channel:
  type: rest-hook # more types are comming
  # url to send hook
  endpoint: https://myservice/subs/patient
  # headers to add to request (for consistency use lowercase names)
  headers:
    Authorization: Bearer <......>
  # recomended timeout for web hook in ms, server may use it's own
  timeout:  1000
  payload:
    # this is default value, you can use id-only
    content: full-resource 
    # means aidbox format, or fhir+json to get resource in FHIR format
    contentType: json 
   

```

After you registered subscription aidbox sends on channel.endpoint `handshake` notification in json format. It's good if your service will respond with `status: 200`

```yaml
POST https://myservice/subs/patient
Content-Type: application/json
... channe.headers ...

{
   "type": "handshake",
   "subscription": { ...SubsSubscription resource content... }
}
```

On every trigger event aidbox will send notification to your service endpoint. Your service has to respond with `status: 200` and optional json body.

```yaml
POST https://myservice/subs/patient
Content-Type: application/json
... channe.headers ...

{
   "id": <unique-id>,
   "type": "notification",
   "event": "create", # update | delete
   "resource":  {"resourceType": "Patient", ..... }
}
```

Results of all notifications are logged into **SubsNotification**  resource:

```yaml
GET /SubsNotification

---
id: <unique-id>
subscription: { id: 'myservice-subs', resourceType: SubsSubscription }
duration: 23 # hook duration in ms
status: success # fail
notification: <notification content>
response: <response content if present>
```

### SubsSubscription/&lt;id&gt;/$handshake

You can force handshake notification for specific subscription with:

```yaml
POST /SubsSubscription/<sub-id>/$handshake

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

### SubsSubscription/&lt;id&gt;/$debug

To debug subscription notifications you can send debug messages with:

```yaml
POST /SubsSubscription/<sub-id>/$debug

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

### SubsNotification/$notify

Or you can send list of notifications by providing list of search params:

```yaml
POST /SubsNotification/$notify?_id=id-1,id-2,id-3
```

### SubsNotification/&lt;id&gt;/$notify

You can resend specific notification with

```yaml
POST /SubsNotification/<notif-id>/$notify


# response

status: ...
duration: ...
notification: ....
response: ...
```

### /subs/webhook

You can subscribe one instance of Aidbox to notifications from another instance and replicate data between boxes by using `/subs/webhook/<source-id>` endpoint:

```yaml
POST /SubsSubscription

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

