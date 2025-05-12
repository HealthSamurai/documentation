---
description: Create custom resources, search parameters, indexes in Configuration project
---

# Repository

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

Repository is our concept of wrapping resourceType-specific entities, as search parameters, indexes, and more, into one entity, called **repository**. With defined repository, you can create custom resource, search parameters and indexes.

## Create repository

Prerequisites: check out [Aidbox configuration project structure](aidbox-configuration-project-structure.md).

To create a repository add a service `aidbox.repository.v1` in system.edn like this.

{% code title="zrc/system.edn" %}
```
{ns main
 import #{aidbox.index.v1
          aidbox.search-parameter.v1
          aidbox
          aidbox.repository.v1
          zen.fhir}

 zen-config
 {...}

patient-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Patient" ; or your own
  :indexes #{<my-indexes>}
  :extra-parameter-sources :all
  :search-parameters #{<my-parameters>}}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :repositories #{patient-repository <my-other-repositories>}
  :load-default true}

 box {:zen/tags #{aidbox/system}
      :config   zen-config
      :services
      {:repositories repositories}}}
```
{% endcode %}

Here we defined patient-repository which can contain custom search-parameters and indexes.

## Create custom resource

Described in [Custom resources module](custom-resources-using-aidbox-project.md).

## Create search parameter

Described in [SearchParameter page](broken-reference).

## Create index for search parameter

Described in [Index page](../../../../deployment-and-maintenance/indexes/#auto-generated-indexes).

## Create index by SQL

Described in[ Index page](../../../../deployment-and-maintenance/indexes/#how-to-make-my-index-explicitly-with-sql).
