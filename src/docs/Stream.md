# Error
- [EventStream](https://github.com/wutsi/wutsi-core/blob/master/src/main/kotlin/com/wutsi/stream/EventStream.kt): for publishing events

## Spring Configuration
| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.stream.type | local | Type of implementation: `local` or `rabbitmq` |
| wutsi.platform.stream.name | | **REQUIRED** - Name of the stream |
| wutsi.platform.stream.subscriptions | | String array of the streams that pushes event to this stream |

### Local Stream Configuration
These are the additional configurations when `wutsi.platform.stream.type=local`

| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.stream.local.directory | ${user.home}/wutsi/stream | Directory where events are stored |

### RabbitMQ Configuration
These are the additional configurations when `wutsi.platform.stream.type=rabbitmq`

| Property | Default Value | Description |
|----------|---------------|-------------|
| wutsi.platform.stream.rabbitmq.url |  | **REQUIRED** - URL RabbitMQ server |
| wutsi.platform.stream.rabbitmq.thread-pool-size | 8 | RabbitMQ consumer threadpool size |
| wutsi.platform.stream.rabbitmq.thread-pool-size | 8 | RabbitMQ consumer threadpool size |
| wutsi.platform.stream.rabbitmq.queue-ttl-seconds | 86400 | Time to Leave of the messages in the queue |
| wutsi.platform.stream.rabbitmq.dlq.max-retries | 10 | Number of retries for consuming the Dead-Letter-Queue, after what the message is discarded |
