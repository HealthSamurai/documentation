# Custom Resources

Sometimes your data does not fit any existing FHIR resources. It is not always obvious, that your data can not be translated to FHIR \(because of some FHIR generalisations\). The "right" first step is to go to [FHIR community chat](http://health-samurai.info/a-cusres-to-zulip) and ask your specific question or contact health-samurai modelling team with your concern. If after this adventure you are sure - there is no such resource in FHIR or it will take to much time to wait for it - in aidbox you can define your own **Custom Resources.**

**Custom Resources** are defined exactly the same way as core FHIR resources, can refer existing resources, have uniform REST API for CRUD and Search and participate in transactions.

Let's imagine in our app we want to save User preferences like UI configuration or personalised Patient List filters. We expect you  already created your box in [Aidbox.Cloud](https://docs.aidbox.app/~/drafts/-LOrgfiiMwbxfp70_ZP0/primary/v/master/installation/use-aidbox.cloud). First of all we have to define new resource type by creating **Entity** resource.

​Go to REST console and paste following request:

```yaml
POST /Entity​

id: UserSetting
type: resource
isOpen: true
```

You should see response like:

```yaml
resourceType: Entity
id: UserSetting
meta:
  lastUpdated: '2018-10-16T12:19:51.672Z'
  versionId: '2'
type: resource
isOpen: true
```

This mean that resource of type Entity was successfully created.  When you create Entity resources with type `resource`, aidbox on fly will initialise storage for your new resources and generate CRUD & Search REST API.

When ew set `isOpen: true` flag - this means resource does not have specific structure and you can store arbitrary JSON document. This is useful when you do not know exact resource structure while working on prototype. Later we will make it's schema more strict and add validations.

Let's checkout API for our custom resource - UserSettings. You can list UserSettings resources by standard FHIR uri template - `GET /{resourceType}` - i.e. `GET /UserSetting`. But at that moment this list is empty. You can test it using same rest console:

```yaml
GET /UserSetting

>> response
resourceType: Bundle
type: searchset
params: []
query-sql: ['SELECT "usersetting".* FROM "usersetting" LIMIT ? OFFSET ?', 100, 0]
query-time: 2
entry: []
total: _undefined
link: []
```

In query-sql we see, what query is executed by aidbox to get this resources and can see table "usersettings" was created to store resources. You can test it using DB Console using following query:

```sql
SELECT * FROM "usersetting";
```

Cool! Now let's create first UserSetting resource using REST console:

```yaml
POST /UserSetting

id: user-1
theme: dark

## response
resourceType: UserSetting
meta:
  lastUpdated: '2018-10-16T12:33:21.225Z'
  versionId: '3'
id: user-1
theme: dark
```

Try to get all user settings now:

```yaml
GET /UserSetting

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

Or execute sql query:

```sql
SELECT id, resource->>'theme' as theme FROM "usersetting";
```

| id | theme |
| :--- | :--- |
| user-1 | dark |

As well you can read, update and delete UserSettings resource with:

```yaml
GET /UserSetting/user-1
# response: resource
id: user-1
theme: white

# Update 
PUT /UserSetting/user-1

theme: white
patientsFilters:
  - location: ICU

# response
resourceType: UserSetting
id: user-1
theme: white
patientsFilters:
- {location: ICU}

# Read history
GET /UserSetting/user-1/_history
# response:
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
  
# Delete resource
DELETE /UserSetting/user-1
# response: status 204


# And again watch history:
GET /UserSetting/user-1/_history

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

Awesome we've got nice API by just providing couple of lines of metadata. But the schema of our custom resource now is too open and API users can put any data into UserSetting resource. For example create such resource:

```yaml
POST /UserSetting

id: user-1
theme:
  - name: white
  - name: black
```

Now let's put some restrictions and define our Custom Resource structure/schema. To describe structure of resource we should use Attribute meta-resource. For example we want to restrict theme attribute to be string from specific enum:

```yaml
POST /Attribute

id: UserSetting.theme
path: ['theme']
type: {id: string, resourceType: Entity}
enum: ['dark', 'white']
resource: {id: UserSetting, resourceType: Entity}
```

To validate incoming resources aidbox uses json-schema, which generated from Entity & Attribute meta-resources. We can expect what schema will be applied to UserSetting resources:

```yaml
GET /$json-schema?path=definitions.UserSetting

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

As we see on line 19, `theme` property now has type string and restricted by enum. 

Let's try to create invalid resource now:

```yaml
POST /UserSetting

id: user-1
theme: 2

# response status 422:

resourceType: OperationOutcome
errors:
- path: [theme]
  message: expected type of string
- path: [theme]
  message: expeceted one of dark, white
warnings: []

# or
POST /UserSetting

id: user-1
theme: unexisting

# response status 422:
resourceType: OperationOutcome
errors:
- path: [theme]
  message: expeceted one of dark, white
warnings: []
```

We constrained only one attribute and because our Entity.isOpen = true -  this resource  can have any additional attributes without schema. We can turn of this by setting Entity.isOpen to false:

```yaml
PATCH /Entity/UserSetting?_type=json-merge-patch

isOpen: false
```

Now let inspect the schema:

```yaml
GET /$json-schema?path=definitions.UserSetting

# response:
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

And we see schema keyword `additionalProperties: false,` which means now our schema is closed. Let's test it:

```yaml
POST /UserSetting

theme: dark
menu: collapsed

# response 422:

resourceType: OperationOutcome
errors:
- path: [menu]
  message: extra property
warnings: []
```

