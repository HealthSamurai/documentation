# Patient CRUD SPA

## Before

For this tutorial you will need git, node js and any version of Aidbox as a backend. We recommend use Aidbox.Dev as more easiest way. Visit to [Aidbox.Dev installation guide]() for detailed information and instruction how to install Aidbox backend for development.

## Introduction

This repository on GitHub [https://github.com/HealthSamurai/Aidbox-angular-sample](https://github.com/HealthSamurai/Aidbox-angular-sample)

In this guide we will locally launch FHIR application based on  [Angular framework](https://angular.io) and Aidbox.Dev as a backend for this SPA. This simple application will realize basic CRUD operations on patient list.

## Install

### Clone repository

```bash
 $ git clone https://github.com/HealthSamurai/aidbox-angular-sample.git
 $ cd aidbox-angular-sample
```

### Configure Base URL

Now you need configure your FHIR server base url.

If you use **Aidbox.Dev** as a backend, you need specify `AIDBOX_URL` as `http://localhost:8888` in `environment.ts` file.

{% code-tabs %}
{% code-tabs-item title="environment.ts" %}
```typescript
export const environment = {
  AIDBOX_URL : "http://localhost:8888"
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

When you want run this sample application with **Aidbox.Cloud** you need specify `AIDBOX_URL` as `https://<YOUR_BOX_NAME>.aidbox.app`

{% code-tabs %}
{% code-tabs-item title="environment.ts" %}
```typescript
export const environment = {
  AIDBOX_URL : "https://<YOUR_BOX_NAME>.aidbox.app"
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Installation and start

This sample application required minimal [Node JS version 8.9](https://nodejs.org/en/)

Install all packages and angular cli:

```bash
 $ npm install
 $ npm install -g @angular/cli
```

And launch application:

```bash
$ ng serve
```

After start - open your browser and go to [`http://localhost:4200`](http://localhost:4200) - we will see Angular simple app.

![](../.gitbook/assets/screenshot-2018-10-18-17.16.29%20%281%29.png)

It is a list of patients and information about them. Patient - it's a [FHIR resource](https://www.hl7.org/fhir/resourcelist.html). We can create, view, edit, and delete patient data — standard set of CRUD operations. Also in this demo, we have the ability to search by last/first name and paginated output of patients list.

