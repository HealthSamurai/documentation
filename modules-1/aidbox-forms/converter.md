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

### Forms conversion on startup

Aidbox Forms support automatic Form convertion to Questionnaire resource on Aidbox startup.

You need to configure your `aidbox/system` with `sdc-service` and it's configuration.

> This feature you can configure via [api-constructor](../../aidbox-configuration/aidbox-api-constructor.md) in zen-project.


Example:

> Your zen-project entrypoint namespace (box.edn for example)

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:sdc sdc-service}}
```

`sdc-service` configuration

```
 sdc-service
 {:zen/tags  #{aidbox/service}
  :engine    aidbox.sdc/service
  :conversion {:convert-forms-on-start true}}
```
