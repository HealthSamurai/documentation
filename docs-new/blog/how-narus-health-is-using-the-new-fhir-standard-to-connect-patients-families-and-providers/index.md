---
title: "FHIR Success Story: Narus Health Connects Patients, Families and Providers"
slug: "how-narus-health-is-using-the-new-fhir-standard-to-connect-patients-families-and-providers"
published: "2017-11-20"
author: "Anthony J. Chan"
reading-time: "5 min read"
tags: []
category: "FHIR"
teaser: "Digital health company Narus Health build two FHIR applications - a cloud electronic health record and a mobile application - to support employee health. Storing their healthcare data in a backend FHIR server and building with FHIR APIs significantly cut costs and time to market. "
image: "cover.jpg"
---

*Published on December 13, 2017 by Anthony J. Chan*

We spoke with [Rob Stokes](https://www.linkedin.com/in/robertleestokes/), Director of Application Development at [Narus Health](https://www.narushealth.com/), about how his company is using FHIR to connect patients, providers and family members.

It’s no surprise that navigating the American healthcare system is a challenge.

Trying to understand your benefits through the barrage of EOBs with unintelligible language is no easy task. Add on a critical medical diagnosis, and this is enough to throw any stable individual and their family into crisis mode.

This is what Narus Health’s CEO, [Michael Burcham](https://www.linkedin.com/in/michaelburcham/), experienced when one of his family members was diagnosed with cancer. The lack of support with decision making and gaps in communication inspired him to start a company to help patients and families plan, make decisions and set goals when faced with life-threatening illnesses. His company focuses on making sure individuals have the knowledge to understand all their treatment options, costs and what is covered under their benefit plan.

**Compassion and mPower**

[Narus Health](https://www.narushealth.com/) is a technology-enabled care management company that educates, connects and supports patients diagnosed with complex medical illnesses. They do this with two mobile and web products built on the [Aidbox](https://www.health-samurai.io/aidbox) platform: [Compassion and mPower](https://www.narushealth.com/solutions).

**Compassion** is a care management and patient support platform that helps to deliver Narus Health’s services. Compassion assesses needs and then generates a personalized care plan for each patient quickly and easily. Once key issues are identified, it connects the patient, family, caregiver, and doctors using their other app, mPower. This keeps everyone in the patient’s support network in the conversation and minimizes communication silos.

**mPower** is a patient-facing mobile application that allows patients to share and export their entire medical record and have quick access to important documents and educational content. It also allows patients to report symptoms, message Narus Health staff, track and record medications, and opens up communication channels to their family members. mPower helps patients learn how to ask the right questions to their doctors to better understand their unique situations, and brings clarity to their benefits plan during critical situations where patients can have a hard time processing all the financial and clinical information.

Narus Health begins working with patients as soon as they are diagnosed, helping them develop a plan that is most tailored to their needs. By assessing treatment options, costs, available resources, potential symptoms and outcomes, they are to deliver transparency, knowledge and support for patients and their families in dire times.

**Choosing FHIR**

Early on, Narus Health chose to build their products from the ground up with the new [FHIR standard](https://www.hl7.org/fhir/overview.html) because there was nothing available on the market to deliver the level of care they wanted. They understood interoperability would be vital to providing the patient and family experience they wanted, and searched for a forward-thinking technology with modern standards that they could build with. After looking to the healthcare community and industry, FHIR became the obvious choice.

In regards to the benefits of using FHIR, Rob says:

*“FHIR has forced us to think about the way everyone in the industry looks at a problem instead of just using a data model that is convenient for our business case and tools. We have found this has led us to deliver higher quality solutions that are familiar to physicians as we begin to scale our business.  FHIR also provides us with an avenue to quickly and simply share data with patient’s providers who are using EMRs that have adopted the FHIR standard.”*

Although the learning curve was steep, using FHIR gave Narus Health a unique, unbiased perspective about how things should be built and structured. As a result, they built their own API around FHIR and pushed themselves to deliver a high quality, interoperable product. Integrating the FHIR standard ensured their company will be ready for the future of healthcare: a connected ecosystem of mobile and web applications that gives patients and family members easy access to medical data and empower providers to optimize care coordination and collaboration through shared data.

**Product Development**

When Narus Health was looking to partner with a FHIR expert to build their product, they found [Health Samurai](https://www.health-samurai.io) after researching [FHIRbase](https://www.health-samurai.io/fhirbase), and [Aidbox](https://www.health-samurai.io/aidbox).

Rob explains the decision to work with Health Samurai:

“*After comparing Aidbox to the other competing products in the space, it was clear that they had the most mature product and complete implementation of the FHIR standard. The primary benefit we received from using Aidbox has been speed to market. When you work for a startup, this is extremely important. Aidbox provides us with a data layer, messaging API, terminology server, queuing system, notification system, audit logging, data validation, and document storage system allowing us to focus on developing our business logic. Additionally, their team has been able to help us take our business problems and solve them using FHIR.”*

To manage development of the products, Rob used lean development practices to help his team stay focused on the items that add the most valuable to the business while also letting them shift focus quickly. They maintained short one week sprints to achieve 3-6 week project plans. This cadence ensured that they did not waste any time breaking down and estimating anything that wasn’t needed.

**Product Launch**

Narus Health launched their direct-to-consumer model in March 2017 and went live with employer customers at the beginning of July 2017.

Initially, they targeted large private insurers as customers but found that sales cycles moved slowly, and insurers seemed more focused on internal issues within the health plan than innovating on the care experience for patients and families. So they pivoted to focus on self-funded employers, TPAs and brokers who are more flexible with purchasing decisions and want a great employee and employer experience.

Rob explains:

*“We saw a significant opportunity to make an impact with self-insured employers and their workforce. They are very dissatisfied with the status quo, and are seeking real, forward-thinking solutions. Given the rising healthcare costs, they are prepared to make decisions and execute new concepts.”*

Shifting target markets changed the use cases of their software products and meant implementing modifications. This market shift also drastically changed the way Narus billed. With employers, they charged per account plus an hourly rate and thus needed **Compassion** to be modified to track and report this information accurately. Additionally, instead of having a few large insurer customers, they now had many employer clients, leading to the need for automating the onboarding process for customers fully and for the backend design to accommodate different customer use cases.

**Customer Feedback**

Employers have shown great appreciation to Narus Health for supporting a healthier workforce, which translates to better work performance, enhanced productivity and a focus on the total cost of care. Patients praise Narus Health for making their lives easier with scheduling and care coordination services, and having access to the care team, community resources and even financial support for medications helps them feel supported along their treatment journeys.

The true beauty of Narus Health’s solution is that it simplifies the complexities of the healthcare system and translates it into concrete knowledge patients can understand and use for themselves.

**Looking Ahead**

Narus Health has started to expand their mobile offering to integrate a holistic view of patient, to help users understand every single benefit opportunity and eventually move beyond terminally-ill patient populations to those suffering from chronic illnesses and enabling a preventative approach to health.

Narus Health has begun forming strategic partnerships with data firms and announced a strategic partnership with Lucent Health, a significant TPA, to streamline business development and onboarding processes for new customers.

**Great Risk, Great Reward**

Working in healthcare is often lauded as one of the most rewarding professions in our society, and working at an early stage startup is considered one of the riskiest career decisions. But for Rob, what gets him excited to wake up in the morning is being able to work for a startup at the intersection of healthcare and technology with a vibrant culture and amazing team:

*“My favorite part about working at Narus is the start-up culture. I get the opportunity to wear many hats every day, and this keeps things interesting and ensures that I am always being challenged and continue to learn. We’re lucky to have a team of brilliant folks.”*

We are beginning to see innovation that connects patients to their medical data and empowers them as consumers. A world of open APIs and modern web technologies and standards in healthcare will benefit all stakeholders, and Narus Health shows firsthand the potential FHIR has in disrupting healthcare for good.

For more information, watch Rob’s [presentation](https://www.youtube.com/watch?v=y4uJwWC_vro) at the latest FHIR Applications Roundtable.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.
