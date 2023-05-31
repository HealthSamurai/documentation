# Add US Core IG to a running Aidbox instance

## Steps

{% hint style="warning" %}
Currently it is only possible to use FHIR IGs for self-hosted installations. We are working to add support for other distributions as well.
{% endhint %}

{% hint style="info" %}
You are advised not to start Aidbox until all steps are finished. Otherwise you risk to start a misconfigured instance.
{% endhint %}

You need to go through the following steps:

1. Set up base Aidbox instance
2. Specify US Core as a dependency
3. Commit changes to your Aidbox configuration project
4. Restart Aidbox and verify that IGs are enabled

All of them are covered in a greater detail below.

{% hint style="info" %}
If you are new to Aidbox it is strongly recommended not to skip any steps. Otherwise you may end up with a misconfigured instance.
{% endhint %}

### Set up base Aidbox instance

Check out our “Getting Started” guides. One of the easiest ways is by following this [guide](../../../getting-started/run-aidbox-locally-with-docker.md).

### Specify US Core as a dependency

Put the following in `project/zen-package.edn`:

```clojure
{:deps {hl7-fhir-us-core "https://github.com/zen-fhir/hl7-fhir-us-core.git"}}
```

Go to the main configuration file for your system (`project/zrc/system.edn` if you followed the guide above) and add `hl7-fhir-us-core` namespace to imports. It should look like this:

```clojure
{…
 :import #{aidbox hl7-fhir-us-core maybe-some-other-deps}
 …}
```

Aidbox configuration project is a git repository so you should commit the changes maybe so that it becomes aware of the new dependency:

```shell
cd project && git add . && git commit -m "Add us-core dependency" && cd ..
```

Done. Now you can start Aidbox. Check “Profiles” page in the Aidbox UI to see that it works — \`hl7-fhir-us-core\` should be listed among other profiles.

Make the following HTTP requests to verify that validating with US Core works. You can use Aidbox REST Console.

{% code title="Status: 201" %}
```
POST /Patient
content-type: text/yaml
accept: text/yaml

meta:
  profile:
    - "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"
identifier:
  - {system: "some-system", value: "unique-value"}
name:
  - {use: "anonymous"}
gender: "other"
```
{% endcode %}

{% code title="Status: 422" %}
```
POST /Patient
content-type: text/yaml
accept: text/yaml

meta:
  profile:
    - "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"
identifier:
  - {system: "some-system", value: "unique-value"}
name:
  - {use: "anonymous"}
gender: "this-gender-is-not-known"
```
{% endcode %}

{% hint style="warning" %}
If you put a dependency while Aidbox is running you need to restart it so that it pulls the dependency and consumes it.
{% endhint %}

### Restart Aidbox

Restart Aidbox by running the following command:

```shell
docker compose down && docker compose up -d
```

#### Database implications

Note that US Core IG takes about 1.5GB in the database. You can significantly reduce this size by disabling the loading of Concepts, ValueSets and CodeSystems. For that you need to set `BOX_FEATURES_FTR_PULL_ENABLE=true|false`.

You’ll still be able to [validate codes from ValueSets](../../../modules-1/terminology/valueset/value-set-validation.md) but [Concept lookups](../../../modules-1/terminology/codesystem-and-concept/concept-lookup.md) and [ValueSet expansions](../../../modules-1/terminology/valueset/value-set-expansion.md) as well as `GET /Concept`, `GET /ValueSet` and `GET /CodeSystem` endpoints won’t work. Lookups and expansions without loading into database are currently work in progress

### Verify that US Core IG works

{% hint style="warning" %}
By default, Aidbox does not load terminologies into the database in order to save the disk space. This still allows you to use them for validation but terminology server functionality won’t be available. If you do wish to load the terminologies, you need to set `BOX_FEATURES_FTR_PULL_ENABLE=true` environment variable.
{% endhint %}

Go to “Profiles” page in the Aidbox UI (`http://localhost:8888/ui/zen-ui`) and make sure that `hl7-fhir-us-core` is listed among the namespaces.

You can also verify that validation with US Core IG works. For that you can make the following HTTP requests and check that response statuses are returned as expected.

{% code title="Status: 201" %}
```
POST /Patient
content-type: text/yaml
accept: text/yaml

meta:
  profile:
    - "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"
birthsex: "F"
gender: "female"
name:
  - {use: "anonymous"}
identifier:
  - {system: "some-system", value: "unique-value"}
```
{% endcode %}

This request checks that valid values for `birthsex` field are allowed.

{% code title="Status: 422" %}
```
POST /Patient
content-type: text/yaml
accept: text/yaml

meta:
  profile:
    - "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"
birthsex: "SOMETHING-UNKNOWN"
gender: "female"
name:
  - {use: "anonymous"}
identifier:
  - {system: "some-system", value: "unique-value"}
```
{% endcode %}

This request checks that invalid values for `birthsex` field are not allowed.

## Resources

If you want to learn more about working with IGs in Aidbox, there is a special page dedicated to it.

{% content-ref url="../../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md" %}
[enable-igs.md](../../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md)
{% endcontent-ref %}
