# Security

Support for Spring Security

## Configuration

| Property                                      | Default Value | Description                                                                                                                                            |
|-----------------------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| wutsi.platform.security.type                  | none          | Type of cache: `none` or `jwt`                                                                                                                         |
| wutsi.platform.security.public-endpoints      |               | List of endpoints that do not require neither authentication or authorization. For format of each endpoint looks like `GET /foo/bar` or `POST /foo/**` |
| wutsi.platform.security.api-key-provider.type | header        | Name of the provider of API Key. Values: `header`, `env`. Default=`header`                                                                             |
| wutsi.platform.security.api-key               |               | Value of the API Key. Required for when API key provider is `env`                                                                                      |
| wutsi.platform.security.token-provider.type   | header        | Name of the provider of authentication token. Values: `header`, `custom`                                                                               |

## Beans

| Name                            | Type                                  | Description                                                        |
|---------------------------------|---------------------------------------|--------------------------------------------------------------------|
| tokenProvider                   | TokenProvider                         | Returns the current authentication token                           |
| authorizationRequestInterceptor | FeignAuthorizationRequestInterceptor  | Interceptor that add `Authorization` headers to all feign requests |
| apiKeyProvider                  | ApiKeyProvider                        | Returns the current API key                                        |
| apiKeyRequestInterceptor        | FeignApiKeyRequestInterceptor         | Interceptor that add `X-Api-Key` headers to all feign requests     |
