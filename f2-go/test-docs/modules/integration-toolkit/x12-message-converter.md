# X12 Message Converter

{% hint style="warning" %}
X12 message converter is in beta. API may change!
{% endhint %}

Aidbox includes `/x12/parse` and `/x12/generate` endpoints to allow the conversion of X12 messages. Supported types of X12 messages:&#x20;

* **270**
* **271**
* **834**
* **835**
* **837 Professional**&#x20;
* **837 Dental (**`005010X224`, `005010X224A2`, `005010X224A3`)
* **837 Institutional (**`005010X223A2`, `005010X223`)



### Parsing a message

To parse a message, use `/x12/parse` endpoint.

{% tabs %}
{% tab title="835 & 837 Request" %}
```
POST /x12/parse
content-type: text/plain
accept: application/json

ISA*00*          *00*          *ZZ*ABCPAYER       *ZZ*ABCPAYER       *190827*0212*^*00501*191511902*0*P*:~
GS*HP*ABCD*ABCD*20190827*123456*12345678*X*005010X221A1~
ST*835*112233~
BPR*I*1100*C*ACH*CCP*01*888999777*DA*24681012*1935665544**01*111333555*DA*144444*20190316~
TRN*1*71700666555*1935665544~
DTM*405*20190314~
N1*PR*RUSHMORE LIFE~
N3*10 SOUTH AVENUET~
N4*RAPID CITY*SD*55111~
PER*BL*JOHN WAYNE*TE*8005551212*EX*123~
N1*PE*ACME MEDICAL CENTER*XX*5544667733~
REF*TJ*777667755~
LX*1~
CLP*5554555444*1*800*500*300*12*94060555410000*11*1~
NM1*QC*1*BUDD*WILLIAM****MI*33344555510~
AMT*AU*800~
SVC*HC:99211*800*500~
DTM*472*20190301~
CAS*PR*1*300~
AMT*B6*800~
CLP*8765432112*1*1200*600*600*12*9407779923000*11*1~
NM1*QC*1*SETTLE*SUSAN****MI*44455666610~
AMT*AU*1200~
SVC*HC:93555*1200*600~
DTM*472*20190310~
CAS*PR*1*600~
AMT*B6*1200~
SE*26*112233~
GE*1*12345678~
GS*HP*ABCD*ABCD*20190827*123456*12345678*X*005010X221A1~
ST*837*20002813*005010X222A1~
BHT*0019*00*20002813*20200728*151613*CH~
NM1*41*2*HCSIS CHC SPLIT PHASE 1*****46*545746153~
PER*IC*IT SUPERVISOR*TE*7174255377~
NM1*40*2*DEPARTMENT OF HUMAN SERVICES*****46*445314156~
HL*1**20*1~
NM1*85*2*HCSIS CHC SPLIT PHASE 1~
N3*1006 HEMLOCK DR~
N4*HARRISBURG*PA*171103595~
REF*EI*000021698~
HL*2*1*22*0~
SBR*P*18*******MC~
NM1*IL*1*PTSURNAME*PTNAME****MI*123456~
N3*33 LONG ISLAND PARK~
N4*NEW YORK CITY*NY*10001~
DMG*D8*19991122*U~
NM1*PR*2*DEPARTMENT OF HUMAN SERVICES*****PI*1700393030~
N3*555 MAIN STREET~
N4*HARRISBURG*PA*171069022~
REF*G2*3004658650007~
CLM*16.31*32***12:B:1*Y*A*Y*Y*P~
HI*ABK:A0100~
LX*1~
SV1*HC:W1724*32*UN*32***1~
PWK*03*AA~
PWK*04*BM~
DTP*472*D8*20200312~
LX*2~
SV1*HC:W1721*31*UN*3***1~
DTP*472*D8*20200312~
REF*6R*31~
SE*32*20002813~
GE*1*12345678~
IEA*2*191511902~

```
{% endtab %}

{% tab title="Response" %}
```json
{
 "message": {
  "isa": {
   "security_info": "          ",
   "date": "190827",
   "receiver_type": "ZZ",
   "security_qual": "00",
   "time": "0212",
   "sender_id": "ABCPAYER       ",
   "mode": "P",
   "ds": ":",
   "auth_info": "          ",
   "sender_type": "ZZ",
   "rs": "^",
   "receiver_id": "ABCPAYER       ",
   "id": "191511902",
   "ack": "0",
   "auth_qual": "00",
   "version": "00501"
  },
  "groups": [
   {
    "transactions": [
     {
      "st": {
       "type": "835",
       "txid": "112233"
      },
      "financial_info": {
       "check_issue_oreft_effective_date": "20190316",
       "sender_bank_account_number": "24681012",
       "receiver_or_provider_bankid_number": "111333555",
       "depository_financial_institutiondfi_identification_number_qualifier": "01",
       "payment_format_code": "CCP",
       "payment_method_code": "ACH",
       "senderdfi_identifier": "888999777",
       "receiver_or_provider_account_number": "144444",
       "account_number_qualifier_14": "DA",
       "depository_financial_institutiondfi_identification_number_qualifier_12": "01",
       "payer_identifier": "1935665544",
       "total_actual_provider_payment_amount": 1100,
       "account_number_qualifier": "DA",
       "credit_or_debit_flag_code": "C",
       "transaction_handling_code": "I"
      },
      "reassociation_trace_number": {
       "type": "1",
       "id": "71700666555",
       "payer_identifier": "1935665544"
      },
      "production_date": {
       "time_qualifier_date": "405",
       "production_date": "20190314"
      },
      "payer_identification": {
       "payer_id": {
        "entity_identifier_code": "PR",
        "payer_name": "RUSHMORE LIFE"
       },
       "payer_address": {
        "line": "10 SOUTH AVENUET"
       },
       "payer_address_ext": {
        "city": "RAPID CITY",
        "state": "SD",
        "zip": "55111"
       },
       "payer_technical_contact_info": [
        {
         "type": "BL",
         "name": "JOHN WAYNE",
         "system": "TE",
         "value": "8005551212",
         "system_2": "EX",
         "value_2": "123"
        }
       ]
      },
      "payee_identification": {
       "payee_id": {
        "entity_identifier_code": "PE",
        "payee_name": "ACME MEDICAL CENTER",
        "identification_code_qualifier": "XX",
        "payee_identification_code": "5544667733"
       },
       "payee_additional_id": [
        {
         "system": "TJ",
         "value": "777667755"
        }
       ]
      },
      "claim_payment_line_item": [
       {
        "header_number": {
         "assigned_number": 1
        },
        "claim_payment_information": [
         {
          "claim_payment_info": {
           "claim_payment_amount": 500,
           "claim_status_code": "1",
           "claim_filing_indicator_code": "12",
           "patient_responsibility_amount": 300,
           "payer_claim_control_number": "94060555410000",
           "patient_control_number": "5554555444",
           "claim_frequency_code": "1",
           "facility_type_code": "11",
           "total_claim_charge_amount": 800
          },
          "patient": {
           "type": "QC",
           "kind": "1",
           "name": "BUDD",
           "name_first": "WILLIAM",
           "identifier_system": "MI",
           "identifier_value": "33344555510"
          },
          "claim_supplemental_info": [
           {
            "type": "AU",
            "amount": 800
           }
          ],
          "service_payment_information": [
           {
            "service_payment_info": {
             "service": {
              "system": "HC",
              "code": "99211"
             },
             "charge_amount": 800,
             "line_item_provider_payment_amount": 500
            },
            "service_date": [
             {
              "time_qualifier_date": "472",
              "service_date": "20190301"
             }
            ],
            "service_adjustment": [
             {
              "amount": 300,
              "group": "PR",
              "reason": "1"
             }
            ],
            "service_supplemental_amount": [
             {
              "type": "B6",
              "amount": 800
             }
            ]
           }
          ]
         },
         {
          "claim_payment_info": {
           "claim_payment_amount": 600,
           "claim_status_code": "1",
           "claim_filing_indicator_code": "12",
           "patient_responsibility_amount": 600,
           "payer_claim_control_number": "9407779923000",
           "patient_control_number": "8765432112",
           "claim_frequency_code": "1",
           "facility_type_code": "11",
           "total_claim_charge_amount": 1200
          },
          "patient": {
           "type": "QC",
           "kind": "1",
           "name": "SETTLE",
           "name_first": "SUSAN",
           "identifier_system": "MI",
           "identifier_value": "44455666610"
          },
          "claim_supplemental_info": [
           {
            "type": "AU",
            "amount": 1200
           }
          ],
          "service_payment_information": [
           {
            "service_payment_info": {
             "service": {
              "system": "HC",
              "code": "93555"
             },
             "charge_amount": 1200,
             "line_item_provider_payment_amount": 600
            },
            "service_date": [
             {
              "time_qualifier_date": "472",
              "service_date": "20190310"
             }
            ],
            "service_adjustment": [
             {
              "amount": 600,
              "group": "PR",
              "reason": "1"
             }
            ],
            "service_supplemental_amount": [
             {
              "type": "B6",
              "amount": 1200
             }
            ]
           }
          ]
         }
        ]
       }
      ],
      "se": {
       "segment_count": 26,
       "txid": "112233"
      }
     }
    ],
    "gs": {
     "function": "HP",
     "sender": "ABCD",
     "receiver": "ABCD",
     "date": "20190827",
     "time": "123456",
     "id": "12345678",
     "standard": "X",
     "version": "005010X221A1"
    },
    "ge": {
     "number_transactions": 1,
     "id": "12345678"
    }
   },
   {
    "transactions": [
     {
      "st": {
       "type": "837",
       "txid": "20002813",
       "version": "005010X222A1"
      },
      "tx": {
       "struct": "0019",
       "purpose": "00",
       "txid": "20002813",
       "date": "20200728",
       "time": "151613",
       "type": "CH"
      },
      "submitter": {
       "name": {
        "type": "41",
        "kind": "2",
        "name": "HCSIS CHC SPLIT PHASE 1",
        "identifier_system": "46",
        "identifier_value": "545746153"
       },
       "contacts": [
        {
         "type": "IC",
         "name": "IT SUPERVISOR",
         "system": "TE",
         "value": "7174255377"
        }
       ]
      },
      "receiver": {
       "name": {
        "type": "40",
        "kind": "2",
        "name": "DEPARTMENT OF HUMAN SERVICES",
        "identifier_system": "46",
        "identifier_value": "445314156"
       }
      },
      "billing_provider_group": [
       {
        "_hl": {
         "id": "1",
         "level": "20",
         "child": "1"
        },
        "billing_provider": {
         "name": {
          "type": "85",
          "kind": "2",
          "name": "HCSIS CHC SPLIT PHASE 1"
         },
         "address": {
          "line": "1006 HEMLOCK DR"
         },
         "address_ext": {
          "city": "HARRISBURG",
          "state": "PA",
          "zip": "171103595"
         },
         "tax_id": {
          "system": "EI",
          "value": "000021698"
         }
        },
        "subscriber": [
         {
          "_hl": {
           "id": "2",
           "parent": "1",
           "level": "22",
           "child": "0"
          },
          "info": {
           "payer_responsibility": "P",
           "relationship": "18",
           "claim_filing": "MC"
          },
          "name": {
           "name": {
            "type": "IL",
            "kind": "1",
            "name": "PTSURNAME",
            "name_first": "PTNAME",
            "identifier_system": "MI",
            "identifier_value": "123456"
           },
           "address": {
            "line": "33 LONG ISLAND PARK"
           },
           "address_ext": {
            "city": "NEW YORK CITY",
            "state": "NY",
            "zip": "10001"
           },
           "demographic_info": {
            "date_format": "D8",
            "birth_date": "19991122",
            "gender": "U"
           }
          },
          "payer": {
           "name": {
            "type": "PR",
            "kind": "2",
            "name": "DEPARTMENT OF HUMAN SERVICES",
            "identifier_system": "PI",
            "identifier_value": "1700393030"
           },
           "address": {
            "line": "555 MAIN STREET"
           },
           "address_ext": {
            "city": "HARRISBURG",
            "state": "PA",
            "zip": "171069022"
           },
           "billing_provider_secondary_id": [
            {
             "system": "G2",
             "value": "3004658650007"
            }
           ]
          },
          "claim": [
           {
            "claim_info": {
             "benifits_assignment": "A",
             "provider_signature": "Y",
             "release_of_information": "Y",
             "service_location": {
              "code": "12",
              "system": "B",
              "frequency": "1"
             },
             "total": 32,
             "patient_signature_source": "P",
             "benifits_assignment_certification": "Y",
             "claim_id": "16.31"
            },
            "diagnoses": {
             "diag_1": {
              "system": "ABK",
              "code": "A0100"
             }
            },
            "service_line": [
             {
              "number": {
               "assigned_number": 1
              },
              "service": {
               "service": {
                "system": "HC",
                "code": "W1724"
               },
               "unit": "UN",
               "charge_amount": 32,
               "diagnoses": {
                "diag": 1
               },
               "unit_count": 32
              },
              "line_supplemental_info": [
               {
                "type": "03",
                "transmission": "AA"
               },
               {
                "type": "04",
                "transmission": "BM"
               }
              ],
              "service_date": {
               "type": "472",
               "format": "D8",
               "date": "20200312"
              }
             },
             {
              "number": {
               "assigned_number": 2
              },
              "service": {
               "service": {
                "system": "HC",
                "code": "W1721"
               },
               "unit": "UN",
               "charge_amount": 31,
               "diagnoses": {
                "diag": 1
               },
               "unit_count": 3
              },
              "service_date": {
               "type": "472",
               "format": "D8",
               "date": "20200312"
              },
              "service_line_id": {
               "system": "6R",
               "value": "31"
              }
             }
            ]
           }
          ]
         }
        ]
       }
      ],
      "se": {
       "segment_count": 32,
       "txid": "20002813"
      }
     }
    ],
    "gs": {
     "function": "HP",
     "sender": "ABCD",
     "receiver": "ABCD",
     "date": "20190827",
     "time": "123456",
     "id": "12345678",
     "standard": "X",
     "version": "005010X221A1"
    },
    "ge": {
     "number_transactions": 1,
     "id": "12345678"
    }
   }
  ],
  "iea": {
   "number_groups": 2,
   "id": "191511902"
  }
 },
 "errors": [ ... ]
}
```
{% endtab %}
{% endtabs %}

This example contains two groups: the first has **835** message, and the other one contains **837** message. Parser supports multiple groups and transaction sets in the document.

### Generating a message

`/x12/generate` endpoint allows the generating X12 messages from the JSON data obtained from the `/x12/parse` operation. Pass the `message` field's content from the parsing result to `/x12/generate` to get your message back almost unchanged.

Keep in mind that the generator disregards the provided segment count value in favor of the one it computes itself.

{% tabs %}
{% tab title="835 & 837 Request" %}
```json
POST /x12/generate
content-type: application/json

{
  "isa": {
   "security_info": "          ",
   "date": "190827",
   "receiver_type": "ZZ",
   "security_qual": "00",
   "time": "0212",
   "sender_id": "ABCPAYER       ",
   "mode": "P",
   "ds": ":",
   "auth_info": "          ",
   "sender_type": "ZZ",
   "rs": "^",
   "receiver_id": "ABCPAYER       ",
   "id": "191511902",
   "ack": "0",
   "auth_qual": "00",
   "version": "00501"
  },
  "groups": [
   {
    "transactions": [
     {
      "st": {
       "type": "835",
       "txid": "112233"
      },
      "financial_info": {
       "check_issue_oreft_effective_date": "20190316",
       "sender_bank_account_number": "24681012",
       "receiver_or_provider_bankid_number": "111333555",
       "depository_financial_institutiondfi_identification_number_qualifier": "01",
       "payment_format_code": "CCP",
       "payment_method_code": "ACH",
       "senderdfi_identifier": "888999777",
       "receiver_or_provider_account_number": "144444",
       "account_number_qualifier_14": "DA",
       "depository_financial_institutiondfi_identification_number_qualifier_12": "01",
       "payer_identifier": "1935665544",
       "total_actual_provider_payment_amount": 1100,
       "account_number_qualifier": "DA",
       "credit_or_debit_flag_code": "C",
       "transaction_handling_code": "I"
      },
      "reassociation_trace_number": {
       "type": "1",
       "id": "71700666555",
       "payer_identifier": "1935665544"
      },
      "production_date": {
       "time_qualifier_date": "405",
       "production_date": "20190314"
      },
      "payer_identification": {
       "payer_id": {
        "entity_identifier_code": "PR",
        "payer_name": "RUSHMORE LIFE"
       },
       "payer_address": {
        "line": "10 SOUTH AVENUET"
       },
       "payer_address_ext": {
        "city": "RAPID CITY",
        "state": "SD",
        "zip": "55111"
       },
       "payer_technical_contact_info": [
        {
         "type": "BL",
         "name": "JOHN WAYNE",
         "system": "TE",
         "value": "8005551212",
         "system_2": "EX",
         "value_2": "123"
        }
       ]
      },
      "payee_identification": {
       "payee_id": {
        "entity_identifier_code": "PE",
        "payee_name": "ACME MEDICAL CENTER",
        "identification_code_qualifier": "XX",
        "payee_identification_code": "5544667733"
       },
       "payee_additional_id": [
        {
         "system": "TJ",
         "value": "777667755"
        }
       ]
      },
      "claim_payment_line_item": [
       {
        "header_number": {
         "assigned_number": 1
        },
        "claim_payment_information": [
         {
          "claim_payment_info": {
           "claim_payment_amount": 500,
           "claim_status_code": "1",
           "claim_filing_indicator_code": "12",
           "patient_responsibility_amount": 300,
           "payer_claim_control_number": "94060555410000",
           "patient_control_number": "5554555444",
           "claim_frequency_code": "1",
           "facility_type_code": "11",
           "total_claim_charge_amount": 800
          },
          "patient": {
           "type": "QC",
           "kind": "1",
           "name": "BUDD",
           "name_first": "WILLIAM",
           "identifier_system": "MI",
           "identifier_value": "33344555510"
          },
          "claim_supplemental_info": [
           {
            "type": "AU",
            "amount": 800
           }
          ],
          "service_payment_information": [
           {
            "service_payment_info": {
             "service": {
              "system": "HC",
              "code": "99211"
             },
             "charge_amount": 800,
             "line_item_provider_payment_amount": 500
            },
            "service_date": [
             {
              "time_qualifier_date": "472",
              "service_date": "20190301"
             }
            ],
            "service_adjustment": [
             {
              "amount": 300,
              "group": "PR",
              "reason": "1"
             }
            ],
            "service_supplemental_amount": [
             {
              "type": "B6",
              "amount": 800
             }
            ]
           }
          ]
         },
         {
          "claim_payment_info": {
           "claim_payment_amount": 600,
           "claim_status_code": "1",
           "claim_filing_indicator_code": "12",
           "patient_responsibility_amount": 600,
           "payer_claim_control_number": "9407779923000",
           "patient_control_number": "8765432112",
           "claim_frequency_code": "1",
           "facility_type_code": "11",
           "total_claim_charge_amount": 1200
          },
          "patient": {
           "type": "QC",
           "kind": "1",
           "name": "SETTLE",
           "name_first": "SUSAN",
           "identifier_system": "MI",
           "identifier_value": "44455666610"
          },
          "claim_supplemental_info": [
           {
            "type": "AU",
            "amount": 1200
           }
          ],
          "service_payment_information": [
           {
            "service_payment_info": {
             "service": {
              "system": "HC",
              "code": "93555"
             },
             "charge_amount": 1200,
             "line_item_provider_payment_amount": 600
            },
            "service_date": [
             {
              "time_qualifier_date": "472",
              "service_date": "20190310"
             }
            ],
            "service_adjustment": [
             {
              "amount": 600,
              "group": "PR",
              "reason": "1"
             }
            ],
            "service_supplemental_amount": [
             {
              "type": "B6",
              "amount": 1200
             }
            ]
           }
          ]
         }
        ]
       }
      ],
      "se": {
       "segment_count": 26,
       "txid": "112233"
      }
     }
    ],
    "gs": {
     "function": "HP",
     "sender": "ABCD",
     "receiver": "ABCD",
     "date": "20190827",
     "time": "123456",
     "id": "12345678",
     "standard": "X",
     "version": "005010X221A1"
    },
    "ge": {
     "number_transactions": 1,
     "id": "12345678"
    }
   },
   {
    "transactions": [
     {
      "st": {
       "type": "837",
       "txid": "20002813",
       "version": "005010X222A1"
      },
      "tx": {
       "struct": "0019",
       "purpose": "00",
       "txid": "20002813",
       "date": "20200728",
       "time": "151613",
       "type": "CH"
      },
      "submitter": {
       "name": {
        "type": "41",
        "kind": "2",
        "name": "HCSIS CHC SPLIT PHASE 1",
        "identifier_system": "46",
        "identifier_value": "545746153"
       },
       "contacts": [
        {
         "type": "IC",
         "name": "IT SUPERVISOR",
         "system": "TE",
         "value": "7174255377"
        }
       ]
      },
      "receiver": {
       "name": {
        "type": "40",
        "kind": "2",
        "name": "DEPARTMENT OF HUMAN SERVICES",
        "identifier_system": "46",
        "identifier_value": "445314156"
       }
      },
      "billing_provider_group": [
       {
        "_hl": {
         "id": "1",
         "level": "20",
         "child": "1"
        },
        "billing_provider": {
         "name": {
          "type": "85",
          "kind": "2",
          "name": "HCSIS CHC SPLIT PHASE 1"
         },
         "address": {
          "line": "1006 HEMLOCK DR"
         },
         "address_ext": {
          "city": "HARRISBURG",
          "state": "PA",
          "zip": "171103595"
         },
         "tax_id": {
          "system": "EI",
          "value": "000021698"
         }
        },
        "subscriber": [
         {
          "_hl": {
           "id": "2",
           "parent": "1",
           "level": "22",
           "child": "0"
          },
          "info": {
           "payer_responsibility": "P",
           "relationship": "18",
           "claim_filing": "MC"
          },
          "name": {
           "name": {
            "type": "IL",
            "kind": "1",
            "name": "PTSURNAME",
            "name_first": "PTNAME",
            "identifier_system": "MI",
            "identifier_value": "123456"
           },
           "address": {
            "line": "33 LONG ISLAND PARK"
           },
           "address_ext": {
            "city": "NEW YORK CITY",
            "state": "NY",
            "zip": "10001"
           },
           "demographic_info": {
            "date_format": "D8",
            "birth_date": "19991122",
            "gender": "U"
           }
          },
          "payer": {
           "name": {
            "type": "PR",
            "kind": "2",
            "name": "DEPARTMENT OF HUMAN SERVICES",
            "identifier_system": "PI",
            "identifier_value": "1700393030"
           },
           "address": {
            "line": "555 MAIN STREET"
           },
           "address_ext": {
            "city": "HARRISBURG",
            "state": "PA",
            "zip": "171069022"
           },
           "billing_provider_secondary_id": [
            {
             "system": "G2",
             "value": "3004658650007"
            }
           ]
          },
          "claim": [
           {
            "claim_info": {
             "benifits_assignment": "A",
             "provider_signature": "Y",
             "release_of_information": "Y",
             "service_location": {
              "code": "12",
              "system": "B",
              "frequency": "1"
             },
             "total": 32,
             "patient_signature_source": "P",
             "benifits_assignment_certification": "Y",
             "claim_id": "16.31"
            },
            "diagnoses": {
             "diag_1": {
              "system": "ABK",
              "code": "A0100"
             }
            },
            "service_line": [
             {
              "number": {
               "assigned_number": 1
              },
              "service": {
               "service": {
                "system": "HC",
                "code": "W1724"
               },
               "unit": "UN",
               "charge_amount": 32,
               "diagnoses": {
                "diag": 1
               },
               "unit_count": 32
              },
              "line_supplemental_info": [
               {
                "type": "03",
                "transmission": "AA"
               },
               {
                "type": "04",
                "transmission": "BM"
               }
              ],
              "service_date": {
               "type": "472",
               "format": "D8",
               "date": "20200312"
              }
             },
             {
              "number": {
               "assigned_number": 2
              },
              "service": {
               "service": {
                "system": "HC",
                "code": "W1721"
               },
               "unit": "UN",
               "charge_amount": 31,
               "diagnoses": {
                "diag": 1
               },
               "unit_count": 3
              },
              "service_date": {
               "type": "472",
               "format": "D8",
               "date": "20200312"
              },
              "service_line_id": {
               "system": "6R",
               "value": "31"
              }
             }
            ]
           }
          ]
         }
        ]
       }
      ],
      "se": {
       "segment_count": 32,
       "txid": "20002813"
      }
     }
    ],
    "gs": {
     "function": "HP",
     "sender": "ABCD",
     "receiver": "ABCD",
     "date": "20190827",
     "time": "123456",
     "id": "12345678",
     "standard": "X",
     "version": "005010X221A1"
    },
    "ge": {
     "number_transactions": 1,
     "id": "12345678"
    }
   }
  ],
  "iea": {
   "number_groups": 2,
   "id": "191511902"
  }
 }
```
{% endtab %}

{% tab title="Response" %}
```
ISA*00*          *00*          *ZZ*ABCPAYER       *ZZ*ABCPAYER       *190827*0212*^*00501*191511902*0*P*:~
GS*HP*ABCD*ABCD*20190827*123456*12345678*X*005010X221A1~
ST*835*112233~
BPR*I*1100*C*ACH*CCP*01*888999777*DA*24681012*1935665544**01*111333555*DA*144444*20190316~
TRN*1*71700666555*1935665544~
DTM*405*20190314~
N1*PR*RUSHMORE LIFE~
N3*10 SOUTH AVENUET~
N4*RAPID CITY*SD*55111~
PER*BL*JOHN WAYNE*TE*8005551212*EX*123~
N1*PE*ACME MEDICAL CENTER*XX*5544667733~
REF*TJ*777667755~
LX*1~
CLP*5554555444*1*800*500*300*12*94060555410000*11*1~
NM1*QC*1*BUDD*WILLIAM****MI*33344555510~
AMT*AU*800~
SVC*HC:99211*800*500~
DTM*472*20190301~
CAS*PR*1*300~
AMT*B6*800~
CLP*8765432112*1*1200*600*600*12*9407779923000*11*1~
NM1*QC*1*SETTLE*SUSAN****MI*44455666610~
AMT*AU*1200~
SVC*HC:93555*1200*600~
DTM*472*20190310~
CAS*PR*1*600~
AMT*B6*1200~
SE*26*112233~
GE*1*12345678~
GS*HP*ABCD*ABCD*20190827*123456*12345678*X*005010X221A1~
ST*837*20002813*005010X222A1~
BHT*0019*00*20002813*20200728*151613*CH~
NM1*41*2*HCSIS CHC SPLIT PHASE 1*****46*545746153~
PER*IC*IT SUPERVISOR*TE*7174255377~
NM1*40*2*DEPARTMENT OF HUMAN SERVICES*****46*445314156~
HL*1**20*1~
NM1*85*2*HCSIS CHC SPLIT PHASE 1~
N3*1006 HEMLOCK DR~
N4*HARRISBURG*PA*171103595~
REF*EI*000021698~
HL*2*1*22*0~
SBR*P*18*******MC~
NM1*IL*1*PTSURNAME*PTNAME****MI*123456~
N3*33 LONG ISLAND PARK~
N4*NEW YORK CITY*NY*10001~
DMG*D8*19991122*U~
NM1*PR*2*DEPARTMENT OF HUMAN SERVICES*****PI*1700393030~
N3*555 MAIN STREET~
N4*HARRISBURG*PA*171069022~
REF*G2*3004658650007~
CLM*16.31*32***12:B:1*Y*A*Y*Y*P~
HI*ABK:A0100~
LX*1~
SV1*HC:W1724*32*UN*32***1~
PWK*03*AA~
PWK*04*BM~
DTP*472*D8*20200312~
LX*2~
SV1*HC:W1721*31*UN*3***1~
DTP*472*D8*20200312~
REF*6R*31~
SE*32*20002813~
GE*1*12345678~
IEA*2*191511902~
```
{% endtab %}
{% endtabs %}
