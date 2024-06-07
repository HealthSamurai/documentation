# Zen Search Parameters

Most of the Search Parameters from IG work with Zen by default, also you can make a new one.

Assuming you already know how to use [configuration projects](../../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md), let's learn how to create zen search parameter by example:

```clojure
{ns main
 import #{aidbox.search-parameter.v1
          aidbox
          aidbox.repository.v1}

 zen-config
 {...}

 my-parameter
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "brthdt"
  :type :date
  :resource {:resourceType "Entity" :id "Patient"}
  :expression [["birthDate"]]}

 patient-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Patient"
  :extra-parameter-sources :all ; allow to use SearchParameters from outside of repo
  :search-parameters #{my-parameter}}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :repositories #{patient-repository}
  :load-default true}

 box {:zen/tags #{aidbox/system}
      :config   zen-config
      :services
      {:repositories repositories}}}
```

First we import `aidbox.search-parameter.v1` and `aidbox.repository.v1` namespaces from edn files. These are zen-namespaces we need to make an `aidbox/service` which name is `repositories`.

This service is our concept of wrapping resourceType-specific entities, as search parameters, indexes, and more, into one entity, called **repository**. We will add indexes for search parameters soon.

We have one repository for Patient resourceType: `patient-repository`. It contains `:search-parameters` key with new SearchParameter `my-parameter`.

SearchParameter must contain:

* type: [FHIR Search Parameter types](../#search-parameters)
* resource, containing resourceType and id
* [jsonknife](zen-search-parameters.md#jsonpath-vs-jsonknife) expression containing path in the resource to search for
* name to use in search

After your Aidbox loads the service, you can use new search parameter:

```yaml
GET /Patient?brthd=lt2023
```

{% hint style="info" %}
You can always look into the definition of Aidbox-specific namespaces in [Profiles page](../../../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md#check-if-your-profile-is-loaded)
{% endhint %}

Formal Zen SearchParameters description:

### SearchParameter

Zen SearchParameter validation schema:

```clojure
{ns aidbox.search-parameter.v1

 search-parameter
 {:type zen/map
  :zen/tags #{zen/schema zen/tag}
  :zen/desc "Search parameter zen resource. _id, _ilike and friends are still handled by Aidbox."
  :require #{:name :type :resource :expression}
  :keys
  {:name {:type zen/string}
   :type {:type zen/keyword
          :enum [{:value :string}
                 {:value :number}
                 {:value :date}
                 {:value :token}
                 {:value :quantity}
                 {:value :reference}
                 {:value :uri}]}
   :resource {:type zen/map
              :keys
              {:resourceType {:type zen/string}
               :id {:type zen/string}}}
   :expression {:type zen/vector
                :every {:type zen/any}}}}}
```

SearchParameter must contain following keys:

* name - Name of search parameter
* type - FHIR type of search parameter, this value should be one of: `string`, `number`, `date`, `token`, `quantity`, `reference` or `uri`.
* resource is a map which consists of two keys:
  * resourceType - "Entity"
  * id - FHIR Resource type
* expression - [jsonknife](zen-search-parameters.md#jsonpath-vs-jsonknife) expression containing path in the resource to search for

### Repository

Zen Repository validation schema:

```clojure
{ns aidbox.repository.v1
 import #{aidbox.search-parameter.v1
          aidbox.index.v1
          aidbox}

 engine
 {:zen/tags #{aidbox/service-engine zen/schema zen/tag}
  :zen/desc "Engine for repositories service"
  :type zen/map
  :require #{:load-default}
  :keys {:repositories {:type zen/set
                        :every {:type zen/symbol
                                :tags #{repository}}}
         :load-default {:type zen/boolean
                        :enum [{:value true}]}}}

 repository
 {:zen/tags #{zen/tag zen/schema}
  :zen/desc "Repository v1"
  :type zen/map
  :require #{:extra-parameter-sources}
  :keys {:resourceType {:type zen/string}
         :indexes {:type zen/set
                   :every {:type zen/case
                           :case [{:when {:type zen/symbol
                                          :tags #{aidbox.index.v1/index}}}
                                  {:when {:type zen/symbol
                                          :tags #{aidbox.index.v1/auto-index}}}]}}
         :extra-parameter-sources {:type zen/keyword 
                                    :enum [{:value :all}]}
         :search-parameters
         {:type zen/set
          :every {:type zen/symbol
                  :tags #{aidbox.search-parameter.v1/search-parameter}}}}}}

```

Repository keys:

* resourceType - FHIR resource type
* extra-parameter-sources _(required)_ - `:all`
* search-parameters - set of SearchParameters

### Composite search parameter

Read [Composite Search Parameters](../composite-search-parameters.md) first.

Composite search parameter must contain additional key: components. It must be a nested array in following structure:

```
[ <paths-to-search-for-value1> <paths-to-search-for-value2> ...]
```

In this example we create SearchParameter with name `composite-string-date` which will look for two parts: one is string, the other is date.

```
 {ns ...
 import #{...
          aidbox.search-parameter.draft
          ...}

 our-param-composite-string-date
 {:zen/tags #{aidbox.search-parameter.draft/composite-search-parameter}
  :name "composite-string-date"
  :type :composite
  :resource {:resourceType "Entity" :id "SomeType"}
  :expression [[]]
  :components [[["name" "given"] ["name" "family"]] [["mydate"]]]
  :search-types [:string :date]}
```

This request

```
GET /SomeType?composite-string-date=somename$2023-08-01
```

will search `somename` in `name.given` and `name.family`, `2023-08-01` in `mydate` in SomeType resources.
