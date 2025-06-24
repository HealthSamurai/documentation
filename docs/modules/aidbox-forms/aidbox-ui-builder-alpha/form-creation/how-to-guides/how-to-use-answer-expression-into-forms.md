---
description: Aidbox forms supports fetching answers into question with conditions and logic
---

# How to use answer expression into forms

## What is answer expression

Aidbox Forms allows to create form which dynamically fetch answers into questions. An expression with FHIRPath or FHIR Query that resolves to a list of permitted answers for the question item or that establishes context for a group item. The user may select from among the list to choose answers for the question.&#x20;

## Example Use Case

Form have two questions. User wants filtrate possible answers into second question depends on answer from first question.&#x20;

For example first question named `Gender` have two options: `Male`and `Female`.  Depends on answer from `Gender` , second question named `Patient` will have two possible lists of answer options with patients who have gender equals to `Male` or `Female`.

## How to build form

In this guide we will create form with usage of FHIRPath expression and FHIR Query expression.

### 1. Form setup

Let's create a form which first group will have questions with FHIRPath expression and second group with FHIR Query expression.

Create in builder group called `Diet` and two items into, so your form in item tree should look like this:

```
Diet [group]
  - Type [string]
  - Food [string]
```

Then create another group called `Patients` and two items into, final item tree should like this:

```
Diet [group]
  - Type [choice]
  - Food [choice]
Patients [group]
  - Gender [choice]
  - Patient [reference]
```

### 2. Setting widgets

Let's setup first group with FHIRPath expression:&#x20;

1. Select `Type` widget in outline
2. Choose `Static` options and create two options:&#x20;
   1. code: `meat`, display: `Meat` ;
   2. code: `vegetables`, display: `Vegetables` .
3. Select `Food` widget in outline
4. Choose `Expression` options and select Language `FHIRPath`&#x20;
5. Write down expression:
   1. We define variable called `value` , which will be used as source of code from `Type` answer
   2. And create function `iif`  which will define answer options matching `Type` answer
   3. Then we use `combine` function to unite our `iif` conditions&#x20;

```sql
defineVariable('value', %resource.repeat(item).where(linkId = 'type-choice').answer.valueCoding.code).
select(iif(%value.exists($this = 'meat'), 'Beef' | 'Lamb').
combine(iif(%value.exists($this = 'vegetable'), 'Carrot' | 'Potato')))
```

{% hint style="info" %}
Take note: Actual linkId may differ! You must select `linkId` equals to `Type` widget.&#x20;
{% endhint %}

Now let's setup second group with FHIR Query expression:

1. Select `Gender` widget in outline
2. Choose `Static` options and create two options:&#x20;
   1. code: `male`, display: `Male` ;
   2. code: `female`, display: `Female` .
3. Select `Patient` widget in outline
4. Choose `Expression` options and select Language `FHIRQuery`&#x20;
5. Write down expression:&#x20;
   1. We define query to our resource `Patient` and parameter `gender`
   2. And links question from where code will be queried&#x20;

<pre class="language-sql" data-full-width="false"><code class="lang-sql"><strong>Patient?gender={{ %resource.repeat(item).where(linkId = 'gender-choice').answer.valueCoding.code }}
</strong></code></pre>

{% hint style="info" %}
Take note: Actual linkId may differ! You must select `linkId` equals to `Gender` widget.&#x20;
{% endhint %}

### Usage

Now let's try to answer on questions:

1. Diet questions:&#x20;
   1. If user choose into Type question `Meat` answer. Into Food question will be available only `Beef` and `Lamb` answer.&#x20;
   2. If user choose `Vegetables` answer into Type question — into Food question will be available only `Carrot` and `Potato` answers.
2. Patients questions:&#x20;
   1. If user choose into Gender question `Male` answer. Into Patient reference question will be available list of patients only with `Male` gender.
   2. If user choose `Female`  answer into Gender question — into Patient question will be available list of patients only with `Female` gender.



