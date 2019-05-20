---
description: In this tutorial we will see how to export logs to ElasticSearch
---

# Export logs to ElasticSearch/Kibana

In this tutorial you will see how export Aidbox.Dev logs into ElasticSearch/Kibana stack using aidbox-cli.

### Configure ElasticSearch & Kibana

So first of all let's add to Aidbox.Dev docker-compose file Elastic & Kibana:

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

### Configure Aidbox & aidbox-cli 

Next step is to configure Aidbox.Dev logging into file system. We have to mount logs volume and provide AIDBOX\_LOGS env variable set to path to logs file:

```yaml
services:
  devbox:
    image: "${DEVBOX_IMAGE}"
    depends_on:
      - "devbox-db"
    links:
      - "devbox-db:database"
    ports:
      - "8888:8888"
    volumes:
    - "./logs:/logs"
    environment:
      ....
      AIDBOX_LOGS: '/logs/devbox'
  devbox-db:
    ...
  logexp:
    links:
      - "elasticsearch:elasticsearch"
    image: 'aidbox/aidbox-cli:0.0.1-alpha'
    command: ["/aidbox", "es", "logs", "-l", "http://elasticsearch:9200/logs/logs", "-f", "/logs/devbox"]
    volumes:
    - "./logs:/logs"
```

In volumes section we mount logs directory as volume to devbox \(Aidbox.Dev\)  service and do the same for aidbox-cli \(logexp\) service. Aidbox.Dev will log into `/logs/devbox` file and aidbox-cli will read this file and send logs into elasticsearch service.

Resulting docker-compose file should similar to  [docker-compose.yaml](https://gist.github.com/niquola/463561e25ea0b6a5c12cd0407a0fd7bf). Do not forget to replace LICENSE\_ID & KEY with your license information.

## Start Aidbox.Dev and all services

Now it's time to start your services:

```bash
docker-compose -d up
```

