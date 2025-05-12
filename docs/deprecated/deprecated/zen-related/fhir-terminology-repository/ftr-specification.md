# FTR Specification

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Broken link](broken-reference)
{% endhint %}

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or contact us if you have questions, feedback, or suggestions.
{% endhint %}

### Introduction

**FTR** (FHIR Terminology Repository) it's a specification for repository layout, formats and algorithms to distribute FHIR terminologies as ValueSets.

Basic unit of distribution is a terminology file which contains _expanded_ ValueSet and CodeSystems used for this ValueSet expansion. _Expanded_ ValueSet is a collection of all Concepts that this ValueSet refers to. _Concept_ must have at least two elements - _system_ and _code_, but may contain other arbitrary properties.

Design of _ValueSets_ is out of the scope of this specification.

FTR defines following entities:

* **Terminology File** format — to store the content of a specific ValueSet version
* **Terminology File Patch** format — changes between two ValueSet versions
* **Terminology File Tag** format — named reference to a specific version and chain of patches
* **Tag Index** format - index of all ValueSets with specific tag
* **Terminology File Update** algorithm - how to update Terminology File via Tag Index & Terminology File Tag
* **Tag Sync** algorithm - how effectively sync many Terminology Files
* **Terminology File Publish** algorithm - how terminology file should be published

### Repository Layout

FTR is a base directory containing module directories (`fhir.r4` or `loinc`). Each module directory contains `vs/` directory with valuesets and `tags/` directory with tag indexes.

```bash
 fhir/ # module
      - tags/ # tags
        - r5.ndjson.gz # tag indexes
        - r4.ndjson.gz
      - vs/ 
        - patient.gender/ # valueset
          - tag.r4.ndjson.gz # vs tag
          - tag.r5.ndjson.gz
          - tf.bc7623b7a94ed3d8feaffaf7580df3eca4f5f5ca.ndjson.gz # vs file
          - tf.e3b0c44298fc1c149afbf4c8996fb92427ae41ca.ndjson.gz
          - patch.bc7623b7a.7ae41ca.ndjson.gz # vs patch
    loinc
      - tags/
        - main.ndjson.gz
      - vs/ 
        - loinc/
          - tag.main.ndjson.gz
          - tf.bc7623b7a94ed3d8feaffaf7580df3eca4f5f5ca.ndjson.gz
```

### Terminology File (TF)

Terminology File (TF) is a gzipped ndjson file. First lines of this file are CodeSystems and ValueSet resources and rest are Concept resources. Concept lines must be sorted lexicographically by `{{system}}-{{code}}` pattern. JSON must be in the [canonical JSON](https://www.rfc-editor.org/rfc/rfc8785) format, i.e. sorted keys and no white-spaces to guarantee reproducible SHA256 hash of files with same content. Terminology File name should follow this pattern `vs.{hash}.ndjson.gz`, where hash is a SHA256 hash of gzipped file content.

### Terminology Patch File (TFP) & Diff Algorithm

#### Diff Algorithm

If you have two versions of Terminology Files you can generate patch file (TFP) in one run with following algorithm:

```
reserve_lines_with_codesystems_and_valueset()
loop( c1 = next_concept(tf1), c2 = next_concept(tf2))
 case
  when is-nil(c1) && is-nil?(c2)
   result
  when is-nil(c1)
   new_concept(c2)
   recur(c1, next_concept(tf2))
  when is-nil(c2)
   remove_concept(c1)
   recur(next_concept(tf1), nil)
  when c1.code&system = c2.code&system
   when c1 not equal c2
    update_concept(c2)
    recur(next_concept(tf1), next_concept(tf2))
  when c1.code&system < c2.code&system
   remove_concept(c1)
   recur(next_concept(tf1), c2)
  when c1.code&system > c2.code&system
   new_concept(c2)
   recur(c1, next_concept(tf2))
```

{% hint style="info" %}
`c1.code&system < c2.code&system` and `c1.code&system > c2.code&system` means you should compare those strings lexicographically.
{% endhint %}

#### Terminology Patch File

Terminology Patch File (TPF) is a gzipped ndjson file. The first line of this file is a JSON object containing the name of the ValueSet for which this patch file was created, and the rest are JSON objects with operations describing how to get a new version of the term file and the contents of the Concept resource.

TPF contains only 3 types of operations:

* `add` — adds new concept
* `update` — update existing concept properties
* `remove` — remove existing concept

#### Terminology Patch File sample

```json
  {"name":"myvs"}
  {"op": "add",    "code":"c1", "display":".."}
  {"op": "remove", "code":"c2", "display":".."}
  {"op": "update", "code":"c3", "display":".."}
```

### ValueSet Tag File (VST)

ValueSet Tag File is gziped ndjson file. Where first line is a hash of latest version of ValueSet and the rest is pointers to patch files. It is used to calculate the effective patch between non-adjacent versions.

```json
{"tag":"fhir.v4", "hash":"{hash-3}"}
{"from": "{hash-1}", "to":"{hash-2}"}
{"from": "{hash-2}", "to":"{hash-3}"}
```

### Tag Index File (TI)

Tag index file is a table of all valueset with specific tag and their actual hashes sorted by valueset name. It is designed to quickly check if anything has changed in the tag and calculate a bulk patch plan.

Client can save last tag index. When client want to check "that something changed" it can compare saved tag index with current tag-index. When tag-indexes are not equal, client can load index file and using previous _VS_ hashes discover which _VS_ should be updated. To update _VS_ client load _VST_ and search for patches to be applied to client version of _VS_ or client may choose just load full version referenced in _VST_.

#### Tag Index File Sample

```json
{"name": "module.myvs-1", "hash":"...."}
{"name": "module.myvs-2", "hash":"...."}
```

### Terminology File Update Algorithm

### Tag Sync Algorithm

### Terminology File Publish Algorithm

To publish TF under specific tag.

* generate TF
* check it does not present in current repo - upload it
* check tag file of this _VS_ pointing to this version
* when not
  * generate patch file from latest version
  * update TFP with new hash and migration
* update TI file and it's hash file
