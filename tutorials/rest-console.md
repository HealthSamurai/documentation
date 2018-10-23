---
description: In this guide we will learn how interact with REST console
---

# REST console

### REST console

Last time, we started at using our REST console. Let's see what can we do here.

![REST console](../.gitbook/assets/screenshot-2018-10-18-18.54.58.png)

REST console is designed to work with resources on your `Box` by sending HTTP requests in accordance with [FHIR RESTful API](http://hl7.org/fhir/http.html). To do this, we need to type - a HTTP verb \(`GET`, `POST`, `PUT`, `PATCH`, `DELETE`\) and the address of the resource \(for example `/Patient` - _pay attention to the resource name with a capital letter_\), in cases when you need to send the request body \(e.g -  `POST` request\), it passed in indented one line below, in YAML or JSON format.

### Create Patient

Last time \(in [Create and Configure Box](create-and-configure-box.md) tutorial\) we try to get a list of our patients \(by requesting them through the `GET /Patient`\) and response was empty. Let's add a couple of new patients -  for this we type in our console `POST /Patient` and in the body of the request wherein we will send the data of our new patient:

{% tabs %}
{% tab title="Request" %}
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

{% tab title="Response" %}
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
 /.../
 "id": "14dc3340-2ac1-4595-bbb7-3803b08c8f6f",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-18T16:32:53.038Z",
  "versionId": "5",
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

This is example, you can change values as you want, but better check full [Patient resource](https://www.hl7.org/fhir/patient.html) description and [official example](https://www.hl7.org/fhir/patient-example.json.html). The `id` field in the request body is not required, if you do not send it to the server, it will be generated. A description of the difference in `create` operation behavior between FHIR and Aidbox endpoints can be found [here](../basic-concepts/difference-between-aidbox-and-fhir.md).

![POST /Patient](../.gitbook/assets/screenshot-2018-10-18-19.41.22.png)

### Get Patient

After sending the request - we receive a response with `Status - 201` and the sent data - our patient is created. We can make sure of this by sending request  `GET /Patient/<id>` and receive created patient data \(in our case id is `14dc3340-2ac1-4595-bbb7-3803b08c8f6f`\),  or we can check a complete list of patients - `GET /Patient` 

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/14dc3340-2ac1-4595-bbb7-3803b08c8f6f
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
 "id": "14dc3340-2ac1-4595-bbb7-3803b08c8f6f",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-19T17:09:58.078Z",
  "versionId": "19",
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

Next step  - update our patient info. For partial update we can use `PATCH /Patient/<id>`,  in body we need send only changed data. For example - let's change our patient name.

{% tabs %}
{% tab title="Request" %}
```javascript
PATCH /Patient/example

{
  "name": [
    {
      "given": ["Maximilian"]
    }
  ]
}
```
{% endtab %}

{% tab title="Responce" %}
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
  {/...../}
 ],
 "telecom": [
  {/...../}
 ],
 "birthDate": "1990-10-10",
 "id": "14dc3340-2ac1-4595-bbb7-3803b08c8f6f",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-19T10:12:35.006Z",
  "versionId": "6",
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

### Update patient

For full resource update we use`PUT /Patient/<id>`

{% tabs %}
{% tab title="Request" %}
```javascript
PUT /Patient/example

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
 "id": "14dc3340-2ac1-4595-bbb7-3803b08c8f6f",
 "resourceType": "Patient",
 "meta": {
  "lastUpdated": "2018-10-19T17:18:37.976Z",
  "versionId": "22",
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
We completely update the data, what was there before but did not fall into the request body - will be deleted
{% endhint %}

### Patient history

We have ability to receive history of our patient changes, just need to send request like this -  `GET /Patient/<id>/_history` 

Let's do it for our patient

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/14dc3340-2ac1-4595-bbb7-3803b08c8f6f/_history
```
{% endtab %}

{% tab title="Responce" %}
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
       "Maximilian"
      ]
     }
    ],
    "gender": "male",
    "address": [
     {/.../}
    ],
    "telecom": [
     {/.../}
    ],
    "birthDate": "1990-10-10",
    "id": "14dc3340-2ac1-4595-bbb7-3803b08c8f6f",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-19T10:12:35.006Z",
     "versionId": "6",
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
     {/.../}
    ],
    "telecom": [
     {/.../}
    ],
    "birthDate": "1990-10-10",
    "id": "14dc3340-2ac1-4595-bbb7-3803b08c8f6f",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-18T16:32:53.038Z",
     "versionId": "5",
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
  /.../
 ]
}
```
{% endtab %}
{% endtabs %}

At the response we receive all versions \(in this case 3\) of our patient. First version - when resource created and second - with changed name and the third - full updated resource.

### Search Patient

Another interesting thing - we can find patient e.g by name - `GET /Patient?name=<Patient_name>`

{% hint style="info" %}
Before this step, it is better to create other patients for different search results. You already know how to do it ;\)
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
 ],
 /.../
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
    "id": "example",
    "resourceType": "Patient",
    "meta": {
     "lastUpdated": "2018-10-19T17:26:33.883Z",
     "versionId": "4",
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

And another standard operation - deletion - `DELETE /Patient/<id>`

{% tabs %}
{% tab title="Request" %}
```javascript
DELETE /Patient/example
```
{% endtab %}

{% tab title="Response" %}
```javascript
Status: 204
```
{% endtab %}
{% endtabs %}

Upon successful deletion we receive empty body and `status - 200`

If we try to get deleted patient  `GET /Patient/example` we will receive `resourceType - OperationOutcome` and `status 410`. 

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/example
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
  "id": "example",
  "resourceType": "Patient",
  "meta": {
   "lastUpdated": "2018-10-19T17:33:47.108Z",
   "versionId": "5",
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

Now we have learned how to search, create, receive, update, delete patients and get a history of their changes.

