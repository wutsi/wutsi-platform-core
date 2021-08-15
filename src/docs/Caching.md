# Cachning
This module provides the following Spring Cache implementations:
- `none`: No caching
- `local`: Cache running in the application memory space
- `memcached`: Distributed cached based on Memcached

## Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.cache.type | none | Type of cache: `none` or local` or `memcached` |
| wutsi.platform.stream.name | default | Name of the cache |

### Memcached Configuration
These are the additional configurations when `wutsi.platform.cache.type=memcached`

| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.stream.name | | **REQUIRED** - Name of the cache |
| wutsi.platform.cache.memcached.username | | **REQUIRED** - Memcached username |
| wutsi.platform.cache.memcached.password | | **REQUIRED** - Memcached password |
| wutsi.platform.cache.memcached.servers | | **REQUIRED** - List of server IPs |
| wutsi.platform.cache.memcached.ttl | 86400 | Cache Time-To-Live |
