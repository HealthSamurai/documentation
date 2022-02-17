---
description: >-
  Aidbox metrics has integration with Grafana, which can generate dashboards and
  upload it to Grafana
---

# Grafana integration

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../../contact-us.md) us if you have questions, feedback, or suggestions.
{% endhint %}

## Configuration

You need to set up the following environment variables:

| Environment variable           | Description                                                                                                      |
| ------------------------------ | ---------------------------------------------------------------------------------------------------------------- |
| `BOX_INSTANCE_NAME`            | Provided instance name will be attached to metrics labels. Required for monitoring of multiple Aidbox instances. |
| `BOX_METRICS_GRAFANA_URL`      | Grafana instance url                                                                                             |
| `BOX_METRICS_GRAFANA_USER`     | Grafana user name                                                                                                |
| `BOX_METRICS_GRAFANA_PASSWORD` | Grafana user password                                                                                            |

## RPC Methods

{% hint style="info" %}
Сreated dashboards will be placed in the `aidboxgen` folder
{% endhint %}

### `aidbox.metrics/update-aidbox-dashboard`

Generates dashboards with instance-specific metrics

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
`status` - "200 OK"
{% endtab %}

{% tab title="Error" %}
`error` - "Grafana user,password,url doesn't provided"

`error` - \<Gfrana internal error>
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
`"Prometheus"` datasource is required
{% endhint %}

#### Request example

```http
POST /rpc
content-type: application/edn

{:method aidbox.metrics/update-aidbox-dashboard}
```

### aidbox.metrics/update-cluster-dashboard

Generates dashboards with cluster-specific metrics

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
`status` - "200 OK"
{% endtab %}

{% tab title="Error" %}
`error` - "Grafana user,password,url doesn't provided"

`error` - \<Gfrana internal error>
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
Required Prometheus exporters:

* `kube-state-metrics`
* `node-exporter`
* `cadvisor`
{% endhint %}

#### Request example

```http
POST /rpc
content-type: application/edn

{:method aidbox.metrics/update-cluster-dashboard}
```
