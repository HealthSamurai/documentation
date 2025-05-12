# Frequently Asked Questions

* **What is NCPDP ID?**\
  The **NCPDP ID** is a unique identifier assigned by the National Council for Prescription Drug Programs (NCPDP) to pharmacies and other entities involved in the electronic prescribing and pharmacy claims process in the United States.
*   **How to create a test NPI or Provider Locations?**

    Visit the Directory Manager section within Surescripts workbench.\
    From there, you'll have access to features for creating test NPI or Provider Locations, searching records, and downloading files.
* **How to check the status of a message?**\
  Visit the Messages section within Surescripts workbench.\
  From there, you can construct a query to select messages by some criteria, and further check the status of each message along with other message details.
*   **How to send inbound messages?**

    Visit the Message Tester section within Surescripts workbench.\
    From there, you can manage templates and compose messages. That's particularly useful for callback testing.
*   **What should I do if I receive an "Authentication Failed - No account matches certificate information" error?**

    This error can be confusing and typically occurs when there is a mismatch in the identifiers used in message header. Specifically, it means either the NCPDP ID or SPI (Surescripts Provider ID) being used is incorrect. Double-check these values and ensure they match the credentials provided to you.
*   **What values should I use for Provider ActiveStart and ActiveEnd dates?**

    For Provider `ActiveStart` and `ActiveEnd` dates, you should use a known period of time that is relevant to the provider's active status. For example, if the provider is currently active, you can use the current date and time as the start date and a known future date as the end date. If you do not know a future date, you can leave it blank or use a date far in the future, such as 12/31/2099 12:00:00 AM EST (which is a default in our module).

    Here is what Surescripts said about 'active end dates' for a Provider location:

    > When it comes to start times - that information is automatically set in the Surescripts directory when the SPI location has been generated. There is currently no documentation that elaborates further on this piece, new SPIs are visible in the nightly file download if they were made before 7:00PM ET of that day. If the SPI was made after 7:00PM ET then the SPI will appear in the full directory download file available the following business day.

    > As for active end dates we advise that the organization update the end date to whenever the provider may be terminating their services with the EHR. However if this is not known then typically when an SPI is generated in workbench or if an SPI record is switched over from one vendor to another Surescripts default date and time is set to 12/31/2099 12:00:00 AM EST which is the max date allowed in the directory. I should also note that the SPI records may also be updated in bulk via the bulk update request(BUR) tool in workbench. The BUR tool is handy when needing to update a lot of SPI’s at the same time.
*   **What to do with Pharmacy store number?**

    Surescripts has a requirement for Pharmacy store number:

    > IF supplied in the Business Name in the SureScripts Directory, `storeNumber` must be displayed and sent.

    The requirement specifies that when the `storeNumber` is included in the `BusinessName` in the SureScripts Directory, it must be shown and transmitted as part of that `BusinessName` field. As the `storeNumber` gets incorporated into the name field itself, and there is no dedicated `storeNumber` field in the Surescripts outbound message schema, no additional handling is needed.
