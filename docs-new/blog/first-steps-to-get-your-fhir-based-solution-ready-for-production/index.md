---
title: "First steps to get your FHIR-based solution ready for production"
slug: "first-steps-to-get-your-fhir-based-solution-ready-for-production"
published: "2022-09-22"
author: "Mike Ryzhikov"
reading-time: "6 min read"
tags: ["fhir", "fhir in production", "high-availability", "FHIR in cloud", "reliable FHIR server", "high-availability FHIR backend", "HIPAA", "infrastructure"]
category: "Infrastructure"
teaser: "Basic tips for building high-availability FHIR systems in a cloud infrastructure. Here, you will learn how to develop a production-ready and reliable FHIR solution in the cloud."
image: "cover.png"
---

A well-designed production infrastructure allows your development team to sleep at night and relax on weekends, while users benefit from your app 24/7.

So, before you go into production with your FHIR-based application, you definitely need to take care of two critical risks and minimize them:

- data loss
- system downtime

These tips will help you develop the production-ready and reliable FHIR solution in the cloud. To keep it simple, we will intentionally skip the topics of security, networking, and monitoring. Instead, we will focus on the basics to move forward.

## **No single point of failure!**

Infrastructure design starts with choosing the right principles. The major principle of building high-availability systems is that there is no single point of failure. In simple words, you will need to duplicate the key components of the system, i.e. make them redundant.

The basic FHIR solution includes two key components that should be duplicated:

- Application(s) – Aidbox FHIR backend
- Database(s) – PostgreSQL

If you have more key components, you can apply this principle to all of them in the same way.

![Typical FHIR solution](image-1.png)

Applications and databases require different approaches and technologies when it comes to duplication. Let’s explore how to deal with them in turn.

## **No system downtime. Duplicate your FHIR server and run in parallel**

To avoid a single point of failure on the application layer, you need to have two or more instances of the FHIR server/backend that will serve requests in parallel. You also can’t operate your solution without a mechanism to monitor, relaunch and allocate traffic for your apps if they fail.

The particular implementation depends on your selected cloud and services. Nowadays, public clouds provide many options, so choose wisely based on your budget and desired level of oversight.

We highly recommend looking at the managed Kubernetes (K8s) service: [EKS in AWS](https://aws.amazon.com/eks/), [GKE in GCP](https://cloud.google.com/kubernetes-engine/), and [AKS in Azure](https://azure.microsoft.com/en-us/services/kubernetes-service/). From our point of view, it’s a win-win and best value for money.

When you run your containerized apps within the Kubernetes cluster, K8s will:

- restart containers that fail
- replace containers
- kill containers that don't respond to your user-defined health check
- advertise them to clients only when they are ready to serve

This process is called [self-healing](https://kubernetes.io/docs/concepts/overview/#why-you-need-kubernetes-and-what-can-it-do).

![High-availability and reliable FHIR server](image-2.png)

To configure this Kubernetes-based solution, you need to start the Kubernetes cluster in a cloud, setting up [health checks](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/) and [self-healing](https://kubernetes.io/docs/concepts/overview/#why-you-need-kubernetes-and-what-can-it-do)/[liveness](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/#define-a-liveness-command) policies. The basic self-healing policy includes the following parameters that you can adjust for your needs:

- check liveness every 5 seconds
- allocate traffic if a container (app) does not respond for more than 10 seconds
- restart if a container (app) does not respond for more than 20 seconds

Find the example of [Kubernetes liveness probe here](https://docs.aidbox.app/getting-started/run-aidbox-in-kubernetes/high-available-aidbox).

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

It is also important to note that Kubernetes is designed so that a single Kubernetes cluster can [run across multiple failure zones](https://kubernetes.io/docs/setup/best-practices/multiple-zones/). It protects your cloud ecosystem against infrastructure and availability zone failures. So, please, take it into account when you configure the cluster.

As a final step, you will need to make sure that your selected FHIR server can serve requests in parallel. For example, the Aidbox FHIR platform has a built-in internal Aidbox cache and a mechanism for cache synchronization between instances. Aidbox instances can share state/common configs between each other, making them interchangeable.

**Short summary**: As a result, you have a basic high-available application layer with automated app failover based on the K8s self-healing mechanism. This approach helps you to minimize system downtime and will not require manual actions as part of the recovery process.

[![Aidbox FHIR platform on AWS marketplace in one click](image-3.png)](https://go.aws/3BEavRy)

## **No data loss. Replicate and backup your database the right way**

To avoid a single point of failure on the database layer, we need to have a replica, backups and a failover/recovery process (ideally automated). In our case, we use PostgreSQL and the examples below will be based on it.

PostgreSQL allows you to set up two or more replicas by using [the built-in streaming replication feature](https://www.postgresql.org/docs/current/runtime-config-replication.html). We suggest sticking with the following parameters:

- at least one synchronous replica, and
- a replica with a 12–24-hour delay

This will save your data if you erroneously delete a table from the database, so you can recover it easily.

![High-available FHIR infrastructure](image-4.png)

PostgreSQL backups can be categorized into two types:

- [incremental](https://www.postgresql.org/docs/9.0/continuous-archiving.html) – maintains a write-ahead log (WAL) that records every change made to a database
- [base (full)](https://www.postgresql.org/docs/9.0/continuous-archiving.html) - full database backup

To configure a backup pipeline, you will need additional extensions and an app to trigger and execute the process. We recommend looking at [the WAL-G extension](https://wal-g.readthedocs.io/) for PostgreSQL. WAL-G is an archival restoration tool for PostgreSQL, MySQL/MariaDB, and MS SQL Server (beta for MongoDB and Redis). The best approach to storing backups in the cloud is [Amazon’s AWS S3 file-storage service](https://aws.amazon.com/s3/) or [Google Cloud Storage](https://cloud.google.com/storage).

Going further, the best practice is having your own documented **backup policy** based on your solution requirements and usage. This policy will form the basis of your backup configuration. Below is an example of the key variables for this policy that can serve as a reference:

- incremental backups ~daily
- base (full) backups ~weekly
- backup retention ~monthly

To test and refine your FHIR-based solution before going live, try the [free version of Aidbox](https://www.health-samurai.io/aidbox#run). It offers a comprehensive environment to develop and validate your solution, providing all necessary tools without any feature limitations.

## **Conclusion and checklist**

Well-designed production-ready infrastructure should be reliable and self-healing. This means that if any of the key components fail, the system will re-balance and keep on running smoothly.

In practice, you need to duplicate your components and embed failover mechanisms on the application and database levels separately.

Kubernetes cluster with native health check monitoring and self-healing logic will juggle your FHIR-based containers, while PostgreSQL will be replicated and backed up with the WAL-G extension and built-in replication features.

The next biggest challenge will be to create a smart failover and recovery mechanism for databases. We’ll be sure to explore this topic in future articles.

### **Basic Checklist**

- Set up Kubernetes cluster with self-healing and run in multiple zones
- Use multiple instances of FHIR servers that work in parallel
- Set up synchronous DB replica
- Set up additional DB replica with 12–24-hour delay
- Configure daily incremental backup
- Configure weekly base (full) backup
- Implement monthly backup retention policy
- Develop and use DB failover and recovery strategy

This checklist is enough to protect your solution from data loss and system downtime. For a turn-key production-ready infrastructure, you will also need to take care of security, networking, and monitoring or delegate it to all-in-one SaaS solutions like [the Aidbox FHIR platform in AWS](https://aws.amazon.com/marketplace/pp/prodview-l5djlpvsd6o5g).

Follow US

Author: [**Mike Ryzhikov**](https://www.linkedin.com/in/fhir-solutions/)   
COO at Health Samurai
