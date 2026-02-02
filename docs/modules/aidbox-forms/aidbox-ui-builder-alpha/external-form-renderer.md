---
description: Build custom FHIR questionnaire renderers as standalone pages for Aidbox Forms Builder preview using SDC SMART Web Messaging.
---

# External Form Renderer in Builder Preview

The Forms Builder can load a custom renderer inside its preview iframe. You provide a URL to a page that implements [SDC SMART Web Messaging](https://github.com/brianpos/sdc-smart-web-messaging). The Builder sends messages to the page; the page renders the questionnaire and replies with updates.

## How it works

1) The Builder opens your renderer page in an iframe.
2) The Builder appends `embedded_mode=true`, `messaging_handle`, and `messaging_origin` to the URL.
3) The Builder and renderer exchange `postMessage` requests and responses.

The protocol is defined here: [SDC SMART Web Messaging](https://github.com/brianpos/sdc-smart-web-messaging)

## Requirements

Your renderer must be a normal web page (HTML + JS). It must:

- Read `messaging_handle` and `messaging_origin` from the URL.
- Send and receive SWM messages via `postMessage`.
- Reply to all request messages with the same `messageType` and `responseToMessageId`.
- Allow embedding in an iframe. Configure `Content-Security-Policy: frame-ancestors` and avoid `X-Frame-Options: DENY` or `SAMEORIGIN`.

## Minimal implementation

This is the smallest useful SWM renderer loop. It performs the handshake, accepts a Questionnaire, and returns a QuestionnaireResponse when asked. Replace the `renderForm` and `getCurrentResponse` stubs with your renderer logic.

```js
const params = new URLSearchParams(window.location.search);
const messagingHandle = params.get("messaging_handle");
const messagingOrigin = params.get("messaging_origin");
const hostWindow = window.opener || window.parent;

function postToHost(message) {
  hostWindow.postMessage(message, messagingOrigin);
}

function sendEvent(messageType, payload) {
  postToHost({
    messagingHandle,
    messageId: crypto.randomUUID(),
    messageType,
    payload,
  });
}

function sendResponse(messageType, responseToMessageId, payload) {
  postToHost({
    messagingHandle,
    messageId: crypto.randomUUID(),
    messageType,
    responseToMessageId,
    payload,
  });
}

function renderForm(questionnaire, questionnaireResponse) {
  // Render your form here
}

function getCurrentResponse() {
  // Return the current QuestionnaireResponse here
  return null;
}

sendEvent("status.handshake", { protocolVersion: "1.0", fhirVersion: "R4" });

window.addEventListener("message", (event) => {
  if (event.origin !== messagingOrigin) return;
  if (event.source !== hostWindow) return;

  const message = event.data || {};
  if (message.messagingHandle && message.messagingHandle !== messagingHandle) {
    return;
  }
  if (!message.messageType) return;

  switch (message.messageType) {
    case "status.handshake":
      sendResponse("status.handshake", message.messageId, {
        application: { name: "My Renderer" },
      });
      return;

    case "sdc.displayQuestionnaire":
      renderForm(message.payload.questionnaire, message.payload.questionnaireResponse);
      sendResponse("sdc.displayQuestionnaire", message.messageId, { status: "success" });
      return;

    case "sdc.requestCurrentQuestionnaireResponse":
      sendResponse("sdc.requestCurrentQuestionnaireResponse", message.messageId, {
        questionnaireResponse: getCurrentResponse(),
      });
      return;

    default:
      sendResponse(message.messageType, message.messageId, {
        status: "error",
        statusDetail: { message: "Unsupported message type" },
      });
  }
});
```

## Supported messages (minimum)

For Builder preview, implement at least these message types and respond with the same `messageType` and `responseToMessageId`:

- [status.handshake](https://github.com/brianpos/sdc-smart-web-messaging/blob/main/docs/sdc-swm-protocol.md#statushandshake)
- [sdc.configure](https://github.com/brianpos/sdc-smart-web-messaging/blob/main/docs/sdc-swm-protocol.md#sdcconfigure) (respond with `{ status: "success" }`)
- [sdc.configureContext](https://github.com/brianpos/sdc-smart-web-messaging/blob/main/docs/sdc-swm-protocol.md#sdcconfigurecontext) (respond with `{ status: "success" }`)
- [sdc.displayQuestionnaire](https://github.com/brianpos/sdc-smart-web-messaging/blob/main/docs/sdc-swm-protocol.md#sdcdisplayquestionnaire) (respond with `{ status: "success" }`)
- [sdc.requestCurrentQuestionnaireResponse](https://github.com/brianpos/sdc-smart-web-messaging/blob/main/docs/sdc-swm-protocol.md#sdcrequestcurrentquestionnaireresponse) (respond with `{ questionnaireResponse: ... }`)

If your renderer emits updates, send [sdc.ui.changedQuestionnaireResponse](https://github.com/brianpos/sdc-smart-web-messaging/blob/main/docs/sdc-swm-protocol.md#sdcuichangedquestionnaireresponse) events when the response changes.

## Add the renderer in Forms Builder

### Local renderer (per user)

1) Open Forms Builder.
2) Click the renderer switcher (eye icon near the theme selector).
3) Click **Add custom renderer**.
4) Provide a name and a renderer URL.
5) Save and select the renderer in the preview.

Local renderers are saved in your browser and are editable/removable. Do not use the **Aidbox Forms** name.

### Managed renderer (via SDCConfig)

You can expose renderers to all users via SDCConfig. These entries are read-only in the Builder and appear with a **Managed** badge.

```json
{
  "resourceType": "SDCConfig",
  "name": "custom-renderers-config",
  "builder": {
    "custom-renderers": [
      {
        "name": "external-renderer",
        "url": "https://example.com/renderer",
        "default": true
      }
    ]
  }
}
```

If a managed renderer has the same name as a local renderer, the managed one wins.

## Hosted renderers

You can use these ready-to-use renderer pages:

- Smart Forms: https://aidbox.github.io/examples/renderers/smart-forms/
- LHC Forms: https://aidbox.github.io/examples/renderers/lhc-forms/

Their source code is available in the examples repository and can serve as a reference implementation if you are building your own renderer:
https://github.com/Aidbox/examples/tree/main/aidbox-forms/aidbox-forms-builder-custom-renderer

## Troubleshooting

- **Preview is blank / iframe doesn’t load**
  - If your server sets `Content-Security-Policy`, allow embedding with `frame-ancestors` (optional if you don’t set CSP).
  - If your server sets `X-Frame-Options`, remove `DENY` or `SAMEORIGIN` (optional if you don’t set this header).
- **No messages received**
  - Ensure your page reads `messaging_handle` and `messaging_origin` from the URL and filters by them.
  - Verify you reply to `status.handshake`, `sdc.configure`, and `sdc.configureContext` before expecting `sdc.displayQuestionnaire`.
- **Messages received but no updates**
  - Confirm you respond with the same `messageType` and `responseToMessageId` for every request.
  - Emit `sdc.ui.changedQuestionnaireResponse` if you want live preview updates.

## Example project

A working example renderer is available here:
https://github.com/Aidbox/examples/tree/main/aidbox-forms/aidbox-forms-builder-custom-renderer
