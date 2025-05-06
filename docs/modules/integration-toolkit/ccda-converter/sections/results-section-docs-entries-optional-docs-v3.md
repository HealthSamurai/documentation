# Results Section (entries optional) (V3)

OID: 2.16.840.1.113883.10.20.22.2.3

LOINCs: #{"30954-2"}

Alias: results

Entries Required: false

Internal ID: ResultsSectionentriesoptionalV3

[IG Link](https://www.hl7.org/ccdasearch/templates/2.16.840.1.113883.10.20.22.2.3.html)

## sample2

```json
[ {
  "category" : [ {
    "coding" : [ {
      "code" : "laboratory",
      "display" : "Laboratory",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "hasMember" : [ {
    "value" : {
      "Quantity" : {
        "value" : 140,
        "unit" : "mmol/L"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "d8fe6100-c31a-4b8c-b97d-57a6bd415bf7",
    "code" : {
      "text" : "Na",
      "coding" : [ {
        "code" : "2947-0",
        "display" : "Sodium [Moles/​volume] in Blood",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  }, {
    "value" : {
      "Quantity" : {
        "value" : 4.2,
        "unit" : "mmol/L"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "3ee6ac58-d22c-4416-9acc-c1c75c1c2c91",
    "code" : {
      "text" : "K",
      "coding" : [ {
        "code" : "6298-4",
        "display" : "Potassium [Moles/​volume] in Blood",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  }, {
    "value" : {
      "Quantity" : {
        "value" : 98,
        "unit" : "mmol/L"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "0431a3ec-8ad6-49da-9de1-0a8704b7609d",
    "code" : {
      "text" : "Cl",
      "coding" : [ {
        "code" : "2069-3",
        "display" : "Chloride [Moles/volume] in Blood",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  }, {
    "value" : {
      "Quantity" : {
        "value" : 24,
        "unit" : "mmol/L"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "70c62997-9f12-4b7c-9023-0cdf249336a7",
    "code" : {
      "text" : "CO2",
      "coding" : [ {
        "code" : "2028-9",
        "display" : "Carbon dioxide, total [Moles/volume] in Serum or Plasma",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  }, {
    "value" : {
      "Quantity" : {
        "value" : 10,
        "unit" : "md/dL"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "e3db8661-71b7-4cf3-a666-4ed9e127e134",
    "code" : {
      "text" : "BUN",
      "coding" : [ {
        "code" : "6299-2",
        "display" : "Urea nitrogen [Mass/volume] in Blood",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  }, {
    "value" : {
      "Quantity" : {
        "value" : 1.2,
        "unit" : "mg/dL"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "21261c16-b527-49f2-916b-2ad5dabf4381",
    "code" : {
      "text" : "Cr",
      "coding" : [ {
        "code" : "38483-4",
        "display" : "Creatinine [Mass/volume] in Blood",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  }, {
    "value" : {
      "Quantity" : {
        "value" : 185,
        "unit" : "mmol/L"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "7e68c2a8-a507-413b-aed4-87de5081b369",
    "code" : {
      "text" : "Glu",
      "coding" : [ {
        "code" : "2339-0",
        "display" : "Glucose [Mass/volume] in Blood",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  } ],
  "resourceType" : "Observation",
  "status" : "final",
  "id" : "cda1fc54-59fb-412d-86e0-acf6b78aa9a6",
  "code" : {
    "text" : "Basic Metabolic Panel",
    "coding" : [ {
      "code" : "51990-0",
      "display" : "Basic Metabolic Panel - Blood",
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
      "code" : "laboratory",
      "display" : "Laboratory",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "hasMember" : [ {
    "value" : {
      "Quantity" : {
        "value" : 0.01,
        "unit" : "ng/mL"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-02T09:08:00-05:00"
    },
    "id" : "01323ca7-4a72-4ae3-963d-af903df828d2",
    "code" : {
      "text" : "Troponin T",
      "coding" : [ {
        "code" : "6598-7",
        "display" : "Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  } ],
  "resourceType" : "Observation",
  "status" : "final",
  "id" : "950c948f-e5b2-4973-b7c0-e9ee08875e04",
  "code" : {
    "text" : "Troponin T",
    "coding" : [ {
      "code" : "6598-7",
      "display" : "Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma",
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
      "code" : "laboratory",
      "display" : "Laboratory",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "hasMember" : [ {
    "value" : {
      "Quantity" : {
        "value" : 0.01,
        "unit" : "ng/mL"
      }
    },
    "resourceType" : "Observation",
    "status" : "final",
    "effective" : {
      "dateTime" : "2012-10-03T10:08:00-05:00"
    },
    "id" : "bf9c0a26-4524-4395-b3ce-100450b9c9ac",
    "code" : {
      "text" : "Troponin T",
      "coding" : [ {
        "code" : "6598-7",
        "display" : "Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma",
        "system" : "http://loinc.org"
      } ]
    },
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    }
  } ],
  "resourceType" : "Observation",
  "status" : "final",
  "id" : "a4307cb2-b3b4-4f42-be03-1d9077376f4a",
  "code" : {
    "text" : "Troponin T",
    "coding" : [ {
      "code" : "6598-7",
      "display" : "Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma",
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
   <templateId root="2.16.840.1.113883.10.20.22.2.3.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.3.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="30954-2"/>
   <title>Results Section (entries optional) (V3)</title>
   <text>
      <list>
         <item>
            <caption>Basic Metabolic Panel, No performer ID, No Lab, No Lab Address</caption>
            <table border="1" width="100%">
               <thead>
                  <tr>
                     <th>Code</th>
                     <th>Value</th>
                     <th>Date</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <td>Na</td>
                     <td>140 mmol/L</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
                  <tr>
                     <td>K</td>
                     <td>4.2 mmol/L</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
                  <tr>
                     <td>Cl</td>
                     <td>98 mmol/L</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
                  <tr>
                     <td>CO2</td>
                     <td>24 mmol/L</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
                  <tr>
                     <td>BUN</td>
                     <td>10 md/dL</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
                  <tr>
                     <td>Cr</td>
                     <td>1.2 mg/dL</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
                  <tr>
                     <td>Glu</td>
                     <td>185 mmol/L</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
               </tbody>
            </table>
         </item>
         <item>
            <caption>Troponin T, No performer ID, No Lab, No Lab Address</caption>
            <table border="1" width="100%">
               <thead>
                  <tr>
                     <th>Code</th>
                     <th>Value</th>
                     <th>Date</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <td>Troponin T</td>
                     <td>0.01 ng/mL</td>
                     <td>10/02/2012 2:08PM UTC</td>
                  </tr>
               </tbody>
            </table>
         </item>
         <item>
            <caption>Troponin T, No performer ID, No Lab, No Lab Address</caption>
            <table border="1" width="100%">
               <thead>
                  <tr>
                     <th>Code</th>
                     <th>Value</th>
                     <th>Date</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <td>Troponin T</td>
                     <td>0.01 ng/mL</td>
                     <td>10/03/2012 3:08PM UTC</td>
                  </tr>
               </tbody>
            </table>
         </item>
      </list>
   </text>
   <entry>
      <organizer classCode="BATTERY" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.1" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.1"/>
         <id root="cda1fc54-59fb-412d-86e0-acf6b78aa9a6"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Basic Metabolic Panel - Blood"
                code="51990-0">
            <originalText>Basic Metabolic Panel</originalText>
         </code>
         <statusCode code="completed"/>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="d8fe6100-c31a-4b8c-b97d-57a6bd415bf7"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Sodium [Moles/​volume] in Blood"
                      code="2947-0">
                  <originalText>Na</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="140" unit="mmol/L" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="3ee6ac58-d22c-4416-9acc-c1c75c1c2c91"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Potassium [Moles/​volume] in Blood"
                      code="6298-4">
                  <originalText>K</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="4.2" unit="mmol/L" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="0431a3ec-8ad6-49da-9de1-0a8704b7609d"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Chloride [Moles/volume] in Blood"
                      code="2069-3">
                  <originalText>Cl</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="98" unit="mmol/L" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="70c62997-9f12-4b7c-9023-0cdf249336a7"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Carbon dioxide, total [Moles/volume] in Serum or Plasma"
                      code="2028-9">
                  <originalText>CO2</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="24" unit="mmol/L" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="e3db8661-71b7-4cf3-a666-4ed9e127e134"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Urea nitrogen [Mass/volume] in Blood"
                      code="6299-2">
                  <originalText>BUN</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="10" unit="md/dL" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="21261c16-b527-49f2-916b-2ad5dabf4381"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Creatinine [Mass/volume] in Blood"
                      code="38483-4">
                  <originalText>Cr</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="1.2" unit="mg/dL" xsi:type="PQ"/>
            </observation>
         </component>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="7e68c2a8-a507-413b-aed4-87de5081b369"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Glucose [Mass/volume] in Blood"
                      code="2339-0">
                  <originalText>Glu</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="185" unit="mmol/L" xsi:type="PQ"/>
            </observation>
         </component>
      </organizer>
   </entry>
   <entry>
      <organizer classCode="BATTERY" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.1" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.1"/>
         <id root="950c948f-e5b2-4973-b7c0-e9ee08875e04"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma"
                code="6598-7">
            <originalText>Troponin T</originalText>
         </code>
         <statusCode code="completed"/>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="01323ca7-4a72-4ae3-963d-af903df828d2"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma"
                      code="6598-7">
                  <originalText>Troponin T</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121002090800-0500"/>
               <value value="0.01" unit="ng/mL" xsi:type="PQ"/>
            </observation>
         </component>
      </organizer>
   </entry>
   <entry>
      <organizer classCode="BATTERY" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.1" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.1"/>
         <id root="a4307cb2-b3b4-4f42-be03-1d9077376f4a"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma"
                code="6598-7">
            <originalText>Troponin T</originalText>
         </code>
         <statusCode code="completed"/>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="bf9c0a26-4524-4395-b3ce-100450b9c9ac"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Troponin T.Cardiac [Mass/Volume] In Serum Or Plasma"
                      code="6598-7">
                  <originalText>Troponin T</originalText>
               </code>
               <statusCode code="completed"/>
               <effectiveTime value="20121003100800-0500"/>
               <value value="0.01" unit="ng/mL" xsi:type="PQ"/>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
```

## sample1

```json
[ {
  "id" : "8DFF4B72-E8FE-11E4-B48A-460231621F93",
  "hasMember" : [ {
    "code" : {
      "coding" : [ {
        "code" : "2028-9",
        "display" : "Carbon dioxide",
        "system" : "http://loinc.org"
      } ]
    },
    "id" : "503B5578-E8FF-11E4-B48A-460231621F93",
    "value" : {
      "Quantity" : {
        "value" : 27,
        "unit" : "mmol/L"
      }
    },
    "effective" : {
      "dateTime" : "2012-08-15T10:05:00-08:00"
    },
    "status" : "final",
    "subject" : {
      "id" : "patient",
      "resourceType" : "Patient"
    },
    "resourceType" : "Observation"
  } ],
  "code" : {
    "coding" : [ {
      "code" : "2028-9",
      "display" : "Carbon dioxide",
      "system" : "http://loinc.org"
    } ]
  },
  "status" : "final",
  "resourceType" : "Observation",
  "category" : [ {
    "coding" : [ {
      "code" : "laboratory",
      "display" : "Laboratory",
      "system" : "http://terminology.hl7.org/CodeSystem/observation-category"
    } ]
  } ],
  "subject" : {
    "id" : "patient",
    "resourceType" : "Patient"
  }
} ]
```

C-CDA Equivalent:
```xml
<section>
   <templateId root="2.16.840.1.113883.10.20.22.2.3.1" extension="2015-08-01"/>
   <templateId root="2.16.840.1.113883.10.20.22.2.3.1"/>
   <code codeSystem="2.16.840.1.113883.6.1"
          codeSystemName="http://loinc.org"
          displayName="display string"
          code="30954-2"/>
   <title>Results Section (entries optional) (V3)</title>
   <text>
      <list>
         <item>
            <caption>Carbon dioxide, No performer ID, No Lab, No Lab Address</caption>
            <table border="1" width="100%">
               <thead>
                  <tr>
                     <th>Code</th>
                     <th>Value</th>
                     <th>Date</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <td>Carbon dioxide</td>
                     <td>27 mmol/L</td>
                     <td>08/15/2012 6:05PM UTC</td>
                  </tr>
               </tbody>
            </table>
         </item>
      </list>
   </text>
   <entry>
      <organizer classCode="BATTERY" moodCode="EVN">
         <templateId root="2.16.840.1.113883.10.20.22.4.1" extension="2015-08-01"/>
         <templateId root="2.16.840.1.113883.10.20.22.4.1"/>
         <id root="8DFF4B72-E8FE-11E4-B48A-460231621F93"/>
         <code codeSystem="2.16.840.1.113883.6.1"
                codeSystemName="http://loinc.org"
                displayName="Carbon dioxide"
                code="2028-9"/>
         <statusCode code="completed"/>
         <component>
            <observation classCode="OBS" moodCode="EVN">
               <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
               <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
               <id root="503B5578-E8FF-11E4-B48A-460231621F93"/>
               <code codeSystem="2.16.840.1.113883.6.1"
                      codeSystemName="http://loinc.org"
                      displayName="Carbon dioxide"
                      code="2028-9"/>
               <statusCode code="completed"/>
               <effectiveTime value="20120815100500-0800"/>
               <value value="27" unit="mmol/L" xsi:type="PQ"/>
            </observation>
         </component>
      </organizer>
   </entry>
</section>
```