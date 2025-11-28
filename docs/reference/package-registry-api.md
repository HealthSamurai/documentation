# FAR Package Management API

API that allows managing FHIR packages (IG, etc.) in Aidbox (install, update, delete, list, etc.)

It's inspired by npm operations but wrapped as FHIR operations.


## Package installation operation: $fhir-package-install


### Description
Install FHIR Implementation Guide packages from a registry or direct URL. The operation downloads, validates, and installs the specified packages along with their dependencies into the FHIR server. References inside canonicals are pinned to exact dependency versions as described in the [FHIR IG Guidance on pinning](https://build.fhir.org/ig/FHIR/ig-guidance/pinning.html). Only referenced canonical dependencies are installed from dependent packages.

URL: 
```http
POST /fhir/$fhir-package-install
```

### In Parameters:
| Name | Cardinality | Type | Documentation |
|------|-------------|------|---------------|
| package | 1..* | string | Package specification in one of the following formats: **•** A gzipped tarball URL using `https:` or `file:` protocol (e.g., `https://example.org/package.tgz` or `file:///path/to/package.tgz`) **•** A `<name>@<version>` identifier published on the registry (e.g., `hl7.fhir.us.core@5.0.0`) |
| registry | 0..1 | url | The package registry URL to use for resolution. If not specified, uses the server's default registry (typically `https://packages.fhir.org`). Only applies when package parameter uses `<name>@<version>` format. |

### Out Parameters:

### Output Parameters

| Parameter | Cardinality | Type | Example | Description |
|-----------|------------|------|---------|-------------|
| result | 1..1 | boolean | `true` | Indicates whether the installation operation succeeded. |
| package | 1..* |  |  | Installed package information. |
| package.name | 1..1 | string | `hl7.fhir.us.core@5.0.0` | Installed package identifier in `<name>@<version>` format. |
| package.installedCanonicals | 1..1 | integer | `194` | Number of canonical resources installed from the package. |
| package.intention | 1..1 | string | `direct` / `transitive` | Whether the package was installed directly or as a dependency. |
| package.source.type | 1..1 | string | `npm` | Source type from which the package was fetched. |
| package.source.registry | 0..1 | string | `https://fs.get-ig.org/pkgs` | URL of the package registry. |



### Examples

#### Install multiple packages from default registry
```
POST [base]/$fhir-package-install
Content-Type: application/fhir+json

{
  "resourceType": "Parameters",
  "parameter": [{
    "name": "package",
    "valueString": "hl7.fhir.us.core@5.0.0"
  }, {
    "name": "package",
    "valueString": "hl7.fhir.us.mcode@2.0.0"
  }]
}
```

#### Install from local file
```
POST [base]/$fhir-package-install
Content-Type: application/fhir+json

{
  "resourceType": "Parameters",
  "parameter": [{
    "name": "package",
    "valueString": "file:///tmp/my-package.tgz"
  }]
}
```

