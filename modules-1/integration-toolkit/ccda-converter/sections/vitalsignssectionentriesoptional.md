# Vital Signs Section (entries optional) (V3)

OID: 2.16.840.1.113883.10.20.22.2.4

LOINCs: #{"8716-3"}

Alias: vital-signs

Entries Required: N/A

Internal ID: VitalSignsSectionentriesoptional

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.4.html)

## sample1

```json
[ {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 120,
      "unit" : "mm[Hg]"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "fdbd831b-5919-4f06-9467-76b07022f8e8",
  "code" : {
    "text" : "SYSTOLIC BLOOD PRESSURE",
    "coding" : [ {
      "code" : "8480-6",
      "display" : "SYSTOLIC BLOOD PRESSURE",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 80,
      "unit" : "mm[Hg]"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "553f3f45-9046-4659-b3e7-5de904003550",
  "code" : {
    "text" : "DIASTOLIC BLOOD PRESSURE",
    "coding" : [ {
      "code" : "8462-4",
      "display" : "DIASTOLIC BLOOD PRESSURE",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 80,
      "unit" : "/min"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "77bfe276-a1dd-4372-9072-e603905acc07",
  "code" : {
    "text" : "HEART RATE",
    "coding" : [ {
      "code" : "8867-4",
      "display" : "HEART RATE",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 37.2,
      "unit" : "Cel"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "24faa204-db62-4610-864f-cb50b650d0fa",
  "code" : {
    "text" : "BODY TEMPERATURE",
    "coding" : [ {
      "code" : "8310-5",
      "display" : "BODY TEMPERATURE",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 18,
      "unit" : "/min"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "79f25395-8ec6-488b-8c05-becc97f79995",
  "code" : {
    "text" : "RESPIRATORY RATE",
    "coding" : [ {
      "code" : "9279-1",
      "display" : "RESPIRATORY RATE",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 170.2,
      "unit" : "cm"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "6d3fa9f8-6049-41bd-b0c3-b0196bb6bd37",
  "code" : {
    "text" : "HEIGHT",
    "coding" : [ {
      "code" : "8302-2",
      "display" : "HEIGHT",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 108.863,
      "unit" : "kg"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "2594e631-2189-4e72-9dd1-d6769ee2a7be",
  "code" : {
    "text" : "WEIGHT",
    "coding" : [ {
      "code" : "29463-7",
      "display" : "WEIGHT",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 37.58,
      "unit" : "kg/m2"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "5858e765-2ffe-413f-9197-260f2c6e7aa8",
  "code" : {
    "text" : "BODY MASS INDEX",
    "coding" : [ {
      "code" : "39156-5",
      "display" : "BODY MASS INDEX",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
}, {
  "category" : [ {
    "coding" : [ {
      "code" : "vital-signs",
      "display" : "Vital Signs",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "value" : {
    "Quantity" : {
      "value" : 98,
      "unit" : "%"
    }
  },
  "resourceType" : "Observation",
  "status" : "final",
  "effective" : {
    "dateTime" : "2014-05-20T19:36:05-05:00"
  },
  "id" : "4ce6046c-f6e3-41b0-91fc-2d5325f2bbc3",
  "code" : {
    "text" : "OXYGEN SATURATION",
    "coding" : [ {
      "code" : "2710-2",
      "display" : "OXYGEN SATURATION",
      "system" : "http://loinc.org"
    } ]
  },
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.4" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.4"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="HTTP://LOINC.ORG"
          displayName="display string"
          code="8716-3"/>
   <title>Vital Signs Section (entries optional) (V3)</title>
   <text>
      <table border="1" width="100%">
         <thead>
            <tr>
               <td>Vital sign</td>
               <td>Date</td>
               <td>Value and units</td>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td>SYSTOLIC BLOOD PRESSURE (8480-6)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>120 mm[Hg]</td>
            </tr>
            <tr>
               <td>DIASTOLIC BLOOD PRESSURE (8462-4)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>80 mm[Hg]</td>
            </tr>
            <tr>
               <td>HEART RATE (8867-4)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>80 /min</td>
            </tr>
            <tr>
               <td>BODY TEMPERATURE (8310-5)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>37.2 Cel</td>
            </tr>
            <tr>
               <td>RESPIRATORY RATE (9279-1)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>18 /min</td>
            </tr>
            <tr>
               <td>HEIGHT (8302-2)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>170.2 cm</td>
            </tr>
            <tr>
               <td>WEIGHT (29463-7)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>108.863 kg</td>
            </tr>
            <tr>
               <td>BODY MASS INDEX (39156-5)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>37.58 kg/m2</td>
            </tr>
            <tr>
               <td>OXYGEN SATURATION (2710-2)</td>
               <td>2014-05-20T19:36:05-05:00</td>
               <td>98 %</td>
            </tr>
         </tbody>
      </table>
   </text>
   <entry>
      <organizer classCode="CLUSTER" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.26" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.26"/>
         <id root="91ca8578-059d-4853-a0d7-601f62742d48"/>
         <code code="46680005"
                displayName="Vital Signs"
                codeSystem="2.16.840.1.113883.6.96"
                codeSystemName="SNOMED CT">
            <translation code="74728-7"
                          displayName="Vital signs, weight, height, head circumference, oximetry, BMI, and BSA panel"
                          codeSystem="2.16.840.1.113883.6.1"
                          codeSystemName="LOINC"/>
         </code>
         <statusCode code="completed"/>
         <effectiveTime>
            <low value="20140520193605-0500"/>
            <high value="20140520193605-0500"/>
         </effectiveTime>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="fdbd831b-5919-4f06-9467-76b07022f8e8"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="SYSTOLIC BLOOD PRESSURE"
                      code="8480-6"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="120" unit="mm[Hg]" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="553f3f45-9046-4659-b3e7-5de904003550"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="DIASTOLIC BLOOD PRESSURE"
                      code="8462-4"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="80" unit="mm[Hg]" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="77bfe276-a1dd-4372-9072-e603905acc07"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="HEART RATE"
                      code="8867-4"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="80" unit="/min" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="24faa204-db62-4610-864f-cb50b650d0fa"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="BODY TEMPERATURE"
                      code="8310-5"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="37.2" unit="Cel" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="79f25395-8ec6-488b-8c05-becc97f79995"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="RESPIRATORY RATE"
                      code="9279-1"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="18" unit="/min" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="6d3fa9f8-6049-41bd-b0c3-b0196bb6bd37"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="HEIGHT"
                      code="8302-2"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="170.2" unit="cm" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="2594e631-2189-4e72-9dd1-d6769ee2a7be"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="WEIGHT"
                      code="29463-7"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="108.863" unit="kg" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="5858e765-2ffe-413f-9197-260f2c6e7aa8"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="BODY MASS INDEX"
                      code="39156-5"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="37.58" unit="kg/m2" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
               <id root="4ce6046c-f6e3-41b0-91fc-2d5325f2bbc3"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="HTTP://LOINC.ORG"
                      displayName="OXYGEN SATURATION"
                      code="2710-2"/>
               <statusCode code="completed"/>
               <effectiveTime value="20140520193605-0500"/>
               <value value="98" unit="%" xsi:type="PQ"/>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
```