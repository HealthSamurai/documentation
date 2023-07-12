# $translate-concepts

$translate-concepts endpoint can be used to translate concepts for given language.

It will search for translations given in `Concept.designation.display` (see [Aidbox concept format](./#designation))

<pre class="language-yaml"><code class="lang-yaml"><strong>POST /$translate-concepts
</strong>
language: &#x3C;locale>
concepts:
- system: &#x3C;system>
  code: &#x3C;code>
- system: &#x3C;system>
  code: &#x3C;code>
</code></pre>

### Examples

Search for translations of concepts with LOINC codes 2-6 and 1-8 in French:&#x20;

```
POST /$translate-concepts

language: fr-CA
concepts:
- system: http://loinc.org
  code: 1-8
- system: http://loinc.org
  code: 2-6
...
```

Result:

```
- code: 2-6
  system: http://loinc.org
  language: fr-CA
  translation: Almécilline:Susceptibilité:Temps ponctuel:Isolat:Quantitatif:CLM
- code: 1-8
  system: http://loinc.org
  language: fr-CA
  translation: 'Acyclovir:Susceptibilité:Temps ponctuel:Isolat:Quantitatif ou ordinal:'
```
