# Custom Resources

All examples from this tutorial you can run in Postman. Here's the [web view](https://documenter.getpostman.com/view/5552124/RWgxsu5Y) of these examples in Postman.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/946aa4df5535c073ce00?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2F946aa4df5535c073ce00%23%3Fenv%5BAidbox.Cloud%5D%3DW3sia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9uZXdlaHIuYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX1d&_ga=2.116593734.1133756186.1540376522-1595564802.1538573158)

## Defining a Custom Resource

Sometimes your data does not fit any existing FHIR resources. It is not always obvious that your data _cannot_ be translated to FHIR — because of some FHIR  generalizations. The "right" first step is to go to [FHIR community chat](http://health-samurai.info/a-cusres-to-zulip) and ask your specific question about mapping to FHIR, or contact Health Samurai modelling team about your concern. If after this adventure you are still sure that there is no appropriate resource in FHIR or it will take too much time to wait for it — in [Aidbox](https://www.health-samurai.io/aidbox) you can define your own **Custom Resources.**

**Custom Resources** are defined exactly the same way as core FHIR resources, they can refer existing resources, have uniform REST API for CRUD and Search, and participate in transactions.

Let's imagine that in our application we want to store user preferences such as UI configuration or personalized Patient List filters. It is expected that you have already created a box in [Aidbox.Cloud](https://docs.aidbox.app/~/drafts/-LOrgfiiMwbxfp70_ZP0/primary/v/master/installation/use-aidbox.cloud). First of all, we have to define a new resource type by creating an **Entity** resource.

### Create Definition​

Access the REST console and paste the following request. You should see the response: 

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Entity

id: UserSetting
type: resource
isOpen: true
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: Entity
id: UserSetting
meta:
  lastUpdated: '2018-10-16T12:19:51.672Z'
  versionId: '2'
type: resource
isOpen: true
```
{% endtab %}
{% endtabs %}

This means that resource of the type `Entity` was successfully created. When you create `Entity` resources with type `resource`, Aidbox will on the fly initialize a storage for new resource type and generate CRUD & Search REST API.

When you set the `isOpen: true` flag this means that resource does not have any specific structure and you can store arbitrary data. This is useful when you do not know exact resource structure, for example while working on a prototype. Later we will make its schema more strict and will constraint it with additional validations.

### API of a Custom Resource

Let's checkout API for our custom resource `UserSetting`. You can list `UserSetting` resources by the standard FHIR URI template `GET /{resourceType}` :

{% tabs %}
{% tab title="Request" %}
```javascript
GET /UserSetting
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: Bundle
type: searchset
params: []
query-sql: ['SELECT "usersetting".* FROM "usersetting" LIMIT ? OFFSET ?', 100, 0]
query-time: 2
entry: []
total: _undefined
link: []
```
{% endtab %}
{% endtabs %}

In the `query-sql` we see what query is executed by Aidbox to get these resources and can see that the table `usersetting` was created. You can test it with the DB Console using the following query:

```sql
SELECT * FROM "usersetting";
```

Cool! Now, let's create first `UserSetting` resource using the REST Console:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /UserSetting

id: user-1
theme: dark
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: UserSetting
meta:
  lastUpdated: '2018-10-16T12:33:21.225Z'
  versionId: '3'
id: user-1
theme: dark
```
{% endtab %}
{% endtabs %}

Try to get all user settings now:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /UserSetting
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: Bundle
type: searchset
params: []
query-sql: ['SELECT "usersetting".* FROM "usersetting" LIMIT ? OFFSET ?', 100, 0]
query-time: 15
entry:
- resource:
    theme: dark
    id: user-1
    resourceType: UserSetting
    meta:
      lastUpdated: '2018-10-16T12:33:21.225Z'
      versionId: '3'
total: 1
link: []
```
{% endtab %}
{% endtabs %}

Or execute the SQL query in the Aidbox.Cloud DB Console:

```sql
SELECT id, resource->>'theme' as theme FROM "usersetting";
```

| id | theme |
| :--- | :--- |
| user-1 | dark |

### CRUD Operations with a Custom Resource

As well you can read, update, and delete `UserSetting` resource with:

{% tabs %}
{% tab title="READ Request" %}
```javascript
GET /UserSetting/user-1
```
{% endtab %}

{% tab title="READ Response" %}
```yaml
id: user-1
theme: white
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="UPDATE Request" %}
```javascript
PUT /UserSetting/user-1
```

```yaml
theme: white
patientsFilters:
  - location: ICU
```
{% endtab %}

{% tab title="UPDATE Response" %}
```yaml
resourceType: UserSetting
id: user-1
theme: white
patientsFilters:
- {location: ICU}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="READ HISTORY Request" %}
```javascript
GET /UserSetting/user-1/_history
```
{% endtab %}

{% tab title="READ HISTORY Response" %}
```yaml
resourceType: Bundle
type: history
total: 2
entry:
- resource:
    theme: white
    patientsFilters:
    - {location: ICU}
    id: user-1
    resourceType: UserSetting
    meta:
      lastUpdated: '2018-10-16T12:39:41.030Z'
      versionId: '4'
  request: {method: PUT, url: UserSetting}
- resource:
    theme: dark
    id: user-1
    resourceType: UserSetting
    meta:
      lastUpdated: '2018-10-16T12:33:21.225Z'
      versionId: '3'
  request: {method: POST, url: UserSetting}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="DELETE Request" %}
```javascript
DELETE /UserSetting/user-1
```
{% endtab %}

{% tab title="DELETE Response" %}
```yaml
Status 204
```
{% endtab %}

{% tab title="READ HISTORY Request" %}
```javascript
# And again watch history:
GET /UserSetting/user-1/_history
```
{% endtab %}

{% tab title="READ HISTORY Response" %}
```yaml
resourceType: Bundle
type: history
total: 3
entry:
- resource:
    theme: white
    patientsFilters:
    - {location: ICU}
    id: user-1
    resourceType: UserSetting
    meta:
      lastUpdated: '2018-10-16T12:42:58.482Z'
      versionId: '5'
  request: {method: DELETE, url: UserSetting}
- resource:
    theme: white
    patientsFilters:
    - {location: ICU}
    id: user-1
    resourceType: UserSetting
    meta:
      lastUpdated: '2018-10-16T12:39:41.030Z'
      versionId: '4'
  request: {method: PUT, url: UserSetting}
- resource:
    theme: dark
    id: user-1
    resourceType: UserSetting
    meta:
      lastUpdated: '2018-10-16T12:33:21.225Z'
      versionId: '3'
  request: {method: POST, url: UserSetting}
```
{% endtab %}
{% endtabs %}

## Refining the Structure of a Custom Resource

Awesome! We've got a nice API by just providing a couple of lines of metadata. But the schema of our custom resource is currently too open and users can put any data into `UserSetting` resource. For example we can do this:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /UserSetting
```

```yaml
id: user-2
theme:
  - name: white
  - name: black
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: UserSetting
id: user-2
theme:
- name: white
- name: black
```
{% endtab %}
{% endtabs %}

### Describe Structure of Custom Resource

Now, let's put some restrictions and define our Custom Resource structure. To describe structure of a resource, we will use [Attribute]() meta-resource. For example we want to restrict the `theme` attribute to be a `string` value from the specific enumeration:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /Attribute
```

```yaml
id: UserSetting.theme
path: ['theme']
type: {id: string, resourceType: Entity}
enum: ['dark', 'white']
resource: {id: UserSetting, resourceType: Entity}
```
{% endtab %}

{% tab title="Response" %}
```yaml
resource:
  id: UserSetting
  resourceType: Entity
id: UserSetting.theme
resourceType: Attribute
path:
- theme
type:
  id: string
  resourceType: Entity
enum:
- dark
- white
```
{% endtab %}
{% endtabs %}

### Validation of Incoming Resources

To validate incoming resources, Aidbox uses json-schema which is generated from Entity & Attribute meta-resources \(read more in [Validation Section]()\). Using [$json-schema](../api/usdjson-schema.md) operation we can inspect which schema will be applied to `UserSetting` resources:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /$json-schema?path=definitions.UserSetting
```
{% endtab %}

{% tab title="Response" %}
```yaml
path: [definitions, UserSetting]
schema:
  type: object
  minProperties: 1
  patternProperties:
    ^(_.*|fhir_.*): {}
  properties:
    id: {type: string}
    extension:
      type: array
      items: {$ref: '#/definitions/Extension'}
    modifierExtension:
      type: array
      items: {$ref: '#/definitions/Extension'}
    meta: {$ref: '#/definitions/Meta'}
    resourceType: {type: string, constant: UserSetting}
    theme:
      type: string
      enum: [dark, white]
```
{% endtab %}
{% endtabs %}

As we see on the line 17 in the response above, the `theme` property has now type `string` and is restricted by the enumeration `[dark, white]`. 

Let's try to create an invalid resource now:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /UserSetting
```

```yaml
id: user-3
theme: 2
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Response status: 422 Unprocessable Entity

resourceType: OperationOutcome
errors:
- path: [theme]
  message: expected type of string
- path: [theme]
  message: expeceted one of dark, white
warnings: []
```
{% endtab %}

{% tab title="Request 2" %}
```javascript
PUT /UserSetting
```

```yaml
id: user-4
theme: unexisting
```
{% endtab %}

{% tab title="Response 2" %}
```yaml
# Response status: 422 Unprocessable Entity

resourceType: OperationOutcome
errors:
- path: [theme]
  message: expected one of dark, white
warnings: []
```
{% endtab %}
{% endtabs %}

### Restriction of Extra Attributes

We constrained only one attribute and because our `Entity.isOpen = true`, this resource can have any additional attributes without a schema. We can turn this off by setting `Entity.isOpen` to `false`:

{% tabs %}
{% tab title="Request" %}
```javascript
PATCH /Entity/UserSetting?_type=json-merge-patch
```

```yaml
isOpen: false
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: Entity
type: resource
id: UserSetting
isOpen: false
```
{% endtab %}
{% endtabs %}

Now, let's inspect the schema:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /$json-schema?path=definitions.UserSetting
```
{% endtab %}

{% tab title="Response" %}
```yaml
path: [definitions, UserSetting]
schema:
  type: object
  minProperties: 1
  patternProperties:
    ^(_.*|fhir_.*): {}
  properties:
    id: {type: string}
    extension:
      type: array
      items: {$ref: '#/definitions/Extension'}
    modifierExtension:
      type: array
      items: {$ref: '#/definitions/Extension'}
    meta: {$ref: '#/definitions/Meta'}
    resourceType: {type: string, constant: UserSetting}
    theme:
      type: string
      enum: [dark, white]
  additionalProperties: false
```
{% endtab %}
{% endtabs %}

And we see the schema keyword `additionalProperties: false` \(line 20 in the response above\) which means that now our schema is closed. Let's test it by the request with additional property `menu`:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /UserSetting
```

```yaml
theme: dark
menu: collapsed
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Response status: 422 Unprocessable Entity

resourceType: OperationOutcome
errors:
- path: [menu]
  message: extra property
warnings: []
```
{% endtab %}
{% endtabs %}

In this tutorial you've seen how to define and use Custom Resources in Aidbox. In future series we will show you how to add more advanced validations on Custom Resources and create custom endpoints to define your business logic. If you have any questions or suggestions please provide us with your feedback!

