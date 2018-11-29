# REST API

This API reference describes basic FHIR endpoints for resources management and retrieval, as well as some custom Aidbox extensions.

### Base URL

In documentation you can find mention of base url or `[base]`. Sometimes it appears in examples:

```
GET [base]/[type]/[id]{?_format=[mime-type]}
```

Basically, it's an address of the box \(an instance of a FHIR server\), but sometimes it's not presented \(for example in REST Console\):

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient
```
{% endtab %}
{% endtabs %}

But it still presented implicitly and full url will be something like `https://<YOUR-BOX>.aidbox.app/Patient`

You may think that base url is equal to value of [Host http header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Host). 

### Errors

Aidbox uses semantic HTTP response codes, which generally means that all `4xx` responses indicates error on client side and all `5xx` responses means server-side error.

Among with HTTP status code you'll get an [OperationOutcome](https://www.hl7.org/fhir/operationoutcome.html) resource in the response with additional error information, such as error message:

```javascript
{
  "resourceType": "OperationOutcome",
  "status": 404,
  "text": "Resource Patient/42 not found"
}
```

### Cross Origin Resource Sharing \(CORS\)

CORS is enabled by default for all Aidbox API endpoints.



