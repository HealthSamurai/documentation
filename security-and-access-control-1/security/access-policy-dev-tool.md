---
description: This pages explains how Access policy dev tool works
---

# Access policy dev tool

Creating and maintaining access policies could be a difficult task. To reduce the complexity of this `dev tool` is created.

## Prerequisites

**Access policy dev tool** user should have access to the RPC method `aidbox.auth/get-eval-policy-debug-token`.

To check if user is granted needed access run following request in the REST Console.&#x20;

```yaml
POST /rpc
accept: text/yaml

method: aidbox.auth/get-eval-policy-debug-token
```

The response should look like.

```yaml
# Response status: 200 OK

result:
  token: <temp-token>
```

## Get to Access policy dev tool

### From the list of access policies

To open **Access policy dev tool** click the _dev tool_ link on the right side of the row.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.40.10.png" alt=""><figcaption><p><code>dev tool</code> link on the selected AccessPolicy</p></figcaption></figure>

### From the selected AccessPolicy

To open **Access policy dev tool** from the AccessPolicy resource page click the _dev tool_ link in the header of the screen.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.48.13.png" alt=""><figcaption><p><code>dev tool</code> link in the header of the screen</p></figcaption></figure>

## Dev tool panels

There are 4 panels in the **Access policy dev tool**:

1. Access policy editor (top left panel) is used to change current access policy. See  [AccessPolicy resource documentation](access-control.md)
2. HTTP request editor (bottom left panel) is used to define an HTTP request. The request is being tested against all the access policies. See details about [REST Console HTTP request structure](../../overview/aidbox-ui/rest-console-1.md#rest-console)
3. Evaluation results (top right panel) is the area where all the access policies is enlisted and the evaluation result is shown
4. HTTP request object (bottom right panel) is the representation of the HTTP request Aidbox received and processed during the evaluation request. See [Request object structure](access-control.md#request-object-structure)

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-23 at 11.40.41.png" alt=""><figcaption><p>Dev tool panels</p></figcaption></figure>

## Run evaluation

To start evaluation access policies against your current HTTP request click the `Save & Run` button. Pressing the button invokes 2 events:

1. `AccessPolicy` is saved
2. `HTTP request` is sent to the evaluator
