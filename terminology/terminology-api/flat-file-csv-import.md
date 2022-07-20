# Flat file (CSV) import

If you have a terminology distributed as a flat file, for example CSV, you can use this API to import it as a set of [Concept](../concept.md) resources and later use them with [Terminology API](../terminology.md).

## Load from URL

Aidbox provides RPC API to load terminology from a flat file by url.

### RPC `aidbox.terminology.import-flat/import-from-url`

This method accepts an URL pointing to a flat file with a terminology, file format description, columns to [concept](../concept.md) mapping, `CodeSystem` resource and `ValueSet` resource. For hierarchical terminology it can build [hierarchy materialized paths](../concept.md#hierarchy).

When upload is done, rows of the flat file are loaded as `Concept` resources into Aidbox. Also `CodeSystem` and `ValueSet` resources are created, these resources describe the full terminology from the file, i.e. the `ValueSet` composed of only this `CodeSystem` and all `Concept`s have `system` set to the `CodeSystem.url`.

{% tabs %}
{% tab title="Request" %}
_Also check "Request with comments" and "Parameters" tabs for full request structure description._

```yaml
POST /rpc
content-type: application/edn
accept: application/edn

{:method aidbox.terminology.import-flat/import-from-url
 :params
 {:source-url "<https://example.com/path/to/your/file.csv>"

  :format "csv"
  :csv-format {:delimiter "<char>"
               :quote "<char>"}

  :header <true|false>
  :header-row <integer>
  :data-row <integer>
  :hierarchy <true|false>

  :mapping {:concept {:code {:column <column>}
                      :display {:column <column>}
                      :deprecated? {:column <column>
                                    :true-values ["<string>"]}
                      :parent-id {:column <column>}
                      :hierarchy-id {:column <column>}}
            :property {"<property-name>" {:column <column>}}}

  :code-system {:url "<CodeSystem url>"}
  :value-set {:url "<ValueSet url>"}}}

```
{% endtab %}

{% tab title="Request with comments" %}
Request structure with commentaries. _(The "Parameters" tab contains the same comments in a different format)_

```clojure
POST /rpc
content-type: application/edn
accept: application/edn

{:method aidbox.terminology.import-flat/import-from-url
 :params
 {:source-url "<https://example.com/path/to/your/file.csv>" ;; Required. URL pointing to a flat file

  :format "csv" ;; Required. Currently the only option is csv
  :csv-format {:delimiter "<char>" ;; Required. Value separator character
               :quote "<char>"}   ;; Required. Quote character used to wrap values which contain delimiter or new-line characters

  :code-system {;; Required. CodeSystem resource. Aidbox creates this resource
                :url "<CodeSystem url>"} ;; Required. CodeSystem canonical URL
  :value-set {;; Required. ValueSet resource. Aidbox creates this resource
              :url "<ValueSet url>"} ;; Required. ValueSet canonical URL

  :header <true|false> ;; Required. Set true if the flat file contains header, otherwhise false
  :header-row <integer> ;; Required if `:header true`. The flat file header row index. Row indexing starts with 0
  :data-row <integer> ;; Required. Index of the first row where data starts in the flat file
  :hierarchy <true|false> ;; Optional. Set true if terminology is hierarchical. If true Aidbox builds hierarchy materialized paths for each concept

  :mapping {;; Required. Mapping of the flat file columns to Concept resource.
            ;; If `:header true` put the <column> as a string, otherwhise put an integer as a column index (indexing starts with 0)
            :concept {;; Required. Maps to main part of a Concept resource.
                      :code {:column <column>} ;; Required. Maps to Concept.code
                      :display {:column <column>} ;; Optional. Maps to Concept.display
                      :deprecated? {:column <column> ;; Optional. Maps to Concept.deprecated
                                    :true-values ["<string>"]} ;; Required if `:deprecated? :column` specified. List of values indicating that a Concept is deprecated. For example: :true-values ["inactive" "deprecated"]
                      :parent-id {:column <column>} ;; Required if `:hierarchy true`. Maps to Concept.parent-id. Aidbox uses this value to find parent Concepts when building materialized paths
                      :hierarchy-id {:column <column>}} ;; Required if `:hierarchy true`. Maps to Concept.hierarchy-id. Aidbox uses this value to check if this Concept is a parent when building materialized paths
            :property {;; Optional. Maps to Concept.property.<property-name>
                       "<property-name>" {:column <column>}}}}} ;; Reference to the flat file column.

```
{% endtab %}

{% tab title="Parameters" %}
Object with the following structure:

* `source-url` - **Required**. URL pointing to a flat file
* `format` - **Required**. Currently the only option is `"csv"`
* `csv-format` - **Required**. Describes parameters of the csv-file:
  * `delimiter` - **Required**. Value separator character
  * `quote` - **Required**. Quote character used to wrap values which contain delimiter or new-line characters
* `code-system` - **Required**. CodeSystem resource. Aidbox creates this resource
  * `url` - **Required**. CodeSystem canonical URL
* `value-set` - **Required**. ValueSet resource. Aidbox creates this resource
  * `url` - **Required**. ValueSet canonical URL
* `header` - **Required**. Set `true` if the flat file contains header, otherwise `false`
* `header-row` - **Required** if `header` `true`. The flat file header row index. Row indexing starts with 0
* `data-row` - **Required**. Index of the first row where data starts in the flat file
* `hierarchy` - _Optional_. Set `true` if terminology is hierarchical. If `true` Aidbox builds hierarchy materialized paths for each concept
* `mapping` - **Required**. Mapping of the flat file columns to Concept resource. If `header true` put the column as a string, otherwise put an integer as a column index (indexing starts with 0)
  * `concept` **Required**. Maps to main part of a Concept resource.
    * `code`
      * `column` - **Required**. Maps to Concept.code
    * `display`
      * `column` - _Optional_. Maps to Concept.display
    * `deprecated?`
      * `column` - _Optional_. Maps to Concept.deprecated
      * `true-values` - **Required** if `deprecated? <column>` specified. List of values indicating that a Concept is deprecated. For example: `:true-values ["inactive" "deprecated"]`
    * `parent-id` - **Required** if `hierarchy true`. Maps to Concept.parent-id. Aidbox uses this value to find parent Concepts when building materialized paths
    * `hierarchy-id` - **Required** if `hierarchy true`. Maps to Concept.hierarchy-id. Aidbox uses this value to check if this Concept is a parent when building materialized paths
  * `property` _Optional_. Maps to `Concept.property.<property-name>`
    * `<property-name>`
      * `column` - Reference to the flat file column.
{% endtab %}

{% tab title="Result" %}
Returns object with following attributes:

```yaml
{:result
  {:count <count of records in the file>,
   :code-system-id <"generated code system uuid">,
   :value-set-id <"generated value set uuid">}}
```
{% endtab %}
{% endtabs %}

#### Examples

Here are 2 examples importing a fragment of ICD-10. The first one specifies as few parameters as possible and uses a CSV file without a header. The second one uses a CSV with header and also specifies deprecation criteria, hierarchy parameters and concept properties mapping. Below examples there are commentaries of parameters  and contents of used CSV files.

{% tabs %}
{% tab title="Request without header parameter" %}
* CSV file delimiter is `;`.This file contains no quoted strings, but since `:quote` parameter is required `'` character is used
* `Concept.code` is mapped to the column with index `2`
* `Concept.display` is mapped to the column with index `3`
* Request will create
  * a `CodeSystem/icd10` resource with `:url http://hl7.org/fhir/sid/icd-10`
  * a `ValueSet/icd10` resource with `:url http://hl7.org/fhir/ValueSet/icd-10`

#### Request

```yaml
POST /rpc
accept: application/edn
content-type: application/edn

{:method aidbox.terminology.import-flat/import-from-url
 :params {:source-url "https://storage.googleapis.com/aidbox-public/documentation/icd10_example_no_header.csv"
          :format      "csv"
          :csv-format  {:delimiter ";"
                        :quote "'"}

          :header   false
          :data-row 0
          :mapping  {:concept  {:code    {:column 2}
                                :display {:column 3}}}

          :code-system {:id "icd10", :url "http://hl7.org/fhir/sid/icd-10"}
          :value-set   {:id "icd10", :url "http://hl7.org/fhir/ValueSet/icd-10"}}}
```

#### Response:

```yaml
{:result
  {:count 8,
   :code-system-id "0be7ce48-edab-497c-bb52-186a9ac64746",
   :value-set-id "f4418460-cdac-4847-99be-6ae4c3a64e87"}}
```

#### CSV file content

File is also available [here](https://storage.googleapis.com/aidbox-public/documentation/icd10\_example\_no\_header.csv)

```
10344;20;XX;External causes of morbidity and mortality;;;1;
16003;2001;V01-X59;Accidents;10344;;1;
15062;20012;W00-X59;Other external causes of accidental injury;16003;;1;10/07/2020
11748;2001203;W50-W64;Exposure to animate mechanical forces;15062;;1;
11870;2001203W64;W64;Exposure to other and unspecified animate mechanical forces;11748;;1;
11871;2001203W640;W64.0;Exposure to other and unspecified animate mechanical forces home while engaged in sports activity;11870;;1;
11872;2001203W641;W64.00;Exposure to other and unspecified animate mechanical forces, home, while engaged in sports activity;11871;;1;
11873;2001203W641;W64.01;Exposure to other and unspecified animate mechanical forces, home, while engaged in leisure activity;11871;;1;
```
{% endtab %}

{% tab title="Request with header parameter" %}
* Value delimiter is `,` and string quotation is `"`
* Header is in the first line (line index is `0`) and data starts in the next line (line index `1`)&#x20;
* Materialized paths building is enabled, thus `:hierarchy true`
  * Column `ID_PARENT` is used to find parents
  * Column `ID` is used to resolve when a row is referenced as a parent
* `Concept.code` is mapped to the column with name `ICD_CODE`
* `Concept.display` is mapped to the column with index `ICD_NAME`
* Concepts are deprecated when the `ACTIVE` column value is `0`
* `Concept.property.updated_date` is mapped to the `DATE` column
* Request will create
  * a `CodeSystem/icd10` resource with `:url http://hl7.org/fhir/sid/icd-10`
  * a `ValueSet/icd10` resource with `:url http://hl7.org/fhir/ValueSet/icd-10`

#### Example

```yaml
POST /rpc
accept: application/edn
content-type: application/edn

{:method aidbox.terminology.import-flat/import-from-url
 :params {:source-url  "https://storage.googleapis.com/aidbox-public/documentation/icd10_example_with_header.csv"
          :format      "csv"
          :csv-format  {:delimiter ","
                        :quote "\""}

          :header      true
          :header-row  0
          :data-row    1
          :hierarchy   true
          :mapping     {:concept  {:code        {:column "ICD_CODE"}
                                   :display     {:column "ICD_NAME"}
                                   :deprecated? {:column "ACTIVE"
                                                 :true-values ["0"]}
                                   :parent-id    {:column "ID_PARENT"}
                                   :hierarchy-id {:column "ID"}}
                        :property {"updated_date" {:column "DATE"}}}

          :code-system {:id "icd10", :url "http://hl7.org/fhir/sid/icd-10"}
          :value-set   {:id "icd10", :url "http://hl7.org/fhir/ValueSet/icd-10"}}}
```

#### Response

```yaml
{:result
  {:count 8,
   :code-system-id "5b56b672-7220-4878-96f6-254df0128a43",
   :value-set-id "170d0ce8-0dc3-4bd1-8465-5e584955105e"}}
```

#### CSV file content

File is also available [here](https://storage.googleapis.com/aidbox-public/documentation/icd10\_example\_with\_header.csv)

```csv
ID,REC_CODE,ICD_CODE,ICD_NAME,ID_PARENT,ADDL_CODE,ACTIVE,DATE
10344,20,XX,External causes of morbidity and mortality,,,1,
16003,2001,V01-X59,Accidents,10344,,1,
15062,20012,W00-X59,Other external causes of accidental injury,16003,,1,10/07/2020
11748,2001203,W50-W64,Exposure to animate mechanical forces,15062,,1,
11870,2001203W64,W64,Exposure to other and unspecified animate mechanical forces,11748,,1,
11871,2001203W640,W64.0,Exposure to other and unspecified animate mechanical forces home while engaged in sports activity,11870,,1,
11872,2001203W641,W64.00,"Exposure to other and unspecified animate mechanical forces, home, while engaged in sports activity",11871,,1,
11873,2001203W641,W64.01,"Exposure to other and unspecified animate mechanical forces, home, while engaged in leisure activity",11871,,1,
```
{% endtab %}
{% endtabs %}
