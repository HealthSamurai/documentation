# Zen Search Parameters

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

SearchParameter must contain following keys:  &#x20;

* name - Name of search parameter
* type - FHIR type of search parameter, this value should be one of: `string`, `number`, `date`, `token`, `quantity`, `reference` or `uri`.
* resource is a map which consists of two keys:
  * resourceType - "Entity"
  * id - FHIR Resource type&#x20;
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

Repository keys:  &#x20;

* resourceType - FHIR resource type
* extra-parameter-sources _(required)_ - `:all`&#x20;
* search-parameters - set of SearchParameters
