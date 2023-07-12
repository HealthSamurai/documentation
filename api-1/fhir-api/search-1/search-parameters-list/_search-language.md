# \_search-language

Search parameter `_search-language` can be used to search specifying language.&#x20;

```
GET [/fhir]/<resource>?_search-language=<locale>&<string-param>=<value>
```

Any [string search parameters](https://www.hl7.org/fhir/search.html#string) (e.g. name) will search in desired language if there is `_search-language` in query. Specifying only `_search-language` without any other string search parameters won't affect anything (except `_sort`).

[List of supported locales](https://hl7.org/fhir/valueset-languages.html) required by FHIR.&#x20;

### Turn on \_search-language

{% hint style="warning" %}
`_search-language` is experimental, generated SQL and semantic may change in future!
{% endhint %}

By default \_search-language is skipped. It can be turned on by

```
features 
{...
 :multilingual
 {:enable-search-language true}}
```

### Resources with Translation Extension

Aidbox searches for [Translation Extension](https://build.fhir.org/ig/HL7/fhir-extensions/StructureDefinition-translation.html) and if the resource contains it and the language is correct, then searches by the content of this translation.&#x20;

Structure of a resource containing translation extension:

```yaml
resourceType: <resourceType>
id: <id>
name: <name>
_name:
  extension:
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: <locale1>
        - url: content
          valueString: <translation in locale1> 
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: <locale2>
        - url: content
          valueString: <translation in locale2>
```

### Fallback

If desired language is not present in Translation Extension, it will search by search-parameter like without \_search-language. This fallback can be turned off by

```
box_features_multilingual_fallback=false
```

environment variable (true by default).

Note on field overwriting: Aidbox returns resource in FHIR format without any replacements.

### Accept-Language

Use `Accept-Language` header instead of `_search-language`:

```
GET [/fhir]/<resourceType>?_sort=<string-search-param>
Accept-Language: <locale>
```

Note that browsers send requests with `Accept-Language` (e.g en-US,en;q=0.5) by default.&#x20;

To enable Accept-Language support use&#x20;

```
box_features_multilingual_use__accept__language__header=true
```

environment variable.

### Sorting

[`_sort` search parameter](\_sort.md) will use desired language if `Accept-Language` or `_search-language` are present.&#x20;

```
GET [/fhir]/<resourceType>?_search-language=<locale>&_sort=<string-search-param>
```

#### Collation

By default PostgreSQL uses system locale [to specify sort order](../../../../tutorials/data-api/change-sort-order-by-locale-collation.md). When searching by language, SQL will contain `GROUP BY <...> COLLATE "<locale>"`. Note that if locale does not contain a territory part (`language[_territory]`), Aidbox will use "language\_LANGUAGE", e.g. "en\_EN" (is not an locale) or "ru\_RU" (correct locale).&#x20;

Collation is supported in **Aidboxdb version 14.7+**.

### Examples

Get all locations, those names start with 'Clinique' in French:

<pre><code><strong>GET /Location?_search-language=fr-CA&#x26;name=Clinique
</strong></code></pre>

Same with `Accept-Language` header:

<pre><code><strong>GET /Location?name=Clinique
</strong><strong>Accept-Language: fr-CA
</strong></code></pre>

Get all locations and sort by their names in French, using French locale for sort order (COLLATE):

```
GET /Location?_search-language=fr-CA%_sort=name
```

#### Array search

If some resource contains an array of strings, e.g. Location.alias, extensions can also be an array.

```
PUT /fhir/Location/my-loc

resourceType: Location
status: active
alias:
- a
- b
_alias:
- extension:
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: fr-CA
        - url: content
          valueString: a-fr-ca
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: en-CA
        - url: content
          valueString: a-en-ca
- extension:
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: fr-CA
        - url: content
          valueString: b-fr-ca
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: en-CA
        - url: content
          valueString: b-en-ca
```

`_search-language` can search through arrays (search parameter Location.name searches in `name`, `alias`, `_name` and `_alias`)

```
GET /Location?_search-language=fr-CA&name=a-fr-ca // found my-loc
GET /Location?_search-language=fr-CA&name=b-fr-ca // found my-loc
GET /Location?_search-language=fr-CA&name=c-fr-ca // not found
GET /Location?_search-language=fr-CA&name=a-fr-ca,c-fr-ca // found my-loc
```

