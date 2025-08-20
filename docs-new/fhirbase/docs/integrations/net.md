# .NET

This page describes .NET library that provide access to fhirbase. This library uses following nuget packages:

* [https://www.nuget.org/packages/Hl7.Fhir.STU3/](https://www.nuget.org/packages/Hl7.Fhir.STU3/) - this is an official .NET API for HL7 FHIR used to model representation;
* [https://www.nuget.org/packages/Newtonsoft.Json/](https://www.nuget.org/packages/Newtonsoft.Json/) - this is a popular high-performance JSON framework for .NET. It is used to convert resources from json string to classes and back;
* [https://www.nuget.org/packages/Npgsql/](https://www.nuget.org/packages/Npgsql/) - this is a .NET Access to PostgreSQL. It is used to connect to fhirbase.

## Installation

Clone this project somewhere:

```bash
git clone https://github.com/fhirbase/fhirbase.net
```

Then add reference to library to your project (for example):

```
dotnet add reference ../FhirbaseConnector/FhirbaseConnector.csproj 
```

That's all!

## Usage

Create `Connector` to fhirbase and use it's methods to resource access like this:

```csharp
var connector = new Connector();
var patient = new Patient
{
    Name = new List<HumanName>
    {
	    new HumanName
	    {
	        Given = new List<string> { "John" },
	        Family = "Doe"
	    }
    }
};
// Create patient
patient = connector.Create(patient);
// Do something with patient

patient.Name[0].Family = "Snow";
patient.BirthDate = "1985-03-20";
// Update patient
patient = connector.Update(patient);
// Do something with patient
		
var sql = "SELECT _fhirbase_to_resource(row(r.*)::_resource) FROM patient AS r WHERE resource#>>'{name,0,family}' ilike '%snow%'";
// Search patient by family
foreach (var p in connector.Read<Patient>(sql))
{
    // Do something with p
}

// Delete patient
patient = connector.Delete(patient);
// Do something with patient
```

## API Reference

### Connector Class

Namespace: FhirbaseConnector

#### Constructors

```csharp
Connector(string host     = "localhost",
          int port        = 5432,
          string user     = "postgres",
          string password = "",
          string db       = "fhirbase")
```

Parameters:

* `host` - network address of the Postgresql server where resides fhirbase;
* `port` - port of the Postgresql server;
* `user` - Postgresql user that have access to the fhirbase;
* `password` - user password;
* `db` - database name of the fhirbase.

#### Methods:

```csharp
public T Create<T>(T resource) where T : Base
```

_Create new resource in the fhirbase._

Parameters:

* `resource` - resource that will be created in the fhirbase.

Returns:

* Returns updated resource. `Base` is the type from the `Hl7.Fhir.STU3`.



```csharp
public T Update<T>(T resource) where T : Base
```

_Update resource in the fhirbase._

Parameters:

* `resource` - resource that will be updated in the fhirbase.&#x20;

Returns:

* Returns updated resource. `Base` is the type from the `Hl7.Fhir.STU3`.



```csharp
public T Delete<T>(T resource) where T : Resource
```

_Delete resource from fhirbase._

Parameters:

* `resource` - resource that will be deleted from the fhirbase.

Returns:

* Returns deleted resource. `Resource` is the type from the `Hl7.Fhir.STU3`.



```csharp
public List<T> Read<T>(int limit = -1) where T : Base
```

_Read resources from fhirbase._

Parameters:

* `limit` - if limit is -1 then all resources will be returned else only specified amount.

Returns:

* Returns list of the resources. `Base` is the type from the `Hl7.Fhir.STU3`



```csharp
public List<T> Read<T>(string sql) where T : Base
```

_Executes arbitrary sql and returns list of resources._

Parameters:

* `sql` - arbitrary sql string.

Returns:

* Returns list of the resources. `Base` is the type from the `Hl7.Fhir.STU3`

## Demo

Install and init fhirbase. Then execute following commands (notice that you need dotnet 2.1):

```bash
git clone https://github.com/fhirbase/fhirbase.net
cd fhirbase.net/Demo/
dotnet run
```
