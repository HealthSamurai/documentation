# Logging & Audit

{% hint style="warning" %}
This is early draft and subject of change
{% endhint %}

### Aidbox Logging

Aidbox generate structured logs in json format into ndjson files. Aidbox logger does manage files rotation.



### Logs Schema

#### General attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| **tnt** | string | tenant |
| **ev** | string | log event name |
| **lvl** | enum | empty - info, warn, error, panic |
| **ts** | date string   | event timestamp in ISO 8601 format |
| **d** | number | duration in milliseconds |
| **w** | string | worker name |
| **msg** | string  | event description |
| **err** | string | error description |
| **etr** | string | stack trace |
| **ctx** | string | context id |

### REST API logs

### SQL logs

### Auth logs

