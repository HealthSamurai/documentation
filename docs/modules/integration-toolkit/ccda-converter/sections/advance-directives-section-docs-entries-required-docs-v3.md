# Advance Directives Section (/entries required) (/V3)

OID: 2.16.840.1.113883.10.20.22.2.21.1

LOINCs: #{"42348-3"}

Alias: advance-directives

Entries Required: true

Internal ID: AdvanceDirectivesSectentriesre

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.21.1.html)

## sample1
Advance Directives Sample


```json
[ {
  "resourceType" : "Observation",
  "hasMember" : [ {
    "value" : {
      "CodeableConcept" : {
        "text" : "Do not resuscitate",
        "coding" : [ {
          "code" : "304253006",
          "display" : "Not for resuscitation",
          "system" : "http://snomed.info/sct"
        } ]
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "Period" : {
        "start" : "2011-02-13"
      }
    },
    "id" : "9b54c3c9-1673-49c7-aef9-b037ed72ed27",
    "code" : {
      "text" : "Resuscitation status",
      "coding" : [ {
        "code" : "304251008",
        "display" : "Resuscitation status",
        "system" : "http://snomed.info/sct"
      }, {
        "code" : "75320-2",
        "display" : "Advance Directive",
        "system" : "http://loinc.org"
      } ]
    },
    "issued" : "2013-08-01T12:35:00-08:00",
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    },
    "performer" : [ {
      "practitioner" : {
        "name" : [ {
          "given" : [ "Nurse" ],
          "family" : "Nightingale",
          "use" : "official",
          "suffix" : [ "RN" ]
        } ],
        "id" : "20cf14fb-b65c-4c8c-a54d-b0cca834c18c",
        "resourceType" : "Practitioner"
      },
      "specialty" : [ {
        "text" : "Nursing Service Providers; Registered Nurse",
        "coding" : [ {
          "code" : "163W00000X",
          "display" : "Nursing Service Providers; Registered Nurse",
          "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
        } ]
      } ],
      "id" : "20cf14fb-b65c-4c8c-a54d-b0cca834c18c",
      "organization" : {
        "name" : "Good Health Hospital",
        "id" : "2.16.840.1.113883.19.5",
        "resourceType" : "Organization"
      },
      "resourceType" : "PractitionerRole",
      "period" : {
        "start" : "2013-08-01T12:35:00-08:00"
      }
    } ]
  } ],
  "id" : "f0691552-2bbc-ce59-e297-804fd2349912",
  "code" : {
    "text" : "SOME USEFUL ADVANCE DIRECTIVE NOTES",
    "coding" : [ {
      "code" : "45473-6",
      "display" : "advance directive - living will",
      "system" : "http://loinc.org"
    } ]
  },
  "status" : "final",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.21" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.21"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="42348-3"/>
   <title>Advance Directives Section (entries required) (V3)</title>
   <text>Advance Directives Section (entries required) (V3)</text>
   <entry>
      <organizer classCode="CLUSTER" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.108" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.108"/>
         <id root="f0691552-2bbc-ce59-e297-804fd2349912"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="advance directive - living will"
                code="45473-6">
            <originalText>SOME USEFUL ADVANCE DIRECTIVE NOTES</originalText>
         </code>
         <statusCode code="completed"/>
         <component>
            <observation moodCode="EVN" classCode="OBS">
               <templateId root="2.16.840.1.113883.10.20.22.4.48" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.48"/>
               <id root="9b54c3c9-1673-49c7-aef9-b037ed72ed27"/>
               <code codeSystem="2.16.840.1.113883.6.96"
                      codeSystemName="http://snomed.info/sct"
                      displayName="Resuscitation status"
                      code="304251008">
                  <originalText>Resuscitation status</originalText>
                  <translation codeSystem="2.16.840.1.113883.6.1"
                                codeSystemName="http://loinc.org"
                                displayName="Advance Directive"
                                code="75320-2"/>
               </code>
               <statusCode code="completed"/>
               <effectiveTime>
                  <low value="20110213"/>
                  <high nullFlavor="UNK"/>
               </effectiveTime>
               <value codeSystem="2.16.840.1.113883.6.96"
                       codeSystemName="http://snomed.info/sct"
                       displayName="Not for resuscitation"
                       code="304253006"
                       xsi:type="CD">
                  <originalText>Do not resuscitate</originalText>
               </value>
               <author>
                  <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
                  <time value="20130801123500-0800"/>
                  <assignedAuthor>
                     <id root="20cf14fb-b65c-4c8c-a54d-b0cca834c18c"/>
                     <code codeSystem="2.16.840.1.113883.6.101"
                            codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                            displayName="Nursing Service Providers; Registered Nurse"
                            code="163W00000X">
                        <originalText>Nursing Service Providers; Registered Nurse</originalText>
                     </code>
                     <addr nullFlavor="UNK"/>
                     <telecom nullFlavor="UNK"/>
                     <assignedPerson>
                        <name use="L">
                           <given>Nurse</given>
                           <family>Nightingale</family>
                           <suffix>RN</suffix>
                        </name>
                     </assignedPerson>
                     <representedOrganization>
                        <id root="f3fca3ed-a178-ca97-912a-c678d404ca93"/>
                        <name>Good Health Hospital</name>
                        <telecom nullFlavor="UNK"/>
                     </representedOrganization>
                  </assignedAuthor>
               </author>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
```
