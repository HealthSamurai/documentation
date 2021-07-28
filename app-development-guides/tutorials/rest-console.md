---
description: Learn how to use REST Console to work with your Aidbox via REST API.
---

# Working with REST Console

### REST Console

![REST Console](../../.gitbook/assets/image%20%2849%29%20%284%29.png)

REST Console is designed to work with your Aidbox via [REST API](../../api-1/api/). To make a request type an HTTP method \(`GET`, `POST`, `PUT`, `PATCH`, `DELETE`\) and an operation endpoint \(for example`/Patient` — _please pay attention to the capital letter in the resource name_\).

Right after the first line you can put HTTP headers. E.g. to use YAML format you can put the following header:

```text
Content-type: text/yaml
```

In case you need to send a request body \(e.g., `POST` requests\), the request body content is passed below the resource address, separated by an empty line.

### Create Patient

Here is an example of creating patient:

{% tabs %}
{% tab title="Request YAML" %}
```yaml
POST /Patient?_pretty=true
Content-Type: text/yaml
Accept: text/yaml

resourceType: Patient
name:
- given:
  - Max
gender: male
birthDate: '1990-10-10'
address:
- line:
  - 123 Oxygen St
  city: Hello
  district: World
  state: NY
  postalCode: '3212'
telecom:
- use: home
- system: phone
  value: "(32) 8934 1234"
  use: work
  rank: 1
```
{% endtab %}

{% tab title="Request JSON" %}
```javascript
POST /Patient?_pretty=true
Content-Type: application/json

{
  "resourceType": "Patient",
  "name": [
    {
      "given": ["Max"]
    }
  ],
  "gender": "male",
  "birthDate": "1990-10-10",
  "address": [
    {
      "line": [
        "123 Oxygen St"
      ],
      "city": "Hello",
      "district": "World",
      "state": "NY",
      "postalCode": "3212"
    }
  ],
  "telecom": [
    {
      "use": "home"
    },
    {
      "system": "phone",
      "value": "(32) 8934 1234",
      "use": "work",
      "rank": 1
    }
  ]
}
```
{% endtab %}

{% tab title="Response YAML" %}
```yaml
name:
  - given:
      - Max
gender: male
address:
  - city: Hello
    line:
      - 123 Oxygen St
    state: NY
    district: World
    postalCode: '3212'
telecom:
  - use: home
  - use: work
    rank: 1
    value: (32) 8934 1234
    system: phone
birthDate: '1990-10-10'
id: '8885ce03-154e-4458-bbb7-09305d86c402'
resourceType: Patient
meta:
  lastUpdated: '2021-07-23T14:36:12.392914Z'
  createdAt: '2021-07-23T14:36:12.392914Z'
  versionId: '13'
```
{% endtab %}

{% tab title="Response JSON" %}
```javascript
{
 "name": [
  {
   "given": [
    "Max"
   ]
  }
 ],
 "gender": "male",
 "address": [
  {
   "city": "Hello",
   "line": [
    "123 Oxygen St"
   ],
   "state": "NY",
   "district": "World",
   "postalCode": "3212"
  }
 ],
 "telecom": [
  {
   "use": "home"
  },
  {
   "use": "work",
   "rank": 1,
   "value": "(32) 8934 1234",
   "system": "phone"
  }
 ],
 "birthDate": "1990-10-10",
 "id": "7544300e-8bfa-4929-a7b5-5e1403d0da2d",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2021-07-23T14:37:03.904617Z",
  "createdAt": "2021-07-23T14:37:03.904617Z",
  "versionId": "16"
 }
}
```
{% endtab %}
{% endtabs %}

To get pretty-formatted response add `_pretty=true` query string parameter:

![POST /patient](../../.gitbook/assets/image%20%2857%29.png)



### Get Patient

After sending the request, we receive a response with `Status - 201` and the sent data, which means that our patient has been created. Use the request `GET /Patient/<id>` to see the newly created patient. Also the request `GET /Patient` could be used to get the complete list of patients. 

![GET /Patient](../../.gitbook/assets/image%20%2856%29.png)

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577?_pretty=true
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "name": [
  {
   "given": [
    "Max"
   ]
  }
 ],
 "gender": "male",
 "address": [
  {
   "city": "Hello",
   "line": [
    "123 Oxygen St"
   ],
   "state": "NY",
   "district": "World",
   "postalCode": "3212"
  }
 ],
 "telecom": [
  {
   "use": "home"
  },
  {
   "use": "work",
   "rank": 1,
   "value": "(32) 8934 1234",
   "system": "phone"
  }
 ],
 "birthDate": "1990-10-10",
 "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-23T09:47:36.555Z",
  "versionId": "222",
  "tag": [
   {
    "system": "https://aidbox.io",
    "code": "created"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

### Patch Patient

Next step is to update the patient information. For a partial update use `PATCH /Patient/<id>` in the request body in order to send changed data only. For example, let's change the patient name.

{% tabs %}
{% tab title="Request" %}
```javascript
PATCH /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577?_pretty=true

{
  "name": [
    {
      "given": ["Maximilian"]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "name": [
  {
   "given": [
    "Maximilian"
   ]
  }
 ],
 "gender": "male",
 "address": [
  {
   "city": "Hello",
   "line": [
    "123 Oxygen St"
   ],
   "state": "NY",
   "district": "World",
   "postalCode": "3212"
  }
 ],
 "telecom": [
  {
   "use": "home"
  },
  {
   "use": "work",
   "rank": 1,
   "value": "(32) 8934 1234",
   "system": "phone"
  }
 ],
 "birthDate": "1990-10-10",
 "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-23T09:49:24.927Z",
  "versionId": "223",
  "tag": [
   {
    "system": "https://aidbox.io",
    "code": "updated"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

### Update Patient

Use`PUT /Patient/<id>` to replace the resource.

{% tabs %}
{% tab title="Request" %}
```javascript
PUT /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577

{
  "resourceType": "Patient",
  "id": "example",
  "name": [
    {
      "given": ["Max", "Pain"]
    }
  ],
  "gender": "male",
  "birthDate": "1991-01-02"
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "name": [
  {
   "given": [
    "Max",
    "Pain"
   ]
  }
 ],
 "gender": "male",
 "birthDate": "1991-01-02",
 "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-23T09:50:13.639Z",
  "versionId": "224",
  "tag": [
   {
    "system": "https://aidbox.io",
    "code": "updated"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
In this case, we're updating the data entirely: data that did not get into the request body will be deleted.
{% endhint %}

### Patient History

Use `GET /Patient/<id>/_history` to receive the version history of the patient resource.

Let's try this for the example patient.

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577/_history?_pretty=true
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "resourceType": "Bundle",
 "type": "history",
 "total": 3,
 "entry": [
  {
   "resource": {
    "name": [
     {
      "given": [
       "Max",
       "Pain"
      ]
     }
    ],
    "gender": "male",
    "birthDate": "1991-01-02",
    "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-23T09:50:13.639Z",
     "versionId": "224",
     "tag": [
      {
       "system": "https://aidbox.io",
       "code": "updated"
      }
     ]
    }
   },
   "request": {
    "method": "PUT",
    "url": "Patient"
   }
  },
  {
   "resource": {
    "name": [
     {
      "given": [
       "Maximilian"
      ]
     }
    ],
    "gender": "male",
    "address": [
     {
      "city": "Hello",
      "line": [
       "123 Oxygen St"
      ],
      "state": "NY",
      "district": "World",
      "postalCode": "3212"
     }
    ],
    "telecom": [
     {
      "use": "home"
     },
     {
      "use": "work",
      "rank": 1,
      "value": "(32) 8934 1234",
      "system": "phone"
     }
    ],
    "birthDate": "1990-10-10",
    "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-23T09:49:24.927Z",
     "versionId": "223",
     "tag": [
      {
       "system": "https://aidbox.io",
       "code": "updated"
      }
     ]
    }
   },
   "request": {
    "method": "PUT",
    "url": "Patient"
   }
  },
  {
   "resource": {
    "name": [
     {
      "given": [
       "Max"
      ]
     }
    ],
    "gender": "male",
    "address": [
     {
      "city": "Hello",
      "line": [
       "123 Oxygen St"
      ],
      "state": "NY",
      "district": "World",
      "postalCode": "3212"
     }
    ],
    "telecom": [
     {
      "use": "home"
     },
     {
      "use": "work",
      "rank": 1,
      "value": "(32) 8934 1234",
      "system": "phone"
     }
    ],
    "birthDate": "1990-10-10",
    "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-23T09:47:36.555Z",
     "versionId": "222",
     "tag": [
      {
       "system": "https://aidbox.io",
       "code": "created"
      }
     ]
    }
   },
   "request": {
    "method": "POST",
    "url": "Patient"
   }
  }
 ]
}
```
{% endtab %}
{% endtabs %}

The response contains all versions \(in this case 3\) of the patient resource. The first is the initial state of the resource, the second one has the name changed, and the third is an entirely updated resource. 

To get a specific version of a resource use `GET /Patient/<id>/_history/<versionId>`. It performs the [vread](http://hl7.org/fhir/http.html#vread) operation.

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577/_history/223
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "name": [
  {
   "given": [
    "Maximilian"
   ]
  }
 ],
 "gender": "male",
 "address": [
  {
   "city": "Hello",
   "line": [
    "123 Oxygen St"
   ],
   "state": "NY",
   "district": "World",
   "postalCode": "3212"
  }
 ],
 "telecom": [
  {
   "use": "home"
  },
  {
   "use": "work",
   "rank": 1,
   "value": "(32) 8934 1234",
   "system": "phone"
  }
 ],
 "birthDate": "1990-10-10",
 "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-23T09:49:24.927Z",
  "versionId": "223",
  "tag": [
   {
    "system": "https://aidbox.io",
    "code": "updated"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

### Search Patient

As an example of using FHIR Search API use `GET /Patient?name=<Patient_name>` to get all patient with matching names:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient?name=max&_pretty=true
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "resourceType": "Bundle",
 "type": "searchset",
 "params": [
  {
   "resourceType": "Patient",
   "type": "param",
   "name": "name",
   "modifier": null,
   "values": [
    {
     "value": "max"
    }
   ],
   "search-param": {
    "module": "fhir-3.0.1",
    "name": "name",
    "id": "Patient.name",
    "type": "string",
    "resourceType": "SearchParameter",
    "resource": {
     "resourceType": "Entity",
     "id": "Patient"
    },
    "expression": [
     [
      "name"
     ]
    ],
    "typedExpression": [
     {
      "path": [
       "name"
      ],
      "type": "HumanName"
     }
    ]
   }
  }
 ],
 "query-sql": [
  "SELECT \"patient\".* FROM \"patient\" WHERE aidbox_text_search(knife_extract_text(\"patient\".resource, $JSON$[[\"name\",\"family\"],[\"name\",\"given\"],[\"name\",\"middle\"],[\"name\",\"text\"]]$JSON$)) ilike unaccent(?) LIMIT ? OFFSET ?",
  "% max%",
  100,
  0
 ],
 "query-time": 13,
 "entry": [
  {
   "resource": {
    "name": [
     {
      "given": [
       "Max",
       "Pain"
      ]
     }
    ],
    "gender": "male",
    "birthDate": "1991-01-02",
    "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-23T09:50:13.639Z",
     "versionId": "224",
     "tag": [
      {
       "system": "https://aidbox.io",
       "code": "updated"
      }
     ]
    }
   }
  }
 ],
 "total": 1,
 "link": [
  {
   "relation": "first",
   "url": "/Patient?name=max&_page=1"
  }
 ]
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Please check [Search API](../../api-1/fhir-api/search-1/) for more details.
{% endhint %}

### Delete Patient

Use `DELETE /Patient/<id>`to delete the patient resource.

{% tabs %}
{% tab title="Request" %}
```javascript
DELETE /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577?_pretty=true
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 200

{
 "name": [
  {
   "given": [
    "Max",
    "Pain"
   ]
  }
 ],
 "gender": "male",
 "birthDate": "1991-01-02",
 "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-23T09:54:16.979Z",
  "versionId": "225",
  "tag": [
   {
    "system": "https://aidbox.io",
    "code": "deleted"
   }
  ]
 }
}
```
{% endtab %}
{% endtabs %}

After successful deletion, the server sends the response with the status `200 OK` and the body containing the last version of the resource.

If we try to get a deleted patient`GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577` we will receive `resourceType - OperationOutcome` and `status 410`. 

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 410

{
 "resourceType": "OperationOutcome",
 "status": 410,
 "resource": {
  "name": [
   {
    "given": [
     "Max",
     "Pain"
    ]
   }
  ],
  "gender": "male",
  "birthDate": "1991-01-02",
  "id": "f8fe69db-c01c-4a3b-bf0c-0a806ea22577",
  "resourceType": "Patient",
  "meta": {
   "lastUpdated": "2018-10-23T09:54:16.979Z",
   "versionId": "225",
   "tag": [
    {
     "system": "https://aidbox.io",
     "code": "deleted"
    }
   ]
  }
 }
}
```
{% endtab %}
{% endtabs %}

