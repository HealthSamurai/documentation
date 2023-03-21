---
description: Create custom resources with zen
---

# Custom resources using Aidbox Project

If you're not familiar with Aidbox Project, read [this](../../reference/configuration/aidbox-project.md) section first.

To create a custom resource in Aidbox Project you need to:

1. Create repositories service;
2. Make a repository, associated with new resource type;
3. Optionally, create a base profile scheme and associate it with your custom type repository.

#### Create repository service

{% code title="system.edn" %}
```clojure
 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :repositories #{our-repository}
  :load-default true}

 box 
 {:zen/tags #{aidbox/system}
  :services {:repositories repositories}}}
```
{% endcode %}

#### Make a repository

In this example new custom resource have name OurType and will be available by e.g. `GET /OurType`.&#x20;

The `:base-profile` key is optional. If you don't want to validate resource on creating, you may not use it. &#x20;

{% code title="system.edn" %}
```clojure
 our-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "OurType"
  :base-profile OurType
  :extra-parameter-sources :all}

```
{% endcode %}

#### Create base profile

The base profile configures validation of the resource. It should be tagged with `zen.fhir/base-schema`.&#x20;

{% code title="system.edn" %}
```clojure
 OurType
 {:zen/tags #{zen/schema zen.fhir/base-schema}
  :zen.fhir/type "OurType"
  :zen.fhir/version "0.5.11"
  :type zen/map
  :require #{:name},
  :keys {:name {:type  zen/vector
                :every {:type zen/map
                        :keys {:given {:type  zen/vector
                                       :every {:type zen/string}}
                               :family {:type zen/string}}}}}}
```
{% endcode %}

In this example, the OurType schema will be used to validate OurType resource, requiring `name` field. We also define a structure of a name: it must be vector, every element can contain `given` or `family` fields. &#x20;

### Full example configuration

{% code title="system.edn" %}
```clojure
{ns main
 import #{aidbox.index.v1
          aidbox.search-parameter.v1
          aidbox
          aidbox.repository.v1
          zen.fhir}

 OurType
 {:zen/tags #{zen/schema zen.fhir/base-schema}
  :zen.fhir/type "OurType"
  :zen.fhir/version "0.5.11"
  :type zen/map
  :require #{:name},
  :keys {:resourceType {:const {:value "OurType"}}
         :name {:type  zen/vector
                :every {:type zen/map
                        :keys {:given {:type  zen/vector
                                       :every {:type zen/string}}
                               :family {:type zen/string}}}}}}

 our-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "OurType"
  :base-profile OurType
  :extra-parameter-sources :all}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :repositories #{our-repository}
  :load-default true}

 box 
 {:zen/tags #{aidbox/system}
  :services {:repositories repositories}}}

```
{% endcode %}
