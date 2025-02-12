---
description: First-class extension as Zen profile
---

# First-Class Extensions using Zen

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](../../../../modules/profiling-and-validation/fhir-schema-validator/)
{% endhint %}

While FHIR uses two different ways to define **core elements** and **extensions**, zen profiles provide a unified framework to describe both. Zen FHIR format offers user-defined elements or "first-class extensions". In zen profiles, you can define new attributes (elements) for existing (FHIR) resources. \
The example below shows how to define extensions in zen profiles.

{% tabs %}
{% tab title="FHIR Patient with race extension" %}
```yaml
resourceType: Patient
id: sample-pt
extension:
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-race
  extension:
  - url: text
    valueString: Asian Indian
  - url: ombCategory
    valueCoding:
       system: urn:oid:2.16.840.1.113883.6.238
       code: 2028-9
       display: Asian
  - url: detailed
    valueCoding:
       system:
       code: 2029-7	
       display: Asian Indian
```
{% endtab %}

{% tab title="Zen profile of Patient with race extension" %}
```clojure
MyPatientProfile
{:zen/tags #{zen/schema zen.fhir/profile-schema}
 :zen.fhir/version "0.5.0"
 :zen.fhir/profileUri "urn:fhir:extension:MyPatientProfile"
 :confirms #{hl7-fhir-r4-core/Patient}
 :type zen/map
 :keys {:race
        {:type zen/map
         :fhir/extensionUri "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race"
         :require #{:text}
         :keys {:ombCategory {:type zen/vector
                              :maxItems 5
                              :every {:confirms #{hl7-fhir-r4-core/Coding}
                                      :zen.fhir/value-set {:symbol omb-race-category-value-set
                                                           :strength :required}}}
                :detailed {:type zen/vector
                           :every {:confirms #{hl7-fhir-r4-core/Coding}
                                   :zen.fhir/value-set {:symbol detailed-race-value-set
                                                        :strength :required}}}
                :text {:confirms #{hl7-fhir-r4-core/string}
                       :zen/desc "Race Text"}}}}}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Note that extension elements have `:confirms` to a FHIR primitive or complex type specified. Previously these were specified in the base profile schema. These `:confirms` and the `:fhir/extensionUri` are needed to allow zen FHIR <-> FHIR format transformation.&#x20;
{% endhint %}

### Steps to define first-class extension as zen profile:

1. [Initialize](https://docs.aidbox.app/profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile#create-a-zen-project) zen project and add additional IGs if necessary.
2.  Define your custom first-class extension. For syntax and more examples refer to [this page](https://github.com/zen-lang/zen).

    ```
    {ns my-zen-project
     import #{zen.fhir
              hl7-fhir-r4-core.string
              hl7-fhir-r4-core.dateTime
              hl7-fhir-r4-core.uri}

     MyPatient
     {:zen/tags #{zen/schema zen.fhir/profile-schema}
      :zen/desc "Patient resource schema with first-class extension definition examples"
      :zen.fhir/version "0.5.20"
      :confirms #{zen.fhir/Resource}
      :zen.fhir/type "Patient"
      :zen.fhir/profileUri "urn:profile:MyPatientProfile"
      :type zen/map
      
      :keys {:meta {:type zen/map
                    :keys {:tenant-id
                           {:confirms #{hl7-fhir-r4-core.string/schema}
                            :zen/desc "Patient.meta.tenant-id first-class extension"
                            :fhir/extensionUri "http://tenant-id-extension-url"}}}

             :form {:type zen/vector
                    :zen/desc "Patient.form.[*] array first-class extension"
                    :every {:confirms #{hl7-fhir-r4-core.uri/schema}
                            :fhir/extensionUri "http://patient-form-url"}}

             :info {:type zen/map
                    :zen/desc "Patient.info nested first-class extension"
                    :fhir/extensionUri "http://patient-info"
                    
                    :keys {:registration {:confirms #{hl7-fhir-r4-core.dateTime/schema}
                                          :zen/desc "Patient.info.registration 
                                                     extension.url deduced from key"}
                           
                           :referral {:confirms #{hl7-fhir-r4-core.uri/schema}
                                      :fhir/extensionUri "http://patient-info-referral"
                                      :zen/desc "Patient.info.referral
                                                 extension.url is specified"}}}}}
    ```
3.  Fix `:zen.fhir/version` errors if needed

    To fix this error, provide IG bundle with the matching zen FHIR version.

    Error message example:

    ```
    Expected '0.5.8', got '0.4.9'
    path: [:zen.fhir/version]
    ```
4. Define `:fhir/extensionUri` property value. \
   `:fhir/extensionUri` "http://tenant-id-extension-url" is an equivalent of the `extensionUrl` field of the [Attribute resource](first-class-extensions-using-zen.md#define-new-extension) and is used to form FHIR extension.
5. Replace plain zen type with FHIR type in your extensions. \
   I.e. use `:confirms #{hl7-fhir-r4-core.string/schema}` instead of `:type zen/string`.
