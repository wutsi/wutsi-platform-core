# Logging
- [KVLogger](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/logging/KVLogger.kt)
is an interface for outputing logs in the format key/value pair.
- [KVLoggerFilter](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/logging/servlet/KVLoggerFilter.kt)
is a Servlet filter that logs each HTTP calls. Each logs will always contains the following information:
  - `http_endpoint`: The request URI (Ex. `http_endpoint=/v1/user`)
  - `http_method`: The request HTTP method (Ex. `http_method=GET`)
  - `http_status`: The HTTP status
  - `client_id`: The name of the client
  - `device_id`: The user device identifier
  - `request_id`: The request identifier
  - `success`: `true`|`false1 if the request is successful or not
  - `latency_millis`: The duration of the request in milliseconds
