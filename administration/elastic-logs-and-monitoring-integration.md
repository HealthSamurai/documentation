# Elastic Logs and Monitoring Integration

### ElasticSearch Logging 

A list of environment variables that we need to declare in order to enable Aidbox to log to ElasticSearch. 

```yaml
AIDBOX_ES_URL = <url>
# Required. Elsaticsearch url

AIDBOX_ES_AUTH = <user>: <password>
# Optional. Basic auth credentials if there is protecion

AIDBOX_ES_BATCH_SIZE = <uint>
# Optional. Size of elastic search post batch, default is 200

AIDBOX_ES_BATCH_TIMEOUT = <millis>
# Optional. Timeout when to post batch if there not enough 
# messages to post a full batch. Default is 1 hour

AIDBOX_LOGS = <fallback logs file path>
# Optional. Path to file where to write logs if error while 
# posting to elastic has occurred
# If not provided, aidbox will just print logs to stdout 
# in case of an errors.
```

#### 

### Elastic APM Monitoring

A list of variables in order to enable monitoring with Elastic APM. 

```yaml
ELASTIC_APM_SERVER_URLS = <url>
# Required. APM server url
ELASTIC_APM_SERVICE_NAME = <name>
# Optional. Default value is "Aidbox"
```

The official full APM configuration documentation can be found [here](https://www.elastic.co/guide/en/apm/agent/java/current/configuration.html).



