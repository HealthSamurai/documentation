# Aidbox Required Environment Variables

### BOX\_WEB\_PORT

old env:

```
AIDBOX_PORT=<port>
```

new env:

```
BOX_WEB_PORT=<port>
```

A port that web server will listen to.

### BOX\_FHIR\_COMPLIANT\_MODE

{% hint style="warning" %}
Becomes required if BOX\_FHIR\_SCHEMA\_VALIDATION: true
{% endhint %}

old env:

```
AIDBOX_COMPLIANCE: enabled
```

new env:

```
BOX_FHIR_COMPLIANT_MODE: true
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

### BOX\_ROOT\_CLIENT\_ID

old env:

```
AIDBOX_CLIENT_ID=<Aidbox Client resource id>
```

new env:

```
BOX_ROOT_CLIENT_ID=<Aidbox Client resource id>
```

Root Client resource id to create on startup.

### BOX\_ROOT\_CLIENT\_SECRET

old env:

```
AIDBOX_CLIENT_SECRET=<Secret for the client defined with AIDBOX_CLIENT_ID>
```

new env:

```
BOX_ROOT_CLIENT_SECRET=<Secret for the client defined with AIDBOX_CLIENT
```

A secret for that Client.

### BOX\_ADMIN\_ID

old env:

```
AIDBOX_ADMIN_ID=<Aidbox administrator User resource id>
```

new env:

```
BOX_ADMIN_ID=<Aidbox administrator User resource id>
```

Root User resource id to create on startup.

### BOX\_ADMIN\_PASSWORD

old env:

```
AIDBOX_ADMIN_PASSWORD=<Password for the Aidbox administrator>
```

new env:

```
BOX_ADMIN_PASSWORD=<Password for the Aidbox administrator>
```

A password for that User.

### AIDBOX\_LICENSE

```
AIDBOX_LICENSE=<license jwt>
```

License JWT obtained from the license server.
