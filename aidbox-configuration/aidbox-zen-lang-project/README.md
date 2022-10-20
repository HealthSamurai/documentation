---
description: >-
  Aidbox configuration project refers to a set of configuration files used to
  set up an Aidbox instance
---

# Aidbox configuration project

**Aidbox configuration project** is a directory which contains configuration files written in [Zen](https://github.com/zen-lang/zen) language. Don’t worry if you are not yet familiar with Zen — we’ll explain all the necessary details along the way. For now you can think of it as a simple syntax well-suited for specifying all sorts of configs.

With Aidbox configuration project you can configure all things you need to have a functioning Aidbox instance including [API endpoints](../aidbox-api-constructor.md), [authorization flows](../../security-and-access-control-1/acl.md), [FHIR IGs](../../profiling-and-validation/profiling-with-zen-lang/) and more.

Once you set up your project for one Aidbox instance you can reuse it across multiple instances — they all will be configured in exactly the same way. This feature is especially useful for testing purposes. By providing a single directory you can be sure that your CI pipeline has the same setup as your deployed instance.

{% hint style="warning" %}
Aidbox configuration project is sometimes referred to as Aidbox Project or just project. It should not be confused with Aidbox User Portal projects containing licenses.
{% endhint %}

{% content-ref url="aidbox-configuration-project-structure.md" %}
[aidbox-configuration-project-structure.md](aidbox-configuration-project-structure.md)
{% endcontent-ref %}

### Features supported by configuration projects

{% content-ref url="enable-igs.md" %}
[enable-igs.md](enable-igs.md)
{% endcontent-ref %}

{% content-ref url="seed-v2.md" %}
[seed-v2.md](seed-v2.md)
{% endcontent-ref %}

{% content-ref url="../aidbox-api-constructor.md" %}
[aidbox-api-constructor.md](../aidbox-api-constructor.md)
{% endcontent-ref %}

{% content-ref url="rpc-api.md" %}
[rpc-api.md](rpc-api.md)
{% endcontent-ref %}

## Use Aidbox configuration project

Aidbox configuration project is simply a directory with files so to use it in Aidbox you just need to tell where it is located and how it can be accessed.

Currently Aidbox configuration projects are supported only for self-hosted instances. Support for Cloud and AWS Marketplace installations is coming soon.

{% hint style="info" %}
Configuration projects can be provided either as a git repository url or as a local path. We recommend to keep your project in git as the source control allows you to easily track changes, revert them or switch between branches.
{% endhint %}

### Set up a configuration project

Setting up a self-hosted project is done by providing access to a project files.&#x20;

{% content-ref url="setting-up-a-configuration-project.md" %}
[setting-up-a-configuration-project.md](setting-up-a-configuration-project.md)
{% endcontent-ref %}

### Production installation

Production installation is the same as a regular installation. Exception can be the case when your production environment has no access to a git repo containing your project. For these cases Aidbox supports [other methods of providing configuration projects](setting-up-a-configuration-project.md#alternative-ways-to-provide-aidbox-configuration-project).

### Use configuration projects on user portal (aidbox.app)

{% hint style="info" %}
Aidbox configuration projects will be supported on aidbox.app in upcoming releases.
{% endhint %}

### Configuration projects in AWS Marketplace Aidbox SaaS&#x20;

{% hint style="info" %}
Aidbox configuration projects will be supported on AWS Marketplace in upcoming releases.
{% endhint %}

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
