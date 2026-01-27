---
description: Monitor Aidbox performance with OTEL metrics exporter parameters metrics and telemetry data.
---

# OTEL metrics exporter parameters

<table><thead><tr><th>Setting</th><th width="133">Value type</th><th>Description</th></tr></thead><tbody><tr><td><a href="../../../../reference/all-settings.md#observability.otel.metrics-url">observability.otel.metrics-url</a></td><td><code>string</code></td><td>The metrics' consumer URL (OTEL collector receiver endpoint, Elastic EPM etc.)</td></tr><tr><td><a href="../../../../reference/all-settings.md#observability.otel.metrics-request-headers">observability.otel.metrics-request-headers</a></td><td><code>string</code></td><td>The headers for OTEL metrics requests, formatted as <code>HeaderName:HeaderValue\nHeaderName:HeaderValue</code></td></tr><tr><td><a href="../../../../reference/all-settings.md#observability.otel.metrics-interval">observability.otel.metrics-interval</a></td><td><code>number</code></td><td>The time interval (in seconds) for sending OTEL metrics.<br>Default value - <code>5</code></td></tr></tbody></table>

