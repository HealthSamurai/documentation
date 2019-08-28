# Uploading Sample Data

## Endpoint for Bundles

Aidbox endpoint for bundles will be the base URL of your Box: `/` in Aidbox REST Console and `[base]` \(`https://<YOUR-BOX>.aidbox.app`\) in any other external client \(Postman for example\). Alternatively `/fhir` and `[base]/fhir` can be used, read more about [/fhir]() endpoints. 

## Bundle Example

According to the [FHIR specification](https://www.hl7.org/fhir/http.html#transaction), bundle is a container for a collection of resources for transport and persistence purposes. Below is an example of request, which creates two patients:

{% tabs %}
{% tab title="Request" %}
```javascript
POST /fhir

{
  "resourceType": "bundle",
  "type": "transaction",
  "entry":
  [{
      "request": {
        "method": "POST",
        "url": "/Patient"
      },
      "resource": {
        "name": [{
            "given": ["Bob"]
          }
        ]
      }
    }, {
      "request": {
        "method": "POST",
        "url": "/Patient"
      },
      "resource": {
        "name": [{
            "given": ["Peter"]
          }
        ]
      }
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
    "id": "64",
    "type": "transaction-response",
    "resourceType": "Bundle",
    "entry": [
        {
            "resource": {
                "name": [
                    {
                        "given": [
                            "Bob"
                        ]
                    }
                ],
                "id": "d1f48e05-220c-4d27-9d30-22df21f1b86b",
                "resourceType": "Patient",
                "meta": {
                    "lastUpdated": "2018-10-29T12:47:45.769Z",
                    "versionId": "64",
                    "tag": [
                        {
                            "system": "https://aidbox.io",
                            "code": "created"
                        }
                    ]
                }
            },
            "status": 201
        },
        {
            "resource": {
                "name": [
                    {
                        "given": [
                            "Peter"
                        ]
                    }
                ],
                "id": "49bcfa88-9915-4832-a763-1d60cb561cc3",
                "resourceType": "Patient",
                "meta": {
                    "lastUpdated": "2018-10-29T12:47:45.769Z",
                    "versionId": "64",
                    "tag": [
                        {
                            "system": "https://aidbox.io",
                            "code": "created"
                        }
                    ]
                }
            },
            "status": 201
        }
    ]
}
```
{% endtab %}
{% endtabs %}

[FHIR specification](https://www.hl7.org/fhir/http.html#transaction) says that bundle should look like:

```javascript
{
  "resourceType": "bundle",
  "type": "transaction",
  "entry": [{
      "request": {
        "method": "GET",
        "url": "/Patient"
      }
    }
  ]
}

```

Every transaction bundle MUST have the **type** field which value can be **transaction** or **batch**, each element of`entry` field MUST has **method** and **url** fields in the `request`.

Bundles must be sent via POST method to BASE\_URL which is basically the URL of your Box for external clients and `/` or `/fhir` for REST Console \(actually hostname exists in REST Console, but it hidden\).

## Aidbox REST Console

1. Access your Box in Aidbox.Cloud.
2. Open REST Console.
3. Type in `POST /` or `POST /fhir`. \(read about [difference]() between Aidbox and FHIR\)
4. Leave next line empty.
5. Paste your bundle.
6. Press Ctrl+Enter or click the **Send** button.

![Bundle in Aidbox.Cloud REST Console](../.gitbook/assets/scr-2018-10-29_16-24-11.png)

This is a brief description about how to work with transactions and batches in [Aidbox](https://www.health-samurai.io/aidbox). More interesting information coming soon, stay tuned!

## Generating transaction bundle with Synthea \(advanced topic\)

Here's how we can install Synthea — [Synthetic Patient Population Simulator](https://github.com/synthetichealth/synthea).

```bash
git clone https://github.com/synthetichealth/synthea.git
cd synthea
./gradlew build check test
```

Generating the population one at a time...

```bash
./run_synthea -s 1000
ls output/fhir
```

...you should see few files with names similar to those:

```bash
hospitalInformation1537450368636.json 
Mckinley734_Johnston597_f25b9177-3c01-4d76-a48f-0a83affa5a56.json
```

{% hint style="danger" %}
**Make sure that you  use`POST /fhir`for synthea bundles, not just `POST /`**
{% endhint %}

Copy content of first file to REST Console, use `POST /fhir` to upload FHIR transaction bundle. Everything else is the same as in the previous step. After that, upload the content of the second file into Aidbox. That's all, you are awesome!

Read more about Synthea generator - [https://github.com/synthetichealth/synthea](https://github.com/synthetichealth/synthea).

