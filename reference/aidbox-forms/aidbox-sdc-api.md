---
description: Custom SDC operations supported by Aidbox Forms.
---

# Aidbox SDC API

* [$generate-link](aidbox-sdc-api.md#generate-a-link-to-a-questionnaireresponse-usdgenerate-link)

## Generate a link to a QuestionnaireResponse - $generate-link

This operation generates a link to a web page to be used to continue answering a specified [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html).

### URLs

```
POST [base]/QuestionnaireResponse/[id]/$generate-link
```

### Parameters

{% hint style="warning" %}
NOTE: All parameters wrapped with `Parameters object`

```yaml
resourceType: Parameters
parameter:
- name:  [var-name]
  value: [var-value]
```
{% endhint %}

| Parameter                                                  | Cardinality | Type                                                                                                                                         |
| ---------------------------------------------------------- | ----------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| [allow-amend](aidbox-sdc-api.md#allow-amend)               | 0..1        | [Boolean](http://hl7.org/fhir/R4/datatypes.html#boolean)                                                                                     |
| [allow-repopulate](aidbox-sdc-api.md#allow-repopulate)     | 0..1        | [Boolean](http://hl7.org/fhir/R4/datatypes.html#boolean)                                                                                     |
| [redirect-on-submit](aidbox-sdc-api.md#redirect-on-submit) | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)                                                                                       |
| [redirect-on-save](aidbox-sdc-api.md#redirect-on-save)     | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)                                                                                       |
| [expiration](aidbox-sdc-api.md#expiration)                 | 0..1        | [Integer](http://hl7.org/fhir/R4/datatypes.html#integer)                                                                                     |
| [theme](aidbox-sdc-api.md#theme)                           | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)                                                                                       |
| [read-only](aidbox-sdc-api.md#read-only)                   | 0..1        | [Boolean](http://hl7.org/fhir/R4/datatypes.html#boolean)                                                                                     |
| [app-name](aidbox-sdc-api.md#read-only)                    | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)                                                                                       |
| source                                                     |             | [Reference\<Device, Organization, Patient, Practitioner, PractitionerRole, RelatedPerson>](http://hl7.org/fhir/R4/references.html#Reference) |
| partOf                                                     |             | [Reference\<Observation, Procedure>](http://hl7.org/fhir/R4/references.html#Reference)                                                       |
| author                                                     |             | [Reference\<Device, Practitioner, PractitionerRole, Patient, RelatedPerson, Organization>](http://hl7.org/fhir/R4/references.html#Reference) |
| basedOn                                                    |             | [Reference\<CarePlan, ServiceRequest>](http://hl7.org/fhir/R4/references.html#Reference)                                                     |

#### allow-amend

Whether the generated link will allow amending and re-submitting the form.

```
name: allow-amend
value:
  Boolean: true
```

#### allow-repopulate

Whether the generated link will allow re-populating the form.

NOTE: Repopulate will be working only with forms that contain populate behavior

```
name: allow-repopulate
value:
  Boolean: true
```

#### redirect-on-submit

A URL where the user will be redirected to after successfully submitting the form.

```yaml
name: redirect-on-submit
value:
  String: https://example.com/submit-hook?questionnaire=123
```

#### redirect-on-save

A URL where the user will be redirected to after hitting Save button.

> By default `Save button is not visible` - form autosaved after every keystroke. But sometimes it's usefull to close form in a partially-filled state

```yaml
name: redirect-on-save
value:
  String: https://example.com/submit-hook?questionnaire=123
```

#### expiration

Link expiration period (days)

```yaml
name: expiration
value:
  Integer: 30
```

> By default thir parameter = 7 days

#### theme

Form theme.

```yaml
name: theme
value:
  String: hs-theme
```

#### read-only

Show form in a **read-only** mode

```yaml
name: read-only
value:
  Boolean: true
```

**app-name**

Application name that will be used in Audit logging when returned link was used.

> Audit logging should be enabled via [configuartion](../../modules/audit/setup-audit-logging.md)

```yaml
- name: app-name
  value
    String: my-app
```

### Usage Example

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

> Aidbox uses HS256 to sign JWT token by default. To use RS256 you need to set
>
> `BOX_AUTH_KEYS_PRIVATE` and `BOX_AUTH_KEYS_PUBLIC` environment variables.
>
> [https://docs.aidbox.app/reference/configuration/environment-variables/optional-environment-variables#set-up-rsa-private-public-keys-and-secret](https://docs.aidbox.app/reference/configuration/environment-variables/optional-environment-variables#set-up-rsa-private-public-keys-and-secret)
