# HL7 v2 integration with Aidbox Project

The HL7 v2 pipeline is the next step in [HL7v2 Aidbox integration](hl7-v2-integration.md), which uses new versions of the HL7 v2 parser and mapper driven by zen/lang configurations. The HL7 v2 pipeline is fully configurable API and is robust against mismatches with HL7 v2 specification.

## Quick start

{% content-ref url="../../getting-started/run-aidbox-locally-with-docker/run-aidbox-locally-using-aidbox-configuraiton-project.md" %}
[run-aidbox-locally-using-aidbox-configuraiton-project.md](../../getting-started/run-aidbox-locally-with-docker/run-aidbox-locally-using-aidbox-configuraiton-project.md)
{% endcontent-ref %}

## About HL7 v2 parser

The HL7 v2 pipeline uses a new version of the HL7 v2 parser, transforming the message into an intermediate format between FHIR and HL7 v2. This is necessary to preserve the original HL7 v2 fields, while allowing you to write the mapping from a structure close to FHIR.

HL7 v2 into intermediate format example:

{% tabs %}
{% tab title="HL7 v2" %}
```
MSH|^~\&|App|Facility|||20190423113910||ADT^A08|8899-39|P|2.3|||
PID|1||151||Name^Family||19990723|M||||||||||151||||||||||||N
```
{% endtab %}

{% tab title="Intermediate" %}
```json
{
  "parsed": {
    "message": {
      "sender": {
        "app": "App",
        "facility": "Facility"
      },
      "datetime": "2019-04-23T11:39:10",
      "type": {
        "code": "ADT",
        "event": "A08"
      },
      "id": "8899-39",
      "proc_id": {
        "id": "P"
      },
      "version": {
        "id": "2.3"
      }
    },
    "patient_group": {
      "patient": {
        "identifier": [
          {
            "value": "151"
          }
        ],
        "name": [
          {
            "use": "official",
            "family": "Name",
            "given": [
              "Family"
            ]
          }
        ],
        "birthDate": "1999-07-23",
        "gender": "M",
        "account": {
          "identifier": {
            "value": "151"
          }
        },
        "death": {
          "indicator": "N"
        }
      }
    }
  }
}
```
{% endtab %}
{% endtabs %}

## Parser configuration

Now only the default configuration is available, but in future versions of the HL7 v2 pipeline it is expected to be possible to configure the transformation of Z segments and unusual message structures.

## Mapping configuration

To convert from an intermediate format to FHIR Bundle or Aidbox Bundle, you need to define mapping, which should be tagged as `lisp/mapping` and contain the `:mapping` key. The `:mapping` value confirms as `lisp/expr` so you may use [Lisp expressions](../../reference/aidbox-forms/lisp.md) to define which intermediate format properties should be included in the Bundle resource.

{% hint style="info" %}
Null values and empty arrays will be truncated automatically in the resulted structure.
{% endhint %}

```clojure
{ns my-mappings
 import #{hl7v2.api
          lisp}

 patient-fhir-mapping
 {:zen/tags #{lisp/mapping}
  :mapping  {:resourceType "Bundle"
             :type "transaction"
             :id (get-in [:parsed :message :proc_id :id])
             :entry [{:request {:url "/fhir/Patient"
                                :method "POST"}

                      :resource {:resourceType "Patient"

                                 :extension
                                 [(when (get-in [:parsed :patient_group :patient :race])
                                    {:url "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race"
                                     :extension (for [i (get-in [:parsed :patient_group :patient :race])]
                                                  {:url "text"
                                                   :valueCoding (select-keys i [:display :system :code])})})]

                                 :identifier   (for [i (get-in [:parsed :patient_group :patient :identifier])]
                                                 (select-keys i [:value :system :type]))

                                 :name         (for [i (get-in [:parsed :patient_group :patient :name])]
                                                 (select-keys i [:family :given :middle :suffix :prefix]))

                                 :birthDate    (get-in [:parsed :patient_group :patient :birthDate])

                                 :gender       (get {"M" "male"
                                                     "F" "female"
                                                     "A" "other"
                                                     "O" "other"
                                                     "U" "unknown"}
                                                    (get-in [:parsed :patient_group :patient :gender]))

                                 :address      (for [i (get-in [:parsed :patient_group :patient :address])]
                                                 (select-keys i [:line :city :state :postalCode :country]))

                                 :telecom      (for [i (get-in [:parsed :patient_group :patient :telecom])]
                                                 (select-keys i [:value :use :system]))

                                 :martialStatus (when (get-in [:parsed :patient_group :patient :martialStatus])
                                                  {:coding (select-keys (get-in [:parsed :patient_group :patient :martialStatus])
                                                                        [:code :display :system])})}}
                     ]}
  }
 }
```

## Api configuration

The HL7 v2 pipeline is primarily a user-configurable API that allows you to describe REST endpoints for different message types and operations for them. HL7 v2 pipeline operations must use `hl7v2.api/in-op` engine, as well as contain previously defined mapping and parser-config.

For example, you need to [define an API](broken-reference) with an operation to process an ADT message and then write it to the database.

```clojure
{ns my-hl7-api
 import #{aidbox.rest
          hl7v2.config
          hl7v2.api
          my-mappings}

 adt-in
 {:zen/tags #{aidbox.rest/op}
  :engine           hl7v2.api/in-op
  :mapping          my-mappings/patient-fhir-mapping
  :parser-config    hl7v2.config/default-config
  }

 api
 {:zen/tags #{aidbox.rest/api}
  "hl7in" {"ADT" {:POST adt-in}}
  }
}
```

## Api endpoints

Each declared endpoint accepts a HL7 v2 message under the `"message"` key. It can be invoked directly through a REST request or through the [HL7 v2 UI](hl7-v2-integration.md#submitting-a-message-with-the-rest-api) on the corresponding tab in the Aidbox UI.

Also, each endpoint accepts a boolean query argument `debug`, which allows you to return a response instead of writing data to the database.

{% tabs %}
{% tab title="Request" %}
```http
POST /hl7in/ADT?debug=true
content-type: application/json
accept: application/json

{"message": 
 "MSH|^~\\&|App|Facility|||20190423113910||ADT^A08|8899-39|P|2.3|||
 PID|1||151||Bond^Tiny||19990723|M|||8388 Secret Agent Way^^Raleigh^NC^27677|||||||151||||||||||||N"}
```
{% endtab %}

{% tab title="Response" %}
```json
{
 "type": "ADT",
 "src": "MSH|^~\\&|App|Facility|||20190423113910||ADT^A08|8899-39|P|2.3|||\n PID|1||151||Bond^Tiny||19990723|M|||8388 Secret Agent Way^^Raleigh^NC^27677|||||||151||||||||||||N",
 "outcome": {
  "result": {
   "resourceType": "Bundle",
   "type": "transaction",
   "id": "P",
   "entry": [
    {
     "request": {
      "url": "/fhir/Patient",
      "method": "POST"
     },
     "resource": {
      "address": [
       {
        "line": [
         "8388 Secret Agent Way"
        ],
        "city": "Raleigh",
        "state": "NC",
        "postalCode": "27677"
       }
      ],
      "martialStatus": null,
      "name": [
       {
        "family": "Bond",
        "given": [
         "Tiny"
        ]
       }
      ],
      "birthDate": "1999-07-23",
      "resourceType": "Patient",
      "extension": [
       null
      ],
      "identifier": [
       {
        "value": "151"
       }
      ],
      "telecom": [],
      "gender": "male"
     }
    }
   ]
  }
 },
 "resourceType": "Hl7v2Message",
 "event": "A08",
 "status": "processed",
 "id": null,
 "parsed": {
  "parsed": {
   "message": {
    "sender": {
     "app": "App",
     "facility": "Facility"
    },
    "datetime": "2019-04-23T11:39:10",
    "type": {
     "code": "ADT",
     "event": "A08"
    },
    "id": "8899-39",
    "proc_id": {
     "id": "P"
    },
    "version": {
     "id": "2.3"
    }
   },
   "patient_group": {
    "patient": {
     "identifier": [
      {
       "value": "151"
      }
     ],
     "name": [
      {
       "use": "official",
       "family": "Bond",
       "given": [
        "Tiny"
       ]
      }
     ],
     "birthDate": "1999-07-23",
     "gender": "M",
     "address": [
      {
       "line": [
        "8388 Secret Agent Way"
       ],
       "city": "Raleigh",
       "state": "NC",
       "postalCode": "27677"
      }
     ],
     "account": {
      "identifier": {
       "value": "151"
      }
     },
     "death": {
      "indicator": "N"
     }
    }
   }
  },
  "errors": []
 },
 "apiOperation": "my-hl7-api/adt-in"
}
```
{% endtab %}
{% endtabs %}
