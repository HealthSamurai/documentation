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

Follow [this guide](../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md). Your aidbox-project directory will contain these files.

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

Start synchronization _**task**_ of indexes from zen-schemas. About tasks and workflow you can read [here](../../modules-1/workflow-engine/).

Request:

```
POST /rpc
Content-Type: text/yaml
Accept: text/yaml

method: aidbox.index.v1/sync-indexesm
```

Response:

```yaml
result:
  params:
    indexes-to-drop: []
    indexes-to-create:
      - indexexpr: >-
          (knife_extract_min_timestamptz("patient".resource,
          '[["birthDate"]]')) 
        indexname: aidbox_mng_idx_patient_brthdt_param_knife_date_min_tstz
        indextype: btree
        tablename: '"patient"'
      - indexexpr: >-
          (knife_extract_max_timestamptz("patient".resource,
          '[["birthDate"]]')) 
        indexname: aidbox_mng_idx_patient_brthdt_param_knife_date_max_tstz
        indextype: btree
        tablename: '"patient"'
      - indexexpr: ("practitioner".resource)
        indexname: aidbox_mng_idx_main_practitioner_identifier_gin_index
        indextype: gin
        tablename: '"practitioner"'
  status: in-progress
  definition: aidbox.index/sync-indexes-workflow
  id: >-
    6c702283-a723-4ef4-a6c1-90f16ebef8aa
  resourceType: AidboxWorkflow
  meta:
    lastUpdated: '2023-06-06T13:19:18.420982Z'
    createdAt: '2023-06-06T13:19:18.420982Z'
    versionId: '4916'
```

Index synchronization may take some time. You can check the status of workflow with UI Aidbox console or with `awf.workflow/status` [rpc method](../../modules-1/workflow-engine/workflow/task-user-api.md#awf.workflow-status):&#x20;

Request:

```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.workflow/status
params:
  id: 8dede5e7-08e4-4f2d-9296-3d4e554629f0
```

Response:

```yaml
result:
  resource:
    params:
      indexes-to-drop: []
      indexes-to-create:
        - indexexpr: >-
            (knife_extract_min_timestamptz("patient".resource,
            '[["birthDate"]]')) 
          indexname: aidbox_mng_idx_patient_brthdt_param_knife_date_min_tstz
          indextype: btree
          tablename: '"patient"'
        - indexexpr: >-
            (knife_extract_max_timestamptz("patient".resource,
            '[["birthDate"]]')) 
          indexname: aidbox_mng_idx_patient_brthdt_param_knife_date_max_tstz
          indextype: btree
          tablename: '"patient"'
        - indexexpr: ("practitioner".resource)
          indexname: aidbox_mng_idx_main_practitioner_identifier_gin_index
          indextype: gin
          tablename: '"practitioner"'
    result:
      message: All indexes are synced
      created-indexes:
        - aidbox_mng_idx_patient_brthdt_param_knife_date_min_tstz
        - aidbox_mng_idx_patient_brthdt_param_knife_date_max_tstz
        - aidbox_mng_idx_main_practitioner_identifier_gin_index
      dropped-indexes: []
      created-indexes-count: 3
      dropped-indexes-count: 0
    status: done
    outcome: succeeded
    definition: aidbox.index/sync-indexes-workflow
    id: >-
      6c702283-a723-4ef4-a6c1-90f16ebef8aa
    resourceType: AidboxWorkflow
    meta:
      lastUpdated: '2023-06-06T13:19:18.753250Z'
      createdAt: '2023-06-06T13:19:18.420982Z'
      versionId: '4975'
```

After workflow is complete, you can see 3 managed indexes are created.

```sql
select * from pg_indexes where indexname ilike 'aidbox_mng%'
```
