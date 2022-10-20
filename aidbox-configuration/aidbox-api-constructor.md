# API constructor (beta)

Using Aidbox API constructor you can:

* define API, endpoints, handlers, and URL params schema
* specify your own middlewares (e.g. Smart on FHIR authorization middleware)

{% hint style="warning" %}
The API constructor is in beta now. Please [contact](../contact-us.md) us if you have questions or need help.
{% endhint %}

{% hint style="info" %}
API constructor requires knowledge of [zen language](https://github.com/zen-lang/zen).
{% endhint %}

#### Common API Constructor use cases

{% content-ref url="aidbox-api-constructor/acl.md" %}
[acl.md](aidbox-api-constructor/acl.md)
{% endcontent-ref %}

{% content-ref url="../modules-1/smartbox/background-information/multitenancy-approach.md" %}
[multitenancy-approach.md](../modules-1/smartbox/background-information/multitenancy-approach.md)
{% endcontent-ref %}

{% content-ref url="../security-and-access-control-1/auth/smart-app/" %}
[smart-app](../security-and-access-control-1/auth/smart-app/)
{% endcontent-ref %}



#### Usage examples:

* [Sample API](https://github.com/Aidbox/aidbox-project-samples/blob/main/aidbox-project-samples/api-constructor/mybox.edn) used in this documentation page example.
* [Smart on FHIR configuration](https://github.com/Aidbox/aidbox-project-samples/blob/main/aidbox-project-samples/smartbox/smartbox/smart-api.edn)
* [ACL example](https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples/acl)
* [Multitenancy example](https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples/multitenancy)

## Example setup

Use [Run Aidbox locally](../getting-started/run-aidbox-locally-with-docker/) to start Aidbox, here is configured [API constructor example](https://github.com/Aidbox/aidbox-project-samples/blob/main/aidbox-project-samples/api-constructor/mybox.edn). Add these environment variables:

```bash
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
AIDBOX_ZEN_ENTRYPOINT=mybox/box
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples/api-constructor
```

Once Aidbox is running, open `Profiles` tab in the Aidbox UI. If everything is configured properly, page should contain namespace with `AIDBOX_ZEN_ENTRYPOINT` symbol which is `mybox/box` in this example. View of the symbol should show loaded routes.

Here's a notebook with the example API demo usage :

```
https://aidbox.app/ExportedNotebook/df9ac147-daa4-4495-87b5-c4367cd441ef
```

You can [import it](../overview/aidbox-ui/notebooks.md#import-a-notebook) into the example and run test REST requests.

## API constructor definitions

### Entrypoint

Define an Aidbox server definition with `AIDBOX_ZEN_ENTRYPOINT` env, the value must be a `namespace/symbol`, e.g.:

```bash
AIDBOX_ZEN_ENTRYPOINT=mybox/box
```

{% hint style="info" %}
The namespace with entrypoint symbol must be loaded: file containing namespace mentioned in `AIDBOX_ZEN_PATHS` and imported or specified directly in`AIDBOX_ZEN_ENTRYPOINT` env.\
[More info on loading namespace](aidbox-zen-lang-project/)
{% endhint %}

Entrypoint symbol must be tagged with `aidbox/system` tag. `aidbox/system` describes a set of services to start and configurations.

#### Example

```clojure
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:http server}}
```

### Services

A service contains `engine` and its configuration.

#### Available engines:

* `aidbox/http` - Describes http service, contains set of `apis`. Each api must be tagged with `aidbox.rest/api`.
* `aidbox/seed` - Creates provided fixtures on start. Described [here](aidbox-zen-lang-project/#seed-import).

#### Example

```clojure
 server
 {:zen/tags #{aidbox/service}
  :engine   aidbox/http
  :apis     #{api}}
```

### `aidbox.rest/api`

An `api` describes routing, route can contain operations, subroutes and other apis. Operations must be tagged with `aidbox.rest/op`.

`:middlewares` can be added to an `api`.

#### Example

```clojure
 api
 {:zen/tags #{aidbox.rest/api}
  "multi-example" {"Patient" {:apis #{get-patient-api change-patient-api}}}}

 change-patient-api
 {:zen/tags    #{aidbox.rest/api}
  :middlewares [inject-tenant-mw]
  :POST multi-box.operations/create-patient
  [:id] {:PUT    multi-box.operations/update-patient
         :DELETE multi-box.operations/delete-patient}}
```

### `aidbox.rest/op`

An `op` describes REST operation. `:engine` specifies what operation handler should be used for this operation. Each engine can accept own parameters

#### Example

```clojure
 create-patient
 {:zen/tags #{aidbox.rest/op}
  :engine   aidbox.rest.v1/create
  :resource "Patient"
  :format   "fhir"}
```

### Available `aidbox.rest/op-engine`s

#### Miscellaneous

* `aidbox.rest.v1/aidbox-action` - Expects `:action`, passes request to existing Aidbox action. You can see list of available operations with this request:\
  `GET /Operation?_elements=action&_result=array&_count=1000`
* `aidbox.rest.v1/echo` - expects `:response` in the definition, returns the response.

#### Regular FHIR API

Expect target resource type as `:resource` and `:format` (`fhir` or `aidbox`)

* `aidbox.rest.v1/search`
* `aidbox.rest.v1/create`
* `aidbox.rest.v1/read`
* `aidbox.rest.v1/update`
* `aidbox.rest.v1/delete`
* `aidbox.rest.v1/patch`
* `aidbox.rest.v1/transaction`

#### ACL FHIR API

* `aidbox.rest.acl/search`
* `aidbox.rest.acl/create`
* `aidbox.rest.acl/read`
* `aidbox.rest.acl/update`
* `aidbox.rest.acl/delete`

See full description and usage examples:

{% content-ref url="aidbox-api-constructor/acl.md" %}
[acl.md](aidbox-api-constructor/acl.md)
{% endcontent-ref %}

### Middlewares

Middlewares can change incoming request before executing `op`. Middlewares are applied to `aidbox.rest/api` which contains operations or other apis. On request Aidbox routing determines what `op` should be executed. Before executing the `op` Aidbox collects all middlewares applied to the apis containing the op. Then collected middlewares are applied in sequence to the request.

A middleware should be defined with specified `:engine`. The `:engine` determines what the middleware will do. Depending on the chosen `:engine` middleware can accept different parameters.

#### Example

```clojure
 inject-tenant-mw
 {:zen/tags #{aidbox.rest/middleware}
  :engine aidbox.rest.v1/transform-middleware
  :rules  {[:resource :meta :tenantId]  [:oauth/user :data :tenantId]}}

 change-patient-api
 {:zen/tags    #{aidbox.rest/api}
  :middlewares [inject-tenant-mw]
  :POST multi-box.operations/create-patient
  [:id] {:PUT    multi-box.operations/update-patient
         :DELETE multi-box.operations/delete-patient}}
```

#### List of available `aidbox.rest/middleware-engine`

* `aidbox.rest.v1/match-middleware` - checks if a request satisfies to some pattern. Similar to the `AccessPolicy` `matcho` engine
* `aidbox.rest.v1/params-middleware` - adds request query-string parameters taking data from the request context
* `aidbox.rest.v1/transform-middleware` - transforms incoming request taking data from the request context
* `aidbox.rest.middlewares/transform-request-data` - the same as `transform-middleware` but provides more complex functionality

### Project with API Constructor example

Aidbox configuration with search and read on `Observation` resource available at `GET /access/Observation` and `GET /access/Observation/<id>`. Defined endpoints also contain a middleware injecting `patient` search parameter to ensure that user can only search for Observations for a specific patient.

```clojure
{ns mybox
 import #{aidbox aidbox.rest aidbox.rest.v1}

 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:http server}}

 server
 {:zen/tags #{aidbox/service}
  :engine   aidbox/http
  :apis     #{api}}

 api
 {:zen/tags #{aidbox.rest/api}
  "access"  {:apis #{access-api}}}

 access-api
 {:zen/tags #{aidbox.rest/api}
  "Observation" {:middlewares [inject-patient-search-parameter-mw]
                 :GET  obs-search-op
                 [:id] {:GET obs-read-op}}}

 obs-search-op
 {:zen/tags #{aidbox.rest/op}
  :engine   aidbox.rest.v1/search
  :resource "Observation"
  :format   "aidbox"}

 obs-read-op
 {:zen/tags #{aidbox.rest/op}
  :engine   aidbox.rest.v1/read
  :params   {:patient observation-patient-param}
  :resource "Observation"
  :format   "aidbox"}

 inject-patient-search-parameter-mw
 {:zen/tags #{aidbox.rest/middleware}
  :engine aidbox.rest.v1/params-middleware
  :params {:patient {:path     [:user :data :pid]
                     :required true}}}

 observation-patient-param
 {:zen/tags   #{aidbox.rest/param}
  :engine     aidbox.rest.v1/param
  :search-by  aidbox.rest.v1/search-by-knife
  :name       "patient"
  :expression [["subject" "id"]]}}
```
