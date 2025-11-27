---
title: "Should you use FHIR resources as your data storage format?"
slug: "should-you-use-fhir-resources-as-your-data-storage-format"
published: "2018-09-05"
author: "Niquola Ryzhikov"
reading-time: "4 min read"
tags: []
category: "Storage"
teaser: "Using FHIR for data persistence is becoming a popular topic of discussion in the community. Our CTO shares his thoughts on when and why you would use FHIR as the storage format for your healthcare data. "
image: "cover.jpg"
---

*G*r*ahame Grieve published a post on his blog about FHIR resources persistence —*[*read it here*](http://www.healthintersections.com.au/?p=2776)*. The main question he addresses is how to store FHIR resources — should we put them into a database “as is” using JSON (ProtoBuf etc.), or create your specific schema?*

### **Format for persistence**

Grahame’s first statement is that the FHIR format was designed for information exchange, not for persistence. We (Health Samurai) are discussing with the FHIR community the possibility of using the [FHIR format specification for persistence](https://github.com/fhir-fuel/fhir-fuel.github.io). However, Grahame does not deny this possibility in his post! So this is one of the topics to discuss at our connectathon track — [Storage & Analytics on FHIR](https://wiki.hl7.org/index.php?title=201809_FHIR_Storage_and_Analytics).

### **Specialized vs. Generic Systems**

Grahame distinguishes between specialized systems with fixed model and more generic systems (i.e., Clinical Data Repositories). In the first case, you can potentially design a more efficient schema to store and query your data. However, the more generic your system becomes, the more attractive it becomes to store FHIR resources as is.

I’d like to share a few thoughts about this.

### **Structural Complexity of Health IT**

In my opinion, one of the most challenging problems in health IT is a good information model.

What makes an information model good? A good model is resistant to changes in requirements and use cases because it has some objective relationships with the real world. Everyone who has worked in health IT knows how hard it is to get it right. Our domain is complex. One of the values FHIR offers is a ready-to-use informational model that is good enough. If you are not a health IT expert with many years of experience and you want to develop the next health IT app, I recommend starting with the FHIR data model, which has accumulated a lot of expert wisdom. At this point, it would be nice to have a well-fitted technical framework.

### **Isomorphism and transformations**

If you want to have your schema/model and support FHIR, you will face a transformation from your model to FHIR and backward. Such type of a transformation is trivial if your model is isomorphic to FHIR. However, if the models to translate are not match to each other — then you have a big problem. There is a well known [Object-relational impedance mismatch](https://en.wikipedia.org/wiki/Object%E2%80%93relational_impedance_mismatch). Or another example from Health IT: translation from HL7 v.2 to FHIR is quite easy — they are similar, but a translation from HL7 v.3 to FHIR is a nightmare — they are designed in a completely different way.

### **Documents vs. Tables**

FHIR resources are designed as [Aggregates from Domain Driven Design](https://martinfowler.com/bliki/DDD_Aggregate.html). The NoSQL movement stated that use of [Document databases](https://www.martinfowler.com/bliki/AggregateOrientedDatabase.html) is well-aligned with aggregates approach. If you try to create a normalized relational database for FHIR resources — you will get thousands of tables generated. Working with so many tables is not trivial. Most of the operations require a resource as an aggregate and getting/manipulating a resource from a dozen tables is not easy and has performance implications.

### **Open vs. Closed**

Another critical aspect to take into account — is if your system open or closed?

In an open system, you potentially allow flexible extension of the model — adding new attributes and constraints to existing entities. In FHIR we have extensions for FHIR resources. Real systems usually evolve into open systems. Building an open system with a strict schema is a significant challenge. The document approach enables flexibility and extensibility much easier.

### **Document/Relational databases**

Most mainstream relational databases (PostgreSQL, Oracle, MySQL and MS SQL) and modern databases like Spark, BigQuery and Spanner allow you to use both SQL, relations and documents. So you can practice hybrid approach — efficiently store your resources as is (without any transformation layers) and use the power of SQL as well as other features of databases (transactions, indexing, etc.).

### **Conclusion**

I think, storing FHIR resources “as is” using modern technologies like PostgreSQL/jsonb or BiqQuery/ProtoBuf is a very promising approach, which is worth to consider if you are starting new Health IT system or analytical platform.

### **Join in the conversation!**

To discuss these kinds of topics, be part of our [Analytics on FHIR](https://chat.fhir.org/login/#narrow/stream/73-analytics-on.20FHIR) and [Storage on FHIR](https://chat.fhir.org/login/#narrow/stream/134-storage-for.20FHIR) groups. Meet us offline at [Storage & Analytics connectathon track](https://wiki.hl7.org/index.php?title=FHIR_Connectathon_19#Storage_and_Analytics). Feel free to join our conversations and share your thoughts.

Follow US

[![Aidbox FHIR Platform Free Trial](image-1.png)](https://www.health-samurai.io/aidbox)
