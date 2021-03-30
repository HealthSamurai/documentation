# Getting Started with Box

In this article, you will learn about the FHIR standard essentials, including:

* What is FHIR 
* How to create an instance of FHIR server
* Basics of FHIR RESTful API
* How to make secure requests to a FHIR server

## Introduction

#### Basic Terms

* [FHIR](https://www.hl7.org/fhir/index.html) is a platform specification that defines a set of [entities](https://www.hl7.org/fhir/resourcelist.html) and [operations](https://www.hl7.org/fhir/http.html) on those entities for interoperability between healthcare systems and applications
* [FHIR server](https://aidbox.app) is a web application implementing FHIR specification and providing RESTful API
* Box is an instance of a FHIR server provided by any Aidbox product

#### Guide Assumptions

In this guide, we will be using Aidbox.Cloud for simplicity of the Box creation, however any other Aidbox product will also work.

This guide assumes that you will set proper values instead of placeholders like this: `<YOUR-BOX>`

## Create a Box

Aidbox.Cloud is a sandbox version of Aidbox in HealthSamurai cloud. Each Box is a private instance of a FHIR server with a separate database and domain. You can create multiple boxes for testing and evaluation.

Please, pay attention, that the sandbox environment doesn't fit for any kind of production usage. The service availability and data integrity are not guaranteed. Do not put any PHI data there.

To try out Aidbox.Cloud, open [https://aidbox.app/](https://aidbox.app/)

Choose how you would like to authorize Aidbox. It can be done via your Github or Google account.

![](../.gitbook/assets/signup.-aidboxcloud.png)

If you chose Github authorization, click the 'Authorize HealthSamurai' button.

![](../.gitbook/assets/scr-2018-10-11_10-50-33.png)

Github will ask you to confirm your password to continue.

![](../.gitbook/assets/scr-2018-10-11_10-51-32.png)

And now you are successfully authorized in Aidbox.Cloud. Click the 'New Box' button to start.

![](../.gitbook/assets/scr-2018-10-11_10-51-55.png)

In the displayed form, enter your future box name. It can be a name of your application you are going to build. It will be the base URL of your FHIR server.

![](../.gitbook/assets/create-box.png)

Choose the desired FHIR version, and click the 'Create' button.

![](../.gitbook/assets/choose-fhir-version.png)

Your new box will be successfully created. Click the box name to proceed.

![](../.gitbook/assets/scr-2018-10-11_10-54-04.png)



### Check the CRUD

Now go to the `REST Console` section and let's see what we can do here.

![REST console](../.gitbook/assets/screenshot-2018-10-18-18.54.58.png)

REST console is designed to work with resources on your `Box` by sending HTTP requests in accordance with [FHIR RESTful API](http://hl7.org/fhir/http.html). To do this, we need to type an HTTP verb \(`GET`, `POST`, `PUT`, `PATCH`, `DELETE`\) and the address of the resource \(for example `/Patient` — _please pay attention to the resource name written with a capital letter_\). In cases when you need to send the request body \(e.g for a `POST` request\), it is passed separated by empty line, in YAML or JSON format — you can choose both \(request and response\) content type by **YAML** \| JSON switcher.

### Create a Patient

Let's add a couple of new patients. For this, we type in our console `POST /Patient` and in the body of the request wherein we will send the data of our new patient \(Aidbox supports JSON and a few other formats but we will use YAML for [compactness and readability]()\):

{% hint style="danger" %}
Use the copy button in the top right corner of a code snippet to avoid copying unnecessary white space characters.
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

resourceType: Patient
name:
- given: [Max]
  family: Turikov
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

{% tab title="Response" %}
```yaml
Status: 201

name:
- given: [Max]
gender: male
address:
- city: Hello
  line: [123 Oxygen St]
  state: NY
  district: World
  postalCode: '3212'
telecom:
- {use: home}
- {use: work, rank: 1, value: (32) 8934 1234, system: phone}
birthDate: '1990-10-10'
id: 957d782d-3e40-4978-968c-63a1ef7d2473
resourceType: Patient
meta:
  lastUpdated: '2018-10-29T09:09:16.604Z'
  versionId: '118'
  tag:
  - {system: 'https://aidbox.io', code: created}
```
{% endtab %}
{% endtabs %}

This is only an example, and you can change values as you want. For more information, check the full [Patient resource](https://www.hl7.org/fhir/patient.html) description and [official example](https://www.hl7.org/fhir/patient-example.json.html). The `id` field in the request body is not required, and if you do not send it to the server, it will be generated. A description of the difference in the `create` operation behavior between FHIR and Aidbox endpoints can be found [here](https://docs.aidbox.app/basic-concepts/aidbox-and-fhir-formats).

![](../.gitbook/assets/2018-10-29-121415_1311x754_scrot.png)

### Get a Patient

After sending the request, we receive a response with `Status: 201` and the sent data, which means that our patient has been created. We can check this by sending the request `GET /Patient/<id>` and receive created patient data \(in our case id is `957d782d-3e40-4978-968c-63a1ef7d2473`, we got the id from the response\), or we can check a complete list of patients — `GET /Patient`

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Patient/957d782d-3e40-4978-968c-63a1ef7d2473
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 200

name:
- given: [Max]
gender: male
address:
- city: Hello
  line: [123 Oxygen St]
  state: NY
  district: World
  postalCode: '3212'
telecom:
- {use: home}
- {use: work, rank: 1, value: (32) 8934 1234, system: phone}
birthDate: '1990-10-10'
id: 957d782d-3e40-4978-968c-63a1ef7d2473
resourceType: Patient
meta:
  lastUpdated: '2018-10-29T09:09:16.604Z'
  versionId: '118'
  tag:
  - {system: 'https://aidbox.io', code: created}
```
{% endtab %}
{% endtabs %}

There are many more operations you can execute using RESTful API but it’s enough to use POST and GET requests to check if everything is set up right.

What's next?

{% page-ref page="rest-console.md" %}

{% page-ref page="run-local-demo.md" %}

{% hint style="info" %}
Want to know more about [Aidbox](https://www.health-samurai.io/aidbox), FHIR or FHIR applications? Join our community [chat](https://t.me/aidbox).
{% endhint %}

