# Getting started

This article describes how to load data, train an MDM model, and use match operation.

## Create a client

First you need to have a client for Aidbox mdm Python integration

```
PUT /Client/mdm-client

secret: secret
grant_types:
  - basic
```

## Create an access policy

Create an acccess policy which allows the client to execute raw SQL queries

```
PUT /AccessPolicy/mdm-policy

engine: allow
link:
  - id: mdm-client
    resourceType: Client
```

## Prepare Python

Install [Python 3](https://www.python.org/downloads/), [pip](https://pip.pypa.io/en/stable/installation/), and [poetry](https://python-poetry.org/docs/#installation).

Follow [MDM Python integration readme](https://github.com/Aidbox/mdm) to launch Jupyter Notebook.

## Train a model

Follow [MDM Python integration tutorial](https://github.com/Aidbox/mdm/blob/master/tutorials/tutorial.ipynb) to learn how to train an MDM model

## Export a model

Use `save_zen_model_edn` method to export zen symbol for your model

## Add model to Aidbox configuration project

Insert exported symbol to your Aidbox configuration project. And enable an MDM service.

Example:

```
{ns mdmbox

 import #{aidbox.mdm
          aidbox

          zenbox
          zen.fhir}

 patient-mdm-model model-exported-from-splink

 patient-mdm
 {:zen/tags #{aidbox/service}
  :engine aidbox.mdm/splink-engine
  :models #{patient-mdm-model}}

 box
 {:zen/tags #{aidbox/system}
  :services
  {:patient-mdm patient-mdm}}}
```

### Prepare MDM tables

Call [`aidbox.mdm/update-mdm-tables` RPC method](../../reference/rpc-reference/aidbox/mdm/aidbox.mdm-update-mdm-tables.md) to prepare MDM tables.

```
POST /rpc

method: aidbox.mdm/update-mdm-tables
params:
  resource-type: Patient
```

## Use match operation

Use [$match](../../api-1/fhir-api/usdmatch.md) operation or [`aidbox.mdm/match` RPC method](../../reference/rpc-reference/aidbox/mdm/aidbox.mdm-match.md) to find similar resources:

```
POST /fhir/Patient/$match

resourceType: Parameters
parameter:
  - name: resource
    resource:
      name:
        - given:
            - John
          family: Smith-Johnson-Williams
      birthDate: "2000-01-01"
  - name: onlyCertainMatches
    valueBoolean: true
```

Example output:

```
resourceType: Bundle
type: searchset
entry:
  - score: 0.99
    resource:
      name:
        - given:
            - John
          family: Smith-Johnson-Williams
      address:
        - city: London
      birthDate: "2000-01-01"
      resourceType: Patient
      id: pt-1
```
