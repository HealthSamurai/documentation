# Aidbox configuration project structure

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference/)
{% endhint %}

Aidbox configuration project consists of a set of files which together describe how a box should behave. Overview is given by the following filesystem tree example:

```
zen-package.edn
zrc/
  system.edn
  box/
    acl.edn
  …
```

The only required names are `zen-package.edn` and `zrc/` directory. As you can see it is just a collection of `.edn` config files put into certain directories. Let’s go through them one by one.

### zen-package.edn

This is a project meta file. Currently it is only used to specify project dependencies.

{% code title="zen-package.edn" %}
```clojure
{:deps {us-core  "https://github.com/zen-fhir/hl7-fhir-us-core.git"
        plan-net "https://github.com/zen-fhir/hl7-fhir-us-davinci-pdex-plan-net.git"}}
```
{% endcode %}

### zrc/

This is a directory with all your configuration files. They specify how a box should behave and what features should be enabled. `zrc/` directory from the example above contains:

* `system.edn` — config file for a system as a whole;
* `box/` — directory with configs for services such as ACL or FHIR IG.\
  Each service config comes with its own set of options.

<table><thead><tr><th>zrc/system.edn</th><th>zrc/box/acl.edn</th></tr></thead><tbody><tr><td><pre class="language-clojure"><code class="lang-clojure">{ns     system
</code></pre></td><td></td></tr><tr><td>import #{aidbox}</td><td></td></tr></tbody></table>

server {...}

box\
{:zen/tags #{aidbox/system}\
:services {:http server …\}}}\
|

```
{ns     box.acl
import #{aidbox.rest.acl}
some-acl-symbol
{…}
…
some-other-acl-symbol
{…}}
```

|

### Examples

{% embed url="https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples" %}
Examples of Aidbox configuration projects in our GitHub repository.
{% endembed %}
