# OTEL metrics exporter parameters

<table><thead><tr><th>Env variable</th><th width="133">Value type</th><th>Description</th></tr></thead><tbody><tr><td><code>BOX_OBSERVABILITY_OTEL_METRICS_URL</code></td><td><code>string</code></td><td>The metrics' consumer URL (OTEL collector receiver endpoint, Elastic EPM etc.)</td></tr><tr><td><code>BOX_OBSERVABILITY_OTEL_METRICS_REQUEST_HEADERS</code></td><td><code>string</code></td><td>The headers for OTEL metrics requests, formatted as <code>HeaderName:HeaderValue\nHeaderName:HeaderValue</code></td></tr><tr><td><code>BOX_OBSERVABILITY_OTEL_METRICS_INTERVAL</code></td><td><code>number</code></td><td>The time interval (in seconds) for sending OTEL metrics.<br>Default value - <code>5</code></td></tr></tbody></table>

