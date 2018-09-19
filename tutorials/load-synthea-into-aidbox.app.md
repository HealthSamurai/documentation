---
description: How to get synthea data in cloud aidbox
---

# Fill Box with data

### Create your box

Read how to register and create box in this [tutorial](create-and-configure-box.md).

### Setup Access Policy

We need to configure access policies to enable access to REST API from outside of the world.

Let's configure simple policy to access API by secret key:



### Generate data

To generate data we will use open source synthetic dataset generator - [Synthea](https://github.com/synthetichealth/synthea).

Here how we can install synthea.

```text
git clone https://github.com/synthetichealth/synthea.git
cd synthea
./gradlew build check test
```

Edit `src\main\resources\synthea.properties` to get output in transaction bundle format:

```text
exporter.fhir.transaction_bundle = true
```

Generating the population one at a time...

```text
./run_synthea -s 1000
```

Read more about synthea generator - [https://github.com/synthetichealth/synthea](https://github.com/synthetichealth/synthea)

### Load data into aidbox

 Using transaction Operation

### Access data by REST API



