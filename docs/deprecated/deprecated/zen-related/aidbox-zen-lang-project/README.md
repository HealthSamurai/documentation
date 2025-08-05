---
description: >-
  Aidbox configuration project refers to a set of configuration files used to
  set up an Aidbox instance
---

# Aidbox Configuration Project

{% hint style="warning" %}
Aidbox is transitioning to the FHIR Schema engine. Existing zen and Entity/Attribute (EA) engines are deprecated and will be obsolete starting August 2025. July 2025 version will become LTS supporting zen and Entity/Attributes for 2 years.

[Read full announcement](https://www.health-samurai.io/news/aidbox-transitions-to-the-fhir-schema-engine)\
\
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

**Aidbox configuration project** is a directory containing configuration files written in [Zen](https://github.com/zen-lang/zen) language. Don’t worry if you are not familiar with Zen yet — we’ll explain all the necessary details along the way. For now you can think of it as a simple syntax well-suited for specifying all sorts of configs.

With Aidbox configuration project, you can configure all the things you need for a functioning Aidbox instance, including [API endpoints](broken-reference), [authorization flows](broken-reference), [FHIR IGs](../profiling-with-zen-lang/README.md) and more.

Once you set up your project for one Aidbox instance you can reuse it across multiple instances — they all will be configured in the exact same way. This feature is especially useful for testing purposes. Using a single configuration project, you can be sure that your CI pipeline has the same setup as your deployed instance.

{% hint style="warning" %}
Aidbox configuration project is sometimes referred to as Aidbox Project or just project. It should not be confused with Aidbox User Portal projects containing licenses.
{% endhint %}

{% content-ref url="aidbox-configuration-project-structure.md" %}
[aidbox-configuration-project-structure.md](aidbox-configuration-project-structure.md)
{% endcontent-ref %}

## Features supported by configuration projects

{% content-ref url="enable-igs.md" %}
[enable-igs.md](enable-igs.md)
{% endcontent-ref %}

{% content-ref url="seed-v2.md" %}
[seed-v2.md](seed-v2.md)
{% endcontent-ref %}

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## Use Aidbox configuration project

Aidbox configuration project is simply a directory of config files. In order to use it with Aidbox you just need to tell the latter where it is located and how it can be accessed.

Currently Aidbox configuration projects are supported by self-hosted instances and Aidbox User Portal Sandbox instances. Support for AWS Marketplace installations is coming soon.

{% hint style="info" %}
Configuration projects can be provided either as a git repository URL or as a local path. We recommend keeping your project in git as source control allows you to easily track changes, revert them or switch between branches.
{% endhint %}

### Set up a configuration project

Setting up a self-hosted project is done by providing access to project files.

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

### Production installation

A production installation is the same as a regular installation, except when your production environment has no access to the git repo containing your configuration project. For these cases Aidbox supports [other methods of providing configuration projects](https://github.com/Aidbox/documentation/blob/master/aidbox-configuration/aidbox-zen-lang-project/broken-reference/README.md).

### Configuration projects for Sandbox instances on Aidbox user portal

1. Proceed to creating a new license
2. Select either a _Standard_ or _Development_ license type and **Sandbox** hosting
3. Choose which projects to enable in FHIR Server Configuration section.

Currently available projects include various FHIR IG [zen packages](enable-igs.md) and [Aidbox SDC forms](../../../../tutorials/other-tutorials/sdc-with-custom-resources.md).

{% hint style="warning" %}
FHIR terminologies from IG packages are not loaded into the Sandbox database. This means they won’t be available for introspection. You can still use them for validation.

If you want to have them in the database, you need to configure projects for a local instance.
{% endhint %}

### Configuration projects in AWS Marketplace Aidbox SaaS

{% hint style="info" %}
Aidbox configuration projects will be supported on AWS Marketplace in upcoming releases.
{% endhint %}

### Configuration projects in self-hosted instances

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## Examples

See [examples of Aidbox configuration projects in our GitHub repository](https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples).

## Common errors

```
Entrypoint 'smartbox.portal' not loaded.

{:message "No file for ns 'smartbox.portal", :missing-ns smartbox.portal, :ns smartbox.portal}
```

Meaning: Wrong zen source files path

```
Cloning into '/tmp/aidbox-project-git'...

remote: Repository not found.

fatal: repository 'https://github.com/Aidfdfdfbox/aidbox-project-samples.git/' not found
```

Meaning: Incorrect git repo url

```
error: pathspec 'git-project-unexist' did not match any file(s) known to git
```

Meaning: Incorrect checkout branch/commit
