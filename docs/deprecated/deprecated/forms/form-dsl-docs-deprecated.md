---
hidden: true
---

# Form DSL (docs deprecated)

Form DSL used just to bind all DSLs to one item.

| Property   | Description                                                      | Type/Struct | required? |
| ---------- | ---------------------------------------------------------------- | ----------- | --------- |
| title      | Title of the form                                                | string      | no        |
| properties | Form metadata. Usable for several scenarious (search/convestion) | map         | no        |
| version    | Form version (managed automatically by aidbox-forms)             | string      | no        |
| document   | Symbolic reference to Document                                   | symbol      | yes       |
| layout     | Symbolic reference to Layout                                     | symbol      | no        |
| launch     | Symbolic reference to Launch                                     | symbol      | no        |
| finalize   | Symbolic reference to Finalize                                   | symbol      | no        |

* Only `document` layer is required.
* Other layers you add only if you need special logic there.
* `properties` can be used for
  * forms search with `include/exclude` filter via [`aidbox.sdc/get-forms`](form-api-docs-deprecated.md#get-forms)
  * form conversion to Questionnaire via [`aidbox.sdc/convert-form`](form-api-docs-deprecated.md#convert-form) and [`aidbox.sdc/convert-forms`](form-api-docs-deprecated.md#convert-forms)

Form DSL example:

```
 VitalsForm
 {:zen/tags   #{aidbox.sdc/Form}
  :title      "Vitals Signs"
  :properties {:teams #{"physician" "surgery"}}
  :document   VitalsDocument
  :layout     VitalsLayout
  :launch     VitalsLaunch
  :finalize   VitalsFinalize}
```

## Properties for Conversion

You can convert Form to Questionnaire, and for that you are able to add some metadata for for your form.

There are some predifined properties for that:

| Property                  | Description                                          | Type/Struct                                      |
| ------------------------- | ---------------------------------------------------- | ------------------------------------------------ |
| `:fhir/id`                | Questionnaire resource id used to store to DB        | `string`                                         |
| `:fhir/publisher`         | Name of the publisher (organization or individual)   | `string`                                         |
| `:fhir/status`            | Questionnaire status                                 | `draft/active/retired/unknown`                   |
| `:fhir/contact`           | Contact details for the publisher                    | `[{name: string, telecom: [{system, value}]}]`   |
| `:fhir/purpose`           | Why this questionnaire is defined                    | `string`                                         |
| `:fhir/copyright`         | Use and/or publishing restrictions                   | `string`                                         |
| `:fhir/approvalDate`      | When the questionnaire was approved by publisher     | `date`                                           |
| `:fhir/lastReviewDate`    | When the questionnaire was last reviewed             | `date`                                           |
| `:fhir/effectivePeriod`   | When the questionnaire is expected to be used        | `{start: date, end: date}`                       |
| ------------------------- | ---------------------------------------------------- | ------------------------------------------------ |

> All these properties correspodents FHIR Questionnaire's root level fields

All properties are optional - but, if you want to save converted Questionnaire to it's resource table you should specify `:fhir/id` property
