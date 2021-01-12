# REST API Access in Aidbox.Dev

## Aidbox.Dev Access 

By default, `Aidbox.Dev` is distributed with public access. This is made to simplify the use of `Aidbox.dev` for the first time. But the other side of this means than anyone who knows the address of your service can get full access to your REST API. We strongly recommend that you do not keep public access in production mode.

If you don't want to restrict an access to your Aidbox.Dev instance and wish to keep it public, you can skip this step and go to the [Patient CRUD SPA](run-local-demo.md).

{% page-ref page="run-local-demo.md" %}

### Restrict Access

If you want to enable Access Policy in your installation, you need to setup `AIDBOX_CLIENT_SECRET` environment variable in the `Aidbox.Dev` container. You can do this by uncommenting the appropriate line in the `docker-compose.yaml` file.

{% code title="docker-compose.yaml" %}
```yaml
version: '3.1'
services:

  devbox:
    ......
    environment:
      # AIDBOX_CLIENT_SECRET: "${AIDBOX_CLIENT_SECRET:-secret}"  # < Uncomment this string
    ......
```
{% endcode %}

After installation of `Aidbox.Dev` you get one default [`Client`]() resource. This resource is used in OAuth 2.0 functionality. Also, this resource may be used for `Basic Authorization`. In this tutorial, we will use this [`Client`]() for basic authorization in Aidbox.Dev.

Default `Client` is created with `password` described in `DEVBOX_PASSWORD` environment variable sent to the container in the `docker-compose.yaml` . If you did not explicitly specify `DEVBOX_PASSWORD` , it is set to `secret` by default. We strongly recommend to change the default password in your installation. [`Client.id`]() of this client is always `root`.

{% code title="docker-compose.yaml" %}
```yaml
version: '3.1'
services:
  devbox:
    ......
    environment:
      AIDBOX_CLIENT_SECRET: "${AIDBOX_CLIENT_SECRET:-secret}"
    ......
```
{% endcode %}

## Basic Authorization

### Authorization header

According to [The 'Basic' HTTP Authentication Scheme](https://tools.ietf.org/html/rfc7617)[ ](https://tools.ietf.org/html/rfc7617)specification you need to transfer `Authorization` header with the request. 

#### Bash

Build your authorization token by getting base64 encode from "`root`:`secret`" string.

```bash
$ echo -n "root:sercret" | base64
# cm9vdDpzZWNyZXQ=
```

Now you can request the Patient list with `Authorization` header equal -   
"`Basic cm9vdDpzZWNyZXQ=`"

```bash
$ curl -H "Authorization: Basic cm9vdDpzZWNyZXQ=" http://localhost:8888/Patient
```

Another way is to specify username and password in [`curl`](https://curl.haxx.se)  directly.

```bash
$ curl -u root:secret http://localhost:8888/Patient
```

#### Postman

Open the `Authorization` tab and select `Basic Auth` in `TYPE` dropdown. After that, type your Client.id into `Username` input and Client password into `Password` input. In our case, it would be `root` and `secret`.

![Username and Password](../.gitbook/assets/screen-shot-2018-10-22-at-12.36.39.png)

Postman automatically added `Authorization` header with token.

![Automatic build Authorization header](../.gitbook/assets/screen-shot-2018-10-22-at-12.36.52.png)

### Create and read Patient resource

Now you have full REST Api access to the Aidbox.Dev. For example, let's play with the `Patient` resource. 

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

**Read Patients list**

{% tabs %}
{% tab title="Request" %}
```bash
$ curl  -u root:secret http://localhost:8888/Patient
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Bundle",
    "type": "searchset",
    "entry": [
      {"resource" : {
         "resourceType": "Patient",
         "name": [
         {
            "given": [
                "Test"
            ],
            "family": "Patient"
        }
       ]
       ....
      }}
    ]
}
```
{% endtab %}
{% endtabs %}

## What's next?

Learn how to start with a simple Single Page Application with a Patient list and Patient CRUD in our next tutorial.

{% page-ref page="run-local-demo.md" %}









