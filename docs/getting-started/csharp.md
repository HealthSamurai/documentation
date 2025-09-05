---
description: Follow these steps to set up a C# project with Aidbox
---

# Use Aidbox with C#

To use Aidbox with C#, we recommend to use our [SDK generation library](https://github.com/fhir-schema/fhir-schema-codegen). 
It will generate static FHIR Client and the set of types for your FHIR package. You can read about the idea of it [here](../developer-experience/developer-experience-overview.md#use-aidbox-sdks-for-customized-experience).

## Prerequisites

- Run Aidbox locally by following the instructions in the [Run Aidbox locally](run-aidbox-locally.md) guide

## Steps

1. Create a Client and an Access Policy in Aidbox.

   Navigate to [REST Console](http://localhost:8080/ui/console#/rest) in Aidbox UI and execute the following requests:
   
   ```json
   POST /fhir/Client
   content-type: application/json
   accept: application/json
   
   {
     "secret": "secret",
     "grant_types": [
       "basic"
     ],
     "id": "basic",
     "resourceType": "Client"
   }
   ```
   
   ```json
   POST /fhir/AccessPolicy
   content-type: application/json
   accept: application/json
   
   {
     "link": [
       {
         "reference": "Client/basic"
       }
     ],
     "engine": "allow",
     "id": "basic-policy",
     "resourceType": "AccessPolicy"
   }
   ```


2. Set up .NET project:

   ```bash
   dotnet new console -n MyApp
   cd MyApp
   dotnet add package System.Text.Json
   dotnet add package Microsoft.Extensions.DependencyInjection
   ```

3. Generate R4 SDK

   ```bash
   npm install -g @fhirschema/codegen
   npx fscg generate -g csharp -o aidbox -p hl7.fhir.r4.core@4.0.1
   ```
   
   Here's how it should look like:
   
   ```
   ├── MyApp
   │   ├── aidbox
   │   ├── bin
   │   ├── MyApp.csproj
   │   ├── obj
   │   └── Program.cs
   ├── Program.cs
   └── docker-compose.yaml
   ```

4. Create a Patient using created Client:
   ```c#
   using Aidbox.Client;
   using Aidbox.FHIR.R4.Core;
   
   var auth = new Auth
   {
       Method = AuthMethods.BASIC,
       Credentials = new AuthCredentials
       {
           Username = "basic",
           Password = "secret"
       }
   };
   
   var client = new Client("http://localhost:8080/fhir", auth);
   
   
   var patient = new Patient
   {
       Identifier = [new Identifier { System = "http://org.io/id", Value = "0000-0000" }],
       Name = [new HumanName { Given = ["John"], Family = "Doe" }],
       Gender = "male",
       BirthDate = "1990-01-01",
   };
   
   var (result, error) = await client.Create(patient);
   if (result != null)
   {
       System.Console.WriteLine("Patient id: " + result.Id);
   }
   else
   {
       System.Console.WriteLine(error);
   }
   ```
5. Run project

   ```bash
      dotnet run
   ```

   The output: 
   ```
   Patient id: 4c43be71-bebb-41ce-8913-e48d3fecbfa4
   ```


## Next steps

* Learn more about [Aidbox SDKs generation](../developer-experience/developer-experience-overview.md#use-aidbox-sdks-for-customized-experience)
* Learn more about [Aidbox Access Control](../access-control/access-control.md)
* See also [how to use Firely .NET SDK with Aidbox](https://github.com/Aidbox/examples/tree/main/developer-experience/aidbox-firely-dotnet-client)
