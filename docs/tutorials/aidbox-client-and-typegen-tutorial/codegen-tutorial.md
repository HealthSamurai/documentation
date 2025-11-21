# Codegen - generate FHIR types for TypeScript

## Install the codegen dependency

{% code %}
```sh
npm install @atomic-ehr/codegen
```
{% endcode %}

## Usage

To use codegen, we'll need a script that sets up various parameters for type generation:

{% code %}
```typescript
import { APIBuilder } from '@atomic-ehr/codegen';

const builder = new APIBuilder()
  .fromPackage("hl7.fhir.r4.core", "4.0.1")
  .typescript({})
  .outputTo("./typescript-r4/fhir-types");

const report = await builder.generate();
console.log(report);

if (!report.success) {
  console.error("FHIR types generation failed.");
  process.exit(1);
}
```
{% endcode %}

To run:

```sh
tsx generate-types.ts
```

This will generate all FHIR R4 Core types.

## Specify a different FHIR version

There are two ways of specifying FHIR version: `fromPackage` and `fromPackageRef`.

- The `fromPackage` method is used to specify the published package and version.
- The `fromPackageRef` is used to specify a package archive URL.

For example, here's how types for SQL on FHIR can be generated:

{% code %}
```typescript
// --- 8< ---

const builder = new APIBuilder()
  .fromPackageRef("https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/package.tgz")
  .typescript({})
  .outputTo("./typescript-r5/fhir-types");

const report = await builder.generate();

// --- 8< ---
```
{% endcode %}

This will bring in the `sql-on-fhir-v2` package, and all of its dependencies, which includes `hl7.fhir.r5.core`.

## Treeshaking

Not all types are needed at all times, so we can specify what types we want to use in our application:

{% code %}

```typescript
// --- 8< ---

const builder = new APIBuilder()
  .fromPackageRef("https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/package.tgz")
  .typescript({})
  .outputTo("./typescript-r5/fhir-types")
  .treeShake({
    "hl7.fhir.r5.core": {
      "http://hl7.org/fhir/StructureDefinition/OperationOutcome": {},
      "http://hl7.org/fhir/StructureDefinition/Bundle": {},
      "http://hl7.org/fhir/StructureDefinition/Resource": {},
    },
    "org.sql-on-fhir.ig": {
      "https://sql-on-fhir.org/ig/StructureDefinition/ViewDefinition": {},
    },
  });

const report = await builder.generate();

// --- 8< ---
```

{% endcode %}
