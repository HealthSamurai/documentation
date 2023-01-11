---
description: Match duplicate records
---

# MDM Module

Aidbox MDM module provides ability to match duplicate resources using mixed mixture model variation of Fellegi-Sunter method.

## Background

See [fastlink](https://imai.fas.harvard.edu/research/files/linkage.pdf) paper for more details.

Let

* c(a, b) be a comparison function which compares records a, b, and return discrete comparison result r;
* M be a set of matching record pairs
* U be a set of non-matching record pairs

Construct probabilities m(a, b, r) such that

$$
m(a, b, r) = \mathrm{P}\left(c\left(a, b\right) = k | (a, b) \in M\right).
$$



Construct probabilities u(a, b, r) such that

$$
u(a, b, r) = P(c(a, b) = k | (a, b) \in U).
$$

Then&#x20;

$$
\frac{m(a, b, r)}{u(a, b, r)}
$$

is a Bayes factor.

Multiply Bayes factors for all observed comparisons and multiply it by random match probability to get match score.

Use Bayes theorem to get match probability from data.

## Using MDM

To use MDM module you need to train an MDM model on your data and load it into Aidbox.

You can train a model using [our fork of splink](https://github.com/Aidbox/mdm) which supports Aidbox. See the [tutorial in the Aidbox MDM repository](https://github.com/Aidbox/mdm) to learn how to use it.

Here is an example of Aidbox project which demostrates how to load a model into Aidbox

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
  {:patient-mdm patient-mdm}}
 }
```

After loading model you need to create MDM tables using `aidbox.mdm/update-mdm-tables` RPC:

```
POST /rpc
Content-Type: text/yaml
Accept: text/yaml

method: aidbox.mdm/update-mdm-tables
params:
  resource-type: Patient
```

This operation creates two tables: one for denormalized resource data, and second for term frequencies.

Denormalized resource data table is updated automatically on each CRUD operation.

Term frequencies table is updated manually using the same `aidbox.mdm/update-mdm-tables` RPC.

Use [FHIR $match](http://build.fhir.org/patient-operation-match.html) operation to find matching resources

```
POST /fhir/Patient/$match
Content-Type: text/yaml
Accept: text/yaml

resourceType: Parameters
parameter:
  - name: resource
    resource:
      name:
        - given:
            - Julia
          family: Luton
      birthDate: "2004-04-27"
  - name: onlyCertainMatches
    valueBoolean: true
```

Aidbox also supports `aidbox.mdm/match` RPC which provides simpler inteface:

```
POST /rpc
Content-Type: text/yaml
Accept: text/yaml

method: aidbox.mdm/match
params:
  resource-type: Patient
  threshold: 0.5
  resource:
    name:
      - given:
          - Julia
        family: Luton
    birthDate: "2004-04-27"
```
