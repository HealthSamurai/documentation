---
coverY: 0
layout:
  cover:
    visible: false
    size: full
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
---

# Init Bundle

{% hint style="info" %}
Available since the 2411 release.
{% endhint %}

Init Bundle is a simple approach to creating configuration resources when starting Aidbox.&#x20;

It is equivalent to just executing Bundle in Aidbox using `POST /fhir`. There are some differences:

1. It is executed before the internal HTTP server starts, and before the health check response.
2. Unsuccessful execution of the init bundle of type **transaction** prevents Aidbox from starting.
3. Unsuccessful execution of the init bundle of type **batch** triggers warnings in the log; Aidbox continues the startup process.
4. Aidbox startup will be interrupted if the specified file is unavailable or is not a valid bundle resource of transaction or batch type.
5. Only JSON format is supported.

## Usage

Specify `BOX_INIT_BUNDLE` env. The value must be an URL.&#x20;

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

1. First, check that Aidbox handles the Bundle as it should using `POST /fhir`. Try to post it several times to make sure it is idempotent. Then add it to `BOX_INIT_BUNDLE`.
2. Note, that the [Aidbox format](../api/rest-api/other/aidbox-and-fhir-formats.md) is not supported.
3. Aidbox handles an `id` in the body of the POST request. That's why posting the resource with an id twice will cause an `duplicate key` error. Use [conditional create](../api/rest-api/crud/create.md) or [update](../api/rest-api/crud/update.md) for that.

## How to inject env variables into Init Bundle

See [tutorial](../deployment-and-maintenance/deploy-aidbox/how-to-inject-env-variables-into-init-bundle.md).
