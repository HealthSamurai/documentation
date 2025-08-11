# Scoped API

Scoped APIs are special APIs limited to resources within a specific compartment. Such API do not require complicated access control logic, because this logic is built into the API definition.

* [Compartments API ](compartments-api.md)- API for resources within a certain [FHIR compartment](https://www.hl7.org/fhir/compartmentdefinition.html)
* [Patient Data API](patient-data-access-api.md) - API for patient-related resources that allows access to resources that belong to a specific patient
* [Organization-based hierarchical access control](organization-based-hierarchical-access-control.md) - API to control access to resources within a specific organization. Supports hierarchical access control
