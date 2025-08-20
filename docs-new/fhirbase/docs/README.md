---
description: What is Fhirbase and why you might want to use it.
---

# Overview

Data is the heart of any healthcare system, and thus should it should be properly modelled and managed reliably. Open source [FHIR](https://hl7.org/fhir/) standard provides you an robust data model covering most important healthcare domains. [PostgreSQL](https://www.postgresql.org/) is a battle-proven open source relational database which supports storing of JSON documents while preserving [ACID guarantees](https://en.wikipedia.org/wiki/ACID_\(computer_science\)) and the richness of the SQL language. Combination of these two technologies is a perfect foundation to build your system on.

Fhirbase is a common name for a command line tool  and set of libraries aiming to lower the entry barrier for both FHIR and PostgreSQL. It gives you an ready to use components to boost your development, as well as guidelines how to store and access your FHIR data. With Fhirbase you can break FHIR API abstraction and operate with FHIR data on database level.

### History of Fhirbase

We've already completed two major re-thinks and re-writes of Fhirbase. Our original idea was to implement an essential part of the FHIR specification - CRUD, Hx, Search - inside a database using stored procedures written in [PL/PgSQL language](https://en.wikipedia.org/wiki/PL/pgSQL). The problem was with the expressiveness of PL/PgSQL, which is an archaic language that is quite slow. So we [rewrote it in JavaScript](https://github.com/fhirbase/fhirbase-plv8) and it was quite successful to be used in several production systems.

While using this version, we recognized that sometimes only the schema and persistence parts of Fhirbase were valuable. Most likely you would want to implement FHIR logic in your application layer instead of database stored procedures. So at this time we decided to decouple Fhirbase into three components:

* Database Schema & utils to store and query FHIR information in PostgreSQL
* FHIR metadata storage and manipulation
* FHIR operations implementation

Current version of Fhirbase is focused on the first one, with an emphasis on good performance and simplicity.
