# Apps

You can extend [Aidbox](https://www.health-samurai.io/aidbox) with custom Apps. App can define custom resources, custom endpoints and subscribe to hooks or subscriptions. App is a standalone service, which will work with Aidbox to implement your specific app or Aidbox Addon. App should be registered in Aidbox.

You define App using Aidbox SDK for your language and should not reverse engenier internal API, because it can be subject of change.

{% page-ref page="nodejs.md" %}

{% page-ref page="clojure-sdk.md" %}

{% page-ref page="python-sdk.md" %}

## App Resource

To define App we should provide app manifest:

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
entities: <Resources Definitions, Profiles & Hooks>
operations: <COperations Definition>
subscriptions: <Subscriptions to changes>
```

### endpoint

In `endpoint` section you describe how aidbox will communicate with your service. 

| Key | type | required | Options | default | description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **type** | string | true | http-rpc \| ws-rpc | http-rpc | Protocol of communication |
| **host** | string  | false  |  |  | Domain of service for http protocols |
| **schema** |  |  | http \| https | https | Schema of service for http |
| **port** | integer |  |  | 80 | Port of service for http |
| **secret** | string |  |  |  | Secret key used for communication |

### entities

In `entities` section of App manifest you can extend existing resources, define custom profiles or hook into lifecycle:

{% code-tabs %}
{% code-tabs-item title="entities:" %}
```yaml
Patient: # existing resource type
  attrs:
     # here we devine extension `race` for patient
     race: { extensionUrl: 'https://myapp/race', type: 'code' }
  hooks: # resource life cycle hooks
    before_create: # map of hooks <hook-id>: { config-map }
       notify_on_patient: { emails: ['admin@hs.io'] }

```
{% endcode-tabs-item %}
{% endcode-tabs %}

As well define custom resources:

{% code-tabs %}
{% code-tabs-item title="entities:" %}
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
{% endcode-tabs-item %}
{% endcode-tabs %}

In entities section you define resources as a map `<resourceType> : <resourceStructure>` :

```yaml
entities:
  Patient: <structure>
  User: <structure>
  Payment: <structure>
```

Resource structure is defined by keyword **attrs**`{ <element-name>: <element-definition> ... }`

{% code-tabs %}
{% code-tabs-item title="entities:" %}
```yaml
User:
  attrs:
    email: <element-definition>
    password: <element-definition>
  profiles: <profiles-definition>
  hooks: <hooks-definition>
```
{% endcode-tabs-item %}
{% endcode-tabs %}

At root of resource definition you can also define **hooks** and **profiles** for this resource.

Element definition will be translated into [Attribute Meta-Resource](../basic-concepts/meta-model/entity-and-attributes.md) and can have following properties:

| name | type | Description |
| :--- | :--- | :--- |
| **type** | code | Primitive or Complex Type name |

### operations

In operation section you define Custom REST operations as a map  &lt;operation-id&gt;: &lt;operation-definition&gt; and access policy \(which will be bound to this operation\):

{% code-tabs %}
{% code-tabs-item title="operations:" %}
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
{% endcode-tabs-item %}
{% endcode-tabs %}

### resources

In resources section you can provide other meta-resources for Aidbox in form `{resourceType: {id: resource}}`

{% code-tabs %}
{% code-tabs-item title="resources:" %}
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
{% endcode-tabs-item %}
{% endcode-tabs %}

### hooks

Aidbox will provide set of hooks you can hook-in to customise it's behaviour:

TBD

### subscriptions

In subscription section you can define subscriptions \(special type of hooks, which do not need to provide response\). You can subscribe to specific life-cycle events of resources filtered by criteria or to changes in search queries.

TBD

