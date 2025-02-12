# CRUD

{% hint style="info" %}
Aidbox provides two REST APIs - FHIR and Aidbox. The main difference is [a format of resources](../../../storage-1/other/aidbox-and-fhir-formats.md). Base URL for FHIR API is **/fhir** and for Aidbox **/**

**It is recommended to always use /fhir endpoint.**
{% endhint %}

## Introduction

A **resource** is an object with a type, associated data, relationships to other resources, and a set of methods that operate on it (information about that can be found in FHIR [specification](https://www.hl7.org/fhir/resourcelist.html)). In most cases, a resource is represented as a JSON/XML/YAML document.

Each resource has its resource **type**, which defines a set of data that can be stored with this resource and possible relationships with other resources.

Every resource type has the same set of **interactions** available. These interactions are described below.

Each interaction can fail with **`403`** **Forbidden** if client is not authorized to perform the interaction.&#x20;
