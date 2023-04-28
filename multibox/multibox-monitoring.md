---
description: >-
  Multibox has pre-built integration with Grafana, generates dashboards and
  upload it to Grafana
---

# Multibox monitoring

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../overview/contact-us.md) us if you have questions, feedback, or suggestions.
{% endhint %}

## Configuration

Set up the following environment variables:

| Environment variable           | Description                                               |
| ------------------------------ | --------------------------------------------------------- |
| `BOX_INSTANCE_NAME`            | Provided instance name will be attached to metrics labels |
| `BOX_METRICS_GRAFANA_URL`      | Grafana instance url                                      |
| `BOX_METRICS_GRAFANA_USER`     | Grafana user name                                         |
| `BOX_METRICS_GRAFANA_PASSWORD` | Grafana user password                                     |

## RPC methods

{% hint style="info" %}
Сreated dashboards will be placed in the `aidboxgen` folder
{% endhint %}

### `multibox.metrics/export-dashboard`

Generates dashboards with instance-specific metrics

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
`status` - "200 OK`, Grafana success info`"
{% endtab %}

{% tab title="Error" %}
`error` - "Provide following environment variables: `BOX_METRICS_GRAFANA_USER`, `BOX_METRICS_GRAFANA_PASSWORD`, `BOX_METRICS_GRAFANA_URL`"

`error` - \<Grafana internal error>
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
`"Prometheus"` datasource is required
{% endhint %}

#### Request example

```
POST /rpc
content-type: application/edn

{:method multibox.metrics/export-dashboard}
```
