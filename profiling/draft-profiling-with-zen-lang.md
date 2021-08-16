---
description: This feature will be available in 2021 August Release
---

# Draft: Profiling with zen-lang

Aidbox provided validation with JSON Schema and Basic FHIR Profiles for a long time. In the nearest future, we are going to switch to **zen/schema** as the main engine for the validation and configuration of Aidbox.

#### Develop profiles with Aidbox.Dev and zen

To start working with Aidbox and zen you have to create "zen project" and mount it into Aidbox.  

{% hint style="info" %}
This feature is experimental and available only on **edge** channel. In your compose file or .env set aidbox image to **healthsamurai/devbox:edge** and ****pull latest image:`docker pull healthsamurai/devbox:edge`
{% endhint %}

Create **zrc** directory in the same directory as your **docker-compose.yaml.** Create your first zen file in zrc  **&lt;myproject&gt;.edn**:

{% code title="zrc/acme.edn" %}
```text
{ns acme

  MySchema
  {:zen/tags #{zen/schema}
   :keys {:name {:type zen/string}}}

}
```
{% endcode %}

In docker-compose file add zrc volume and set envs `ZEN_PROJECT: '/zrc'` and `ZEN_ENTRY: 'acme'`

```text
devbox:
    container_name: "devbox"
    image: "health"
    volumes:
    - "./zrc:/zrc"
    depends_on:
      - "devbox-db"
      - es
    links:
      - "devbox-db:database"
      - es:es
    ports:
      - "${AIDBOX_PORT}:${AIDBOX_PORT}"
    env_file:
      - .env
    environment:
      PGHOST: database
      AIDBOX_ES_URL: "http://es:9200/"
      AIDBOX_ES_BATCH_SIZE: 10
      AIDBOX_ES_BATCH_TIMEOUT: 5000
      AIDBOX_ES_INDEX_PAT: "'aidbox-logs'-yyyy-MM-dd"
      ZEN_PROJECT: '/zrc'
      ZEN_ENTRY: acme
      ZEN_DEV_MODE: ok
```

Restart container `docker-compose restart`

In Aidbox UI open new Profiles tab:

![](../.gitbook/assets/image%20%2865%29.png)

You should be able to see zen-ui screen:

![](../.gitbook/assets/image%20%2866%29.png)

