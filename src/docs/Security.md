# Security
Support for Spring Security

## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.security.type | none | Type of cache: `none` or `jwt` |
| wutsi.platform.security.public-endpoints | | List of endpoints that do not require neither authentication or authorization. For format of each endpoint looks like `GET /foo/bar` or `POST /foo/**` |
