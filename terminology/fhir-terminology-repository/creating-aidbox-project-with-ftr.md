---
description: Create a Aidbox projcet with ftr that can be used in aidbox
---

# Creating Aidbox project with FTR

### Prerequisites

1. Download zen-ftr CLI: [Download link](https://github.com/HealthSamurai/ftr/releases/download/0.0.1-3/zen.jar)
2. Optionally create an alias to simplify CLI start-up process:\
   `alias zen="rlwrap java -jar /path/to/zen.jar"`

### Creating Aidbox project with FTR based on CSV Source

#### Create a directory `project` with following structure:

| Directory structure                                                      | zen-package.edn                                                                  | zrc/system.edn                                                                                                                                       |
| ------------------------------------------------------------------------ | -------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| <pre><code>project/
  zen-package.edn
  zrc/
    system.edn</code></pre> | <pre class="language-clojure"><code class="lang-clojure">{:deps {}}</code></pre> | <pre class="language-clojure"><code class="lang-clojure">{:ns system
 :import #{aidbox diagnosis}
 
 box
 {:zen/tags #{aidbox/system}}}</code></pre> |

#### Create `zrc/diagnosis.edn` file, containing ValueSet definition:

Replace `<ABSOLUTE_PATH_TO_PROJECT_DIR>` with absolute path to your project directory

```clojure
{:ns diagnosis
 :import #{zen.fhir}
 
 diagnosis-vs
 {:tags #{zen.fhir/value-set}
  :version "0.5.0"
  :uri "diagnosis-vs"
  :ftr
  {:module "csv"
   :source-url "<ABSOLUTE_PATH_TO_PROJECT_DIR>/resources/icd-10.csv"
   :ftr-path "<ABSOLUTE_PATH_TO_PROJECT_DIR>/ftr"
   :tag "v1"
   :source-type "flat-table"
   :package-name "project",
   :extractor-options
   {:format "csv"
    :csv-format {:delimiter ";" :quote "'"}
    :header false
    :data-row 0
    :mapping {:concept {:code {:column 2} :display {:column 3}}}
    :code-system {:id "icd10" :url "http://hl7.org/fhir/sid/icd-10"}
    :value-set   {:id "icd10" :name "icd10.accidents" :url "diagnosis-vs"}}}}}

```

#### Create `resources/icd-10.csv` file, containing CSV source for your ValueSet:

```csv
10344;20;XX;External causes of morbidity and mortality;;;1;
16003;2001;V01-X59;Accidents;10344;;1;
15062;20012;W00-X59;Other external causes of accidental injury;16003;;1;10/07/2020
11748;2001203;W50-W64;Exposure to animate mechanical forces;15062;;1;
11870;2001203W64;W64;Exposure to other and unspecified animate mechanical forces;11748;;1;
11871;2001203W640;W64.0;Exposure to other and unspecified animate mechanical forces home while engaged in sports activity;11870;;1;
11872;2001203W641;W64.00;Exposure to other and unspecified animate mechanical forces, home, while engaged in sports activity;11871;;1;
11873;2001203W641;W64.01;Exposure to other and unspecified animate mechanical forces, home, while engaged in leisure activity;11871;;1;
```

#### Initialize this directory as a git repository, commit your initial set-up

```
git init && git add . && git commit -m "init"
```

#### Generating FTR

Execute command listed below in project directory

```
java -jar <PATH_TO_JAR> build-ftr
```

Afer that, your project should have following structure:

```
project/
  zen-package.edn
  resources/
    icd-10.csv
  zrc/
    system.edn
    disagnosis.edn
  ftr/csv
```
