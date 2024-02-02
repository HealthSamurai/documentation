# Default Section Rules

OID: N/A

LOINCs: #{}

Alias: default

Entries Required: N/A

Internal ID: default

## sample-observation

```json
{
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2025-11-01"
  },
  "id" : "5c500351-d0d8-823a-3ae2-71785f11dc2b",
  "code" : {
    "text" : "Some planned observation"
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  },
  "issued" : "2023-12-15T17:20:41-05:00",
  "performer" : [ {
    "resourceType" : "PractitionerRole",
    "id" : "2.16.840.1.113883.4.6",
    "organization" : {
      "name" : "ABC Clinic",
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1-333-04-01",
        "use" : "work"
      } ],
      "address" : [ {
        "line" : [ "95 SOMETHING RD" ],
        "use" : "work",
        "city" : "CITYVILLE",
        "state" : "NY",
        "postalCode" : "01010",
        "country" : "USA"
      } ],
      "id" : "2.16.840.1.113883.4.2",
      "identifier" : [ {
        "value" : "10",
        "system" : "urn:oid:1.2.840.114350.1.13.591.2.7.2.696570"
      } ],
      "resourceType" : "Organization"
    },
    "period" : {
      "start" : "2023-12-15T17:20:41-05:00"
    },
    "practitioner" : {
      "id" : "2.16.840.1.113883.4.6",
      "resourceType" : "Practitioner"
    }
  } ]
}
```

C-CDA Equivalent:
```xml
<section>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"/>
   <title>Default Section Rules</title>
   <text>Default Section Rules</text>
   <entry>
      <observation moodCode="EVN" classCode="OBS">
         <id root="5c500351-d0d8-823a-3ae2-71785f11dc2b"/>
         <code>
            <originalText>Some planned observation</originalText>
         </code>
         <statusCode code="completed"/>
         <effectiveTime value="20251101"/>
         <author>
            <templateId root="2.16.840.1.113883.10.20.22.4.119"/>
            <time value="20231215172041-0500"/>
            <assignedAuthor>
               <id root="e9e2ccb4-e681-2e3f-32d6-eceeecb985ec"/>
               <addr nullFlavor="UNK"/>
               <telecom nullFlavor="UNK"/>
               <assignedPerson>
                  <name nullFlavor="UNK"/>
               </assignedPerson>
               <representedOrganization>
                  <id root="6819ad66-611c-5144-05d3-d612ec8d5c46"/>
                  <id extension="10" root="1.2.840.114350.1.13.591.2.7.2.696570"/>
                  <name>ABC Clinic</name>
                  <telecom value="+1-333-04-01" use="WP"/>
                  <addr use="WP">
                     <country>USA</country>
                     <state>NY</state>
                     <city>CITYVILLE</city>
                     <postalCode>01010</postalCode>
                     <streetAddressLine>95 SOMETHING RD</streetAddressLine>
                  </addr>
               </representedOrganization>
            </assignedAuthor>
         </author>
      </observation>
   </entry>
</section>
```

## sample-document-reference

```json
{
  "resourceType" : "DocumentReference",
  "id" : "1a23f7da-2aae-b35b-435c-3fc7559cfc4c",
  "content" : [ {
    "attachment" : {
      "data" : "/9j/4AAQSkZJRgABAAEAYABhAAD",
      "contentType" : "image/jpeg"
    }
  } ],
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}
```

C-CDA Equivalent:
```xml
<section>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"/>
   <title>Default Section Rules</title>
   <text>Default Section Rules</text>
   <entry>
      <observationMedia moodCode="EVN" classCode="DGIMG">
         <id root="1a23f7da-2aae-b35b-435c-3fc7559cfc4c"/>
         <value representation="B64" mediaType="image/jpeg">/9j/4AAQSkZJRgABAAEAYABhAAD</value>
      </observationMedia>
   </entry>
</section>
```