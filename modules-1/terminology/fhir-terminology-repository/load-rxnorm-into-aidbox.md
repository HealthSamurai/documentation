# Load RxNorm into Aidbox

RxNorm is a large medical terminology which can be used in [FHIR ValueSet](http://hl7.org/fhir/valueset.html) resources. It is distributed under a free license.

We provide out-of-the-box integration with RxNorm through [Aidbox Configuration project](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project).&#x20;

{% hint style="warning" %}
This bundle contains only codes with SAB(Source Abbreviation) = RXNORM
{% endhint %}

## Setting up Aidbox configuration project

To set up the Aidbox configuration project, carefully follow [this](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md) guide.&#x20;

During the step labeled `Configure the Aidbox` instead of cloning the proposed configuration projects, clone the following pre-packaged configuration project with the RxNorm-related configuration:

```sh
git clone \
  https://github.com/Panthevm/aidbox-project-template-rxnorm.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

### Configuration overview

#### Added RxNorm dependency to configuration project

{% code title="zen-package.edn" %}
```
{:deps {rxnorm "https://github.com/zen-fhir/rxnorm.git"}}
```
{% endcode %}

#### Imported RxNorm namespace to configuration project entrypoint

<pre><code>{ns main
 import #{aidbox
          config
          <a data-footnote-ref href="#user-content-fn-1">rxnorm</a>}
 …}
</code></pre>

#### FTR Pull Feature — instruct Aidbox to load concepts into the database

By default, Aidbox does not load terminologies into the database as that can take a lot of disk space. This means that full terminology functionality won’t be available until you enable it manually. When you set it to `true`, Aidbox will load terminologies into the database on the next startup and start functioning as a fully-featured terminology server.

To achieve that we set `ftr.pull.enable` to true in `features` map.

{% code title="zrc/config.edn" %}
```
 features
 {:zen/tags #{aidbox.config/features}
  :ftr {:pull {:enable true}}}
```
{% endcode %}

[^1]: Namespace we've imported
