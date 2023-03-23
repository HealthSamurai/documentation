---
description: This pages explains how Access policy dev tool works
---

# Access policy dev tool

Access policy dev tool simplifies development & debugging AccessPolicy resources. It was introduced in March v2303 release of Aidbox.

{% hint style="info" %}
If you have any questions or ideas, how to make the dev tool better, feel free to communicate with us in [Aidbox community chat](https://t.me/aidbox).
{% endhint %}

## Overview

....

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.40.41.png" alt=""><figcaption><p>Access policy dev tool</p></figcaption></figure>

## Get to Access policy dev tool

### From the list of access policies

To open **Access policy dev tool** click the _dev tool_ link on the right side of the row.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.40.10.png" alt=""><figcaption><p>AccessPolicy list with the link to dev tool</p></figcaption></figure>

### From the selected AccessPolicy

To open **Access policy dev tool** from the AccessPolicy resource page click the _dev tool_ link in the header of the screen.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.48.13.png" alt=""><figcaption><p>AccessPolicy view page with the link to dev tool</p></figcaption></figure>

## Dev tool panels

There are 4 panels in the **Access policy dev tool**:

1. Access policy editor (top left panel) is used to change current access policy. See [AccessPolicy resource documentation](access-control.md)
2. HTTP request editor (bottom left panel) is used to define an HTTP request. The request is being tested against all the access policies. See details about [REST Console HTTP request structure](../../overview/aidbox-ui/rest-console-1.md#rest-console)
3. Evaluation results (top right panel) is the area where all the access policies is enlisted and the evaluation result is shown
4. HTTP request object (bottom right panel) is the representation of the HTTP request Aidbox received and processed during the evaluation request. See [Request object structure](access-control.md#request-object-structure)

## Run evaluation

To start evaluation access policies against your current HTTP request click the `Save & Run` button. Pressing the button invokes 2 events:

1. `AccessPolicy` is saved
2. `HTTP request` is sent to the evaluator

## Tips

### How to send request on behalf of user or client?

You may provide Authorization header in the request. Example of request with provided Authorization header.

```yaml
GET /Patient
Authorization: Bearer eyJ...w5c
```

If Authorization header is not provided, Aidbox will authenticate the request by _asid_ cookie.

### Debug token

Access policy dev tool expects you to have permission to run aidbox.auth/get-eval-policy-debug-token RPC method. Dev tool automatically renews the token.&#x20;

You can check permission from Aidbox REST Console.

```yaml
POST /rpc
accept: text/yaml

method: aidbox.auth/get-eval-policy-debug-token

# Response: 200 OK
# Body:
# result:
#  token: <temp-debug-token>
```

## That's it

Access policy dev tool makes it easier to develop access policy. If you have any questions or ideas, how to make the dev tool better, feel free to communicate with us in [Aidbox community chat](https://t.me/aidbox).

### What's next

If you are looking for recommendations, how to develop access policies, check out our [AccessPolicy best practices article](accesspolicy-best-practices.md).

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.
