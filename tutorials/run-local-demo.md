# Patient CRUD SPA

## Introduction

In this guide we will  locally launch FHIR application based on Aidbox.Dev.

## Installation and run

Our application consists of two parts - backend and frontend.

On backend part we will use [Aidbox for developers](../devbox/), and for frontend we have demo application made with Angular and Typescript. 

{% hint style="success" %}
[Devbox installation guide](../installation/)
{% endhint %}

 After successful install and launch let's go to directory with our demo:

{% hint style="warning" %}
We will need an installed node version above 8
{% endhint %}

Install dependencies:

```bash
$ npm install
$ npm install -g @angular/cli
```

Now you need configure your FHIR server base url. This setting located in `environment.ts` file. If you run Aidbox.Dev on default port `8888`, you can skep this step.

{% code-tabs %}
{% code-tabs-item title="environment.ts" %}
```typescript
export const environment = {
  AIDBOX_URL : "http://localhost:8888"
}

```
{% endcode-tabs-item %}
{% endcode-tabs %}

And launch frontend:

```bash
$ ng server
```

After start - open your browser and go to [`http://localhost:4200`](http://localhost:4200) - we will see our simple app.

![](../.gitbook/assets/screen-shot-2018-10-15-at-16.02.07.png)

It is a list of patients and information about them. Patient - it's a [FHIR resource](https://www.hl7.org/fhir/resourcelist.html).  We can create, view, edit, and delete patient data — standard set of CRUD operations. Also in this demo, we have the ability to search by last/first name and paginated output of patients list.

### Our task

Now let's try to create new Patient.

Press `New patient` and fill fields with information about resource. Note - fields marked with `*` - are required. Fill out our first patient card, you can add multiple phones and emails. After you're done press `Submit` and voila! We create our first resource - patient, it shows at list at left.

Try creating some more patients yourself, also don't forget try to update and delete them ;\)

This is not full list opportunities of our backend service. Devbox allows you to use itself as a ready backend for your application. Based on it, you can create web, mobile applications, services and much more \(see our folder with examples\).

