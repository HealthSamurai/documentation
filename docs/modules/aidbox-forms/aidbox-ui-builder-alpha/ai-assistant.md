---
description: >-
  AI-powered tool for building and editing FHIR SDC Questionnaires through
  natural language interaction.
---

# AI Assistant

The AI Assistant in the Aidbox UI Builder is a powerful tool designed to help you quickly build and manage **FHIR SDC Questionnaires** through natural language interaction. It provides seamless integration with the builder's tools and controls, significantly accelerating form creation and editing.

## Getting Started

{% stepper %}
{% step %}
#### Open the AI Assistant

Navigate to the top-right corner of the Aidbox UI Builder, click the context menu, and select **AI Tools** from the dropdown.
{% endstep %}

{% step %}
#### Set up API key

If no API key is configured, you'll see a prompt. Click **Go to Settings** to enter your key.
{% endstep %}

{% step %}
#### Configure settings

In the **Settings** modal, choose your preferred language, enter your **OpenAI** or **Google Gemini** API key, and click **Save**.

{% hint style="info" %}
You can get a free Gemini API key from [Google AI Studio](https://aistudio.google.com/).
{% endhint %}
{% endstep %}
{% endstepper %}

## Chat Interface

Once your API key is set, you'll see a traditional chat interface:

* Type your request and hit **Send** button or press **Shift+Enter** keys.
* The assistant will respond and execute actions on your behalf.
* Context and token usage are shown below the input field.
* Use **Clear chat** to reset the conversation.

{% hint style="info" %}
Chat history is saved per saved questionnaire using browser local memory. For unsaved questionnaires, history will not persist across sessions.
{% endhint %}

## Capabilities

The AI Assistant can comprehensively manage FHIR SDC Questionnaires including:

* **Item structure** - Add, remove, move, and nest questionnaire items
* **Population** - Pre-fill items from patient data, observations, or static values
* **Answer options** - Configure choice options, value sets, and dynamic expressions
* **Enablement** - Set up conditional display logic based on other responses
* **Calculation** - Create calculated fields and computed values
* **Extraction** - Map form responses back to FHIR observations and resources
* **Validation** - Add constraints, required fields, and data type validation
* **Presentation** - Modify layout, styling, media content, and user interface elements
* **Metadata** - Managing questionnaire-level properties such as title, version, and tags

For example, when asked to create a BMI form, the assistant:

* Created height and weight fields
* Added a calculated BMI field
* Updated the form preview accordingly

## Use Case Scenarios

### 1. Building a Depression Screening Form (Iterative Approach)

{% stepper %}
{% step %}
#### Search for LOINC codes

**User:** "Search for PHQ-9 LOINC codes for depression screening."\
**Assistant:** Finds relevant LOINC codes and displays them.
{% endstep %}

{% step %}
#### Create first question

**User:** "Create the first PHQ-9 question about feeling down or depressed."\
**Assistant:** Creates a choice item with 0-3 scale options.
{% endstep %}

{% step %}
#### Add remaining questions

**User:** "Add the remaining 8 PHQ-9 questions using the same answer options."\
**Assistant:** Creates consistent choice items for all questions.
{% endstep %}

{% step %}
#### Add scoring calculation

**User:** "Add a calculated total score field for the PHQ-9."\
**Assistant:** Creates a calculation that sums all responses.
{% endstep %}

{% step %}
#### Apply validation and styling

**User:** "Make all questions required and adjust the layout to be more compact."\
**Assistant:** Applies validation and styling improvements.
{% endstep %}
{% endstepper %}

### 2. Setting Up Patient Data Pre-population

{% stepper %}
{% step %}
#### Create weight field

**User:** "Create a decimal field for patient weight in kilograms."\
**Assistant:** Creates a basic weight input field.
{% endstep %}

{% step %}
#### Configure pre-population

**User:** "Configure this weight field to pre-populate from recent observations."\
**Assistant:** Sets up population rules using LOINC code `29463-7`.
{% endstep %}

{% step %}
#### Set lookback period

**User:** "Set the lookback period to 6 months for weight observations."\
**Assistant:** Configures the time period for observation search.
{% endstep %}

{% step %}
#### Add validation constraints

**User:** "Add validation to ensure weight is between 1-500 kg."\
**Assistant:** Adds appropriate min/max constraints.
{% endstep %}
{% endstepper %}

### 3. Refining Form Structure

{% stepper %}
{% step %}
#### Review current structure

**User:** "Show me the current questionnaire structure."\
**Assistant:** Displays the form outline.
{% endstep %}

{% step %}
#### Update field properties

**User:** "Make the BMI field read-only since it's calculated."\
**Assistant:** Updates the field's `readOnly` property.
{% endstep %}

{% step %}
#### Reposition items

**User:** "Move the BMI field to appear right after the weight field."\
**Assistant:** Repositions the item in the hierarchy.
{% endstep %}

{% step %}
#### Verify functionality

**User:** "Verify that the BMI calculation still works correctly."\
**Assistant:** Confirms calculation expressions are functioning.
{% endstep %}
{% endstepper %}

## Best Practices

To get the most out of the AI Assistant, follow these recommendations:

* **Be specific and clear** - Provide detailed descriptions of what you want to achieve
* **Break down complex requests** - Split large tasks into smaller, manageable steps
* **Use examples** - Reference existing form elements or provide sample data when possible
* **Specify item types** - Mention the exact field types you need (choice, text, date, etc.)
* **Include context** - Explain the purpose of the form or questionnaire you're building
* **Review before saving** - Always check the assistant's changes before clicking Save
* **Use medical terminology** - Reference standard codes (LOINC, SNOMED) when working with clinical forms
* **Test iteratively** - Make changes gradually and test functionality as you go
* **Keep chat history manageable** - Clear chat history when conversations become too long, you want a fresh approach, the assistant seems confused, or you're switching task types.

## Questions & Answers

<details>

<summary>What API providers are supported?</summary>

Currently, the AI Assistant supports:

* **OpenAI** (GPT-4o via OpenAI API) - **Recommended**
* **Google Gemini** (Gemini 2.5 Pro)

You can enter your API key in the **Settings** panel under _AI Tools_.

</details>

<details>

<summary>Is my data sent to external servers?</summary>

Only the messages you type into the AI Assistant and necessary context (like form structure) are sent to the AI provider (OpenAI or Gemini), depending on the key you've configured. No patient-identifiable information is sent unless you explicitly include it.

</details>

<details>

<summary>How is my data and chat history stored?</summary>

Chat history is stored in your **browser's local memory** and persists per saved questionnaire. For unsaved questionnaires, chat history will reset if cleared manually.

Questionnaire changes made by the assistant are temporary until you manually click the **"Save"** button to persist them on Aidbox. Unsaved changes remain in your browser session but are not permanently stored.

</details>

<details>

<summary>Does the assistant affect other questionnaires?</summary>

No. The assistant only affects the current questionnaire being edited and does not make changes to other forms or saved questionnaires.

</details>

<details>

<summary>Can I use the assistant to edit existing forms?</summary>

Yes. The assistant can edit any aspect of existing FHIR SDC Questionnaires. See the **Capabilities** section above for a complete list of supported features.

</details>

<details>

<summary>What happens if I lose internet connection?</summary>

If you lose connection, the assistant will be **unable to communicate with the AI provider**. Once you're reconnected, you can type **"Retry"** or **"Continue"** to resume the session.

</details>

<details>

<summary>Can I undo the assistant's changes?</summary>

Yes. You can use the **undo/redo** buttons in the builder interface to revert changes made by the assistant.

</details>

<details>

<summary>Does it support multilingual use?</summary>

Yes. If supported by the underlying AI provider, the assistant will respond in the same language you use in your prompts.

</details>

<details>

<summary>Can I export the chat conversation?</summary>

Not directly at the moment. You can manually copy the conversation or extract it using developer tools. Export options may be available in future versions.

</details>
