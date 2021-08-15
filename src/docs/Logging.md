# Logging
Utility for logging using key/value pair format

## Configuration
None

## Log Schema
Each log output contains has the following fields:
  - `http_endpoint`: The request URI (Ex. `http_endpoint=/v1/user`)
  - `http_method`: The request HTTP method (Ex. `http_method=GET`)
  - `http_status`: The HTTP status
  - `client_id`: The name of the client
  - `device_id`: The user device identifier
  - `request_id`: The request identifier
  - `success`: `true` if the request is successful
  - `latency_millis`: The duration of the request in milliseconds
  - `HttpResponseLength`: The response content length
