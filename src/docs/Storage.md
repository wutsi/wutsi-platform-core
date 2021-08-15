# Storage
Utility provides an implementation of `StorageService` for storing files

## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.storage.type | local | Type of storage implementation: `local` or `aws` |
| wutsi.platform.storage.local.directory | ${user.home}/wutsi/storage | Directory where files are stored |
| wutsi.platform.storage.local.servlet.path | /storage | URL path of the servlet that serves the file stored | 
| wutsi.platform.storage.aws.region | us-east-1 | AWS Region |
| wutsi.platform.storage.aws.bucket | | REQUIRED - Name of the S3 bucket where files are stored |
