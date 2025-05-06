# Multilingual search tutorial

{% hint style="warning" %}
Multilingual search is **experimental**! API may change!
{% endhint %}

FHIR specifies the [translation extension](http://hl7.org/fhir/StructureDefinition/translation) to store translations from the base language of the resource to other languages. In this tutorial, we will cover how to search through resources specifying the language.

First, let's create a resource Location, which has a name in English and French:

```
POST /fhir/Location

resourceType: Location
id: loc-1
status: active
name: Location name
_name:
  extension:
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: fr-CA
        - url: content
          valueString: Clinique médicale du cœur de la ville
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: en-CA
        - url: content
          valueString: Downtown Heart Medical Clinic
```

Here, translations of the name are specified in the \_name, which is [FHIR primitive extension](https://hl7.org/fhir/json.html#primitive).

## Search in the specified language

Aidbox has non-FHIR  \_search-language search parameter to specify the locale to search. Then provide some search-parameter, for example name. To enable \_search-language:

```
BOX_FHIR_SEARCH_MULTILINGUAL_ENABLE=true
```

To search locations by name in French:

```
GET /Location?_search-language=fr-CA&name=Clinique
```

To search in English:

```
GET /Location?_search-language=en-CA&name=Downtown
```

### Using Accept-Language header

Accept-Language HTTP header can be used instead of \_search-language. To enable such behavior, use this environment variable:

```
BOX_FHIR_SEARCH_MULTILINGUAL_USE_ACCEPT_LANGUAGE_HEADER=true
```

Now we can use the header instead:

```
GET /Location?name=Clinique
Accept-Language: fr-CA
```

## Fallback

If desired language is not present in Translation Extension, it will search by search-parameter like without \_search-language. This fallback can be turned off by

```
BOX_FHIR_SEARCH_MULTILINGUAL_FALLBACK
```

environment variable (true by default).

Note on field overwriting: Aidbox returns resource in FHIR format without any replacements.

## Sorting <a href="#sorting" id="sorting"></a>

[`_sort` search parameter](https://docs.aidbox.app/api-1/api/search-1/search-parameters-list/_sort) will use the desired language if `Accept-Language` or `_search-language` are present.

```
GET [/fhir]/<resourceType>?_search-language=<locale>&_sort=<string-search-param>
```

### **Collation**

By default PostgreSQL uses system locale [to specify sort order](https://docs.aidbox.app/readme-1/data-api/change-sort-order-by-locale-collation). When searching by language, SQL will contain `GROUP BY <...> COLLATE "<locale>"`. Note that if you want to use the non-default locale you must pass it to Aidboxdb with the `EXTRA_LOCALES` [environment variable](https://docs.aidbox.app/reference/environment-variables/aidboxdb-environment-variables#optional-environment-variables). Also, note that if locale does not contain a territory part (`language[_territory]`), Aidbox will use "language\_LANGUAGE", e.g. "en\_EN" (is not an locale) or "ru\_RU" (correct locale).

Collation is supported in **Aidboxdb version 14.7+**.

## Examples

Get all locations, those names start with 'Clinique' in French:

```
GET /fhir/Location?_search-language=fr-CA&name=Clinique
```

Same with `Accept-Language` header:

```
GET /fhir/Location?name=Clinique
Accept-Language: fr-CA
```

Get all locations and sort by their names in French, using French locale for sort order (COLLATE):

```
GET /fhir/Location?_search-language=fr-CA%_sort=name
```

### **Array search**

If some resource contains an array of strings, e.g. `Location.alias`, extensions can also be an array.

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
GET /fhir/Location?_search-language=fr-CA&name=a-fr-ca // found my-loc
GET /fhir/Location?_search-language=fr-CA&name=b-fr-ca // found my-loc
GET /fhir/Location?_search-language=fr-CA&name=c-fr-ca // not found
GET /fhir/Location?_search-language=fr-CA&name=a-fr-ca,c-fr-ca // found my-loc
```
