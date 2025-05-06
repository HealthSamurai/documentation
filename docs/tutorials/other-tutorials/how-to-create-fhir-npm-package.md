---
description: >-
  In this tutorial, you will learn how to create a FHIR NPM package and import
  it into Aidbox.
---

# How to create FHIR NPM package

A FHIR package groups a coherent collection of conformance resources, like StructureDefinitions, SearchParameters, etc., into an easily distributed NPM package.

## 1. Prepare the package manifest file

To start, we need to describe the package manifest file for our NPM package. A package manifest is a JSON file called `package.json`.

{% code title="package.json" %}
```json
{
    // The globally unique identifier of the package.
    // A package name consists of two or more namespaces separated by a dot.
    // Each namespace starts with a lowercase alphabet character
    "name": "my.fhir.package.name",

    // The version of the package (SemVer).
    // SHALL contain only letters, numbers, and the characters ".", "_", and "-"
    "version": "1.0.0",

    // The description of the package.
    "description": "My FHIR NPM Package",

    // The author of the package.
    "author": "Author Name",

    // The list of package dependencies.
    // Should contain at least one FHIR core package.
    "dependencies": {
        "hl7.fhir.r4.core": "4.0.1",
        "hl7.fhir.us.core": "5.0.1"
    }
}
```
{% endcode %}

The list of packages that can be specified in dependencies can be found in the Aidbox FHIR IGs Registry:

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## 2. Write Package Content

A package contains a set of FHIR resources in the JSON format. Let's describe a few resources for the package.

### 2.1 StructureDefinition resource

Let's describe a profile of the US Core Patient in the file `my-patient-profile.json`.

{% code title="my-patient-profile.json" %}
```json
{
    // Type of the FHIR resource.
    "resourceType": "StructureDefinition",
    
    // How the type relates to the baseDefinition.
    "derivation": "constraint",
    
    // Defines the kind of structure that this definition is describing.
    "kind": "resource",
    
    // Type constrained by this structure.
    "type": "Patient",
    
    // The status of this structure definition.
    "status": "active",
    
    // Name for this structure definition (computer friendly).
    "name": "my-patient-profile",
    
    // 	Whether the structure is abstract.
    "abstract": false,
    
    //The Canonical identifier for this structure definition is represented as a URI (globally unique).
    "url": "http://custom-url/my-patient-profile",
    
    // Definition from which this type is constrained.
    "baseDefinition": "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient",
    
    "differential": {
        "element": [
            {
                // Element ID
                "id": "Patient.address",
                
                // Path of the element in the hierarchy of elements
                "path": "Patient.address",
                
                // The minimum number of times this element SHALL appear in the instance.
                "min": 1
            }
        ]
    }
}
```
{% endcode %}

We have described a profile that inherits from the US Core Patient and adds a required field for address.

### 2.2 SearchParameter resource

Let's describe a custom search parameter on the Patient resource in the file `my-patient-search.json`.

{% code title="my-patient-search.json" %}
```json
{
    // Type of the FHIR resource.
    "resourceType": "SearchParameter",
    
    // ID of the search parameter.
    "id": "Patient-myname",
    
    // Canonical identifier for this search parameter, represented as a URI (globally unique).
    "url": "http://hl7.org/fhir/SearchParameter/Patient-myname",
    
    // Business version of the search parameter.
    "version": "4.0.1", 
    
    // Name for this search parameter (computer friendly).
    "name": "myname",
    
    // Natural language description of the search parameter.
    "description": "text",
    
    // The resource type(s) this search parameter applies to.
    "base": ["Patient"],
    
    // Status of the search parameter.
    "status": "active", 
    
    // Recommended name for the parameter in search url.
    "code": "myname",
    
    // 	The type of value that a search parameter may contain, and how the content is interpreted.
    "type": "string",
    
    //FHIRPath expression that extracts the values.
    "expression": "Patient.name"
}
```
{% endcode %}

## 3. Build NPM package

The resulting npm package has the following structure:

```
package
├── my-patient-profile.json
├── my-patient-search.json
└── package.json
```

We need to build the artifacts of the npm package into an archive in the TAR (.tar.gz) format.

{% tabs %}
{% tab title="Linux" %}
```bash
tar -czvf package.tar.gz package 
```
{% endtab %}

{% tab title="Windows" %}
If Windows supports a native tar command:

```
tar -cvzf package.tar.gz package
```

or download and install **7-Zip**

1. `"C:\Program Files\7-Zip/7z.exe" a -ttar package.tar package`
2. `"C:\Program Files\7-Zip/7z.exe" a -tgzip package.tar.gz package.tar`
{% endtab %}
{% endtabs %}

Following the guide below, upload the archive to Aidbox.

{% content-ref url="../validation-tutorials/upload-fhir-implementation-guide/aidbox-ui/local-ig-package.md" %}
[local-ig-package.md](../validation-tutorials/upload-fhir-implementation-guide/aidbox-ui/local-ig-package.md)
{% endcontent-ref %}
