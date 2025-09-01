---
description: Follow these steps to create a TypeScript project with Aidbox
---

# Use Aidbox with TypeScript

## Prerequisites

- Run Aidbox locally by following the instructions in the [Run Aidbox locally](run-aidbox-locally.md) guide

## Steps

1. Generate SDK package

   ```bash
   npm install -g @fhirschema/codegen
   npx fscg generate -g typescript -p hl7.fhir.r4.core@4.0.1 -o aidbox
   ```

2. Create a new typescript project

   ```bash
   npm init -y
   npm i typescript
   npm i --save-dev @type-challenges/utils
   npm i --save-dev @types/node
   ```

3. Create a new file `tsconfig.json` and add the following code:

   ```json
   {
     "compilerOptions": {
       "target": "ES2020",
       "module": "NodeNext",
       "moduleResolution": "NodeNext",
       "esModuleInterop": true,
       "strict": true,
       "skipLibCheck": true,
       "noEmit": true
     },
     "include": ["./aidbox/**/*.ts"]
   }
   ```

3. Navigate to [Aidbox REST console](http://localhost:8080/ui/console/#rest) and create a client by executing the following request:

   ```http
   POST /fhir/Client
   Content-Type: application/json

   {
     "resourceType": "Client",
     "id": "my-client",
     "secret": "my-secret",
     "grant_types": ["basic"]
   }
   ```

4. Create an access policy by executing the following request in the [Aidbox REST console](http://localhost:8080/ui/console/#rest):

   ```http
   POST /fhir/AccessPolicy
   Content-Type: application/json

   {
     "resourceType": "AccessPolicy",
     "engine": "allow",
     "link": [
       { "id": "my-client", "resourceType": "Client" }
     ]
   }
   ```

5. Create a new file `index.ts` and add the following code:

   ```typescript
   import { Client } from "../aidbox";
   import type { Patient } from "../aidbox/types/hl7-fhir-r4-core";
   
   async function main() {
     const client = new Client("http://localhost:8080", {
       auth: {
         method: "basic",
         credentials: {
           username: "my-client",
           password: "my-secret"
         }
       }
     });
   
     const patient: Patient = {
       identifier: [{ system: "http://org.io/id", value: "0000-0000" }],
       name: [{ given: ["John"], family: "Doe" }],
       gender: "male",
       birthDate: "1990-01-01",
     };
   
     const response = await client.resource.create("Patient", patient);
   
     console.log(JSON.stringify(response, null, 2));
   }
   
   main().catch((error) =>
     console.log(error instanceof Error ? error.message : String(error)),
   );
   ```

6. Run the project

   ```bash
   npx tsx index.ts
   ```

## Next Steps

* Learn more about [Aidbox SDKs generation](../developer-experience/developer-experience-overview.md#use-aidbox-sdks-for-customized-experience)
* Learn more about [Aidbox Access Control](../access-control/access-control.md)
