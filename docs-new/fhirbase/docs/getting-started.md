---
description: >-
  Step-by-step tutorial to setup PostgreSQL, Fhirbase and perform first data
  import.
---

# Getting Started

{% hint style="info" %}
If you have Docker installed, you might want to switch to [Docker version of this tutorial](getting-started-docker-version).
{% endhint %}

{% embed url="https://www.youtube.com/watch?v=8rQnF2yGH8Y" %}
Video Version of This Tutorial
{% endembed %}

### Installing PostgreSQL <a href="#installing-postgresql" id="installing-postgresql"></a>

To start using Fhirbase you have to install [PostgreSQL database](https://www.postgresql.org/) first. Any version above **9.6** should be fine. Please proceed to the section describing operating system you're running.

#### Docker (cross-platform) <a href="#docker-cross-platform" id="docker-cross-platform"></a>

If you have [Docker](https://www.docker.com/) installed, you might want to start PostgreSQL as a Docker container:

```
$ docker run --name fhirbase-postgres -p=5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres:latest
```

#### Windows <a href="#windows" id="windows"></a>

For Windows the most simple way is to use [EnterpriseDB PostgreSQL Installer](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads). Also there is a [YouTube video](https://www.youtube.com/watch?v=e1MwsT5FJRQ) demonstrating the whole installation process.

#### Linux <a href="#linux" id="linux"></a>

Most likely that PostgreSQL will be available through your distribution's package manager. On Ubuntu or Debian, it's as easy as

```
$ sudo apt-get install postgresql postgresql-client
```

For other Linux distributions, please use search functionality of your package manager or just Google the right command.

#### MacOS (Homebrew) <a href="#macos-homebrew" id="macos-homebrew"></a>

You can install PostgreSQL with [Homebrew](https://brew.sh/):

```
$ brew install postgresql
```

Also you'll need to create default user with some password (we advice to use "postgres" as password for simplicity):

```
$ createuser -s -P postgres
Enter password for new role: postgres
Enter it again: postgres
```

#### MacOS (Postgres.app) <a href="#macos-postgres-app" id="macos-postgres-app"></a>

As an alternative, there is a [Postgres.app](https://postgresapp.com/) project which provides PostgreSQL as a regular MacOS application with common drag-and-drop installation. Please follow instructions at [Postgres.app Installation Page](https://postgresapp.com/documentation/install.html) and especially make sure you've updated you PATH variable to be able to use `psql` command-line tool.

When you finished installing it, open Terminal and execute following command to set password for `postgres` user:

```
$ psql -h localhost -U postgres -c '\password postgres'
Enter new password: postgres
Enter it again: postgres
```

### Checking Postgres connection <a href="#checking-postgres-connection" id="checking-postgres-connection"></a>

After you finished installing Postgres, please check it's running and accepting TCP/IP connections.

You can do this with `psql` command-line client or with [pgAdmin](https://www.pgadmin.org/) GUI application. For `psql`, use following command:

```
$ psql -h localhost -p 5432 -U postgres -W postgres
Password for user postgres: xxxxxxxxxx
psql (10.4, server 9.6.3)
Type "help" for help.
​
postgres=# _
```

If you got `postgres=#` prompt waiting for your input, your installation is fine. You can quit `psql` typing `\q` followed by Enter.

If you got an error like this instead:

```
psql: could not connect to server: Connection refused
        Is the server running on host "localhost" (127.0.0.1) and accepting
        TCP/IP connections on port 5432?
```

then it looks like PostgreSQL is either not running or not accepting TCP/IP connections. Please check presence of `postgres` process using your operating system's Process Viewer (or Task Manager). If `postgres` process is running, please consider updating `pg_hba.conf` file as described in this [wiki article](https://wiki.postgresql.org/wiki/Client_Authentication).

TODO: better troubleshooting guide.

### Downloading and installing Fhirbase <a href="#downloading-and-installing-fhirbase" id="downloading-and-installing-fhirbase"></a>

Go to [Fhirbase Download page](https://health-samurai.io/fhirbase-downloads) and download most recent release of Fhirbase. Make sure you've picked right file, it should match your operating system and CPU architecture. Nightly-build release is being updated after every commit to master branch, so it might be unstable.

When you downloaded the Fhirbase binary, make it executable (Windows users don't need this):

```
$ chmod a+x /path/to/downloads/folder/fhirbase-os-arch
```

Then rename it from `fhirbase-os-arch` to just `fhirbase` and put in some directory listed in you `$PATH` environment variable. To check that it was properly installed, type following command:

```
$ fhirbase help
```

It should output Fhirbase command-line help.

{% hint style="warning" %}
**Note to Windows users:** Fhirbase is a terminal application and it's supposed to be run inside Command Prompt window. If you'll just double-click it, Command Prompt window will popup and disappear immediately. To be able to use Fhirbase, do following:

* Start Menu => Run... => cmd.exe
* type `cd C:\Path\To\Fhirbase\Exe` and press Enter (use real path instead of example one)
* type `fhirbase` and press Enter (make sure you've renamed downloaded exe file to just `fhirbase`)
{% endhint %}

If you don't know what the `$PATH` variable is, it's ok, you can skip this step. But you'll have to type full path to the `fhirbase` executable every time you'll invoke Fhirbase command. So instead of typing

```
$ fhirbase help
```

you'll need to type something like this:

```
$ /Users/xxxx/Downloads/fhirbase help
```

So please remember that if your shell says it cannot find `fhirbase` command, most likely `fhirbase` binary wasn't correctly placed inside `$PATH`.

### Creating and initializing database <a href="#creating-and-initializing-database" id="creating-and-initializing-database"></a>

Next step is to create database where we're going to store FHIR data. Again, we can use `psql` command-line client or some GUI client like pgAdmin. Connect to Postgres as you previously did:

```
$ psql -h localhost -p 5432 -U postgres -W postgres
```

To create database there is a `CREATE DATABASE` statement:

```
postgres=# CREATE DATABASE fhirbase;
```

Do not forget to put semicolon at the end of statement. Of course, you can change "fhirbase" to any database name you want.

If you got `CREATE DATABASE` text as a response to your command, your database was succesfully created. Type `\q` to quit `psql`.

Now we have database, but before loading any FHIR data into it we need to create schema - some tables and stored procedures. This operation is performed with `fhirbase init` command:

```
fhirbase --host localhost -p 5432 -d fhirbase -U postgres -W postgres --fhir=3.3.0 init
```

Let's briefly describe meaning of command-line arguments:

* `--host localhost` specifies host where Postgres is running
* `-p 5432` specifies Postgres port
* `-d fhirbase` specifies name of the database to connect to (don't forget to change it if you named database differently)
* `-U postgres` name of Postgres user, "postgres" is fine for most cases
* `-W postgres` user's password
* `--fhir=3.3.0` FHIR version you're going to use

If you got no error, database was initialized. Most common errors are connection errors (check your Postgres credentials) and SQL errors (you're trying to initialize non-empty database).

### Importing FHIR data <a href="#importing-fhir-data" id="importing-fhir-data"></a>

We're ready to import data into our newly created database. We can import data from two different sources:

* local files (ndjson files)
* ​[Bulk Data API](https://github.com/smart-on-fhir/fhir-bulk-data-docs) endpoints

Let's try to use local file first. Fhirbase GitHub repository contains sample file, go ahead and [download it](https://github.com/fhirbase/fhirbase/blob/master/demo/bundle.ndjson.gzip). When this file is on your local filesystem, invoke `fhirbase load` command:

```
fhirbase --host localhost -p 5432 -d fhirbase -U postgres -W postgres --fhir=3.3.0 load -m insert /path/to/downloaded/file/bundle.ndjson.gzip
```

Usually it takes from 25 to 45 seconds to import this file. At the end of the input Fhirbase will output statistics about inserted data:

```
Done, inserted 127454 resources in 27 seconds:
       ImagingStudy 135
 AllergyIntolerance 272
              Claim 17937
               Goal 1100
          Encounter 15384
          Procedure 14109
       Immunization 6548
           CarePlan 1544
              Basic 600
       Organization 768
   DiagnosticReport 4143
  MedicationRequest 2553
            Patient 600
          Condition 4142
        Observation 57619
```

{% hint style="info" %}
There are two different load modes: **insert** and **copy**. Using **copy** mode in some cases can drastically decrease load time. Also you can load data from Bulk Data API endpoints, FHIR bundles and plain JSON files. To get more information on this, take a look at [load command reference](../command-line-interface#fhirbase-load).
{% endhint %}

### Running Web UI and invoking SQL queries <a href="#running-web-ui-and-invoking-sql-queries" id="running-web-ui-and-invoking-sql-queries"></a>

Fhirbase contains tiny web server to provide basic Web UI for querying database. You can use it instead of desktop Postgres clients for simplicity. Following command will start Fhirbase webserver on [http://localhost:3000](http://localhost:3000/)​

![Fhirbase Demo running locally](https://1716361951-files.gitbook.io/~/files/v0/b/gitbook-legacy-files/o/assets%2F-LNI6sKNYJOW0Q34Adqs%2F-LONTjzZ6FIR5gXiamaP%2F-LONrh_ycnNjAcHqWs1u%2FUntitled.png?alt=media\&token=c02a9325-8109-44b9-b536-aabf31718ad7)

```
fhirbase --host localhost -p 5432 -d fhirbase -U postgres -W postgres web
```

Point your browser to [http://localhost:3000](http://localhost:3000/) and type following query in the editor:

```
SELECT COUNT(*), resource#>>'{name,0,given,0}'
FROM patient
GROUP BY resource#>>'{name,0,given,0}'
ORDER BY COUNT(*) DESC;
```

As a result you'll get most popular first names for patients stored in your database.
