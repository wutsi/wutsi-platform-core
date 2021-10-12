# Security
Support for Spring Security

## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.security.type | none | Type of cache: `none` or `jwt` |
| wutsi.platform.security.public-endpoints | | List of endpoints that do not require neither authentication or authorization. For format of each endpoint looks like `GET /foo/bar` or `POST /foo/**` |
| wutsi.platform.security.api-key-provider | header | Name of the provider of API Key. The values are `header` or `env`. For `header`, the API Key is extracted from request header `X-Api-Key`, for `env` the API key is extracted from a system configuration |
| wutsi.platform.security.api-key |  | Value of the API Key. **REQUIRED** when `wutsi.platform.security.api-key-provider=env` |
