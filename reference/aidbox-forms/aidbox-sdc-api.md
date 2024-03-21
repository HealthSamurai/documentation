---
description: Custom SDC operations supported by Aidbox Forms.
---

* [$generate-link](aidbox-sdc-api.md#generate-a-link-to-a-questionnaireresponse-usdgenerate-link)

# Generate a link to a QuestionnaireResponse - $generate-link

This operation generates a link to a web page to be used to continue answering a specified [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html).

## URLs

```
POST [base]/QuestionnaireResponse/[id]/$generate-link
```

## Parameters

### allow-amend

Whether the generated link will allow amending and re-submitting the form.

```
name: allow-amend
value:
  Boolean: true
```

### redirect-on-submit

A URL where the user will be redirected to after successfully submitting the form.

```yaml
name: redirect-on-submit
value:
  String: https://example.com/submit-hook?questionnaire=123
```
