# Concept

## Overview

Concept resource has similar structure as Coding and [CodeSystem.concept](https://www.hl7.org/fhir/codesystem-definitions.html#CodeSystem.concept) element. When you upload code system into [Aidbox](https://www.health-samurai.io/aidbox), we split CodeSystem resource into concepts. But for your convenience CRUD operations are allowed as well on Concept resource directly. To create and manage simple  dictionary/terminology you can create set of Concepts using Create operation.

```yaml
resourceType: Concept
// id of concept; usually short name for terminology + code
id: loinc-17861-6
// inactivity flag
deprecated: true
system: http://loinc.org
code: 17861-6
display: Calcium [Mass/​volume] in Serum or Plasma	 
definition: <Definition string>
hierarchy:
  - parent-code-1
  - parent-code-2
designation:
  display:
    ge: der Calcium
    es: el calcium
    ru: Кальций
  definition:
    ge: der Calcium
    es: el calcium
property:
   LOINC:
     long_common_name: ...
     relatednames2: ...
```

## Structure

### deprecated

This is boolean flag, which can be used to deactivate \(but not delete\) concept. This is a best-practice for terminology management - _never delete concepts, but deprecate_

### system

It's symbolic link on CodeSystem

### hierarchy

Hierarchy element contains codes of parent concepts for hierarchical terminologies from top to bottom. This element is used for Subsumption Operation - where you test that some code **isA** another code.

### designation

Designation element is copy semantic of [CodeSystem.concept.designation](https://www.hl7.org/fhir/codesystem-definitions.html#CodeSystem.concept.designation) - Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc. In Aidbox it is represented in more database friendly format. 

In  [CodeSystem.concept.designation](https://www.hl7.org/fhir/codesystem-definitions.html#CodeSystem.concept.designation) is a collection of complex elements:

```yaml
designation:
  - use:
      system: http://...designation-usage
      code: display
    language: ge
    value: eigenclass
  - use:
      system: http://...designation-usage
      code: display
    language: es
    value: el eigenclass
// to access it you need something like this
// designation.where(use='display' and language='ge').value
```

In Aidbox it's grouped by _use.code_ and after that by l_anguage_ attributes:

```yaml
designation:
  display:
    ge: der Calcium
    es: el calcium
    ru: Кальций
  definition:
    ge: der Calcium
    es: el calcium
// much more easy with this format
// designation.display.ge
```

### property

Property element is polymorphic element, which contains additional properties for concept like axis of classification etc. In Aidbox we use more database oriented encoding instead of FHIR generic property constructor.



