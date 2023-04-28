---
description: >-
  This section is a step-by-step guide for working with your Aidbox logs with
  Datadog.
---

# 🎓 Export logs to Datadog tutorial

## Set up Datadog API Key

You need to enable Datadog Logs API and create an API Key

To create a new API key go to [Datadog](https://app.datadoghq.com/) then go to Integrations -> APIs:

![](<../../../.gitbook/assets/image (11) (2).png>)

Click API Keys

![](<../../../.gitbook/assets/image (13).png>)

Enter the name of the new key and click Create API Key

![](<../../../.gitbook/assets/image (15).png>)

Copy the newly generated key

![](<../../../.gitbook/assets/image (16).png>)

## Configure Aidbox

Set the `AIDBOX_DD_API_KEY` environment variable to the Datadog API Key.

For example, if you are using Docker Compose, add the variable to the environment section:

```yaml
aidbox:
  # ...
  environment:
    # ...
    AIDBOX_DD_API_KEY: 64977deeb1fff8e27763028e596a6856
```

## Configure Datadog

Go to Logs -> Configuration

![](<../../../.gitbook/assets/image (18).png>)

Click edit on Preprocessing for JSON logs

![](<../../../.gitbook/assets/image (19).png>)

Set the attributes

| Name                | Value   |
| ------------------- | ------- |
| Date attribute      | ts      |
| Service attributes  | tn      |
| Status attributes   | lvl     |
| Trace Id attributes | ctx     |
| Message attributes  | msg,err |

![](<../../../.gitbook/assets/image (22).png>)

Now add a facet for the event.

Go to logs then click add on the left

![](<../../../.gitbook/assets/image (23).png>)

Use `@ev` path for the new facet

![](<../../../.gitbook/assets/image (24).png>)

In this guide, we will add a metric for authorization failures

Go to Logs -> Generate Metrics

![](<../../../.gitbook/assets/image (25).png>)

Click Add a new metric

![](<../../../.gitbook/assets/image (26).png>)

Define a query for the `auth/authorize-failed` event

![](<../../../.gitbook/assets/image (27).png>)

Enter the name for the new metric and click Create metric

![](<../../../.gitbook/assets/image (28).png>)

Now we can see our metric in the Metrics Explorer

![](<../../../.gitbook/assets/image (29).png>)

And we can define monitoring to alert if there are too many authorization failures. To do this navigate to Monitors -> New Monitor

![](<../../../.gitbook/assets/image (30).png>)

Select monitor type "metric"

![](<../../../.gitbook/assets/image (31).png>)

Set up monitoring options

![](<../../../.gitbook/assets/image (32).png>)

And notifications

![](<../../../.gitbook/assets/image (33).png>)

Now we can see our monitor on the Monitor page

![](<../../../.gitbook/assets/image (34).png>)

It takes some time for Datadog to set up the monitor. After a while, it will become green

![](<../../../.gitbook/assets/image (35).png>)

And when there are too many authorization failures in a short period of time, it changes the status to warning

![](<../../../.gitbook/assets/image (36).png>)

Then when it hits the threshold, the alert is created

![](<../../../.gitbook/assets/image (37).png>)

On the Monitor page, you can see statistics

![](<../../../.gitbook/assets/image (38).png>)

{% hint style="warning" %}
By default Aidbox sends logs as bundles of multiple records so if there's not enough data **you won't see any logs in Datadog**. For testing purposes reduce bundle size to 1 record by setting environment variable:

AIDBOX\_DD\_BATCH\_SIZE=1
{% endhint %}
