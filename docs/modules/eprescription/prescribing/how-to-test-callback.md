---
description: Test ePrescription callback webhooks for asynchronous message processing and error handling.
---

# How to test Callback

Visit the Message Tester section within Surescripts workbench. From there, you can manage templates and compose messages.&#x20;

{% hint style="warning" %}
Surescripts makes up to 3 additional delivery attempts with a delay of at least 1 minute, 5 minutes and then 10 minutes between each attempt.
{% endhint %}

Right now callback is supposed to be used for `NewRx` and `CancelRx` only.

Endpoint expects a `xml` body with message. In a Header element there is `RelatesToMessageID` tag which have to contain a message id that was generated on transmit. You can find it in a relevant `MedicationRequest` identifiers, eg `MedicationRequest.identifier.where(system = "urn:app:aidbox:e-prescriptions:surescripts:message-id").value`.

{% hint style="warning" %}
For `CancelRx` there is same `MedicationRequest` used, so to distinguish between `NewRx` and `CancelRx` there is type specified, eg:
{% endhint %}

```
{
  "system": "urn:app:aidbox:e-prescriptions:surescripts:message-id",
  "type": {
    "coding": [
      {
        "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
        "code": "Cancel"
      }
    ]
  },
  "value": 123
}
```

Then the body with will be mapped and processed accordingly to message type.

## Examples for `NewRx` callback

* **Verify** message

<pre class="language-xml"><code class="lang-xml">&#x3C;?xml version="1.0" encoding="utf-8"?>
&#x3C;Message DatatypesVersion="20170712" TransportVersion="20170712" TransactionDomain="SCRIPT" TransactionVersion="20170712" StructuresVersion="20170712" ECLVersion="20170712">
  &#x3C;Header>
    &#x3C;To Qualifier="P">*ToEndpoint*&#x3C;/To>
    &#x3C;From Qualifier="D">*FromEndpoint*&#x3C;/From>
    &#x3C;MessageID>249afd334b944255863f11e08d234a0c&#x3C;/MessageID>
    &#x3C;RelatesToMessageID><a data-footnote-ref href="#user-content-fn-1">249afd334b944255863f11e08d234a0c</a>&#x3C;/RelatesToMessageID>
    &#x3C;SentTime>2017-06-23T19:34:31Z&#x3C;/SentTime>
    &#x3C;SenderSoftware>
      &#x3C;SenderSoftwareDeveloper>OneDeveloper&#x3C;/SenderSoftwareDeveloper>
      &#x3C;SenderSoftwareProduct>1Dev&#x3C;/SenderSoftwareProduct>
      &#x3C;SenderSoftwareVersionRelease>1&#x3C;/SenderSoftwareVersionRelease>
    &#x3C;/SenderSoftware>
  &#x3C;/Header>
  &#x3C;Body>
    &#x3C;Verify>
      &#x3C;VerifyStatus>
        &#x3C;Code>010&#x3C;/Code>
      &#x3C;/VerifyStatus>
    &#x3C;/Verify>
  &#x3C;/Body>
&#x3C;/Message>
</code></pre>

* **Error** message

<pre class="language-xml"><code class="lang-xml">&#x3C;?xml version="1.0" encoding="utf-8"?>
&#x3C;Message DatatypesVersion="20170712" TransportVersion="20170712" TransactionDomain="SCRIPT" TransactionVersion="20170712" StructuresVersion="20170712" ECLVersion="20170712">
  &#x3C;Header>
    &#x3C;To Qualifier="P">*ToEndpoint*&#x3C;/To>
    &#x3C;From Qualifier="D">*FromEndpoint*&#x3C;/From>
    &#x3C;MessageID>d1e736a88a88423ba5ea3a543a7f869d&#x3C;/MessageID>
    &#x3C;RelatesToMessageID><a data-footnote-ref href="#user-content-fn-1">249afd334b944255863f11e08d234a0c</a>&#x3C;/RelatesToMessageID>
    &#x3C;SentTime>2017-06-08T19:34:31.20Z&#x3C;/SentTime>
    &#x3C;SenderSoftware>
      &#x3C;SenderSoftwareDeveloper>OneDeveloper&#x3C;/SenderSoftwareDeveloper>
      &#x3C;SenderSoftwareProduct>1Dev&#x3C;/SenderSoftwareProduct>
      &#x3C;SenderSoftwareVersionRelease>1&#x3C;/SenderSoftwareVersionRelease>
    &#x3C;/SenderSoftware>
  &#x3C;/Header>
  &#x3C;Body>
    &#x3C;Error>
      &#x3C;Code>900&#x3C;/Code>
      &#x3C;Description>Sender ID not on file&#x3C;/Description>
    &#x3C;/Error>
  &#x3C;/Body>
&#x3C;/Message>
</code></pre>

[^1]: Put your ID here
