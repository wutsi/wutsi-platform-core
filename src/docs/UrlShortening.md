# Security

Support for Spring Security

## Configuration

| Property                                        | Default Value | Description                             |
|-------------------------------------------------|---------------|-----------------------------------------|
| wutsi.platform.url-shortener.type               | none          | Type of URL shortener: `none` or `bitly` |

### Bitly Configuration

| Property                                        | Default Value | Description                   |
|-------------------------------------------------|---------------|-------------------------------|
| wutsi.platform.url-shortener.bitly.access-token |               | (REQUIRED) Bitly access token |

## Beans

| Name                            | Type                                 | Description                                                        |
|---------------------------------|--------------------------------------|--------------------------------------------------------------------|
| urlShortener                    | UrlShortener                         | Instance of the service that shorten the URLs                      |
