# SDK internals

### Simplified manifest syntax

SDK clients uses simplified manifest syntax, which will be transformed and merged in aidbox mainfest. 

The example of simplified manifest can be found on NodeJs SDK [page](untitled.md).

Main keys is `addon`, `resources` and `operations`. `app` is information about aidbox app \(name and version\), `resources` is a map of resource name and definition of resource attributes, operations is a map of operation name and definition of request pattern \(http verb and path segments\).

