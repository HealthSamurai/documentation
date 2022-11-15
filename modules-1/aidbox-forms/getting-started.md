---
description: This article outlines the basic steps to get started with Aidbox Forms
---

# Getting started

## Create a license on Aidbox portal

* Sign up on [Aidbox portal](https://aidbox.app/ui/portal#/signin)
* Create a license: product type - **Aidbox**, licence type - **development**, hosting - **self-hosted**
* Copy & save a license key

## Configure Aidbox project

1. You need to have the following software installed:

* docker
* docker-compose
* text editor with color highlighting \*> Recommended VSCode editor with `zen-lsp` plugin
* any modern web-browser
* git
* make
* npm

2\. Run Aidbox locally

{% content-ref url="../../getting-started/run-aidbox-locally-with-docker/" %}
[run-aidbox-locally-with-docker](../../getting-started/run-aidbox-locally-with-docker/)
{% endcontent-ref %}

Run Aidbox in Aidbox sandbox

{% content-ref url="../../getting-started/run-aidbox-in-aidbox-sandbox.md" %}
[run-aidbox-in-aidbox-sandbox.md](../../getting-started/run-aidbox-in-aidbox-sandbox.md)
{% endcontent-ref %}

3\. Clone [aidbox-zen-sdc](https://github.com/HealthSamurai/aidbox-zen-sdc) repository&#x20;

4\. Configure project with a license. You need to create `.env file` in the root of repository (by copying from .env.tpl) and update AIDBOX\_LICENSE with the generated license key that you saved

5\. Run `make init` to initialize project

6\. Run in the terminal `make up` in the root of repository

7\. Open Aidbox console in browser [http://localhost:8080/ ](http://localhost:8080/), using login / passwod  - admin / admin

8\. Go to forms by pressing the button `Forms` (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )

9\. To finish development you can run `make down` to shutdown all services.

{% hint style="info" %}
You can edit zen files in `zrc` directory and they will be automatically reloaded
{% endhint %}

## Start form designing

