# Pharmacies synchronization

Pharmacies synchronization is a process that ensures the local list of pharmacies (local directory) is up-to-date with the latest data from Surescripts. This process involves fetching, processing, and storing pharmacy information, including their identifiers, contacts, and other information.

### How pharmacies are stored

Pharmacies are stored as FHIR `Organization` resources in the Aidbox. The resource id is set to match the pharmacy's NCPDP id for easy lookup and reference. These resources are structured to include essential details such as identifiers, address, contact information, and metadata. The storage process ensures compliance with FHIR standards and supports multi-tenancy.

#### Data Structure

Each pharmacy is represented as an `Organization` resource with the following fields:

* **Identifiers**:
  * `NCPDPID`: A unique identifier for the pharmacy issued by the National Council for Prescription Drug Programs.
  * Other identifiers, such as `NPI` (National Provider Identifier), if available.
* **Name**: The name of the pharmacy.
* **Address**:
  * Physical address, including street, city, state, postal code, and country.
  * Geolocation data (latitude and longitude) is stored as extensions.
* **Contact Information**:
  * Phone numbers (e.g., work, home, pager).
  * Email addresses (e.g., DirectAddress for clinical messaging).
* **Metadata**:
  * Source of the data (e.g., "Surescripts").
  * Tags and extensions for additional context, such as service levels and specialties.
* **Status**: Active or inactive status, based on the pharmacy's operational state.
* **Parent Organization**:
  * If the pharmacy is part of a larger organization, a reference to the parent organization is included.

### Synchronization Process

Pharmacies are updated through two types of synchronization processes: _Full Update_ and _Nightly Update_ (only changes).

#### What are Surescripts Requirements?

Surescripts has specific requirements for synchronization:

* If NOT using daily Nightly downloads: must download the Full version daily
* If using daily Nightly downloads: must still download Full version weekly

#### Sync schedule

There is a default sync schedule configuration

For _nightly sync_:

Runs every weekday (Sunday through Friday) at 1:00 AM

```json
{
  "id": "nightly-pharmacies-sync",
  "schedule": "0 1 * * 0-5",
  "cancel-in-progress": false,
  "continue-on-failure": true,
  "max-attempts": 5,
  "delay-on-retry-ms": 1200000
}
```

**Behavior**:

* Will not cancel a currently running job
* Will continue on failure
* Will retry up to 5 times, with a 20-minute delay between attempts

For _full update_:

Runs every Saturday at 1:00 AM

```json
{
  "id": "full-pharmacies-sync",
  "schedule": "0 1 * * 6",
  "cancel-in-progress": false,
  "continue-on-failure": true,
  "max-attempts": 5,
  "delay-on-retry-ms": 3600000
}
```

**Behavior**:

* Will not cancel a currently running job
* Will continue on failure
* Will retry up to 5 times, with a 1-hour delay between attempts

#### Sync schedule Configuration

The synchronization schedule can be configured using a configuration file. The scheduler configuration is loaded from the path specified by the `SCHEDULER_CONFIGURATION_PATH` environment variable. This file should be in JSON format and define the schedule for various synchronization jobs.

**Example Configuration File**

```json
{
  "jobs": [
    {
      "id": "nightly-pharmacies-sync",
      "schedule": "0 2 * * 1-5",
      "cancel-in-progress": true,
      "max-attempts": 3
    }
  ]
}
```

**NOTE**: Default settings are preserved for missing fields\
If a certain configuration field is not provided, the default value will be used.

In this example, for _continue-on-failure_ and _delay-on-retry-ms_ settings the default value will be used.

#### Manual sync

In addition to the scheduled synchronization, a manual sync can be triggered via the API. This is useful for immediate updates or testing purposes. The manual sync can be triggered with the following API call:

```http
POST /e-prescription/directories/pharmacies/sync?nightly=false
```

#### Backup File

The backup file mechanism ensures that the previously downloaded organizations' data is preserved before overwriting it with new data. This provides a fallback in case of errors or data corruption during the synchronization process.

* All files are stored in the `/data` directory.
* The file names are based on the synchronization type:
  * `nightly-organizations.csv` for nightly updates.
  * `full-organizations.csv` for full updates.
* The backup file name is the same as the current file with a `.backup` suffix:
  * `nightly-organizations.csv.backup`
  * `full-organizations.csv.backup`
* You can restore pharmacies from backup file using a direct endpoint `/api/directories/pharmacies/restore` <sub>_(since 4.0)_</sub>

### Environment variables

* The `TENANT_ORGANIZATION_ID` environment variable specifies the organization under which the pharmacy data is stored. This ensures multi-tenancy support and proper segregation of data.
* The `SCHEDULER_CONFIGURATION_PATH`environment variable specifies the path to the scheduler configuration file. If not set, the default schedule is used.

### Example of Organization resource

```json
{
  "id": "123456789",
  "name": "Example pharmacy",
  "resourceType": "Organization",
  "active": true,
  "meta": {
    "tag": [
      {
        "system": "urn:app:aidbox:e-prescriptions:surescripts",
        "code": "v6_1_erx"
      },
      {
        "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
        "display": "New"
      },
      {
        "system": "urn:app:aidbox:e-prescriptions:surescripts:serviceLevel",
        "display": "Cancel"
      }
    ],
    "source": "surescripts"
  },
  "type": [
    {
      "coding": [
        {
          "code": "pharm",
          "system": "urn:app:aidbox:e-prescriptions:surescripts:organization"
        }
      ]
    }
  ],
  "identifier": [
    {
      "value": "123456789",
      "system": "urn:app:aidbox:e-prescriptions:ncpdp"
    },
    {
      "type": {
        "coding": [
          {
            "code": "NPI",
            "system": "http://terminology.hl7.org/CodeSystem/v2-0203"
          }
        ]
      },
      "value": "1234567890",
      "system": "http://hl7.org/fhir/sid/us-npi"
    }
  ],
  "address": [
    {
      "city": "Westlake",
      "line": ["One Cvs Drive", "Suite 123"],
      "type": "physical",
      "state": "OH",
      "country": "US",
      "extension": [
        {
          "url": "http://hl7.org/fhir/StructureDefinition/geolocation",
          "extension": [
            {
              "url": "latitude",
              "valueDecimal": 1.2345
            },
            {
              "url": "longitude",
              "valueDecimal": -1.2345
            }
          ]
        }
      ],
      "postalCode": "44145"
    }
  ],
  "extension": [
    {
      "url": "http://hl7.org/fhir/StructureDefinition/organization-period",
      "valuePeriod": {
        "end": "2099-12-31T00:00:00Z",
        "start": "2016-10-24T21:04:20Z"
      }
    }
  ],
  "telecom": [
    {
      "rank": 1,
      "value": "1234567890 ext. 123",
      "system": "phone"
    },
    {
      "value": "1234567890",
      "system": "fax"
    }
  ]
}
```
