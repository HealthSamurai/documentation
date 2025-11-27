---
title: "How to use multilingual search"
slug: "how-to-use-multilingual-search"
published: "2025-04-11"
author: "Evgeny Mukha"
reading-time: "5 min"
tags: []
category: "FHIR"
teaser: "FHIR specifies the translation extension to store translations from the base language of a resource to other languages. In this post, we will cover how to search through resources by specifying the language."
image: "cover.png"
---

## Multilingual search

FHIR specifies the translation extension to store translations from the base language of a resource to other languages. In this post, we will cover how to search through resources by specifying the language.

**1. Create a Resource with Translations:**

Define a [FHIR resource](http://www.health-samurai.io/articles/extending-fhir-resources) (e.g., `Location`) with translations for specific fields using the FHIR [translation](https://hl7.org/fhir/extensions/StructureDefinition-translation.html) extension. Specify translations for each language by including the `lang` and `content` sub-extensions.

```javascript
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

**2. Enable Multilingual Search:**

Activate the `\_search-language` feature in Aidbox by providing the following environment variable `BOX\_FEATURES\_MULTILINGUAL\_ENABLE\_\_SEARCH\_\_LANGUAGE` and setting it to `true`. This allows you to use the **non-FHIR** search parameter `[\_search-language](https://docs.aidbox.app/api-1/fhir-api/search-1/search-parameters-list/_search-language)` to specify the locale for searching.

**3. Perform Searches in Specific Languages:**

Use the `\_search-language` parameter in your API request to specify the desired language for searching (e.g., French or English). For example, to search for locations by name in French:

```javascript
GET /Location?_search-language=fr-CA&name=Clinique
```

To search locations by name in English:

```javascript
GET /Location?_search-language=en-CA&name=Downtown
```

**4. Alternatively, you can use the `Accept-Language` Header:**

Instead of the `\_search-language` search parameter, you can enable the use of the `Accept-Language` HTTP header by providing the environment variable `BOX\_FEATURES\_MULTILINGUAL\_USE\_\_ACCEPT\_\_LANGUAGE\_\_HEADER` and setting it to `true`. This allows you to specify the language directly in the request header. So, instead of using the following search query:

```javascript
GET /Location?_search-language=fr-CA&name=Clinique
```

You can perform the following request:

```javascript
GET /Location?name=Clinique
Accept-Language: fr-CA
```

**5. Translate Concepts:**

Use the `$translate-concept` endpoint to retrieve translations of specific terminology concepts based on their code and system. Specify the target language and the list of concepts to fetch their translations. In the following example, we will fetch translations to French Canadian for LOINC terminology concepts.

```javascript
POST /$translate-concepts

language: fr-CA
concepts:
- system: http://loinc.org
  code: 1-8
- system: http://loinc.org
  code: 2-6
```

In response, you will receive concepts with displays translated to Canadian French.

```javascript
- code: 2-6
  system: http://loinc.org
  language: fr-CA
  translation: Almécilline:Susceptibilité:Temps ponctuel:Isolat:Quantitatif:CLM
- code: 1-8
  system: http://loinc.org
  language: fr-CA
  translation: 'Acyclovir:Susceptibilité:Temps ponctuel:Isolat:Quantitatif ou ordinal:'
```

[More details](https://docs.aidbox.app/readme-1/data-api/how-to-use-multilingual-search)
