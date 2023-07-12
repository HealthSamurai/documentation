# How to use multilingual search

FHIR specifies the [translation extension](http://hl7.org/fhir/StructureDefinition/translation) to store translations from base language of resource to another languages. In this tutorial we will cover how to search through resources specifying language.

First, let's create a resource Location, which has name in English and French:

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

## Search in specified language

Aidbox has non-FHIR search parameter [\_search-language](../../api-1/fhir-api/search-1/search-parameters-list/\_search-language.md) to specify the locale to search. Then provide some search-parameter, for example name. To enable \_search-language:

```
features 
{...
 :multilingual
 {:enable-search-language true}}
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
features 
{...
 :multilingual
 {:use-accept-language-header true}}
```

or

```
box_features_muitilingual_use__accept__language__header=true
```

Now we can use the header instead:

```
GET /Location?name=Clinique
Accept-Language: fr-CA
```

## Translate concepts

$translate-concept endpoint is used to fetch translations of concepts by their code and system.

```
POST /$translate-concepts

language: fr-CA
concepts: 
  - code: 1-8
    system: http://loinc.org
  - code: 10000-8
    system: http://loinc.org
```

will response with

```
- code: 10000-8
  system: http://loinc.org
  language: en
  translation: R wave duration in lead AVR
- code: 1-8
  system: http://loinc.org
  language: en
  translation: Acyclovir [Susceptibility]
```
