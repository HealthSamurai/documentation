# Web Server

Web Server settings

## Base URL<a href="#web.base-url" id="web.base-url"></a>

```yaml
BOX_WEB_BASE_URL: "<String>"
```

Base URL is the URL Aidbox is available at. It consists of scheme (HTTP, HTTPS), domain, port (optional) and URL path (optional). Trailing slash is not allowed.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.base-url</code></td></tr><tr><td>Type</td><td>String</td></tr><tr><td>Default value</td><td>(no default)</td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_BASE_URL</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_BASE_URL</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Web server port<a href="#web.port" id="web.port"></a>

```yaml
BOX_WEB_PORT: "8080"
```

Web server port that Aidbox listens on.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.port</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8080</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_PORT</code></td></tr><tr><td>Deprecated environment variables</td><td><code>AIDBOX_PORT</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Web thread count<a href="#web.thread" id="web.thread"></a>

```yaml
BOX_WEB_THREAD: "8"
```

The number of web server workers in Aidbox. The number of workers determines how many concurrent web requests Aidbox can handle.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.thread</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_THREAD</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## HTTP initial line max length<a href="#web.max-line" id="web.max-line"></a>

```yaml
BOX_WEB_MAX_LINE: "8192"
```

Length limit for HTTP initial line and per header length, 414 (Request-URI Too Long) will be returned if exceeding this limit.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.max-line</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>8192</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_MAX_LINE</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_WEB_MAX__LINE</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>

## Request max body size<a href="#web.max-body" id="web.max-body"></a>

```yaml
BOX_WEB_MAX_BODY: "20971520"
```

Maximum size of the request body in bytes.

<details><summary>Details</summary><table data-header-hidden="true"><thead><tr><th width="200"></th><th></th></tr></thead><tbody><tr><td>ID</td><td><code>web.max-body</code></td></tr><tr><td>Type</td><td>Int</td></tr><tr><td>Default value</td><td><code>20971520</code></td></tr><tr><td>Environment variable</td><td><code>BOX_WEB_MAX_BODY</code></td></tr><tr><td>Deprecated environment variables</td><td><code>BOX_WEB_MAX__BODY</code></td></tr><tr><td>Sensitive</td><td><code>false</code> — value will be visible in plaintext in Admin UI</td></tr><tr><td>Set via</td><td>Admin UI → Settings<br />Environment variables</td></tr><tr><td>Hot reload</td><td><code>false</code> — setting requires system restart</td></tr></tbody></table></details>
