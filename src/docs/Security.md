# Security
Support for Spring Security

## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.security.type | none | Type of cache: `none` or `jwt` |

### JTW Configuration
These are the additional configurations when `wutsi.platform.security.type=jwt`

| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.security.api-key | | API-KEY of the application |
| wutsi.platform.security.secured-endpoints | | List of secured endpoints. Each endpoint has the format `<METHOD> <URL>`.|
