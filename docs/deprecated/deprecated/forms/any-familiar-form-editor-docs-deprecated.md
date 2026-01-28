---
description: >-
  This article outlines the basic steps to start designing form locally in any
  familiar editor
hidden: true
---

# Any familiar form editor (docs deprecated)

{% hint style="warning" %}
We strongly recommend using our UI Form Builder based on FHIR SDC (Structured Data Capture), which we are continuously developing. The current solution will remain supported but will not receive further development at this time.
{% endhint %}

## Create file.edn with form name in zrc/ forms directory

Example:

```
...zrc/ forms

allergies.edn
vitals.edn
....
```

{% hint style="warning" %}
Don't forget to import the created form into the forms.edn file so that the form is loaded after restarting the program.
{% endhint %}

Example

```
{ns forms
 import #{forms.vitals
          forms.phq2phq9
          forms.gad-7
          forms.allergies
          forms.ros
          forms.physical-exam
          forms.annual-wellness-visit}
 }
```

## To design the form, it is necessary to describe some schemes

* [Form DSL](form-dsl-docs-deprecated.md)
* [Document DSL](document-dsl-docs-deprecated.md)
* [Layout DSL](layout-dsl-docs-deprecated.md)
* [Launch DSL](launch-dsl-docs-deprecated.md)
* [Finalize DSL](finalize-dsl-docs-deprecated.md)
* [FinalizeConstrains DSL](finalizeconstraints-dsl-docs-deprecated.md)

Go to [this page](aidbox-code-editor/form-creation.md) to start designing a form.

{% hint style="success" %}
In order not to describe the form from scratch, you can copy any suitable form from the templates and make a new form based on it
{% endhint %}

## To debug & test form, it is necessary to open Aidbox console in browser

* Open [http://localhost:8080/ ](http://localhost:8080/), using login / passwod - admin / admin
* Go to forms by pressing the button `Forms` (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )
* Find the created form on the `Forms & Workflows` page
* Open the created form in Aidbox editor by pressing on the form

{% hint style="info" %}
Any changes in Aidbox editor in browser will be saved to the local file system in your repository
{% endhint %}
