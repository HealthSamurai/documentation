---
description: How to print forms in their native look
---

# Native look form printing

Since forms are rendered dynamically in the web browser, achieving the same appearance in print requires a browser engine. This approach involves calling an API to retrieve the HTML, then rendering it in a browser engine to produce the final HTML for printing. Typically, [Puppeteer](https://pptr.dev/) or [Playwright](https://playwright.dev/) are used for this purpose.

## API

### $print Operation

Returns an HTML snippet containing an iframe and a link to the form renderer.

```
GET /fhir/QuestionnaireResponse/[id]/$print
```

Parameters:

| Parameter | Type   | Description           | required? |
|:---------:|:------:|:---------------------:|:---------:|
| config    | string | SDCConfig id          | optional  |
| theme     | string | QuestionnaireTheme id | optional  |

Example:

```
GET /fhir/QuestionnaireResponse/[id]/$print?theme=my-theme
```

Response:
```
"<iframe src=\"link-to-form\" style=\"width: 100%; height: 100%\"></iframe>"
```


You can use this HTML snippet with Puppeteer (or a similar engine) to render the form and print it to PDF.

> The iframe is used for embedding the form in other HTML.
> It will automatically load all styles and assets. The engine should wait until the content is fully rendered before printing.

> If the form is too long for a single page, it can be useful to extract the styles and content from the iframe and embed them in a standard HTML element for better print control.
