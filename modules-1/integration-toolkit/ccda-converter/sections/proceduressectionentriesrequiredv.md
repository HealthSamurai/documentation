# Procedures Section (entries required) (V2)

OID: 2.16.840.1.113883.10.20.22.2.7.1

LOINCs: #{"47519-4"}

Alias: procedures

Entries Required: N/A

Internal ID: ProceduresSectionentriesrequiredV

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.7.1.html)

## Procedure example.
This is a typical procedure resource sample.
          
* :performed field contains information about start and end dates of procedure.
          
* :bodySite field contains information about body site of procedure.
          
* :status field contains information about status of procedure.
          

 In this case it is used to distinguish procedure from other acts like observation or even any act in common.


```json
{
  "extension" : [ {
    "url" : "rim-class",
    "value" : {
      "code" : "procedure"
    }
  } ],
  "id" : "64af26d5-88ef-4169-ba16-c6ef16a1824f",
  "code" : {
    "coding" : [ {
      "code" : "6025007",
      "display" : "Laparoscopic appendectomy",
      "system" : "http://snomed.info/sct"
    }, {
      "code" : "44970",
      "display" : "Laparoscopic Appendectomy",
      "system" : "http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
    }, {
      "code" : "0DTJ4ZZ",
      "display" : "Resection of Appendix, Percutaneous Endoscopic Approach",
      "system" : "2.16.840.1.113883.6.4"
    }, {
      "code" : "47.01",
      "display" : "Laparoscopic appendectomy",
      "system" : "http://hl7.org/fhir/sid/icd-9-cm"
    } ]
  },
  "performed" : {
    "Period" : {
      "start" : "2014-02-03T09:22:05-07:00",
      "end" : "2014-02-03T11:15:14-07:00"
    }
  },
  "status" : "completed",
  "bodySite" : [ {
    "coding" : [ {
      "code" : "181255000",
      "display" : "Entire Appendix",
      "system" : "http://snomed.info/sct"
    } ]
  } ],
  "resourceType" : "Procedure",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.7.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.7.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="HTTP://LOINC.ORG"
          displayName="display string"
          code="47519-4"/>
   <title>Procedures Section (entries required) (V2)</title>
   <text>Procedures Section (entries required) (V2)</text>
   <entry>
      <procedure moodCode="EVN" classCode="PROC">
         <templateId root="2.16.840.1.113883.10.20.22.4.14" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.14"/>
         <id root="64af26d5-88ef-4169-ba16-c6ef16a1824f"/>
         <code codeSystem="2.16.840.1.113883.6.96"
                codeSystemName="HTTP://SNOMED.INFO/SCT"
                displayName="Laparoscopic appendectomy"
                code="6025007">
            <translation codeSystem="2.16.840.1.113883.6.12"
                          codeSystemName="HTTP://TERMINOLOGY.HL7.ORG/VALUESET/V3-ACTENCOUNTERCODE"
                          displayName="Laparoscopic Appendectomy"
                          code="44970"/>
            <translation codeSystem="2.16.840.1.113883.6.4"
                          codeSystemName="2.16.840.1.113883.6.4"
                          displayName="Resection of Appendix, Percutaneous Endoscopic Approach"
                          code="0DTJ4ZZ"/>
            <translation codeSystem="2.16.840.1.113883.6.104"
                          codeSystemName="HTTP://HL7.ORG/FHIR/SID/ICD-9-CM"
                          displayName="Laparoscopic appendectomy"
                          code="47.01"/>
         </code>
         <statusCode code="completed"/>
         <effectiveTime>
            <low value="20140203092205-0700"/>
            <high value="20140203111514-0700"/>
         </effectiveTime>
         <methodCode nullFlavor="UNK"/>
         <targetSiteCode codeSystem="2.16.840.1.113883.6.96"
                          codeSystemName="HTTP://SNOMED.INFO/SCT"
                          displayName="Entire Appendix"
                          code="181255000"/>
      </procedure>
   </entry>
</section>
```

## Procedure Act example.
Th converter rules don't use :extension field to distinguish Procedure Act from other acts, but you can add related rules to do it.
          
In this case :extension field is used to distinguish more common procedure act like patient education.


```json
{
  "extension" : [ {
    "url" : "rim-class",
    "value" : {
      "code" : "act"
    }
  } ],
  "id" : "9c0f070c-2e9e-4be1-a5b5-ff6d0f68123c",
  "code" : {
    "coding" : [ {
      "code" : "61310001",
      "display" : "Nutrition education",
      "system" : "http://snomed.info/sct"
    }, {
      "code" : "97802",
      "display" : "Medical nutrition therapy; initial",
      "system" : "http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
    }, {
      "code" : "S9470",
      "display" : "Nutritional counseling, diet",
      "system" : "2.16.840.1.113883.6.13"
    }, {
      "code" : "V65.3",
      "display" : "Dietary surveillance and counseling",
      "system" : "2.16.840.1.113883.6.103"
    }, {
      "code" : "Z71.3",
      "display" : "Dietary counseling and surveillance",
      "system" : "http://hl7.org/fhir/sid/icd-10"
    } ]
  },
  "performed" : {
    "dateTime" : "2014-03-29T10:45:13-05:00"
  },
  "status" : "completed",
  "resourceType" : "Procedure",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

The section contains an Act entry converted from the input Procedure resource.
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.7.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.7.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="HTTP://LOINC.ORG"
          displayName="display string"
          code="47519-4"/>
   <title>Procedures Section (entries required) (V2)</title>
   <text>Procedures Section (entries required) (V2)</text>
   <entry>
      <act classCode="ACT" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.12" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.12"/>
         <id root="9c0f070c-2e9e-4be1-a5b5-ff6d0f68123c"/>
         <code codeSystem="2.16.840.1.113883.6.96"
                codeSystemName="HTTP://SNOMED.INFO/SCT"
                displayName="Nutrition education"
                code="61310001">
            <translation codeSystem="2.16.840.1.113883.6.12"
                          codeSystemName="HTTP://TERMINOLOGY.HL7.ORG/VALUESET/V3-ACTENCOUNTERCODE"
                          displayName="Medical nutrition therapy; initial"
                          code="97802"/>
            <translation codeSystem="2.16.840.1.113883.6.13"
                          codeSystemName="2.16.840.1.113883.6.13"
                          displayName="Nutritional counseling, diet"
                          code="S9470"/>
            <translation codeSystem="2.16.840.1.113883.6.103"
                          codeSystemName="2.16.840.1.113883.6.103"
                          displayName="Dietary surveillance and counseling"
                          code="V65.3"/>
            <translation codeSystem="2.16.840.1.113883.6.90"
                          codeSystemName="HTTP://HL7.ORG/FHIR/SID/ICD-10"
                          displayName="Dietary counseling and surveillance"
                          code="Z71.3"/>
         </code>
         <statusCode code="completed"/>
         <effectiveTime value="20140329104513-0500"/>
      </act>
   </entry>
</section>
```

## Procedure Observation example.
Like the previous examples, this is a typical Procedure resource sample.


```json
{
  "extension" : [ {
    "url" : "rim-class",
    "value" : {
      "code" : "observation"
    }
  } ],
  "id" : "c03e5445-af1b-4911-a419-e2782f21448c",
  "code" : {
    "coding" : [ {
      "code" : "268400002",
      "display" : "12 lead ECG",
      "system" : "http://snomed.info/sct"
    }, {
      "code" : "93000",
      "display" : "Electrocardiogram, complete",
      "system" : "http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
    }, {
      "code" : "G8704",
      "display" : "12-Lead Electrocardiogram (Ecg) Performed",
      "system" : "2.16.840.1.113883.6.13"
    }, {
      "code" : "89.52",
      "display" : "Electrocardiogram",
      "system" : "http://hl7.org/fhir/sid/icd-9-cm"
    }, {
      "code" : "4A02X4Z",
      "display" : "Measurement of Cardiac Electrical Activity, External Approach",
      "system" : "2.16.840.1.113883.6.4"
    } ]
  },
  "performed" : {
    "dateTime" : "2014-03-29T09:15:22-12:00"
  },
  "status" : "completed",
  "resourceType" : "Procedure",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.7.1" extension="2014-06-09"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.7.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="HTTP://LOINC.ORG"
          displayName="display string"
          code="47519-4"/>
   <title>Procedures Section (entries required) (V2)</title>
   <text>Procedures Section (entries required) (V2)</text>
   <entry>
      <observation classCode="OBS" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.13" extension="2014-06-09"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.13"/>
         <id root="c03e5445-af1b-4911-a419-e2782f21448c"/>
         <code codeSystem="2.16.840.1.113883.6.96"
                codeSystemName="HTTP://SNOMED.INFO/SCT"
                displayName="12 lead ECG"
                code="268400002">
            <translation codeSystem="2.16.840.1.113883.6.12"
                          codeSystemName="HTTP://TERMINOLOGY.HL7.ORG/VALUESET/V3-ACTENCOUNTERCODE"
                          displayName="Electrocardiogram, complete"
                          code="93000"/>
            <translation codeSystem="2.16.840.1.113883.6.13"
                          codeSystemName="2.16.840.1.113883.6.13"
                          displayName="12-Lead Electrocardiogram (Ecg) Performed"
                          code="G8704"/>
            <translation codeSystem="2.16.840.1.113883.6.104"
                          codeSystemName="HTTP://HL7.ORG/FHIR/SID/ICD-9-CM"
                          displayName="Electrocardiogram"
                          code="89.52"/>
            <translation codeSystem="2.16.840.1.113883.6.4"
                          codeSystemName="2.16.840.1.113883.6.4"
                          displayName="Measurement of Cardiac Electrical Activity, External Approach"
                          code="4A02X4Z"/>
         </code>
         <statusCode code="completed"/>
         <effectiveTime value="20140329091522-1200"/>
         <value xsi:type="CD"/>
      </observation>
   </entry>
</section>
```