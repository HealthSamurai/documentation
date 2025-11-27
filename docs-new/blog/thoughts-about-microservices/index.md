---
title: "Thoughts About Microservices"
slug: "thoughts-about-microservices"
published: "2016-05-22"
author: "Niquola Ryzhikov"
reading-time: "4 min read"
tags: ["Microservices", "Software Development"]
category: "System Design"
teaser: "Many people are talking about microservies as the preferred approach to structure complex health information systems. But there are overlooked consequences to using microservices over monolithic architectures. "
image: "cover.jpg"
---

*Published on May 22, 2016 by Nicola Rizhikov*

Everybody is talking about microservices as a preferred approach to structure complex informational systems.

Here are some popular reasons:

- Our monolithic app became too big and complex, so we're going to split it into services
- We’ve reached the performance limit for our monolith, by splitting into services we hope to boost it
- Our system will be huge, so we are starting from microservices architecture to scale in the future
- Successful companies are using microservices, we just want to use industry best practices.

From my point of view these motivations do not have a direct connection to this architectural pattern and may bring you into real troubles or waste. Decision to use a microservices architectural style should be conscious and with understanding of all consequences and trade-offs.

Let’s list some serious penalties you will pay for microservices:

#### Consistency and dependency management

When you split your system into independently evolving services, these parts still have logical dependencies between each other and the system as a whole should be consistent. In a monolithic app these dependencies are hard-coded explicitly in your code, but with splitting it into pieces, dependencies became “implicit” and you need additional efforts (versions, contracts, tests etc) to make sure they are satisfied. Your system becomes more fragile and unpredictable. This in some ways similar to choice between [ACID](https://en.wikipedia.org/wiki/ACID) and [BASE](https://en.wikipedia.org/wiki/Eventual_consistency) options. In case of ACID in every point of time you have only one confident version of the world, in BASE world — many parallel galaxies could coexist and you have to manually handle consistency. In worst case you could end up with well known problem — [dependency hell](https://en.wikipedia.org/wiki/Dependency_hell). So without solid **“dependency management”** skills and tools you are on a dangerous road.

#### “Right” decomposition

The most challenging concern in micro-services approach (in programming at all :)— how to decompose the whole into pieces? As many matured architects know — initial decomposition almost always incorrect. Doing this “right” means you are able to predict the future. Instead of refer you to theoretical hints, let’s discuss consequences of **“wrong”** decomposition.

**Cross-cutting feature:**it’s easy to get new feature request, which require to make changes in many services and then consistently ship it. Sometimes this feature would take minutes in monolith and hours/days in microservices environment. Same about **cross-cutting bugs.**

**Re-composition** — if you’ve found better way to structure your system, then you have to re-compose your architecture. In monolith — it’s just well known “refactoring”, which could be done relatively safely. With microservices such transformation in many cases is not a trivial task.

#### **Operational Expenses**

It’s relatively known payment — every new component brings operational overhead in a non-linear manner. Distributed systems require qualitatively more complicated infrastructure, tools and experts to make it work reliably.

### Microservices Pros

Growing your company and system **right** is not easy. You have to manage two interrelated entities — organization and system. The most important argument for microservices, for me, is align both in agreement with [Conway’s law](https://en.wikipedia.org/wiki/Conway%27s_law). (Micro)Services make it possible to feed service to cross-functional team boundaries — to make teams independent as much as possible. The critical constraint, you should not miss, is that the team should be able to design, develop and deliver **“real value”** to the end user with minimal overlapping and conflicts with other teams. **“Scale your system and organization”** is worth goal to pay microservices price. The simple formula sounds like:

- one team → many services → wrong (overheads)
- many teams → one service → wrong (conflicts)
- one team→ one service → right

Under this conditions the size of the service should not be “too micro”, but defined only by the size and skills of your teams. As we know, team could scale in very limited interval — 2–7 people, but skills can vary in couple of orders.

Another valid argument for me is “**technological switch**”, situation when your current technology could not provider required technical characteristics, for example performance, and you have to [**“write in c”**](https://www.youtube.com/watch?v=1S1fISh-pag) some critical portion of your system.

#### Conclusion

This challenges and it’s derivatives will eat most of your time at the start, significantly slow you down in critical section. That’s why “**micro-services first”**approach for me sounds very suspicious. Most of successful “microservices” applications were done as re-work of monoliths in production into pieces. In that cases engineers had deep understanding system internals and problems.

In most of cases, I vote for [**“Monolitish Fist”**](http://martinfowler.com/bliki/MonolithFirst.html).

[![Aidbox FHIR Platform Free Trial](image-1.png)](https://www.health-samurai.io/aidbox)
