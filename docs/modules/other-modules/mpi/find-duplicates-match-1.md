---
description: >-
  This page describes how to use the $match operation to search for potential
  duplicate patient records, including request format, query parameters, and
  response structure.
---

# Find duplicates: $match

{% hint style="warning" %}
To use `$match` operation you need to set up an MPI. Read the [MPI manual](./) to learn how to run and use it.
{% endhint %}

{% hint style="success" %}
Currently, the `$match` operation is available **only for Patient resources**.\
If you are interested in extending this functionality to **other resource types**, please [contact us](../../../overview/contact-us.md).
{% endhint %}

The `$match` operation is used to **find potential duplicate patient records**.&#x20;

It performs a probabilistic search based on a **matching model** that compares the patient record you provide with other patient records in the system across multiple features and estimates how similar they are. The structure of the matching model and its parameters are described on the [Matching Model Explanation](matching-model-explanation.md) page.

The **result is a list of potential duplicates**, each with a calculated match score and a detailed breakdown of feature similarity.&#x20;

This page provides key information about using `$match`. For full API details, refer to our [Swagger documentation](https://mpi.aidbox.io/static/swagger.html).

## $match

The match operation can be initiated either through the **MPI user interface** or by using the **API**.&#x20;

The `$match` operation supports several **query parameters** that let you control how matching is performed and how results are returned:

<table><thead><tr><th width="108.1796875">Name</th><th width="82.6328125">Type</th><th width="101.2578125">Default</th><th width="357.34765625">Description</th><th>Example</th></tr></thead><tbody><tr><td><code>model</code></td><td>string</td><td><code>model</code></td><td>Matching model ID to be used for matching</td><td><code>model</code></td></tr><tr><td><code>threshold</code></td><td>integer</td><td><code>0</code></td><td>Minimum score threshold for a candidate to appear in the match results</td><td><code>0</code></td></tr><tr><td><code>page</code></td><td>integer</td><td><code>1</code></td><td>Page number of results</td><td><code>1</code></td></tr><tr><td><code>size</code></td><td>integer</td><td><code>10</code></td><td>Number of results per page</td><td><code>10</code></td></tr></tbody></table>

To call the `$match` operation, you have to send a FHIR `Parameters` resource that includes the **patient record** for which you want to search potential duplicates. Typically, this record contains demographic data such as:

* Name (given and family)
* Address (e.g., city, state)
* Birth date
* Other identifying attributes if available (e.g., telecom, identifiers)

For example, the request can looks like this:

<pre class="language-http"><code class="lang-http"><strong>POST /fhir/Patient/$match?model=model&#x26;threshold=10&#x26;page=1&#x26;size=10
</strong>Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "resource",
      "resource": {
        "name": [
          {
            "given": [
              "Freya"
            ],
            "family": "Shah"
          }
        ],
        "address": [
          {
            "city": "London"
          }
        ],
        "birthDate": "1970-12-17"
      }
    }
  ]
}
</code></pre>

As a result, you will receive the following:

* A **list of candidate duplicate patient records**
* For each candidate record:
  * `match_weight` — an overall similarity score calculated by the matching model
  * `match_details` — per-feature similarity contributions (e.g., name similarity, date of birth match, address closeness, etc.)
  * `resource` — the full FHIR Patient resource for that candidate

The response is sorted by `match_weight` in descending order so that the most similar records appear first.

For example:

```json
[
  {
    "match_details": {
      "fn": 13.336495228175629,
      "dob": 10.59415069916466,
      "ext": -10.517360697819983,
      "sex": 0
    },
    "match_weight": 13.413285229520307,
    "resource": {
      "id": "236",
      "resourceType": "Patient",
      "name": [
        {
          "given": [
            "Freya"
          ],
          "family": "Shah"
        }
      ],
      "address": [
        {
          "city": "Londodn"
        }
      ],
      "birthDate": "1970-12-17",
      "identifier": [
        {
          "value": "62",
          "system": "cluster"
        }
      ]
    }
  },
  {
    "match_details": {
      "fn": 13.336495228175629,
      "dob": 10.59415069916466,
      "ext": -10.517360697819983,
      "sex": 0
    },
    "match_weight": 13.413285229520307,
    "resource": {
      "id": "242",
      "resourceType": "Patient",
      "name": [
        {
          "given": [
            "Freya"
          ],
          "family": "Shah"
        }
      ],
      "address": [
        {
          "city": "Lonnod"
        }
      ],
      "birthDate": "1970-12-17",
      "identifier": [
        {
          "value": "62",
          "system": "cluster"
        }
      ]
    }
  },
  {
    "match_details": {
      "fn": 13.104401641242227,
      "dob": 10.59415069916466,
      "ext": -10.517360697819983,
      "sex": 0
    },
    "match_weight": 13.181191642586905,
    "resource": {
      "id": "238",
      "resourceType": "Patient",
      "name": [
        {
          "given": [
            "Shah"
          ],
          "family": "Freya"
        }
      ],
      "address": [
        {
          "city": "London"
        }
      ],
      "telecom": [
        {
          "value": "f.s@flynn.com",
          "system": "email"
        }
      ],
      "birthDate": "1970-12-17",
      "identifier": [
        {
          "value": "62",
          "system": "cluster"
        }
      ]
    }
  }
]
```
