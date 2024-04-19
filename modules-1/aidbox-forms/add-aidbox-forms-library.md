---
description: The article outlines how to setup and use Aidbox Forms library
---

# Aidbox Forms Library

The Aidbox Forms Library offers forms in two formats: Aidbox format (DSL-based) and FHIR format (FHIR Questionnaire). These forms can be loaded into Aidbox using various methods:

1. Using Aidbox SDC configuration project while creating a license on the [Aidbox portal](https://aidbox.app/ui/portal#/signin)
2. Using the [Aidbox Community Notebook](https://aidbox.app/ExportedNotebook/91387a94-fb51-4973-aa28-e9e21f47639b) (FHIR Format Forms Only)
3. Through the aidbox-sdc-zen  project

By offering multiple loading methods, the Aidbox Forms Library provides flexibility for users to choose the most suitable method for their needs.

## Setup Aidbox Forms Library using Aidbox SDC configuration project on the Aidbox portal

When creating a license on the [Aidbox portal](https://aidbox.app/ui/portal#/signin), you need to select the Aidbox SDC configuration project. Then the forms from the library will be loaded into your instance

## Setup Aidbox Forms Library using Aidbox Notebooks

{% hint style="warning" %}
This way you can only upload forms in the FHIR format (FHIR Questionnaire)
{% endhint %}

The Aidbox Forms Library has been updated to include the following forms in the FHIR format:

* **ROS (Review of System)**
* **Physical Exam**
* **Cough**
* **Vitals Sign** (coded in the LOINC code system)
* **PHQ2 / PHQ9 Depression form** (coded in the LOINC code system)

Users can upload these forms using the `fhir/$load` operation and then open them in the Aidbox Forms module for editing according to their requirements.&#x20;

Use [Aidbox Forms Library notebook](https://aidbox.app/ExportedNotebook/88275c87-00f3-4d1b-8811-1f650a19e41a) (community) for this purpose that is available in the Aidbox console of your instance.

## Setup Aidbox Forms Library using aidbox-zen-sdc project

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

