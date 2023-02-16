# Converter

You can use Aidbox to convert 
- FHIR Questionnaires to Aidbox Forms
- Aidbox Forms to FHIR Questionnaires (with some limitations)
- SDCDocument to FHIR QuestionnaireResponse resource.

You can use [Aidbox Form API](../../reference/aidbox-forms/api-reference.md)

* [`aidbox.sdc/convert-document`](../../reference/aidbox-forms/api-reference.md#convert-document) - converts SDCDocument to FHIR QuestionnaireResponse
* [`aidbox.sdc/convert-questionnaire`](../../reference/aidbox-forms/api-reference.md#convert-questionnaire)- converts FHIR Questionnaire to Aidbox SDC Form&#x20;
* [`aidbox.sdc/convert-form`](../../reference/aidbox-forms/api-reference.md#convert-form) - converts Form to FHIR Questionnaire
* [`aidbox.sdc/convert-forms`](../../reference/aidbox-forms/api-reference.md#convert-forms) - converts all Forms to Questionnaire and save them in Aidbox




## Optional features

Aidbox Forms supports

- automatic Form convertion to Questionnaire resource on Aidbox startup.
- automatic SDCDocument convertion to QuestionnaireResponse resource on SDCDocument save.

> These features can be configure via [api-constructor](../../aidbox-configuration/aidbox-api-constructor.md) in zen-project.

You need to configure your `aidbox/system` with `sdc-service` and it's configuration.

Example:

> Your zen-project entrypoint namespace (box.edn for example)

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:sdc sdc-service}}
```


### Forms conversion on startup

For forms conversion you should set `[:conversion :convert-forms-on-start]` property to `true`

Example:

```
 sdc-service
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.sdc/service
  :conversion {:convert-forms-on-start true}}
```

You need to restart aidbox to take effect of changed configuration


### Document conversion on save

For Document conversion you should set `[:conversion :convert-doc-on-save]` property to `true`

Example:

```
 sdc-service
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.sdc/service
  :conversion {:convert-doc-on-save true}}
```

You need to restart aidbox to take effect of changed configuration

After that - every document changes will be reflected in QuestionnaireResponse resources.


> NOTE: `id` of converted `QuestionnaireResponse` will be the same as `id` of `SDCDocument`.
