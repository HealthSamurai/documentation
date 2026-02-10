# (g)(9): Application access - all data request

Aidbox supports the **§170.315(g)(9)** "all data request" capability by providing an API that returns a **CCD (C-CDA R2.1) summary record** for a requested patient, optionally constrained to a **specific date** or **date range**.

The CCD is formatted in accordance with the **C-CDA version 2.1** specification and the **USCDI v3** data element list.

## Standards compliance

| Standard | Detail | Regulation |
|---|---|---|
| **C-CDA** | R2.1 (Consolidated CDA, 2015 Edition with Errata) | §170.205(a)(4) |
| **C-CDA Companion Guide** | Release 4.1 — US Realm | §170.205(a)(6) |
| **USCDI** | v3 (October 2022 Errata) | §170.213(b) |
| **CCD template OID** | `2.16.840.1.113883.10.20.22.1.2` | |

## Base URL

`https://[your-aidbox]`

## API endpoint

### `GET /ccda/make-doc`

Returns a **CCD (C-CDA XML)** for the requested patient.

Query parameters:

- `pid` (string, required): Patient identifier in Aidbox
- `docdef-id` (string, required): Aidbox document definition identifier. Use `continuity-of-care` for the predefined CCD definition.
- `start-date` (string `YYYY-MM-DD`, optional): start of requested time window
- `end-date` (string `YYYY-MM-DD`, optional): end of requested time window
- `filename` (string, optional): desired filename for the document

Example request:

```http
GET /ccda/make-doc?docdef-id=continuity-of-care&pid=amb1&start-date=2015-06-22&end-date=2015-06-25&filename=amb1.xml
Authorization: Bearer <access_token>
```

Response:

- **200 OK**: CCD as XML
- **Content-Type**: `application/xml`

Errors:

- **400** invalid parameters (e.g., invalid date format)
- **404** patient not found

## CCD sections included

The predefined `continuity-of-care` document definition produces a CCD with the following sections:

| Section | LOINC Code |
|---|---|
| Allergies and Intolerances | `48765-2` |
| Medications | `10160-0` |
| Problems | `11450-4` |
| Procedures | `47519-4` |
| Results | `30954-2` |
| Vital Signs | `8716-3` |
| Immunizations | `11369-6` |
| Encounters | `46240-8` |
| Functional Status | `47420-5` |
| Mental Status | `10190-7` |
| Social History | `29762-2` |
| Family History | `10157-6` |
| Medical Equipment | `46264-8` |
| Plan of Treatment | `18776-5` |
| Advance Directives | `42348-3` |
| Nutrition | `61144-2` |
| Payers | `48768-6` |

Sections with no data for the patient are rendered with "entries optional" or omitted per C-CDA R2.1 rules.

## Date and date-range requests

When `start-date` and/or `end-date` are provided, the API returns patient data **associated with the specified date/date range** and does not ignore the filter by returning the entire record.

Date parameters must be provided as `YYYY-MM-DD`.

## References

- [Aidbox C-CDA / FHIR Converter](../../modules/integration-toolkit/ccda-converter/)
- [Producing C-CDA Documents](../../modules/integration-toolkit/ccda-converter/producing-c-cda-documents.md)
- [Supported C-CDA Sections](../../modules/integration-toolkit/ccda-converter/sections/)
- [USCDI v3](https://isp.healthit.gov/united-states-core-data-interoperability-uscdi#uscdi-v3)
- [C-CDA R2.1 Specification](http://www.hl7.org/implement/standards/product_brief.cfm?product_id=492)
