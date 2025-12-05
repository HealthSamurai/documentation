---
description: JSON-RPC API for Aidbox with remote procedure calls using HTTP POST to the /rpc endpoint.
---

# RPC API

With release 2108 we introduce RPC API. JSON-RPC is a stateless, light-weight remote procedure call (RPC) protocol. Primarily this specification defines several data structures and the rules around their processing. It is transport agnostic in that the concepts can be used within the same process, over sockets, over http, or in many various message passing environments. It uses [JSON](http://www.json.org/) ([RFC 4627](http://www.ietf.org/rfc/rfc4627.txt)) as data format.

### Request Formatting

To make a JSON-RPC request, send an HTTP POST request to the `[AIDBOX_BASE_URL]/rpc` endpoint with a `Content-Type: application/json` header. The JSON request data should contain 2 fields:

* `method: <string>`, a string containing the method to be invoked
* `params: <array>`, a JSON object of parameters

Example using curl:

{% tabs %}
{% tab title="Request" %}
```bash
curl "$AIDBOX_BASE_URL/rpc" \
  -H "Content-Type: application/json" \
  -d \
    '{
       "method": "aidbox/echo", 
       "params": {
         "hello": "world"
       }
     }'
```
{% endtab %}

{% tab title="Response (formatted)" %}
```javascript
{
  "result": {
    "hello": "world"
  }
}
```
{% endtab %}
{% endtabs %}

The response output is a JSON object with one of the following fields:

* `result: <array|number|object|string>`, requested data or success confirmation
* `error: <array|number|object|string>`

Requests can be sent in batches by sending an array of JSON-RPC request objects as the data for a single POST.
