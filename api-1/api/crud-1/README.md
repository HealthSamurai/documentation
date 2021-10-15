---
description: >-
  This section of the documentation describes how to create, read, update, and
  delete resources. Also, it covers some advanced topics like conditional
  create, update, and delete.
---

# CRUD



{% hint style="info" %}
Aidbox provides two REST APIs - FHIR and Aidbox. The main difference is [a format of resources](../../../modules-1/fhir-resources/aidbox-and-fhir-formats.md). Base URL for FHIR API is **/fhir **and for Aidbox **/**
{% endhint %}

All sample requests can be run from the Postman collection:[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/cd401dd93c5efab171ac?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2Fcd401dd93c5efab171ac%23%3Fenv\[Aidbox.Cloud]%3DW3sia2V5IjoiYmFzZTEiLCJ2YWx1ZSI6Imh0dHBzOi8vbWVyZWRpdGguYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6ZmFsc2V9LHsia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cHM6Ly9wYXZseXNoaW5hMjAxODExMDkuYWlkYm94LmFwcCIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX1d&\_ga=2.141573233.23745025.1543592968-654445837.1543359065)

## Introduction

A **resource** is an object with a type, associated data, relationships to other resources, and a set of methods that operate on it (information about that can be found in FHIR [specification](https://www.hl7.org/fhir/resourcelist.html) or through Aidbox [metadata](../../../modules-1/custom-resources/custom-metadata.md)). In most cases, a resource is represented as a JSON/XML/YAML document.

Each resource has its own resource **type**, this type defines a set of data that can be stored with this resource, and possible relationships with other resources.

**Attribute** is a part of the resource definition, which describes what fields can or must be present in the resource document, type of such fields, and their cardinality.

Every resource type has the same set of **interactions** available. These interactions are described below. 

Each interaction can fail with:

* **`403`** **Forbidden** — client is not authorized to perform the interaction
