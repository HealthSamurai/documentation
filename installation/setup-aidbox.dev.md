# Setup Aidbox.Dev

## Overview

We are providing a lightweight version of Aidbox named **Aidbox.Dev** — a special version aimed at local development and in your CI. To obtain access to Aidbox.Dev, please use our [License server](https://license-ui.aidbox.app/).

## Installation

The recommended way to work with Aidbox.Dev is docker-compose. So you need  [docker](https://docs.docker.com/install/) and [docker-compose](https://docs.docker.com/compose/install/) installed. Before you start you have to get free development license keys.

### License obtaining

1. Visit and register on the [License server](https://license-ui.aidbox.app) then click the `GET LICENSE KEY` button.

![](../.gitbook/assets/scr-2018-10-31_15-08-05.png)

2. Enter a short description of your project and select the `Aidbox.Dev` option.

![](../.gitbook/assets/screen-shot-2018-10-02-at-17.28.09.png)

3. Congratulations, now you have a license key.

![](../.gitbook/assets/screen-shot-2018-10-02-at-17.34.31.png)

Minimal installation consists of two containers - one with Aidbox.Dev and second with our PostgreSQL build. You can copy docker-compose.yaml content below or clone it from example repo - [https://github.com/Aidbox/devbox.git](https://github.com/Aidbox/devbox.git)

{% code-tabs %}
{% code-tabs-item title="docker-compose.yaml" %}
```yaml
version: '3.1'
services:
  devbox:
    image: "${AIDBOX_IMAGE}"
    depends_on:
      - "devbox-db"
    links:
      - "devbox-db:database"
    ports:
      - "${AIDBOX_PORT}:${AIDBOX_PORT}"
    env_file:
      - .env
    environment:
      PGHOST: database
  devbox-db:
    image: "${PGIMAGE}"
    ports:
      - "${PGPORT}:${PGHOSTPORT}"
    volumes:
    - "./pgdata:/data"
    environment:
      POSTGRES_USER:     "${PGUSER}"
      POSTGRES_PASSWORD: "${PGPASSWORD}"
      POSTGRES_DB:       "${PGDATABASE}"
```
{% endcode-tabs-item %}
{% endcode-tabs %}

`docker-compose.yaml` file is parametrised with environment variables, which can be stored by convention in `.env` file \( read more about[ env & docker-compose](https://docs.docker.com/compose/environment-variables/)\).

{% code-tabs %}
{% code-tabs-item title=".env" %}
```bash
AIDBOX_LICENSE_ID=<your-license-id>
AIDBOX_LICENSE_KEY=<your-license-key>

AIDBOX_CLIENT_ID=root
AIDBOX_CLIENT_SECRET=secret

AIDBOX_PORT=8888
AIDBOX_FHIR_VERSION=3.0.1


PGPORT=5432
PGHOSTPORT=5437
PGUSER=postgres
PGPASSWORD=postgres
PGDATABASE=devbox

PGIMAGE=aidbox/aidboxdb:0.0.1-alpha6
AIDBOX_IMAGE=healthsamurai/devbox:edge
```
{% endcode-tabs-item %}
{% endcode-tabs %}

| Variable | required | Desc |
| :--- | :--- | :--- |
| AIDBOX\_LICENSE\_ID | true | Your license ID |
| AIDBOX\_LICENSE\_ID | true | Your license key |
| AIDBOX\_CLIENT\_ID | false | Root Client ID \* |
| AIDBOX\_CLIENT\_SECRET | false | Root Client Secret \* |
| AIDBOX\_FHIR\_VERSION | true | Version of FHIR - 1.0.2, 1.4.0, 1.8.0, 3.01, 3.2.0, 3.3.0; Currently 3.0.1 is recommended version. |
| AIDBOX\_IMAGE | true | can be specific image tag or one of :edge or :latest |

{% hint style="info" %}
If **AIDBOX\_CLIENT\_ID** & **AIDBOX\_CLIENT\_SECRET** are provided - Aidbox will start in secured mode with access control turned on; API Client with provided ID and secret will be created as well Access Policy, which grants root privileges to this Client. In a simplest way you can access Aidbox API using basic auth and this client credentials.

If  **AIDBOX\_CLIENT\_SECRET** variable is not set, Aidbox will run in open mode and API will not be secured at all \(technically Access Policy, which allows access to any endpoint by everyone will be created at start\)
{% endhint %}

{% hint style="info" %}
**AIDBOX\_IMAGE** specify one of image from **healthsamurai/devbox** repository. You can use tag **:latest** to point to stable channel or use **:edge** for latest development version. Edge version can be unstable!
{% endhint %}

### Minimal steps to run Aidbox.Dev

Clone our [official repository](https://github.com/Aidbox/devbox) with sample configuration .

```text
$ git clone https://github.com/Aidbox/devbox.git
$ cd devbox
$ cp .env.tpl .env
```

Open the `.env` file and insert your `License ID` and `License KEY,`specify FHIR version and decide about box security and distribution channel \( latest vs edge\).

Now you can run Aidbox.Dev.

```bash
$ docker-compose up
```

That's it! Aidbox.Dev is running and you can point your browser to [http://localhost:8888](http://localhost:8888/) to see a fancy welcome page.

![Aidbox.Dev welcome page](../.gitbook/assets/screen-shot-2018-10-22-at-13.37.09.png)

### What next?

Learn how to obtain access to the [REST API](../tutorials/how-to-use-rest-api.md) by the link below.

{% page-ref page="../tutorials/how-to-use-rest-api.md" %}

### Advanced tips

You can test your docker-compose.yaml with

```bash
docker-compose config # to inspect your configuration
```

You can access you PostgreSQL  on localhost using $PGUSER and $PGPASSWORD from env variables on $PGHOSTPORT .

```bash
# source variables
$ set -o allexport && source .env && set +o allexport
# run psql
$ psql -h localhost -p $PGHOSTPORT
psql (10.3, server 9.6.3)
Type "help" for help.

devbox=# \dt
```

Or you can do it in db container:

```bash
$ docker-compose exec devbox-db psql devbox
psql (9.6.3)
Type "help" for help.

devbox=#
```

Inspect Aidbox logs:

```bash
$ docker-compose logs -f devbox
```

Use curl to access API 

```bash
$ curl localhost:7777/\$metadata?_format=yaml
{message: Access Denied}
# ups box is secured
$ curl -u $AIDBOX_CLIENT_ID:$AIDBOX_CLIENT_SECRET \
  localhost:8888/\$metadata?_format=yaml | less
```

{% hint style="info" %}
Be careful with **$** sign in url paths \(aka **/$metadata**\) in shell  - you have to escape it \( **/\$metadata**\) otherwise shell will try to interpret it as variables ;\)
{% endhint %}

#### Run multiple instances

To run multiple instances you can use `docker-compose up` command with `-p` argument to provide prefix for created containers, i.e. \(don't forget change ports for avoiding duplicates\):

```bash
$ docker-compose -p devbox1 up -d
$ docker-compose -p devbox2 up -d
```

#### Stop Aidbox.Dev

```bash
$ docker-compose stop
```

#### Destroy Aidbox.Dev

```bash
$ docker-compose down
```

By default docker-compose file mount folder `./pgdata` as persistent volume for PostgreSQL, so it will survive restarts and destroy of containers. If you want to completely cleanup previous installation just `rm -rf ./pgdata`

#### Upgrade Aidbox.Dev

```
$ docker-compose down
$ docker-compose pull
$ docker-compose up -d
```

