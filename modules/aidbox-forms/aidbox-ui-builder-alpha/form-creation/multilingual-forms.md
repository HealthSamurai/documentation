---
description: The article describes how to translate or create a multilingual form.
---

# Multilingual forms

In some applications, there is a need to support multiple languages within the same questionnaire. Aidbox Forms allows you to create multilingual forms that can cater to users speaking different languages.

**Steps to Create a Multilingual Form:**

1. **Select the Default Language:**
   * Begin by selecting the default language for your form in the form settings. This will be the primary language used during the initial creation of the form. Once the default language is selected, the option to add translations will become available in the UI form builder.
2. **Access the Translation Feature:**
   * After you have created your form in the default language, you can add translations. Click the translation icon located in the toolbar at the top of the form builder. This will open a separate page dedicated to translations.
3.  **Add and Manage Translations:**

    * On the translation page, click the plus (`+`) icon to select a new language for translation. For each selected language, you can translate all field labels and answer options. All translations are stored within a single FHIR Questionnaire resource, utilizing a specific extension for each questionnaire element to maintain multilingual support.



An example of the simple multilingual questionnaire:

```json
// {
  "meta": {
    "lastUpdated": "2024-08-29T11:42:41.986043Z",
    "versionId": "1091",
    "extension": [
      {
        "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
        "valueInstant": "2024-08-29T11:42:41.986043Z"
      }
    ]
  },
  "item": [
    {
      "type": "string",
      "text": "Name",
      "linkId": "mSyajZIe",
      "extension": [
        {
          "url": "http://aidbox.io/questionnaire-itemColumnSize",
          "valueInteger": 4
        }
      ],
      "_text": {
        "extension": [
          {
            "url": "http://hl7.org/fhir/StructureDefinition/translation",
            "extension": [
              {
                "url": "lang",
                "valueCode": "fr"
              },
              {
                "url": "content",
                "valueString": "Nom"
              }
            ]
          }
        ]
      }
    },
    {
      "type": "date",
      "text": "Date of Birth",
      "linkId": "63ttDmtg",
      "extension": [
        {
          "url": "http://aidbox.io/questionnaire-itemColumnSize",
          "valueInteger": 4
        }
      ],
      "_text": {
        "extension": [
          {
            "url": "http://hl7.org/fhir/StructureDefinition/translation",
            "extension": [
              {
                "url": "lang",
                "valueCode": "fr"
              },
              {
                "url": "content",
                "valueString": "Date de naissance"
              }
            ]
          }
        ]
      }
    },
    {
      "type": "choice",
      "text": "Gender",
      "answerOption": [
        {
          "valueCoding": {
            "display": "Male",
            "_display": {
              "extension": [
                {
                  "url": "http://hl7.org/fhir/StructureDefinition/translation",
                  "extension": [
                    {
                      "url": "lang",
                      "valueCode": "fr"
                    },
                    {
                      "url": "content",
                      "valueString": "Mâle"
                    }
                  ]
                }
              ]
            }
          }
        },
        {
          "valueCoding": {
            "display": "Female ",
            "_display": {
              "extension": [
                {
                  "url": "http://hl7.org/fhir/StructureDefinition/translation",
                  "extension": [
                    {
                      "url": "lang",
                      "valueCode": "fr"
                    },
                    {
                      "url": "content",
                      "valueString": "Femelle"
                    }
                  ]
                }
              ]
            }
          }
        }
      ],
      "linkId": "_YUClivb",
      "extension": [
        {
          "url": "http://aidbox.io/questionnaire-itemColumnSize",
          "valueInteger": 4
        }
      ],
      "_text": {
        "extension": [
          {
            "url": "http://hl7.org/fhir/StructureDefinition/translation",
            "extension": [
              {
                "url": "lang",
                "valueCode": "fr"
              },
              {
                "url": "content",
                "valueString": "Genre"
              }
            ]
          }
        ]
      }
    }
  ],
  "resourceType": "Questionnaire",
  "title": "Test Form",
  "status": "draft",
  "language": "en",
  "id": "1d4ff43e-41f3-443d-9165-7de40ee8020d",
  "url": "http://forms.aidbox.io/questionnaire/test-translation",
  "_title": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/StructureDefinition/translation",
        "extension": [
          {
            "url": "lang",
            "valueCode": "fr"
          },
          {
            "url": "content",
            "valueString": "Formulaire de test"
          }
        ]
      }
    ]
  }
}
```



{% hint style="info" %}
Aidbox Forms supports a predefined set of languages:

Chinese, Croatian, Czech, Danish, Dutch, English, Finnish, French, German, Hungarian, Italian, Japanese, Korean, Polish, Russian, Spanish

Additional languages can be added upon request.
{% endhint %}

4. **Switching Languages During Form Completion:**
   * When users are filling out the form, they will initially see the form in the default language. They can switch to any of the translated languages using a dropdown menu available on the form. This allows users to complete the form in their preferred language.

{% hint style="info" %}
Validation errors are translated automatically.&#x20;
{% endhint %}
