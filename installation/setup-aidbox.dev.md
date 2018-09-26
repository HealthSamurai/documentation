# Setup aidbox.dev

### Requirements

* [docker](https://docs.docker.com/install/)
* [docker-compose](https://docs.docker.com/compose/install/)

### License obtaining

1\) Visit and register on [License server ](https://license-ui.aidbox.app), and press "Get a license"

![](../.gitbook/assets/screen-shot-2018-09-26-at-19.01.45.png)

2\) Enter short description of license key and choice "DevBox"  

![](../.gitbook/assets/screen-shot-2018-09-26-at-19.02.33.png)

3\) Congratulation, now you have license key

![](../.gitbook/assets/screen-shot-2018-09-26-at-19.02.48.png)

### 

### Start DevBox

Clone our official documentation repository with applications samples and installation instructions

```text
$ git clone https://github.com/Aidbox/devbox.git
```

Insert into `license.env` file your `License ID` and `License KEY`

{% code-tabs %}
{% code-tabs-item title="license.env" %}
```text
DEVBOX_LICENSE_ID=3fea917d53df476
DEVBOX_LICENSE_KEY=1e53e71ed0e2430c8807fd9b85751894
```
{% endcode-tabs-item %}
{% endcode-tabs %}

.......

