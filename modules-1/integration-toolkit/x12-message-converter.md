# X12 message converter

Aidbox includes a couple of endpoints to allow the conversion of x12 messages.

Right now, the converter only support two types of x12 messages: 835 and 837.

### Parsing a message

To parse a message, use `/x12/parse` endpoint.

```
POST /x12/parse?type=837
content-type: text/plain
accept: application/json

<message>
```

This query returns an object with fields `errors`, `control` and `message`.

If you omit the `type` parameter, parser will try to infer the message type based on the `ST` header. This won't work if the message has several `ST` sections within it.

### Generating a message

`/x12/generate` endpoint allows generating x12 messages from the JSON data obtained from `/x12/parse` operation. Pass the content of the `message` field from the parsing result to `/x12/generate` to get your message back in almost unchanged form.

Keep in mind that the generator disregards the provided segment count value in favor of the one it computes itself.

```
POST /x12/generator
content-type: application/json

<message in JSON format>
```
