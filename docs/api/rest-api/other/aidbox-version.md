---
description: Get Aidbox version information including release channel and commit hash using $version endpoint.
---

# Aidbox version

Aidbox version is available to obtain with the following `REST` method:&#x20;

{% tabs %}
{% tab title="Request" %}
```http
GET /$version
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status 200" %}
```
version: '2602'
commit: abc12345
timestamp: '2026-02-01T12:00:00Z'
```
{% endcode %}
{% endtab %}
{% endtabs %}
