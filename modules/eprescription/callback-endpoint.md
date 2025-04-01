# Callback endpoint

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

```xml
<?xml version="1.0" encoding="utf-8"?>
<Message DatatypesVersion="20170712" TransportVersion="20170712" TransactionDomain="SCRIPT" TransactionVersion="20170712" StructuresVersion="20170712" ECLVersion="20170712">
  <Header>
    <To Qualifier="P">*ToEndpoint*</To>
    <From Qualifier="D">*FromEndpoint*</From>
    <MessageID>249afd334b944255863f11e08d234a0c</MessageID>
    **<RelatesToMessageID>249afd334b944255863f11e08d234a0c</RelatesToMessageID>**
    <SentTime>2017-06-23T19:34:31Z</SentTime>
    <SenderSoftware>
      <SenderSoftwareDeveloper>OneDeveloper</SenderSoftwareDeveloper>
      <SenderSoftwareProduct>1Dev</SenderSoftwareProduct>
      <SenderSoftwareVersionRelease>1</SenderSoftwareVersionRelease>
    </SenderSoftware>
  </Header>
  <Body>
    **<Verify>
      <VerifyStatus>
        <Code>010</Code>
      </VerifyStatus>
    </Verify>**
  </Body>
</Message>
```

* **Error** message

```xml
<?xml version="1.0" encoding="utf-8"?>
<Message DatatypesVersion="20170712" TransportVersion="20170712" TransactionDomain="SCRIPT" TransactionVersion="20170712" StructuresVersion="20170712" ECLVersion="20170712">
  <Header>
    <To Qualifier="P">*ToEndpoint*</To>
    <From Qualifier="D">*FromEndpoint*</From>
    <MessageID>d1e736a88a88423ba5ea3a543a7f869d</MessageID>
    **<RelatesToMessageID>249afd334b944255863f11e08d234a0c</RelatesToMessageID>**
    <SentTime>2017-06-08T19:34:31.20Z</SentTime>
    <SenderSoftware>
      <SenderSoftwareDeveloper>OneDeveloper</SenderSoftwareDeveloper>
      <SenderSoftwareProduct>1Dev</SenderSoftwareProduct>
      <SenderSoftwareVersionRelease>1</SenderSoftwareVersionRelease>
    </SenderSoftware>
  </Header>
  <Body>
    **<Error>
      <Code>900</Code>
      <Description>Sender ID not on file</Description>
    </Error>**
  </Body>
</Message>
```
