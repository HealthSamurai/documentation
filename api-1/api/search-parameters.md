# Search Parameters

FHIR Search Parameters are used to search resources by elements values. 

**Note:** Search Parameter name is not equal to element name. 

You can see in [FHIR](https://www.hl7.org/fhir/searchparameter.html) specification Search Parameters and their according attributes. You can look up attached to see where search parameters can be found in the Aidbox interface Aidbox also can return search parameters for specific resouce by REST API. Here's an example for Encounter: GET /SearchParameter?resource=Encounter

To search by Encounter.serviceProvider as it's stated in FHIR Encounter specification you should use service-provider search parameter: http:///fhir/Encounter?patient=&\_include=Encounter:service-provider:Organization

![](../../.gitbook/assets/image%20%2890%29.png)

![](../../.gitbook/assets/image%20%2889%29.png)

hl7.org/fhir/encounter.html\#search



