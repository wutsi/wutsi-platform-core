# Security

Support for Spring Security

## Configuration

| Property                                 | Default Value | Description                                                                                                                                            |
|------------------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| wutsi.platform.security.type             | none          | Type of cache: `none` or `jwt`                                                                                                                         |
| wutsi.platform.security.public-endpoints |               | List of endpoints that do not require neither authentication or authorization. For format of each endpoint looks like `GET /foo/bar` or `POST /foo/**` |
| wutsi.platform.security.cors.enabled     | true          | If `true`, the service will enabled CORS request for all requests                                                                                      |
| wutsi.platform.security.api-key          |               | API-Key of the application                                                                                                                             |

## Beans

| Name                            | Type                                 | Description                                                        |
|---------------------------------|--------------------------------------|--------------------------------------------------------------------|
| tokenProvider                   | TokenProvider                        | Returns the current authentication token                           |
| applicationTokenProvider        | ApplicationTokenProvider             | Return the authentication token of the application.                |
| applicationTokenProvider        | ApplicationTokenProvider             | Returns the current application token                              |
| authorizationRequestInterceptor | FeignAuthorizationRequestInterceptor | Interceptor that add `Authorization` headers to all feign requests |
