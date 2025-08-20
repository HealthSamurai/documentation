---
description: Getting started with Fhirbase using Docker image.
---

# Getting Started (Docker version)

This tutorial explains how to run Fhirbase using [Docker Container Platform](https://www.docker.com/).

{% hint style="info" %}
If for some reason you want to install PostgreSQL and Fhirbase natively (without containerisation), consider switching to [another version of this tutorial](getting-started).
{% endhint %}

### Installing Docker

Please follow [Official Docker installation guide](https://docs.docker.com/install/). There are handy installers for all major operating systems.

### Running Fhirbase Web Demo

Fhirbase Docker image comes with PostgreSQL instance and already prepared database with [Synthea](https://github.com/synthetichealth/synthea) data loaded in it.

```
docker pull fhirbase/fhirbase:latest
```

{% hint style="info" %}
For some Docker installations (mostly Linux) you may have to prepend all `docker` commands with `sudo`prefix, i.e. `sudo docker pull`
{% endhint %}

When image is downloaded, type following command to start web demo:

```
docker run --rm -p 3000:3000 fhirbase/fhirbase:latest
```

When you'll see an message that web server was started on port 3000, point your browser to the [http://localhost:3000](http://localhost:3000), and you'll see the Fhirbase demo page.

![Fhirbase Web demo running locally](https://1716361951-files.gitbook.io/~/files/v0/b/gitbook-legacy-files/o/assets%2F-LNI6sKNYJOW0Q34Adqs%2F-LONTjzZ6FIR5gXiamaP%2F-LONrh_ycnNjAcHqWs1u%2FUntitled.png?alt=media\&token=c02a9325-8109-44b9-b536-aabf31718ad7)

To stop webserver, press `Ctrl+C`.

### Creating and Initializing Database

To be able to invoke Fhirbase commands, start container in demonised (`-d`) mode:

```
docker run --rm -p 3000:3000 -d fhirbase/fhirbase:latest
```

Docker will respond with ID of newly created container, a long hash string like `34b55aae4b2538b3a51b87a125bba93f667dc5ddf67aec95d0cb6da6b953993e`. Copy this ID to the clipboard. Now we're ready to get Bash shell inside running container. Do following command and paste ID from the clipboard instead of ID placeholder:

```
docker exec -it [container ID from previous step] /bin/bash
```

If you see an `postgres@xxxxxxxxx:/$` bash prompt, you can use `psql` command to create a new database. After invoking `psql` command your terminal should look like this:

```
postgres@xxxxxxxxx:/$ psql
psql (10.5 (Debian 10.5-1.pgdg90+1))
Type "help" for help.

postgres=#
```

Type `CREATE DABATASE fb;` statement into the prompt and press Enter. Do not forget to put semicolon at the end of the statement. `fb` here is the name of new database, you can change it to anything you want, but don't forget to change it in all command examples in this tutorial as well.

If PostgreSQL responded with `CREATE DATABASE`, your new database is created and now you can quit `psql` with typing `\q` and pressing Enter.

{% hint style="info" %}
Proceed to [PostgreSQL documentation](https://www.postgresql.org/docs/10/static/app-psql.html) to get more information about `psql`, an standard command-line PostgreSQL client.
{% endhint %}

Now we need to initialize newly created database with [FHIR schema](schema) to store data in. We will use `fhirbase init` command for that:

```
fhirbase -d fb --fhir=3.0.1 init
```

In this command `-d fb` specifies name of the database (don't forget to change it if you named database differently in previous step). `--fhir=3.0.1` sets a FHIR version you're going to use.

If Fhirbase did not return any error, your database was properly initialized with FHIR schema.

### Loading FHIR Data into the Database

To import data into newly created database, use `fhirbase load` command. Fhirbade Docker image comes with sample NDJSON file containing 127454 [Synthea](https://github.com/synthetichealth/synthea)-generated FHIR resources. This bundle is located at `/bundle.ndjson.gzip`. Invoke following command to load it:

```
fhirbase -d fb --fhir=3.0.1 load /bundle.ndjson.gzip
```

Usually it takes about 30 seconds to load this bundle.

{% hint style="info" %}
There are two different load modes: **insert** and **copy**. Using **copy** mode in some cases can drastically decrease load time. Also you can load data from Bulk Data API endpoints, FHIR bundles and plain JSON files. To get more information on this, take a look at [load command reference](../command-line-interface#fhirbase-load).
{% endhint %}

### Querying FHIR data with SQL

Now we're ready to query our FHIR database with SQL. Let's start `psql` providing database name:

```
psql -d fb
```

You should get `fb=#` prompt. Copy-and-paste examples below to see some trivial SQL magic.

#### Most popular given names

```
SELECT COUNT(*), resource#>>'{name,0,given,0}'
FROM patient
GROUP BY resource#>>'{name,0,given,0}'
ORDER BY COUNT(*) DESC;
```

#### Accessing FHIR elements

```
SELECT resource->'name', resource->'birthDate' 
FROM patient
LIMIT 10;
```

#### Searching Patient by name

```
SELECT id, resource->'name'
FROM patient
WHERE resource#>>'{name,0,given}' ilike '%alec%';
```
