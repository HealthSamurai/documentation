---
description: Follow these steps to set up a Python project with Aidbox
---

# Use Aidbox with Python

## Prerequisites

- Run Aidbox locally by following the instructions in the [Run Aidbox locally](run-aidbox-locally.md) guide

## Steps

1. Generate SDK package

   ```bash
   npm install -g @fhirschema/codegen
   npx fscg generate -g typescript -p hl7.fhir.r4.core@4.0.1 -o aidbox --py-sdk-package aidbox --py-allow-extra-fields
   ```

2. Init virtual env and install dependencies:

   ```bash
   python3 -m venv venv
   source venv/bin/activate
   pip install -r aidbox/requirements.txt
   ```

3. Create a Client in a new **main.py** file:

   ```python
   from typing import Any, Dict
   
   import requests
   
   from aidbox.client import Auth, AuthCredentials, Client
   from aidbox.hl7_fhir_r4_core import (CodeableConcept, HumanName, Identifier,
                                        Observation, ObservationComponent,
                                        Patient, Quantity, Reference)
   
   client = Client(
       base_url="http://localhost:8080/fhir",
       auth=Auth(
           method="basic",
           credentials=AuthCredentials(
               username="root",
               password="<secret>", # get actual value from docker-compose.yaml: BOX_ROOT_CLIENT_SECRET
           ),
       ),
   )
   ```

3. Create Patient with id = "pt1" (PUT):

   ```python
   patient = Patient(
       id="pt1",
       identifier=[Identifier(system="http://org.io/id", value="0000-0000")],
       name=[HumanName(given=["John"], family="Doe")],
       gender="male",
       birth_date="1990-01-01",
   )
   
   try:
       result = client.update(patient)
       print(result.model_dump_json(exclude_unset=True, exclude_none=True))
   except requests.exceptions.RequestException as e:
       print("Error:", e)
       if e.response is not None:
           response_json: Dict[str, Any] = e.response.json()
           print(response_json)
   ```

4. Create Observation that references the Patient:

   ```python
   observation = Observation(
       id="obs1",
       code=CodeableConcept(coding=[{"system": "http://loinc.org", "code": "85354-9"}]),
       status="final",
       effective_date_time="2025-07-08",
       subject=Reference(reference="Patient/pt1"),
       component=[
           ObservationComponent(
               code=CodeableConcept(
                   coding=[{"system": "http://loinc.org", "code": "8480-6"}]
               ),
               value_quantity=Quantity(
                   value=120,
                   unit="mmHg",
                   system="http://unitsofmeasure.org",
                   code="mm[Hg]",
               ),
           ),
           ObservationComponent(
               code=CodeableConcept(
                   coding=[{"system": "http://loinc.org", "code": "8462-4"}]
               ),
               value_quantity=Quantity(
                   value=80, unit="mmHg", system="http://unitsofmeasure.org", code="mm[Hg]"
               ),
           ),
       ],
   )
   
   
   try:
       result = client.update(observation)
       print(result.model_dump_json(exclude_unset=True, exclude_none=True))
   except requests.exceptions.RequestException as e:
       print("Error:", e)
       if e.response is not None:
           response_json: Dict[str, Any] = e.response.json()
           print(response_json)
   ```

6. Run the project

   ```bash
   python3 main.py
   ```

## Next steps

* Learn more about [Aidbox SDKs generation](../developer-experience/developer-experience-overview.md#use-aidbox-sdks-for-customized-experience)
* Learn more about [Aidbox Access Control](../access-control/access-control.md)
