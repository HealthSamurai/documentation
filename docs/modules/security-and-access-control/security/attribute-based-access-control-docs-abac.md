# Attribute-based Access Control (docs/ABAC)

Attribute-Based Access Control (ABAC) in the context of FHIR refers to a method of managing access to healthcare information by evaluating attributes rather than relying solely on roles or static permissions.

In ABAC for FHIR, access decisions are based on policies that consider multiple attributes, including:

1. **User Attributes**: Attributes related to the identity of the user trying to access the data, such as their role (doctor, nurse, administrator), specialty, or organizational affiliation.
2. **Resource Attributes**: Characteristics of the FHIR resources being accessed, like the type of information (e.g., clinical notes, lab results), sensitivity level, or patient consent preferences associated with the data.
3. **Action Attributes**: The type of action being requested on the FHIR resources, such as read, write, update, or delete operations.
4. **Contextual Attributes**: The context of the access request, which might include the time of the request, the location from which the request originates, or the urgency of the situation.

* [AccessPolicy](accesspolicy.md)

* [Evaluation engines](evaluation-engines.md)

* [Label-based Access Control](../../access-control/authorization/label-based-access-control.md)
