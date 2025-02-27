---
description: >-
  AI-powered extraction of structured FHIR SDC Questionnaires from scanned medical forms 
---

# Generating Form from PDF

## Overview

This API enables the automatic digitization of paper-based medical forms by converting scanned PDFs into structured FHIR SDC (Structured Data Capture) `Questionnaire` resources. Using either OpenAI or Gemini for intelligent inference, the system extracts relevant form fields from OCR-processed documents and maps them to a standardized FHIR structure. 

## Prerequisites

To use this API, you must set one of the following environment variables. These keys determine which AI model is used for processing and extracting structured data from scanned forms:

### Environment Variables

- **`BOX_SDC_OPENAI_API_KEY`** – Uses **GPT-4o Mini** for inference.
- **`BOX_SDC_GEMINI_API_KEY`** – Uses **Gemini Pro 2** for inference.

## Endpoint

### Request

```http
POST /$ai-generate-questionnaire HTTP/1.1
Content-Type: multipart/form-data; boundary=------------------------boundary12345

--------------------------boundary12345
Content-Disposition: form-data; name="file"; filename="example.pdf"
Content-Type: application/pdf

<file-content>
--------------------------boundary12345--
```

Where `<file-content>` is the content of the PDF file being uploaded.

### Response

```json
{
  "resourceType": "Questionnaire",
  "id": "<questionnaire-id>",
  "status": "draft",
  "title": "<questionnaire-title>",
  "item": [
    {
      "linkId": "<questionnaire-item-linkId>",
      "text": "<questionnaire-item-text>",
      "type": "<questionnaire-item-type>"
    }
  ]
}
```

Where `<questionnaire-id>` is the unique identifier of the generated questionnaire, `<questionnaire-title>` is the extracted title, and `<questionnaire-item-linkId>`, `<questionnaire-item-text>`, and `<questionnaire-item-type>` represent structured fields extracted from the PDF.

## Error Responses

If the request is invalid or the file cannot be processed, the API may respond with an appropriate HTTP error code and a standard Operation Outcome resource.
