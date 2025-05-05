---
description: >-
  This guide explains how to find out what resources certain Bulk client had
  access to
---

# Find out what resources were exported during the $export operation

[$export](../../../api/bulk-api-1/usdexport.md) operation is used to export huge amounts of patient data directly to S3 buckets. Sometimes it is important to clarify what resources certain Bulk client had access to.

{% hint style="info" %}
In the guide we analyze the behavior of the `inferno-my-clinic-bulk-client` client
{% endhint %}

## When the client used $export

To clarify when our client exported the data:

1. Open the `kibana` UI
2. Enter the search request like `w_cid:inferno-my-clinic-bulk-client and w_m:get and w_url like /bulk-api/Group//$export`
   * `w_cid` is the id of out client
3. Define the dates range
4. Press the `Refresh` button

When the request list appears, collect if of the exported groups and timestamps.

1. Extract group id from the `w_url` property `/tenant/my-clinic/bulk-api/Group/test-group-1/$export` -> `test-group-1`
2. Extract timestamp from the `ts` property -> `Dec 5, 2022 @ 16:34:30.417`

## Get list of patients

To get list of affected by the $export patient records:

1. Open the `REST Console` in Aidbox
2. Perform the `GET /Group/test-group-1/_history` request for each group collected on the previous step
3. Search for the version being active during the export (`ts` property). The matching version has `lastUpdated` less than `ts`. If there are several versions, the closes to `ts` should be taken
4. List of patient are held in the `members` section

```yaml
 member:
 - entity:
     id: test-pt-1
     resourceType: Patient
```

## Get list of the resource were exported

### List of resource types client had access to

List of resources types are defined with the `scope` attribute.

1. Open the `REST Console` in Aidbox
2. Perform the `GET /Client/inferno-my-clinic-bulk-client/_history` request
3. Search for the version being active during the export (`ts` property). The matching version has `lastUpdated` less than `ts`. If there are several versions, the closes to `ts` should be taken
4. List of resources the client had access to is defined by the `scope` property
5. Convert scopes to the resource types\
   `system/Observation.read` -> `Observation` resource type

{% hint style="info" %}
`See how system/*.read` [expands](../background-information/considerations-for-testing-with-inferno-onc.md#how-aidbox-expands-wildcard-scope)
{% endhint %}

### List the id of resources

{% hint style="info" %}
This should be done for every patient and resource type
{% endhint %}

To build the list of patient compartment resources:

1. Open the `REST Console` in Aidbox
2. Clarify how to search for the patient's resources (see [patient compartment](https://www.hl7.org/fhir/compartmentdefinition-patient.html))
3. Get the list of the resources. For example,\
   `GET /fhir/Observation?subject=test-pt-1&_elements=id`
4. Collect all the ids related to the patient and a resource type.

The output could be a table like this.

| patient\_id | resource\_type | resource\_id |
| ----------- | -------------- | ------------ |
| `test-pt-1` | `Observation`  | `test-obs-1` |
| `test-pt-1` | `Observation`  | `test-obs-2` |
|             | `Location`     | `loc-1`      |
| `test-pt-1` | `Patient`      | `test-pt-1`  |

{% hint style="info" %}
`Location` here is the example of the resource not in the compartment so the `patient_id` is empty
{% endhint %}

## Get the exported resources details

{% hint style="info" %}
This should be done for every row on the table above
{% endhint %}

To get the exported resources you should repeat those steps changing the resource type

1. Open the `REST Console` in Aidbox
2. Perform the `GET /fhir/{resource_type}/{resource_id}/_history` request. For example, `GET /fhir/Observation/test-obs-1/_history`
3. Search for the version being active during the export (`ts` property). The matching version has `lastUpdated` less than `ts`. If there are several versions, the closes to `ts` should be taken
