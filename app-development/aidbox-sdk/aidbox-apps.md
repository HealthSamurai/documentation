# Apps

You can extend [Aidbox](https://www.health-samurai.io/aidbox) with custom Apps. The app can:&#x20;

* define custom resources,&#x20;
* define custom endpoints
* subscribe to hooks or subscriptions.&#x20;

The app is a standalone service that will work with Aidbox to implement your specific app. The app should be registered in Aidbox.\
Aidbox SDKs support using Apps:

{% content-ref url="../../tools/aidbox-sdk/nodejs.md" %}
[nodejs.md](../../tools/aidbox-sdk/nodejs.md)
{% endcontent-ref %}

{% content-ref url="../../tools/aidbox-sdk/python-sdk.md" %}
[python-sdk.md](../../tools/aidbox-sdk/python-sdk.md)
{% endcontent-ref %}

## Example of App resource

To define the App, we should provide the app manifest.&#x20;

```yaml
resourceType: App
id: myorg.myapp
apiVersion: 1
type: app
endpoint:
   url: https://my.service.com:8888
   type: http-rpc
   secret: <yoursercret>
operations: <Operation's Definitions>
resources: 
```

## App manifest structure

Here's the manifest structure:

<table><thead><tr><th width="207">Key</th><th width="149">Type</th><th>Description</th></tr></thead><tbody><tr><td><strong>id</strong></td><td>string</td><td>Id of App resource</td></tr><tr><td><strong>apiVersion (required)</strong></td><td>integer</td><td>App API version. Currently is <code>1</code>.</td></tr><tr><td><strong>type (required)</strong></td><td>enum</td><td>Type of application. Currently, the only option is <code>app</code>.</td></tr><tr><td><strong>endpoint</strong></td><td>object</td><td>Information about endpoint: url where to redirect the request, protocol, and secret</td></tr><tr><td><strong>operations</strong></td><td>array of operations</td><td>Custom endpoints.</td></tr><tr><td><strong>resources</strong></td><td>array of resources in Aidbox format</td><td>Related resources that should be also created. </td></tr><tr><td><strong>subscriptions</strong></td><td>array of subscriptions</td><td>Deprecated subscriptions support. Consider using <a href="../../modules-1/topic-based-subscriptions/wip-dynamic-subscriptiontopic-with-destinations/">Aidbox topic-based subscriptions</a> or <a href="../../api-1/reactive-api-and-subscriptions/subscriptions-1.md">SubsSubscriptions</a> instead.</td></tr><tr><td><strong>entities</strong></td><td>array of entities</td><td>Deprecated <a href="../../core-modules/entities-and-attributes.md">Entities/Attributes</a> approach to create custom resources.</td></tr></tbody></table>

### endpoint

In the `endpoint` section, you describe how Aidbox will communicate with your service.

<table><thead><tr><th width="172">Key</th><th width="103.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td><strong>type (required)</strong></td><td>string</td><td>Protocol of communication.  The only option now is <code>http-rpc</code></td></tr><tr><td><strong>url (required)</strong></td><td>string</td><td>Url of the service to redirect a request</td></tr><tr><td><strong>secret</strong></td><td>string</td><td>Secret for Basic Authorization header: <code>base64(&#x3C;id>:&#x3C;secret>)</code></td></tr></tbody></table>

### operations

In the operation section, you define Custom REST operations as a map \<operation-id>: \<operation-definition> and access policy (which will be bound to this operation):

{% code title="operations:" %}
```yaml
operations: <Operation's Definitions>
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
{% endcode %}



| Key          | Type                        | Description                                               |
| ------------ | --------------------------- | --------------------------------------------------------- |
| **method**   | string                      | One of: `GET`, `POST`, `PUT,` `DELETE`, `PATCH`, `OPTION` |
| **path**     | array of strings or objects | New endpoint in Aidbox in array                           |
| **policies** | object                      | Access policies to create and bound to this operation     |

### resources

In the resources section, you can provide other resources for Aidbox in the form `{resourceType: {id: resource}}` using Aidbox format:

<pre class="language-yaml" data-title="resources:"><code class="lang-yaml">resourceType: App
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
It is deprecated option to create custom resources via [Entity/Attribute](../../core-modules/entities-and-attributes.md) appproach.
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

In the entities section, resources are defined in format `<resourceType> : <resourceStructure>` :

```yaml
entities:
  Patient: <structure>
  User: <structure>
  Payment: <structure>
```

Resource structure is defined by keyword **attrs:**

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

Element definition will be translated into [Attribute Meta-Resource](https://github.com/Aidbox/documentation/blob/master/tools/aidbox-sdk/broken-reference/README.md) and have the following properties:
