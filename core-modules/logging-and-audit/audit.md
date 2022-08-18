## Audit

Aidbox automatically logs all auth, API, database and network events, so in most cases basic audit log may be derived from [Aidbox logs](README.md).

In rare cases Aidbox logs are not enough for auditing. For instance, you'd like to track business-relevant events happening outside of Aidbox control.

{% hint style="info" %}
*Example:* You may use the same patient search operaion `GET /Patient?_sort=-lastUpdated&_count=100` for showing it on UI and for making reports. 
And from audit perspective you may consider it as different events.
{% endhint %}

In this case you may consider to options Aidbox provides:

Extending Aidbox logs. Aidbox allows you to enhance logs with your own data and push your own logs into Aidbox stream along with its internal logs.

Use FHIR AuditEvent to store business-relevant events. FHIR introduced AuditEvent resource which plays well in FHIR ecosystem. Aidbox provides all FHIR API operation for AuditEvent resource.

http://hl7.org/fhir/auditevent.html
