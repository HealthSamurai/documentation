---
title: "Accelerating Healthcare Innovation with HL7 FHIR"
slug: "open-innovation-at-the-era-of-fhir-and-stage-3-regulations"
published: "2017-04-01"
author: "Pavel Smirnov"
reading-time: "5 min read"
tags: []
category: "FHIR"
teaser: "Digital health innovation drives patient engagement, lowers costs and improves the quality of care. Having a team of FHIR experts who understand electronic health records systems and know how to manage healthcare data the right way will help open the doors to innovation at your organization. "
image: "cover.jpg"
---

Published on April 1, 2017 by Pavel Smirnov

#### **Why do you need to innovate?**

Digital health innovation in a healthcare organization is not easy by any means. Doing research and development when your major concerns are about endless regulations, security and patient safety is a huge challenge. Introducing digital health startups with totally different cultures into conservative hospital setting is difficult. But why do you need to innovate in the first place? Isn’t it enough to just implement an EHR from a large, respectable vendor?

When implementing an EHR from a large vendor, clients receive all the modules. This includes both the good modules as well as the bad ones. Сhoosing such a system is always a compromise since no single system can satisfy all user requirements. Opening the door to innovation, on the contrary, allows your healthcare organization to choose what it really needs and wants without compromising. With modern technologies, this can be achieve with spending significantly fewer resources. There are great development teams out there. They can deliver what your healthcare organization needs in an agile way. They can do more with less. They can solve the problems that bother your healthcare organization but do not bother other clients of your EHR vendor. And I am not talking about minor tasks - I know small cross-functional teams under 10 people which successfully developed modern cloud EHR products, physician and patient portals, care coordination systems, and HIEs.

But innovation is still tough, expensive, and considered risky by many. These capable development teams struggle with getting into hospital doors and are slowed down by the need to integrate their modern applications with a siloed ecosystem of legacy software. Is it possible to make the whole process of digital health innovation easier?

#### **Recipe for innovation**

I believe that nowadays there is a way to facilitate digital health innovation in a hospital without compromising on critical aspects of hospital information systems. Open your healthcare organization’s doors to innovative technologies but only those that are compliant with the current industry standards. Don’t waste your time on cumbersome custom integrations with fancy applications that are not interoperable out of the box.

First, set up a modern infrastructure for next generation healthcare solutions. Choose and deploy a platform with a FHIR-compliant clinical data repository that will future-proof your products. Then, set up production and sandbox areas with production and testing data respectively. Provide sandboxes with testing data to companies that view you as a potential client, and let them integrate their software with these sandboxes via a standard FHIR API. Have your production environment configured similar to your sandboxes so that you can easily move any technologies developed on the sandboxes that you like into production. Then, just promote your sandboxes to innovation communities and look out for great ideas.

Hospital that start building an ecosystem of connected healthcare applications will save money on integration of new technologies through standardization. It will increase the number of selection of technologies that can be brought in. And last but not the least, it will make necessary steps to accomplish Meaningful Use stage 3 open healthcare API requirements from the the CMS.

**Potential concerns**

**1. Integration of a FHIR platform with an existing legacy ecosystem is a challenge.** It might be a challenge for some legacy software or specific health data, but common medical data such as common meaningful use dataset is usually translated to the FHIR standard without significant efforts. A lot of health data can be received in real time from HL7 v.2 feeds which exist in every hospital. There are many tools for translating HL7 v.2 to HL7 FHIR. Some HL7 engines like iFW Iguana can do it. And even if your HL7 engine doesn’t the speak the FHIR standard yet, you can use HL7 v2 to FHIR translator from other companies. Our Aidbox FHIR backend can receive HL7 v.2 and translate it to FHIR.

**2. The HL7 FHIR standard is not mature and keeps changing. Migration to new versions is time consuming.**

![](image-1.png)

Any IT solution that is not dead is constantly changing. This is true for all other IT verticals and should be true for healthcare information technology as well. Look at all those changes as a positive sign. You are investing in a technology that will not be replaced tomorrow. And regarding the migration, the HL7 FHIR community recognizes this need. Grahame Grieve (the creator of FHIR) mentioned at the HIMSS17 conference in Orlando that he is thinking about offering scripts that will migrate health data to newer versions of the FHIR standard. At Health Samurai, we’re doing this for our clients as well.

**3. How can I make a new infrastructure reliable, scalable and secure? I’m not comfortable with the cloud.**

First of all, standards of interoperability don’t affect your choice of infrastructure. If you want to keep using your old in-house health data center, just deploy an HL7 FHIR platform there. But the cloud can bring a whole new level of reliability and scalability to your healthcare organization. For example, our Aidbox FHIR backend is usually deployed to several servers located in different geographical zones, or different cloud infrastructure providers (IaaS). Health data is replicated between these servers so that even the complete shutdown of a single AWS region will not bring Aidbox down. There are several other infrastructure properties that a cloud Aidbox installation has by default as well: automated backups, encryption of all health data at rest and in transit, detailed audit logs with GUI, and even point in time recovery service that rolls back any changes and repairs health data after harmful human errors.

**Don’t wait for your EHR vendor!**

One other consideration I want to share with you - do not wait for EHR vendors and their HL7 FHIR marketplaces. Most EHR companies have other priorities than disrupting their own business models via building an ecosystem of connected healthcare applications from third party companies. They’ve been working on the HL7 FHIR API for years but still only provide API access to a limited subset of health data that is in read-only mode. You can innovate much faster on your own with commercial HL7 FHIR products on in the marketplace, and it will not take that long.

[![Pluggable Aidbox FHIR API for EHRs](image-2.png)](https://www.health-samurai.io/fhir-api)
