---
description: >-
  This article outlines the basic steps to start designing form locally in any
  familiar editor
---

# Any familiar form editor

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

* [Form DSL ](../../reference/aidbox-forms/form-dsl.md)
* [Document DSL](../../reference/aidbox-forms/document-dsl.md)
* [Layout DSL](../../reference/aidbox-forms/layout-dsl.md)
* [Launch DSL](../../reference/aidbox-forms/launch-dsl.md)
* [Finalize DSL](../../reference/aidbox-forms/finalize-dsl.md)
* [FinalizeConstrains DSL](../../reference/aidbox-forms/finalizeconstraints-dsl.md)

Go to [this page](aidbox-code-editor/how-to-create-a-form.md) to start designing a form.

{% hint style="success" %}
In order not to describe the form from scratch, you can copy any suitable form from the templates and make a new form based on it
{% endhint %}

## To debug & test form, it is necessary to open Aidbox console in browser&#x20;

* Open  [http://localhost:8080/ ](http://localhost:8080/), using login / passwod  - admin / admin
* Go to forms by pressing the button `Forms` (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )
* Find the created form on the `Forms & Workflows` page
* Open the created form in Aidbox editor by pressing on the form

{% hint style="info" %}
Any changes in Aidbox editor in browser will be saved to the local file system in your repository
{% endhint %}
