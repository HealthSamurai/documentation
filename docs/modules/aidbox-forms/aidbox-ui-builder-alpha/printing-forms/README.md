---
description: How to print a form or save it as PDF.
---

# Printing forms

When filling out a form, you can print it in its current state at any moment by clicking the Print button in the bottom left corner. Once you click it, you'll see the default browser print dialog allowing you to save the form as PDF or print it. The general layout is preserved, but the final look of the form in print or PDF will depend on the options you select in this print dialog (e.g. paper size, margins, background graphics).

Supported browsers: the latest versions of Chrome and Firefox.

## Machine Printing

For machine printing, you can choose between two approaches depending on your needs:

* [**$print**](native-look-form-printing.md) **API** – Prints forms in their original/native appearance, preserving the exact layout and styling as seen in the browser. (Requires a headless browser)\
  &#xNAN;_&#x42;est when you want a faithful replica of the on-screen form._
* [**$render**](template-based-pdf-generation.md) **API** – Prints forms using custom templates, allowing full control over layout, styling, and formatting.\
  &#xNAN;_&#x42;est when you need a tailored design or a standardized PDF format._
