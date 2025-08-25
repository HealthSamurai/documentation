# Apps

You can extend [Aidbox](https://www.health-samurai.io/aidbox) with custom Apps. The app can:

* define custom resources,
* define custom endpoints,
* subscribe to hooks or subscriptions.

The app is a standalone service that will work with Aidbox to implement your specific app. The app should be registered in Aidbox.\
Aidbox SDKs support using Apps:

## Example of App resource

To define the App, we should provide the app manifest.

<pre class="language-yaml"><code class="lang-yaml"><strong>PUT /App/myorg.myapp
</strong>
<strong>resourceType: App
</strong>id: myorg.myapp
apiVersion: 1
type: app
endpoint:
   url: https://my.service.com:8888
   type: http-rpc
   secret: &#x3C;your-sercret>
operations: &#x3C;Operations-definitions>
resources: &#x3C;resources-to-be-created>
</code></pre>

## App manifest structure

Here's the manifest structure:

<table><thead><tr><th width="207">Key</th><th width="149">Type</th><th>Description</th></tr></thead><tbody><tr><td><strong>id</strong></td><td>string</td><td>Id of the App resource</td></tr><tr><td><strong>apiVersion (required)</strong></td><td>integer</td><td>App API version. Currently, the only option is <code>1</code></td></tr><tr><td><strong>type (required)</strong></td><td>enum</td><td>Type of application. Currently, the only option is <code>app</code></td></tr><tr><td><strong>endpoint</strong></td><td>object</td><td>Information about endpoint: url to redirect the request, protocol, and secret</td></tr><tr><td><strong>operations</strong></td><td>array of operations</td><td>Custom endpoints</td></tr><tr><td><strong>resources</strong></td><td>array of resources in Aidbox format</td><td>Related resources that should be also created</td></tr><tr><td><strong>subscriptions</strong></td><td>array of subscriptions</td><td>Deprecated subscriptions support. Consider using <a href="../../modules/topic-based-subscriptions/aidbox-topic-based-subscriptions">Aidbox topic-based subscriptions</a> or <a href="../../app-development/aidbox-sdk/broken-reference/">SubsSubscriptions</a> instead</td></tr><tr><td><strong>entities</strong></td><td>array of entities</td><td>Deprecated <a href="../../deprecated/deprecated/entity-attribute/entities-and-attributes.md">Entities/Attributes</a> approach to create custom resources</td></tr></tbody></table>

### endpoint

In the `endpoint` section, you describe how Aidbox will communicate with your service:

<table><thead><tr><th width="172">Key</th><th width="103.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td><strong>type (required)</strong></td><td>string</td><td>Protocol of communication. The only option now is <code>http-rpc</code></td></tr><tr><td><strong>url (required)</strong></td><td>string</td><td>Url of the service to redirect a request</td></tr><tr><td><strong>secret</strong></td><td>string</td><td>Secret for Basic Authorization header: <code>base64(&#x3C;id>:&#x3C;secret>)</code></td></tr></tbody></table>

### operations

In the operation section, you define Custom REST operations as a map \<operation-id>: \<operation-definition> and access policy (which will be bound to this operation):

```yaml
operations:
  daily-patient-report:
    method: GET
    # GET /Patient/$daily-report/2024-01-01
    # GET /Patient/$daily-report/2024-01-02
    path: ['Patient', '$daily-report', { name: 'date'} ]
  register-user:
    method: POST
    path: [ 'User', '$register' ]
    policies: 
      register-user: {  engine: allow }
```

Parameters:

| Key          | Type                        | Description                                               |
| ------------ | --------------------------- | --------------------------------------------------------- |
| **method**   | string                      | One of: `GET`, `POST`, `PUT,` `DELETE`, `PATCH`, `OPTION` |
| **path**     | array of strings or objects | New endpoint in Aidbox in array                           |
| **policies** | object                      | Access policies to create and bound to this operation     |

### resources

In the resources section, you can provide other resources for Aidbox in the form `{resourceType: {id: resource}}` using Aidbox format:

<pre class="language-yaml"><code class="lang-yaml">resourceType: App
resources:
<strong>  # resource type
</strong>  AccessPolicy:
    # resource id
    public-policy:
      # resource body
      engine: allow
      link:
        - {id: 'opname', resourceType: 'Operation'}
</code></pre>

In this example, the AccessPolicy resource will be created as well as the App resource.

### entities

{% hint style="warning" %}
It is a deprecated option to create custom resources via [Entity/Attribute](../deprecated/deprecated/entity-attribute/entities-and-attributes.md) approach.
{% endhint %}

In the `entities` section of the App manifest, you can extend existing resources, define custom profiles, or hook into the lifecycle:

{% code title="entities:" %}
```yaml
Patient: # existing resource type
  attrs:
     # here we define extension `race` for patient
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
     email:    { type: 'email', isRequired: true }
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

In the `entities` section, resources are defined in the format `<resourceType> : <structure>` :

```yaml
entities:
  Patient: <structure>
  User: <structure>
  Payment: <structure>
```

Resource structure is defined by `attrs` keywor&#x64;**:**

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
