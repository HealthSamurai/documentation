# FHIR Questionnaire to Aidbox Forms and Back Conversion

You can use Aidbox to convert

* FHIR Questionnaires to Aidbox Forms
* Aidbox Forms to FHIR Questionnaires (with some limitations)
* SDCDocument to FHIR QuestionnaireResponse resource.

You can use [Aidbox Form API](../../../deprecated/deprecated/forms/form-api-docs-deprecated.md)

* [`aidbox.sdc/convert-document`](../../../deprecated/deprecated/forms/form-api-docs-deprecated.md#convert-document) - converts SDCDocument to FHIR QuestionnaireResponse
* [`aidbox.sdc/convert-questionnaire`](../../../deprecated/deprecated/forms/form-api-docs-deprecated.md#convert-questionnaire)- converts FHIR Questionnaire to Aidbox SDC Form
* [`aidbox.sdc/convert-form`](../../../deprecated/deprecated/forms/form-api-docs-deprecated.md#convert-form) - converts Form to FHIR Questionnaire
* [`aidbox.sdc/convert-forms`](../../../deprecated/deprecated/forms/form-api-docs-deprecated.md#convert-forms) - converts all Forms to Questionnaire and save them in Aidbox

## Optional features

Aidbox Forms supports

* automatic Form convertion to Questionnaire resource on Aidbox startup.
* automatic SDCDocument convertion to QuestionnaireResponse resource on SDCDocument save.
* Form rules conversion to human-readable description.

> These features can be configured via [api-constructor](../../../deprecated/deprecated/zen-related/api-constructor-docs-beta.md) in zen-project.

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
  :conversion {:convert-forms-on-start {:enabled true}}
```

You need to restart aidbox to take effect of changed configuration

### Document conversion on save

For Document conversion you should set `[:conversion :convert-doc-on-save]` property to `true`

Example:

```
 sdc-service
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.sdc/service
  :conversion {:convert-doc-on-save {:enabled true}}
```

You need to restart aidbox to take effect of changed configuration

After that - every document changes will be reflected in QuestionnaireResponse resources.

> NOTE: `id` of converted `QuestionnaireResponse` will be the same as `id` of `SDCDocument`.

### Form rules conversion to human-readable description.

You can enable `aidbox.sdc/rules` conversion to human friendly text while converting Form or Document.

Because Questionnaire and QuestionnaireResponse structures doesn't have place where to store such data We define 2 extra profiles for these resource-types.

You can enable this feature by adding:

* `zen.fhir` and `hl7-fhir-r4-core` dependecies to your zen-package.edn
* `aidbox.sdc.extra` import to your entrypoint namespace
* `:describe-rules` key to service configuration or RPC call

Example:

zen-package.edn

```
{:deps {zen.fhir "https://github.com/zen-fhir/zen.fhir.git"
        hl7-fhir-r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"}}
```

box.edn

> Assume that box.edn is your zen-project entrypoint namespace

```
{ns box
 import #{aidbox.sdc.extra}

 ...

 sdc-service
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.sdc/service
  :conversion {:convert-doc-on-save {:enabled true
                                     :describe-rules true}}
 }
```

When feature is enabled you will see additional data in `Questionnaire` and `QuestionnaireResponse` after converting `Forms` and `Documents` with rules.

In `Aidbox` format you will see `rule-description` field.

```
GET /Questionnaire/q1

resourceType: Questionnaire
item:
  - linkId: 1
    text: BMI
    rule-description: "A / B * B\n\n where\n* A - body weight\n* B - body height"
```

In `FHIR` format you will see an extension with `valueMarkdown`

```
GET /fhir/Questionnaire/q1

resourceType: Questionnaire
item:
  - linkId: 1
    text: BMI
    extension:
       - url: urn:fhir:extension:sdc-questionnaire-ruleDescription
         valueMarkdown: A / B * B\n\n where\n* A - body weight\n* B - body height
```

### Include `score` value in QuestionnaireResponse

`aidbox.sdc/choice` field can have additional field called `:score` that have no represantation in QuestionnaireResponse.

You can include this field in conversion by adding:

* `zen.fhir` and `hl7-fhir-r4-core` dependecies to your zen-package.edn
* `aidbox.sdc.extra` import to your entrypoint namespace
* `:include-scores` key to service configuration or RPC call

Example:

zen-package.edn

```
{:deps {zen.fhir "https://github.com/zen-fhir/zen.fhir.git"
        hl7-fhir-r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"}}
```

box.edn

> Assume that box.edn is your zen-project entrypoint namespace

```
{ns box
 import #{aidbox.sdc.extra}

 ...

 sdc-service
 {:zen/tags   #{aidbox/service}
  :engine     aidbox.sdc/service
  :conversion {:convert-doc-on-save {:enabled true
                                     :include-scores true}}
 }
```

When feature is enabled you will see additional data in `QuestionnaireResponse` after converting `Documents` with scores.

In `Aidbox` format you will see `score` field.

```
GET /QuestionnaireResponse/q1

resourceType: QuestionnaireResponse
item:
  - linkId: 69734-2
   	text: Trouble relaxing
    answer:
      - value:
          Coding:
            system: http://loinc.org
            code: LA6571-9
            display: Nearly every day
            score: 3
```

In `FHIR` format you will see an extension with `valueInteger`

```
GET /fhir/Questionnaire/q1

resourceType: Questionnaire
item:
  - linkId: 69725-0
    text: Feeling nervous, anxious, or on edge
    answer:
      - valueCoding:
          system: http://loinc.org
          code: LA6569-3
          display: Several days
          extension:
            - url: urn:fhir:extension:sdc-questionnaire-score
              valueInteger: 1
```
