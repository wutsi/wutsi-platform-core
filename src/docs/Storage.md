# Storage
- [StorageService](https://github.com/wutsi/wutsi-platform-core/blob/master/src/main/kotlin/com/wutsi/platform/core/storage/StorageService.kt) is an interface for storing/retrieving files.

## Spring Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.storage.type | local | Type of storage implementation: `local` or `aws` |

### Local Storage Configuration
These are the additional configurations when `wutsi.platform.storage.type=local`

| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.storage.local.directory | ${user.home}/wutsi/storage | Directory where files are stored |
| wutsi.platform.storage.local.servlet.path | /storage | URL path of the servlet that serves the file stored |


### AWS Configuration
These are the additional configurations when `wutsi.platform.storage.type=aws`

| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.storage.aws.region | us-east-1 | AWS Region |
| wutsi.platform.storage.aws.bucket | | Name of the S3 bucket where files are stored.**REQUIRED** if `wutsi.platform.storage.type=aws` |
