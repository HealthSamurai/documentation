# FDB

### DA & DDI checks: supported systems

For drugs:

* `urn:app:aidbox:e-prescriptions:fdb:DispensableDrugID` - FDB dispensable drug id
* `urn:app:aidbox:e-prescriptions:fdb:DrugNameID` - FDB drug name id
* [`http://www.nlm.nih.gov/research/umls/rxnorm`](http://www.nlm.nih.gov/research/umls/rxnorm) - RxNorm

For allergies:

* [`http://snomed.info/sct`](http://snomed.info/sct) - SNOMED
* `urn:app:aidbox:e-prescriptions:fdb:AllergenGroupID` - FDB allergen group id
* `urn:app:aidbox:e-prescriptions:fdb:DrugNameID` - FDB drug name id
* `urn:app:aidbox:e-prescriptions:fdb:IngredientID` - FDB ingredient id

Example of request body for `/api/medications/interactions`:

```json
{
  "medications": [
    {
      "system": "http://www.nlm.nih.gov/research/umls/rxnorm",
      "code": "167"
    }
  ],
  "allergies": [
    {
      "system": "urn:app:aidbox:e-prescriptions:fdb:IngredientID",
      "code": "2432"
    }
  ]
}
```

### Rx and OTC: how to distinguish

There’s an endpoint for dispensable drugs search - `/api/medications/{drug-name-id}/dispensable`. It responds with a bundle of `MedicationKnowledge`s. To figure out, whether medication is OTC or Rx, look at `regulatory.schedule.schedule`. While `text` contains something human-readable, like `Prescription Required`, `No Prescription Required`, `Non-drug, Non-device` or other, `coding.code` is for machines.

But what those numbers mean? Since this endpoint is using FDB API under the cover, it’s using the same codes. From FDB docs:

| Code | Description                   | Indicates that…                                                                                                                                                                                                                                                               |
| ---- | ----------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1    | Prescription Required         | The product is a prescription drug or medical device as defined in the Food Drug and Cosmetic Act (FDCA), including bulk drug ingredients and dietary supplements approved as drugs.                                                                                          |
| 2    | No Prescription Required      | The product is a prescription drug or medical device as defined in the Food Drug and Cosmetic Act (FDCA), including bulk drug ingredients and dietary supplements approved as drugs.                                                                                          |
| 3    | Available in Multiple Classes | The associated Packaged Drugs (NDC records) contain varied values.                                                                                                                                                                                                            |
| 4    | Non-drug, Non-device          | The product is neither a drugs nor device as defined in the FDCA, such as dietary supplements (including prenatal and other vitamins), medical foods, herbal preparations, bulk excipients, compounding vehicles, flavorings or colorants, supplies and nutritional products. |
| 9    | No Value                      | There are no Packaged Drugs (NDC records) associated with the MEDID.                                                                                                                                                                                                          |

From this we can conclude that 1 = Rx, 0 or 4 = OTC, 3 or 9 = Unclear.

Example of bundle entry from response body:

```json
{
  "resourceType": "MedicationKnowledge",
  "code": ...,
  "doseForm": ...,
  "intendedRoute": [
    ...
  ],
  "drugCharacteristic": [
    ...
  ],
  "regulatory": [
    {
      "regulatoryAuthority": {
        "display": "FDCA"
      },
      "schedule": [
        {
          "schedule": {
            "text": "Prescription Required",
            "coding": [
              {
                "code": "1",
                "system": "urn:app:aidbox:e-prescriptions:fdb:FederalLegendCode"
              }
            ]
          }
        }
      ]
    }
  ]
}

```
