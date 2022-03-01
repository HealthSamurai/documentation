# API constructor (beta)

Using Aidbox API constructor you can:

* define API, endpoints, handlers, and URL params schema&#x20;
* specify your own middlewares (e.g. Smart on FHIR authorization middleware)

{% hint style="warning" %}
The API constructor is beta now. You can find how API constructor is used to enable SMART on FHIR in [Aidbox sample project](https://github.com/Aidbox/aidbox-project-samples#smart-on-fhir-aidbox-installation).
{% endhint %}

{% hint style="info" %}
Please [contact](../contact-us.md) us if you have questions or need help with API constructor.
{% endhint %}

## Example setup

Use [`bb` Devbox setup](../getting-started/installation/devbox-with-bb.md) to start Aidbox, it contains configured API Constructor example. Once Aidbox is running, open `Profiles` tab in the Aidbox UI. If everything is configured properly, page should contain namespace with `BOX_ENTRYPOINT` symbol. View of the symbol should show loaded routing.

Here's a notebook with routing defined via API constructor demo:&#x20;

```
https://aidbox.app/ExportedNotebook/cd4a85b1-fbf2-4f8e-ad73-ee8a18b63f5b 
```

You can [import it](https://web.telegram.org/o/-LHqtKitlMYF2y7QBlXS/s/-LHqtKiuedlcKJLm337\_/\~/changes/gsp3ApDuLk8tbTr5YTKj/aidbox-ui/notebooks#import-a-notebook) into the Aidbox you have running with bb and run test REST requests.

## API Constructor

### Entrypoint

Define an Aidbox server definition with `BOX_ENTRYPOINT` env, the value must be a `namespace/symbol`, e.g.:

```bash
BOX_ENTRYPOINT=mybox/box
```

{% hint style="info" %}
The namespace with entrypoint symbol must be loaded: file containing namespace mentioned in `AIDBOX_ZEN_PATHS` and imported or specified directly in `AIDBOX_ZEN_ENTRY`/`AIDBOX_ZEN_ENTRYPOINT` env.\
[More info on loading namespace](aidbox-zen-lang-project.md)
{% endhint %}

Entrypoint symbol must be tagged with `aidbox/system` tag. `aidbox/system` describes set of services to start and configurations.

### Services

A service contains `engine` and its configuration.

List of available engines:

* `aidbox/http`

#### `aidbox/http`

Describes http service, contains set of `apis`. Each api must be tagged with `aidbox.rest/api`.

`aidbox.rest/api` describes routing, route can contain operations, subroutes and other apis. Operations must be tagged with `aidbox.rest/op`.

`aidbox.rest/op` describes REST operation.

### Example

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
  "Observation" {:middlewares  [inject-patient-search-parameter-mw]
                 :GET   obs-search-op
                 [:id]  {:GET obs-read-op}}}

 obs-search-op
 {:zen/tags  #{aidbox.rest/op}
  :engine    aidbox.rest.v1/search
  :resource  "Observation"
  :format    "aidbox"}

 obs-read-op
 {:zen/tags    #{aidbox.rest/op}
  :engine      aidbox.rest.v1/read
  :params      {:patient observation-patient-param}
  :resource    "Observation"
  :format      "aidbox"}

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

