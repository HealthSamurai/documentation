---
description: Configure visual hierarchy padding for nested Questionnaire items including groups and question nesting in Formbox.
---

# Nesting

Questionnaire has 2 ways to nest items. 

- Items that live inside **group** item
- Nested items that live inside **question** item

In _Formbox_ these 2 groups have different ways to represent nesting by default.

1. Groups have labels with different sized to convey structure.
2. Nested items doesn't have any visual representation - to make form look more nicer, visually cleaner and less noisy.

But sometimes it's usefull to convey that nested structure of the questions.

For this purpose we have introduced **Hierarchy padding** property.

## Hierarchy padding property

This property sets left padding(in REMs) against item's parent. (up to 3 levels deep)
It takes numerical value from **0 to 10** (decimal numbers also acceptable - ex. 3.25).

> REM - [Relative to font-size of the root element](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics/Values_and_units)

The property is stored in the **QuestionnaireTheme** resource.

```json
 {
  "resourceType": "QuestionnaireTheme"
  "theme-name": "My Theme",
  "input": {
    "hierarchy-padding": 3.25
  }
}
```

## How to set Hierarchy padding property

Property should be set for **current** or **default** theme.

This process consists of 2 steps:

1. set theme property 
2. use theme

Theme can be edited with 

- **Theme editor**
- **Aidbox REST Console**
- **Resource Browser**

### Theme Editor

Theme editor located in 
```
Forms Builder > Theme button (with palitra icon - in the top-right corner) > Edit theme > Hierarchy padding slider
```

This is a most convinient way to change hierarchy padding property because you can see applied changes immediately.

### Aidbox REST Console or Resource Browser

Also it's possible to create/update themes programmatically. 
It's usefull if you want to make a lot of themes, or automate this flow in some way.

```json
PUT /QuestionnaireTheme/[id]
content-type: application/json
accept: application/json

 {
  "resourceType": "QuestionnaireTheme"
  "id": "[id]",
  "theme-name": "My Theme",
  "input": {
    "hierarchy-padding": 3.25
  }
}
```

Resource Browser gives you a generic way to manage resources, and **QuestionnaireTheme** is not an exception

Just put Theme as JSON/YAML resource there. 

You can find **Resource Browser** in 

```
Aidbox Console / Resources
```


### How use theme

There are 3 ways to do that.

- via SDCCOnfig
- via GenerateLink
- via passing theme directly to web-component

#### SDCConfig

You can specify theme in your [SDCConfig](../configuration.md)

```json
{
  "resourceType": "SDCConfig",
  "id": "cfg1",
  "name": "default-config",
  "default": true, // in case if this is defualt config
  "theme" : {
    "reference" : "QuestionnaireTheme/my-theme"
  }
}
```

### GenerateLink

Theme also can be specified through `$generate-link`/`$populatelink` APIs when generating link for sending it to enduser

* [$generate-link](../../../../reference/aidbox-forms-reference/aidbox-sdc-api.md#generate-a-link-to-a-questionnaireresponse-generate-link)
* [$populatelink](../../../../reference/aidbox-forms-reference/fhir-sdc-api.md#populate-questionnaire-and-generate-a-link-populatelink) 


### Direct theme parameter to Aidbox-renderer and Aidbox form builder web-component

It's also possible to specify theme while [embedding](../embedding.md) webcomponent in your application.


### EntryMode exception

This property has no effect when used with the sequential or prior-edit [entryMode](entry-mode.md)
