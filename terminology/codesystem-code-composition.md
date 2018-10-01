# CodeSystem Code Composition

## Overview

Given a set of properties, return one or more possible matching codes. For more details see official FHIR terminology documentation [CodeSystem Code Composition](https://www.hl7.org/fhir/codesystem-operations.html#compose)

$compose may return  3 possible types of match

* complete match - a code that represents all the provided properties correctly
* partial match - a code that represents some of the provided properties correctly, and not others
* possible match - code that may represent the provided properties closely,

When send `exact` parameter is `true` - $compose operation return only complete and partial matches. When exact is `false` - $compose include in to response possible matches. Default value is true, that means, that by default returning only complete and partial matches.

##  Api

```text
GET/POST URL: [base]/ValueSet/$compose
```

```text
GET/POST URL: [base]/ValueSet/[id]/$compose
```

## Parameters

| Parameter | Type | Status |
| :--- | :--- | :--- |
| system | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` |
| version | [string](https://www.hl7.org/fhir/datatypes.html#string) | `not supported` |
| property |  | `supported` |
| property.code | [code](https://www.hl7.org/fhir/datatypes.html#code) | `supported` |
| property.value | code \| Coding \| string \| integer \| boolean \| dateTime | `supported` |
| property.subproperty |  | `not supported` |
| property.subproperty.code | [code](https://www.hl7.org/fhir/datatypes.html#code) | `not supported` |
| property.subproperty.value | code \| Coding \| string \| integer \| boolean \| dateTime | `not supported` |
| exact | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `supported` |
| compositional | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` |

## Example



{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

