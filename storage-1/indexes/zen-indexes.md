# Zen Indexes

### Index

Zen Index validation schema:

```clojure
{ns aidbox.index.v1
 import #{aidbox.search-parameter.v1
          zenbox}

 index
 {:zen/tags #{zen/schema zen/tag}
  :zen/desc "Index definition for Aidbox V1"
  :type zen/map
  :require #{:table :expression :type}
  :keys
  {:table {:type zen/string}
   :expression {:type zen/string}
   :type {:type zen/keyword
          :enum [{:value :gin}
                 {:value :btree}]}}}

 auto-index
 {:zen/tags #{zen/schema zen/tag}
  :zen/desc "Auto-generated index definition for Aidbox V1"
  :type zen/map
  :require #{:for}
  :keys
  {:for {:type zen/symbol :tags #{aidbox.search-parameter.v1/search-parameter}}}}}
```

Index must contain:

* table - name of the table
* expression - SQL expression&#x20;
* type - two possible values: `:gin` or `:btree`

Auto-index is auto-generated based on Zen SearchParameter.&#x20;

Auto-index must contain:

* for - symbol with tag `search-parameter`
