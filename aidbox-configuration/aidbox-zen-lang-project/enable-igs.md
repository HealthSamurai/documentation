# Enable IGs

{% hint style="info" %}
This feature is currently available on the Aidbox`:latest` and `:edge` channels.
{% endhint %}

Aidbox configuration project allows you to easily enable [FHIR IGs](https://www.hl7.org/fhir/implementationguide.html) which extend basic FHIR functionality with a variety of features including additional profiles and terminologies. Currently Aidbox can only work with a subset of IG specification but we plan to extend it in the future.

Currently supported list of features:

* **BaseProfiles** — enable base FHIR resources validation
* **Extensions** — enable [First-Class Extensions](../../modules-1/first-class-extensions.md) with validation&#x20;
* **Profiles** — enable validation via `meta.profile`
* **Searches** (_partial support_) — enable search
* **CodeSystems & ValueSets** (_partial support_) — enable lookup and binding validation

## Available IGs

View the gallery of available FHIR IGs in our GitHub repository:

{% embed url="https://github.com/orgs/zen-fhir/repositories" %}

## How to enable IG

You can enable FHIR IGs you want to use by specifying dependencies in the `zen-package.edn`.

You need to go through the following steps:

1. Specify zen FHIR IGs in your `zen-package.edn`
2. Import the zen FHIR IGs entrypoints
3. Commit changes to your Aidbox configuration project
4. Restart Aidbox and verify that IGs are enabled

All of them are covered in a greater detail below.

{% hint style="warning" %}
Zen FHIR packages require `AIDBOX_CORRECT_AIDBOX_FORMAT=true` environment variable.
{% endhint %}

### Specify zen FHIR IGs in your zen-package.edn

Here's an example for enabling US Core IG and DaVinci PDEX Plan Net:

{% code title="zen-package.edn" %}
```clojure
{:deps {hl7-fhir-us-core  "https://github.com/zen-fhir/hl7-fhir-us-core.git"
        hl7-fhir-us-davinci-pdex-plan-net "https://github.com/zen-fhir/hl7-fhir-us-davinci-pdex-plan-net.git"}}
```
{% endcode %}

Or another example for enabling FHIR r4 core IG:

{% code title="zen-package.edn" %}
```clojure
{:deps {hl7-fhir-r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"}}
```
{% endcode %}

{% hint style="info" %}
To the left of each zen FHIR IG URL you give a name. In the examples above the names are `r4-core`, `us-core` and `plan-net`. These names are arbitrary and currently are used only to annotate a URL.
{% endhint %}

### Import the zen FHIR IGs entrypoints

To enable zen FHIR IGs you provided you need to import them inside of your [entrypoint](broken-reference).

Using the example from [the getting started page](../../getting-started/run-aidbox-locally-with-docker.md#create-and-set-up-your-aidbox-configuration-project) the updated entrypoint file will look like this for both of the examples above:

| r4 core                                                                                                                                                    | us-core and plan-net                                                                                                                                                                                |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| <pre class="language-clojure"><code class="lang-clojure">{:ns system
 :import #{aidbox hl7-fhir-r4-core}
 box
 {:zen/tags #{aidbox/system}}}
</code></pre> | <pre class="language-clojure"><code class="lang-clojure">{:ns system
 :import
 #{aidbox
   hl7-fhir-us-core
   hl7-fhir-us-davinci-pdex-plan-net}
 box
 {:zen/tags #{aidbox/system}}}
</code></pre> |

### Commit changes to your Aidbox configuration project

Don't forget to do `git commit` after you made changes to your Aidbox configuration project. To commit you can execute the following command inside your Aidbox configuration project directory:

```bash
git add zen-package.edn && git commit -m "Add IG dependencies"
```

### Restart Aidbox

After you changed `zen-package.edn` you need to restart Aidbox for your changes to be applied.

### Verify that IGs are enabled

After Aidbox restarted with a new configuration you can use profiles, terminology and other IG features.

{% hint style="warning" %}
By default, Aidbox does not load terminologies into the database in order to save the disk space. This still allows you to use them for validation but terminology server functionality won’t be available. If you do wish to load the terminologies, you need to set `BOX_FEATURES_FTR_PULL_ENABLE=true` environment variable.
{% endhint %}

To see currently loaded profiles you can go to the `profiles` UI page:

<img src="../../.gitbook/assets/image (4) (1).png" alt="" data-size="original">             ![](<../../.gitbook/assets/image (3) (3).png>)

If you want to verify that validation with IGs work, you can create FHIR resource using FHIR REST API which will validate provided resource against specified Profile.

For example, if you want to check that US Core IG works, you can send the following requests and check that response statuses are as expected.

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

### Development and production tips

If you want some tips regarding development and production usage, visit the links below:

* [Development tips](setting-up-a-configuration-project.md#tips-for-local-development)
* [Production tips](setting-up-a-configuration-project.md#tips-for-production)
