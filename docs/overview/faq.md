
# Aidbox Network Requirements FAQ

## What network access does Aidbox require to function properly?

To ensure full functionality, the Aidbox container must be able to make **outbound HTTPS (port 443)** connections to the following endpoints:

### Required Internet Addresses

| Purpose                      | Address                                                      | Port |
|-----------------------------|---------------------------------------------------------------|------|
| License verification        | `https://aidbox.app`                                          | 443  |
| Terminology server (TxBox)  | `https://tx.health-samurai.io/fhir`                           | 443  |
| IG Package fetch on startup | `https://storage.googleapis.com/fhir-schema-registry/`        | 443  |
| Usage statistics reporting  | `https://ph.aidbox.app`                                       | 443  |

If you're using a firewall or proxy, make sure **TCP connections to port 443** are allowed for the domains above. You may also want to explicitly allow access to:

- `104.155.179.23:443` – IP address of `https://aidbox.app` (used for license verification)

## What data is transmitted during license verification?

Only the **JWT license token** is sent to `https://aidbox.app` during the verification process. No other customer data is transmitted.

## Does Aidbox collect any telemetry?

Yes. Aidbox may send basic usage statistics to `https://ph.aidbox.app` to help improve the product. You can read more and configure this behavior in the documentation:  
[https://docs.aidbox.app/reference/settings/general#usage-stats](https://docs.aidbox.app/reference/settings/general#usage-stats)
