# Storage
- [StorageService](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/storage/StorageService.kt) is an interface for storing/retrieving files.
  - [LocalStorageService](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/storage/local/LocalStorageService.kt) implementation of `StorageService` for storing/retrieving on local file/system.
  - [S3StorageService](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/storage/aws/S3StorageService.kt) is an interface for storing/retrieving on AQS S3.

## Spring Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.storage.type | local | Type of storage implementation: `local` or `aws` |
| wutsi.platform.storage.local.directory | ${user.home}/wutsi/storage | Directory where files are stored |
| wutsi.platform.storage.local.servlet.path | /storage | URL path of the servlet that serves the file stored |
| wutsi.platform.storage.aws.region | us-east-1 | AWS Region |
| wutsi.platform.storage.aws.bucket | | Name of the S3 bucket where files are stored.**REQUIRED** if `wutsi.platform.storage.type=aws` |
