# Apps

You can extend [Aidbox](https://www.health-samurai.io/aidbox) with custom Apps. The app can define custom resources, custom endpoints, and subscribe to hooks or subscriptions. The app is a standalone service, which will work with Aidbox to implement your specific app or Aidbox Add-on. App should be registered in Aidbox.

You define the App using Aidbox SDK for your language and should not reverse engineer internal API, because it can be subject to change.

{% page-ref page="nodejs.md" %}

{% page-ref page="clojure-sdk.md" %}

{% page-ref page="python-sdk.md" %}

## App Resource

To define the App, we should provide the app manifest:

```yaml
resourceType: App
id: myorg.myapp  # id of your application
apiVersion: 1    # App API version
type: app        # type of app - app | addon
endpoint:        # communication protocol between aidbox and app
   type: http-rpc         # type of protocol - http-rpc | ws-rpc
   host: app.mydomain.com # app service host for http protocols 
   scheme: https          # app service schema for http protocols - default = https
   port: 8080             # app service port - default = 80
   secret: <yoursercret>  # will be used to secure aidbox - app communication
entities: <Resource's Definitions, Profiles & Hooks>
operations: <Operation's Definitions>
subscriptions: <Subscriptions>
```

### endpoint

In the `endpoint` section, you describe how Aidbox will communicate with your service. 

| Key | type | required | Options | default | description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **type** | string | true | http-rpc \| ws-rpc | http-rpc | Protocol of communication |
| **host** | string  | false  |  |  | Domain of service for http protocols |
| **schema** |  |  | http \| https | https | Schema of service for http |
| **port** | integer |  |  | 80 | Port of service for http |
| **secret** | string |  |  |  | Secret key used for communication |

### entities

In the `entities` section of the App manifest, you can extend existing resources, define custom profiles, or hook into lifecycle:

{% code title="entities:" %}
```yaml
Patient: # existing resource type
  attrs:
     # here we devine extension `race` for patient
     race: { extensionUrl: 'https://myapp/race', type: 'code' }
  hooks: # resource life cycle hooks
    before_create: # map of hooks <hook-id>: { config-map }
       notify_on_patient: { emails: ['admin@hs.io'] }

```
{% endcode %}

As well as define custom resources:

{% code title="entities:" %}
```yaml
User: # custom resource type
  attrs:
     email:    { type: 'email', isRequred: true }
     password: { type: 'password' }
     active:   { type: 'boolean' }
     patient:  { type: 'Reference', refers: ['Patient'] }
     settings:  
        attrs:
           theme: { type: 'string', enum: ['black', 'white'] }
  hooks:
     before_create:
        check_ldap: {}
     after_create:
        save_to_third_paty: {}
  profiles:
     user-uniq-email: 
       type: sql 
       query: SELECT true FROM "User" WHERE resource->>'email' == {{ .email }}
       message: Email is already taken
```
{% endcode %}

In the entities section, you define resources as a map `<resourceType> : <resourceStructure>` :

```yaml
entities:
  Patient: <structure>
  User: <structure>
  Payment: <structure>
```

Resource structure is defined by keyword **attrs**`{ <element-name>: <element-definition> ... }`

{% code title="entities:" %}
```yaml
User:
  attrs:
    email: <element-definition>
    password: <element-definition>
  profiles: <profiles-definition>
  hooks: <hooks-definition>
```
{% endcode %}

At the root of resource definition, you can also define **hooks** and **profiles** for this resource.

Element definition will be translated into [Attribute Meta-Resource]() and have following properties:

| name | type | Description |
| :--- | :--- | :--- |
| **type** | code | Primitive or Complex Type name |

### operations

In the operation section, you define Custom REST operations as a map  &lt;operation-id&gt;: &lt;operation-definition&gt; and access policy \(which will be bound to this operation\):

{% code title="operations:" %}
```yaml
daily-patient-report:
  method: GET
  path: ['Patient', '$daily-report', { name: 'date'} ]
register-user:
  method: POST
  path: [ 'User', '$register' ]
  policies: 
     register-user: {  engine: allow }
```
{% endcode %}

### resources

In the resources section, you can provide other meta-resources for Aidbox in the form `{resourceType: {id: resource}}`

{% code title="resources:" %}
```yaml
# resource type
AccessPolicy:
   # resource id
   public-policy:
      # resource body
      engine: allow
      link:
        - {id: 'opname', resourceType: 'Operation'}
```
{% endcode %}

### hooks

Aidbox will provide a set of hooks you can hook-in to customize it's behavior:

TBD

### subscriptions

In the subscription section, you can define subscriptions \(a special type of hooks, which do not need a response\). You can subscribe to specific life-cycle events of resources filtered by criteria or to changes in search queries.

TBD

