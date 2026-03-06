# FAR Package Management API

Manage FHIR packages with npm-style operations exposed as FHIR operations.

## $fhir-package-install

Installs FHIR Implementation Guide packages from a registry or direct URL. Downloads, validates, and installs packages along with their dependencies.

The default NPM registry URL can be changed via the [FHIR NPM Package registry](../reference/all-settings#fhir-npm-package-registry) setting.

```http
POST /fhir/$fhir-package-install
```

### Installation Behavior

During installation, Aidbox performs **pinning** and **tree-shaking** on canonicals:

* The **Core** package (e.g., `hl7.fhir.r4.core`) is automatically installed if missing.
* The **Terminology (THO)** package must be provided explicitly if needed.
* Only referenced canonicals from dependency packages are included (tree-shaking).
* Each canonical `<url>|<version>` pair is unique in the final result.

For details on the candidate selection algorithm and recursive pinning process, see [Pinning and Tree-Shaking](../artifact-registry/artifact-registry-overview.md#pinning-and-tree-shaking).

### Input Parameters

| Name | Card. | Type | Description |
|------|-------------|------|-------------|
| package | 1..* | string | Package specification in one of the following formats:<ul><li>`<name>@<version>` — identifier from the registry (e.g., `hl7.fhir.us.core@5.0.0`)</li><li>`https://` — remote gzipped tarball URL (e.g., `https://example.org/package.tgz`)</li><li>`file://` — local gzipped tarball path (e.g., `file:///path/to/package.tgz`)</li></ul> |
| registry | 0..1 | url | Package registry URL used for resolving non-local packages.<br>Defaults to `https://fs.get-ig.org/pkgs`. |
| override | 0..* | | Dependency override rule. Each `override` parameter contains two `part` elements: `from` and `to`. See [Dependency Overrides](#dependency-overrides). |
| override.part:from | 1..1 | string | Dependency to override. Accepts a package name (e.g., `some.package`) or a version-qualified name (e.g., `some.package@1.0.0`). |
| override.part:to | 1..1 | string \| boolean | Override value. See [Dependency Overrides](#dependency-overrides) for accepted formats. |

### Output Parameters

| Parameter | Cardinality | Type | Example | Description |
|-----------|-------------|------|---------|-------------|
| result | 1..1 | boolean | `true` | Whether the installation succeeded. |
| package | 1..* | | | Installed package information. |
| package.name | 1..1 | string | `hl7.fhir.us.core@5.0.0` | Package identifier in `<name>@<version>` format. |
| package.installedCanonicals | 1..1 | integer | `194` | Number of canonical resources installed. |
| package.intention | 1..1 | string | `direct` / `transitive` | Whether installed directly or as a dependency. |
| package.source.type | 1..1 | string | `npm` / `file` | Source type from which the package was fetched. |
| package.source.registry | 0..1 | string | `https://fs.get-ig.org/pkgs` | Package registry URL. |

### Dependency Overrides

The `override` parameter allows you to control how dependencies are resolved during package installation. This is useful when a package declares a dependency that is unavailable, needs to be pinned to a different version, or should be replaced with an alternative package.

Each `override` parameter must contain exactly two `part` elements:

- **`from`** (`valueString`) — identifies the dependency to override.
- **`to`** (`valueString` or `valueBoolean`) — specifies the replacement.

#### `from` matching rules

| Format | Description |
|--------|-------------|
| `<name>` | Matches the dependency by package name regardless of version (e.g., `some.package`). |
| `<name>@<version>` | Matches only when both the package name and version match (e.g., `some.package@1.0.0`). Version-qualified overrides take priority over name-only overrides. |

#### `to` override values

| Value | Type | Description |
|-------|------|-------------|
| `<version>` | string | Replaces the dependency version (e.g., `"2.0.0"`). The package name stays the same. |
| `npm:<name>@<version>` | string | Replaces the dependency with an entirely different package (e.g., `"npm:alt.package@1.0.0"`). |
| `false` | boolean | Skips the dependency entirely — it will not be installed. |

Multiple `override` parameters can be provided in a single request to override several dependencies at once.

#### Override examples

The examples below demonstrate installing `hl7.fhir.us.core@8.0.0` from the Simplifier registry. US Core 8.0.0 depends on `us.nlm.vsac@0.23.0`, but Simplifier stopped hosting VSAC versions after `0.17.0`, which causes installation to fail. Overrides solve this problem.

##### Override dependency version

Pin `us.nlm.vsac` to version `0.17.0` (the latest available on Simplifier) instead of the requested `0.23.0`:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.nlm.vsac"
        },
        {
          "name": "to",
          "valueString": "0.17.0"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

##### Override a specific dependency version

Use version-qualified `from` to only override VSAC when the exact version `0.23.0` is requested:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.nlm.vsac@0.23.0"
        },
        {
          "name": "to",
          "valueString": "0.17.0"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

##### Skip a dependency

Exclude `us.nlm.vsac` entirely by setting `to` to `false`:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.nlm.vsac"
        },
        {
          "name": "to",
          "valueBoolean": false
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

##### Skip a dependency only for a specific version

Use version-qualified `from` with `to: false` to skip the dependency only when that exact version is requested. Other versions of the same package are still installed normally:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.nlm.vsac@0.23.0"
        },
        {
          "name": "to",
          "valueBoolean": false
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

This skips `us.nlm.vsac` only when version `0.23.0` is requested (e.g. by US Core 8.0.0). If another package requested a different version of `us.nlm.vsac`, it would still be installed.

##### Replace dependency with a different package

Replace `us.nlm.vsac` with an alternative package using the `npm:` prefix:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.nlm.vsac"
        },
        {
          "name": "to",
          "valueString": "npm:my.custom.vsac@1.0.0"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

##### Multiple overrides in one request

Combine several overrides — pin VSAC to `0.17.0` and skip another dependency:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.nlm.vsac"
        },
        {
          "name": "to",
          "valueString": "0.17.0"
        }
      ]
    },
    {
      "name": "override",
      "part": [
        {
          "name": "from",
          "valueString": "us.cdc.phinvads"
        },
        {
          "name": "to",
          "valueBoolean": false
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

### Examples

#### Install package from default registry

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@5.0.0"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.core@5.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 194
        },
        {
          "name": "intention",
          "valueString": "direct"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "us.nlm.vsac@0.3.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 20
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.uv.sdc@3.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 11
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.terminology.r4@3.1.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 7
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

#### Install package from specific registry

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@5.0.0"
    },
    {
      "name": "registry",
      "valueString": "https://packages.simplifier.net"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.core@5.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 194
        },
        {
          "name": "intention",
          "valueString": "direct"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "us.nlm.vsac@0.3.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 20
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.uv.sdc@3.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 11
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.terminology.r4@3.1.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 7
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

#### Install latest version of package

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.core@8.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 205
        },
        {
          "name": "intention",
          "valueString": "direct"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "us.nlm.vsac@0.23.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 96
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.terminology.r4@6.4.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 23
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.uv.extensions.r4@5.2.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 2
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "us.cdc.phinvads@0.12.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 2
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.uv.sdc@3.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 11
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

##### Install package from a local file

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "file:///tmp/my-package.tgz"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "my.package@1.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 5
        },
        {
          "name": "intention",
          "valueString": "direct"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "file"
            }
          ]
        }
      ]
    },
  ]
}
```
{% endtab %}
{% endtabs %}

#### Install multiple packages

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@5.0.0"
    },
    {
      "name": "package",
      "valueString": "hl7.fhir.us.mcode@2.0.0"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.core@5.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 194
        },
        {
          "name": "intention",
          "valueString": "direct"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.mcode@2.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 153
        },
        {
          "name": "intention",
          "valueString": "direct"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "us.nlm.vsac@0.3.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 20
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.uv.sdc@3.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 11
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.terminology.r4@3.1.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 7
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.core@4.0.0"
        },
        {
          "name": "installedCanonicals",
          "valueInteger": 30
        },
        {
          "name": "intention",
          "valueString": "transitive"
        },
        {
          "name": "source",
          "part": [
            {
              "name": "type",
              "valueString": "npm"
            },
            {
              "name": "registry",
              "valueString": "https://fs.get-ig.org/pkgs"
            }
          ]
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

## $fhir-package-uninstall

Uninstalls FHIR Implementation Guide packages from the server. Removes the specified packages and their canonical resources.

```http
POST /fhir/$fhir-package-uninstall
```

### Input Parameters

| Name | Card. | Type | Description |
|------|-------|------|-------------|
| package | 1..* | string | Package identifier in `<name>@<version>` format (e.g., `hl7.fhir.us.core@5.0.0`). |

### Output Parameters

| Parameter | Cardinality | Type | Example | Description |
|-----------|-------------|------|---------|-------------|
| result | 1..1 | boolean | `true` | Whether the uninstallation succeeded. |
| package | 1..* | | | Uninstalled package information. |
| package.name | 1..1 | string | `hl7.fhir.us.core@5.0.0` | Package identifier in `<name>@<version>` format. |
| package.status | 1..1 | string | `deleted` | Status of the uninstallation. |

### Examples

#### Uninstall package

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-uninstall
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.us.core@8.0.0"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "result",
      "valueBoolean": true
    },
    {
      "name": "package",
      "part": [
        {
          "name": "name",
          "valueString": "hl7.fhir.us.core@8.0.0"
        },
        {
          "name": "status",
          "valueString": "deleted"
        }
      ]
    }
  ]
}
```
{% endtab %}
{% endtabs %}

## See also


{% content-ref url="../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/how-to-load-fhir-ig-with-init-bundle.md" %}
[how-to-load-fhir-ig-with-init-bundle.md](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/how-to-load-fhir-ig-with-init-bundle.md)
{% endcontent-ref %}
