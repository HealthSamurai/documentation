---
description: >-
  Learn how to easily upload, validate, and import questionnaires using JSON or
  PDF files with the Import Questionnaire feature. Streamline your workflow with
  AI-powered OCR and real-time validation.
---

# Import Questionnaire

The **Import Questionnaire** feature allows you to easily upload and import questionnaires into the system from various sources. Whether you have a JSON file ready, a PDF scan of a questionnaire, or just the raw JSON data, this feature provides a streamlined process to validate, modify, and import your questionnaires into the system for further use. This document covers the core functionalities and workflows available within the **Import Questionnaire** tool.

***

## **Core Scenarios**

The import process supports three key scenarios:

### **1. Selecting a JSON File**

In this scenario, you can upload an existing JSON file containing questionnaire data. The system reads the file and presents its content for further editing and validation.

**Steps:**

1. Click the **"Choose PDF or JSON file"** button and select a JSON file from your local storage.
2. The content of the JSON file will be displayed in the **JSON area** for review.
3. The system automatically validates the JSON content. Any errors (e.g., duplicate URLs or invalid structure) will be displayed as error messages beneath the JSON area.
4. You can manually edit the JSON content within the displayed area. Validation will trigger on every change.
5. Once the content is valid, the **Import** button becomes active. Pressing this button will import the questionnaire and open it in the questionnaire builder for further editing or deployment.

### **2. Selecting a PDF File**

This scenario is ideal for users who have paper forms or fillable PDFs that they want to digitize. The system uses AI and OCR to extract text from the PDF and generate a corresponding JSON structure that represents the questionnaire.

**Steps:**

1. Click the **"Choose PDF or JSON file"** button and select a PDF file containing the questionnaire form.
2. The system will display images of each page of the PDF below the file input.
3. The text content of the PDF will be extracted using **Optical Character Recognition (OCR)** and **AI tools**. This extracted content will be transformed into a JSON format and displayed in the **JSON area**.
4. You will be prompted to provide an **OpenAI API key** to facilitate the generation of the questionnaire from the extracted content.
5. Once the AI processing is complete, the generated JSON will be available for review, editing, and validation.
6. As with the JSON scenario, the system will validate the content and display any errors below the JSON area.
7. After the content is validated, you can press **Generate** to import the questionnaire and open it in the builder.

{% hint style="info" %}
Your OpenAI API key is used securely and remains private. It is neither transferred nor stored on any backend servers or third-party services. All interactions with OpenAI happen locally in your browser, ensuring your API key is protected throughout the process.
{% endhint %}

There's the [API for generating a form (Questionnaire resource) from a PDF-file](../../../reference/aidbox-forms-reference/generating-questionnaire-from-pdf-api.md) using AI.

### **3. Pasting JSON Directly into the JSON Area**

If you already have a prepared JSON structure, you can paste it directly into the JSON area. This allows for quick imports without needing to upload a file.

**Steps:**

1. Paste the raw JSON structure directly into the **JSON area**.
2. The system will immediately display the content and start validating it.
3. If any validation errors are found (e.g., missing fields, invalid structure), the system will show error messages below the JSON area.
4. You can manually adjust the JSON content, with validation being triggered on every modification.
5. Once the content passes validation, you can press **Import** to create the questionnaire and open it in the builder.

## **Common Functionality Across All Scenarios**

1. **Displaying JSON Content:**
   * Regardless of the scenario (whether selecting a JSON file, processing a PDF, or pasting JSON), the JSON content will eventually appear in the **JSON area**.
   * You can freely modify this content, and the system will dynamically validate it.
2. **Validation Process:**
   * The system automatically validates the JSON content on every change, ensuring that it adheres to the required structure.
   * Validation errors are shown directly below the JSON area in red text.
   * The **Import** or **Generate** button will remain inactive until the JSON passes validation.
3. **Importing the Questionnaire:**
   * Once the JSON is valid and free of errors, the **Import** button becomes active.
   * Pressing the **Import** button creates the questionnaire in the system and opens it in the **questionnaire builder** for further customization or use.
4.  **Re-generation from PDF and insertion of a part of the form into an existing form:**

    When converting a form from a PDF, if some data is lost or not fully converted, the user can cut out the necessary part of the form, generate the piece, and insert it into the desired location within the existing form.

    1. After opening the form in the UI builder, go to the action menu and select the option **AI** **Tools**.
    2. A new panel will open on the right side of the screen, allowing the user to upload a PDF.
    3. Upload the PDF file and then select the part of the form. Cut out this section and convert into an electronic representation using the `generate`button. To use this feature, the user needs to provide an OpenAI or Google Generative AI API key.
    4. In the outline, select where to insert the extracted part of the form. Click the `Insert` button. By default, if the insertion point is not selected, the new part will be added at the end of the form as a group. This group can be ungrouped if needed.

    This process allows the user to seamlessly integrate portions of a PDF into your existing form structure, preserving all necessary content.

## **Error Handling and Validation**

Throughout the process, the system continuously monitors and validates the JSON content. Common validation checks include:

* **Duplicate URLs:** The system will ensure that no two questionnaires share the same URL.
* **Invalid JSON Structure:** If the JSON format is incorrect (e.g., missing commas or invalid fields), the system will display a message indicating the error and where the correction is needed.
* **FHIR Schema Conformance:** The system checks whether the JSON conforms to the FHIR schema for questionnaires, ensuring that all fields and structures adhere to the required standard.

**Resolving Errors:**

* For **duplicate URLs**, open the JSON file and modify the `url` field to ensure it is unique. Once the change is made, the updated content can be validated and imported.
* For **invalid JSON**, you must correct the structure and formatting before the system will accept the content.
* For **FHIR Schema Conformance**, ensure that the JSON adheres to the FHIR standard for questionnaires. You may need to add or adjust required fields or fix any incorrect structures to match the schema before proceeding.

## **Best Practices**

1. **Ensuring Unique Identifiers:**
   * Before importing, ensure that the questionnaire's URL is unique to avoid conflicts with existing entries.
2. **Correct JSON Structure:**
   * Double-check the JSON for syntax errors or missing fields before uploading or pasting to avoid unnecessary validation errors.
3. **Review OCR Accuracy (for PDF Uploads):**
   * After uploading a PDF, carefully review the OCR-extracted text to ensure accuracy before generating the questionnaire.
4. **FHIR Schema Conformance:**
   * Ensure that your JSON follows the FHIR schema standards for questionnaires. This includes having the required fields, proper data types, and correct structures as per the FHIR specification to avoid schema validation errors.

The **Import Questionnaire** feature is a versatile tool that allows you to upload, edit, and validate questionnaires from various sources. Whether working with pre-existing JSON files, scanning paper forms into PDFs, or manually inputting JSON content, the system offers an intuitive and powerful way to digitize and manage questionnaires for further use. By following the steps outlined in the scenarios and adhering to best practices, you can ensure a smooth, error-free import process that will help streamline your workflow.
