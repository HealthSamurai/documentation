# ISiK

In this tutorial we will guide you how to setup ISiK Stufe 2 FHIR Implementation Guide.&#x20;

## Setup Aidbox with ISiK Stufe 2 IG with prebuilt Aidbox config

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects.&#x20;

There's an [existing guide](broken-reference) for this process. Adhere to this guide, but note a variation when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```
git clone \
  --branch=isik-stufe-2 \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

The git template project contains ISiK FHIR IG preconfigured via .env file.

{% hint style="info" %}
If you already have a running Aidbox instance, please refer to the following guide:
{% endhint %}

{% content-ref url="upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](upload-fhir-implementation-guide/README.md)
{% endcontent-ref %}

## Validate example resources against ISIK Stufe 2 profiles

<details>

<summary>Valid Patient resource example</summary>

{% code lineNumbers="true" fullWidth="false" %}
```json
POST /fhir/Patient
Content-Type: application/json

{
    "resourceType": "Patient",
    "meta": {
        "profile":  [
            "https://gematik.de/fhir/isik/v3/Basismodul/StructureDefinition/ISiKPatient"
        ]
    },
    "identifier":  [
        {
            "type": {
                "coding":  [
                    {
                        "code": "MR",
                        "system": "http://terminology.hl7.org/CodeSystem/v2-0203"
                    }
                ]
            },
            "system": "https://fhir.krankenhaus.example/sid/PID",
            "value": "TestPID"
        },
        {
            "type": {
                "coding":  [
                    {
                        "code": "GKV",
                        "system": "http://fhir.de/CodeSystem/identifier-type-de-basis"
                    }
                ]
            },
            "system": "http://fhir.de/sid/gkv/kvid-10",
            "value": "A123456789"
        },
        {
            "use": "secondary",
            "type": {
                "coding":  [
                    {
                        "code": "PKV",
                        "system": "http://fhir.de/CodeSystem/identifier-type-de-basis"
                    }
                ]
            },
            "value": "1234567890",
            "assigner": {
                "display": "Test PKV AG"
            }
        }
    ],
    "name":  [
        {
            "use": "official",
            "family": "Fürstin von Musterfrau",
            "_family": {
                "extension":  [
                    {
                        "url": "http://fhir.de/StructureDefinition/humanname-namenszusatz",
                        "valueString": "Fürstin"
                    },
                    {
                        "url": "http://hl7.org/fhir/StructureDefinition/humanname-own-name",
                        "valueString": "Musterfrau"
                    },
                    {
                        "url": "http://hl7.org/fhir/StructureDefinition/humanname-own-prefix",
                        "valueString": "von"
                    }
                ]
            },
            "given":  [
                "Erika"
            ],
            "prefix":  [
                "Dr."
            ],
            "_prefix":  [
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier",
                            "valueCode": "AC"
                        }
                    ]
                }
            ]
        },
        {
            "use": "maiden",
            "family": "Gabler",
            "_family": {
                "extension":  [
                    {
                        "url": "http://hl7.org/fhir/StructureDefinition/humanname-own-name",
                        "valueString": "Gabler"
                    }
                ]
            }
        }
    ],
    "active": true,
    "gender": "female",
    "birthDate": "1964-08-12",
    "address":  [
        {
            "type": "both",
            "line":  [
                "Musterweg 2",
                "3. Etage"
            ],
            "_line":  [
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetName",
                            "valueString": "Musterweg"
                        },
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber",
                            "valueString": "2"
                        }
                    ]
                },
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-additionalLocator",
                            "valueString": "3. Etage"
                        }
                    ]
                }
            ],
            "city": "Musterhausen",
            "postalCode": "98764",
            "country": "DE"
        },
        {
            "type": "postal",
            "line":  [
                "Postfach 8 15"
            ],
            "_line":  [
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-postBox",
                            "valueString": "Postfach 8 15"
                        }
                    ]
                }
            ],
            "city": "Musterhausen",
            "postalCode": "98764",
            "country": "DE"
        }
    ]
}
```
{% endcode %}



</details>

{% hint style="info" %}
The sample of the Patient resource is invalid as it does not comply with the 'Name' requirement (an 'official' name must be present) and the 'Patientennummer' requirement (an identifier with the code 'MR' must be present). Additionally, the sample breaches the rules of the 'humanname-namenszusatz' union type extension.
{% endhint %}

<details>

<summary>Invalid Patient resource example</summary>

{% code lineNumbers="true" %}
```json
POST /fhir/Patient
Content-Type: application/json

{
    "resourceType": "Patient",
    "meta": {
        "profile":  [
            "https://gematik.de/fhir/isik/v3/Basismodul/StructureDefinition/ISiKPatient"
        ]
    },
    "identifier":  [
        {
            "type": {
                "coding":  [
                    {
                        "code": "GKV",
                        "system": "http://fhir.de/CodeSystem/identifier-type-de-basis"
                    }
                ]
            },
            "system": "http://fhir.de/sid/gkv/kvid-10",
            "value": "A123456789"
        },
        {
            "use": "secondary",
            "type": {
                "coding":  [
                    {
                        "code": "PKV",
                        "system": "http://fhir.de/CodeSystem/identifier-type-de-basis"
                    }
                ]
            },
            "value": "1234567890",
            "assigner": {
                "display": "Test PKV AG"
            }
        }
    ],
    "name":  [
        {
            "family": "Fürstin von Musterfrau",
            "_family": {
                "extension":  [
                    {
                        "url": "http://fhir.de/StructureDefinition/humanname-namenszusatz",
                        "valueCode": "Fürstin"
                    },
                    {
                        "url": "http://hl7.org/fhir/StructureDefinition/humanname-own-name",
                        "valueString": "Musterfrau"
                    },
                    {
                        "url": "http://hl7.org/fhir/StructureDefinition/humanname-own-prefix",
                        "valueString": "von"
                    }
                ]
            },
            "given":  [
                "Erika"
            ],
            "prefix":  [
                "Dr."
            ],
            "_prefix":  [
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier",
                            "valueCode": "AC"
                        }
                    ]
                }
            ]
        },
        {
            "use": "maiden",
            "family": "Gabler",
            "_family": {
                "extension":  [
                    {
                        "url": "http://hl7.org/fhir/StructureDefinition/humanname-own-name",
                        "valueString": "Gabler"
                    }
                ]
            }
        }
    ],
    "active": true,
    "gender": "female",
    "birthDate": "1964-08-12",
    "address":  [
        {
            "type": "both",
            "line":  [
                "Musterweg 2",
                "3. Etage"
            ],
            "_line":  [
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetName",
                            "valueString": "Musterweg"
                        },
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber",
                            "valueString": "2"
                        }
                    ]
                },
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-additionalLocator",
                            "valueString": "3. Etage"
                        }
                    ]
                }
            ],
            "city": "Musterhausen",
            "postalCode": "98764",
            "country": "DE"
        },
        {
            "type": "postal",
            "line":  [
                "Postfach 8 15"
            ],
            "_line":  [
                {
                    "extension":  [
                        {
                            "url": "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-postBox",
                            "valueString": "Postfach 8 15"
                        }
                    ]
                }
            ],
            "city": "Musterhausen",
            "postalCode": "98764",
            "country": "DE"
        }
    ]
}
```
{% endcode %}



</details>
