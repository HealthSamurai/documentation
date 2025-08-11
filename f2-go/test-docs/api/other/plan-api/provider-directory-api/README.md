# Provider Directory API

Welcome! This documentation is designed to help developers and integrators navigate the functionalities and features of the Plan API Provider Directory, which conforms to the [DaVinci PDEX Plan Net Implementation Guide](https://hl7.org/fhir/us/davinci-pdex-plan-net/STU1/profiles.html).

Plan API makes Provider Directory available through public endpoints.

You can begin by [exploring](https://hl7.org/fhir/us/davinci-pdex-plan-net/STU1/profiles.html) the resource specifications to familiarize yourself with the data structures and types available. Then, dive into the [search parameters](../../../rest-api/fhir-search/searchparameter.md) to learn how to craft queries tailored to your needs. You will find query examples that illustrate common use cases and demonstrate how to retrieve the data you need using the API.

We store provider information in these FHIR entities:

* Practitioner — basic information about a practitioner. Contains demographics, specialty, language and identifiers like NPI and IRS.
* PractitionerRole — central entity to connect practitioner's other entities.
* Organization — a practitioner's place of work. Also represents networks a provider is part of.
* OrganizationAffiliation — relationship between multiple providers.
* Location — a practitioner's location info (address) and telecom info (phone, email).
* HealthcareService — services provided by a practitioner.

{% content-ref url="practitioner.md" %}
[practitioner.md](practitioner.md)
{% endcontent-ref %}

{% content-ref url="practitionerrole.md" %}
[practitionerrole.md](practitionerrole.md)
{% endcontent-ref %}

{% content-ref url="organization.md" %}
[organization.md](organization.md)
{% endcontent-ref %}

{% content-ref url="organizationaffiliation.md" %}
[organizationaffiliation.md](organizationaffiliation.md)
{% endcontent-ref %}
