# Train model

{% hint style="info" %}
Zen model for MDM module is scheduled for deprecation in several upcoming releases. Please consider using new \[guide].
{% endhint %}

Train MDM model on your data.

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

## Train model

Follow [MDM Python integration tutorial](https://github.com/Aidbox/mdm/blob/master/tutorials/tutorial.ipynb) to learn how to train an MDM model

## Export model

The last step in the tutorial is the following cell

```
linker.save_zen_model_edn('model.edn')
```

This will create a file with model symbol. You will need it to set up MDM module in Aidbox
