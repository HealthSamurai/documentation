# Working with REST Console

### REST Console

In our previous article, we started working in our REST Console. Let's see what we can do here.

![REST Console](../.gitbook/assets/screenshot-2018-10-18-18.54.58.png)

REST Console is designed to work with resources in your `Box` by sending HTTP requests in accordance with [FHIR RESTful API](http://hl7.org/fhir/http.html). To do this, we need to type an HTTP verb \(`GET`, `POST`, `PUT`, `PATCH`, `DELETE`\) and the resource address \(for example `/Patient` — _please pay attention to the resource name with a capital letter_\).

In cases when you need to send a request body \(e.g., `POST` requests\), the request body content is passed below the resource address, separated by an empty line, in YAML or JSON format — you can choose both request and response content type by **YAML/JSON** switch.

### Create Patient

Last time \(in [Create and Configure Box]() tutorial\) we tried to get a list of our patients \(by requesting them through the `GET /Patient`\), and the response was empty. Let's add a couple of new patients. To do that, we type  `POST /Patient` in our console and the body of the request, wherein we send the data of our new patient:

{% tabs %}
{% tab title="Request YAML" %}
```yaml
POST /Patient

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
POST /Patient

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

```
{% endtab %}

{% tab title="Response JSON" %}
```javascript
Status: 201

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

This is only an example, you can change the values as you want but it would be good to check the full [Patient resource](https://www.hl7.org/fhir/patient.html) description and [official example](https://www.hl7.org/fhir/patient-example.json.html). The `id` field in the request body is not required: if you do not send it to a server, it will be generated. Description of the difference in `create` operation behavior between FHIR and [Aidbox](https://www.health-samurai.io/aidbox) endpoints can be found [here]().

![POST /Patient](../.gitbook/assets/screenshot-2018-10-18-19.41.22.png)

### Get Patient

After sending the request, we receive a response with `Status - 201` and the sent data, which means that our patient has been created. We can check this by sending the request  `GET /Patient/<id>` and receiving created patient data \(in our case the `id` is `f8fe69db-c01c-4a3b-bf0c-0a806ea22577`\), or we can check a complete list of patients — `GET /Patient` 

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577
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

Next step is to update our patient information. In a partial update, we can use `PATCH /Patient/<id>` in the request body in order to send changed data only. For example, let's change our patient name.

{% tabs %}
{% tab title="Request" %}
```javascript
PATCH /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577

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

For a full resource update we use`PUT /Patient/<id>`

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

We can receive history of our patient changes, we just need to send a request like this —  `GET /Patient/<id>/_history`.

Let's try this for our patient.

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577/_history
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

In the response, we receive all versions \(in this case 3\) of our patient resource. The first version is when the resource was created, the second one is with the changed name, and the third is an entirely updated resource. 

And now we can do the operation called [vread](http://hl7.org/fhir/http.html#vread) to get a specific version of a resource with the following request`GET /Patient/<id>/_history/<versionId>`

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

Another interesting thing is that we can find a patient by some criteria, e.g. by name — `GET /Patient?name=<Patient_name>`

{% hint style="info" %}
Before this step, it is recommended to create other patients for different search results. You already know how to do it ;\)
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient?name=max
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

Great! Now we can find patients.

### Delete Patient

And another standard operation — deletion — `DELETE /Patient/<id>`

{% tabs %}
{% tab title="Request" %}
```javascript
DELETE /Patient/f8fe69db-c01c-4a3b-bf0c-0a806ea22577
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

After successful deletion we receive last version of resource and `status - 200`

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

Now we have learned how to search, create, receive, update, and delete patients, and get a history of their changes.

