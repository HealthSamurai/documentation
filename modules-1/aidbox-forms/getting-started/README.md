---
description: This article outlines the basic steps to get started with Aidbox Forms
---

# Getting started

You can try the Aidbox Forms module, edit test forms, create new ones, see how the data will be saved to the database and extracted, for this you need to run [the Aidbox Forms in Aidbox Sandbox](./#run-aidbox-forms-in-aidbox-sandbox).  But all your changes will not be saved. To develop forms, you need to run [the Aidbox Forms locally](./#run-aidbox-forms-locally).

## Run Aidbox Forms in Aidbox Sandbox

### Create a license on Aidbox portal

* Sign up on [Aidbox portal](https://aidbox.app/ui/portal#/signin)
* Create a license: product type - **Aidbox**, licence type - **standard** or **development**, hosting - **in Cloud (for free)**
* Configure your project: select Aidbox SDC Forms

### &#x20; Start playing with Aidbox Forms

* Go to [Aidbox portal](https://aidbox.app/ui/portal#/signin), find your license in the "Personal project licenses" list. Click on your new license and navigate to the "URL" link in the "Hosting" box.
* Press the button `Forms` in the Aidbox console in browser
* Play with forms

{% hint style="danger" %}
This is only a sandbox with test forms, all your changes are not saved
{% endhint %}

## Run Aidbox Forms locally

### Create a license on Aidbox portal

* Sign up on [Aidbox portal](https://aidbox.app/ui/portal#/signin)
* Create a license: product type - **Aidbox**, licence type - **development**, hosting - **self-hosted**
* Copy & save a license key

### Configure Aidbox project

1. You need to have the following software installed:

* docker
* docker-compose
* text editor with color highlighting (recommended VSCode editor with `zen-lsp` plugin)
* any modern web-browser
* git
* make
* npm

2\. Run Aidbox locally

{% content-ref url="../../../getting-started/run-aidbox-locally-with-docker/" %}
[run-aidbox-locally-with-docker](../../../getting-started/run-aidbox-locally-with-docker/)
{% endcontent-ref %}

3\. Clone [aidbox-zen-sdc](https://github.com/HealthSamurai/aidbox-zen-sdc) repository&#x20;

4\. Configure project with a license. You need to create `.env file` in the root of repository (by copying from .env.tpl) and update AIDBOX\_LICENSE with the generated license key that you saved

5\. Run `make init` to initialize project

6\. Run in the terminal `make up` in the root of repository

7\. Open Aidbox console in browser [http://localhost:8080/ ](http://localhost:8080/), using login / password  - admin / password

8\. Go to forms by pressing the button `Forms` (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )

9\. To finish development you can run `make down` to shutdown all services.

{% hint style="info" %}
You can edit zen files in `zrc` directory and they will be automatically reloaded
{% endhint %}

### Start form designing

You have two options how to design form

* [Design form locally (in your familiar editor)](design-form-locally-in-familiar-editor.md)
* [Design form in Aidbox Form IDE (in browser)](design-form-in-aidbox-form-ide.md)

