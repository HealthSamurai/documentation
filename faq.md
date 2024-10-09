# FAQ

## **How can I load lots of data at a time with Aidbox API?**

You can use [`Bulk API`](https://docs.aidbox.app/api-1/bulk-api-1) for that.

## **How to increase speed/performance for requests with complex parameters?**

Create [indexes](https://docs.aidbox.app/api-1/fhir-api/search-1/usdlookup#create-indexes) for that.

## **Does Aidbox support MFA authentication?**

Yes, please, check out [the documentation](modules/security-and-access-control/auth/two-factor-authentication.md).

## **How to create a SearchParameter for extensions on a property in a resource?**

You can create [custom search parameters](https://docs.aidbox.app/api-1/fhir-api/search-1/searchparameter) for extensions and to make SearchParameter easier to write you can also [define attributes](https://docs.aidbox.app/modules-1/first-class-extensions).

## **I’m building a request with revinclude. Is it possible to also apply a search for the included resource in that request?**

In the FHIR Search API, this is called chained parameters. Read the details [here](https://docs.aidbox.app/api-1/fhir-api/search-1/chained-parameters) and [here](https://www.hl7.org/fhir/search.html#chaining).

## Aidbox appears to have a POST size limit of 20 MB by default. Is this configurable?

You can change the size limit by editing [this environment variable](https://docs.aidbox.app/reference/configuration/environment-variables/optional-environment-variables#box\_web\_max\_\_body). However, for sizable data, we recommend using [Bulk API](https://docs.aidbox.app/api-1/bulk-api-1).

## I don't want to use BASIC authentication for security reasons. What can I use instead?

We suggest OAuth implementation in the Client Credentials Grant. You need to use “Client Credentials Grant + Access Policy”:

* Create a Client and teach the external system to [receive tokens](https://docs.aidbox.app/auth/client-credentials);
* Create an appropriate[ access policy](https://docs.aidbox.app/security/access-control#matcho-engine).

## My search doesn’t work with the full link to the resource included. Why is that?

An example of a query that doesn't work:

`GET /DocumentReference?patient=`[`https://my.aidbox.app/Patient/1234`](https://my.aidbox.app/Patient/1234)

It’s called an absolute reference. Aidbox doesn't work with absolute references because the logic can become very tricky since these references can point to external servers.

Use relative references instead: `GET /DocumentReference?patient=Patient/1234`
