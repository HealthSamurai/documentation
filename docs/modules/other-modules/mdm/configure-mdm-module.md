# Configure MDM module

The current MDM implementation only supports the Patient resource. If you need support for additional resources, please [contact us](../../../../modules/contact-us.md).

## Add model to Aidbox

To add a matching model, you need to create a custom resource named `AidboxLinkageModel`. This resource contains a DSL that describes:

* **resource**: The type of resource being matched
* **thresholds**: Contains thresholds for automatic and manual matching
* **vars**: Defines variables that can be used in expressions
* **features**: Defines the matching features with expressions and their corresponding Bayes Factors
* **blocks**: Defines blocking conditions to limit the number of comparisons

The features section contains multiple matching criteria, each with an `expr` (expression) and a `bf` (Bayes Factor). The bf values are used to calculate the overall match score based on the specified expressions.

Here's an example of creating an AidboxLinkageModel:

```yaml
POST /fhir/AidboxLinkageModel
content-type: application/json
accept: application/json

{
  "id" : "test-model",
  "resourceType" : "AidboxLinkageModel",
  "resource" : "Patient",
  "thresholds" : {
    "auto" : 25,
    "manual" : 16
  },
  "vars" : {
    "family" : "(#.resource#>>'{name,0,family}')",
    "given" : "(#.resource#>>'{name,0,given,0}')",
    "name" : "#.#family || ' ' || #.#given",
    "gender" : "(#.resource#>>'{gender}')",
    "dob" : "(#.resource#>>'{birthDate}')",
    "address" : "(#.resource#>>'{address,0,text}')",
    "addressQuery" : "array(select jsonb_array_elements_text(jsonb_path_query_array( #.resource, '$.telecom[*] ? (@.value != \"\").value')))"
  },
  "features" : {
    "fn" : [ {
      "expr" : " ( l.resource->'name' IS NULL OR r.resource->'name' IS NULL )",
      "bf" : 0.0
    }, {
      "expr" : "l.#name = r.#name",
      "bf" : 13.336495228175629
    }, {
      "expr" : "r.#given = l.#family AND l.#given = r.#family",
      "bf" : 13.104401641242227
    }, {
      "expr" : "levenshtein(l.#name, r.#name) <= 2",
      "bf" : 9.288385498954133
    }, {
      "expr" : "r.#given = l.#given AND string_to_array(l.#family, ' ') && string_to_array(r.#family, ' ')",
      "bf" : 10.36329167966839
    }, {
      "expr" : "r.#family = l.#family AND string_to_array(l.#given, ' ') && string_to_array(r.#given, ' ')",
      "bf" : 10.36329167966839
    }, {
      "expr" : "r.#given = l.#given",
      "bf" : 2.402276401131933
    }, {
      "else" : -12.37233293924643
    } ],
    "dob" : [ {
      "expr" : " ( l.#dob  IS NULL OR r.#dob IS NULL )",
      "bf" : 0.0
    }, {
      "expr" : "l.#dob = r.#dob",
      "bf" : 10.59415069916466
    }, {
      "expr" : "levenshtein(l.#dob, r.#dob) <= 1",
      "bf" : 3.9911610470417744
    }, {
      "expr" : "levenshtein(l.#dob, r.#dob) <= 2",
      "bf" : 0.5164298695732575
    }, {
      "else" : -10.322063538772698
    } ],
    "sex" : [ {
      "expr" : " ( l.#gender IS NULL OR r.#gender IS NULL )",
      "bf" : 0.0
    }, {
      "expr" : " l.#gender = r.#gender",
      "bf" : 1.8504082299552485
    }, {
      "else" : -4.842034404727677
    } ],
    "ext" : [ {
      "expr" : "l.#address = r.#address",
      "bf" : 7.465648574292063
    }, {
      "expr" : "l.#addressQuery  && r.#addressQuery",
      "bf" : 9.236771286242664
    }, {
      "else" : -10.517360697819983
    } ]
  },
  "blocks" : {
    "fn" : {
      "var" : "name"
    },
    "dob" : {
      "var" : "dob"
    },
    "addr" : {
      "sql" : "l.#address = r.#address"
    }
  }
}
```
