---
description: In this tutorial we will show you how to use REST Api with Aidbox.Dev
---

# How to use REST Api in Aidbox.dev

## Basic Authorization

After installation `Aidbox.Dev` create one default [`Client`](../oauth-2.0/users-and-clients.md#client) resource. This resource used in OAuth 2.0 functionality. Also this resource may be used for `basic authorization`. In this tutorial we will use this [`Client`](../oauth-2.0/users-and-clients.md#client) for basic authorization in to Aidbox.Dev .

Default `Client` will create  with `password` described in `DEVBOX_PASSWORD` environment variable sent to the container in the `docker-compose.yaml` . If you do not explicitly specify `DEVBOX_PASSWORD` by default if was set as `secret`. We are strongly recommending to change default password in your installation. [`Client.id`](../oauth-2.0/users-and-clients.md#client) of this client is constantly `root`.

{% code-tabs %}
{% code-tabs-item title="docker-compose.yaml" %}
```yaml
version: '3.1'
services:
  devbox:
    environment:
      DEVBOX_PASSOWRD: "${DEVBOX_PASSOWRD:-secret}"
...
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Authorization header

By [The 'Basic' HTTP Authentication Scheme](https://tools.ietf.org/html/rfc7617)[ ](https://tools.ietf.org/html/rfc7617)specification you need transfer `Authorization` header with request.

#### Bash

Build your authrozation token by getting base64 encode from "`root`:`secret`" string.

```bash
$ echo -n "root:sercret" | base64
# cm9vdDpzZWNyZXQ=
```

Now you can request Patient list with `Aiuthorization` header equal "`Basic cm9vdDpzZWNyZXQ=`"

```bash
$ curl -H "Authorization: Basic cm9vdDpzZWNyZXQ=" http://localhost:8888/Patient
```

Another way is directly specify username and password in [`curl`](https://curl.haxx.se)

```bash
$ curl -u root:secret http://localhost:8888/Patient
```

#### Postman

Open `Authorization` tab and select `Basic Auth` in authorization `TYPE` dropdown. After that type you Client.id in to `Username` input, and Client password in to `Password` input. In our case it wold be `root` and `secret`.

![Username and Password](../.gitbook/assets/screen-shot-2018-10-22-at-12.36.39.png)

Postman automatically added `Authorization` header with token.

![Automatic build Authorization header](../.gitbook/assets/screen-shot-2018-10-22-at-12.36.52.png)

### Play with Patient

Now you have full REST Api access to the Aidbox.Dev. For example let's play with `Patient` resource.

**Create test Patient**

{% tabs %}
{% tab title="Request" %}
```bash
$ curl -X POST  http://localhost:8888/Patient \
  -u root:secret \
  -H 'Content-Type: application/json' \
  -d '{"name": [{"given": ["Test"], "family": "Patient"}]}'
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201
{
    "resourceType": "Patient",
    "name": [
        {
            "given": [
                "Test"
            ],
            "family": "Patient"
        }
    ]
    ...    
    }
}
```
{% endtab %}
{% endtabs %}



## What next?

Learn how to start up with simple Single Page Application with Patient list and Patient CRUD in our tutorial.

{% page-ref page="run-local-demo.md" %}









