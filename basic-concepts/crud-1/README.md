---
description: 'Common operations: create, read, update, and delete.'
---

# CRUD

This part of еру documentation describes how to create, read, update, and delete resources. Also, it covers some advanced topics like conditional create, update, and delete. Aidbox REST API slightly differs from canonical FHIR REST API. There is the [article]() that describes those differences.

All sample requests can be run from the Postman collection:[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/cd401dd93c5efab171ac?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2Fcd401dd93c5efab171ac%23%3Fenv[Aidbox.Cloud]%3DW3sia2V5IjoiYmFzZTEiLCJ2YWx1ZSI6Imh0dHBzOi8vbWVyZWRpdGguYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6ZmFsc2V9LHsia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9wYXZseXNoaW5hMjAxODExMDkuYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX1d&_ga=2.141573233.23745025.1543592968-654445837.1543359065)

## Introduction

A **resource** is an object with a type, associated data, relationships to other resources, and a set of methods that operate on it \(information about that can be found in FHIR [specification](https://www.hl7.org/fhir/resourcelist.html) or through Aidbox [metadata](../introspection/custom-metadata.md)\). In most cases, a resource is represented as a JSON/XML/YAML document.

Each resource has its own resource **type**, this type defines a set of data which can be stored with this resource, and possible relationships with other resources.

**Attribute** is a part of the resource definition, which describes what fields can or must be present in the resource document, type of such fields, and their cardinality.

Every resource type has the same set of **interactions** available. These interactions are described below. 

Each interaction can fail with:

* **`403`** **Forbidden** — client is not authorized to perform the interaction

## 

## 

