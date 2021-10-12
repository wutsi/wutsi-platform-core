# Security
Support for Spring Security

## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.security.type | none | Type of cache: `none` or `jwt` |
| wutsi.platform.security.public-endpoints | | List of endpoints that do not require neither authentication or authorization. For format of each endpoint looks like `GET /foo/bar` or `POST /foo/**` |
| wutsi.platform.security.api-key-provider | header | Name of the provider of API Key. Values: `header`, `env`. `header` |
| wutsi.platform.security.api-key |  | Value of the API Key. **REQUIRED** when `wutsi.platform.security.api-key-provider=env` |
| wutsi.platform.security.token-provider | header | Name of the provider of authentication token. Values: `header`, `custom` |

### API Key Provider
- When `wutsi.platform.security.api-key-provider=header`, the API Key is extracted from request header `X-Api-Key`
- When `wutsi.platform.security.api-key-provider=env`, the API key is extracted from a system configuration `wutsi.platform.security.api-key`

### Token Provider
- When `wutsi.platform.security.token-provider=header`, the authentication token is extracted from request header `Authorization`
- When `wutsi.platform.security.token-provider=cumstom` the application must create a bean configuration that implements [AbstractTokenConfiguration](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/security/spring/AbstractTokenConfiguration.kt) to expose the bean `TokenProvider`
