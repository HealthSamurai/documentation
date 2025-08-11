# History of Present Illness Section

OID: 1.3.6.1.4.1.19376.1.5.3.1.3.4

LOINCs: #{"10164-2"}

Alias: history-of-present-illness

Entries Required: N/A

Internal ID: HistoryofPresentIllnessSection

[IG Link](https://www.hl7.org/ccdasearch/templates/1.3.6.1.4.1.19376.1.5.3.1.3.4.html)

## History of present illness

```json
{
  "code" : {
    "coding" : [ {
      "code" : "11506-3",
      "system" : "http://loinc.org",
      "display" : "Progress note"
    } ]
  },
  "note" : [ {
    "text" : "SOME USEFUL NOTES"
  } ],
  "effective" : {
    "dateTime" : "2023-04-24T14:30:00-04:00"
  },
  "performer" : [ {
    "practitioner" : {
      "name" : [ {
        "given" : [ "WTF" ],
        "family" : "MAYR",
        "use" : "official",
        "suffix" : [ " DDS" ]
      } ],
      "id" : "2.16.840.1.113883.4.6",
      "identifier" : [ {
        "value" : "322045066",
        "system" : "urn:oid:1.2.840.114350.1.13.232.2.7.1.1133"
      }, {
        "value" : "77398",
        "system" : "urn:oid:1.2.840.114350.1.13.232.2.7.2.836982"
      } ],
      "resourceType" : "Practitioner"
    },
    "specialty" : [ {
      "text" : "Dentistry, General"
    } ],
    "id" : "2.16.840.1.113883.4.6",
    "organization" : {
      "name" : "ADCare Health System",
      "address" : [ {
        "line" : [ "000 1st Ave" ],
        "use" : "work",
        "city" : "New York",
        "state" : "NY",
        "postalCode" : "11111",
        "country" : "US"
      } ],
      "id" : "2.16.840.1.113883.4.2",
      "identifier" : [ {
        "value" : "20100",
        "system" : "urn:oid:1.2.840.114350.1.13.232.2.7.2.688879"
      } ],
      "resourceType" : "Organization"
    },
    "resourceType" : "PractitionerRole",
    "period" : {
      "start" : "2023-05-01T17:09:30-04:00"
    }
  } ],
  "status" : "final",
  "resourceType" : "Observation"
}
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.4"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="10164-2"/>
   <title>History of Present Illness Section</title>
   <text>
      <paragraph>
         <caption>WTF MAYR - 04/24/2023 6:30PM UTC</caption>SOME USEFUL NOTES</paragraph>
   </text>
   <entry>
      <act classCode="ACT" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.202" extension="2016-11-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.202"/>
         <code code="34109-9"
                xsi:type="CE"
                codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="LOINC"
                displayName="Note">
            <translation codeSystem="2.16.840.1.113883.6.1"
                          codeSystemName="LOINC"
                          code="11506-3"
                          displayName="Progress note"/>
         </code>
         <text>SOME USEFUL NOTES</text>
         <statusCode code="completed"/>
         <effectiveTime value="20230424143000-0400"/>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20230501170930-0400"/>
            <assignedAuthor>
               <id root="e9e2ccb4-e681-2e3f-32d6-eceeecb985ec"/>
               <id extension="322045066" root="1.2.840.114350.1.13.232.2.7.1.1133"/>
               <id extension="77398" root="1.2.840.114350.1.13.232.2.7.2.836982"/>
               <code>
                  <originalText>Dentistry, General</originalText>
               </code>
               <addr nullFlavor="UNK"/>
               <telecom nullFlavor="UNK"/>
               <assignedPerson>
                  <name use="L">
                     <given>WTF</given>
                     <family>MAYR</family>
                     <suffix> DDS</suffix>
                  </name>
               </assignedPerson>
               <representedOrganization>
                  <id root="6819ad66-611c-5144-05d3-d612ec8d5c46"/>
                  <id extension="20100" root="1.2.840.114350.1.13.232.2.7.2.688879"/>
                  <name>ADCare Health System</name>
                  <telecom nullFlavor="UNK"/>
                  <addr use="WP">
                     <country>US</country>
                     <state>NY</state>
                     <city>New York</city>
                     <postalCode>11111</postalCode>
                     <streetAddressLine>000 1st Ave</streetAddressLine>
                  </addr>
               </representedOrganization>
            </assignedAuthor>
         </author>
      </act>
   </entry>
</section>
```