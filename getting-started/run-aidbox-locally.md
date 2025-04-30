---
description: Follow these steps to launch Aidbox locally using Docker
---

# Run Aidbox locally

## Prerequisites

{% hint style="warning" %}
<img src="../.gitbook/assets/docker.png" alt="" data-size="original">

Please **make sure** that both [Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

## Steps

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

Open in browser [http://localhost:8080/](http://localhost:8080)

### 4. Activate your Aidbox instance

<figure><img src="../.gitbook/assets/image (163).png" alt=""><figcaption></figcaption></figure>

* Using AidboxID\
  AidboxID is a unique identifier within the Aidbox ecosystem used for product activation
* Using Aidbox license\
  Aidbox license can be issued on the [Aidbox user portal](https://aidbox.app/). More about Aidbox licenses [here](../overview/aidbox-user-portal/licenses.md).&#x20;

{% hint style="info" %}
If you’re using an Aidbox license, you’ll be prompted for a username (or email) and password. Use the **admin** username and retrieve the password from the **BOX\_ADMIN\_PASSWORD** environment variable in the `docker-compose.yaml` file.\
\
For security, Aidbox generates a unique password for each instance.
{% endhint %}

### 5. **Discover Aidbox features using Notebooks**

Use Getting Started Notebooks to explore basic Aidbox features&#x20;

<figure><img src="../.gitbook/assets/Screenshot 2024-05-30 at 20.23.23.png" alt="Aidbox UI: notebooks"><figcaption></figcaption></figure>

{% hint style="info" %}
**Aidbox Notebooks**

Notebooks are interactive tutorials within the Aidbox UI with built-in REST, RPC, and SQL editors and the ability to execute requests and queries on the fly and see the result. You can use pre-built or create your own Notebooks. [Learn more.](../overview/aidbox-ui/notebooks.md)
{% endhint %}

## Next Steps

* Learn more about [Aidbox Configuration](../deprecated/deprecated/zen-related/aidbox-zen-lang-project/)
* Unlock additional capabilities of [Aidbox UI](../overview/aidbox-ui/)
* Dive into the built-in [Aidbox Notebooks](../overview/aidbox-ui/notebooks.md)
