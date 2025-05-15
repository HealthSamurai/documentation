# Handling hierarchies using ancestors

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or contact us if you have questions, feedback, or suggestions.
{% endhint %}

{% hint style="info" %}
If your current terminology (ICD-10, Loinc, RxNorm, etc.) based on `hierarchy` attribute you may [request](../../../overview/contact-us.md) to update this terminology
{% endhint %}

### Ancestors

We've introduced new way of handling hierarchies in Aidbox two-phase terminology approach. Previously we used only `Concept.hierarchy` attribute but this approach had a few limitations: for complex ontology-like terminologies (such as [SnomedCT](https://www.snomed.org/)) it didn't allow to support several value set filter operations such as `child-of`, `generalizes`, `descendent-leaf`.

Use of `ancestors` attribute is optional and we still support `hierarchy` attribute as well. For all terminologies provided since Aidbox `July 2022 latest` release `ancestors` attribute will be included by default instead of `hierarchy` attribute.

For example, source terminology structure looks as follows:

![](<../../../../.gitbook/assets/2016f335c4c14019b7cf7447fa56f4c5.png>)

This is how ancestors hierarchy is supposed to be represented in Aidbox `Concept` resource:

{% code title="Concepts" %}
```json
{"code": "code1"}
{"code": "code1.1",  "ancestors":  {"code1": 1}}
{"code": "code1.2",  "ancestors":  {"code1": 1}}
{"code": "code1.1.1", "ancestors": {"code1": 2, "code1.1": 1}}
{"code": "code1.2.1", "ancestors": {"code1": 2, "code1.1": 1, "code1.2": 1}}
```
{% endcode %}

Ancestors attribute is an object which stores all concept ancestors and number of hops between them, where key is an ancestor code and value is the number of hops.

### How to use ancestors attribute for ValueSet declaration & expansion

If you want to declare ValueSet that based on concepts with ancestors attribute you should add `"use-ancestors"` resource meta tag.

```yaml
resourceType: ValueSet
meta:
  tag:
  - code: use-ancestors
id: code1.1-descendents
url: http://example.org/code1.1-descendents
status: draft
compose:
  include:
  - system: http://example.org/CodeSystem
    filter:
    - property: code
      op: is-a
      value: 'code1.1'

```
