# Configure Search API

Aidbox Search API can be configured in many ways. This page explains the difference between general types of Search API.&#x20;

## Jsonpath vs jsonknife

Aidbox has two engines to search: [jsonpath](https://www.postgresql.org/docs/12/datatype-json.html#DATATYPE-JSONPATH) and [jsonknife](https://github.com/niquola/jsonknife). ****&#x20;

The engine is responsible for SQL generation for search operations. SQL by jsonpath and jsonknife is different for search parameter types: date, number, quantity, reference, string, token, uri.  \_lastUpdated, \_createdAt search parameters and :missing modifier searches are also differs by engine.

jsonpath-engine:

* supported by PostgreSQL without external extensions, can be used with managed PostgreSQL, e.g. Azure PostgreSQL
* better performance for string search parameters and all string-related search (e.g. :text modifier)\*
* will be supported as main engine

jsonknife:&#x20;

* is an external extension, can not be used with managed PostgreSQL&#x20;
* better performance for dates, number and quantity search parameters\*

\*[using indexes](../../../storage-1/indexes/get-suggested-indexes.md) makes performance approximately the same

### Set engine

Use `BOX_SEARCH_ENGINE` environment variable to choose engine. The default is knife.&#x20;

```yaml
BOX_SEARCH_ENGINE="jsonpath" # or "knife"
```

## Zen Search

[zen-lang ](https://github.com/zen-lang/zen)is a powerful DSL language to [configure Aidbox](../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md), [validate](../../../profiling-and-validation/profiling-with-zen-lang/) profiles, and search. It allows you to set up search parameters from IGs automatically, make your own search parameters and indexes associated with them.

#### How to configure zen-search?

To enable zen-search, use `BOX_SEARCH_ZEN__FHIR=true` and `BOX_SEARCH_RESOURCE__COMPAT=false` to use preferred version of zen-search (backward compatibility environment variable).

```yaml
BOX_SEARCH_ZEN__FHIR=true
BOX_SEARCH_RESOURCE__COMPAT=false # default is true
```

#### How to make my Zen Search Parameter in configuration project?

Follow [this](searchparameter.md#define-custom-searchparameter-with-extension) guide.

#### Indexes for SearchParameters

You can also auto-generate indexes for SearchParameter:

{% content-ref url="../../../storage-1/indexes/" %}
[indexes](../../../storage-1/indexes/)
{% endcontent-ref %}

