# RPC API

{% hint style="info" %}
This is a beta feature, available only on **edge** channel. Please join [the discussion](https://github.com/Aidbox/Issues/discussions/430) or [contact](../contact-us.md) us if you want to contribute.
{% endhint %}

With release 2108 we introduce RPC API. JSON-RPC is a stateless, light-weight remote procedure call \(RPC\) protocol. Primarily this specification defines several data structures and the rules around their processing. It is transport agnostic in that the concepts can be used within the same process, over sockets, over http, or in many various message passing environments. It uses [JSON](http://www.json.org/) \([RFC 4627](http://www.ietf.org/rfc/rfc4627.txt)\) as data format.

### Methods



### Request Formatting

To make a JSON-RPC request, send an HTTP POST request with a `Content-Type: application/json` header. The JSON request data should contain 4 fields:

* `jsonrpc: <string>`, set to `"2.0"`
* `id: <number>`, a unique client-generated identifying integer
* `method: <string>`, a string containing the method to be invoked
* `params: <array>`, a JSON array of ordered parameter values

Example using curl:

The response output will be a JSON object with the following fields:

* `jsonrpc: <string>`, matching the request specification
* `id: <number>`, matching the request identifier
* `result: <array|number|object|string>`, requested data or success confirmation

Requests can be sent in batches by sending an array of JSON-RPC request objects as the data for a single POST.

