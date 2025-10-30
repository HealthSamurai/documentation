---
description: Adaptive Forms
---

## Adaptive Forms in FHIR SDC

**Adaptive Forms** are a feature of the [FHIR Structured Data Capture (SDC)](https://build.fhir.org/ig/HL7/sdc/en/adaptive.html) Implementation Guide that enables dynamic, intelligent data collection experiences.
In adaptive mode, a form does not present all questions at once — instead, it adapts as the user responds, showing or hiding questions based on previous answers or dynamically requesting the next question set from a backend service.

This approach allows:
- **Personalized experiences** — only relevant questions are displayed.
- **Improved efficiency** — fewer unnecessary questions.
- **Backend-driven logic** — forms can fetch and display new items based on runtime logic, AI models, or decision support rules.

In SDC, adaptive forms are typically implemented through the **Questionnaire/$next-question** operation, which computes the next items to display based on a partial **QuestionnaireResponse**.



## Aidbox Implementation

Aidbox supports **Adaptive Forms** as part of the **Aidbox Forms** product, following the SDC Adaptive Form workflow.

Aidbox implements the following key elements:

### ✅ `$next-question` Operation

Aidbox exposes a **Questionnaire/$next-question** operation that:

- validates incoming parameters
- calls 3rd party service with same endpoint **Questionnaire/$next-question** to obtain new questions.
  - or can be used itself for testing and demonstration purposes.

> To enable validation you should add **hl7.fhir.uv.sdc** FHIR package.

When used without 3rd party service URL - it does next things:

- Takes partial **QuestionnaireResponse** with embedded adaptive **Questionnaire**
- Validates **QuestionnaireResponse** and **Questionnaire** according Adaptive form profiles
- Returns **QuestionnaireResponse** with updated **Questionnaire** and new *items* that should be displayed to the user
- Can optionally include populate logic or computed expressions

> This implementation has a predefined behavior and used for testing purposes only.
> Real logic should be implemented on 3rd party service with same **Questionnaire/$next-question** endpoint.


Example request:

```json
POST /Questionnaire/$next-question
content-type: application/json
accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "questionnaire-response",
      "resource": {
        "resourceType": "QuestionnaireResponse",
        "questionnaire": "#q1",
        "contained": [
          {
            "resourceType": "Questionnaire",
            "id": "q1",
            "status": "active",
            "extension": [
              {
                "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-questionnaireAdaptive",
                "valueUri": "http://assesment-center.mycompany.org"
              }
            ],
          "item": [...]
          }
        ],
        "status": "in-progress",
        "item": [...]
      }
    }
  ]
}
 ```

Example Response:

```json
{
  "resourceType": "QuestionnaireResponse",
  "questionnaire": "#q1",
  "contained": [
    {
      "resourceType": "Questionnaire",
      "id": "q1",
      "status": "active",
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-questionnaireAdaptive",
          "valueUri": "http://assesment-center.mycompany.org"
        }
      ],
      "item": [...]
    }
  ],
  "status": "in-progress",
  "item": [...]
}
```

### 🧩 Smart Extensions

Aidbox adds support for relevant SDC extensions, including:

- enableWhen and enableBehavior
- variable and calculatedExpression
- launchContext
- entryMode (see below)

This ensures compatibility with both standard SDC adaptive forms and Aidbox-specific enhancements.


## Forms Builder

In the **Aidbox Forms Builder**, form authors can enable **Adaptive Mode** for a Questionnaire:

- for demonstration purposes (select ***Adaptive Form** option)
- for specifying their assessment center (set URL for Adaptive form)

**Forms Builder** has validation rules for adaptive forms:

- Questionnaire should not contain any items
- Questionnaire should have URL property


## Form Renderer

The **Aidbox Form Renderer** provides a runtime engine for adaptive questionnaires.

Key behaviors:

- Automatically invokes the **Questionnaire/$next-question** on start to get first questions
- Automatically invokes the **Questionnaire/$populate** operation when new populate expressions found
- Provides **Next Question** button - to obtain new items
- Recognizes **completed** responses and invokes extraction logic.

**Example flow:**

1. Renderer loads a Questionnaire in adaptive mode
2. Renderer sends empty **QuestionnaireResponse** to `$next-question` to get first question
3. User answers the first question and press `Next Question` button
4. Renderer sends partial **QuestionnaireResponse** to `$next-question`
5. Renderer adds new questions to the UI and continues


## Adaptive Forms and *entryMode*

The SDC specification defines an *entryMode* extension that controls **how data is collected**.
In adaptive workflows, *entryMode* influences when and how the next questions are shown.

Supported modes include:

- **sequential** — the user answers questions in order, one at a time. The $next-question operation is invoked when the user chooses to proceed to the next question.

- **prior-edit** — the user answers questions in order but can skip ahead or return to previous questions to edit responses. The `$next-question` operation is invoked when the user attempts to move to a question that has not yet been reached.



**In summary**, Adaptive Forms in Aidbox extend FHIR SDC’s intelligent data capture capabilities with a full implementation of `$next-question`, seamless integration between builder and renderer, and robust support for dynamic, server-driven questionnaires.
