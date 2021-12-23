# Security

Support for Spring Security

## Configuration

| Property                                      | Default Value | Description                                                                                                                                            |
|-----------------------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| wutsi.platform.security.type                  | none          | Type of cache: `none` or `jwt`                                                                                                                         |
| wutsi.platform.security.public-endpoints      |               | List of endpoints that do not require neither authentication or authorization. For format of each endpoint looks like `GET /foo/bar` or `POST /foo/**` |

## Beans

| Name                             | Type                                 | Description                                                        |
|----------------------------------|--------------------------------------|--------------------------------------------------------------------|
| tokenProvider                    | TokenProvider                        | Returns the current authentication token                           |
| applicationTokenProvider         | TokenProvider                        | Returns the current application token                              |
| authorizationRequestInterceptor  | FeignAuthorizationRequestInterceptor | Interceptor that add `Authorization` headers to all feign requests |
| apiKeyProvider                   | ApiKeyProvider                       | Returns the current API key                                        |
| apiKeyRequestInterceptor         | FeignApiKeyRequestInterceptor        | Interceptor that add `X-Api-Key` headers to all feign requests     |
