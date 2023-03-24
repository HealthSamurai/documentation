---
description: >-
  Access policy dev tool simplifies development & debugging AccessPolicy
  resources
---

# Access policy dev tool

Access policy dev tool simplifies development & debugging AccessPolicy resources. It was introduced in March v2303 release of Aidbox.

{% hint style="info" %}
If you have any questions or ideas, how to make the dev tool better, feel free to communicate with us in [Aidbox community chat](https://t.me/aidbox).
{% endhint %}

## Overview

The dev tool is a part of Aidbox UI Console, which aims

* to edit AccessPolicy resource, and
* to give a nice view for AccessPolicy debug output for specific request in the same place.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.40.41.png" alt=""><figcaption><p>Access policy dev tool</p></figcaption></figure>

The dev tool is split on two sides, editor side and result side. On the left side you define&#x20;

* [AccessPolicy resource](access-control.md) and
* HTTP request you are going to debug.

When you press Save\&Run button, the dev tool saves AccessPolicy and performs policy debug operation for the specified request, and displays the result on the right side. You can see there

* _Eval policy result._ List of all access policies and the result of evaluation.
* _Parsed HTTP request._ It's an [internal representation](access-control.md#request-object-structure) of the request, which Aidbox passes to the eval-policy function.

### How to get access to the dev tool

You can get access to the dev tool from AccessPolicy list page by clicking dev tool link on a AccessPolicy you want to debug.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.40.10.png" alt=""><figcaption><p>AccessPolicy list with the link to dev tool</p></figcaption></figure>

You also can get access to the dev tool from AccessPolicy view page. The link is available on the top right corner.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.48.13.png" alt=""><figcaption><p>AccessPolicy view page with the link to dev tool</p></figcaption></figure>

## Tips

### How to send request on behalf of user or client?

You may provide Authorization header in the request. Example of request with provided Authorization header.

```yaml
GET /Patient
Authorization: Bearer eyJ...w5c
```

If Authorization header is not provided, Aidbox authenticates the request by _asid_ cookie.

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
