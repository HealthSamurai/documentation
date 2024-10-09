# Import via FTR

[Aidbox Configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/) is used to configure Aidbox with various features. One of the things it’s used for is providing Aidbox with external terminologies.

## Import terminologies using FTR

[FHIR Terminology Repository](../fhir-terminology-repository/), or FTR, is an efficient way to store and handle terminologies. It is the recommended way to work with CodeSystem and ValueSet resources in Aidbox. You can start using it by providing [Aidbox Configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/) set up with FTR.

{% content-ref url="../fhir-terminology-repository/creating-aidbox-project-with-ftr/" %}
[creating-aidbox-project-with-ftr](../fhir-terminology-repository/creating-aidbox-project-with-ftr/)
{% endcontent-ref %}

## Import terminology bundles

{% hint style="warning" %}
This approach is deprecated. Its support may end in the near future.
{% endhint %}

Specify path or url to zen terminology bundle in [`AIDBOX_ZEN_PATHS` environment variable](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project#load-project-using-environment-variables).\
Source is either `url` or `path`. `url` is used to download Aidbox project from a remote location; `path` is used to load Aidbox project from the filesystem.\
\
Aidbox imports terminology bundles found in zen paths. Bundles are just `.ndjson.gz` files with filenames matching `*terminology-bundle.ndjson.gz` wildcard. By default, the import is done asynchronously and you can [track the progress](import-using-an-aidbox-project.md#undefined). In some cases (e.g CI/CD pipeline) you might want to override such behavior. Setting `BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC` environment variable will change the import mode to synchronous.

### Import terminology bundle from local system using Aidbox project

Aidbox reads files from [Aidbox projects](../../../aidbox-configuration/aidbox-zen-lang-project/#aidbox-project) on startup and imports files whose names end with `terminology-bundle.ndjson.gz` .

For example, path to terminology bundle GZIP fie is: `/my/aidbox/project/my-terminology-bundle.ndjson.gz`.

To load the terminology bundle into Aidbox set the following environment variables:

```
AIDBOX_ZEN_PATHS=path:dir:/my/aidbox/project
BOX_FEATURES_FTR_PULL_ENABLE=true
```

You can read more about [AIDBOX\_ZEN\_PATHS](../../../reference/configuration/environment-variables/aidbox-project-environment-variables.md) and [BOX\_FEATURES\_FTR\_PULL\_ENABLE](../../../reference/configuration/environment-variables/ftr.md) in our [configuration reference](../../../reference/configuration/).

{% hint style="warning" %}
Make sure that your Aidbox container [has access to the specified path](../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md#setup-devbox-to-use-zen-project) on your system.
{% endhint %}

### Import terminology bundle from a remote location

Zip-archive your terminology bundle that is my-terminology-budnle.ndjson.gz file in my-terminology.zip file.

Upload your my-terminology.zip to a remote server e.g. https://example.com/terminologies/my-terminology.zip

Set [`AIDBOX_ZEN_PATHS` environment variable:](../../../aidbox-configuration/aidbox-zen-lang-project/#aidbox\_zen\_paths)

```
AIDBOX_ZEN_PATHS=url:zip:https://example.com/terminologies/my-terminology.zip
```

Aidbox will import terminology from the specified path on start.

### Track import progress

Aidbox offers an RPC method to track the async import progress. The response shows bundles in the import queue, failed imports and currently importing bundle.

Request:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox.zen/terminology-bundle-import-status
params: {}
```

Response example:

```yaml
result:
  pending:
    - hl7-fhir-us-core-terminology-bundle.ndjson.gz
  fail:
    - us-nlm-vsac-terminology-bundle.ndjson.gz
  in-progress:
    - hl7-fhir-r4-core-terminology-bundle.ndjson.gz
```
