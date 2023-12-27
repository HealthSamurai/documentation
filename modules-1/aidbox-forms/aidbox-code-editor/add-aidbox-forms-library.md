---
description: Setup and use Aidbox Forms library in your SDC project
---

# Add Aidbox Forms Library

{% hint style="info" %}
When you use [aidbox-zen-sdc](https://github.com/HealthSamurai/aidbox-zen-sdc) project as your project configuration - it already has aidbox-forms-library depency and several forms enabled.
{% endhint %}

### Use library forms

To use aibox forms library you need to add it to your project dependencies and enable needed forms.

#### Add library to project dependencies

\
{PROJECT\_ROOT}/zen-package.edn

```clojure
{:deps {aidbox-forms "https://github.com/Aidbox/sdc-forms-library"}}

```

#### Enable forms

To enable forms you need add them to import section of your project-entrypoint namespace \
(or some different namespace which will be loaded)

a) You can import them all by importing library root namespace

```
{ns sdc-box
 import #{aidbox.forms}
```

b) Or for more precise control - you can add them one by one.

```
{ns sdc-box
 import #{aidbox.forms.vitals 
          aidbox.forms.phq2phq9}
```

After that, you can go to Aibox Forms UI and see

### Change Library Forms

If you need to change one of the library form - you should copy it (as file) from library to your project `/zrc` directory and edit it there. To preserve your changes you commit them to git and push to remote git-repo.

{% hint style="info" %}
Library Forms should not be edited manually - only used. Because these changes will not be pushed to upstream. And after restart you will get clean state of these forms.
{% endhint %}

