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

Most of the Search Parameters from IG work with Zen by default, also you can make a new one.

Assuming you already know how to use [configuration projects](../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md), let's learn how to create zen search parameter by example:

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
  :extra-parameters-sources :all ; allow to use SearchParameters from outside of repo
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

Firstly we import `aidbox.search-parameter.v1` and `aidbox.repository.v1` namespaces from .edn files. These are zen-namespaces we need to make an `aidbox/service` which name is `repositories`.

This service is our concept of wrapping resourceType-specific entities, as search parameters, indexes, and more, into one entity, called **repository**. We will add indexes for search parameters soon.

We have one repository for Patient resourceType: `patient-repository`. It contains `:search-parameters` key with new SearchParameter `my-parameter`. &#x20;

SearchParameter must contain:

* type: [FHIR Search Parameter types](./#search-parameters)
* resource, containing resourceType and id
* [jsonknife](configure-search-api.md#jsonpath-vs-jsonknife) expression containing path in the resource to search for
* name to use in search

After your Aidbox loads the service, you can use new search parameter:

```yaml
GET /Patient?brthd=lt2023
```

{% hint style="info" %}
You can always look into the definition of Aidbox-specific namespaces in [Profiles page](../../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md#check-if-your-profile-is-loaded)
{% endhint %}

#### How to make my index for Zen Search Parameter automatically?

You can make index by your own with [Index Suggestion API](../../../storage-1/indexes/get-suggested-indexes.md). However, Aidbox can be configured to make indexes for desired SearchParameters at start automatically.

Import `aidbox.index.v1` in the example above and add `:indexes` into `patient-repository.`

```clojure
 patient-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Patient"
  :indexes #{my-index1}
  :extra-parameters-sources :all
  :search-parameters #{my-parameter}}
```

Add new symbol `my-index1` with tag `aidbox.index.v1/auto-index` to make index on start, based on Index Suggestion API for Patient.brthd SearchParameter.

```clojure
  my-index1
 {:zen/tags #{aidbox.index.v1/auto-index}
  :for my-parameter}
```

After restart new index will be added.

```sql
select * from pg_indexes where tablename = 'patient';
```

#### How to make my index explicitly with SQL?

Use `aidbox.index.v1/index` tag with `:expression` and [PostgreSQL index`:type`](https://www.postgresql.org/docs/15/indexes-types.html) fields:

```clojure
 my-index2
 {:zen/tags #{aidbox.index.v1/index}
  :table "patient"
  :expression "(jsonb_path_query_array(\"patient\".resource, ( '($.\"name\"[*]).** ? (@.type() == \"string\")')::jsonpath)::text) gin_trgm_ops"
  :type :gin}
```

After restart new index will be added:

```sql
CREATE INDEX aidbox_mng_idx_main_my_index2
ON public.patient 
USING gin (((jsonb_path_query_array(resource, '$.\"name\"[*].**?(@.type() == \"string\")'::jsonpath))::text) gin_trgm_ops)
```

#### Why should I create Zen SearchParameter in configuration project instead of `POST /SearchParameter` or `AidboxQuery` or `SearchQuery`?
