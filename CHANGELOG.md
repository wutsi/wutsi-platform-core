# Change Log

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
- Support for securty configuration

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

