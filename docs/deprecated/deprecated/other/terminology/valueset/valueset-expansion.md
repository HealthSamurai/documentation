# ValueSet Expansion (deprecated)

## Overview

$expand operation expand given ValueSet in to set of concepts. For more information, see the official documentation [FHIR Terminology ValueSet Expansion](https://www.hl7.org/fhir/valueset-operations.html#expand)

## Api

```
GET/POST URL: [base]/ValueSet/$expand
```

```
GET/POST URL: [base]/ValueSet/[id]/$expand
```

Example for expand default AdministrativeGender ValueSet. [List of default ValueSets.](https://www.hl7.org/fhir/terminologies-valuesets.html)

```
GET [base]/ValueSet/administrative-gender/$expand
```

## Parameters

| Parameter              | Type                                                         | Status          | Example                                        |
| ---------------------- | ------------------------------------------------------------ | --------------- | ---------------------------------------------- |
| url                    | [uri](https://www.hl7.org/fhir/datatypes.html#uri)           | `supported`     | [url](valueset-expansion.md#url)               |
| valueSet               | [ValueSet](https://www.hl7.org/fhir/valueset.html)           | `supported`     | [valueSet](valueset-expansion.md#valueset)     |
| context                | [uri](https://www.hl7.org/fhir/datatypes.html#uri)           | `not supported` | -                                              |
| filter                 | [string](https://www.hl7.org/fhir/datatypes.html#string)     | `supported`     | [filter](valueset-expansion.md#filter)         |
| profile                | [uri](https://www.hl7.org/fhir/datatypes.html#uri)           | `not supported` | -                                              |
| date                   | [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime) | `not supported` | -                                              |
| offset                 | [integer](https://www.hl7.org/fhir/datatypes.html#integer)   | `supported`     | [offset](valueset-expansion.md#offset)         |
| count                  | [integer](https://www.hl7.org/fhir/datatypes.html#integer)   | `supported`     | [count](valueset-expansion.md#count)           |
| includeDesignations    | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `not supported` | -                                              |
| includeDefinition      | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `not supported` | -                                              |
| activeOnly             | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `supported`     | [activeOnly](valueset-expansion.md#activeonly) |
| excludeNested          | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `not supported` | -                                              |
| excludeNotForUI        | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `not supported` | -                                              |
| excludePostCoordinated | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `not supported` | -                                              |
| displayLanguage        | [code](https://www.hl7.org/fhir/datatypes.html#code)         | `not supported` | -                                              |
| limitedExpansion       | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)   | `not supported` | -                                              |

### url

A canonical url for a value set.

{% tabs %}
{% tab title="Request" %}
```http
GET [base]/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender
```

Or

```yaml
POST [base]/ValueSet/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- {name: url, valueUri: 'http://hl7.org/fhir/ValueSet/administrative-gender'}
```
{% endtab %}

{% tab title="Response" %}
```yaml
id: administrative-gender
resourceType: ValueSet
url: http://hl7.org/fhir/ValueSet/administrative-gender
description: The gender of a person used for administrative purposes.
compose:
  include:
  - {system: 'http://hl7.org/fhir/administrative-gender'}
name: AdministrativeGender
expansion:
  timestamp: '2018-09-25T16:24:55Z'
  identifier: http://hl7.org/fhir/ValueSet/administrative-gender
  contains:
  - {code: male, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Male, definition: Male}
  - {code: female, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Female, definition: Female}
  - {code: other, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Other, definition: Other}
  - {code: unknown, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Unknown, definition: Unknown}
......
```
{% endtab %}
{% endtabs %}

### valueSet

The value set is provided directly as a part of the request.

{% tabs %}
{% tab title="Request" %}
```yaml
POST [base]/ValueSet/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- name: valueSet
  resource:
    resourceType: ValueSet
    url: http://custom/administrative-gender
    compose:
      include:
      - valueSet: ['http://hl7.org/fhir/ValueSet/administrative-gender']
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ValueSet
url: http://custom/administrative-gender
compose:
  include:
  - valueSet: ['http://hl7.org/fhir/ValueSet/administrative-gender']
expansion:
  timestamp: '2018-09-26T08:51:30Z'
  identifier: http://custom/administrative-gender
  contains:
  - {code: male, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Male, definition: Male}
  - {code: female, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Female, definition: Female}
  - {code: other, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Other, definition: Other}
  - {code: unknown, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Unknown, definition: Unknown}
```
{% endtab %}
{% endtabs %}

### filter

A text filter that is applied to restrict the codes

{% tabs %}
{% tab title="Request" %}
```yaml
GET [base]/ValueSet/administrative-gender/$expand?filter=male
```

Or

```yaml
POST [base]/ValueSet/administrative-gender/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- {name: filter, valueString: male}
```
{% endtab %}

{% tab title="Response" %}
```yaml
id: administrative-gender
resourceType: ValueSet
url: http://hl7.org/fhir/ValueSet/administrative-gender
description: The gender of a person used for administrative purposes.
compose:
  include:
  - {system: 'http://hl7.org/fhir/administrative-gender'}
name: AdministrativeGender
expansion:
  timestamp: '2018-09-25T16:24:55Z'
  identifier: http://hl7.org/fhir/ValueSet/administrative-gender
  contains:
  - {code: male, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Male, definition: Male}
  - {code: female, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Female, definition: Female}
......
```
{% endtab %}
{% endtabs %}

### offset

Paging support - where to start if a subset is desired (default = 0).

{% tabs %}
{% tab title="Request" %}
```yaml
GET [base]/ValueSet/administrative-gender/$expand?offset=2
```

Or

```yaml
POST [base]/ValueSet/administrative-gender/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- {name: offset, valueInteger: 2}
```
{% endtab %}

{% tab title="Response" %}
```yaml
id: administrative-gender
resourceType: ValueSet
url: http://hl7.org/fhir/ValueSet/administrative-gender
description: The gender of a person used for administrative purposes.
compose:
  include:
  - {system: 'http://hl7.org/fhir/administrative-gender'}
name: AdministrativeGender
expansion:
  timestamp: '2018-09-25T16:24:55Z'
  identifier: http://hl7.org/fhir/ValueSet/administrative-gender
  contains:
  - {code: other, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Other, definition: Other}
  - {code: unknown, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Unknown, definition: Unknown}
......
```
{% endtab %}
{% endtabs %}

### count

Paging support - how many codes should be provided in a partial page view.

{% tabs %}
{% tab title="Request" %}
```yaml
GET [base]/ValueSet/administrative-gender/$expand?count=1
```

Or

```yaml
POST [base]/ValueSet/administrative-gender/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- {name: count, valueInteger: 1}
```
{% endtab %}

{% tab title="Response" %}
```yaml
id: administrative-gender
resourceType: ValueSet
url: http://hl7.org/fhir/ValueSet/administrative-gender
description: The gender of a person used for administrative purposes.
compose:
  include:
  - {system: 'http://hl7.org/fhir/administrative-gender'}
name: AdministrativeGender
expansion:
  timestamp: '2018-09-25T16:24:55Z'
  identifier: http://hl7.org/fhir/ValueSet/administrative-gender
  contains:
  - {code: male, module: fhir-3.3.0, system: 'http://hl7.org/fhir/administrative-gender', display: Male, definition: Male}
......
```
{% endtab %}
{% endtabs %}

### activeOnly

Controls whether inactive concepts are included or excluded in value set expansions.

For example, we create a testing ValueSet with one current active concept and one deprecated concept provided directly as a part of the request.

Get all concepts

{% tabs %}
{% tab title="Request" %}
```yaml
POST [base]/ValueSet/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- {name: activeOnly, valueBoolean: false}
- name: valueSet
  resource:
    resourceType: ValueSet
    url: http://custom/testing
    compose:
      include:
      - system: http://testing
        concept:
        - {code: active}
        - {code: inactive, deprecated: true}
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ValueSet
url: http://custom/testing
compose:
  include:
  - system: http://testing
    concept:
    - {code: active}
    - {code: inactive, deprecated: true}
expansion:
  timestamp: '2018-09-26T09:19:03Z'
  identifier: http://custom/testing
  contains:
  - {code: active, system: 'http://testing'}
  - {code: inactive, system: 'http://testing', deprecated: true}
```
{% endtab %}
{% endtabs %}

\
Get only active concepts

{% tabs %}
{% tab title="Request" %}
Get only active concepts

```yaml
POST [base]/ValueSet/$expand
content-type: text/yaml

resourceType: Parameters
parameter:
- {name: activeOnly, valueBoolean: true}
- name: valueSet
  resource:
    resourceType: ValueSet
    url: http://custom/testing
    compose:
      include:
      - system: http://testing
        concept:
        - {code: active}
        - {code: inactive, deprecated: true}
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ValueSet
url: http://custom/testing
compose:
  include:
  - system: http://testing
    concept:
    - {code: active}
    - {code: inactive, deprecated: true}
expansion:
  timestamp: '2018-09-26T09:19:03Z'
  identifier: http://custom/testing
  contains:
  - {code: active, system: 'http://testing'}
```
{% endtab %}
{% endtabs %}
