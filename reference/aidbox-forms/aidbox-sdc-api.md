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

{% hint style="warning" %}
NOTE:  All parameters wrapped with  `Parameters object`

```yaml
resourceType: Parameters
parameter:
- name:  [var-name]
  value: [var-value]
```
{% endhint %}


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

### redirect-on-submit

A URL where the user will be redirected to after hitting Save button.

> By default `Save button is not visible` - form autosaved after every keystroke. But sometimes it's usefull to close form in a partially-filled state

```yaml
name: redirect-on-save
value:
  String: https://example.com/submit-hook?questionnaire=123
```

### link expiration time

Link expiration period (days)


```yaml
name: expiration
value:
  Integer: 30
```

> By default thir parameter = 7 days


### theme

Form theme.

```yaml
name: theme
value:
  String: hs-theme
```

## Usage Example

{% tabs %}
{% tab title="Request" %}
```http
POST [base]/QuestionnaireResponse/[id]/$generate-link
content-type: text/yaml

resourceType: Parameters
parameter:
  - name: allow-amend
    value:
      Boolean: true
  - name: redirect-on-submit
    value:
      String: https://example.com/submit-hook?questionnaire=123
```
{% endtab %}

{% tab title="Success Response" %}

HTTP status: 200

```yaml
link: http://forms.aidbox.io/ui/sdc#/questionnaire-response/12c1178c-70a9-4e02-a53d-65b13373926e?token=eyJhbGciOiJIUzI
```
{% endtab %}

{% tab title="Failure Response" %}

HTTP status: 422

```yaml
resourceType: OperationOutcome
text:
  status: generated
  div: Parameters are invalid
issue:
- severity: error
  code: invalid
  expression:
  - parameter.0.resource
  diagnostics: unknown key :resource

```
{% endtab %}

{% endtabs %}
