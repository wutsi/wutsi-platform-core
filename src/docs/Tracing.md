# Tracing
Utilities that adds tracing headers to all HTTP request:
- `X-Trace-ID`: Identifier of all the HTTP requests associated by a user interaction. 
- `X-Device-ID`: Identifier of the device of the user who initiate the call. 
- `X-Client-ID`: Identifier of the client that is making the call


## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.tracing.client-id |  | REQUIRED - Value of the client ID associated with this application |
