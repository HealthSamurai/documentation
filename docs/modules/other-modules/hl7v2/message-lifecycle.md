---
description: Aidbox module: Message Lifecycle for extended FHIR functionality and integration.
---

# Message Lifecycle

The entire message lifecycle is persisted in the HL7v2ModuleMessage resource. There are several statuses with own meanings:

1. `blank` - Initial state of the empty resource. It is used to get an id of the created resource for mapping. This step is automated during the mapping process.
2. `draft` - This status means that the message is correctly mapped to the HL7 format. Only this status allows you to send the message via the send endpoint.
3. `requested` - This status appears when the send endpoint is called and the message is successfully placed in the send queue, which means that the delivery process has started and the module is waiting for the message consumer's response.
4. `received` - This is a successful completion status, meaning that the message was received by the consuming system without any problems.
5. `failed` - This is a negative status that appears when an error occurs in either the mapping or the sending process. The corresponding error is persisted under the `outcomeReason` key.
