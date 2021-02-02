# Elastic Logs and Monitoring Integration

### ElasticSearch Logging 

In order to enable Aidbox logging to ElasticSearch you need to define `AIDBOX_ES_URL` environment variable

```yaml
AIDBOX_ES_URL = <url>
# Required. Elasticsearch url

AIDBOX_ES_AUTH = <user>:<password>
# Optional. Basic auth credentials if there is protection

AIDBOX_ES_BATCH_SIZE = <uint>
# Optional. Size of elastic search post batch, default value 
# is 200

AIDBOX_ES_BATCH_TIMEOUT = <millis>
# Optional. Timeout when to post batch if there not enough 
# messages to post a full batch. Default time is 1 hour

AIDBOX_LOGS = <fallback logs file path>
# Optional. Path to file where to write logs if error while 
# posting to elastic has occurred
# If not provided, aidbox will just print logs to stdout 
# in case of an errors.
```

#### 

### Elastic APM Monitoring

You need to define `ELASTIC_APM_SERVER_URLS` environment variable to enable monitoring with Elastic APM. 

```yaml
ELASTIC_APM_SERVER_URLS = <url>
# Required. APM server url
ELASTIC_APM_SERVICE_NAME = <name>
# Optional. Default value is "Aidbox"
```

Full list of configuration options can be found [here](https://www.elastic.co/guide/en/apm/agent/java/current/configuration.html#_option_reference)



