# External Form Renderer in Builder Preview

The Aidbox Forms Builder supports custom form renderers that allow you to create personalized questionnaire experiences while maintaining compatibility with FHIR standards. This feature enables developers to build custom rendering logic for questionnaires while leveraging the Forms Builder's preview and configuration capabilities.

## Renderer Implementation

Custom renderers must be implemented as web components that extend `HTMLElement` and provide specific methods for Forms Builder integration. The renderer component needs to handle questionnaire data rendering and response collection through defined callback interfaces.

```javascript
class QuestionnaireCustomRenderer extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
    this._questionnaire = null;
    this._questionnaireResponse = null;
    this._onQuestionnaireResponseChange = null;
  }

  static get observedAttributes() {
    return ['questionnaire', 'questionnaire-response'];
  }

  connectedCallback() {
    try {
      this._questionnaire = JSON.parse(this.getAttribute('questionnaire'));
      this._questionnaireResponse = JSON.parse(this.getAttribute('questionnaire-response'));
    } catch (e) {
      console.error('Error parsing attributes:', e);
    }
    this.render();
  }

  attributeChangedCallback(name, oldValue, newValue) {
    if (!this.shadowRoot || oldValue === newValue) return;
    
    switch (name) {
      case 'questionnaire':
        this._questionnaire = newValue ? JSON.parse(newValue) : null;
        break;
      case 'questionnaire-response':
        this._questionnaireResponse = newValue ? JSON.parse(newValue) : null;
        break;
    }
    this.render();
  }

  set onQuestionnaireResponseChange(callback) {
    this._onQuestionnaireResponseChange = callback;
  }

  render() {
    // Custom rendering logic implementation
  }
}
```

## Hosting Configuration

Custom renderers require hosting through web-accessible endpoints that the Forms Builder can load dynamically

## Forms Builder Integration

Integration with the Forms Builder occurs through SDC (Structured Data Capture) configuration resources that register custom renderers with the system. These configurations specify renderer metadata including source URLs, display names, and loading parameters.

```json
{
  "resourceType": "SDCConfig",
  "name": "custom-renderers-config",
  "builder": {
    "custom-renderers": [{
      "name": "simple-questionnaire-renderer",
      "source": "http://localhost:8081/simple-questionnaire-renderer.js",
      "title": "Simple Questionnaire Renderer"
    }]
  }
}
```

## Preview and Testing

The Forms Builder preview system loads custom renderers dynamically when users select them from the available options. This preview functionality enables real-time testing of custom rendering logic without requiring separate deployment or testing environments.

Testing custom renderers involves verifying proper component loading, data binding functionality, and response capture mechanisms. The preview system provides immediate feedback on renderer behavior and helps identify integration issues before deployment to production environments.

## Example Project

A complete working example of custom renderer implementation is available in the [Aidbox Forms Builder Custom Renderer repository](https://github.com/Aidbox/examples/tree/main/aidbox-forms/aidbox-forms-builder-custom-renderer). This example demonstrates the full setup process including Docker configuration, renderer implementation, and Forms Builder integration.

