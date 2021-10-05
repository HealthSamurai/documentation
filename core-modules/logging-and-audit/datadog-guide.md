---
description: >-
  This section is a step-by-step guide for working with your Aidbox logs with
  Datadog.
---

# 🎓 Export logs to Datadog tutorial

## Set up Datadog API Key

You need to enable Datadog Logs API and create an API Key

To create a new API key go to [Datadog](https://app.datadoghq.com/) then go to Integrations -&gt; APIs:

![](../../.gitbook/assets/image%20%286%29.png)

Click API Keys

![](../../.gitbook/assets/image%20%2811%29.png)

Enter the name of the new key and click Create API Key

![](../../.gitbook/assets/image%20%2817%29.png)

Copy the newly generated key

![](../../.gitbook/assets/image%20%2815%29.png)

## Configure Aidbox

Set the `AIDBOX_DD_API_KEY`  environment variable to the Datadog API Key.

For example, if you are using Docker Compose, add the variable to the environment section:

```yaml
devbox:
  # ...
  environment:
    # ...
    AIDBOX_DD_API_KEY: 64977deeb1fff8e27763028e596a6856
```

##  Configure Datadog

Go to Logs -&gt; Configuration

![](../../.gitbook/assets/image%20%2837%29.png)

Click edit on Preprocessing for JSON logs

![](../../.gitbook/assets/image%20%2823%29.png)

Set the attributes

| Name | Value |
| :--- | :--- |
| Date attribute | ts |
| Service attributes | tn |
| Status attributes | lvl |
| Trace Id attributes | ctx |
| Message attributes | msg,err |

![](../../.gitbook/assets/image%20%2820%29.png)

Now add a facet for the event.

Go to logs then click add on the left

![](../../.gitbook/assets/image%20%2824%29.png)

Use `@ev` path for the new facet

![](../../.gitbook/assets/image%20%2822%29.png)

In this guide, we will add a metric for authorization failures

Go to Logs -&gt; Generate Metrics

![](../../.gitbook/assets/image%20%2839%29.png)

Click Add a new metric

![](../../.gitbook/assets/image%20%2825%29.png)

Define a query for the `auth/authorize-failed` event

![](../../.gitbook/assets/image%20%2819%29.png)

Enter the name for the new metric and click Create metric

![](../../.gitbook/assets/image%20%2836%29.png)

Now we can see our metric in the Metrics Explorer

![](../../.gitbook/assets/image%20%2827%29.png)

And we can define monitoring to alert if there are too many authorization failures. To do this navigate to Monitors -&gt; New Monitor 

![](../../.gitbook/assets/image%20%2829%29.png)

Select monitor type "metric"

![](../../.gitbook/assets/image%20%2834%29.png)

Set up monitoring options

![](../../.gitbook/assets/image%20%2828%29.png)

And notifications

![](../../.gitbook/assets/image%20%2833%29.png)

Now we can see our monitor on the Monitor page

![](../../.gitbook/assets/image%20%2830%29.png)

It takes some time for Datadog to set up the monitor. After a while, it will become green

![](../../.gitbook/assets/image%20%2826%29.png)

And when there are too many authorization failures in a short period of time, it changes the status to warning

![](../../.gitbook/assets/image%20%2835%29.png)

Then when it hits the threshold, the alert is created

![](../../.gitbook/assets/image%20%2821%29.png)

On the Monitor page, you can see statistics

![](../../.gitbook/assets/image%20%2818%29.png)

{% hint style="warning" %}
By default Aidbox sends logs as bundles of multiple records so if there's not enough data **you won't see any logs in Datadog**. For testing purposes reduce bundle size to 1 record by setting environment variable:

AIDBOX\_DD\_BATCH\_SIZE=1
{% endhint %}

