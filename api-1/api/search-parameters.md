# Search Parameters

_A `SearchParameter` resource specifies a search parameter that may be used on the RESTful API to search or filter on a resource. The SearchParameter resource declares:_

* _how to refer to the search parameter from a client_
* _how the search parameter is to be understood by the server_
* _where in the source resource the parameter matches_

**`Note:`** `Search Parameter name is not equal to element name.`

You can see in [FHIR](https://www.hl7.org/fhir/searchparameter.html) specification Search Parameters and their according[ attributes](https://github.com/Aidbox/documentation/tree/560cedaf13f66f43be9f122cb8c4e2af0dcc066c/api-1/api/www.hl7.org/fhir/encounter.html#search). You can look up attached to see where search parameters can be found in the Aidbox interface Aidbox also can return search parameters for specific resouce by REST API. Here's an example for Encounter: GET /SearchParameter?resource=Encounter

![](../../.gitbook/assets/image%20%2853%29.png)

To search by Encounter.serviceProvider as it's stated in FHIR Encounter specification you should use service-provider search parameter.

![Search Parameters in Aidbox UI](../../.gitbook/assets/image%20%2846%29%20%281%29.png)

