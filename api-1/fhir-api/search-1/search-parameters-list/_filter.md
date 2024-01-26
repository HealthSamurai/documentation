# \_filter

Aidbox offers the partial support of FHIR [\_filter](https://www.hl7.org/fhir/search\_filter.html) API. However, we tend to use other search capabilities like AidboxQuery, SearchQuery, or Search resource for complex queries. They offer better expressiveness with SQL and better performance.

## Supported operators

<table><thead><tr><th width="128">Operation</th><th width="87" align="center">String</th><th width="100" align="center">Number</th><th align="center">Date</th><th width="100" align="center">Token</th><th width="122" align="center">Reference</th><th align="center">Quantity</th></tr></thead><tbody><tr><td>eq</td><td align="center">+</td><td align="center">+**</td><td align="center">+</td><td align="center">+*</td><td align="center">n/a</td><td align="center">+***</td></tr><tr><td>ne</td><td align="center">-</td><td align="center">+**</td><td align="center">+</td><td align="center">-</td><td align="center">n/a</td><td align="center">+***</td></tr><tr><td>co</td><td align="center">+</td><td align="center">-</td><td align="center">-</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>sw</td><td align="center">+</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>ew</td><td align="center">+</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>gt/ge/lt/le</td><td align="center">-</td><td align="center">+</td><td align="center">+</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">+***</td></tr><tr><td>po</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">-</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>ss</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">-</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>sb</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">-</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>in</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">-</td><td align="center">n/a</td><td align="center">n/a</td></tr><tr><td>re</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">n/a</td><td align="center">-</td><td align="center">n/a</td></tr></tbody></table>

\* token search is case sensitive

\*\* number search doesn't support implicit precision

\*\*\* support only numbers, not system with code

## Chaining, dot expressions

Aidbox supports forward chained search parameters in \_filter query and dot expressions.

{% content-ref url="../chained-parameters.md" %}
[chained-parameters.md](../chained-parameters.md)
{% endcontent-ref %}

{% content-ref url="../.-expressions.md" %}
[.-expressions.md](../.-expressions.md)
{% endcontent-ref %}

## Examples

```
# returns patient with specific id
GET /fhir/Patient?_filter=id eq 'pt-2'

# returns patients with name that contain specific substring e.g. Smith
GET /fhir/Patient?_filter=name co 'smi'

# returns patients with address.city starting with provided string, e.g. London
GET /fhir/Patient?_filter=address-city sw 'Lon'

# returns all patients with birthdate >= (<=) provided date
GET /fhir/Patient?_filter=birthdate ge 1996-06-06
GET /fhir/Patient?_filter=birthdate le 1996-06-06
```

### Logical expressions support

You can compose logical expressions using parentheses

```
GET /fhir/Patient?_filter=(name co 'smi' or name co 'fed') or name co 'unex'
```

### Forward chains

Aidbox requires to specify chain targets explicitly:

```
GET /fhir/Patient?_filter=(organization:Organization.name eq 'myorg')
```

### Dot expressions

```
GET /fhir/Patient?_filter=.name.0.family eq 'Doe'
GET /fhir/Patient?_filter=.name isnull true
```
