# Past Medical History (/V3)

OID: 2.16.840.1.113883.10.20.22.2.20

LOINCs: #{"11348-0"}

Alias: past-medical-history

Entries Required: N/A

Internal ID: PastMedicalHistoryV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.20.html)

## sample1

```json
{
  "id" : "ea3b4865-890b-5db8-997e-538aa8117eb3",
  "code" : {
    "text" : "Community Acquired Pneumonia",
    "coding" : [ {
      "code" : "385093006",
      "display" : "Community Acquired Pneumonia",
      "system" : "http://snomed.info/sct"
    } ]
  },
  "onset" : {
    "dateTime" : "2014-02-27"
  },
  "recorder" : {
    "practitioner" : {
      "name" : [ {
        "given" : [ "Heartly" ],
        "family" : "Sixer",
        "use" : "official",
        "suffix" : [ "MD" ]
      } ],
      "address" : [ {
        "line" : [ "6666 StreetName St." ],
        "city" : "Silver Spring",
        "state" : "MD",
        "postalCode" : "20901",
        "country" : "US"
      } ],
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(301)666-6666",
        "use" : "work"
      } ],
      "id" : "b6a1ceb6-f75b-615b-8d36-06a4f488ea04",
      "identifier" : [ {
        "value" : "66666",
        "system" : "http://hl7.org/fhir/sid/us-npi"
      } ],
      "resourceType" : "Practitioner"
    },
    "specialty" : [ {
      "text" : "Cardiovascular Disease",
      "coding" : [ {
        "code" : "207RC0000X",
        "display" : "Cardiovascular Disease",
        "system" : "http://hl7.org/fhir/ValueSet/provider-taxonomy"
      } ]
    } ],
    "id" : "9f2b039d-536f-964f-c78f-202c1ffca04a",
    "resourceType" : "PractitionerRole",
    "period" : {
      "start" : "2014-03-02T12:45:36Z"
    }
  },
  "recordedDate" : "2014-03-02T12:45:36Z",
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "resourceType" : "Condition"
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.20" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.20"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="11348-0"/>
   <title>Past Medical History (V3)</title>
   <text>
      <table border="1" width="100%">
         <caption>Past Medical History</caption>
         <thead>
            <tr>
               <th>Problem</th>
               <th>Noted Date</th>
               <th>Diagnosed Date</th>
               <th>Resolved Date</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>Community Acquired Pneumonia</td>
               <td>02/27/2014 12:00AM UTC</td>
               <td>03/02/2014 12:45PM UTC</td>
               <td>~</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <observation moodCode="EVN" classCode="OBS">
         <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
         <id root="ea3b4865-890b-5db8-997e-538aa8117eb3"/>
         <code xsi:type="CD"
                code="55607006"
                displayName="Problem"
                codeSystemName="SNOMED-CT"
                codeSystem="2.16.840.1.113883.6.96">
            <translation nullFlavor="UNK"/>
         </code>
         <statusCode code="completed"/>
         <effectiveTime>
            <low value="20140227"/>
            <high nullFlavor="NI"/>
         </effectiveTime>
         <value codeSystem="2.16.840.1.113883.6.96"
                 codeSystemName="http://snomed.info/sct"
                 displayName="Community Acquired Pneumonia"
                 code="385093006"
                 xsi:type="CD">
            <originalText>Community Acquired Pneumonia</originalText>
         </value>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20140302124536-0000"/>
            <assignedAuthor>
               <id root="9f2b039d-536f-964f-c78f-202c1ffca04a"/>
               <id extension="66666" root="2.16.840.1.113883.4.6"/>
               <code codeSystem="2.16.840.1.113883.6.101"
                      codeSystemName="http://hl7.org/fhir/ValueSet/provider-taxonomy"
                      displayName="Cardiovascular Disease"
                      code="207RC0000X">
                  <originalText>Cardiovascular Disease</originalText>
               </code>
               <addr>
                  <country>US</country>
                  <state>MD</state>
                  <city>Silver Spring</city>
                  <postalCode>20901</postalCode>
                  <streetAddressLine>6666 StreetName St.</streetAddressLine>
               </addr>
               <telecom value="+1(301)666-6666" use="WP"/>
               <assignedPerson>
                  <name use="L">
                     <given>Heartly</given>
                     <family>Sixer</family>
                     <suffix>MD</suffix>
                  </name>
               </assignedPerson>
            </assignedAuthor>
         </author>
      </observation>
   </entry>
</section>
```
