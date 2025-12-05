---
description: Compose multiple forms into complex workflows with sections, status tracking, and form grouping in Aidbox Forms.
---

# Aidbox Form Workflow

Forms can be embedded into a workflow.

{% hint style="info" %}
**Workflow** is a skeleton for forms composition in more complex structures.
{% endhint %}

Initially Workflow have nested structure of items, where each item can be:

* Section - used for forms grouping.
* Form - reference to existed form.

Workflow and items has status model , model is slightly different

**Workflow statuses:**

* `new`
* `in-progress`
* `canceled`
* `completed`
* `in-amendment`
* `amended`

**Item statuses:**

* `new`
* `in-progress`
* `skipped`
* `completed`
* `in-amendment`
* `amended`

{% hint style="warning" %}
**`canceled`** status used for WF because `skipped` status is not obvious in this domain. WF is a process of action, but item is just a step that can be optionally and can be omitted.
{% endhint %}

## Workflow definition

```
 DemoWF3
 ;; Tag to mark entity as Workflow
 {:zen/tags #{aidbox.sdc/Workflow} 
  ;; title of the WF
  :title "Demo workflow"
  ;; items consists of sections/forms in any level deep
           ;; section key can be arbitrary keyword
  :items {:section1
                    ;; section definition with 2 forms
                    {:item aidbox.sdc/SectionItem
                     :title "My custom section 1"
                     :items {:phisical-exam 
                                            ;; form item definition.
                                            {:item aidbox.sdc/FormItem
                                             :form VitalsForm}
                             :questionnaire  {:item aidbox.sdc/FormItem
                                              :form PHQ2PHQ9Form}}}}}
```

When WF is started - it will be populated with items `:order` information, launched forms, document's references and state statuses.

```
 DemoWF3
 ;; Tag to mark entity as Workflow
 {:zen/tags #{aidbox.sdc/Workflow} 
  :title "Demo workflow"
  :order [:section1]
  :status "new"
  :items {:section1 {:item aidbox.sdc/SectionItem
                     :title "My custom section 1"
                     :status "new"
                     :order [:phisical-exam :questionnaire]
                     :items {:phisical-exam {:item aidbox.sdc/FormItem
                                             :form myforms/Demographic
                                             :title "Demographic"
                                             :layout {... layout DSL}
                                             :rules {... rules ...}
                                             :document {id: doc-id :resourceType}
                                             :status "new"}
                             :questionnaire  {... launched form ...}}}}}
```

> Also see _Optional features_ section for some workflow payload customization.

### Section

Section item is item with type aidbox.sdc/SectionItem`It contains`title`and children`items\`. These fields are mandatory

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :section1 {:item aidbox.sdc/SectionItem
                    :title "My custom Section"
                    :items {:form1 {:item aidbox.sdc/FormItem
                                    :form myforms/Demographic}}}}}
```

When workflow is started - all `SectionItems` will be populated with meta information of items order.

> Order generated from `:items` natural placement in ZEN file.

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :order [:form1 :section1]
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :section1 {:item aidbox.sdc/SectionItem
                    :title "My custom Section"
                    :order [:form1]
                    :items {:form1 {:item aidbox.sdc/FormItem
                                    :form myforms/Demographic}}}}}
```

### Form

FormItem is item with type `aidbox.sdc/FormItem` It contains only reference to existed `form`. This field is mandatory

```
{:item aidbox.sdc/FormItem
 :form myforms/Demographic}
```

> Zen validates reference - it can refer to symbols with `aidbox.sdc/Form` tag only.

When workflow is started - all `FormItems` will be populated with `Form` definition, Launched `Layout`, form+document rules and reference to created `Document`.

```
{:item aidbox.sdc/FormItem
 :form myforms/Demographic
 :title "Demographic"
 :layout {... layout DSL}
 :rules {... rules ...}
 :document {id: doc-id :resourceType}}
```

### StatelessForm

StatelessForm is item with type `aidbox.sdc/StatelessFormItem` It contains only reference to existed `form`. This field is mandatory

It is different from typical `FormItem` in that way:

* data populated to it's state on every read-workflow operation.
* data extracted and populated back on every save operation.
* this form can't be skipped.

```
{:item aidbox.sdc/StatelessFormItem
 :form myforms/AllergyIntolerance}
```

> Zen validates reference - it can refer to symbols with `aidbox.sdc/Form` tag only.

There is no difference between `StatelessFormItems` and `FormItem` on `workflow-start`

```
{:item aidbox.sdc/StatelessFormItem
 :form myforms/AllergyIntolerance
 :title "AllergyIntolerance"
 :layout {... layout DSL}
 :rules {... rules ...}
 :document {id: doc-id :resourceType}}

```

### **Form rule keys**

FormItem also supports special rule based keys: `sdc/inject`, `sdc/enable-when`

`:sdc/inject` used for injecting values to form context.

* This rule is client side rule. You don't have access to DB.
* This rule is invoked on each time when form is focused.

Value of the `:sdc/inject` key should be rules map - where key should match expected key by form's rules, and value is a lisp expression. Lisp expressions are invoked in the context of workflow state.

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :order [:form1 :form2]
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :form2 {:item aidbox.sdc/FormItem
                 :sdc/inject {:vitals/bmi (get-in [:items :form1 :document :bmi])}
                 :form myforms/Demographic}}}

;; myforms namespace
DemographicDocument
{:zen/tags #{aidbox.sdc/doc}
 :sdc/rules {:external-bmi (get :vitals/bmi)}
 ...}
 
Demographic
{:zen/tags #{aidbox.sdc/Form}
 :document DemographicDocument 
 ...}
```

`:sdc/enable-when` used in situations when you have optional forms.

* The rule is used on the client to show/hide the form
* The rule is used on the server
  * to launch forms without storing SDCDocuments in DB.
  * to validate form optionality on the complete-workflow/complete-step rpcs

Value of the key `:sdc/enable-when` should be boolean lisp expression. Lisp expression is invoked in the context of workflow state.

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :order [:form1 :form2]
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :form2 {:item aidbox.sdc/FormItem
                 :sdc/enable-when (> (get-in [:items :form1 :document :bmi]) 5)
                 :form myforms/Demographic}}}
```

## Basic WF usage scenarios

Assume we have such WF with one section and two forms

```
 DemoWF
 {:zen/tags #{aidbox.sdc/Workflow}
  :title "Demo workflow"
  :version "0.0.1"
  :items {:section1 {:item aidbox.sdc/SectionItem
                     :title "My custom section 1"
                     :items {:phisical-exam {:item aidbox.sdc/FormItem
                                             :form VitalsForm}
                             :questionnaire  {:item aidbox.sdc/FormItem
                                             :form PHQ2PHQ9Form}}}}}
```

From the start (after WF start) we are working with form-items directly via step functions (step-save/step-complete/step-skip) When form-items pushed to some final state (completed/skipped) - sections will complete/skip automatically. And as final action - you need to complete/cancel workflow.

### Fast forward scenario

[![Fast forward scenario](https://camo.githubusercontent.com/2b274346d1eeafa418f5a32a1b9f57486ee13e33a3880bd34ca762ec8f61755d/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6e5044445169443033384e7446654d775173627a574c31414c6876316f5351524f496e78316b5064363664677a6c51444b624347586a30694964516f7a467055766d532d34666c41424f526b574872365845304e6e7169724e7a71784f4f4e2d334b78333049776756667378716f3268385454755736315169677756706d2d6f562d73745651624f536364314334766d6b4f674c74434e6768356d7a6b787341544142766d39754f3270704132354573424d425939754d574a576759624b635356752d4165485973336162595855547666724f654469534961377a6d54426e56716a5f594e794f342d35626f624d4c356f67725166456a474f786835506256536c6e477266646a726a5271593851506e5a65517a314b42424659565153634c4d5f3356736678765764517a4b7148716e4a56425256304330)](https://camo.githubusercontent.com/2b274346d1eeafa418f5a32a1b9f57486ee13e33a3880bd34ca762ec8f61755d/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6e5044445169443033384e7446654d775173627a574c31414c6876316f5351524f496e78316b5064363664677a6c51444b624347586a30694964516f7a467055766d532d34666c41424f526b574872365845304e6e7169724e7a71784f4f4e2d334b78333049776756667378716f3268385454755736315169677756706d2d6f562d73745651624f536364314334766d6b4f674c74434e6768356d7a6b787341544142766d39754f3270704132354573424d425939754d574a576759624b635356752d4165485973336162595855547666724f654469534961377a6d54426e56716a5f594e794f342d35626f624d4c356f67725166456a474f786835506256536c6e477266646a726a5271593851506e5a65517a314b42424659565153634c4d5f3356736678765764517a4b7148716e4a56425256304330)

### Fast skip scenario

[![Fast skip scenario](https://camo.githubusercontent.com/e19ba76d9bd76df635d12beb32a839021ece740e022c94b17ad4a1e52a678702/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6850354452694b5733384a746443387a30315475676767746f5773676a74523132495a794d363234556c6b36664167514c6a637a394a455a2d4752764e574d426a315a4b597059413143346c4a665132642d677a4b57377a31655f6631646a417a72617a665935446b534752313670394533726c5a77365f77632d5432743763356233386d6b7745676b3646675974506c525a5f674e656368667672354d4230347048594d533563716f386230516e57433161415034794a565741444247684c6c6d694f6e4a59773864757957705a435775544453614a6e5061556d4a6d6a4b436b39706b6a6872557a50317170734e544777764e5f74614a6963454377656c)](https://camo.githubusercontent.com/e19ba76d9bd76df635d12beb32a839021ece740e022c94b17ad4a1e52a678702/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6850354452694b5733384a746443387a30315475676767746f5773676a74523132495a794d363234556c6b36664167514c6a637a394a455a2d4752764e574d426a315a4b597059413143346c4a665132642d677a4b57377a31655f6631646a417a72617a665935446b534752313670394533726c5a77365f77632d5432743763356233386d6b7745676b3646675974506c525a5f674e656368667672354d4230347048594d533563716f386230516e57433161415034794a565741444247684c6c6d694f6e4a59773864757957705a435775544453614a6e5061556d4a6d6a4b436b39706b6a6872557a50317170734e544777764e5f74614a6963454377656c)

## Optional features

Workflow support 2 additional features:

* `versioning` - is automatic and based on hashing essential fields of definitions. If some essential field of form/wf is changed - created a new version and snapshotted to DB
* `section id generation -` is generated from item path (path from WF root to item itself).

These features you can configure via api-constructor in zen-project.

You need to configure your `aidbox/system` with `sdc-service` and it's configuration

> Your zen-project entrypoint namespace

`aidbox/system` with `sdc-service`

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:sdc sdc-service}}
```

`sdc-service` configuration

```
 sdc-service
 ;; bind your sdc/service with engine `aidbox.sdc/service`
 {:zen/tags         #{aidbox/service}
  :engine           aidbox.sdc/service
  ;; Enable Form/Workflow versioning
  :versioning       {:enabled false}
  ;; Enable WF items id generation
  :wf-items-ids-gen {:enabled true}}
```

### Section id generation

When `:wf-items-ids-gen` feature is enabled - each section will receive `:id` property. That id returned on `start-workflow`, `read-workflow` rpc payloads, and don't affect WF storage.

Id is generated from item path (path from WF root to item itself).

Example:

This WF definition

```
 {:zen/tags #{aidbox.sdc/Workflow} 
  :title "My Workflow"
  :items {:section1 {:item aidbox.sdc/SectionItem
                     :items {:phisical-exam {:item aidbox.sdc/FormItem
                                             :form VitalsForm}
                             :questionnaire  {:item aidbox.sdc/FormItem
                                              :form PHQ2PHQ9Form}}}}}
```

on `start-workflow`/`read-workflow` rpcs call - adds items :id information to payload

```
 {:title "My Workflow"
  :items {:section1 {:id "section1"
                     :items {:phisical-exam {... :id "section1.phisical-exam" ...}
                             :questionnaire {... :id "section1.questionnaire" ...}}
                     ...}}}
```

You can use [Workflow API](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md): 
* [get-workflow](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#get-workflow) - get workflow definition
* [get-workflows](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#get-workflows) - return existed workflows
* [start-workflow](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#start-workflow) - start WF and launch forms
* [save-step](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#save-step) - save document through WF, mark step as in-progress
* [skip-step](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#skip-step) - skip WF step
* [amend-step](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#amend-step) - amend WF step
* [complete-step](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#complete-step) - try complete WF step with document, call sign on it.
* [complete-workflow](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#complete-workflow) - complete WF
* [cancel-workflow](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#cancel-workflow) - try cancel WF
* [amend-workflow](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#amend-workflow) - amend completed WF
* [add-workflow-note](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#add-workflow-note) - add addendum note to the given WF
* [get-workflow-addendums](../../../deprecated/deprecated/forms/workflow-api-docs-deprecated.md#get-workflow-addendums) - get list of addendums for the given WF
