---
title: "Getting started with HL7 messaging over MLLP using a VPN : HL7 MLLP"
slug: "getting-started-with-hl7-messaging-over-mllp-using-a-vpn"
published: "2022-10-27"
author: "Artem Alexeev, Viktor Gusakov"
reading-time: "7 min read"
tags: ["mllp", "vpn", "kubernetes"]
category: "System Design"
teaser: "Sharing our experience: How to securely transport an HL7 message using the MLLP protocol and effectively resolve connectivity issues.\n"
image: "cover.png"
---

## Using HL7 MLLP

In the 1990s, we witnessed the jump from bare-metal servers to the virtual machine world. Physical servers still exist, so we’re usually still required to support messages coming in and out of our system. But how can we make the process a little less stressful?

In this article we’re going to give you a short tour explaining how to connect plain old bare-metal EHR local networks to modern cloud-based healthcare applications and establish HL7 v2/v3 message exchanges between them using MLLP.

Keep reading to find out:

- What is HL7 MLLP?
- How does a VPN connection work in general?
- What are the main units needed to establish S2S VPN connections in a cloud environment?

### **The basics: What is MLLP?**

MLLP (Minimal Low Layer Protocol) is a protocol for an HL7 message exchange. It is composed of two basic elements:

- transmission of HL7 messages via TCP/IP
- framing the start and end of an HL7 message

Security is formally outside the scope of MLLP, but as long as HIPAA is not amended, implementers should take into account the security issues. This makes the transporting of HL7 messages via MLLP a lot more complex.

### **How do we establish secure connections using MLLP?**

To enable secure communication, we need to establish an encrypted TCP/IP connection between the sender and receiver. This is done via an S2S VPN (site-to-site virtual private network) connection.

Some other ways of sending HL7 messages over the internet (HTTPS) include:

- Hybrid Lower Layer Protocol (HLLP), a variation of MLLP that additionally requires a transmission of a checksum
- other protocols that transmit data over TCP/IP: SOAP, SMTP, S/FTP (not compliant with HIPAA/GDPR by default)

### **Historical note about serious technical challenges**

MLLP was introduced in the 1990s. It was still an era of bare-metal servers and local networks with real machines and real network hardware. The era of the internet hadn’t even arrived yet.

The issue is that nowadays we tend to use virtual machines. Our applications are deployed on Azure/AWS/Google Cloud Platform with the use of container-based technologies like Kubernetes (K8S) and Docker, so we literally have to establish an S2S VPN connection from our K8S network.

### **W-w-w-what? S2S VPN from a K8S cluster? Is it possible?**

In brief, yes it is. However, we should note that almost all entities used to establish a connection will be purely virtual (logical). Here at Health Samurai we’ve managed to establish such connections using Microsoft Azure/Amazon AWS/Google Cloud Platform.

### **What is a common topology of S2S VPN?**

![mllp hl7](image-1.png)

We have two local networks connected via an encrypted VPN tunnel connection. It looks quite simple, right? But the usage of virtual machines adds some complexity to it.

VPN stands for virtual private network. In the case of S2S (site-to-site), it merges two networks into one logical network. The connection between these two networks is made by establishing a tunnel (in effect just a pack of encrypted data). We will send our HL7 message to the EHR via this tunnel.

### **What does the usual cloud S2S VPN connection look like? Spoiler: It’s a little bit more complicated**

As we mentioned before, in the case of cloud networks we have extra complexity levels. We have a virtual network with a virtual machine inside, and it somehow reaches the outer internet using a public IP address. This is what usually happens:

![mllp protocol](image-2.png)

Don't worry, we’ll explain everything in detail:

#### **Application**

Our application is just a K8S cluster pod with a private (virtual) IP address. This IP address exists inside the virtual subnet.

#### **Virtual subnet**

The virtual subnet is a part of the whole virtual network inside our Kubernetes cluster. So if a cluster has a lot of IP addresses, the virtual network will probably use the required quantity of addresses from this network.

#### **Virtual network**

The virtual network is a purely logical entity. It is a network with a certain range of private IP addresses. All entities inside such virtual networks are connected using software, rather than real wired connections.

#### **How does a pod located in a private network perform requests to the outer internet (and other networks)?**

As you can see above, it is done through:

- a virtual private gateway (other networks)
- an internet gateway (internet)

#### **How do I decide whether our connection should go to the internet or the local network?**

This is done via route tables.

#### **Route table**

Route tables are just a set of rules governing where the traffic should go. In our case route tables are configured as follows:

- all connections made to the EHR network go to the EHR network
- all other connections go to the internet

#### **How can outer networks connect to the pod?**

If outer networks are connected to the pod network, the pod can be accessed via its private IP address. A VPN connection actually makes it easier to connect two networks into one.

#### **How can a VPN connection be established if the pod is the initiator?**

- The pod sends a request to the EHR network IP address.
- The request goes to the route table and says that the next hop of the connection is a VPG (virtual private gateway).
- The VPG invokes the VPN tunnel with the other machine.

> Want to streamline your setup? Speak with our specialists for tailored solutions. [Get in Touch](https://hubs.ly/Q02y4V7J0)

#### **How exactly does the VPN tunnel work?**

Here, we will cover how the IPSEC connection is established. There are a number of VPN protocols:

- Layer 2 Tunneling Protocol (L2TP)
- Point–to–Point Tunneling Protocol (PPTP)
- SSL and TLS
- Secure Shell (SSH)
- OpenVPN
- WireGuard

In our experience, IPSEC is the most common way to transmit HL7 traffic using MLLP.

IPSEC actually comprises a group protocol used to establish the connection:

- Internet Key Exchange (IKE)
- Authentication Header (AH)
- Encapsulating Security Payload (ESP)

Each of the abovementioned protocols may also be treated as a step to establish the connection:

- we ensure that connection is established between the proper parties
- we reach an agreement on the encryption type
- we ensure that the data was not intercepted or modified

#### **How do we reach an agreement on the encryption type?**

The two-phased IKE protocol helps to set up a secure and authenticated communication channel. The most common ways to come to an agreement on encryption algorithms include:

- exchange of pre-shared key (PSK)
- signature based authentication
- public key encryption

This is called IKE phase 1. It is the stage where both parties are sure that the connection was initiated by the proper parties and are able to discuss exactly how the data will be encrypted.

Usually, three steps are needed to pass IKE phase 1:

- exchange of applicable encryption rules (SHA, Diffie–Hellman etc.)
- exchange of the open part of Diffie–Hellman (DH) data and auxiliary data
- confirmation on results of DH exchange

What is IKE phase 2? IKE phase 2 is the main phase of the connection when the user data transmission happens.

We will not cover AH and ESP in detail here because they are operated using the same principles as the IKE protocol and serve the same purpose. There are still several points to remember:

- Communication is just an interchange of data packs (IP packets).
- Every security protocol adds some extra data (headers) to the original IP packet.
- This is done by wrapping one piece of data into another by means of encryption.
- De-encryption of data happens on the receiver’s side.

### **Conclusion**

To sum up, we can say that:

- MLLP is an old but viable technology for transmitting HL7 messages across the internet and local networks.
- You have to take security into account when transmitting messages over MLLP and use technologies to obtain this goal (like VPN or SFTP).
- VPN connections can be easily established between the on-premises network and cloud infrastructure (AWS/GCP/Azure) using virtual entities.
- From an application standpoint data transmission over VPN does not differ from any other data transmission using TCP.

If you are trying to establish a VPN connection for your HL7 feed in a cloud environment and get stuck, [talk to us](http://hello@health-samurai.io) and we’ll be happy to show you how it works.

To explore setting up HL7 messaging over MLLP with a VPN in your environment, consider using the [free version of Aidbox](https://www.health-samurai.io/aidbox#run). It provides a secure and fully functional environment to test these configurations, offering all necessary tools without any limitations.

Follow US

Authors: Artem Alexeev, Viktor Gusakov

[![Pluggable Aidbox FHIR API for EHRs](image-3.png)](https://www.health-samurai.io/fhir-api)

*Looking to take your EHR data transmission and storage to the next level? We recommend taking advantage of our pluggable* [*Aidbox FHIR API module*](https://www.health-samurai.io/fhir-api)*. It’s ONC-certified and provides an API to ingest and process HL7® v2 messages seamlessly. Aidbox is well positioned to serve as the next frontier for HIPAA compliance, and your competitors probably haven’t made it through – yet.*
