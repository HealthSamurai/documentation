# FTR Manifest

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Broken link](broken-reference)
{% endhint %}

You can declare FTR manifests in your ValueSet definitions, this manifest will determine source of data for expanded ValueSet and what the resulting FTR will look like.

The FTR manifest contains the following properties:

* `:module` — defines the module name, ValueSet folders will be contained inside the folders with the specified `:module` and `:tag` names (e.g `module/tag/my-valueset`)
* `:source-url` — path to source data (e.g. CSV file, IG folder)
* `:source-type` — `:flat-table`, `:ig`
* `:ftr-path` — path where the resulting FTR will be created
* `:tag` — under which tag the ValueSets will be created
* `:extractor-options` — options that will be applied to the FTR extractor engine
