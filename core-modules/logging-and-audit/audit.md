## Audit logs

Aidbox automatically logs all auth, API, database and network events, so in most cases basic audit log may be derived from Aidbox system log.

In rare cases Aidbox system logs are not enough. For instance, you would like to track business-relevant events happening outside of Aidbox control.

> Example:
> Showing list of patients on UI and export patient list to a file may require the same request to Aidbox
> You can use the same patient search operation for, let's say, for just showing the patient list on UI or to make an export file, which are different events from business perspective.


Aidbox provides you two ways to recieve your events. ???

Extending Aidbox logs. Aidbox allows you to enhance logs with your own data and push your own logs into Aidbox stream along with its internal logs.

Use FHIR AuditEvent to store business-relevant events. FHIR introduced AuditEvent resource which plays well in FHIR ecosystem. Aidbox provides all FHIR API operation for AuditEvent resource.

http://hl7.org/fhir/auditevent.html
