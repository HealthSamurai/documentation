---
description: Follow these steps to launch Aidbox locally using Docker
---

# Run Aidbox locally

## Prerequisites

{% hint style="warning" %}
<img src="../../.gitbook/assets/docker.png" alt="" data-size="original">

Please **make sure** that both [Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

## Steps

1. **Create a Directory**

### 1. Create a directory

```sh
mkdir aidbox && cd aidbox
```

### 2. **Run Aidbox on Docker**

```bash
curl -JO https://aidbox.app/runme && docker compose up
```

This command downloads the Aidbox script and starts Aidbox using Docker Compose.

### 3. Access Aidbox

Navigate to [http://localhost:8888/](http://localhost:8888/)

### 4. Activate your Aidbox instance

* Using AidboxID\
  AidboxID is a unique identifier within Aidbox ecosystem used for product activation
* Using Aidbox license\
  Aidbox license can be issued on the [Aidbox user portal](https://aidbox.app/). More about Aidbox licenses [here](../../overview/aidbox-user-portal/licenses.md).&#x20;

### 5. **Discover Aidbox features using Notebooks**

Use Getting Started Notebooks to explore basic Aidbox features&#x20;

<figure><img src="../../.gitbook/assets/notebooks.png" alt=""><figcaption></figcaption></figure>

{% hint style="info" %}
**Aidbox Notebooks**

Notebooks are interactive tutorials within the Aidbox UI with built-in REST, RPC, and SQL editors and the ability to execute requests and queries on the fly and see the result. You can use pre-built or create your own Notebooks. [Learn more.](../../overview/aidbox-ui/notebooks.md)
{% endhint %}

## Next Steps

* Learn more about [Aidbox Configuration](../../aidbox-configuration/aidbox-zen-lang-project/)
* Unlock additional capabilities of [Aidbox UI](../../overview/aidbox-ui/)
* Dive into the built-in [Aidbox Notebooks](../../overview/aidbox-ui/notebooks.md)
