# Zen Indexes

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Broken link](broken-reference "mention")
{% endhint %}

## Introduction

Formal description of Zen Indexes in `aidbox.index.v1`.  Read [this](../../../../deployment-and-maintenance/indexes/#how-to-make-my-index-explicitly-with-sql) guide first.

Index can be used to create any PostgreSQL index. Auto-index is auto-generated based on Zen SearchParameter.&#x20;

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

Auto-index must contain:

* for - symbol with tag `search-parameter`
