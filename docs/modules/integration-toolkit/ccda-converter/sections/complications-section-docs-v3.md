# Complications Section (/V3)

OID: 2.16.840.1.113883.10.20.22.2.37

LOINCs: #{"55109-3"}

Alias: complications

Entries Required: N/A

Internal ID: ComplicationsSectionV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.37.html)

## sample-problem

```json
{
  "onset" : {
    "dateTime" : "2014-02-27"
  },
  "category" : [ {
    "coding" : [ {
      "system" : "http://terminology.hl7.org/CodeSystem/condition-category",
      "code" : "problem-list-item",
      "display" : "Problem List Item"
    } ]
  } ],
  "resourceType" : "Condition",
  "recordedDate" : "2014-03-02T12:45:36Z",
  "id" : "9d841140-d92b-0f69-daa2-f1784653dec8",
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
      "id" : "cef8b367-67e0-5bc5-3260-d66139c4b3ab",
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
    "id" : "bbdb2030-cff6-2837-723f-2b8f8d0bb51f",
    "resourceType" : "PractitionerRole",
    "period" : {
      "start" : "2014-03-02T12:45:36Z"
    }
  },
  "code" : {
    "text" : "Community Acquired Pneumonia",
    "coding" : [ {
      "code" : "385093006",
      "display" : "Community Acquired Pneumonia",
      "system" : "http://snomed.info/sct"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.37" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.37"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="55109-3"/>
   <title>Complications Section (V3)</title>
   <text>
      <table border="1" width="100%">
         <caption>Complications</caption>
         <thead>
            <tr>
               <th>Complication</th>
               <th>Noted Date</th>
               <th>Diagnosed Date</th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="3">No records</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <observation moodCode="EVN" classCode="OBS">
         <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
         <id root="9d841140-d92b-0f69-daa2-f1784653dec8"/>
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
               <id root="bbdb2030-cff6-2837-723f-2b8f8d0bb51f"/>
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
