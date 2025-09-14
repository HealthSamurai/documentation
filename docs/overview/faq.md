# FAQ

## What network access does Aidbox require to function properly?

To ensure full functionality, the Aidbox container must be able to make **outbound HTTPS (port 443)** connections to the following endpoints:

### Required Internet addresses

| Purpose                     | Address                                                       | Port |
|-----------------------------|---------------------------------------------------------------|------|
| License portal              | `https://aidbox.app`, `https://*.aidbox.app`                  | 443  |
| Terminology server (TxBox)  | `https://tx.health-samurai.io/fhir`                           | 443  |
| IG Package fetch on startup | `https://storage.googleapis.com/fhir-schema-registry/`        | 443  |
| Aidbox documentation*       | `https://docs.aidbox.app/`                                    | 443  |

*Not required for Aidbox to function, but access is recommended for reference and development.

If you're using a firewall or proxy, make sure **TCP connections to port 443** are allowed for the domains above. You may also want to explicitly allow access to:

- `104.155.179.23:443` – IP address of `https://*.aidbox.app` and `https://*.health-samurai.io/fhir` 

## What data is transmitted during license verification?

Only the **JWT license token** is sent to `https://aidbox.app` during the verification process. No other customer data is transmitted.

## Does Aidbox collect any telemetry?

Yes. Aidbox may send basic usage statistics to `https://ph.aidbox.app` to help improve the product. You can [read more and configure this behavior here](../reference/settings/general.md#usage-stats).
