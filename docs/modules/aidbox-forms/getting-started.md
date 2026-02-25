---
description: This article outlines the basic steps to get started with Aidbox Forms
---

# Getting started

You can try the Aidbox Forms module, edit test forms, create new ones, see how the data will be saved to the database and extracted, for this you need to run [the Aidbox Forms in Aidbox Sandbox](getting-started.md#run-aidbox-forms-in-aidbox-sandbox). At the same time, you can run [the Aidbox Forms locally](getting-started.md#run-aidbox-forms-locally).

You can also use the standalone Formbox product — see [Getting started with Formbox](getting-started-formbox.md).

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

1. Follow this [guide](../../getting-started/run-aidbox-locally.md) to start Aidbox locally


2. Press the button `Forms` in the Aidbox console in browser (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )

### Start form designing

See [Design form in Aidbox UI Builder](aidbox-ui-builder-alpha/)

## Enable Audit log

Aidbox Forms support [Audit logs](../../access-control/audit-and-logging.md).

To enable Audit logging follow this [guide](../../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md)

## Disable SDC operations

In case you have conflicts with your own implementation, you can easily disable SDC operations by setting environment variable:

```
AIDBOX_SDC_ENABLED=false
```
