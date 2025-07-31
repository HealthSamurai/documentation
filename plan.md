# Multi-Product Documentation Support Plan

## Overview
Implement support for multiple documentation products (aidbox, forms) with backward compatibility using WORKDIR environment variable.

## Directory Structure
```
project-root/
├── .gitbook/           # Images remain here (always loaded from root)
│   └── assets/
├── docs/               # Legacy docs (used when WORKDIR not set)
├── docs-new/           # New multi-product root (WORKDIR points here)
│   ├── products.yaml
│   ├── aidbox/
│   │   ├── .gitbook.yaml → ../../.gitbook.yaml (symlink)
│   │   └── docs/ → ../../docs (symlink)
│   └── forms/
│       ├── .gitbook.yaml
│       └── docs/
│           ├── SUMMARY.md
│           ├── README.md
│           └── getting-started.md
```

## Implementation Steps

1. **Create directory structure**
   - Create docs-new folder structure
   - Create symbolic links for aidbox
   - Create minimal forms documentation

2. **Update system initialization**
   - Read WORKDIR at startup only
   - Store in system state
   - Load products based on WORKDIR

3. **Update file loading**
   - Use workdir from context, not env vars
   - Support both legacy and multi-product modes

4. **Update build process**
   - Support WORKDIR in build.clj
   - Update deps.edn to include both paths
   - Update Makefile for both modes

## Key Principles
- Environment variables read only at system startup
- All configuration stored in system state
- Full backward compatibility when WORKDIR not set
- Images always load from root /.gitbook/assets/

## Usage
- Multi-product: `WORKDIR=docs-new make repl`
- Legacy: `make repl-legacy`
- Build: `WORKDIR=docs-new make uberjar`

## Implementation Details

### 1. System State Management
```clojure
;; Store WORKDIR in system state at startup
(system/set-system-state context [::workdir] workdir)

;; Access workdir from context during runtime
(defn get-workdir [context]
  (system/get-system-state context [:gitbok.core/workdir]))
```

### 2. Products Configuration
```clojure
;; Load products based on workdir presence
(if workdir
  (load-products-config-from-workdir context workdir)
  (load-products-config-legacy context))
```

### 3. File Access Pattern
```clojure
;; All file access uses workdir from context
(defn read-summary [context]
  (let [workdir (products/get-workdir context)
        ;; Build path based on workdir
        full-path (build-resource-path workdir docs-path summary-file)]
    (utils/slurp-resource full-path)))
```

### 4. Build Process
- Copy workdir contents when WORKDIR is set
- Copy legacy docs when WORKDIR is not set
- Always include .gitbook from root for images

## Products.yaml Structure
```yaml
products:
  - id: aidbox
    name: "Aidbox Documentation"
    path: /aidbox
    docs_path: aidbox/docs/    # Relative to workdir
    summary_file: SUMMARY.md
    logo: /.gitbook/assets/aidbox_logo.jpg
    
  - id: forms
    name: "Forms Documentation"
    path: /forms
    docs_path: forms/docs/     # Relative to workdir
    summary_file: SUMMARY.md
    logo: /.gitbook/assets/forms_logo.jpg
```