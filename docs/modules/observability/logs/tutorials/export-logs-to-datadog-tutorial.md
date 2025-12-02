---
description: >-
  This section is a step-by-step guide for working with your Aidbox logs with
  Datadog.
---

# Export logs to Datadog tutorial

## Set up Datadog API Key

You need to enable Datadog Logs API and create an API Key

To create a new API key go to [Datadog](https://app.datadoghq.com/) then go to Integrations -> APIs:

![Datadog Integrations menu showing APIs option](../../../../.gitbook/assets/24ed01b8-a8f8-4512-bb93-2acb88a67b3e.png)

Click API Keys

![Datadog API Keys section](../../../../.gitbook/assets/b3df2c18-df8a-443f-ac63-ccf8f9f7bdc5.png)

Enter the name of the new key and click Create API Key

![Create API Key dialog in Datadog](../../../../.gitbook/assets/c12ed7a0-ed34-4241-90ea-f68fa1a406fa.png)

Copy the newly generated key

![Newly generated API key in Datadog](../../../../.gitbook/assets/50ca7105-ac37-4859-9ca2-0fee901bb78e.png)

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

Click edit on Preprocessing for JSON logs

Set the attributes

| Name                | Value   |
| ------------------- | ------- |
| Date attribute      | ts      |
| Service attributes  | tn      |
| Status attributes   | lvl     |
| Trace Id attributes | ctx     |
| Message attributes  | msg,err |

Now add a facet for the event.

Go to logs then click add on the left

![Datadog Logs interface with add facet option](../../../../.gitbook/assets/dd3e2706-a448-40ec-b286-328c21c65227.png)

Use `@ev` path for the new facet

![Add facet dialog with event path configuration](../../../../.gitbook/assets/e1119164-27bc-41d0-b42c-3159a3256a56.png)

In this guide, we will add a metric for authorization failures

Go to Logs -> Generate Metrics

Click Add a new metric

![Datadog Generate Metrics page](../../../../.gitbook/assets/7daea20d-4808-4060-87ef-3c964deb4254.png)

Define a query for the `auth/authorize-failed` event

![Metric query definition for authorization failures](../../../../.gitbook/assets/f79665ba-7d90-4e8f-86b2-2ab3dfd0595d.png)

Enter the name for the new metric and click Create metric

![Create metric dialog with metric name input](../../../../.gitbook/assets/36321f5b-8e5b-4de1-aed2-4a9482dae447.png)

Now we can see our metric in the Metrics Explorer

![Datadog Metrics Explorer showing authorization failures metric](../../../../.gitbook/assets/6ebc86c6-16d7-42ac-9226-ed5ddf98a2bc.png)

And we can define monitoring to alert if there are too many authorization failures. To do this navigate to Monitors -> New Monitor

![Datadog Monitors menu with New Monitor option](../../../../.gitbook/assets/816d254f-244b-447c-9ec4-f26d65dcf0b3.png)

Select monitor type "metric"

![Monitor type selection showing metric option](../../../../.gitbook/assets/3030e22e-bb9c-4568-9bcc-4824e8235316.png)

Set up monitoring options

![Monitor configuration with alert thresholds](../../../../.gitbook/assets/a6088ddf-a566-46b3-ab9e-48a3aa824450.png)

And notifications

Now we can see our monitor on the Monitor page

![Datadog Monitor page showing created monitor](../../../../.gitbook/assets/1dfd30b4-e490-4982-bd4d-d4c28dc907cd.png)

It takes some time for Datadog to set up the monitor. After a while, it will become green

And when there are too many authorization failures in a short period of time, it changes the status to warning

![Monitor status showing warning state](../../../../.gitbook/assets/f2acc928-122c-4730-a439-e507fbdc2bdb.png)

Then when it hits the threshold, the alert is created

![Monitor alert triggered notification](../../../../.gitbook/assets/c0721bd1-fd50-4c27-b898-1b6444d32977.png)

On the Monitor page, you can see statistics

![Monitor statistics and performance data](../../../../.gitbook/assets/a0f32af2-1e72-43a1-ac6a-9860cfa89597.png)

{% hint style="warning" %}
By default Aidbox sends logs as bundles of multiple records so if there's not enough data **you won't see any logs in Datadog**. For testing purposes reduce bundle size to 1 record by setting environment variable:

AIDBOX\_DD\_BATCH\_SIZE=1
{% endhint %}
