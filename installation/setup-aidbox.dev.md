---
description: Getting started with Aidbox.Dev.
---

# Setup Aidbox.Dev

### Requirements

* [docker](https://docs.docker.com/install/)
* [docker-compose](https://docs.docker.com/compose/install/)

### License obtaining

1. Visit and register on [License server](https://license-ui.aidbox.app) then click the `Get a license` button.

![](../.gitbook/assets/scr-2018-10-08_17-53-52.png)

2. Enter a short description of your application and select the `Aidbox.Dev` product.

![](../.gitbook/assets/screen-shot-2018-10-02-at-17.28.09.png)

3. Congratulations, now you have a license key.

![](../.gitbook/assets/screen-shot-2018-10-02-at-17.34.31.png)

### Start Aidbox.Dev

Clone our official documentation repository with sample applications and installation instructions.

```text
$ git clone https://github.com/Aidbox/devbox.git
```

Go to the cloned directory.

```text
$ cd devbox
```

Open the `license.env` file and insert your `License ID` and `License KEY` . Or download and move your `license.env` file to the devbox root folder.

{% code-tabs %}
{% code-tabs-item title="license.env" %}
```text
DEVBOX_LICENSE_ID=856421ad5e57430
DEVBOX_LICENSE_KEY=35b0bb5b403a4bfdbc86d3ce23a5a75a
```
{% endcode-tabs-item %}
{% endcode-tabs %}

After that, run Aidbox.Dev.

```bash
$ docker-compose up
```

That's it! Aidbox.Dev is running and you can point your browser to [http://localhost:8888/](http://localhost:8888/) to see a fancy welcome page.

### Getting Started

Learn how to obtain access to the [REST Api](../tutorials/how-to-use-rest-api.md) by link below

{% page-ref page="../tutorials/how-to-use-rest-api.md" %}



