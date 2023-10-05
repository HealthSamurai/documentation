---
description: Create custom resources with zen
---

# Custom resources using Aidbox Project

If you're not familiar with Aidbox Project, read [this](broken-reference) section first.

Since 2303, Aidbox can be configured to create custom resources by Aidbox Projects.&#x20;

## Create custom resource

To create a custom resource in Aidbox Project you need to:

1. Create repositories service;
2. Make a repository, associated with new resource type;
3. Optionally, create a base profile scheme and associate it with your custom type repository.

### Create repository service

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

### Make a repository

In this example new custom resource have name OurType and will be available by e.g. `GET /OurType`.&#x20;

The `:base-profile` key is optional. If you don't want to validate resource on creating, you may not use it.  If not specified, custom resource won't be validated and search won't support this custom type.

{% code title="main.edn" %}
```clojure
 our-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "OurType"
  :base-profile OurType
  :extra-parameter-sources :all}

```
{% endcode %}

### Create base profile

The base profile configures validation of the resource. It should be tagged with `zen.fhir/base-schema`.&#x20;

{% code title="main.edn" %}
```clojure
 OurType
 {:zen/tags #{zen/schema zen.fhir/base-schema}
  :zen.fhir/type "OurType"
  :zen.fhir/version "0.5.11"
  :type zen/map
  :confirms #{zen.fhir/Resource}
  :require #{:name}
  :keys {:name {:type  zen/vector
                :every {:type zen/map
                        :keys {:given {:type  zen/vector
                                       :every {:type zen/string}}
                               :family {:type zen/string}}}}}}
```
{% endcode %}

In this example, the OurType schema will be used to validate OurType resource, requiring `name` field. We also define a structure of a name: it must be vector, every element can contain `given` or `family` fields. &#x20;

### Full example configuration

{% code title="main.edn" %}
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
  :require #{:name}
  :confirms #{zen.fhir/Resource}
  :keys {:resourceType {:const {:value "OurType"}
                        :type zen/string}
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

## Examples

<details>

<summary>Extended OurType with date, number, reference, quantity, uri type fields</summary>

```clojure
{ns main
 import #{aidbox.index.v1
          aidbox.search-parameter.v1
          aidbox
          aidbox.repository.v1
          zen.fhir}

 Resource
 {:zen/tags #{zen/schema zen.fhir/structure-schema}
  :type zen/map
  :zen.fhir/version "0.5.11"
  :keys {:id {:type zen/string}
         :resourceType {:type zen/string}
         :meta {:type zen/map
                :keys {:profile {:type zen/vector
                                 :every {:type zen/string}}
                       :lastUpdated {:type zen/datetime}}}}}

 OurType
 {:zen/tags #{zen/schema zen.fhir/base-schema}
  :confirms #{Resource}
  :zen.fhir/type "OurType"
  :zen.fhir/version "0.5.11"
  :type zen/map
  :require #{:name},
  :keys {:resourceType {:const {:value "OurType"}}
         :mydate {:type zen/string
                  :zen.fhir/type "date"
                  :regex "^([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?$"}
         :mynumber {:type zen/number
                    :zen.fhir/type "integer"}
         :active {:type zen/boolean
                  :zen.fhir/type "boolean"}
         :myreference {:confirms #{zen.fhir/Reference}
                       :zen.fhir/type "Reference"
                       ; MyType, MyType2 are also base-profile schemas
                       ; for custom repositories 
                       :zen.fhir/reference {:refers #{MyType MyType2}}}
         :myquantity {:zen.fhir/type "Quantity"
                      :type zen/map
                      :keys {:value {:type zen/number}
                             :code {:type zen/string}
                             :system {:type zen/string}}}
         :myuri {:zen.fhir/type "uri"
                 :type zen/string}
         :name {:type zen/vector
                :every {:type zen/map
                        :keys {:given {:type zen/vector
                                       :every {:type zen/string}}
                               :family {:type zen/string}}}}}}

 our-param-reference
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "reference"
  :type :reference
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["myreference"]]}

 our-param-string
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "string"
  :type :string
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["name" "given"] ["name" "family"]]}

 our-param-date
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "date"
  :type :date
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["mydate"]]}

 our-param-number
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "number"
  :type :number
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["mynumber"]]}

 our-param-token
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "token"
  :type :token
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["active"]]}

 our-param-quantity
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "quantity"
  :type :quantity
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["myquantity"]]}

 our-param-uri
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "uri"
  :type :uri
  :resource {:resourceType "Entity" :id "OurType"}
  :expression [["myuri"]]}

 our-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "OurType"
  :base-profile OurType
  :extra-parameter-sources :all
  :search-parameters #{our-param-string 
                       our-param-date 
                       our-param-reference
                       our-param-number 
                       our-param-token
                       our-param-uri
                       our-param-quantity}}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :repositories #{our-repository mytype-repository mytype2-repository}
  :load-default true}

 box {:zen/tags #{aidbox/system}
      :services
      {:repositories repositories}}}
```

</details>

