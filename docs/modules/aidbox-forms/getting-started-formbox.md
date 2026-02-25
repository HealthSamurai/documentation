---
description: Follow these steps to launch Formbox locally using Docker
---

# Getting started with Formbox

## Prerequisites

{% hint style="warning" %}
<img src="../../.gitbook/assets/docker.png" alt="Docker logo" data-size="original">

Please **make sure** that both [Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

## Steps

### 1. Create a directory

```sh
mkdir formbox && cd formbox
```

### 2. Run Formbox on Docker

```bash
curl -JO https://aidbox.app/runme/formbox && docker compose up
```

This command downloads the Formbox script and starts Formbox using Docker Compose.

### 3. Access Formbox

Open in browser [http://localhost:8080/](http://localhost:8080)

### 4. Activate your Formbox instance

Click "Continue with Aidbox account" and create a free Aidbox account in [Aidbox user portal](https://aidbox.app/).

More about Aidbox licenses [here](../../overview/aidbox-user-portal/licenses.md)

### 5. Start form designing

Press the button `Forms` in the console in browser (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc))

See [Design form in Aidbox UI Builder](aidbox-ui-builder-alpha/)
