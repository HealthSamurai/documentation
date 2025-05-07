---
description: >-
  This article outlines the basic steps to customise a form according to custom
  guidelines
---

# Form customisation in Theme Editor

## Theme Editor

The Theme Editor allows you to customize the appearance of your forms by applying pre-designed themes or creating your own.

### **Accessing the Theme Editor:**

In the UI Form Builder, the toolbar at the top contains a **palette** icon. Clicking on this icon opens the Theme Editor, where you can either select a ready-made theme for your form or create a new one.

When selecting or creating a theme, you can instantly preview how your form will look with the applied theme.

### **Theme Storage and Reusability:**

Themes are stored in the database in a custom resource called **QuestionnaireTheme**. This setup allows flexibility in form design:

* You can apply the same theme to multiple forms.
* You can use different themes with the same form without needing to duplicate the form. This is particularly useful if you want to provide tailored designs for different clients. Simply create one form and assign a set of themes based on client needs.

### **How to Use a Theme:**

* You can set a **default theme** in the configuration, and it will be applied to all forms. More details can be found [here](../configuration.md).
* You can also pass a theme as a **parameter** when generating a link to a specific form. More details can be found [here](../../../../reference/aidbox-forms-reference/aidbox-sdc-api.md#parameters).

### **How to Create a New Theme:**

To create a new theme, select the **Add New Theme** option. This opens the theme editor, which consists of the following sections:

1. **General Settings**
   * **Theme Name:** Assign a name to your theme.
   * **Default Language:** Set the default language if you’re creating a theme for multilingual forms. When a language is selected, an additional **translation** icon will appear. Clicking on this icon opens a separate window where you can provide translations for the buttons used on the multilingual form.
2.  **Colors**

    In this section, you can customize the color scheme of your form:

    * **Primary Color:** The main color used across the form.
    * **Page Color:** The background color for the page where the form is displayed.
    * **Form Color:** The background color for the form itself.
    * **Toolbar Color:** The color for the form's toolbar.

{% hint style="info" %}
The color for the tooltip and hover will be set automatically according to the Primary color or the user can specify it via the accent color of the input.
{% endhint %}

1.  **Brand Image**

    You can add a brand image to your form in two possible locations:

    * **Top-right corner** of the form.
    * **Bottom-left corner** of the form.

    To include the image, provide a publicly available URL for the image file.
2.  **Font**

    In this section, you can select the font family and set the font size for your form. Three options are available for the font family: **Product Sans**, **Inter** and **Metropolis**.
3.  **Inputs**

    In this section, you can customize the appearance of input fields:

    * **Accent Color:** Set the accent color for input fields, which typically affects borders and highlights.
    * **Text Color:** Define the color of the text entered in the input fields.
    * **Background Color:** Set the background color for input fields.
    * **Font Size:** Adjust the font size for text within the input fields.
4.  **Buttons**

    In this section, you can customize the appearance of buttons in your form:

    The following settings are available for the **Submit**, **Print**, **Amend**, and **Save** buttons:

    * **Button Appearance:**
      * **Button Text:** Set the text for the close button.
      * **Button Color:** Set the background color of the close button.
      * **Button Text Color:** Define the color of the text on the close button.

After creating the theme, you need to click the save button.
