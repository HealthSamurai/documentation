# Enable IGs

Aidbox configuration project allows you to easily enable [FHIR IGs](https://www.hl7.org/fhir/implementationguide.html) which extend basic FHIR functionality with a variety of features including additional profiles and terminologies. Currently Aidbox can only work with a subset of IG specification but we plan to extend it in the future.

Currently supported list of features:

* **BaseProfiles** — enable base FHIR resources validation
* **Extensions** — enable [First-Class Extensions](../../modules-1/first-class-extensions.md) with validation&#x20;
* **Profiles** — enable validation via `meta.profile`
* **Searches** (_partial support_) — enable search
* **CodeSystems & ValueSets** (_partial support_) — enable lookup and binding validation

## Enable IG

You can enable FHIR IGs you want to use by specifying dependencies in `zen-package.edn`. Here's an example for enabling US Core IG and Plan Net IG:

{% code title="zen-package.edn" %}
```clojure
{:deps {us-core  "https://github.com/zen-fhir/hl7-fhir-us-core.git"
        plan-net "https://github.com/zen-fhir/hl7-fhir-us-davinci-pdex-plan-net.git"}}
```
{% endcode %}

{% hint style="info" %}
Don't forget to commit changes after you made changes to your Aidbox configuration project:

`git add zen-package.edn && git commit -m "Add IG dependencies"`
{% endhint %}

## Available IGs

View the gallery of available FHIR IGs in our GitHub repository:

{% embed url="https://github.com/orgs/zen-fhir/repositories" %}
Gallery of available FHIR IGs&#x20;
{% endembed %}
