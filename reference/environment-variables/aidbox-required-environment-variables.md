# Aidbox required environment variables

### AIDBOX\_PORT

```
AIDBOX_PORT=<port>
```

A port that web server will listen to.

### AIDBOX\_FHIR\_VERSION

```
AIDBOX_FHIR_VERSION=<fhir version>
```

`<fhir version>` is one of: `1.0.2`, `1.4.0`, `1.8.0`, `3.0.1`, `3.2.0`, `3.3.0`, `4.0.0`, `4.0.1`, `4.3.0`, `5.0.0`

{% hint style="warning" %} We recommend using version 4.0.1 {% endhint %}

### AIDBOX\_COMPLIANCE

{% hint style="warning" %} Becomes required if AIDBOX_FHIR_SCHEMA_VALIDATION: true {% endhint %}

```
AIDBOX_COMPLIANCE: enabled
```

### PGHOST

```
PGHOST=<PostgreSQL server hostname>
```

Aidboxdb host address.

### PGPORT

```
PGPORT=<PostgreSQL server port>
```

Aidboxdb port.

### PGDATABASE

```
PGDATABASE=<PostgreSQL database name>
```

Name of the database that is used to store resources. `postgres` database name is not supported. Use `AIDBOX_AIDBOX_DB` environment variable if you really need to set `postgres` to be the database for Aidbox.

### PGUSER

```
PGUSER=<PostgreSQL role name>
```

Database role name to use.

### PGPASSWORD

```
PGPASSWORD=<PostgreSQL role password>
```

A password of that role.

### AIDBOX\_CLIENT\_ID

```
AIDBOX_CLIENT_ID=<Aidbox Client resource id>
```

Root Client resource id to create on startup.

### AIDBOX\_CLIENT\_SECRET

```
AIDBOX_CLIENT_SECRET=<Secret for the client defined with AIDBOX_CLIENT_ID>
```

A secret for that Client.

### AIDBOX\_ADMIN\_ID

```
AIDBOX_ADMIN_ID=<Aidbox administrator User resource id>
```

Root User resource id to create on startup.

### AIDBOX\_ADMIN\_PASSWORD

```
AIDBOX_ADMIN_PASSWORD=<Password for the Aidbox administrator>
```

A password for that User.

### AIDBOX\_LICENSE

```
AIDBOX_LICENSE=<license jwt>
```

License JWT obtained from the license server.
