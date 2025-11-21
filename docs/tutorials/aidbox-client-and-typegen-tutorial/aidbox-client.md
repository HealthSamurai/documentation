# Aidbox Client in TypeScript

## Install the aidbox-client dependency

{% code %}
```sh
npm install @health-samurai/aidbox-client
```
{% endcode %}

By default, the client comes with a selection of predefined types from the HL7 FHIR R4 Core package.

## Usage

First, create the client:

{% code %}
```typescript
import { makeClient } from "@health-samurai/aidbox-client"

const aidbox = makeClient({ baseurl: "https://aidbox.address/" })
```
{% endcode %}

The `makeClient` constructor accepts an object with the following properties:

- `baseurl`: URL to the Aidbox server
- `onRawResponseHook`: a function that runs after the raw response is received from the server.

After that, the client is ready to be used.

### Untyped raw requests

To make a request that doesn't assume any response type, use the `rawRequest` method:

{% code %}
```typescript
const result = await aidbox.rawRequest({
  method: "GET",
  url: "/fhir/Patient/pt-1",
})
```
{% endcode %}

The response is an object of the `AidboxRawResponse` type with the following fields:

- `response`: a [Response](https://developer.mozilla.org/en-US/docs/Web/API/Response) object
- `responseHeaders`: a `Record<string, string>` object with response headers
- `duration`: a `number` of milliseconds it took for the server to receive and respond to the request
- `request`: a request body formatted as it was sent to the server (for debugging purposes)

The `response` field can then be used to receive the response body as JSON, text, or read manually.

If the server responded with a non-2XX code, this function throws an `AidboxErrorResponse` object.
See [errors](#errors) for more information on error handling.

### Typed requests

To make a request that returns a given type, use the `request` method:

{% code %}
```typescript
const result = await aidbox.request<Patient>({
  method: "GET",
  url: "/fhir/Patient/pt-1",
})
```
{% endcode %}

Typed requests can return either the requested type, or the `OperationOutcome`, if the server responded with one.

The returned object will be of type `AidboxResponse<T>`, and its only difference from `AidboxRawResponse` is that it contains an additional field `responseBody` of type `T`, specified in the request.

For example, when we request an existing `Patient`, the server responds with the patient object:

{% code %}
```typescript
const patient1: Patient | OperationOutcome = await aidbox.request<Patient>({
  method: "GET",
  url: "/fhir/Patient/pt-1",
}).then((result) => result.responseBody)

// patient1:
{
  "name": [{
    "family": "John2"
  }],
  "id": "pt-1",
  "resourceType": "Patient",
  "meta": {
    "lastUpdated": "2025-11-17T15:01:46.339168Z",
    "versionId": "109",
    "extension": [{
      "url": "ex:createdAt",
      "valueInstant": "2025-11-14T11:46:27.128067Z"
    }]
  }
}
```
{% endcode %}

However, when we request a non-existing patient, we get an operation outcome:

{% code %}
```typescript
const patient2: Patient | OperationOutcome = await aidbox.request<Patient>({
  method: "GET",
  url: "/fhir/Patient/non-existent-patient",
}).then((result) => result.responseBody)

// patient2:
{
  "resourceType": "OperationOutcome",
  "id": "not-found",
  "text": {
    "status": "generated",
    "div": "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>Resource Patient/non-existent-patient not found</p></div>"
  },
  "issue": [{
    "severity": "fatal",
    "code": "not-found",
    "diagnostics": "Resource Patient/non-existent-patient not found"
  }]
}
```
{% endcode %}

In cases when the error occurred, but the server did not respond with the `OperationOutcome`, this function throws an `AidboxErrorResponse` object.

### Errors

Errors are represented either as `AidboxErrorResponse` or `AidboxClientError`.

`AidboxErrorResponse` is thrown when the request was sent but an error occurred during response processing.
This class extends the `Error` class, and contains one additional field: `rawResponse`, which is an instance of the `Response` class.

`AidboxClientError` is thrown if the error happened before the request was sent, or if an unexpected error happened during the request.
The `cause` field may contain the original error in the latter case.

## Response preprocessing

The `makeClient` constructor accepts a function as the `onResponse` argument, which will be called on every response from the server.
This function accepts a `Response`, and doesn't return anything.
Its purpose is to handle termination of request processing, based on some information in the `Response` object, for example, authentication redirecting:

{% code %}
```typescript
const baseurl = "https://aidbox.address/"

function authHandler(response: Response): void {
  if (response.status === 401 || response.status === 403) {
    const encodedLocation = btoa(window.location.href);
    const redirectTo = `${baseurl}/auth/login?redirect_to=${encodedLocation}`;
    window.location.href = redirectTo;
    throw redirect({ href: redirectTo });
  }
}

makeClient<Bundle, OperationOutcome, User>({
  baseurl,
  onResponse: makeAuthHandler(baseurl),
})
```
{% endcode %}
