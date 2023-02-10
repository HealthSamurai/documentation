# Log event configuration

Aidbox logs all sql queries by default and truncate sql query up to 500 characters.&#x20;

#### BOX\_LOGGING\_SQL\_MAX\_\_LENGTH

Allow you to specify sql query max length  to be logged. Value **-1** mean log full query without any restriction. Default: **500**

```
BOX_LOGGING_SQL_MAX__LENGTH=-1 
#OR
BOX_LOGGING_SQL_MAX__LENGTH=100
```

#### BOX\_LOGGING\_SQL\_MIN\_\_DURATION

Similar to postgresql configuration. In some cases you can don't log query that was be less then 100 millisecond. Default **-1** (log all). Values must be in milliseconds.

```
BOX_LOGGING_SQL_MIN__DURATION=100
```

Also available in Aidbox configuration project config k

{% content-ref url="../../reference/configuration/aidbox-project/aidbox.config-config.md" %}
[aidbox.config-config.md](../../reference/configuration/aidbox-project/aidbox.config-config.md)
{% endcontent-ref %}

