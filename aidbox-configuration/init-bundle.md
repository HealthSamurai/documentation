# Init Bundle

{% hint style="info" %}
Init Bundle has been available since the 2410 release.
{% endhint %}

Init Bundle is a simple approach to creating configuration resources when starting Aidbox.&#x20;

It is equivalent to just executing Bundle in live Aidbox using `POST /fhir`. There are only three differences:

1. It is executed before the internal HTTP server starts, and before the health check works.
2. If the bundle is a **transaction** bundle, and it is created unsuccessfully, Aidbox exits with an error; otherwise, it continues startup.
3. Only JSON format is supported.

{% hint style="info" %}
If the bundle file can not be found, Aidbox starts as usual.
{% endhint %}

## Usage

Add `BOX_INIT_BUNDLE` env. The value must be a one URL.&#x20;

```
BOX_INIT_BUNDLE=<URL>
```

Examples of URLs:

* `file:///tmp/bundle.json`
* `https://storage.googleapis.com/<BUCKET_NAME>/<OBJECT_NAME>`

## Example

If a Bundle file is created at `/tmp/bundle.json`:

```json
{
  "type": "batch",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "/Observation",
        "ifNoneExist": "_id=o1"
      },
      "resource": {
        "id": "o1",
        "code": {
          "text": "text"
        },
        "status": "final",
        "effectiveDateTime": "2000-01-01"
      }
    }
  ]
}
```

Aidbox will apply it if the `BOX_INIT_BUNDLE` is set to:

```
BOX_INIT_BUNDLE=file:///tmp/bundle.json
```

## Hints

1. First, check that Aidbox handles the Bundle as it should in live mode. Try to post it several times to make sure it is idempotent. Then add it to `BOX_INIT_BUNDLE`.
2. Note, that the Aidbox format is not supported, because `POST /fhir` does not respect it.
3. Aidbox respects `id` in body of the POST request. That's why posting the resource with id twice will cause an `duplicate key` error. Use [conditional create](../api-1/api/crud-1/fhir-and-aidbox-crud.md#conditional-create) or [update](../api-1/api/crud-1/update.md) for that.
