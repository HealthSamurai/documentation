# Enable IGs

{% hint style="info" %}
This feature currently available on the Aidbox`:latest` and `:edge` channels.
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

## How to enable an IG

You can enable FHIR IGs you want to use by specifying dependencies in the `zen-package.edn`.&#x20;

### Specify zen FHIR IGs in your zen-package.edn

Here's an example for enabling US Core IG and DaVinci PDEX Plan Net:

{% code title="zen-package.edn" %}
```clojure
{:deps {us-core  "https://github.com/zen-fhir/hl7-fhir-us-core.git"
        plan-net "https://github.com/zen-fhir/hl7-fhir-us-davinci-pdex-plan-net.git"}}
```
{% endcode %}

Or another example for enabling FHIR r4 core IG:

```
{:deps {r4-core "https://github.com/zen-fhir/hl7-fhir-r4-core.git"}}
```

{% hint style="info" %}
To the left of each zen FHIR IG URL you give a name. In the examples above the names are `r4-core`, `us-core` and `plan-net`. These names are arbitrary and currently are used only to annotate a URL.
{% endhint %}

### Import the zen FHIR IGs entrypoints

To enable zen FHIR IGs you provided you need to import them inside of your [entrypoint](setting-up-a-configuration-project.md#set-system-entrypoint).

Using the example from [the getting started page](../../getting-started/run-aidbox-locally-with-docker/#create-and-set-up-your-aidbox-configuration-project) the updated entrypoint file will look like this for both of the examples above:

| r4 core                                                                                                                                                   | us-core and plan-net                                                                                                                                                                               |
| --------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| <pre class="language-clojure"><code class="lang-clojure">{:ns system
 :import #{aidbox hl7-fhir-r4-core}
 box
 {:zen/tags #{aidbox/system}}}</code></pre> | <pre class="language-clojure"><code class="lang-clojure">{:ns system
 :import
 #{aidbox
   hl7-fhir-us-core
   hl7-fhir-us-davinci-pdex-plan-net}
 box
 {:zen/tags #{aidbox/system}}}</code></pre> |

### Commit changes to your Aidbox configuration project

Don't forget to do `git commit` after you made changes to your Aidbox configuration project. To commit you can execute the following command inside your Aidbox configuration project directory:

```bash
git add zen-package.edn && git commit -m "Add IG dependencies"
```

### Restart Aidbox

Currently, after you changed `zen-package.edn`, you need to restart Aidbox for your changes to be applied.

### Check if an IG is enabled

After Aidbox restarted with a new configuration you can use profiles, terminology and other IG features.

To see currently loaded profiles you can go to the `profiles` UI page:

<img src="../../.gitbook/assets/image (4).png" alt="" data-size="original">             ![](<../../.gitbook/assets/image (3).png>)
