# Audit

Aidbox automatically logs all auth, API, database, and network events, so in most cases basic audit logs may be derived from [Aidbox logs](../../core-modules/logging-and-audit/).

In rare cases, Aidbox logs are not enough for auditing. For instance, you'd like to track business-relevant events that happen outside of Aidbox control.

{% hint style="info" %}
_**Example.**_ You may use the same patient search operation&#x20;

`GET /Patient?_sort=-lastUpdated&_count=100`&#x20;

to show it on UI and for making reports. And from an audit perspective, you may consider it as different events.
{% endhint %}

In this case, you may consider two options Aidbox provides.

### FHIR AuditEvent

Aidbox provides comprehensive FHIR API for [AuditEvent](https://www.hl7.org/fhir/auditevent.html) from [FHIR Security ecosystem](http://hl7.org/fhir/security.html#audit).&#x20;

{% content-ref url="audit-logging.md" %}
[audit-logging.md](audit-logging.md)
{% endcontent-ref %}

### Extending Aidbox Logs

Aidbox allows you to enhance logs with your data and push your logs into Aidbox stream along with its internal logs.

{% content-ref url="../observability/logging-and-audit/extending-access-logs.md" %}
[extending-access-logs.md](../observability/logging-and-audit/extending-access-logs.md)
{% endcontent-ref %}
