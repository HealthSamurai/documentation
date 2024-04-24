# X12 message converter

Aidbox includes a couple of endpoints to allow the conversion of x12 messages.

Right now, the converter only support four types of x12 messages: **835**, **837**, **270** and **271**.

### Parsing a message

To parse a message, use `/x12/parse` endpoint.

{% tabs %}
{% tab title="835 Request" %}
```
POST /x12/parse
content-type: text/plain
accept: application/json

ISA*00*          *00*          *ZZ*ABCPAYER       *ZZ*ABCPAYER       *190827*0212*^*00501*191511902*0*P*:~
GS*HP*ABCD*ABCD*20190827*12345678*12345678*X*005010X221A1~
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
IEA*1*191511902~
```
{% endtab %}

{% tab title="385 Response" %}
```json
{
  "errors": [ /* ... */ ],
  "control": {
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
        "gs": {
          "function": "HP",
          "sender": "ABCD",
          "receiver": "ABCD",
          "date": "20190827",
          "time": "12345678",
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
      "number_groups": 1,
      "id": "191511902"
    }
  },
  "message": {
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
      "total_actual_provider_payment_amount": 1100.0,
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
              "claim_payment_amount": 500.0,
              "claim_status_code": "1",
              "claim_filing_indicator_code": "12",
              "patient_responsibility_amount": 300.0,
              "payer_claim_control_number": "94060555410000",
              "patient_control_number": "5554555444",
              "claim_frequency_code": "1",
              "facility_type_code": "11",
              "total_claim_charge_amount": 800.0
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
                "amount": 800.0
              }
            ],
            "service_payment_information": [
              {
                "service_payment_info": {
                  "service": {
                    "system": "HC",
                    "code": "99211"
                  },
                  "charge_amount": 800.0,
                  "line_item_provider_payment_amount": 500.0
                },
                "service_date": [
                  {
                    "time_qualifier_date": "472",
                    "service_date": "20190301"
                  }
                ],
                "service_adjustment": [
                  {
                    "amount": 300.0,
                    "group": "PR",
                    "reason": "1"
                  }
                ],
                "service_supplemental_amount": [
                  {
                    "type": "B6",
                    "amount": 800.0
                  }
                ]
              }
            ]
          },
          {
            "claim_payment_info": {
              "claim_payment_amount": 600.0,
              "claim_status_code": "1",
              "claim_filing_indicator_code": "12",
              "patient_responsibility_amount": 600.0,
              "payer_claim_control_number": "9407779923000",
              "patient_control_number": "8765432112",
              "claim_frequency_code": "1",
              "facility_type_code": "11",
              "total_claim_charge_amount": 1200.0
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
                "amount": 1200.0
              }
            ],
            "service_payment_information": [
              {
                "service_payment_info": {
                  "service": {
                    "system": "HC",
                    "code": "93555"
                  },
                  "charge_amount": 1200.0,
                  "line_item_provider_payment_amount": 600.0
                },
                "service_date": [
                  {
                    "time_qualifier_date": "472",
                    "service_date": "20190310"
                  }
                ],
                "service_adjustment": [
                  {
                    "amount": 600.0,
                    "group": "PR",
                    "reason": "1"
                  }
                ],
                "service_supplemental_amount": [
                  {
                    "type": "B6",
                    "amount": 1200.0
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
}

```
{% endtab %}

{% tab title="270 Request" %}
```
POST /x12/parse
accept: application/json

ISA*00*          *00*          *ZZ*1234567        *ZZ*11111          *170508*1141*^*00501*000000101*1*P*:~
GS*HC*XXXXXXX*XXXXX*20170617*1741*101*X*005010X279A1~
ST*270*1234*005010X279A1~
BHT*0022*13*10001234*20060501*1319~
HL*1**20*1~
NM1*PR*2*ABC COMPANY*****PI*842610001~
HL*2*1*21*1~
NM1*1P*2*BONE AND JOINT CLINIC*****SV*2000035~
HL*3*2*22*0~
TRN*1*93175-012547*9877281234~
NM1*IL*1*SMITH*ROBERT****MI*11122333301~
DMG*D8*19430519~
DTP*291*D8*20060501~
EQ*30~
SE*13*1234~
GE*1*101~
IEA*1*000000101~
```
{% endtab %}

{% tab title="270 Response" %}
```json
{
  "errors": [/* ... */],
  "control": {
    "isa": {
      "security_info": "          ",
      "date": "170508",
      "receiver_type": "ZZ",
      "security_qual": "00",
      "time": "1141",
      "sender_id": "1234567        ",
      "mode": "P",
      "ds": ":",
      "auth_info": "          ",
      "sender_type": "ZZ",
      "rs": "^",
      "receiver_id": "11111          ",
      "id": "000000101",
      "ack": "1",
      "auth_qual": "00",
      "version": "00501"
    },
    "groups": [
      {
        "gs": {
          "function": "HC",
          "sender": "XXXXXXX",
          "receiver": "XXXXX",
          "date": "20170617",
          "time": "1741",
          "id": "101",
          "standard": "X",
          "version": "005010X279A1"
        },
        "ge": {
          "number_transactions": 1,
          "id": "101"
        }
      }
    ],
    "iea": {
      "number_groups": 1,
      "id": "000000101"
    }
  },
  "message": {
    "st": {
      "type": "270",
      "txid": "1234",
      "version": "005010X279A1"
    },
    "tx": {
      "struct": "0022",
      "purpose": "13",
      "txid": "10001234",
      "date": "20060501",
      "time": "1319"
    },
    "request": [
      {
        "_hl": {
          "id": "1",
          "level": "20",
          "child": "1"
        },
        "source": {
          "name": {
            "type": "PR",
            "kind": "2",
            "name": "ABC COMPANY",
            "identifier_system": "PI",
            "identifier_value": "842610001"
          }
        },
        "receivers": [
          {
            "_hl": {
              "id": "2",
              "parent": "1",
              "level": "21",
              "child": "1"
            },
            "receiver": {
              "name": {
                "type": "1P",
                "kind": "2",
                "name": "BONE AND JOINT CLINIC",
                "identifier_system": "SV",
                "identifier_value": "2000035"
              }
            },
            "subscribers": [
              {
                "_hl": {
                  "id": "3",
                  "parent": "2",
                  "level": "22",
                  "child": "0"
                },
                "trace_number": [
                  {
                    "type": "1",
                    "id": "93175-012547",
                    "trace_assigning_entity_identifier": "9877281234"
                  }
                ],
                "name": {
                  "name": {
                    "type": "IL",
                    "kind": "1",
                    "name": "SMITH",
                    "name_first": "ROBERT",
                    "identifier_system": "MI",
                    "identifier_value": "11122333301"
                  },
                  "demographic_info": {
                    "date_format": "D8",
                    "birth_date": "19430519"
                  },
                  "date": [
                    {
                      "type": "291",
                      "format": "D8",
                      "date": "20060501"
                    }
                  ],
                  "eligibility_or_benefit_inquiry": [
                    {
                      "subscriber_eligibility_or_benefit_inquiry": {
                        "service_type_code": "30"
                      }
                    }
                  ]
                }
              }
            ]
          }
        ]
      }
    ],
    "se": {
      "segment_count": 13,
      "txid": "1234"
    }
  }
}

```
{% endtab %}
{% endtabs %}

This query returns an object with fields `errors`, `control` and `message`.

By default, parser will try to infer the message type based on the `ST` header. You can use `type` parameter to force parser to use a specific type of message (e. g. `type=837`).

### Generating a message

`/x12/generate` endpoint allows generating x12 messages from the JSON data obtained from `/x12/parse` operation. Pass the content of the `message` field from the parsing result to `/x12/generate` to get your message back in almost unchanged form.

Keep in mind that the generator disregards the provided segment count value in favor of the one it computes itself.

{% tabs %}
{% tab title="835 Request" %}
```json
POST /x12/generate
content-type: application/json

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
    "total_actual_provider_payment_amount": 1100.0,
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
            "claim_payment_amount": 500.0,
            "claim_status_code": "1",
            "claim_filing_indicator_code": "12",
            "patient_responsibility_amount": 300.0,
            "payer_claim_control_number": "94060555410000",
            "patient_control_number": "5554555444",
            "claim_frequency_code": "1",
            "facility_type_code": "11",
            "total_claim_charge_amount": 800.0
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
              "amount": 800.0
            }
          ],
          "service_payment_information": [
            {
              "service_payment_info": {
                "service": {
                  "system": "HC",
                  "code": "99211"
                },
                "charge_amount": 800.0,
                "line_item_provider_payment_amount": 500.0
              },
              "service_date": [
                {
                  "time_qualifier_date": "472",
                  "service_date": "20190301"
                }
              ],
              "service_adjustment": [
                {
                  "amount": 300.0,
                  "group": "PR",
                  "reason": "1"
                }
              ],
              "service_supplemental_amount": [
                {
                  "type": "B6",
                  "amount": 800.0
                }
              ]
            }
          ]
        },
        {
          "claim_payment_info": {
            "claim_payment_amount": 600.0,
            "claim_status_code": "1",
            "claim_filing_indicator_code": "12",
            "patient_responsibility_amount": 600.0,
            "payer_claim_control_number": "9407779923000",
            "patient_control_number": "8765432112",
            "claim_frequency_code": "1",
            "facility_type_code": "11",
            "total_claim_charge_amount": 1200.0
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
              "amount": 1200.0
            }
          ],
          "service_payment_information": [
            {
              "service_payment_info": {
                "service": {
                  "system": "HC",
                  "code": "93555"
                },
                "charge_amount": 1200.0,
                "line_item_provider_payment_amount": 600.0
              },
              "service_date": [
                {
                  "time_qualifier_date": "472",
                  "service_date": "20190310"
                }
              ],
              "service_adjustment": [
                {
                  "amount": 600.0,
                  "group": "PR",
                  "reason": "1"
                }
              ],
              "service_supplemental_amount": [
                {
                  "type": "B6",
                  "amount": 1200.0
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

```
{% endtab %}

{% tab title="835 Response" %}
```
ST*835*112233~
BPR*I*1100.0*C*ACH*CCP*01*888999777*DA*24681012*1935665544**01*111333555*DA*144444*20190316~
TRN*1*71700666555*1935665544~
DTM*405*20190314~
N1*PR*RUSHMORE LIFE~
N3*10 SOUTH AVENUET~
N4*RAPID CITY*SD*55111~
PER*BL*JOHN WAYNE*TE*8005551212*EX*123~
N1*PE*ACME MEDICAL CENTER*XX*5544667733~
REF*TJ*777667755~
LX*1~
CLP*5554555444*1*800.0*500.0*300.0*12*94060555410000*11*1~
NM1*QC*1*BUDD*WILLIAM****MI*33344555510~
AMT*AU*800.0~
SVC*HC:99211*800.0*500.0~
DTM*472*20190301~
CAS*PR*1*300.0~
AMT*B6*800.0~
CLP*8765432112*1*1200.0*600.0*600.0*12*9407779923000*11*1~
NM1*QC*1*SETTLE*SUSAN****MI*44455666610~
AMT*AU*1200.0~
SVC*HC:93555*1200.0*600.0~
DTM*472*20190310~
CAS*PR*1*600.0~
AMT*B6*1200.0~
SE*26*112233~
```
{% endtab %}
{% endtabs %}
