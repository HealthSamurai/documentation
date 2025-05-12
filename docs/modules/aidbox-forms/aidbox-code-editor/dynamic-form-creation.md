# How to create a dynamic form

In this tutorial, we will learn how to create a form with conditional logic. This means, that based on the patient's answers the result will be calculated differently and some form fields will be displayed conditionally.

A toggle button can be used to display all form fields despite the calculated PHQ2 score.

The first two questions representing [PHQ-2](https://loinc.org/55757-9/) are used for quick depression screening. If the patient's score for PHQ-2 lies above the defined threshold, further questions will be shown to calculate the [PHQ-9](https://loinc.org/44249-1/) score and the toggle button will be disabled.

We'll omit some form fields to keep this example short.

## Prerequisites

To accomplish the goal we assume the SDC Forms project is already configured as described in ["Getting Started"](broken-reference).

Next, we need an example namespace to put the form we are about to create into. Let's create a file `zrc/tutorial/phq2phq9.edn` and declare the desired namespace:

```
{ns tutorial.phq2phq9}
```

Make sure the created Zen file is imported into the project so Zen can (re-) load it. Available forms can be imported via an aggregation file (e.g. `/zrc/forms.edn`) or directly via the entry point file `/zrc/sdc-box.edn` like this:

```
{ns sdc-box
 import #{,,, tutorial.phq2phq9 ,,,}

 ,,,
}
```

## Steps

What helps in building dynamic forms?

* Declare dynamic rules using lisp expressions under `:sdc/rules` attribute
* Conditionally render form fields or field groups using `:sdc/display-when` attribute bound to SDC rules
* Disable or enable elements via `:sdc/disable-when` attribute and SDC rules

### Form layer

First we create an example form to illustrate conditional rendering and dynamic rule based calculation. In the following steps we'll describe the document and the form layout using the form layers. Launch description is not relevant for this tutorial and can be found in Appendix for completeness.

Add following lines to `/zrc/tutorial/phq2phq9.edn` file:

```
 PHQ2PHQ9Form
 {:zen/tags #{aidbox.sdc/Form}
  :title    "PHQ2/PHQ9 Depression Form"
  :version  "1.0.0"
  :document PHQ2PHQ9Document
  :layout   PHQ2PHQ9Layout
  :launch   PHQ2PHQ9Launch}
```

### Describe static document structure

Let's describe form questions we want to be answered in order to calculate the final score. First, we define two fields from PHQ2 and then we take only one additional field from PHQ9.

To keep the example short we define common schema `LL358-3` for each answer.

```
 LL358-3
 {:zen/tags #{zen/schema}
  :type zen/map
  :confirms #{aidbox.sdc.fhir/coding}
  :enum [{:value {:display "Not at all" :code "LA6568-5" :score 0 :system "http://loinc.org"}}
         {:value {:display "Several days" :code "LA6569-3" :score 1 :system "http://loinc.org"}}
         {:value {:display "More than half the days" :code "LA6570-1" :score 2 :system "http://loinc.org"}}
         {:value {:display "Nearly every day" :code "LA6571-9" :score 3 :system "http://loinc.org"}}]}

PHQ2PHQ9Document
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :type zen/map,
 :confirms #{aidbox.sdc/Document},

 :keys {
        ;; following two fields are used to calculate PHQ2 score
        :loinc-44250-9 {:text "Little interest or pleasure in doing things"
                        :confirms #{aidbox.sdc.fhir/coding LL358-3}}
        :loinc-44255-8 {:text "Feeling down, depressed, or hopeless"
                        :confirms #{aidbox.sdc.fhir/coding LL358-3}}

        ;; this additional field is used to calculate PHQ9 score
        :loinc-44254-1 {:text "Feeling tired or having little energy"
                        :confirms #{aidbox.sdc.fhir/coding LL358-3}}

        ;; calculated scores
        :phq2-score {:text "PHQ-2 total score [Reported]"
                     :type zen/number}
        :phq9-score {:text "PHQ-9 total score [Reported]"
                     :type zen/number}

        ;; final result will be either PHQ2 or PHQ9 score
        :final-score {:text "UI score display" 
                      :type zen/number}

```

### Rule to calculate PHQ scores

Now we define the calculation formula using Zen Lisp expression. With `get-in` we grab the user's answer score and sum them up.

{% hint style="info" %}
The rule name must be the same as the document's field name.
{% endhint %}

The final score will be chosen conditionally based on the calculated PHQ-2 score. If the PHQ-2 score is too high we should display additional questions to the user. Calculated fields can be used to implement conditional rendering. Let's see an example in the Layout layer.

```
PHQ2PHQ9Document
{,,,
 :sdc/rules
 {;; formula for PHQ2 score
  :phq2-score (+ (get-in [:loinc-44250-9 :score])
                 (get-in [:loinc-44255-8 :score]))

  ;; formula for PHQ9 score
  :phq9-score (+ (get-in [:loinc-44250-9 :score])
                 (get-in [:loinc-44255-8 :score])
                 (get-in [:loinc-44254-1 :score]))

  ;; helper rule
  :phq2-threshold-exceeded? (>= (get :phq2-score) 3)

  ;; take PHQ9 score if PHQ2 score is bigger then 3 or take PHQ2 score otherwise
  :final-score (if (get :phq2-threshold-exceeded?)
                 (get :phq9-score)
                 (get :phq2-score))}
```

### Layout with conditional questions

In the `Layout` layer we define how the form fields are rendered. All fields should be placed vertically in a single-column layout.

```
PHQ2PHQ9Layout
{:zen/tags #{aidbox.sdc/Layout aidbox.sdc/rules}
 :document PHQ2PHQ9Document
 :engine   aidbox.sdc/Hiccup

 ;; display rules
 :sdc/rules
 {;; we reuse previously defined rule for conditional rendering
  :enable-phq9? (get-in [:phq2-threshold-exceeded?])}

 :layout
 {:type aidbox.sdc/col
  :children
  [{:type aidbox.sdc/fields
    :children
    [{:type aidbox.sdc/label
      :label "Over the past 2 weeks, how often have you been bothered by:"}

     {:type aidbox.sdc/fields,
      :children
      [;; PHQ2 fields
       {:type aidbox.sdc/fields,
        :children
        [{:bind [:loinc-44250-9]}
         {:bind [:loinc-44255-8]}]}

       ;; additional PHQ9 fields
       {:type aidbox.sdc/fields
        ;; render this container only if the condition is met
        :sdc/display-when (get :enable-phq9?)
        :children
        [{:bind [:loinc-44254-1]}]}]}

     {:bind [:final-score] :label "PHQ2/PHQ-9 Depression Screening"}]}]}}
```

We split the declared fields in two groups using `aidbox.sdc/fields` container. To declare conditional rendering block we make use of `:sdc/display-when` attribute and bind it to the previously defined rule with a meaningful name.

### Combine conditions

The form layout can be easily extended to be more complex and dynamic using this simple technique. Let's add a button to show additional PHQ-9 questions regardless of the PHQ-2 score. This button should be disabled if the PHQ-2 score threshold is already exceeded. Use `:sdc/disable-when` attribute in combination with a rule to achieve this.

```
PHQ2PHQ9Document
{,,,

 ;; declare additional document field
 :keys
 {:force-phq9-questions? {:type zen/boolean
                          :text "For positive depression screen or follow up"}}
 ,,,
 }

PHQ2PHQ9Layout
{,,,

 :sdc/rules
 {;; introduce logic to enable/disable the button
  :disable-button? (get :phq2-threshold-exceeded?)

  ;; extend this rule, allow the button to overrule the PHQ-2 score
  :enable-phq9?
  (or (get :force-phq9-questions?)
      (get :phq2-threshold-exceeded?))}

 :layout
 {:type aidbox.sdc/col
  :children
  [;; enable manual toggling only if PHQ-2 score is not exceeded
   {:bind [:force-phq9-questions?]
    :sdc/disable-when (get :disable-button?)}

   {:type aidbox.sdc/fields
    ;; omit previously defined fields
    :children [,,,]}]}
 }
```

### Appendix

```
PHQ2PHQ9Launch
{:zen/tags #{aidbox.sdc/Launch}
 :document PHQ2PHQ9Document
 :populate-engine aidbox.sdc/LispPopulate
 :populate {:author (get-in [:ctx :user])}}
```
