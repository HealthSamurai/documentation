---
description: >-
  Aidbox metrics has integration with Grafana, which can generate dashboards and
  upload it to Grafana
---

# Set-up Grafana integration

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](broken-reference) us if you have questions, feedback, or suggestions.
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
_`datasource`  (Optional) - specify prometheus like datasource name. Default: **Prometheus**_

_`kibana-url` (Optional) - specify Kibana base url for generating a drill-down link to Kibana._
{% endtab %}

{% tab title="Result" %}
`status` - "200 OK"
{% endtab %}

{% tab title="Error" %}
`error` - "Provide following environment variables: `BOX_METRICS_GRAFANA_USER`, `BOX_METRICS_GRAFANA_PASSWORD`, `BOX_METRICS_GRAFANA_URL`"

`error` - \<Grafana internal error>
{% endtab %}
{% endtabs %}

#### Request example

```http
POST /rpc
content-type: application/edn

{:method aidbox.metrics/update-aidbox-dashboard
 :params {:datasource "My Prometheus Datasource name"
          :kibana-url "https://my.kibana.url.com"}}
```

<figure><img src="../../../../.gitbook/assets/Screen Shot 2022-12-06 at 08.44.55.png" alt=""><figcaption><p>Grafana Aidbox Dashboard</p></figcaption></figure>

### `aidbox.metrics/update-cluster-dashboard`

Generates dashboards with cluster-specific metrics

{% tabs %}
{% tab title="Parameters" %}

{% endtab %}

{% tab title="Result" %}
`status` - "200 OK"
{% endtab %}

{% tab title="Error" %}
`error` - "Provide following environment variables: `BOX_METRICS_GRAFANA_USER`, `BOX_METRICS_GRAFANA_PASSWORD`, `BOX_METRICS_GRAFANA_URL`"

`error` - \<Grafana internal error>
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

### `aidbox.metrics/get-aidbox-dashboard`

Generates dashboards as a JSON&#x20;

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
Grafana dashboard JSON:

```
{
 "uid": "metrics-dev",
 "title": "Aidbox metrics dev",
 // omitted
}
```
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
content-type: application/json
accept: application/json

{"method": "aidbox.metrics/get-aidbox-dashboard"}
```
