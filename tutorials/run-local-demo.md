# Patient CRUD SPA

## Before

For this tutorial you will need git, node js and any version of [Aidbox](https://www.health-samurai.io/aidbox) as a backend. We recommend to use Aidbox.Dev as the most easiest way. Visit the [Aidbox.Dev installation guide](../installation/setup-aidbox.dev.md) for detailed information and instructions how to install Aidbox backend for development.

## Introduction

This repository on GitHub [https://github.com/HealthSamurai/Aidbox-angular-sample](https://github.com/HealthSamurai/Aidbox-angular-sample).

In this guide we will locally launch FHIR application based on [Angular framework](https://angular.io) and Aidbox.Dev as a backend for this SPA. This simple application will implement basic CRUD operations on patient list.

## Install

### Clone repository

```bash
 $ git clone https://github.com/HealthSamurai/aidbox-angular-sample.git
 $ cd aidbox-angular-sample
```

### Configure Base URL

Now you need to configure your FHIR server base URL.

If you are using **Aidbox.Dev** as a backend, you need to specify `AIDBOX_URL` as `http://localhost:8888` in the `environment.ts` file.

{% code-tabs %}
{% code-tabs-item title="environment.ts" %}
```typescript
export const environment = {
  AIDBOX_URL : "http://localhost:8888"
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

When you want to run this sample application with **Aidbox.Cloud,** you need to specify `AIDBOX_URL` as `https://<YOUR_BOX_NAME>.aidbox.app`.

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

This sample application requires minimum [Node JS version 8.9](https://nodejs.org/en/).

Install all packages and angular cli:

```bash
 $ npm install
 $ npm install -g @angular/cli
```

And launch application:

```bash
$ ng serve
```

After server starts, open your browser and go to [`http://localhost:4200`](http://localhost:4200) — you will see Angular simple app.

![](../.gitbook/assets/screenshot-2018-10-18-17.16.29%20%281%29.png)

It is a list of patients and information about them. Patient is a [FHIR resource](https://www.hl7.org/fhir/resourcelist.html). You can create, view, edit, and delete patient data — this is a standard set of CRUD operations. Also in this demo, we have the ability to search by last/first name and get a paginated output of patient list.

