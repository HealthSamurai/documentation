# Elastic Logs and Monitoring Integration

### ElasticSearch Logging 

In order to enable Aidbox logging to ElasticSearch, you need to define `AIDBOX_ES_URL` environment variable:

```yaml
AIDBOX_ES_URL = <url>
# Required
# Elasticsearch url

AIDBOX_ES_AUTH = <user>:<password>
# Optional
# Basic auth credentials if there is protection

AIDBOX_ES_BATCH_SIZE = <uint>
# Optional. Default value is 200
# Size of elastic search post batch

AIDBOX_ES_BATCH_TIMEOUT = <millis>
# Optional. Default value is 3600000 (1 hour)
# Timeout when to post batch if there not enough 
# messages to post a full batch.

AIDBOX_LOGS = <fallback logs file path>
# Optional.
# Path to file where to write logs if error while 
# posting to elastic has occurred
# If not provided, aidbox will just print logs to stdout 
# in case of an errors.

AIDBOX_ES_INDEX_PAT = <format string>
# Optional. Default value is 'aidbox-logs'-yyyy-MM-dd
# Сustom index name format string.
# By changing the date precision you can control
# how often new indixes should be created. Example:
# 'aidbox-logs'-yyyy-MM will create new index monthly
# 'aidbox-logs'-yyyy-MM-W will create new index weekly
```

`AIDBOX_ES_INDEX_PAT`  formatter syntax documentation is [here](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)

{% hint style="warning" %}
Note. If elastic was down and some logs were accumulated in `AIDBOX_LOGS`, Aidbox doesn't resend those logs to elastic
{% endhint %}

### Elastic APM Monitoring

You need to define `ELASTIC_APM_SERVER_URLS` environment variable to enable monitoring with Elastic APM:

```yaml
ELASTIC_APM_SERVER_URLS = <url>
# Required. APM server url
ELASTIC_APM_SERVICE_NAME = <name>
# Optional. Default value is "Aidbox"
```

The full list of configuration options can be found [here](https://www.elastic.co/guide/en/apm/agent/java/current/configuration.html#_option_reference).



