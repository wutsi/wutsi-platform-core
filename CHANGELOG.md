# Change Log

## [0.0.97] - 2022-02-08
### Changed
- Add ErrorDecoder that support Retry

## [0.0.95] - 2022-01-15
### Changed
- Add into the tracing context the `client-info`

## [0.0.92] - 2021-12-09
### Changed
- Add a flag to enable/disable CORS configuration. CORS is enabled by default

## [0.0.81] - 2021-11-13
### Fixed
- Subscription configuration

## [0.0.74] - 2021-11-13
### Updated
- Disable JWT validation

## [0.0.63] - 2021-11-07
### Updated
- Add CORS headers `Access-Control-Expose-Headers: *`

## [0.0.63] - 2021-10-12
### Updated
- Added in the JWT the claims for `name`, `email` and `phone_number`

## [0.0.62] - 2021-10-12
### Added
- Added provider for APIKey
- Configure the type of TokenProvider: header vs custom
- Update dependencies

## [0.0.54] - 2021-10-10
### Added
- Added classes for forwarding the header `X-Api-Key`
### Changed
- Log authorization and api-key headers

## [0.0.54] - 2021-10-10
### Changed
- Configure the primary TracingContext

## [0.0.43] - 2021-10-05
### Changed
- Enable caching configuration

## [0.0.44] - 2021-09-12
### Changed
- Add support for public endpoint for security

## [0.0.38] - 2021-09-10
### Changed
- Return `downstreamCode` on error

## [0.0.37] - 2021-08-28
### Changed
- Add WutsiPrincipal

## [0.0.35] - 2021-08-28
### Changed
- Add JWTBuilder

## [0.0.29] - 2021-08-28
### Changed
- Exclude OPTION endpoints from security configuration

## [0.0.27] - 2021-08-26
### Changed
- Exclude actuator endpoints from security configuration

## [0.0.26] - 2021-08-26
### Changed
- `JWTAuthentificationFilter` logs in anonymous user when no token available

## [0.0.22] - 2021-08-21
### Changed
- Make `secured-endpoint` configuration optional

## [0.0.20] - 2021-08-20
### Added
- Add `@EnableGlobalMethodSecurity` to `SecurityConfigurationJWT`
- Add `@EnableCaching` to cahe configuration classes

## [0.0.18] - 2021-08-20
### Added
- Support for security configuration

## [0.0.12] - 2021-08-16
### Added
- The class URN for creating and parsing URN strings
### Changed
- RestControllerErrorHandler logs stack trace

## [0.0.10] - 2021-08-16
### Changed
- Change the configuration for device-id-provider
- Update documentation

## [0.0.9] - 2021-08-16
### Changed
- Add all open feign dependencies

## [0.0.6] - 2021-08-16
### Changed
- Update dependencies

## [0.0.4] - 2021-08-15
### Added
- Add `caching` with memcached and local implementation

### Changed
- Set no-stream as default implementation
- Set no-cache a default implementation
- Set local storage a default implementation

## [0.0.1] - 2021-08-15
Initial version
### Added
- Initial version with `error`, `logging`, `storage`, `stream` and `tracing`

