# Getting Started with SPA

After reading this guide, you will know:

* What is FHIR and SPA
* How to create an instance of FHIR server
* Create a new SPA, and connect your application to a FHIR server
* Basics of FHIR RESTful API
* How to make secure requests to FHIR server
* How to quickly generate the starting pieces of a FHIR SPA application

## Introduction

#### Basic terms

* [FHIR](https://www.hl7.org/fhir/index.html) is a platform specification that defines a set of [entities](https://www.hl7.org/fhir/resourcelist.html) and [operations](https://www.hl7.org/fhir/http.html) on those entities for interoperability between healthcare systems and applications
* [FHIR server](https://aidbox.app) is a web application implementing FHIR specification and providing RESTful API
* [SPA](https://en.wikipedia.org/wiki/Single-page_application) is a single-page application, which runs in user web browser, in our case it will be an [Angular](https://angular.io/) based project
* Box is an instance of FHIR server provided by any Aidbox product

#### Guide assumptions

This guide assumes that you already have installed git, npm, postman and have terminal application.

In this guide we will be using Aidbox.Cloud for simplicity of Box creation, but any other Aidbox product will also work.

You will set proper values instead of placeholders like this: `<YOUR-BOX>`

## Create a box

Choose how you would like to authorize [Aidbox](https://ui.aidbox.app). It can be done via your Github or Google account.

![](../.gitbook/assets/scr-2018-10-11_10-49-57.png)

After you have been successfully authorized in [Aidbox.Cloud](https://ui.aidbox.app), click the `New Box` button to start.

![](../.gitbook/assets/scr-2018-10-11_10-51-55%20%281%29.png)

In the displayed form, enter your future box name. It can be a name of your application you are going to build, choose your plan and click `Create` button.

![](../.gitbook/assets/2018-10-25-131455_633x702_scrot.png)

Your new box was successfully created. Click the box name to proceed.

![](../.gitbook/assets/scr-2018-10-11_10-54-04%20%281%29.png)

## Check the CRUD

Now go to `REST console` section and let's see what can we do here.

![REST console](../.gitbook/assets/screenshot-2018-10-18-18.54.58.png)

REST console is designed to work with resources on your `Box` by sending HTTP requests in accordance with [FHIR RESTful API](http://hl7.org/fhir/http.html). To do this, we need to type - a HTTP verb \(`GET`, `POST`, `PUT`, `PATCH`, `DELETE`\) and the address of the resource \(for example `/Patient` - _pay attention to the resource name with a capital letter_\), in cases when you need to send the request body \(e.g -  `POST` request\), it passed separated by empty line, in YAML or JSON format - you can choose both \(request and response\) content type by **YAML** \| JSON switcher.

### Create Patient

Let's add a couple of new patients -  for this we type in our console `POST /Patient` and in the body of the request wherein we will send the data of our new patient:

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

This is example, you can change values as you want, but better check full [Patient resource](https://www.hl7.org/fhir/patient.html) description and [official example](https://www.hl7.org/fhir/patient-example.json.html). The `id` field in the request body is not required, if you do not send it to the server, it will be generated. A description of the difference in `create` operation behavior between FHIR and Aidbox endpoints can be found [here](../basic-concepts/aidbox-vs-fhir.md).

![POST /Patient](../.gitbook/assets/screenshot-2018-10-18-19.41.22.png)

### Get Patient

After sending the request - we receive a response with `Status: 201` and the sent data - our patient is created. We can make sure of this by sending request  `GET /Patient/<id>` and receive created patient data \(in our case id is `f8fe69db-c01c-4a3b-bf0c-0a806ea22577`\),  or we can check a complete list of patients - `GET /Patient` 

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

There are much more operations that can be done with server using [RESTful API](../api/), but for our case to check if everything set up properly and to get basic understanding of FHIR RESTful API  `POST` and `GET` requests are enough.

## Give access to external clients

Aidbox products support [OAuth2.0](../oauth-2.0/) authorization framework to provide ability for developers to create applications, which can interact securely with Boxes \(Aidbox FHIR server instances\). For single-page application it's a common practice to use OAuth2.0 [Implicit Grant flow](../oauth-2.0/implicit.md).

To implement this flow we need to create 3 entities:

* **User** - the person, who will login and use application
* **Client** - our single-page application, which will interact with FHIR server
* **AccessPolicy** - set of rules, which describes, who and how can access FHIR server

We will create all three entities with one request \(don't forget to change admin password\):

{% tabs %}
{% tab title="Request" %}
```yaml
POST /

type: transaction
entry:
- resource:
    id: SPA
    redirect_uri: http://localhost:4200
  request:
    method: POST
    url: "/Client"
    
- resource:
    id: admin
    email: admin@mail.com # Change this value
    password: password # Change this value
  request:
    method: POST
    url: "/User"
    
- resource:
    engine: json-schema
    schema:
      type: object
      required:
      - user
  request:
    method: POST
    url: "/AccessPolicy"
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

id: '15'
type: transaction-response
resourceType: Bundle
entry:
- resource:
    secret: null
    redirect_uri: http://localhost:4200
    id: SPA
    resourceType: Client
    meta:
      lastUpdated: '2018-10-26T10:06:47.614Z'
      versionId: '15'
      tag:
      - {system: 'https://aidbox.io', code: created}
  status: 201
- resource:
    email: admin@mail.com
    password: $s0$f0801$Pc1z+JwonWUqNdpiIbHmkw==$V90luPjihU+QT38nXE26SvFMm+x1EnMrb+NMUAlli/w=
    id: admin
    resourceType: User
    meta:
      lastUpdated: '2018-10-26T10:06:47.614Z'
      versionId: '15'
      tag:
      - {system: 'https://aidbox.io', code: created}
  status: 201
- resource:
    engine: json-schema
    schema:
      type: object
      required: [user]
    id: all-registered-users
    resourceType: AccessPolicy
    meta:
      lastUpdated: '2018-10-26T10:06:47.614Z'
      versionId: '15'
      tag:
      - {system: 'https://aidbox.io', code: created}
  status: 201
```
{% endtab %}
{% endtabs %}

We created a Client resource with redirect uri equal to our SPA address, admin User with password `password` and AccessPolicy, which tells to authorize any registered user.

Now we request a token from our box using OAuth2.0 implicit grant flow.

Change &lt;your-box&gt; to name of your box and open following url in your browser.

`https://<your-box>.aidbox.app/oauth2/authorize?response_type=token&client_id=SPA&redirect_uri=http://localhost:4200/`

![box10 is a box name in my case](../.gitbook/assets/2018-10-26-131804_1164x709_scrot.png)

Enter email and password of the User, click 'Sign In' and you will be redirected to localhost:4200 \(redirect\_uri of SPA client\).

![](../.gitbook/assets/2018-10-26-132119_874x590_scrot.png)

Copy access\_token value, we will use it to obtain Patient resource with external http client.

Open [Postman](https://www.getpostman.com/) or any other http client, create new `GET` request, enter following url: `https://<your-box>.aidbox.app/Patient` and add `Authorization` header with value equal `Bearer <you-access-token-here>`.

![](../.gitbook/assets/2018-10-26-134351_1179x664_scrot.png)

  
You should get a bundle with Patient resources. Yay! It seems working

## Create FHIR SPA

On the final step we will configure and start our SPA. Make sure that you have [git](https://git-scm.com/downloads) and [npm](https://www.npmjs.com/get-npm) installed.

```bash
git clone https://github.com/HealthSamurai/aidbox-angular-sample.git
cd aidbox-angular-sample
vim environment.ts # or use any other editor of your choice
# set proper url to your box
npm install
npm install -g @angular/cli
ng serve
```

Open [http://localhost:4200](http://localhost:4200)

It works! You are awesome!

