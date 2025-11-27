---
title: "About FHIR facades (part I) - two approaches"
slug: "about-fhir-facades-part-i-two-approaches"
published: "2019-09-12"
author: "Pavel Smirnov"
reading-time: "4 min read"
tags: ["FHIR"]
category: "System Design"
teaser: "There are many reasons to let your software solution speak to others. Between interacting with other applications and extending your solution with new modules ..."
image: "cover.png"
---

#### Why do you need an API facade for your healthcare app at all?

  
There are many reasons to let your software solution speak to others. Between interacting with other applications and extending your solution with new modules your solution needs to share data and perform operations by external requests. One common way of doing this is to provide API (Application Program Interface). Moreover, if you want your app to speak fluently with everybody else without hiring expensive interpreters, you should stick to the standard API, which is FHIR API.  
  
Experienced developers of the new healthcare technology think about API from day one. Many of them are embracing the FHIR-first development process when a new solution's backend is built on top of the FHIR server. It uses the FHIR data model internally and supports complete FHIR API out of the box.  
  
However, what if you developed your solution time ago, and it doesn't have an API layer? You need to allow your app to communicate with others, and this is what you use an API facade for - an interface added to your solution that gives other applications access to your solution's operations and data.  

#### FHIR facade how to

Storage-less FHIR facade

![Storage-less API facade](image-1.png)

One approach to a FHIR facade implementation is to translate FHIR REST calls to queries to the underlying database or services of the original system. You need to map your internal information model to FHIR - find what FHIR resources and attributes represent data structures in your system. Such a facade passes all the calls to the original database.

The implementation is straightforward for read-only API and can become complicated if you want to allow your users to write data back to the original system. Simple use cases can be solved with the mapping tools, and more complex implementations will require a lot of custom development.

#### FHIR facade with an intermediate FHIR server

![API facade with FHIR server](image-2.png)

The other approach is to use a generic FHIR Server for storing data that you are going to serve over API. You perform all the same mapping of your internal information model to FHIR but then synchronize data in your database with the data in the FHIR server that does the rest of the work.

The FHIR server handles all the API commands without passing them to the underlying database of the original system. All the changes in the original database are timely passed to the FHIR server so that the original system and the FHIR server stay in sync.

#### Pros and cons

Both approaches are viable for different use cases and have different pros and cons. Storage-less FHIR facade works well for simple use cases when you need to implement a few individual API end-points. There is no duplication of data, and it uses minimal computer resources.

However, if you want your FHIR facade to host enterprise applications and enable population-level use cases then “caching” data in an intermediate FHIR server can be beneficial and sometimes even necessary. When a FHIR server handles all API calls, you control the performance of every call. You can achieve excellent performance at scale even when a FHIR server virtualizes data from several systems. The same API calls with a storage-less API facade might be very slow.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

It’s important to mention the complexity of the FHIR search. It will be much faster with the intermediate FHIR server because you can support search with indexes which is impossible in a storage-less approach.

Even if your FHIR server gets many queries from external applications, it doesn't load the underlying system. The data is requested only once from the original system; the FHIR server supports all the API calls. If you use a storage-less facade, all the queries are going to the underlying database of the original system, and that might affect the performance of the original system and its users.

Using a full-blown FHIR server gives you one more advantage comparing to the storage-less FHIR facade. It allows you to build the whole new ecosystem of modern FHIR-first apps on top of it so that one day you can move on from your legacy proprietary backend to a new FHIR based future. Even though it requires more computer resources, you do not waste these resources. They provide you the next level of flexibility.

#### Conclusions

Which way to go with your API implementation is your choice. Health Samurai can support both approaches with its solutions and services. [Send us a message](http://hello@health-samurai.io) or give us a call.

Follow US

[![Aidbox FHIR facade Free Trial](image-3.png)](https://www.health-samurai.io/aidbox)
