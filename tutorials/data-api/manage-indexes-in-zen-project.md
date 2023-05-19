# Manage Indexes in Zen Project

Managing PostgreSQL indexes in Aidbox zen-project has following advantages:

* sharing between many environments
* generate indexes for desired search parameters automatically, with no SQL&#x20;

In this guide we will:

* setup Aidbox locally
* create index with SQL in zen
* create index without SQL in zen&#x20;
* synchronize these indexes via Aidbox RPC API

## Setup Aidbox locally

Follow [this guide](../../getting-started/run-aidbox/run-aidbox-locally-with-docker.md). Your aidbox-project directory will contain these files.

```
├── docker-compose.yaml
├── .env
├── .gitignore
├── pgdata
├── README.md
├── zen-package.edn
├── zen-packages
└── zrc
    ├── config.edn
    └── main.edn
```

Next we will edit files in /zrc directory.&#x20;

## Create repositories

Read about [Index Management](../../storage-1/indexes/#index-management).&#x20;

We will create index for Practitioner.index search parameter with SQL, define new search parameter Patient.brthdt and create auto-index for that.

First we need to create repositories. Each repository will represent one resourceType, e.g. `patient-repository` and `practitioner-repository`.&#x20;

Edit main.edn:

{% code title="main.edn" lineNumbers="true" %}
```clojure
{ns main
 import #{aidbox.index.v1
          aidbox
          config
          aidbox.repository.v1}

 practitioner-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Practitioner"
  :indexes #{practitioner-identifier-gin-index}
  :extra-parameter-sources :all
  :search-parameters #{}}

 patient-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Patient"
  :indexes #{patient-birthdate-auto-index}
  :extra-parameter-sources :all
  :search-parameters #{patient-birthdate-parameter}}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :load-default true
  :repositories #{patient-repository practitioner-repository}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/base-config
  :services {:repositories repositories
             :admin-user-seed config/admin-user-seed
             :root-client-seed config/root-client-seed}}}
```
{% endcode %}

All repositories must be referenced into one schema `repositories` tagged with `aidbox/service`.

Each repository contains references for indexes (can be empty - `#{}`) and search-parameters (also can be empty).&#x20;

## Create indexes

Now let's define search parameter `patient-birthdate-parameter` and indexes `patient-birthdate-auto-index`, `practitioner-identifier-gin-index.`&#x20;

{% code title="main.edn" lineNumbers="true" %}
```clojure
{ns main
 import #{aidbox.index.v1
          aidbox
          config
          aidbox.repository.v1}

 practitioner-identifier-gin-index
 {:zen/tags #{aidbox.index.v1/index}
  :table "practitioner"
  :type :gin
  :expression "(\"practitioner\".resource)"}

 patient-birthdate-parameter
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "brthdt"
  :type :date
  :resource {:resourceType "Entity" :id "Patient"}
  :expression [["birthDate"]]}

 patient-birthdate-auto-index
 {:zen/tags #{aidbox.index.v1/auto-index}
  :for patient-birthdate-parameter}

 practitioner-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Practitioner"
  :indexes #{practitioner-identifier-gin-index}
  :extra-parameter-sources :all
  :search-parameters #{}}

 patient-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Patient"
  :indexes #{patient-birthdate-auto-index}
  :extra-parameter-sources :all
  :search-parameters #{patient-birthdate-parameter}}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :load-default true
  :repositories #{patient-repository practitioner-repository}}
 
 box
 {:zen/tags #{aidbox/system}
  :config   config/base-config
  :services {:repositories repositories
             :admin-user-seed config/admin-user-seed
             :root-client-seed config/root-client-seed}}}
```
{% endcode %}

Auto-index means that Aidbox will create index (or indexes!) for that particular search parameter. Each auto-index must reference search-parameter in `:for`.&#x20;

`Practitioner-identifier-gin-index` is the ordinary index because it is tagged with `aidbox.index.v1/index` and it must contain an SQL expression.

Now we can test it.

## Test indexes

Start Aidbox:

```
docker compose up --force-recreate
```

Test if new `Patient.brthdt` search parameter work:&#x20;

```
GET /fhir/Patient?brthdt=2000-01-01&_explain=1
```

Test that no indexes are created:

```sql
select * from pg_indexes where indexname ilike 'aidbox_mng%'
```

Start synchronization of indexes from zen-schemas.

```
POST /rpc
Content-Type: application/json
Accept: application/json

{"method": "aidbox.index.v1/sync-indexes"}
```

Response:&#x20;

```
{
 "result": {
  "removed": [],
  "added": [
   "aidbox_mng_idx_patient_brthdt_param_knife_date_min_low_tstz",
   "aidbox_mng_idx_patient_brthdt_param_knife_date_max_high_tstz",
   "aidbox_mng_idx_patient_brthdt_param_knife_date_max_tstz",
   "aidbox_mng_idx_patient_brthdt_param_knife_date_min_tstz",
   "aidbox_mng_idx_main_practitioner_identifier_gin_index"
  ]
 }
}
```

Now 5 managed indexes are created.

```sql
select * from pg_indexes where indexname ilike 'aidbox_mng%'
```
