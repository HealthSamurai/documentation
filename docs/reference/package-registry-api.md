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

### Examples

<details>
<summary>Install package from default registry</summary>

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

</details>

<details>
<summary>Install package from specific registry</summary>

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

</details>

<details>
<summary>Install latest version of package</summary>

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

</details>

<details>
<summary>Install package from a local file</summary>

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

</details>

<details>
<summary>Install multiple packages</summary>

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

</details>


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

<details>
<summary>Uninstall package</summary>

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

</details>

## See also


{% content-ref url="../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/how-to-load-fhir-ig-with-init-bundle.md" %}
[how-to-load-fhir-ig-with-init-bundle.md](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/how-to-load-fhir-ig-with-init-bundle.md)
{% endcontent-ref %}
