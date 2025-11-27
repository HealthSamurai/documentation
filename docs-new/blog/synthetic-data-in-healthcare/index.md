---
title: "Synthetic data in healthcare"
slug: "synthetic-data-in-healthcare"
published: "2025-04-11"
author: "Alexandr Penskoy"
reading-time: "5 min"
tags: []
category: "Storage"
teaser: "How to start working with “Personal Health Information” without any responsibility?"
image: "cover.png"
---

## How to upload Synthetic healthcare data

How to start working with “Personal Health Information” without any responsibility?

My problem: *I want to start working with real data, but HIPAA prohibits me from doing so.*   
• I can’t simply deploy a FHIR Server in the cloud with real data because I need to be careful about its lifetime, access, and so on. I should take into account what I show to my colleagues and friends.  
• I don’t have a lot of data right now, but I want to know how a [FHIR Server](https://www.health-samurai.io/aidbox) and my application will react to 10 GB, 100 GB, or more.  
• I can write scripts to generate artificial data, but I also want to see realistic diagrams in my application.

The solution is simple: don’t work with real PHI; work with generated data. Generate data using tools like [Synthea](https://github.com/synthetichealth/synthea) as much data as I need and upload it into my FHIR Server, gradually increasing the amount.[](https://github.com/synthetichealth/synthea)

However, it can be tricky. Doing it from a laptop can consume a lot of time to generate and upload data. From a [FHIR server](https://www.health-samurai.io/aidbox), you need to start and set it up, build Synthea, generate data, expose it to the internet, and not forget to shut down the machine when all the work is done.

Alternatively, you can use the [Aidbox Bulk API](https://docs.aidbox.app/api-1/bulk-api-1/synthea-by-bulk-api) with an already generated and published dataset. To do this, you only need to copy and paste the HTTP request into the [REST Console for uploading](https://docs.aidbox.app/getting-started/load-bundle-into-fhir-server) sample data:

• 100 patients (around 300 Mb)  
• 1000 patients (around 3 Gb)  
• 10 000 patients (around 30 Gb)  
• 100 000 patients (around 300 Gb)
