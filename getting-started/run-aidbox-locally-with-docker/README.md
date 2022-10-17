# Run Aidbox locally with Docker

This quickstart guide explains how to run Aidbox locally using docker compose. You will learn how to obtain a free short-time license and set up Aidbox.

### Get a license

Go to the [Aidbox portal](https://aidbox.app). Sign up and click the new license button. Choose product type "Aidbox" and hosting type "Self-hosted".

You'll see your license in the "My Licenses" list. Click on your new license and copy credentials. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose.

### Create docker-compose.yaml

Firstly, let's make the configuration file. There are two services: `aidbox-db` and `aidbox`. The first one is PostgreSQL database and the second one is the Aidbox itself.

{% code title="docker-compose.yaml" %}
```yaml
version: '3.7'
services:
  aidbox-db:
    image: "${PGIMAGE}"
    ports:
      - "${PGHOSTPORT}:${PGPORT}"
    volumes:
    - "./pgdata:/data"
    environment:
      POSTGRES_USER:     "${PGUSER}"
      POSTGRES_PASSWORD: "${PGPASSWORD}"
      POSTGRES_DB:       "${PGDATABASE}"

  aidbox:
    image: "${AIDBOX_IMAGE}"
    depends_on: ["aidbox-db"]
    links:
      - "aidbox-db:database"
    ports:
      - "${AIDBOX_PORT}:${AIDBOX_PORT}"
    volumes:
      - "./project:/project"
    env_file:
      - .env
    environment:
      PGHOST: database
```
{% endcode %}

### Create .env file

To configure Aidbox we need to pass environment variables to it. We can pass them with `.env` file.

{% code title=".env" %}
```shell
# postgres image to run
PGIMAGE=healthsamurai/aidboxdb:14.2

# aidbox image to run
# AIDBOX_IMAGE=healthsamurai/aidboxone:stable
AIDBOX_IMAGE=healthsamurai/aidboxone:edge

# license details
AIDBOX_LICENSE=<your-license-key>

# if you got pair of id and key use this instead
# AIDBOX_LICENSE_ID=<your-license-id>
# AIDBOX_LICENSE_KEY=<your-license-key>

# Client to create on start up
AIDBOX_CLIENT_ID=root
AIDBOX_CLIENT_SECRET=secret

# root user to create on start up
AIDBOX_ADMIN_ID=admin
AIDBOX_ADMIN_PASSWORD=secret

# port to run webserver at
AIDBOX_PORT=8888
AIDBOX_FHIR_VERSION=4.0.1

# db connection params
PGPORT=5432
PGHOSTPORT=5437
PGUSER=postgres
PGPASSWORD=postgres
PGDATABASE=aidbox

AIDBOX_COMPLIANCE=enabled

# Aidbox configuraiton project path and entrypoint
BOX_PROJECT_GIT_URL=project/
AIDBOX_ZEN_ENTRYPOINT=system/box
```
{% endcode %}

Insert your license key into environment file. Change the line

```shell
AIDBOX_LICENSE=<your-license-key>
```

in the `.env` file where `<your-license-key>` is a license key which you obtained on the [get a license](./#get-a-license) step.

You can find more about required Aidbox environment variables [here.](../../reference/configuration/environment-variables/aidbox-required-environment-variables.md)

### Create and set up your [Aidbox configuration project](../../aidbox-configuration/aidbox-zen-lang-project/)

Your project is a place to set various Aidbox instance configurations, such as: [API constructor](../../aidbox-configuration/aidbox-api-constructor.md), [seed service](../../aidbox-configuration/aidbox-zen-lang-project/seed-v2.md), etc.

#### Create a directory `project` with following structure:

| Directory structure                                                      | zen-package.edn                                                                  | zrc/system.edn                                                                                                                           |
| ------------------------------------------------------------------------ | -------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| <pre><code>project/
  zen-package.edn
  zrc/
    system.edn</code></pre> | <pre class="language-clojure"><code class="lang-clojure">{:deps {}}</code></pre> | <pre class="language-clojure"><code class="lang-clojure">{:ns system
 :import #{aidbox}
 box
 {:zen/tags #{aidbox/system}}}</code></pre> |

{% hint style="info" %}
You can change the names `project`, `system` and `box` to your preference. This requires updating `docker-compose.yml` `aidbox.volumes` and `AIDBOX_ZEN_ENTRYPOINT`, `BOX_PROJECT_GIT_URL` variables. Please refer to the [Aidbox configuration project documentation](../../aidbox-configuration/aidbox-zen-lang-project/) for details.
{% endhint %}

#### Initialize this directory as a git repo and commit all the files you created:

```shell-session
cd project/ && git init && git add --all && git commit -m "Initial commit"
```

After this you have successfully set up your Aidbox configuration project and now can proceed to the next step and launch Aidbox.

If you want to learn in detail options available during Aidbox configuration project set up, please refer to this page:

{% content-ref url="../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md" %}
[setting-up-a-configuration-project.md](../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md)
{% endcontent-ref %}

### Launch Aidbox&#x20;

Start Aidbox with Docker Compose

```shell
docker compose up
```

This command will download and start Aidbox and its dependencies. This can take a few minutes.

### Go to the Aidbox UI

Open [http://localhost:8888](http://localhost:8888). You'll see Aidbox login page. Sign in using login `admin` and password `secret`.

Now you are ready to use Aidbox.
