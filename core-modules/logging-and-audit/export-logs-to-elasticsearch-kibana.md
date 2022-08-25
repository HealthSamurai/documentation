---
description: In this tutorial, you will learn how to export logs to ElasticSearch.
---

# Export logs to ElasticSearch/Kibana

In this tutorial, you will learn how to export Aidbox logs into ElasticSearch/Kibana stack using aidbox-cli.

### Configure ElasticSearch & Kibana

So first of all, let's add to Aidbox docker-compose file Elastic & Kibana:

```yaml
services:
  ...
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.0.0
    volumes:
      - ./esdata:/usr/share/elasticsearch/data
    environment:
      - cluster.name=aidbox-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - discovery.type=single-node
    ulimits:
      memlock:
        soft: -1
        hard: -1
    ports:
      - "9200:9200"
  kibana:
    links:
      - "elasticsearch:elasticsearch"
    image: docker.elastic.co/kibana/kibana:7.0.0
    ports:
      - "5601:5601"
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
  ....
```

### Configure Aidbox & aidbox-cli&#x20;

Next step is to configure Aidbox logging into the file system. We have to mount logs volume and provide AIDBOX\_LOGS env variable set to path to logs file:

```yaml
services:
  aidbox:
    image: "${AIDBOX_IMAGE}"
    depends_on:
      - "aidbox-db"
    links:
      - "aidbox-db:database"
    ports:
      - "8888:8888"
    volumes:
    - "./logs:/logs"
    environment:
      ....
      AIDBOX_LOGS: '/logs/aidbox'
  aidbox-db:
    ...
  logexp:
    links:
      - "elasticsearch:elasticsearch"
    image: 'aidbox/aidbox-cli:0.0.1-RC1'
    command: ["/aidbox", "es", "logs", "-l", "http://elasticsearch:9200/logs", "-f", "/logs/aidbox"]
    volumes:
    - "./logs:/logs"
```

In the volumes section we mount logs directory as a volume to aidbox (Aidbox)  service and do the same for aidbox-cli (logexp) service. Devbox will log into `/logs/aidbox` file and aidbox-cli will read this file and send logs into elasticsearch service.

Logger use elastic url prefix  `http://elasticsearch:9200/logs` and send logs into index like this`http://lasticsearch:9200/logs-2019-08-01/logs` , i.e. index **prefix-\<date>** will be used as an index name**.**  You can use pattern `logs-*` to search in indexes in kibana.

Resulting docker-compose file should similar to  [docker-compose.yaml](https://gist.github.com/niquola/463561e25ea0b6a5c12cd0407a0fd7bf). Do not forget to replace LICENSE\_ID & KEY with your license information.

## Start Aidbox and all services

Now it's time to start your services:

```bash
docker-compose -d up
```
