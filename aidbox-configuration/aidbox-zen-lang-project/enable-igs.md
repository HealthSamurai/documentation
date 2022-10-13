# Enable IGs

Aidbox configuration project allows you to easily enable [FHIR IGs](https://www.hl7.org/fhir/implementationguide.html) which extend basic FHIR functionality with a variety of features including additional profiles and terminologies. Currently Aidbox can only work with a subset of IG specification but we plan to extend it in the future.

Currently supported list of features:

* **BaseProfiles** — enable base FHIR resources validation
* **Extensions** — enable [First-Class Extensions](../../modules-1/first-class-extensions.md) with validation&#x20;
* **Profiles** — enable validation via `meta.profile`
* **Searches** (_partial support_) — enable search
* **CodeSystems & ValueSets** (_partial support_) — enable lookup and binding validation

## How to enable an IG

You can enable FHIR IGs you want to use by specifying dependencies in the `zen-package.edn`.&#x20;

#### Specify zen FHIR IGs in your zen-package.edn

Here's an example for enabling US Core IG and DaVinci PDEX Plan Net:

{% code title="zen-package.edn" %}
```clojure
{:deps {us-core  "https://github.com/zen-fhir/hl7-fhir-us-core.git"
        plan-net "https://github.com/zen-fhir/hl7-fhir-us-davinci-pdex-plan-net.git"}}
```
{% endcode %}

{% hint style="info" %}
To the left of each zen FHIR IG URL you give a name. In this example the names are `us-core` and `plan-net`. These names are arbitrary and currently are used only to annotate a URL.
{% endhint %}

#### Commit changes to your Aidbox configuration project

Don't forget to do `git commit` after you made changes to your Aidbox configuration project. To commit you can execute the following command inside your Aidbox configuration project directory:

```bash
git add zen-package.edn && git commit -m "Add IG dependencies"
```

## Available IGs

View the gallery of available FHIR IGs in our GitHub repository:

{% embed url="https://github.com/orgs/zen-fhir/repositories" %}
Gallery of available FHIR IGs&#x20;
{% endembed %}
