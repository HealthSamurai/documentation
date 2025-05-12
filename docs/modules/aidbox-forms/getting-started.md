---
description: This article outlines the basic steps to get started with Aidbox Forms
---

# Getting started

You can try the Aidbox Forms module, edit test forms, create new ones, see how the data will be saved to the database and extracted, for this you need to run [the Aidbox Forms in Aidbox Sandbox](getting-started.md#run-aidbox-forms-in-aidbox-sandbox). At the same time, you can run [the Aidbox Forms locally](getting-started.md#run-aidbox-forms-locally).

## Run Aidbox Forms in Aidbox Sandbox

### Create a license on Aidbox portal

* Sign up on [Aidbox portal](https://aidbox.app/ui/portal#/signin)
* Create a license: licence type - **production** or **development**, hosting - **in Cloud (for free)**

### Start playing with Aidbox Forms

* Go to [Aidbox portal](https://aidbox.app/ui/portal#/signin), find your license in the "Personal project licenses" list. Click on your new license and navigate to the "URL" link in the "Hosting" box.
* Press the button `Forms` in the Aidbox console in browser
* Play with forms

## Run Aidbox Forms locally

### Create a license on Aidbox portal

* Sign up on [Aidbox portal](https://aidbox.app/ui/portal#/signin)
* Create a license: licence type - **development**, hosting - **self-hosted**
* Copy and save a license key

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

3\. Clone [aidbox-zen-sdc](https://github.com/HealthSamurai/aidbox-zen-sdc) repository

4\. Configure project with a license. You need to create `.env file` in the root of repository (by copying from .env.tpl) and update AIDBOX\_LICENSE with the generated license key that you saved

5\. Run `make init` to initialize project

6\. Run in the terminal `make up` in the root of repository

7\. Open Aidbox console in browser [http://localhost:8080/ ](http://localhost:8080/), using login / password - admin / password

8\. Go to forms by pressing the button `Forms` (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )

9\. To finish development you can run `make down` to shutdown all services.

{% hint style="info" %}
You can edit zen files in `zrc` directory and they will be automatically reloaded
{% endhint %}

### Start form designing

See [Design form in Aidbox UI Builder](aidbox-ui-builder-alpha/)

## Enable Audit log

Aidbox Forms support [Audit logs](../security-and-access-control/audit/README.md).

To enable Audit logging with Aidbox project, edit the `zrc/sdc-box.edn` file:

Find the following code:

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :config   config
  :services {:seed-fixtures seed-fixtures
             :seed-questionnaires seed-questionnaires
             :sdc sdc-service}}

```

Add the following pair:

```
  :audit {:storage "AuditEvent"}
```

So the result is

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :config   config
  :audit {:storage "AuditEvent"}
  :services {:seed-fixtures seed-fixtures
             :seed-questionnaires seed-questionnaires
             :sdc sdc-service}}
```

Restart Aidbox. Aidbox will start recording audit logs and you can see them in the Audit log viewer or access via FHIR API.

Learn more about Audit logs:

{% content-ref url="../security-and-access-control/audit/" %}
[audit](../security-and-access-control/audit/README.md)
{% endcontent-ref %}

## Disable SDC operations

In case you have conflicts with your own implementation, you can easily disable SDC operations by setting environment variable:

```
AIDBOX_SDC_ENABLED=false
```
