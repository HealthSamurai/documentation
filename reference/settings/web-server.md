## Web Server

Web Server settings

### Base URL<a href="#web.base-url" id="web.base-url"></a>

Base URL is the URL Aidbox is available at. It consists of scheme (HTTP, HTTPS), domain, port (optional) and URL path (optional). Trailing slash is not allowed.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.base-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variables</td><td><code>BOX_WEB_BASE_URL</code> , <br /><code>AIDBOX_BASE_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Web server port<a href="#web.port" id="web.port"></a>

Web server port that Aidbox listens on.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8080</code></td></tr><tr><td>Environment variables</td><td><code>BOX_WEB_PORT</code> , <br /><code>AIDBOX_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Web thread count<a href="#web.thread" id="web.thread"></a>

The number of web server workers in Aidbox. The number of workers determines how many concurrent web requests Aidbox can handle.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.thread</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variables</td><td><code>BOX_WEB_THREAD</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### HTTP initial line max length<a href="#web.max-line" id="web.max-line"></a>

Length limit for HTTP initial line and per header length, 414 (Request-URI Too Long) will be returned if exceeding this limit.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.max-line</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8192</code></td></tr><tr><td>Environment variables</td><td><code>BOX_WEB_MAX_LINE</code> , <br /><code>BOX_WEB_MAX__LINE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>

### Request max body size<a href="#web.max-body" id="web.max-body"></a>

Maximum size of the request body in bytes.

<table data-header-hidden="true"><thead><tr><th width="165"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.max-body</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>20971520</code></td></tr><tr><td>Environment variables</td><td><code>BOX_WEB_MAX_BODY</code> , <br /><code>BOX_WEB_MAX__BODY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — can be set via Ul and environment variable</td></tr><tr><td>Hot reload</td><td><code>false</code> — requires Aidbox restart</td></tr></tbody></table>
